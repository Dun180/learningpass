<template>
  <div class="container">
    <div class="m-content">

      <el-form :model="grade" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <span style="font-size: 30px">{{this.taskTitle}}</span>
        </el-form-item>
        <div class="line"></div>


        <div v-for="(item, index) in this.evaluate.answerDtoList" :key="index">


          <el-form-item
              :label="'题目'+(index+1)"
          >
            <v-md-preview :text="item.stem"></v-md-preview>
          </el-form-item>
          <el-form-item
              label="答案"
          >
            <v-md-preview :text="item.answer"></v-md-preview>
          </el-form-item>
          <el-form-item label="分值" >
            <span style="margin-left: 32px">{{item.score}}</span>
          </el-form-item>
          <el-form-item label="得分">
            <span style="margin-left: 32px">{{item.actualScore}}</span>
          </el-form-item>
          <el-form-item label="评价">
            <span style="margin-left: 32px">{{item.evaluation}}</span>

          </el-form-item>
          <div class="line"></div>
        </div>

        <el-form-item label="互评质量打分(满分100)" style="margin-top: 20px">
          <el-input style="margin-left: 32px" v-model="this.grade.evaluationQuality" placeholder="请输入互评质量"/>
        </el-form-item>
        <el-form-item style="margin-top: 20px">
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>


      </el-form>


    </div>
  </div>
</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "GradeEvaluate",
  data(){
    return{
      templateId: '',
      userId:'',

      taskTitle:'',
      evaluate:{
        answerDtoList:'',
        templateId:'',
        answerId:'',

      },

      grade:{
        templateId: '',
        userId:'',
        evaluationQuality:'',
      },
      rules: {

      }
    }
  },
  methods:{
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.grade.templateId = this.templateId
          this.grade.userId = this.userId
          console.log("submit")
          //axios异步向后端请求数据验证

          const resp = await this.$api.submitGradeEvaluate(this.grade);

          //console.log(response.data)
          if(resp){
            console.log('提交成功')
            ElMessage({
              message: '提交成功',
              type: 'success',
            })
            await this.$router.push("/teacher")

          }else{
            ElMessage.error('提交失败')
          }

        } else {
          ElMessage.error('提交失败')
          return false;
        }
      });
    },
    async getMutualEvaluationInfo(){
      const resp = await this.$api.getMutualEvaluationInfo(this.userId,this.templateId)
      console.log(resp)
      this.evaluate.answerDtoList = resp.answerDtoList
      this.evaluate.answerId = resp.answerId
      this.grade.evaluationQuality = resp.evaluationQuality
      this.taskTitle = resp.title
    }
  },
  created() {
    this.templateId = this.$route.params.templateId
    this.userId = this.$route.params.studentId

    this.getMutualEvaluationInfo()

  },
}
</script>

<style scoped>
.m-content{
  text-align: left;
  margin-top: 40px;
}
.container{
  width: 1200px;
  margin: 0 auto;
}
.line{
  width: 100%;
  height: 0;
  border-top: 1px solid var(--el-border-color-base);
}
</style>