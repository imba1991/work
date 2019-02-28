<template>
  <div class="tabComponent">
    <div class="searchBar">
      <el-select v-model="searchData.putAway"
                 placeholder="商品状态">
        <el-option value="1"
                   label="上架"></el-option>
      </el-select>
      <el-select v-model="searchData.ticketSource"
                 placeholder="商品来源">
      </el-select>
      <el-input placeholder="商品id"
                v-model="searchData.id"></el-input>
      <el-input v-model="searchData.ticketName"
                placeholder="商品名称"></el-input>
      <el-select v-model="searchData.salesChannels"
                 placeholder="销售渠道">
      </el-select>
      <el-date-picker v-model="searchData.dateRange"
                      type="daterange"
                      range-separator="至"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期">
      </el-date-picker>
      <el-input v-model="searchData.connectorName"
                placeholder="供应商名称/接口名称"></el-input>
      <el-button type="primary"
                 @click="search('isFirst')">搜索</el-button>
    </div>
    <el-table :data="tableData"
              @cell-click="rowClick"
              style="width: 100%">
      <el-table-column prop="id"
                       width="160px"
                       label="商品id">
      </el-table-column>
      <el-table-column label="【票型】商品名称"
                       show-overflow-tooltip>
        <template slot-scope="scope">

          <p style="text-align:left;color:#1E66DC;cursor:pointer">{{scope.row.ticketName}}</p>
          <p style="text-align:left;">商品来源：{{scope.row.connectorName}}</p>

        </template>
      </el-table-column>
      <el-table-column prop="costPrice"
                       width="120px"
                       label="成本价">
        <template slot-scope="scope">
          <span>￥{{(scope.row.costPrice).toFixed(2)}}</span>
        </template>
      </el-table-column>
      <el-table-column prop="suggestPrice"
                       width="120px"
                       label="售价"> <template slot-scope="scope">
          <span>￥{{(scope.row.suggestPrice).toFixed(2)}}</span>
        </template>
      </el-table-column>
      <el-table-column width="220px"
                       label="销售有效期">
        <template slot-scope="scope">
          <span>{{scope.row.sellBeginTime.substr(0,10)+' 至 '+scope.row.sellEndTime.substr(0,10)}}</span>

        </template>
      </el-table-column>
      <el-table-column width="300px"
                       label="操作">
        <template slot-scope="scope">
          <el-button v-if="scope.row.putAway==1"
                     @click="tableRowMethod(scope.row.id,'upOrDown')">下架</el-button>
          <el-button v-else-if="scope.row.putAway==0"
                     @click="tableRowMethod(scope.row.id,'upOrDown')">上架</el-button>
          <el-button>同步</el-button>
          <el-button>购买</el-button>
          <el-button @click="tableRowMethod(scope.row.id,'delete')">删除</el-button>
          <el-button @click="showDialog(scope.row.id)">价格库存</el-button>
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
    <el-dialog title="价格库存"
               :visible.sync="dialogVisible"
               width="640px">
      <div class="formCard"
           v-for="(form,index) of formData"
           :key="index">
        <el-button icon="el-icon-minus"
                   type="danger"
                   size="mini"
                   circle
                   v-show="formData.length>1"
                   @click="removeForm(index)"></el-button>
        <p style="line-height:32px">可售日期 <span style="color:red">（日期请不要重复）</span></p>
        <p>
          <el-date-picker v-model="form.dateRange"
                          :picker-options="pickerOptions"
                          @change="dateRangeChanged(form,index)"
                          type="daterange"
                          range-separator="至"
                          start-placeholder="开始日期"
                          end-placeholder="结束日期">
          </el-date-picker>
          <el-checkbox v-model="form.permanentSales"
                       :false-label="0"
                       :true-label="1">永久销售（依照供应可售日期）</el-checkbox>
        </p>
        <div class="inputsBar">
          <div>
            <p>日库存</p>
            <p>
              <el-input size="small"
                        placeholder="不限"
                        @change="dataChangeToInifity(form,index)"
                        v-model="form.inventory"></el-input>
            </p>

          </div>
          <div>
            <p>票面价</p>
            <p>
              <el-input-number size="small"
                               :precision="2"
                               :min="0"
                               :controls="false"
                               v-model.number="form.sellPrice"></el-input-number>
            </p>
          </div>
          <div>
            <p>建议售价</p>
            <p>
              <el-input-number size="small"
                               :precision="2"
                               :min="0"
                               :controls="false"
                               v-model="form.suggestPrice"></el-input-number>
            </p>
          </div>
          <div>
            <p>供货价</p>
            <p>
              <el-input-number size="small"
                               :precision="2"
                               :min="0"
                               :controls="false"
                               v-model="form.supplyPrice"></el-input-number>
            </p>
          </div>
          <div>
            <p>成本价</p>
            <p>
              <el-input-number size="small"
                               :precision="2"
                               :min="0"
                               :controls="false"
                               v-model="form.costPrice"></el-input-number>
            </p>
          </div>
        </div>
        <div class="weekBar flex-row">
          <el-checkbox-group v-model="form.saleWeekday">
            <el-checkbox v-for="week in weekdays"
                         :label="week.value"
                         :key="week.value">{{week.label}}</el-checkbox>
          </el-checkbox-group>
          <span @click="isWeekOnly(index)">仅周末</span>
        </div>

      </div>
      <div class="buttonAdd"
           v-show="isDialogButtonShow"
           @click="addForm"><i class="el-icon-plus"></i>增加</div>
      <p class="buttonBar">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary"
                   @click="saveForm">确定</el-button>
      </p>
    </el-dialog>
  </div>
