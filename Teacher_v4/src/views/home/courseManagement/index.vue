<template>

  <div class="course">



    <!-- 工具栏 -->
    <!-- <div class="main_tool">
      <el-button type="primary" round icon="el-icon-circle-plus" @click="dialogFormVisible = true">添加课程</el-button>
    </div> -->
    <div class="operation">
      <div class="createclass">
        <button class="createButton el-icon-circle-plus" @click="dialogFormVisible = true">创建课程</button>
      </div>
      <div class="search">
        <div class="search-box">
          <input v-model="couName" @input="searchCourse" type="search" placeholder="请输入课程名" />
          <button type="submit" class="search-btn"><i class="el-icon-search"></i></button>
        </div>

      </div>
    </div>

    <!-- 课程列表 -->
    <div class="classes">
       <el-scrollbar style="height: 100%;" class="scrollbar-for">
      <ul class="classList">
        <el-empty description="暂无课程" v-if="courseItems.length == 0" class="classList"></el-empty>
        <li class="classList-item" v-for="(item, index) in courseItems" :keys="index">
          <div class="classList-item-img">
            <!-- 遮罩 -->
            <!-- <div class="img-shadow">
              <div class="img-icon">
                <i class="el-icon-view"></i>
              </div>
            </div> -->



            <img style="width: 100%;border-top-left-radius: 10px;border-top-right-radius: 10px;height:140px;"
              :src="item.couPicIO" alt="">
          </div>
          <div class="classList-item-other">
            <div class="classList-item-other-name">
              <span>
                {{ item.couName }}
              </span>

            </div>

            <div class="classList-item-other-buttons">
              <el-tooltip class="item" effect="dark" content="课程详情" placement="top">
                <el-button type="primary" icon="el-icon-edit" size="medium" circle @click="openEdit(item)"></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="章节管理" placement="top">
                <el-button type="success" icon="el-icon-s-order" size="medium" circle @click="toCourse(item)"></el-button>
              </el-tooltip>
              <el-tooltip class="item" effect="dark" content="删除课程" placement="top">
                <el-button type="danger" icon="el-icon-delete" size="medium" circle
                  @click="dialogVisible = true;openDel(item.id)">
                </el-button>
              </el-tooltip>
            </div>
          </div>
        </li>
      </ul>
       </el-scrollbar>
    </div>

    <!-- 添加课程对话框 -->
    <div>
      <el-dialog title="添加课程" :visible.sync="dialogFormVisible" class="dialog">
        <el-form :model="addForm" style="padding-top: 0px">
          <el-form-item label="课程名称" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input v-model="addForm.couName" autocomplete="off" style="width: 30%;"></el-input>
          </el-form-item>
          <el-form-item label="课程介绍" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input type="textarea" v-model="addForm.couIntroduction" rows="6" maxlength="250" show-word-limit>
            </el-input>
          </el-form-item>

          <el-form-item label="课程类别" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-select v-model="typeList2" placeholder="请选择">
              <el-option v-for="item in typeList1" :key="item.value" :label="item.value" :value="item">
              </el-option>
            </el-select>
            <el-select v-show="!typeList2==' '" v-model="addForm.couTypeId" placeholder="请选择">
              <el-option v-for="item in typeList2.ch_type" :key="item.value" :label="item.value" :value="item.typeId">
              </el-option>
            </el-select>
          </el-form-item>


          <el-form-item label="课程价格" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input-number v-model="addForm.couPrice" :min="0" :max="100" label="描述文字"></el-input-number>
          </el-form-item>

          <el-form-item label="课程封面" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-upload class="avatar-uploader" action="" list-type="picture-card" :show-file-list="false"
              :on-change="handleChange" :before-upload="beforeAvatarUpload" :headers="headers" :http-request="upload">
             <img v-if="imgUrl" :src="imgUrl" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
               <!-- <i class="el-icon-plus"></i> -->
            </el-upload>

          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="addCourse()">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <!-- 编辑课程信息对话框 -->
    <div>
      <el-dialog title="课程信息" :visible.sync="dialogEditVisible" class="dialog">
        <el-form :model="editForm">
          <el-form-item label="课程名称" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input v-model="editForm.couName" autocomplete="off" style="width: 30%;">
            </el-input>
          </el-form-item>
          <el-form-item label="课程介绍" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input type="textarea" v-model="editForm.couIntroduction" rows="6" maxlength="250" show-word-limit>
            </el-input>
          </el-form-item>
          <el-form-item label="课程类别" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-select v-model="typeList3">
              <el-option v-for="item in typeList1" :key="item.value" :label="item.value" :value="item">
              </el-option>
            </el-select>
            <el-select v-model="editForm.couTypeId">
              <el-option v-for="item in typeList3.ch_type" :key="item.typeId" :label="item.value" :value="item.typeId">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="课程价格" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-input-number v-model="editForm.couPrice" :min="0">
            </el-input-number>
          </el-form-item>



          <el-form-item label="课程封面" :label-width="formLabelWidth" style="margin-bottom: 10px;">
            <el-upload action="" list-type="picture-card" :show-file-list="false" :on-change="handleChange"
              :before-upload="beforeAvatarUpload" :headers="headers" :http-request="editimg" class="avatar-uploader">
              <img v-if="imgUrl" :src="imgUrl" class="avatar">
              <img v-else :src='oldImg' class="avatar">
            </el-upload>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogEditVisible = false">关 闭</el-button>
          <el-button type="primary" @click="editCourse()">修 改</el-button>
        </div>
      </el-dialog>
    </div>

    <!-- 删除课程对话框 -->
    <div>
      <el-dialog title="提示" :visible.sync="dialogVisible" width="30%">
        <span>是否删除课程</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="delCourse()">确 定</el-button>
        </span>
      </el-dialog>
    </div>


  </div>

