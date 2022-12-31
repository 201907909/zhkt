<template>
  <div class="body">
    <!-- <coursetypeall></coursetypeall> -->
    <div class="top">
      <div class="left-top">
        <div class="addPracticing" @click="dialogVisible=true">
          <div class="practice-img">
            <img src="../../../assets/add.png" alt="" height="100%" width="100%"
              style="border-radius: 8px;opacity: 0.5;">
          </div>
          <div class="practice-img-bgc">
            <img src="../../../assets/add-bgc.png" alt="" height="70%" width="70%"
              style="border-radius: 8px;opacity: 0.3;">
          </div>
        </div>
        <div class="practicing" v-for="(practice,index) in practicing" :id="'practicing'+index">
          <div class="practice-delete el-icon-delete" @click="deletePractice(practice.id,practice.teaId)"></div>
          <div class="practicing-click" @click="showPractice(practicing[index])">
            <div class="practice-img">
              <img :src="practice.praImgIO" alt="" height="100%" width="100%" style="border-radius: 8px;opacity: 0.9;">
            </div>
            <div class="practice-course">
              <div class="course-name-delete">
                <div class="course-name">
                  {{practice.couName}}
                </div>
              </div>
              <div class="practice-name">
                {{practice.praName}}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-top">
        <div class="right-top-tow">
          <div class="input">
            <el-input v-model="input" placeholder="查询正在进行中的实践"></el-input>
          </div>
          <div class="button">
            <el-button type="primary" icon="el-icon-search" @click="queryPracticing(input)"></el-button>
          </div>
        </div>
        <div class="right-top-middle">
          <div class="type-button" :id="type.typeName" v-for="type in practiceType"
            @click="queryPracticeByType(type.praTypeId,type.typeName)">
            {{type.typeName}}
          </div>
        </div>
        <div class="right-top-bottom">
          <div class="info-windows">
            <div class="history_and_name">
              <div class="main-name">
                {{practiceMain.praName}}
              </div>
              <div class="info-windows-history" @click="practiceDetails(practiceMain.id)">
                <div class="history-button el-icon-s-data">&nbsp; 实践详情</div>
              </div>
            </div>
            <el-row class="info-windows-row">
              <el-col :span="24">
                <div class="info-windows-img">
                  <img :src="practiceMain.praImgIO" alt="" height="195px" width="310px"
                    style="border:2px solid rgb(103 114 161 / 86%);border-radius: 6px;box-shadow: rgb(45 52 83) 1px 1px 2px;">
                </div>
                <div class="editPractice" title="可点击编辑实践详情" @click="editDialogVisible=true">
                  <img src="../../../assets/编辑.png" alt="" height="85px" width="90px">
                </div>
              </el-col>
              <el-col :span="24">
                <div class="info-windows-build-one">
                  <el-row>
                    <el-col :span="12">
                      <div class="build-teacher el-icon-user">
                        {{practiceMain.teaName}}
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="build-date el-icon-date">
                        开始&nbsp;&nbsp;{{practiceMain.createTime}}
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <div class="info-windows-build-two">
                  <el-row>
                    <el-col :span="12">
                      <div class="build-teacher el-icon-s-order" v-if="practiceMain.couName.length<=9">
                        {{practiceMain.couName}}
                      </div>
                      <div class="build-teacher el-icon-s-order" v-if="practiceMain.couName.length>9">
                        {{practiceMain.couName.slice(0,9)}}<br />&nbsp;&nbsp;&nbsp;&nbsp;{{practiceMain.couName.slice(9,practiceMain.couName.length)}}
                      </div>
                    </el-col>
                    <el-col :span="12">
                      <div class="build-date el-icon-date">
                        结束&nbsp;&nbsp;{{practiceMain.endTime}}
                      </div>
                    </el-col>
                    <el-col :span="24">
                      <div class="practice-region el-icon-office-building" style="margin: 8px 0;">
                        {{practiceMain.praSite}}
                      </div>
                    </el-col>
                  </el-row>
                </div>
                <div class="info-windows-main">
                  <div class="main-main">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{practiceMain.praMain}}
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>
      </div>
    </div>
    <div class="bottom">
      <div class="left-bottom">
        <div class="history" ref="history">
          <div class="history-head" ref="historyHead">
            <div class="title">
              历史实践
            </div>
          </div>
          <div class="history-body">
            <el-table
              :data="tableData.filter(data => !search || data.name.toLowerCase().includes(search.toLowerCase()))"
              style="width: 100%;font-size: 13px;" class="history-table" height="100%">
              <el-table-column label="结束日期" prop="date" sortable width="102">
              </el-table-column>
              <el-table-column label="实践名称" prop="name" width="186">
              </el-table-column>
              <el-table-column align="right">
                <template slot="header" slot-scope="scope">
                  <el-input v-model="search" size="mini" placeholder="输入名称搜索" />
                </template>
                <template slot-scope="scope">
                  <el-button size="mini" @click="handleCheck(scope.$index, scope.row)">显示详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
      <div class="right-bottom">
        <div class="right-bottom-echarts">
          <div class="bottom-echarts-one" id="nightingaleRose" ref="nightingaleRose"></div>
          <div class="bottom-echarts-two" id="manyXLineChart" ref="manyXLineChart"></div>
        </div>
        <div class="right-bottom-main">
          <div class="bottom-main-text">
            <div class="text-top">
              <div class="name">{{historyPraDetailsSubject.praName}}</div>
              <div class="goodNumber" v-if="historyPraDetailsSubject.goodNumber != 0">
                {{historyPraDetailsSubject.goodNumber}} 人点赞
              </div>
            </div>
            <div class="text-bottom">
              <div class="img">
                <el-tooltip placement="bottom" effect="light" v-show="historyPraDetailsSubject.praMain!=''">
                  <div slot="content">
                    {{historyPraDetailsSubject.praMain.slice(0,historyPraDetailsSubject.praMain.length/3)}}<br />{{historyPraDetailsSubject.praMain.slice(historyPraDetailsSubject.praMain.length/3,historyPraDetailsSubject.praMain.length/3*2)}}<br />{{historyPraDetailsSubject.praMain.slice(historyPraDetailsSubject.praMain.length/3*2,historyPraDetailsSubject.praMain.length)}}
                  </div>
                  <img :src="historyPraDetailsSubject.praImgIO" alt="" height="120px" width="170px"
                    style="border:1px solid rgb(103 114 161 / 86%);border-radius: 6px;box-shadow: rgb(45 52 83) 1px 1px 2px;">
                </el-tooltip>
                <img :src="historyPraDetailsSubject.praImgIO" alt="" height="120px"
                  v-show="historyPraDetailsSubject.praMain==''" width="170px"
                  style="border:1px solid rgb(103 114 161 / 86%);border-radius: 6px;box-shadow: rgb(45 52 83) 1px 1px 2px;">
              </div>
              <div class="text">
                <div class="buildName el-icon-user">&nbsp; {{historyPraDetailsSubject.teaName}}</div>
                <div class="buildTime el-icon-date">&nbsp;开始 {{historyPraDetailsSubject.createTime}}</div>
                <div class="endTime el-icon-date">&nbsp;结束 {{historyPraDetailsSubject.endTime}}</div>
                <div class="typeName el-icon-s-grid">&nbsp; {{historyPraDetailsSubject.praType}}</div>
                <div class="courseName el-icon-s-order">&nbsp; {{historyPraDetailsSubject.couName}}</div>
              </div>
            </div>
          </div>
          <div class="bottom-main-echarts">
            <div class="join">
              <div class="circle-join">
                <div class="content-join">{{historyPraDetailsSubject.joinNumber}}</div>
              </div>
              <div class="content-join-text">参加人数</div>
            </div>
            <div class="complete">
              <div class="circle-complete">
                <div class="content-complete">{{historyPraDetailsSubject.completeNumber}}</div>
              </div>
              <div class="content-complete-text">达标人数</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <el-dialog title="创建实践" :visible.sync="dialogVisible" width="45%" :before-close="handleClose" top="3.5vh"
      @close='closeDialogSimple'>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="实践名称">
          <el-input v-model="form.praName"></el-input>
        </el-form-item>
        <el-form-item label="实践区域">
          <el-input v-model="form.praSite"></el-input>
        </el-form-item>
        <el-form-item label="实践课程">
          <el-select v-model="form.couId" placeholder="请选择实践课程">
            <el-option v-for="course in teaCourse" :key="course.praTypeId" :label="course.couName"
              :value="course.couId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实践类别">
          <el-select v-model="form.praTypeId" placeholder="请选择实践类别">
            <el-option v-for="type in practiceType" :key="type.praTypeId" :label="type.typeName"
              :value="type.praTypeId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实践时间">
          <el-col :span="12">
            <el-date-picker type="date" placeholder="选择开始日期" v-model="form.createTime" style="width: 100%;">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="实践内容">
          <el-input type="textarea" v-model="form.praMain"></el-input>
        </el-form-item>
        <el-form-item label="实践图片">
          <el-upload ref='upload' action list-type="picture-card" :limit="2" :http-request="uploadPicture"
            :file-list="pictureList" :headers='headers' :on-change="handleChangePicture"
            :on-preview="handlePictureCardPreview" :on-remove="handleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="onSubmit">立即创建</el-button>
          <el-button @click="dialogVisible = false;closeDialog()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisibleUrl">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
    <el-dialog title="修改实践详情" :visible.sync="editDialogVisible" width="45%" :before-close="editHandleClose" top="3.5vh"
      @close='editCloseDialogSimple'>
      <el-form ref="edit" :model="edit" label-width="80px">
        <el-form-item label="实践名称">
          <el-input v-model="edit.praName"></el-input>
        </el-form-item>
        <el-form-item label="实践区域">
          <el-input v-model="edit.praSite"></el-input>
        </el-form-item>
        <el-form-item label="实践课程">
          <el-select v-model="edit.couId" placeholder="请选择实践课程" @change="editCouIdChange">
            <el-option v-for="course in teaCourse" :key="course.praTypeId" :label="course.couName"
              :value="course.couId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实践类别">
          <el-select v-model="edit.praTypeId" placeholder="请选择实践类别" @change="editPraTypeIdChange">
            <el-option v-for="type in practiceType" :key="type.praTypeId" :label="type.typeName"
              :value="type.praTypeId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="实践时间">
          <el-col :span="12">
            <el-date-picker type="date" placeholder="选择开始日期" v-model="edit.createTime" style="width: 100%;">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="实践内容">
          <el-input type="textarea" v-model="edit.praMain"></el-input>
        </el-form-item>
        <el-form-item label="实践图片">
          <el-upload ref='upload' action list-type="picture-card" :limit="2" :http-request="editUploadPicture"
            :file-list="editPictureList" :headers='headers' :on-change="editHandleChangePicture"
            :on-preview="handlePictureCardPreview" :on-remove="editHandleRemove">
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="editPractice(edit,practiceMain)">确认修改</el-button>
          <el-button @click="editDialogVisible = false;editCloseDialog()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import courseTypeAll from '../../../components/courseTypeAll.vue';
  export default {
    name: "practiceManagement",
    data() {
      return {
        headers: {
          ContentType: 'multipart/form-data'
        },
        shinyTips: null,
        pictureList: [],
        editPictureList: [],
        form: {
          praName: '',
          praSite: '',
          couId: '',
          praTypeId: '',
          createTime: '',
          praMain: '',
          praImg: ''
        },
        edit: {
          id: null,
          praName: '',
          praSite: '',
          couId: '',
          praTypeId: '',
          createTime: '',
          praMain: '',
          praImg: '',
          oldImg: '',
          teaId: ''
        },
        editImg: '',
        addImg: '',
        teaCourse: [],
        dialogImageUrl: '',
        dialogVisibleUrl: false,
        dialogVisible: false,
        editDialogVisible: false,
        input: "",
        queryPracticingList: [],
        practicing: [{
          praImgIO: "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png",
          praImgName: "default",
          praName: "实践名称",
          praMain: "",
          teaName: "创建者名称",
          teaId: "创建者ID",
          createTime: "创建时间",
          endTime: "结束时间",
          id: null,
          couName: "课程名称",
          praType: "课程类别",
          praSite: '实践地点'
        }],
        practiceMain: {
          praImgIO: "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png",
          praImgName: "default",
          praName: "实践名称",
          praMain: "",
          teaName: "创建者名称",
          teaId: "创建者ID",
          createTime: "创建时间",
          endTime: "结束时间",
          id: null,
          couName: "课程名称",
          praType: "课程类别",
          praSite: '实践地点'
        },
        practiceType: [],
        tableData: [],
        search: null,
        historyPraDetailsSubject: {
          praImgIO: "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png",
          praName: null,
          praMain: '',
          teaName: null,
          createTime: null,
          endTime: null,
          praType: null,
          couName: null,
          goodNumber: null,
          praSite: null,
          joinNumber: 0,
          completeNumber: 0
        },
        historyPraDetailsDefault: {
          praImgIO: "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png",
          praName: "实践名称",
          praMain: "",
          teaName: "创建者名称",
          createTime: "创建时间",
          endTime: "结束时间",
          praType: "实践类别",
          couName: "课程名称",
          goodNumber: 0,
          praSite: '实践地点',
          joinNumber: 0,
          completeNumber: 0
        }
      }
    },
    components: {
      "coursetypeall": courseTypeAll
    },
    mounted() {
      document.querySelector('#practiceManagement').classList.add("router-link-exact-active");
      document.querySelector('#practiceManagement').classList.add("router-link-active");
      if (this.$route.params.returnPracticing != null) {
        this.practicing = this.$route.params.returnPracticing;
      }
      if (this.$route.params.returnId != null) {
        setTimeout(() => {
          this.showPractice(this.practicing[this.$route.params.returnId - 1]);
        }, 1100);
      } else {
        this.getAllPracticeInfoByTea();
      }
    },
    methods: {
      // 上传图标事件，直接删除图片和更换图片的概念不一样！！！
      // 后端与服务器分析：前端选择好图片后，先上传服务器上，若前端更换了，则会将服务器上刚上传的图片删除了，
      // 服务器一直保存最新上传的图片，并且一直保留着最开始的旧图片。但如果不想上传新图片了，前端删除了图片，
      // 则服务器上会同步删除刚刚上传的新图片，旧图片不会受影响。
      // 前端与数据库分析：后端每次上传完新图片都会返回该图片的名称，前端将其保存在form.praImg数据中
      // 如果前端更换了新图片，则后端也会一直将最新的图片名称返回来，所以form.praImg数据中保存的一直是
      // 最新图片的名称。但如果不想上传新图片了，前端删除了图片，则form.praImg中的数据也会被删除，最后
      // 创建实践时，前端传给后端的图片名称为空，后端创建实践接口如果判断新图片名称为空，则服务器上不删除旧图
      // 片且数据库中不修改图片名称，但如果新图片名称不为空，则数据库会修改图片名称并在服务器中删除旧图片。
      async uploadPicture(res) {
        const formData = new FormData()
        formData.append('multipartFile', res.file)
        const result = await this.$API.praManagement.uploadPicture(formData);
        if (result.data.code == 200) {
          this.form.praImg = result.data.data;
          this.addImg = result.data.data;
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'success'
          });
        } else {
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'error'
          });
        }
      },
      // 更新图片操作，上传图标事件
      async editUploadPicture(res) {
        const formData = new FormData()
        formData.append('multipartFile', res.file)
        const result = await this.$API.praManagement.uploadPicture(formData);
        if (result.data.code == 200) {
          this.edit.praImg = result.data.data;
          this.editImg = result.data.data;
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'success'
          });
        } else {
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'error'
          });
        }
      },
      // 覆盖图片，只允许上传一张图片;同时删除未录入数据库保存在服务器的旧图片
      handleChangePicture(file, fileList) {
        if (fileList.length > 1) {
          fileList.splice(0, 1);
          this.handleRemove();
        }
      },
      // 更新图片操作，覆盖图片，只允许上传一张图片;同时删除未录入数据库保存在服务器的旧图片
      editHandleChangePicture(file, fileList) {
        if (fileList.length > 1) {
          fileList.splice(0, 1);
          this.editHandleRemove();
        }
      },
      // 创建实践，需要防止图片未上传成功而进行的创建操作
      async onSubmit() {
        try {
          if (this.form.praImg == '') {
            const loading = this.$loading({
              lock: true,
              text: 'Loading',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.7)'
            });
            if (this.form.praImg == '') {
              setTimeout(() => {
                loading.close();
                this.$message({
                  showClose: true,
                  message: "请完善实践详情或等待图片上传",
                  type: 'success'
                });
              }, 1000);
            } else {
              loading.close();
              this.dialogVisible = false;
              const result = await this.$API.praManagement.addPractice(this.form);
              if (result.data.code == 200) {
                this.$refs.upload.clearFiles();
                this.getAllPracticeInfoByTea();
                this.showPractice(this.practicing[0]);
                this.form.praName = '';
                this.form.praSite = '';
                this.form.couId = '';
                this.form.praTypeId = '';
                this.form.createTime = '';
                this.form.praMain = '';
                this.form.praImg = '';
                this.$message({
                  showClose: true,
                  message: result.data.msg,
                  type: 'success'
                });
              } else {
                this.$message({
                  showClose: true,
                  message: result.data.msg,
                  type: 'error'
                });
              }
            }
          } else if (this.form.praImg != '') {
            this.dialogVisible = false
            const result = await this.$API.praManagement.addPractice(this.form);
            if (result.data.code == 200) {
              this.$refs.upload.clearFiles();
              this.getAllPracticeInfoByTea();
              this.showPractice(this.practicing[0]);
              this.form.praName = '';
              this.form.praSite = '';
              this.form.couId = '';
              this.form.praTypeId = '';
              this.form.createTime = '';
              this.form.praMain = '';
              this.form.praImg = '';
              this.$message({
                showClose: true,
                message: result.data.msg,
                type: 'success'
              });
            } else {
              this.$message({
                showClose: true,
                message: result.data.msg,
                type: 'error'
              });
            }
          }
        } catch (e) {
          this.$message.error('创建实践失败，请完善实践详情');
        }
      },
      // 删除图片
      async handleRemove() {
        if (this.addImg != '') {
          const result = await this.$API.praManagement.deletePicture(this.addImg);
          if (result.data.code == 200) {
            this.dialogImageUrl = '';
            this.form.praImg = '';
            this.addImg = '';
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'error'
            });
          }
        }
      },
      // 更新图片操作，删除图片
      async editHandleRemove() {
        if (this.editImg != '') {
          const result = await this.$API.praManagement.deletePicture(this.editImg);
          if (result.data.code == 200) {
            this.dialogImageUrl = '';
            this.edit.praImg = '';
            this.editImg = '';
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'success'
            });
          } else {
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'error'
            });
          }
        }
      },
      // 查看图片
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisibleUrl = true;
      },
      // 创建实践弹窗关闭
      handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      // 创建实践弹窗关闭取消清空数据
      closeDialog() {
        this.form.praName = '';
        this.form.praSite = '';
        this.form.couId = '';
        this.form.praTypeId = '';
        this.form.createTime = '';
        this.form.praMain = '';
        this.handleRemove();
        this.$refs.upload.clearFiles();
      },
      closeDialogSimple() {
        this.form.praName = '';
        this.form.praSite = '';
        this.form.couId = '';
        this.form.praTypeId = '';
        this.form.createTime = '';
        this.form.praMain = '';
        this.dialogImageUrl = '';
        this.form.praImg = '';
        this.addImg = '';
        this.$refs.upload.clearFiles();
      },
      // 修改实践详情弹窗关闭
      editHandleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      // 修改实践弹窗关闭取消清空数据
      editCloseDialog() {
        this.edit.praName = this.practiceMain.praName;
        this.edit.praSite = this.practiceMain.praSite;
        for (var i = 0; i < this.teaCourse.length; i++) {
          if (this.practiceMain.couName == this.teaCourse[i].couName) {
            this.edit.couId = this.teaCourse[i].couId;
          }
        }
        for (var i = 0; i < this.practiceType.length; i++) {
          if (this.practiceMain.praType == this.practiceType[i].typeName) {
            this.edit.praTypeId = this.practiceType[i].praTypeId;
          }
        }
        this.edit.createTime = this.practiceMain.createTime;
        this.edit.praMain = this.practiceMain.praMain;
        this.editHandleRemove();
        this.$refs.upload.clearFiles();
      },
      editCloseDialogSimple() {
        this.edit.praName = this.practiceMain.praName;
        this.edit.praSite = this.practiceMain.praSite;
        for (var i = 0; i < this.teaCourse.length; i++) {
          if (this.practiceMain.couName == this.teaCourse[i].couName) {
            this.edit.couId = this.teaCourse[i].couId;
          }
        }
        for (var i = 0; i < this.practiceType.length; i++) {
          if (this.practiceMain.praType == this.practiceType[i].typeName) {
            this.edit.praTypeId = this.practiceType[i].praTypeId;
          }
        }
        this.edit.createTime = this.practiceMain.createTime;
        this.edit.praMain = this.practiceMain.praMain;
        this.dialogImageUrl = '';
        this.edit.praImg = '';
        this.editImg = '';
        this.$refs.upload.clearFiles();
      },
      // 删除实践
      async deletePractice(id, teaId) {
        let response = await this.$confirm('此操作将永久删除该实践, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        if(response === 'confirm'){
          const result = await this.$API.praManagement.deletePractice(id, teaId);
          if (result.data.code == 200) {
            this.getAllPracticeInfoByTea();
            this.showPractice(this.practicing[0]);
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'success'
            });
          } else if (result.data.code == 403) {
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'warning'
            });
          } else {
            this.$message({
              showClose: true,
              message: result.data.msg,
              type: 'error'
            });
          }
        }
      },
      // 修改实践详情时获取实践类别ID
      editPraTypeIdChange(e) {
        this.edit.praTypeId = e;
      },
      // 修改实践详情时获取实践所属课程ID
      editCouIdChange(e) {
        this.edit.couId = e;
      },
      // 修改实践详情
      async editPractice(edit, practiceMain) {
        this.editDialogVisible = false;
        edit.teaId = practiceMain.teaId;
        const result = await this.$API.praManagement.editPractice(edit);
        if (result.data.code == 200) {
          this.getAllPracticeInfoByTea();
          this.edit.praImg = '';
          setTimeout(() => {
            this.transitionShowPractice(result.data.data, practiceMain);
          }, 1000);
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'success'
          });
        }
      },
      transitionShowPractice(data, practiceMain) {
        practiceMain.praName = data.praName;
        practiceMain.praSite = data.praSite;
        practiceMain.couName = data.couName;
        practiceMain.praType = data.praType;
        practiceMain.createTime = data.createTime.slice(0, 10);
        practiceMain.endTime = data.endTime.slice(0, 10);
        practiceMain.praMain = data.praMain;
        practiceMain.praImgName = data.praImgName;
        if (data.praImgName == "default") {
          practiceMain.praImgIO = "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png";
        } else {
          practiceMain.praImgIO = data.praImgIO;
        }
        this.showPractice(practiceMain);
      },
      // 查询已过期实践
      async getOverduePractice() {
        const result = await this.$API.praManagement.getOverduePractice();
        for (var i = 0; i < result.data.data.length; i++) {
          this.tableData = result.data.data;
          this.tableData[i].date = result.data.data[i].date.slice(0, 10);
          this.tableData[i].practice.createTime = result.data.data[i].practice.createTime.slice(0, 10);
          this.tableData[i].practice.endTime = result.data.data[i].practice.endTime.slice(0, 10);
          if (this.tableData[i].practice.praImgName == "default") {
            this.tableData[i].practice.praImgIO = "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png";
          }
        }
        this.handleCheck(0, this.tableData[0]);
      },
      // 实践历史，查看历史实践详情
      handleCheck(index, row) {
        // 饼状图：反映了哪些班级对该实践的参与度比较高
        var myChart = echarts.init(this.$refs.nightingaleRose);
        var option;
        option && myChart.setOption(option);
        myChart.setOption({
          legend: {
            orient: 'vertical',
            left: 'left'
          },
          series: [{
            type: 'pie',
            radius: [25, 110],
            center: ['59%', '50%'],
            roseType: 'area',
            itemStyle: {
              borderRadius: 8
            },
            label: {
              normal: {
                position: 'inner',
                show: false
              }
            },
            labelLine: {
              show: false
            },
            label: {
              normal: {
                position: 'inner',
                show: true,
                formatter: '{d}%'
              }
            },
            data: row.classNumber
          }]
        });

        // 折线图：反映了该实践举办过程中每天的活跃度
        var myChart = echarts.init(this.$refs.manyXLineChart);
        var option;
        option && myChart.setOption(option);
        const colors = ['#5470C6', '#EE6666'];
        myChart.setOption({
          color: colors,
          tooltip: {
            trigger: 'none',
            axisPointer: {
              type: 'cross'
            }
          },
          legend: {},
          grid: {
            top: 70,
            bottom: 50
          },
          xAxis: [{
              type: 'category',
              axisTick: {
                alignWithLabel: true
              },
              axisLine: {
                onZero: false,
                lineStyle: {
                  color: colors[0]
                }
              },
              axisPointer: {
                label: {
                  formatter: function(params) {
                    return (
                      '活跃度  ' +
                      params.value +
                      (params.seriesData.length ? '：' + params.seriesData[0].data : '')
                    );
                  }
                }
              },
              // 前7天
              data: row.beforeDate
            },
            {
              type: 'category',
              axisTick: {
                alignWithLabel: true
              },
              axisLine: {
                onZero: false,
                lineStyle: {
                  color: colors[1]
                }
              },
              axisPointer: {
                label: {
                  formatter: function(params) {
                    return (
                      '活跃度  ' +
                      params.value +
                      (params.seriesData.length ? '：' + params.seriesData[0].data : '')
                    );
                  }
                }
              },
              // 后7天
              data: row.afterDate
            }
          ],
          yAxis: [{
            type: 'value'
          }],
          series: [{
              name: '前'+row.beforeDate.length+'天' + row.beforeDate[0] + '~' + row.beforeDate[row.beforeDate.length - 1],
              type: 'line',
              xAxisIndex: 1,
              smooth: true,
              emphasis: {
                focus: 'series'
              },
              data: row.beforeData
            },
            {
              name: '后'+row.afterDate.length+'天' + row.afterDate[0] + '~' + row.afterDate[row.afterDate.length - 1],
              type: 'line',
              smooth: true,
              emphasis: {
                focus: 'series'
              },
              data: row.afterData
            }
          ]
        });
        if (row.practice != null) {
          this.historyPraDetailsSubject = row.practice;
        } else {
          this.historyPraDetailsSubject = this.historyPraDetailsDefault;
        }
        if (row.joinNumber != null) {
          this.joinNumber = row.joinNumber;
          this.completeNumber = row.completeNumber;
        } else {
          this.joinNumber = 0;
          this.completeNumber = 0;
        }
      },
      // 获取所有实践
      async getAllPracticeInfoByTea() {
        const result = await this.$API.praManagement.getAllPracticeInfoByTea();
        if (result.data.data.length != 0) {
          this.practicing = result.data.data;
          for (var i = 0; i < result.data.data.length; i++) {
            this.practicing[i].createTime = result.data.data[i].createTime.slice(0, 10);
            this.practicing[i].endTime = result.data.data[i].endTime.slice(0, 10);
            if (this.practicing[i].praImgName == "default") {
              this.practicing[i].praImgIO = "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png";
            }
          }
          this.showPractice(this.practicing[0]);
        }
      },
      // 获取所有实践类别
      async getAllPracticeType() {
        const result = await this.$API.praManagement.getAllPracticeType();
        if(result.data.code == 200){
          this.practiceType = result.data.data;
        }
      },
      // 获取该教师所有课程
      async getAllCourseByTeaId() {
        const result = await this.$API.praManagement.getAllCourseByTeaId();
        if(result.data.code == 200){
          this.teaCourse = result.data.data;
        }
      },
      // 根据类别获取所有实践
      async queryPracticeByType(praTypeId, typeName) {
        const result = await this.$API.praManagement.getPracticeByInfo(praTypeId);
        if (result.data.data.length != 0) {
          this.queryPracticingList = result.data.data;
          for (var i = 0; i < result.data.data.length; i++) {
            this.queryPracticingList[i].createTime = result.data.data[i].createTime.slice(0, 10);
            this.queryPracticingList[i].endTime = result.data.data[i].endTime.slice(0, 10);
            if (this.queryPracticingList[i].praImgName == "default") {
              this.queryPracticingList[i].praImgIO = "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png";
            }
          }
          for (var i = 0; i < this.practicing.length; i++) {
            for (var j = 0; j < this.queryPracticingList.length; j++) {
              if (this.practicing[i].id == this.queryPracticingList[j].id) {
                var index = this.indexOf(this.practicing[i]);
                if (index > -1) {
                  this.practicing.splice(index, 1);
                }
              }
            }
          }
          for (var i = this.queryPracticingList.length - 1; i >= 0; i--) {
            this.practicing.unshift(this.queryPracticingList[i]);
          }
          this.showPractice(this.queryPracticingList[0]);
          for (var i = 0; i < this.queryPracticingList.length; i++) {
            document.querySelector('#practicing' + i).classList.add("practicingBoxShadow");
            this.timer(i);
          }
          this.input = typeName;
        } else {
          document.querySelector('#' + typeName).classList.add("shinyTips");
          if (this.shinyTips != null) {
            if (this.shinyTips != typeName) {
              document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
            }
          }
          this.shinyTips = typeName
          this.input = typeName;
        }
      },
      // 查询practicing数组下标
      indexOf(val) {
        for (var i = 0; i < this.practicing.length; i++) {
          if (this.practicing[i] == val) {
            return i;
          }
        }
        return -1;
      },
      // 跳转到实践详情界面
      practiceDetails(id) {
        if (id != null) {
          this.$router.push({
            name: "practiceDetails",
            params: {
              id: id,
              practicing: this.practicing
            }
          })
        }
      },
      // 显示实践基本详情
      showPractice(practicing) {
        this.practiceMain = practicing;

        this.edit.id = practicing.id;
        this.edit.praName = practicing.praName;
        for (var i = 0; i < this.teaCourse.length; i++) {
          if (practicing.couName == this.teaCourse[i].couName) {
            this.edit.couId = this.teaCourse[i].couId;
          }
        }
        this.edit.praMain = practicing.praMain;
        this.edit.oldImg = practicing.praImgName;
        this.edit.createTime = practicing.createTime;
        this.edit.praSite = practicing.praSite;
        for (var i = 0; i < this.practiceType.length; i++) {
          if (practicing.praType == this.practiceType[i].typeName) {
            this.edit.praTypeId = this.practiceType[i].praTypeId;
          }
        }
        switch (practicing.praType) {
          case "法律法规":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "康复护理":
            console.log(document.querySelector('#' + practicing.praType))
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "编程语言":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "体育运动":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "财务会计":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "现代医学":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "商业管理":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
          case "生活常识":
            document.querySelector('#' + practicing.praType).classList.add("shinyTips");
            if (this.shinyTips != null) {
              if (this.shinyTips != practicing.praType) {
                document.querySelector('#' + this.shinyTips).classList.remove("shinyTips");
              }
            }
            this.shinyTips = practicing.praType
            break;
        }
      },
      // 输入框查询实践
      async queryPracticing(input) {
        const result = await this.$API.praManagement.getPracticeByInfo(input);
        if (result.data.data.length != 0) {
          this.queryPracticingList = result.data.data;
          for (var i = 0; i < result.data.data.length; i++) {
            this.queryPracticingList[i].createTime = result.data.data[i].createTime.slice(0, 10);
            this.queryPracticingList[i].endTime = result.data.data[i].endTime.slice(0, 10);
            if (this.queryPracticingList[i].praImgName == "default") {
              this.queryPracticingList[i].praImgIO = "http://rmk1q1a73.hn-bkt.clouddn.com/fcb2a840b7db49a98a4e6045c2e11524.png";
            }
          }
          for (var i = 0; i < this.practicing.length; i++) {
            for (var j = 0; j < this.queryPracticingList.length; j++) {
              if (this.practicing[i].id == this.queryPracticingList[j].id) {
                var index = this.indexOf(this.practicing[i]);
                if (index > -1) {
                  this.practicing.splice(index, 1);
                }
              }
            }
          }
          for (var i = this.queryPracticingList.length - 1; i >= 0; i--) {
            this.practicing.unshift(this.queryPracticingList[i]);
          }
          this.showPractice(this.queryPracticingList[0]);
          for (var i = 0; i < this.queryPracticingList.length; i++) {
            document.querySelector('#practicing' + i).classList.add("practicingBoxShadow");
            this.timer(i);
          }
        }
      },
      // 实践框效果定时器
      timer(index) {
        return setTimeout(() => {
          document.querySelector('#practicing' + index).classList.remove("practicingBoxShadow");
        }, 2000)
      }
    },
    destroyed() {
      clearTimeout(this.timer)
      clearTimeout(this.bufferShowPractice)
    },
    created() {
      this.getOverduePractice();
      this.getAllPracticeType();
      this.getAllCourseByTeaId();
    }
  }
