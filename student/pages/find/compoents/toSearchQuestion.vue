<template>
  <!-- 这里搜索问题的详情页面 -->
  <view class="to-search-question">
    <view class="search-wrap">
      <u-search input-align="center" placeholder="请输入要搜索的问题" :focus="true" action-text="取消" height="56"
        v-model="keyword" @search="toSearch(true)" @custom="cancel">
      </u-search>
    </view>
    <u-empty v-show="none" text="没有该问题,要不换个关键字" icon-color="#7f94ff" color="black" mode="search"></u-empty>
    <!-- 搜索资讯列表 -->
    <view class="question-wrapper" v-if="questionResult.length!=0">
      <view class="question-info" @click="goQuestionDetail(item.queId)" v-for="item in questionResult" :key="item.id">
        <view class="info-left">
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
            <text style="opacity: 0.6; margin-left: 30upx;">{{item.count}}评论</text>
            <text style="opacity: 0.6; margin-left: 30rpx;">{{item.createTime}}</text>
          </view>
        </view>
        <view class="image-wrapper" v-if="item.imgs==null">
          <image src="../../../static/find/none.png" mode="aspectFill">
        </view>
        <view class="image-wrapper" v-else>
          <image :src="item.imgs[0]" mode="aspectFill">
          </image>
          </image>
        </view>
      </view>
    </view>
    <u-back-top :scroll-top="scrollTop" :bottom="300" :iconStyle="iconStyle" :customStyle="customStyle"></u-back-top>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        keyword: "",
        //搜索的结果
        questionResult: [],
        //判断搜索是否有该问题
        none: false,
        page: 1,
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
      }
    },
    onLoad(options) {
      //防止出现不是点击标签，也请求
      if (options.index) {
        //从问题的类别点击再搜索
        this.keyword = options.index
        this.toSearch(true)
      }
    },
    //回到顶部
    onPageScroll(e) {
      this.scrollTop = e.scrollTop;
    },
    //上拉加载
    onReachBottom() {
      this.toSearch(false)
    },
    methods: {
      cancel() {
        uni.navigateBack({ //uni.navigateTo跳转的返回，默认1为返回上一级
          delta: 1
        });

      },
      // 去问题详情
      goQuestionDetail(id) {
        uni.navigateTo({
          url: "/pages/find/compoents/questionDetail?id=" + id
        })
      },
      //去搜索内容
      toSearch(index) {

        //清理上一次请求的搜索的结果集
        if (index) {
          uni.showLoading({
            title: '搜索中',
            mask: true
          });
          this.page = 1
          this.questionResult = []
        }
        uni.request({
          method: 'GET',
          url: "/api/interaction//wenda/search",
          data: {
            current: this.page,
            val: this.keyword
          },
          success: (res) => {
            if (res.data.data.data.length == 0) {
              //没有数据就替换一下loadingText的值
              if (!index) {
                uni.showToast({
                  title: '没有更多数据了',
                  icon: 'none',
                  mask: false
                })
              }
            } else {
              // 问题搜索结果集的拼接
              this.questionResult = this.questionResult.concat(res.data.data.data)
              this.page += 1
            }
            if (this.questionResult.length == 0) {
              this.none = true;
            } else {
              this.none = false;
            }

          },
          //失败或成功都哦用
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
          }
        });


      }

    }
  }
</script>

<style lang="scss" scoped>
  .to-search-question {
    .search-wrap {
      margin: 0 20rpx;
      margin-top: 15rpx;
      flex: 1;
    }

    .question-wrapper {

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
  }
</style>
