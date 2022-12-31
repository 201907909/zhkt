<template>
  <view class="essay">
    <view class="course_item" v-for="num in 6">
      <view class="left">
        <image :src="headImage" class="courseimg" mode="aspectFill"></image>
        <view class="descrip">
          <view class="name">
            <view class="course_name">修仙大法</view>
            <view class="content">
              白大仙请另行在小程序开发工具的控制台查看。白大仙请另行在小程序开发工具的控制台查看。白大仙请另行在小程序开发工具的控制台查看。白大仙请另行在小程序开发工具的控制台查看。白大仙请另行在小程序开发工具的控制台查看。白大仙请另行在小程序开发工具的控制台查看。
            </view>
          </view>
        </view>
      </view>
      <image class="iconimg" src="/static/user/20.png" mode="aspectFit"></image>
    </view>
    <!-- 到底了 -->
    <view class="course_bottom">
      到底了~~~
    </view>
  </view>
  </view>
</template>

<script>
  export default {
    data() {
		return {
			headImage: '/static/study/study.png',
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
      align-items: center;
      justify-content: space-between;
      padding: 5rpx 20rpx 5rpx 20rpx;
      border-radius: 16rpx;
      width: 100%;
      height: 160rpx;
      border-bottom: 2rpx solid #eeefef;


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
            font-size: 26rpx;
            margin-top: 6rpx;
          }

          .content {
            margin-top: 10rpx;
            font-size: 24rpx;
            colof: #999;
            max-height: 120rpx;
            overflow: hidden;
            word-break: break-all;
            /* break-all(允许在单词内换行。) */
            text-overflow: ellipsis;
            /* 超出部分省略号 */
            display: -webkit-box;
            /** 对象作为伸缩盒子模型显示 **/
            -webkit-box-orient: vertical;
            /** 设置或检索伸缩盒对象的子元素的排列方式 **/
            -webkit-line-clamp: 2;
            /** 显示的行数 **/



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
