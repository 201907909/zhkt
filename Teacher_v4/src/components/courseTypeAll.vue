<template>
  <div class="app">
    <div class="main">
      <div class="father">
        <div class="type-father" v-for="type in courseParentType"
          @click="getCouTypeByParentTypeId(type.parentTypeId,type.typeName)">
          {{type.typeName}}
        </div>
      </div>
      <div class="son">
        <div class="top">
          <div class="fiery-type">
            <img src="../assets/热度.svg" alt="" width="40px" height="40px">
          </div>
          <div class="type-top" v-for="type in courseFireSonType"
            @click="getCourseByInfo(type.couTypeId,type.typeName)">
            {{type.typeName}}
          </div>
        </div>
        <div class="bottom">
          <div class="bottom-type">
            <transition name="el-zoom-in-center">
              <div v-show="show" class="transition-box">
                <div class="type-bottom" v-for="type in courseSonType"
                  @click="getCourseByInfo(type.couTypeId,type.typeName)">
                  {{type.typeName}}
                </div>
              </div>
            </transition>
          </div>
          <div class="bottom-search">
            <div class="input">
              <el-input v-model="input" placeholder="根据课程或教师名称查询课程" @blur="input=''"></el-input>
            </div>
            <div class="button">
              <el-button type="primary" icon="el-icon-search" @click="queryCourseByInfo(input)"></el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog :title=parentTypeName :visible.sync="courseDialogVisible" width="70%" top="14.5vh"
      @close='courseCloseDialogSimple'>
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item v-for="(item,index) in courseInfo" :key="index" style="border-radius: 10px">
          <img :src="item.couPicIO" @click="jumpToDetailsInfo(item.couId,item.couName,item.couPicIO)" alt=""
            height="350px" width="100%">
          <div style="color: #ffffff;">
            <div style="float: left;margin-left: 60px;line-height:40px;font-size: 28px;font-weight: 700;">
              {{ item.couName }}
            </div>
            <div style="float: right;margin-right: 20px;font-weight: 700;">
              <div style="line-height:30px;font-size: 14px;">
                {{ item.teaName }}
              </div>
              <div style="line-height:10px;font-size: 12px;">
                {{ item.createTime }}
              </div>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    name: "courseTypeAll",
    data() {
      return {
        show: true,
        input: '',
        parentTypeName: '',
        courseDialogVisible: false,
        courseParentType: [],
        courseFireSonType: [],
        courseSonType: [],
        courseInfo: []
      }
    },
    methods: {
      // 获取所有父类别
      async getParentTypeId() {
        console.log("获取所有父类别")
        const result = await this.$API.praManagement.getParentTypeId();
        if (result.data.code == 200) {
          this.courseParentType = result.data.data;
        }
      },
      // 获取所有火热子类别
      async getFieryCouTypeId() {
        console.log("获取所有火热子类别")
        const result = await this.$API.praManagement.getFieryCouTypeId();
        if (result.data.data.length != 0) {
          this.courseFireSonType = result.data.data;
        }
      },
      // 根据父类别ID查询子类别
      async getCouTypeByParentTypeId(parentTypeId, typeName) {
        console.log(parentTypeId + " " + typeName)
        this.show = false;
        const result = await this.$API.praManagement.getCouTypeByParentTypeId(parentTypeId);
        if (result.data.data.length != 0) {
          this.courseSonType = result.data.data;
          setTimeout(() => {
            this.show = true;
          }, 150);
        }
      },
      // 根据课程ID查询课程
      async getCourseByInfo(couTypeId, typeName) {
        console.log(couTypeId + " " + typeName)
        const result = await this.$API.praManagement.getCourseByInfo(couTypeId);
        if (result.data.data != null) {
          this.courseInfo = result.data.data;
          this.parentTypeName = typeName;
          this.courseDialogVisible = true;
        } else {
          this.$message({
            showClose: true,
            message: "该类别课程为空",
            type: 'warning'
          });
        }
      },
      // 根据课程详情查询课程
      async queryCourseByInfo(courseInfo) {
        const result = await this.$API.praManagement.getCourseByInfo(courseInfo);
        if (result.data.data != null) {
          this.courseInfo = result.data.data;
          this.parentTypeName = "查询内容为：" + courseInfo;
          this.courseDialogVisible = true;
        } else {
          this.$message({
            showClose: true,
            message: "该类别课程为空",
            type: 'warning'
          });
        }
      },
      courseCloseDialogSimple() {
        this.parentTypeName = '';
        this.courseDialogVisible = false;
      },
      jumpToDetailsInfo(couId, couName, couPicIO) {
        this.$router.push({
          path: "/home/courseShow",
          query: {
            couId: couId,
            couName: couName,
            couPicIO: couPicIO
          }
        })
        //console.log("跳转至" + couId + " " + couName + " " + couPicIO + "界面")
      }
    },
    created() {
      this.getParentTypeId();
      this.getFieryCouTypeId();
      this.getCouTypeByParentTypeId("1037507015724768964", "default");
    }
  }