</script>

<style lang="scss" scoped>
.body {
  width: 85%;
  height: 1024px;
  margin: auto;

  .top {
    width: 100%;
    height: 56.5%;
    margin-top: 10px;

    .left-top {
      width: 69%;
      height: 98%;
      overflow-y: scroll;
      background-color: #6e81de17;
      border-radius: 6px 0 0 6px;
      margin-bottom: 10px;
      padding-top: 0.5%;
      padding-bottom: 0.5%;
      padding-left: 1%;
      box-shadow: 1px 1px 2px 1px lightgrey;
      float: left;
      position: relative;

      .practicingBoxShadow {
        box-shadow: 0px 4px 10px #375dffde !important;
        position: absolute;
        top: 2px;
      }

      .practicing {
        width: 23%;
        height: 22.7%;
        //width: 200px;
        //height: 130px;
        float: left;
        margin: 6px 12px 7px 4px;
        border-radius: 8px;
        position: relative;
        cursor: pointer;
        box-shadow: 0px 2px 6px #49506ede;
        position: relative;

        .practice-delete {
          z-index: 9999;
          width: 12%;
          height: 16%;
          text-align: center;
          float: right;
          color: #eeeef4;
          font-size: 20px;
          display: none;
          position: absolute;
          top: 5px;
          right: 5px;
        }

        .practice-delete:hover {
          color: #ff0000 !important;
          font-weight: 700;
          text-shadow: 0 0 5px #ff0000;
        }

        .practicing-click {
          width: 100%;
          height: 100%;
          position: relative;

          .practice-img {
            width: 100%;
            height: 100%;
            background-color: #1c124e;
            border-radius: 8px;
          }

          .practice-course {
            width: 100%;
            height: 45%;
            font-family: "Lucida Calligraphy", cursive, serif, sans-serif;
            position: absolute;
            top: 20px;

            .practice-name {
              width: 100%;
              margin-top: 65px;
              height: 44.8px;
              font-size: 16px;
              //padding-left: 5px;
              text-align: left;
              display: flex;
              justify-content: center;
              align-items: center;
              font-weight: 700;
              color: #343434;
              //color: #ffffff;
              border-bottom-left-radius: 8px;
              border-bottom-right-radius: 8px;
              background-color: #ffffffc2;
              font-family: "Microsoft YaHei";
              //background-image: linear-gradient(180deg,rgba(0,0,0,0) 0%,rgba(0,0,0,.8) 100%);


            }

            .course-name-delete {
              width: 100%;
              height: 50%;
              font-size: 14px;
              margin-top: -15px;
              float: left;
              font-weight: 700;
              color: #ffffff;

              .course-name {
                width: 70%;
                text-align: center;
                float: left;
              }
            }
          }
        }
      }

      .addPracticing {
        width: 23%;
        height: 22.7%;

        float: left;
        margin: 6px 12px 7px 4px;
        border-radius: 8px;
        position: relative;
        cursor: pointer;
        position: relative;
        box-shadow: 0px 1px 5px #49506ede;

        .practice-img,
        .practice-img-bgc {
          width: 100%;
          height: 100%;
          background-color: #aeccffb0;
          border-radius: 8px;
        }

        .practice-img{
          opacity: 0.4;
        }

        .practice-img-bgc {
          width: 100% !important;
          height: 100% !important;
          background-color: #aeccff00 !important;
          position: absolute;
          top: 18px;
          left: 28%;
          img {
            width: 95px;
            height: 95px;
          }
        }

        .practice-img-bgc:hover {
          img {
            opacity: 0.5 !important;
          }
        }
      }

      .practicing:hover {
        box-shadow: 0px 8px 14px #656f999c;
        .practicing-click{
          .practice-course{
            .practice-name {
              color: #f1f1f1;
              background-color: rgba(114, 129, 215, 0.91);
            }
          }
        }


        .practice-delete {
          display: block !important;
        }
      }

      .addPracticing:hover {
        box-shadow: 0px 5px 10px #5c76e6de;
      }

      .practicing:active,
      .addPracticing:active {
        box-shadow: 0px 3px 6px #656f999c;
      }
    }

    /*定义滚动条高宽及背景
     高宽分别对应横竖滚动条的尺寸*/
    .left-top::-webkit-scrollbar {
      width: 6px;
      height: 12px;
    }

    /*定义滚动条轨道
     内阴影+圆角*/
    .left-top::-webkit-scrollbar-track {
      -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
      background-color: #F5F5F5;
    }

    /*定义滑块
     内阴影+圆角*/
    .left-top::-webkit-scrollbar-thumb {
      border-radius: 10px;
      -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
      background-color: #bababa;
    }

    .left-top::-webkit-scrollbar-thumb:vertical:hover {
      background-color: #a9a9a9;
    }

    /*定义滚动条两端按钮*/
    .left-top::-webkit-scrollbar-button {
      height: 10px;
      width: 20px;
      background-color: #acc2ff;
    }

    .left-top::-webkit-scrollbar-button:vertical:hover {
      background-color: #86a0e7;
    }

    .right-top {
      width: 30%;
      height: 98%;
      background-color: #6e81de17;
      border-radius: 0 6px 6px 0;
      margin-bottom: 10px;
      padding-top: 0.5%;
      padding-bottom: 0.5%;
      box-shadow: 1px 1px 2px 1px lightgrey;
      float: left;

      .right-top-tow {
        width: 100%;
        height: 8%;

        .input {
          margin: 2px 2px 2px 6px;
          width: 80%;
          float: left;
        }

        .button {
          margin: 2px 2px 2px 1px;
          width: 7%;
          float: left;
        }
      }

      .right-top-middle {
        width: 100%;
        height: 14%;

        .type-button {
          width: 20%;
          height: 40%;
          background-color: #4d58b5e8;
          float: left;
          margin: 3px 8px 3px 9px;
          text-align: center;
          line-height: 32px;
          border-radius: 3px;
          cursor: pointer;
          font-size: 13px;
          color: #fff;
          box-shadow: 0px 4px 6px #656f999c;
        }

        .type-button:active {
          box-shadow: 0px 1px 6px #656f999c;
        }

        .shinyTips {
          font-size: 15px;
          box-shadow: 0px 2px 10px 1px #0010ff !important;
        }
      }

      .right-top-bottom {
        width: 100%;
        height: 79%;

        .info-windows {
          width: 96%;
          height: 96%;
          background-color: #4d58b5ab;
          //background-color: #e6eaf5;
          //background-color: white;
          margin: 4px 0px 0px 7px;
          border-radius: 3px;
          box-shadow: 0px 0px 2px #353e65;

          .history_and_name {
            width: 100%;
            height: 12%;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .main-name {
              color: white;
              //display: flex;
              //justify-content: center;
              //align-items: center;
              width: 70%;
              //height: 90%;
              float: left;
              margin-left: 13px;
              font-size: 18px;
              font-weight: 700;
            }

            .info-windows-history {
              width: 25%;
              height: 62%;
              background-color: #4d58b5;
              color: #fff;
              float: right;
              margin: 0px 8px 0px 0px;
              text-align: center;
              line-height: 30px;
              border-radius: 3px;
              cursor: pointer;
              font-size: 13px;
              font-weight: 700;
              -webkit-box-shadow: 0px 2px 2px #7280bd;
              box-shadow: 0px 1px 5px #444965d4;
            }

            .info-windows-history:active {
              box-shadow: 0px 1px 3px #5a6e84d4;
            }
          }

          .info-windows-row {
            float: left;
            width: 100%;
            height: 84%;
            color: #f7faff;
            position: relative;

            .editPractice {
              height: 200px;
              width: 315px;
              cursor: pointer;
              position: absolute;
              background-color: #00000000;
              top: 0;
              margin: 0 10px;

              img {
                margin: 57px 113px;
                opacity: 0;
              }
            }

            .editPractice:hover {
              background-color: #0000002e;

              img {
                opacity: 0.8;
              }
            }

            .info-windows-img {
              cursor: pointer;
              margin: 0 10px;
            }

            .info-windows-build-one {
              margin: 8px 0 0 18px;
              font-size: 15px;
            }

            .info-windows-build-two {
              margin: 4px 0 0 18px;
              font-size: 15px;
              height: 40px;

              .build-teacher {
                line-height: 18px;
              }
            }

            .info-windows-main {
              width: 95%;
              margin: 5px 0px 0 14px;
              font-size: 12px;
              display: -webkit-box;
              -webkit-box-orient: vertical;
              -webkit-line-clamp: 6;
              overflow: hidden;
            }
          }
        }
      }
    }
  }

  .bottom {
    width: 100%;
    height: 41%;
    background-color: white;

    .left-bottom {
      width: 41%;
      height: 100%;
      background-color: #6e81de17;
      border-radius: 5px 5px 5px 5px;
      box-shadow: 1px 1px 5px 2px lightgrey;
      float: left;

      .history {
        width: 98%;
        margin: 0 auto;
        height: 97.5%;
        border-radius: 5px 5px 5px 5px;
        margin-top: 1%;
        box-shadow: 0 1px 6px 1px #b3b3b3;
        background-color: #485edc38;

        .history-head {
          height: 14%;
          background-color: #485edc6e;
          display: flex;
          justify-content: center;
          border-radius: 5px 5px 0 0;
          border-bottom: 1px solid rgba(0, 0, 0, 0.1);

          .title {
            font-size: 30px;
            float: left;
            color: white;
            margin: 11px 172px;
            font-weight: 700;
            font-family: "Lucida Calligraphy", cursive, serif, sans-serif;
            //font-family: "Microsoft YaHei",cursive, serif, sans-serif;
          }

          .fa-chevron-down {
            font-size: 20px;
            float: right;
            margin: 19px 25px 0 0;
            font-weight: 700;
            cursor: pointer;
          }

          .fa-chevron-down:hover {
            color: #485edc;
          }
        }

        .history-body {
          overflow-y: scroll;

          ::-webkit-scrollbar {
            display: none;
          }

          height: 85.5%;
          width: 100%;
          margin: auto;

          .history-table {
            height: 100%;
          }
        }

        /*定义滚动条高宽及背景
         高宽分别对应横竖滚动条的尺寸*/
        .history-body::-webkit-scrollbar {
          width: 6px;
          height: 12px;
        }

        /*定义滚动条轨道
         内阴影+圆角*/
        .history-body::-webkit-scrollbar-track {
          -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
          border-radius: 10px;
          background-color: #F5F5F5;
        }

        /*定义滑块
         内阴影+圆角*/
        .history-body::-webkit-scrollbar-thumb {
          min-height: 20px;
          min-width: 20px;
          border-radius: 10px;
          -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
          background-color: #bababa;
        }

        .history-body::-webkit-scrollbar-thumb:vertical:hover {
          background-color: #a9a9a9;
        }

        /*最外层透明*/
        /deep/ .el-table,
        /deep/ .el-table__expanded-cell {
          background-color: #e6eaf5;
        }

        /* 表格内背景颜色 */
        /deep/ .el-table th,
        /deep/ .el-table tr,
        /deep/ .el-table td {
          //background-color: transparent;
          background-color: #e6eaf5;
        }
      }
    }

    .right-bottom {
      width: 58%;
      height: 100%;
      background-color: #6e81de17;
      margin: 0 2px 0 0px;
      border-radius: 5px 5px 5px 5px;
      box-shadow: -1px 1px 5px 2px lightgrey;
      float: right;

      .right-bottom-echarts {
        width: 100%;
        height: 58%;

        .bottom-echarts-one {
          width: 42%;
          height: 95%;
          margin: 5px 0px 0px 5px;
          //background-color: #b3bef39e;
          background-color: #e6eaf5;
          border-radius: 5px 5px 5px 5px;
          box-shadow: 1px 1px 5px 1px lightgrey;
          float: left;
        }

        .bottom-echarts-two {
          width: 55%;
          height: 95%;
          margin: 5px 5px 0px 0px;
          //background-color: #b3bef39e;
          background-color: #e6eaf5;
          border-radius: 5px 5px 5px 5px;
          box-shadow: 1px 1px 5px 1px lightgrey;
          float: right;
        }
      }

      .right-bottom-main {
        width: 100%;
        height: 42%;

        .bottom-main-text {
          width: 52%;
          height: 95%;
          margin: 3px 0px 0px 5px;
          //background-color: #b3bef39e;
          background-color: #e6eaf5;
          border-radius: 5px 5px 5px 5px;
          box-shadow: 1px 1px 5px 1px lightgrey;
          float: left;

          .text-top {
            width: 100%;
            height: 18%;
            float: left;

            .name {
              width: 74%;
              height: 100%;
              float: left;
              font-size: 16px;
              font-weight: 700;
              line-height: 40px;
              margin-left: 5px;
            }

            .goodNumber {
              width: 23%;
              height: 100%;
              float: right;
              font-size: 15px;
              font-weight: 700;
              line-height: 42px;
              background-image: linear-gradient(0deg, #5200ff 0%, #fc1000 66%);
              background-clip: text;
              -webkit-background-clip: text;
              color: transparent;
            }
          }

          .text-bottom {
            width: 100%;
            height: 83%;
            float: left;

            .img {
              width: 51%;
              height: 100%;
              float: left;
              cursor: pointer;

              img {
                margin: 8px 4px;
              }
            }

            .text {
              width: 49%;
              height: 100%;
              float: right;
              font-size: 14px;

              .buildName {
                width: 94%;
                height: 20%;
                padding-left: 10px;
                float: right;
                line-height: 30px;
                font-weight: 700;
              }

              .buildTime {
                width: 94%;
                height: 15%;
                margin: 2px 0;
                padding-left: 10px;
                float: right;
                line-height: 23px;
                font-weight: 700;
              }

              .endTime {
                width: 94%;
                height: 15%;
                margin: 2px 0;
                padding-left: 10px;
                float: right;
                line-height: 23px;
                font-weight: 700;
              }

              .typeName {
                width: 94%;
                height: 15%;
                padding-left: 10px;
                margin: 3px 0;
                float: right;
                line-height: 20px;
                font-weight: 700;
              }

              .courseName {
                width: 94%;
                height: 25%;
                padding-left: 10px;
                float: right;
                font-weight: 700;
              }
            }
          }
        }

        .bottom-main-echarts {
          width: 45%;
          height: 95%;
          margin: 3px 0px 0px 10px;
          //background-color: #b3bef39e;
          background-color: #e6eaf5;
          //background-color: white;
          display: flex;
          justify-content: space-around;
          border-radius: 5px 5px 5px 5px;
          box-shadow: 1px 1px 5px 1px lightgrey;
          float: left;

          .join {
            width: 47%;
            height: 100%;
            //margin: 4px 2px 2px 8px;
            float: left;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            .circle-join {
              width: 135px;
              height: 135px;
              border-radius: 50%;
              background: linear-gradient(0deg, #36A5ED 0%, #6EEE96 100%);

              .content-join {
                position: relative;
                top: 10px;
                left: 10px;
                width: 115px;
                height: 115px;
                border-radius: 50%;
                background-color: #fff;
                text-align: center;
                line-height: 115px;
                font-size: 35px;
              }
            }

            .content-join-text {
              color: #3952e2;
              font-size: 20px;
              font-weight: 700;
              text-align: center;
              //margin-right: 5px;
            }
          }

          .complete {
            width: 47%;
            height: 100%;
            //margin: 4px 0px 2px 4px;
            float: right;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;

            .circle-complete {
              width: 135px;
              height: 135px;
              border-radius: 50%;
              //background: linear-gradient(0deg, #f0e110 0%, #ee4827 100%);
              background: linear-gradient(0deg, #36A5ED 0%, #6EEE96 100%);

              .content-complete {
                position: relative;
                top: 10px;
                left: 10px;
                width: 115px;
                height: 115px;
                border-radius: 50%;
                background-color: #fff;
                text-align: center;
                line-height: 115px;
                font-size: 35px;
              }
            }

            .content-complete-text {
              //color: red;
              color: #3952e2;
              font-size: 20px;
              font-weight: 700;
              text-align: center;
              margin-right: 5px;
            }
          }
        }
      }
    }
  }
}
</style>
