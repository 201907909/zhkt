<template>
  <view class="introduction">
    <view class="course-introduct">
      <text class="set-one">课程简介</text>
      <text class="set-two">{{courseData.couIntroduction}}</text>
    </view>
    <view class="course-teacher">
      <text class="set-one">授课老师</text>
      <text class="set-two">{{courseData.teaName}}</text>
    </view>
    <view class="course-relative">
      <text class="set-one">相关课程</text>
      <view class="course-wrapper" @click="toDetail(course.couId)" v-for=" (course,index) in relativeCourse"
        :key="course.id">
        <view class="wrap-image">
          <image :src="course.couPic.replace(/[\r\n]/g,'')" mode="aspectFill"></image>
        </view>
        <view class="course-detail">
          <text class="set-one">{{course.couName}}</text>
          <text style="opacity: 0.7;">{{course.teName}}</text>
          <text class="set-two">{{course.couIntroduction}}</text>
        </view>
      </view>
    </view>


  </view>
</template>

<script>
  export default {
    //课程雪花ID
    props: ['couId'],
    data() {
      return {
        courseData: [],
        relativeCourse: []

      }
    },
    methods: {
      toDetail(id) {
        uni.navigateTo({
          url: 'courseDetail?couId=' + id,
        });
      },
      //获取课程
      getCourseDetail() {
        uni.request({
          method: "GET",
          url: '/api/course/course/courseDetail2',
          data: {
            couId: this.couId
          },
          success: (res) => {
            this.courseData = res.data.data
            this.relativeCourse = res.data.data.couRelatedVOList;
          },
          complete() {

          }
        })

      }
    },

    created() {
      this.getCourseDetail()
    }

  }
</script>

<style lang="scss" scoped>
  .introduction {
    padding: 20rpx;

    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .course-introduct {
      display: flex;
      flex-direction: column;
      background-color: white;
      justify-content: space-between;
      padding-bottom: 20rpx;
      border-bottom: solid 0.5px gainsboro;

      .set-one {
        padding-bottom: 10rpx;
        font-size: 28rpx;
        font-weight: bold;
      }

      .set-two {
        // padding-top: 20rpx;
        // padding-bottom: 20rpx;
        opacity: 0.9;
      }


    }

    .course-teacher {
      display: flex;
      flex-direction: column;
      background-color: white;
      justify-content: space-between;
      border-bottom: solid 0.5px gainsboro;

      .set-one {
        padding-top: 20rpx;
        font-size: 28rpx;
        font-weight: bold;
      }

      .set-two {
        padding-top: 20rpx;
        padding-bottom: 20rpx;
        opacity: 0.9;
      }
    }

    .course-relative {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      padding-bottom: 110rpx;

      .set-one {
        padding-top: 20rpx;
        font-size: 28rpx;
        font-weight: bold;
      }



      .course-wrapper {
        display: flex;
        flex-direction: row;
        background-color: white;
        margin-bottom: 10rpx;

        .wrap-image {
          padding-top: 20rpx;
        }

        image {
          border-radius: 10%;
          min-width: 230rpx;
          max-width: 200rpx;
          height: 150rpx;

        }

        .course-detail {
          display: flex;
          flex-direction: column;
          justify-content: space-between;

          padding-left: 20rpx;


          .set-one {
            font-size: 28rpx;
            font-weight: bold;
          }

          .set-two {

            display: -webkit-box;
            overflow: hidden;
            text-overflow: ellipsis;
            word-wrap: break-word;
            white-space: normal !important;
            //显示行
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            margin-bottom: 30rpx;
          }
        }
      }
    }
  }
</style>
