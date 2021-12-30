import axios from "axios";
import {ElMessage} from "element-plus"
import router from './router'
import store from './store'



 //axios.defaults.baseURL='http://192.168.31.9:8081'
axios.defaults.baseURL='http://localhost:8081'

//前置拦截
//添加请求拦截器
axios.interceptors.request.use(config => {
    //在发送请求之前做些什么
    return config;
}, error => {
    //对请求错误做些什么
    return Promise.reject(error);
});

//后置拦截
//添加响应拦截器
axios.interceptors.response.use(response => {
        // 对响应数据做点什么
        let res = response.data;

        console.log("================")
        console.log(res)
        console.log("================")

        if(res.code === 200){
            return response
        }else {

            ElMessage({
                showClose: true,
                message: response.data.msg,
                type: 'error'
            });

            return Promise.reject(response.data.msg)
        }
    },
    error => {
        console.log("error:"+error)
        // 对响应错误做点什么
        if(error.response){
            if (error.response.data){
                error.message = error.response.data.msg
            }


            //权限错误
            if (error.response.status === 401){
                store.commit("REMOVE_INFO")
                router.push("/login");
            }



        }

        ElMessage({
            showClose: true,
            message: error.message,
            type: 'error'
        });

        return Promise.reject(error)
    })