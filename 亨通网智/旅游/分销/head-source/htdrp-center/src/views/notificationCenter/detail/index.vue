<template>
  <div>
    <div class="container">
      <p>{{detail.title}}</p>
      <span>发布时间：{{detail.createTime}}</span>
      <div class="content">
        {{detail.content}}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      detail: {}
    };
  },
  mounted() {
    let id = window.location.hash.substr(window.location.hash.indexOf("?") + 4); // 获取url上的id
    if (id) this.searchDetail(id);
  },
  methods: {
    /**
     * 查询详情
     * @param id
     */
    async searchDetail(id) {
      let result = await this.$axios.post(
        "/notice/findOneNotice",
        this.buildFormData({ id: id })
      );
      if (result.data.code == "1001") {
        this.detail = result.data.datas;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  width: 100%;
  padding: 20px;
  background-color: #ffffff;
  border: solid 1px #e5e5e5;
  p {
    margin: 20px 0;
    font-size: 16px;
    color: #000000;
    line-height: 1;
  }
  span {
    color: #888888;
    font-size: 12px;
  }
  .content {
    margin: 20px 20px 20px 0;
    font-size: 14px;
    color: #000000;
    line-height: 1.6;
  }
}
</style>

