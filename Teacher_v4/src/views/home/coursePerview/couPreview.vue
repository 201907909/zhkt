<template>
  <div class="couPreview_body">
    <div class="couPreview_top">
      <coursetypeall></coursetypeall>
    </div>
    <div class="couPreview_middle">
      <div class="couPreview_middle_left">
        <div class="carousel_swipe" v-bind:id="'carousel_swipe' + id" @mouseenter.stop="handleStop"
          @mouseleave.stop="handleGo">
          <!--图片轮播区域-->
          <transition-group :name="'list-' + (direction === 'forward' ? 'in' : 'out')" tag="ul" class="swipe_list">
            <li v-for="(item, index) in carouselCourseList" :key="index + 1" class="list-item"
              v-show="index === currentIndex">
              <a target="_blank">
                <img alt="" :src="item.couPicIO" @click="toCourse(item)">
              </a>
            </li>
          </transition-group>
          <!--控制点-->
          <div class="swipe_dian">
            <span v-for="(item, index) in carouselCourseList" :class="{ 'active': index === currentIndex }" :key="index"
              @mouseover="handleChange(index)"></span>
          </div>
          <!--左右控制，使用elementUI 的图标-->
          <div class="swipe_control" v-bind:id="'swipe_control' + id" v-show="true">
            <i class="el-icon-arrow-left prev" v-on:click="handleChange(prevIndex)"></i>
            <i class="el-icon-arrow-right next" v-on:click="handleChange(nextIndex)"></i>
          </div>
          <!--底部标题-->
          <div class="swipe_title" v-for="(item, index) in carouselCourseList" v-show="index === currentIndex"
            :key="index">
            {{ item.couName }}
          </div>
          <div class="swipe_bottom"></div>
        </div>
      </div>
      <div class="couPreview_middle_rigth">
        <!-- <div class="couPreview_middle_rigth_top">
          <el-input style="width: 40%;"  placeholder="请输入内容" suffix-icon="el-icon-search"></el-input>
        </div> -->
        <div class="couPreview_middle_rigth_main">
          <div class="couPreview_middle_rigth_main_list">
            <div class="couPreview_middle_rigth_main_list_item" v-for="item in firstCourseList">
              <div class="couPreview_middle_rigth_main_list_item_img">
                <img :src="item.couPicIO" alt="" @click="toCourse(item)">
              </div>
              <div class="couPreview_middle_rigth_main_list_item_info">
                <span class="courseName">{{ item.couName }}</span>
                <span>{{ item.teaName }}</span>
                <span class="el-icon-time">{{item.createTime}}</span>
              </div>

            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="couPreview_foot">
      <div class="couPreview_foot_recommedCourseFour" style="border-bottom: 2px solid #dbdbdb;padding-top: 10px;border-top: 2px solid rgb(219, 219, 219);">
        <div class="couPreview_foot_recommedCourseFour_title">
          <img src="../../../assets/热门.png" alt="">
          <h2>热门推荐</h2>
        </div>
        <div class="card-carousel-wrapper">
          <div class="card-carousel--nav__left" @click="moveCarouselRemen(-1)" :disabled="atHeadOfListRemen"></div>
          <div class="card-carousel">
            <div class="card-carousel--overflow-container">
              <div class="card-carousel-cards"
                :style="{ transform: 'translateX' + '(' + currentOffsetRenmen + 'px' + ')' }">
                <div class="card-carousel--card" v-for="item in secondCourseList"><img :src="item.couPicIO"
                    @click="toCourse(item)">
                  <div class="card-carousel--card--footer">
                    <p>{{ item.couName }}</p>
                    <p>{{ item.teaName }}</p>
                    <span style="font-weight:bolder;font-size:13px" class="el-icon-time">{{item.createTime}}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="card-carousel--nav__right" @click="moveCarouselRemen(1)" :disabled="atEndOfListRemen"></div>
        </div>
      </div>
      <!-- 四列推荐课 -->
      <div>
        <div class="couPreview_foot_recommedCourseFour" v-for="(courses, index) in thirdCourseList" :key="index">
          <div class="card-carousel-wrapper">
            <div class="card-carousel--nav__left" @click="moveCarousel(-1, index)" :disabled="atHeadOfList[index]">
            </div>
            <div class="card-carousel">
              <div class="card-carousel--overflow-container">
                <div class="card-carousel-cards"
                  :style="{ transform: 'translateX' + '(' + currentOffset[index] + 'px' + ')' }">
                  <div class="card-carousel--card" v-for="item in courses"><img :src="item.couPicIO"
                      @click="toCourse(item)">
                    <div class="card-carousel--card--footer">
                      <p>{{ item.couName }}</p>
                      <p>{{ item.teaName }}</p>
                      <span style="font-weight:bolder;font-size:13px" class="el-icon-time">{{item.createTime}}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="card-carousel--nav__right" @click="moveCarousel(1,index)" :disabled="atEndOfList[index]"></div>
          </div>
        </div>

      </div>
    </div>


  </div>
