<template>
  <div class="classDetails">
    <div class="classDetails_left">
      <div class="classDetails_left_top">
        <el-tooltip placement="top">
          <div slot="content">
            {{ oneClassDetails.classDetails.slice(0, oneClassDetails.classDetails.length / 4) }}<br />
            {{ oneClassDetails.classDetails.slice(oneClassDetails.classDetails.length / 4,
                oneClassDetails.classDetails.length / 4 * 2)
            }}<br />
            {{ oneClassDetails.classDetails.slice(oneClassDetails.classDetails.length / 4 * 2,
                oneClassDetails.classDetails.length / 4 * 3)
            }}<br />
            {{ oneClassDetails.classDetails.slice(oneClassDetails.classDetails.length / 4 * 3,
                oneClassDetails.classDetails.length)
            }}
          </div>
          <div class="classDetails_left_top_left">

            <div class="classDetails_left_top_left_img">
              <img :src="oneClassDetails.classLogoIO" alt="">
              <span>{{ oneClassDetails.className }}</span>
            </div>
          </div>
        </el-tooltip>

      </div>


      <div class="classDetails_left_foot">
        <div class="classDetails_left_foot_top">
          <div class="classDetails_left_foot_top_head">
            <div class="classDetails_left_foot_top_head_title">
              <div class="classDetails_left_foot_top_head_title_img">
                <img style="width:45px;height:45px;" src="../../assets/适用学生.png" alt="">
              </div>
              <div class="classDetails_left_foot_top_head_title_num">
                {{ oneClassDetails.students.length }} 人
              </div>

            </div>
            <div class="classDetails_left_foot_top_head_addButton">
              <el-button type="primary" icon="el-icon-plus" circle @click="dialogFormVisible = true"></el-button>
            </div>
          </div>
          <div class="classDetails_left_foot_top_search">
            <input type="text" placeholder="请输入学生的名字" v-model="searchStudentName" @input="searchStudnetByName()">
          </div>
        </div>

        <div class="classDetails_left_foot_list">
          <el-empty v-if="oneClassDetails.students.length == 0" :image-size="200" description="暂无学生"
            class="classDetails_left_foot_list_stuList"></el-empty>
          <el-scrollbar style="height: 92%;width: 100%;" class="scrollbar-for">
            <div class="classDetails_left_foot_list_stuList" v-if="oneClassDetails.students.length != 0">
              <div class="classDetails_left_foot_list_stuList_item" v-for="(item, index) in showStudent" :key="index"
                @click="getStudentIndex(index)">
                <div class="classDetails_left_foot_list_stuList_item_img">
                  <img :src="item.imageIO" alt="">
                </div>
                <div class="classDetails_left_foot_list_stuList_item_name">
                  {{ item.stuName }}
                  <div class="classDetails_left_foot_list_stuList_item_gender">
                    <img v-if="item.gender == 1" src="../../assets/性别男 (1).png" alt="">
                    <img v-if="item.gender == 0" src="../../assets/性别女.png" alt="">
                  </div>
                </div>
                <div class="classDetails_left_foot_list_stuList_item_delete"
                  @click="deleteOneStudnetInClass(item.stuId)">
                  <img src="../../assets/删除2.png" alt="">
                </div>
              </div>
            </div>
          </el-scrollbar>
        </div>

      </div>
    </div>
    <div class="classDetails_right">
      <div class="classDetails_right_left">
        <div class="classDetails_right_left_stuInfo">
          <div class="classDetails_right_left_stuInfo_img">
            <img v-if="oneClassDetails.students.length != 0" :src="oneClassDetails.students[studentIndex].imageIO"
              alt="">
            <img v-if="oneClassDetails.students.length == 0" src="../../assets/人.png" alt="">
          </div>
          <div class="classDetails_right_left_stuInfo_names">
            <span v-if="oneClassDetails.students.length != 0">姓名: {{ oneClassDetails.students[studentIndex].stuName
            }}</span>
            <span v-if="oneClassDetails.students.length != 0">电话: {{ oneClassDetails.students[studentIndex].phone
            }}</span>
            <span v-if="oneClassDetails.students.length != 0">邮箱: {{
                oneClassDetails.students[studentIndex].email
            }}</span>
            <span v-if="oneClassDetails.students.length != 0">积分: {{
                oneClassDetails.students[studentIndex].scoreTotal
            }}</span>
            <span v-if="oneClassDetails.students.length != 0">学习时长：{{
                oneClassDetails.students[studentIndex].stuStudyTime
            }}</span>

            <span v-if="oneClassDetails.students.length == 0">姓名: 空</span>
            <span v-if="oneClassDetails.students.length == 0">电话: 空</span>
            <span v-if="oneClassDetails.students.length == 0">邮箱: 空</span>
            <span v-if="oneClassDetails.students.length == 0">积分: 空</span>
            <span v-if="oneClassDetails.students.length == 0">学习时长：空</span>
          </div>
          <div class="dakaLianxu">
            <div class="circle">
              <div class="content" v-if="oneClassDetails.students.length != 0">
                连续打卡{{ oneClassDetails.students[studentIndex].stuContinuedClock }}天</div>
              <div class="content" v-if="oneClassDetails.students.length == 0">暂无数据</div>
            </div>

          </div>
        </div>
        <div class="charts" ref="jifen"></div>
        <div class="charts">
          <div class="cla_student_shape_intergral_selectDiv">
            <el-select v-model="getWeekIndex" :change="getWeekIndexs()" class="cla_student_shape_intergral_select"
              style="width:100px;height:20px;" placeholder="请选择" size="mini">
              <el-option v-for="item in allStudentOptions[studentIndex]" :key="item.value" :value="item.value"
                :label="item.label">
              </el-option>
            </el-select>
          </div>
          <div class="charts2" ref="studyTime"></div>
        </div>

      </div>
      <div class="classDetails_right_right">
        <div class="classDetails_right_right_dakaDate">
          <dakaDate></dakaDate>
        </div>
        <div class="classDetails_right_right_courseList">
          <div class="classDetails_right_right_courseList_top">
            <span class="span1">课程</span>
            <span class="span2" v-if="oneClassDetails.students.length != 0">共
              {{ courseAboutVOS.length }} 门</span>
            <span class="span2" v-if="oneClassDetails.students.length == 0">共 0 门</span>
          </div>
          <el-empty v-if="oneClassDetails.students.length == 0" :image-size="200" description="暂无数据"
            class="classDetails_right_right_stuCouList"></el-empty>
          <div v-else-if="oneClassDetails.students.length != 0">
            <el-empty v-if="courseAboutVOS.length == 0" :image-size="200" description="无课程"
              class="classDetails_right_right_stuCouList"></el-empty>
          </div>

          <div class="classDetails_right_right_stuCouList" v-if="oneClassDetails.students.length != 0">
            <el-scrollbar style="height: 92%;width: 100%;" class="scrollbar-for">
            <div class="classDetails_right_right_stuCouList_item" v-for="item in courseAboutVOS">
              <div class="classDetails_right_right_stuCouList_item_img">
                <img :src="item.couPicIO" alt="">
              </div>
              <div class="classDetails_right_right_stuCouList_item_couName">
                <div class="classDetails_right_right_stuCouList_item_couNames">
                  <span class="span3">{{ item.couName }}</span>
                  <span class="span4">{{ item.totalTime }} 分钟</span>
                </div>
                <div class="classDetails_right_right_stuCouList_item_process">
                  <el-progress :percentage="item.couProgressBfb"></el-progress>
                </div>
              </div>
            </div>
            </el-scrollbar>

          </div>
        </div>
      </div>

    </div>

    <!-- 其他组件 -->
    <!-- 添加学生的弹窗 -->
    <div class="addStudentForm">
      <el-dialog center title="添加学生" :visible.sync="dialogFormVisible" width="65%">
        <div class="cla-searchStudent">
          <div class="cla-searchStudent-left">
            <div class="cla-searchStudent-left-searchInput">
              <el-input v-model="studentEmail" prefix-icon="el-icon-search" placeholder="请输入学生的邮箱"
                @input="searchstudentByEmail()"></el-input>
            </div>
            <div class="cla-searchStudent-left-studentList">
              <div v-for="(item, index) in searchStudentList" :key="index"
                class="cla-searchStudent-left-studentList-item">
                <div class="cla-searchStudent-left-studentList-item-img">
                  <input type="checkbox" name="" id="" :value="item.stuId" v-model="addStudentId"
                    @change="handleChangeId">
                  <img style="width:45px;height:45px;border-radius:50%;margin-left:5px;" :src="item.image" alt="">
                </div>
                <div class="cla-searchStudent-left-studentList-item-nameEmail">
                  <span style="font-size: 16px;margin-left: 10px;">姓名: <span>{{ item.stuName }}</span></span>
                  <span style="font-size: 16px;margin-left: 10px;">邮箱: <span>{{ item.email }}</span></span>
                </div>
              </div>
            </div>
          </div>
          <div class="cla-searchStudent-right">
            <div class="cla-searchStudent-right-top">
              <span>您选中的学生</span>
            </div>
            <div v-for="(item, index) in toRightStudent" :key="index" class="cla-searchStudent-left-studentList-item">
              <div class="cla-searchStudent-left-studentList-item-img">
                <img style="width:45px;height:45px;border-radius:50%;margin-left:5px;" :src="item.image" alt="">
              </div>
              <div class="cla-searchStudent-left-studentList-item-nameEmail">
                <span style="font-size: 16px;margin-left: 10px;">姓名: <span>{{ item.stuName }}</span></span>
                <span style="font-size: 16px;margin-left: 10px;">邮箱: <span>{{ item.email }}</span></span>
              </div>
            </div>
          </div>
        </div>

        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false; clearstudentEmail()">取 消</el-button>
          <el-button type="primary" @click="dialogFormVisible = false; classAddStudentList()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import console from 'console';
