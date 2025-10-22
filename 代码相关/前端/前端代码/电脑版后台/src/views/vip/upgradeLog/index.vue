<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="代理编码" prop="agentCode">
        <el-input
          v-model="queryParams.agentCode"
          placeholder="请输入代理编码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入代理名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="VIP等级" prop="vipLevel">
        <el-select v-model="queryParams.vipLevel" placeholder="请选择等级" clearable style="width: 180px">
          <el-option v-for="item in vipLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="升级类型" prop="upgradeType">
        <el-select v-model="queryParams.upgradeType" placeholder="请选择类型" clearable style="width: 160px">
          <el-option v-for="item in upgradeTypeOptions" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" v-hasPermi="['vip:upgradeLog:query']">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['vip:upgradeLog:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="logList" row-key="id" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" prop="userId" width="100" align="center" />
      <el-table-column label="代理编码" prop="agentCode" show-overflow-tooltip />
      <el-table-column label="代理名称" prop="agentName" show-overflow-tooltip />
      <el-table-column label="升级前等级" prop="fromLevel" width="120" align="center">
        <template slot-scope="scope">
          {{ formatVipLevel(scope.row.fromLevel) }}
        </template>
      </el-table-column>
      <el-table-column label="升级后等级" prop="toLevel" width="120" align="center">
        <template slot-scope="scope">
          {{ formatVipLevel(scope.row.toLevel) }}
        </template>
      </el-table-column>
      <el-table-column label="升级类型" prop="upgradeType" width="120" align="center">
        <template slot-scope="scope">
          {{ formatUpgradeType(scope.row.upgradeType) }}
        </template>
      </el-table-column>
      <el-table-column label="升级原因" prop="upgradeReason" show-overflow-tooltip />
      <el-table-column label="订单数量" prop="orderCount" width="120" align="center" />
      <el-table-column label="操作人" prop="operatorName" width="140" align="center" show-overflow-tooltip />
      <el-table-column label="创建时间" prop="createTime" width="180" align="center">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNo"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listVipUpgradeLog, exportVipUpgradeLog } from '@/api/vip/upgradeLog'
import { listVipConfig } from '@/api/vip/config'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'VipUpgradeLog',
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      logList: [],
      ids: [],
      vipLevelOptions: [],
      upgradeTypeOptions: [
        { label: '自动升级', value: 'AUTO' },
        { label: '手动升级', value: 'MANUAL' }
      ],
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        agentCode: undefined,
        agentName: undefined,
        vipLevel: undefined,
        upgradeType: undefined
      }
    }
  },
  created() {
    this.getList()
    this.fetchVipLevelOptions()
  },
  methods: {
    parseTime,
    getList(pagination) {
      if (pagination) {
        if (pagination.page !== undefined) {
          this.queryParams.pageNo = pagination.page
        }
        if (pagination.limit !== undefined) {
          this.queryParams.pageSize = pagination.limit
        }
      }
      this.loading = true
      const query = Object.assign({}, this.queryParams)
      if (query.vipLevel !== undefined && query.vipLevel !== null && query.vipLevel !== '') {
        query.vipLevel = Number(query.vipLevel)
      }
      listVipUpgradeLog(query).then(res => {
        const data = res.data || {}
        this.logList = data.rows || []
        this.total = data.totalRows || 0
        if (typeof data.pageNo === 'number') {
          this.queryParams.pageNo = data.pageNo
        }
        if (typeof data.pageSize === 'number') {
          this.queryParams.pageSize = data.pageSize
        }
      }).finally(() => {
        this.loading = false
      })
    },
    fetchVipLevelOptions() {
      const fallback = [
        { value: 0, label: '0级-普通会员' },
        { value: 1, label: '1级-铜牌会员' },
        { value: 2, label: '2级-银牌会员' },
        { value: 3, label: '3级-金牌会员' },
        { value: 4, label: '4级-白金会员' },
        { value: 5, label: '5级-钻石会员' }
      ]
      listVipConfig({ pageNo: 1, pageSize: 100, isEnabled: 1 }).then(res => {
        const data = res.data || {}
        const rows = data.rows || []
        this.vipLevelOptions = rows.map(item => ({
          value: item.vipLevel,
          label: `${item.vipLevel}级-${item.levelName}`
        }))
        if (!this.vipLevelOptions.length) {
          this.vipLevelOptions = fallback
        }
      }).catch(() => {
        this.vipLevelOptions = fallback
      })
    },
    formatVipLevel(level) {
      const target = this.vipLevelOptions.find(item => item.value === level)
      return target ? target.label : level
    },
    formatUpgradeType(type) {
      const target = this.upgradeTypeOptions.find(item => item.value === type)
      return target ? target.label : type
    },
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryParams.pageNo = 1
      this.queryParams.pageSize = 10
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
    },
    handleExport() {
      exportVipUpgradeLog(this.ids).then(res => {
        this.$modal.msgSuccess(`导出任务提交成功，记录数：${res.data}`)
      })
    }
  }
}
</script>
