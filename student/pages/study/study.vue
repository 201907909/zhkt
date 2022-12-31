<template>
  <view class="study">
    <!-- 签到 -->
    <view class="sign_in">
      <image :src="headImage" class="headimg"></image>
      <view class="sign_box">
        <text class="study_time">快来学习吧~</text>
        <navigator :url="'/pages/signIn/signIn'">
          <view class="sign_but">签 到</view>
        </navigator>
      </view>
    </view>
    <!-- 学习时长 -->
    <view class="time">
      <view class="see_time">
        今日已学
        <view class="time_num">{{todayTime}}</view>
      </view>
      <view class="see_time">
        本月已学
        <view class="time_num">{{monthTime}}</view>
      </view>
    </view>
    <!-- 学习课程分类  -->
    <view class="classify">
      <view class="btu" @click="selectCourse1()" :class="selected1 == true ? 'active' : ''">
        班级课程
      </view>
      <view class="btu" @click="selectCourse2()" :class="selected1 == false ? 'active' : ''">
        自选课程
      </view>
    </view>
    <!-- 班级课程 -->
    <view v-show="selected1">
      <view class="context" v-show="courses.length!=0">
        <uni-collapse class="uni-col">
          <uni-collapse-item v-for="course in courses" :title="course.className" :thumb="course.classLogo" >
            <view class="course_item" v-for="c in course.courseProgressVOList"
              @click="toClassPlay(c.couId,c.chaId,c.url,c.startTime,course.classId)">
              <view class=" left">
                <image :src="c.couPic.replace(/[\r\n]/g,'')" class="courseimg"></image>
                <view class="descrip">
                  <view class="name">
                    <view class="course_name">{{c.couName}}</view>
                    <view class="author_name">{{course.teaName}}</view>
                  </view>
                  <progress :percent="c.ratio" show-info stroke-width="3" />
                </view>
              </view>
              <image class="iconimg" src="/static/user/20.png" mode="aspectFit"></image>
            </view>
          </uni-collapse-item>
        </uni-collapse>
      </view>
      <!-- 内容为空  -->
      <view class="empty" v-show="courses.length==0">
        <image src="../../static/img/nodata.png" mode=""></image>
        <view class="tag">暂无数据</view>
      </view>
    </view>
    <!-- 自选课程 -->
    <view v-show="!selected1">
      <view class="context" v-show="myCourses.length!=0">
        <view class=" course_item" v-for="c in myCourses" @click="toClassPlay(c.couId,c.chaId,c.url,c.startTime)">
          <view class=" left">
            <image :src="c.couPic.replace(/[\r\n]/g,'')" class="courseimg"></image>
            <view class="descrip">
              <view class="name">
                <view class="course_name">{{c.couName}}</view>
                <view class="author_name">{{c.teaName}}</view>
              </view>
              <progress :percent="c.ratio" show-info stroke-width="3" />
            </view>
          </view>
          <image class="iconimg" src="/static/user/20.png" mode="aspectFit"></image>
        </view>
      </view>

      <!-- 内容为空  -->
      <view class="empty" v-show="myCourses.length==0">
        <image src="../../static/img/nodata.png" mode=""></image>
        <view class="tag">暂无数据</view>
      </view>
    </view>
  </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        selected1: true,
        selectCourse: true,
        headImage: '/static/img/true.png',
        stuId: "",
        courses: [],
        myCourses: [],
        todayTime: '0',
        monthTime: '0',
        classId: "",
      };
    },
    onLoad() {
      // var storageUserId = uni.getStorageSync('userId');
      // if (storageUserId != null && storageUserId != "") {
      //   this.stuId = storageUserId;
      //   this.getMyAlIReply();
      //   this.getcourseDetail();
      //   this.getTodayTime();
      //   this.getMonthTime();
      // } else {
      //   uni.navigateTo({
      //     url: '/pages/login/login'
      //   });
      // }
    },
    onShow() {
      if (uni.getStorageSync('userId')) {
        this.stuId = uni.getStorageSync('userId');
        this.getMyAlIReply();
        this.getcourseDetail();
        this.getTodayTime();
        this.getMonthTime();
      } else {
        uni.navigateTo({
          url: '/pages/login/login'
        });
      }
    },
    methods: {
      onPullDownRefresh() {
        setTimeout(() => {
          this.getMyAlIReply();
          this.getcourseDetail();
          this.getTodayTime();
          this.getMonthTime();
          uni.stopPullDownRefresh()
        }, 1200)
      },
      // 班级课程
      async getMyAlIReply() {
        await uni.request({
          method: 'GET',
          url: '/api/progress/progress/getClasCouProgress',
		  header: {
		    "Authorization": "Bearar " + uni.getStorageSync("userId")
		  },
          success: (res) => {
            this.courses = res.data.data;
            if (this.courses == undefined) {
              this.courses = [];
            }
          },
        })
      },
      // 自选课程
      async getcourseDetail() {
        await uni.request({
          method: 'GET',
          url: '/api/progress/progress/getCouProgress',
		  header: {
		    "Authorization": "Bearar " + uni.getStorageSync("userId")
		  },
          success: (res) => {
            this.myCourses = res.data.data;
            if (this.myCourses == undefined) {
              this.myCourses = [];
            }
          },
        })
      },
      // 学习时长
      async getTodayTime() {
        await uni.request({
          method: 'GET',
          url: '/api/progress/progress/tStudyTime',
		 header: {
		   "Authorization": "Bearar " + uni.getStorageSync("userId")
		 },
          success: (res) => {
            this.todayTime = res.data.data;
            if (this.todayTime == undefined) {
              this.todayTime = "0秒";
            }
          },
        })
      },
      async getMonthTime() {
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/monthTime?stuId=${this.stuId}`,
		  header: {
		    "Authorization": "Bearar " + uni.getStorageSync("userId")
		  },
          success: (res) => {
            this.monthTime = res.data.data;
            if (this.monthTime == undefined) {
              this.monthTime = "0秒";
            }
          },
        })
      },
      selectCourse1() {
        this.selected1 = true
      },
      selectCourse2() {
        this.selected1 = false
        console.log(this.selected1);
      },
      toClassPlay(couId, chaId, url, startTime, classId) {
        if (classId == undefined) {
          this.classId = ""
        } else {
          this.classId = classId
        };
        uni.navigateTo({
          url: '/subpkg/study_play/study_play?classId=' + this.classId + '&couId=' + couId + '&stuId=' + this
            .stuId + '&chaId=' + chaId + '&url=' + url + '&startTime=' + startTime
        })
      }
    }
  }
</script>

<style lang="scss" scoped>
  .study {
    background-color: #FFFFFF;
    padding-bottom: 50rpx;
    border-bottom-left-radius: 50rpx;
    border-bottom-right-radius: 50rpx;

    // 签到
    .sign_in {

      .headimg {
        width: 100%;
        height: 340rpx;
      }

      .sign_box {
        position: absolute;
        top: 8%;
        left: 34%;
        color: white;

        .study_time {
          margin-left: 10rpx;
          margin-right: 10rpx;
          font-size: 40rpx;
          color: #eaedff;
          font-weight: normal;
        }

        .sign_but {
          margin-top: 20rpx;
          width: 250rpx;
          height: 60rpx;
          border-radius: 10rpx;
          background-color: #7f94ff;
          text-align: center;
          line-height: 60rpx;
          color: white;
          font-weight: normal;
        }
      }
    }

    // 学习时长
    .time {
      background-color: #FFFFFF;
      border-radius: 15rpx;
      margin: auto;
      margin-top: 10rpx;
      display: flex;
      justify-content: space-between;
      width: 96%;


      .see_time {
        width: 50%;
        height: 100rpx;
        text-align: center;
        border-radius: 15rpx;
        padding-top: 10rpx;
        font-size: 30rpx;

        .time_num {
          padding-top: 5rpx;
          color: #7f94ff
        }
      }
    }

    // 学习课程分类
    .classify {
      display: flex;
      justify-content: space-between;
      width: 96%;
      margin-top: 10rpx;
      margin: 10rpx auto;
      padding: 10rpx 30rpx 10rpx 30rpx;
      border-radius: 16rpx;
      background-color: white;

      .btu {
        width: 45%;
        height: 70rpx;
        font-size: 28rpx;
        display: flex;
        justify-content: center;
        align-items: center;
        line-height: 60rpx;
        text-align: center;
        border-radius: 15rpx;
        border: 1px solid #7f94ff;
        opacity: 0.8;
      }

      .active {
        background-color: #95acff;

        color: #FFFFFF;
        opacity: 0.95;
      }
    }

    // 学习内容
    .context {
      width: 100%;
      margin-top: 10rpx;
      border-radius: 16rpx;
      background-color: white;
      padding: 20rpx 20rpx 50rpx 20rpx;

      .course_item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 96%;
        margin-left: 2%;
        margin-right: 2%;
        margin-top: 10rpx;
        margin-bottom: 20rpx;
        padding: 5rpx 20rpx 5rpx 20rpx;
        height: 160rpx;
        border-radius: 16rpx;
        box-shadow: 0 3rpx 12rpx 0 rgba(0, 0, 0, .12);

        .left {
          display: flex;
          width: 92%;

          .courseimg {
            width: 250rpx;
            height: 120rpx;
            border-radius: 10rpx;
          }

          .descrip {
            width: 100%;
            margin-left: 20rpx;
            display: flex;
            flex-direction: column;
            justify-content: space-between;

            .course_name {
              font-size: 24rpx;
              margin-top: 4rpx;
            }

            .author_name {
              margin-top: 6rpx;
              font-size: 20rpx;
            }
          }
        }

        .iconimg {
          margin-left: 10rpx;
          width: 15rpx;
          height: 23rpx;
        }
      }
    }

    // 内容为空
	.empty {
	  margin: 0 auto;
	  width: 96%;
	  border-radius: 16rpx;
	  background-color: white;
	  text-align: center;
	  line-height: 200rpx;
	  font-size: 32rpx;
      
       image{
		  width: 540rpx;
		  height: 480rpx;
       }
       
	  .tag {
        opacity: 0.7;
		margin-top: -150rpx;
	  }
	}

  }
</style>
