<template>
<div>
    <v-row>
        <v-col cols="4">
            <v-select v-model="securityType" :items="securityTypeList" label="类型" variant="underlined">
            </v-select>
            <v-select v-model="exchange" :items="exchangeList" label="交易所" variant="underlined">
            </v-select>
            <v-autocomplete ref="keyword" @input="handleInput" v-model="securityCode" :items="securityList" label="代码/名称" variant="underlined">
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
        <v-col cols="8">
            <div :class="transactionRecordClass">
                <transaction-record :refreshState="recordState"></transaction-record>
            </div>
            <br />
            <v-row v-if="securityData.code">
                <v-col cols="6">
                    <v-row>
                        <v-col cols="6">
                            <v-list-item :title="securityData.lastPrice" subtitle="当前价格"></v-list-item>
                        </v-col>
                        <v-col cols="6">
                            <v-list-item :title="securityData.volumeHands" subtitle="成交量（手）"></v-list-item>
                        </v-col>
                        <v-col cols="6">
                            <v-list-item :title="securityData.previousClose" subtitle="昨日收盘价"></v-list-item>
                        </v-col>
                        <v-col cols="6">
                            <v-list-item :title="securityData.open" subtitle="今日开盘价"></v-list-item>
                        </v-col>
                        <v-col>
                            <v-list-item :title="securityData.low" subtitle="最低价"></v-list-item>
                        </v-col>
                        <v-col cols="6">
                            <v-list-item :title="securityData.high" subtitle="最高价"></v-list-item>
                        </v-col>
                    </v-row>
                </v-col>
                <v-col cols="6">
                    <v-slider v-model="price" class="align-center" :max="securityData.high" :min="securityData.low" step="0.01" hide-details>
                        <template v-slot:append>
                            <v-text-field v-model="price" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                        </template>
                        <template v-slot:prepend>
                            卖出价格
                        </template>
                    </v-slider>
                    <br />
                    <v-slider v-model="volume" class="align-center" :max="maxVolume" min="0" step="100" hide-details>
                        <template v-slot:append>
                            <v-text-field v-model="volume" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                        </template>
                        <template v-slot:prepend>
                            &nbsp;&nbsp;&nbsp;&nbsp;卖出量
                        </template>
                    </v-slider>
                    <br />
                    <v-btn-toggle v-model="position" rounded color="red" group>
                        <v-btn value="self">自选</v-btn>
                        <v-btn value="quarter">1/4仓</v-btn>
                        <v-btn value="half">半仓</v-btn>
                        <v-btn value="three-quarter">3/4仓</v-btn>
                        <v-btn value="full">全仓</v-btn>
                    </v-btn-toggle>
                    <br />
                    <br />
                    <div class="button-group">
                        <v-btn prepend-icon="mdi-reload" align="right">稍后再买</v-btn>&nbsp;&nbsp;&nbsp;&nbsp;
                        <v-btn prepend-icon="mdi-check" align="right" color="red" @click="sell">卖出</v-btn>
                    </div>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <trade-verify v-if="verifyDialog" @verify="handleSell"></trade-verify>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</div>
</template>

  
<script setup>
import TransactionRecord from '@/views/TransactionPages/TransactionRecord.vue'
import TradeVerify from '@/components/TradeVerify.vue'
import {
    HttpManager
} from '@/api'
import {
    ref,
    watch,
    onMounted,
    defineEmits
} from 'vue'

const emits = defineEmits(['refresh'])

const securityType = ref('STOCK')
const securityTypeList = ref([{
        value: 'STOCK',
        title: '股票'
    },
    {
        value: 'FUND',
        title: '基金'
    },
    {
        value: 'BOND',
        title: '债券'
    },
])
const exchange = ref('SH')
const exchangeList = ref([{
        value: 'SH',
        title: '上海证券交易所'
    },
    {
        value: 'SZ',
        title: '深圳证券交易所'
    }
])

