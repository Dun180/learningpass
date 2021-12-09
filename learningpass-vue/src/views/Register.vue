<template>
<div>
  <div class="container">
    <img class="mlogo" src="girl.png" alt="" style="height: 100px;width: 100px">
  <el-form
      ref="ruleForm"
      :model="ruleForm"
      status-icon
      :rules="rules"
      label-width="120px">
    <el-form-item label="Username" prop="username">
      <el-input v-model="ruleForm.username"></el-input>
    </el-form-item>
    <el-form-item label="Password" prop="password">
      <el-input
          v-model="ruleForm.password"
          type="password"
          autocomplete="off"
      ></el-input>
    </el-form-item>
    <el-form-item label="Confirm" prop="checkPass">
      <el-input
          v-model="ruleForm.checkPass"
          type="password"
          autocomplete="off"
      ></el-input>
    </el-form-item>
  <el-form-item label="Identity" prop="identity">
    <el-radio-group v-model="ruleForm.identity">
      <el-radio label="Student"></el-radio>
      <el-radio label="Teacher"></el-radio>
    </el-radio-group>
  </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')"
      >Submit</el-button
      >
      <el-button @click="resetForm('ruleForm')">Reset</el-button>
    </el-form-item>
  </el-form>

  </div>
</div>
</template>

<script>
export default {
  name: "Register",
  data() {
    const validateUsername = (rule, value, callback) => {
          if (value === '') {
            callback(new Error('Please input the username'))
          }
            callback()
          }
    const validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password'))
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    const validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'))
      } else if (value !== this.ruleForm.password) {
        callback(new Error("Two inputs don't match!"))
      } else {
        callback()
      }
    }
    const validateIdentity = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please choose the identity'))
      }
      callback()
    }
    return {
      ruleForm: {
        username: '',
        password: '',
        checkPass: '',
        identity: '',
      },
      rules: {
        username: [{ validator: validateUsername, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        checkPass: [{ validator: validatePass2, trigger: 'blur' }],
        identity: [{ validator: validateIdentity, trigger: 'blur' }],
      },
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          console.log(this.ruleForm)
          _this.$axios.post('/register',this.ruleForm).then(response => {
            //console.log(response.data)
            if(response.data.data.registerResult){
              console.log('注册成功')
              _this.$router.push("/login")
            }else{
              _this.$alert("注册失败");
            }

          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
  },
}
</script>

<style scoped>
.container{
  width: 600px;
  margin: 0 auto;
}
</style>