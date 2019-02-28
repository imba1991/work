<template>
  <div class="goodsDetail">
    <div>
      <!-- 页面上部分 -->
      <div class="detailTop commomItem">
        <div class="topLeft inline-block">
          <el-carousel :interval="5000"
                       arrow="always">
            <el-carousel-item v-for="item in imgUrls"
                              :key="item">
              <img :src="item"
                   alt>
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="topRight inline-block">
          <p class="text-bold title">{{pageData.resourceName}}</p>
          <p class="text-lightBlack"><span class="">资 源 id</span>：{{pageData.id}}</p>
          <p class="text-lightBlack"><span class="">商品来源</span>：{{pageData.sourceName}}</p>
          <p class="text-lightBlack">
            <span>景区等级</span>:
            <span>{{getViewStar(pageData.level)}}</span>
          </p>
          <p class="text-lightBlack">
            <span>景区电话</span>：
            <span>{{pageData.phone}}</span>
          </p>
          <p class="text-lightBlack">
            <span>景区地址</span>：
            <span class="text-lg">{{pageData.address}}</span>
          </p>
        </div>
      </div>
      <!-- 页面下部分 -->

      <div class="detailBottom commomItem">
        <img src="./u902.png"
             alt="">
      </div>
    </div>
    <p style="text-align:center;">

      <el-button type="primary"
                 @click="$router.push('/goodsCenter/goodsList/detail/goodsDetail')">返回</el-button>
    </p>
  </div>
</template>
<script>
export default {
  data() {
    return {
      resourceID: "",
      pageData: {},
      imgUrls: [
        "https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/tuhuchunjie/270170.jpg?t=1549816303"
      ],
      activeName: "first"
    };
  },
  mounted() {
    if (location.href.includes("?id=")) {
      this.resourceID = location.href.split("?id=")[1];
    } else {
      this.resourceID = sessionStorage.getItem("resourceID");
    }
    this.getPageData();
  },
  methods: {
    async getPageData() {
      let result = await this.$axios.post(
        "/product/resource/selectByPrimaryKey",
        this.buildFormData({ id: this.resourceID })
      );
      if (result.data.code == "1001") {
        this.pageData = result.data.datas;
      }
    },
    getViewStar(num) {
      let str = "";
      for (let i = 0; i < num; i++) {
        str += "A";
      }
      return str;
    },
    handleClick(tab, event) {
      console.log(tab, event);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../../../assets/css/childPage.css";

.commomItem {
  padding: 0;
}
.detailTop {
  display: flex;
  padding: 10px 20px;
  min-width: 1000px;
  .topLeft {
    width: 30%;
    img {
      width: 100%;
    }
  }
  .topRight {
    width: 70%;
    padding: 8px 20px;
    .title {
      margin-bottom: 18px;
    }
    p {
      margin-bottom: 7px;
    }
    .tagLine {
      margin: 15px 0;
    }
    .tag {
      border: 1px solid #444;
      border-radius: 15px;
      padding: 2px 15px;
      margin-right: 10px;
    }
    .buyBtn {
      margin-top: 13px;
      padding: 8px 26px;
      font-size: 15px;
    }
  }
}
.detailBottom {
  padding: 8px 20px;
  min-width: 1000px;
  img {
    width: 900px;
  }
}
</style>
