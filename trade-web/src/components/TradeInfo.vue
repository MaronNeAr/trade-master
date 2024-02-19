<template>
    <v-dialog v-model="dialog" width="500">
        <v-card title="个人资料">
            <v-card-text>
                <br />
                <v-row>
                    <v-col cols="6">
                        <v-text-field label="昵称" variant="plain" v-model="userInfo.nickname"></v-text-field>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="手机号" variant="plain" v-model="userInfo.cellphone"></v-text-field>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="邮箱" variant="plain" v-model="userInfo.email"></v-text-field>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="账号" variant="plain" v-model="userInfo.username"></v-text-field>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="实名认证" variant="plain" v-model="userInfo.name"></v-text-field>
                    </v-col>
                    <v-col cols="6">
                        <v-text-field label="注册时间" variant="plain" v-model="userInfo.createTime"></v-text-field>
                    </v-col>
                </v-row>
                <v-card-actions>
                    <v-row>
                        <v-col cols="6">
                            <v-btn color="primary" block  @click="update">更新资料</v-btn>
                        </v-col>
                        <v-col cols="6">
                            <v-btn color="red" block  @click="logout">退出登录</v-btn>
                        </v-col>
                    </v-row>
                </v-card-actions>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, defineEmits, watch, onMounted } from 'vue'
import Cookies from 'js-cookie'
import { HttpManager } from '@/api'

const emits = defineEmits(['toggleInfo', 'message', 'exit'])
const dialog = ref(true)
const userInfo = ref({})

const update = () => {
    closeInfoDialog()
}

const logout = () => {
    Cookies.remove("nickname")
    Cookies.remove("token")
    emits('exit', false)
    closeInfoDialog()
}

const closeInfoDialog = () => {
    emits('toggleInfo', false)
}

const getUserInfo = async(token) => {
    const params = new URLSearchParams()
    params.append("token", token)
    const result = await HttpManager.checkToken(params).catch(error => {
        console.log(error)
        return
    })
    if (result?.user_name) {
        userInfo.value = JSON.parse(result.user_name)
    }
    else if (result?.error) emits('message', result.error_description)
    else emits('message', "服务器出错，请稍后再试")
} 

watch(dialog, () => {
    closeInfoDialog()
})

onMounted(() => {
    getUserInfo(Cookies.get("token"))
})
</script>