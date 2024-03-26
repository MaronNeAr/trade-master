<template>
<v-card class="quant-card">
    <h3>移动平均-量化交易</h3>
    <v-divider></v-divider>
    <br />
    <v-row>
        <v-col cols="4">
            <v-combobox
                v-model="securityCodes"
                :items="sse50List"
                label="证券代码/名称"
                chips
                multiple
                variant="underlined"
            ></v-combobox>
            <v-text-field label="开始时间" variant="underlined" @click="startDatePickerShow = !startDatePickerShow; endDatePickerShow = false" v-model="startFormattedDate"></v-text-field>
            <div class="date-picker" v-if="startDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedStartDates" elevation="12" v-model="startDate" @update:model-value="startDatePickerShow = false"></v-date-picker>
            </div>
            <v-text-field label="结束时间" variant="underlined" @click="endDatePickerShow = !endDatePickerShow; startDatePickerShow = false" v-model="endFormattedDate"></v-text-field>
            <div class="date-picker" v-if="endDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedEndDates" elevation="12" range v-model="endDate" @update:model-value="endDatePickerShow = false"></v-date-picker>
            </div>
            <v-slider label="窗口大小" v-model="window" class="align-center" max="1000" min="5" step="5" hide-details>
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
            <v-slider label="默认交易量" v-model="volume" class="align-center" max="2000" min="100" step="100" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="volume" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="LOW-RATIO" v-model="lowRatio" class="align-center" max="5" min="0" step="0.1" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="lowRatio" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
            <br />
            <v-slider label="HIGH-RATIO" v-model="highRatio" class="align-center" max="5" min="0" step="0.1" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="highRatio" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
        </v-col>
        <v-col cols="8">
            <quant-chart class="quant-chart" :marketData="marketDataMap"></quant-chart>
        </v-col>
    </v-row>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</v-card>
</template>

<script setup>
import QuantChart from '@/components/QuantChart.vue'

import { HttpManager } from '@/api';
import {
    ref,
    onMounted,
    computed,
    watch
} from 'vue'

const securityCodes = ref([])
const sse50List = ref([])
const marketDataMap = ref({})

const startDate = ref(new Date(2024, 1, 27))
const endDate = ref(new Date())
const startFormattedDate = computed(() => formattedDate(startDate.value))
const endFormattedDate = computed(() => formattedDate(endDate.value))
const startDatePickerShow = ref(false)
const endDatePickerShow = ref(false)

const window = ref(100)
const offset = ref(50)
const volume = ref(1000)
const lowRatio = ref(0.1)
const highRatio = ref(0.1)
 
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

const getSSE50List = async() => {
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
    securityCodes.value.push(sse50List.value[0])
    refreshMarket()
}

watch(securityCodes, () => {refreshMarket()})
watch(startDate, () => {refreshMarket()})
watch(endDate, () => {refreshMarket()})

const refreshMarket = () => {
    securityCodes.value.forEach(async(item) => {
        if (item.value.length < 2) return
        const params = new URLSearchParams()
        const code = item.value.substring(2)
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
        marketDataMap.value[code] = result.data
    })
}

const allowedStartDates = (date) => {
    return date < endDate.value
}

const allowedEndDates = (date) => {
    const today = new Date();
    return date <= today && date > startDate.value
}

onMounted(() => {
    getSSE50List()
})
</script>

<style scoped>
.quant-card {
    padding: 20px;
    height: 600px;
    overflow: scroll;
}

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
</style>
