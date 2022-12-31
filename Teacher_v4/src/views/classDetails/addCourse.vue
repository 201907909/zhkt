
<template>
  <div class="addCourse_body">
    <div class="addCourse_body_left">
      <div class="addCourse_body_left_top">
        <div class="addCourse_body_left_top_claImg">
          <img :src="classLogoIO" alt="">
        </div>
        <div class="addCourse_body_left_top_claName">{{ className }}</div>
      </div>


      <div class="addCourse_body_left_foot">
        <div class="addCourse_body_left_foot_categoryList">
          <div class="addCourse_body_left_foot_categoryList_item" v-for="item in showCategory"
            @click="selectCourseByCategory(item.type)">
            <div class="addCourse_body_left_foot_categoryList_item_img">
              <img :src="item.img.img" alt="">
              <!-- <el-image style="width: 100px; height: 100px" :src="item.img" :fit="fit"></el-image> -->
            </div>
            <div class="addCourse_body_left_foot_categoryList_item_name">
              <span>{{ item.type }}</span>
              <span>{{ item.num }}</span>
            </div>
          </div>
        </div>
      </div>

    </div>
    <div class="addCourse_body_right">
      <div class="addCourse_body_right_contian">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="已有课程" name="first">
            <div class="addCourse_body_right_contian_courseList">
              <el-empty description="暂无课程" v-if="tableData.length == 0" class="addCourse_body_right_contian"></el-empty>
              <div class="addCourse_body_right_contian_courseList_item" v-for="item in showtableDate"
                v-if="tableData.length != 0">
                <div class="addCourse_body_right_contian_courseList_item_img">
                  <img :src="item.couPicIO" alt="" @click="toCourse(item)">
                  <span @click="delCourse(item.couId)">删除课程</span>
                </div>
                <div class="addCourse_body_right_contian_courseList_item_info">
                  <span class="addCourse_body_right_contian_courseList_item_info_span">{{ item.couName }}</span>
                  <span>类别:{{ item.parentTypeName }}</span>
                  <span>{{ item.teaName }}</span>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="可添加课程" name="second">
            <div class="searchCourse">
              <el-input v-model="courseName" placeholder="请输入课程名称" @input="searchCourse()" style="width:200px;"
                suffix-icon="el-icon-search"></el-input>
            </div>
            <div class="addCourse_body_right_contian_courseList">
              <div class="addCourse_body_right_contian_courseList_item" v-for="(item, index) in showAllCourse"
                :key="index">
                <div class="addCourse_body_right_contian_courseList_item_img" @click="toCourse(item)">
                  <img :src="item.couPicIO" alt="">
                  <!-- <span @click="delCourse(item.couId)">删除课程</span> -->
                </div>
                <div class="addCourse_body_right_contian_courseList_item_info">
                  <span class="addCourse_body_right_contian_courseList_item_info_span">{{ item.couName }}</span>
                  <span>{{ item.parentTypeName }}</span>
                  <span>{{ item.teaName }}</span>
                </div>
                <div class="addButtons">
                  <el-button v-if="item.state == 0" type="primary" icon="el-icon-plus" round size="small"
                    @click="addCourse(item.couId, index)">添加</el-button>
                  <el-button v-if="item.state == 1" type="success" icon="el-icon-check" round
                    size="small">已有</el-button>
                </div>
              </div>
              <div class="pagination"><el-pagination background layout="total,prev, pager, next, jumper" :total="total"
                  @size-change="handleSizeChange" @current-change="handleCurrentChange"
                  :current-page.sync="currentPage3" :page-size="pageSize">
                </el-pagination></div>
            </div>

          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      tempTotal: 0,
      courseName: '',
      pageSize: 8,
      currentPage3: 1,
      tabName: 'first',
      showAddButton: true,
      addIndex: -1,
      addType: 'primary',
      addIcon: 'el-icon-plus',
      total: 0,
      activeName: 'first',
      showCategory: [],
      category: [],
      category2: [{ num: 12, img: require('@/assets/财务.png') }, { num: 12, img: require('@/assets/法律.png') }, { num: 12, img: require('@/assets/护理.png') }, { num: 12, img: require('@/assets/体育跨栏.png') }, { num: 12, img: require('@/assets/医疗.png') }, { num: 12, img: require('@/assets/商业.png') }, { num: 12, img: require('@/assets/生活.png') }, { num: 12, img: require('@/assets/编程窗口.png') }],
      categoryImg: [{ img: require('@/assets/法律1.png') }, { img: require('@/assets/护理1.png') }, { img: require('@/assets/体育跨栏1.png') }, { img: require('@/assets/财务1.png') }, { img: require('@/assets/医疗1.png') }, { img: require('@/assets/商业1.png') }, { img: require('@/assets/生活1.png') }, { img: require('@/assets/编程窗口1.png') }],
      showOtherCourse: false,
      showClassCourse: true,
      allCourse: [],
      showAllCourse: [],
      classId: -1,
      tableData: [],
      showtableDate: [],
      className: "",
      classLogoIO: "",
      dialogDelVisible: false,
      dialogAddVisible: false,
      formLabelWidth: '100px', //表单标签宽度
      form: {
        value: ''
      },
      addCourses: {
        classId: '',
        couIds: []
      },
      needDeleteCouser: {
        classId: '',
        couIds: []
      },
      classCourse: {
        classId: 0,
        startIndex: 0,
        pageSize: 8,
      },
      allClassCourseType: [],
      showAllClassCourseType: [],
      courseAll: [],
      flag: false,
      addFlag: false,
      tempCategory: [],
      categoryTpye: '',



    }
  },

  mounted() {
    this.classId = this.$route.query.id
    this.className = this.$route.query.className
    this.classLogoIO = this.$route.query.classLogoIO
    this.getAllClassCourse().then(() => {

    })
    // this.teaQueryAllCourse()

  },
  methods: {
    toCourse(cou) { //跳转章节
      console.log("跳转")
      console.log(cou)
      this.$router.push({
        path: "/home/courseShow",
        query: {
          couId: cou.couId,
          couName: cou.couName,
          couPicIO: cou.couPicIO
        }
      })
    },
    searchCourse() {
      this.flag = false;
      if (this.courseName == '' || this.courseName == null) {
        this.showAllCourse = this.allCourse
        this.total = this.tempTotal
        console.log(this.courseAll.length)
      } else {
        this.showAllCourse = []
        for (var i = 0; i < this.allCourse.length; i++) {
          if (this.allCourse[i].couName.includes(this.courseName)) {
            this.showAllCourse.push(this.allCourse[i])
            this.flag = true
          }
        };
        if (this.flag == false) {
          this.$message({
            message: '不存在该课程',
            type: 'warning'
          })
          this.showAllCourse = this.allCourse
          // this.total=this.showAllCourse.length
        }
        this.total = this.showAllCourse.length
      }
      //
    },

    selectCourseByCategory(type) {
      this.categoryTpye = type
      if (this.tabName == 'first') {
        this.showtableDate = []
        this.tableData.forEach(item => {
          if (item.parentTypeName == type) {
            this.showtableDate.push(item)
          }
        })
      }
      if (this.tabName == 'second') {
        this.showAllCourse = []
        this.tempCategory = []
        for (var i = 0; i < this.courseAll.length; i++) {
          if (this.courseAll[i].parentTypeName == type) {
            this.tempCategory.push(this.courseAll[i])
          }
          this.total = this.tempCategory.length
        }
        for (var j = 0; j < 8; j++) {
          if (this.tempCategory[j] == null) {
            break
          } else {
            this.showAllCourse.push(this.tempCategory[j])
          }

        }
      }

    },

    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
    },
    async handleCurrentChange(val) {
      if (this.categoryTpye != '') {
        console.log(this.categoryTpye + "+++++++++++++++++++++")
        this.showAllCourse = []
        const current = val - 1
        const size = 8
        for (var i = current * size; i < 8 + (current * size); i++) {
          if (this.tempCategory[i] == null) {
            break
          }
          this.showAllCourse.push(this.tempCategory[i])
        }
      } else {
        this.classCourse.startIndex = val - 1
        const result = await this.$API.webClass.pageCourse(this.classCourse)
        this.showAllCourse = result.data
      }

    },

    async handleClick(tab, event) {
      console.log(tab.name, event);
      this.tabName = tab.name
      if (tab.name == 'second') {
        const result2 = await this.$API.webClass.pageCourse(this.classCourse)
        this.showAllCourse = result2.data
        this.total = this.tempTotal
        this.getAllCourseCategory()
      }
      if (tab.name == 'first') {
        const result2 = await this.$API.webClass.queryCourseByClassId(this.classCourse)
        this.showtableDate = result2.data.data.courses
        this.category = result2.data.data.classCourseType
        this.getCategory()
      }
    },


    async getAllCourse() {
      const result2 = await this.$API.webClass.pageCourse(this.classCourse)
      this.showAllCourse = result2.data
      this.total = this.tempTotal
      this.courseAll = result2.data.data.courseAllComplete
      this.$forceUpdate()

    },

    async getAllClassCourse() {//获取该班级的课程
      this.classCourse.classId = this.classId
      const result = await this.$API.webClass.queryCourseByClassId(this.classCourse)
      console.log(result.data.data)

      this.tableData = result.data.data.courses
      this.showtableDate = result.data.data.courses

      this.category = result.data.data.classCourseType
      this.getCategory()

      this.$forceUpdate()
      this.allClassCourseType = result.data.data.allCourseType
      this.allCourse = result.data.data.courseAll

      this.courseAll = result.data.data.courseAllComplete

      this.tempTotal = result.data.data.courseAllComplete.length
    },

    getAllCourseCategory() {
      if (this.allClassCourseType.length > 0) {
        for (var i = 0; i < this.allClassCourseType.length; i++) {
          for (var j = 0; j < this.categoryImg.length; j++) {
            this.$set(this.allClassCourseType[i], 'img', this.categoryImg[i])
          }
        }
        this.showAllClassCourseType = this.allClassCourseType
        this.showCategory = this.showAllClassCourseType
      }
    },
    getCategory() {
      if (this.category.length > 0) {
        for (var i = 0; i < this.category.length; i++) {
          for (var j = 0; j < this.categoryImg.length; j++) {
            this.$set(this.category[i], 'img', this.categoryImg[i])
          }
        }
        this.showCategory = this.category
        console.log(this.showCategory)
      }
    },


    async addCourse(couId, index) { //添加课程
      this.addIndex = index
      this.addCourses.couIds = []
      this.addCourses.classId = this.classId
      this.addCourses.couIds.push(couId)
      await this.$API.webClass.classAddCouList(this.addCourses)
      const result = await this.$API.webClass.pageCourse(this.classCourse)
      this.showAllCourse = result.data
      return this.$message.success('添加成功')
    },

    async delCourse(couId) { //删除课程
      this.needDeleteCouser.couIds = []
      this.needDeleteCouser.classId = this.classId
      this.needDeleteCouser.couIds.push(couId)
      await this.$API.webClass.deleteCourseBanch(this.needDeleteCouser)
      this.getAllClassCourse()
      return this.$message.success('删除成功')

    }
  }
};

