<template>
  <div class="container">
    <div class="panel">
      <el-steps :active="active"
                align-center>
        <el-step title="申请合作">
        </el-step>
        <el-step title="合作审核"></el-step>
        <el-step title="合作成功"></el-step>
      </el-steps>
      <div v-if="active == 0">
        <div class="searchBox">
          <el-input v-model="params.param"
                    placeholder="请输入账号（手机号）/商户id">
            <template slot="prepend"><i class="iconfont icon-sousuo"></i></template>
          </el-input>
          <button class="btn"
                  @click="searchData(1)">搜索</button>
        </div>
        <el-table :data="list">
          <el-table-column prop="id"
                           label="商户id"></el-table-column>
          <el-table-column prop="mobile"
                           label="商户电话">
            <template slot-scope="scoped">
              {{scoped.row.mobile.substr(0,3)}}****{{scoped.row.mobile.substr(7)}}
            </template>
          </el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scoped">
              <span v-if="scoped.row.status == '1'">已合作</span>
              <el-button type="primary"
                         class="plan-primary"
                         v-else
                         @click="applyCooperate(scoped.row.id)">申请合作</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="text-center"
             v-if="sw">
          <el-pagination @size-change="(val) => {params.size = val;searchData();}"
                         @current-change="(val) => {params.page = val;searchData();}"
                         :current-page="params.page"
                         :page-sizes="[5, 10, 20, 30, 40]"
                         :page-size="params.size"
                         layout="total, sizes, prev, pager, next, jumper"
                         :total="total"></el-pagination>
        </div>
      </div>
      <div v-if="active == 1">
        <div class="notification">
          <p><i class="iconfont icon-success"></i> 提交申请合作成功</p>
          <span>请耐心等待商户审核</span>
          <el-button class="plan-primary"
                     @click="active = 0">继续申请合作</el-button>
        </div>
      </div>
      <div v-if="active == 2">
        <div class="notification">
          <p><i class="iconfont icon-success"></i> 合作成功</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0,
      total: 0,
      params: {
        page: 1,
        size: 10,
        param: ""
      },
      sw: false,
      list: [
        { id: "MP458761657687", mobile: "19991831917", status: "0" },
        { id: "MP458761657688", mobile: "19991831918", status: "1" }
      ]
    };
  },
  mounted() {},
  methods: {
    /**
     * 搜索数据
     */
    async searchData(page = "") {
      if (page) this.params.page = page;
      let result = await this.$axios.post("", this.buildFormData(this.params));
      if (result.data.code == "1001") {
        this.list = result.data.datas;
        if (result.data.datas.length > 10) this.sw = true;
        else this.sw = false;
      }
    },

    /**
     * 申请合作
     * @param id
     */
    applyCooperate(id) {
      this.$confirm(
        '<i class="el-icon-warning" style="margin-left: -17px;"></i> 是否确定要申请与该商户建立合作关系？',
        "提示",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "确定",
          cancelButtonText: "取消"
        }
      )
        .then(() => {
          this.$axios.post("", { id: id }).then(result => {
            if (result.data.code == "1001") {
              this.active = 1;
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消申请"
          });
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.container {
  .panel {
    padding: 20px;
    min-height: calc(100vh - 156px);
    background-color: #ffffff;
    box-shadow: 0px 1px 1.4px 0.6px rgba(0, 0, 0, 0.1);
    border-radius: 3px;
    .el-steps {
      width: 612px;
      margin: 22px auto;
    }
    .searchBox {
      width: 708px;
      margin: 80px auto 60px;
      .el-input {
        width: 600px;
        & + button {
          margin-left: 16px;
        }
      }
    }
    .el-table {
      .el-button {
        width: 86px;
        padding: 10px 0;
        &.plan-primary {
          border-radius: 3px;
          border: solid 1px #1e66dc;
          color: #1e66dc;
          background: #fff;
        }
      }
    }
    .notification {
      width: 150px;
      margin: 118px auto;
      color: #333333;
      text-align: center;
      p {
        font-size: 14px;
        .icon-success {
          font-size: 28px;
          color: #07b851;
          vertical-align: middle;
        }
      }
      span {
        display: block;
        margin: 56px auto 46px;
        font-size: 12px;
      }
      .el-button {
        padding: 10px 16px;
        width: 116px;
        height: 36px;
        &.plan-primary {
          border-radius: 3px;
          border: solid 1px #1e66dc;
          color: #1e66dc;
          background: #fff;
        }
      }
    }
  }
}
</style>

