<template>
  <!-- 课程目录 -->
  <view class="directory">
    <view class="course-wapper" v-for="(item,index) in couserChapter " :key="item.id">
      <!--    <text class="set-one" v-if="item.chapter<10">0{{item.chapter}}</text>
      <text class="set-one" v-else>{{item.chapter}}</text> -->
      <text class="set-one">{{item.chaTitle}}</text>
      <text class="set-two">{{item.time}}</text>
    </view>
    <view class="divider">
      <u-divider color="#aaaa7f" half-width="200" border-color="#6d6d6d">到底了</u-divider>
    </view>
  </view>

</template>

<script>
  export default {
    //课程雪花ID
    props: ['couId'],
    data() {
      return {
        couserChapter: [],

      }
    },
    created() {
      uni.showLoading({
        title: '加载中',
        mask: true
      });
      this.getCouserChapter()

    },
    methods: {
      //课详章节详情
      getCouserChapter() {
        uni.request({
          method: "GET",
          url: '/api/course/course/allChapterByCouId',
          data: {
            couId: this.couId
          },
          success: (res) => {
            this.couserChapter = res.data.data

          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 10);
          }
        })

      },

    }
  }
</script>

<style lang="scss" scoped>
  .directory {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding-bottom: 120rpx;
    padding-top: 5rpx;
    background-color: #f7f6f8;


    .course-wapper {
      height: 88rpx;
      margin-top: 16rpx;
      // margin-bottom: 6rpx;
      width: 96%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      flex-direction: row;
      background-color: white;
      border-radius: 18rpx;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
      // text-align: center;

      .set-one {
        min-width: 120rpx;
        font-size: 30rpx;
        margin-left: 44rpx;
        // font-weight: bolder;

      }

      .set-two {
        min-width: 100rpx;
        max-width: 148rpx;
        font-size: 28rpx;
        margin-right: 28rpx;
        opacity: 0.7;
        // margin-left: 100rpx;
        // font-weight: bolder;
      }
    }

    .divider {
      margin-top: 40rpx;
    }
  }
</style>