</template>

<script>
  import courseTypeAll from '../../../components/courseTypeAll.vue';
export default {
  name: 'couPreview',
  data() {
    return {
      atEndOfList: [false, false, false, false],
      atHeadOfList: [false, false, false, false],
      tempEnd2: 0,
      tempEnd: 0,
      temp: 0,
      currentOffset: [0, 0, 0, 0],
      windowSize: 4,
      paginationFactor: 250,
      id: 0,
      currentIndex: 0,
      timer: "",
      control: false,
      direction: 'forward',
      carouselCourseList: [],
      firstCourseList: [],
      secondCourseList: [],
      thirdCourseList: [],
      currentOffsetRenmen: 0,
    }
  },
  components: {
    "coursetypeall": courseTypeAll
  },


  created() {
    this.id = Math.random();
    this.getAllAnyFieryCourse()
  },

  mounted() {
    document.getElementById("carousel_swipe" + this.id).style.width =
      this.imageWidth + "rem";
    document.getElementById("carousel_swipe" + this.id).style.height =
      this.imageHeight + "rem";
    document.getElementById("swipe_control" + this.id).style.top =
      this.imageHeight / 2 - 1 + "rem";
    this.handleGo();
  },

  computed: {

    atEndOfListRemen() {
      return this.currentOffsetRenmen <= (this.paginationFactor * -1) * (this.secondCourseList.length - this.windowSize);
    },
    atHeadOfListRemen() {
      return this.currentOffsetRenmen === 0;
    },

    prevIndex() {
      if (this.currentIndex === 0) {
        return this.carouselCourseList.length - 1;
      } else {
        return this.currentIndex - 1;
      }
    },
    nextIndex() {
      if (this.currentIndex === this.carouselCourseList.length - 1) {
        return 0;
      } else {
        return this.currentIndex + 1;
      }
    }
  },

  methods: {
    toCourse(cou) { //跳转章节
      console.log("跳转")
      console.log(cou)
      this.$router.push({
        path: "/home/courseShow",
        query: {
          couId: cou.couId,
          couName: cou.couName,
          couPicIO: cou.couPicIO
        }
      })
    },
    async getAllAnyFieryCourse() {
      const result = await this.$API.webClass.getAllAnyFieryCourse()
      this.carouselCourseList = result.data.data.carouselCourseList
      this.firstCourseList = result.data.data.firstCourseList
      this.secondCourseList = result.data.data.secondCourseList
      this.thirdCourseList = result.data.data.thirdCourseList
      console.log(result.data.data)
    },
    moveCarouselRemen(direction) {
      if (direction === 1 && !this.atEndOfListRemen) {
        this.currentOffsetRenmen -= this.paginationFactor;
      } else if (direction === -1 && !this.atHeadOfListRemen) {
        this.currentOffsetRenmen += this.paginationFactor;
      }

    },

    moveCarousel(direction, index) {
      this.temp = index
      this.tempEnd = (this.thirdCourseList[index].length * this.paginationFactor) - 1000
      if (direction == 1) {
        this.tempEnd2 = -this.currentOffset[index]
        if (this.tempEnd2 != this.tempEnd) {
          this.currentOffset[index] -= this.paginationFactor;
          this.$forceUpdate()
        } else if (this.tempEnd2 == this.tempEnd) {
          this.atHeadOfList = true
        }
      } else if (direction == -1 && this.currentOffset[index] != 0) {
        this.currentOffset[index] += this.paginationFactor
        this.$forceUpdate()
      }
    },
    /**
     * 开始轮播
     */
    handleGo() {
      this.control = false;
      this.timer = setInterval(() => {
        this.currentIndex++;
        if (this.currentIndex > this.carouselCourseList.length - 1) {
          this.currentIndex = 0;
        }
      }, 3000);
    },
    /**
     * 停止轮播
     */
    handleStop() {
      this.control = true;
      clearInterval(this.timer);
      this.timer = null;
    },
    /**
     * 控制左右切换
     * @param index
     */
    handleChange(index) {
      if (index > this.currentIndex) {
        this.direction = 'forward'
      } else {
        this.direction = ''
      }
      this.currentIndex = index;
    }
  },
}

</script>

