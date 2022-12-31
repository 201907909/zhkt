<template>
  <!-- 课程列表 -->
  <view class="course-list">
    <view class="courese-wrapper" @click="toDetail(courseItem.couId)" v-for="(courseItem,index) in coures"
      :key="courseItem.id">
      <view class="image-wrapper">
        <image :src="courseItem.couPic.replace(/[\r\n]/g,'')" mode="scaleToFill"></image>
      </view>
      <view class="center">
        <view class="center-top">
          <text class="set-one">{{courseItem.couName}}</text>
          <text class="set-two">{{courseItem.teaName}}</text>
        </view>
        <view class="center-bottom">
          <image src="../../../static/index/h5-index-course.png" mode="aspectFit"></image>
          <text>{{courseItem.couCataNum}}课时</text>
        </view>

      </view>
      <view class="right">
        <text class="set-text">{{courseItem.couPrice}}</text>
        <image class='set-icon' src="../../../static/img/point.png" mode="aspectFit"></image>
      </view>

    </view>
  </view>
</template>

<script>
  export default {
    props: ['coures'],
    data() {
      return {

      }
    },
    methods: {
      toDetail(id) {
        uni.navigateTo({
          url: 'courseDetail?couId=' + id,
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  .course-list {
    display: flex;
    flex-direction: column;
    // justify-content: space-between;
  }

  .courese-wrapper {
    padding: 6rpx;
    margin-left: 2%;
    margin-top: 14rpx;
    display: flex;
    height: 200rpx;
    width: 96%;
    flex-direction: row;
    justify-content: space-between;
    background-color: white;
    border-radius: 18rpx;
    box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);

    .image-wrapper {
      // padding-top: 20rpx;


      image {
        border-radius: 10%;
        width: 280rpx;
        margin-left: 28rpx;
        height: 180rpx;

      }
    }

    .center {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      // padding-top: 20rpx;
      width: 250rpx;
      margin-left: 20rpx;
      justify-content: space-between;

      .center-top {
        margin-top: 20rpx;

        .set-one {
          display: -webkit-box;
          overflow: hidden;
          font-size: 14px;
          font-weight: bolder;
          text-overflow: ellipsis;
          word-wrap: break-word;
          white-space: normal !important;
          //显示行
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
        }

        .set-two {
          font-size: 13px;
          // padding-top: 30rpx;
          opacity: 0.8;
        }
      }



      .center-bottom {
        // padding-top: 30rpx;
        display: flex;
        flex-direction: row;
        align-items: center;
        margin-bottom: 20rpx;

        image {
          // margin-right: 10rpx;
          width: 45rpx;
          height: 45rpx;
        }

        text {
          font-size: 8px;
          font-weight: bold;
        }


      }

    }


  }

  .right {
    // padding-top: 18rpx;
    display: flex;
    width: 100rpx;
    flex-direction: row;
    justify-content: center;
    align-items: flex-end;
    margin-bottom: 20rpx;
    // justify-content: flex-end;

    .set-text {
      font-size: 17px;
      color: red;
    }

    .set-icon {

      width: 45rpx;
      height: 45rpx;
    }
  }
</style>
