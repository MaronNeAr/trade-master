<template>
    <v-dialog v-model="dialog" width="300">
        <v-card>
            <v-card-text>确认操作？</v-card-text>
            <v-card-actions>
                <v-row>
                    <v-col cols="6">
                        <v-btn color="red" block="" @click="closeConfirmDialog">取消</v-btn>
                    </v-col>
                    <v-col cols="6">
                        <v-btn color="primary" block  @click="confirm">确认</v-btn>
                    </v-col>
                </v-row>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, defineEmits, watch} from 'vue'

const emits = defineEmits(['confirm'])
const dialog = ref(true)

const closeConfirmDialog = () => {
    emits('confirm', false)
}

const confirm = () => {
    emits('confirm', true)
}

watch(dialog, () => {
    closeConfirmDialog()
})
</script>

<style>

</style>