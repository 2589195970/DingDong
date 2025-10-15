<template>
  <div style="min-height: 200px">
    <!-- 需要上传照片选择 -->
    <div>
      <el-form-item label="需要上传照片" prop="photoRequired">
        <el-radio-group v-model="localPhotoRequired">
          <el-radio :label="0">否</el-radio>
          <el-radio :label="1">是</el-radio>
        </el-radio-group>
      </el-form-item>
    </div>

    <!-- 照片配置编辑（当需要照片时显示，单独一行） -->
    <div>
      <el-form-item v-if="localPhotoRequired === 1" label="照片配置">
        <div class="photo-config-section">
          <!--<div class="config-header">-->
          <!--  <span>照片类型配置</span>-->
          <!--  <div>-->
          <!--    <el-button type="primary" size="mini" icon="el-icon-plus" @click="addPhotoType">添加照片类型</el-button>-->
          <!--    <el-button type="success" size="mini" icon="el-icon-refresh" @click="loadDefaultConfig">加载默认配置</el-button>-->
          <!--  </div>-->
          <!--</div>-->

          <div class="photo-types">
            <div v-for="(photoType, index) in editablePhotoTypes" :key="index" class="photo-type-item">
              <el-card shadow="hover">
                <div slot="header" class="photo-type-header">
                  <el-row :gutter="10">
                    <el-col :span="7">
                      <el-input v-model="photoType.photoTypeName" placeholder="照片类型名称(如:身份证正面)" size="small" />
                    </el-col>
                    <el-col :span="8">
                      <el-input v-model="photoType.title" placeholder="标题(如:身份证正面照片)" size="small" />
                    </el-col>
                    <el-col :span="6">
                      <el-switch
                        v-model="photoType.required"
                        active-text="启用"
                        inactive-text="不启用"
                        :active-value="1"
                        :inactive-value="0"
                        size="small">
                      </el-switch>
                    </el-col>
                    <!--<el-col :span="2">-->
                    <!--  <el-button type="danger" icon="el-icon-delete" circle size="mini" @click="removePhotoType(index)"></el-button>-->
                    <!--</el-col>-->
                    <el-col :span="3">
                      <el-button type="info" icon="el-icon-upload" size="mini" @click="uploadExampleImage(index)">上传示例</el-button>
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
                          <el-button icon="el-icon-picture" size="mini" @click="previewImage(photoType.exampleUrl)">预览</el-button>
                        </template>
                      </el-input>
                    </el-col>
                    <!--<el-col :span="12">-->
                    <!--  <el-input v-model="photoType.supportedFormats" placeholder="支持的格式(如:jpg,jpeg,png)" size="small" />-->
                    <!--</el-col>-->
                  </el-row>

                  <!--<el-row :gutter="10" style="margin-top: 10px;">-->
                  <!--  <el-col :span="6">-->
                  <!--    <el-input-number v-model="photoType.maxSize" placeholder="最大大小(MB)" :min="1" :max="50" size="small" style="width: 100%">-->
                  <!--      <template slot="append">MB</template>-->
                  <!--    </el-input-number>-->
                  <!--  </el-col>-->
                  <!--  <el-col :span="6">-->
                  <!--    <el-input-number v-model="photoType.minWidth" placeholder="最小宽度" :min="1" size="small" style="width: 100%">-->
                  <!--      <template slot="append">px</template>-->
                  <!--    </el-input-number>-->
                  <!--  </el-col>-->
                  <!--  <el-col :span="6">-->
                  <!--    <el-input-number v-model="photoType.minHeight" placeholder="最小高度" :min="1" size="small" style="width: 100%">-->
                  <!--      <template slot="append">px</template>-->
                  <!--    </el-input-number>-->
                  <!--  </el-col>-->
                  <!--  <el-col :span="6">-->
                  <!--    <el-input-number v-model="photoType.sortOrder" placeholder="排序" :min="1" size="small" style="width: 100%">-->
                  <!--    </el-input-number>-->
                  <!--  </el-col>-->
                  <!--</el-row>-->

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

          <div v-if="editablePhotoTypes.length === 0 && !isLoading" class="no-config">
            <el-empty description="暂无照片配置，请添加照片类型或加载默认配置" :image-size="100">
              <el-button type="primary" @click="addDefaultPhotoTypes">加载默认配置</el-button>
            </el-empty>
          </div>

          <div v-if="isLoading" class="loading-config">
            <i class="el-icon-loading"></i> 正在加载照片配置...
          </div>
        </div>
      </el-form-item>
    </div>
  </div>
