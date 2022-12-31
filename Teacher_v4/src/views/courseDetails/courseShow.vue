<template>
  <div class="body">
    <div class="left">
      <div class="left-head">
        <h1>【第{{ chaItem.chaIndex }}章】 {{chaItem.chaTitle}}</h1>
      </div>
      <div class="left-center">
        <video :src='videoSrc' controls="controls" autoplay width="97%" height="519px"></video>
      </div>
    </div>
    <div class='right'>
      <div class="right-head">
        <div class="head-img">
          <img :src="course.couPicIO"/>
        </div>
        <h3>{{course.couName}}</h3>
      </div>
      <div class="right-center">
        <div class="title">
          <span>章节列表</span>
        </div>
        <div class="items">
          <div class="item" v-for="(item,index) in chapterItems" :key="index">
            <el-link :underline="false" style="font-size: 16px;" @click="changeVideo(item)">【第{{ item.chaIndex }}章】
              {{item.chaTitle}}
            </el-link>
          </div>

        </div>
      </div>

    </div>
  </div>
</template>

<script>
  export default {
    name: 'courseShow',
    data() {
      return {
        course: {
          couId: '',
          couName: '',
          couPicIO: ''
        },
        chapterItems: [],
        videoSrc: '',
        chaItem: {}
      }
    },
    methods: {
      getAllChapter(cou_id) { //得到所有章节
        this.$http({
          method: 'get',
          url: '/course/teacherChapter/getAllCourseOfChapterInfoByTea',
          params: {
            cou_id: cou_id
          }
        }).then((res) => {


          this.chapterItems = res.data.data;
          console.log(this.chapterItems)
          this.changeVideo(this.chapterItems[0])

        })
      },
      changeVideo(i) {
        this.videoSrc = i.chaUrl
        this.chaItem = i
      }
    },
    created() {
      if (this.$route.query != null) {
        this.course.couId = this.$route.query.couId
        this.course.couName = this.$route.query.couName
        this.course.couPicIO = this.$route.query.couPicIO
      }


      this.getAllChapter(this.course.couId)


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
    .left {
      width: 70%;
      height: 800px;
      .left-head {
        width: 100%;
        height: 100px;
        padding-left: 10px;
        display: flex;
        flex-direction: column;
        align-items: center;
        align-content: center;
        justify-content: center;
        .head-img {
          width: 188px;
          height: 105px;
          border-radius: 10px;
          img {
            width: 100%;
            height: 100%;
            border-radius: 10px;
          }
        }
        h1 {
          //margin-left: 30px;
          font-size: 22px;
          color: #18191C;
          ;
          //font-weight: bold;
          //display: none;
        }
      }
      .left-center {
        width: 100%;
        padding-left: 10px;
        height: 519px;
      }
    }
    .right {
      width: 30%;
      height: 800px;
      //background-color: bisque;
      .right-head {
        width: 100%;
        height: 100px;
        padding-top: 10px;
        display: flex;
        //flex-direction: column;
        align-items: center;
        //align-content: center;
        //justify-content: center;
        .head-img {
          width: 180px;
          height: 80px;
          border-radius: 8px;
          img {
            width: 100%;
            height: 100%;
            border-radius: 8px;
          }
        }

        h3 {
          margin-left: 20px;
          font-size: 22px;
          color: #18191C;
          ;
          //font-weight: bold;
          //display: none;
        }
      }

      .right-center {
        width: 100%;
        //margin-top: 20px;
        .title {
          width: 100%;
          height: 45px;
          background-color: #f1f2f3;
          display: flex;
          align-items: center;
          border-radius: 2%;
          // justify-content: center;
          span {
            margin-left: 20px;
            font-size: 18px;
          }
        }
        .items {
          margin-top: 10px;
          width: 100%;
          min-height: 455px;
          //max-height: 519px;;
          overflow: hidden;
          overflow-y: auto;
          background-color: #f1f2f3;
          .item {
            height: 45px;
            width: 100%;
            padding-left: 18px;
            display: flex;
            align-items: center;
            //background-color: white;
          }
        }
      }
    }
  }
</style>
