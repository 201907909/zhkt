<template>
  <div class="body">
    <div class="center">
      <el-table :data="tableData" style="width: 100%" :default-sort="{prop: 'createTime', order: 'descending'}">



        <el-table-column prop="createTime" label="发布日期" sortable min-width="80">
        </el-table-column>

        <el-table-column prop="userName" label="学生姓名" min-width="80">
        </el-table-column>

        <el-table-column prop="title" label="问题标题" min-width='200'>
        </el-table-column>

        <el-table-column prop="maxName" label="课程类别" min-width='180'>
          <template slot-scope="scope">
            <p>{{scope.row.maxName}}/{{scope.row.minName}}</p>
          </template>
        </el-table-column>



        <el-table-column prop="isMyStu" label="类型" min-width='80'
          :filters="[{ text: '其他学生', value: 0 }, { text: '我的学生', value: 1 }]" :filter-method="filterType">
          <template slot-scope="scope">
            <p v-if="scope.row.isMyStu == 0" style="color:#E6A23C;">其他学生</p>
            <p v-if="scope.row.isMyStu == 1" style="color:#67C23A;">我的学生</p>
          </template>
        </el-table-column>

        <el-table-column label="操作" min-width="80">
          <template slot-scope="scope">
            <el-button @click="openQuestion(scope.row)" type="text">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="page">
        <el-pagination background layout="prev, pager, next,total,jumper" :page-size='7' :pager-count="7" :total="total" @current-change="getAllQuestion">
        </el-pagination>
      </div>
    </div>




  </div>
</template>

<script>
  export default {
    name: 'myMessage',
    data() {
      return {
        tableData:[],
        formLabelWidth: '50px',
        dialogVisible: false,
        total:0,
        pager:1,
      }
    },
    methods: {
      filterType(value, row, column) { //筛选器
        console.log(row);
        return row.isMyStu === value;
      },

      openQuestion(r) {
        console.log(r)
        this.$router.push({
          path: "/home/messageDetails",
          query: {
            queId: r.queId
          }
        })
      },
      getAllQuestion(page) {
        console.log(page)
        this.$http({
          method: 'get',
          url: '/interaction/wenda/teaGetAllWenDa',
         params: {
           current: page
         }
        }).then((res) => {
          console.log(res.data.data)
          this.total = res.data.data.total
          this.tableData = res.data.data.data
        })
      },
    },
    created() {
      this.getAllQuestion(1)
    }
  }
</script>

<style lang="scss" scoped>
  .body {
    width: 85%;
    height: 680px;
    margin: auto;
    margin-top: 10px;
    background-color: white;
    border-radius: 1%;

    .center {
      width: 96%;
      margin: auto;

      .page {
        margin: auto;
        margin-top: 30px;

      }


    }

  }
</style>
