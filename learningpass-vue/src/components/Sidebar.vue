<template>
  <el-aside width="200px" style="float: left">

    <div class="personalInfo" >
      <el-avatar :size="100" style="margin-top: 15px"
                 src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
      ></el-avatar>
      <div class="space_nickname">
        <div class="personalName">{{ user.name }}</div>
        <el-button size="medium" class="manageBtn" type="warning" round>账号管理</el-button>
      </div>
    </div>



    <el-menu
        active-text-color="#ffd04b"
        background-color="#545c64"
        class="el-menu-vertical-demo"
        :default-active="$route.path"
        text-color="#fff"

        :router="true"
    >


      <el-menu-item v-if="flag" index="/teacher">
        <span>班级管理</span>
      </el-menu-item>
      <el-menu-item v-if="flag" index="/teacher/taskList">
        <span>作业管理</span>
      </el-menu-item>
      <el-menu-item v-if="!flag" index="/student">
        <span>我的班级</span>
      </el-menu-item>
      <el-menu-item v-if="!flag" index="/student/taskList">
        <span>作业任务</span>
      </el-menu-item>
      <el-menu-item v-if="!flag" index="/student/mutualEvaluationList">
        <span>互评任务</span>
      </el-menu-item>
      <el-menu-item v-if="!flag" index="/student/scoreQuery">
        <span>成绩查询</span>
      </el-menu-item>

<!--      <el-menu-item index="2">-->
<!--        <span>Navigator Two</span>-->
<!--      </el-menu-item>-->
<!--      <el-menu-item index="3" disabled>-->
<!--        <span>Navigator Three</span>-->
<!--      </el-menu-item>-->
    </el-menu>




  </el-aside>
</template>

<script>
export default {
  name: "Sidebar",
  data(){
    return {
      user: {
        name: '请先登录',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'
      },
      flag:'',

    }
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.user.name = this.$store.getters.getUser.name
      this.user.avatar = this.$store.getters.getUser.avatar
      console.log(this.$store.getters.getUser.identity)
      if (this.$store.getters.getUser.identity == "Teacher"){
        this.flag = true
      }else{
        this.flag = false
      }
    }
  },
}
</script>

<style scoped>
.personalInfo {
  height: 200px;
  width: 100%;
  line-height: normal;
  margin-bottom: 20px;
}

.space_nickname{
  height: 70px;
  width: 100%;

}
.personalName{
  font-size: 20px;
  line-height: 50px;
}
</style>