</template>

<script>
  export default {
    name: "courseManagement",

    data() {
      return {
        flag: false,
        courseItems: [],
        courseItemsBackup: [],
        dialogFormVisible: false, //添加课程对话框
        dialogVisible: false, //删除课程对话框

        dialogEditVisible: false,
        formLabelWidth: '100px', //表单标签宽度
        delId: 0,
        imgUrl: "",
        oldImg: '',
        couName: '',

        addForm: { //添加课程提交表单
          couName: '',
          couIntroduction: '',
          couPrice: '',
          couType: 0,
          couPic: '',
          couTypeId: '',
        },
        addForm2:{
          couName: '',
          couIntroduction: '',
          couPrice: '',
          couType: 0,
          couTypeId: '',
        },
        typeList1: [{ //总类别列表（后台获取）
        }],
        typeList2: [ //临时子类变量

        ],
        typeList3: [ //临时子类变量

        ],
        editForm: { //编辑课程信息表单
          couName: '',
          couIntroduction: '',
          couPrice: '',
          couType: 0,
          oldCouPic: '',
          couTypeId: '',
        },
        showForm: {

        },
        headers: {
          ContentType: 'multipart/form-data'
        },

      }
    },

    methods: {
      getAllCourse() { //获取所有课程
        this.$http({
          method: 'get',
          url: '/course/course/teaQueryAllCourse'
        }).then((res) => {
          if (res.data.code == 200) {
            console.log(res.data.data)
            this.courseItems = res.data.data.courseDetailedVOS
            this.courseItemsBackup = res.data.data.courseDetailedVOS
            this.typeList1 = res.data.data.typeList1
            this.flag = true
          }
        })
      },
      searchCourse() { //查询班级
        console.log(this.couName)
        let flag = false;
        if (this.couName == '' || this.couName == null) {
          this.courseItems = this.courseItemsBackup
        } else {
          this.courseItems = []
          for (var i = 0; i < this.courseItemsBackup.length; i++) {
            if (this.courseItemsBackup[i].couName.includes(this.couName)) {
              console.log(1)

              this.courseItems.push(this.courseItemsBackup[i])
              flag = true
            }
          };
          if (flag == false) {
            this.$message({
              message: '课程不存在或课程名为空',
              type: 'warning'
            })
            this.courseItems = this.courseItemsBackup
          }
        }
      },
      upload(param) { //上传图片
        const formData = new FormData()
        formData.append('multipartFile', param.file)
        this.$http({
          method: 'post',
          url: '/course/course/courseFile',
          data: formData
        }).then((res) => {
          // if(res.data.code)
          console.log(res.data)
          if(res.data.code == 200){
            this.$message({
              message: '上传成功',
              type: 'success'
            });
            console.log(res.data)
            this.addForm.couPic = res.data.data
          }else{
            this.$message.error('图片上传失败')
          }


        })
      },
      editimg(param) { //修改图片
        const editData = new FormData()
        editData.append('multipartFile', param.file)
        this.$http({
          method: 'post',
          url: '/course/course/courseFile',
          data: editData
        }).then((res) => {

          if(res.data.code == 200){
            this.$message({
              message: '上传成功',
              type: 'success'
            });
             this.editForm.newCouPic = res.data.data
          }else{
            this.$message.error('图片上传失败')
          }




        })
      },
      addCourse() { //添加课程的方法
        this.addForm.couType = 0

        if(this.addForm.couPic ==""){
          this.addForm2.couIntroduction = this.addForm.couIntroduction
          this.addForm2.couName = this.addForm.couName
          this.addForm2.couPrice = this.addForm.couPrice
          this.addForm2.couTypeId = this.addForm.couTypeId
          this.addForm2.couType = this.addForm.couType
          this.addForm = this.addForm2

        }

        console.log(this.addForm)
        this.$http({
          method: 'post',
          url: '/course/course/addCourse',
          data: this.addForm
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: '添加成功',
              type: 'success'
            });
            this.getAllCourse()
                            

          }
          this.addForm = {}
          this.typeList2 = []
          this.imgUrl = ""
        })
        this.dialogFormVisible = false

      },
      delCourse() { //确认删除课程的方法
        this.$http({
          method: 'get',
          url: 'course/course//deleteCourseById',
          params: {
            id: this.delId
          }
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            });
            this.getAllCourse()
          } else {
            this.$message.error(res.data.msg);
          }
        })
        this.dialogVisible = false
      },
      handleChange(file, fileList) { //图片变化时
        this.imgUrl = URL.createObjectURL(file.raw);
        this.$message({
          message: '上传中请等待',
          type: 'warning'
        });
      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('图片上传失败，上传图片只能是 JPG 格式!,请更新图片!');
        }
        if (!isLt2M) {
          this.$message.error('图片上传失败，上传图片大小不能超过 2MB!,请更新图片!');
        }
        return isJPG && isLt2M;
      },
      openEdit(r) { //打开编辑界面
        this.typeList1.forEach((v) => {
          if (v.typeId == r.parentTypeId) {
            this.typeList3 = v
          }
        })
        this.imgUrl = ''

        this.editForm.couTypeId = ''
        this.editForm.id = r.id
        this.editForm.couId = r.couId
        this.editForm.couName = r.couName
        this.editForm.couIntroduction = r.couIntroduction
        this.editForm.couPrice = r.couPrice
        this.editForm.couTypeId = r.typeId
        this.editForm.oldCouPic = r.couPic
        this.editForm.newCouPic = null
        this.oldImg = r.couPicIO


        //console.log(r)
        this.dialogEditVisible = true
      },
      editCourse() { //确认编辑课程信息
        console.log(this.editForm)

        this.$http({
          method: 'post',
          url: 'course/course/updateCourseById',
          data: this.editForm
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            })
            this.getAllCourse()
          }
          console.log(res.data)
        })


        this.dialogEditVisible = false


      },
      // closeEdit() {
      //   //this.getAllCourse()

      // },
      openDel(id) {
        this.delId = id
      },
      toCourse(cou) { //跳转章节
        console.log("跳转")
        console.log(cou)
        this.$router.push({
          path: "/home/courseDetails",
          query: {
            couId: cou.couId,
            couName: cou.couName,
            couPicIO: cou.couPicIO
          }
        })
      },

    },

    created() {
      this.getAllCourse()
      console.log(1)
    }


  };
