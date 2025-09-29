<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="代理商名称" prop="agentName">
        <el-input
          v-model="queryParams.agentName"
          placeholder="请输入代理商名称"
          clearable
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="代理商编码" prop="agentCode">
        <el-input
          v-model="queryParams.agentCode"
          placeholder="请输入代理商编码"
          clearable
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="抖音号" prop="douyinAccount">
        <el-input
          v-model="queryParams.douyinAccount"
          placeholder="请输入抖音号"
          clearable
          style="width: 200px"
        />
      </el-form-item>
      <el-form-item label="审核状态" prop="status">
        <el-select
          v-model="queryParams.status"
          placeholder="请选择审核状态"
          clearable
          style="width: 200px"
        >
          <el-option label="待认证" :value="0" />
          <el-option label="审核失败" :value="1" />
          <el-option label="审核成功" :value="2" />
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
          v-hasPermi="['agent:liveaudit:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-setting"
          size="mini"
          @click="handleConfig"
          v-hasPermi="['agent:liveaudit:config']"
        >配置说明</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table ref="tables" v-loading="loading" :data="liveAuditList" border>
      <el-table-column label="审核ID" align="center" prop="liveAuditId" />

      <el-table-column label="代理商信息" align="left" min-width="120">
        <template slot-scope="scope">
          <div>代理商名称：{{ scope.row.agentName }}</div>
          <div>代理商编码：{{ scope.row.agentCode }}</div>
        </template>
      </el-table-column>

      <el-table-column label="抖音信息" align="left" min-width="120">
        <template slot-scope="scope">
          <div>抖音UID：{{ scope.row.douyinUid }}</div>
          <div>抖音号：{{ scope.row.douyinAccount }}</div>
        </template>
      </el-table-column>

      <el-table-column label="直播背景图" align="center" min-width="100">
        <template slot-scope="scope">
          <el-image
            v-if="scope.row.backgroundImage"
            :src="scope.row.backgroundImage"
            :preview-src-list="[scope.row.backgroundImage]"
            style="width: 60px; height: 40px"
            fit="cover"
          />
          <span v-else>暂无图片</span>
        </template>
      </el-table-column>

      <el-table-column label="审核状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 0" type="warning">待认证</el-tag>
          <el-tag v-else-if="scope.row.status === 1" type="danger">审核失败</el-tag>
          <el-tag v-else-if="scope.row.status === 2" type="success">审核成功</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="审核备注" align="left" prop="remark" show-overflow-tooltip min-width="120" />

      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ formatTimestamp(scope.row.createTime) }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['agent:liveaudit:edit']"
          >修改</el-button>
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleAudit(scope.row, 2)"
            v-hasPermi="['agent:liveaudit:audit']"
            style="color: #67C23A"
          >通过</el-button>
          <el-button
            v-if="scope.row.status === 0"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleAudit(scope.row, 1)"
            v-hasPermi="['agent:liveaudit:audit']"
            style="color: #F56C6C"
          >拒绝</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['agent:liveaudit:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="Number(total)"
      :page.sync="queryParams.pageNo"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改直播审核对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="代理商名称" prop="agentName">
              <el-input v-model="form.agentName" placeholder="请输入代理商名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="代理商编码" prop="agentCode">
              <el-input v-model="form.agentCode" placeholder="请输入代理商编码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="抖音UID" prop="douyinUid">
              <el-input v-model="form.douyinUid" placeholder="请输入抖音UID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="抖音号" prop="douyinAccount">
              <el-input v-model="form.douyinAccount" placeholder="请输入抖音号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="直播背景图" prop="backgroundImage">
              <el-input v-model="form.backgroundImage" placeholder="请输入直播背景图URL" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择审核状态">
                <el-option label="待认证" :value="0" />
                <el-option label="审核失败" :value="1" />
                <el-option label="审核成功" :value="2" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="审核备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入审核备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 审核对话框 -->
    <el-dialog :title="auditTitle" :visible.sync="auditOpen" width="500px" append-to-body>
      <el-form ref="auditForm" :model="auditForm" label-width="80px">
        <el-form-item label="审核备注">
          <el-input v-model="auditForm.remark" type="textarea" placeholder="请输入审核备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="auditOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitAudit">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 配置说明对话框 -->
    <el-dialog title="直播配置说明" :visible.sync="configOpen" width="600px" append-to-body>
      <el-form ref="configForm" :model="configForm" label-width="100px">
        <el-form-item label="配置内容">
          <el-input v-model="configForm.configValue" type="textarea" :rows="10" placeholder="请输入配置说明内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="configOpen = false">取 消</el-button>
        <el-button type="primary" @click="submitConfig">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  selectLiveAuditListPage,
  addLiveAudit,
  updateLiveAudit,
  updateLiveAuditStatus,
  deleteLiveAudit,
  getLiveConfig,
  updateLiveConfig
} from "@/api/agent/liveAudit";

