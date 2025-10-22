<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="等级名称" prop="levelName">
        <el-input
          v-model="queryParams.levelName"
          placeholder="请输入等级名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="isEnabled">
        <el-select v-model="queryParams.isEnabled" placeholder="请选择启用状态" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label" :value="Number(dict.value)" />
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
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['vip:config:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['vip:config:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['vip:config:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="等级" prop="vipLevel" width="80" align="center" />
      <el-table-column label="等级名称" prop="levelName" min-width="120" />
      <el-table-column label="订单阈值" prop="requiredOrders" width="120" align="center" />
      <el-table-column label="固定加成(元)" prop="fixedCommission" width="140" align="center" />
      <el-table-column label="等级图标" prop="levelIcon" min-width="160">
        <template slot-scope="scope">
          <span v-if="scope.row.levelIcon">{{ scope.row.levelIcon }}</span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="160" show-overflow-tooltip />
      <el-table-column label="启用状态" prop="isEnabled" width="110" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.isEnabled" />
        </template>
      </el-table-column>
      <el-table-column label="更新时间" prop="updateTime" min-width="160" align="center">
        <template slot-scope="scope">
          {{ parseTime(scope.row.updateTime) }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220" align="center" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['vip:config:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            :icon="scope.row.isEnabled === 1 ? 'el-icon-close' : 'el-icon-check'"
            @click="handleToggle(scope.row)"
            v-hasPermi="['vip:config:edit']"
          >
            {{ scope.row.isEnabled === 1 ? '停用' : '启用' }}
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['vip:config:remove']"
          >删除</el-button>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="VIP 等级" prop="vipLevel">
          <el-input-number v-model="form.vipLevel" :min="0" :max="99" :controls="false" style="width: 100%;" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="等级名称" prop="levelName">
          <el-input v-model="form.levelName" placeholder="请输入等级名称" />
        </el-form-item>
        <el-form-item label="所需订单数" prop="requiredOrders">
          <el-input-number v-model="form.requiredOrders" :min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="固定加成(元)" prop="fixedCommission">
          <el-input-number v-model="form.fixedCommission" :min="0" :step="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="等级图标" prop="levelIcon">
          <el-input v-model="form.levelIcon" placeholder="请输入等级图标地址" />
        </el-form-item>
        <el-form-item label="启用状态" prop="isEnabled">
          <el-radio-group v-model="form.isEnabled">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listVipConfig,
  getVipConfig,
  addVipConfig,
  updateVipConfig,
  delVipConfig,
  toggleVipConfig,
  exportVipConfig
} from '@/api/vip/config'
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'VipConfig',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      loading: false,
      showSearch: true,
      total: 0,
      configList: [],
      ids: [],
      single: true,
      multiple: true,
      open: false,
      dialogTitle: '',
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        levelName: undefined,
        isEnabled: undefined
      },
      form: {},
      rules: {
        vipLevel: [
          { required: true, message: '请输入VIP等级', trigger: 'blur' }
        ],
        levelName: [
          { required: true, message: '请输入等级名称', trigger: 'blur' }
        ],
        requiredOrders: [
          { type: 'number', required: true, message: '请输入订单阈值', trigger: 'blur' }
        ],
        fixedCommission: [
          { type: 'number', min: 0, message: '固定加成不能小于0', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getList()
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
      listVipConfig(query).then(res => {
        const data = res.data || {}
        this.configList = data.rows || []
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
    handleQuery() {
      this.queryParams.pageNo = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = selection.length === 0
    },
    resetFormData() {
      this.form = {
        id: null,
        vipLevel: undefined,
        levelName: '',
        requiredOrders: 0,
        fixedCommission: 0,
        levelIcon: '',
        isEnabled: 1,
        remark: ''
      }
      this.resetForm('formRef')
    },
    handleAdd() {
      this.resetFormData()
      this.dialogTitle = '新增VIP配置'
      this.open = true
    },
    handleUpdate(row) {
      this.resetFormData()
      getVipConfig(row.id).then(({ data }) => {
        const detail = Object.assign({}, data || {})
        if (detail.fixedCommission === null || detail.fixedCommission === undefined) {
          detail.fixedCommission = 0
        }
        this.form = detail
        this.dialogTitle = '修改VIP配置'
        this.open = true
      })
    },
    submitForm() {
      this.$refs.formRef.validate(valid => {
        if (!valid) {
          return
        }
        const payload = Object.assign({}, this.form)
        payload.vipLevel = Number(payload.vipLevel || 0)
        payload.fixedCommission = Number(payload.fixedCommission || 0)
        payload.requiredOrders = Number(payload.requiredOrders || 0)
        if (payload.isEnabled === undefined || payload.isEnabled === null) {
          payload.isEnabled = 1
        }
        const request = payload.id
          ? updateVipConfig(payload.id, payload)
          : addVipConfig(payload)
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
        return
      }
      this.$modal.confirm(`是否确认删除编号为【${ids.join(', ')}】的配置？`).then(() => {
        return delVipConfig(ids.join(','))
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.getList()
      }).catch(() => {})
    },
    handleToggle(row) {
      const targetEnabled = row.isEnabled !== 1
      const text = targetEnabled ? '启用' : '停用'
      this.$modal.confirm(`确认${text}等级【${row.vipLevel}】吗？`).then(() => {
        return toggleVipConfig(row.id, targetEnabled)
      }).then(() => {
        this.$modal.msgSuccess(`${text}成功`)
        row.isEnabled = targetEnabled ? 1 : 0
      }).catch(() => {})
    },
    handleExport() {
      const ids = this.ids
      exportVipConfig(ids).then(res => {
        this.$modal.msgSuccess(`导出任务提交成功，记录数：${res.data}`)
      })
    }
  }
}
</script>