</template>

<script>
import _cloneDeep from "lodash/cloneDeep";
export default {
  props: ["activeName"],
  data() {
    return {
      isDialogButtonShow: true,
      dialogVisible: false,
      total: 100,
      pickerOptions: {},
      weekdays: [
        { value: 0, label: "周一" },
        { value: 1, label: "周二" },
        { value: 2, label: "周三" },
        { value: 3, label: "周四" },
        { value: 4, label: "周五" },
        { value: 5, label: "周六" },
        { value: 6, label: "周天" }
      ],
      searchData: {
        tableType: "", //用于后期添加多中套餐功能（景区门票/套票 etc.）预留参数
        page: 1,
        size: 10,
        putAway: "",
        ticketSource: "",
        id: "",
        ticketName: "",
        salesChannels: "",
        sellBeginTime: null,
        sellEndTime: null,
        dateRange: [],
        connectorName: ""
      },
      formData: [],
      formModel: {
        costPrice: null,
        inventory: null,
        permanentSales: 0,
        saleWeekday: [],
        sellBeginTime: null,
        sellEndTime: null,
        sellPrice: null,
        status: 1,
        suggestPrice: null,
        supplyPrice: null
      },
      rules: {},
      tableData: []
    };
  },
  watch: {
    formData: {
      handler: function(val, oldval) {
        this.getDisabledDate();
        this.getFormAddButtonState();
      },
      deep: true
    }
  },
  mounted() {
    this.searchData.tableType = this.activeName;
    this.search("isFirst");
  },
  methods: {
    async tableRowMethod(id, type) {
      let url =
        type == "delete"
          ? "/product/tickets/delete"
          : "/product/tickets/putAwayOrSoldOut";
      let result = await this.$axios.post(url, this.buildFormData({ id: id }));
      if (result.data.code == "1001") {
        this.search();
      }
    },
    rowClick(row, column, cell, event) {
      if (column.label == "商品id" || column.label == "【票型】商品名称") {
        sessionStorage.setItem("goodsListDetailId", row.id);
        this.$router.push("/goodsCenter/goodsList/detail?id=" + row.id);
      }
    },
    async search(param) {
      if (param == "isFirst") {
        this.searchData.page = 1;
        this.searchData.size = 10;
      }
      let result = await this.$axios.post(
        "/product/tickets/selectListWithPage",
        this.buildFormData(this.searchData)
      );
      if (result.data.code == "1001") {
        this.total = result.data.count;
        this.tableData = result.data.datas;
      }
    },
    async showDialog(id) {
      let result = await this.$axios.post(
        "/product/inventoryPrice/selectListWithTickets",
        this.buildFormData({ ticketsId: id })
      );
      if (result.data.code == "1001") {
        result.data.datas.map(item => {
          item.sellBeginTime = new Date(item.sellBeginTime);
          item.sellEndTime = new Date(item.sellEndTime);
          item.dateRange = [item.sellBeginTime, item.sellEndTime];
          item.saleWeekday = (item.saleWeekday.length > 1
            ? item.saleWeekday.split("&&")
            : []
          ).map(function(item) {
            return Number(item);
          });
          item.costPrice = item.costPrice / 100;
          item.sellPrice = item.sellPrice / 100;
          item.suggestPrice = item.suggestPrice / 100;
          item.supplyPrice = item.supplyPrice / 100;
          return item;
        });
        this.formData = result.data.datas;
        this.dialogVisible = true;
      }
    },
    handleSizeChange(val) {
      this.searchData.size = val;
      this.search("search");
    },
    handleCurrentChange(val) {
      this.searchData.page = val;
      this.search("search");
    },
    isWeekOnly(index) {
      this.formData[index].weekLimit = [5, 6];
    },
    addForm() {
      this.formData.push(_cloneDeep(this.formModel));
    },
    removeForm(index) {
      this.formData.splice(index, 1);
    },
    async saveForm() {
      if (this.isDateRangeRepeat()) {
        this.$message.error("日期存在重复区间，请检查");
      } else {
        let objArr = _cloneDeep(this.formData);
        objArr.map(function(item) {
          item.saleWeekday = item.saleWeekday.join("&&");
          item.costPrice = Math.round(item.costPrice * 100);
          item.sellPrice = Math.round(item.sellPrice * 100);
          item.suggestPrice = Math.round(item.suggestPrice * 100);
          item.supplyPrice = Math.round(item.supplyPrice * 100);
        });
        let result = await this.$axios.post(
          "/product/inventoryPrice/save",
          objArr
        );
        if (result.data.code == "1001") {
          this.dialogVisible = false;
        }
      }
    },
    dateRangeChanged(form, index) {
      if (form.dateRange != null && form.dateRange.length == 2) {
        form.sellBeginTime = new Date(form.dateRange[0]);
        form.sellEndTime = new Date(form.dateRange[1]);
      }
    },
    dataChangeToInifity(form, index) {
      if (!Number(form.inventory)) {
        form.inventory = "";
      } else {
        form.inventory = Math.floor(Number(form.inventory));
      }
    },
    getDisabledDate() {
      let _this = this;
      let arr = _this.formData.map(item => {
        return item.dateRange;
      });
      this.pickerOptions = {
        disabledDate: time => {
          let flag = false;
          for (let item of arr) {
            if (item != null && item.length == 2) {
              if (
                time.getTime() >= item[0].getTime() &&
                time.getTime() <= item[1].getTime()
              ) {
                flag = true;
                break;
              }
            }
          }
          return (
            time.getTime() <
              new Date(
                this.$dateFormate(new Date(), "yyyy-MM") +
                  "-" +
                  (new Date().getDate() - 1)
              ).getTime() || flag
          );
        }
      };
    },
    isDateRangeRepeat() {
      let Data = _cloneDeep(this.formData);
      let rangeArr = Data.map(item => {
        return item.dateRange;
      }).filter(function(item) {
        return item != null;
      });
      let arr = rangeArr.reduce(function(a, b) {
        return a.concat(b);
      });
      arr.sort(function(j, k) {
        return j - k;
      });
      let flag = false;
      for (let i = 0; i < rangeArr.length; i++) {
        if (
          rangeArr[i].length == 2 &&
          arr.indexOf(rangeArr[i][0]) - arr.indexOf(rangeArr[i][1]) !== -1
        ) {
          flag = true;
          break;
        }
      }
      console.log(flag);
      return flag;
    },
    getFormAddButtonState() {
      let dataArr = this.formData;
      let arr = dataArr
        .filter(function(item) {
          return item.permanentSales == 1;
        })
        .map(item => {
          return item.permanentSales;
        });
      if (arr.includes(1) && this.formData.length == 1) {
        this.isDialogButtonShow = false;
      } else if (arr.includes(1) && this.formData.length > 1) {
        this.isDialogButtonShow = false;
        this.formData = [this.formData[0]];
        this.formData[0].permanentSales = 1;
      } else {
        this.isDialogButtonShow = true;
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.el-button{
    min-width: 0;
}
.searchBar {
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: flex-end;
  .el-input {
    width: 144px;
    height: 36px;
    margin: 0 5px;
  }
  .el-select {
    margin: 0 5px;
    width: 120px;
  }
  .el-button {
    font-size: 14px;
    padding: 11px 29px;
  }
}
.el-table {
  margin-top: 15px;
  /deep/ .el-button.el-button--default {
    border: none;
    color: #2368d9;
    padding: 0;
  }
}
.formCard {
  background: #f2f2f2;
  margin-bottom: 24px;
  padding: 15px;
  font-size: 12px;
  position: relative;
  /deep/ .el-button--mini.is-circle {
    padding: 1px;
    position: absolute;
    right: -5px;
    top: -5px;
  }
  /deep/ .el-checkbox__label {
    font-size: 12px;
  }
  .inputsBar {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    div {
      flex: 1;
      text-align: center;
      margin-right: 8px;
      margin-bottom: 8px;
      p:first-child {
        line-height: 40px;
      }
      .el-input-number,
      .el-input {
        width: 100%;
      }
      /deep/ .el-input__inner {
        text-align: center;
      }
    }
  }
  .weekBar {
    line-height: 20px;
    span:first-of-type {
      color: #2368d9;
      cursor: pointer;
    }
    .el-checkbox {
      margin-right: 22px;
    }
  }
}
.buttonAdd {
  width: 100%;
  height: 40px;
  color: #444;
  text-align: center;
  line-height: 40px;
  border-radius: 3px;
  border: dashed 1px #cccccc;
  cursor: pointer;
}
.buttonBar {
  text-align: center;
  margin-top: 24px;
  .el-button {
    padding: 12px 29px;
  }
}
</style>