export default {
  name: "LiveAudit",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 直播审核表格数据
      liveAuditList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 审核弹出层
      auditOpen: false,
      auditTitle: "",
      // 配置说明弹出层
      configOpen: false,
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        agentName: null,
        agentCode: null,
        douyinAccount: null,
        status: null
      },
      // 表单参数
      form: {},
      // 审核表单
      auditForm: {},
      // 配置表单
      configForm: {},
      // 表单校验
      rules: {
        agentName: [
          { required: true, message: "代理商名称不能为空", trigger: "blur" }
        ],
        agentCode: [
          { required: true, message: "代理商编码不能为空", trigger: "blur" }
        ],
        douyinAccount: [
          { required: true, message: "抖音号不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    // 确保 total 是数字类型
    this.total = Number(this.total) || 0;
  },
  methods: {
    /** 查询直播审核列表 */
    getList() {
      this.loading = true;
      selectLiveAuditListPage(this.queryParams).then(response => {
        if (response.code === 200 && response.data) {
          this.liveAuditList = response.data.records || [];
          this.total = parseInt(response.data.total) || 0;
        } else {
          this.liveAuditList = [];
          this.total = 0;
        }
        this.loading = false;
      }).catch(error => {
        console.error('获取直播审核列表失败:', error);
        this.liveAuditList = [];
        this.total = 0;
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
        liveAuditId: null,
        agentName: null,
        agentCode: null,
        douyinUid: null,
        douyinAccount: null,
        backgroundImage: null,
        status: 0,
        remark: null
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
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加直播审核";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const liveAuditId = row.liveAuditId || this.ids;
      this.form = Object.assign({}, row);
      this.open = true;
      this.title = "修改直播审核";
    },
    /** 审核按钮操作 */
    handleAudit(row, status) {
      this.auditForm = {
        liveAuditId: row.liveAuditId,
        status: status,
        remark: ''
      };
      this.auditTitle = status === 2 ? '审核通过' : '审核拒绝';
      this.auditOpen = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.liveAuditId != null) {
            updateLiveAudit(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLiveAudit(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 提交审核 */
    submitAudit() {
      updateLiveAuditStatus(this.auditForm).then(response => {
        this.$modal.msgSuccess("审核完成");
        this.auditOpen = false;
        this.getList();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const liveAuditIds = row.liveAuditId || this.ids;
      this.$modal.confirm('是否确认删除直播审核编号为"' + liveAuditIds + '"的数据项？').then(function() {
        return deleteLiveAudit(liveAuditIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 配置说明 */
    handleConfig() {
      getLiveConfig().then(response => {
        if (response.code === 200 && response.data) {
          this.configForm.configValue = response.data.configValue;
        } else {
          this.configForm.configValue = '';
        }
        this.configOpen = true;
      });
    },
    /** 提交配置 */
    submitConfig() {
      updateLiveConfig(this.configForm).then(response => {
        this.$modal.msgSuccess("配置更新成功");
        this.configOpen = false;
      });
    },
    /** 格式化时间戳 */
    formatTimestamp(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      return date.getFullYear() + '-' +
             String(date.getMonth() + 1).padStart(2, '0') + '-' +
             String(date.getDate()).padStart(2, '0') + ' ' +
             String(date.getHours()).padStart(2, '0') + ':' +
             String(date.getMinutes()).padStart(2, '0') + ':' +
             String(date.getSeconds()).padStart(2, '0');
    }
  }
};
</script>