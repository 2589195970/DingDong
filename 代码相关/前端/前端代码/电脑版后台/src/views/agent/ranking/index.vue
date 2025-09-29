<template>
  <div class="app-container">

    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="代理商姓名" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入代理商姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="排名类型" prop="rankingType">
        <el-select v-model="queryParams.rankingType" placeholder="请选择排名类型" clearable>
          <el-option
            v-for="dict in rankingTypeOptions"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['agent:ranking:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-refresh"
          size="mini"
          @click="handleRefresh"
        >刷新</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="agentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="排名" align="center" prop="ranking" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.ranking <= 3" :type="getRankingTagType(scope.row.ranking)" size="small">
            {{ scope.row.ranking }}
          </el-tag>
          <span v-else>{{ scope.row.ranking }}</span>
        </template>
      </el-table-column>
      <el-table-column label="代理商编码" align="center" prop="agentCode" />
      <el-table-column label="代理商姓名" align="center" prop="agentName" :show-overflow-tooltip="true" />
      <el-table-column label="订单总数" align="center" prop="totalOrders">
        <template slot-scope="scope">
          <span class="number-text">{{ formatNumber(scope.row.totalOrders) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="激活订单数" align="center" prop="activatedOrders">
        <template slot-scope="scope">
          <span class="number-text">{{ formatNumber(scope.row.activatedOrders) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="佣金总额" align="center" prop="totalCommission">
        <template slot-scope="scope">
          <span class="money-text">¥{{ formatMoney(scope.row.totalCommission) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="下游代理数" align="center" prop="downstreamAgentCount">
        <template slot-scope="scope">
          <span class="number-text">{{ formatNumber(scope.row.downstreamAgentCount) }}</span>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNo"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { getAgentRankingPage, exportAgentRanking } from "@/api/agent";

export default {
  name: "AgentRanking",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 代理商排名表格数据
      agentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        agentName: null,
        rankingType: 0
      },
      // 排名类型字典
      rankingTypeOptions: [
        { value: 0, label: "总排名" },
        { value: 1, label: "本月排名" },
        { value: 2, label: "昨日排名" },
        { value: 3, label: "今日排名" }
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询代理商排名列表 */
    getList() {
      this.loading = true;
      getAgentRankingPage(this.queryParams).then(response => {
        this.agentList = response.data.rows;
        this.total = response.data.totalRows;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        agentCode: null,
        agentName: null,
        totalOrders: null,
        activatedOrders: null,
        totalCommission: null,
        downstreamAgentCount: null,
        ranking: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNo = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.agentCode)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加代理商排名";
    },
    /** 刷新按钮操作 */
    handleRefresh() {
      this.getList();
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('agentManagement/exportAgentRanking', {
        ...this.queryParams
      }, `代理商排名_${new Date().getTime()}.xlsx`)
    },
    /** 格式化数字 */
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      } else if (num >= 1000) {
        return (num / 1000).toFixed(1) + 'k'
      }
      return num.toString()
    },
    /** 格式化金额 */
    formatMoney(amount) {
      // 金额单位是分，转换为元
      const yuan = (amount / 100).toFixed(2);
      return yuan;
    },
    /** 获取排名标签类型 */
    getRankingTagType(ranking) {
      switch (ranking) {
        case 1:
          return 'danger'; // 第一名 - 红色
        case 2:
          return 'warning'; // 第二名 - 橙色
        case 3:
          return 'success'; // 第三名 - 绿色
        default:
          return 'info'; // 其他 - 蓝色
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.app-container {
  padding: 20px;
}

.number-text {
  font-weight: bold;
  color: #409EFF;
}

.money-text {
  font-weight: bold;
  color: #67C23A;
}

.rate-text {
  font-weight: bold;
  color: #E6A23C;
}

.el-table {
  .el-tag {
    font-weight: bold;
  }
}

.mb8 {
  margin-bottom: 8px;
}

</style>
