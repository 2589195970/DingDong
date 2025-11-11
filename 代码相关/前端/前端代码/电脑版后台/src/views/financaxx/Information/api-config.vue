<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- H5入口配置 -->
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>H5入口配置</span>
          </div>
          <el-form label-width="150px" class="demo-ruleForm" :inline="true">
            <div class="form-content">
              <el-form-item label="我的店铺链接：">
                <span>{{queryParams1.shopUrl}}</span>
                &emsp; &emsp;<span v-if="queryParams1.shopUrl" @click="share(queryParams1.shopQrcodeMap)"
                  style="color: red; cursor: pointer;">分享店铺</span>
              </el-form-item>
              <el-form-item label="移动端url：">
                <span>{{queryParams1.mobileUrl}}</span>
              </el-form-item>
              <el-form-item label="推广邀请：">
                <span>{{queryParams1.extendUrl}}</span>
              </el-form-item>
              <el-form-item label="推广海报图：">
                <div class="poster-container">
                  <!-- 海报图1 -->
                  <div class="poster-item">
                    <div class="poster-wrapper">
                      <img
                        v-if="queryParams1.registerQrcodeMap1"
                        :src="queryParams1.registerQrcodeMap1"
                        alt="海报图1"
                        class="poster-image"
                        @click="share(queryParams1.registerQrcodeMap1)"
                      >
                      <div v-else class="poster-placeholder">
                        <i class="el-icon-picture-outline"></i>
                        <span>暂无海报图1</span>
                      </div>
                      <div class="poster-actions">
                        <el-button
                          v-if="queryParams1.registerQrcodeMap1"
                          size="mini"
                          type="success"
                          icon="el-icon-view"
                          @click="share(queryParams1.registerQrcodeMap1)"
                        >
                          预览
                        </el-button>
                        <el-button
                          size="mini"
                          type="warning"
                          icon="el-icon-refresh"
                          :loading="resetLoading[1]"
                          @click="handleResetPoster(1)"
                        >
                          重置生成
                        </el-button>
                      </div>
                    </div>
                    <div class="poster-title">海报图1</div>
                  </div>

                  <!-- 海报图2 -->
                  <div class="poster-item">
                    <div class="poster-wrapper">
                      <img
                        v-if="queryParams1.registerQrcodeMap2"
                        :src="queryParams1.registerQrcodeMap2"
                        alt="海报图2"
                        class="poster-image"
                        @click="share(queryParams1.registerQrcodeMap2)"
                      >
                      <div v-else class="poster-placeholder">
                        <i class="el-icon-picture-outline"></i>
                        <span>暂无海报图2</span>
                      </div>
                      <div class="poster-actions">
                        <el-button
                          v-if="queryParams1.registerQrcodeMap2"
                          size="mini"
                          type="success"
                          icon="el-icon-view"
                          @click="share(queryParams1.registerQrcodeMap2)"
                        >
                          预览
                        </el-button>
                        <el-button
                          size="mini"
                          type="warning"
                          icon="el-icon-refresh"
                          :loading="resetLoading[2]"
                          @click="handleResetPoster(2)"
                        >
                          重置生成
                        </el-button>
                      </div>
                    </div>
                    <div class="poster-title">海报图2</div>
                  </div>

                  <!-- 海报图3 -->
                  <div class="poster-item">
                    <div class="poster-wrapper">
                      <img
                        v-if="queryParams1.registerQrcodeMap3"
                        :src="queryParams1.registerQrcodeMap3"
                        alt="海报图3"
                        class="poster-image"
                        @click="share(queryParams1.registerQrcodeMap3)"
                      >
                      <div v-else class="poster-placeholder">
                        <i class="el-icon-picture-outline"></i>
                        <span>暂无海报图3</span>
                      </div>
                      <div class="poster-actions">
                        <el-button
                          v-if="queryParams1.registerQrcodeMap3"
                          size="mini"
                          type="success"
                          icon="el-icon-view"
                          @click="share(queryParams1.registerQrcodeMap3)"
                        >
                          预览
                        </el-button>
                        <el-button
                          size="mini"
                          type="warning"
                          icon="el-icon-refresh"
                          :loading="resetLoading[3]"
                          @click="handleResetPoster(3)"
                        >
                          重置生成
                        </el-button>
                      </div>
                    </div>
                    <div class="poster-title">海报图3</div>
                  </div>
                </div>
              </el-form-item>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- API对接配置 -->
      <el-col :span="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>API对接配置</span>
          </div>
          <el-form label-width="150px" class="demo-ruleForm" :inline="true">
            <div class="form-content">
              <el-form-item label="下单地址：">
                <span>{{queryParams.apiUrl}}</span>
              </el-form-item>
              <el-form-item label="回调地址：">
                <div v-if="queryParams.callbackUrl">
                  <span>{{queryParams.callbackUrl}}</span>
                  &emsp; &emsp;<span @click="clickcallback" style="color: blue; cursor: pointer;">更换回调地址</span>
                </div>
                <div v-else>
                  <span>{{queryParams.callbackUrl}}</span>
                  <span @click="clickcallback" style="color: blue; cursor: pointer;">添加回调地址</span>
                </div>
              </el-form-item>
              <el-form-item label="商户ID：">
                <span>{{queryParams.agentCode}}</span>
              </el-form-item>
              <el-form-item label="apikey：">
                <span>{{queryParams.securityKey}}</span>
              </el-form-item>
            </div>
          </el-form>
        </el-card>
      </el-col>
    </el-row>

    <!-- 分享弹窗 -->
    <el-dialog :visible.sync="shareOpen" width="350px" append-to-body>
      <img :src="sharedata" alt="" style="width: 100%;">
      <div slot="footer">
        <el-button type="primary" @click="downloadImage">保存图片</el-button>
      </div>
    </el-dialog>

    <!-- 回调地址弹窗 -->
    <el-dialog :visible.sync="open" width="350px" append-to-body>
      <el-form ref="form" v-model="form" label-width="100px">
        <el-form-item label="回调地址：">
          <el-input v-model="queryParams.callbackUrl"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormUpdata">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAgentApiVO, getAgentExtendUrlVO, updateCallbackUrl, resetPosterImage } from "@/api/monitor/finance";

