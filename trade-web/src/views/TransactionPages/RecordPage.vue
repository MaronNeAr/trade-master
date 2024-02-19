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
                    交易状态
                </th>
                <th class="text-center">
                    交易时间
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
                <td class="text-center">{{ record.transactionVolume }}</td>
                <td :class="record.transactionStatus" class="text-center">{{ getRecordStatus(record.transactionStatus) }}</td>
                <td class="text-center">{{ record.transactionDate }}</td>
            </tr>
        </tbody>
    </v-table>
</div>
</template>

  
<script setup>
import {
    HttpManager
} from '@/api'
import {
    onMounted,
    ref
} from 'vue'

const recordList = ref([])

const getRecordStatus = (status) => {
    if (status == 'active') return '发起交易'
    else if (status == 'filled') return '交易完成'
    else if (status == 'cancelled') return '已撤单'
    else return '--'
}

const getRecordList = async () => {
    const result = await HttpManager.details().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        return
    }
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
}

onMounted(() => {
    getRecordList()
})
</script>

  
<style scoped>
.filled {
    color: blue;
}

.cancelled, .buy {
    color: red;
}

.active, .sell {
    color: green;
}
</style>
