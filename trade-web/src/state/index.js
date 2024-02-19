import { provide, ref } from 'vue';
// import Cookies from 'js-cookie'
const loginState = ref(true)

// 使用 provide 提供全局变量
provide('loginState', loginState)
// console.log(inject('loginState'))
