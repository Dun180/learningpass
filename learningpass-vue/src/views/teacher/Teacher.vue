<template>
  <el-container>
    <div class="container">
      <el-aside width="200px" style="float: left">

        <div class="personalInfo">
            <el-avatar :size="100" style="margin-top: 15px"
                :src="user.avatar"
            ></el-avatar>
          <div class="space_nickname">
            <div class="personalName">{{ user.username }}</div>
            <el-button size="medium" class="manageBtn" type="warning" round>Warning</el-button>
          </div>
        </div>


            <h5 style="line-height: 50px;">Custom</h5>
            <el-menu
                active-text-color="#ffd04b"
                background-color="#545c64"
                class="el-menu-vertical-demo"
                default-active="2"
                text-color="#fff"
                @open="handleOpen"
                @close="handleClose"
            >
              <el-sub-menu index="1">
                <template #title>
                  <el-icon><location /></el-icon>
                  <span>Navigator One</span>
                </template>
                <el-menu-item-group title="Group One">
                  <el-menu-item index="1-1">item one</el-menu-item>
                  <el-menu-item index="1-2">item one</el-menu-item>
                </el-menu-item-group>
                <el-menu-item-group title="Group Two">
                  <el-menu-item index="1-3">item three</el-menu-item>
                </el-menu-item-group>
                <el-sub-menu index="1-4">
                  <template #title>item four</template>
                  <el-menu-item index="1-4-1">item one</el-menu-item>
                </el-sub-menu>
              </el-sub-menu>
              <el-menu-item index="2">
                <el-icon><icon-menu /></el-icon>
                <span>Navigator Two</span>
              </el-menu-item>
              <el-menu-item index="3" disabled>
                <el-icon><document /></el-icon>
                <span>Navigator Three</span>
              </el-menu-item>
              <el-menu-item index="4">
                <el-icon><setting /></el-icon>
                <span>Navigator Four</span>
              </el-menu-item>
            </el-menu>




      </el-aside>
      <el-main style="padding-top: 0">
        <div class="content">
          <el-row style="line-height: 60px">
            <el-col :span="4">
              <h5 style="text-align: center;margin-left: 40px">我教的班级</h5>
            </el-col>
            <el-col :span="16"></el-col>
            <el-col :span="4">
              <el-button type="primary" @click="dialogTableVisible = true" round>添加班级</el-button>
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
        </div>

      </el-main>
    </div>
  </el-container>

<!--  <el-dialog-->
<!--      v-model="dialogTableVisible"-->
<!--      title="Tips"-->
<!--      width="30%"-->

<!--  >-->
<!--    <span>This is a message</span>-->
<!--    <template #footer>-->
<!--      <span class="dialog-footer">-->
<!--        <el-button @click="dialogTableVisible = false">Cancel</el-button>-->
<!--        <el-button type="primary" @click="dialogTableVisible = false"-->
<!--        >Confirm</el-button-->
<!--        >-->
<!--      </span>-->
<!--    </template>-->
<!--  </el-dialog>-->
  <!-- 添加用户弹框 -->
  <el-dialog
      title="添加用户"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 添加用户的表单 -->
    <el-form ref="addFormRef" :rules="rulesAddClass" :model="addClass" label-width="100px">
      <el-form-item v-if="false" prop="teacherId" label="教师ID">
        <el-input v-model="addClass.teacherId"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="班级名">
        <el-input v-model="addClass.name"></el-input>
      </el-form-item>
      <el-form-item prop="semester" label="学期">
        <el-input v-model="addClass.semester"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onAddUser">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "Teacher",
  inject:['reload'],                                 //注入App里的reload方法
  data(){
    return {
      user: {
        username: '请先登录',
        avatar: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        userId:''
      },
      classes:{},
      dialogTableVisible: false, // 添加用户弹框
      // 添加班级
      addClass: {
        teacherId: '',
        semester: '',
        name: '',
      },
      // 验证规则
      rulesAddClass: {
        name: [
          { required: true, message: '请输入班级名', trigger: 'blur' }
        ],
        semester: [
          { required: true, message: '请输入学期', trigger: 'blur' }
        ]
      }
    }
  },
  methods:{
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加用户
    onAddUser() {
      this.addClass.teacherId = this.user.userId
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.addClass)
          _this.$axios.post('/teacher/class:',this.addClass).then(response => {
            //console.log(response.data)
            if(response.data.data.addResult){
              console.log('添加成功')
              ElMessage({
                message: '添加成功',
                type: 'success',
              })
              _this.dialogTableVisible = false  // 关闭弹框
              _this.$refs.addFormRef.resetFields() // 清空表单
              _this.$store.commit('increment')
            }else{
              ElMessage.error('添加失败')
            }

          })
        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
    },
  },
  created() {
    console.log(this.$store.getters.getUser)
    if(this.$store.getters.getUser){
      this.user.username = this.$store.getters.getUser.username
      this.user.userId = this.$store.getters.getUser.id
    }
    const _this = this
    _this.$axios.get("/teacher/classes/" + _this.user.userId).then(res =>{
      console.log(res)
      _this.classes = res.data.data.classes
      console.log(_this.classes)
    }).catch(error => {
      console.log("出错")
    })
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

</style>