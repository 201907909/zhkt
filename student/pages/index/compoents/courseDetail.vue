<template>
  <!-- 课程详情页 -->
  <view class="courseDetail">
    <view class="image-wrap">
      <image :src="courseDetail.couPic.replace(/[\r\n]/g,'')" mode="aspectFill"></image>
    </view>
    <view class="course-data">
      <view class="course-data-box">
        <view class="vertical-bar"></view>
        <text style="font-weight: bold;margin-top: 10rpx;">{{courseDetail.couName}}</text>
        <view class="classify">
          <view class="classify-one">
            <u-tag @click="maxSearch" :index="courseDetail.maxTypeName" :text="courseDetail.maxTypeName" shape="square"
              mode="dark" type="info" />
          </view>
          <view class="classify-two">
            <u-tag @click="minSearch" :index="courseDetail.minTypeName" :text="courseDetail.minTypeName" size="mini"
              shape="square" mode="light" type="info" />
          </view>
        </view>
      </view>
      <view class="course-data-funtion">
        <view class="give-wrap">
          <image style="width:30rpx;height: 30rpx;" src="/static/index/give.png"></image>
          <text>{{courseDetail.recommendNum}}</text>
        </view>
        <view class="love-wrap">
          <image style="width:30rpx;height: 30rpx;" src="/static/index/like.png"></image>
          <text>{{courseDetail.couCollNum}}</text>
        </view>
        <view class="share-wrap">
          <image style="width:30rpx;height: 30rpx;" src="/static/index/wx_login.png"></image>
          <text>{{courseDetail.couShareNum}}</text>
        </view>
      </view>
    </view>

    <view class="tabs-wrap">
      <u-tabs name="cate_name" count="cate_count" :list="list" :is-scroll="false" :current="current" @change="change">
      </u-tabs>
    </view>
    <view class="content_page">
      <view v-if="current == 0">
        <introduction :couId="couId"></introduction>
      </view>
      <view v-if="current == 1">
        <directory :couId="couId"></directory>
      </view>
      <view v-if="current == 2">
        <appraise :couId="couId" ref="childAppraise"></appraise>
      </view>
      <cart-bottom @fatherGive="give" @fatherGiveCancel="giveCancel" @fatherLike="like" @fatherLikeCancel="likeCancel"
        :couId="couId">
      </cart-bottom>
    </view>
  </view>
</template>

<script>
  // 简介
  import introduction from "./introduction.vue"
  // 目录
  import directory from "./directory.vue"
  // 评价
  import appraise from './appraise.vue'
  //底部购物栏
  import cartBottom from "./cartBottom.vue"

  export default {
    components: {
      introduction,
      directory,
      appraise,
      cartBottom
    },
    data() {
      return {
        current: 0,
        // 课程雪花Id
        couId: 0,
        courseDetail: [],
        list: [{
          cate_name: '简介'
        }, {
          cate_name: '目录'
        }, {
          cate_name: '评价',
        }],

      }
    },
    //下拉刷新
    onPullDownRefresh() {
      this.getCourseDetail()
      setTimeout(function() {
        uni.stopPullDownRefresh(); //停止下拉刷新动画
      }, 1000);
    },
    //上拉加载 
    onReachBottom() {
      // appraise 页面有效
      if (this.current == 2) {
        this.$refs.childAppraise.getCouserAppraise(true); // 调用子组件的方法getCouserAppraise()
      }

    },
    methods: {

      //刷新赞数+1,子组件调用
      give() {

        this.courseDetail.recommendNum = this.courseDetail.recommendNum + 1
      },
      //刷新赞数-1，子组件调用
      giveCancel() {
        this.courseDetail.recommendNum = this.courseDetail.recommendNum - 1
      },
      //刷新收藏数+1,子组件调用
      like() {
        this.courseDetail.couCollNum = this.courseDetail.couCollNum + 1
      },
      //刷新收藏数-1，子组件调用
      likeCancel() {
        this.courseDetail.couCollNum = this.courseDetail.couCollNum - 1
      },
      //点击大类标签，去搜索
      maxSearch(index) {
        uni.navigateTo({
          url: 'searchPage?index=' + index
        })

      },
      //点击小类标签，去搜索
      minSearch(index) {
        uni.navigateTo({
          url: 'searchPage?index=' + index
        })
      },
      //tabs改变切换页面
      change(index) {
        this.current = index
      },
      //获取课程详情
      getCourseDetail() {
        uni.request({
          method: "GET",
          url: '/api/course/course/courseDetail1',
          data: {
            couId: this.couId
          },
          success: (res) => {
            this.courseDetail = res.data.data

          },
          complete() {

          }
        })

      }
    },
    onLoad(option) {
      uni.showLoading({
        title: '加载中',
        mask: true
      });
      this.couId = option.couId //上个页面传递的参数。
      this.getCourseDetail()
    }
  }
</script>

<style lang="scss" scoped>
  .courseDetail {
    background-color: #FFFFFF;

    .image-wrap {
      height: 300rpx;
      width: 100%;
      margin-bottom: 15rpx;

      image {
        width: 100%;
        height: 100%;


      }
    }

    .course-data {
      width: 100%;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      padding: 10rpx;
      height: 130rpx;
      // padding-bottom: 20rpx;
      box-shadow: 0 16rpx 16rpx rgba(10, 16, 20, 0.24), 0 0 16rpx rgba(10, 16, 20, 0.12);
    }

    .course-data-box {
      margin-bottom: 10rpx;
      display: flex;
      flex-direction: row;
      align-items: center;


      .classify {
        margin-top: 10rpx;
        display: flex;
        align-items: center;
        margin-left: 10rpx;

        .classify-two {
          margin-left: 10rpx;
        }

      }
    }

    .vertical-bar {
      width: 8rpx;
      height: 50rpx;
      margin-right: 10rpx;
      background-color: gray;

    }

    .course-data-funtion {
      display: flex;
      margin-top: 10rpx;
      // flex-direction: row;
      // justify-content: flex-start;
      align-items: center;
      justify-content: space-around;

      .give-wrap {
        // margin-left: 30rpx;

      }

      .love-wrap {
        // margin-left: 30rpx;

      }

      .share-wrap {
        // margin-left: 50%;
      }
    }


  }
</style>
