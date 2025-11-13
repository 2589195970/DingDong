<template>
  <div class="app-container">
    <el-form
      v-show="showSearch"
      ref="queryForm"
      :model="queryParams"
      size="small"
      :inline="true"
      label-width="90px"
    >
      <el-form-item label="代理名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入代理名称"
          clearable
          style="width: 150px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="代理编码" prop="agentCode">
        <el-input
          v-model="queryParams.agentCode"
          placeholder="请输入代理编码"
          clearable
          style="width: 150px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="isEnabled">
        <el-select v-model="queryParams.isEnabled" placeholder="请选择状态" clearable style="width: 150px">
          <el-option label="启用" :value="0" />
          <el-option label="禁用" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="实名状态" prop="isRealName">
        <el-select v-model="queryParams.isRealName" placeholder="请选择实名状态" clearable style="width: 150px">
          <el-option label="未实名" :value="0" />
          <el-option label="已实名" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-refresh" size="mini" @click="getList">刷新</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-money"
          size="mini"
          :disabled="single"
          @click="openBalanceDialog()"
        >余额调整</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-star-on"
          size="mini"
          :disabled="single"
          @click="openVipDialog()"
        >设置VIP</el-button>
      </el-col>
      <right-toolbar :show-search.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table
      v-loading="loading"
      :data="agentList"
      row-key="agentAccountId"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" prop="agentAccountId" align="center" width="90" />
      <el-table-column label="代理编码" prop="agentCode" show-overflow-tooltip />
      <el-table-column label="代理名称" prop="agentName" show-overflow-tooltip />
      <el-table-column label="手机号" prop="phone" width="130" />
      <el-table-column label="VIP等级" prop="level" width="140">
        <template slot-scope="scope">
          {{ formatAgentVipLevel(scope.row) }}
        </template>
      </el-table-column>
      <el-table-column label="实名状态" prop="isRealName" width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isRealName === 1" type="success">已实名</el-tag>
          <el-tag v-else type="info">未实名</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="启用状态" prop="isEnabled" width="110" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isEnabled === 0" type="success">启用</el-tag>
          <el-tag v-else type="danger">禁用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="订单加解密" prop="isEncrypt" width="120" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isEncrypt === 0" type="success">订单加密</el-tag>
          <el-tag v-else type="warning">订单解密</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="注册时间" prop="createTime" width="180" align="center">
        <template slot-scope="scope">
          {{ formatTimestamp(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="360" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="openVipDialog(scope.row)">设置VIP</el-button>
          <el-button type="text" size="mini" @click="openBalanceDialog(scope.row)">调整余额</el-button>
          <el-button type="text" size="mini" @click="handleToggleEncrypt(scope.row)">
            {{ scope.row.isEncrypt === 0 ? '订单解密' : '订单加密' }}
          </el-button>
          <el-button type="text" size="mini" @click="handleToggleStatus(scope.row)">
            {{ scope.row.isEnabled === 0 ? '禁用' : '启用' }}
          </el-button>
          <el-button type="text" size="mini" @click="handleLoginFree(scope.row)">免密登录</el-button>
          <el-button type="text" size="mini" @click="handleDelete(scope.row)">删除</el-button>
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

    <el-dialog title="调整余额" :visible.sync="balanceDialogVisible" width="420px" append-to-body>
      <el-form ref="balanceFormRef" :model="balanceForm" :rules="balanceRules" label-width="110px">
        <el-form-item label="调整类型" prop="type">
          <el-select v-model="balanceForm.type" placeholder="请选择类型">
            <el-option label="余额增加" value="0" />
            <el-option label="余额减少" value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额（元）" prop="amount">
          <el-input v-model="balanceForm.amount" placeholder="请输入调整金额" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="balanceSubmitting" @click="balanceDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="balanceSubmitting" @click="submitBalance">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog title="设置VIP等级" :visible.sync="vipDialogVisible" width="420px" append-to-body>
      <el-form ref="vipFormRef" :model="vipForm" :rules="vipFormRules" label-width="110px">
        <el-form-item label="VIP等级" prop="vipLevel">
          <el-select v-model="vipForm.vipLevel" placeholder="请选择VIP等级">
            <el-option v-for="item in vipLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="vipForm.remark" type="textarea" :rows="3" placeholder="可填写备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button :disabled="vipSubmitting" @click="vipDialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="vipSubmitting" @click="submitVipLevel">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import RightToolbar from '@/components/RightToolbar'
import { parseTime } from '@/utils/ruoyi'
import { normalizeVipLevel, resolveVipLevelLabel } from '@/utils/vip'
import {
  selectChildAgentList,
  deleteAgentAccount,
  updateBalance,
  updateAgentStatus,
  updateAgentEncryptStatus,
  loginFreePassword
} from '@/api/monitor/business'
import { listVipConfig } from '@/api/vip/config'
import { listVipUser, addVipUser, setVipLevel } from '@/api/vip/user'
import { setToken } from '@/utils/auth'

const amountValidator = (rule, value, callback) => {
  const num = Number(value)
  if (Number.isNaN(num) || num <= 0) {
    callback(new Error('金额需为正数'))
  } else {
    callback()
  }
}

export default {
  name: 'AgentChildList',
  components: { RightToolbar },
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      agentList: [],
      fullList: [],
      ids: [],
      single: true,
      multiple: true,
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        agentName: undefined,
        agentCode: undefined,
        isEnabled: undefined,
        isRealName: undefined
      },
      balanceDialogVisible: false,
      balanceSubmitting: false,
      balanceForm: {
        agentAccountId: null,
        type: '0',
        amount: ''
      },
      balanceRules: {
        type: [{ required: true, message: '请选择调整类型', trigger: 'change' }],
        amount: [
          { required: true, message: '请输入金额', trigger: 'blur' },
          { validator: amountValidator, trigger: 'blur' }
        ]
      },
      vipDialogVisible: false,
      vipSubmitting: false,
      vipForm: {
        vipLevel: null,
        remark: ''
      },
      vipFormRules: {
        vipLevel: [{ required: true, message: '请选择VIP等级', trigger: 'change' }]
      },
      vipLevelOptions: [],
      currentAgent: null,
      vipRecordCache: {}
    }
  },
  watch: {
    'queryParams.pageNo'(val, oldVal) {
      if (val !== oldVal) {
        this.applyFilters()
      }
    },
    'queryParams.pageSize'(val, oldVal) {
      if (val !== oldVal) {
        this.queryParams.pageNo = 1
        this.applyFilters()
      }
    }
  },
  created() {
    this.fetchVipLevelOptions()
    this.getList()
  },
  methods: {
    parseTime,
    async getList() {
      this.loading = true
      try {
        const params = {
          pageNo: this.queryParams.pageNo,
          pageSize: this.queryParams.pageSize,
          agentName: this.queryParams.agentName,
          agentCode: this.queryParams.agentCode,
          isEnabled: this.queryParams.isEnabled,
          isRealName: this.queryParams.isRealName
        }
        const res = await selectChildAgentList(params)
        const rows = Array.isArray(res?.data) ? res.data : []
        this.fullList = rows
        this.applyFilters()
      } catch (error) {
        console.error('getList error', error)
      } finally {
        this.loading = false
      }
    },
    applyFilters() {
      const { agentName, agentCode, isEnabled, isRealName, pageNo, pageSize } = this.queryParams
      let rows = Array.isArray(this.fullList) ? [...this.fullList] : []
      if (agentName) {
        const keyword = agentName.trim()
        rows = rows.filter(item => item.agentName && item.agentName.includes(keyword))
      }
      if (agentCode) {
        const keyword = agentCode.trim()
        rows = rows.filter(item => item.agentCode && item.agentCode.includes(keyword))
      }
      if (isEnabled !== undefined && isEnabled !== null && isEnabled !== '') {
        rows = rows.filter(item => String(item.isEnabled) === String(isEnabled))
      }
      if (isRealName !== undefined && isRealName !== null && isRealName !== '') {
        rows = rows.filter(item => String(item.isRealName) === String(isRealName))
      }
      this.total = rows.length
      const start = (pageNo - 1) * pageSize
      this.agentList = rows.slice(start, start + pageSize)
    },
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    resetQuery() {
      if (this.$refs.queryForm) {
        this.$refs.queryForm.resetFields()
      }
      this.queryParams.pageNo = 1
      this.queryParams.pageSize = 10
      this.getList()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.agentAccountId)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    formatTimestamp(timestamp) {
      if (!timestamp) {
        return '-'
      }
      return parseTime(timestamp)
    },
    formatAgentVipLevel(agent = {}) {
      const level = normalizeVipLevel(agent.vipLevel != null ? agent.vipLevel : agent.level)
      if (level === null || level === undefined) {
        return '-'
      }
      const matched = this.vipLevelOptions.find(item => normalizeVipLevel(item.value) === level)
      if (matched) {
        return matched.label
      }
      return `${level}级-${resolveVipLevelLabel({ vipLevel: level })}`
    },
    resolveAgent(agent) {
      if (agent && agent.agentAccountId) {
        return agent
      }
      if (this.ids.length === 1) {
        return (
          this.agentList.find(item => item.agentAccountId === this.ids[0]) ||
          this.fullList.find(item => item.agentAccountId === this.ids[0]) ||
          null
        )
      }
      return null
    },
    openBalanceDialog(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        this.$message.warning('请选择代理商')
        return
      }
      this.balanceForm = {
        agentAccountId: target.agentAccountId,
        type: '0',
        amount: ''
      }
      this.balanceDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.balanceFormRef) {
          this.$refs.balanceFormRef.clearValidate()
        }
      })
    },
    submitBalance() {
      if (!this.$refs.balanceFormRef) {
        return
      }
      this.$refs.balanceFormRef.validate(async valid => {
        if (!valid) {
          return
        }
        this.balanceSubmitting = true
        try {
          const payload = {
            agentAccountId: this.balanceForm.agentAccountId,
            type: this.balanceForm.type,
            balanceYun: this.balanceForm.amount
          }
          await updateBalance(payload)
          this.$message.success('操作成功')
          this.balanceDialogVisible = false
          this.getList()
        } catch (error) {
          console.error('submitBalance error', error)
        } finally {
          this.balanceSubmitting = false
        }
      })
    },
    handleToggleStatus(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        return
      }
      const next = target.isEnabled === 0 ? 1 : 0
      const actionText = next === 0 ? '启用' : '禁用'
      this.$confirm(`确认要${actionText}该代理商吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const payload = { ...target, isEnabled: next }
          return updateAgentStatus(payload)
        })
        .then(() => {
          this.$message.success(`${actionText}成功`)
          this.getList()
        })
        .catch(() => {})
    },
    handleToggleEncrypt(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        return
      }
      const next = target.isEncrypt === 0 ? 1 : 0
      const actionText = next === 0 ? '订单加密' : '订单解密'
      const process = () => {
        const payload = { ...target, isEncrypt: next }
        updateAgentEncryptStatus(payload)
          .then(() => {
            this.$message.success(`${actionText}成功`)
            this.getList()
          })
          .catch(error => {
            console.error('handleToggleEncrypt error', error)
          })
      }
      if (next === 1) {
        process()
      } else {
        this.$confirm('确认要加密订单吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            process()
          })
          .catch(() => {})
      }
    },
    handleDelete(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        return
      }
      this.$confirm(`确认要删除代理商【${target.agentName}】吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => deleteAgentAccount(target.agentAccountId))
        .then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
        .catch(() => {})
    },
    async handleLoginFree(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        return
      }
      if (!target.phone) {
        this.$message.warning('该代理缺少手机号，无法免密登录')
        return
      }
      try {
        const res = await loginFreePassword({ username: target.phone })
        const token = res && res.token
        if (!token) {
          this.$message.error('免密登录失败，未返回token')
          return
        }
        setToken(token)
        this.$message.success('免密登录成功，正在跳转')
        this.$router.push('/login')
        this.$router.go(0)
      } catch (error) {
        console.error('handleLoginFree error', error)
      }
    },
    openVipDialog(agent) {
      const target = this.resolveAgent(agent)
      if (!target) {
        this.$message.warning('请选择代理商')
        return
      }
      if (!this.vipLevelOptions.length) {
        this.fetchVipLevelOptions()
      }
      const defaultLevel = normalizeVipLevel(target.vipLevel != null ? target.vipLevel : target.level)
      const fallback = this.vipLevelOptions.length ? normalizeVipLevel(this.vipLevelOptions[0].value) : 0
      this.currentAgent = target
      this.vipForm = {
        vipLevel: defaultLevel !== null ? defaultLevel : fallback,
        remark: ''
      }
      this.vipDialogVisible = true
      this.$nextTick(() => {
        if (this.$refs.vipFormRef) {
          this.$refs.vipFormRef.clearValidate()
        }
      })
    },
    submitVipLevel() {
      if (!this.$refs.vipFormRef) {
        return
      }
      this.$refs.vipFormRef.validate(async valid => {
        if (!valid) {
          return
        }
        if (!this.currentAgent) {
          this.$message.warning('缺少代理商信息')
          return
        }
        this.vipSubmitting = true
        try {
          const level = normalizeVipLevel(this.vipForm.vipLevel)
          const vipRecord = await this.ensureVipRecord(this.currentAgent, level)
          if (!vipRecord || !vipRecord.id) {
            this.$message.error('无法找到对应的VIP记录')
            return
          }
          await setVipLevel({
            id: vipRecord.id,
            vipLevel: level,
            remark: this.vipForm.remark
          })
          this.$message.success('设置成功')
          this.vipDialogVisible = false
          this.currentAgent.level = level
          this.currentAgent.vipLevel = level
          this.vipRecordCache[this.currentAgent.agentCode] = {
            ...vipRecord,
            vipLevel: level
          }
          this.applyFilters()
        } catch (error) {
          console.error('submitVipLevel error', error)
        } finally {
          this.vipSubmitting = false
        }
      })
    },
    async ensureVipRecord(agent, vipLevel) {
      if (!agent || !agent.agentCode) {
        throw new Error('缺少代理商编码')
      }
      const cache = this.vipRecordCache[agent.agentCode]
      if (cache && cache.id) {
        return cache
      }
      let record = await this.fetchVipRecord(agent.agentCode)
      if (record && record.id) {
        this.vipRecordCache[agent.agentCode] = record
        return record
      }
      if (!agent.sysUserId || !agent.agentAccountId) {
        throw new Error('代理商缺少ID，无法创建VIP记录')
      }
      await addVipUser({
        agentAccountId: agent.agentAccountId,
        userId: agent.sysUserId,
        agentCode: agent.agentCode,
        agentName: agent.agentName,
        vipLevel: vipLevel ?? 0,
        remark: '我的代理商自动创建VIP记录'
      })
      record = await this.fetchVipRecord(agent.agentCode)
      if (record && record.id) {
        this.vipRecordCache[agent.agentCode] = record
      }
      return record
    },
    async fetchVipRecord(agentCode) {
      if (!agentCode) {
        return null
      }
      try {
        const res = await listVipUser({
          pageNo: 1,
          pageSize: 1,
          agentCode
        })
        const rows = res?.data?.rows
        return Array.isArray(rows) && rows.length ? rows[0] : null
      } catch (error) {
        console.error('fetchVipRecord error', error)
        return null
      }
    },
    fetchVipLevelOptions() {
      const fallback = [
        { value: 0, label: '0级-普通会员' },
        { value: 1, label: '1级-青铜会员' },
        { value: 2, label: '2级-银牌会员' },
        { value: 3, label: '3级-金牌会员' },
        { value: 4, label: '4级-白金会员' },
        { value: 5, label: '5级-钻石会员' }
      ]
      listVipConfig({ pageNo: 1, pageSize: 100, isEnabled: 1 })
        .then(res => {
          const rows = res?.data?.rows || []
          const options = rows
            .map(item => {
              const level = normalizeVipLevel(item.vipLevel)
              if (level === null) {
                return null
              }
              return {
                value: level,
                label: `${level}级-${item.levelName || resolveVipLevelLabel({ vipLevel: level })}`
              }
            })
            .filter(Boolean)
            .sort((a, b) => normalizeVipLevel(a.value) - normalizeVipLevel(b.value))
          this.vipLevelOptions = options.length ? options : fallback
        })
        .catch(() => {
          this.vipLevelOptions = fallback
        })
    }
  }
}
</script>
