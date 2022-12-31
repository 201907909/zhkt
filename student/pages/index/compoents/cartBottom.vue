<template>
  <!-- 底部购物车 -->
  <view class="cartBottom">
    <view class="cartBottom-wrapper">
      <view class="left">
        <view class="set" v-if="ifLike==1" @click="likeCancel">
          <image src="/static/index/collection.png" mode="aspectFit"></image>
          <text>已收藏</text>
        </view>
        <view class="set" v-else @click="like">
          <image src="../../../static/index/cancelCollection.png" mode="aspectFit"></image>
          <text>收藏</text>
        </view>
        <view class="set" v-if="ifGive==1" @click="giveCancel">
          <image src="/static/index/give.png" mode="aspectFit"></image>
          <text>已赞</text>
        </view>
        <view class="set" v-else @click="give">
          <image src="/static/index/cancelGive.png" mode="aspectFit"></image>
          <text>点赞</text>
        </view>
        <view class="set">
          <image src="../../../static/index/wx_login.png" mode="aspectFit"></image>
          <text>分享</text>
        </view>

      </view>
      <view class="right" @click="open" v-if="ifbuy==1&&isBuy==0">
        <!-- //模态框 -->
        <view>
          <u-modal v-model="show" @confirm="toCart" :content="content" :showCancelButton="true"></u-modal>
        </view>
        <text class="set-three">已加入购物车</text>
      </view>

      <view class="right" @click="toStudy" v-else-if="isBuy==1">
        <text class="set-two">已购课程，现在学习</text>
      </view>
      <view class="right" @click="open" v-else>
        <view class="money">
          <text class="set-one">{{CartData.couPrice}}</text>
          <image src="../../../static/index/point.png" mode="aspectFit"></image>
        </view>
        <text class="set-two">加入购物车</text>
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
        // 是1否0收藏
        ifLike: 0,
        //是1否0点赞
        ifGive: 0,
        // 是1否0加入购物车
        ifbuy: 0,
        //是否已经购买
        isBuy: 0,
        show: false,
        content: '已成功加入购物车，是否前去结算',
        CartData: [],
        //价格
        couPrice: 0,

      }
    },
    created() {

      this.getCourseDetail()
    },
    methods: {
      //课程详情
      getCourseDetail() {
        uni.request({
          method: "GET",
          url: '/api/course/course/courseDetail3',
          data: {
            couId: this.couId
          },
          success: (res) => {
            this.CartData = res.data.data
            this.ifGive = this.CartData.isClick
            this.ifLike = this.CartData.isCollection
            this.ifbuy = this.CartData.isAddCart
            this.isBuy = this.CartData.isBuy

          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 300);
          }
        })

      },
      // 收藏
      like() {
        if (!uni.getStorageSync("userId")) {
          uni.showModal({
            title: '提示',
            content: '未登录，登录才能操作！',
            success: function(res) {
              if (res.confirm) {
                uni.navigateTo({
                  url: '/pages/login/login'

                });
              } else if (res.cancel) {

              }
            }
          });

        } else {

          this.ifLike = 1
          uni.showToast({
            title: '收藏成功',
            duration: 1000
          });
          //调用父方法，‘刷新’收藏数  like()
          this.$emit('fatherLike');
          uni.request({
            url: '/api/user/colletion/add',
            method: 'POST',
            data: {
              couId: this.couId,
              // couCollNum: this.CartData.couCollNum
            },
            header: {
              "Content-Type": "application/x-www-form-urlencoded",
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            success: (res) => {

            }

          })
        }
      },
      // 取消收藏
      likeCancel() {
        uni.showModal({
          title: '提示',
          content: '确定取消收藏吗？',
          cancelText: "取消", // 取消按钮的文字  
          confirmText: "确认", // 确认按钮文字  
          showCancel: true, // 是否显示取消按钮，默认为 true
          confirmColor: 'red',
          cancelColor: '#899fff',
          success: (res) => {
            if (res.confirm) {
              this.ifLike = 0
              //调用父方法，‘刷新’收藏数  likeCancel()
              this.$emit('fatherLikeCancel');
              uni.request({
                url: '/api/user/colletion/deleted',
                method: 'POST',
                data: {
                  couId: this.couId,
                  // couCollNum: this.CartData.couCollNum
                },
                header: {
                  "Content-Type": "application/x-www-form-urlencoded",
                  "Authorization": "Bearar " + uni.getStorageSync("userId")
                },
                success: (res) => {

                }

              })

            } else if (res.cancel) {

            }
          }
        });
      },


      //点赞
      give() {
        if (!uni.getStorageSync("userId")) {
          uni.showModal({
            title: '提示',
            content: '未登录，登录才能操作！',
            success: function(res) {
              if (res.confirm) {
                uni.navigateTo({
                  url: '/pages/login/login'

                });
              } else if (res.cancel) {

              }
            }
          });

        } else {
          this.ifGive = 1
          //调用父方法，‘刷新’赞数  give()
          this.$emit('fatherGive');
          uni.request({
            url: '/api/course/course/click',
            method: 'POST',
            data: {
              couId: this.couId,
            },
            header: {
              "Content-Type": "application/x-www-form-urlencoded",
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            success: (res) => {
              // this.ifGive = res.data.data.isClick

            }

          })
        }
      },
      //取消点赞
      giveCancel() {

        this.ifGive = 0
        //调用父方法，‘刷新’赞数  giveCancel()
        this.$emit('fatherGiveCancel');
        uni.request({
          url: '/api/course/course/cancelClick',
          method: 'POST',
          data: {
            couId: this.couId,
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            // this.ifGive = res.data.data.isClick

          }

        })

      },
      //点击购物车，就会加入并且会提示
      open() {
        if (!uni.getStorageSync("userId")) {

          uni.showModal({
            title: '提示',
            content: '未登录，登录才能操作！',
            success: function(res) {
              if (res.confirm) {
                uni.navigateTo({
                  url: '/pages/login/login'

                });
              } else if (res.cancel) {

              }
            }
          });

        } else {
          this.show = true;
          if (this.ifbuy == 0) {
            this.ifbuy = 1;
            uni.request({
              url: "/api/user/cart/add",
              method: 'POST',
              data: {
                couId: this.couId
              },
              header: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Authorization": "Bearar " + uni.getStorageSync("userId")
              },
              success: (res) => {

              }
            })
          }
        }

      },
      //点击确定，就会前往购物车页面
      toCart() {
        uni.navigateTo({
          url: "/pages/cart/cart"
        })
      },
      //已经购买就去学习
      toStudy() {
        console.log('跳转到学习')
        // uni.navigateTo({
        //   url:'/pages/study/study'
        // })
        uni.switchTab({
          url: '/pages/study/study'
        })
      }

    }
  }
