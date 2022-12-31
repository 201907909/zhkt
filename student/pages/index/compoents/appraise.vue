<template>
  <!-- 评价 -->
  <view class="appraise">
    <view class="appraise-wrapper">
      <text>综合评价</text>
      <text class="a-one">（{{num}}人评价）</text>
      <view class="stars">
        <u-rate :count="count" :current="value" :disabled="true" active-color="#ff0000" inactive-color="#d3d3d3"
          gutter="20"></u-rate>
      </view>

    </view>
    <view class="comment">
      <view class="comment-wrapper" v-for="(commentItem,index) in comments" :key="commentItem.id">
        <view class="left">
          <u-avatar :src="commentItem. image.replace(/[\r\n]/g, '')" mode="circle"></u-avatar>
        </view>
        <view class="right">
          <view class="stars-one">
            <text class="one">{{commentItem.stuName}}</text>
            <u-rate :size="20" :count="count" :current="commentItem.evScore" :disabled="true" active-color="#ff0000"
              inactive-color="#d3d3d3" gutter="10"></u-rate>
          </view>
          <text class="two">{{commentItem.createTime}}</text>
          <text class="three">{{commentItem.couAppraise}}</text>
        </view>
      </view>
    </view>
    <!--  <u-divider color="#aaaa7f" half-width="200" border-color="#6d6d6d">到底了</u-divider> -->
  </view>
</template>

<script>
  export default {
    //课程雪花ID
    props: ['couId'],
    data() {
      return {
        //总共的星数
        count: 5,
        // 课程评分
        value: 0,
        //课程评价人数
        num: 0,
        page: 1,
        comments: []
      }
    },
    created() {
      uni.showLoading({
        title: '加载中',
        mask: true
      });
      this.getCouserAppraise(false)
    },
    methods: {
      //课详评价详情 (评价用户内容分页)
      getCouserAppraise(index) {
        if (index) {
          this.page += 1; //每触底一次 page +1
        }
        if (!index) {
          this.comments = []
          this.page = 1
        }
        uni.request({
          method: "GET",
          url: '/api/course/course/courseAppraise',
          data: {
            couId: this.couId,
            current: this.page
          },
          success: (res) => {
            this.comments = this.comments.concat(res.data.data.couAppraiseVOList) //将数据拼接在一起
            this.num = res.data.data.num
            this.value = res.data.data.allScore
            if (index) {
              //如果返回没有数据了,数组长度为0
              if (res.data.data.couAppraiseVOList.length == 0) {
                //没有数据就替换一下loadingText的值
                uni.showToast({
                  title: '没有更多评价了',
                  icon: 'none',
                  mask: false
                })
              }
            }

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
  .appraise {
    padding-top: 5rpx;
    padding-bottom: 150rpx;

    .appraise-wrapper {
      padding-top: 40rpx;
      display: flex;
      flex-direction: column;
      align-items: center;
      height: 150rpx;
      background-color: white;

      .a-one {
        opacity: 0.8;
        font-size: 10rpx
      }

      text {
        font-weight: bold;
        // 字体间距属性
        letter-spacing: 9rpx;
        margin-bottom: 10rpx;
      }

    }

    .comment {
      padding-top: 5rpx;

      display: flex;
      flex-direction: column;
      justify-content: space-between;


      .comment-wrapper {
        padding-top: 20rpx;
        background-color: white;
        display: flex;
        flex-direction: row;
        align-content: center;
        margin-bottom: 5rpx;


        .left {
          padding-left: 20rpx;

        }

        .right {
          padding-left: 20rpx;
          padding-right: 10rpx;
          padding-bottom: 5rpx;
          display: flex;
          flex-direction: column;

          .stars-one {
            display: flex;
            align-items: center;
          }

          .one {
            font-size: 10px;
            font-weight: bold;
          }

          .two {
            opacity: 0.5;
            font-size: 8px;
            margin-bottom: 10rpx;
          }

          .three {
            font-size: 12px;
            font-weight: bold;
          }
        }

      }
    }
  }
</style>
