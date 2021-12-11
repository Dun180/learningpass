<template>
  <el-tabs tab-position="left" style="height: 200px" @tab-click="handleClick" v-model="activeName">
    <el-tab-pane label="成员" name="member">
      <el-row
          style="line-height: 60px;margin-top: 20px"
          v-for="member in members"
      >
        <el-col>
          <el-card :body-style="{ padding: '0px' }">
            <div style="padding: 14px">
              <span>{{member.name}}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-tab-pane>
    <el-tab-pane label="Config" name="second">Config</el-tab-pane>
    <el-tab-pane label="Role" name="third">Role</el-tab-pane>
    <el-tab-pane label="Task" name="fourth">Task</el-tab-pane>
  </el-tabs>
</template>

import {API} from "@/lib/api";

<script>
export default {
  name: "ClassManage",
  props:{
    classId:'',
  },
  data(){
    return{
      activeName: 'member',
      members:{},
    }
  },
  methods: {
    handleClick(tab, event) {
      console.log(this.classId)
      switch (this.activeName){
        case "member":

          break
        default:
          break
      }
    },
  },
  watch: {
    classId:async function (indexVal,oldVal){
      const _this = this
      const data = await _this.$api.getClassMemberById(_this.classId)
      _this.members = data
      console.log("-----------------")
      console.log(data)

    }
  },
}
</script>

<style scoped>

</style>