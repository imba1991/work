<template>
  <div class="auditTab">
    <div class="searchBar">
      <el-date-picker v-model="searchData.dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期">
                    </el-date-picker>
      <el-input placeholder="商户主体" v-model="searchData.name"></el-input>
      <el-button type="primary" @click="search()">搜索</el-button>
    </div>
    <el-table :data="auditData" style="width: 100%">
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column prop="id" label="商品id"></el-table-column>
      <el-table-column prop="name" label="商户主体"></el-table-column>
      <el-table-column prop="submitTime" label="申请提交时间"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="refuse(scope.row)">拒绝合作</el-button>
          <el-button type="text" @click="agree(scope.row)">同意合作</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="searchData.page"
      :page-sizes="[10,20, 40, 80]"
      :page-size="searchData.size"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    ></el-pagination>
  </div>
</template>
<script>
import _clone from "lodash/clone";
export default {
  props: ["activeName"],
  data(){
    return{
      total: 0,
      searchData: {
        dateRange: "",
        name: "",
        page: 1,
        size: 10,
      },
      auditData: [{
        id: "213213",
        name: "45632132154",
        submitTime: "2019-09-01 12:30:00",
        group: "自由分组",
      }]
    }
  },
  mounted(){
    this.search();
  },
  methods:{
    search(){

    },

    /**
     * 分页点击事件
     */
    handleSizeChange(val) {
      this.searchData.size = val;
      this.search();
    },
    handleCurrentChange(val) {
      this.searchData.page = val;
      this.search();
    },

    /**
     * 拒绝合作
     */
    refuse(data){
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定拒绝合作？', 
        '提示', {
          dangerouslyUseHTMLString: true,
          distinguishCancelAndClose: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          cancelButtonClass: 'button',
        }).then(() => {
          
        })
        .catch(action => {
          
        });
    },

    /**
     * 同意合作
     */
    agree(data){
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定同意合作？同意合作后，该商户将进入分销商【'+ data.group +'】中。', 
        '提示', {
          dangerouslyUseHTMLString: true,
          distinguishCancelAndClose: true,
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          cancelButtonClass: 'button',
        }).then(() => {
          
        })
        .catch(action => {
          
        });
    },
  }
}
</script>
<style lang="scss" scoped>
.searchBar {
  margin-bottom: 10px;
  text-align: right;
  .el-date-editor {
    width: 240px;
    margin-right: 8px;
  }
  .el-input {
    width: 160px;
    margin-right: 8px;
  }
}
</style>