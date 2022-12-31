<template>
  <view class="container">
    <view class="search-wrap">
      <u-search input-align="center" bg-color="#f8fafc" border-color="#7f94ff" search-icon-color="#7f94ff"
        placeholder="请输入要搜索的课程名" :disabled="true" @click="toSearch" height="56" :keyword="keyword" :show-action="false">
      </u-search>
    </view>
    <!-- 头部轮播 -->
    <view class="swiper-wrap">
      <u-swiper bg-color="#f8fafc" :list="list" :title="true" @click="toFuntion" :effect3d="true" height="350">
      </u-swiper>
    </view>

    <view class="type-wrap">
      <view class="cate-item" @click="toURL(item)" v-for="(item, index) in websiteH5NavigateList" :key="index">
        <image :src="item.img" mode="scaleToFill">
        </image>
        <text>{{item.typeName}}</text>
      </view>
      <view class="cate-item" @click="toURL(true)">
        <image width="100%" height="100%" src="/static/index/more.png" mode="aspectFit"></image>
        <text>更多</text>
      </view>
    </view>
    <view class="middle">
      <u-section font-size="40"
        style="width: 100%;height: 70rpx;margin-top:8rpx;margin-bottom:20rpx;margin-left: 20rpx;margin-right: 15rpx;"
        color="#6374FE" title="推荐课程" sub-title="查看更多" @click="toMore(true)">
      </u-section>
      <view class="course-items" v-for="(item, index) in courseList" :key="index" @click="toDetail(item.couId)">
        <view class="image-wrapper">
          <u-image border-radius="15" width="290" height="195" :src="item.couPic.replace(/[\r\n]/g,'')"
            mode=" aspectFit"></u-image>
          <text class="hover_one">
            {{item.couName}}</text>
        </view>
      </view>
      <!-- <u-divider color="#aaaa7f" half-width="200" border-color="#6d6d6d">到底了</u-divider> -->
    </view>
  </view>
</template>

<script>
  import logicLogic from '../../common/loginLogic.js'
  export default {
    data() {
      return {
        keyword: '',
        //轮播图
        list: [],
        websiteH5NavigateList: [],
        //推荐课程
        courseList: [],
      }
    },
    //下拉刷新
    onPullDownRefresh() {
      this.list = []
      this.websiteH5NavigateList = []
      this.getRecommendCourse()
      uni.showLoading({
        mask: true
      })
      this.getRecommendType()
    },

    created() {
      this.loginUser() //自动登录
    },
    onLoad() {
      uni.showLoading({
        title: '加载中',
        mask: true
      });

      this.list = []
      this.websiteH5NavigateList = []
      this.getRecommendCourse()
      this.getRecommendType()
      // this.loginUser()
    },
    methods: {
      //获取推荐课程
      getRecommendCourse() {
        uni.request({
          method: 'GET',
          url: '/api/course/course/recommendCourse',
          success: (res) => {
            this.courseList = res.data.data
            for (var i = 0; i < this.courseList.length; i++) {
              var temp = {
                couId: '',
                image: '',
                title: ''
              }
              temp.couId = this.courseList[i].couId
              temp.title = this.courseList[i].couIntroduction
              temp.image = this.courseList[i].couPic.replace(/[\r\n]/g, '');
              this.list = this.list.concat(temp)
            }

          },
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          }
        })
      },
      // 获取推荐课程类型
      getRecommendType() {
        uni.request({
          method: 'GET',
          url: '/api/course/course/recommendType',
          success: (res) => {
            for (let s of res.data.data) {
              var temp = {
                couTypeId: "",
                id: 0,
                parentTypeId: '',
                typeName: "",
                img: ''
              }
              temp.couTypeId = s.couTypeId
              temp.id = s.id
              temp.parentTypeId = s.parentTypeId
              temp.typeName = s.typeName
              temp.img = s.img
              this.websiteH5NavigateList = this.websiteH5NavigateList.concat(temp)
            }
          },
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
            setTimeout(function() {
              uni.stopPullDownRefresh(); //停止下拉刷新动画
            }, 1000);
          }

        })
      },

      //轮播图的去课程详情
      toFuntion(index) {
        this.toDetail(this.list[index].couId)
      },

      //去课程详情
      toDetail(courseId) {
        uni.navigateTo({
          url: 'compoents/courseDetail?couId=' + courseId,
        });
      }, //去课程详情
      //去更多
      toMore(index) {
        uni.navigateTo({
          url: 'compoents/moreCourse?index=' + index,
        });
      },
      //去搜索
      toSearch() {
        uni.navigateTo({
          url: 'compoents/searchPage',
        });
      },
      toURL(object) {
        if (object == true) {
          uni.navigateTo({
            url: 'compoents/moreCourse?show=' + object
          });
        } else {

          uni.navigateTo({
            url: 'compoents/moreCourse?couTypeId=' + object.couTypeId

          });
        }

      },

      loginUser() {
        if (uni.getStorageSync('userId')) {
          let account = uni.getStorageSync('account');
          let password = uni.getStorageSync('password');
          logicLogic.login(account, password, function(status, res) {
            if (status) {
              uni.showToast({
                title: '自动登录成功',
                duration: 2000,
              })
            }
          });
        }
      }

    }
  }
