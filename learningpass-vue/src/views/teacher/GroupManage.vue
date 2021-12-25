<template>

  <el-row>
    <el-col :span="7">
      <el-card style="height: 600px;">
        <template #header>
          <div class="card-header">
            <span>分组</span>
            <el-button type="text" @click="dialogTableVisible = true">+新建分组</el-button>
          </div>
        </template>
      </el-card>
    </el-col>
    <el-col :span="1"></el-col>
    <el-col :span="16">
      <el-card style="height: 600px;">
        <template #header>
          <div class="card-header">
            <span>未分组</span>
            <el-button type="primary" round>添加成员</el-button>
          </div>
        </template>
      </el-card>
    </el-col>
  </el-row>



  <!-- 添加分组弹框 -->
  <el-dialog
      title="添加分组"
      @close="addDialogClose"
      v-model="dialogTableVisible"

      :close-on-click-modal="false"
  >
    <!-- 添加分组的表单 -->
    <el-form ref="addFormRef" :rules="rulesAddGroup" :model="addGroup" label-width="100px">
      <el-form-item prop="groupName" label="分组名称">
        <el-input v-model="addGroup.groupName"></el-input>
      </el-form-item>


      <el-form-item>
        <el-button @click="dialogTableVisible = false">取消</el-button>
        <el-button type="primary" @click="onAddGroup">确定</el-button>
      </el-form-item>
    </el-form>
  </el-dialog>

</template>

<script>
import {ElMessage} from "element-plus";

export default {
  name: "GroupManage",
  data(){
    return {
      dialogTableVisible: false, // 添加分组弹框
      addGroup:{
        groupName:'',
        groupMode:'',
        maximumNumber:'',
        minimumNumber:'',
      },
      // 验证规则
      rulesAddGroup: {
        groupName: [
          { required: true, message: '请输入班级名', trigger: 'blur' }
        ],
      },
    }
  },
  methods:{
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加班级
    onAddGroup() {

      this.$refs.addFormRef.validate(async valid => {
        if (valid) {
          const _this = this
          console.log("submit")
          //axios异步向后端请求数据验证
          // console.log(this.addClass)
          // const resp = await _this.$api.createClass(_this.addClass);
          //
          // //console.log(response.data)
          // if(resp.addResult){
          //   console.log('添加成功')
          //   ElMessage({
          //     message: '添加成功',
          //     type: 'success',
          //   })
          //   _this.dialogTableVisible = false  // 关闭弹框
          //   _this.$refs.addFormRef.resetFields() // 清空表单
          //   _this.$store.commit('increment')//刷新页面
          // }else{
          //   ElMessage.error('添加失败')
          // }


        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
    },
  }
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>