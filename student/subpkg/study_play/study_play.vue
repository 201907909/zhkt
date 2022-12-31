<template>
  <view class="study_play">
    <!-- 播放器 -->
    <view class="video_box">
      <video id="myVideo" class="video_full" :initial-time="initialTime" autoplay="true"
        poster="https://store.storeimages.cdn-apple.com/8756/as-images.apple.com/is/store-card-13-watch-nav-202209_GEO_CN?wid=200&hei=130&fmt=png-alpha&.v=1661796451520"
        :src="videourl" @play="videoPlay" @timeupdate="timeUpdate" @pause="videoPause"></video>
    </view>
    <!-- 导航条 -->
    <view class="video_tab">
      <view class="tab_btu" @click="exchangedTab1()" :class="activeTab==true?'active':'' ">
        简介
      </view>
      <view class=" tab_btu" @click="exchangedTab2()" :class="activeTab==false?'active':'' ">
        评论
      </view>
      <span class="iconfont icon-fenxiang1 iconimg"></span>
    </view>

    <!-- 简介内容 -->
    <view class="brief" v-show="activeTab==true">
      <view class="evaluate">
        <view class="stars">
          <u-rate :count="count" :current="value" :disabled="true" active-color="#7f94ff" inactive-color="#d3d3d3"
            gutter="20"></u-rate>
        </view>
        <view class="score" @click="score()">评分</view>
        <view class="practice practice_state" @click="applyPrac()">
          报名实践
        </view>
      </view>

      <!-- 简介 -->
      <view class="course_introduct">
        <view>
          <text class="con">{{introduction}}</text>
        </view>
      </view>

      <!-- 章节 -->
      <view class="unit">
        <view class="unit_item" v-for="u in units" @click="switchVideol(u,u.chaUrl,u.chaId)">
          <text class="">{{u.chaTitle}}</text>
          <text class="now_play" v-if="u.chaId==nowChaId">正在播放</text>
          <progress :percent="u.ratio" activeColor="#7f94ff" backgroundColor="#ffffff" show-info stroke-width="3" />
        </view>
        <view class="unit_bottom">
          到底了~~
        </view>
      </view>

      <!-- 评分 -->
      <view class="practice_bac" v-show="pracScore==true">
        <view class="practice_box">
          <view class="pe_title1">客官，手下留情~~</view>
          <view class="stars">
            <uni-rate v-model="rateValue" />
          </view>
          <textarea class="uni-input assontents" maxlength="180" placeholder="请输入评价" v-model="assontents" />
          <view class="btu_box">
            <view class="but" @click="cancel()">
              取消
            </view>
            <view class="but but1" @click="commitStar()">
              确定
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 评论内容 -->
    <view class="comments" v-show="activeTab==false">
      <!-- 评论列表 -->
      <view class="comments_lists" v-for="c in commments">
        <view class="individual">
          <image class="avatar" src="../../static/img/1.png" mode=""></image>
          <view class="nickname">{{c.stuName}}</view>
        </view>
        <view class="content_box">
          <view :class="fold==true?'content':'full' ">
            {{c.couComment}}
          </view>
          <view v-show="c.couComment.length>60">
            <view class="show" @click="btn" v-show="fold">展开 </view>
            <view class="show" @click="btn" v-show="!fold">收起 </view>
          </view>
          <view class="content_evaluation">
            <span class="iconfont icon-dianzan_kuai evaluate_logo" :class="{ evaluate_active: c.isClick!=0 }"
              @click="like(c.clicks,c.couComId,c.drops)">
            </span>
            <text>{{c.clicks}}</text>
            <span class="iconfont icon-zan4 evaluate_logo2" :class="{ evaluate_active: c.isDrop!=0 }"
              @click="unlike(c.clicks,c.couComId,c.drops)"></span>
            <text>{{c.drops}}</text>
          </view>
        </view>
      </view>
      <!-- 评论为空 -->
      <view class="comments_emp" v-if="commments==[]">暂无评论~~~</view>
    </view>

    <!-- 评论框 -->
    <view class=" make_comments" v-show="activeTab==false">
      <input class="uni-input" placeholder="请输入内容" v-model="couComment" />
      <view class="make_but" @click="addComment()">
        评论
      </view>
    </view>
  </view>
