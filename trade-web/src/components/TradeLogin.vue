<template>
<v-dialog v-model="dialog" width="500">
    <v-card>
        <v-card-text>
            <v-tabs v-model="tab">
                <v-tab value="password">密码登录</v-tab>
                <v-tab value="checkcode">验证码登录</v-tab>
                <v-tab value="register">账户注册</v-tab>
            </v-tabs>

            <v-card-text>
                <v-window v-model="tab">
                    <v-window-item value="password">
                        <v-text-field label="用户名/邮箱/手机号" variant="underlined" v-model="username"></v-text-field>
                        <v-text-field label="密码" type="password" variant="underlined" v-model="password"></v-text-field>
                        <v-text-field label="验证码" variant="underlined" v-model="checkcode">
                            <template v-slot:append>
                                <v-img :src="checkCodePic" width="160" @click="getCheckCode" />
                            </template>
                        </v-text-field>
                    </v-window-item>

                    <v-window-item value="checkcode">
                        Two
                    </v-window-item>

                    <v-window-item value="register">
                        Two
                    </v-window-item>

                </v-window>
            </v-card-text>
            <v-card-actions>
                <v-btn color="primary" block  @click="login">登录</v-btn>
            </v-card-actions>
        </v-card-text>
    </v-card>
</v-dialog>
</template>

<script setup>
import { HttpManager } from "@/api"
import {
    ref,
    defineEmits,
    onMounted,
    watch
} from "vue"
import Cookies from 'js-cookie'

const emits = defineEmits(['toggleLogin', 'message', 'success'])
const tab = ref(0)
const dialog = ref(true)
const username = ref()
const password = ref()
const checkcode = ref()
const accessToken = ref()
const checkCodeKey = ref()
const checkCodePic = ref()

const getCheckCode = async() => {
    const result = await HttpManager.getCheckCodePic().catch(error => {
        console.log(error)
        return
    })
    if (result?.key) checkCodeKey.value = result.key
    if (result?.aliasing) checkCodePic.value = result.aliasing
}

const login = async() => {
    // console.log(username.value + password.value)
    const flag = await verifyCheckCode(checkCodeKey.value, checkcode.value)
    if (!flag) {
        getCheckCode()
        emits('message', "验证码错误，请重新输入")
        checkcode.value = ""
        return
    }
    await passwordAuth(username.value, password.value)
    if (accessToken.value) getUserInfo(accessToken.value)
}

const verifyCheckCode = async(key, code) =>{
    const params = new URLSearchParams()
    params.append("key", key)
    params.append("code", code)
    const result = await HttpManager.verifyCheckCode(params).catch(error => {
        console.log(error)
        return false
    })
    return result
}

const passwordAuth = async(username, password) => {
    const params = '{"username":"' + username + '","password":"' + password + '","authType":"password"}'
    const encodeURI = encodeURIComponent(params)
    const result = await HttpManager.passwordAuth(encodeURI).catch(error => {
        console.log(error)
        return
    })
    if (result?.access_token) {
        accessToken.value = result.access_token
        Cookies.set("refresh_token", result.refresh_token)
    } else if (result?.error) emits('message', result.error_description)
    else emits('message', "服务器出错，请稍后再试")
}

const getUserInfo = async(token) => {
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
        Cookies.set("token", token)
        location.reload()
        emits('success', true)
        closeLoginDialog()
    }
    else if (result?.error) emits('message', result.error_description)
    else emits('message', "服务器出错，请稍后再试")
} 

const closeLoginDialog = () => {
    emits('toggleLogin', false)
}

watch(dialog, () => {
    closeLoginDialog()
})

onMounted(() => {
    getCheckCode()
})
</script>
