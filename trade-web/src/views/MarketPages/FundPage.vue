<template>
<div>
    <v-row>
        <v-col cols="6">
            <k-canvas class="k-canvas" :securityCode="securityCode"></k-canvas>
        </v-col>
        <v-col cols="4">
            <v-select v-model="type" :items="typeList" label="基金类型" variant="underlined">
            </v-select>
            <v-autocomplete ref="keyword" @input="handleInput" v-model="securityCode" :items="securityList" label="基金代码/名称" variant="underlined">
                <template v-slot:append-inner>
                </template>
            </v-autocomplete>
            <div class="market-depth">
                <div class="buy-depth" v-for="item, idx in sellDepth" :key="idx">
                    <div class="type">卖{{5 - idx}}</div>
                    <div class="price green">{{item[0]}}</div>
                    <div class="count">{{item[1]}}</div>
                </div>
                <nut-divider></nut-divider>
                <div class="sell-depth" v-for="item, idx in buyDepth" :key="idx">
                    <div class="type">买{{idx + 1}}</div>
                    <div class="price red">{{item[0]}}</div>
                    <div class="count">{{item[1]}}</div>
                </div>
            </div>
        </v-col>
        <v-col cols="2" v-if="securityData.code">
            <v-list lines="two">
                <v-list-item :title="securityData.lastPrice" subtitle="当前价格"></v-list-item>
                <v-list-item :title="securityData.previousClose" subtitle="昨日收盘价"></v-list-item>
                <v-list-item :title="securityData.open" subtitle="今日开盘价"></v-list-item>
                <v-list-item :title="securityData.high" subtitle="最高价"></v-list-item>
                <v-list-item :title="securityData.low" subtitle="最低价"></v-list-item>
                <v-list-item :title="securityData.volumeHands" subtitle="成交量（手）"></v-list-item>
            </v-list>
        </v-col>
    </v-row>
    <br />
    <v-divider></v-divider>
    <v-row v-if="securityData.code">
        <v-col cols="2">
            <v-list-item :title="securityData.code" subtitle="基金代码"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.name" subtitle="基金简称"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.change" subtitle="跌涨额"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.changePercent" subtitle="跌涨百分比"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.turnoverTenThousand" subtitle="成交额（万）"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.peRatio" subtitle="市盈率"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.amplitude" subtitle="振幅"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.circulationMarketValue" subtitle="流通市值"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.outsideDisc" subtitle="外盘成交量"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.insideDisc" subtitle="内盘成交量"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.time" subtitle="最近交易时间"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.turnoverRate" subtitle="换手率"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.totalMarketValue" subtitle="总市值"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.pbRatio" subtitle="市净率"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.limitUpPrice" subtitle="涨停价"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.limitDownPrice" subtitle="跌停价"></v-list-item>
        </v-col>
    </v-row>
</div>
</template>

    
<script setup>
import {
    HttpManager
} from '@/api'
import KCanvas from '@/components/KCanvas.vue'
import {
    ref,
    watch,
    onMounted
} from 'vue'

// const keyword = ref()
const type = ref('SH')
const typeList = ref([{
        value: 'SH',
        title: '上海证券交易所'
    },
    {
        value: 'SZ',
        title: '深圳证券交易所'
    },
    {
        value: 'SH-ETF',
        title: '上证EOF'
    },
    {
        value: 'SH-LOF',
        title: '上证LOF'
    },
    {
        value: 'SH-REIT',
        title: '上证REIT'
    },
    {
        value: 'SZ-ETF',
        title: '深证ETF'
    },
    {
        value: 'SZ-LOF',
        title: '深证LOF'
    }
])

const keyword = ref()
const securityCode = ref()
const securityList = ref([])
const securityData = ref({

})

const buyDepth = ref([
    ['--', '--'],
    ['--', '--'],
    ['--', '--'],
    ['--', '--'],
    ['--', '--']
])
const sellDepth = ref([
    ['--', '--'],
    ['--', '--'],
    ['--', '--'],
    ['--', '--'],
    ['--', '--']
])

const handleInput = async (event) => {
    // console.log(event.target.value)
    keyword.value = event.target.value
    search()
}

