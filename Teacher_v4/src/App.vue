<template>
  <div id="app" >
    <!-- 顶部 -->
    <div class="tea_header" v-show="$route.meta.isShow || false">
      <div style="margin-left: 10px">企业内部培训系统-教师端</div>
      <el-link :underline="false" @click="exit" class="log_exit">退出登录</el-link>
    </div>
    <!-- 内容 -->
    <router-view></router-view>
   <!-- <div class="footer">
    	<h4>@企业内部培训系统</h4>
    </div> -->
  </div>
</template>

<script>
export default {
  name: "App",
  // beforeCreate() {
  //     document.querySelector('body').setAttribute('style', 'margin:0;')
  //   },
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

};
</script>

<style lang="scss" scoped>

.tea_header {
  display: flex;
  width: 100%;
  height: 60px;
  justify-content: space-between;
  align-items: center;
 background: url(./assets/bac.png) no-repeat center;
 //background-color: #483D8B;
  background-size: cover;
  color: #ffffff;
  font-size: 22px;

  .log_exit {
    margin-right: 30px;
    font-size: 14px;
    cursor: pointer;
    text-decoration: none;
    color: white;
  }

  .log_exit:hover {
    color: #000000;
  }
}
.footer{
  //position: absolute;
  //bottom: 0;
  width: 100%;
  height: 100px;
  background:#E6E6FA;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;

  h4{
    margin: 0 auto;
    font-size: 14px;
    color:	#A9A9A9;
  }
}
</style>
