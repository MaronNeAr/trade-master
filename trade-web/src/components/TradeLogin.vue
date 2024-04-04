<template>
<v-dialog v-model="dialog" width="500">
    <v-card>
        <v-card-text>
            <v-tabs v-model="tab">
                <v-tab value="password">密码登录</v-tab>
                <v-tab value="register">账户注册</v-tab>
                <v-tab value="findPsw">密码找回</v-tab>
            </v-tabs>

            <v-card-text>
                <v-window v-model="tab">
                    <v-window-item value="password">
                        <v-text-field label="用户名/邮箱/手机号" variant="underlined" v-model="username"></v-text-field>
                        <v-text-field label="密码" type="password" variant="underlined" v-model="password"></v-text-field>
                        <v-text-field label="验证码" variant="underlined" v-model="checkcode">
                            <template v-slot:append>
                                <v-img :src="checkcodePic" width="160" @click="getCheckcode" />
                            </template>
                        </v-text-field>
                        <v-btn color="primary" block variant="outlined" @click="login">登录</v-btn>
                    </v-window-item>

                    <v-window-item value="register">
                        <v-text-field label="用户名" variant="underlined" v-model="username"></v-text-field>
                        <v-text-field label="昵称" variant="underlined" v-model="nickname"></v-text-field>
                        <v-text-field label="密码" type="password" variant="underlined" v-model="password"></v-text-field>
                        <v-text-field label="确认密码" type="password" variant="underlined" v-model="confirmpwd"></v-text-field>
                        <v-text-field label="手机号" variant="underlined" v-model="cellphone"></v-text-field>
                        <v-text-field label="邮箱" variant="underlined" v-model="email">
                            <template v-slot:append>
                                <v-btn color="primary" variant="underlined" @click="sendEmail">发送验证码</v-btn>
                            </template>
                        </v-text-field>
                        <v-text-field label="验证码" variant="underlined" v-model="checkcode"></v-text-field>
                        <v-btn color="black" block variant="outlined" @click="register">注册</v-btn>
                    </v-window-item>

                    <v-window-item value="findPsw">
                        <v-text-field label="手机号" variant="underlined" v-model="cellphone"></v-text-field>
                        <v-text-field label="邮箱" variant="underlined" v-model="email">
                            <template v-slot:append>
                                <v-btn color="primary" variant="underlined" @click="sendEmail">发送验证码</v-btn>
                            </template>
                        </v-text-field>
                        <v-text-field label="验证码" variant="underlined" v-model="checkcode"></v-text-field>
                        <v-text-field label="密码" type="password" variant="underlined" v-model="password"></v-text-field>
                        <v-text-field label="确认密码" type="password" variant="underlined" v-model="confirmpwd"></v-text-field>
                        <v-btn color="red" block variant="outlined" @click="findPsw">找回密码</v-btn>
                    </v-window-item>

                </v-window>
            </v-card-text>
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
const nickname = ref()
const password = ref()
const confirmpwd = ref()
const cellphone = ref()
const email = ref()
const checkcode = ref()
const accessToken = ref()
const checkcodeKey = ref()
const checkcodePic = ref()

const getCheckcode = async() => {
    const result = await HttpManager.getCheckcodePic().catch(error => {
        console.log(error)
        return
    })
    if (result?.key) checkcodeKey.value = result.key
    if (result?.aliasing) checkcodePic.value = result.aliasing
}

const login = async() => {
    const flag = await verifyCheckcode(checkcodeKey.value, checkcode.value)
    if (!flag) {
        getCheckcode()
        emits('message', "验证码错误，请重新输入")
        checkcode.value = ""
        return
    }
    await passwordAuth(username.value, password.value)
    if (accessToken.value) getUserInfo(accessToken.value)
}

const register = async() => {
    const params = {
        username: username.value,
        nickname: nickname.value,
        password: password.value,
        confirmpwd: confirmpwd.value,
        cellphone: cellphone.value,
        email: email.value,
        checkcode: checkcode.value
    }
    const result = await HttpManager.register(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?. success) {
        console.log(result ?. message)
        emits('message', result ?. message)
        return
    }
    emits('message', result ?. message)
    await passwordAuth(username.value, password.value)
    if (accessToken.value) getUserInfo(accessToken.value)
}

const findPsw = async() => {
    const params = {
        cellphone: cellphone.value,
        email: email.value,
        password: password.value,
        confirmpwd: confirmpwd.value,
        checkcode: checkcode.value
    }
    const result = await HttpManager.findPassword(params).catch(error => {
        console.log(error)
        return
    })
    if (!result ?. success) {
        console.log(result ?. message)
        emits('message', result ?. message)
        return
    }
    emits('message', result ?. message)
    await passwordAuth(username.value, password.value)
    if (accessToken.value) getUserInfo(accessToken.value)
}

const sendEmail = () => {
    const params = new URLSearchParams()
    params.append("email", email.value)
    HttpManager.sendEmail(params).catch(error => {
        console.log(error)
        return
    })
}

const verifyCheckcode = async(key, code) =>{
    const params = new URLSearchParams()
    params.append("key", key)
    params.append("code", code)
    const result = await HttpManager.verifyCheckcode(params).catch(error => {
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
    getCheckcode()
    
})
</script>
