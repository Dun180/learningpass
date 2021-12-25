<template>
  <el-container>
    <div class="container">
      <Sidebar></Sidebar>
      <el-main style="padding-top: 0">
        <div class="content">
          <el-row style="line-height: 60px">
            <el-col :span="4">
              <h5 style="text-align: center;margin-left: 40px">我的作业</h5>
            </el-col>
            <el-col :span="16"></el-col>
            <el-col :span="4">
              <router-link :to="{name:'TaskEdit'}">
                <el-button type="primary" round>添加作业</el-button>
              </router-link>
            </el-col>
          </el-row>
          <el-row style="line-height: 40px">
            <div class="line"></div>
          </el-row>
          <el-row
              style="line-height: 60px;margin-top: 20px"
              v-for="task in tasks"
          >
            <el-col>
              <router-link :to="{name: 'TaskEdit',params: {taskId: task.id}}">

                <el-card :body-style="{ padding: '0px' }">
                  <div style="padding: 14px">
                    <span style="margin-right: 20px">作业标题: {{task.title}}</span>
                    <span style="margin-right: 20px">创建时间: {{task.createTime}}</span>
                  </div>
                </el-card>
              </router-link>
            </el-col>
          </el-row>
          <el-pagination class="mpage"
                         layout="prev, pager, next"
                         :current-page="currentPage"
                         :page-size="pageSize"
                         :total="total"
                         @current-change=page
                         style="line-height: 30px;margin-top: 20px"
          >
          </el-pagination>
        </div>

      </el-main>
    </div>
  </el-container>

</template>

<script>
import Sidebar from "@/components/Sidebar";
import {ElMessage} from "element-plus";

export default {
  name: "TaskList",
  components:{
    Sidebar
  },
  //inject:['reload'],                                 //注入App里的reload方法
  data(){
    return {

      userId:'',

      tasks:{},
      //分页数据
      currentPage: 1,
      total: 0,
      pageSize: 6,


    }
  },
  methods:{

    //分页
    async page(currentPage) {
      const _this = this
      const data = await _this.$api.getTasksByTeacherId(_this.userId,currentPage)
      console.log(data.records)
      _this.tasks = data.records
      _this.currentPage = data.current
      _this.total = data.total
      _this.pageSize = data.size


    }
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.userId = this.$store.getters.getUser.id
    }
    this.page(1)
  },
}
</script>

<style scoped>
.container{
  width: 1200px;
  margin: 0 auto;
}
.image{
  height: 130px;
  width: 240px;
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
.mpage{
  margin: 0 auto;
  text-align: center;
}
</style>