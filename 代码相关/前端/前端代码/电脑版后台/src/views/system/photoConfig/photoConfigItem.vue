<template>
  <div class="photo-config-item">
    <el-card class="config-item-card" shadow="hover">
      <div slot="header" class="card-header">
        <span class="config-title">
          <i class="el-icon-picture-outline"></i>
          照片配置项 #{{ index + 1 }}
        </span>
        <el-button
          v-if="showDelete"
          type="danger"
          size="mini"
          icon="el-icon-delete"
          @click="removeItem"
          circle
        />
      </div>

      <el-form
        ref="configForm"
        :model="itemData"
        :rules="rules"
        label-width="120px"
        label-position="right"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="照片类型" prop="photoType">
              <el-select
                v-model="itemData.photoType"
                placeholder="请选择照片类型"
                style="width: 100%"
                @change="handlePhotoTypeChange"
              >
                <el-option
                  v-for="option in photoTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否必填" prop="required">
              <el-switch
                v-model="itemData.required"
                :active-value="1"
                :inactive-value="0"
                active-text="必填"
                inactive-text="选填"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="标题" prop="title">
              <el-input
                v-model="itemData.title"
                placeholder="请输入照片标题"
                maxlength="50"
                show-word-limit
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大文件大小" prop="maxSize">
              <el-input-number
                v-model="itemData.maxSize"
                :min="1"
                :max="20"
                :step="1"
                style="width: 100%"
              />
              <span class="input-suffix">MB</span>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述" prop="description">
          <el-input
            v-model="itemData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入照片描述"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="提示信息" prop="tips">
          <el-input
            v-model="itemData.tips"
            type="textarea"
            :rows="2"
            placeholder="请输入上传提示信息"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="示例图片">
          <image-upload
            v-model="itemData.exampleUrl"
            placeholder="上传示例图片"
            tip="建议尺寸：宽度不超过800px，高度不超过600px"
            :max-size="3"
            alt="示例图片"
          />
        </el-form-item>

        <el-divider content-position="left">
          <span class="divider-title">高级设置</span>
        </el-divider>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="最小宽度" prop="minWidth">
              <el-input-number
                v-model="itemData.minWidth"
                :min="50"
                :max="2000"
                :step="50"
                style="width: 100%"
              />
              <span class="input-suffix">px</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最小高度" prop="minHeight">
              <el-input-number
                v-model="itemData.minHeight"
                :min="50"
                :max="2000"
                :step="50"
                style="width: 100%"
              />
              <span class="input-suffix">px</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="排序序号" prop="sortOrder">
              <el-input-number
                v-model="itemData.sortOrder"
                :min="0"
                :max="999"
                :step="1"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="最大宽度" prop="maxWidth">
              <el-input-number
                v-model="itemData.maxWidth"
                :min="100"
                :max="4000"
                :step="100"
                style="width: 100%"
              />
              <span class="input-suffix">px</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="最大高度" prop="maxHeight">
              <el-input-number
                v-model="itemData.maxHeight"
                :min="100"
                :max="4000"
                :step="100"
                style="width: 100%"
              />
              <span class="input-suffix">px</span>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="支持格式" prop="supportedFormats">
              <el-input
                v-model="itemData.supportedFormats"
                placeholder="如：jpg,png,gif"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import ImageUpload from './imageUpload.vue'

