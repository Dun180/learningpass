<template>
  <div class="container">
    <div class="m-content">
      <router-link :to="{name: 'MutualEvaluationList'}">

        <el-page-header :icon="this.ArrowLeft" content="" style="margin-bottom: 20px"/>
      </router-link>

      <el-form :model="evaluate" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <span style="font-size: 30px">{{this.taskTitle}}</span>
        </el-form-item>
        <div class="line"></div>


        <div v-for="(item, index) in evaluate.answerDtoList" :key="index">


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
            <el-input style="margin-left: 32px" v-model="item.actualScore" placeholder="请输入该题得分"/>
          </el-form-item>
          <el-form-item label="评价">
            <el-input style="margin-left: 32px" v-model="item.evaluation" placeholder="请输入评价"/>
          </el-form-item>
          <div class="line"></div>
        </div>


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
  name: "Evaluate",
  data(){
    return{
      templateId:'',
      userId:'',
      taskTitle:'',
      evaluate:{
        answerDtoList:'',
        templateId:'',
        answerId:'',
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
          this.evaluate.templateId = this.templateId
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.evaluate)
          const resp = await this.$api.submitEvaluation(this.evaluate);

          //console.log(response.data)
          if(resp){
            console.log('提交成功')
            ElMessage({
              message: '提交成功',
              type: 'success',
            })
            await this.$router.push("/student/mutualEvaluationList")

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
      this.taskTitle = resp.title
    }
  },
  created() {
    this.templateId = this.$route.params.templateId
    if(this.$store.getters.getUser){
      this.userId = this.$store.getters.getUser.id
      this.getMutualEvaluationInfo()
    }
  },
  setup(){
    const ArrowLeft = require('@element-plus/icons/lib/ArrowLeft');
  }
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