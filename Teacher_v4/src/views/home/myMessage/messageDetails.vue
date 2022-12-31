<template>
  <div class="body">

    <div class="body-left">
      <div class="left-head">
        <div class="head-avatar">
          <el-avatar :size="50" :src='allData.image'></el-avatar>
        </div>
        <div class="head-name">
          <span>{{allData.userName}}</span>
        </div>
      </div>
      <div class="left-main">
        <el-scrollbar style="height: 100%;overflow-x: hidden;" class="scrollbar-for">
          <div class="main-content">
            <span>{{allData.content}}
            </span>
          </div>
          <div class="main-img" v-if="allData.img != ''">
            <!--                <img src="../../../assets/default.jpg" /> -->
            <el-image v-for="(item,index) in allData.imgs" :key="index" style="width: 100%; height: 100%" :src="item"
              :preview-src-list="allData.imgs">
            </el-image>
          </div>
        </el-scrollbar>
      </div>

    </div>


    <div class="body-right">

      <div class="items">
        <el-scrollbar style="height: 100%;" class="scrollbar-for">
        <div class="item" v-for="(item,index) in items" :key="index">
          <div class="item-head">
            <div class="head-avatar">
              <el-avatar v-if="item.role == 1" :size="50" :src="item.image"></el-avatar>
              <el-avatar v-else :size="50" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png">
              </el-avatar>
            </div>
            <div class="head-name">
              <span>{{item.userName}}</span>
              <el-tag v-if="item.role == 1" type="success" size="mini" style="margin-left: 10px;">学生</el-tag>
              <el-tag v-else size="mini" style="margin-left: 10px;">老师</el-tag>

            </div>
          </div>
          <div class="item-content">
            <span>
              {{item.content}}
            </span>

            <div class="content-time">{{item.createTime}}</div>
            <el-divider></el-divider>
          </div>

        </div>
        </el-scrollbar>
      </div>

      <div class="answer">
        <div class="answer-content">
          <el-input type="textarea" resize=none :rows="3" placeholder="请解答" v-model="form.content">
          </el-input>
        </div>
        <button class="answer-button" @click="add()">发 布</button>

      </div>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'messageDetails',
    data() {
      return {

        items: [],
        allData: {},
        queId: '',
        url: '',
        textare: '',
        srcList: [],
        form: {
          answeredTqId: '',
          content: '',
          queId: '',
          role: 0,
          userId: '',
        }

      }
    },

    methods: {
      getteaIssueVODetail() {
        this.$http({
          method: 'get',
          url: '/interaction/wenda/teaIssueVODetail',
          params: {
            queId: this.queId
          }
        }).then((res) => {
          console.log(res.data.data)
          this.allData = res.data.data
          this.srcList = res.data.data.imgs
          this.items = res.data.data.replyVOs

        })
      },
      add() {
        this.form.userId = sessionStorage.getItem('userId')
        this.form.answeredTqId = this.allData.queId
        console.log(this.form)
        this.$http({
          method: 'post',
          url: '/interaction/wenda/reply',
          data: this.form
        }).then((res) => {
          this.$message({
            message: res.data.msg,
            type: 'success'
          })
          this.getteaIssueVODetail()
          this.form.content = ""
        })
        //console.log(this.allData)
      }
    },

    created() {
      if (this.$route.query != null) {
        this.queId = this.$route.query.queId
      }

      this.getteaIssueVODetail()


    }

  }
</script>

<style lang="scss" scoped>
  .body {
    width: 85%;
    height: 680px;
    margin: auto;
    margin-top: 10px;
    background-color: white;
    display: flex;
    justify-content: space-between;

    .body-left {
      width: 50%;
      height: 680px;

      .left-head {
        width: 100%;
        padding-top: 20px;
        padding-left: 20px;
        display: flex;

        .head-avatar {}

        .head-name {

          margin-left: 10px;
          display: flex;
          align-content: center;
          align-items: center;
          //justify-content: space-around;
          color: #409EFF;
        }
      }

      .left-main {
        width: 100%;
        height: 606px;
        //margin-left: 70px;
        overflow: hidden;
        //overflow-y: auto;

        // background-color: blue;
        /deep/ .el-scrollbar__wrap {
          overflow-x: hidden;
        }

        .main-content {
          width: 80%;
          margin-left: 70px;
        }

        .main-img {
          width: 80%;
          height: 400px;
          margin-left: 70px;
          margin-top: 10px;

          img {
            width: 100%;
            height: 100%;
          }

        }
      }


    }

    .body-right {
      width: 50%;
      height: 680px;
      border-left: 0.5px solid #DCDFE6;

      .items {
        width: 100%;
        height: 580px;
        overflow: hidden;
        // overflow-y: auto;
        // overflow-x: auto;

        //border-bottom: 0.5px solid #987cb9;
        /deep/ .el-scrollbar__wrap {
          overflow-x: hidden;
        }
        .item {

          width: 100%;
          // border-bottom: 2px solid #909399;

          .item-head {
            width: 100%;
            display: flex;
            margin-top: 20px;
            //margin-left: 20px;
            // padding-left: 20px;

            .head-avatar {
              margin-left: 20px;
            }

            .head-name {

              margin-left: 10px;
              display: flex;
              align-items: center;
              //align-content: center;
              //justify-content: center;

              color: #409EFF;
            }
          }

          .item-content {
            width: 80%;
            margin-left: 80px;

            // border-bottom: 2px solid #909399;
            .content-time {
              float: right;
              margin-top: 10px;
              margin-bottom: 5px;
              color: #909399;
            }
          }

        }
      }

      .answer {
        width: 100%;
        height: 100px;
        //background-color: #DCDFE6;
        display: flex;
        align-items: center;
        justify-content: center;

        .answer-content {
          width: 80%;

        }

        .answer-button {
          width: 75px;
          height: 75px;
          background-color: #409EFF;
          margin-left: 10px;

          font-size: 18px;
          border-radius: 4px;
          border: 0px;
          color: white;
        }

        //box-shadow: 0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)
      }

      // background-color: aquamarine;
    }
  }
</style>
