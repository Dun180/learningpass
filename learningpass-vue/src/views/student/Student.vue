<template>
  <el-container>
    <div class="container">
      <Sidebar></Sidebar>
      <el-main style="padding-top: 0">
        <div class="content">
          <el-row style="line-height: 60px">
            <el-col :span="4">
              <h5 style="text-align: center;margin-left: 40px">我的班级</h5>
            </el-col>
            <el-col :span="16"></el-col>
            <el-col :span="4">
              <el-button type="primary" @click="dialogTableVisible = true" round>加入班级</el-button>
            </el-col>
          </el-row>
          <el-row style="line-height: 40px">
            <div class="line"></div>
          </el-row>
          <el-row
              style="line-height: 60px;margin-top: 20px"
              v-for="cClass in classes"
          >
            <el-col>
              <el-card :body-style="{ padding: '0px' }">
                <div style="padding: 14px">
                  <span style="margin-right: 20px">{{cClass.name}}</span>
                  <span>{{cClass.semester}}</span>
                </div>
              </el-card>
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


  <!-- 添加用户弹框 -->
  <el-dialog
      title="加入班级"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 添加用户的表单 -->
    <el-form ref="addFormRef" :rules="rulesAddClass" :model="addClass" label-width="100px">
      <el-form-item v-if="false" prop="studentId" label="学生ID">
        <el-input v-model="addClass.studentId"></el-input>
      </el-form-item>
      <el-form-item prop="code" label="班级代码">
        <el-input v-model="addClass.code"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onJoinClass">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {ElMessage} from "element-plus";
import Sidebar from "@/components/Sidebar";

export default {
  name: "Student",
  components:{
    Sidebar
  },
  data(){
    return {
      user: {
        name: '请先登录',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        userId:''
      },
      classes:{},
      dialogTableVisible: false, // 添加用户弹框
      // 添加班级
      addClass: {
        studentId: '',
        code: '',
      },
      // 验证规则
      rulesAddClass: {
        code: [
          { required: true, message: '请输入班级代码', trigger: 'blur' }
        ],
      },
      //分页数据
      currentPage: 1,
      total: 0,
      pageSize: 6
    }
  },
  methods:{
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击加入班级
    onJoinClass() {
      this.addClass.studentId = this.user.userId
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.addClass)
          const data = await _this.$api.joinClass(this.addClass)
            //console.log(response.data)
            if(data.addResult){
              console.log('加入成功')
              ElMessage({
                message: '加入成功',
                type: 'success',
              })
              _this.dialogTableVisible = false  // 关闭弹框
              _this.$refs.addFormRef.resetFields() // 清空表单
              _this.$store.commit('increment')
            }else{
              ElMessage.error('加入失败')
            }


        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
    },
    //分页
    async page(currentPage) {
      const _this = this
        const data = await _this.$api.getClassesByStudentId(_this.user.userId,currentPage)
        console.log(data)
        _this.classes = data.records
        _this.currentPage = data.current
        _this.total = data.total
        _this.pageSize = data.size

    }
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.user.name = this.$store.getters.getUser.name
      this.user.userId = this.$store.getters.getUser.id
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
.mpage{
  margin: 0 auto;
  text-align: center;
}
</style>