<template>
  <!-- 这里是搜索资讯的页面 -->
  <view class="to-search-info">
    <view class="search-wrap">
      <u-search input-align="center" placeholder="请输入要搜索的资讯" :focus="true" action-text="取消" height="56"
        v-model="keyword" @search="toSearch" @custom="cancel">
      </u-search>
    </view>
    <u-empty v-show="none" text="没有该资讯,要不换个关键字" icon-color="#7f94ff" color="black" mode="search"></u-empty>
    <!-- 搜索资讯列表 -->
    <view class="info-wrapper" v-show="infoResult.length!=0">
      <view class="info-item" @click="goInfoDetail(item.infoId)" v-for="item in infoResult " :key="item.idex">
        <view class="info-title">
          <text>{{item.infoTitle}}</text>
        </view>
        <view class="info-author">
          <text>{{item.infoAuthor}}</text>
        </view>
        <view class="info-content">
          <text class="info-content-set">{{item.infoMain.replace(/<[^>]+>/gi,'')}}</text>
        </view>
        <view class="info-data">
          <view class="info-data-give">
            <text>{{item.infoLike}} 点赞</text>
          </view>
          <view class="info-data-comment">
            <text>{{item.count}} 评论</text>
          </view>
          <view class="info-data-time">
            <text>发表时间：{{item.createTime}}</text>
          </view>
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
        infoResult: [],
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
      // 去资讯详情
      goInfoDetail(id) {
        uni.navigateTo({
          url: "/pages/find/compoents/infoDetail?id=" + id
        })
      },
      //去搜索内容
      toSearch(index) {
        uni.showLoading({
          title: '搜索中',
          mask: true
        });
        if (index) {
          this.page = 1
          this.infoResult = []
        }
        uni.request({
          method: 'GET',
          url: "/api/interaction/information/search",
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
              this.infoResult = this.infoResult.concat(res.data.data.data)
              this.page += 1
            }
            if (this.infoResult.length == 0) {
              this.none = true;
            } else {
              this.none = false;
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

    }
  }
</script>

<style lang="scss" scoped>
  .to-search-info {
    .search-wrap {
      margin: 0 20rpx;
      margin-top: 15rpx;
      flex: 1;
    }

    .info-wrapper {

      .info-item:hover {
        box-shadow: 0 4px 24px -10px rgba(0, 0, 0, .44);
      }

      .info-item {
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        padding-left: 20rpx;
        width: 94%;
        margin-left: 3%;
        margin-top: 20rpx;
        padding: 15rpx;
        padding-left: 20rpx;
        padding-right: 20rpx;
        max-height: 250rpx;
        border-radius: 10rpx;
        box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
        transition: box-shadow 0.3s ease-in-out;
        /* border-bottom: 2px solid midnightblue; */
        /* box-shadow: 0 16rpx 16rpx rgba(20, 19, 19, 0.2), 0 0 16rpx rgba(20, 19, 19, 0.1); */
        background: rgba(255, 255, 255, 1);

        .info-title {
          font-size: 36rpx;
          font-weight: bolder;
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
        }

        .info-author {
          font-size: 26rpx;
          color: #888888;
        }

        .info-content-set {
          /* margin-left: 20rpx; */
          margin-right: 15rpx;
          margin-bottom: 10rpx;
          display: -webkit-box;
          overflow: hidden;
          font-size: 28rpx;
          font-weight: 500;
          text-overflow: ellipsis;
          word-wrap: break-word;
          white-space: normal !important;
          //显示行
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }

        .info-data {
          // opacity: 0.5;
          color: #888888;
          font-size: 26rpx;
          // margin-top: 10rpx;
          display: flex;
          flex-direction: row;
          justify-content: space-between;
        }

      }
    }
  }
</style>
