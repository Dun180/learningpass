<template>
  <div class="container">
    <div class="m-content">


      <el-form :model="answer" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <span style="font-size: 30px">{{this.taskTitle}}</span>
        </el-form-item>
        <div class="line"></div>

        <!-- 动态增加项目 -->
        <div v-for="(item, index) in answer.answerDtoList" :key="index">


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
  name: "Grade",
  data(){
    return{
      taskArrangementId: '',
      userId:'',
      taskTitle:'',
      answer:{
        answerId:'',
        answerDtoList:'',
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

          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.answer)
          const resp = await this.$api.submitGrade(this.answer);

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
    async getTaskInfo(){
      const resp = await this.$api.getTaskInfo(this.userId,this.taskArrangementId)
      console.log(resp)
      this.answer.answerDtoList = resp.answerDtoList
      this.answer.answerId = resp.answerId
      this.taskTitle = resp.title
    }
  },
  created() {
    this.taskArrangementId = this.$route.params.taskArrangementId
    this.userId = this.$route.params.studentId
    console.log("taskArrangementId:"+this.taskArrangementId)

    this.getTaskInfo()

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