</template>

<script>
import { listPhotoConfig } from "@/api/system/photoConfig";
import { uploadExampleImage } from "@/api/system/photoDefaultConfig";

export default {
  name: "PhotoConfig",
  props: {
    // 照片要求状态
    value: {
      type: Object,
      default: () => ({
        photoRequired: 0,
        photoConfig: null
      })
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      localPhotoRequired: 0,
      localPhotoConfig: null,
      editablePhotoTypes: [], // 可编辑的照片类型列表
      isLoading: false,
      // 多级标志位控制系统
      isInitializing: false,      // 组件初始化标志
      isLoadingFromAPI: false,    // API加载中标志
      isUpdatingFromParent: false, // 父组件更新标志
      skipNextValueUpdate: false  // 跳过下次value更新的标志
    };
  },
  watch: {
    // 监听外部value变化 - 只在父组件主动更新时响应
    value: {
      handler(newVal) {
        // 如果设置了跳过标志，直接清除并返回
        if (this.skipNextValueUpdate) {
          this.skipNextValueUpdate = false;
          return;
        }

        // 如果正在初始化或从API加载，跳过处理
        if (this.isInitializing || this.isLoadingFromAPI) {
          return;
        }

        // 标记为父组件更新
        this.isUpdatingFromParent = true;

        this.localPhotoRequired = newVal.photoRequired || 0;
        this.localPhotoConfig = newVal.photoConfig || null;

        // 只有在需要上传照片且有配置时才加载配置
        if (this.localPhotoRequired === 1) {
          this.loadPhotoConfig(false); // false表示不触发emitChange
        }

        // 使用nextTick清除标志
        this.$nextTick(() => {
          this.isUpdatingFromParent = false;
        });
      },
      immediate: true,
      deep: true
    },
    // 监听编辑的照片类型变化 - 只处理用户内容编辑
    editablePhotoTypes: {
      handler(newVal) {
        // 只在不是初始化、不是父组件更新、不是API加载时才处理
        if (this.localPhotoRequired === 1 &&
            !this.isInitializing &&
            !this.isUpdatingFromParent &&
            !this.isLoadingFromAPI) {
          this.localPhotoConfig = JSON.stringify(newVal);
          this.emitChange(false); // false表示不触发value watcher
        }
      },
      deep: true,
      immediate: false
    },
    // 监听内部状态变化 - 只处理用户交互
    localPhotoRequired: {
      handler(newVal, oldVal) {
        // 只在不是初始化、不是父组件更新、不是API加载时才处理
        if (!this.isInitializing &&
            !this.isUpdatingFromParent &&
            !this.isLoadingFromAPI) {

          // 设置加载标志，防止循环
          this.isLoadingFromAPI = true;

          // 立即发送变更
          this.emitChange(false);

          // 如果切换到需要上传照片，加载默认配置
          if (newVal === 1) {
            this.loadPhotoConfig(false); // false表示不触发emitChange
          } else {
            // 如果切换到不需要照片，清空配置
            this.editablePhotoTypes = [];
            this.localPhotoConfig = null;
          }

          // 使用nextTick清除标志
          this.$nextTick(() => {
            this.isLoadingFromAPI = false;
          });
        }
      },
      immediate: false
    }
  },
  created() {
    this.isInitializing = true;
    this.loadPhotoConfig(true); // true表示需要触发emitChange
    this.$nextTick(() => {
      this.isInitializing = false;
    });
  },
  methods: {
    // 加载默认照片配置
    async loadPhotoConfig(shouldEmitChange = true) {
      // 如果不需要上传照片，直接返回
      if (this.localPhotoRequired !== 1) {
        this.editablePhotoTypes = [];
        return;
      }

      // 如果已经有配置且不是初始化调用，直接解析
      if (this.localPhotoConfig && !this.isInitializing) {
        try {
          this.editablePhotoTypes = JSON.parse(this.localPhotoConfig) || [];
          return;
        } catch (error) {
          console.error('解析照片配置失败:', error);
          this.editablePhotoTypes = [];
        }
      }

      // 如果需要加载默认配置
      if (!this.localPhotoConfig || this.isInitializing) {
        try {
          // 根据调用来源设置不同的标志
          if (this.isInitializing) {
            this.isLoading = true;
          } else {
            this.isLoadingFromAPI = true;
          }

          // 调用系统默认配置API
          const response = await listPhotoConfig({
            pageNum: 1,
            pageSize: 1,
            configType: 1, // 查询默认模板
            isActive: 1   // 只查询启用的配置
          });

          if (response.rows && response.rows.length > 0) {
            const defaultConfig = response.rows[0];
            // 直接使用photoConfigList，作为可编辑数据
            this.editablePhotoTypes = defaultConfig.photoConfigList || [];
            console.log("默认配置：", defaultConfig)
            // 为了保持数据结构一致性，将photoConfigList转换为JSON字符串存储
            this.localPhotoConfig = JSON.stringify(this.editablePhotoTypes);

            // 只有在允许的情况下才触发变更
            if (shouldEmitChange) {
              this.emitChange(true); // true表示这次更新会触发value watcher
            }
          } else {
            // 如果没有默认配置，使用空数组
            this.editablePhotoTypes = [];
            this.localPhotoConfig = JSON.stringify([]);

            if (shouldEmitChange) {
              this.emitChange(true);
            }
          }
        } catch (error) {
          console.error('加载默认照片配置失败:', error);
          this.$message.error('加载默认照片配置失败');
          this.editablePhotoTypes = [];
          this.localPhotoConfig = JSON.stringify([]);

          if (shouldEmitChange) {
            this.emitChange(true);
          }
        } finally {
          this.isLoading = false;
          this.isLoadingFromAPI = false;
        }
      }
    },
    // 加载默认配置（手动触发）
    async loadDefaultConfig() {
      try {
        this.isLoadingFromAPI = true;
        const response = await listPhotoConfig({
          pageNum: 1,
          pageSize: 1,
          configType: 1, // 查询默认模板
          isActive: 1   // 只查询启用的配置
        });
        if (response.rows && response.rows.length > 0) {
          const defaultConfig = response.rows[0];
          this.editablePhotoTypes = defaultConfig.photoConfigList || [];
          this.localPhotoConfig = JSON.stringify(this.editablePhotoTypes);
          this.emitChange(false); // 手动加载不触发value watcher
          this.$message.success('已加载默认配置');
        } else {
          this.$message.warning('没有找到可用的默认配置');
        }
      } catch (error) {
        console.error('加载默认配置失败:', error);
        this.$message.error('加载默认配置失败');
      } finally {
        this.isLoadingFromAPI = false;
      }
    },
    // 添加照片类型
    addPhotoType() {
      this.editablePhotoTypes.push({
        photoType: this.editablePhotoTypes.length + 1,
        photoTypeName: '',
        required: 0,
        title: '',
        description: '',
        exampleUrl: '',
        maxSize: 5,
        supportedFormats: 'jpg,jpeg,png',
        minWidth: 300,
        minHeight: 400,
        maxWidth: null,
        maxHeight: null,
        sortOrder: this.editablePhotoTypes.length + 1
      });
    },
    // 删除照片类型
    removePhotoType(index) {
      this.editablePhotoTypes.splice(index, 1);
      // 重新设置sortOrder
      this.editablePhotoTypes.forEach((item, idx) => {
        item.sortOrder = idx + 1;
      });
    },
    // 添加默认照片类型
    addDefaultPhotoTypes() {
      this.editablePhotoTypes = [
        {
          photoType: 1,
          photoTypeName: 'idCardFront',
          required: 1,
          title: '身份证正面照片',
          description: '请上传清晰的身份证正面照片，确保证件完整、无遮挡、无反光',
          exampleUrl: '',
          maxSize: 5,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 800,
          minHeight: 600,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 1
        },
        {
          photoType: 2,
          photoTypeName: 'idCardBack',
          required: 1,
          title: '身份证反面照片',
          description: '请上传清晰的身份证反面照片，确保证件完整、国徽清晰可见',
          exampleUrl: '',
          maxSize: 5,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 800,
          minHeight: 600,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 2
        },
        {
          photoType: 3,
          photoTypeName: 'personPhoto',
          required: 1,
          title: '免冠照片',
          description: '请上传近期免冠正面照片，要求正面免冠、背景简洁、表情自然',
          exampleUrl: '',
          maxSize: 5,
          supportedFormats: 'jpg,jpeg,png',
          minWidth: 300,
          minHeight: 400,
          maxWidth: null,
          maxHeight: null,
          sortOrder: 3
        }
      ];
      this.$message.success('已添加默认照片类型');
    },
    // 上传示例图片
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
            this.$message.error('上传图片大小不能超过 5MB!');
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
              this.editablePhotoTypes[index].exampleUrl = response.url;
              this.$message.success("图片上传成功!");
            } else {
              this.$message.error(response.msg || '图片上传失败');
            }
          }).catch(error => {
            loading.close();
            console.error('图片上传失败:', error);
            this.$message.error('图片上传失败，请重试!');
          });
        }

        // 移除临时创建的input元素
        document.body.removeChild(input);
      });

      // 添加到DOM并触发点击
      document.body.appendChild(input);
      input.click();
    },
    // 预览图片
    previewImage(url) {
      if (url) {
        window.open(url, '_blank');
      } else {
        this.$message.warning('请先上传图片');
      }
    },
    // 向父组件发送变更事件
    emitChange(shouldSkipNextUpdate = false) {
      const newValue = {
        photoRequired: this.localPhotoRequired,
        photoConfig: this.localPhotoRequired === 1 ? this.localPhotoConfig : null
      };

      // 如果需要跳过下次更新，设置标志
      if (shouldSkipNextUpdate) {
        this.skipNextValueUpdate = true;
      }

      this.$emit('input', newValue);
      this.$emit('change', newValue);
    },
    // 重置配置
    reset() {
      this.localPhotoRequired = 0;
      this.localPhotoConfig = null;
      this.editablePhotoTypes = [];
      this.skipNextValueUpdate = false;
      this.isInitializing = false;
      this.isLoadingFromAPI = false;
      this.isUpdatingFromParent = false;
      this.emitChange();
    }
  }
};
</script>

