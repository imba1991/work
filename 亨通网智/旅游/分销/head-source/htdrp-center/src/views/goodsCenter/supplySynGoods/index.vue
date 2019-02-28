<template>
  <div class="supply">
    <div class="commomItem supplyDetail">
      <div class="itemLogo">
        <img src="../../../assets/images/logo.png" alt>
      </div>
      <div class="interfaceName text-lg">{{interfaceData.name}}</div>
      <div class="interfaceContent text-lightBlack">{{interfaceData.content}}</div>
      <div class="updateTime text-gray text-right">最新更新时间： {{interfaceData.time}}</div>
    </div>
    <div class="commomItem">
      <div class="searchBar">
        <el-button type="primary" @click="synchronousAll()">同步全部商品</el-button>
        <div class="inline-block right">
          <el-input v-model="searchData.id" placeholder="商品id"></el-input>
          <el-input v-model="searchData.name" placeholder="商品名称"></el-input>
          <el-button type="primary" @click="search()">搜索</el-button>
        </div>
        <div class="clearfix"></div>
      </div>
      <el-table :data="goodsData" style="width: 100%">
        <el-table-column prop="id" label="商品id"></el-table-column>
        <el-table-column label="【票型】商品名称" width="500">
          <template slot-scope="scope">
            <div class="text-left primary pointer"
                 @click="$router.push('/goodsCenter/goodsList/detail?id=' + scope.row.id)"
                >【{{scope.row.type}}】{{scope.row.name}}</div>
            <div class="text-left">商品来源：{{scope.row.source}}</div>
          </template>
        </el-table-column>
        <el-table-column label="建议售价">
          <template slot-scope="scope">
            {{parseFloat(scope.row.price).toFixed(2)}}
          </template>
        </el-table-column>
        <el-table-column prop="validPeriod" label="销售有效期"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="synchronous(scope.row)">同步</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @size-change="handleSizeChange"
                   @current-change="handleCurrentChange"
                   :current-page="searchData.page"
                   :page-sizes="[10,20, 40, 80]"
                   :page-size="searchData.size"
                   layout="total, sizes, prev, pager, next, jumper"
                   :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      interfaceData: {
        name: "驴妈妈",
        content: "专做鸽子窝",
        time: "2018-09-08 15:50:20"
      },
      total: 0,
      searchData: {
        page: 1,
        size: 10,
        id: "",
        name: ""
      },
      goodsData: [
        {
          id: "1231232",
          type: "成人票",
          name: "泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
        {
          id: "1231232",
          type: "成人票",
          name: "泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
        {
          id: "1231232",
          type: "成人票",
          name: "泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
        {
          id: "1231232",
          type: "成人票",
          name: "泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        }
      ]
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    synchronous(data) {
      let _this = this;

      _this.$message.success("同步成功")
    },
    handleSizeChange(val) {
      this.searchData.size = val;
      this.search();
    },
    handleCurrentChange(val) {
      this.searchData.page = val;
      this.search();
    },
    search(){
      
    },
    /**
     * 同步全部商品
     */
    synchronousAll(){
      
    }
  }
};
</script>
<style lang="scss" scoped>
@import url("../../../assets/css/childPage.css");
.supplyDetail {
  display: flex;
  justify-content: center;
  align-items: center;
  .itemLogo {
    width: 10%;
    img {
      max-width: 50px;
    }
  }
  .interfaceName {
    width: 12%;
  }
  .interfaceContent {
    width: 62%;
  }
  .updateTime {
    width: 16%;
  }
}
.searchBar {
  margin: 10px 0;
  .el-input {
    width: 160px;
    margin-right: 10px;
  }
}
</style>