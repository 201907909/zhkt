<template>
  <div class="center_main">
    <div class="sideNav">
      <div class="demo-type">
        <el-avatar size="large" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
        <p style="color: white">{{teaName}}</p>
      </div>

      <ul class="nav-class">
        <router-link to="/home/dataScreen" class="side_tab">
          <li>
            <i class="el-icon-s-data"></i>
            数据大屏
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>
        <router-link to="/home/courseManagement" class="side_tab">
          <li>
            <i class="el-icon-s-grid"></i>
            课程管理
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>
        <router-link to="/home/classManagement" class="side_tab">
          <li>
            <i class="el-icon-s-custom el-ex"></i> 班级管理
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>
        <router-link to="/home/inforManagement" class="side_tab">
          <li>
            <i class="el-icon-tickets el-ex"></i> 资讯管理
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>

        <router-link to="/home/practiceManagement" class="side_tab" id="practiceManagement">
          <li>
            <i class="el-icon-map-location el-ex"></i> 线下实践
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>

        <router-link to="/home/myMessage" class="side_tab">
          <li>
            <i class="el-icon-question el-ex"></i> 问答广场
            <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>

        <router-link to="/home/couPreview" class="side_tab">
           <li>
             <i class="el-icon-monitor el-ex"></i> 课程预览
             <i class="el-icon-arrow-right el-ex"></i>
          </li>
        </router-link>

      </ul>
    </div>

    <!-- 路由组件出口的位置 -->
    <transition name="homezoom">
      <router-view style="transform-origin: 0% 0%"></router-view>
    </transition>
  </div>
</template>
<script>
  export default {
    name: "Center",
    data() {
      return {
        teaName: ''
      }
    },
    methods: {
      getTeacher() {
        this.$http({
          method: 'get',
          url: 'user/teacher/getTeaDetails'
        }).then((res) => {
          console.log(res.data)
          this.teaName = res.data.data.teaName
          sessionStorage.setItem('teaName', res.data.data.teaName);
        })
      }

    },
    created() {
      //console.log('main')
      this.getTeacher()
      // this.teaName = sessionStorage.getItem('teaName')
    }
  };
</script>

<style lang="scss" scoped>
  .homezoom-enter-active {
    animation: box-zoom 0.42s;
  }

  .homezoom-leave-active {
    display: none;
  }

  @keyframes box-zoom {
    from {
      transform: scale(0.72);
    }

    to {
      transform: scale(1);
    }
  }

  .center_main {
    display: flex;
    width: 100%;
    //overflow: hidden;
    margin: auto;
    justify-content: space-between;

    .sideNav {
      width: 14%;
      height: 2240rpx;
      //background-color: #A4ABD6;
      background-color: #4d58b5;
      //border-radius: 8px;

      .demo-type {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        width: 100%;
        height: 120px;
        padding-top: 10px;
        border-bottom: 2px solid #999fcd;
      }

      .nav-class {
        margin-left: -40px;

        .side_tab {
          display: block;
          height: 56px;
          width: 100%;
          line-height: 56px;
          text-align: center;
          text-decoration: none;
          list-style: none;
          color: white;
        }

        .el-ex {
          font-size: 15px;
        }
      }
    }

    .router-link-active {
      // background-color: #EEEAD9;
      // color: #A4ABD6 !important;

      background-color: #ffffff;
      color: #0063e4 !important;
      // border-left: #378eff solid 4px;
    }
  }
</style>
