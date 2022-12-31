<template>
  <!--  这里是资讯和问题的发表的底部的页面 -->
  <view class="pulish-botom">
    <view class="wrap-input">
      <u-input v-model="value" :type="type" placeholder="欢迎发表你的观点" :border="border" />
    </view>

    <!-- 收藏
    <view class="wrap-like">
      <image src="../../../static/collection.png" mode="aspectFit"></image>
    </view> -->
    <view v-if="value!=''" class="wrap-sent">
      <u-button @click="sendComment" shape="square" :custom-style="customStyle" :ripple="true" ripple-bg-color="#7f94ff"
        type="success"> 发送
      </u-button>
    </view>

    <view>
      <u-toast ref="uToast" />
    </view>

  </view>


</template>

<script>
  export default {
    props: ['comment', 'reply'],
    created() {

    },
    data() {
      return {

        navbarHeight: 44,
        value: '',
        type: 'text',
        border: true,
        // 资讯
        commentDto: {
          comContent: "",
          comId: 0,
          infoId: "",
          //0为教师，1为学生
          role: 1,
          userId: uni.getStorageSync("userId")
        },
        // 问题
        replyDTO: {
          answeredTqId: 0,
          content: "",
          img: "",
          //0为教师，1为学生
          role: 1,
          queId: 0,
          uid: 0,
          userId: uni.getStorageSync("userId")
        },


        customStyle: {
          height: '55rpx', // 注意驼峰命名，并且值必须用引号包括，因为这是对象
          width: '100rpx',
          backgroundColor: '#7f94ff',


        }
      }

    },
    methods: {
      //问题和资讯的观点发布
      sendComment() {
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
          // 问题的观点
          if (!this.comment) {
            this.replyDTO.content = this.value
            this.replyDTO.answeredTqId = this.reply.aid

            uni.request({
              method: 'POST',
              url: "/api/interaction/wenda/reply",
              data: this.replyDTO,
              header: {
                "Content-Type": "application/json",
                "Authorization": "Bearar " + uni.getStorageSync("userId")
              },
              success: (res) => {
                if (res.data.code == 500) {
                  this.$refs.uToast.show({
                    title: '内容含有敏感词！',
                    type: 'error',
                    duration: 2000
                  })

                } else {
                  this.value = '';
                  this.$emit('commentAdd');
                  this.$emit('fatherMethod');
                }

              }
            });
            // 资讯的观点
          } else {
            this.commentDto.comContent = this.value;
            this.commentDto.infoId = this.comment.infoId;
            uni.request({
              method: 'POST',
              url: "/api/interaction/comment/add",
              data: this.commentDto,
              header: {
                "Content-Type": "application/json",
                "Authorization": "Bearar " + uni.getStorageSync("userId")
              },
              success: (res) => {
                // 消息违规 500
                if (res.data.code == 500) {
                  this.$refs.uToast.show({
                    title: '内容含有敏感词！',
                    type: 'error',
                    duration: 2000
                  })

                } else {
                  this.value = '';
                  this.$emit('commentAdd');
                  this.$emit('fatherMethod');
                }


              }
            });
          }

        }
      }

    },

  }
</script>

<style lang="scss" scoped>
  //固定底部 
  .pulish-botom {
    position: fixed;
    bottom: 0;
    width: 100%;
    height: 100rpx;
    background-color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;




    .wrap-input {

      width: 80%;
      margin-left: 20rpx;
    }

    // .wrap-like {
    //   padding-left: 50rpx;

    //   image {
    //     width: 40rpx;
    //     height: 40rpx;
    //     margin-right: 50rpx;


    //   }
    // }

    .wrap-sent {
      padding-right: 10rpx;

    }
  }
</style>