</template>
<script>
  import {
    throttle
  } from "../../utils/throttle.js";
  export default {
    data() {
      return {
        // 简介，评论
        activeTab: true,
        //课程章节 
        units: [],
        // 评论展开，收起
        fold: true,
        // 评分
        pracScore: false,
        rateValue: 5,
        // 评论列表
        commments: [],
        // 班级，课程，学生id
        classId: "",
        stuId: "",
        couId: "",
        // 每条评论id
        commentid: [],
        // 评论内容
        couComment: "",
        //视频组件
        videourl: '',
        videoTime: 0, // 视频总时间长度
        videoContext: '', // 用来存储video对象
        watchTime: 0, // 实际观看时间
        videoRealTime: 0, // 实时播放进度
        initialTime: 0, //视频初始位置
        chaId: "", //章节id
        startTime: 0, //开始学习时长
        studyTime: 0,
        endTime: 0,
        isLeave: 0,
        nowChaId: "",
        // 点赞
        count: 5,
        value: 4.2,
        // 评价内容
        assontents: "",
        // 课程简介
        introduction: "",
        orginTime: "",
      }
    },
    onLoad(option) {
      this.classId = option.classId;
      this.stuId = option.stuId;
      this.couId = option.couId;
      this.getProgress(this.classId, this.couId);
      this.getCourseDetail(option.classId, option.couId);
      this.getCouCommment(option.couId, option.stuId);
    },
    // 视频唯一ID
    onReady() {
      this.videoContext = uni.createVideoContext('myVideo');
      this.myDate = new Date();
    },
    //播放时间点更新时
    methods: {
      // 学习时长
      videoPlay() {
        this.startTime = new Date().getTime();
      },
      videoPause() {
        // if (this.startTime == 0) {
        //   this.startTime = new Date().getTime();
        // }
        // this.startTime = new Date().getTime();
        var t = new Date().getTime();
        this.studyTime = (parseInt((t - this.startTime) / 1000)) + this.studyTime;
      },
      // 禁止拖动进度条
      timeUpdate(e) {
        //视频总时间长度
        this.videoTime = parseInt(e.detail.duration);
        // 记录用户当前视频进度
        var jumpTime = parseInt(e.detail.currentTime);
        // 判断用户当前视频进度比实际观看时间差别，这里只判断了用户快进
        if (jumpTime - this.watchTime > 3) {
          // 差别过大，调用seek方法跳转到实际观看时间
          this.videoContext.seek(this.watchTime);
        } else {
          this.videoRealTime = parseInt(e.detail.currentTime)
          if (this.videoRealTime > this.watchTime) {
            this.watchTime = this.videoRealTime;
          }
        }
      },
      ended() {
        // 用户把进度条拉到最后，但是实际观看时间不够，跳转回去会自动暂停。
        if (this.watchTime < this.videoTime) {
          this.videoContext.play()
        }
      },
      // 班级、自选课程进度详情
      async getCourseDetail(classId, couId) {
        if (classId != "") {
          await uni.request({
            method: 'GET',
            url: `/api/progress/progress/clasCourseDetail?classId=${classId}&couId=${couId}`,
            header: {
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            success: (res) => {
              this.value = res.data.data.compreEvScore;
              this.units = res.data.data.chapterVOList;
              this.introduction = res.data.data.couIntroduction;
            },
          })
        } else {
          await uni.request({
            method: 'GET',
            url: `/api/progress/progress/courseDetail?couId=${couId}`,
            header: {
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            success: (res) => {
              this.value = res.data.data.compreEvScore;
              this.units = res.data.data.chapterVOList;
              this.introduction = res.data.data.couIntroduction;
            },
          })
        }
      },
      // 课程积分
      async addScore(classId) {
        var scy;
        if (classId == "") {
          scy = 1
        } else {
          scy = 2;
        };
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/addScore?scoreType=${scy}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
        })
      },
      // 课程学习记录
      async addCouProgress(chaId) {
        var ti = new Date().getTime();
        this.studyTime = (parseInt((ti - this.startTime) / 1000)) + this.studyTime;
        // console.log(this.studyTime);
        await uni.request({
          method: 'POST',
          url: '/api/progress/progress/addCouProgress',
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          data: {
            chaId: chaId,
            classId: this.classId,
            couId: this.couId,
            endTime: this.endTime,
            isLeave: this.isLeave,
            stuId: this.stuId,
            time: this.studyTime,
          }
        })
      },
      // 章节视频最新记录
      async getchapterProgress(chaId, classId) {
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/chapterProgress?chaId=${chaId}&classId=${classId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            this.watchTime = res.data.data.progressTime;
            this.videoContext.seek(res.data.data.startTime);
          },
        })
      },
      // 章节原本时长
      async getchapterTime(chaId, classId) {
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/chapterProgress?chaId=${chaId}&classId=${classId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            this.orginTime = res.data.data.progressTime;
          },
        })
      },
      // 上次观看记录
      async getProgress(classId, couId) {
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/newChapterProgress?classId=${classId}&couId=${couId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            this.videourl = res.data.data.url;
            this.watchTime = res.data.data.progressTime;
            this.initialTime = res.data.data.startTime;
            this.nowChaId = res.data.data.chaId;
            this.chaId = res.data.data.chaId + "";
          }
        })
      },
      // 切换章节视频
      switchVideol(u, url, chaId) {
        if (this.chaId == chaId) {
          return;
        }
        throttle(this.getUrl(u, url, chaId), 1500);
      },
      getUrl(u, url, chaId) {
        // 上次章节(加积分)
        this.getchapterTime(this.chaId, this.classId);
        if (this.orginTime < this.videoTime) {
          if (this.watchTime == this.videoTime) {
            this.addScore(this.classId);
          }
        };
        // console.log(this.videoRealTime);
        // // 最长时间
        // console.log(this.watchTime);
        // // 总时间
        // console.log(this.videoTime);
        // 修改上次章节学习记录
        this.endTime = this.videoContext.pageVm.videoRealTime;
        // this.studyTime = parseInt(this.studyTime);
        this.addCouProgress(this.chaId);

        // 获取在看章节信息
        this.getchapterProgress(chaId, this.classId);
        this.nowChaId = chaId;
        this.videourl = url;

        // 重新复制章节id
        this.chaId = chaId;
      },
      // 课程评论
      async getCouCommment(couId) {
        await uni.request({
          method: 'GET',
          url: `/api/progress/progress/getCouCommment?couId=${couId}`,
          success: (res) => {
            this.commments = res.data.data;
          },
        })
      },
      // tab栏切换
      exchangedTab1() {
        this.activeTab = true
      },
      exchangedTab2() {
        this.activeTab = false
      },
      // 报名实践
      applyPrac() {
        uni.navigateTo({
          url: '/subpkg/exchange_course/exchange_course?couId=' + this.couId + '&stuId=' + this.stuId
        });
      },
      // 评分
      score() {
        this.pracScore = true;
      },
      //取消按钮
      cancel() {
        this.pracScore = false;
      },
      // 确定按钮
      async commitStar() {
        this.pracScore = false;
        await uni.request({
          method: 'POST',
          url: 'api/course/course/addCouAppraise',
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          data: {
            couAppraise: this.assontents,
            couId: this.couId,
            evaluateScore: this.rateValue,
            stuId: this.stuId
          },
        });
      },
      // 评论折叠
      btn() {
        this.fold = !this.fold;
      },
      // 点赞
      like(clicks, couComId, drops) {
        throttle(this.zan(clicks, couComId, drops), 1000)
      },
      async zan(clicks, couComId, drops) {
        await uni.request({
          method: 'POST',
          url: `/api/progress/progress/click?couComId=${couComId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            this.getCouCommment(this.couId);
          },
        });
      },
      // 不点赞
      unlike(clicks, couComId, drops) {
        throttle(this.unZan(clicks, couComId, drops), 1000)
      },
      async unZan(clicks, couComId, drops) {
        await uni.request({
          method: 'POST',
          url: `/api/progress/progress/drop?couComId=${couComId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            this.getCouCommment(this.couId);
          },
        });
      },
      // 评论
      async addComment() {
        await uni.request({
          method: 'POST',
          url: '/api/progress/progress/addComment',
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          data: {
            couComment: this.couComment,
            couId: this.couId,
            stuId: this.stuId
          },
          success: (res) => {
            this.getCouCommment(this.couId, this.stuId);
            this.couComment = ''
          },
        });
      },
    },
    onUnload() {
      if (this.startTime == 0) {
        this.startTime = new Date().getTime();
      }
      var ti = new Date().getTime();
      this.studyTime = (parseInt((ti - this.startTime) / 1000)) + this.studyTime;
      this.endTime = this.videoContext.pageVm.videoRealTime;
      this.isLeave = 1;
      this.chaId = this.videoContext.pageVm.chaId;
      this.addCouProgress(this.chaId);
    },
  }
