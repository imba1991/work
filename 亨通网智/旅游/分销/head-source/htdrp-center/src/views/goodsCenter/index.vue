<template>
  <div class="goodsCenter">
    <div class="main">
      <div class="goodsTop commomItem boxShadow"
           @click="$router.push('/goodsCenter/goodsList/index')">
        <div class="itemTitle">商品列表</div>
        <div class="padding-15">
          <span class="trisection inline-block text-center border-right">现有商品总数 &nbsp; &nbsp;
            <span class="text-lg num">{{listMount.total}}</span> &nbsp; &nbsp; 个
          </span>
          <span class="trisection inline-block text-center text-gray">分销门票 &nbsp; &nbsp;
            <span class="text-lg num">{{listMount.synchronization}}</span> &nbsp; &nbsp; 个
          </span>
          <span class="trisection inline-block text-center text-gray">同步门票 &nbsp; &nbsp;
            <span class="text-lg num">{{listMount.selfBuild}}</span> &nbsp; &nbsp; 个
          </span>
        </div>
      </div>
      <div class="goodsBottom commomItem boxShadow">
        <div class="interfaceItem">
          <div class="itemTitle">
            供应接口
            &nbsp;
            <a class="titleLink primary pointer"
               @click="$router.push('/goodsCenter/newInterface')">开通新接口</a>
          </div>
          <div class="itemList"
               v-for="(item,index) in supplyData"
               :key="index">
            <div class="itemLogo inline-block text-center">
              <img :src="item.logoUrl" alt>
            </div>
            <div class="interfaceName inline-block">{{item.supplierName}}</div>
            <div class="interfaceContent inline-block text-lightBlack">{{item.supplierDes}}</div>
            <div class="updateTime inline-block text-gray">最新更新时间： {{item.updateTime}}</div>
            <div class="synchNum inline-block text-gray">已同步
              <span class="text-black">{{item.syncProductNum}}</span> 个
            </div>
            <div class="synchGoods inline-block">
              <el-button type="primary"
                         @click="$router.push('/goodsCenter/supplySynGoods')">同步商品</el-button>
            </div>
          </div>
        </div>
        <div class="interfaceItem">
          <div class="itemTitle">
            分销接口
            &nbsp;
            <a class="titleLink primary pointer"
               @click="$router.push('/goodsCenter/newInterface')">开通新接口</a>
          </div>
          <div class="itemList"
               v-for="(item,index) in distributeData"
               :key="index">
            <div class="itemLogo inline-block text-center">
              <img :src="item.logoUrl" alt>
            </div>
            <div class="interfaceName inline-block">{{item.distributerName}}</div>
            <div class="interfaceContent inline-block text-lightBlack">{{item.distributerDes}}</div>
            <div class="updateTime inline-block text-gray">最新更新时间： {{item.updateTime}}</div>
            <div class="synchNum inline-block text-gray">已同步
              <span class="text-black">{{item.syncProductNum}}</span> 个
            </div>
            <div class="synchGoods inline-block">
              <el-button type="primary"
                         @click="$router.push('/goodsCenter/distriSynGoods')">同步商品</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      listMount: {
        total: null,
        synchronization: null,
        selfBuild: null
      },
      supplyData: [],
      distributeData: [],
    };
  },
  mounted() {
    this.getListMountData();
    this.getSupplyData();
    this.getDistributeData();
  },
  methods: {
    async getListMountData() {
      let result = await this.$axios.post("/product/tickets/selectCountSource");
      if (result.data.code == "1001") {
        this.listMount = result.data.datas;
      }
    },

    /**
     * 获取供应接口数据
     */
    getSupplyData(){
      let _this = this;

      _this.$axios.post("/supplier/queryAvailableInstances")
        .then(result =>{
          if(result.data.code == "1001"){
            _this.supplyData = result.data.datas;
          }
        })
    },

    /**
     * 获取分销接口数据
     */
    async getDistributeData(){
      let result = await this.$axios.post("/ditributer/queryAvailableInstances");
      if(result.data.code == "1001"){
        this.distributeData = result.data.datas;
      }
    },
  }
};
</script>
<style lang="scss" scoped>
@import "../../assets/css/childPage.css";
.interfaceItem {
  margin-bottom: 60px;
}
.trisection{
  .num{
    color: #333;
  }
}
.itemList {
  border: 1px solid #ddd;
  border-radius: 3px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 15px;
  padding: 8px 0;
  .itemLogo {
    width: 10%;
    img {
      max-width: 50px;
    }
  }
  .interfaceName {
    width: 10%;
  }
  .interfaceContent {
    width: 50%;
    font-size: 12px;
  }
  .updateTime {
    width: 15%;
    font-size: 12px;
  }
  .synchNum {
    width: 8%;
    font-size: 12px;
  }
  .synchGoods {
    width: 5%;
  }
}
</style>
