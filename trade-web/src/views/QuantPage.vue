<template>
<v-card class="quant-card">
    <v-tabs v-model="tab" bg-color="primary">
        <v-tab value="rolling">移动平均-量化交易</v-tab>
        <v-tab value="disc">内外盘-量化交易</v-tab>
    </v-tabs>

    <v-card-text>
        <v-window v-model="tab" class="market-panel">
            <v-window-item value="rolling">
                <rolling-window></rolling-window>
            </v-window-item>

            <v-window-item value="disc">
                <in-outside></in-outside>
            </v-window-item>
        </v-window>
    </v-card-text>

</v-card>
</template>

<script setup>
import RollingWindow from '@/views/QuantPages/RollingWindowPage.vue'
import InOutside from '@/views/QuantPages/InOutsideDiscPage.vue'

import {
    ref
} from 'vue'

const tab = ref('rolling')
</script>

<style scoped>
.quant-card {
    height: 600px;
}
</style>