</script>

<style lang="scss" scoped>
.app {
  height: 120px;
  width: 99.5%;

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  .main {
    width: 100%;
    height: 100%;
    border-radius: 5px;

    .father {
      background-color: white;
      width: 29%;
      height: 98%;
      float: left;
      border-radius: 10px 0 0 10px;
      border: 1px solid #656f9947;
      margin: 0 0 0 8px;
      box-shadow: -1px 1px 5px #656f999c;
      font-family: "Helvetica Neue", Helvetica, "PingFang SC", "Hiragino Sans GB", "Microsoft YaHei", "微软雅黑", Arial, sans-serif;
      //font-weight: 700;

      .type-father {
        width: 22%;
        height: 34%;
        background-color: #4c58b6;
        //background-color: #6172e3;
        float: left;
        margin: 12px 5px 3px 5px;
        text-align: center;
        line-height: 40px;
        border-radius: 8px;
        cursor: pointer;
        font-size: 14px;
        color: #fff;
        box-shadow: 0px 2px 4px #656f999c;
      }

      .type-father:active {
        box-shadow: 0 0 6px #656f999c;
      }
    }

    .son {
      width: 69%;
      height: 98%;
      float: left;
      background-color: white;
      border-radius: 0 10px 10px 0;
      margin: 0 0 0 6px;
      box-shadow: 1px 1px 5px #656f999c;
      border: 1px solid #656f9947;

      .top {
        width: 99%;
        height: 45%;
        border-radius: 0 10px 0 0;
        //border: 1px solid #656f9947;
        //box-shadow: 0px 1px 2px #656f999c;
        margin: 3px 0 10px 6px;

        .type-top {
          width: 15%;
          height: 68%;
          //background-color: #903cf1;
          //background-color: #409eff;
          background-color: #6172e3;
          float: left;
          margin: 8px 2px 3px 2px;
          text-align: center;
          line-height: 36px;
          border-radius: 6px;
          cursor: pointer;
          font-size: 15px;
          font-weight: 700;
          color: #fff;
          box-shadow: 0px 2px 4px #656f999c;
        }

        .fiery-type {
          width: 6%;
          height: 78%;
          float: left;
          margin: 6px 0px 0px 3px;
        }

        .type-top:active {
          box-shadow: 0 0 6px #656f999c;
        }
      }

      .bottom {
        width: 100%;
        height: 42%;
        border-radius: 0 0 10px 0;

        .bottom-type {
          width: 56%;
          height: 95%;
          float: left;
          border: 1px solid #656f9947;
          box-shadow: 0px 1px 3px #656f999c;
          margin: -3px 0 0px 3px;
          font-weight: 600;

          .transition-box {
            width: 100%;
            height: 100%;
            box-sizing: border-box;

            .type-bottom {
              width: 24%;
              height: 72%;
              //background-color: #3042f4;
              background-color: #6172e3;
              float: left;
              margin: 7px 2px 3px 2px;
              text-align: center;
              line-height: 34px;
              border-radius: 6px;
              cursor: pointer;
              font-size: 12px;
              color: #fff;
              box-shadow: 0px 2px 4px #656f999c;
            }

            .type-bottom:active {
              box-shadow: 0 0 6px #656f999c;
            }
          }
        }

        .bottom-search {
          width: 42%;
          height: 95%;
          float: right;
          border-radius: 0 0 10px 0;
          border: 1px solid #656f9947;
          box-shadow: 0px 1px 3px #656f999c;
          margin: -3px 3px 0px 0px;

          .input {
            margin: 4px 2px 2px 4px;
            width: 80%;
            float: left;
          }

          .button {
            margin: 4px 2px 2px 1px;
            width: 7%;
            float: left;
          }
        }
      }
    }
  }
}
</style>
