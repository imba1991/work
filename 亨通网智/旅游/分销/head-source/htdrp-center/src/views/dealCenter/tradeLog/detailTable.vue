<template>
  <div class="detailTable">
    <div class="searchBar flex-row">
      <el-select v-model="form.saleChanal"
                 clearable>
        <i slot="prefix"
           class="input-frefix-text">支付方式 | </i>
        <el-option value=""
                   label="全部"></el-option>
      </el-select>
      <el-select v-model="form.saleChanal"
                 clearable>
        <i slot="prefix"
           class="input-frefix-text">&nbsp;状&emsp;态 | </i>
        <el-option value=""
                   label="全部"></el-option>
      </el-select>
      <el-input v-model="form.goodsName"
                clearable>
        <i slot="prefix"
           class="input-frefix-text">游&nbsp;玩&nbsp;人 | </i>
      </el-input>
      <el-input v-model="form.goodsName"
                clearable>
        <i slot="prefix"
           class="input-frefix-text">订单编号 | </i>
      </el-input>
      <el-input v-model="form.goodsName"
                clearable>
        <i slot="prefix"
           class="input-frefix-text">消&nbsp;费&nbsp;码 | </i>
      </el-input>
      <el-button type="primary">搜索</el-button>
    </div>
    <el-table border
              :data="tableData">
      <el-table-column prop="orderID"
                       label="订单编号">
      </el-table-column>
      <el-table-column prop="user"
                       label="游玩人">
      </el-table-column>
      <el-table-column prop="goodsName"
                       label="商品名称"
                       width="180"
                       show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="total"
                       label="总金额">
      </el-table-column>
      <el-table-column prop="mount"
                       label="数量">
      </el-table-column>
      <el-table-column prop="buyer"
                       label="下单人">
      </el-table-column>
      <el-table-column prop="saleChannal"
                       label="销售渠道">
        <template slot-scope="scope">
          {{getSaleChannal(scope.row.saleChannal)}}
        </template>
      </el-table-column>
      <el-table-column prop="expireDate"
                       label="消费有效期"
                       width="180">
      </el-table-column>
      <el-table-column label="状态">
        <template slot-scope="scope">{{getState(scope.row.state)}}</template>
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
            <span @click="rowClick(scope.row.orderID)">支付</span>
            </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      dateIndex: 0,
      form: {
        dateRange: [],
        saleChanal: "",
        goodsName: "",
        dateValue: 0
      },
      tableData: [
        {
          orderID: 1122324,
          user: "Lucky",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 1,
          expireDate: "2018-03-30",
          state: 0
        },
        {
          orderID: 1122324,
          user: "Roman",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 2,
          expireDate: "2018-03-30",
          state: 1
        },
        {
          orderID: 1122324,
          user: "Jack",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 3,
          expireDate: "2018-03-30",
          state: 2
        },
        {
          orderID: 1122324,
          user: "Lily",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 4,
          expireDate: "2018-03-30",
          state: 3
        },
        {
          orderID: 1122324,
          user: "Sunny",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 2,
          expireDate: "2018-03-30",
          state: 4
        },
        {
          orderID: 1122324,
          user: "Jinx",
          goodsName: "【九寨沟风景区（765）】",
          total: 10000,
          mount: 6,
          buyer: "178****4545",
          saleChannal: 1,
          expireDate: "2018-03-30",
          state: 5
        }
      ]
    };
  },
  mounted() {},
  methods: {
    getState(state) {
      switch (state) {
        case 0:
          return "待支付";
        case 1:
          return "待消费";
        case 2:
          return "已过期";
        case 3:
          return "已退款";
        case 4:
          return "已完成";
        case 5:
          return "已取消";
      }
    },
    rowClick(id) {
      console.log(id);
    },
    getSaleChannal(saleChannal) {
      switch (saleChannal) {
        case 1:
          return "后台售卖";
        case 2:
          return "微店售卖";
        case 3:
          return "分销中心";
        case 4:
          return "携程接口";
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.detailTable {
  background: #fff;
  padding: 8px;
  .searchBar {
    align-items: center;
    justify-content: flex-end;
    margin-bottom: 8px;
    * {
      margin-right: 8px;
    }
    .el-select,
    .el-input {
      width: 240px;
      /deep/ .el-input__inner {
        padding-left: 82px;
        color: #606266;
      }
      .input-frefix-text {
        padding-left: 8px;
        font-style: normal;
        line-height: 36px;
        color: #606266;
      }
    }
  }
}
</style>

