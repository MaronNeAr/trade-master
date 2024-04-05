<template>
  <div>
    <div v-for="record, idx in recordList" :key="idx">
        <v-row :class="record.transactionType == 'buy' ? 'red' : 'green'">
            <v-col cols="6">
                <v-stepper :model-value="status2ModelValue(record.transactionStatus)" >
                    <v-stepper-header>
                      <v-stepper-item
                        complete
                        title="发起交易"
                        value="1"
                      ></v-stepper-item>
                
                      <v-divider></v-divider>
                
                      <v-stepper-item
                        :rules="[() => record.transactionStatus != 'cancelled']"
                        :title="record.transactionStatus != 'cancelled' ? '风险检测' : '撤单'"
                        value="2"
                      ></v-stepper-item>
                
                      <v-divider></v-divider>
                
                      <v-stepper-item
                        title="交易完成"
                        value="3"
                      ></v-stepper-item>
                    </v-stepper-header>
                </v-stepper>
            </v-col>
            <v-col cols="2">
                <v-list-item :title="record.securityCode" :subtitle="record.securityName" height="70"></v-list-item>
            </v-col>
            <v-col cols="2">
                <v-list-item :title="(record.transactionPrice * record.transactionVolume).toFixed(2)" :subtitle="record.transactionVolume" height="70"></v-list-item>
            </v-col>
            <v-col cols="2">
                <v-list-item :title="record.transactionPrice.toFixed(2)" :subtitle="record.currentPrice.toFixed(2)" height="70"></v-list-item>
            </v-col>
        </v-row>
        <br />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { HttpManager } from "@/api"

const recordList = ref([])

const status2ModelValue = (status) => {
  if (status == 'active') return 0
  else if (status == 'cancelled') return 1
  else return 2
}

const getAllTradeDetails = async() => {
  const result = await HttpManager.details().catch(error => {
    console.log(error)
    return
  })
  if (!result ?. success) {
    console.log(result ?.message)
    return
  }
  recordList.value = []
  result.data.forEach(async(item) => {
    const ret = await HttpManager.getMarketData(item.securityCode).catch(error => {
      console.log(error)
      return
    })
    if (!ret ?. success) {
      console.log(ret ?.message)
      return
    }
    item.securityName = ret.data.name
    item.currentPrice = ret.data.lastPrice
    recordList.value.push(item)
  });
  recordList.value.sort((o1, o2) => o2.transactionDate - o1.transactionDate)
}

onMounted(() => {
  getAllTradeDetails()
})
</script>

<style scoped>
.v-stepper {
    border-radius: 0;
    box-shadow: 0 0;
}

.red {
  background: linear-gradient(to right, #FFFFFF, #fcecee);
  color: red;
}

.red * {
  background-color: rgba(152, 251, 152, 0); 
  color: red;
}
.green {
  background: linear-gradient(to right, #FFFFFF, #ebfceb);
  color: green;
}

.green * {
  background-color: rgba(152, 251, 152, 0); 
  color: green;
}
</style>