</script>

<style lang="scss" scoped>
  .cartBottom {
    .cartBottom-wrapper {
      position: fixed;
      bottom: 0;
      left: 0;

      width: 100%;
      height: 100rpx;
      background-color: white;


      display: flex;
      flex-direction: row;
      justify-content: flex-end;

      .left {
        display: flex;
        flex-direction: row;
        width: 100%;
        // padding-top: 20rpx;
        align-items: center;
        justify-content: space-around;

        .set {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 72rpx;

          text {
            font-size: 20rpx;
            opacity: 0.8;
          }


          image {
            width: 40rpx;
            height: 40rpx;
          }
        }


      }

      .right {
        height: 100%;
        width: 100%;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: center;
        // justify-content: space-around;
        // background-color: #7f94ff;
        background: repeating-linear-gradient(to right, #80a6ff, #7f94ff);
        padding-left: 20rpx;

        .money {
          display: flex;
          justify-content: center;
          align-items: center;

          image {
            width: 48rpx;
            margin-left: 8rpx;
            height: 48rpx;
          }
        }

        .set-one {
          font-size: 48rpx;
          color: white;
        }

        .set-two {
          font-size: 28rpx;
          color: white;
        }

        .set-three {
          font-size: 28rpx;
          color: white;
        }


      }



    }
  }
</style>
