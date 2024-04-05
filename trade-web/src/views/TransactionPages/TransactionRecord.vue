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
                <td class="text-center">{{ record.transactionVolume }}</td>
                <td :class="record.transactionStatus" class="text-center">{{ getRecordStatus(record.transactionStatus) }}</td>
                <td class="text-center">{{ formatDateTime(record.transactionTime) }}</td>
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
    ref,
    defineProps,
    watch
} from 'vue'

const props = defineProps(['refreshState'])
const recordList = ref([])

watch(() => props.refreshState, () => {
    getRecordList()
})

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

    recordList.value = result.data

    recordList.value.forEach(async (item) => {
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
    });
}

const formatDateTime = (date_str) => {
    // 如果未提供日期对象，则默认使用当前时间
    const date = new Date(date_str)

    // 获取年、月、日、时、分、秒
    var year = date.getFullYear()
    var month = ('0' + (date.getMonth() + 1)).slice(-2) // 月份从0开始，因此需要加1
    var day = ('0' + date.getDate()).slice(-2)
    var hours = ('0' + date.getHours()).slice(-2)
    var minutes = ('0' + date.getMinutes()).slice(-2)
    var seconds = ('0' + date.getSeconds()).slice(-2)

    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds
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
