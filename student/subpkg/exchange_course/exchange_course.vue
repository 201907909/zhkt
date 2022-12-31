<template>
  <view>
    <!-- 实践查询 -->
    <view class="make_comments">
      <input class="uni-input" placeholder="请输入" v-model="couComment" />
      <view class="make_but" @click="addComment()">
        搜索
      </view>
    </view>

    <!-- 实践列表 -->
    <view class="course">
      <view class="course_item" v-for="num in pracs">
        <view class="left">
		  <image src="/static/img/none.5aa59dea.png" v-if="num.praImgName=='default'" class="courseimg"></image>
          <image :src="num.praImgIO" v-if="num.praImgName!='default'" class="courseimg"></image>
          <view class="descrip">
            <view class="name">
              <view class="course_name">{{num.praName}}</view>
              <view class="author_name">{{num.teaName}}</view>
            </view>
          </view>
        </view>

        <image src="../../static/img/youu.png" mode="widthFix" style="width: 56rpx;" @click="applyPrac(num.id
)"></image>

      </view>

      <!-- 实践详情 -->
      <view class="practice_bac" v-show="isBac==true">
        <!-- 实践详情 -->
        <view class="practice_box" v-show="isSubmit==true">
          <view class="pe_box">
            <view class="title_box">
              <view class="pe_title">实践详情</view>
              <span class="iconfont icon-guanbi" @click="cancel()"></span>
            </view>
            <!-- <image :src="pracDtails.praImgName" style="width: 98%;height: 280rpx;margin-top: 40rpx;" mode="aspectFit"> -->
            <image src="/static/img/none.5aa59dea.png" v-if="pracDtails.praImgName=='default'"  style="width: 500rpx;height: 280rpx;margin-top: 40rpx;" mode="aspectFill"></image>
            <image :src="pracDtails.praImgIO" v-if="pracDtails.praImgName!='default'"  style="width: 500rpx;height: 280rpx;margin-top: 40rpx;" mode="aspectFill"></image>
			
			</image>
            <view class="pe_line">
              指导老师：<text>{{pracDtails.teaName}}</text>
            </view>
            <view class="pe_line">
              实践时间：<text>{{pratime}}</text>
            </view>
            <view class="pe_line">
              实践地点：<text>{{pracDtails.praSite}}</text>
            </view>
            <view class="pe_line" style="color: #7f94ff;">
              剩余时间：<text>{{pracDtails.surplusTime}}</text>
            </view>
            <view class="pe_line">
              实践内容：
              <view :class="fold==true?'content':'full' ">
                {{pracDtails.praMain}}
              </view>
              <view v-show="praMain.length>9">
                <view class="show" @click="btn" v-show="fold">展开 </view>
                <view class="show" @click="btn" v-show="!fold">收起 </view>
              </view>
            </view>
          </view>
          <view class="btu_box" style="margin-bottom: 20rpx" v-if="btuIn">
            <view class="but" @click="canDetail()">
              {{btuInfoe}}
            </view>
            <view class="but but1" @click="subDetail()">
              {{btuInfoe1}}
            </view>
          </view>
          <view class="btu_box" style="margin-bottom: 20rpx" v-if="!btuIn">
            <view class="but3">{{btuInform}}</view>
          </view>
        </view>

        <!-- 实践提示 -->
        <view class="practice_box" v-show="isSub==true" style="width: 70%;height: 280rpx;">
          <view class="prompt">手下留情~~</view>
          <view class="stars">
            <uni-rate v-model="rateValue" />
          </view>
          <view class="btu2" @click="submitPr()">
            提交
          </view>
        </view>

        <view class="practice_box" v-show="isEnd==true" style="width: 70%;height: 280rpx;">
          <view class="prompt">{{prompt}}</view>

          <view class="btu2" @click="cancel()">
            关闭
          </view>
        </view>
      </view>

      <!-- 到底了 -->
      <view class="course_bottom">
        到底了~~~
      </view>
    </view>
  </view>
</template>

