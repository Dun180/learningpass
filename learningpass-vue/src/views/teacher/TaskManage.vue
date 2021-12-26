<template>
  <el-card style="height: 600px;width: 800px;margin: 0 auto">
    <template #header>
      <div class="card-header">
        <el-row>
          <el-col :span="20">
            <span style="line-height: 28px;text-align: center">已布置的作业</span>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" size="mini" @click="dialogTableVisible = true" round>布置作业</el-button>
          </el-col>
        </el-row>
      </div>
    </template>
  </el-card>



  <!-- 布置作业弹框 -->
  <el-dialog
      title="作业布置"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 布置作业的表单 -->
    <el-form ref="addFormRef" :rules="rulesArrangement" :model="arrangementInfo" label-width="100px">
      <el-form-item>
        <el-select v-model="this.arrangementInfo.taskId" placeholder="Select">
          <el-option
              v-for="item in taskList"
              :key="item.id"
              :label="item.title"
              :value="item.id"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="开始/结束时间">
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
export default {
  name: "TaskManage",
  props:{
    classId:'',
  },
  data(){
    return{
      dialogTableVisible: false, // 添加布置作业弹框
      arrangementInfo:{
        taskId:'',
        time:'',

      },
      // 验证规则
      rulesArrangement: {
      },

      taskList:[],
    }
  },
  methods: {
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    onArrangement(){
      console.log(this.arrangementInfo)
    }
    ,
    async getAllTaskByTeacher(){
      if(this.$store.getters.getUser){
        const resp = await this.$api.getAllTaskByTeacherId(this.$store.getters.getUser.id)
        this.taskList = resp
      }

    }
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

    }

  },
}
</script>

<style scoped>

</style>