<style  lang="scss" scoped>
.couPreview_body {
  width: 85%;
  height: auto;
  margin: auto;
  background-color: #f6f6ff;
  border-radius: 1%;
  margin-top: 10px;

  .couPreview_foot {
    width: 100%;
    display: flex;
    flex-direction: column;

    .couPreview_foot_recommedCourseFour {
      width: 100%;
      // height: 200px;
      // background-color: #5f5853;

      .couPreview_foot_recommedCourseFour_title {
        width: 98%;
        margin: auto;
        display: flex;

        img {
          width: 30px;
          height: 30px;
        }

        h2 {
          margin: 0px;
        }
      }

      .card-carousel-wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 20px 0 40px;
        color: #666a73;
      }

      .card-carousel {
        display: flex;
        justify-content: center;
        width: 90%;
      }

      .card-carousel--overflow-container {
        overflow: hidden;
      }

      .card-carousel--nav__left,
      .card-carousel--nav__right {
        display: inline-block;
        width: 15px;
        height: 15px;
        padding: 10px;
        box-sizing: border-box;
        border-top: 2px solid #42b883;
        border-right: 2px solid #42b883;
        cursor: pointer;
        margin: 0 10px;
        transition: transform 150ms linear;
      }

      .card-carousel--nav__left[disabled],
      .card-carousel--nav__right[disabled] {
        opacity: 0.2;
        border-color: black;
      }

      .card-carousel--nav__left {
        transform: rotate(-135deg);
      }

      .card-carousel--nav__left:active {
        transform: rotate(-135deg) scale(0.9);
      }

      .card-carousel--nav__right {
        transform: rotate(45deg);
      }

      .card-carousel--nav__right:active {
        transform: rotate(45deg) scale(0.9);
      }

      .card-carousel-cards {
        display: flex;
        transition: transform 150ms ease-out;
        transform: translatex(0px);

        img {
          width: 232px;
          height: 150px;
          border-radius: 10px;
        }
      }

      // .card-carousel-cards{
      //   width: 95%;
      // }

      .card-carousel-cards .card-carousel--card {
        margin: 0 10px;
        cursor: pointer;
        box-shadow: 0 4px 15px 0 rgba(40, 44, 53, 0.06), 0 2px 2px 0 rgba(40, 44, 53, 0.08);
        background-color: #fff;
        // border-radius: 4px;
        z-index: 3;
        margin-bottom: 2px;
        border-radius: 10px;
      }

      .card-carousel--card {
        width: 232px;
        border-top-left-radius: 10px;
        border-top-right-radius: 10px;
      }

      .card-carousel-cards .card-carousel--card:first-child {
        margin-left: 0;
      }

      .card-carousel-cards .card-carousel--card:last-child {
        margin-right: 0;
      }

      .card-carousel-cards .card-carousel--card img {
        vertical-align: bottom;
        border-top-left-radius: 4px;
        border-top-right-radius: 4px;
        transition: opacity 150ms linear;
        user-select: none;
        border-radius: 10px;
      }

      .card-carousel-cards .card-carousel--card img:hover {
        opacity: 0.5;
      }

      .card-carousel-cards .card-carousel--card--footer {
        border-top: 0;
        padding: 7px 15px;
      }

      .card-carousel-cards .card-carousel--card--footer p {
        padding: 3px 0;
        margin: 0;
        margin-bottom: 2px;
        font-size: 16px;
        font-weight: bolder;
        color: #2c3e50;
        user-select: none;
      }

      .card-carousel-cards .card-carousel--card--footer p:nth-of-type(2) {
        // font-size: 12px;
        // font-weight: 300;
        // padding: 6px;
        // background: rgba(40, 44, 53, 0.06);
        // display: inline-block;
        // position: relative;
        // margin-left: 4px;
        // color: #666a73;
        font-size: 14px;
      }

      .card-carousel-cards .card-carousel--card--footer p:nth-of-type(2):before {
        content: "";
        float: left;
        position: absolute;
        top: 0;
        left: -12px;
        width: 0;
        height: 0;
        border-color: transparent rgba(40, 44, 53, 0.06) transparent transparent;
        border-style: solid;
        border-width: 12px 12px 12px 0;
      }

      .card-carousel-cards .card-carousel--card--footer p:nth-of-type(2):after {
        content: "";
        position: absolute;
        top: 10px;
        left: -1px;
        float: left;
        width: 4px;
        height: 4px;
        border-radius: 2px;
        background: white;
        box-shadow: -0px -0px 0px #004977;
      }

      h1 {
        font-size: 3.6em;
        font-weight: 100;
        text-align: center;
        margin-bottom: 0;
        color: #42b883;
      }

    }

  }


  .couPreview_top {
    width: 100%;
    height: 120px;
    // background-color: aqua;
  }

  .couPreview_middle {
    width: 100%;
    height: 480px;
    margin-top: 30px;
    //padding-left: 10px;
    //padding-right: 10px;
    // background-color: antiquewhite;
    display: flex;
    justify-content: space-between;
    align-items: center;

    .couPreview_middle_left {
      width: 40%;
      height: 500px;
      margin-left: 10px;
      // box-shadow: 0 4px 15px 0 rgba(40, 44, 53, 0.06), 0 2px 2px 0 rgba(40, 44, 53, 0.08);
      // background-color: palegoldenrod;

      .carousel_swipe {
        position: relative;
        width: 100%;
        height: 400px;
        overflow: hidden;
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 15px 0 rgba(40, 44, 53, 0.06), 0 2px 2px 0 rgba(40, 44, 53, 0.08);
      }

      .swipe_list {
        width: 100%;
        height: 100%;
      }

      .swipe_list li {
        position: absolute;
        width: 100%;
        height: 100%;
        top: 0px;
        left: 0px;

      }

      .swipe_list li a {
        width: 100%;
        height: 100%;
      }

      .swipe_list li img {
        width: 100%;
        height: 100%;
      }

      .swipe_dian {
        position: absolute;
        z-index: 90;
        bottom: 0;
        height: 60px;
        text-align: center;
        font-size: 0;
        right: 10px;
      }

      .swipe_dian span {
        display: inline-block;
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin: 26px 5px;
        background-color: #5f5853;
        cursor: pointer;
      }

      .swipe_dian .active {
        background-color: #ffffff;
      }

      .swipe_title {
        position: absolute;
        line-height: 60px;
        height: 60px;
        width: 76%;
        z-index: 20;
        text-align: left;
        color: white;
        font-size: 18px;
        margin-left: 10px;
        bottom: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap
      }

      .swipe_bottom {
        position: absolute;
        width: 100%;
        height: 60px;
        bottom: 0;
        background-color: #3a2113;
        opacity: 0.5;
        z-index: 10;
      }

      .swipe_control {
        position: absolute;
        z-index: 10;
        top: 180px;
        width: 100%;
        height: 30px;
        padding: 0 0px;
        display: flex;
        justify-content: space-between;
        cursor: pointer;
      }

      .swipe_control .prev,
      .swipe_control .next {
        text-align: center;
        line-height: 30px;
        margin-right: 10px;
        margin-left: 10px;
        width: 30px;
        height: 30px;
        border-radius: 50%;
        background-color: #190f0b;
        color: white;
        opacity: 0.5;
      }

      .swipe_control .prev:hover {
        color: #cccccc;
        font-weight: bold;
        opacity: 1;
      }

      .swipe_control .next:hover {
        color: #cccccc;
        font-weight: bold;
        opacity: 0.7;
      }
    }

    .couPreview_middle_rigth {
      width: 56%;
      height: 500px;
      margin-right: 2%;
      // background-color: aliceblue;
      display: flex;
      flex-direction: column;

      .couPreview_middle_rigth_main {
        width: 100%;
        height: 450px;

        // background-color: pink;
        .couPreview_middle_rigth_main_list {
          width: 100%;
          display: flex;
          justify-content: space-around;
          flex-wrap: wrap;
          // justify-content: center;
          // align-items: center;

          .couPreview_middle_rigth_main_list_item {
            width: 31%;
            height: 210px;
            // margin-right: 17px;
            // margin-bottom: 25px;
            // background-color: palegoldenrod;
            display: flex;
            flex-direction: column;
            // margin: 0 10px;
            cursor: pointer;
            box-shadow: 0 4px 15px 0 rgba(40, 44, 53, 0.06), 0 2px 2px 0 rgba(40, 44, 53, 0.08);
            //box-shadow: 0 0px 8px 0 rgba(0, 0, 0, .12);
            background-color: #fff;
            // border-radius: 4px;
            z-index: 3;
            margin-bottom: 20px;
            border-radius: 10px;

            .couPreview_middle_rigth_main_list_item_info {
              width: 100%;
              display: flex;
              flex-direction: column;

              .courseName {
                font-weight: bold;
                padding-left: 10px;
                font-size: 16px;
                height: 30px;
                line-height: 30px;
                padding-top: 5px;
              }

              span {
                display: block;
                width: 100%;
                height: 20px;
                padding-left: 10px;
                font-size: 12px;
                font-weight: bold;

              }
            }

            .couPreview_middle_rigth_main_list_item_img {
              width: 100%;
              height: 135px;
              border-radius: 10px;

              img {
                width: 100%;
                height: 100%;
                //border-radius: 10px;
                border-top-left-radius: 10px;
                border-top-right-radius: 10px;
              }
              img:hover{
                opacity: 0.5;
              }
            }
          }
        }
      }

      .couPreview_middle_rigth_top {
        width: 100%;
        height: 50px;
        // background-color: aqua;
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }
    }
  }

}
</style>
