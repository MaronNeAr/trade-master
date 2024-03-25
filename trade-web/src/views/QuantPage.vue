<template>
<v-card class="quant-card">
    <h3>移动平均-量化交易</h3>
    <v-divider></v-divider>
    <br />
    <v-row>
        <v-col cols="3">
            <v-combobox
                v-model="securityCodes"
                :items="items"
                label="证券代码/名称"
                chips
                multiple
                variant="underlined"
            ></v-combobox>
            <!-- <v-autocomplete label="证券代码/名称" variant="underlined" v-model="brokerage" :items="brokerageList"></v-autocomplete> -->
            <v-text-field label="开始时间" variant="underlined" @focus="startDatePickerShow = true" v-model="startFormattedDate"></v-text-field>
            <div class="date-picker" v-if="startDatePickerShow">
                <v-date-picker color="primary" elevation="12" v-model="startDate" @update:model-value="startDatePickerShow = false"></v-date-picker>
            </div>
            <v-text-field label="结束时间" variant="underlined" @focus="endDatePickerShow = true" v-model="endFormattedDate"></v-text-field>
            <div class="date-picker" v-if="endDatePickerShow">
                <v-date-picker color="primary" elevation="12" v-model="endDate" @update:model-value="endDatePickerShow = false"></v-date-picker>
            </div>
        </v-col>
        <v-col cols="3"></v-col>
        <v-col cols="3"></v-col>
        <v-col cols="3"></v-col>
    </v-row>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
</v-card>
</template>

<script setup>
import { computed } from 'vue';
import {
    ref,
    onMounted
} from 'vue'

const securityCodes = ref([])
const items = ref([
          'Programming',
          'Design',
          'Vue',
          'Vuetify',
        ])

const startDate = ref(new Date(2024, 2, 27))
const endDate = ref(new Date())
const startFormattedDate = computed(() => formattedDate(startDate.value))
const endFormattedDate = computed(() => formattedDate(endDate.value))
const startDatePickerShow = ref(false)
const endDatePickerShow = ref(false)

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

onMounted(() => {
    showMessage("QUANT启动")
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
    
}
</style>