const keyword = ref()
const securityCode = ref()
const securityList = ref([])
const securityData = ref({
    code: null
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

const price = ref(0)
const volume = ref(100)
const maxVolume = ref(10000)
const position = ref('self')
const transactionRecordClass = ref('transaction-record-default')

const handleInput = async (event) => {
    // console.log(event.target.value)
    keyword.value = event.target.value
    search()
}

const search = async () => {
    let params = new URLSearchParams()
    params.append("type", securityType.value)
    params.append("exchange", exchange.value)
    params.append("keyword", keyword.value)
    const result = await HttpManager.getMarketList(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
    // securityCode.value = null;
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    });
}

const verifyDialog = ref(false)
const recordState = ref(false)
const snackbar = ref(false)
const snackbarText = ref("")

watch(position, () => {
    if (position.value == "quarter") volume.value = Math.floor(maxVolume.value / 4 / 100) * 100
    else if (position.value == "half") volume.value = Math.floor(maxVolume.value / 2 / 100) * 100
    else if (position.value == "three-quarter") volume.value = Math.floor(maxVolume.value / 4 * 3 / 100) * 100
    else if (position.value == "full") volume.value = Math.floor(maxVolume.value / 100) * 100
})

watch(securityType, () => {
    keyword.value = ""
    search()
})

watch(exchange, () => {
    keyword.value = ""
    search()
})

watch(securityCode, async (code) => {
    if (code == undefined || code == null || code == "") return
    const result = await HttpManager.getMarketData(code).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }

    maxVolume.value = 10000

    buyDepth.value.forEach((item, idx) => {
        item[0] = result.data['buy' + (idx + 1)]
        item[1] = result.data['buy' + (idx + 1) + 'Volume']
    })
    sellDepth.value.forEach((item, idx) => {
        item[0] = result.data['sell' + (5 - idx)]
        item[1] = result.data['sell' + (5 - idx) + 'Volume']
    })
    securityData.value = result.data
    price.value = result.data.sell1
    for (let i = 1; i <= 5; i++) maxVolume.value = Math.min(maxVolume.value, result.data['buy' + i + 'Volume'] * 100)
    transactionRecordClass.value = 'transaction-record'
    // console.log(maxVolumn.value)
})

const getDefaultMarketList = async () => {
    let params = new URLSearchParams()
    params.append("type", securityType.value)
    params.append("exchange", "SH")
    const result = await HttpManager.getMarketList(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
    // console.log(result.data)
    securityList.value = []
    result.data.forEach(item => {
        securityList.value.push({
            title: '【' + item.securityCode + '】' + item.securityName,
            value: item.securityCode
        })
    });
}

const sell = () => {
    verifyDialog.value = true
}

const handleSell = async (state) => {
    verifyDialog.value = false
    if (!state) {
        showMessage("验证码错误")
        return
    }
    const params = new URLSearchParams()
    params.append("code", securityCode.value)
    params.append("price", price.value)
    params.append("volume", volume.value)
    const result = await HttpManager.sell(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) showMessage(result ?.message)
    else showMessage(result ?.message)
    emits("refresh")
    recordState.value = !recordState.value
}

const showMessage = (message) => {
    snackbar.value = true
    snackbarText.value = message
}

onMounted(() => {
    getDefaultMarketList()
})
</script>

  
<style scoped>
.market-depth {
    padding: 5px;
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

.transaction-record-default {
    height: 460px;
    overflow-y: scroll;
    border-radius: 10px;
    border: 1px solid rgb(170, 170, 170);
}

.transaction-record {
    height: 250px;
    overflow-y: scroll;
    border-radius: 10px;
    border: 1px solid rgb(170, 170, 170);
}

.v-btn-group--density-default.v-btn-group {
    height: 32px;
}

.button-group {
    position: relative;
    text-align: right;
    right: 44px;
}

.button-group .v-btn {
    height: 32px;
}
</style>