export default {
  name: "ApiConfig",
  data() {
    return {
      queryParams: {},
      queryParams1: {},
      form: {},
      open: false,
      shareOpen: false,
      sharedata: '',
      resetLoading: {
        1: false,
        2: false,
        3: false
      }
    };
  },
  beforeCreate() {
    getAgentApiVO().then((res) => {
      this.$set(this, 'queryParams', res.data);
      console.log(JSON.parse(JSON.stringify(this.queryParams)));
      this.queryParams = this.queryParams
    });

    getAgentExtendUrlVO().then((res) => {
      this.queryParams1 = { ...res.data };
      console.log(this.queryParams1);
    });
  },
  methods: {
    // 分享
    share(data) {
      this.sharedata = data;
      this.shareOpen = true;
    },

    // 下载图片
    downloadImage() {
      const url = this.sharedata;
      const link = document.createElement('a');
      link.href = url;
      link.download = 'img.jpg';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    // 点击回调地址
    clickcallback() {
      this.open = true;
    },

    // 提交回调地址
    submitFormUpdata() {
      updateCallbackUrl(this.queryParams.agentCode, this.queryParams.callbackUrl).then((res) => {
        this.$message({
          type: 'success',
          message: '添加成功!'
        });
        this.open = false;
        getAgentApiVO().then((res) => {
          this.$set(this, 'queryParams', res.data);
          console.log(JSON.parse(JSON.stringify(this.queryParams)));
          this.queryParams = this.queryParams
        });
      })
    },

    // 重置生成海报图
    async handleResetPoster(posterIndex) {
      try {
        await this.$confirm(`确认重新生成海报图${posterIndex}吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        });
      } catch (cancelError) {
        return;
      }

      this.$set(this.resetLoading, posterIndex, true);

      try {
        const response = await resetPosterImage(posterIndex);

        if (response.code === 200) {
          this.$message({
            type: 'success',
            message: '海报图已重新生成!'
          });

          const key = `registerQrcodeMap${posterIndex}`;
          if (response.data) {
            this.$set(this.queryParams1, key, response.data);
          } else {
            const res = await getAgentExtendUrlVO();
            this.queryParams1 = { ...res.data };
          }
        } else {
          this.$message.error(response.message || '重置失败');
        }
      } catch (error) {
        console.error('重置海报图失败:', error);
        const message = (error && error.message) ? error.message : '重置失败，请稍后重试';
        this.$message.error(message);
      } finally {
        this.$set(this.resetLoading, posterIndex, false);
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.form-content {
  padding: 20px;
}

.el-form-item__content {
  width: 80%;
}

.el-input-medium {
  width: 80%;
}

.el-form-item {
  width: 100%;
}

.box-card {
  margin-bottom: 20px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}
.clearfix:after {
  clear: both;
}

// 海报图容器样式
.poster-container {
  display: flex;
  gap: 20px;
  margin-top: 10px;

  .poster-item {
    flex: 1;
    min-width: 200px;

    .poster-wrapper {
      border: 2px dashed #d9d9d9;
      border-radius: 8px;
      padding: 10px;
      text-align: center;
      background-color: #fafafa;
      transition: border-color 0.3s;

      &:hover {
        border-color: #409eff;
      }

      .poster-image {
        width: 100%;
        max-width: 200px;
        height: 280px;
        object-fit: cover;
        border-radius: 4px;
        cursor: pointer;
        transition: transform 0.3s;

        &:hover {
          transform: scale(1.02);
        }
      }

      .poster-placeholder {
        width: 100%;
        max-width: 200px;
        height: 280px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #999;
        background-color: #f5f5f5;
        border-radius: 4px;
        margin: 0 auto;

        i {
          font-size: 48px;
          margin-bottom: 10px;
          color: #c0c4cc;
        }

        span {
          font-size: 14px;
        }
      }

      .poster-actions {
        margin-top: 10px;
        display: flex;
        gap: 8px;
        justify-content: center;
        align-items: center;

        // 统一所有按钮样式
        .el-button {
          padding: 7px 15px !important;
          font-size: 12px !important;
          min-width: 60px !important;
          height: 32px !important;
          line-height: 1 !important;
          border-radius: 4px !important;
          box-sizing: border-box !important;
          display: inline-flex !important;
          align-items: center !important;
          justify-content: center !important;
        }
      }
    }

    .poster-title {
      text-align: center;
      margin-top: 8px;
      font-size: 14px;
      color: #666;
      font-weight: 500;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .poster-container {
    flex-direction: column;
    gap: 15px;

    .poster-item {
      min-width: auto;

      .poster-wrapper {
        .poster-image {
          max-width: 150px;
          height: 210px;
        }

        .poster-placeholder {
          max-width: 150px;
          height: 210px;

          i {
            font-size: 36px;
          }
        }

        .poster-actions {
          // 移动端按钮调整
          .el-button {
            padding: 6px 12px !important;
            font-size: 11px !important;
            min-width: 50px !important;
            height: 28px !important;
          }
        }
      }
    }
  }
}
</style>
