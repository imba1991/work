<template>
  <div class="goodsBuy">
    <div class="commomItem">
      <div class="topLine">
        <span class="choose">已选择商品</span> &nbsp;
        <a href="#/goodsCenter/goodsList">更换商品</a>
      </div>
      <div class="topLine">
        <a href>泉州石龙谷森林游乐园大门票+项目联票优待票（老人/儿童/军人）</a>
      </div>
      <div class="topLine">
        <span>商品来源：</span>
        <span>同步-驴妈妈（1235621）</span>
      </div>
    </div>
    <div class="commomItem">
      <el-form ref="form" :model="form" label-width="90px">
        <el-form-item label="分销商名称">
          <el-autocomplete
            class="inline-input"
            value-key="name"
            v-model="form.account"
            :fetch-suggestions="querySearch"
            placeholder="请输入分销商账号（自己下单可不选）"
            :trigger-on-focus="false"
            @select="handleSelect"
          >
            <a class="pointer" slot="suffix" @click="handleIconClick">搜索</a>
            <template slot-scope="{ item }">
              <div class="name">{{ item.name }}</div>
            </template>
          </el-autocomplete> &nbsp;&nbsp;
          <a href>添加分销商</a>
        </el-form-item>
        <el-form-item label="消费日期">
          <el-date-picker v-model="form.date" type="date"></el-date-picker> &nbsp;&nbsp;
          <span class="text-lightBlack">供货价：</span><span class="text-red">￥210.00</span>
        </el-form-item>
        <el-form-item label="购买数量">
          <el-input-number
            v-model="form.buyNum"
            @change="handleNumChange"
            :min="1"
            :max="10"
            label="描述文字"
          ></el-input-number>
        </el-form-item>
        <el-form-item label="客人信息" class="touristInfo">
          <el-input class="name" v-model="form.name" placeholder="姓名"></el-input>
          <el-input class="numInput" v-model="form.phone" placeholder="手机号"></el-input>
          <el-input class="numInput" v-model="form.idCard" placeholder="身份证号码"></el-input>
        </el-form-item>
        <el-form-item label="支付方式" class="payType">
          <el-radio-group v-model="form.payType">
            <el-radio-button label="商户预存款" class="prepaid"></el-radio-button>
            <el-radio-button label="支付宝支付" class="alipay"></el-radio-button>
            <el-radio-button label="微信支付" class="wechat"></el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="合计">
          <span class="text-red text-lg">￥{{parseFloat(form.total).toFixed(2)}}</span>
        </el-form-item>
        <el-form-item>
          <el-button type="danger">支付</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="commomItem">
      <div class="rules">
        <div class="itemTitle">消费规则</div>
        <div class="ruleContent">
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
        </div>
      </div>
      <div class="rules"> 
        <div class="itemTitle">退票规则</div>
        <div class="ruleContent">
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
          <p><span>消费限制：</span><span>下单选择消费日期起12天可以消费</span></p>
        </div>
      </div>
      <div class="clearfix"></div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      input: "",
      restaurants: [],
      form: {
        account: "",
        date: "",
        buyNum: 1,
        name: "",
        phone: "",
        idCard: "",
        payType: "微信支付",
        total: 180.00,
      }
    };
  },
  mounted() {
    // this.restaurants = this.loadAll();
  },
  methods: {
    async querySearch(queryString, cb) {
      var chId = "";
      var user = {
        mer_id: "",
        ch_id: chId
      };

      //这里是从后台获取数据
      // await getMohu(queryString,user).then(res=>{
      // this.restaurants = res

      this.restaurants = [
        {
          name: "粤A05965D"
        },
        {
          name: "粤A02637D"
        },
        {
          name: "粤A02891D"
        }
      ];
      var restaurants = this.restaurants;
      var results = queryString ? this.searchData(queryString) : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
      // })
    },
    searchData(issue_content) {
      return this.restaurants.filter(function(product) {
        return Object.keys(product).some(function(key) {
          return (
            String(product[key])
              .toUpperCase()
              .indexOf(issue_content) > -1
          );
        });
      });
      return this.products;
    },
    handleSelect(item) {
      console.log(item);
    },
    handleIconClick(ev) {
      console.log(ev);
    },

    /**
     * 购买数量改变
     */
    handleNumChange(value) {
      console.log(value);
    }
    // querySearch(queryString, cb) {
    //   var restaurants = this.restaurants;
    //   var results = queryString
    //     ? restaurants.filter(this.createFilter(queryString))
    //     : restaurants;
    //   // 调用 callback 返回建议列表的数据
    //   cb(results);
    // },
    // createFilter(queryString) {
    //     debugger
    //   return restaurant => {
    //     return (
    //       restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    //     );
    //   };
    // },
    // loadAll() {
    //   return [
    //     { "value": "三全鲜食（北新泾店）", "address": "长宁区新渔路144号" },
    //     { "value": "Hot honey 首尔炸鸡（仙霞路）", "address": "上海市长宁区淞虹路661号" },
    //     { "value": "新旺角茶餐厅", "address": "上海市普陀区真北路988号创邑金沙谷6号楼113" },
    //     { "value": "泷千家(天山西路店)", "address": "天山西路438号" },
    //     { "value": "胖仙女纸杯蛋糕（上海凌空店）", "address": "上海市长宁区金钟路968号1幢18号楼一层商铺18-101" },
    //     { "value": "贡茶", "address": "上海市长宁区金钟路633号" },
    //     { "value": "豪大大香鸡排超级奶爸", "address": "上海市嘉定区曹安公路曹安路1685号" },
    //     { "value": "茶芝兰（奶茶，手抓饼）", "address": "上海市普陀区同普路1435号" },
    //     { "value": "十二泷町", "address": "上海市北翟路1444弄81号B幢-107" },
    //   ];
    // },
    // handleSelect(item) {
    //   console.log(item);
    // },
    // handleIconClick(ev) {
    //   console.log(ev);
    // }
  }
};
</script>
<style lang="scss" scoped>
@import "../../../assets/css/childPage.css";
.topLine {
  margin-top: 5px;
  margin-bottom: 10px;
  .choose {
    font-size: 15px;
  }
}
.my-autocomplete {
  li {
    line-height: normal;
    padding: 7px;

    .name {
      text-overflow: ellipsis;
      overflow: hidden;
    }
    .addr {
      font-size: 12px;
      color: #b4b4b4;
    }

    .highlighted .addr {
      color: #ddd;
    }
  }
}
.commomItem {
  .el-form{
    margin-top: 10px;
  }
  /deep/ .el-input__prefix {
    right: 5px;
    left: unset;
    color: #1e66dc;
  }
  /deep/ .el-input-number {
    margin-left: 50px;
    line-height: 36px;
    width: 160px;
    /deep/ .el-input-number__decrease {
      left: -50px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
      font-size: 15px;
      font-weight: 700;
      background: transparent;
    }
    /deep/ .el-input-number__increase {
      right: -50px;
      border-radius: 4px;
      border: 1px solid #dcdfe6;
      font-size: 15px;
      font-weight: 700;
      background: transparent;
    }
  }
  .touristInfo{
    .el-input{
      float: left;
      margin-right: 10px;
    }
    .name{
      width: 140px;
    }
    .numInput{
      width: 210px;
    }
  }
  .el-radio-button{
    margin-right: 10px;
  }
  /deep/ .el-radio-button__orig-radio:checked+.el-radio-button__inner{
    background: transparent;
  }
  .prepaid{
    /deep/ .el-radio-button__inner{
      color: #1e66dc;
      font-size: 15px;
      border-radius: 0;
      align-items:center; 
      display: -webkit-flex;
    }
    /deep/ .el-radio-button__inner::before{
      display: inline-block;
      content: "";
      width: 32px;
      height: 20px;
      background: url("../../../assets/images/prepaid.png") no-repeat;
      background-size: auto 100%;
    }
  }
  .alipay{
    /deep/ .el-radio-button__inner{
      color: rgb(1,170,238);
      font-size: 15px;
      border-radius: 0;
      border-left: 1px solid #dcdfe6;
      align-items:center; 
      display: -webkit-flex;
    }
    /deep/ .el-radio-button__inner::before{
      display: inline-block;
      content: "";
      width: 32px;
      height: 20px;
      background: url("../../../assets/images/alipay.png") no-repeat;
      background-size: auto 100%;
    }
  }
  .wechat{
    /deep/ .el-radio-button__inner{
      color: rgb(17,193,25);
      font-size: 15px;
      border-radius: 0;
      border-left: 1px solid #dcdfe6;
      align-items:center; 
      display: -webkit-flex;
    }
    /deep/ .el-radio-button__inner::before{
      display: inline-block;
      content: "";
      width: 32px;
      height: 20px;
      background: url("../../../assets/images/wechat.png") no-repeat;
      background-size: auto 100%;
    }
  }
  .rules {
    width: 50%;
    float: left;
    .ruleContent{
      p{
        margin-bottom: 15px;
      }
    }
  }
}
</style>
