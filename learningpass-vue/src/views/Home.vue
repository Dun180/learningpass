<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <h2>欢迎回来！{{user.username}}</h2>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'

export default {
  name: 'Home',
  components: {
    HelloWorld
  },
  data(){
    return{
      user:''
    }
  },
  created() {
    this.user = this.$store.getters.getUser


  }
}
</script>