</script>
<style lang="scss" scoped>
  .study_play {
    background-color: white;

    .video_full {
      width: 100%;
    }

    .video_tab {
      display: flex;
      justify-content: space-between;
      padding: 10rpx 50rpx 0rpx 40rpx;

      .tab_btu {
        width: 30%;
        height: 60rpx;
        text-align: center;
        line-height: 60rpx;
        color: #4b4b4b;
      }

      .iconimg {
        color: #7f94ff;
        font-size: 40rpx;
      }

      .active {
        color: #7f94ff;
        border-bottom: 2rpx solid #7f94ff;
      }
    }

    // 简介内容 
    .brief {
      margin-top: 20rpx;

      .evaluate {
        margin: 0 auto;
        margin-top: 30rpx;
        padding: 0 30rpx 0 30rpx;
        display: flex;
        justify-content: space-between;

        .score {
          margin-left: -130rpx;
          color: #6071c2;
        }

        .practice {
          width: 180rpx;
          height: 50rpx;
          border: 4rpx solid #7f94ff;
          border-radius: 10rpx;
          text-align: center;
          line-height: 42rpx;
          font-size: 24rpx;
        }

        .practice_state {
          background-color: #7f94ff;
          color: whitesmoke;
        }
      }

      .course_introduct {
        padding: 20rpx 40rpx 20rpx 40rpx;

        .con {
          margin-left: 10rpx;
          color: #616161;
        }
      }

      .unit {
        height: 100;
        margin-top: 10rpx;
        padding: 20rpx 20rpx 30rpx 20rpx;

        .unit_bottom {
          margin-top: 30rpx;
          text-align: center;
        }

        .unit_item {
          padding: 20rpx 30rpx 20rpx 30rpx;
          background-color: #f4f4f4;
          height: 100rpx;
          border-radius: 10rpx;
          margin-top: 14rpx;
          color: #00000d;

          .now_play {
            margin-left: 20rpx;
            color: #7f94ff;
            font-size: 20rpx;
          }
        }
      }

      .practice_bac {
        position: absolute;
        top: 0rpx;
        width: 100%;
        height: 100%;
        background: rgba(255, 252, 243, 0.28);

        .practice_box {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          position: absolute;
          left: 50%;
          top: 50%;
          transform: translate(-50%, -50%);
          width: 80%;
          padding: 40rpx;
          background-color: #ffffff;
          z-index: 999 !important;
          border-radius: 10rpx;
          border: 2rpx solid #bcbcbc;

          .pe_title1 {
            margin-top: 60rpx;
            font-size: 36rpx;
            color: #6071c2;
          }

          .assontents {
            width: 94%;
            padding: 20rpx;
            border: 4rpx solid #5e5e5e;
            border-radius: 10rpx;
            margin-top: 30rpx;
          }

          .stars {
            margin-top: 20rpx;
          }

          .btu_box {
            display: flex;

            .but {
              margin-top: 60rpx;
              width: 160rpx;
              height: 60rpx;
              border: 4rpx solid #7f94ff;
              border-radius: 10rpx;
              line-height: 58rpx;
              text-align: center;
            }

            .but1 {
              margin-left: 60rpx;
              margin-bottom: 60rpx;
            }
          }
        }
      }
    }

    // 评论内容
    .comments {
      padding-bottom: 120rpx;

      // 评论列表
      .comments_lists {
        border-bottom: 2rpx solid #dfdfdf;
        padding: 20rpx 30rpx 30rpx 20rpx;

        .individual {
          display: flex;
          align-items: center;


          .avatar {
            width: 66rpx;
            height: 66rpx;
            border-radius: 33rpx;
          }

          .nickname {
            margin-left: 16rpx;
          }
        }

        .content_box {
          .content {
            padding: 10rpx 10rpx 10rpx 80rpx;
            color: #000;
            max-height: 116rpx;
            overflow: hidden;
            word-break: break-all;
            /* break-all(允许在单词内换行。) */
            text-overflow: ellipsis;
            /* 超出部分省略号 */
            display: -webkit-box;
            /** 对象作为伸缩盒子模型显示 **/
            -webkit-box-orient: vertical;
            /** 设置或检索伸缩盒对象的子元素的排列方式 **/
            -webkit-line-clamp: 3;
            /** 显示的行数 **/
          }

          .full {
            padding: 10rpx 10rpx 10rpx 80rpx;
            color: #000;
          }

          .show {
            margin-left: 80rpx;
            color: #7f94ff;
          }

          .content_evaluation {
            display: flex;
            align-items: center;
            margin-top: 10rpx;
            margin-left: 80rpx;

            .evaluate_logo {
              font-size: 30rpx;
              color: #616161;
            }

            .evaluate_logo2 {
              position: relative;
              top: 8rpx;
              font-size: 30rpx;
              color: #616161;
              margin-left: 20px;
            }

            .evaluate_active {
              color: #7f94ff;
            }

            .evaluate_logo3 {
              width: 36rpx;
              height: 36rpx;
              margin-left: 60rpx;
              position: relative;
              top: 6rpx
            }
          }
        }
      }

      .comments_emp {
        padding: 20rpx 50rpx 20rpx 50rpx;
        text-align: center;
      }

    }

    .make_comments {
      width: 100%;
      display: flex;
      justify-content: space-between;
      align-items: center;
      position: fixed;
      bottom: 0rpx;
      background-color: #ffffff;
      padding: 12rpx;
      border-top: 2rpx solid #dfdfdf;

      .uni-input {
        background-color: #ececec;
        width: 78%;
        height: 60rpx;
        padding-left: 18rpx;
        border-radius: 10rpx;
      }

      .make_but {
        width: 17%;
        height: 60rpx;
        background-color: #7f94ff;
        color: white;
        text-align: center;
        line-height: 60rpx;
        border-radius: 10rpx;
      }
    }
  }
</style>
