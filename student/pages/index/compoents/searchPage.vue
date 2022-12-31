<template>
  <view class="search-page">
    <view class="search-wrap">
      <u-search input-align="center" bg-color="#f8fafc" border-color="#7f94ff" search-icon-color="#7f94ff"
        placeholder="请输入要搜索的课程名" :focus="true" action-text="取消" height="56" v-model="keyword" @search="toSearch"
        @custom="cancel">
      </u-search>
    </view>
    <view class="search-wrap-temp"></view>
    <u-empty v-show="none" text="没有该课程,要不换个关键字" icon-color="#7f94ff" color="black" mode="search"></u-empty>
    <!-- 搜索课程列表 -->
    <view class="course-list" v-show="courseList.length!=0">
      <view class="courese-wrapper" @click="toDetail(courseItem.couId)" v-for="(courseItem,index) in courseList"
        :key="courseItem.index">
        <view class="image-wrapper">
          <image :src="courseItem.couPic.replace(/[\r\n]/g,'')" mode="scaleToFill"></image>
        </view>
        <view class="center">
          <view class="center-top">
            <text class="set-one">{{courseItem.couName}}</text>
            <text class="set-two">{{courseItem.teaName}}</text>
          </view>
          <view class="center-bottom">
            <image src="../../../static/index/h5-index-course.png" mode="aspectFit"></image>
            <text>{{courseItem.couCataNum}}课时</text>
          </view>
        </view>
        <view class="right">
          <text class="set-text">{{courseItem.couPrice}}</text>
          <image class='set-icon' src="../../../static/index/point.png" mode="aspectFit"></image>
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
        keyword: '',
        //搜索的结果
        courseList: [],
        //判断搜索是否有该课程
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
      //从课程的类别点击再搜索
      //防止出现不是点击标签，也请求
      if (options.index) {
        this.keyword = options.index
        this.toSearch(true)
      }
    },
    methods: {
      //回到顶部
      onPageScroll(e) {
        this.scrollTop = e.scrollTop;
      },
      //上拉加载
      onReachBottom() {
        this.toSearch(false)
      },
      cancel() {
        uni.navigateBack({ //uni.navigateTo跳转的返回，默认1为返回上一级
          delta: 1
        });

      },
      // 去课程详情
      toDetail(id) {
        uni.navigateTo({
          url: 'courseDetail?couId=' + id,
        });
      },
      //去搜索内容
      toSearch(index) {
        uni.showLoading({
          title: '搜索中',
          mask: true
        });
        if (index) {
          this.page = 1
          this.courseList = []
        }
        uni.request({
          method: 'GET',
          url: "/api/course/course/search",
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
              this.courseList = this.courseList.concat(res.data.data.data)
              this.page += 1
            }
            if (this.courseList.length == 0) {
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
        })

      }
    },
  }
</script>

<style lang="scss">
  .search-page {
    .search-wrap {
      padding: 0 20rpx;
      padding-top: 20rpx;
      padding-bottom: 20rpx;
      flex: 1;
      background-color: #f8fafc;
      width: 100%;
      position: fixed;
      top: 0;
      z-index: 999; //一直将导航栏置于最顶层
    }

    .search-wrap-temp {
      width: 100%;
      height: 88rpx;
      margin-bottom: 10rpx;
    }

    .course-list {
      display: flex;
      flex-direction: column;
      // justify-content: space-between;
    }

    .courese-wrapper {
      padding: 6rpx;
      margin-left: 2%;
      margin-top: 14rpx;
      display: flex;
      height: 200rpx;
      width: 96%;
      flex-direction: row;
      justify-content: space-between;
      background-color: white;
      border-radius: 18rpx;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);

      .image-wrapper {
        // padding-top: 20rpx;


        image {
          border-radius: 10%;
          width: 280rpx;
          margin-left: 28rpx;
          height: 180rpx;

        }
      }

      .center {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        // padding-top: 20rpx;
        width: 250rpx;
        margin-left: 20rpx;
        justify-content: space-between;

        .center-top {
          margin-top: 20rpx;

          .set-one {
            display: -webkit-box;
            overflow: hidden;
            font-size: 14px;
            font-weight: bolder;
            text-overflow: ellipsis;
            word-wrap: break-word;
            white-space: normal !important;
            //显示行
            -webkit-line-clamp: 1;
            -webkit-box-orient: vertical;
          }

          .set-two {
            font-size: 13px;
            // padding-top: 30rpx;
            opacity: 0.8;
          }
        }



        .center-bottom {
          // padding-top: 30rpx;
          display: flex;
          flex-direction: row;
          align-items: center;
          margin-bottom: 20rpx;

          image {
            // margin-right: 10rpx;
            width: 45rpx;
            height: 45rpx;
          }

          text {
            font-size: 8px;
            font-weight: bold;
          }


        }

      }


    }

    .right {
      // padding-top: 18rpx;
      display: flex;
      width: 100rpx;
      flex-direction: row;
      justify-content: center;
      align-items: flex-end;
      margin-bottom: 20rpx;
      // justify-content: flex-end;

      .set-text {
        font-size: 17px;
        color: red;
      }

      .set-icon {

        width: 45rpx;
        height: 45rpx;
      }
    }

  }
</style>
