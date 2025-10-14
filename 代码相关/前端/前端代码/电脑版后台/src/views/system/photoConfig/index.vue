<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="配置名称" prop="configName">
        <el-input
          v-model="queryParams.configName"
          placeholder="请输入配置名称"
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="启用状态" prop="isActive">
        <el-select v-model="queryParams.isActive" placeholder="启用状态" clearable>
          <el-option
            label="是"
            value="1"
          />
          <el-option
            label="否"
            value="0"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="primary"-->
      <!--    plain-->
      <!--    icon="el-icon-plus"-->
      <!--    size="mini"-->
      <!--    @click="handleAdd"-->
      <!--    v-hasPermi="['system:photoConfig:add']"-->
      <!--  >新增配置</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="success"-->
      <!--    plain-->
      <!--    icon="el-icon-edit"-->
      <!--    size="mini"-->
      <!--    :disabled="single"-->
      <!--    @click="handleUpdate"-->
      <!--    v-hasPermi="['system:photoConfig:edit']"-->
      <!--  >修改配置</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="danger"-->
      <!--    plain-->
      <!--    icon="el-icon-delete"-->
      <!--    size="mini"-->
      <!--    :disabled="multiple"-->
      <!--    @click="handleDelete"-->
      <!--    v-hasPermi="['system:photoConfig:remove']"-->
      <!--  >删除配置</el-button>-->
      <!--</el-col>-->
      <!--<el-col :span="1.5">-->
      <!--  <el-button-->
      <!--    type="warning"-->
      <!--    plain-->
      <!--    icon="el-icon-copy-document"-->
      <!--    size="mini"-->
      <!--    @click="handleCopy"-->
      <!--    v-hasPermi="['system:photoConfig:add']"-->
      <!--  >复制配置</el-button>-->
      <!--</el-col>-->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="configList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置ID" align="center" prop="configId" width="80" />
      <el-table-column label="配置名称" align="center" prop="configName" :show-overflow-tooltip="true" />
      <el-table-column label="配置说明" align="center" prop="description" :show-overflow-tooltip="true" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-hasPermi="['system:photoConfig:query']"
          >查看</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:photoConfig:edit']"
          >修改</el-button>
          <!--<el-button-->
          <!--  size="mini"-->
          <!--  type="text"-->
          <!--  icon="el-icon-copy-document"-->
          <!--  @click="handleCopy(scope.row)"-->
          <!--  v-hasPermi="['system:photoConfig:add']"-->
          <!--&gt;复制</el-button>-->
          <!--<el-button-->
          <!--  size="mini"-->
          <!--  type="text"-->
          <!--  icon="el-icon-delete"-->
          <!--  @click="handleDelete(scope.row)"-->
          <!--  v-hasPermi="['system:photoConfig:remove']"-->
          <!--&gt;删除</el-button>-->
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改照片配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="960px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="form.configName" placeholder="请输入配置名称" />
        </el-form-item>

        <el-form-item label="配置说明" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="请输入配置说明" :rows="2" />
        </el-form-item>

        <el-form-item label="照片配置">
          <div class="photo-config-container">
            <div class="config-header">
              <span>照片类型配置</span>
              <el-button type="primary" size="mini" icon="el-icon-plus" @click="addPhotoType">添加照片类型</el-button>
            </div>

            <div class="photo-types">
              <div v-for="(photoType, index) in photoTypes" :key="index" class="photo-type-item">
                <el-card shadow="hover">
                  <div slot="header" class="photo-type-header">
                    <el-row :gutter="10">
                      <el-col :span="8">
                        <el-input v-model="photoType.photoTypeName" placeholder="照片类型名称(如:身份证正面)" size="small" />
                      </el-col>
                      <el-col :span="10">
                        <el-input v-model="photoType.title" placeholder="标题(如:身份证正面照片)" size="small" />
                      </el-col>
                      <el-col :span="4">
                        <el-switch
                          v-model="photoType.required"
                          active-text="必填"
                          inactive-text="选填"
                          :active-value="1"
                          :inactive-value="0"
                          size="small">
                        </el-switch>
                      </el-col>
                      <el-col :span="2">
                        <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="removePhotoType(index)"></el-button>
                      </el-col>
                    </el-row>
                  </div>

                  <div class="photo-type-content">
                    <el-row :gutter="10">
                      <el-col :span="24">
                        <el-input
                          v-model="photoType.description"
                          type="textarea"
                          placeholder="照片描述说明"
                          :rows="2"
                          size="small"
                        />
                      </el-col>
                    </el-row>

                    <el-row :gutter="10" style="margin-top: 10px;">
                      <el-col :span="12">
                        <el-input v-model="photoType.exampleUrl" placeholder="示例图片URL" size="small">
                          <template slot="append">
                            <el-button icon="el-icon-upload" size="mini" @click="uploadExampleImage(index)">上传</el-button>
                          </template>
                        </el-input>
                      </el-col>
                      <el-col :span="12">
                        <el-input v-model="photoType.title" placeholder="示例图片标题（使用标题字段）" size="small" />
                      </el-col>
                    </el-row>

                    <el-row :gutter="10" style="margin-top: 10px;" v-if="photoType.exampleUrl">
                      <el-col :span="24">
                        <div class="example-preview">
                          <span>示例预览：</span>
                          <el-image
                            :src="photoType.exampleUrl"
                            :preview-src-list="[photoType.exampleUrl]"
                            fit="cover"
                            style="width: 250px"
                          ></el-image>
                          <span class="example-title">{{ photoType.title }}</span>
                        </div>
                      </el-col>
                    </el-row>
                  </div>
                </el-card>
              </div>
            </div>
          </div>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看配置详情对话框 -->
    <el-dialog title="照片配置详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <div class="config-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="配置名称">{{ viewForm.configName }}</el-descriptions-item>
          <el-descriptions-item label="启用状态">
            <el-tag :type="viewForm.isActive === 1 ? 'success' : 'info'" size="mini">
              {{ viewForm.isActive === 1 ? '是' : '否' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="配置类型">
            <el-tag :type="viewForm.configType === 1 ? 'success' : 'info'" size="mini">
              {{ viewForm.configType === 1 ? '默认模板' : '自定义模板' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="配置说明" :span="2">{{ viewForm.description || '暂无说明' }}</el-descriptions-item>
        </el-descriptions>

        <div class="photo-config-detail" style="margin-top: 20px;">
          <h4>照片类型配置</h4>
          <div v-for="(photoType, index) in viewPhotoTypes" :key="index" class="photo-type-detail">
            <el-card shadow="hover" style="margin-bottom: 10px;">
              <div slot="header">
                <strong>{{ photoType.photoTypeName }}</strong>
                <el-tag :type="photoType.required === 1 ? 'danger' : 'info'" size="mini" style="margin-left: 10px;">
                  {{ photoType.required === 1 ? '必填' : '选填' }}
                </el-tag>
              </div>
              <p><strong>标题：</strong>{{ photoType.title }}</p>
              <p><strong>描述：</strong>{{ photoType.description || '暂无描述' }}</p>
              <div v-if="photoType.exampleUrl" class="example-detail">
                <p><strong>示例图片：</strong></p>
                <el-image
                  style="width: 250px;"
                  :src="photoType.exampleUrl"
                  :preview-src-list="[photoType.exampleUrl]"
                  fit="cover"
                ></el-image>
                <p style="margin-top: 5px;"><em>{{ photoType.title }}</em></p>
              </div>
            </el-card>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPhotoConfig, getPhotoConfig, delPhotoConfig, addPhotoConfig, updatePhotoConfig } from "@/api/system/photoConfig";
import { uploadExampleImage } from "@/api/system/photoDefaultConfig";

export default {
  name: "PhotoConfig",
  dicts: ['sys_yes_no'],
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
      // 照片配置表格数据
      configList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示查看详情对话框
      viewOpen: false,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        configName: undefined,
        isActive: undefined
      },
      // 表单参数
      form: {},
      // 查看表单参数
      viewForm: {},
      // 照片类型列表
      photoTypes: [],
      // 查看的照片类型列表
      viewPhotoTypes: [],
      // 表单校验
      rules: {
        configName: [
          { required: true, message: "配置名称不能为空", trigger: "blur" }
        ],
        isActive: [
          { required: true, message: "启用状态不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询照片配置列表 */
    getList() {
      this.loading = true;
      listPhotoConfig(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.configList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        configId: undefined,
        configName: undefined,
        description: undefined,
        configType: 1,
        isActive: 1,
        photoConfig: undefined
      };
      this.photoTypes = [];
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.configId)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增照片配置";
      // 添加默认照片类型
      this.addDefaultPhotoTypes();
    },
    /** 查看按钮操作 */
    handleView(row) {
      const configId = row.configId;
      getPhotoConfig(configId).then(response => {
        this.viewForm = response.data;
        // 使用后端返回的photoConfigList
        this.viewPhotoTypes = response.data.photoConfigList || [];
        this.viewOpen = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const configId = row.configId || this.ids
      getPhotoConfig(configId).then(response => {
        // 只复制需要的字段到form对象，避免包含photoConfigList
        this.form = {
          configId: response.data.configId,
          configName: response.data.configName,
          description: response.data.description,
          configType: response.data.configType,
          isActive: response.data.isActive,
          createBy: response.data.createBy,
          createTime: response.data.createTime,
          updateBy: response.data.updateBy,
          updateTime: response.data.updateTime,
          remark: response.data.remark
        };
        // 使用后端返回的photoConfigList
        this.photoTypes = response.data.photoConfigList || [];
        this.open = true;
        this.title = "修改照片配置";
      });
    },
    /** 复制按钮操作 */
    handleCopy(row) {
      this.reset();
      const configId = row.configId;
      getPhotoConfig(configId).then(response => {
        // 只复制需要的字段到form对象，避免包含photoConfigList
        this.form = {
          configId: undefined,
          configName: response.data.configName + "_副本",
          description: response.data.description,
          configType: 2, // 复制的配置设为自定义模板
          isActive: response.data.isActive,
          remark: response.data.remark
        };
        // 使用后端返回的photoConfigList
        this.photoTypes = response.data.photoConfigList || [];
        this.open = true;
        this.title = "复制照片配置";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 验证照片类型配置
          if (this.photoTypes.length === 0) {
            this.$modal.msgError("请至少添加一个照片类型配置");
            return;
          }

          // 验证照片类型必填字段
          for (let i = 0; i < this.photoTypes.length; i++) {
            const photoType = this.photoTypes[i];
            if (!photoType.photoTypeName || !photoType.title) {
              this.$modal.msgError(`第${i + 1}个照片类型的名称和标题不能为空`);
              return;
            }
          }

          // 将照片配置列表转换为JSON字符串
          this.form.photoConfig = JSON.stringify(this.photoTypes);

          if (this.form.configId != undefined) {
            updatePhotoConfig(this.form).then(() => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPhotoConfig(this.form).then(() => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const configIds = row.configId || this.ids;
      this.$modal.confirm('是否确认删除配置编号为"' + configIds + '"的数据项？').then(function() {
          return delPhotoConfig(configIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
    },
    /** 添加照片类型 */
    addPhotoType() {
      this.photoTypes.push({
        photoType: this.photoTypes.length + 1,
        photoTypeName: '',
        required: 0,
        title: '',
        description: '',
        exampleUrl: '',
        maxSize: 5242880,
        supportedFormats: 'jpg,jpeg,png',
        minWidth: 300,
        minHeight: 400,
        maxWidth: null,
        maxHeight: null,
        sortOrder: this.photoTypes.length + 1
      });
    },
    /** 删除照片类型 */
    removePhotoType(index) {
      this.photoTypes.splice(index, 1);
    },
    /** 添加默认照片类型 */
    addDefaultPhotoTypes() {
      this.photoTypes = [
        {
          photoType: 1,
          photoTypeName: '身份证正面',
          required: 1,
          title: '身份证正面照片',
          description: '请上传清晰的身份证正面照片，确保证件完整、无遮挡、无反光',
          exampleUrl: '',
          maxSize: 5242880,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 800,
          minHeight: 600,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 1
        },
        {
          photoType: 2,
          photoTypeName: '身份证反面',
          required: 1,
          title: '身份证反面照片',
          description: '请上传清晰的身份证反面照片，确保证件完整、国徽清晰可见',
          exampleUrl: '',
          maxSize: 5242880,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 800,
          minHeight: 600,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 2
        },
        {
          photoType: 3,
          photoTypeName: '免冠照片',
          required: 1,
          title: '免冠照片',
          description: '请上传近期免冠正面照片，要求正面免冠、背景简洁、表情自然',
          exampleUrl: '',
          maxSize: 5242880,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 300,
          minHeight: 400,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 3
        }
      ];
    },
    /** 上传示例图片 */
    uploadExampleImage(index) {
      // 创建隐藏的文件输入框
      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/jpeg,image/jpg,image/png,image/gif';
      input.style.display = 'none';

      // 添加文件选择监听
      input.addEventListener('change', (event) => {
        const selectedFile = event.target.files[0];
        if (selectedFile) {
          // 验证文件大小
          const isLt5M = selectedFile.size / 1024 / 1024 < 5;
          if (!isLt5M) {
            this.$modal.msgError('上传图片大小不能超过 5MB!');
            return;
          }

          // 显示上传进度
          const loading = this.$loading({
            lock: true,
            text: '正在上传...',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });

          // 调用上传API
          uploadExampleImage(selectedFile).then(response => {
            loading.close();
            if (response.code === 200) {
              // 更新对应的照片类型
              this.photoTypes[index].exampleUrl = response.url;
              this.$modal.msgSuccess("图片上传成功!");
            } else {
              this.$modal.msgError(response.msg || '图片上传失败');
            }
          }).catch(error => {
            loading.close();
            console.error('图片上传失败:', error);
            this.$modal.msgError('图片上传失败，请重试!');
          });
        }

        // 移除临时创建的input元素
        document.body.removeChild(input);
      });

      // 添加到DOM并触发点击
      document.body.appendChild(input);
      input.click();
    }
  }
};
</script>

<style scoped>
.photo-config-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
}

.config-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  font-weight: bold;
}

.photo-types {
  max-height: 400px;
  overflow-y: auto;
}

.photo-type-item {
  margin-bottom: 15px;
}

.photo-type-header {
  padding: 0;
}

.photo-type-content {
  padding: 15px;
  background-color: #fafafa;
}

.example-preview {
  display: flex;
  align-items: center;
  gap: 10px;
}

.example-preview .example-title {
  font-size: 12px;
  color: #666;
  max-width: 200px;
  word-break: break-all;
}

.config-detail {
  padding: 0 20px;
}

.photo-config-detail h4 {
  margin: 20px 0 15px 0;
  color: #303133;
  border-bottom: 1px solid #e4e7ed;
  padding-bottom: 10px;
}

.photo-type-detail {
  margin-bottom: 15px;
}

.example-detail {
  margin-top: 10px;
}

.example-detail p {
  margin: 5px 0;
}
</style>
