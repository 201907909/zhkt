<template>
  <div class="infor">
    <div v-show="!isDetail">
      <!-- 资讯功能 -->
      <div class="infor_fun" v-show="!isShowTable">
        <div class="infor_search">
          <input
            type="text"
            class="search_input"
            placeholder=" 搜索标题、内容"
            v-model="searchInput"
            maxlength="10"
          /><i class="el-icon-search search_posi" @click="getInforList()"></i>
        </div>
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="small"
          @click="isShowT()"
          >创建咨询</el-button
        >
        <el-button
          type="danger"
          icon="el-icon-delete"
          size="small"
          @click="delAll()"
          >多选删除</el-button
        >
      </div>
      <!-- 咨询列表 -->
      <div class="info_table" v-show="!isShowTable">
        <el-table
          style="width: 100%"
          border
          :data="inforList"
          ref="multipleTable"
          @selection-change="handleSelectionChange"
        >
          >
          <el-table-column
            type="selection"
            width="60"
            align="center"
            label="全选"
          >
          </el-table-column>
          <el-table-column type="index" label="序号" width="80" align="center">
          </el-table-column>
          <el-table-column
            prop="infoAuthor"
            label="作者"
            width="140"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="infoTitle" label="标题" align="center">
          </el-table-column>
          <el-table-column
            prop="createTime"
            label="时间"
            sortable
            width="160"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="infoLike"
            label="点赞数"
            sortable
            width="90"
            align="center"
          >
          </el-table-column>
          <el-table-column
            prop="count"
            label="评论数"
            sortable
            width="90"
            align="center"
          >
          </el-table-column>
          <el-table-column prop="prop" label="操作" width="300" align="center">
            <template slot-scope="{ row }">
              <el-button
                type="warning"
                icon="el-icon-edit"
                size="mini"
                @click="editInfor(row)"
                >编辑</el-button
              >
              <el-button
                type="primary"
                icon="el-icon-view"
                size="mini"
                @click="seeInfor(row)"
                >查看</el-button
              >
              <el-button
                type="danger"
                icon="el-icon-delete"
                size="mini"
                @click="deleteInfor(row)"
                >删除</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          style="margin-top: 10px; margin-left: -8px"
          background
          :current-page="page"
          :page-size="7"
          :pager-count="7"
          layout="prev, pager, next,total,jumper"
          :total="total"
          @current-change="getInforList"
        >
        </el-pagination>
      </div>
      <!-- 创建资讯表单 -->
      <div v-show="isShowTable" class="create_table">
        <el-button
          type="primary"
          style="margin-left: -30px; margin-top: -0px; font-size: 16px"
        >
          {{ this.updataId == "" ? "添加咨询" : "修改咨询" }}
        </el-button>
        <el-button
          size="small"
          style="position: absolute; right: 62px"
          @click="cancel()"
          >返回</el-button
        >
        <el-form
          label-width="100px"
          :model="ruleForm"
          ref="ruleForm"
          :rules="rules"
        >
          <el-form-item label="作者" prop="infoAuthor">
            <el-input
              style="width: 350px"
              placeholder="请输入作者"
              maxlength="10"
              show-word-limit
              v-model="ruleForm.infoAuthor"
            >
            </el-input>
          </el-form-item>

          <el-form-item label="标题" prop="infoTitle">
            <el-input
              style="width: 350px"
              placeholder="请输入标题"
              maxlength="30"
              show-word-limit
              v-model="ruleForm.infoTitle"
            ></el-input>
          </el-form-item>
          <!-- 富文本编辑器 -->
          <div class="rich_text" style="border: 1px solid #ccc; width: 88%">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editor"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 500px; overflow-y: hidden"
              v-model="html"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="onCreated"
            />
          </div>
          <!-- 按钮 -->
          <el-form-item style="margin-top: 20px; margin-left: 67%">
            <el-button type="primary" @click="addInfor('ruleForm')"
              >保存</el-button
            >
            <el-button @click="cancel">取消</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
    <!-- 查看资讯详情 -->
    <div v-show="isDetail" class="si_detail">
      <div class="seeinfor">
        <el-button size="small" @click="backTable()" style="margin-left: 20px"
          >返回</el-button
        >
        <div class="seeinfor_box">
          <!-- 回到顶部 -->
          <el-backtop
            target=".seeinfor .seeinfor_box"
            :right="60"
            :bottom="50"
          ></el-backtop>
          <!-- 文章 -->
          <div class="infor_content">
            <h2>{{ detailConent.infoTitle }}</h2>
            <div class="infor_mes">
              作者：{{ detailConent.infoAuthor }}
              <span>点赞数：{{ detailConent.infoLike }}</span>
              <span>评论数：{{ detailConent.count }}</span>
            </div>
            <div class="infor_main" v-html="detailConent.infoMain"></div>
          </div>
          <!-- 评论 -->
          <div class="comments">
            <!-- 评论列表 -->
            <div
              class="comments_lists"
              v-for="(c, index) in this.detailConent.commentVOs"
              :key="index"
            >
              <div class="individual">
                <img class="avatar" :src="c.image" />
                <div class="nickname">{{ c.userName }}</div>
              </div>
              <div class="content_box">
                <div class="full">
                  {{ c.comContent }}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="seelist">
        <div
          v-for="(i, index) in inforList"
          :key="index"
          class="list_box"
          :class="i.infoId == infoId ? 'list_box1' : ''"
          @click="seeOne(i.infoId)"
        >
          <div class="seelist_title">{{ i.infoTitle }}</div>
          <div class="seelist_tag">
            点赞数：{{ i.infoLike }} 评论数：{{ i.count }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Vue from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { getToken } from '../../../utils/auth';
export default Vue.extend({
  components: {
    Editor,
    Toolbar,
  },
  data() {
    return {
      teaId: "",
      //代表的分页器第几页
      page: 1,
      //总共数据条数
      total: 0,
      // 列表
      inforList: [1],
      // 搜索框
      searchInput: "",
      isShowTable: false,
      // 多选框
      multipleSelection: [],
      multiinfo: [],
      // 咨询作者，标题
      ruleForm: {
        infoAuthor: "作者",
        infoTitle: "",
      },
      // 详情咨询id
      infoId: "",
      // 修改咨询id
      updataId: "",
      // seelist:[],
      rules: {
        infoAuthor: [
          {
            required: true,
            message: "请输入作者",
            trigger: "blur",
          },
        ],
        infoTitle: [
          {
            required: true,
            message: "请输入标题",
            trigger: "blur",
          },
        ],
      },
      //富文本编辑器
      infoImage: "",
      editor: null,
      mode: "simple",
      html: "<p>hello</p>",
      toolbarConfig: {},
      editorConfig: {
        placeholder: "请输入内容...",
        MENU_CONF: {
          // 图片上传
          uploadImage: {
            server:
              "http://localhost:8080/api/interaction/information/inforFile",
            fieldName: "file",
            headers: { Authorization: getToken(), ContentType: 'multipart/form-data' },
            // 自定义插入图片
            customInsert(res, insertFn) {
              if (res.code == 200) {
                insertFn(res.data, "无", res.data);
              }
              this.infoImage = res.data;
            },
          },
        },
      },
      // 咨询详情
      isDetail: false,
      detailConent: {
        infoAuthor: "",
        infoTitle: "",
        infoMain: "",
        infoLike: "",
        count: "",
        commentVOs: [],
      },

      // 评价 索引唯一
      fold: true,
      height: [],
      // 点赞
      zan: true,
      val: false,
    };
  },
  methods: {
     // 创建咨询
    isShowT(){
      this.ruleForm.infoAuthor="";
      this.ruleForm.infoTitle="";
      this.html="";
      this.updataId="";
      this.isShowTable = true;
    },
    // 收集多选
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 多选删除咨询
    async delAll() {
      for (var i = 0; i < this.multipleSelection.length; i++) {
        this.multiinfo[i] = this.multipleSelection[i].infoId;
      }
      if (this.multiinfo.length != 0) {
        const result = await this.$API.informations.deleteInfor(this.multiinfo);
        if (result.status == 200) {
          //弹出信息
          this.$message({
            type: "success",
            message: result.data.msg,
          });
        }
      } else {
        this.$message({
          type: "warning",
          message: "不能为空！",
        });
      }
      this.getInforList();
      this.multiinfo = [];
    },
    // 单选删除咨询
    async deleteInfor(row) {
      this.infoId = {
        ...row,
      };
      this.multiinfo[0] = this.infoId.infoId;
      const result = await this.$API.informations.deleteInfor(this.multiinfo);
      console.log(result);
      if (result.status == 200) {
        //弹出信息
        this.$message({
          type: "success",
          message: result.data.msg,
        });
      }
      this.getInforList();
      this.multiinfo = [];
    },
    // 查看咨询详情
    async seeInfor(row) {
      this.detailConent.infoAuthor = "";
      this.detailConent.infoTitle = "";
      this.detailConent.infoMain = "";
      this.detailConent.infoLike = "";
      this.detailConent.count = "";
      this.detailConent.commentVOs = [];
      this.isDetail = true;
      this.infoId = {
        ...row,
      };
      const result = await this.$API.informations.getInforDetail(
        this.page,
        this.infoId.infoId
      );
      const list = await this.$API.informations.getComment(this.infoId.infoId);
      this.detailConent = {
        ...result.data.data,
      };
      this.detailConent.commentVOs = list.data.data.commentVOs;
      this.infoId = this.infoId.infoId;
    },
    async seeOne(infoId) {
      const result = await this.$API.informations.getInforDetail(
        this.page,
        infoId
      );
      this.detailConent = {
        ...result.data.data,
      };
      this.infoId = infoId;
    },
    // 编辑咨询
    editInfor(row) {
      this.ruleForm.infoAuthor = row.infoAuthor;
      this.ruleForm.infoTitle = row.infoTitle;
      this.isShowTable = true;
      this.html = row.infoMain;
      this.updataId = row.infoId;
    },
    // 搜索资讯
    async searchInfor() {
      await this.$API.informations.searchInfor(inforTable);
    },
    // 增加咨询
    async addInfor(formName) {
      const inforTable = {
        infoId: this.updataId,
        infoAuthor: this.ruleForm.infoAuthor,
        infoTitle: this.ruleForm.infoTitle,
        infoMain: this.html,
        infoImage: this.infoImage,
        teaId: this.teaId,
      };
      this.$refs[formName].validate((valid) => {
        this.val = valid;
      });
      if (this.val) {
        if (this.updataId == "") {
          const result = await this.$API.informations.addInforList(inforTable);
          console.log(result.data.msg);
          if (result.data.msg == "发布成功") {
            //弹出信息
            this.$message({
              type: "success",
              message: result.data.msg,
            });
            // 清空增加表单
            this.updataId = "";
            this.ruleForm.infoAuthor = "";
            this.ruleForm.infoTitle = "";
            this.html = "";
          } else {
            this.$message({
              type: "warning",
              message: result.data.msg,
            });
          }
          this.getInforList(this.updataId == "" ? 1 : this.page);
        } else {
          const result = await this.$API.informations.updateList(inforTable);
          if (result.status == 200) {
            //弹出信息
            this.$message({
              type: "success",
              message: "修改成功！",
            });
          } else {
            this.$message({
              type: "error",
              message: "修改失败！",
            });
          }

          this.getInforList(this.updataId != "" ? this.page : 1);
          // 清除id
          this.updataId = "";
        }
        this.isShowTable = false;
      } else {
        this.$message({
          type: "warning",
          message: "请输入相关信息！",
        });
        return false;
      }
    },
    // 获取资讯
    async getInforList(pager = 1) {
      this.page = pager;
      if (this.searchInput == "") {
        const result = await this.$API.informations.getInforList(this.page);
        this.inforList = result.data.data.data;
        this.total = result.data.data.total;
      } else {
        const result = await this.$API.informations.searchInfor(
          this.page,
          this.searchInput
        );
        this.inforList = result.data.data.data;
        this.total = result.data.data.total;
      }
    },
    // 取消增加
    cancel() {
      this.isShowTable = false;
    },
    //富文本编辑器 一定要用 Object.seal() ，否则会报错
    onCreated(editor) {
      this.editor = Object.seal(editor);
    },
    onChange(editor) {
      this.inforDetail = editor.getHtml();
    },
    backTable() {
      this.isDetail = false;
    },
  },

  mounted() {
    this.teaId = sessionStorage.getItem("userId"); //写入登录状态
    this.getInforList();
  },

  watch: {},
  beforeDestroy() {
    const editor = this.editor;
    if (editor == null) return;
    editor.destroy(); // 组件销毁时，及时销毁编辑器
  },
});
</script>

<style src="@wangeditor/editor/dist/css/style.css"></style>
<style lang="scss" scoped>
.infor {
  width: 85%;
  height: 680px;
  margin: auto;
  margin-top: 10px;
  background-color: rgb(255, 255, 255);
  border-radius: 10px;
  padding-bottom: 30px;

  // 资讯功能
  .infor_fun {
    display: flex;
    padding: 30px 30px 20px 6px;

    .infor_search {
      margin-left: 22px;
      margin-right: -6px;

      .search_input {
        width: 200px;
        height: 34px;
        font-size: 14px;
        color: #131b26;
        border: 1px solid #d4d6d9;
        padding: 0 14px;
        outline: none;
        border-radius: 6px;
      }

      .search_input:focus {
        border: 1px solid #3a8bff;
      }

      .search_posi {
        position: relative;
        left: -36px;
        color: #e1e1e5;
        font-size: 19px;
        top: 3px;
        cursor: pointer;
      }

      .search_posi:hover {
        color: #3a8bff;
      }
    }
  }

  // 资讯表单
  .info_table {
    padding: 26px;
    padding-top: 0px;
  }

  // 创建资讯表单
  .create_table {
    width: 100%;
    height: 570px;
    padding-left: 50px;
    margin-top: 22px;
    overflow-y: auto;
  }

  // 查看资讯详情
  .si_detail {
    display: flex;
    justify-content: space-between;

    .seeinfor {
      // width: 100%;
      width: 77%;
      height: 570px;
      margin-top: 20px;
      // background-color: #3a8bff;

      .seeinfor_box {
        height: 520px;
        margin-top: 10px;
        overflow: auto;
        padding: 0px 80px 20px 80px;

        // 文章
        .infor_content {
          padding-bottom: 20px;
          text-align: center;

          .infor_mes {
            color: #646464;

            span {
              margin-left: 20px;
            }
          }

          .infor_main {
            word-break: break-all;
            text-align: left;
          }
        }

        // 评论
        .comments {
          padding: 20px;
          border-top: 2px solid #dfdfdf;

          // 评论列表
          .comments_lists {
            margin-top: 10px;
            padding-bottom: 4px;
            border-bottom: 1px solid #e9e9e9;
            .individual {
              display: flex;
              align-items: center;

              .avatar {
                border: 1px solid rgb(126, 126, 126);
                width: 36px;
                height: 36px;
                border-radius: 18px;
              }

              .nickname {
                font-size: 14px;
                margin-left: 10px;
              }
            }

            .content_box {
              .full {
                margin-left: 48px;
                color: #000;
              }

              .show1 {
                margin-left: 58px;
                color: #5f77fa;
                cursor: pointer;
              }

              .show2 {
                display: none;
                margin-left: 58px;
                color: #5f77fa;
                cursor: pointer;
              }
            }
          }
        }
      }

      // 滚动条整体样式
      .seeinfor_box::-webkit-scrollbar {
        /*高宽分别对应横竖滚动条的尺寸*/
        width: 6px;
        height: 1px;
      }

      .seeinfor_box::-webkit-scrollbar-thumb {
        /*滚动条里面小方块*/
        border-radius: 10px;
        -webkit-box-shadow: inset 0 0 5px rgba(0, 94, 200, 0.2);
        background: #dadada;
      }

      .seeinfor_box::-webkit-scrollbar-track {
        /*滚动条里面轨道*/
        -webkit-box-shadow: inset 0 0 5px rgba(107, 196, 255, 0.2);
        border-radius: 10px;
        background: #f1f1f1;
      }
    }

    .seelist {
      width: 19%;
      margin-top: 70px;
      margin-right: 30px;

      .list_box {
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: 60px;
        padding: 0px 20px 0px 20px;
        border-radius: 6px;
        background-color: #f8f8f8;
        margin-top: 6px;
        cursor: pointer;

        .seelist_title {
          color: rgb(0, 0, 0);
          overflow: hidden;
          //  word-break: break-all;
          /* break-all(允许在单词内换行。) */
          text-overflow: ellipsis;
          /* 超出部分省略号 */
          display: -webkit-box;
          /** 对象作为伸缩盒子模型显示 **/
          -webkit-box-orient: vertical;
          /** 设置或检索伸缩盒对象的子元素的排列方式 **/
          -webkit-line-clamp: 1;
        }

        .seelist_tag {
          color: #378eff;
          margin-top: 4px;
          font-size: 12px;
        }
      }

      .list_box1 {
        border-bottom: 2px solid #2142ff;
      }
    }
  }
}
</style>
