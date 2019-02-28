<template>
  <div class="distribute">
    <div class="commomItem">
      <el-tabs v-model="activeName"
             class="boxshadow"
             type="card">
      <el-tab-pane label="分销合作商" name="distribute">
        <tab-distribute v-if="activeName=='distribute'"
                    :activeName="activeName"></tab-distribute>
      </el-tab-pane>
      <el-tab-pane label="合作供应商" name="supply">
        <tab-supply v-if="activeName=='supply'"
                    :activeName="activeName"></tab-supply>
      </el-tab-pane>
      <el-tab-pane label="商户审核" name="audit">
        <tab-audit v-if="activeName=='audit'"
                    :activeName="activeName"></tab-audit>
      </el-tab-pane>
    </el-tabs>
    </div>
  </div>
</template>
<script>
import distributeTab from "./distributeTab.vue";
import supplyTab from "./supplyTab.vue";
import auditTab from "./auditTab.vue";
export default {
  components: { 
      "tab-distribute": distributeTab,
      "tab-supply": supplyTab,
      "tab-audit": auditTab,
    },
  data() {
    return {
      activeName: "distribute",
    };
  },
  methods: {}
};
</script>
<style lang="scss" scoped>
// @import url('../../../assets/css/childPage.css');
.el-tabs {
  padding-top: 15px;
  background: #fff;
  /deep/ .el-tabs__content {
    padding: 0 20px 20px;
  }
  /deep/ .el-tabs__item{
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
</style>
