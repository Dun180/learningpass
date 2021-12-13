<template>
  <div class="container">
    <el-card shadow="always" style="margin-top: 20px"> {{ classData.name }} </el-card>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="User" name="first">User</el-tab-pane>
      <el-tab-pane label="管理" name="second"><ClassManage :classId="classData.id"></ClassManage></el-tab-pane>
      <el-tab-pane label="Role" name="third">Role</el-tab-pane>
      <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ClassManage from "@/views/teacher/ClassManage";

export default {
  name: "ClassController",
  components:{
    ClassManage
  },
  data() {
    return {
      activeName: 'first',
      classData:{
        id:'',
        name:'',
        semester:'',
      }
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
  },
  async created() {
    const classId = this.$route.params.classId
    const _this = this
    if(classId){
      const data =await _this.$api.getClassById(classId);
        _this.classData.id = data.id
        _this.classData.name = data.name
        _this.classData.semester = data.semester
    }
  }
}

</script>

<style scoped>
.container{
  width: 1125px;
  margin: 0 auto;
}
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


</style>