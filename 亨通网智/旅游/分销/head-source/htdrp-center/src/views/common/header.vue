<template>
  <div>
    <div class="header">
      <div class="sys_name">{{userInfo.companyName}}</div>
      <div class="menu">
        <div :class="activePage==item.name?'nav active':'nav'"
             v-for="(item,index) of navData"
             @click="changeView(item)"
             :key="index">
          {{item.name}}
        </div>
      </div>
      <div class="others">
        <i class="iconfont icon-xiaoxi"
           @click="$router.push('/notificationCenter')">
          <div class="badge">
            <span v-if="count > 0 && count <= 99">{{count}}</span>
            <span v-else-if="count>99">99+</span>
          </div>
        </i>
        <div class="userImg">
          <img v-if="userInfo.userPortrait"
               :src="userInfo.userPortrait"
               @click="$router.push('/userCenter')">
          <i v-else
             class="iconfont icon-shangchuantupian"
             @click="$router.push('/userCenter')"></i>
        </div>

        <span class="mr-10">{{userInfo.userName}}</span>
        <span @click="loginOut">退出</span>
      </div>
    </div>
    <el-breadcrumb v-if="breadcrumbData.length>0"
                   separator="/">
      <el-breadcrumb-item v-for="(item,index) of breadcrumbData"
                          :key="index"
                          :to="{ path: item.path }">{{item.name}}</el-breadcrumb-item>
    </el-breadcrumb>
    <router-view class="router-view"></router-view>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  data() {
    return {
      activePage: "",
      navData: [
        { name: "首页", path: "/" },
        { name: "商品中心", path: "/goodsCenter" },
        { name: "分销中心", path: "/distributionCenter" },
        { name: "交易中心", path: "/dealCenter" }
      ],
      userInfo: {
        companyName: "亨通分销系统",
        userPortrait: ""
      },
      breadcrumbData: [],
      count: 0
    };
  },
  computed: {
    // 监听userInfo的改变
    ...mapGetters(["getUserInfo"])
  },

  watch: {
    $route(val) {
      this.paintBreadCrumb();
    }
  },
  mounted() {
    this.changeView();
    this.paintBreadCrumb();
    // 获取用户未读消息的条数
    this.getCount();
    this.userInfo = this.$store.getters.getUserInfo;
  },
  methods: {
    changeView(item) {
      if (item) {
        this.activePage = item.name;
        this.$router.push(item.path);
      } else {
        let routes = this.$route.matched;
        for (let route of routes) {
          if (route.meta.name != undefined) {
            this.activePage = route.meta.name;
            break;
          }
        }
      }
    },
    paintBreadCrumb() {
      let arr = [];
      let routes = this.$route.matched;
      for (let item of routes) {
        if (item.meta.name != undefined) {
          arr.push({ name: item.meta.name, path: item.path });
        }
      }
      this.breadcrumbData = arr;
    },

    /**
     * 退出
     */
    loginOut() {
      this.$router.push({ path: "/login" });
      sessionStorage.clear();
    },

    /**
     * 获取用户未读消息的条数
     */
    async getCount() {
      let result = await this.$axios.post("/notice/getUnreadNoticeCount");
      if (result.data.code == "1001") {
        this.count = result.data.datas;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.el-breadcrumb /deep/ .el-breadcrumb__inner.is-link {
  color: #1e66dc;
}
.el-breadcrumb /deep/ .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #606266;
}
.header {
  width: 100%;
  min-width: 900px;
  height: 50px;
  line-height: 50px;
  background: #1e66dc;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  font-size: 14px;
  color: #fff;
  .sys_name {
    margin-left: 15px;
    font-family: HYGangYiTi-IEW;
    font-size: 18px;
  }
  .menu {
    width: 640px;
    display: flex;
    flex-direction: row;

    .nav {
      position: relative;
      padding-left: 44px;
      padding-right: 44px;
      line-height: 50px;
      font-size: 14.7px;
      font-weight: bold;
      color: #ccc;
    }
    .active.nav {
      font-size: 16px;
      color: #fff;
    }
    .active.nav:after {
      content: "";
      display: block;
      position: absolute;
      bottom: 0;
      left: calc(50% - 8px);
      border-width: 0 8px 8px;
      border-style: solid;
      border-color: transparent transparent #fff;
    }
  }
  .others {
    padding-right: 15px;
    font-size: 14px;
    .icon-xiaoxi {
      position: relative;
      margin-right: 24px;
      font-size: 24px;
      vertical-align: middle;
      .badge {
        position: absolute;
        display: inline-block;
        top: -18px;
        left: 16px;
        span {
          padding: 1px 1px;
          line-height: 12px;
          border-radius: 6px;
          font-size: 12px;
          background: #ff844e;
        }
      }
    }
    .userImg {
      display: inline-block;
      margin-right: 8px;
      width: 30px;
      height: 30px;
      border-radius: 50%;
      vertical-align: middle;
      background: #fff;
      line-height: 30px;
      text-align: center;
      overflow: hidden;
      img {
        width: 30px;
        height: 30px;
      }
      .icon-shangchuantupian {
        color: #333333;
        font-size: 24px;
      }
    }

    .mr-10 {
      margin-right: 10px;
    }
  }
}
.el-breadcrumb {
  line-height: 50px;
  padding-left: 15px;
  background: #f5f5f5;
}
.router-view {
  min-height: calc(100vh - 115px);
  padding: 15px;
  box-sizing: border-box;
  padding-top: 0px;
  background: rgb(245, 245, 245);
}
</style>
