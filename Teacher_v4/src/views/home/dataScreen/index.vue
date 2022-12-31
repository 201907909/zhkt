<template>
  <div class="body">
    <div class="left">
      <div class="left-top" ref="chart1">

      </div>
      <div class="left-bottom" ref="chart2"></div>
    </div>
    <div class="right">
      <div class="right-top" ref="chart3"></div>
      <div class="right-bottom" ref="chart4"></div>
    </div>

  </div>
</template>

<script>
  export default {
    name: "dataScreen",
    data() {
      return {
        class: ['软件204', '软件205', '软件201', '软件202', '软件203', '通信201', '通信202'],
        allClassData: [{
            name: '软件204',
            data: [150, 200, 204, 218, 220, 240, 260],
          },
          {
            name: '软件205',
            data: [140, 210, 224, 268, 270, 40, 60],
          },
          {
            name: '软件201',
            data: [123, 230, 141, 212, 240, 130, 60],
          },
          {
            name: '软件202',
            data: [131, 210, 104, 128, 110, 230, 110],
          },
          {
            name: '软件203',
            data: [263, 233, 244, 118, 120, 140, 200],
          },
          {
            name: '通信201',
            data: [123, 146, 143, 99, 130, 240, 200],
          },
          {
            name: '通信202',
            data: [130, 270, 104, 118, 120, 120, 230],
          },
        ],
        allStudentData: [{
            name: '张三',
            data: [150, 200, 204, 218, 220, 240, 260],
          },
          {
            name: '李四',
            data: [140, 210, 224, 268, 270, 40, 60],
          },
          {
            name: '王五',
            data: [123, 230, 141, 212, 240, 130, 60],
          },
          {
            name: '赵六',
            data: [131, 210, 104, 128, 110, 230, 110],
          },
          {
            name: '阿米娅',
            data: [263, 233, 244, 118, 120, 140, 200],
          },
          {
            name: '琪亚娜',
            data: [123, 146, 143, 99, 130, 240, 200],
          },
          {
            name: '山姆',
            data: [130, 270, 104, 118, 120, 120, 230],
          },
        ]

      }
    },


    mounted() {
      this.getEchartData1()
      this.getEchartData2()
      this.getEchartData3()
      this.getEchartData4()



    },
    methods: {

      getEchartData1() {
        const myChart = this.$echarts.init(this.$refs.chart1)
        const option = {
          title: {
            text: '班级总进度'
          },
          //backgroundColor: '#0f375f',
          legend: {
            data: ['班级目标学时', '班级完成学时'],
            textStyle: {
              color: '#ccc'
            }
          },
          tooltip: {},
          xAxis: {
            type: 'category',
            data: this.class,
            axisLine: {
              lineStyle: {
                color: '#ccc'
              }
            }
          },
          yAxis: {
            splitLine: {
              show: false
            },
            axisLine: {
              lineStyle: {
                color: '#ccc'
              }
            }
          },
          series: [{
              name: '班级目标学时',
              type: 'line',
              smooth: true,
              showAllSymbol: true,
              symbol: 'emptyCircle',
              data: [1000, 800, 1250, 1100, 1300, 1200, 990],
            },
            {
              name: '班级完成学时',
              type: 'bar',
              barWidth: 10,
              itemStyle: {
                borderRadius: 5,
                color: '#87CEFA'

              },
              data: [400, 300, 600, 700, 300, 400, 700],
            },
            {
              name:'班级目标学时',
              type: 'bar',
              barWidth: 10,
              barGap: '-100%',
              itemStyle: {
                borderRadius: 5,
                //color: '#ADD8E6'
                color: this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: 'rgba(20,200,212,0.5)'
                  },
                  {
                    offset: 0.2,
                    color: 'rgba(20,200,212,0.2)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(20,200,212,0)'
                  }
                ])
              },
              z: -12,
               data: [1000, 800, 1250, 1100, 1300, 1200, 990],
            },
            // {
            //   name: 'dotted',
            //   type: 'pictorialBar',
            //   symbol: 'rect',
            //   itemStyle: {
            //     color: '#0f375f'
            //   },
            //   symbolRepeat: true,
            //   symbolSize: [12, 4],
            //   symbolMargin: 1,
            //   z: -10,
            //   data: [80, 70, 60, 100, 110, 130, 90],
            // }
          ]
        }
        myChart.setOption(option)
        myChart.on('click', (e) => {
          this.setEchartData2(e.name);
        })
        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })

      },

      setEchartData2(name) {
        let data;
        for (var i = 0; i < this.allClassData.length; i++) {
          if (this.allClassData[i].name == name) {
            data = this.allClassData[i].data
          }
        }
        const myChart = this.$echarts.init(this.$refs.chart2)
        const option = {
          title: {
            text: '班级学习变化'
          },
          legend: {},
          tooltip: {},
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: name,
            data: data,
            type: 'line'
          }]
        }
        myChart.setOption(option)
        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })

      },

      getEchartData2() {
        const myChart = this.$echarts.init(this.$refs.chart2)
        const option = {
          title: {
            text: '班级学习变化'
          },
          legend: {},
          tooltip: {},
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '软件204',
            data: [150, 200, 204, 218, 220, 240, 260],
            type: 'line'
          }]
        }
        myChart.setOption(option)
        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })
      },

      getEchartData3() {
        const myChart = this.$echarts.init(this.$refs.chart3)
        const option = {
          title: {
            text: '学习之星'
          },
          legend: {},
          tooltip: {},
          xAxis: {},
          yAxis: {

            data: ['张三', '李四', '王五', '赵六', '阿米娅', '琪亚娜', '山姆']
          },
          series: [{
            name: '学习时长',
            data: [150, 200, 204, 218, 235, 250, 260],
            type: 'bar'
          }]
        }
        myChart.setOption(option)

        myChart.setOption(option)
        myChart.on('click', (e) => {
          this.setEchartData4(e.name);
        })

        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })
      },

      setEchartData4(name) {
        let data;
        for (var i = 0; i < this.allStudentData.length; i++) {
          if (this.allStudentData[i].name == name) {
            data = this.allStudentData[i].data
          }
        }
        const myChart = this.$echarts.init(this.$refs.chart4)
        const option = {
          title: {
            text: '个人学习变化'
          },
          legend: {},
          tooltip: {},
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: name,
            data: data,
            type: 'line'
          }]
        }
        myChart.setOption(option)
        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })

      },



      getEchartData4() {
        const myChart = this.$echarts.init(this.$refs.chart4)
        const option = {
          title: {
            text: '学生学习时长变化'
          },
          legend: {},
          tooltip: {},
          xAxis: {
            type: 'category',
            data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name: '山姆',
            data: [150, 230, 224, 218, 135, 147, 260],
            type: 'line'
          }]
        }
        myChart.setOption(option)
        window.addEventListener("resize", function() {
          myChart.resize()
        })

        this.$on('hook:destroyed', () => {
          window.removeEventListener("resize", function() {
            myChart.resize();
          });
        })
      }


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
    display: flex;
    justify-content: space-between;

    .left {
      width: 50%;
      height: 100%;
      //background-color: aqua;
      display: flex;
      flex-direction: column;
      align-content: space-between;
      justify-content: center;

      .left-top {
        width: 100%;
        height: 50%;
        //background-color: antiquewhite;

      }

      .left-bottom {
        width: 100%;
        height: 50%;
        //background-color: black;

      }
    }

    .right {
      width: 50%;
      height: 100%;
      //background-color: aqua;
      display: flex;
      flex-direction: column;
      align-content: space-between;
      justify-content: center;

      .right-top {
        width: 100%;
        height: 50%;
        //background-color: blue;

      }

      .right-bottom {
        width: 100%;
        height: 50%;
        //background-color: orange;

      }
    }

  }
</style>
