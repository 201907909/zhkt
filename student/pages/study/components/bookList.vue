<template>
  <view class="essay">
    <view class="course_item" v-for="num in 6">
      <view class="descrip">
        修仙大法
      </view>
      <image class="iconimg" src="/static/user/20.png" mode="aspectFit"></image>
    </view>
    <!-- 到底了 -->
    <view class="course_bottom">
      到底了~~~
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {

      };
    }
  }
</script>

<style lang="scss" scoped>
  .essay {
    background-color: #ffffff;
    border-radius: 16rpx;
    color: black;

    .course_item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      width: 100%;
      height: 60rpx;
      border-radius: 16rpx;
      padding: 5rpx 20rpx 5rpx 40rpx;

      .descrip {}

      .iconimg {
        width: 15rpx;
        height: 23rpx;
      }
    }

    .course_item:hover {
      background-color: #ececec;
    }

    .course_bottom {
      height: 80rpx;
      line-height: 80rpx;
      color: #41a863;
      text-align: center;
    }



  }
</style>
