<template>
  <div class="container">
    <div class="m-content">
      <router-link :to="{name: 'StudentTaskList'}">

        <el-page-header :icon="this.ArrowLeft" content="" style="margin-bottom: 20px"/>
      </router-link>

      <el-form :model="answer" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
        <el-form-item>
          <span style="font-size: 30px">{{this.taskTitle}}</span>
        </el-form-item>


        <div v-for="(item, index) in answer.answerDtoList" :key="index">


          <el-form-item
              :label="'题目'+(index+1)"
          >
            <v-md-preview :text="item.stem"></v-md-preview>
          </el-form-item>
          <el-form-item
              label="答案"
          >
            <v-md-editor
                v-model="item.answer"
                height="400px"
                :disabled-menus="[]"
                @upload-image="handleUploadImage"
            ></v-md-editor>
          </el-form-item>
        </div>


        <el-form-item>
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
  name: "Answer",
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
          const resp = await this.$api.submitAnswer(this.answer);

          //console.log(response.data)
          if(resp){
            console.log('提交成功')
            ElMessage({
              message: '提交成功',
              type: 'success',
            })
            await this.$router.push("/student/taskList")

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
    },
    async handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      console.log(files);
      let file = files[0]
      let param = new FormData()  // 创建form对象
      param.append('file', file)  // 通过append向form对象添加数据
      param.append('chunk', '0') // 添加form表单中其他数据
      console.log(param.get('file')) // FormData私有类对象，访问不到，可以通过get判断值是否传进去
      let config = {
        headers: {'Content-Type': 'multipart/form-data'}
      }
      const resp = await this.$axios.post("http://localhost:8081/upload",param,config)
      console.log(resp.data.data)
      // 此处只做示例
      insertImage({
        url:
            'http://localhost:8081/viewPhoto/'+resp.data.data,
        desc: '图片',
        width: 'auto',
        height: 'auto',
      });
    },
  },
  created() {
    this.taskArrangementId = this.$route.params.taskArrangementId
    console.log("taskArrangementId:"+this.taskArrangementId)
    if(this.$store.getters.getUser){
      this.userId = this.$store.getters.getUser.id
      this.getTaskInfo()
    }
  },
  setup(){
    const ArrowLeft = require('@element-plus/icons/lib/ArrowLeft');
  }
}
</script>

<style scoped>
.m-content{
  text-align: center;
  margin-top: 40px;
}
.container{
  width: 1200px;
  margin: 0 auto;
}
</style>