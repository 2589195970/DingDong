<template>
  <div class="image-upload-container">
    <!-- 上传区域 -->
    <el-upload
      ref="upload"
      :action="uploadUrl"
      :headers="headers"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-error="handleError"
      :on-progress="handleProgress"
      :file-list="fileList"
      :limit="limit"
      :accept="accept"
      :multiple="multiple"
      :disabled="disabled"
      :show-file-list="false"
      class="image-uploader"
      drag
    >
      <div v-if="!imageUrl" class="upload-placeholder">
        <i class="el-icon-plus uploader-icon"></i>
        <div class="upload-text">{{ placeholder }}</div>
        <div class="upload-tip" v-if="tip">{{ tip }}</div>
      </div>

      <!-- 图片预览 -->
      <div v-else class="image-preview">
        <img :src="imageUrl" :alt="alt" class="preview-image" />
        <div class="image-actions">
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-zoom-in"
            @click.stop="previewImage"
            circle
          />
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click.stop="removeImage"
            circle
          />
        </div>
      </div>
    </el-upload>

    <!-- 进度条 -->
    <el-progress
      v-if="uploading"
      :percentage="uploadPercentage"
      :status="uploadStatus"
      class="upload-progress"
    />

    <!-- 图片预览对话框 -->
    <el-dialog
      title="图片预览"
      :visible.sync="previewVisible"
      width="50%"
      center
      append-to-body
    >
      <img :src="imageUrl" :alt="alt" style="width: 100%; height: auto;" />
    </el-dialog>
  </div>
</template>

<script>
import { uploadExampleImage, deleteExampleImage } from '@/api/system/photoDefaultConfig'
import { getToken } from '@/utils/auth'

export default {
  name: 'ImageUpload',
  props: {
    // 图片URL
    value: {
      type: String,
      default: ''
    },
    // 占位文本
    placeholder: {
      type: String,
      default: '点击或拖拽文件到此处上传'
    },
    // 提示文本
    tip: {
      type: String,
      default: ''
    },
    // 文件大小限制(MB)
    maxSize: {
      type: Number,
      default: 5
    },
    // 上传数量限制
    limit: {
      type: Number,
      default: 1
    },
    // 是否支持多选
    multiple: {
      type: Boolean,
      default: false
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    },
    // 接受的文件类型
    accept: {
      type: String,
      default: 'image/jpeg,image/jpg,image/png,image/gif'
    },
    // 图片alt属性
    alt: {
      type: String,
      default: '图片'
    },
    // 是否自动上传
    autoUpload: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      uploadUrl: process.env.VUE_APP_BASE_API + '/system/photoConfig/uploadExample',
      headers: {
        Authorization: 'Bearer ' + getToken()
      },
      fileList: [],
      uploading: false,
      uploadPercentage: 0,
      uploadStatus: '',
      previewVisible: false,
      imageUrl: this.value
    }
  },
  watch: {
    value(newVal) {
      this.imageUrl = newVal
    },
    imageUrl(newVal) {
      this.$emit('input', newVal)
      this.$emit('change', newVal)
    }
  },
  methods: {
    // 上传前检查
    beforeUpload(file) {
      // 检查文件类型
      const isImage = file.type.startsWith('image/')
      if (!isImage) {
        this.$message.error('只能上传图片文件!')
        return false
      }

      // 检查文件大小
      const isLtMaxSize = file.size / 1024 / 1024 < this.maxSize
      if (!isLtMaxSize) {
        this.$message.error(`图片大小不能超过 ${this.maxSize}MB!`)
        return false
      }

      this.uploading = true
      this.uploadPercentage = 0
      this.uploadStatus = ''

      this.$emit('before-upload', file)
      return true
    },

    // 上传进度
    handleProgress(event) {
      this.uploadPercentage = Math.floor(event.percent)
      this.$emit('progress', event)
    },

    // 上传成功
    handleSuccess(response, file) {
      this.uploading = false
      this.uploadStatus = 'success'

      if (response.code === 200) {
        this.imageUrl = response.data.url
        this.$message.success('上传成功!')
        this.$emit('success', response, file)
      } else {
        this.$message.error(response.msg || '上传失败!')
        this.$emit('error', response, file)
      }
    },

    // 上传失败
    handleError(error, file) {
      this.uploading = false
      this.uploadStatus = 'exception'
      this.$message.error('上传失败，请重试!')
      this.$emit('upload-error', error, file)
    },

    // 预览图片
    previewImage() {
      if (this.imageUrl) {
        this.previewVisible = true
      }
    },

    // 删除图片
    removeImage() {
      this.$confirm('确定要删除这张图片吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 如果是上传到服务器的图片，需要调用删除接口
        if (this.imageUrl && this.imageUrl.includes('/profile/upload/')) {
          const fileName = this.imageUrl.substring(this.imageUrl.lastIndexOf('/') + 1)
          deleteExampleImage(fileName).then(response => {
            if (response.code === 200) {
              this.imageUrl = ''
              this.$message.success('删除成功!')
              this.$emit('remove')
            } else {
              this.$message.error(response.msg || '删除失败!')
            }
          }).catch(() => {
            // 即使删除失败，也清空本地显示
            this.imageUrl = ''
            this.$message.warning('图片已从本地移除，但服务器删除失败')
            this.$emit('remove')
          })
        } else {
          // 本地图片直接清空
          this.imageUrl = ''
          this.$message.success('删除成功!')
          this.$emit('remove')
        }
      }).catch(() => {
        // 用户取消删除
      })
    },

    // 手动触发上传
    submitUpload() {
      this.$refs.upload.submit()
    },

    // 清空文件列表
    clearFiles() {
      this.$refs.upload.clearFiles()
    },

    // 获取文件列表
    getFiles() {
      return this.$refs.upload.uploadFiles
    }
  }
}
</script>

<style lang="scss" scoped>
.image-upload-container {
  .image-uploader {
    width: 100%;

    ::v-deep .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: border-color 0.3s;

      &:hover {
        border-color: #409EFF;
      }
    }

    ::v-deep .el-upload-dragger {
      width: 100%;
      height: auto;
      min-height: 180px;
      border: none;
      background: transparent;
    }
  }

  .upload-placeholder {
    padding: 40px 20px;
    text-align: center;

    .uploader-icon {
      font-size: 28px;
      color: #8c939d;
      margin-bottom: 16px;
    }

    .upload-text {
      font-size: 14px;
      color: #606266;
      margin-bottom: 8px;
    }

    .upload-tip {
      font-size: 12px;
      color: #909399;
      line-height: 1.4;
    }
  }

  .image-preview {
    position: relative;
    width: 100%;
    height: auto;

    .preview-image {
      width: 100%;
      height: auto;
      max-height: 300px;
      object-fit: contain;
      display: block;
    }

    .image-actions {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      display: flex;
      gap: 8px;
      opacity: 0;
      transition: opacity 0.3s;
    }

    &:hover .image-actions {
      opacity: 1;
    }
  }

  .upload-progress {
    margin-top: 12px;
  }
}

// 响应式设计
@media (max-width: 768px) {
  .image-upload-container {
    .upload-placeholder {
      padding: 30px 15px;

      .uploader-icon {
        font-size: 24px;
      }

      .upload-text {
        font-size: 13px;
      }

      .upload-tip {
        font-size: 11px;
      }
    }

    .image-preview {
      .image-actions {
        .el-button {
          padding: 5px;

          ::v-deep i {
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>