<template>
    <v-bottom-navigation v-model="tag" mode="shift" grow>
        <v-btn value="market" to="/market">
            <v-icon>mdi-chart-line</v-icon>
            <span>行情</span>
          </v-btn>
      
        <v-btn value="transaction" to="/transaction">
          <v-icon>mdi-set-none</v-icon>
          <span>交易</span>
        </v-btn>

        <v-btn value="quant" to="/quant">
            <v-icon>mdi-script-text-outline</v-icon>
            <span>QUANT</span>
          </v-btn>
      
        <v-btn value="account" to="/account">
          <v-icon>mdi-account-outline</v-icon>
      
          <span>我</span>
        </v-btn>
      </v-bottom-navigation>
</template>

<script setup>
import { ref } from 'vue'
const tag = ref('/')
</script>

<style scoped>
</style>
