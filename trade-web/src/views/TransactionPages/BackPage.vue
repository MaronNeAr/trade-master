<template>
<div>
    <v-table fixed-header height="500px">
        <thead>
            <tr>
                <th class="text-center">
                    证券代码
                </th>
                <th class="text-center">
                    证券简称
                </th>
                <th class="text-center">
                    交易类型
                </th>
                <th class="text-right">
                    交易价格
                </th>
                <th class="text-right">
                    当前价格
                </th>
                <th class="text-right">
                    交易量
                </th>
                <th class="text-center">
                    操作
                </th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="record, idx in recordList" :key="idx">
                <td class="text-center">{{ record.securityCode }}</td>
                <td class="text-center">{{ record.securityName }}</td>
                <td :class="record.transactionType" class="text-center">{{ record.transactionType == 'buy' ? '买入':'卖出' }}</td>
                <td class="text-right">{{ record.transactionPrice.toFixed(2) }}</td>
                <td class="text-right">{{ record.currentPrice.toFixed(2) }}</td>
                <td class="text-right">{{ record.transactionVolume }}</td>
                <td class="text-center">
                    <v-btn size="small" color="red" @click="confirm(record.taskId)"> 撤单</v-btn>
                </td>
            </tr>
        </tbody>
    </v-table>
    <trade-confirm v-if="confirmDialog" @confirm="handleConfirm"></trade-confirm>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</div>
</template>

  
<script setup>
import {
    HttpManager
} from '@/api'
import {
    onMounted,
    ref,
    defineEmits
} from 'vue'
import TradeConfirm from '@/components/TradeConfirm.vue'

const emits = defineEmits(['refresh'])

const confirmDialog = ref(false)
const cancelTaskId = ref()
const recordList = ref([])

const snackbar = ref(false)
const snackbarText = ref("")

const handleConfirm = async(state) => {
    confirmDialog.value = false
    if (!state) return
    const params = new URLSearchParams()
    params.append("tid", cancelTaskId.value)
    const result = await HttpManager.cancel(params).catch(error => {
        console.log(error)
        return
    })
    if (!result?.success)showMessage(result ?. message)
    else showMessage(result ?. message)
    emits("refresh")
    getRecordList()
}

const confirm = (taskId) => {
    confirmDialog.value = true
    cancelTaskId.value = taskId
}

const getRecordList = async () => {
    const params = new URLSearchParams()
    params.append("status", "active")
    const result = await HttpManager.details(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
    recordList.value = []
    result.data.forEach(async (item) => {
        const ret = await HttpManager.getMarketData(item.securityCode).catch(error => {
            console.log(error)
            return
        })
        if (!ret ?.success) {
            console.log(ret ?.message)
            return
        }
        item.securityName = ret.data.name
        item.currentPrice = ret.data.lastPrice
        recordList.value.push(item)
    });
    recordList.value.sort((o1, o2) => o2.transactionDate - o1.transactionDate)
}

const showMessage = (message) => {
    snackbar.value = true
    snackbarText.value = message
}

onMounted(() => {
    getRecordList()
})
</script>

  
<style scoped>
.filled {
    color: blue;
}

.cancelled,
.buy {
    color: red;
}

.active,
.sell {
    color: green;
}
</style>
