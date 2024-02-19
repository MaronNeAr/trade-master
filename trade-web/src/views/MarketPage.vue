<template>
<v-card>
    <v-tabs v-model="tab" bg-color="primary">
        <v-tab value="stock">股票</v-tab>
        <v-tab value="fund">基金</v-tab>
        <v-tab value="bond">债券</v-tab>
        <v-tab value="index">指数</v-tab>
        <v-tab value="futures">期货</v-tab>
    </v-tabs>

    <v-card-text>
        <v-window v-model="tab" class="market-panel">
            <v-window-item value="stock">
                <stock-page></stock-page>
            </v-window-item>

            <v-window-item value="fund">
                <fund-page></fund-page>
            </v-window-item>

            <v-window-item value="bond">
                <bond-page></bond-page>
            </v-window-item>

            <v-window-item value="index">
                <index-page></index-page>
          </v-window-item>

          <v-window-item value="futures">
            Three
        </v-window-item>
        </v-window>
    </v-card-text>
</v-card>
</template>

<script setup>
import StockPage from './MarketPages/StockPage.vue'
import FundPage from './MarketPages/FundPage.vue'
import BondPage from './MarketPages/BondPage.vue'
import IndexPage from './MarketPages/IndexPage.vue'
import {ref} from 'vue'

const tab = ref()
</script>

<style scoped>
.market-panel {
  height: 100%;
  overflow-y: auto;
}


</style>
