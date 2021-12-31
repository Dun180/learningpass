<template>
  <el-container>
    <div class="container">
      <Sidebar></Sidebar>
      <el-main style="padding-top: 0">
        <div class="content">
          <el-row style="line-height: 60px">
            <el-col :span="24">
              <h5 style="text-align: center;">成绩查询</h5>
            </el-col>
          </el-row>
          <el-row style="line-height: 40px">
            <div class="line"></div>
          </el-row>
          <el-row>
            <el-card class="box-card" style="width: 960px">
              <el-form ref="form" :model="selectInfo" label-width="120px">
                <el-form-item>
                  <el-row style="line-height: 60px">
                    <el-col :span="18">
                      <el-select v-model="selectInfo.select" placeholder="请选择查询条件">
                        <el-option label="普通作业" value="0"></el-option>
                        <el-option label="互评作业" value="1"></el-option>
                      </el-select>
                    </el-col>
                    <el-col :span="6">
                      <el-button type="primary" @click="onSelect">查询</el-button>
                    </el-col>
                  </el-row>

                </el-form-item>
              </el-form>

              <el-table :data="scoreList" style="width: 100%">
                <el-table-column prop="taskTitle" label="作业名" width="240" />
                <el-table-column prop="endTime" label="结束时间" width="480" />
                <el-table-column prop="score" label="分数" width="180"/>
<!--                <el-table-column prop="rank" label="排名" />-->
              </el-table>
            </el-card>
          </el-row>




        </div>

      </el-main>
    </div>
  </el-container>
</template>

<script>
import Sidebar from "@/components/Sidebar";

export default {
  name: "ScoreQuery",
  components:{
    Sidebar
  },
  data(){
    return{
      selectInfo:{
        select:'0',
        studentId:'',
      },
      scoreList:[],
    }
  },
  methods:{
    async onSelect(){
      const resp = await this.$api.scoreQuery(this.selectInfo)
      this.scoreList = resp
    },
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.selectInfo.studentId = this.$store.getters.getUser.id

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
  line-height: 60px;
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