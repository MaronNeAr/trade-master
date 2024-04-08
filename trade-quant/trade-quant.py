from flask import Flask, request, jsonify
from flask_cors import CORS
import pymysql
import pandas as pd

# 创建 Flask 应用
app = Flask(__name__)
CORS(app, resources={r"/quant/*": {"origins": "*"}})

con = pymysql.connect(host="127.0.0.1",user="root",password="12345678",db="trade_transaction", init_command="SET time_zone = '+08:00'")

def query_market(code, start, end):
    return "SELECT * FROM transaction_market WHERE code = '" + code + "'AND create_time BETWEEN '" + start + "' AND '" + end + "';"

def query_fisrt_close(code, date):
    return "SELECT last_price FROM transaction_market WHERE code = '" + code + "'AND create_time >= '" + date + "' LIMIT 1;"

def query_last_close(code, date):
    return "SELECT last_price FROM transaction_market WHERE code = '" + code + "'AND create_time <= '" + date + "' ORDER BY create_time DESC LIMIT 1;"

# 回测
def backtest(df, signals, volumes = [], commission_rate = 0.0005, init_cash = 100000000):
    """回测策略"""
    # 初始化持仓
    position = 0
    # 初始化资金
    cash = init_cash
    # 初始化交易记录
    trades = []
    # 初始化价格
    price = df['last_price'].iloc[0]
    # 初始化佣金
    commission = 0

    if volumes == []: volumes = [100 for i in range(len(df))]

    # 遍历信号
    for i in range(len(signals)):
        price = df['last_price'].iloc[i]
        time = df['create_time'].iloc[i]
        volume = (volumes[i] + 50) // 100 * 100;

        # 加仓
        if signals[i] == 'buy' and cash >= price * volume:
            cash -= price * volume
            commission += price * volume * commission_rate            
            position += volume
            trades.append((time, 'buy', price, volume))
        
        # 减持
        elif signals[i] == 'sell' and position >= volume:
            position -= volume
            cash += price * volume
            commission += price * volume * commission_rate
            trades.append((time, 'sell', price, volume))
            
        # 平仓
        elif signals[i] == 'sell':
            cash += price * position
            commission += price * position * commission_rate
            trades.append((time, 'sell', price, volume))
            position = 0
        # 持仓
        else: trades.append((time, 'hold', '-', '-'))
    
    print("cash:", cash)
    print("market_value:", position * price)
    print("commission:", commission) 
    print("total_asset:", cash + position * price - commission) 
    return cash, position * price, commission, trades

# 实时行情对比
def compare(init_assets, last_assets, code, start, end):
    start_df = pd.read_sql(query_fisrt_close(code, start), con)['last_price']
    end_df = pd.read_sql(query_last_close(code, end), con)['last_price']
    
    if len(start_df) == 0 or len(end_df) == 0:
        return {}
    
    start_price = start_df.iloc[0]
    end_price = end_df.iloc[0]
    profit = last_assets - init_assets;

    return {
        'strategy_yield': '{:.1%}'.format(profit / init_assets),
        'default_yield': '{:.1%}'.format((end_price - start_price) / start_price)
    }


# 定义路由和处理函数
@app.route('/quant/rolling-window', methods = ['POST'])
def rolling_window():
    req = request.json
    print(req)

    if 'code' not in req or 'start' not in req or 'end' not in req:
        return jsonify({'success': False, 'message': '请至少键入code、start、end参数'})
    
    window = req['window'] if 'window' in req else 100
    offset = req['offset'] if 'offset' in req else 0
    default_volume = req['default_volume'] if 'default_volume' in req else 1000
    high_ratio = req['high_ratio'] if 'high_ratio' in req else 0.001
    low_ratio = req['low_ratio'] if 'low_ratio' in req else 0.001
    commission_rate = req['commission_rate'] if 'commission_rate' in req else 0.0005
    init_cash = req['init_cash'] if 'init_cash' in req else 100000000

    code, start, end = req['code'], req['start'], req['end']
    df = pd.read_sql(query_market(code, start, end), con)
    rolling_mean = df['last_price'].rolling(window).mean()
    signals, volumes = [], []

    for i in range(len(df)):
        price = df['last_price'].iloc[i]
        if i < window + offset:
            signals.append('hold')
            volumes.append(0)
        elif price * (1 + high_ratio) < rolling_mean[i - offset]:
            signals.append('buy')
            volumes.append(default_volume)    
        elif price * (1 - low_ratio) > rolling_mean[i - offset]:
            signals.append('sell')
            volumes.append(default_volume)
        else:
            signals.append('hold')
            volumes.append(0)
        
    cash, market_value, commission, trades = backtest(df, signals, volumes, commission_rate, init_cash)
    total_assets = cash + market_value - commission
    compare_data = compare(init_cash, total_assets, code, start, end)

    return jsonify({
        'success': True,
        'message':'获取移动均值-量化交易结果成功',
        'data': {
            'trades': trades[window + offset: window + offset + 100],
            'total_asset': '{:.2f}'.format(total_assets),
            'cash': '{:.2f}'.format(cash),
            'market_value': '{:.2f}'.format(market_value),
            'commission': '{:.2f}'.format(commission),
            'compare_data': compare_data
        }
    })

@app.route('/quant/in-outside-disc', methods = ['POST'])
def in_outside_disc():
    req = request.json

    if 'code' not in req or 'start' not in req or 'end' not in req:
        return jsonify({'success': False, 'message': '请至少键入code、start、end参数'})
    
    default_mult = req['default_mult'] if 'default_mult' in req else 1
    commission_rate = req['commission_rate'] if 'commission_rate' in req else 0.0005
    init_cash = req['init_cash'] if 'init_cash' in req else 100000000

    code, start, end = req['code'], req['start'], req['end']
    df = pd.read_sql(query_market(code, start, end), con)
    signals, volumes = [], []

    in_diff, out_diff = df['inside_disc'].diff(), df['outside_disc'].diff()
    diff = (in_diff - out_diff).diff()

    for i in range(len(df)):
        if i < 2 or diff.iloc[i] < -5000:
            signals.append('hold')
            volumes.append(0)
        elif diff.iloc[i] >= 0:
            signals.append('buy')
            volumes.append(diff.iloc[i] * default_mult)
        else:
            signals.append('sell')
            volumes.append(- diff.iloc[i] * default_mult)
            
    cash, market_value, commission, trades = backtest(df, signals, volumes, commission_rate, init_cash)
    total_assets = cash + market_value - commission
    compare_data = compare(init_cash, total_assets, code, start, end)

    return jsonify({
        'success': True,
        'message':'获取内外盘-量化交易结果成功',
        'data': {
            'trades': trades[:100],
            'total_asset': '{:.2f}'.format(total_assets),
            'cash': '{:.2f}'.format(cash),
            'market_value': '{:.2f}'.format(market_value),
            'commission': '{:.2f}'.format(commission),
            'compare_data': compare_data
        }
    })

@app.route('/quant/rolling-mean', methods = ['POST'])
def rolling_data():
    req = request.json

    if 'code' not in req or 'start' not in req or 'end' not in req:
        return jsonify({'success': False, 'message': '请至少键入code、start、end参数'})
    
    window = req['window'] if 'window' in req else 100

    code, start, end = req['code'], req['start'], req['end']
    df = pd.read_sql(query_market(code, start, end), con)
    rolling_mean = df['last_price'].rolling(window).mean()
    return jsonify({
        'success': True,
        'message':'获取移动均值成功',
        'data': rolling_mean
    })

# 运行应用
if __name__ == '__main__':
    app.run(debug=True)