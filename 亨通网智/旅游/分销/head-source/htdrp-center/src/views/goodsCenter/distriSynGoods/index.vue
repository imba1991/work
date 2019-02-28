<template>
  <div class="distribution">
    <div class="commomItem distriDetail">
      <div class="itemLogo">
        <img src="../../../assets/images/logo.png" alt>
      </div>
      <div class="interfaceName text-lg">{{interfaceData.name}}</div>
      <div class="interfaceContent text-lightBlack">{{interfaceData.content}}</div>
      <div class="updateTime text-gray text-right">最新更新时间： {{interfaceData.time}}</div>
    </div>
    <div class="commomItem">
      <div class="searchBar">
        <el-button type="primary" @click="chooseVisible = true">选择商品</el-button>
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
            <div
              class="text-left primary pointer"
              @click="$router.push('/goodsCenter/goodsList/detail?id=' + scope.row.id)"
            >【{{scope.row.type}}】{{scope.row.name}}</div>
            <div class="text-left">商品来源：{{scope.row.source}}</div>
          </template>
        </el-table-column>
        <el-table-column label="建议售价">
          <template slot-scope="scope">{{parseFloat(scope.row.price).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column prop="validPeriod" label="销售有效期"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="synchronous(scope.row)">同步</el-button>
            <el-button type="text" @click="unbind(scope.row)">解绑</el-button>
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

      <!-- 选择商品 -->
      <el-dialog title="选择商品" :visible.sync="chooseVisible" width="60%" :before-close="handleClose">
        <div class="content">
          <!-- 条件查询 -->
          <div class="dialogSearch">
            <el-input placeholder="商品id" v-model="dialogSearchData.id"></el-input>
            <el-input placeholder="商品名称" v-model="dialogSearchData.name"></el-input>
            <el-button type="primary" @click="dialogSearch()">搜索</el-button>
          </div>
          <!-- 可选择商品 -->
          <el-table :data="dialogTableData"
                    tooltip-effect="dark"
                    style="width: 100%"
                    @selection-change="handleSelectionChange">
            <el-table-column type="selection" label="全选" width="35"></el-table-column>
            <el-table-column prop="id" label="商品id"></el-table-column>
            <el-table-column label="【票型】商品名称" width="400">
              <template slot-scope="scope">
                <div class="dialogName">【{{scope.row.type}}】{{scope.row.name}} 商品来源：{{scope.row.source}}</div>
              </template>
            </el-table-column>
            <el-table-column prop="supplier" class-name="supplier" label="供应商" width="200"></el-table-column>
            <el-table-column label="售价">
              <template slot-scope="scope">{{parseFloat(scope.row.price).toFixed(2)}}</template>
            </el-table-column>
            <el-table-column prop="validPeriod" label="销售有效期" width="170"></el-table-column>
          </el-table>
          <el-pagination @size-change="handleDialogSizeChange"
                        @current-change="handleDialogCurrentChange"
                        :current-page="dialogSearchData.page"
                        :page-sizes="[5,8,10]"
                        :page-size="dialogSearchData.size"
                        layout="total, sizes, prev, pager, next, jumper"
                        :total="dialogTotal"
                    ></el-pagination>
          <!-- 已选择商品 -->
          <p class="el-dialog__title">已选择 ({{choseData.length}})</p>
          <div class="choose">
            <div class="chooseItem" v-for="(item, index) in choseData" :key="index">
              <div class="discribe">【{{item.type}}】{{item.name}}</div>
              <div class="id text-center">（{{item.id}}）</div>
              <div class="price text-center">售价 {{parseFloat(item.price).toFixed(2)}}</div>
              <div class="timePeriod text-center">有效期:{{item.validPeriod}}</div>
              <el-button icon="el-icon-minus"
                   type="danger"
                   size="mini"
                   class="removeBtn"
                   circle
                   @click="removeItem(index)"></el-button>
            </div>
          </div>
        </div>
        <div slot="footer" class="dialog-footer text-center">
          <el-button @click="chooseVisible = false">取消</el-button>
          <el-button type="primary" @click="chooseVisible = false">同步</el-button>
        </div>
      </el-dialog>
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
      ],
      chooseVisible: false,
      dialogTotal: 0,
      dialogSearchData: {
        id: "",
        name: "",
        page: 1,
        size: 5
      },
      dialogTableData: [
        {
          id: "12444444",
          type: "成人票",
          name: "001泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          supplier: "九寨沟（186****1236）mmmmmmmmm",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
        {
          id: "1252222",
          type: "老人票",
          name: "002泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          supplier: "九寨沟（186****1236）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
        {
          id: "1231232",
          type: "儿童票",
          name: "003泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）",
          source: "同步-驴妈妈（1234552）",
          supplier: "九寨沟（186****1236）",
          price: "124",
          validPeriod: "2015/02/16~2018/10/30"
        },
      ],
      choseData: [],
    };
  },
  mounted() {
    this.search();
  },
  methods: {
    synchronous(data) {
      let _this = this;

      _this.$message.success("同步成功");
    },

    /**
     * 页面上分页事件
     */
    handleSizeChange(val) {
      this.searchData.size = val;
      this.search();
    },
    handleCurrentChange(val) {
      this.searchData.page = val;
      this.search();
    },

    search() {},
    /**
     * 同步全部商品
     */
    synchronousAll(rowData) {
      let _this = this;
    },

    /**
     * 解绑
     */
    unbind(rowData) {
      let _this = this;

      _this
        .$confirm(
          '<i class="el-icon-warning" style="margin-left: -17px;"></i> 确定是否解绑？解绑之后，该商品将不能在该接口进行售卖。',
          "提示",
          {
            dangerouslyUseHTMLString: true,
            distinguishCancelAndClose: true,
            confirmButtonText: "解绑",
            cancelButtonText: "取消",
            cancelButtonClass: "button"
          }
        )
        .then(() => {})
        .catch(action => {});
    },
    handleClose() {
      this.chooseVisible = false;
    },

    /**
     * 弹窗checkbox选择事件
     */
    handleSelectionChange(val) {
      this.choseData = val;
    },

    /**
     * remove 已选择的商品
     */
    removeItem(index){
      this.choseData.splice(index, 1);
    },

    /**
     * 弹窗分页事件
     */
    handleDialogSizeChange(val){
      this.dialogSearchData.size = val;
      this.dialogSearch();
    },
    handleDialogCurrentChange(val) {
      this.dialogSearchData.page = val;
      this.dialogSearch();
    },
    dialogSearch(){},
  }
};
</script>
<style lang="scss" scoped>
@import url("../../../assets/css/childPage.css");
.distriDetail {
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
.dialogSearch {
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  margin-bottom: 10px;
  .el-input {
    width: 150px;
    margin-right: 10px;
  }
}
.dialogName, 
/deep/ .supplier .cell{
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.choose {
  border: 1px solid #e5e5e5;
  margin-top: 8px;
  padding: 15px;
  max-height: 200px;
  overflow-y: auto;
  .chooseItem {
    display: flex;
    justify-content: center;
    background: #f1f1f1;
    padding: 8px 10px;
    font-size: 12px;
    margin-bottom: 15px;
    width: 95%;
    .discribe{
      width: 42%;
      overflow: hidden;
      text-overflow: ellipsis;
      white-space: nowrap;
    }
    .id {
      width: 14%;
    }
    .price {
      width: 14%;
    }
    .timePeriod {
      width: 30%;
    }
    .removeBtn{
      position: relative;
      right: -18px;
      top: -17px;
      padding: 1px;
    }
  }
}
</style>