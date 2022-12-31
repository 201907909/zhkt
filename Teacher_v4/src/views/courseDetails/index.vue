<template>
  <div class="course_body">

    <!-- 课程名 -->
    <div class="course_head">
      <div class="head">




        <img class="head_img" :src="course.couPicIO"/>




        <h3>{{course.couName}}</h3>




      </div>
    </div>




    <!-- 章节管理 -->
    <div class="course_chapter">
      <div class="chapter_title">
        <h2>章节管理</h2>


        <div class="title_btn">
          <el-button v-show="saveEditBtn == true" type="primary" @click="indexEditFlag = true;saveEditBtn = false"
            size="small">
            修改章节顺序
          </el-button>
          <el-button v-show="saveEditBtn == false" type="primary" @click="indexEditSave()" size="small">
            保存编辑
          </el-button>
          <el-button type="primary" @click="dialogAddVisible = true" size="small">
            添加章节
          </el-button>
        </div>
      </div>


      <div class="chapter_items">
        <el-table
          :data="showTable.filter(data => !search || data.chaTitle.toLowerCase().includes(search.toLowerCase()))" stripe
          style="width: 100%" height="500px">

          <el-table-column label="章节顺序" min-width="120">
            <template slot-scope="scope">
              <span style="margin-left: 10px">第{{ scope.row.chaIndex }}章</span>
            </template>
          </el-table-column>

          <el-table-column prop="chaTitle" label="章节名称" min-width="150">
          </el-table-column>


          <el-table-column prop="chaUrl" label="视频地址" min-width="450">
            <template slot-scope="scope">
              <el-link @click="openVideo(scope.row.chaUrl)">
                {{scope.row.chaUrl}}
              </el-link>
            </template>
          </el-table-column>

          <el-table-column prop="chaTime" label="时长/秒" min-width="100">
          </el-table-column>

          <el-table-column label="操作" min-width="150">
            <template slot="header" slot-scope="scope">
              <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
            </template>

            <template slot-scope="scope">

              <el-button v-show="indexEditFlag == false" type="primary" icon="el-icon-edit" circle
                @click="dialogEditVisible = true;openEdit(scope.row)"></el-button>

              <el-button v-show="indexEditFlag == false" type="danger" icon="el-icon-delete" circle
                @click="dialogDelVisible = true;openDel(scope.row)">
              </el-button>

              <el-button v-show="indexEditFlag == true" type="success" icon="el-icon-caret-top" circle
                @click="top(scope.row,scope.$index)">
              </el-button>
              <el-button v-show="indexEditFlag == true" type="success" icon="el-icon-caret-bottom" circle
                @click="bottom(scope.row,scope.$index)"></el-button>



            </template>
          </el-table-column>

        </el-table>



       <!-- <el-button class="addBtn" type="primary" icon="el-icon-circle-plus-outline" @click="dialogAddVisible = true">
          添加新章节
        </el-button> -->
      </div>



    </div>

    <!-- 添加章节 -->
    <div>
      <el-dialog title="添加章节" :visible.sync="dialogAddVisible">
        <el-form :model="addForm">
          <el-form-item label="章节名称" :label-width="formLabelWidth">
            <el-input v-model="addForm.chaTitle" autocomplete="off" style="width: 30%;"></el-input>
          </el-form-item>
          <el-form-item label="视频地址" :label-width="formLabelWidth">
            <el-input v-model="addForm.chaUrl" style="width: 80%;"></el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogAddVisible = false">取 消</el-button>
          <el-button type="primary" @click="addChapter()">确 定</el-button>
        </div>
      </el-dialog>
    </div>

    <!-- 编辑章节 -->
    <div>
      <el-dialog title="编辑章节" :visible.sync="dialogEditVisible">
        <el-form :model="editForm">

          <el-form-item label="章节名称" :label-width="formLabelWidth">
            <el-input v-model="editForm.chaTitle" style="width: 30%;"></el-input>
          </el-form-item>
          <el-form-item label="视频地址" :label-width="formLabelWidth">
            <el-input v-model="editForm.chaUrl" style="width: 80%;"></el-input>
          </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="dialogEditVisible = false">取 消</el-button>
          <el-button type="primary" @click="editChapter()">确 定</el-button>
        </div>
      </el-dialog>
    </div>



    <!-- 删除章节 -->
    <div>
      <el-dialog title="提示" :visible.sync="dialogDelVisible" width="30%">
        <span>是否删除章节</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogDelVisible = false">取 消</el-button>
          <el-button type="primary" @click="delChapter()">确 定</el-button>
        </span>
      </el-dialog>
    </div>

    <!--    视频播放 -->
    <div>
      <el-dialog title="视频播放" :visible.sync="dialogVideoVisible" width="50%" @close='closeDialog'>
        <video :src="videoSrc" controls="controls" autoplay width="100%" height="100%">
          您的浏览器不支持 video 标签。
        </video>

      </el-dialog>
    </div>

  </div>
</template>

