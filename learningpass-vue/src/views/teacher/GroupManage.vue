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
        <el-scrollbar height="480px">
          <el-row
              style="line-height: 60px;margin-top: 20px"
              v-for="group in groupList"
          >
            <el-col>
                <el-card
                    :body-style="{ padding: '0px' }"
                    @click="handleClick(group.id,group.groupName)"
                    style="cursor:pointer"
                >
                  <div style="padding: 14px">
                    <span>{{group.groupName}}</span>
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
          <div class="card-header">
            <span>{{this.groupName}}</span>
            <el-button type="primary" round>添加成员</el-button>
          </div>
        </template>
        <el-table :data="this.groupMemberList" style="width: 100%">
          <el-table-column prop="username" label="学号" width="180" />
          <el-table-column prop="name" label="姓名" width="180" />
          <el-table-column prop="identity" label="身份" />
          <el-table-column label="Operations">
            <template #default="scope">
              <el-button
                  size="mini"
                  type="danger"
                  @click=""
              >删除</el-button
              >
              <el-button
                  size="mini"
                  type="warning"
                  @click=""
              >重置密码</el-button
              >
            </template>
          </el-table-column>
        </el-table>
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

      <el-form-item label="分组模式">
        <el-radio-group v-model="addGroup.groupMode">
          <el-radio label="1">随机分组</el-radio>
          <el-radio label="2">顺序分组</el-radio>
          <el-radio label="3">自由分组</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="最小人数">
        <el-input-number size="small" v-model="addGroup.minimumNumber" :min="1" :max="10" />
      </el-form-item>

      <el-form-item prop="maximumNumber" label="最大人数">
        <el-input-number size="small" v-model="addGroup.maximumNumber" :min="1" :max="10" />
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
  props:{
    classId:'',
  },
  data(){
    let validMaximum = (rule, value, callback) => {
      if (parseInt(this.addGroup.maximumNumber) < parseInt(this.addGroup.minimumNumber)) {
        callback(new Error("最大人数需要大于等于最小人数"));
      } else {
        //这里验证成功之后一定要给一个callback，不然提交的时候验证都通过，但是valid为false
        callback();
      }
    };
    return {
      groupList:[],
      groupName:'未分组',
      groupMemberList:[],
      dialogTableVisible: false, // 添加分组弹框
      addGroup:{
        classId: '',
        groupName: '',
        groupMode: '1',
        maximumNumber: '10',
        minimumNumber: '1',
      },
      // 验证规则
      rulesAddGroup: {
        groupName: [
          { required: true, message: '请输入班级名', trigger: 'blur' }
        ],
        maximumNumber: [
          {validator: validMaximum, trigger: "blur"}
        ],
      },
      currentGroupId:'',


    }
  },
  methods:{
    //点击分组
    handleClick(value1,value2) {
      this.currentGroupId = value1
      this.groupName = value2
    },
    // 关闭弹框的回调
    addDialogClose() {
      this.$refs.addFormRef.resetFields() // 清空表单
    },
    // 点击添加班级
    onAddGroup() {
      this.addGroup.classId = this.classId
      this.$refs.addFormRef.validate(async valid => {
        if (valid) {

          console.log(this.addGroup)
          //axios异步向后端提交数据
          const resp = await this.$api.creatGroup(this.addGroup)
          if (resp){

              ElMessage({
                message: '创建成功',
                type: 'success',
              })
              this.dialogTableVisible = false  // 关闭弹框
              this.$refs.addFormRef.resetFields() // 清空表单
              this.$store.commit('increment')//刷新页面
          }else {
              ElMessage.error('创建失败')

          }

        } else {
          ElMessage.error('提交失败')
          return false
        }

      })
    },
    //获取分组列表
    async getGroupList(){
      const resp = await this.$api.getGroupList(this.classId)

      console.log(resp)

      this.groupList =resp
    },
    //获取分组成员
    async getGroupMember(){
      const resp = await this.$api.getGroupMember(this.currentGroupId)
      console.log(resp)
      this.groupMemberList = resp

    }
  },
  watch: {

    classId:async function (indexVal,oldVal){

      await this.getGroupList()

    },
    currentGroupId:async function (indexVal,oldVal){

      await this.getGroupMember()


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