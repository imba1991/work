<template>
  <div>
    <total-info></total-info>
    <detail-table></detail-table>
  </div>
</template>

<script>
import totalInfo from "./totalInfo.vue";
import detailTable from "./detailTable.vue";
export default {
  components: { "total-info": totalInfo, "detail-table": detailTable },
  data() {
    return {};
  },
  mounted() {
    console.log(this.$route.meta.type);//以此来区别页面是买进还是卖出
  }
};
</script>

<style>
</style>
