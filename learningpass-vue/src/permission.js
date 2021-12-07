import router from "./router";
import axios from "axios";



// 路由判断登录 根据路由配置文件的参数
router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requireAuth)) { // 判断该路由是否需要登录权限
        const token = localStorage.getItem("token")
        console.log("------------" + token)
        if (token) { // 判断当前的token是否存在 ； 登录存入的token
            if (to.path === '/login') {
                this.$store.commit("REMOVE_INFO")
                next()
            } else {

                //校验token合法性
                const _this = this
                //axios异步向后端请求数据验证
                axios({
                    url:'/checkToken',
                    method:'get',
                    headers:{
                        token:token
                    }
                }).then(response => {
                    console.log(response.data.data.checkResult)
                    if(!response.data.data.checkResult){
                        console.log('校验失败')
                        next({path:'/error'})
                    }else{
                        next()
                    }
                }).catch(function (error) {
                    console.log(error);
                })

            }
        } else {
            next({
                path: '/login'
            })
        }
    } else {
        next()
    }
})

