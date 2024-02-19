<template>
    <v-dialog v-model="dialog" width="500">
        <v-card>
            <v-img :src="checkCodePic" width="160" height="100" @click="getCheckCode" />
            <v-otp-input type="text" length="4" @finish="verifyCheckCode" v-model="checkcode"></v-otp-input>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { HttpManager } from '@/api'
import {onMounted, ref, defineEmits, watch} from 'vue'

const emits = defineEmits(['verify'])
const dialog = ref(true)
const checkcode = ref('')
const checkCodeKey = ref()
const checkCodePic = ref()

const getCheckCode = async() => {
    const result = await HttpManager.getCheckCodePic().catch(error => {
        console.log(error)
        return
    })
    if (result.key) checkCodeKey.value = result.key
    if (result.aliasing) checkCodePic.value = result.aliasing
}

const verifyCheckCode = async() =>{
    const params = new URLSearchParams()
    params.append("key", checkCodeKey.value)
    params.append("code", checkcode.value)
    const state = await HttpManager.verifyCheckCode(params).catch(error => {
        console.log(error)
        emits('verify', false)
        return
    })
    if (state) emits('verify', true)
    else emits('verify', false)
}

const closeVerifyDialog = () => {
    emits('verify', false)
}

watch(dialog, () => {
    closeVerifyDialog()
})

onMounted(() => {
    getCheckCode()
})
</script>

<style>

</style>