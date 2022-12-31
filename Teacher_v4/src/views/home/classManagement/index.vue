<template>
  <div class="cla-de">
    <div class="container">

      <div class="main">
        <div class="operation">
          <div class="createclass">
            <button class="createButton el-icon-circle-plus" @click="createClassDialogVisible = true">创建班级</button>
          </div>
          <div class="search">
            <div class="search-box">
              <input  @input="searchClass()" v-model="className" type="search" placeholder="请输入班级名称" />
              <button type="submit" class="search-btn"><i class="el-icon-search"></i></button>
            </div>

          </div>
        </div>
        <div class="classes">
          <el-scrollbar style="height: 100%;" class="scrollbar-for">
          <ul class="classList">
            <el-empty description="暂无班级" v-if="classList.length == 0" class="classList"></el-empty>
            <li class="classList-item" v-for="(item, index) in showClassList" :keys="index"
              v-if="classList.length != 0">
              <div class="classList-item-img">
                <img style="width: 100%;border-top-left-radius: 10px;border-top-right-radius: 10px;height:140px;"
                  :src="item.classLogoIO" alt="">
              </div>
              <div class="classList-item-other">
                <div class="classList-item-other-name">
                  <span>
                    {{ item.className }}
                  </span>

                </div>

                <div class="classList-item-other-buttons">
                  <el-tooltip class="item" effect="dark" content="修改班级信息" placement="top">
                    <el-button type="primary" icon="el-icon-edit" circle
                      @click="editClassDialogVisible = true; openEditClass(item)">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" content="查看课程" placement="top">
                    <el-button type="success" icon="el-icon-reading" circle
                      @click="toAddCourse(item.claId, item.className, item.classLogoIO)">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" content="人员管理" placement="top">
                    <el-button type="primary" icon="el-icon-s-custom" circle @click="getClassDetails(item.id)">
                    </el-button>
                  </el-tooltip>
                  <el-tooltip class="item" effect="dark" content="删除班级" placement="top">
                    <el-button type="danger" icon="el-icon-delete" circle @click="deleteClass(item.id)"></el-button>
                  </el-tooltip>

                </div>
              </div>
            </li>
          </ul>
          </el-scrollbar>
        </div>
      </div>
    </div>
    <div>
      <el-dialog center width="40%" title="创建班级" :visible.sync="createClassDialogVisible">
        <el-form>
          <el-form-item label="班级名称" :label-width="formLabelWidth">
            <el-input style="width:300px;" autocomplete="off" v-model="oneClass.className"></el-input>
          </el-form-item>
          <el-form-item label="班级详情" :label-width="formLabelWidth">
            <el-input style="width:350px;" type="textarea" :rows="2" placeholder="请输入内容不可超过两百字"
              v-model="oneClass.classDetails">
            </el-input>
          </el-form-item>
          <el-form-item label="班级logo" :label-width="formLabelWidth">
            <el-upload action="sssss" list-type="picture-card" :on-remove="handleRemove" :headers="headers"
              :http-request="uploadImage">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="dialogVisible">
              <img width="100%" :src="dialogImageUrl" alt="">
            </el-dialog>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="createClassDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="createClassDialogVisible = false; createClass()">确 定</el-button>
        </div>
      </el-dialog>

    </div>

    <!-- 修改班级信息的弹窗 -->
    <div>
      <el-dialog center width="40%" title="修改信息" :visible.sync="editClassDialogVisible">
        <el-form>
          <el-form-item label="班级名称" :label-width="formLabelWidth">
            <el-input style="width:300px;" autocomplete="off" v-model="editClass.className"></el-input>
          </el-form-item>
          <el-form-item label="班级详情" :label-width="formLabelWidth">
            <el-input style="width:350px;" type="textarea" :rows="2" placeholder="请输入内容不可超过两百字"
              v-model="editClass.classDetails">
            </el-input>
          </el-form-item>
          <el-form-item label="班级logo" :label-width="formLabelWidth">
            <el-upload action="" list-type="picture-card" :show-file-list="false" :on-change="handleChange"
              :headers="headers" :http-request="updateloadImage" class="avatar-uploader">
              <img v-if="imgUrl" :src="imgUrl" class="avatar">
              <img v-else :src='oldClassLogoIO' class="avatar">
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="editClassDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="editClassDialogVisible = false; updateClass()">确 定</el-button>
        </div>
      </el-dialog>

    </div>

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
              <div v-for="item in searchStudentList" class="cla-searchStudent-left-studentList-item">
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
            <div v-for="item in toRightStudent" class="cla-searchStudent-left-studentList-item">
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
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="dialogFormVisible = false; classAddStudentList()">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getToken } from '../../../utils/auth';
export default {
  name: "classManagement",
  data() {
    return {
      imgUrl: '',
      searchStudentList: [],
      toRightStudent1: [],
      toRightStudent: [],
      editClassDialogVisible: false,
      dialogFormVisible: false,
      studentEmail: '',
      classDetails: {},
      headers: { Authorization: getToken(), ContentType: 'multipart/form-data' },
      classId: -1,
      className: '',
      dialogImageUrl: '',
      dialogVisible: false,
      formLabelWidth: '150px',
      createClassDialogVisible: false,
      checkClassDialogVisible: false,
      people: 38,
      classLogo: '',
      classList: [],
      showClassList: [],
      oneClass: {
        className: '',
        classDetails: '',
        classLogo: ''
      },
      editClass: {
        id: 0,
        className: '',
        classDetails: '',
        newClassLogo: '',
        oldClassLogo: '',
      },
      oldClassLogoIO: ''
    }
  },

  mounted() {
    this.getAllClassById()
  },
  methods: {

    handleChange(file, fileList) { //图片变化时
      this.imgUrl = URL.createObjectURL(file.raw);

    },

    async uploadImage(param) {// 上传图片方法
      const formData = new FormData()
      console.log(param)
      formData.append('multipartFile', param.file)
      const result = await this.$API.webClass.classFile(formData)
      console.log(result.data.data)
      this.oneClass.classLogo = result.data.data
    },

    async updateloadImage(param) {// 上传图片方法
      const formData = new FormData()
      console.log(param)
      formData.append('multipartFile', param.file)
      const result = await this.$API.webClass.classFile(formData)
      console.log(result.data.data)
      this.editClass.newClassLogo = result.data.data
    },

    async updateClass() {
      try {
        await this.$API.webClass.updateClass(this.editClass)
      } catch (error) {
        console.log(error)
      }
      this.getAllClassById()
      return this.$message.success('修改成功')
    },

    openEditClass(item) {
      this.editClass.id = item.id
      this.editClass.className = item.className
      this.editClass.oldClassLogo = item.classLogo
      this.editClass.classDetails = item.classDetails
      this.oldClassLogoIO = item.classLogoIO
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
    },
    async classAddStudentList() {//添加学生
      try {
        this.students.classId = this.claId
        this.students.stuIds = this.addStudentId
        const result = await this.$API.webClass.addClassStudent(this.students)
        this.getClassDetailsById()
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
        console.log(this.searchStudentList)
      }

    },

     getClassDetails(id) {//获取班级详情
      this.toclassById(id)
    },

    async getAllClassById() {//获取全部班级
      const result = await this.$API.webClass.getAllClassById()
      this.classList = result.data.data
      this.showClassList = result.data.data
      console.log(this.classList)
    },
    async createClass() {//添加班级
      try {

        await this.$API.webClass.createClass(this.oneClass)
        this.getAllClassById()
        return this.$message.success('添加成功')
      } catch (error) {
        return this.$message.info(error)
      }
    },



    searchClass() {//查询班级
      let flag = false;
      if (this.className == '' || this.className == null) {
        this.showClassList = this.classList
      } else {
        this.showClassList = []
        for (var i = 0; i < this.classList.length; i++) {
          if (this.classList[i].className.includes(this.className)) {

            this.showClassList.push(this.classList[i])
            flag = true
          }
        };
        if (flag == false) {
          this.$message({
            message: '班级不存在或班级名为空',
            type: 'warning'
          })
          this.showClassList = this.classList
        }
      }

    },

    handleRemove(file, fileList) {//添加图片时移除图片
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      console.log(file.url)
      this.dialogVisible = true;
    },

    async deleteClass(id) {//删除班级
      console.log(id)
      const confirmResult = await this.$confirm('此操作将该班级，是否继续?', '提示', {
        type: 'warning'
      }).catch((err) => {
      })

      if (confirmResult !== 'confirm') {
        return this.$message.info('已经取消删除')
      }
      const result = await this.$API.webClass.deleteClass(id)
      this.getAllClassById()
      return this.$message.success('删除成功')
    },

    toclassById(id) {//携带参数跳转到班级详情页面
      this.$router.push({
        path: "/classDetails",
        query: {
          id: id
        }
      })
    },

    toAddCourse(id, className, classLogoIO) {//携带参数跳转到添加课程
      this.$router.push({
        path: "/home/addCourse",
        query: {
          id: id,
          className: className,
          classLogoIO: classLogoIO
        }
      })
    },

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



.cla-de {
  width: 85%;
  height: 680px;
  margin: auto;
  // margin-right: 0px;
  margin-top: 10px;
  overflow: hidden;

  border-radius: 10px;
  background-color: rgb(255, 255, 255);
}

.container {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.head {
  width: 100%;
  height: 45px;
  background-color: #8ba3c9;
  text-align: center;

}

.title {
  line-height: 45px;
  font-size: 22px;

}

.main {
  flex: 93%;
  // background-color: aqua;
  display: flex;
  flex-direction: column;
  // background: #E2DDDE;

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

.cla-noneStudent {
  width: 100%;
  height: 350px;
  background-color: #3399ff;
}
</style>
