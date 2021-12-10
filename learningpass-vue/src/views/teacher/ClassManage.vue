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
    classId:function (indexVal,oldVal){
      const _this = this
      _this.$axios.get("/class/member/"+_this.classId).then(res => {
        console.log(res)
        _this.members = res.data.data
        console.log(_this.members)
      }).catch(error => {
        console.log("出错")
      })
    }
  },
}
</script>

<style scoped>

</style>