<script>
  export default {
    data() {
      return {
        couId: '',
        stuId: '',
        pracs: [],
        praId: '',
        pracDtails: [],
        couComment: "",
        praMain: '',
        isSubmit: false,
        isEnd: false,
        isBac: false,
        prompt: "报名成功!",
        cancelC: "取消提交",
        sureC: "确定提交",
        fold: true,
        btuInfoe: "取消报名",
        btuInfoe1: "确定报名",
        pratime: "",
        rateValue: 5,
        btuIn: true,
        btuInform: "实践已结束",
        isSub: false,
        rateValue: 5
      };
    },
    onLoad(option) {
      this.couId = option.couId;
      this.stuId = option.stuId;
      this.getAllPractice();
    },
    methods: {
      // 内容折叠
      btn() {
        this.fold = !this.fold;
      },
      // 所有实践
      async getAllPractice() {
        await uni.request({
          method: 'GET',
          url: `/api/course/studentPractice/getAllPracticeSimpleInfo?couId=${this.couId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            console.log(res.data.data);
            this.pracs = res.data.data;
          },
        });
      },
      // 查询实践
      async addComment() {
        await uni.request({
          method: 'GET',
          url: `/api/course/studentPractice/getPracticeSimpleByInfo?info=${this.couComment}&couId=${this.couId}`,
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          success: (res) => {
            console.log(res.data.data);
            this.pracs = res.data.data;
          },
        });
      },
      // 实践详情
      async applyPrac(id) {
        this.btuInfoe = "";
        this.btuInfoe1 = "";
        this.pratime = "";
        await uni.request({
          method: 'GET',
          url: `/api/course/studentPractice/getPracticeSimpleInfo?id=${id}&stuId=${this.stuId}`,
          header: {
            "Bearar": uni.getStorageSync("userId")
          },
          success: (res) => {
            console.log(res.data.data);
            this.praId = res.data.data.praId;
            this.praMain = res.data.data.praMain;
            this.pracDtails = res.data.data;
            this.pratime = res.data.data.createTime.substring(0, 10);
            // 实践结束
            if (this.pracDtails.isEndP == 1) {
              this.btuInform = "实践已结束"
              this.btuIn = false;
            } else {
              this.btuIn = true;
              // 已报名
              if (this.pracDtails.state == 0) {
                // 未提交
                if (this.pracDtails.isEndS == 0) {
                  this.btuInfoe = "取消报名";
                  this.btuInfoe1 = "提交实践";
                } else {
                  this.btuInform = "实践已提交"
                  this.btuIn = false;
                }
              } else {
                this.btuInfoe = "返回列表";
                this.btuInfoe1 = "确定报名";
              }
            };
          }
        });
        this.fold = true;
        this.cancelC = "";
        this.sureC = "";
        this.isEnd = false;
        this.isBac = true;
        this.isSubmit = true;
      },
      cancel() {
        this.isBac = false;
        this.isEnd = false;
      },
      async canDetail() {
        if (this.btuInfoe == "返回列表") {
          this.isBac = false;
        };
        if (this.btuInfoe == "取消报名") {
          await uni.request({
            method: 'POST',
            url: '/api/course/studentPractice/studentJoinPractice',
            header: {
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            data: {
              praId: this.praId
            },
            success: (res) => {
              this.prompt = res.data.msg;
              this.isSubmit = false;
              this.isEnd = true;
              this.getAllPractice();
            }
          });
        };
      },
      // 确定报名
      async subDetail() {
        if (this.btuInfoe1 == "确定报名") {
          await uni.request({
            method: 'POST',
            url: '/api/course/studentPractice/studentJoinPractice',
            header: {
              "Authorization": "Bearar " + uni.getStorageSync("userId")
            },
            data: {
              praId: this.praId
            },
            success: (res) => {
              this.prompt = res.data.msg;
              this.isSubmit = false;
              this.isEnd = true;
              this.getAllPractice();
            }
          });
        }
        if (this.btuInfoe1 == "提交实践") {
          this.isSubmit = false;
          this.isSub = true;
        }
      },
      async submitPr() {
        this.isSub = false;
        await uni.request({
          method: 'POST',
          url: '/api/course/studentPractice/studentSubmitPractice',
          header: {
            "Authorization": "Bearar " + uni.getStorageSync("userId")
          },
          data: {
            praId: this.praId,
            score: this.rateValue,
          },
          success: (res) => {
            this.prompt = res.data.msg;
            this.isEnd = true;
            this.getAllPractice();
          }
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  .make_comments {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #ffffff;
    padding: 12rpx;
    position: fixed;
    border-top: 2rpx solid #dfdfdf;
    z-index: 999;

    .uni-input {
      background-color: #f3f3f3;
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

  .course {
    background-color: #ffffff;
    border-radius: 16rpx;
    color: black;
    position: relative;
    top: 82rpx;

    .course_item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      width: 100%;
      padding: 5rpx 30rpx 5rpx 30rpx;
      height: 160rpx;
      border-bottom: 2rpx solid #eeefef;
      border-radius: 16rpx;

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
          justify-content: center;

          .course_name {
            font-size: 28rpx;
            margin-top: 4rpx;
          }

          .author_name {
            margin-top: 6rpx;
            font-size: 24rpx;
          }
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
        align-items: center;
        position: fixed;
        left: 50%;
        transform: translate(-50%, 0%);
        width: 80%;
        height: 1000rpx;
        overflow-y: auto;
        padding: 40rpx;
        background-color: #ffffff;
        z-index: 999 !important;
        border-radius: 10rpx;
        border: 2rpx solid #bcbcbc;

        .assontents {
          width: 94%;
          padding: 20rpx;
          border: 4rpx solid #5e5e5e;
          border-radius: 10rpx;
          margin-top: 30rpx;
        }

        .prompt {
          font-size: 40rpx;
          color: #6071c2;
        }

        .btu2 {
          margin-top: 60rpx;
          width: 200rpx;
          height: 60rpx;
          border: 4rpx solid #7f94ff;
          border-radius: 10rpx;
          line-height: 58rpx;
          text-align: center;
        }

        .pe_box {
          .title_box {
            display: flex;
            justify-content: space-between;

            .icon-guanbi {
              font-size: 48rpx;
              color: #6071c2;
            }

            .pe_title {
              font-size: 34rpx;
            }
          }


          .pe_line {
            font-size: 32rpx;
            margin-top: 16rpx;

            .content {
              margin-top: -52rpx;
              margin-left: 80rpx;
              padding: 10rpx 10rpx 10rpx 80rpx;
              color: #000;
              max-height: 60rpx;
              overflow: hidden;
              word-break: break-all;
              /* break-all(允许在单词内换行。) */
              text-overflow: ellipsis;
              /* 超出部分省略号 */
              display: -webkit-box;
              /** 对象作为伸缩盒子模型显示 **/
              -webkit-box-orient: vertical;
              /** 设置或检索伸缩盒对象的子元素的排列方式 **/
              -webkit-line-clamp: 1;
              /** 显示的行数 **/
            }

            .full {
              margin-top: -52rpx;
              margin-left: 80rpx;
              padding: 10rpx 10rpx 10rpx 80rpx;
              color: #000;
            }

            .show {
              margin-left: 160rpx;
              font-size: 20rpx;
              color: #7f94ff;
            }
          }
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
			display: flex;
			justify-content: center;
			align-items: center;
          }

          .but3 {
            margin-top: 60rpx;
            width: 220rpx;
            height: 60rpx;
            background-color: #7f94ff;
            border-radius: 6rpx;
            line-height: 60rpx;
            text-align: center;
            color: #eeefef;
          }

          .but1 {
            margin-left: 60rpx;
          }
        }

        .btu_box1 {
          margin-bottom: 60rpx;
        }
      }
    }

    .course_bottom {
      height: 120rpx;
      line-height: 80rpx;
      // color: #6071c2;
	  opacity: 0.5;
      text-align: center;
    }
  }
</style>
