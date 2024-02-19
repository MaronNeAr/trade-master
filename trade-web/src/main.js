import { createApp } from 'vue'
import App from './App.vue'
import vuetify from './plugins/vuetify'
import router from "./router"
import VueCookies from 'vue-cookies'
import '@/state/index.js'
import '@/assets/css/main.css'
import './api'
import { loadFonts } from './plugins/webfontloader'

loadFonts()

createApp(App)
  .use(vuetify)
  .use(router)
  .use(VueCookies)
  .mount('#app')