</script>

<style lang="scss" scoped>
.addCourse_body {
  width: 86%;
  height: 700px;
  margin: auto;
  margin-top: 10px;
  // background-color: white;
  display: flex;
  justify-content: space-around;

  .addCourse_body_left {
    width: 24%;
    height: 700px;
    background-color: white;
    border-radius: 10px;


    .addCourse_body_left_foot {
      width: 100%;
      height: 580px;
      // background-color: pink;
      margin-top: 10px;

      .addCourse_body_left_foot_categoryList {
        width: 96%;
        height: 570px;
        // background-color: aqua;
        margin: auto;
        display: flex;
        flex-wrap: wrap;

        .addCourse_body_left_foot_categoryList_item {
          width: 48%;
          height: 130px;
          // background-color: pink;
          margin-right: 5px;
          margin: auto;
          border-radius: 10px;
          display: flex;
          // box-shadow: 1px 1px 1px 1px lightgrey;
          border: 1px solid #dbdbdb;

          .addCourse_body_left_foot_categoryList_item_name {
            width: 50%;
            height: 130px;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;

            span {
              display: block;
              width: 100%;
              height: 30px;
              text-align: center;
              font-size: 15px;
              font-weight: bold;
            }
          }

          .addCourse_body_left_foot_categoryList_item_img {
            width: 50%;
            height: 130px;
            display: flex;
            align-items: center;
            justify-content: center;

            img {
              width: 60px;
              height: 60px;
            }
          }
        }
      }
    }

    .addCourse_body_left_top {
      width: 100%;
      height: 110px;
      display: flex;
      justify-content: space-around;
      border-bottom: 1px solid #dbdbdb;

      .addCourse_body_left_top_claName {
        width: 56%;
        height: 110px;
        line-height: 110px;
        font-size: 30px;
        font-weight: bold;
        text-align: center;
      }

      .addCourse_body_left_top_claImg {
        width: 42%;
        height: 110px;
        display: flex;
        justify-content: center;
        align-items: center;

        img {
          width: 90%;
          height: 92%;
          border-radius: 10px;
        }
      }
    }
  }

  .addCourse_body_right {
    width: 74%;
    height: 700px;
    background-color: white;
    border-radius: 10px;
    display: flex;
    // justify-content: center;
    // align-items: center;

    /deep/.el-tabs__item {
      /* 修改为您想要的文字大小 */
      font-size: 17px !important;
      font-weight: bold;
    }

    .addCourse_body_right_contian {
      width: 96%;
      height: 630px;
      // background-color: pink;
      margin: auto;
      margin-top: 25px;

      .searchCourse {
        float: right;
        margin-bottom: 10px;
        margin-right: 10px;
      }

      .addCourse_body_right_contian_courseList {
        width: 100%;
        height: 570px;
        margin-left: 9px;
        // background-color: pink;
        display: flex;
        flex-wrap: wrap;
        align-content: flex-start;
       

        // justify-content: space-around;
        .pagination {
          position: absolute;
          top: 530px;
          left: 250px;
        }

        .addCourse_body_right_contian_courseList_item {
          width: 23%;
          height: 200px;
          // background-color: aqua;
          // margin-right:10px;
          margin-right: 16px;
          margin-bottom: 40px;
          border: 1px solid #dbdbdb;
          border-radius: 10px;

          .addButtons {
            width: 100%;
            position: relative;
            top: -45px;
            left: 130px;

          }

          &:hover {
            .addCourse_body_right_contian_courseList_item_info_span {
              color: #14a4ec;
              cursor: pointer;
            }
          }

          .addCourse_body_right_contian_courseList_item_info {
            width: 100%;
            height: 70px;

            span {
              display: block;
              width: 100%;
              height: 20px;
              padding-left: 10px;
              font-size: 12px;
              line-height: 22px;
            }

            .addCourse_body_right_contian_courseList_item_info_span {
              margin-top: 5px;
              font-size: 15px;
              font-weight: bold;
            }

          }

          .addCourse_body_right_contian_courseList_item_img {
            width: 100%;
            height: 130px;

            &:hover {
              span {
                display: block;
              }

            }

            img {
              width: 100%;
              height: 130px;
              border-top-right-radius: 10px;
              border-top-left-radius: 10px;
            }

            span {
              text-align: center;
              position: relative;
              top: -133px;
              left: 145px;
              display: block;
              width: 75px;
              height: 20px;
              background-color: white;
              font-size: 14px;
              border-bottom-left-radius: 10px;
              border-top-right-radius: 10px;
              display: none;
              cursor: pointer;
            }
          }

        }
      }
    }
  }
}
</style>
