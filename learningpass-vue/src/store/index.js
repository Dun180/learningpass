import { createStore } from 'vuex'

export default createStore({
  state: {
    //私有属性
    token: localStorage.getItem("token"),
    userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
    key: Date.now()
  },
  mutations: {
    //  set
    SET_TOKEN: (state,token) => {
      state.token = token
      localStorage.setItem("token", token)
    },
    SET_USERINFO: (state,userInfo) => {
      state.userInfo = userInfo
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    },
    REMOVE_INFO: (state) => {
      state.token = ''
      state.userInfo = {}
      localStorage.setItem("token", '')
      sessionStorage.setItem("userInfo", JSON.stringify(''))


    },
    increment(state) {//刷新页面用
      state.key=Date.now();
    }
  },
  getters:{
    //  get
    getUser: state => {
      return state.userInfo
    },
    getToken: state => {
      return state.token
    }
  },
  actions: {
  },
  modules: {
  }
})
