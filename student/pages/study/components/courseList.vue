<template>
  <view class="course">
    <view class="course_item" v-for="num in 6">
      <view class="left">
        <image :src="headImage" class="courseimg"></image>
        <view class="descrip">
          <view class="name">
            <view class="course_name">修仙大法</view>
            <view class="author_name">白大仙</view>
          </view>
          <progress :percent="pgList" show-info stroke-width="3" />
        </view>

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
        headImage: '/static/img/350.png',
        pgList: 80,
      };
    }
  }
</script>

<style lang="scss">
  .course {
    background-color: #ffffff;
    border-radius: 16rpx;
    color: black;

    .course_item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      padding: 5rpx 20rpx 5rpx 20rpx;
      height: 160rpx;
      border-bottom: 2rpx solid #eeefef;
      border-radius: 16rpx;

      .left {
        display: flex;
        width: 92%;

        .courseimg {
          width: 250rpx;
          height: 120rpx;
          border-radius: 10rpx;
        }

        .descrip {
          width: 100%;
          margin-left: 20rpx;
          display: flex;
          flex-direction: column;
          justify-content: space-between;

          .course_name {
            font-size: 24rpx;
            margin-top: 4rpx;
          }

          .author_name {
            margin-top: 6rpx;
            font-size: 20rpx;
          }
        }
      }

      .iconimg {
        margin-left: 10rpx;
        width: 15rpx;
        height: 23rpx;
      }

    }

    .course_item:hover {
      background-color: #eeefef;
    }

    .course_bottom {
      height: 120rpx;
      line-height: 80rpx;
      color: #41a863;
      text-align: center;
    }
  }
</style>
