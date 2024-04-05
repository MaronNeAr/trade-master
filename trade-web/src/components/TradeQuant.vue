<template>
    <v-dialog v-model="dialog" width="700">
        <v-card title="量化回测分析">
            <v-card-text>
                <br />
                <v-row>
                    <v-col cols="9">
                        <v-table fixed-header height="400px">
                            <thead>
                                <tr>
                                    <th class="text-center">
                                        交易时间
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
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="record, idx in backtestResult.trades" :key="idx" :class="record[1]">
                                    <td class="text-center">{{ formatDateTime(record[0]) }}</td>
                                    <td :class="record[1]" class="text-center">{{ record[1] == 'buy' ? '买入': record[1] == 'sell' ? '卖出': '持仓' }}</td>
                                    <td class="text-right">{{ record[2] }}</td>
                                    <td class="text-right">{{ record[3] }}</td>
                                </tr>
                            </tbody>
                        </v-table>
                    </v-col>
                    <v-col cols="3">
                        <div class="half-br"></div>
                        <v-text-field label="总资产" variant="plain" v-model="backtestResult.total_asset"></v-text-field>
                        <v-text-field label="证券市值" variant="plain" v-model="backtestResult.market_value"></v-text-field>
                        <v-text-field label="账户余额" variant="plain" v-model="backtestResult.cash"></v-text-field>
                        <v-text-field label="佣金" variant="plain" v-model="backtestResult.commission"></v-text-field>
                        <v-text-field label="策略收益率" variant="plain" v-model="backtestResult.compare_data.strategy_yield"></v-text-field>
                        <v-text-field label="基准收益率" variant="plain" v-model="backtestResult.compare_data.default_yield"></v-text-field>              
                    </v-col>
                </v-row>
                <v-card-actions>
                    <v-btn color="primary" block  @click="exit">EXIT</v-btn>
                </v-card-actions>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, defineEmits, defineProps, watch, onMounted } from 'vue'

const emits = defineEmits(['toggleQuant'])
const props = defineProps(['backtestResult'])
const dialog = ref(true)
const backtestResult = ref({
            trades: [],
            total_asset: 0,
            cash: 0,
            market_value: 0,
            commission: 0,
            compare_data: {
                strategy_yield: '0%',
                default_yield: '0%'
            }
        });

const exit = () => {
    closeQuantDialog()
}

const closeQuantDialog = () => {
    emits('toggleQuant', false)
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
    backtestResult.value = props.backtestResult
})

watch(dialog, () => {
    closeQuantDialog()
})
</script>

<style scoped>
.buy {
    color: red;
}

.sell {
    color: green
}
</style>