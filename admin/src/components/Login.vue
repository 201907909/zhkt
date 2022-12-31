<template>
  <div class="login2">
    <div class="login-box">
      <form v-model="form">
        <h2>管 理 员 登 录</h2>
        <div class="form">
          <div class="item">
            <i class="fa fa-user-circle-o" aria-hidden="true"></i>
            <input type="text" placeholder="账号" v-model="form.account" required />
          </div>
          <div class="item">
            <i class="fa fa-key" aria-hidden="true"></i>
            <input type="password" placeholder="密码" v-model="form.password" required />
          </div>
        </div>
        <button type="button" @click="Login()">登 录</button>
      </form>
    </div>
  </div>
</template>

<script>
export default {
  name:'Login',
  data(){
    return{
      form:{
        account:'',
        password:''

      }
    }
  },
  methods:{
    Login(){
      this.$http({
        method: 'post',
        url: '/user/admin/login',
        data: this.form
      }).then((res) => {
        //console.log(res.data);
        if (res.data.code == 200) {
          console.log(res.data)

          this.$message({
            message: res.data.msg,
            type: 'success'
          });
          sessionStorage.setItem('userId', res.data.data); //写入登录状态

          this.$router.push("/center/textAudit");
        } else {
          this.$message.error(res.data.msg);
        }
      })
    }
  }
};
</script>

<style lang="scss" scoped>
.login2 {
  height: 100vh;
  background: url("../assets/star.png");
  background-repeat: no-repeat;
  background-size: 100% auto;
  .login-box {
    width: 200px;
    position: relative;
    top: 28%;
    margin: 0 auto;
    text-align: center;
    background: #00000090;
    background-color: white;
    padding: 28px 50px;
    border-radius: 14px;
    h2 {
      color: rgb(0, 0, 0);
    }
    .item {
      margin-top: 15px;
      i {
        font-size: 18px;
        color: rgb(0, 0, 0);
      }
      input {
        width: 180px;
        font-size: 18px;
        border: 0;
        border-bottom: 2px solid rgb(0, 0, 0);
        padding: 5px 10px;
        background: #ffffff00;
        color: rgb(0, 0, 0);
        outline: none;
      }
    }
    button {
      margin-top: 15px;
      width: 180px;
      height: 30px;
      font-size: 18px;
      font-weight: 400;
      color: #fff;
      border: 0;
      border-radius: 15px;
      background-image: linear-gradient(
        to right,
        #e4afcb 0%,
        #b8cbb8 0%,
        #b8cbb8 0%,
        #e2c58b 30%,
        #c2ce9c 64%,
        #7edbdc 100%
      );
      cursor: pointer;
    }
  }
}
</style>
