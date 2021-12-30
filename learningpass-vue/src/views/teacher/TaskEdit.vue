<template>
  <div class="container">
    <div class="m-content">
      <router-link :to="{name: 'TaskList'}">

      <el-page-header :icon="this.ArrowLeft" content="" style="margin-bottom: 20px"/>
      </router-link>
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

        <el-form-item label="标题" prop="title">
          <el-input v-model="ruleForm.title"></el-input>
        </el-form-item>

        <el-form-item>
            <el-tag style="margin-right: 8px;font-size: 15px;float: left">题目1</el-tag>
        </el-form-item>


        <el-form-item label="分数" prop="score">
          <el-input v-model="ruleForm.score"></el-input>
        </el-form-item>

        <el-form-item label="题干" prop="stem">
          <v-md-editor
              v-model="ruleForm.stem"
              height="400px"
              :disabled-menus="[]"
              @upload-image="handleUploadImage"
          ></v-md-editor>
        </el-form-item>



        <!-- 动态增加项目 -->
        <div v-for="(item, index) in ruleForm.dynamicItem" :key="index">
          <el-form-item>
            <div style="float: left">
              <el-tag style="margin-right: 8px;font-size: 15px">题目{{index+2}}</el-tag>
              <el-button type="text" @click="deleteItem(item, index)">删除</el-button>
            </div>

          </el-form-item>

          <el-form-item
              label="分数"
              :prop="'dynamicItem.'+index+'.score'"
              :rules="{ required: true, message: '请填写分数', trigger: 'blur' }"
          >
            <el-input v-model="item.score"></el-input>
          </el-form-item>

          <el-form-item
              label="题干"
              :prop="'dynamicItem.'+index+'.stem'"
              :rules="{ required: true, message: '请填写题干', trigger: 'blur' }"
          >
            <v-md-editor
                v-model="item.stem"
                height="400px"
                :disabled-menus="[]"
                @upload-image="handleUploadImage"
            ></v-md-editor>
          </el-form-item>

        </div>


        <el-form-item>
          <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
          <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button type="primary" @click="addItem" style="float: right">添加题目</el-button>
        </el-form-item>


      </el-form>


    </div>
  </div>
</template>

<script>

import {ElMessage} from "element-plus";

export default {
  name: "TaskEdit",

  data() {
    return {
      ruleForm: {
        taskId:'',
        creatorId:'',
        title: '',
        questionId:'',
        score: '',
        stem: '',
        dynamicItem: []
      },
      rules: {
        title: [
          { required: true, message: '请输入标题', trigger: 'blur' },
          { min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur' }
        ],
        score: [
          { required: true, message: '请填写分数', trigger: 'blur' }
        ],
        stem: [
          { required: true, message: '请填写题干', trigger: 'blur' }
        ],
      }
    };
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          let sum = 0;
          sum+=parseInt(this.ruleForm.score);
          if (this.ruleForm.dynamicItem!=undefined&&this.ruleForm.dynamicItem.length!=0){
            for (let i = 0; i < this.ruleForm.dynamicItem.length; i++) {
              sum+=parseInt(this.ruleForm.dynamicItem[i].score);
              console.log("score"+this.ruleForm.dynamicItem[i].score)
            }
          }
          console.log("总分"+sum)
          if (sum!=100){
            ElMessage.error('总分不为100')
            return false;
          }else{
            console.log("form input"+this.ruleForm)
            this.ruleForm.creatorId = this.$store.getters.getUser.id
            const _this = this
            console.log("submit")
            //axios异步向后端请求数据验证

            const resp = await _this.$api.createOrUpdateTask(_this.ruleForm);

            //console.log(response.data)
            if(resp){
              console.log('更新成功')
              ElMessage({
                message: '更新成功',
                type: 'success',
              })
              this.$router.push("/teacher/taskList")

            }else{
              ElMessage.error('添加失败')
            }
          }
        } else {
          ElMessage.error('提交失败')
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    addItem () {
      this.ruleForm.dynamicItem.push({
        questionId:'',
        score: '',
        stem: ''
      })
    },
    deleteItem (item, index) {
      this.ruleForm.dynamicItem.splice(index, 1)
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
  async created() {
    const taskId = this.$route.params.taskId
    console.log("taskId"+taskId)
    if(taskId){
      const data = await this.$api.getTaskDetails(taskId)
      console.log(data)
      this.ruleForm.taskId = data.taskId
      this.ruleForm.creatorId = data.creatorId
      this.ruleForm.title = data.title
      this.ruleForm.questionId = data.questionId
      this.ruleForm.score = data.score
      this.ruleForm.stem = data.stem
      this.ruleForm.dynamicItem = data.dynamicItem
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