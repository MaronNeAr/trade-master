<template>
    <v-dialog v-model="dialog" width="500">
        <v-card>
            <v-img :src="checkcodePic" width="160" height="100" @click="getCheckcode" />
            <v-otp-input type="text" length="4" @finish="verifyCheckcode" v-model="checkcode"></v-otp-input>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { HttpManager } from '@/api'
import {onMounted, ref, defineEmits, watch} from 'vue'

const emits = defineEmits(['verify'])
const dialog = ref(true)
const checkcode = ref('')
const checkcodeKey = ref()
const checkcodePic = ref()

const getCheckcode = async() => {
    const result = await HttpManager.getCheckcodePic().catch(error => {
        console.log(error)
        return
    })
    if (result.key) checkcodeKey.value = result.key
    if (result.aliasing) checkcodePic.value = result.aliasing
}

const verifyCheckcode = async() =>{
    const params = new URLSearchParams()
    params.append("key", checkcodeKey.value)
    params.append("code", checkcode.value)
    const state = await HttpManager.verifyCheckcode(params).catch(error => {
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
    getCheckcode()
})
</script>

<style>

</style>