<style scoped>
.photo-config-section {
  width: 1000px;
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
}


.photo-types {
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
  background-color: #ffffff;
}

.photo-type-name {
  font-weight: bold;
  color: #303133;
}

.photo-title {
  color: #606266;
  font-size: 14px;
}

.photo-description {
  color: #606266;
  font-size: 13px;
  line-height: 1.5;
  min-height: 40px;
  background-color: #f8f9fa;
  padding: 8px 12px;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.example-preview {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 4px;
  border: 1px solid #e9ecef;
}

.example-preview .example-title {
  font-size: 12px;
  color: #666;
  max-width: 200px;
  word-break: break-all;
}

.loading-config {
  text-align: center;
  padding: 20px;
  color: #909399;
  font-size: 14px;
}

.loading-config i {
  margin-right: 8px;
}

.no-config {
  text-align: center;
  padding: 40px 20px;
  color: #909399;
}

/* 卡片样式优化 */
.photo-type-item .el-card {
  border: 1px solid #e4e7ed;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.12), 0 0 6px rgba(0, 0, 0, 0.04);
}

.photo-type-item .el-card__header {
  background-color: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
  padding: 12px 15px;
}

.photo-type-item .el-card__body {
  padding: 15px;
}

/* 标签样式优化 */
.el-tag {
  font-weight: 500;
}

/* 头部按钮组样式 */
.config-header > div {
  display: flex;
  gap: 10px;
}

.config-header .el-button {
  font-size: 12px;
  padding: 7px 15px;
}

/* 输入框样式优化 */
.el-input-number {
  width: 100%;
}

.el-input-number .el-input__inner {
  text-align: left;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .photo-type-header .el-col {
    margin-bottom: 8px;
  }

  .config-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }

  .config-header > div {
    width: 100%;
    justify-content: flex-start;
  }
}

@media (max-width: 768px) {
  .photo-type-content .el-row .el-col {
    margin-bottom: 10px;
  }

  .example-preview {
    flex-direction: column;
    align-items: flex-start;
  }

  .example-preview .example-title {
    max-width: 100%;
  }
}

/* 滚动条样式 */
.photo-types::-webkit-scrollbar {
  width: 6px;
}

.photo-types::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.photo-types::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.photo-types::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}
</style>
