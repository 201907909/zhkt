<template>
  <div class="body">
    <div class="left">
      <div class="top-info">
        <div class="history_and_name">
          <div class="main-name">
            {{practiceMain.praName}}
          </div>
          <div class="info-windows-history" @click="returnManagement()">
            <div class="history-button el-icon-data-line">&nbsp; 查看更多实践</div>
          </div>
        </div>
        <el-row class="info-windows-row">
          <el-col :span="24">
            <div class="info-windows-img">
              <img :src="practiceMain.praImgIO" alt="" height="195px" width="320px"
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
              <div class="practice-type">{{practiceMain.praType}}</div>
              <div class="main-main">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {{practiceMain.praMain}}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="bottom-data-two">
        <div class="join">
          <div class="circle-join">
            <div class="content-join">{{practiceMain.joinNumber}}</div>
          </div>
          <div class="content-join-text">参加人数</div>
        </div>
        <div class="good">
          <div class="circle-good">
            <div class="content-good">{{practiceMain.goodNumber}}</div>
          </div>
          <div class="content-good-text">点赞数</div>
        </div>
        <div class="complete">
          <div class="circle-complete">
            <div class="content-complete">{{practiceMain.completeNumber}}</div>
          </div>
          <div class="content-complete-text">提交人数</div>
        </div>
      </div>
    </div>
    <div class="right">
      <div class="top-echarts">
        <div class="top-echarts-main" id="dynamicBarChart" ref="dynamicBarChart"></div>
      </div>
      <div class="bottom-echarts-two">
        <div class="echarts-left" title="可点击查看指定班级的参与情况，点击中心位置即可返回">
          <div class="echarts-left-main" id="risingSunchart" ref="risingSunchart"></div>
        </div>
        <div class="echarts-right">
          <div class="echarts-right-main" id="guangDongMap"></div>
          <div class="regionTitle">实践地址</div>
          <div class="query-route el-icon-search" @click="baiduMapLink">&nbsp;查询前往路线</div>
        </div>
      </div>
    </div>
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
            <el-option v-for="type in practiceType" :key="type.praTypeId" :label="type.typeName" :value="type.praTypeId">
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
          <el-button type="primary" @click="editPractice(edit)">确认修改</el-button>
          <el-button @click="editDialogVisible = false;editCloseDialog()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <el-dialog :visible.sync="dialogVisibleUrl">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import baiduMap from "./baiduMap.json";
  export default {
    name: "practiceDetails",
    data() {
      return {
        headers: {
          ContentType: 'multipart/form-data'
        },
        indexro: 'https://map.baidu.com/@12586657,2638353,13z',
        map: '',
        mapZoom: 15,
        id: null,
        practicing: null,
        editDialogVisible: false,
        dialogImageUrl: '',
        dialogVisibleUrl: false,
        editImg: '',
        editPictureList: [],
        teaCourse: [],
        practiceType: [],
        practiceMain: {
          praImgIO: require("../../../assets/R-C.jpg"),
          praImgName: "实践宣传图R-C.jpg",
          praName: "实践名称",
          praMain: "实践内容",
          couName: "课程名称",
          teaName: "创建者",
          teaId: "创建者ID",
          createTime: "开始时间",
          endTime: "结束时间",
          id: this.id,
          praType: "实践类型",
          praSite: '实践地址',
          goodNumber: 0,
          joinNumber: 0,
          completeNumber: 0
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
        echartsData: {
          precticeName: '法律法规民法实践',
          latitude: 113.04135500395992,
          longitude: 23.164559680980247,
          year: '2022',
          date: ['11-01', '11-02', '11-03', '11-04', '11-05', '11-06', '11-07', '11-08', '11-09', '11-10', '11-11',
            '11-12', '11-13', '11-14'
          ],
          dataJoin: [60, 170, 160, 290, 165, 200, 225, 135, 140, 250, 75, 110, 190, 210],
          dataEnd: [160, 175, 180, 105, 120, 210, 225, 135, 155, 150, 125, 170, 210, 195],
          dataGood: [120, 145, 180, 95, 150, 115, 220, 235, 145, 180, 160, 175, 235, 195],
          classStudentData: [{
              className: '软件1班',
              joinData: {
                total: 15,
                male: 9,
                female: 6
              },
              endData: {
                total: 9,
                male: 7,
                female: 2
              },
              goodData: {
                total: 6,
                male: 6,
                female: 0
              }
            },
            {
              className: '软件2班',
              joinData: {
                total: 23,
                male: 12,
                female: 11
              },
              endData: {
                total: 17,
                male: 7,
                female: 10
              },
              goodData: {
                total: 13,
                male: 4,
                female: 9
              }
            },
            {
              className: '软件3班',
              joinData: {
                total: 16,
                male: 8,
                female: 8
              },
              endData: {
                total: 7,
                male: 4,
                female: 3
              },
              goodData: {
                total: 5,
                male: 2,
                female: 3
              }
            },
            {
              className: '软件4班',
              joinData: {
                total: 38,
                male: 26,
                female: 12
              },
              endData: {
                total: 30,
                male: 21,
                female: 9
              },
              goodData: {
                total: 30,
                male: 21,
                female: 9
              }
            },
            {
              className: '软件5班',
              joinData: {
                total: 17,
                male: 12,
                female: 5
              },
              endData: {
                total: 12,
                male: 7,
                female: 5
              },
              goodData: {
                total: 4,
                male: 3,
                female: 1
              }
            },
            {
              className: '大数据1班',
              joinData: {
                total: 13,
                male: 7,
                female: 6
              },
              endData: {
                total: 5,
                male: 2,
                female: 3
              },
              goodData: {
                total: 3,
                male: 2,
                female: 1
              }
            }
          ]
        }
      }
    },
    mounted() {
      document.querySelector('#practiceManagement').classList.add("router-link-exact-active");
      document.querySelector('#practiceManagement').classList.add("router-link-active");
      this.id = this.$route.params.id;
      this.practicing = this.$route.params.practicing;
      this.getPracticeInfoByTea(this.id);
      // 条形图：当前该实践举办过程中每天各数据的活跃度；从实践开始日期到当前日期
      var myChart = echarts.init(this.$refs.dynamicBarChart);
      var option;
      option && myChart.setOption(option);
      var dataMap = {};
      var echartsData = {};
      echartsData = this.echartsData;

      function dataFormatter(obj) {
        var pList = echartsData.date;
        var temp;
        var monthStr1 = echartsData.date[0].substr(0, 4);
        var monthStr2 = echartsData.date[0].substr(0, 3);
        for (var year = 1; year <= 14; year++) {
          var max = 0;
          var sum = 0;
          if (year < 10) {
            temp = obj[monthStr1 + year];
          } else {
            temp = obj[monthStr2 + year];
          }
          for (var i = 0, l = temp.length; i < l; i++) {
            max = Math.max(max, temp[i]);
            sum += temp[i];
            if (year < 10) {
              obj[monthStr1 + year][i] = {
                name: pList[i],
                value: temp[i]
              };
            } else {
              obj[monthStr2 + year][i] = {
                name: pList[i],
                value: temp[i]
              };
            }
          }
          if (year < 10) {
            obj[monthStr1 + year + 'max'] = Math.floor(max / 100) * 100;
            obj[monthStr1 + year + 'sum'] = sum;
          } else {
            obj[monthStr2 + year + 'max'] = Math.floor(max / 100) * 100;
            obj[monthStr2 + year + 'sum'] = sum;
          }
        }
        return obj;
      }
      var echartsJoinDataObj = {};
      var echartsEndDataObj = {};
      var echartsGoodDataObj = {};
      for (var i = 1; i <= echartsData.dataJoin.length; i++) {
        var dateDataKey = null;
        var dateDataJoinValue = [];
        var dateDataEndValue = [];
        var dateDataGoodValue = [];
        if (i < 10) {
          dateDataKey = echartsData.date[0].substr(0, 4) + i;
        } else {
          dateDataKey = echartsData.date[0].substr(0, 3) + i;
        }
        for (var j = 0; j < i; j++) {
          dateDataJoinValue[j] = echartsData.dataJoin[j];
          dateDataEndValue[j] = echartsData.dataEnd[j];
          dateDataGoodValue[j] = echartsData.dataGood[j];
        }
        echartsJoinDataObj[dateDataKey] = dateDataJoinValue;
        echartsEndDataObj[dateDataKey] = dateDataEndValue;
        echartsGoodDataObj[dateDataKey] = dateDataGoodValue;
      }
      dataMap.dataJoin = dataFormatter(echartsJoinDataObj);
      dataMap.dataEnd = dataFormatter(echartsEndDataObj);
      dataMap.dataGood = dataFormatter(echartsGoodDataObj);
      var dateDatas = echartsData.date;
      var datas = [];
      for (var k = 0; k < dateDatas.length; k++) {
        if (k % 2 == 0) {
          datas[k] = dateDatas[k];
        } else {
          datas[k] = '\n' + dateDatas[k];
        }
      }
      myChart.setOption({
        baseOption: {
          timeline: {
            axisType: 'category',
            autoPlay: true,
            playInterval: 1000,
            data: [
              dateDatas[0],
              dateDatas[1],
              dateDatas[2],
              dateDatas[3],
              dateDatas[4],
              dateDatas[5],
              {
                value: dateDatas[6],
                tooltip: {
                  formatter: '{b} 活跃度达到一个高度'
                },
                symbol: 'diamond',
                symbolSize: 16
              },
              dateDatas[7],
              dateDatas[8],
              dateDatas[9],
              dateDatas[10],
              dateDatas[11],
              dateDatas[12],
              {
                value: dateDatas[13],
                tooltip: {
                  formatter: function(params) {
                    return params.name + '活跃度达到又一个高度';
                  }
                },
                symbol: 'diamond',
                symbolSize: 18
              }
            ],
            label: {
              formatter: function(s) {
                return new Date(s).getDate();
              }
            }
          },
          title: {
            subtext: '数据来自WisdomCR团队'
          },
          tooltip: {},
          legend: {
            right: 'right',
            top: '30px',
            data: ['参加实践', '提交实践', '实践点赞']
          },
          calculable: true,
          grid: {
            top: 80,
            bottom: 100,
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
                label: {
                  show: true,
                  formatter: function(params) {
                    return params.value.replace('\n', '');
                  }
                }
              }
            }
          },
          xAxis: [{
            type: 'category',
            axisLabel: {
              interval: 0
            },
            data: datas,
            splitLine: {
              show: false
            }
          }],
          yAxis: [{
            type: 'value',
            name: '活跃度'
          }],
          series: [{
              name: '参加实践',
              type: 'bar',
              color: '#6170c3'
            },
            {
              name: '提交实践',
              type: 'bar',
              color: '#be34c5'
            },
            {
              name: '实践点赞',
              type: 'bar',
              color: '#00b5e2'
            },
            {
              name: '活跃度占比',
              type: 'pie',
              color: ['#6170c3', '#be34c5', '#00b5e2'],
              center: ['74%', '18%'],
              labelLine: {
                normal: {
                  length: 8
                }
              },
              radius: '28%',
              z: 100
            }
          ]
        },
        options: [{
            title: {
              text: echartsData.year + '-' + dateDatas[0] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[0]]
              },
              {
                data: dataMap.dataEnd[dateDatas[0]]
              },
              {
                data: dataMap.dataGood[dateDatas[0]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[0] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[0] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[0] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[1] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[1]]
              },
              {
                data: dataMap.dataEnd[dateDatas[1]]
              },
              {
                data: dataMap.dataGood[dateDatas[1]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[1] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[1] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[1] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[2] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[2]]
              },
              {
                data: dataMap.dataEnd[dateDatas[2]]
              },
              {
                data: dataMap.dataGood[dateDatas[2]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[2] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[2] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[2] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[3] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[3]]
              },
              {
                data: dataMap.dataEnd[dateDatas[3]]
              },
              {
                data: dataMap.dataGood[dateDatas[3]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[3] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[3] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[3] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[4] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[4]]
              },
              {
                data: dataMap.dataEnd[dateDatas[4]]
              },
              {
                data: dataMap.dataGood[dateDatas[4]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[4] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[4] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[4] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[5] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[5]]
              },
              {
                data: dataMap.dataEnd[dateDatas[5]]
              },
              {
                data: dataMap.dataGood[dateDatas[5]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[5] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[5] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[5] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[6] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[6]]
              },
              {
                data: dataMap.dataEnd[dateDatas[6]]
              },
              {
                data: dataMap.dataGood[dateDatas[6]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[6] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[6] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[6] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[7] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[7]]
              },
              {
                data: dataMap.dataEnd[dateDatas[7]]
              },
              {
                data: dataMap.dataGood[dateDatas[7]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[7] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[7] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[7] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[8] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[8]]
              },
              {
                data: dataMap.dataEnd[dateDatas[8]]
              },
              {
                data: dataMap.dataGood[dateDatas[8]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[8] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[8] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[8] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[9] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[9]]
              },
              {
                data: dataMap.dataEnd[dateDatas[9]]
              },
              {
                data: dataMap.dataGood[dateDatas[9]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[9] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[9] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[9] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[10] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[10]]
              },
              {
                data: dataMap.dataEnd[dateDatas[10]]
              },
              {
                data: dataMap.dataGood[dateDatas[10]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[10] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[10] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[10] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[11] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[11]]
              },
              {
                data: dataMap.dataEnd[dateDatas[11]]
              },
              {
                data: dataMap.dataGood[dateDatas[11]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[11] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[11] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[11] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[12] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[12]]
              },
              {
                data: dataMap.dataEnd[dateDatas[12]]
              },
              {
                data: dataMap.dataGood[dateDatas[12]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[12] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[12] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[12] + 'sum']
                  }
                ]
              }
            ]
          },
          {
            title: {
              text: echartsData.year + '-' + dateDatas[13] + '  ' + echartsData.precticeName + '活跃度占比'
            },
            series: [{
                data: dataMap.dataJoin[dateDatas[13]]
              },
              {
                data: dataMap.dataEnd[dateDatas[13]]
              },
              {
                data: dataMap.dataGood[dateDatas[13]]
              },
              {
                data: [{
                    name: '参加实践',
                    value: dataMap.dataJoin[dateDatas[13] + 'sum']
                  },
                  {
                    name: '提交实践',
                    value: dataMap.dataEnd[dateDatas[13] + 'sum']
                  },
                  {
                    name: '实践点赞',
                    value: dataMap.dataGood[dateDatas[13] + 'sum']
                  }
                ]
              }
            ]
          }
        ]
      });

      // 旭日图：当前该实践举办过程中各班级参加、提交、点赞指标的情况，各班级男女参与比例
      var myChart = echarts.init(this.$refs.risingSunchart);
      var option;
      option && myChart.setOption(option);
      var data = [];
      var colorObj = {
        color: ['#ebb40f', '#187a2f', '#0aa3b5', '#4B7CF3', '#ad213e', '#01C2F9'],
        colorChild: {
          joinColor: ['#c3a738', '#a2b029', '#8da0a4', '#627bb2', '#794752', '#01edf9'],
          endColor: ['#e8eb28', '#718933', '#76c0cb', '#4a4ae7', '#e70003', '#0ae7c2'],
          goodColor: ['#eb9123', '#3aa255', '#8fcbb5', '#0f00e7', '#8f0000', '#4475e7']
        }
      };
      for (var i = 0; i < echartsData.classStudentData.length; i++) {
        var joinTotal = echartsData.classStudentData[i].joinData.total;
        var joinMale = echartsData.classStudentData[i].joinData.male;
        var joinFemale = echartsData.classStudentData[i].joinData.female;
        var endTotal = echartsData.classStudentData[i].endData.total;
        var endMale = echartsData.classStudentData[i].endData.male;
        var endFemale = echartsData.classStudentData[i].endData.female;
        var goodTotal = echartsData.classStudentData[i].goodData.total;
        var goodMale = echartsData.classStudentData[i].goodData.male;
        var goodFemale = echartsData.classStudentData[i].goodData.female;
        var dataObj = {
          name: echartsData.classStudentData[i].className,
          itemStyle: {
            color: colorObj.color[i]
          },
          children: [{
              name: '参加' + joinTotal,
              value: joinTotal,
              itemStyle: {
                color: colorObj.colorChild.joinColor[i]
              },
              children: [{
                  name: '男' + joinMale,
                  value: joinMale,
                  itemStyle: {
                    color: '#0000e0'
                  }
                },
                {
                  name: '女' + joinFemale,
                  value: joinFemale,
                  itemStyle: {
                    color: '#ff0080'
                  }
                }
              ]
            },
            {
              name: '提交' + endTotal,
              value: endTotal,
              itemStyle: {
                color: colorObj.colorChild.endColor[i]
              },
              children: [{
                  name: '男' + endMale,
                  value: endMale,
                  itemStyle: {
                    color: '#0000e0'
                  }
                },
                {
                  name: '女' + endFemale,
                  value: endFemale,
                  itemStyle: {
                    color: '#ff007f'
                  }
                }
              ]
            },
            {
              name: '点赞' + goodTotal,
              value: goodTotal,
              itemStyle: {
                color: colorObj.colorChild.goodColor[i]
              },
              children: [{
                  name: '男' + goodMale,
                  value: goodMale,
                  itemStyle: {
                    color: '#0000e0'
                  }
                },
                {
                  name: '女' + goodFemale,
                  value: goodFemale,
                  itemStyle: {
                    color: '#ff007f'
                  }
                }
              ]
            }
          ]
        }
        data[i] = dataObj;
      }
      myChart.setOption({
        title: {
          text: '各班参与情况与男女参与比例',
          left: 'center',
          top: 'bottom',
          textStyle: {
            verticalAlign: 'bottom',
            fontSize: 18,
            align: 'center'
          }
        },
        series: {
          type: 'sunburst',
          center: ['50%', '46%'],
          data: data,
          radius: [0, '95%'],
          sort: undefined,
          emphasis: {
            focus: 'ancestor'
          },
          levels: [{},
            {
              r0: '15%',
              r: '35%',
              itemStyle: {
                borderWidth: 2
              },
              label: {
                rotate: 'tangential'
              }
            },
            {
              r0: '35%',
              r: '70%',
              label: {
                align: 'right'
              }
            },
            {
              r0: '75%',
              r: '72%',
              label: {
                position: 'outside',
                padding: 3,
                silent: false
              },
              itemStyle: {
                borderWidth: 3
              }
            }
          ]
        }
      });

      // 广东地图：表示当前实践进行的地点
      this.initMap();
    },
    methods: {
      baiduMapLink() {
        this.copy(this.practiceMain.praSite);
        this.openCopyTips();
        return setTimeout(() => {
          window.open(this.indexro, '_blank'); // 在新窗口打开外链接
        }, 2500)
      },
      copy(data) {
        var sel = window.getSelection(); //获取Selection对象
        var range = document.createRange(); //创建Range对象
        var node = document.createTextNode(data); //创建文本节点，并指定内容
        document.body.appendChild(node); //加入body末尾（否则无法选中）
        range.selectNode(node); //选中文本节点
        sel.removeAllRanges(); //删除原先选区
        sel.addRange(range); //将区域加入选区
        document.execCommand("copy"); //execCommand执行复制操作
        document.body.removeChild(node); //删除临时节点
      },
      openCopyTips() {
        const h = this.$createElement;
        this.$notify({
          title: '实践地址已自动复制',
          message: h('i', {
            style: 'color: teal'
          }, '正在跳转至百度地图页面')
        });
      },
      // 地图初始化
      initMap() {
        // 创建Map实例
        this.map = new BMap.Map('guangDongMap')
        // 初始化地图,设置中心点坐标和地图级别
        this.map.centerAndZoom(new BMap.Point(this.echartsData.latitude, this.echartsData.longitude), this.mapZoom)
        // 设置地图显示的城市 此项是必须设置的
        this.map.setCurrentCity('广州')
        // 启用滚轮放大缩小，默认禁用
        this.map.enableScrollWheelZoom(true)
        // 启用地图惯性拖拽，默认禁用
        this.map.enableContinuousZoom(true)
        this.map.setMapStyleV2({
          styleJson: baiduMap
        });
        var point = new BMap.Point(this.echartsData.latitude, this.echartsData.longitude);
        var marker = new BMap.Marker(point); // 创建标注
        this.map.addOverlay(marker); // 将标注添加到地图中
      },
      //跳转到更多实践界面
      returnManagement() {
        this.$router.push({
          name: 'practiceManagement',
          params: {
            returnId: this.id,
            returnPracticing: this.practicing
          }
        })
      },
      // 获取该教师所有课程
      async getAllCourseByTeaId() {
        const result=await this.$API.praManagement.getAllCourseByTeaId();
        this.teaCourse = result.data.data;
      },
      // 获取所有实践类别
      async getAllPracticeType() {
        const result=await this.$API.praManagement.getAllPracticeType();
        this.practiceType = result.data.data;
      },
      //根据实践主键ID查询实践详情
      async getPracticeInfoByTea(id) {
        const result = await this.$API.praManagement.getPracticeInfoByTea(id);
        if (result.data.code == 200) {
          this.practiceMain = result.data.data;
          this.practiceMain.createTime = result.data.data.createTime.slice(0, 10);
          this.practiceMain.endTime = result.data.data.endTime.slice(0, 10);
          if(result.data.data.praImgName == "default"){
            result.data.data.praImgIO = require("../../../assets/R-C.jpg");
          }
          this.edit.praName = this.practiceMain.praName;
          this.edit.praSite = this.practiceMain.praSite;
          this.edit.id = this.practiceMain.id;
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
          this.edit.teaId = this.practiceMain.teaId;
          this.edit.createTime = this.practiceMain.createTime;
          this.edit.praMain = this.practiceMain.praMain;
          this.edit.oldImg = this.practiceMain.praImgName;
        }
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
        this.editImg = '';
        this.$refs.upload.clearFiles();
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
      async editPractice(edit) {
        this.editDialogVisible = false;
        const result = await this.$API.praManagement.editPractice(edit);
        if (result.data.code == 200) {
          this.practiceMain = result.data.data;
          this.practiceMain.createTime = result.data.data.createTime.slice(0, 10);
          this.practiceMain.endTime = result.data.data.endTime.slice(0, 10);
          if(result.data.data.praImgName == "default"){
            this.practiceMain.praImgIO = require("../../../assets/R-C.jpg");
          }else{
            this.practiceMain.praImgIO = result.data.data.praImgIO;
          }
          this.edit.praName = this.practiceMain.praName;
          this.edit.praSite = this.practiceMain.praSite;
          this.edit.id = this.practiceMain.id;
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
          this.edit.teaId = this.practiceMain.teaId;
          this.edit.createTime = this.practiceMain.createTime;
          this.edit.praMain = this.practiceMain.praMain;
          this.edit.praImg = '';
          this.edit.oldImg = this.practiceMain.praImgName;
          this.$message({
            showClose: true,
            message: result.data.msg,
            type: 'success'
          });
        }
      },
      // 修改实践详情弹窗关闭
      editHandleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
      },
      // 上传图标事件
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
      editHandleChangePicture(file, fileList) {
        if (fileList.length > 1) {
          fileList.splice(0, 1);
          this.editHandleRemove();
        }
      },
      // 查看图片
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisibleUrl = true;
      },
      // 删除图片
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
      }
    },
    destroyed() {
      document.querySelector('#practiceManagement').classList.remove("router-link-exact-active");
      document.querySelector('#practiceManagement').classList.remove("router-link-active");
    },
    created() {
      this.getAllPracticeType();
      this.getAllCourseByTeaId();
    }
  }