import * as echarts from 'echarts';
import dakaDate from '../classDetails/dakaDate.vue'
export default {
  components: {
    dakaDate
  },
  data() {
    return {
      courseAboutVOS: [],
      showStudent: [],
      copyshowStudent: [],
      searchStudentName: '',
      getWeekIndex: 0,
      accoutweek: [],
      everyAccoutweek: [],
      headers: { ContentType: 'multipart/form-data' },
      formLabelWidth2: '150px',
      editImageUrl: '',
      editClassDialogVisible: false,
      second: 0,
      minute: 0,
      editClass: {
        id: '',
        className: '',
        oldClassLogo: '',
        classDetails: '',
        newClassLogo: '',
      },
      students: {
        classId: '',
        stuIds: []
      },
      needDelete: {
        classId: 0,
        stuIds: []
      },
      // stutotalScore: [],
      claId: '',
      toRightStudent1: [],
      toRightStudent: [],
      addStudentId: [],
      searchStudentList: [],
      studentEmail: '',
      studentName: '',
      oneClassDetails: {},
      studentIndex: 0,
      coursesNum: -1,
      id: -1,
      idArr: [],
      dialogFormVisible: false,
      deleteVisble: true,
      deleteEctive: -1,
      active: -1,
      student: [],
      classImg: [],
      temp: 0,
      tempOptions: [],
      allStudentOptions: [],
      jifenX: [],
      jifenY: [],
      flag: false,
      tempJifenY: [],
      studyTimes: [],
      tempStudyTime: [],
      studyTimesFinally: [],

    }
  },

  mounted() {
    this.id = this.$route.query.id
    this.getClassDetailsById().then(() => {
      if (this.oneClassDetails.students.length != 0) {
        this.getCourseByStuId(this.oneClassDetails.students[this.studentIndex].stuId)
      }
      this.getWeekIndexs()
      this.initPic()
      this.initPic2()
      this.getDakaDate()

    })
  },
  methods: {


    async getCourseByStuId(stuId) {
      const result = await this.$API.webClass.getCourseByStuId(stuId)
      this.courseAboutVOS = result.data.data
      this.getTotalStudyTime()
    },

    clearstudentEmail() {
      this.studentEmail = ''
      this.searchStudentList = []
    },
    getJifenY() {//获取每个学生的积分纵坐标
      this.oneClassDetails.students.forEach(item => {
        this.tempJifenY = []
        item.stuScore.forEach(item2 => {
          this.tempJifenY.push(item2.value)
        })
        this.jifenY.push(this.tempJifenY)
      })
      // console.log(this.jifenY)
    },
    searchStudnetByName() { //通过名字查学生
      this.flag = false;
      if (this.searchStudentName == '' || this.searchStudentName == null) {
        this.showStudent = this.copyshowStudent
      } else {
        this.showStudent = []
        for (var i = 0; i < this.copyshowStudent.length; i++) {
          if (this.copyshowStudent[i].stuName.includes(this.searchStudentName)) {
            this.showStudent.push(this.copyshowStudent[i])
            this.flag = true
          }
        };
        if (this.flag == false) {
          this.$message({
            message: '不存在改学生',
            type: 'warning'
          })
          this.showStudent = this.copyshowStudent
        }
      }

    },

    getWeekIndexs() {
      if (this.oneClassDetails.students.length == 0) {
        this.studyTimesFinally = []
      } else {
        this.studyTimesFinally = this.studyTimes[this.studentIndex][this.getWeekIndex]

        setTimeout(() => {
          this.initPic2()
        }, 200);


      }
    },

    getAllStudentOptions() {
      for (var i = 0; i < this.studyTimes.length; i++) {
        this.tempOptions = []
        for (var j = 1; j <= this.studyTimes[i].length; j++) {
          const obj = {
            value: j - 1,
            label: '第' + j + '周'
          }
          this.tempOptions.push(obj)
        }
        this.allStudentOptions.push(this.tempOptions)
      }
      // console.log(this.allStudentOptions)
    },
    getAcountWeek() {
      this.studyTimes = []
      this.oneClassDetails.students.forEach(item => {
        this.tempStudyTime = []
        item.stuDailyTime.forEach(item2 => {
          this.tempStudyTime.push(item2)
        })
        this.studyTimes.push(this.tempStudyTime)
      })
      // console.log(this.studyTimes)
    },

    async uploadImageInClass(param) {// 上传图片方法
      const formData = new FormData()
      // console.log(param)
      formData.append('multipartFile', param.file)
      const result = await this.$API.webClass.classFile(formData)
      // console.log(result.data.data)
      this.editClass.newClassLogo = result.data.data
    },

    handlePictureCardPreview(file) {
      this.editImageUrl = file.url;
      this.editClassDialogVisible = true;
    },


    async deleteOneStudnetInClass(value) {
      this.needDelete.classId = this.oneClassDetails.claId
      this.needDelete.stuIds.push(value)
      const confirmResult = await this.$confirm('此操作永久删除删除所勾选的学生，是否继续?', '提示', {
        type: 'warning'
      }).catch((err) => {
        //return this.$message.info(err)
      })

      if (confirmResult !== 'confirm') {
        return this.$message.info('已经取消删除')
      }
      await this.$API.webClass.deleteBanch(this.needDelete).then(() => {
        this.getClassDetailsById()
        this.jifenY[this.studentIndex] = []
        this.studyTimesFinally = []
        this.initPic()
        this.initPic2()
      })
      return this.$message.success('删除成功')

    },


    async classAddStudentList() {//添加学生
      try {
        this.students.classId = this.claId
        this.students.stuIds = this.addStudentId
        const result = await this.$API.webClass.addClassStudent(this.students).then(() => {
          this.getClassDetailsById()
        })

        return this.$message.success('添加成功')
      } catch (error) {
        console.log(error)
      }
    },

    async searchstudentByEmail() {//查询学生添加
      if (this.studentEmail == '' || this.studentEmail == null) {
        this.searchStudentList = []
      } else {
        const result = await this.$API.student.classQueryAllStu(this.studentEmail)
        this.searchStudentList = result.data.data
        // console.log(this.searchStudentList)
      }

    },

    initPic2() {//学习时长图

      var chartDom = document.getElementById('charts2');
      var myChart2 = echarts.init(this.$refs.studyTime);
      var option;
      option && myChart2.setOption(option);
      myChart2.setOption({
        color: ['#80FFA5', '#00DDFF', '#37A2FF', '#FF0087', '#FFBF00'],
        title: {
          text: '学习时长',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#6a7985'
            }
          }
        },
        legend: {

        },
        // toolbox: {
        //   feature: {
        //     saveAsImage: {}
        //   }
        // },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            boundaryGap: false,
            data: ['星期一', '星期二', '星期三', '星期四', '星期五', '星期六', '星期天']
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: '学习时长',
            type: 'line',
            stack: 'Total',
            smooth: true,
            showSymbol: false,
            areaStyle: {
              opacity: 0.8,
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                {
                  offset: 0,
                  color: 'rgb(128, 255, 165)'
                },
                {
                  offset: 1,
                  color: 'rgb(1, 191, 236)'
                }
              ])
            },
            emphasis: {
              focus: 'series'
            },
            data: this.studyTimesFinally
          }
        ]

      })
    },

    initPic() {//积分图
      var myChart1 = echarts.init(this.$refs.jifen);
      var option;
      option && myChart1.setOption(option);
      myChart1.setOption({
        title: {
          text: '积分获取方式',
          textStyle: {
            fontSize: 14
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            type: 'category',
            data: ["其他互动", "日常打卡", "自选课程", "班级课程", "采纳所得", "点赞所得", "扣分流水"],
            axisTick: {
              alignWithLabel: true
            }
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ],
        series: [
          {
            name: 'Direct',
            type: 'bar',
            barWidth: '60%',
            data: this.jifenY[this.studentIndex]
          }
        ]
      })
    },

    getStudentIndex(index) {
      this.studyTimesFinally = this.studyTimes[this.studentIndex][this.getWeekIndex]
      this.studentIndex = index
      this.getWeekIndex = 0
      setTimeout(() => {
        this.initPic()
      }, 200);
      this.getDakaDate()
      this.getCourseByStuId(this.oneClassDetails.students[this.studentIndex].stuId)
    },

    handleChange() {
      this.needDelete.stuIds = this.idArr
    },

    unique(arr) {
      const res = new Map();
      return arr.filter((arr) => !res.has(arr.stuId) && res.set(arr.stuId, 1))
    },


    handleChangeId() {
      this.toRightStudent1 = []
      this.searchStudentList.forEach(item1 => {
        this.addStudentId.forEach(item2 => {
          if (item1.stuId == item2) {
            this.toRightStudent1.push(item1)
          }
        });
      });
      this.toRightStudent = this.unique(this.toRightStudent1)
      // console.log(this.toRightStudent)
    },
    getTotalStudyTime() {
      this.minute = 0
      this.second = 0
      this.courseAboutVOS.forEach(item2 => {
        this.second = item2.couTotalProgress % 60
        this.minute = item2.couTotalProgress / 60
        if (this.second > 60) {
          this.minute = this.minute + 1
        }
        this.$set(item2, "totalTime", this.minute)
      });
      console.log(this.courseAboutVOS)
    },

    getDakaDate() {
      this.$bus.$emit('getDakaDate', this.oneClassDetails.students[this.studentIndex].stuClockDate)
    },

    async getClassDetailsById() {
      const result = await this.$API.webClass.getClassById(this.id)
      this.oneClassDetails = result.data.data
      this.getJifenY()
      this.getAcountWeek()
      this.getAllStudentOptions()
      this.showStudent = this.oneClassDetails.students
      this.copyshowStudent = this.oneClassDetails.students
      this.coursesNum = result.data.data.courses.length
      this.claId = this.oneClassDetails.claId
      // this.paixunjifen()
      this.editClass.id = this.oneClassDetails.id
      this.editClass.className = this.oneClassDetails.className
      this.editClass.oldClassLogo = this.oneClassDetails.classLogo
      this.editClass.classDetails = this.oneClassDetails.classDetails
      this.getAcountWeek()
    }
  },

};
</script>

