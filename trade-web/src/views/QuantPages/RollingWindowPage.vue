<template>
<div>
    <v-row>
        <v-col cols="4">
            <v-autocomplete v-model="securityCode" :items="sse50List" label="证券代码/名称" variant="underlined"></v-autocomplete>
            <v-text-field label="开始时间" variant="underlined" @click="startDatePickerShow = !startDatePickerShow; endDatePickerShow = false" v-model="startFormattedDate"></v-text-field>
            <div class="date-picker" v-if="startDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedStartDates" elevation="12" v-model="startDate" @update:model-value="startDatePickerShow = false"></v-date-picker>
            </div>
            <v-text-field label="结束时间" variant="underlined" @click="endDatePickerShow = !endDatePickerShow; startDatePickerShow = false" v-model="endFormattedDate"></v-text-field>
            <div class="date-picker" v-if="endDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedEndDates" elevation="12" range v-model="endDate" @update:model-value="endDatePickerShow = false"></v-date-picker>
            </div>
            <v-slider label="窗口大小" v-model="window" class="align-center" max="500" min="5" step="5" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="window" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="偏移量" v-model="offset" class="align-center" max="200" min="5" step="5" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="offset" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="默认交易量" v-model="volume" class="align-center" max="5000" min="100" step="100" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="volume" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="LOW-RATIO(‰)" v-model="lowRatio" class="align-center" max="5" min="0" step="0.1" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="lowRatio" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="HIGH-RATIO(‰)" v-model="highRatio" class="align-center" max="5" min="0" step="0.1" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="highRatio" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
        </v-col>
        <v-col cols="8">
            <quant-chart class="quant-chart" :marketData="marketData" :chartConfig="chartConfig"></quant-chart>
        </v-col>
    </v-row>
    <v-btn class="config-btn" color="black" @click="configShow = true">CONFIG</v-btn>
    <v-btn class="exec-btn" color="primary" @click="execuate">EXEC</v-btn>
    <trade-quant v-if="quantDialog" @toggleQuant="toggleQuant" :backtestResult="backtestResult"></trade-quant>
    <v-dialog v-model="configShow" width="300">
        <v-card title="量化交易配置">
            <v-card-text>
                <v-text-field label="初始资产" variant="plain" v-model="init_cash"></v-text-field>
                <v-text-field label="佣金比例" variant="plain" v-model="commision_rate"></v-text-field>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" block  @click="configShow = false">SAVE</v-btn>
            </v-card-actions>
        </v-card>
        
    </v-dialog>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</div>
</template>

    
    
<script setup>
import QuantChart from '@/components/RollingQuantChart.vue'
import TradeQuant from '@/components/TradeQuant.vue'

import {
    HttpManager
} from '@/api';
import {
    ref,
    onMounted,
    computed,
    watch
} from 'vue'

const securityCode = ref('sh600519')
const sse50List = ref([])
const marketData = ref([])

const startDate = ref(new Date(2024, 3, 1))
const endDate = ref(new Date())
const startFormattedDate = computed(() => formattedDate(startDate.value))
const endFormattedDate = computed(() => formattedDate(endDate.value))
const startDatePickerShow = ref(false)
const endDatePickerShow = ref(false)

const window = ref(100)
const offset = ref(50)
const volume = ref(1000)
const lowRatio = ref(0.5)
const highRatio = ref(0.5)
const chartConfig = computed(() => {
    return {
        window: window.value,
        offset: offset.value,
        lowRatio: lowRatio.value,
        highRatio: highRatio.value
    }
})

const quantDialog = ref(false)
const backtestResult = ref({})
const configShow = ref(false)
const commision_rate = ref(0.0001)
const init_cash = ref(100000000)

const toggleQuant = () => {
    quantDialog.value = false
}

const snackbar = ref(false)
const snackbarText = ref("")


const formattedDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
};

const showMessage = (message) => {
    snackbar.value = true
    snackbarText.value = message
}

const getSSE50List = async () => {
    const result = await HttpManager.getSSE50List().catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        showMessage(result ?.message)
        return
    }
    result.data.forEach(item => {
        sse50List.value.push({
            title: item.securityName,
            value: item.securityCode
        })
    });
    if (result.data.length == 0) return
    refreshMarket()
}

watch(securityCode, () => {
    refreshMarket()
})
watch(startDate, () => {
    refreshMarket()
})
watch(endDate, () => {
    refreshMarket()
})

const refreshMarket = async () => {
    if (securityCode.value == undefined || securityCode.value.length < 2) return
    const params = new URLSearchParams()
    const code = securityCode.value.substring(2)
    params.append('code', code)
    params.append('start', startFormattedDate.value)
    params.append('end', endFormattedDate.value)
    const result = await HttpManager.getQuantMarketData(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        showMessage(result ?.message)
        return
    }
    marketData.value = result.data
}

const allowedStartDates = (date) => {
    return date < endDate.value
}

const allowedEndDates = (date) => {
    const today = new Date();
    return date <= today && date > startDate.value
}

const execuate = async() => {
    const params = new URLSearchParams()
    const json = JSON.stringify({
        code: securityCode.value.substring(2),
        start: startFormattedDate.value,
        end: endFormattedDate.value,
        window: window.value,
        offset: offset.value,
        default_volume: volume.value,
        high_ratio: highRatio.value / 1000,
        low_ratio: lowRatio.value / 1000,
        commision_rate: Number(commision_rate.value),
        init_cash: Number(init_cash.value)
    })
    params.append("json_req", json)
    const result = await HttpManager.rollingWindowQuant(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        showMessage("获取量化回测分析数据失败")
        return
    }
    backtestResult.value = result.data
    quantDialog.value = true
}

onMounted(() => {
    getSSE50List()
})
</script>
    
    
<style scoped>

.date-picker {
    position: absolute;
    transform: scale(60%);
    transform-origin: top left;
    z-index: 1;
}

.quant-chart {
    width: 100%;
    height: 480px;
}

.config-btn {
    position: absolute;
    width: 100px;
    right: 160px;
    bottom: 60px;
}

.exec-btn {
    position: absolute;
    width: 100px;
    right: 40px;
    bottom: 60px;
}
</style>
