<template>
<div>
    <v-row>
        <v-col cols="6">
            <k-canvas class="k-canvas" :securityCode="securityCode"></k-canvas>
        </v-col>
        <v-col cols="4">
            <v-select v-model="type" :items="typeList" label="股票类型" variant="underlined">
            </v-select>
            <v-autocomplete ref="keyword" @input="handleInput" v-model="securityCode" :items="securityList" label="股票代码/名称" variant="underlined">
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
    <br/>
    <v-divider></v-divider>
    <v-row v-if="securityData.code">
        <v-col cols="2">
            <v-list-item :title="securityData.code" subtitle="股票代码"></v-list-item>
        </v-col>
        <v-col cols="2">
            <v-list-item :title="securityData.name" subtitle="股票简称"></v-list-item>
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
import { HttpManager } from '@/api'
import KCanvas from '@/components/KCanvas.vue'
import {
    ref,
    watch,
    onMounted
} from 'vue'

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
        value: 'SH-A',
        title: '上证A股'
    },
    {
        value: 'SH-B',
        title: '上证B股'
    },
    {
        value: 'SH-ChiNext',
        title: '上证创业板'
    },
    {
        value: 'SZ-A',
        title: '深证A股'
    },
    {
        value: 'SZ-B',
        title: '深证B股'
    },
    {
        value: 'SZ-ChiNext',
        title: '深证创业板'
    },
])

const keyword = ref()
const securityCode = ref()
const securityList = ref([])
const securityData = ref({})

const buyDepth= ref([
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

const handleInput = async(event) => {
    // console.log(event.target.value)
    keyword.value = event.target.value
    search()
}

const search = async() => {
    let securityType = "STOCK", exchange = type.value.slice(0, 2)
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
    // securityCode.value = null;
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    })
    if (securityList.value.length > 0) securityCode.value = securityList.value[0].value
}

watch(type, async(newType) => {
    let securityType = "STOCK", exchange = newType.slice(0, 2)
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

watch(securityCode, (code) => {
    getSecurityData(code)
}
)

const getSecurityData = async(code) => {
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
}

const getDefaultMarketList = async() => {
    let params = new URLSearchParams()
    params.append("type", "STOCK")
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
    setInterval(() => {
        getSecurityData(securityCode.value)
    }, 3000);
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
