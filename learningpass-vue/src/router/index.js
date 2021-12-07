import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Login from "@/views/Login";
import Error from "@/views/Error";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/about',
    name: 'About',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/About.vue'),
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/error',
    name:'Error',
    component: Error
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// //路由跳转验证
// router.beforeEach((to,from,next) =>{
//   if (to.path.startsWith('/login')){//跳转到登录页面就移除用户信息
//     this.$store.commit("REMOVE_INFO")
//     next()
//   }else {
  //校验token合法性
  //   const _this = this
  //   //axios异步向后端请求数据验证
  //   this.$axios.get('/checkToken',_this.$store.getters.getToken).then(response => {
  //     if(!response.data){
  //       console.log('校验失败')
  //       next({path:'/error'})
  //     }
  //   })
//     next()
//   }
// })


export default router
