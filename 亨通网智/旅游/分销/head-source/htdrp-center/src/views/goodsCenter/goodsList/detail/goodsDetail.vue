<template>
  <div class="goodsDetail">
    <div class="commomItem">
      <!-- 页面上部分 -->
      <div class="detailTop">
        <div class="topLeft inline-block">
          <el-carousel :interval="5000"
                       arrow="always">
            <el-carousel-item v-for="item in imgUrls"
                              :key="item">
              <img :src="item"
                   alt>
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="topRight inline-block">
          <p class="text-bold">{{pageData.ticketName}}</p>
          <p class="text-lightBlack tagLine">
            <span class="tag">门票</span>
            <span v-if="pageData.putAway==0"
                  class="tag">下架</span>
            <span v-else-if="pageData.putAway==1"
                  class="tag">上架</span>
          </p>
          <p class="text-lightBlack"><span class="">商 品 id</span>：{{pageData.id}}</p>
          <p class="text-lightBlack"><span class="">商品来源</span>：{{pageData.connectorName}}</p>
          <p class="text-lightBlack">
            <span>资源名称</span>:
            <span class="primary"
                  style="cursor:pointer"
                  @click="toResourcePage()">{{pageData.resourceName}}</span>
          </p>
          <p class="text-lightBlack">
            <span>建议售价</span>：
            <span>￥{{(pageData.suggestPrice/100).toFixed(2)}}</span>
          </p>
          <p class="text-lightBlack">
            <span>供 货 价</span>：
            <span class="text-red text-lg">￥{{(pageData.costPrice/100).toFixed(2)}}</span>
          </p>
          <el-button type="danger"
                     size="small"
                     class="buyBtn">购买</el-button>
        </div>
      </div>
      <!-- 页面下部分 -->
      <div class="detailBottom">
        <el-tabs v-model="activeName"
                 type="card"
                 @tab-click="handleClick">
          <el-tab-pane label="商品详情"
                       name="first">

          </el-tab-pane>
          <el-tab-pane label="预定须知"
                       name="second">
            <!-- <div class="rulesItem"
                 v-for="(item, index) in rulesData"
                 :key="index">
              <p class="ruleTitle">{{item.title}}</p>
              <div class="ruleContent">
                <div class="ruleLine"
                     v-for="(rule, idx) in item.rulesContent"
                     :key="idx">
                  <div class="inline-block ruleName text-center">{{rule.ruleName}}</div>
                  <div class="inline-block rule">{{rule.content}}</div>
                </div>
              </div>
            </div> -->
            <div class="rulesItem">
              <p class="ruleTitle">预定规则</p>
              <div class="ruleContent">
                <div class="ruleLine flex-row">
                  <div class="ruleName">预定限制</div>
                  <div class="rule">{{pageData.prioriLimitation}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">生日限制</div>
                  <div class="rule">{{pageData.ageLimitation}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">地区限制</div>
                  <div class="rule">{{pageData.territorialRestrictions}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">年龄限制</div>
                  <div class="rule">{{pageData.ageLimitation}}</div>
                </div>
              </div>
              <!--  -->
              <p class="ruleTitle">消费规则</p>
              <div class="ruleContent">
                <div class="ruleLine flex-row">
                  <div class="ruleName">消费限制</div>
                  <div class="rule">{{pageData.limitedConsumption}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">入园时间</div>
                  <div class="rule">{{pageData.entryTime}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">入园次数</div>
                  <div class="rule">{{pageData.entryNumber}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">时间限制</div>
                  <div class="rule">{{pageData.timeLimit}}</div>
                </div>
              </div>
              <!--  -->
              <p class="ruleTitle">退款规则</p>
              <div class="ruleContent">
                <div class="ruleLine flex-row">
                  <div class="ruleName">退款类型</div>
                  <div class="rule">{{pageData.refundType}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">退款时限</div>
                  <div class="rule">{{pageData.refundTime}}</div>
                </div>
                <div class="ruleLine flex-row">
                  <div class="ruleName">其他说明</div>
                  <div class="rule lineHeight_2">{{pageData.remark}}</div>
                </div>
              </div>
              <!--  -->
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {
      goodsId: "",
      resourceId: "",
      imgUrls: [
        "https://gss0.bdstatic.com/7051cy89RcgCncy6lo7D0j9wexYrbOWh7c50/tuhuchunjie/270170.jpg?t=1549816303"
      ],
      activeName: "second",
      pageData: {},
      rulesData: [
        {
          title: "预定规则",
          rulesContent: [
            {
              ruleName: "预定限制",
              content: "每单最少购买一张"
            },
            {
              ruleName: "生日限制",
              content: "每单最少购买一张"
            },
            {
              ruleName: "地区限制",
              content: "每单最少购买一张"
            },
            {
              ruleName: "年龄限制",
              content: "每单最少购买一张"
            }
          ]
        },
        {
          title: "消费规则",
          rulesContent: [
            {
              ruleName: "消费限制",
              content: "每单最少购买一张"
            },
            {
              ruleName: "入园时间",
              content: "每单最少购买一张"
            },
            {
              ruleName: "入园次数",
              content: "每单最少购买一张"
            },
            {
              ruleName: "时间限制",
              content: "每单最少购买一张"
            }
          ]
        }
      ]
    };
  },
  created() {
    if (location.href.includes("?id=")) {
      this.goodsId = location.href.split("?id=")[1];
    } else {
      this.goodsId = sessionStorage.getItem("goodsListDetailId");
    }
    this.getPageData();
  },
  mounted() {},
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
    },
    async getPageData() {
      let result = await this.$axios.post(
        "/product/tickets/selectByPrimaryKey",
        this.buildFormData({ id: this.goodsId })
      );
      if (result.data.code == "1001") {
        this.pageData = result.data.datas;
        this.resourceId = result.data.datas.resourceId;
      }
    },
    toResourcePage() {
      //TODO 需要将资源ID传入资源页进行数据查询
      sessionStorage.setItem("resourceID", this.resourceId);
      this.$router.push(
        "/goodsCenter/goodsList/detail/resourceDetail?id=" + this.resourceId
      );
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../../../assets/css/childPage.css";

.commomItem {
  padding: 0;
}
.detailTop {
  display: flex;
  padding: 10px 20px;
  .topLeft {
    width: 30%;
    img {
      width: 100%;
    }
  }
  .topRight {
    width: 70%;
    padding: 8px 20px;
    p {
      margin-bottom: 7px;
    }
    .tagLine {
      margin: 15px 0;
    }
    .tag {
      border: 1px solid #444;
      border-radius: 15px;
      padding: 2px 15px;
      margin-right: 10px;
    }
    .buyBtn {
      margin-top: 13px;
      padding: 8px 26px;
      font-size: 15px;
    }
  }
}
.detailBottom {
  margin-top: 30px;
  .el-tabs {
    /deep/ .el-tabs__header {
      padding: 0 20px;
    }
    /deep/ .el-tabs__content {
      padding: 0 20px 40px;
    }
  }
  .ruleTitle {
    font-weight: bold;
    margin-bottom: 8px;
  }
  .ruleContent {
    border: 1px solid #e4e7ed;
    border-bottom: 0;
    margin-bottom: 20px;
    .ruleLine {
      width: 100%;
      border-bottom: 1px solid #e4e7ed;
      .ruleName {
        width: 180px;
        line-height: 34px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-right: 1px solid #e4e5ed;
        background: rgb(245, 245, 245);
      }
      .rule {
        flex: 1;
        display: flex;
        align-items: center;
        padding-left: 15px;
      }
      .lineHeight_2 {
        padding-top: 14px;
        padding-bottom: 14px;
        line-height: 2em;
      }
    }
  }
}
</style>
