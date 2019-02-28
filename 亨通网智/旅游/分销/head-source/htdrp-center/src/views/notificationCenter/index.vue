<template>
  <div>
    <div class="container">
      <div class="searchBox">
        <el-input style="width:200px"
                  v-model="param.params.content"
                  placeholder="消息内容关键词"></el-input>
        <button class="btn"
                @click="searchData(1)">搜索</button>
      </div>
      <el-tabs v-model="param.params.messageType"
               type="card"
               @tab-click="searchData(1)">

        <el-tab-pane label="全部"
                     name="0">
        </el-tab-pane>
        <el-tab-pane label="系统通知"
                     name="1">
        </el-tab-pane>
        <div class="list">
          <div class="item"
               v-for="(item,index) in list"
               :key="index">
            <div class="item-header">
              <i v-if="!item.read_state"
                 class="iconfont icon-yuandianxiao"></i>
              <span class="title"
                    @click="readNotification(item.id,item.read_state)">{{item.title}}</span>
              <span class="time">{{item.create_time}}</span>
            </div>
            <div class="item-body">
              <p>{{item.content}}</p>
            </div>
          </div>
        </div>
        <div v-if="sw"
             class="text-center">已经到底了</div>
      </el-tabs>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      sw: true, // 数据是否已经到底的标志，true表示到底了
      param: {
        params: {
          content: "", // 搜索内容
          messageType: "0"
        },
        page: 1,
        size: 10
      },
      list: []
    };
  },
  mounted() {
    this.searchData();
    window.addEventListener("scroll", this.loadMore);
  },

  beforeDestroy() {
    // 移除滚动事件
    window.removeEventListener("scroll", this.load);
  },

  methods: {
    /**
     * 加载数据
     */
    async searchData(page = "") {
      if (page) {
        this.param.page = page;
        this.list = [];
      }
      let result = await this.$axios.post("/notice/qeuryPage", this.param);
      if (result.data.code == "1001") {
        this.list.push(...result.data.datas.content);
        if (result.data.datas.content.length < this.param.size) {
          this.sw = true;
        } else this.sw = false;
      }
    },
    /**
     * 滚动加载更多
     */
    loadMore() {
      let scrollTop =
        document.documentElement.scrollTop ||
        window.pageYOffset ||
        document.body.scrollTop;
      if (scrollTop + window.innerHeight >= document.body.offsetHeight) {
        if (!this.sw) {
          this.sw = true;
          this.param.page = Number(this.param.page) + 1;
          this.searchData();
        }
      }
    },

    /**
     * 点击阅读通知事件
     * @param id
     * @param status
     */
    async readNotification(id, status) {
      // 将消息置为已读状态
      if (!status) {
        let result = await this.$axios.post(
          "/notice/updateNotice",
          this.buildFormData({
            ids: [id]
          })
        );
      }
      this.$router.push("notificationDetail?id=" + id);
    }
  }
};
</script>
<style lang="scss" scoped>
.container {
  position: relative;
  min-height: 778px;
  box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
  border-radius: 3px;
  background: #ffffff;
  box-sizing: border-box;
  .searchBox {
    position: absolute;
    top: 15px;
    right: 10px;
    z-index: 1000;
    .el-input {
      /deep/ .el-input__inner {
        -webkit-appearance: none;
        background-color: #fff;
        padding: 0 10px;
        height: 36px;
        line-height: 36px;
        font-size: 14px;
        border-radius: 3px;
        border: 1px solid #e5e5e5;
        color: #000;
      }
    }
  }
  .el-tabs {
    padding-top: 15px;
    background: #fff;
    /deep/ .el-tabs__content {
      padding: 0 20px 20px;
    }
    /deep/ .el-tabs__item {
      width: 140px;
      text-align: center;
    }
    /deep/ .el-tabs__nav.is-top {
      transform: translateX(40px) !important;
      margin-left: -20px;
      .el-tabs__item.is-top.is-active {
        color: #1e66dc;
      }
    }
  }
  .list {
    .item {
      padding: 31px 0 0;
      width: 100%;
      font-size: 14px;
      color: #000000;
      line-height: 1;
      .item-header {
        position: relative;
        padding-left: 20px;
        .icon-yuandianxiao {
          position: absolute;
          left: 0;
          color: #e54545;
          font-size: 18px;
        }
        span {
          display: inline-block;
          &.title {
            margin-right: 20px;
            cursor: pointer;
          }
          &.time {
            font-size: 12px;
            color: #888888;
          }
        }
      }
      .item-body {
        margin-left: 20px;
        padding: 18px 20px 31px 0;
        width: 100%;
        border-bottom: 1px solid #e5e5e5;
        p {
          width: 1355px;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
      // &:last-child {
      //   .item-body {
      //     border-bottom: none;
      //   }
      // }
    }
  }
  .text-center {
    text-align: center;
    font-size: 14px;
    color: #888888;
    line-height: 1.8;
  }
}
</style>


