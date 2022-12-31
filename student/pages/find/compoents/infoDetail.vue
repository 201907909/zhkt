<template>
  <!-- 资讯的详情页面 -->
  <view class="infoDetail">
    <view class="content-wrapper">
      <!--  <view class="time-wrapper">
        <text>{{infoDetail.createTime}}</text>
      </view> -->
      <view class="info">
        <view class="up">
          <view class="title">{{infoDetail.infoTitle}}</view>
        </view>
        <view class="info-center">
          <view class="userIfo">
            <u-avatar :src="infoDetail.image.replace(/[\r\n]/g,'')"></u-avatar>
            <text>{{infoDetail.infoAuthor}}</text>
            <u-tag text="教师" size="mini" shape="square" mode="light" type="info" />
          </view>
          <view class="time">{{infoDetail.createTime}}</view>
        </view>

        <view class="page">
          <view class="u-content">
            <u-parse :html="infoDetail.infoMain" :show-with-animation="true"></u-parse>
          </view>
        </view>

      </view>
      <view class="down">
        <view class="down-text">
          <!-- <text>转载</text> -->
        </view>
        <view class="down-funtion">
          <image @click="infoGive" v-show="infoDetail.recommend==0" src="../../../static/find/cancelGive.png"
            mode="aspectFit">
          </image>
          <image @click="infoCancel" v-show="infoDetail.recommend==1" src="../../../static/find/give.png"
            mode="aspectFit">
          </image>
          <text>{{infoDetail.infoLike}}</text>
          <image src="../../../static/wx_login.png"></image>
          <text>分享</text>
        </view>
      </view>
      <view class="point">
        <view class="point-up">
          <view class="vertical-bar"></view>
          <text>观点</text>
          <text>({{infoDetail.count}})</text>
        </view>
        <view class="point-content">
          <view class="comment-wrapper" v-for="(commentItem,index) in comments" :key="commentItem.id">
            <view class="left">
              <u-avatar :src="commentItem.image.replace(/[\r\n]/g,'')" mode="circle"></u-avatar>
            </view>
            <view class="right">
              <view class="right-up">
                <text class="one">{{commentItem.userName}}</text>
                <u-tag v-show="commentItem.role==0" text="教师" size="mini" shape="square" mode="light" type="info" />
                <u-tag v-show="commentItem.role==1" text="学生" size="mini" shape="square" mode="light" type="info" />
              </view>
              <text class="two">{{commentItem.createTime}}</text>
              <text class="three">{{commentItem.comContent}}</text>
            </view>
          </view>
        </view>
      </view>

    </view>
    <!-- <u-divider v-show="comments.length!=0" color="#aaaa7f" half-width="200" border-color="#6d6d6d">没有更多了</u-divider> -->
    <u-empty v-show="comments.length==0" text="该资讯暂时还没有观点,客官要不来一条" icon-color="#7f94ff" mode="message"></u-empty>
    <pulish-botom :comment="comment" @fatherMethod="getInfoData(true)" @commentAdd="commentAdd()"></pulish-botom>
    <u-back-top :scroll-top="scrollTop" :bottom="300" :iconStyle="iconStyle" :customStyle="customStyle"></u-back-top>
  </view>
</template>

<script>
  import pulishBotom from "./pulishBotom.vue"

  export default {
    components: {
      pulishBotom
    },
    data() {
      return {
        // 子组件资讯发表观点需要的数据
        comment: {
          infoId: '',
        },
        scrollTop: 0,
        page: 1,
        // 返回顶部样式
        iconStyle: {
          fontSize: '32rpx',
          color: '#ffffff'
        },
        customStyle: {
          backgroundColor: '#7f94ff',
        },
        // 返回顶部样式 end
        infoId: '',
        infoDetail: {},
        comments: [],
        infoLike: 0,

      };

    },
    onReady() {
      uni.showLoading({
        title: '加载中',
        mask: true
      });
    },
    //回到顶部
    onPageScroll(e) {
      this.scrollTop = e.scrollTop;
    },
    // 上拉加载
    onReachBottom() {
      this.page += 1
      this.getInfoData(false)
    },
    //下拉刷新
    onPullDownRefresh() {
      this.page = 1
      this.comments = []
      this.getInfoDataUP()
      this.getInfoData()
      setTimeout(function() {
        uni.stopPullDownRefresh(); //停止下拉刷新动画
      }, 2000);
    },
    methods: {
      //预览图片
      // previewImg(photoImg) {

      // },
      //刷新观点数+1,子组件调用
      commentAdd() {
        this.infoDetail.count = this.infoDetail.count + 1
      },
      //获取该资讯详情上部分
      getInfoDataUP() {
        uni.request({
          method: 'GET',
          url: "/api/interaction/information/DetailInfoByInfoId",
          data: {
            // current: this.page,
            infoId: this.infoId,
          },
          success: (res) => {
            this.infoDetail = res.data.data
            this.infoLike = this.infoDetail.infoLike

          },

        });
      },
      //获取该资讯详请下部分(评论)
      getInfoData(index) {
        if (index) {
          this.page = 1
        }
        uni.request({
          method: 'GET',
          url: "/api/interaction/information/commentByInfoId",
          data: {
            current: this.page,
            infoId: this.infoId,
          },
          success: (res) => {
            //给comment数组赋值
            this.comment.infoId = this.infoId
            //end
            // 观点
            if (index) {
              this.comments = []
              this.comments = res.data.data.commentVOs
              //滚动
              uni.pageScrollTo({
                scrollTop: this.scrollTop / 2,
                // duration: 100
              })
            }
            if (res.data.data.commentVOs.length != 0 && !index) {
              this.comments = this.comments.concat(res.data.data.commentVOs) //将数据拼接在一起

            }
            if (res.data.data.commentVOs.length === 0 && index == false) {
              uni.showToast({
                title: '没有更多观点了',
                icon: 'none',
                mask: false
              })
            }

          },
          complete() {
            //关闭校验中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
          }
        });
      },
      // 点赞
      infoGive() {
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

          this.infoDetail.recommend = 1
          this.infoDetail.infoLike = this.infoDetail.infoLike + 1
          uni.request({
            method: 'POST',
            url: "/api/interaction/information/approval",
            data: {
              infoId: this.infoId,
              // infoLike: this.infoLike,
              //是否点赞
              // recommend: this.infoDetail.recommend,
            },
            header: {
              "Content-Type": "application/x-www-form-urlencoded",
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },

            success: (res) => {
              // this.infoDetail.infoLike = res.data.data.clicks
              // this.infoDetail.recommend = res.data.data.recommend

            }
          });
        }
      },
      //取消赞
      infoCancel() {
        this.infoDetail.recommend = 0
        this.infoDetail.infoLike = this.infoDetail.infoLike - 1
        uni.request({
          method: 'POST',
          url: "/api/interaction/information/cancelApproval",
          data: {
            infoId: this.infoId,
            // infoLike: this.infoLike,
            // recommend: this.infoDetail.recommend,

          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },

          success: (res) => {
            // this.infoDetail.infoLike = res.data.data.clicks
            // this.infoDetail.recommend = res.data.data.recommend
          }
        });
      }

    },

    onLoad(e) {
      this.infoId = e.id
      this.commentVOs = []
      this.page = 1
      this.getInfoDataUP()
      this.getInfoData()


    },



  }
