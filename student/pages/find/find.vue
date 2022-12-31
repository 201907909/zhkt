<template>
  <view class="container">
    <view class="tabs-wrap">
      <u-tabs name="cate_name" :offset="[0,110]" count="cate_count" :list="list" :is-scroll="false" :current="current"
        @change="change">
      </u-tabs>
    </view>
    <view class="content_page">
      <view v-if="current == 0">
        <info ref="mychildOne"></info>
        <u-back-top :scroll-top="scrollTopOne" :bottom="300" :iconStyle="iconStyle" :customStyle="customStyle">
        </u-back-top>
      </view>
      <view v-if="current == 1">
        <question ref="mychildTwo"></question>
        <u-back-top :scroll-top="scrollTopTwo" :right="45" :bottom="350" :iconStyle="iconStyle"
          :customStyle="customStyle">
        </u-back-top>
      </view>
    </view>

  </view>
</template>

<script>
  import info from "./compoents/info.vue"
  import question from "./compoents/question.vue"

  export default {
    components: {
      info,
      question
    },
    watch: {
      // 使用watch来响应数据的变化，异步刷新数据
      infoCount(newValue, oldValue) {
        if (newValue >= oldValue) {
          uni.showLoading({
            title: '',
            mask: true
          });
          this.$refs.mychildOne.getAllInfo(false); // 调用子组件的方法getAllInfo()
          this.getTotal()
        }

      }

    },

    data() {
      return {
        //未读徽标数,主要用来监听
        infoCount: 0,
        current: 0,
        //资讯
        scrollTopOne: 0,
        //问题
        scrollTopTwo: 0,
        // 返回顶部样式
        iconStyle: {
          fontSize: '32rpx',
          color: '#ffffff'
        },
        customStyle: {
          backgroundColor: '#7f94ff',
        },

        // 返回顶部样式 end
        list: [{
          cate_name: '资讯',
          //未读徽标数
          cate_count: 0
        }, {
          cate_name: '问题',
          //徽标数
          cate_count: 0
        }],


      }

    },


    methods: {
      //获得资讯和问题的总条数
      getTotal() {
        uni.request({
          url: '/api/interaction/information/getAllTotal',
          method: 'GET',
          success: (res) => {
            if (res.data.data != 0 && res.data.data) {
              this.list[0].cate_name = '资讯' + '(' + res.data.data + ')';
            }

          }
        })

        uni.request({
          url: '/api/interaction/wenda/getAllTotal',
          method: 'GET',
          success: (res) => {
            if (res.data.data != 0 && res.data.data) {
              this.list[1].cate_name = '问题' + '(' + res.data.data + ')';
            }
          }
        })


      },
      //监控当前tabs
      change(index) {
        this.current = index;
        this.getTotal()

      },

      //取资讯未读的数
      getNoRead() {
        //取资讯未读的数
        if (this.current == 0) {
          uni.request({
            method: 'GET',
            url: '/api/interaction/information/getAllInfor',
            data: {
              current: 1
            },
            success: (res) => {
              this.infoCount = res.data.data.unReadNum
              this.list[0].cate_count = res.data.data.unReadNum
              if (this.list[0].cate_count > 0) {
                //显示数字
                uni.setTabBarBadge({
                  //tabBar的哪一项，从左边算起
                  index: 2,
                  text: this.list[0].cate_count.toString()
                })
              } else {
                // 移除数字
                uni.removeTabBarBadge({
                  index: 2
                })
              }

            },
          });

        }

      }
    },

    //
    onReady() {

      uni.showLoading({
        title: '加载中',
        mask: true
      });
      //获取总数
      this.$refs.mychildOne.getAllInfo(); // 调用子组件的方法getAllInfo()
      this.getTotal()

    },

    /**
     * 监听 TabBar 切换点击 find
     */
    onTabItemTap() {

    },

    onShow() {
      //取资讯未读的数
      this.getNoRead()
      //s保存，发布成功，上个页面传来是否有值，来刷新问题页面！！
      if (uni.getStorageSync('option')) {
        uni.showLoading({
          title: '',
          mask: true
        });
        this.$refs.mychildTwo.getAllQuestion(false); // 调用子组件的方法getAllQuestion()
        this.getTotal()
        uni.removeStorageSync('option')
      }

    },
    //下拉刷新
    onPullDownRefresh() {
      if (this.current == 0) {
        this.$refs.mychildOne.getAllInfo(false); // 调用子组件的方法getAllInfo()
        //取资讯未读的数
        this.getNoRead()
        setTimeout(function() {
          uni.stopPullDownRefresh(); //停止下拉刷新动画
        }, 1000);
        this.getTotal()

      } else {
        this.$refs.mychildTwo.getAllQuestion(false); // 调用子组件的方法getAllQuestion()
        setTimeout(function() {
          uni.stopPullDownRefresh(); //停止下拉刷新动画
        }, 1000);
        this.getTotal()

      }

    },
    //上拉加载
    onReachBottom() {
      if (this.current == 0) {
        this.$refs.mychildOne.getAllInfo(true); // 调用子组件的方法getAllInfo()
      } else {
        this.$refs.mychildTwo.getAllQuestion(true); // 调用子组件的方法getAllQuestion()
      }

    },
    onPageScroll(e) {

      if (this.current == 0) {
        this.scrollTopOne = e.scrollTop;
      } else {
        this.scrollTopTwo = e.scrollTop;

      }
    }

  }
</script>

<style lang="scss" scoped>
  .container {
    .wrap {
      height: 100vh;
    }
  }
</style>
