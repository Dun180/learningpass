import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/views/Login";
import Error from "@/views/Error";
import Index from "@/views/Index";
import Student from "@/views/student/Student";
import Teacher from "@/views/teacher/Teacher";
import Register from "@/views/Register";
import Header from "@/components/Header";
import ClassController from "@/views/teacher/ClassController";
import TaskEdit from "@/views/teacher/TaskEdit";
import TaskList from "@/views/teacher/TaskList";
import StudentTaskList from "@/views/student/StudentTaskList";
import Answer from "@/views/student/Answer";
import Grade from "@/views/teacher/Grade";
import MutualEvaluationList from "@/views/student/MutualEvaluationList";
import Evaluate from "@/views/student/Evaluate";
import GradeEvaluate from "@/views/teacher/GradeEvaluate";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Login,
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/error',
    name:'Error',
    component: Error
  },
  {
    path: '/index',
    name:'Index',
    component: Index
  },
  {
    path: '/student',
    name:'Student',
    components: {
      default:Student,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/student/taskList',
    name:'StudentTaskList',
    components: {
      default:StudentTaskList,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/student/answer',
    name:'Answer',
    components: {
      default:Answer,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/student/evaluate',
    name:'Evaluate',
    components: {
      default:Evaluate,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/student/mutualEvaluationList',
    name:'MutualEvaluationList',
    components: {
      default:MutualEvaluationList,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/teacher',
    name:'Teacher',
    components: {
      default:Teacher,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/class/:classId',
    name:'ClassController',
    components: {
      default:ClassController,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/teacher/taskEdit',
    name:'TaskEdit',
    components: {
      default:TaskEdit,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/teacher/taskList',
    name:'TaskList',
    components: {
      default:TaskList,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/teacher/grade',
    name:'Grade',
    components: {
      default:Grade,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
  {
    path: '/teacher/gradeEvaluate',
    name:'GradeEvaluate',
    components: {
      default:GradeEvaluate,
      'header':Header
    },
    meta: {
      requireAuth: true
    }
  },
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
