<template>
  <div>
    <div class="top">


        <h1>企业内部培训系统-管理端</h1>

      <el-link :underline="false" @click="exit" class="exit">退出登录</el-link>




    </div>
  </div>
</template>

<script>
  export default {
    name: "Top",
    methods:{
  exit(){
    this.$http({
      method:'get',
      url:'/user/teacher/logout'
    }).then((res)=>{
      // console.log(res)
      this.$message({
        message: '成功退出',
        type: 'success'
      });


      this.$router.push("/login");

    })
  }
}

  }
</script>

<style scoped lang="scss">
  .top {
    width: 100%;
    height: 60px;
      background-color: white;;
    display: flex;
    align-items: center;
     justify-content: space-between;
    h1{
      margin-left: 20px;
      //color: white;
      font-size: 24px;
    }
    .exit{
      margin-right: 30px;
      font-size: 18px;
      cursor: pointer;
      text-decoration: none;
      color: black;

    }

  }





  a {
    text-decoration: none;
    font-size: 30px;
    color: #6095ee;
  }

  .router-link-active {
    text-decoration: none;
  }
</style>
