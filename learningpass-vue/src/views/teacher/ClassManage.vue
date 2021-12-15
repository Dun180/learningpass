<template>
  <el-tabs tab-position="left"  @tab-click="handleClick" v-model="activeName">
    <el-tab-pane label="成员" name="member">
<!--      <el-row-->
<!--          style="line-height: 60px;margin-top: 20px"-->
<!--          v-for="member in members"-->
<!--      >-->
<!--        <el-col>-->
<!--          <el-card :body-style="{ padding: '0px' }">-->
<!--            <div style="padding: 14px">-->
<!--              <span>{{member.name}}</span>-->
<!--            </div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--      </el-row>-->

      <el-button class="button" type="primary" @click="dialogTableVisible = true" style="float: right">添加学生</el-button>

      <el-table :data="members" style="width: 100%">
        <el-table-column prop="username" label="Number" width="180" />
        <el-table-column prop="name" label="Name" width="180" />
        <el-table-column prop="identity" label="identity" />
        <el-table-column label="Operations">
          <template #default="scope">
            <el-button
                size="mini"
                type="danger"
            >Delete</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination class="mpage"
                     layout="prev, pager, next"
                     :current-page="currentPage"
                     :page-size="pageSize"
                     :total="total"
                     @current-change=page
                     style="line-height: 30px;margin-top: 20px"
      >
      </el-pagination>
    </el-tab-pane>
    <el-tab-pane label="Config" name="second">Config</el-tab-pane>
    <el-tab-pane label="Role" name="third">Role</el-tab-pane>
    <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
  </el-tabs>

  <!-- 添加学生弹框 -->
  <el-dialog
      title="添加学生"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 添加学生的表单 -->
    <el-form ref="addFormRef" :rules="rulesAddStudent" :model="addStudent" label-width="100px">
      <el-form-item v-if="false" prop="classId" label="班级ID">
        <el-input v-model="addStudent.classId"></el-input>
      </el-form-item>
      <el-form-item prop="username" label="学号">
        <el-input v-model="addStudent.username"></el-input>
      </el-form-item>
      <el-form-item prop="name" label="姓名">
        <el-input v-model="addStudent.name"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onAddStudent">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>


</template>

import {API} from "@/lib/api";

<script>
import {ElMessage} from "element-plus";

export default {
  name: "ClassManage",
  props:{
    classId:'',
  },
  data(){
    return{
      activeName: 'member',
      members:[],
      //分页数据
      currentPage: 1,
      total: 0,
      pageSize: 15,

      //弹出框管理
      dialogTableVisible:false,

      //添加学生
      addStudent:{
        classId:'',
        username:'',
        name:'',
      },

      // 验证规则
      rulesAddStudent: {
        username: [
          { required: true, message: '请输入学号', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' }
        ]
      },
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(this.classId)
      switch (this.activeName){
        case "member":

          break
        default:
          break
      }
    },
    //分页
    async page(currentPage) {
      const _this = this
      const data = await _this.$api.getClassMemberById(_this.classId,currentPage)

      console.log(data.records)
      console.log(typeof(data.records))

      _this.members = data.records
      _this.currentPage = data.current
      _this.total = data.total
      _this.pageSize = data.size


    },
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加班级
    onAddStudent() {
      this.addStudent.classId = this.classId
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.addStudent)
          const resp = await _this.$api.addStudentToClass(_this.addStudent);

          //console.log(response.data)
          if (resp) {
            console.log('添加成功')
            ElMessage({
              message: '添加成功',
              type: 'success',
            })
            _this.dialogTableVisible = false  // 关闭弹框
            _this.$refs.addFormRef.resetFields() // 清空表单
            _this.$store.commit('increment')//刷新页面
          } else {
            ElMessage.error('添加失败')
          }


        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
    },
  },
  watch: {

    classId:async function (indexVal,oldVal){

      await this.page(1)

    }

  },
}
</script>

<style scoped>
.mpage{
  margin: 0 auto;
  text-align: center;
}
</style>