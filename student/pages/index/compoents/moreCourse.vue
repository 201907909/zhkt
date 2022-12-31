<template>
  <view class="moreCourse">
    <view class="moreCourse-up">
      <view class="input-one">
        <u-input inputAlign="center" class="type-font" :selectOpen="showSeclect" placeholder="课程类别"
          v-model="selectValue" :type="type" @click="showSeclect = true" />
        <u-select :defaultValue="defaultValue" v-model="showSeclect" mode="mutil-column-auto" :list="typeList"
          @confirm="confirm" @cancel="cancel"></u-select>
      </view>
      <view class="dropdown-wrap">
        <!-- 在279行修改了源码，注释了遮罩 -->
        <u-dropdown menuIcon="arrow-down-fill">
          <u-dropdown-item v-model="value1" title="销量" :options="options1" @change="changeClick"></u-dropdown-item>
          <u-dropdown-item v-model="value2" title="价格" :options="options2" @change="changePrice"></u-dropdown-item>
        </u-dropdown>
      </view>
    </view>
    <u-gap height="5" bg-color="#bbb"></u-gap>
    <view class="course-content">
      <courseList :coures="coures"></courseList>
    </view>
    <u-back-top :scroll-top="scrollTop" :bottom="200" :iconStyle="iconStyle" :customStyle="customStyle"></u-back-top>
  </view>
</template>

<script>
  import courseList from "./courseList.vue"
  export default {
    components: {
      courseList
    },
    data() {
      return {
        value1: 0,
        value2: 0,
        options1: [{
            label: '默认排序',
            value: 0,
          }, {
            label: '从高到低',
            value: 2,
          },
          {
            label: '从低到高',
            value: 1,
          },
        ],
        options2: [{
            label: '默认排序',
            value: 0,
          }, {
            label: '从高到低',
            value: 2,
          },
          {
            label: '从低到高',
            value: 1,
          },
        ],
        //选择课程类型
        showSeclect: false,
        type: "select",
        //选择高亮显示
        defaultValue: [],
        //显示出来
        selectValue: '课程类别',
        // 课程类型
        typeList: [],
        //大类Id
        maxTypeId: '',
        //小类Id
        minTypeId: '',
        //课程结果
        coures: [],
        //页码
        page: 1,
        //index true表示从更多进来，undifine为类型进来
        index: false,
        scrollTop: 0,
        iconStyle: {
          fontSize: '32rpx',
          color: '#ffffff',
        },
        customStyle: {
          backgroundColor: '#7f94ff',
        }
      }
    },
    methods: {
      //购买量排序
      changeClick(value) {
        this.value1 = value
        this.getCourse(true)

      },
      //价格排序
      changePrice(value) {
        this.value2 = value
        this.getCourse(true)
      },
      //查询所有类别
      getAllType() {
        uni.request({
          method: 'GET',
          url: '/api/interaction/wenda/getAllType',
          success: (res) => {
            this.typeList = res.data.data
          }
        });

      },
      //查询课程 index 作为标识，是否执行回到第一页和数组清空操作
      getCourse(index) {
        uni.showLoading({
          title: '加载中',
          mask: true
        });
        if (index) {
          this.coures = []
          this.page = 1
        }
        uni.request({
          method: 'GET',
          url: '/api/course/course/seeMore',
          data: {
            clickNum: this.value1,
            couPrice: this.value2,
            current: this.page,
            maxTypeId: this.maxTypeId,
            minTypeId: this.minTypeId

          },
          success: (res) => {
            if (res.data.data.data.length == 0) {
              //没有数据就替换一下loadingText的值
              uni.showToast({
                title: '没有更多数据了',
                icon: 'none',
                mask: false
              })
            } else {
              this.coures = this.coures.concat(res.data.data.data)
            }
          },
          complete() {
            //关闭加载中
            setTimeout(function() {
              uni.hideLoading();
            }, 100);
          }
        });

      },

      //选择课程类型
      confirm(e) {
        this.defaultValue[0] = e[0].value;
        this.defaultValue[1] = e[1].value;
        var label1 = e[0].label;
        var label2 = e[1].label;
        //默认小类
        var label = label2;
        this.selectValue = label;
        for (let s of this.typeList) {
          if (label1 == s.label) {
            this.maxTypeId = s.maxTypeId;
            for (let s1 of s.children) {
              if (label2 == s1.label) {
                this.minTypeId = s1.minTypeId
                break
              }
            }

          }

        }
        this.getCourse(true)

      },
      cancel(e) {
        // 说明从更多进来,并且点击取消
        if (this.selectValue === '课程类别') {
          this.getCourse()
        }
      },
      //end
    },
    //上拉加载
    onReachBottom() {
      this.page += 1
      this.getCourse()
    },
    //回到顶部
    onPageScroll(e) {
      this.scrollTop = e.scrollTop;
    },
    onLoad(options) {
      uni.showLoading({
        title: '加载中',
        mask: true
      });

      this.index = options.index
      uni.request({
        method: 'GET',
        url: '/api/interaction/wenda/getAllType',
        success: (res) => {
          this.typeList = res.data.data
          //option为object类型，会序列化上个页面传递的参数
          if (options.show == "true") {
            this.showSeclect = true
          } else if (options.couTypeId) {
            var value = 0
            var label = ''
            for (let s of this.typeList) {
              if (s.maxTypeId == options.couTypeId) {
                value = s.value
                label = s.label
                break;
              }
            }
            // //默认选择大类下面的第一个小
            this.selectValue = label
            this.maxTypeId = options.couTypeId
            this.defaultValue[0] = value
            this.defaultValue[1] = 0
            this.getCourse()
          }
          if (this.index) {
            this.getCourse()

          }

        },
        complete() {
          //关闭加载中
          setTimeout(function() {
            uni.hideLoading();
          }, 100);
        }
      });


    },


  }
</script>

<style lang="scss">
  .moreCourse {

    .moreCourse-up {
      background-color: #FFFFFF;
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      align-items: center;

      .input-one {
        width: 50%;
        border: none;
      }

      .dropdown-wrap {
        width: 50%;
      }


    }
  }
</style>
