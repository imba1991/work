<template>
  <div class="supplyTab">
    <div class="searchBar">
      <el-button type="primary">申请合作</el-button>
      <div>
        <el-input placeholder="商户id" v-model="searchData.id"></el-input>
        <el-input placeholder="商户主体" v-model="searchData.name"></el-input>
        <el-button type="primary" @click="search()">搜索</el-button>
      </div>
    </div>
    <el-table :data="merchantData" style="width: 100%">
      <el-table-column label="序号" type="index"></el-table-column>
      <el-table-column prop="id" label="商品id"></el-table-column>
      <el-table-column prop="name" label="商户主体"></el-table-column>
      <el-table-column label="预存款余额">
        <template slot-scope="scope">￥{{parseFloat(scope.row.money).toFixed(2)}}</template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button type="text" @click="deposit(scope.row)">预存款</el-button>
          <el-button type="text" @click="deleteMerchant(scope.row)">删除</el-button>
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
        id: "",
        name: "",
        page: 1,
        size: 10,
      },
      merchantData: [
        {
          id: "4651321321",
          name: "8765431232",
          money: "25300",
          company: "成都亨通"
        },{
          id: "4651321321",
          name: "8765431232",
          money: "25300",
          company: "亨通网智"
        }
      ],
    }
  },
  mounted(){
    this.search();
  },
  methods:{
    search(){},

    /**
     * 预存款
     */
    deposit(data){

    },

    /**
     * 删除
     */
    deleteMerchant(data){
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确认删除该账户？<br>如果删除，则默认与【'
        + data.company +'】解除合作。并同时删除相关产品，及相关配置。<br>如果想要重新建立合作关系，需重新申请。', 
        '提示', {
          dangerouslyUseHTMLString: true,
          distinguishCancelAndClose: true,
          confirmButtonText: '删除',
          cancelButtonText: '取消',
          cancelButtonClass: 'button',
        }).then(() => {
          
        })
        .catch(action => {
          
        });
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
  }
}
</script>
<style lang="scss" scoped>
.searchBar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  .el-input {
    width: 160px;
    margin-right: 8px;
  }
}
</style>