</script>

<style lang="scss" scoped>
.body {
  width: 85%;
  height: 740px;

  .left {
    width: 29.5%;
    height: 100%;
    float: left;
    margin-top: 10px;

    .top-info {
      width: 100%;
      height: 64%;
      //background-color: #b3bef39e;
      border-radius: 5px 5px 5px 5px;
      box-shadow: 1px 1px 4px 1px lightgrey;
      display: flex;
      flex-direction: column;
      align-items: center;
      .history_and_name {
        width: 100%;
        height: 12%;

        .main-name {
          width: 66%;
          height: 95%;
          float: left;
          margin: 9px 0px 0px 8px;
          font-size: 18px;
          font-weight: 700;
        }

        .info-windows-history {
          width: 30%;
          height: 62%;
          background-color: #4d58b5;
          color: #fff;
          float: right;
          margin: 8px 5px 0px 0px;
          text-align: center;
          line-height: 35px;
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
        color: #111b59;
        //position: relative;
        display: flex;
        flex-direction: column;
        align-items: center;

        .info-windows-row-top{
          display: flex;
          justify-content: center;
        }

        .editPractice {
          height: 200px;
          width: 325px;
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
          margin: 15px 0px 0 14px;
          font-size: 13px;
          display: -webkit-box;
          -webkit-box-orient: vertical;
          -webkit-line-clamp: 6;
          overflow: hidden;

          .practice-type {
            font-weight: 700;
            color: #111b59;
            margin: 4px 0;
          }
        }
      }
    }

    .bottom-data-two {
      width: 100%;
      height: 33%;
      margin-top: 8px;
      //background-color: #b3bef39e;
      border-radius: 5px 5px 5px 5px;
      box-shadow: 1px 1px 4px 1px lightgrey;

      .join {
        width: 35%;
        height: 62%;
        margin: 85px 0px 0px 3px;
        float: left;

        .circle-join {
          width: 120px;
          height: 120px;
          border-radius: 50%;
          background: linear-gradient(0deg, #7676f6 0%, #d789ff 68%);

          .content-join {
            position: relative;
            top: 10px;
            left: 10px;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background-color: #fff;
            text-align: center;
            line-height: 105px;
            font-size: 35px;
          }
        }

        .content-join-text {
          color: #3a3aff;
          font-size: 20px;
          font-weight: 700;
          text-align: center;
          margin: 4px 0px 0 0;
        }
      }

      .good {
        width: 28%;
        height: 70%;
        margin: 10px 0px 0px 0px;
        float: left;

        .circle-good {
          width: 100px;
          height: 100px;
          border-radius: 50%;
          //background: linear-gradient(0deg, #ff3a3a 0%, #7446ff 100%);
          background: linear-gradient(0deg, #7676f6 0%, #d789ff 68%);
          .content-good {
            position: relative;
            top: 10px;
            left: 10px;
            width: 80px;
            height: 80px;
            border-radius: 50%;
            background-color: #fff;
            text-align: center;
            line-height: 82px;
            font-size: 35px;
          }
        }

        .content-good-text {
          font-size: 20px;
          font-weight: 700;
          text-align: center;
          margin: 4px 0px 0 0;
          //color: #3a3aff;
          //background-image: linear-gradient(0deg, #5200ff 0%, #fc1000 66%);
          background-image: linear-gradient(#3a3aff);
          background-clip: text;
          -webkit-background-clip: text;
          color: transparent;
        }
      }

      .complete {
        width: 35%;
        height: 62%;
        margin: 85px 3px 0px 0px;
        float: right;

        .circle-complete {
          width: 120px;
          height: 120px;
          border-radius: 50%;
          //background: linear-gradient(0deg, #ff2b2b 0%, #fbff00 68%);
          background: linear-gradient(0deg, #7676f6 0%, #d789ff 68%);
          .content-complete {
            position: relative;
            top: 10px;
            left: 10px;
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background-color: #fff;
            text-align: center;
            line-height: 105px;
            font-size: 35px;
          }
        }

        .content-complete-text {
          //color: #ff0000;
          color: #3a3aff;
          font-size: 20px;
          font-weight: 700;
          text-align: center;
          margin: 4px 0px 0 0;
        }
      }
    }
  }

  .right {
    width: 70%;
    height: 100%;
    float: right;
    margin-top: 10px;

    .top-echarts {
      width: 99.5%;
      height: 49%;
      //background-color: #b3bef39e;
      border-radius: 5px 5px 5px 5px;
      box-shadow: 1px 1px 4px 1px lightgrey;

      .top-echarts-main {
        width: 100%;
        height: 100%;
      }
    }

    .bottom-echarts-two {
      width: 99.5%;
      height: 48.5%;
      //background-color: #6e81de17;
      margin-top: 4px;
      border-radius: 5px 5px 5px 5px;
      box-shadow: 1px 1px 4px 1px lightgrey;

      .echarts-left {
        width: 49.5%;
        height: 99%;
        //background-color: #b3bef39e;
        margin: 4px 2px 0 0;
        border-radius: 5px 5px 5px 5px;
        box-shadow: 1px 1px 4px 1px lightgrey;
        float: left;

        .echarts-left-main {
          width: 100%;
          height: 100%;
        }
      }

      .echarts-right {
        width: 49.5%;
        height: 99%;
        //background-color: #b3bef39e;
        margin: 4px 0 0 2px;
        border-radius: 5px 5px 5px 5px;
        box-shadow: 1px 1px 4px 1px lightgrey;
        float: right;
        position: relative;

        .regionTitle {
          width: 20%;
          height: 8%;
          font-size: 19px;
          font-weight: 700;
          text-align: center;
          line-height: 30px;
          background-color: #b7c1f347;
          color: #464646;
          border-radius: 0 0 5px 5px;
          position: absolute;
          bottom: 0;
          left: 40%;
        }

        .query-route {
          width: 31%;
          height: 8%;
          background-color: #6170c3;
          text-align: center;
          position: absolute;
          right: 5px;
          bottom: 5px;
          border-radius: 5px 5px 5px 5px;
          line-height: 25px;
          // font-weight: 700;
          cursor: pointer;
          color: #fff;
        }

        .echarts-right-main {
          width: 100%;
          height: 100%;
          opacity: 0.8;
          border-radius: 5px 5px 0 0;
        }
      }
    }
  }
}
</style>
