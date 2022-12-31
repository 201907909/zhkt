<template>
  <div class="center-body">
    <el-container>
      <el-aside width="220px" class="left">
        <el-menu>



          <el-submenu index="1">
            <template slot="title"><i class="el-icon-s-comment"></i>文本审核</template>

            <el-menu-item-group>

              <router-link to="/Center/textSafe" :underline="false">
                <el-menu-item index="1-1">

                  文本安全

                </el-menu-item>
              </router-link>

              <router-link to="/Center/textAudit" :underline="false">
                <el-menu-item index="1-2">

                  复审文本

                </el-menu-item>
              </router-link>



              <router-link to="/Center/textHistory" :underline="false">
                <el-menu-item index="1-3">

                  历史文本

                </el-menu-item>
              </router-link>



            </el-menu-item-group>

          </el-submenu>


          <el-submenu index="2">

            <template slot="title"><i class="el-icon-picture"></i>图像审核</template>

            <el-menu-item-group>

              <router-link to="/Center/imgSafe" :underline="false">
                <el-menu-item index="2-1">

                  图像安全

                </el-menu-item>
              </router-link>

              <router-link to="/Center/imgAudit" :underline="false">
                <el-menu-item index="2-2">

                  复审图像

                </el-menu-item>
              </router-link>

              <router-link to="/Center/imgHistory" :underline="false">
                <el-menu-item index="2-3">

                  历史图像

                </el-menu-item>
              </router-link>
            </el-menu-item-group>

          </el-submenu>





        </el-menu>
      </el-aside>

      <el-container class="right">

        <el-main>

          <router-view></router-view>
        </el-main>
      </el-container>

    </el-container>
  </div>
</template>

<style lang="scss" scoped>
  .center-body {
    //box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
    background-color: #F2F4F7;
    width: 100%;
    margin: auto;
    overflow: hidden;

    .el-container {


      .right {
        box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        height: 690px;
        overflow: hidden;
        .el-main{
          padding: 0;
        }


      }

      .left {
         box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);
        height: 690px;
        background-color: white;
      }
    }

  }

  .avatar {
    width: 220px;
    height: 150px;
    background-color: #8BA3C7;
    //margin-top: 0px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;

    .el-avatar {
      margin-top: 10px;
    }

  }

  // .avatar-icon{
  //   width: 125px;
  //   height: 125px;

  //   .el-avatar{
  //     margin:auto ;
  //   }
  // }

  .avatar-name {
    width: 100px;
    height: 30px;
    margin: auto;
    margin-bottom: 30px;
    text-align: center;



  }





  a {
    text-decoration: none;
  }

  .router-link-active {
    text-decoration: none;
  }
</style>

<script>
  export default {

    name: "Center"

  };
</script>
