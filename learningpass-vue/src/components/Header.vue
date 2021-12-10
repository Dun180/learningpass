<template>
  <el-container>
    <el-header>Header
      <el-dropdown>
        <el-button type="primary" style="padding-top: 0">
          <el-avatar :size="25" style="position: relative;top: 6px"
                     src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          ></el-avatar>
          {{ user.username }}
          <el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>进入空间</el-dropdown-item>
            <el-dropdown-item>退出登录</el-dropdown-item>
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
        username: '请先登录',
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
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.user.username = this.$store.getters.getUser.username
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