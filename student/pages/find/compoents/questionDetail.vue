<template>
  <view class="question-detail">
    <view class="content-wrapper">
      <view class="q-title">
        <text>{{questionDetail.title}}</text>
      </view>
      <view class="detail">
        <view class="up">
          <view class="left">
            <u-avatar :src="questionDetail.image.replace(/[\r\n]/g,'')"></u-avatar>
            <text class="one">{{questionDetail.userName}}</text>
            <u-tag text="学生" size="mini" shape="square" mode="light" type="info" />
          </view>
          <text class="two">{{questionDetail.createTime}}</text>
        </view>
        <view class="content">
          <u-read-more :toggle="true">
            <view class="u-content">
              <u-parse :html="questionDetail.content"></u-parse>
              <view v-for="(imgItem,index) in questionDetail.imgs " :key="index">
                <image v-if="questionDetail.imgs!=null" :src="imgItem" @click="previewImg(index)" mode="widthFix">
                </image>
              </view>
            </view>
          </u-read-more>
        </view>
      </view>
      <view class="down">
        <view class="classify">
          <view class="classify-one">
            <u-tag @click="maxSearch" :index="questionDetail.maxName" :text="questionDetail.maxName" shape="square"
              mode="light" type="success" />
          </view>
          <view class="classify-two">
            <u-tag @click="minSearch" :index="questionDetail.minName" :text="questionDetail.minName" size="mini"
              shape="square" mode="dark" type="info" />
          </view>

        </view>
        <view class="down-funtion">
          <image @click="questionGive" v-show="questionDetail.isClick==0" src="../../../static/find/cancelGive.png"
            mode="aspectFit">
          </image>
          <image @click="questionCancel" v-show="questionDetail.isClick==1" src="../../../static/find/give.png"
            mode="aspectFit">
          </image>
          <text>{{questionDetail.clicks}}</text>
          <image src="../../../static/wx_login.png"></image>
          <text>分享</text>

        </view>
      </view>
      <!-- 观点 -->
      <view class="point">
        <view class="point-up">
          <view class="vertical-bar"></view>
          <text>观点</text>
          <text>({{questionDetail.count}})</text>
        </view>
        <view class="point-content">
          <view class="comment-wrapper" v-for="(commentItem,index) in comments" :key="index">
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
              <view class="three">
                <text>{{commentItem.content}}</text>
                <view class="accept" v-show="questionDetail.isPublish==1">
                  <!-- /* 以下形式在微信小程序会无效，APP和H5有效 */ -->
                  <u-button v-if="commentItem.isSelect==1"
                    @click="cancelAdopt(commentItem.queId),commentItem.isSelect=0" class="custom-style" type="success"
                    size="mini">已采纳</u-button>
                  <u-button v-if="commentItem.isSelect==0"
                    @click="accept(commentItem.answeredTqId,commentItem.queId),commentItem.isSelect=1"
                    class="custom-style" size="mini">
                    采纳</u-button>
                </view>
              </view>
            </view>
          </view>
        </view>
      </view>

    </view>
    <!-- <u-divider v-show="comments.length!=0" color="#aaaa7f" half-width="200" border-color="#6d6d6d">没有更多了</u-divider> -->
    <u-empty v-show="comments.length==0" text="该问题暂时还没有观点,客官要不来一条" icon-color="#7f94ff" mode="message"></u-empty>
    <publishBottom :reply="reply" @fatherMethod="getQuestionData(true)" @commentAdd="commentAdd()"></publishBottom>
    <u-back-top :scroll-top="scrollTop" :bottom="300" :iconStyle="iconStyle" :customStyle="customStyle"></u-back-top>
  </view>
</template>

