<template>
  <el-container>
    <el-header>Header
      <el-dropdown>
        <el-button type="primary">
          Dropdown List<el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item>Action 1</el-dropdown-item>
            <el-dropdown-item>Action 2</el-dropdown-item>
            <el-dropdown-item>Action 3</el-dropdown-item>
            <el-dropdown-item>Action 4</el-dropdown-item>
            <el-dropdown-item>Action 5</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>
    <div class="container">
      <el-container>
        <el-aside width="200px">Aside

        </el-aside>
        <el-main>Main</el-main>
      </el-container>
    </div>

  </el-container>
</template>


<script>
import { ArrowDown } from '@element-plus/icons'

export default {
  name: "Header",
  components:{
    ArrowDown
  },
  setup() {
    const handleClick = () => {
      alert('button click')
    }
    return {
      handleClick,
    }
  }
}
</script>

<style scoped>
.container{
  width: 1200px;
  margin: 0 auto;
}
.el-header,
.el-footer {
  background-color: #b3c0d1;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #d3dce6;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 200px;
}

.el-main {
  background-color: #e9eef3;
  color: var(--el-text-color-primary);
  text-align: center;
  line-height: 160px;
}

body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}

.el-dropdown {
  float: right;
  margin-top: 10px;
}
</style>