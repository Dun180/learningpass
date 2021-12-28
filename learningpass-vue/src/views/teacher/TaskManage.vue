<template>
  <el-row>
    <el-col :span="7">
      <el-card style="height: 600px;">
        <template #header>
          <div class="card-header">
            <el-row>
              <el-col :span="16">
                <span style="line-height: 28px;text-align: center">已布置的作业</span>
              </el-col>
              <el-col :span="8">
                <el-button type="primary" size="mini" @click="dialogTableVisible = true" round>布置作业</el-button>
              </el-col>
            </el-row>
          </div>
        </template>
        <el-scrollbar height="480px">
          <el-row
              style="line-height: 60px;margin-top: 20px"
              v-for="task in arrangementList"
          >
            <el-col>
              <el-card
                  :body-style="{ padding: '0px' }"
                  @click=""

              >
                <div style="padding: 14px">
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">{{task.taskTitle}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">开始时间：{{task.beginTime}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">结束时间：{{task.endTime}}</span>
                  </el-row>

                </div>
              </el-card>

            </el-col>
          </el-row>
        </el-scrollbar>
      </el-card>
    </el-col>

    <el-col :span="1"></el-col>
    <el-col :span="16">
      <el-card style="height: 600px;">
        <template #header>
          <div class="card-header" style="height: 28px">
            <span>{{this.taskTitle}}</span>

          </div>
        </template>
<!--        <el-table :data="this.groupMemberList" style="width: 100%">-->
<!--          <el-table-column prop="username" label="学号" width="180" />-->
<!--          <el-table-column prop="name" label="姓名" width="180" />-->
<!--          <el-table-column prop="identity" label="身份" />-->
<!--          <el-table-column label="Operations">-->
<!--            <template #default="scope">-->
<!--              <el-button-->
<!--                  size="mini"-->
<!--                  type="danger"-->
<!--                  @click=""-->
<!--              >删除</el-button-->
<!--              >-->
<!--              <el-button-->
<!--                  size="mini"-->
<!--                  type="warning"-->
<!--                  @click=""-->
<!--              >重置密码</el-button-->
<!--              >-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
      </el-card>
    </el-col>

  </el-row>


  <!-- 布置作业弹框 -->
  <el-dialog
      title="作业布置"
      @close="addDialogClose"
      v-model="dialogTableVisible"
      width="40%"
      :close-on-click-modal="false"
  >
    <!-- 布置作业的表单 -->
    <el-form ref="addFormRef" :rules="rulesArrangement" :model="arrangementInfo" label-width="120px">
      <el-form-item label="选择作业" prop="taskId">
        <el-select v-model="this.arrangementInfo.taskId" placeholder="Select" >
          <el-option
              v-for="item in taskList"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="布置方式">
        <el-radio-group v-model="this.arrangementInfo.mode">
          <el-radio label="1">学生布置</el-radio>
          <el-radio label="2">小组布置</el-radio>
          <el-radio label="3">抢答模式</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="开始/结束时间" prop="time">
        <el-date-picker
            v-model="this.arrangementInfo.time"
            type="datetimerange"
            :shortcuts="shortcuts"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
        >
        </el-date-picker>
      </el-form-item>


      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onArrangement">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
import { defineComponent, reactive, toRefs } from 'vue'
import {ElMessage} from "element-plus";
export default {
  name: "TaskManage",
  props:{
    classId:'',
  },
  data(){
    return{
      dialogTableVisible: false, // 添加布置作业弹框
      arrangementList:[],
      arrangementInfo:{
        classId:'',
        taskId:'',
        mode:'1',
        time:'',

      },
      // 验证规则
      rulesArrangement: {
        taskId: [
          { required: true, message: '请选择作业', trigger: 'blur' }
        ],
        time: [
          { required: true, message: '请选择开始/结束时间', trigger: 'blur' }
        ],
      },

      taskList:[],

      currentTaskId:'',
      taskTitle:'',

    }
  },
  methods: {
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    onArrangement(){

      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          this.arrangementInfo.classId = this.classId
          console.log(this.arrangementInfo)
          //axios异步向后端提交数据
          const resp = await this.$api.taskArrangement(this.arrangementInfo)
          if (resp){

            ElMessage({
              message: '创建成功',
              type: 'success',
            })
            this.dialogTableVisible = false  // 关闭弹框
            this.$refs.addFormRef.resetFields() // 清空表单
            this.$store.commit('increment')//刷新页面
          }else {
            ElMessage.error('创建失败')

          }

        } else {
          ElMessage.error('创建失败')
          return false
        }

      })
    }
    ,
    async getAllTaskByTeacher(){
      if(this.$store.getters.getUser){
        const resp = await this.$api.getAllTaskByTeacherId(this.$store.getters.getUser.id)
        this.taskList = resp
      }

    },
    async getTaskArrangementList(){
      const resp = await this.$api.getTaskArrangementList(this.classId)
      this.arrangementList = resp
    },

  },
  setup() {
    const state = reactive({
      shortcuts: [
        {
          text: 'Next 3 days',
          value: () => {
            const end = new Date()
            const start = new Date()
            end.setTime(start.getTime() + 3600 * 1000 * 24 * 3)
            return [start, end]
          },
        },
        {
          text: 'Next week',
          value: () => {
            const end = new Date()
            const start = new Date()
            end.setTime(start.getTime() + 3600 * 1000 * 24 * 7)
            return [start, end]
          },
        },
        {
          text: 'Next month',
          value: () => {
            const end = new Date()
            const start = new Date()
            end.setTime(start.getTime() + 3600 * 1000 * 24 * 30)
            return [start, end]
          },
        },
      ],
      value: '',
    })
    return toRefs(state)
  },
  watch: {

    classId:async function (indexVal,oldVal){

      await this.getAllTaskByTeacher()
      await this.getTaskArrangementList()

    },
    currentTaskId:async function (indexVal,oldVal){


    },

  },
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>