<script>
  import publishBottom from "./pulishBotom.vue"
  export default {
    components: {
      publishBottom
    },
    data() {
      return {
        comments: [],
        queId: '',
        questionDetail: {},
        clicks: 0,
        page: 1,
        content: '',
        //滑动距离
        scrollTop: 0,
        // 返回顶部样式
        iconStyle: {
          fontSize: '32rpx',
          color: '#ffffff'
        },
        customStyle: {
          backgroundColor: '#7f94ff',
        },
        // 返回顶部样式 end
        // 子组件资讯发表观点需要的数据
        reply: {
          userId: '202361020504301',
          aid: '',
        }
      }

    },
    //下拉刷新
    onPullDownRefresh() {
      this.page = 1
      this.comments = []
      this.getQuestionData()
      this.getQuestionDataUp()
      setTimeout(function() {
        uni.stopPullDownRefresh(); //停止下拉刷新动画
      }, 2000);
    },
    //回到顶部
    onPageScroll(e) {
      this.scrollTop = e.scrollTop;
    },
    // 上拉加载
    onReachBottom() {
      this.page += 1
      this.getQuestionData(false)
    },
    onReady() {
      uni.showLoading({
        title: '加载中',
        mask: true
      });
    },


    methods: {
      //点击大类标签，去搜索
      maxSearch(index) {
        uni.navigateTo({
          url: '/pages/find/compoents/toSearchQuestion?index=' + index
        })

      },
      //点击小类标签，去搜索
      minSearch(index) {
        uni.navigateTo({
          url: '/pages/find/compoents/toSearchQuestion?index=' + index
        })
      },
      //预览图片
      previewImg(index) {
        let photoList = this.questionDetail.imgs.map(item => {
          return item;
        });
        uni.previewImage({
          current: index,
          urls: photoList
        });
      },
      //获取问题详情上部分
      getQuestionDataUp() {
        uni.request({
          method: 'GET',
          url: "/api/interaction/wenda/issueVODetail",
          data: {
            queId: this.queId,
          },
          success: (res) => {
            this.questionDetail = res.data.data
            this.clicks = this.questionDetail.clicks
            //end
          },
        });
      },
      //刷新观点数+1,子组件调用
      commentAdd() {
        this.questionDetail.count = this.questionDetail.count + 1
      },


      //获取问题详情下部分(评论)
      getQuestionData(index) {
        if (index) {
          this.page = 1
        }

        uni.request({
          method: 'GET',
          url: "/api/interaction/wenda/replyVOSDetail",
          data: {
            current: this.page,
            queId: this.queId,

          },
          success: (res) => {

            // 给reply数组赋值
            this.reply.aid = this.queId
            this.reply.userId = this.userId
            //end
            //观点
            if (index) {
              this.comments = []
              this.comments = res.data.data.replyVOs;
              //滚动
              uni.pageScrollTo({
                scrollTop: this.scrollTop / 2,
                // duration: 100
              })
            }
            if (res.data.data.replyVOs.length != 0 && !index) {
              this.comments = this.comments.concat(res.data.data.replyVOs); //将数据拼接在一起
            }
            if (res.data.data.replyVOs.length === 0 && index == false) {
              uni.showToast({
                title: '没有更多观点了',
                icon: 'none',
                mask: false
              })
            }
          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
          }
        });
      },

      //点赞
      questionGive() {
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
          this.questionDetail.clicks = this.questionDetail.clicks + 1
          this.questionDetail.isClick = 1
          uni.request({
            method: 'POST',
            url: "/api/interaction/wenda/click",
            data: {
              queId: this.queId,

            },
            header: {
              "Content-Type": "application/x-www-form-urlencoded",
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            success: (res) => {
              // this.questionDetail.clicks = res.data.data.clicks
              // this.questionDetail.isClick = res.data.data.isClick
            }
          });
        }
      },
      //取消赞
      questionCancel() {
        this.questionDetail.clicks = this.questionDetail.clicks - 1
        this.questionDetail.isClick = 0
        uni.request({
          method: 'POST',
          url: "/api/interaction/wenda/cancelClick",
          data: {
            queId: this.queId,
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            // this.questionDetail.clicks = res.data.data.clicks
            // this.questionDetail.isClick = res.data.data.isClick
          }
        });
      },
      //点赞和取消end


      //采纳
      accept(queId, replyId) {
        //限制一条采纳
        for (let s of this.comments) {
          if (replyId === s.answeredTqId) {
            continue
          } else {
            s.isSelect = 0
          }
        }
        uni.request({
          method: 'POST',
          url: '/api/interaction/wenda/adopt',
          data: {
            queId: queId,
            replyId: replyId,
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {

          }

        })
      },
      //取消采纳
      cancelAdopt(replyId) {
        //限制一条采纳
        for (let s of this.comments) {
          if (replyId === s.answeredTqId) {
            continue
          } else {
            s.isSelect = 0
          }


        }
        uni.request({
          method: 'POST',
          url: '/api/interaction/wenda/cancelAdopt',
          data: {
            replyId: replyId,
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {

          }

        })
      },

    },

    onLoad(e) {
      this.queId = e.id
      this.comments = []
      this.page = 1
      this.getQuestionDataUp()
      this.getQuestionData()
    },

  }
</script>

<style lang="scss" scoped>
  .question-detail {
    padding-bottom: 130rpx;
    background-color: #FFFFFF;

    .content-wrapper {
      margin-left: 1.5%;
      width: 97%;
      display: flex;
      // padding: 0 10rpx 0 10rpx;
      flex-direction: column;

      .q-title {
        padding: 20rpx;
        padding-bottom: 5rpx;
        background-color: #FFFFFF;

        text {
          font-size: 40rpx;
          font-weight: bolder;
        }
      }

      .detail {
        display: flex;
        padding-top: 15rpx;
        background-color: white;
        flex-direction: column;

        .up {
          display: flex;
          align-items: center;
          padding-left: 20rpx;
          justify-content: space-between;

          .left {
            display: flex;
            align-items: center;

            image {
              width: 50rpx;
              height: 50rpx;
            }

            .one {
              margin-left: 10rpx;
              margin-right: 10rpx;
              font-weight: bold;
            }
          }

          .two {
            font-size: 24rpx;
            margin-left: 10rpx;
            opacity: 0.7;
            margin-right: 20rpx;
          }
        }

        .content {
          .u-content {
            // display: flex;
            margin-top: 10rpx;
            margin-left: -38rpx;
            margin-right: 60rpx;
            // padding-left: 20rpx;
            padding-right: 20rpx;
            padding-bottom: 20rpx;

            image {
              width: 100%;
            }
          }
        }
      }

      .down {
        .classify {
          margin-top: 10rpx;
          display: flex;
          margin-left: 15rpx;
          align-items: center;

          .classify-two {
            margin-left: 10rpx;
          }

        }

        .down-funtion {
          margin-left: 20rpx;
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
