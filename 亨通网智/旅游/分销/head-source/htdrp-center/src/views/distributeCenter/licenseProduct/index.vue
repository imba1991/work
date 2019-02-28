<template>
  <div class="container">
    <div class="panel content">
      <div class="flex-space-between">
        <div>
          <button class="btn btn-medium"
                  @click="licenseVisible = true">单授权</button>
          <span class="little-font">* 已授权【成都亨通网...】23个商品</span>
        </div>
        <div class="searchBox">
          <el-select v-model="param.source"
                     placeholder="商品来源">
            <el-option v-for="item in options"
                       :key="item.value"
                       :label="item.label"
                       :value="item.value">
            </el-option>
          </el-select>
          <el-input v-model="param.id"
                    placeholder="商品id"></el-input>
          <el-input v-model="param.productName"
                    placeholder="商品名称"></el-input>
          <button class="btn"
                  @click="searchList(1)">搜索</button>
        </div>
      </div>
      <el-table :data="list"
                style="width: 100%">
        <el-table-column prop="id"
                         label="商品id">
          <template slot-scope="scoped">
            <!-- 添加点击事件跳转到商品页面 -->
            {{scoped.row.id}}
            <span v-if="scoped.row.status == 1"
                  class="single">单</span>
            <span v-else-if="scoped.row.status == 2"
                  class="together">组</span>
          </template>
        </el-table-column>
        <el-table-column prop="name"
                         label="【票型】商品名称"
                         width="440">
          <template slot-scope="scoped">
            <span class="primary">{{scoped.row.name}}</span><br>
            <span class="gray">{{scoped.row.source}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="costPrice"
                         label="成本价">
          <template slot-scope="scoped">￥{{Number(scoped.row.costPrice).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column prop="authorizedPrice"
                         label="分销授权价">
          <template slot-scope="scoped">
            ￥{{Number(scoped.row.authorizedPrice).toFixed(2)}}</template>
        </el-table-column>
        <el-table-column prop="authorizedDate"
                         label="分销有效期">
          <template slot-scope="scoped">
            <span v-if="scoped.row.expired">已过期</span>
            <span v-else>{{scoped.row.authorizedDate}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scoped">
            <el-button type="text"
                       @click="rescissionAuthorization(scoped.row.id)">解除授权</el-button>
            <el-button type="text"
                       @click="showDialog(scoped.row)">调整加价</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="text-center">
        <el-pagination @size-change="handleSizeChange"
                       @current-change="handleCurrentChange"
                       :current-page="param.page"
                       :page-sizes="[5, 10, 20, 30, 40]"
                       :page-size="param.size"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="total"></el-pagination>
      </div>
    </div>
    <el-dialog title="单授权商品"
               :visible.sync="licenseVisible"
               width="1038px">
      <div class="flex-end">
        <el-input placeholder="商品id"></el-input>
        <el-input placeholder="供应商名称/商品名称"></el-input>
        <button class="btn"
                @click="searchProductList(1)">搜索</button>
      </div>
      <el-table :data="productList"
                ref="productTable"
                style="width: 100%"
                @selection-change="(selection) => checkedList = selection">
        <el-table-column type="selection"
                         width="55">
        </el-table-column>
        <el-table-column prop="id"
                         label="商品id">
        </el-table-column>
        <el-table-column prop="name"
                         label="【票型】商品名称"
                         width="300">
          <template slot-scope="scoped">
            <span class="name">{{scoped.row.name}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="source"
                         label="商品来源"
                         width="182">
          <template slot-scope="scoped">
            <span class="name">{{scoped.row.source}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="costPrice"
                         label="成本价">
          <template slot-scope="scoped">
            <span class="name">￥{{Number(scoped.row.costPrice).toFixed(2)}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="authorizedPrice"
                         label="供货价">
          <template slot-scope="scoped">
            <span class="name">￥{{Number(scoped.row.authorizedPrice).toFixed(2)}}</span>
          </template>
        </el-table-column>
      </el-table>
      <div class="text-center">
        <el-pagination @size-change="(val)=>{productParam.size=val;searchProductList();}"
                       @current-change="(val)=>{productParam.page=val;searchProductList();}"
                       :current-page="productParam.page"
                       :page-sizes="[5, 10, 20, 30, 40]"
                       :page-size="productParam.size"
                       layout="total, sizes, prev, pager, next, jumper"
                       :total="productTotal"></el-pagination>
      </div>
      <p class="title">已选择</p>
      <div class="table-list">
        <div class="item"
             v-for="item in checkedList"
             :key="item.id">
          <span class="name">{{item.name}}</span>
          <span class="source">商品来源：{{item.source}}</span>
          <span class="price">成本价：￥{{Number(item.costPrice).toFixed(2)}}</span>
          <span class="price">供货价：￥{{Number(item.authorizedPrice).toFixed(2)}}</span>
          <i class="iconfont icon-jianqu"
             @click="$refs.productTable.toggleRowSelection(item, false)"></i>
        </div>
      </div>
      <div class="footer-row">分销授权价：在每个商品的成本价上面增加 <el-input></el-input> 元 <label>说明：如果不设置分销授权价，则默认供货价为分销授权价</label></div>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="licenseVisible = false">取 消</el-button>
        <el-button type="primary"
                   @click="save()">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog title="调整加价"
               :visible.sync="priceVisible"
               width="558px">
      <div class="price-content">
        <p class="my-20">{{model.name}}</p>
        <p class="my-20"><span>成本价：￥<label class="danger">{{Number(model.costPrice).toFixed(2)}}</label></span><span>默认供货价：￥<label class="danger">{{Number(model.authorizedPrice).toFixed(2)}}</label></span></p>
        <div class="plates">
          <div class="plate"><span>当前供货价</span><br><span>￥{{Number(model.authorizedPrice).toFixed(2)}}</span></div>
          <div class="operator">
            <span v-if="operator"
                  class="iconfont icon-zengjia"></span>
            <span v-else
                  class="iconfont icon-jianshao"></span>
            <div><span class="iconfont icon-xiangxia"
                    @click="operator=!operator"></span></div>
          </div>
          <div class="plate"><span>{{operator?"增加":"减少"}}</span><br>
            <el-input v-model="model.changePrice"></el-input>
          </div>
          <div class="operator-equal"><i class="iconfont icon-dengyu"></i></div>
          <div class="plate">
            <span>调后供货价</span><br>
            <span v-if="operator">￥{{(Number(model.authorizedPrice) + Number(model.changePrice)).toFixed(2)}}</span>
            <span v-else>￥{{Number(model.authorizedPrice-model.changePrice).toFixed(2)}}</span>
          </div>
        </div>
      </div>
      <span slot="footer"
            class="dialog-footer">
        <el-button @click="priceVisible = false">取 消</el-button>
        <el-button type="primary"
                   @click="save()">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      total: 0,
      productTotal: 0,
      param: {
        page: 1,
        size: 10,
        source: "",
        id: "",
        productName: ""
      },
      productParam: {
        page: 1,
        size: 5,
        id: "",
        name: ""
      },
      licenseVisible: false,
      priceVisible: false,
      options: [
        {
          value: "1",
          label: "自建商品"
        },
        {
          value: "2",
          label: "同步商品"
        },
        {
          value: "3",
          label: "分销商品"
        }
      ],
      list: [
        {
          status: "1",
          id: "119418",
          name:
            "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票(老人/儿童/军人)",
          source: "商品来源：同步-驴妈妈（1326742）",
          costPrice: "60.00",
          authorizedPrice: "10.00",
          authorizedDate: "2018/09/09-2018/10/10"
        },
        {
          status: "1",
          id: "1243",
          name: "【票型】商品名称",
          source: "商品来源：供应商名称/接口名称（空或第三方id）/自建",
          costPrice: "60.00",
          authorizedPrice: "10.00",
          authorizedDate: "2018/09/09-2018/10/10"
        },
        {
          status: "2",
          id: "119418",
          name:
            "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票(老人/儿童/军人)",
          source: "商品来源：自建",
          costPrice: "0",
          authorizedPrice: "0",
          authorizedDate: "2018/09/09-2018/10/10",
          expired: "1"
        }
      ],
      productList: [
        {
          id: "MP458761657687",
          name: "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票",
          source: "九寨沟旅游（1987**8767）",
          costPrice: 12,
          authorizedPrice: 20
        },
        {
          id: "MP458761657688",
          name: "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票",
          source: "九寨沟旅游（1987**8767）",
          costPrice: 12,
          authorizedPrice: 20
        },
        {
          id: "MP458761657689",
          name: "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票",
          source: "九寨沟旅游（1987**8767）",
          costPrice: 12,
          authorizedPrice: 20
        },
        {
          id: "MP458761657686",
          name: "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票",
          source: "九寨沟旅游（1987**8767）",
          costPrice: 12,
          authorizedPrice: 20
        },
        {
          id: "MP458761657685",
          name: "【成人票】泉州石龙谷森林游乐园大门票+项目联票优待票",
          source: "九寨沟旅游（1987**8767）",
          costPrice: 12,
          authorizedPrice: 20
        }
      ],
      checkedList: [],
      model: {
        costPrice: 0,
        authorizedPrice: 0,
        changePrice: 0
      },
      operator: true
    };
  },
  mounted() {},
  methods: {
    /**
     * 搜索商品列表数据
     */
    async searchList(page = "") {
      if (page) this.param.page = 1;
      let result = await this.$axios.post("", this.buildFormData(this.param));
    },

    /**
     * 搜索授权商品列表数据
     */
    async searchProductList(page = "") {
      if (page) this.productParam.page = 1;
      let result = await this.$axios.post(
        "",
        this.buildFormData(this.productParam)
      );
    },

    /**
     * 解除授权
     * @param id
     */
    rescissionAuthorization(id) {
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定解除授权，解除授权之后，商户将不能再售卖该商品',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "解除授权",
          cancelButtonText: "取消"
        }
      )
        .then(() => {
          this.$axios.post("", this.buildFormData({ id: id })).then(result => {
            if (result.data.code == "1001") {
              this.$message.success("解除授权成功");
            }
          });
        })
        .catch(() => {
          this.$message.info("已取消解除授权");
        });
    },

    /**
     * 保存授权
     */
    save() {},

    /**
     * 显示调整加价的弹窗
     * @param model
     */
    showDialog(model) {
      this.priceVisible = true;
      this.model = { ...JSON.parse(JSON.stringify(model)), changePrice: 0 };
    },

    /**
     * 修改显示条数
     * @param {*} val
     */
    handleSizeChange(val) {
      this.param.size = val;
      this.search();
    },

    /**
     * 修改当前页数
     * @param {*} val
     */
    handleCurrentChange(val) {
      this.param.page = val;
      this.search();
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  font-size: 12px;
  color: #444;
  .panel {
    padding: 20px;
    background-color: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    .flex-space-between {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .little-font {
        margin-left: 10px;
        font-size: 12px;
        color: #888888;
      }
    }
  }
  .content {
    min-height: calc(100vh - 156px);
  }
  .el-select {
    width: 120px;
  }
  .el-input {
    width: 140px;
    & + .el-input,
    & + .btn {
      margin-left: 10px;
    }
  }
  .el-pagination {
    /deep/ .el-input__inner {
      height: 30px;
      line-height: 30px;
      font-size: 12px;
    }
  }
  .el-button {
    width: 86px;
    padding: 10px 0;
  }
  .flex-end {
    display: flex;
    justify-content: flex-end;
  }
  .el-table {
    margin-top: 10px;
    .primary {
      color: #1e66dc;
      cursor: pointer;
    }
    .single,
    .together {
      display: inline-block;
      padding: 3px;
      line-height: 1;
      text-align: center;
      font-size: 10px;
      -webkit-transform: scale(0.8);
      border: solid 1px #1e66dc;
      border-radius: 50%;
      color: #1e66dc;
    }
    .together {
      color: #32b16c;
      border: solid 1px #32b16c;
    }
  }

  p.title {
    font-size: 16px;
    color: #333333;
  }
  .table-list {
    margin: 10px 0 20px;
    padding: 20px;
    height: 230px;
    overflow-y: scroll;
    border-radius: 3px;
    border: solid 1px #e5e5e5;
    .item {
      position: relative;
      padding: 12px 20px;
      width: 918px;
      background-color: #f2f2f2;
      border-radius: 3px;
      line-height: 1;
      & + .item {
        margin-top: 10px;
      }
      span {
        display: inline-block;
        font-size: 12px;
        color: #38474f;
        line-height: 1;
        & + span {
          margin-left: 50px;
        }
      }
      .icon-jianqu {
        position: absolute;
        color: #e54545;
        font-size: 16px;
        top: -8px;
        right: -8px;
        cursor: pointer;
      }
    }
  }
  span {
    &.name {
      width: 312px;
      white-space: nowrap;
      text-overflow: ellipsis;
      overflow: hidden;
    }
    &.source {
      width: 218px;
    }
    &.price {
      width: 100px;
    }
    .danger {
      color: #e54545;
    }
  }
  .footer-row {
    font-size: 12px;
    color: #444444;
    label {
      margin-left: 20px;
      color: #888888;
    }
  }
  .price-content {
    p {
      margin-bottom: 20px;
      margin-top: 20px;
      span {
        margin-left: 10px;
        & + span {
          margin-left: 20px;
        }
      }
    }
    .plates {
      display: flex;
      justify-content: space-around;
      align-items: stretch;
      margin: 0 0 40px;
      text-align: center;
      .plate {
        width: 121px;
        height: 76px;
        background-color: #f2f2f2;
        border-radius: 3px;
        span {
          line-height: 32px;
        }
        .el-input {
          width: 86px;
        }
      }
      .operator {
        height: 76px;
        .icon-zengjia,
        .icon-jianshao {
          padding: 7px;
          border-radius: 3px;
          border: solid 1px #e5e5e5;
          font-size: 16px;
          text-align: center;
          line-height: 76px;
        }
        div {
          margin-top: -20px;
          .icon-xiangxia {
            color: #1e66dc;
          }
        }
      }
      .operator-equal {
        line-height: 76px;
      }
    }
  }
}
</style>
