<template>
  <div class="totalInfo">
    <div class="searchBar flex-row">
      <p class="flex-row"><span v-for="(item,index) of dates"
              @click="tagClick(item,index)"
              :class="dateIndex==index?'dateTag isActive':'dateTag'"
              :key="index">{{item.label}}</span></p>

      <el-date-picker v-model="form.dateRange"
                      type="datetimerange"
                      prefix-icon="dateRangeText"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期"
                      :default-time="['00:00:00', '23:59:59']">
      </el-date-picker>
      <el-select v-model="form.saleChanal"
                 clearable>
        <i slot="prefix"
           class="input-frefix-text">销售渠道 | </i>
        <el-option value=""
                   label="全部"></el-option>
      </el-select>
      <el-input v-model="form.goodsName"
                clearable>
        <i slot="prefix"
           class="input-frefix-text">商品名称 | </i>
      </el-input>
      <el-button type="primary">搜索</el-button>
    </div>
    <div class="reportBar flex-row">
      <div class="card">
        <p>订单总额</p>
        <p><span class="bold">1000</span>&nbsp;&nbsp;元</p>
        <p class="small"><span>13</span>&nbsp;&nbsp;笔</p>
      </div>
      <div class="card">
        <p>成交订单总额</p>
        <p><span class="bold">1000</span>&nbsp;&nbsp;元</p>
        <p class="small"><span>13</span>&nbsp;&nbsp;笔</p>
      </div>
      <div class="card">
        <p>退款总额</p>
        <p><span class="bold">1000</span>&nbsp;&nbsp;元</p>
        <p class="small"><span>13</span>&nbsp;&nbsp;笔</p>
      </div>
      <div class="card">
        <p>待结算总额</p>
        <p><span class="bold">1000</span>&nbsp;&nbsp;元</p>
        <p class="small"><span>13</span>&nbsp;&nbsp;笔</p>
      </div>
    </div>
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
      dates: [
        {
          label: "今天",
          value: 0
        },
        {
          label: "昨天",
          value: -1
        },
        {
          label: "最近七天",
          value: -7
        },
        {
          label: "最近30天",
          value: -30
        }
      ]
    };
  },
  mounted() {},
  methods: {
    tagClick(item, index) {
      this.dateIndex = index;
      this.form.dateValue = item.value;
    }
  }
};
</script>

<style lang="scss" scoped>
.totalInfo {
  padding: 8px;
  background: #fff;
  margin-bottom: 14px;
  .searchBar {
    align-items: center;
    justify-content: flex-end;
    * {
      margin-right: 8px;
    }
    p.flex-row {
    }
    .dateTag {
      margin-right: 28px;
    }
    .dateTag.isActive {
      color: #2368d9;
    }
    /deep/ .dateRangeText {
      display: inline-block;
      width: 80px;
      height: 36px;
      &:after {
        display: inline;
        color: #606266;
        font-style: normal;
        line-height: 36px;
        font-size: 8px;
        content: "创建时间 | ";
      }
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

  .reportBar {
      justify-content: space-around;
    .card {
      p {
        margin: 8px;
      }
      .bold {
        font-weight: bold;
      }
      .small {
        font-size: 0.8em;
      }
    }
  }
}
</style>