</script>

<style lang="scss" scoped>
  .infoDetail {

    padding-bottom: 130rpx;
    background-color: #FFFFFF;

    .content-wrapper {
      padding: 20rpx;
      padding-bottom: 1rpx;
      display: flex;
      flex-direction: column;

      .time-wrapper {
        display: flex;

        flex-direction: row;
        justify-content: center;

        text {
          font-size: 18rpx;
          opacity: 0.5;
          font-weight: bold;
          // 字体间距属性 
          letter-spacing: 10rpx;
        }
      }

      .info {
        margin-top: 10rpx;
        background-color: white;


        display: flex;
        flex-direction: column;

        .up {
          padding-bottom: 5rpx;
          background-color: #FFFFFF;

          .title {
            font-size: 40rpx;
            font-weight: bolder;
          }
        }

        .info-center {
          display: flex;
          justify-content: space-between;
          align-items: center;

          .time {
            font-size: 24rpx;
            margin-left: 10rpx;
            opacity: 0.7;
            margin-right: 20rpx;
          }
        }

        .userIfo {
          display: flex;
          flex-direction: row;
          align-items: center;
          background-color: white;
          padding-top: 10rpx;
          padding-bottom: 10rpx;

          text {
            margin-left: 20rpx;
            margin-right: 10rpx;
            font-weight: bold;
          }

          image {
            width: 80rpx;
            height: 80rpx;
          }

        }

        .page {
          background-color: white;


          padding-bottom: 10rpx;
        }



      }

      .down {

        .down-text {
          margin-top: 20rpx;

          text {
            opacity: 0.8;
            font-size: 12px;
          }
        }

        .down-funtion {
          display: flex;
          flex-direction: row;
          margin-top: 20rpx;
          align-items: center;

          image {
            width: 30rpx;
            height: 30rpx;

          }

          text {
            margin-left: 10rpx;
            margin-right: 90rpx;
          }
        }
      }

      //观点一下内容
      .point {
        margin-top: 60rpx;
        display: flex;
        flex-direction: column;
        justify-content: space-between;

        .point-up {
          display: flex;
          flex-direction: row;
          align-items: center;


          .vertical-bar {
            width: 8rpx;
            height: 50rpx;
            background-color: gray;

          }

          text {
            font-weight: bolder;
            font-size: 15px;
            margin-left: 10rpx;
          }
        }

        .point-content {
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
            margin-top: 20rpx;
            border-radius: 15rpx;
            box-shadow: 0 0rpx 16rpx 0 rgba(0, 0, 0, .12);

            .left {
              padding-left: 20rpx;

            }

            .right {
              padding-left: 20rpx;
              padding-right: 10rpx;
              padding-bottom: 5rpx;
              width: 100%;
              display: flex;
              flex-direction: column;

              .right-up {
                .one {
                  margin-right: 16rpx;
                }
              }

              .one {
                font-size: 10px;
                font-weight: bold;
              }

              .two {
                margin-top: 5rpx;
                opacity: 0.5;
                font-size: 8px;
                margin-bottom: 10rpx;
              }

              .three {
                display: flex;
                justify-content: space-between;

                text {
                  font-size: 5px;
                  opacity: 0.9;
                }

                margin-bottom: 20rpx;

                .accept {
                  margin-right: 14rpx;

                  .custom-style {
                    height: 50rpx;
                    width: 90rpx;
                  }
                }

              }
            }

          }
        }
      }
    }




  }
</style>
