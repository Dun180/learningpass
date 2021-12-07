<template>

</template>

<script>
export default {
  name: "Error",
  created() {
    let _this = this
    _this.$alert('登录信息失效！','提示',{
      confirmButtonText:'确定'
    }).then((response) => {
      _this.$store.commit("REMOVE_INFO")
      _this.$router.replace({path:'/login'})
    })
  }
}
</script>

<style scoped>

</style>