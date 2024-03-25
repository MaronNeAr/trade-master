<template>
<v-app-bar :elevation="2" active title="基于分布式架构的T + 0量化交易平台" rounded>
    <template v-slot:prepend>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
      </template>
    <template v-slot:append>
        <v-btn v-if="props.active" color="primary" @click="openInfoDialog">{{ nickname }}</v-btn>
        <v-btn v-else color="primary" @click="openLoginDialog">登录/注册</v-btn>

        <v-btn icon="mdi-magnify"></v-btn>

        <v-btn icon="mdi-dots-vertical"></v-btn>
    </template>
</v-app-bar>
</template>

<script setup>
import { defineEmits, defineProps, ref, watch } from 'vue'
import Cookies from 'js-cookie'

const emits = defineEmits(['toggleLogin', 'toggleInfo'])
const props = defineProps(['active'])

const openLoginDialog = () => {
    emits("toggleLogin", true)
}

const openInfoDialog = () => {
    emits("toggleInfo", true)
}

const nickname = ref(Cookies.get("nickname"))

watch(props.active, () => {
    nickname.value = Cookies.get("nickname")
})
</script>