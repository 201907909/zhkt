<template>
  <div class="body">
    <el-table :data="tableData" border style="width: 100%" height="640" @selection-change="handleSelectionChange">
      <el-table-column type="selection" min-width="55">
      </el-table-column>
      <el-table-column fixed prop="createTime" label="发布日期" min-width="150">
      </el-table-column>
      <el-table-column prop="userName" label="发布者姓名" min-width="150">
      </el-table-column>
      <el-table-column prop="role" label="发布者身份" min-width="150" :filters="[{ text: '学生', value: 1 }, { text: '老师', value: 0 }]"
                       :filter-method="filterRole">
        <template slot-scope="scope">
          <p v-if="scope.row.role == 1" style="color:#67C23A;">学生</p>
          <p v-else style="color:#409EFF;">老师</p>
        </template>
      </el-table-column>
      <el-table-column prop="auditImg" label="审核内容" min-width="210">
        <template slot-scope="scope">
          <el-image style="width: 200px; height: 100px" :src="scope.row.auditImg" @click="openImg(scope.row)">
          </el-image>
        </template>
      </el-table-column>
      <el-table-column prop="auditResult" label="最终审核结果" min-width="180" :filters="[{ text: '不通过', value: 0 }, { text: '通过', value: 2 }]"
                       :filter-method="filterResult">
        <template slot-scope="scope">
          <p v-if="scope.row.auditResult == 0" style="color:#F56C6C;">不通过</p>
          <p v-if="scope.row.auditResult == 1" style="color:#E6A23C;">复审</p>
          <p v-if="scope.row.auditResult == 2" style="color:#67C23A;">通过</p>
        </template>
      </el-table-column>

      <el-table-column prop="auditSource" label="审核源" min-width="180">
        <template slot-scope="scope">
          <p v-if="scope.row.auditSource == 0" >资讯</p>
          <p v-if="scope.row.auditSource == 1" >问题</p>

        </template>
      </el-table-column>

      <el-table-column label="操作" min-width="200">
        <template slot-scope="scope">
          <el-button @click="pass(scope.row)" type="success" size="small">通过</el-button>
          <el-button @click="overruled(scope.row)" type="danger" size="small">不通过</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="page">
      <div class="button">
        <el-button @click="passAll" type="success" size="small">批量通过</el-button>

      </div>
      <el-pagination style="margin-right: 20px;" background layout="total,prev, pager, next,jumper" :page-size='7' :pager-count="7" :total="total"
        @current-change="getHistoryImg">
      </el-pagination>
    </div>
    <div>
      <el-dialog :visible.sync="dialogVisible" width="60%">
        <img width="100%" :src="dialogImageUrl" alt="">
      </el-dialog>
    </div>
  </div>
</template>

<script>
  export default {
    name: "imgHistory",
    data() {
      return {
        tableData: [],
        form:{
          id: [],
          auditId: [],
          auditType: 1,
        },
        list:[],
        total:0,
        dialogVisible:false,
        dialogImageUrl:'#',
        urlList:[],
      }
    },
    methods: {
      getHistoryImg(page) {
        console.log(page)
        this.$http({
          method: 'get',
          url: '/user/admin/historyImg',
          params: {
            current: page
          }
        }).then((res) => {
          console.log(res.data)
          this.total = res.data.data.total
          this.tableData = res.data.data.data

        })
      },

      pass(r) {
        this.form.id.push(r.id)
        this.form.auditId.push(r.auditId)
        console.log(this.form)
        this.$http({
          method:'post',
          url:'/user/admin/auditPass',
          data:this.form
        }).then((res)=>{
          console.log(res.data)
          if(res.data.code == 200){
            this.$message({
              message: res.data.msg,
              type: 'success'
            });

          }
          this.form.id = []
          this.form.auditId = []
          this.getHistoryImg(1)
        })


      },
      overruled(r) {

        this.$http({
          method:'get',
          url:'/user/admin/overruled',
          params:{
            id:r.id,
            auditId:r.auditId,
            auditType:1
          }
        }).then((res)=>{
          console.log(res.data)
          if(res.data.code == 200){
            this.$message({
              message: res.data.msg,
              type: 'success'
            });

          }

          this.getHistoryImg(1)
        })

      },
      handleSelectionChange(val) {
        console.log(val)
        this.list = val

      },
      filterRole(value, row, column) { //筛选器
        console.log(row);
        return row.role === value;
      },
      filterResult(value, row, column) { //筛选器
        console.log(row);
        return row.auditResult === value;
      },
      passAll() {
        console.log(this.list)
        if(this.list.length == 0){
          this.$message.error("请选择")
        }else{
          for(var i = 0;i<this.list.length;i++){
            this.form.id.push(this.list[i].id)
            this.form.auditId.push(this.list[i].auditId)

          }
          console.log(this.form)
          this.$http({
            method: 'post',
            url: '/user/admin/auditPass',
            data:this.form
          }).then((res)=>{
            console.log(res.data)
            if(res.data.code == 200){
              this.$message({
                message: res.data.msg,
                type: 'success'
              });

            }
            this.form.id = []
            this.form.auditId = []
            this.getHistoryImg(1)
          })

        }
      },
      openImg(r){
        this.dialogImageUrl = r.auditImg
        this.dialogVisible = true
      }
    },
    created() {
      this.getHistoryImg(1)
    }
  }
</script>

<style lang="scss" scoped>
  .body {
    width: 100%;
    margin: auto;
    height: 690px;
    background-color: white;

    .page {
      height: 50px;
      display: flex;
      align-items: center;
      justify-content: space-between;
      .button{
        margin-left: 20px;
      }
    }


  }
</style>
