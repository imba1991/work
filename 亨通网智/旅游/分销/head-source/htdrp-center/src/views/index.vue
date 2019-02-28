<template>
  <div class="container">
    <el-row :gutter="20">
      <el-col :md="8"
              class="col-24">
        <div class="panel">
          <p>接口</p>
          <div>
            <span>已对接</span>
            <div class="list">
              <template v-for="(item,index) in list">
                <img :src="item.picUrl"
                     :key="index"
                     height="36px">
              </template>
              ...
            </div>
            <span class="text-right">5/12个</span>
          </div>
        </div>
      </el-col>
      <el-col :md="8"
              class="col-12">
        <div class="panel">
          <p>合作分销商</p>
          <div>
            <span>已有</span>
            <label>8655</label>
            <span class="text-right">户</span>
          </div>
        </div>
      </el-col>
      <el-col :md="8"
              class="col-12">
        <div class="panel">
          <p>合作供应商</p>
          <div>
            <span>已有</span>
            <label>866</label>
            <span class="text-right">户</span>
          </div>
        </div>
      </el-col>
    </el-row>
    <div class="statisticalBoard">
      <p>数据更新时间：{{dateNow}}</p>
      <el-row>
        <el-col :span="6">
          <span>今日消费人数</span>
          <label>8585</label>
          <span>人</span> </el-col>
        <el-col :span="6">
          <span>今日销售额</span><label>185</label>
          <span>万</span>
        </el-col>
        <el-col :span="6">
          <span>今日销售量</span>
          <label>1885</label>
          <span>万</span>
        </el-col>
        <el-col :span="6">
          <span><sup>99+</sup> 今日退票</span>
          <label>51</label>
          <span>张</span>
        </el-col>
      </el-row>
    </div>
    <div class="chartBoard">
      <div class="header">
        <el-select v-model="channel"
                   placeholder="请选择销售渠道">
          <el-option value=""
                     label="全部销售渠道"></el-option>
          <el-option v-for="item in options"
                     :key="item.id"
                     :label="item.name"
                     :value="item.id">
          </el-option>
        </el-select>
        <div class="time">
          <span>数据统计周期为：2018-09-08 至 2019-09-08</span>
          <div class="btn-group">
            <span :class="activeAction == '上季度' ? 'active':''"
                  @click="pickDate('上季度')">上季度</span>
            <span :class="activeAction == '上月' ? 'active':''"
                  @click="pickDate('上月')">上月</span>
            <span :class="activeAction == '上周' ? 'active':''"
                  @click="pickDate('上周')">上周</span>
            <span :class="activeAction == '最近7天' ? 'active':''"
                  @click="pickDate('最近7天')">最近7天</span>
          </div>
        </div>
      </div>
      <ve-line :data="chartData"
               width="100%"
               :settings="chartSettings"
               :loading="loading"
               :data-empty="dataEmpty"></ve-line>
    </div>
  </div>
</template>
<script>
import "v-charts/lib/style.css";
import VeLine from "v-charts/lib/line.common";
import img from "../assets/images/alipay.png";