</script>

<style lang="scss" scoped>
  .container {
    padding: 0px;
    font-size: 14px;
    line-height: 24px;
    // background-color: #f8fafc;



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

    .swiper-wrap {
      margin-top: 116rpx;
      margin-bottom: 20rpx;
      padding-top: 20rpx;
      padding-bottom: 20rpx;
      margin-left: 3%;
      width: 94%;
      border-radius: 15rpx;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
      background-color: #f8fafc;
    }

    .type-wrap {
      display: flex;
      padding-top: 15rpx;
      padding-bottom: 15rpx;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      width: 94%;
      margin-left: 3%;
      height: 170rpx;
      margin-bottom: 20rpx;
      background-color: #f8fafc;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
      border-radius: 15rpx;
    }

    .cate-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      color: #535353;
      font-family: Microsoft JhengHei
    }

    /* 原图标颜色太深,不想改图了,所以加了透明度 */
    image {
      width: 66rpx;
      height: 66rpx;
      // margin-bottom: 14rpx;
      // border-radius: 50%;
      opacity: 0.7;
    }

    .middle {
      width: 94%;
      // padding: 10rpx;
      padding-top: 10rpx;
      margin-left: 3%;
      margin-bottom: 30rpx;
      // height: 100%;
      background-color: #f8fafc;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
      // box-shadow: 0 7rpx 7rpx rgba(10, 16, 20, 0.24), 0 0 10rpx rgba(10, 16, 20, 0.12);
      border-radius: 15rpx;
      display: flex;
      flex-direction: row;
      align-items: center;
      flex-wrap: wrap;
      justify-content: space-around;
    }

    .course-items {
      display: flex;
      text-align: center;
      justify-content: center;
      align-items: center;
      flex-wrap: wrap;
      width: 328rpx;
      height: 270rpx;
      box-shadow: 0 0 12px 0 rgba(0, 0, 0, .06);
      padding-top: 10rpx;
      background-color: #FFFFFF;
      margin-bottom: 20rpx;
      border-radius: 15rpx;

      .classify-one {
        // position: absolute;
        // z-index: 100;
      }

      .image-wrapper {
        text {
          display: -webkit-box;
          overflow: hidden;
          font-size: 28rpx;
          color: black;
          // font-weight: bold;
          font-family: Microsoft JhengHei; //字体
          text-overflow: ellipsis;
          word-wrap: break-word;
          white-space: normal !important;
          //显示行
          -webkit-line-clamp: 1;
          -webkit-box-orient: vertical;
        }
      }
    }

  }
</style>