export default {
  name: 'PhotoConfigItem',
  components: {
    ImageUpload
  },
  props: {
    // 配置项数据
    value: {
      type: Object,
      default: () => ({})
    },
    // 索引位置
    index: {
      type: Number,
      default: 0
    },
    // 是否显示删除按钮
    showDelete: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      itemData: {
        photoType: null,
        title: '',
        description: '',
        exampleUrl: '',
        maxSize: 5,
        required: 1,
        tips: '',
        minWidth: 100,
        minHeight: 100,
        maxWidth: 2000,
        maxHeight: 2000,
        supportedFormats: 'jpg,jpeg,png,gif',
        sortOrder: this.index,
        ...this.value
      },
      photoTypeOptions: [
        { label: '身份证正面', value: 1 },
        { label: '身份证反面', value: 2 },
        { label: '免冠照片', value: 3 },
        { label: '银行卡正面', value: 4 },
        { label: '手持身份证', value: 5 },
        { label: '营业执照', value: 6 },
        { label: '其他证件', value: 99 }
      ],
      rules: {
        photoType: [
          { required: true, message: '请选择照片类型', trigger: 'change' }
        ],
        title: [
          { required: true, message: '请输入照片标题', trigger: 'blur' },
          { min: 2, max: 50, message: '标题长度在 2 到 50 个字符', trigger: 'blur' }
        ],
        description: [
          { max: 200, message: '描述长度不能超过 200 个字符', trigger: 'blur' }
        ],
        maxSize: [
          { required: true, message: '请设置最大文件大小', trigger: 'blur' },
          { type: 'number', min: 1, max: 20, message: '文件大小限制在 1-20MB', trigger: 'blur' }
        ],
        tips: [
          { max: 100, message: '提示信息不能超过 100 个字符', trigger: 'blur' }
        ],
        minWidth: [
          { type: 'number', min: 50, max: 2000, message: '最小宽度在 50-2000px', trigger: 'blur' }
        ],
        minHeight: [
          { type: 'number', min: 50, max: 2000, message: '最小高度在 50-2000px', trigger: 'blur' }
        ],
        maxWidth: [
          { type: 'number', min: 100, max: 4000, message: '最大宽度在 100-4000px', trigger: 'blur' }
        ],
        maxHeight: [
          { type: 'number', min: 100, max: 4000, message: '最大高度在 100-4000px', trigger: 'blur' }
        ],
        sortOrder: [
          { type: 'number', min: 0, max: 999, message: '排序序号在 0-999', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    value: {
      handler(newVal) {
        if (newVal) {
          this.itemData = { ...this.itemData, ...newVal }
        }
      },
      deep: true,
      immediate: true
    },
    itemData: {
      handler(newVal) {
        this.$emit('input', newVal)
        this.$emit('change', newVal, this.index)
      },
      deep: true
    }
  },
  methods: {
    // 照片类型改变时自动填充标题
    handlePhotoTypeChange(value) {
      const option = this.photoTypeOptions.find(opt => opt.value === value)
      if (option && !this.itemData.title) {
        this.itemData.title = option.label
      }

      // 根据照片类型设置默认值
      this.setDefaultValuesByType(value)
    },

    // 根据照片类型设置默认值
    setDefaultValuesByType(photoType) {
      switch (photoType) {
        case 1: // 身份证正面
        case 2: // 身份证反面
          this.itemData.minWidth = 600
          this.itemData.minHeight = 400
          this.itemData.maxWidth = 1200
          this.itemData.maxHeight = 800
          break
        case 3: // 免冠照片
          this.itemData.minWidth = 295
          this.itemData.minHeight = 413
          this.itemData.maxWidth = 358
          this.itemData.maxHeight = 441
          this.itemData.tips = '白色背景，免冠，正脸面向镜头'
          break
        case 4: // 银行卡
          this.itemData.minWidth = 800
          this.itemData.minHeight = 500
          this.itemData.maxWidth = 1600
          this.itemData.maxHeight = 1000
          break
        case 5: // 手持身份证
          this.itemData.minWidth = 800
          this.itemData.minHeight = 600
          this.itemData.maxWidth = 1600
          this.itemData.maxHeight = 1200
          break
        case 6: // 营业执照
          this.itemData.minWidth = 1000
          this.itemData.minHeight = 700
          this.itemData.maxWidth = 2000
          this.itemData.maxHeight = 1400
          break
        default:
          // 使用通用默认值
          break
      }
    },

    // 删除配置项
    removeItem() {
      this.$confirm('确定要删除这个照片配置项吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$emit('remove', this.index)
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 验证表单
    validate() {
      return new Promise((resolve, reject) => {
        this.$refs.configForm.validate((valid) => {
          if (valid) {
            resolve(this.itemData)
          } else {
            reject(new Error('表单验证失败'))
          }
        })
      })
    },

    // 重置表单
    resetForm() {
      this.$refs.configForm.resetFields()
      this.itemData = {
        photoType: null,
        title: '',
        description: '',
        exampleUrl: '',
        maxSize: 5,
        required: 1,
        tips: '',
        minWidth: 100,
        minHeight: 100,
        maxWidth: 2000,
        maxHeight: 2000,
        supportedFormats: 'jpg,jpeg,png,gif',
        sortOrder: this.index
      }
    },

    // 获取配置项数据
    getItemData() {
      return { ...this.itemData }
    },

    // 设置配置项数据
    setItemData(data) {
      this.itemData = { ...this.itemData, ...data }
    }
  }
}
</script>

<style lang="scss" scoped>
.photo-config-item {
  margin-bottom: 20px;

  .config-item-card {
    border: 1px solid #e4e7ed;
    transition: box-shadow 0.3s;

    &:hover {
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0;

      .config-title {
        font-size: 16px;
        font-weight: 600;
        color: #303133;
        display: flex;
        align-items: center;
        gap: 8px;

        i {
          color: #409EFF;
        }
      }
    }
  }

  .input-suffix {
    margin-left: 5px;
    color: #909399;
    font-size: 12px;
  }

  .divider-title {
    font-size: 14px;
    color: #606266;
    font-weight: 500;
  }

  // 表单样式调整
  ::v-deep .el-form-item__label {
    font-weight: 500;
    color: #606266;
  }

  ::v-deep .el-input-number {
    width: 100%;

    .el-input__inner {
      text-align: left;
    }
  }

  ::v-deep .el-switch {
    .el-switch__label {
      color: #606266;
      font-weight: normal;
    }
  }
}

// 响应式设计
@media (max-width: 1200px) {
  .photo-config-item {
    ::v-deep .el-col {
      margin-bottom: 10px;
    }
  }
}

@media (max-width: 768px) {
  .photo-config-item {
    .config-item-card {
      .card-header {
        .config-title {
          font-size: 14px;
        }
      }
    }

    ::v-deep .el-form-item__label {
      text-align: left;
      padding-bottom: 0;
    }

    .input-suffix {
      display: none;
    }
  }
}
</style>