</script>

<style lang="scss" scoped>
  $white: #ffffff;
  $black: #000000;
  $orange: #e7693b;
  $blue: #81CCE8;
  $font: 'Lato',
  sans-serif;
  $ease: all .3s ease;

  a {
    text-decoration: none;
  }

  .router-link-active {
    text-decoration: none;
  }



  .avatar-uploader {
    margin-top: 20px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    width: 240px;
    height: 180px;

    /deep/.el-upload--picture-card {
      width: 240px;
      height: 180px;
      line-height: 100px;
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
    }

    .avatar {
      width: 240px;
      height: 180px;
      display: flex;
    }

    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 240px;
      height: 180px;
      line-height: 180px;
      text-align: center;
    }
  }
  .dialog{
    /deep/.el-dialog__body {
        padding-top: 10px;
        padding-bottom: 0px;
    }
  }

  .course {
    width: 85%;
    height: 680px;
    margin: auto;
    // margin-right: 0;
    margin-top: 10px;
    background-color: white;
    border-radius: 1%;
    overflow: hidden;
    //overflow-y: scroll; // 设置y轴方向的滚动条

    .head {
      width: 100%;
      border-radius: 1%;
      height: 55px;
      //box-shadow:  0 1px  rgba(0, 0, 0 ,0.24);
      background-color: #8BA3C7;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      p {
        color: aliceblue;

      }
    }

    .main_tool {
      .el-button {
        margin-left: 4%;
        margin-top: 1%;
      }
    }

    .main_center {
      .main_center_items {
        list-style: none;

        // .main_center_item:hover {
        //   .item_image .item_span {
        //     display: block;
        //   }
        //  }
        .main_center_item:hover {
          .item_name h3 {
            color: #3a8bff;
          }
        }

        .main_center_item {
          float: left;
          width: 240px;
          height: 220px;
          //background-color: #8BA3C7;
          margin: 0px 55px 40px 0px;

          &:hover {
            .item_image .item_span {
              display: block;
            }
          }

          .item_image {
            width: 240px;
            height: 180px;
            position: relative;
            border-radius: 5%;

            //background-image: url("../../../assets/reg_bg_min.jpg");

            .item_span {
              display: none;
            }

            img {
              width: 100%;
              height: 170px;
              border-radius: 5%;
            }

            //background-size: 100% 100%;
            ul {
              position: absolute;
              top: 0;
              right: 0;
              background-color: rgb(255, 255, 255);
              opacity: 0.5;
              border-radius: 0 5px 0 5px;
              padding: 0 0;

              //box-sizing: border-box;
              //z-index: 10;
              li {
                //float: left;
                list-style-type: none;
                font-size: 12px;
                color: #3a8bff;
                display: inline-block;
                padding: 0 5px 5px 5px;
                //box-sizing: border-box;
              }
            }
          }

          .item_name {
            width: 240px;
            height: 40px;
            padding: 0 0px;

            h3 {
              width: 100%;
              position: relative;
              font-size: 16px;
              font-weight: bold;
              color: #000;
            }
          }
        }
      }
    }
  }

  .classes {
    width: 100%;
    height: 630px;
    // background-color: #3399ff;
    /deep/ .el-scrollbar__wrap {
      overflow-x: hidden;
    }
  }

  .classList {
    width: 95%;
    height: 100%;
    // background-color: #8ba3c9;
    //display: flex;
    //flex-wrap: wrap;
    margin-top: 10px;
    margin-left: 10px;
    list-style-type: none;
    // justify-content: space-around;
  }

  .classList-item {
    float: left;
    width: 260px;
    height: 220px;
    background-color: white;
    margin-bottom: 40px;
    margin-right: 45px;
    border-radius: 10px;
    box-shadow: 1px 3px 6px 1px lightgrey;
    list-style: none;
  }

  .classList-item-img {
    position: relative;
    width: 100%;
    height: 140px;

    // background-color: #3399ff;
    &:hover {
      .img-shadow {
        display: block;
      }
    }

  }

  .img-shadow {
    position: absolute;
    display: none;
    width: 100%;
    height: 60%;
    border-top-left-radius: 10px;
    border-top-right-radius: 10px;
    height: 180px;
    background-color: black;
    opacity: 0.6;


    .img-icon {
      width: 100%;
      height: 100%;
      //background-color: #3a8bff;
      font-size: 70px;
      color: #8BA3C7;
      display: flex;
      align-content: center;
      justify-content: center;
      align-items: center;

    }

  }

  .classList-item-other {
    // width: 100%;
    // height: 40%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .classList-item-other-name {
    // width: 100%;
    // height: 37px;
    // background-color: red;
    margin-top: 5px;
    margin-bottom: 5px;
    font-size: 16px;
  }

  .classList-item-other-buttons {
    // width: 94%;
    // display: flex;
    // justify-content: flex-end;
  }



  .operation {
    // background-color:black;
    width: 100%;
    height: 45px;
    display: flex;
  }

  .createclass {
    width: 150px;
    height: 45px;
    flex: 50%;
    // background-color: blue;
  }

  .createButton {
    width: 16%;
    height: 35px;
    border-radius: 20px;
    margin-left: 50px;
    margin-top: 10px;
    background-color: #3399ff;
    border: none;
    color: white;
  }

  .search {
    flex: 50%;
    width: 100%;
    height: 45px;
    // background-color: aqua;

  }

  .search-box {
    margin-top: 10px;
    display: flex;
    margin-left: 49%;
    align-items: center;
  }

  input[type=search] {
    // box-shadow: 10px 10px 4px rgba($black, .4);
    background: $white;
    border: 2px solid #3399ff;
    outline: none;
    width: 200px;
    height: 30px;
    border-radius: 15px 0 0 15px;
    font-size: 13px;
    font-weight: 300;
    padding: 0px 10px;
    letter-spacing: 2px;
    text-transform: uppercase;
    color: $orange;
  }

  ::placeholder {
    color: $blue;
    font-size: .8em;
  }

  .search-btn {
    // box-shadow: 10px 10px 4px rgba($black, .4);
    height: 30px;
    width: 45px;
    outline: none;
    border-radius: 0 15px 15px 0;
    background: #3399ff;
    color: white;
    border: none;
    transition: $ease;

    &:hover {
      transition: $ease;

      i {
        font-size: 1.5em;
      }
    }

    i {
      font-size: 1.3em;
    }
  }

  // .item_span {
  //   display: none;
  //   //background-color: #000;

  // }


  //   .item_image:hover .item_span{

  //     display: block;
  //   }
</style>
