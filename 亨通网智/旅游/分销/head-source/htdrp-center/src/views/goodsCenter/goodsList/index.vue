<template>
  <div class="goodsList">
    <el-tabs v-model="activeName"
             class="boxshadow"
             type="card">
      <el-tab-pane label="景区门票"
                   name="ticket">
        <tab-ticket v-if="activeName=='ticket'"
                    :activeName="activeName"></tab-ticket>
      </el-tab-pane>
      <el-tab-pane label="套票"
                   name="ticketGroup">
        <tab-ticket v-if="activeName=='ticketGroup'"
                    :activeName="activeName"></tab-ticket>
      </el-tab-pane>
    </el-tabs>

  </div>
</template>

<script>
import ticketTab from "./ticketTab.vue";
export default {
  components: { "tab-ticket": ticketTab },
  data() {
    return {
      activeName: "ticket"
    };
  },
  methods: {}
};
</script>

<style lang="scss" scoped>
@import "../../../assets/css/childPage.css";
.el-tabs {
  padding-top: 15px;
  background: #fff;
  /deep/ .el-tabs__content {
    padding: 0 20px;
  }
  /deep/ .el-tabs__item{
    width: 140px;
    text-align: center;
  }
  /deep/ .el-tabs__nav.is-top {
    transform: translateX(40px) !important;
    .el-tabs__item.is-top.is-active {
      color: #1e66dc;
    }
  }
}
// .el-tabs
</style>
