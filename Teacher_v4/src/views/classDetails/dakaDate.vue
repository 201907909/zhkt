<template>
  <div class="con">
    <div class="now-data-myself">
      <div class="now-data-myself-time" style="font-size:20px;margin-top: 10px;">打卡日历</div>
    </div>
    <Calendar v-on:choseDay="clickDay" v-on:changeMonth="changeDate" v-on:isToday="clickToday" :markDateMore=arr>
    </Calendar>
  </div>
</template>
   
<script>
/* eslint-disable */
import Calendar from 'vue-calendar-component';
export default {
  components: {
    Calendar
  },
  data() {
    return {
      arrObject: {
        date: '',
        className: 'mark1'
      },
      arr: [],
      date: "",
      week: ""
    };
  },
  created() {
    var now = new Date();
    this.date = now.getDate();//得到日期
    var day = now.getDay();//得到周几
    var arr_week = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六");
    this.week = arr_week[day];
  },
  mounted() {
    this.$bus.$on('getDakaDate', (data) => {
      this.arr = []
      data.forEach(item => {
        const arrObject = {
          date: '',
          className: 'mark1'
        }
        arrObject.date = item
        this.arr.push(arrObject)
      });
    })
  },
  methods: {
    clickDay(data) {
      // console.log(data); //选中某天
    },
    changeDate(data) {
      // console.log(data); //左右点击切换月份
    },
    clickToday(data) {
      // console.log(data); // 跳到了本月
    }
  },
};
</script>
   
<style>
.wh_content_item .wh_chose_day {
    /* background: #409EFF !important; */
    color: #ffff !important;
}
.wh_content_item .wh_isToday[data-v-2ebcbc83] {
    background: #409Eff;
    border-radius: 100px;
}
.wh_item_date[data-v-2ebcbc83]:hover {
    background:white;
    cursor: pointer;
}

.wh_content_all[data-v-2ebcbc83]{
  background-color: white;
}
.mark1 {
  /* background-color: red; */
  background-image: url('../../assets/打勾.png') !important;
  background-repeat: no-repeat;
  background-size: 20px 20px;
  background-position: top left;
  background-position-x: 12px;
  background-position-y: 22px;
  z-index: 999;
}

.now-data-myself {
  width: 40%;
  position: absolute;
  border-right: 1px solid rgba(227, 227, 227, 0.6);
}

.con {
  position: relative;
  max-width: 280px;
  margin: auto;
}

/* .con .wh_content_all {
  background: transparent !important;
} */

.wh_top_changge li {
  color: #F56C6C !important;
  font-size: 15px !important;
}

.wh_content_item,
.wh_content_item_tag {
  color: #303133 !important;
}

/* .wh_content_item .wh_isToday {
  background: #00d985 !important;
  color: #fff !important;
} */

.wh_content_item .wh_chose_day {
  background: #409EFF !important;
  color: #ffff !important;
}

/* .wh_item_date:hover {
  background: rgb(217, 236, 255) !important;
  border-radius: 100px !important;
  color: rgb(102, 177, 255)  !important;
} */

.wh_jiantou1[data-v-2ebcbc83] {
  border-top: 2px solid #909399;
  border-left: 2px solid #909399;
  width: 7px;
  height: 7px;
}

.wh_jiantou2[data-v-2ebcbc83] {
  border-top: 2px solid #909399;
  border-right: 2px solid #909399;
  width: 7px;
  height: 7px;
}

.wh_top_tag[data-v-2ebcbc83] {
  color: #409EFF;
  border-top: 1px solid rgba(227, 227, 227, 0.6);
  border-bottom: 1px solid rgba(227, 227, 227, 0.6);
}

.wh_container[data-v-2ebcbc83] {
  max-width: 280px;
}

.wh_top_changge[data-v-2ebcbc83] {
  display: flex;
  width: 50%;
  margin-left: 43%;
}

.now-data-myself-time {
  color: #F56C6C;
  font-size: 28px;
  height: 30px;
  font-family: "Helvetica Neue";
  text-align: center;
}

.now-data-myself-week {
  font-size: 10px;
  color: #909399;
  text-align: center;
}

.wh_top_changge .wh_content_li[data-v-2ebcbc83] {
  font-family: Helvetica;
  flex: 3.5;
}


.wh_content_item .wh_isToday[data-v-2ebcbc83] {
  /* background: #ff0; */
  border-radius: 100px;
}

/* .wh_item_date[data-v-2ebcbc83]:hover {
  background: #71c7a5;
  cursor: pointer;
} */

.wh_content_item .wh_isToday {
  /* background: #00d985 !important; */
  /* color: #fff !important; */
  z-index: -999;
}

.wh_content_item .wh_isToday[data-v-2ebcbc83] {
  /* background: #ff0; */
  border-radius: 100px;
}

.wh_content_item .wh_chose_day[data-v-2ebcbc83] {
  z-index: -99;
  background: #409Eff;
  border-radius: 100px;
  background-repeat: no-repeat;
  background-position-x: 8px;
  background-size: 25px 25px;
  background-position-y: 17px;
}
</style>