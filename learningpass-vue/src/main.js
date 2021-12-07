import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import VueAxios from 'vue-axios'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import "./axios"//请求拦截
import "./permission"//路由拦截

const app = createApp(App)
app
    .use(store)
    .use(router)
    .use(VueAxios,axios)
    .use(ElementPlus)
    .mount('#app')

app.config.globalProperties.$axios = axios
app.config.devtools = true