export default {
  components: {
    VeLine: VeLine
  },
  data() {
    return {
      chartSettings: {
        dimension: ["date"],
        metrics: ["order_amount", "order_num"],
        area: true,
        labelMap: {
          order_amount: "订单金额",
          order_num: "订单笔数"
        },
        xAxisType: "category"
      },
      chartData: [],
      loading: true,
      dataEmpty: false,
      list: [
        { picUrl: img },
        { picUrl: img },
        { picUrl: img },
        { picUrl: img },
        { picUrl: img }
      ],
      channel: "",
      options: [
        {
          id: 1,
          name: "微商店售票"
        }
      ],
      activeAction: "上季度",
      params: {
        startDate: "",
        endDate: ""
      },
      dateNow: "",
      headerData: ""
    };
  },
  mounted() {
    this.checkConfig();
  },
  methods: {
    /**
     * 查询微信收款配置
     */
    searchWeChatConfig() {
      return this.$axios.post("/wxPayManage/detailsByUser");
    },
    /**
     * 查询支付宝收款配置
     */
    searchAlipayConfig() {
      return this.$axios.post("/aplipay/properties/detailsByUser");
    },
    /**
     * 校验是否添加收款方式
     */
    checkConfig() {
      let _this = this;
      this.$axios
        .all([this.searchWeChatConfig(), this.searchAlipayConfig()])
        .then(
          this.$axios.spread(function(weChatResult, alipayResult) {
            if (
              weChatResult.data.code == "1001" &&
              alipayResult.data.code == "1001"
            ) {
              if (weChatResult.data.datas || alipayResult.data.datas) {
                return;
              } else {
                _this
                  .$confirm(
                    "<i class='el-icon-warning' style='margin-left: -17px;'></i> 您如果要经营还需要做好如下操作：配置收款方式",
                    "提示",
                    {
                      dangerouslyUseHTMLString: true,
                      confirmButtonText: "配置收款方式",
                      cancelButtonText: "稍后再配置"
                    }
                  )
                  .then(() => {
                    _this.$router.push("/userCenter");
                  })
                  .catch(() => {});
              }
            }
          })
        );
    },

    async getHeaderData() {
      let result = await this.$axios.post(
        "",
        this.buildFormData({ scenicId: this.params.scenicId })
      );
      if (result.data.code == "1001") {
        this.headerData = result.data.datas;
      }
    },
    /**
     * 获取折线图数据
     */
    async getData() {
      this.loading = true;
      this.dateNow =
        new Date().toLocaleDateString() + " " + new Date().toLocaleTimeString();
      let result = await this.$axios.post("", this.buildFormData(this.params));
      if (result.data.code == "1001") {
        this.chartData.rows = result.data.datas;
        this.loading = false;
        if (!this.chartData.rows.length) this.dataEmpty = true;
      }
    },
    /**
     * 构造时间选择参数
     * @param action
     */
    pickDate(action) {
      this.activeAction = action;
      let start,
        end,
        date = new Date();
      switch (action) {
        case "上周":
          if (date.getDay == 0) {
            end = date.getTime() - 3600 * 1000 * 24 * 7;
          } else {
            end = date.getTime() - 3600 * 1000 * 24 * date.getDay();
          }
          start = end - 3600 * 1000 * 24 * 6;
          break;
        case "上月":
          end = date.getTime() - 3600 * 1000 * 24 * date.getDate();
          start = end - 3600 * 1000 * 24 * (new Date(end).getDate() - 1);
          break;
        case "上季度":
          let year = date.getFullYear();
          let month = date.getMonth() + 1;
          if (month <= 3) {
            year--;
            month = 9;
          } else if (month > 3 && month <= 6) {
            month = 0;
          } else if (month > 6 && month <= 9) {
            month = 3;
          } else month = 6;
          start = new Date(year, month, 1).getTime();
          let monthDays = new Date(year, month + 3, 0).getDate(); // 获取上季度的最后一月天数
          end = new Date(year, month + 2, monthDays).getTime();
          break;
        default:
          start = date.getTime() - 3600 * 1000 * 24 * 7; //如果是昨日则查询最近一周的数据
          end = date.getTime() - 3600 * 1000 * 24;
      }
      // 格式化数据
      this.params.startDate = this.formateTime(start);
      this.params.endDate = this.formateTime(end);
      this.getData();
    },

    /**
     * 格式化数据时间戳数据
     * @param timeStap
     */
    formateTime(timeStap) {
      let date = new Date(timeStap);
      let seperator1 = "-";
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();
      if (month >= 1 && month <= 9) {
        month = "0" + month;
      }
      if (day >= 0 && day <= 9) {
        day = "0" + day;
      }
      var currentdate = year + seperator1 + month + seperator1 + day;
      return currentdate;
    }
  }
};
</script>
<style lang="scss" scoped>
.container {
  .panel {
    margin-bottom: 20px;
    padding: 0 20px;
    height: 98px;
    background-color: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    box-sizing: border-box;
    color: #333333;
    p {
      padding: 10px 0 20px;
      font-size: 14px;
      font-weight: bold;
      line-height: 1;
    }
    div {
      display: flex;
      justify-content: space-between;
      align-items: center;
      line-height: 1;
      span {
        flex: 0 0 50px;
        flex-shrink: 0;
        color: #999999;
        font-size: 12px;
        &.text-right {
          text-align: right;
        }
      }
      label {
        flex-shrink: 1;
        font-size: 36px;
        text-align: center;
      }
      .list {
        img {
          height: 36px;
          margin-right: 20px;
        }
      }
    }
  }
  .statisticalBoard {
    margin-bottom: 20px;
    padding: 0 20px 20px;
    background: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    p {
      padding: 10px 0;
      font-size: 12px;
      color: #666666;
    }
    .el-row {
      .el-col-6 {
        line-height: 26px;
        text-align: center;
        & + .el-col-6 {
          border-left: 1px solid #e5e5e5;
        }
        span {
          display: inline-block;
          font-size: 12px;
          color: #999999;
          sup {
            color: #ff0000;
          }
        }
        label {
          display: inline-block;
          min-width: 92px;
          font-size: 18px;
          color: #333333;
        }
      }
    }
  }
  .chartBoard {
    padding: 10px 20px 20px;
    background: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .time {
        flex: 0 0 516px;
        font-size: 12px;
        span {
          color: #666666;
        }
        .btn-group {
          display: inline-block;
          margin-left: 20px;
          border-radius: 14px;
          border: solid 1px #e5e5e5;
          span {
            display: inline-block;
            padding: 6px 12px;
            color: #999999;
            font-size: 12px;
            text-align: center;
            &.active {
              color: #ffffff;
              background-color: #1e66dc;
              border-radius: 14px;
            }
          }
        }
      }
    }
  }
}
@media screen and (max-width: 1360px) {
  .col-12 {
    width: 50%;
  }
  .col-24 {
    width: 100%;
  }
}
</style>


