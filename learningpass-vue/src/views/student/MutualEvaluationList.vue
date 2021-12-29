<template>
  <el-container>
    <div class="container">
      <Sidebar></Sidebar>
      <el-main style="padding-top: 0">
        <div class="content">
          <el-row style="line-height: 60px">
            <el-col :span="24">
              <h5 style="text-align: center;">我的作业</h5>
            </el-col>
          </el-row>
          <el-row style="line-height: 40px">
            <div class="line"></div>
          </el-row>

          <el-scrollbar height="750px">
            <el-row
                style="line-height: 60px;margin-top: 20px"
                v-for="task in mutualEvaluationList"
            >
              <el-col>
                <router-link :to="{name: 'Evaluate',params: {templateId: task.template.id}}">

                  <el-card :body-style="{ padding: '0px' }">
                    <div style="padding: 14px">
                      <el-row style="line-height: 20px;">
                        <span style="margin: 0 auto">{{task.taskTitle}}</span>
                      </el-row>
                      <el-row style="line-height: 20px;">
                        <span style="margin: 0 auto">开始时间：{{task.template.beginTime}}</span>
                      </el-row>
                      <el-row style="line-height: 20px;">
                        <span style="margin: 0 auto">结束时间：{{task.template.endTime}}</span>
                      </el-row>

                    </div>
                  </el-card>
                </router-link>
              </el-col>
            </el-row>
          </el-scrollbar>



        </div>

      </el-main>
    </div>
  </el-container>
</template>

<script>
import Sidebar from "@/components/Sidebar";

export default {
  name: "MutualEvaluationList",
  components:{
    Sidebar
  },
  data(){
    return {

      userId:'',
      mutualEvaluationList:{},

    }
  },
  methods:{
    async getMutualEvaluationListByStudentId(){
      const resp = await this.$api.getMutualEvaluationListByStudentId(this.userId)
      this.mutualEvaluationList = resp
    }
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.userId = this.$store.getters.getUser.id

      this.getMutualEvaluationListByStudentId()
    }


  }
}
</script>

<style scoped>
.container{
  width: 1200px;
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
.personalInfo {
  height: 200px;
  width: 100%;

}

.space_nickname{
  height: 70px;
  width: 100%;
  line-height: 70px;
  position: relative;
  bottom: 110px;
}
.personalName{
  font-size: 20px;
}
.space_nickname .manageBtn{
  position: relative;
  bottom: 35px;
}
.box-card .item{
  width: 100%;
  height: 50px;
  margin-bottom: 20px;
}
.el-card >>> .el-card__body {
  height: 100%;
}
.content .line{
  width: 100%;
  height: 0;
  border-top: 1px solid var(--el-border-color-base);
}
</style>