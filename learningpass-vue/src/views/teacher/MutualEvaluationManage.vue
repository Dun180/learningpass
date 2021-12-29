<template>
  <el-row>
    <el-col :span="7">
      <el-card style="height: 600px;">
        <template #header>
          <div class="card-header">
            <el-row>
              <el-col >
                <span style="line-height: 28px;text-align: center">已发布的互评</span>
              </el-col>

            </el-row>
          </div>
        </template>
        <el-scrollbar height="480px">
          <el-row
              style="line-height: 60px;margin-top: 20px"
              v-for="task in mutualEvaluationList"
          >
            <el-col>
              <el-card
                  :body-style="{ padding: '0px' }"
                  @click="handleClick(task.template.id,task.taskTitle)"
                  style="cursor:pointer"

              >
                <div style="padding: 14px">
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">{{task.taskTitle}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">评分方式：{{task.template.gradeMode}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">分值比例：{{task.template.scoreDistribution}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">开始时间：{{task.template.beginTime}}</span>
                  </el-row>
                  <el-row style="line-height: 20px;">
                    <span style="margin: 0 auto">结束时间：{{task.template.endTime}}</span>
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
        <el-scrollbar height="480px">

          <el-table :data="this.mutualEvaluationCompletion" style="width: 100%">
            <el-table-column prop="studentNumber" label="学号" width="180" />
            <el-table-column prop="studentName" label="姓名" width="180" />
            <el-table-column prop="mutualEvaluationCompletion" label="完成情况" />
            <el-table-column label="Operations">
              <template #default="scope">
                  <el-button
                      size="mini"
                      type="primary"
                      @click=""
                  >批阅</el-button
                  >
              </template>
            </el-table-column>
          </el-table>
        </el-scrollbar>
      </el-card>
    </el-col>
  </el-row>


</template>

<script>
import { defineComponent, reactive, toRefs } from 'vue'
import {ElMessage} from "element-plus";
export default {
  name: "MutualEvaluationManage",
  props:{
    classId:'',
  },
  data(){
    return{
      mutualEvaluationList:[],

      currentTemplateId:'',
      taskTitle:'',
      mutualEvaluationCompletion:[],
    }
  },
  methods: {
    //点击分组
    handleClick(value1,value2) {
      this.currentTemplateId = value1
      this.taskTitle = value2
    },


    async getMutualEvaluationList(){
      const resp = await this.$api.getMutualEvaluationList(this.classId)
      this.mutualEvaluationList = resp
      console.log(this.mutualEvaluationList)
      for (let i = 0; i < this.mutualEvaluationList.length; i++) {
        switch (this.mutualEvaluationList[i].template.gradeMode){
          case 0:
            this.mutualEvaluationList[i].template.gradeMode = "总分误差"
                break;
          case 1:
            this.mutualEvaluationList[i].template.gradeMode = "分项误差"
                break;
          case 2:
            this.mutualEvaluationList[i].template.gradeMode = "分项标准差"
                break;
          default:
              break;
        }
      }

    },
    async getMutualEvaluationCompletion(){
      const resp = await this.$api.getMutualEvaluationCompletion(this.currentTemplateId)
      this.mutualEvaluationCompletion = resp
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

      await this.getMutualEvaluationList()

    },
    currentTemplateId:async function (indexVal,oldVal){

      await this.getMutualEvaluationCompletion()
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