<style lang="scss" scoped>
.classDetails {
  width: 86%;
  height: 710px;
  margin: auto;
  margin-top: 10px;
  display: flex;
  justify-content: space-around;
  background-color: #eef0f4;

  .classDetails_left {
    width: 340px;
    height: 700px;
    background-color: white;
    border-radius: 10px;

    .classDetails_left_top {
      width: 100%;
      height: 110px;
      // background-color: aqua;
      display: flex;
      justify-content: center;
      // border-radius: 10px;
      // box-shadow: 1px 1px 1px 1px lightgrey;
      border-bottom: 1px solid #dbdbdb;

      .classDetails_left_top_left {
        width: 95%;
        height: 110px;

        // background-color: palevioletred;
        .classDetails_left_top_left_img {
          width: 100%;
          height: 110px;
          display: flex;
          // justify-content: center;
          align-items: center;

          img {
            width: 42%;
            height: 92%;
            border-radius: 10px;
          }

          span {
            font-size: 30px;
            font-weight: bold;
            margin-left: 20px;
          }
        }
      }
    }

    .classDetails_left_foot {
      margin-top: 10px;
      // box-shadow: 1px 1px 2px 0px lightgrey;
      width: 100%;
      height: 590px;
      border-radius: 10px;
      // background-color: pink;
      margin: auto;

      .classDetails_left_foot_list {
        margin-top: 25px;
        width: 100%;
        height: 480px;
        // background-color: pink;
        display: flex;
        justify-content: center;
        /deep/ .el-scrollbar__wrap {
            overflow-x: hidden;
          }

        .classDetails_left_foot_list_stuList {
          width: 95%;
          height: 90%;
          // background-color: rebeccapurple;
          display: flex;
          flex-direction: column;
          align-items: center;
          overflow: hidden;

          // overflow-x: hidden;
          // overflow-y: auto;

          .classDetails_left_foot_list_stuList_item {
            width: 85%;
            height: 70px;
            border-bottom: 1px solid #dbdbdb;
            // background-color: #81CCE8;
            margin-bottom: 8px;
            display: flex;

            .classDetails_left_foot_list_stuList_item_img {
              width: 25%;
              height: 70px;
              display: flex;
              justify-content: center;
              align-items: center;

              // background-color: pink;
              img {
                width: 55px;
                height: 55px;
                border-radius: 50%;
              }
            }

            .classDetails_left_foot_list_stuList_item_delete {
              width: 15%;
              height: 70px;
              line-height: 70px;

              img {
                width: 15px;
                height: 15px;
              }
            }

            .classDetails_left_foot_list_stuList_item_name {
              width: 60%;
              height: 70px;
              line-height: 70px;
              font-weight: bold;
              font-size: 20px;
              padding-left: 20px;
              display: flex;

              .classDetails_left_foot_list_stuList_item_gender {
                width: 40px;
                height: 70px;
                display: flex;
                justify-content: center;
                align-items: center;

                img {
                  width: 25px;
                  height: 25px;
                }
              }

            }
          }
        }
      }

      .classDetails_left_foot_top {
        width: 100%;
        height: 110px;
        // background-color:paleturquoise;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;

        .classDetails_left_foot_top_search {
          margin-top: 15px;
          width: 85%;
          height: 50px;

          input {
            width: 100%;
            height: 30px;
            border: none;
            border-bottom: 1px solid #dbdbdb;
            outline: none;
            font-size: 15px;

          }

          input::-webkit-input-placeholder {
            /* placeholder颜色 */
            color: lightgray;
            /* placeholder字体大小 */
            font-size: 14px
          }
        }

        .classDetails_left_foot_top_head {
          margin-top: 10px;
          width: 85%;
          height: 45px;
          // background-color: aqua;
          display: flex;
          justify-content: space-between;

          .classDetails_left_foot_top_head_title {
            font-weight: bold;
            width: 50%;
            height: 45px;
            line-height: 45px;
            font-size: 20px;
            display: flex;

            .classDetails_left_foot_top_head_title_img {
              width: 33%;
              height: 45px;
              display: flex;
              // justify-items: center;
              // align-items: center;
            }

            .classDetails_left_foot_top_head_title_num {
              width: 50%;
              height: 45px;
              line-height: 54px;
            }
          }

          .classDetails_left_foot_top_head_addButton {}
        }
      }

    }
  }

  .classDetails_right {
    width: 935px;
    height: 700px;
    background-color: white;
    border-radius: 10px;
    display: flex;

    .classDetails_right_left {
      width: 67%;
      height: 700px;

      // background-color: antiquewhite;
      .charts {
        width: 96%;
        height: 250px;
        // background-color: #6EEE96;
        margin: auto;

        .cla_student_shape_intergral_selectDiv {
          width: 40%;
          position: relative;
          z-index: 99;

          .cla_student_shape_intergral_select {
            position: relative;
            top: 0px;
            left: 465px;
            z-index: 999;

          }

        }

      }

      .charts2 {
        position: relative;
        top: -25px;
        width: 96%;
        height: 250px;
        // background-color:pink;
        margin: auto;

      }

      .classDetails_right_left_stuInfo {
        width: 96%;
        height: 200px;
        // background-color: antiquewhite;
        margin: auto;
        display: flex;

        .classDetails_right_left_stuInfo_img {
          width: 30%;
          height: 200px;
          display: flex;
          // background-color: #eef0f4;
          justify-content: center;
          align-items: center;

          img {
            width: 140px;
            height: 140px;
            border-radius: 50%;
          }
        }

        .classDetails_right_left_stuInfo_names {
          padding-left: 10px;
          width: 40%;
          height: 200px;
          // background-color: aquamarine;

          span {

            display: block;
            margin-top: 5px;
            width: 100%;
            height: 30px;
            font-size: 17px;
          }
        }

        .dakaLianxu {
          width: 30%;
          height: 200px;
          display: flex;
          justify-content: center;
          align-items: center;

          .circle {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            background: linear-gradient(0deg, #36A5ED 0%, #6EEE96 100%);
          }

          .content {
            position: relative;
            top: 11px;
            left: 10px;
            width: 130px;
            height: 130px;
            border-radius: 50%;
            background-color: #fff;
            text-align: center;
            line-height: 130px;
          }
        }
      }


    }

    .classDetails_right_right {
      width: 33%;
      height: 700px;
      // background-color: aquamarine;
      display: flex;
      flex-direction: column;
      border-left: 1px solid #dbdbdb;

      .classDetails_right_right_dakaDate {
        width: 96%;
        height: 340px;
        margin: auto;
        // background-color: royalblue;
      }

      .classDetails_right_right_courseList {
        width: 92%;
        height: 345px;
        // background-color: #36A5ED;
        margin: auto;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;
        border-radius: 10px;
        border: 1px solid #dbdbdb;


        .classDetails_right_right_courseList_top {
          width: 96%;
          height: 40px;
          // background-color: aqua;
          display: flex;
          border-bottom: 1px solid #dbdbdb;
          justify-content: space-between;

          .span1 {
            display: block;
            width: 50%;
            height: 40px;
            line-height: 40px;
            padding-left: 20px;
            font-size: 18px;
          }

          .span2 {
            padding-right: 20px;
            text-align: end;
            display: block;
            width: 50%;
            height: 40px;
            line-height: 40px;
            font-size: 18px;
          }
        }

        .classDetails_right_right_stuCouList {
          width: 96%;
          height: 300px;
          // background-color: #6EEE96;
          /deep/ .el-scrollbar__wrap {
            overflow-x: hidden;
          }

          .classDetails_right_right_stuCouList_item {
            width: 96%;
            height: 64px;
            margin-bottom: 5px;
            margin: auto;
            // background-color: peru;
            display: flex;

            .classDetails_right_right_stuCouList_item_img {
              width: 25%;
              height: 64px;
              display: flex;
              align-items: center;
              justify-content: center;
              // background-color: red;

              img {
                width: 50px;
                height: 50px;
                border-radius: 10px;
              }
            }

            .classDetails_right_right_stuCouList_item_couName {
              width: 75%;
              height: 64px;
              // background-color: blanchedalmond;
              display: flex;
              flex-direction: column;

              .classDetails_right_right_stuCouList_item_couNames {
                width: 100%;
                height: 40px;
                // background-color: palevioletred;
                display: flex;
                justify-content: space-between;

                .span3 {

                  display: block;
                  width: 50%;
                  height: 32px;
                  font-size: 16px;
                  padding-left: 5px;
                  line-height: 32px;
                }

                .span4 {
                  display: block;
                  width: 50%;
                  height: 32px;
                  font-size: 16px;
                  text-align: end;
                  padding-right: 5px;
                  line-height: 32px;
                }
              }

              .classDetails_right_right_stuCouList_item_process {
                width: 100%;
                height: 22px;
                padding-left: 5px;
                // background-color: #36A5ED;
              }
            }
          }
        }

      }
    }
  }

  .cla-searchStudent {
    width: 100%;
    height: 350px;
    // background-color: #3399ff;
    display: flex;
  }

  .cla-searchStudent-left {
    width: 48%;
    height: 350px;
    // background-color: #6EEE96;
    // margin: 0px 10px 0px 0px;
    border-radius: 10px;
    // box-shadow: 1px 3px 6px 1px lightgrey;

  }

  .cla-searchStudent-left-searchInput {
    width: 95%;
  }

  .cla-searchStudent-left-studentList {
    width: 95%;
    height: 320px;
    background: color #f2f2f2;
    ;
    overflow: hidden;
    overflow-x: hidden;
    overflow-y: auto;
  }

  .cla-searchStudent-left-studentList-item {
    width: 98%;
    height: 60px;
    // background-color: #e7693b;
    margin: 5px 5px 5px 5px;
    display: flex;
    border-radius: 10px;
  }

  .cla-searchStudent-left-studentList-item-img {
    width: 20%;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;

  }

  .cla-searchStudent-left-studentList-item-nameEmail {
    width: 80%;
    height: 60px;
    line-height: 60px;
  }

  .cla-searchStudent-right {
    width: 48%;
    height: 350px;
    // background-color: #e7693b;
    border-radius: 10px;
    // box-shadow: 1px 3px 6px 1px lightgrey;
  }

  .cla-searchStudent-right-top {
    width: 100%;
    height: 40px;
    text-align: center;
    line-height: 40px;
    font-size: 20px;
  }
}
</style>