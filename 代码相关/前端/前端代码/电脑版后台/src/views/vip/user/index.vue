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
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery" v-hasPermi="['vip:user:query']">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['vip:user:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleEdit" v-hasPermi="['vip:user:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-s-operation" size="mini" :disabled="single" @click="handleSetLevelBatch" v-hasPermi="['vip:user:setLevel']">设置VIP等级</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['vip:user:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-download" size="mini" @click="handleExport" v-hasPermi="['vip:user:export']">导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-refresh-right"
          size="mini"
          :loading="supplementLoading"
          @click="handleSupplement"
          v-hasPermi="['vip:user:supplement']"
        >补录VIP用户</el-button>
      </el-col>
      <el-col :span="6" v-if="supplementTipVisible">
        <el-alert
          type="success"
          show-icon
          :closable="false"
          :title="`最近一次补录新增 ${lastSupplementCount} 条记录`"
        />
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList" />
    </el-row>

    <el-table v-loading="loading" :data="vipUserList" row-key="id" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="用户ID" prop="userId" align="center" width="100" />
      <el-table-column label="代理编码" prop="agentCode" show-overflow-tooltip align="center" />
      <el-table-column label="代理名称" prop="agentName" show-overflow-tooltip align="center" />
      <el-table-column label="VIP等级" prop="vipLevel" align="center" width="120">
        <template slot-scope="scope">
          {{ formatVipLevel(scope.row.vipLevel) }}
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" show-overflow-tooltip />
      <el-table-column label="更新时间" prop="updateTime" align="center" width="180">
        <template slot-scope="scope">
          {{ parseTime(scope.row.updateTime) }}
        </template>
      </el-table-column>
      <el-table-column label="创建时间" prop="createTime" align="center" width="180">
        <template slot-scope="scope">
          {{ parseTime(scope.row.createTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="240">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleEdit(scope.row)" v-hasPermi="['vip:user:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-star-on" @click="handleSetLevel(scope.row)" v-hasPermi="['vip:user:setLevel']">设置等级</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['vip:user:remove']">删除</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="110px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="代理编码" prop="agentCode">
          <el-input v-model="form.agentCode" placeholder="请输入代理编码" />
        </el-form-item>
        <el-form-item label="代理名称" prop="agentName">
          <el-input v-model="form.agentName" placeholder="请输入代理名称" />
        </el-form-item>
        <el-form-item label="VIP等级" prop="vipLevel">
          <el-select v-model="form.vipLevel" placeholder="请选择VIP等级">
            <el-option v-for="item in vipLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="3" v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="设置VIP等级" :visible.sync="setLevelOpen" width="500px" append-to-body>
      <el-form ref="setLevelRef" :model="setLevelForm" :rules="setLevelRules" label-width="110px">
        <el-form-item label="VIP等级" prop="vipLevel">
          <el-select v-model="setLevelForm.vipLevel" placeholder="请选择VIP等级">
            <el-option v-for="item in vipLevelOptions" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input type="textarea" :rows="3" v-model="setLevelForm.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitSetLevel">确 定</el-button>
        <el-button @click="setLevelOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listVipUser,
  getVipUser,
  addVipUser,
  updateVipUser,
  delVipUser,
  exportVipUser,
  setVipLevel,
  supplementVipUser
} from '@/api/vip/user'
import { listVipConfig } from '@/api/vip/config'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'VipUser',
  data() {
    return {
      loading: false,
      supplementLoading: false,
      showSearch: true,
      total: 0,
      vipUserList: [],
      ids: [],
      single: true,
      multiple: true,
      open: false,
      dialogTitle: '',
      setLevelOpen: false,
      vipLevelOptions: [],
      supplementTipVisible: false,
      lastSupplementCount: 0,
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        agentCode: undefined,
        agentName: undefined,
        vipLevel: undefined
      },
      form: {},
      setLevelForm: {
        id: null,
        vipLevel: null,
        remark: ''
      },
      rules: {
        userId: [
          { required: true, message: '请输入用户ID', trigger: 'blur' }
        ],
        agentCode: [
          { required: true, message: '请输入代理编码', trigger: 'blur' }
        ],
        agentName: [
          { required: true, message: '请输入代理名称', trigger: 'blur' }
        ],
        vipLevel: [
          { required: true, message: '请选择VIP等级', trigger: 'change' }
        ]
      },
      setLevelRules: {
        vipLevel: [
          { required: true, message: '请选择VIP等级', trigger: 'change' }
        ]
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
      listVipUser(query).then(res => {
        const data = res.data || {}
        this.vipUserList = data.rows || []
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
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    resetFormData() {
      this.form = {
        id: null,
        userId: null,
        agentCode: '',
        agentName: '',
        vipLevel: null,
        remark: ''
      }
    },
    handleAdd() {
      this.resetFormData()
      if (this.vipLevelOptions.length > 0) {
        this.form.vipLevel = this.vipLevelOptions[0].value
      }
      this.dialogTitle = '新增VIP用户'
      this.open = true
      this.$nextTick(() => {
        this.$refs.formRef && this.$refs.formRef.clearValidate()
      })
    },
    handleEdit(row) {
      const target = row && row.id ? row.id : (this.ids[0] || null)
      if (!target) {
        this.$modal.msgWarning('请选择需要修改的数据')
        return
      }
      this.resetFormData()
      getVipUser(target).then(({ data }) => {
        const detail = Object.assign({}, data || {})
        this.form = detail
        this.dialogTitle = '修改VIP用户'
        this.open = true
        this.$nextTick(() => {
          this.$refs.formRef && this.$refs.formRef.clearValidate()
        })
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.form)
        if (payload.userId !== undefined && payload.userId !== null && payload.userId !== '') {
          const userIdNumber = Number(payload.userId)
          payload.userId = Number.isNaN(userIdNumber) ? payload.userId : userIdNumber
        } else {
          payload.userId = null
        }
        payload.vipLevel = Number(payload.vipLevel || 0)
        const request = payload.id ? updateVipUser(payload.id, payload) : addVipUser(payload)
        request.then(() => {
          this.$modal.msgSuccess(payload.id ? '修改成功' : '新增成功')
          this.open = false
          this.getList()
        })
      })
    },
    cancel() {
      this.open = false
    },
    handleDelete(row) {
      const ids = row && row.id ? [row.id] : this.ids
      if (!ids.length) {
        this.$modal.msgWarning('请选择需要删除的数据')
        return
      }
      this.$modal.confirm(`是否确认删除编号为【${ids.join(', ')}】的数据项？`).then(() => {
        return delVipUser(ids.join(','))
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    handleExport() {
      exportVipUser(this.ids).then(res => {
        this.$modal.msgSuccess(`导出任务提交成功，记录数：${res.data}`)
      })
    },
    handleSupplement() {
      this.$modal.confirm('确认执行VIP用户补录操作？').then(() => {
        this.supplementLoading = true
        return supplementVipUser()
      }).then(res => {
        const count = Number(res && res.data ? res.data : 0)
        if (count > 0) {
          this.lastSupplementCount = count
          this.supplementTipVisible = true
          this.$modal.msgSuccess(`补录完成，共新增 ${count} 条VIP用户记录`)
        } else {
          this.supplementTipVisible = false
          this.$modal.msg('本次未新增任何VIP用户记录')
        }
        this.getList()
      }).catch(() => {}).finally(() => {
        this.supplementLoading = false
      })
    },
    handleSetLevel(row) {
      if (!row || !row.id) {
        this.$modal.msgWarning('当前行数据异常，无法设置等级')
        return
      }
      this.setLevelForm = {
        id: row.id,
        vipLevel: row.vipLevel,
        remark: ''
      }
      if (this.setLevelForm.vipLevel === undefined || this.setLevelForm.vipLevel === null) {
        this.setLevelForm.vipLevel = this.vipLevelOptions.length ? this.vipLevelOptions[0].value : 0
      }
      this.setLevelOpen = true
      this.$nextTick(() => {
        this.$refs.setLevelRef && this.$refs.setLevelRef.clearValidate()
      })
    },
    handleSetLevelBatch() {
      if (this.single) {
        this.$modal.msgWarning('请选择一条需要设置等级的数据')
        return
      }
      const targetId = this.ids[0]
      const row = this.vipUserList.find(item => item.id === targetId)
      if (row) {
        this.handleSetLevel(row)
      } else {
        this.$modal.msgWarning('未找到选中的数据，请重试')
      }
    },
    submitSetLevel() {
      this.$refs.setLevelRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.setLevelForm)
        if (!payload.id) {
          this.$modal.msgWarning('缺少用户标识，无法设置等级')
          return
        }
        payload.vipLevel = Number(payload.vipLevel)
        setVipLevel(payload).then(() => {
          this.$modal.msgSuccess('设置成功')
          this.setLevelOpen = false
          this.getList()
        })
      })
    }
  }
}
</script>
