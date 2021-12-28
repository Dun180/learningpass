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
import "./../public/css/reset.css"
import {API} from "@/lib/api";

//markdown编辑器
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';
//预览组件
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js';
import '@kangc/v-md-editor/lib/theme/style/github.css';
// highlightjs
import hljs from 'highlight.js';
// Prism
import Prism from 'prismjs';
// highlight code
import 'prismjs/components/prism-json';

VMdPreview.use(githubTheme, {
    Hljs: hljs,
});

const app = createApp(App)
app
    .use(store)
    .use(router)
    .use(VueAxios,axios)
    .use(ElementPlus)
    .use(VMdEditor)
    .use(VMdPreview)
    .mount('#app')
// app.config.globalProperties.$api = new API("http://192.168.31.9:8081")
app.config.globalProperties.$api = new API("http://localhost:8081")
app.config.globalProperties.$axios = axios
app.config.devtools = true
VMdEditor.use(vuepressTheme, {
    Prism,
});