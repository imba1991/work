<template>
  <div>
    <div class="container">
      <div class="searchRow">
        <el-date-picker v-model="param.time"
                        type="datetimerange"
                        range-separator="至"
                        start-placeholder="开始时间"
                        end-placeholder="结束时间">
        </el-date-picker>
        <button class="btn ml-20"
                @click="search(1)">搜索</button>
      </div>
      <el-table :data="list"
                height="250"
                style="width: 100%">
        <el-table-column label="序号"
                         type="index">
        </el-table-column>
        <el-table-column prop="date"
                         label="操作人">
        </el-table-column>
        <el-table-column prop="name"
                         label="操作时间">
        </el-table-column>
        <el-table-column prop="address"
                         label="操作内容">
        </el-table-column>
      </el-table>
      <div class="text-center">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="param.page"
                       :page-sizes="[5, 10, 20, 30, 40]"
                       :page-size="param.size"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="total"></el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      total: 0,
      param: {
        page: 1,
        size: 10,
        time: ""
      },
      list: []
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    /**
     * 搜索数据
     * @param page
     */
    async search(page = "") {
      if (page) this.param.page = page;
      let result = await this.$axios.post(
        "/user/base/findReceiptRecord",
        this.buildFormData(this.param)
      );
      if (result.data.code == "1001") {
        this.list = result.data.datas;
        this.total = result.data.total;
      }
    },

    /**
     * 修改显示条数
     * @param {*} val
     */
    handleSizeChange(val) {
      this.param.size = val;
      this.search();
    },

    /**
     * 修改当前页数
     * @param {*} val
     */
    handleCurrentChange(val) {
      this.param.page = val;
      this.search();
    }
  }
};
</script>
<style scoped>
.el-table >>> thead,
.el-table >>> th {
  color: #000;
  background: #e5e5e5;
}
.container {
  padding: 20px;
  background: #ffffff;
}
.searchRow {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding-bottom: 10px;
}
.el-table {
  border: 1px solid #e5e5e5;
  border-radius: 3px;
}
.ml-20 {
  margin-left: 20px;
}
.el-input__inner {
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
</style>
