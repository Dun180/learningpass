<template>
  <div>
    <Header/>
    <div class="home">
      <img alt="Vue logo" src="../assets/logo.png">
      <h2>欢迎回来！{{user.username}}</h2>
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import HelloWorld from '@/components/HelloWorld.vue'
import Header from "@/components/Header";

export default {
  name: 'Home',
  components: {
    HelloWorld,
    Header
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
