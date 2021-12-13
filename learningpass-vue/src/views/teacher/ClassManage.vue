<template>
  <el-tabs tab-position="left"  @tab-click="handleClick" v-model="activeName">
    <el-tab-pane label="成员" name="member">
<!--      <el-row-->
<!--          style="line-height: 60px;margin-top: 20px"-->
<!--          v-for="member in members"-->
<!--      >-->
<!--        <el-col>-->
<!--          <el-card :body-style="{ padding: '0px' }">-->
<!--            <div style="padding: 14px">-->
<!--              <span>{{member.name}}</span>-->
<!--            </div>-->
<!--          </el-card>-->
<!--        </el-col>-->
<!--      </el-row>-->
      <el-table :data="members" style="width: 100%">
        <el-table-column prop="date" label="Date" width="180" />
        <el-table-column prop="name" label="Name" width="180" />
        <el-table-column prop="identity" label="identity" />
      </el-table>
      <el-pagination class="mpage"
                     layout="prev, pager, next"
                     :current-page="currentPage"
                     :page-size="pageSize"
                     :total="total"
                     @current-change=page
                     style="line-height: 30px;margin-top: 20px"
      >
      </el-pagination>
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
      members:[],
      //分页数据
      currentPage: 1,
      total: 0,
      pageSize: 15

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
    //分页
    async page(currentPage) {
      const _this = this
      const data = await _this.$api.getClassMemberById(_this.classId,currentPage)

      console.log(data.records)
      console.log(typeof(data.records))

      _this.members = data.records
      _this.currentPage = data.current
      _this.total = data.total
      _this.pageSize = data.size


    }
  },
  watch: {

    classId:async function (indexVal,oldVal){

      await this.page(1)

    }

  },
}
</script>

<style scoped>
.mpage{
  margin: 0 auto;
  text-align: center;
}
</style>