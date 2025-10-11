<template>
  <el-dialog
    title="公告详情"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
    custom-class="notice-detail-dialog"
  >
    <div v-if="notice" class="notice-content">
      <!-- 公告头部信息 -->
      <div class="notice-header">
        <h3 class="notice-title">{{ notice.noticeTitle }}</h3>
        <div class="notice-meta">
          <el-tag
            :type="getNoticeTypeTag(notice.noticeType)"
            size="mini"
          >
            {{ getNoticeTypeText(notice.noticeType) }}
          </el-tag>
          <span class="notice-time">
            <i class="el-icon-time"></i>
            {{ parseTime(notice.createTime) }}
          </span>
          <span class="notice-author">
            <i class="el-icon-user"></i>
            {{ notice.createBy }}
          </span>
        </div>
      </div>

      <!-- 产品公告特殊展示 -->
      <div v-if="isProductNotice" class="product-notice-content">
        <div v-html="notice.noticeContent" class="product-html-content"></div>
      </div>

      <!-- 普通公告展示 -->
      <div v-else class="regular-notice-content">
        <div class="notice-text" v-html="formattedContent"></div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button v-if="!isProductNotice" type="primary" @click="markAsRead">标记已读</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { parseTime } from '@/utils/ruoyi'

export default {
  name: 'NoticeDetail',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    noticeData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      dialogVisible: false,
      notice: null
    }
  },
  computed: {
    isProductNotice() {
      return this.notice && this.notice.noticeTitle === '产品上下架公告'
    },
    formattedContent() {
      if (!this.notice || !this.notice.noticeContent) {
        return ''
      }
      // 处理换行符，将\n转换为<br>
      return this.notice.noticeContent.replace(/\n/g, '<br>')
    }
  },
  watch: {
    visible(newVal) {
      this.dialogVisible = newVal
      if (newVal && this.noticeData) {
        this.notice = { ...this.noticeData }
      }
    },
    noticeData(newVal) {
      if (newVal) {
        this.notice = { ...newVal }
      }
    }
  },
  methods: {
    parseTime,

    handleClose() {
      this.dialogVisible = false
      this.$emit('update:visible', false)
    },

    getNoticeTypeTag(type) {
      const typeMap = {
        '1': 'info',
        '2': 'success'
      }
      return typeMap[type] || 'info'
    },

    getNoticeTypeText(type) {
      const typeMap = {
        '1': '通知',
        '2': '公告'
      }
      return typeMap[type] || '未知'
    },

    markAsRead() {
      // 这里可以调用API标记公告为已读
      this.$message.success('已标记为已读')
      this.handleClose()
    }
  }
}
</script>

<style lang="scss">
.notice-detail-dialog {
  .el-dialog__body {
    padding: 20px;
  }
}

.notice-content {
  .notice-header {
    margin-bottom: 20px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;

    .notice-title {
      margin: 0 0 10px 0;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }

    .notice-meta {
      display: flex;
      align-items: center;
      gap: 15px;
      font-size: 13px;
      color: #909399;

      .notice-time,
      .notice-author {
        display: flex;
        align-items: center;
        gap: 4px;
      }
    }
  }

  .product-notice-content {
    margin: 20px 0;

    .product-html-content {
      background: #fafafa;
      padding: 16px;
      border-radius: 6px;
      border: 1px solid #e4e7ed;
    }
  }

  .regular-notice-content {
    margin: 20px 0;

    .notice-text {
      line-height: 1.6;
      color: #606266;
      background: #f8f9fa;
      padding: 16px;
      border-radius: 6px;
      border-left: 4px solid #409eff;
    }
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>