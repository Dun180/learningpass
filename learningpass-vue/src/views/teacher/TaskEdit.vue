<template>
  <div class="container">
    <div class="m-content">


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
          <v-md-editor v-model="ruleForm.stem" height="400px"></v-md-editor>
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
            <v-md-editor v-model="item.stem" height="400px"></v-md-editor>
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
        title: '',
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
      this.$refs[formName].validate((valid) => {
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
        id: '',
        title: '',
        description: '',
        content: ''
      })
    },
    deleteItem (item, index) {
      this.ruleForm.dynamicItem.splice(index, 1)
    }
  },
  created() {
    // const blogId = this.$route.params.blogId
    // const _this = this
    // if(blogId){
    //   this.$axios.get('/blogs/'+blogId).then(res => {
    //     const blog = res.data.data
    //     _this.ruleForm.id = blog.id
    //     _this.ruleForm.title = blog.title
    //     _this.ruleForm.description = blog.description
    //     _this.ruleForm.content = blog.content
    //   })
    // }
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