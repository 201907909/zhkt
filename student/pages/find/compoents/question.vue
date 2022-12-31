<template>
  <view class="question-content">
    <view class="search-wrap">
      <u-search bg-color="#ffffff" border-color="#7f94ff" search-icon-color="#7f94ff" :height="56" inputAlign="center"
        :disabled="true" @click="toSearch" placeholder="请输入你要搜索的问题" v-model="keyword" :show-action="false">
      </u-search>
    </view>
    <view class="question-wrapper">

      <view class="question-info" @click="goQuestionDetail(item.queId)" v-for="item in questionList">
        <view class=" info-left">
          <view class="question-title">
            <text>{{item.title}}</text>
          </view>
          <view class="classify">
            <view class="classify-one">
              <u-tag :text="item.maxName" shape="square" mode="light" type="success" />
            </view>
            <view class="classify-two">
              <u-tag :text="item.minName" size="mini" shape="square" mode="dark" type="info" />
            </view>
          </view>
          <view class="question-detail">
            <text>{{item.content}}</text>
          </view>
          <view class="info">
            <text style="opacity: 0.6;">{{item.clicks}} 点赞</text>
            <text style="opacity: 0.6; margin-left: 30rpx;">{{item.count}}评论</text>
            <text style="opacity: 0.6; margin-left: 30rpx;">{{item.createTime}}</text>
          </view>
        </view>
        <view class="image-wrapper" v-if="item.imgs==null">
          <image src="../../../static/find/none.png" mode="aspectFill">
        </view>
        <view class="image-wrapper" v-else>
          <image :src="item.imgs[0]" mode="aspectFill">
          </image>

        </view>
      </view>
    </view>
    <view class="fab">
      <uni-fab :pattern="pattern" horizontal="right" @fabClick="toAddQuestion">
      </uni-fab>
    </view>
    <view class="empty">
      <u-empty v-show="questionList.length==0" text="问题空荡荡,客观要不来一发" mode="order" icon-color="#7f94ff" color="">
      </u-empty>
    </view>
  </view>

</template>

<script>
  export default {

    data() {
      return {
        keyword: '',
        //悬浮加号的样式
        pattern: {
          buttonColor: "#7f94ff"
        },
        questionList: [],
        page: 1, //页数,
        // //问题总记录数(供父find调用)
        // total: 0

      }
    },
    methods: {
      //去问题详情
      goQuestionDetail(id) {
        uni.navigateTo({
          url: 'compoents/questionDetail?id=' + id
        })
      },

      toAddQuestion() {
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
          uni.navigateTo({
            url: "/pages/find/compoents/addQuestion"
          });
        }
      },
      //去搜索
      toSearch() {
        uni.navigateTo({
          url: '/pages/find/compoents/toSearchQuestion',
        });
      },
      //获取所有问题(分页)
      //index 判断是否下拉和刷新(false)，或者下滑(true)
      getAllQuestion(index) {
        if (!index) {
          this.questionList = []
          this.page = 1
        }
        if (index) {
          this.page += 1; //每触底一次 page +1
        }
        uni.request({
          method: 'GET',
          url: '/api/interaction/wenda/getAllWenDa',
          data: {
            current: this.page //默认第一页
          },
          success: (res) => {
            // //问题总记录数
            // this.total = res.data.data.total
            this.questionList = this.questionList.concat(res.data.data.data); //将数据拼接在一起
            if (index) {
              //如果返回没有数据了,数组长度为0
              if (res.data.data.data.length == 0) {
                //没有数据就替换一下loadingText的值
                uni.showToast({
                  title: '没有更多数据了',
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
            }, 100);
          }
        });
      }

    },

    created() {
      this.getAllQuestion(false);
      uni.showLoading({
        title: '加载中',
        mask: true
      });
    },

  }
</script>

<style lang="scss" scoped>
  .question-content {
    display: flex;
    flex-direction: column;
    justify-content: space-between;

    .search-wrap {
      padding: 0 20rpx;
      padding-top: 20rpx;
      padding-bottom: 20rpx;
      flex: 1;
      background-color: #FFFFFF;
      width: 100%;
    }

    .question-wrapper {
      margin-top: 8rpx;

      .question-info:hover {
        box-shadow: 0 4px 24px -10px rgba(0, 0, 0, .44);
      }

      .question-info {
        background-color: #FFFFFF;
        margin-top: 10rpx;

        width: 94%;
        margin-left: 3%;
        min-height: 250upx;
        padding: 15rpx 20rpx 15rpx 20rpx;
        flex-direction: row;
        display: flex;
        align-items: center;
        justify-content: space-between;

        border-radius: 10rpx;
        box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
        transition: box-shadow 0.3s ease-in-out;


        display: flex;

        .info-left {
          margin-left: 15rpx;
          width: 60%;
          display: flex;
          flex-direction: column;
          flex-wrap: wrap;
          justify-content: space-between;

          .classify {
            margin-top: 10rpx;
            display: flex;
            align-items: center;

            .classify-two {
              margin-left: 10rpx;

            }

          }

          .question-title {
            font-weight: bolder;
            margin-top: 15rpx;
            font-size: 32rpx;
            display: -webkit-box;
            overflow: hidden;
            text-overflow: ellipsis;
            word-wrap: break-word;
            white-space: normal !important;
            //显示行
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
          }

          .question-detail {
            margin-top: 20rpx;
            display: -webkit-box;
            overflow: hidden;
            font-size: 30upx;
            text-overflow: ellipsis;
            word-wrap: break-word;
            white-space: normal !important;
            //显示行
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;

          }

          .info {
            margin-top: 20rpx;
            margin-bottom: 20rpx;
          }

        }

        .image-wrapper {

          image {
            width: 230rpx;

            height: 180rpx;

          }
        }

      }
    }

    .fab {
      opacity: 0.5;
    }
  }

  .empty {
    margin-top: 200rpx;
  }
</style>
