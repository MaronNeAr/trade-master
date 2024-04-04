<template>
  <div class="main-box">
    <trade-header class="trade-header" :active="active" @toggleLogin="toggleLogin" @toggleInfo="toggleInfo"></trade-header>
    <div class="trade-content">
        <router-view class="router-view"></router-view>
    </div>
    <trade-footer class="trade-footer"></trade-footer>
    <trade-login v-if="loginDialog" @toggleLogin="toggleLogin" @message="showMessage" @success="activeChange"></trade-login>
    <trade-info v-if="infoDialog" @toggleInfo="toggleInfo" @exit="activeChange"></trade-info>
    <v-snackbar :timeout="2000" v-model="snackbar">
        {{ snackbarText }}
    </v-snackbar>
  </div>
</template>

<script setup>
import TradeFooter from '@/components/layout/TradeFooter.vue'
import TradeHeader from '@/components/layout/TradeHeader.vue'
import TradeLogin from '@/components/TradeLogin.vue'
import TradeInfo from '@/components/TradeInfo.vue'
import Cookies from 'js-cookie'
import router from '@/router'
import { onMounted, ref } from 'vue'
import { HttpManager } from '@/api'

const loginDialog = ref(false)
const infoDialog = ref(false)
const active = ref(false)
const snackbar = ref(false)
const snackbarText = ref("")

const toggleLogin = (state) => {
    loginDialog.value = state

}

const toggleInfo = (state) => {
    infoDialog.value = state
}

const showMessage = (message) => {
    snackbar.value = true
    snackbarText.value = message 
}

const activeChange = (state) => {
    active.value = state
}

const initUserInfo = async(token) => {
    const params = new URLSearchParams()
    params.append("token", token)
    const result = await HttpManager.checkToken(params).catch(error => {
        console.log(error)
        return
    })
    if (result?.user_name) {
        const userInfo = JSON.parse(result.user_name)
        console.log(userInfo)
        Cookies.set("nickname", userInfo.nickname)
        active.value = true
        return
    }
    else if (result?.error) snackbarText.value = result?.error_description
    else snackbarText.value = "服务器出错，请稍后再试"
    router.push("/login")
}

onMounted(() => {
    const token = Cookies.get("token")
    if (token) initUserInfo(token)
    else router.push("/login")
})
</script>

<style scoped>
.trade-header {
    position: fixed;
}

.trade-content {
    position: fixed;
    top: 75px;
    width: 100%;
    bottom: 64px;
    background: url("../assets/images/background.png");
}

.router-view {
    position: relative;
    left: 10%;
    width: 80%;
}

.trade-footer {
    position: fixed;
}

.test {
    border: 1px solid black;
}

div {
    animation: fadeInAnimation ease 0.5s;
}
</style>