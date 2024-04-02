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
        <th class="text-right">
          持仓量
        </th>
        <th class="text-right">
          市值
        </th>
        <th class="text-right">
          成本
        </th>
        <th class="text-right">
          当前价格
        </th>
        <th class="text-right">
          平均成本
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="position, idx in positionList"
        :key="idx"
        :class="position.currentPrice * position.positionQuantity > position.positionCost ? 'red' : 'green'"
      >
        <td class="text-center">{{ position.securityCode }}</td>
        <td class="text-center">{{ position.securityName }}</td>
        <td class="text-right">{{ position.positionQuantity }}</td>
        <td class="text-right">{{ (position.positionQuantity * position.currentPrice).toFixed(2) }}</td>
        <td class="text-right">{{ position.positionCost.toFixed(2) }}</td>
        <td class="text-right">{{ position.currentPrice }}</td>
        <td class="text-right">{{ (position.positionCost / position.positionQuantity).toFixed(2) }}</td>
      </tr>
    </tbody>
  </v-table>
</div>
</template>
  
<script setup>
import { HttpManager } from '@/api'
import { onMounted, ref } from 'vue'

const positionList = ref([])

const getPostionList = async() => {
  const result = await HttpManager.position().catch(error => {
    console.log(error)
    return
  })
  if (!result ?. success) {
    console.log(result ?.message)
    return
  }
  positionList.value = result.data

  positionList.value.forEach(async (item) => {
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

onMounted(() => {
  getPostionList()
})
</script>

<style scoped>
.red {
  color: red;
}

.green {
  color: green;
}
</style>

