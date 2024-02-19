<template>
  <v-card>
    <v-row>
        <v-col cols="3">
            <v-list-item :title="assets.brokerage_assets.toFixed(2)" subtitle="证券资产"></v-list-item>
        </v-col>
        <v-col cols="3">
            <v-list-item :title="assets.total_market_value.toFixed(2)" subtitle="总市值"></v-list-item>
        </v-col>
        <v-col cols="3">
            <v-list-item :title="assets.floating_profit_loss.toFixed(2)" subtitle="浮动盈亏"></v-list-item>
        </v-col>
        <v-col cols="3">
            <v-list-item :title="assets.account_assets.toFixed(2)" subtitle="账户资产"></v-list-item>
        </v-col>
    </v-row>
    <v-tabs v-model="tab">
        <v-tab value="buy">买入</v-tab>
        <v-tab value="sell">卖出</v-tab>
        <v-tab value="back">撤单</v-tab>
        <v-tab value="position">持仓</v-tab>
        <v-tab value="record">查询</v-tab>
    </v-tabs>
    <v-card-text>
        <v-window v-model="tab" class="market-panel">
            <v-window-item value="buy">
                <buy-page v-if="tab == 'buy'" @refresh="refresh"></buy-page>
            </v-window-item>

            <v-window-item value="sell">
                <sell-page v-if="tab == 'sell'" @refresh="refresh"></sell-page>
            </v-window-item>

            <v-window-item value="back">
                <back-page v-if="tab == 'back'" @refresh="refresh"></back-page>
            </v-window-item>

            <v-window-item value="position">
                <position-page v-if="tab == 'position'" @refresh="refresh"></position-page>
            </v-window-item>

            <v-window-item value="record">
                <record-page v-if="tab == 'record'" @refresh="refresh"></record-page>
            </v-window-item>
        </v-window>
    </v-card-text>
  </v-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import BuyPage from '@/views/TransactionPages/BuyPage.vue'
import SellPage from '@/views/TransactionPages/SellPage.vue'
import BackPage from '@/views/TransactionPages/BackPage.vue'
import PositionPage from '@/views/TransactionPages/PositionPage.vue'
import RecordPage from '@/views/TransactionPages/RecordPage.vue'
import { HttpManager } from '@/api'

const assets = ref({
    brokerage_assets: 0.00,
    total_market_value: 0.00,
    floating_profit_loss: 0.00,
    account_assets: 0.00
})
const tab = ref()

const getAccountBalance = async () => {
    const result = await HttpManager.getAccountBalance().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) console.log(result ?.message);
    else if (result ?.data) assets.value.account_assets = result.data
}

const getBrokerageAccount = async() => {
    const result = await HttpManager.getBrokerageAccount().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
    assets.value.brokerage_assets = 0.00
    result.data.forEach(item => {
        assets.value.brokerage_assets += item.balance
    });
}

const getPostionList = async() => {
  const result = await HttpManager.position().catch(error => {
    console.log(error)
    return
  })
  if (!result ?. success) {
    console.log(result ?.message)
    return
  }
  assets.value.total_market_value = 0.00
  assets.value.floating_profit_loss = 0.00
  result.data.forEach(async (item) => {
        const ret = await HttpManager.getMarketData(item.securityCode).catch(error => {
            console.log(error)
            return
        })
        if (!ret ?.success) {
            console.log(ret ?.message)
            return
        }
        item.currentPrice = ret.data.lastPrice
        assets.value.total_market_value += item.positionQuantity * item.currentPrice
        assets.value.floating_profit_loss += item.positionQuantity * item.currentPrice - item.positionCost
    });
}

const refresh = () => {
    getAccountBalance()
    getBrokerageAccount()
    getPostionList()
}

onMounted(() => {
    getAccountBalance()
    getBrokerageAccount()
    getPostionList()
})
</script>

<style>

</style>