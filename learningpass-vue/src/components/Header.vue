<template>
  <el-container>
    <el-header>
      <div style="float: left">
        <img src="/img/dog.png" alt="" style="height: 40px;width: 40px;">
        <span style="font-size: 20px">学习通</span>
      </div>
      <el-dropdown>
        <el-button type="primary" style="padding-top: 0">
          <el-avatar :size="25" style="position: relative;top: 6px"
                     src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          ></el-avatar>
          {{ user.name }}
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <router-link :to="{name: 'Teacher'}">

              <el-dropdown-item>返回主页</el-dropdown-item>
            </router-link>
            <el-dropdown-item v-on:click="logout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>

  </el-container>
</template>


<script>
import { ArrowDown } from '@element-plus/icons'

export default {
  name: "Header",
  components:{
    ArrowDown
  },
  data(){
    return {
      user: {
        name: '请先登录',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      },
    }
  },
  setup() {
    const handleClick = () => {
      alert('button click')
    }
    return {
      handleClick,
    }
  },
  methods:{
    logout(){
      console.log("logout")
      this.$store.commit("REMOVE_INFO")
      this.$router.push("/login")
    }
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.user.name = this.$store.getters.getUser.name
      this.user.avatar = this.$store.getters.getUser.avatar

    }
  },
}
</script>

<style scoped>

.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.el-dropdown {
  float: right;
  margin-top: 10px;
}
</style>