<template>
  <div class="container">
    <div style="margin-top: 20px"></div>
    <el-card shadow="always" style="margin: 0 auto;width: 200px"> {{ classData.name }} </el-card>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="班级信息" name="first">
        <el-card class="box-card" >
          <template #header>
            <div class="card-header">
              <span>班级信息</span>
              <el-button class="button" type="text" @click="dialogTableVisible = true">修改班级信息</el-button>
            </div>
          </template>
          <div class="text item">班级名：{{classData.name}}</div>
          <div class="text item">学期：{{classData.semester}}</div>
          <div class="text item">班级代码：{{classData.code}}</div>
        </el-card>
      </el-tab-pane>
      <el-tab-pane label="管理" name="second"><ClassManage :classId="classData.id"></ClassManage></el-tab-pane>
      <el-tab-pane label="分组" name="third"><GroupManage :classId="classData.id"/></el-tab-pane>
      <el-tab-pane label="作业" name="fourth"><TaskManage :classId="classData.id"/></el-tab-pane>
      <el-tab-pane label="互评" name="fifth"></el-tab-pane>
    </el-tabs>
  </div>



  <!-- 修改班级弹框 -->
  <el-dialog
      title="修改班级"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 修改班级的表单 -->
    <el-form ref="addFormRef" :rules="rulesModifyClass" :model="modifyClass" label-width="100px">
      <el-form-item v-if="false" prop="id" label="教师ID">
        <el-input v-model="modifyClass.id"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="班级名" >
        <el-input v-model="modifyClass.name"></el-input>
      </el-form-item>
      <el-form-item prop="semester" label="学期">
        <el-input v-model="modifyClass.semester"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onModifyClass">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import ClassManage from "@/views/teacher/ClassManage";
import {ElMessage} from "element-plus";
import GroupManage from "@/views/teacher/GroupManage";
import TaskManage from "@/views/teacher/TaskManage";

export default {
  name: "ClassController",
  components:{
    ClassManage,
    GroupManage,
    TaskManage,
  },
  data() {
    return {
      activeName: 'first',
      classData:{
        id:'',
        name:'',
        semester:'',
        code:'',
      },
      dialogTableVisible: false, // 添加修改班级弹框
      // 修改班级
      modifyClass: {
        id: '',
        semester: '',
        name: '',
      },
      // 验证规则
      rulesModifyClass: {
        name: [
          { required: true, message: '请输入班级名', trigger: 'blur' }
        ],
        semester: [
          { required: true, message: '请输入学期', trigger: 'blur' }
        ]
      },
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event)
    },
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加班级
    onModifyClass() {
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.addClass)
          const resp = await _this.$api.modifyClassInfo(_this.modifyClass);

          //console.log(response.data)
          if (resp) {
            console.log('修改成功')
            ElMessage({
              message: '修改成功',
              type: 'success',
            })
            _this.dialogTableVisible = false  // 关闭弹框
            _this.$refs.addFormRef.resetFields() // 清空表单
            _this.$store.commit('increment')//刷新页面
          } else {
            ElMessage.error('修改失败')
          }


        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
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
        _this.classData.code = data.code
        _this.modifyClass.id = data.id
        _this.modifyClass.name = data.name
        _this.modifyClass.semester = data.semester
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 480px;
  margin: 0 auto;
}

</style>