const search = async () => {
    let securityType = "FUND",
        exchange = type.value.slice(0, 2)
    if (type.value.length > 2) securityType = type.value.slice(3)
    let params = new URLSearchParams()
    params.append("type", securityType)
    params.append("exchange", exchange)
    params.append("keyword", keyword.value)
    const result = await HttpManager.getMarketList(params).catch(error => {
        console.log(error)
        return
    })
    if (!result?.success) {
        console.log(result?.message)
        return
    }
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    })
    if (securityList.value.length > 0) securityCode.value = securityList.value[0].value
}

watch(type, async (newType) => {
    let securityType = "FUND",
        exchange = newType.slice(0, 2)
    if (newType.length > 2) securityType = newType.slice(3)
    let params = new URLSearchParams()
    params.append("type", securityType)
    params.append("exchange", exchange)
    const result = await HttpManager.getMarketList(params).catch(error => {
        console.log(error)
        return
    })
    if (!result?.success) {
        console.log(result?.message)
        return
    }
    securityCode.value = null;
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    })
    if (securityList.value.length > 0) securityCode.value = securityList.value[0].value
})

watch(securityCode, async (code) => {
    if (code == undefined || code == null || code == "") return
    const result = await HttpManager.getMarketData(code).catch(error => {
        console.log(error)
        return
    })
    if (!result?.success) {
        console.log(result?.message)
        return
    }
    buyDepth.value.forEach((item, idx) => {
        item[0] = result.data['buy' + (idx + 1)]
        item[1] = result.data['buy' + (idx + 1) + 'Volume']
    })
    sellDepth.value.forEach((item, idx) => {
        item[0] = result.data['sell' + (idx + 1)]
        item[1] = result.data['sell' + (idx + 1) + 'Volume']
    })
    securityData.value = result.data
    // v_sh501005="1~精准医疗LOF~501005~0.835~0.843~0.837~2333~1022~1311~0.835~10~0.827~5~0.810~141~0.760~600~0.000~0~0.836~25~0.842~1~0.858~40~0.860~10~0.880~68~~20240116150615~-0.008~-0.95~0.838~0.826~0.835/2333/193935~2333~19~0.72~~~0.838~0.826~1.42~0.27~0.27~0.00~0.927~0.759~0.83~612~0.831~~~~~~19.3935~0.0000~0~ ~LOF~-7.12~-1.18~~~~1.196~0.826~-5.76~-8.74~-5.76~32365800~32365800~68.00~-23.32~32365800~-0.75~~-28.63~0.00~0.8413~CNY~0~___D__F__N~0.885~-1";
    // v_sh600000="1~浦发银行~600000~6.59~6.51~6.49~531865~315109~216756~6.59~2617~6.58~855~6.57~3291~6.56~1134~6.55~2166~6.60~14021~6.61~5219~6.62~3206~6.63~2754~6.64~2830~~20240116150900~0.08~1.23~6.60~6.49~6.59/531865/348808283~531865~34881~0.18~5.00~~6.60~6.49~1.69~1934.31~1934.31~0.32~7.16~5.86~1.75~-17967~6.56~5.18~3.78~~~0.80~34880.8283~0.0000~0~ ~GP-A~-0.45~-0.30~4.86~5.41~0.45~7.90~6.47~-0.15~0.15~-3.51~29352176848~29352176848~-47.17~-9.35~29352176848~~~-5.99~0.15~~CNY~0~___D__F__N~6.51~7354";
})

const getDefaultMarketList = async () => {
    let params = new URLSearchParams()
    params.append("type", "FUND")
    params.append("exchange", "SH")
    const result = await HttpManager.getMarketList(params).catch(error => {
        console.log(error)
        return
    })
    if (!result?.success) {
        console.log(result?.message)
        return
    }
    // console.log(result.data)
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    })
    if (securityList.value.length > 0) securityCode.value = securityList.value[0].value
}

onMounted(() => {
    getDefaultMarketList()
})
</script>

    
<style scoped>
.k-canvas {
    border-radius: 10px;
    background-color: #d8f5f2;
    height: 400px;
    border: 1px rgb(149, 197, 240);
    box-shadow: 3px 2px #cfcfcf;
    animation-fill-mode: none;
}

.market-depth {
    padding: 5px;
    box-shadow: 2px 2px #cfcfcf;
}

.type {
    width: 20%;
    text-align: center;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    display: inline-block;
}

.price {
    width: 40%;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
}

.count {
    width: 40%;
    display: inline-block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
}
</style>
