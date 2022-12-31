<template>
  <!-- 发布问题页面 -->
  <view class="addQuestion">
    <view class="content">
      <view class="q-title">
        <u-form :model="formTitle" ref="uForm">
          <u-form-item prop="value">
            <u-input :focus="true" v-model="formTitle.value" placeholderStyle="font-size:29rpx" maxlength="50"
              :height="100" placeholder="输入问题并以问号结尾(不超过50个字符)" :clearable="true" :type="type1" />
          </u-form-item>
        </u-form>
      </view>
      <view class="tip">
        <u-alert-tips :show-icon="true" :description="description" :show="show" type="primary" @close="show = false"
          :title="title" :close-able="true"></u-alert-tips>
      </view>
      <view class="detail">
        <u-form :model="form" ref="uForm1">
          <u-form-item label="">
            <u-input placeholder="对问题补充说明,可以更快地获得解答(可选)" v-model="valueDetail" :type="type2" :border="border"
              :height="height" :auto-height="autoHeight" />
          </u-form-item>
          <u-form-item label="附件可选">
            <u-upload max-count="4" :max-size="1048576" :action="action" @on-success="successUpload"
              @on-remove="onRemove">
            </u-upload>
            </u-upload>
          </u-form-item>
          <u-form-item>
            <u-input :selectOpen="showSeclect" placeholder="添加分类" v-model="selectValue1" :type="type3"
              :border="borderQuestion" @click="showSeclect = true" />
            <u-select :defaultValue="selectValue" v-model="showSeclect" mode="mutil-column-auto" :list="typeList"
              @confirm="confirm"></u-select>
          </u-form-item>
        </u-form>
        <u-toast ref="uToastTwo" />
        <u-button @click="submit" :custom-style="customStyle">确定发布</u-button>
        <view class="tip-success">
          <u-toast ref="uToastOne" />
        </view>
        <view>
          <u-toast ref="uToast" />
        </view>

      </view>
    </view>
  </view>
</template>
<script>
  export default {
    data() {
      return {
        type1: 'text',
        formTitle: {
          value: '',
        },
        action: '/api/interaction/wenda/wenDaFile',

        title: '让你的问题会有更多的回复',
        description: '保持文字简练，表述清晰问题的关键点,必要时上传图片作为辅助，添加合适的分类！',
        show: true,
        customStyle: {
          backgroundColor: '#899fff',
          color: 'white'
        },

        value1: '',
        type2: 'textarea',
        border: true,
        height: 100,
        autoHeight: true,
        form: {},
        //选择问题类型
        //选择高亮显示
        selectValue: [],
        type3: 'select',
        showSeclect: false,
        borderQuestion: true,
        // 课程类型
        typeList: [],
        selectValue: [],
        selectValue1: "",
        rules: {
          value: [{
            required: true,
            message: '问题不能为空',
            // 可以单个或者同时写两个触发验证方式 
            trigger: ['change', 'blur'],
          }]
        },
        //问题详细描述 
        valueDetail: '',
        //发布问题数据整合 
        interlocutionDTO: {
          // //0为教师，1为学生
          // role: 1,
          content: "",
          imgs: [],
          isSelect: 0,
          maxTypeId: '',
          minTypeId: '',
          queId: 0,
          title: "",
          uid: 0,
          userId: uni.getStorageSync("userId")
        },

      }
    },
    methods: {
      // 成功上传
      successUpload(e) {

        this.interlocutionDTO.imgs = this.interlocutionDTO.imgs.concat(e.toString())


      },
      //移除图片
      onRemove(name) {
        this.interlocutionDTO.imgs.splice(name, 1)
        this.removeImage(this.interlocutionDTO.imgs[name].toString())

      },

      //移除图片,后端
      removeImage(imgpath) {
        uni.request({
          method: 'POST',
          url: '/api/interaction/wenda/fileDelete',
          data: {
            imgpath: imgpath
          },
          header: {
            "Content-Type": "application/x-www-form-urlencoded",


          },
          success: (res) => {

          }

        })

      },

      //选择问题类型
      confirm(e) {
        for (let s of this.typeList) {
          if (s.value == e[0].value) {
            this.interlocutionDTO.maxTypeId = s.maxTypeId
            for (let i of s.children) {
              if (i.value == e[1].value) {
                this.interlocutionDTO.minTypeId = i.minTypeId
                break;
              }
            }
          }
        }
        this.selectValue[0] = e[0].value;
        this.selectValue[1] = e[1].value;
        var label1 = e[0].label;
        var label2 = e[1].label;
        var label = label1 + "-" + label2;
        this.selectValue1 = label;
      },

      //end
      //发布问题成功提示
      submit() {
        this.$refs.uForm.validate(valid => {
          if (valid) {
            if (this.selectValue != '') {
              this.interlocutionDTO.title = this.formTitle.value;
              this.interlocutionDTO.content = this.valueDetail;
              uni.showLoading({
                title: '正在校验',
                mask: true
              });
              uni.request({
                method: 'POST',
                url: "/api/interaction/wenda/add",
                data: this.interlocutionDTO,
                header: {
                  "Content-Type": "application/json",
                  "Authorization": "Bearar " + uni.getStorageSync("userId")
                },
                success: (res) => {
                  if (res.data.code == 500) {
                    this.$refs.uToast.show({
                      title: res.data.msg,
                      type: 'error',
                      duration: 2000
                    })

                  } else {
                    this.showToast()
                  }


                },
                complete() {
                  //关闭加载中
                  setTimeout(function() {
                    uni.hideLoading();
                  }, 100);
                }
              });

            } else {
              this.$refs.uToastTwo.show({
                title: '添加分类为必选',
                type: 'error',
              })
            }

          } else {

          }
        });

      },
      showToast() {
        //保存，发布成功，刷新问题页面！！
        uni.setStorageSync('option', {
          refresh: true,
        })
        this.$refs.uToastOne.show({
          title: '发布成功，正在回到问题广场',
          type: 'success',
          //返回上一页
          url: '/pages/find/find',
          isTab: "true",
        })
      },
      //end
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

    },
    onLoad() {
      this.getAllType()

    },

    // 必须要在onReady生命周期，因为onLoad生命周期组件可能尚未创建完毕
    onReady() {
      this.$refs.uForm.setRules(this.rules);
    }
  }
</script>

<style lang="scss" scoped>
  .addQuestion {

    .content {
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      padding: 20rpx;


      .q-title {
        background-color: white;
      }

      .detail {
        width: 100%;
        background-color: white;
      }



    }
  }
</style>
