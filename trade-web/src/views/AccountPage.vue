<template>
<v-card class="account-card">
    <v-row>
        <v-col cols="3" class="avatar-box">
            <v-list-item title="Aurora_Near" width="200" subtitle="@marlon1475" :prepend-avatar="require('@/assets/images/icon.jpg')"></v-list-item>
        </v-col>
        <v-col cols="9">
            <v-row>
                <v-col cols="6">
                    <v-list-item :title="assets.brokerage_assets.toFixed(2)" subtitle="证券资产"></v-list-item>
                </v-col>
                <v-col cols="6">
                    <v-list-item :title="assets.total_market_value.toFixed(2)" subtitle="总市值"></v-list-item>
                </v-col>
                <v-col cols="6">
                    <v-list-item :title="assets.floating_profit_loss.toFixed(2)" subtitle="浮动盈亏"></v-list-item>
                </v-col>
                <v-col cols="6">
                    <v-list-item :title="assets.account_assets.toFixed(2)" subtitle="账户资产"></v-list-item>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <br />
    <h3>证券账户</h3>
    <v-divider></v-divider>
    <br />
    <v-row>
        <v-col cols="3">
            <v-autocomplete label="默认账户" variant="underlined" v-model="brokerage" :items="brokerageList"></v-autocomplete>
            <v-text-field label="金额" variant="underlined" type="numbder" :rules="[positiveNumberRule]" v-model="amount"></v-text-field>
            <v-row>
                <v-col cols="6" align="right">
                    <v-btn color="green" @click="transfer">充值</v-btn>
                </v-col>
                <v-col cols="6">
                  <v-btn color="red" @click="withdraw">提现</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="9" class="brokerage-account-list">
            <v-row>
                <v-col cols="4" v-for="brokerage, idx in brokerageAccount" :key="idx">
                    <v-list-item :title="brokerage.balance.toFixed(2)" :subtitle="brokerage.brokerageName"></v-list-item>
                </v-col>
            </v-row>
        </v-col>
    </v-row>
    <trade-verify v-if="verifyDialog" @verify="handleVerify"></trade-verify>
    <v-snackbar :timeout="2000" v-model="snackbar">
      {{ snackbarText }}
    </v-snackbar>
</v-card>
</template>

<script setup>
import {
    HttpManager
} from '@/api'
import {
    onMounted,
    ref
} from 'vue'
import TradeVerify from '@/components/TradeVerify.vue'

const assets = ref({
    brokerage_assets: 200000.00,
    total_market_value: 0.00,
    floating_profit_loss: 0.00,
    account_assets: 0.00
})

const brokerage = ref()
const brokerageList = ref([])
const brokerageAccount = ref([])
const amount = ref(0)
const positiveNumberRule = (value) => {
    return /^[+]?\d+(\.\d+)?$/.test(value) || '只允许输入正数';
}
const verifyDialog = ref(false)
const verifyType = ref()

const snackbar = ref(false)
const snackbarText = ref("")

const getAccountBalance = async () => {
    const result = await HttpManager.getAccountBalance().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) console.log(result ?.message);
    else if (result ?.data) assets.value.account_assets = result.data
}

const getDefaultBrokerageId = async() => {
    const result = await HttpManager.getDefaultBrokerageId().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) console.log(result ?.message);
    else if (result ?.data) brokerage.value = result.data
} 

const getBrokerageList = async() => {
    const result = await HttpManager.getBrokerages().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) console.log(result ?.message)
    else if (result ?.data) {
        result.data.forEach(item => {
            brokerageList.value.push({
                value: item.brokerageId,
                title: item.brokerageName
            })
        });
    }
}

// const getBrokerageAccount = async() => {
//     const result = await HttpManager.getBrokerageAccount().catch(error => {
//         console.log(error)
//         return
//     })
//     if (!result ?.success) console.log(result ?.message);
//     else if (result ?.data) brokerageAccount.value = result.data
// }

const getBrokerageAccount = async() => {
    const result = await HttpManager.getBrokerageAccount().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
    brokerageAccount.value = result.data
    assets.value.brokerage_assets = 0.00
    result.data.forEach(item => {
        assets.value.brokerage_assets += item.balance
    });
}

const transfer = () => {
    verifyType.value = 'transfer'
    verifyDialog.value = true
}

const withdraw = () => {
    verifyType.value = 'withdraw'
    verifyDialog.value = true
}

const handleTransfer = async() => { 
    const params = new URLSearchParams()
    params.append('bid', brokerage.value)
    params.append('amount', amount.value)
    const result = await HttpManager.transfer(params).catch(error => {
        console.log(error)
        showMessage("证券账户充值失败")
        return
    })
    if (!result ?.success) showMessage(result.message)
    else showMessage(result.message)
    getAccountBalance()
    getBrokerageAccount()
}

const handleWithdraw = async() => {
    const params = new URLSearchParams()
    params.append('bid', brokerage.value)
    params.append('amount', amount.value)
    const result = await HttpManager.withdraw(params).catch(error => {
        console.log(error)
        showMessage("证券账户体现失败")
        return
    })
    if (!result ?.success) showMessage(result.message)
    else showMessage(result.message)
    getAccountBalance()
    getBrokerageAccount()
}

const handleVerify = (state) => {
    verifyDialog.value = false
    if (!state) {
        showMessage("验证码错误")
        return
    }
    else if (verifyType.value == 'transfer') handleTransfer()
    else if (verifyType.value == 'withdraw') handleWithdraw()
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

const showMessage = (message) => {
    snackbar.value = true
    snackbarText.value = message
}

onMounted(() => {
    getAccountBalance()
    getDefaultBrokerageId()
    getBrokerageList()
    getBrokerageAccount()
    getPostionList()
})
</script>

<style scoped>
.account-card {
    padding: 20px;
}

.avatar-box {
    position: relative;
    text-align: center;
    top: 40px;
}

.avatar-box * {
    position: relative;
    transform: translateX(-50%);
    left: 50%;
}

.brokerage-select {
    height: 72px;
}

.height-16 {
    height: 16px;
}

.brokerage-account-list {
  height: 300px;
  overflow: scroll;
}
</style>
