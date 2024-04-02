<template>
<div>
    <v-row>
        <v-col cols="4">
            <v-combobox v-model="securityCodes" :items="sse50List" label="证券代码/名称" chips multiple variant="underlined"></v-combobox>
            <v-text-field label="开始时间" variant="underlined" @click="startDatePickerShow = !startDatePickerShow; endDatePickerShow = false" v-model="startFormattedDate"></v-text-field>
            <div class="date-picker" v-if="startDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedStartDates" elevation="12" v-model="startDate" @update:model-value="startDatePickerShow = false"></v-date-picker>
            </div>
            <v-text-field label="结束时间" variant="underlined" @click="endDatePickerShow = !endDatePickerShow; startDatePickerShow = false" v-model="endFormattedDate"></v-text-field>
            <div class="date-picker" v-if="endDatePickerShow">
                <v-date-picker color="primary" :allowed-dates="allowedEndDates" elevation="12" range v-model="endDate" @update:model-value="endDatePickerShow = false"></v-date-picker>
            </div>
            <br />
            <v-slider label="交易量-内外盘差异倍率" v-model="multiple" class="align-center" max="10" min="0.1" step="0.1" hide-details>
                <template v-slot:append>
                    <v-text-field v-model="multiple" hide-details single-line density="compact" type="number" style="width: 56px" variant="underlined"></v-text-field>
                </template>
            </v-slider>
        </v-col>
        <v-col cols="8">
            <quant-chart class="quant-chart" :marketData="marketDataMap" :chartConfig="chartConfig"></quant-chart>
        </v-col>
    </v-row>
    <v-btn class="exec-btn" color="primary" @click="execuate">EXEC</v-btn>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</div>
</template>

    
        
        
    
<script setup>
import QuantChart from '@/components/DiscQuantChart.vue'

import {
    HttpManager
} from '@/api';
import {
    ref,
    onMounted,
    computed,
    watch
} from 'vue'

const securityCodes = ref([])
const sse50List = ref([])
const marketDataMap = ref({})

const startDate = ref(new Date(2024, 2, 1))
const endDate = ref(new Date())
const startFormattedDate = computed(() => formattedDate(startDate.value))
const endFormattedDate = computed(() => formattedDate(endDate.value))
const startDatePickerShow = ref(false)
const endDatePickerShow = ref(false)

const multiple = ref(1)

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
    securityCodes.value.push(sse50List.value[14])
    refreshMarket()
}

watch(securityCodes, () => {
    refreshMarket()
})
watch(startDate, () => {
    refreshMarket()
})
watch(endDate, () => {
    refreshMarket()
})

const refreshMarket = () => {
    securityCodes.value.forEach(async (item) => {
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

const execuate = () => {
    const params = new URLSearchParams()
    const json = JSON.stringify({
        code: securityCodes.value[0].value.substring(2),
        start: startFormattedDate.value,
        end: endFormattedDate.value,
        mul: multiple.value
    })
    params.append("json_req", json)
    const result = HttpManager.rollingWindowQuant(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?.success) {
        console.log(result ?.message)
        showMessage(result ?.message)
        return
    }
    showMessage(result.data)
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
    width: 800px;
    height: 480px;
}

.exec-btn {
    position: absolute;
    right: 40px;
    bottom: 100px;
}
</style>