<script>
  export default {
    data() {
      return {
        showTable: [],
        search: '',
        course: {
          couName: '',
          couId: '',
          couPicIO: ''
        },
        videoSrc: '',
        dialogEditVisible: false, //编辑对话框
        dialogDelVisible: false, //删除对话框
        dialogAddVisible: false, //添加对话框
        dialogIndexVisible: false, //顺序对话框
        dialogVideoVisible: false,
        indexEditFlag: false,
        saveEditBtn: true,
        formLabelWidth: '120px', //表单标签宽度
        addForm: { //添加章节表单

          faChaId: "0",
          chaIndex: 0,
          chaTitle: "",
          chaUrl: "",

        },
        delForm: { //删除章节表单

        },
        editForm: { //编辑章节表单
          chaUrl: '',
          chaTitle: '',

        },
        editIndexList: [{ //修改章节顺序列表

        }],
      }
    },

    methods: {


      addChapter() { //确认添加章节url
        console.log(this.showTable.length)

        this.addForm.chaIndex = this.showTable.length + 1
        this.addForm.couId = this.course.couId
        console.log(this.addForm)

        this.$message({
          message: '创建中请稍后',
          type: 'warning'
        });

        this.$http({
          method: 'post',
          url: '/course/teacherChapter/createCourseOfChapterByTea',
          data: this.addForm,
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            });
            this.getAllChapter(this.course.couId)
          }
        })

        this.dialogAddVisible = false
      },

      editChapter() { //确认编辑章节url
        this.editForm.couId = this.course.couId

        this.$http({
          method: 'post',
          url: '/course/teacherChapter/reEditCourseOfChapterInfoByTea',
          data: this.editForm
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            });
            this.getAllChapter(this.course.couId)
          }
        })

        this.dialogEditVisible = false
      },

      delChapter() { //确认删除章节url
        console.log(this.delForm)

        this.$http({
          method: 'post',
          url: '/course/teacherChapter/delCourseChapterByTea',
          data: {
            chaId: this.delForm.chaId,
            couId: this.course.couId
          },
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            });
            this.getAllChapter(this.course.couId)
          }
        })

        this.dialogDelVisible = false
      },

      openEdit(r) { //打开编辑章节
        //console.log(r);
        this.editForm.chaTitle = r.chaTitle;
        this.editForm.chaUrl = r.chaUrl;
        this.editForm.id = r.id;
        this.editForm.chaTime = r.chaTime
        console.log(this.editForm)
      },
      openDel(r) { //打开删除章节
        console.log(r)
        this.delForm = r

      },

      getAllChapter(cou_id) { //得到所有章节
        this.$http({
          method: 'get',
          url: '/course/teacherChapter/getAllCourseOfChapterInfoByTea',
          params: {
            cou_id: cou_id
          }
        }).then((res) => {

          console.log(res.data.data)
          this.showTable = res.data.data;
          //console.log(this.showTable)
          //console.log("刷新之后")
          console.log(this.showTable)
        })
      },
      indexEditSave() { //保存顺序
        this.$http({
          method: 'post',
          url: '/course/teacherChapter/reEditCourseOfChapterIndexOfFaByTea',
          data: this.showTable
        }).then((res) => {
          if (res.data.code == 200) {
            this.$message({
              message: res.data.msg,
              type: 'success'
            });
            this.getAllChapter(this.course.couId)
          }
        })

        this.saveEditBtn = true;
        this.indexEditFlag = false;
      },
      top(r, index) {

        //this.showTable[r.chaIndex-2].chaIndex++;
        //r.chaIndex-=1;
        if (index == 0) {
          this.$message.error('已是第一章，无法上调');

        } else {
          let tempTable = r;
          let i = r.chaIndex
          this.showTable[index].chaIndex = this.showTable[index - 1].chaIndex;
          this.showTable[index - 1].chaIndex = i;
          this.showTable[index] = this.showTable[index - 1];
          this.showTable[index - 1] = tempTable;
          this.showTable.splice(1, 0)

          //console.log(this.showTable[index])
        }

      },
      bottom(r, index) {
        if (index == this.showTable.length - 1) {
          this.$message.error('已是最后一章，无法下调');

        } else {
          let tempTable = r;
          let i = r.chaIndex
          this.showTable[index].chaIndex = this.showTable[index + 1].chaIndex;
          this.showTable[index + 1].chaIndex = i;
          this.showTable[index] = this.showTable[index + 1];
          this.showTable[index + 1] = tempTable;
          this.showTable.splice(1, 0)

          //console.log(this.showTable[index])
        }

      },
      openVideo(url) {
        this.dialogVideoVisible = true
        this.videoSrc = url
      },
      closeDialog(){
        this.videoSrc = ''
      }




    },
    created() {

      if (this.$route.query != null) {
        this.course.couId = this.$route.query.couId
        this.course.couName = this.$route.query.couName
        this.course.couPicIO = this.$route.query.couPicIO
      }
      console.log(this.course)

      this.getAllChapter(this.course.couId)


    }
  };
</script>

<style lang="scss" scoped>
  .course_body {

    width: 85%;
    height: 680px;
    margin: auto;
    margin-top: 10px;
    background-color: white;



    .course_head {
      width: 100%;
      height: 120px;
      background-color:white;
      display: flex;
      flex-direction: row;
      align-items: center;
      //justify-content:  space-between;

      .head {
        padding-left: 10px;
        // min-width: 200px;
        height: 130px;
        border-radius: 1%;
        display: flex;
        align-items: center;

        &:hover {
          .btn {
            display: block;
          }
        }

        img {
          width: 188px;
          height: 105px;
          border-radius: 6%;
        }

        h3 {
          margin-left: 30px;
          white-space: nowrap; //强制不换行
          font-size: 40px;
          font-weight: bold;
          color: black;
          //display: none;
        }

        .input {
          margin-left: 30px;

        }

        .btn {
          margin-left: 10px;
          display: none;
        }


      }



    }
    .course_chapter {
      width: 100%;
      height: 500px;

      //margin-left: 20px;
      .chapter_title {
        margin-left: 20px;
        height: 50px;
        display: flex;
        flex-direction: row;
        align-items: center;
        justify-content: space-between;

        .title_btn {
          margin-right: 3%;

        }
      }

      .chapter_items {
        width: 96%;
        margin: auto;

        //margin-left: 25px;
        .addBtn {
          width: 100%;
          height: 50px;

        }
      }

    }

  }
</style>
