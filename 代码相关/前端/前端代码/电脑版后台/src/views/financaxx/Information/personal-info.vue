<template>
  <div class="app-container personal-info-page">
    <div class="page-header">
      <div class="page-heading">
        <h2>个人信息与实名认证</h2>
        <p>完善资料，保障账户与交易安全</p>
      </div>
      <div class="status-pill" :class="`status-pill--${realNameStatus.state}`">
        <i class="status-dot"></i>
        <span>{{ realNameStatus.text }}</span>
      </div>
    </div>

    <el-row :gutter="24">
      <el-col :span="7" :xs="24">
        <div class="info-panel">
          <div class="info-panel__section info-panel__section--center">
            <userAvatar :user="user" />
            <div class="user-name">{{ user.userName || '未设置' }}</div>
            <div class="user-meta">{{ agentCodeText }}</div>
          </div>
          <div class="info-panel__section">
            <div class="meta-item">
              <span class="meta-label">创建日期</span>
              <span class="meta-value">{{ user.createTime || '未知' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-label">绑定手机号</span>
              <span class="meta-value">{{ queryParams2.phone || '未绑定' }}</span>
            </div>
            <div class="meta-item">
              <span class="meta-label">实名认证</span>
              <span class="meta-value">{{ realNameStatus.shortText }}</span>
            </div>
          </div>
          <div class="info-panel__section info-panel__section--actions">
            <el-button
              v-if="realNameStatus.showAction"
              size="mini"
              type="primary"
              @click="handleRealNameShortcut"
            >
              {{ realNameStatus.actionText }}
            </el-button>
            <el-button size="mini" type="text" @click="activeTab = 'userinfo'">完善资料</el-button>
          </div>
        </div>

        <div class="status-card">
          <p class="status-card__title">实名认证提醒</p>
          <p class="status-card__desc">{{ realNameStatus.desc }}</p>
          <div class="status-card__meta">
            <span>最近提交</span>
            <span>{{ queryParams2.auditTime || '-' }}</span>
          </div>
        </div>
      </el-col>

      <el-col :span="17" :xs="24">
        <div class="content-card">
          <el-tabs v-model="activeTab" class="profile-tabs">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" @refresh="getUserProfile" />
            </el-tab-pane>

            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>

            <el-tab-pane label="手机号绑定" name="phone">
              <div class="section-card">
                <div class="section-card__item">
                  <span class="section-card__label">当前手机号</span>
                  <div class="section-card__value">
                    <template v-if="queryParams2.phone">
                      <span>{{ queryParams2.phone }}</span>
                      <el-button type="text" size="mini" @click="clickPhone(queryParams2)">更换</el-button>
                    </template>
                    <span v-else>未绑定</span>
                  </div>
                </div>
              </div>
            </el-tab-pane>

            <el-tab-pane label="实名认证" name="realname">
              <div class="status-callout" :class="`status-callout--${realNameStatus.state}`">
                <div>
                  <p class="status-callout__title">{{ realNameStatus.text }}</p>
                  <p class="status-callout__desc">{{ realNameStatus.desc }}</p>
                </div>
                <el-button
                  v-if="realNameStatus.showAction"
                  size="small"
                  type="primary"
                  @click="handleRealNameShortcut"
                >
                  {{ realNameStatus.actionText }}
                </el-button>
              </div>
              <p class="status-hint" v-if="realNameStatus.state === 'pending'">
                完成实名认证后即可查看全部菜单、发起业务及提现操作。
              </p>
              <p class="status-hint" v-else-if="realNameStatus.state === 'processing'">
                审核期间请保持手机畅通，如需补充材料可随时更新信息。
              </p>
              <p class="status-hint status-hint--danger" v-else-if="realNameStatus.state === 'danger'">
                资料未通过审核，可重新提交或联系客服确认原因。
              </p>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-col>
    </el-row>

    <!-- 手机号修改弹窗 -->
    <el-dialog :visible.sync="openphone" width="450px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" v-model="formphone" label-width="100px">
        <el-form-item label="手机号：">
          <el-input v-model="formphone.phone"></el-input>
        </el-form-item>
        <el-form-item prop="smsCode">
          <el-input v-model="formphone.smsCode" auto-complete="off" placeholder="验证码" style="width: 70%">
            <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
          </el-input>
          <div class="login-code" @click="getCode1">
            {{ countdown }}
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitphone">修改</el-button>
      </div>
    </el-dialog>

    <!-- 实名认证弹窗 -->
    <el-dialog :visible.sync="openName" width="450px" append-to-body>
      <el-form ref="form" v-model="form" label-width="100px">
        <el-form-item label="姓名">
          <el-input v-model="form.cardName"></el-input>
        </el-form-item>
        <el-form-item label="身份证">
          <el-input v-model="form.cardId"></el-input>
        </el-form-item>
        <el-form-item label="身份证正面" prop="resource" style="width: 200px;height: 200px;">
          <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
            :on-success="handleAvatarSuccess" :headers=headers>
            <img v-if="form.cardIdUrlFront" :src="form.cardIdUrlFront" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="身份证反面" prop="resource" style="width: 200px;height: 200px;">
          <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
            :on-success="handleAvatarSuccess1" :headers=headers>
            <img v-if="form.cardIdUrlBack" :src="form.cardIdUrlBack" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAudit">提交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAgentInfoVO, updateAgentPhone } from "@/api/monitor/finance";
import { addNameAudit, updateNameAudit, selectNameAudit } from "@/api/monitor/business";
import { getUserProfile } from "@/api/system/user";
import { getToken } from "@/utils/auth";
import { sendSms } from "@/api/login";
import CryptoJS from 'crypto-js'
import userAvatar from "@/views/system/user/profile/userAvatar";
import userInfo from "@/views/system/user/profile/userInfo";
import resetPwd from "@/views/system/user/profile/resetPwd";

export default {
  name: "PersonalInfo",
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture",
      headers: { Authorization: "Bearer " + getToken() },
      queryParams2: {},
      user: {},
      form: {},
      formphone: {},
      countdown: "获取验证码",
      openName: false,
      openphone: false,
      xgai: false,
      activeTab: "realname",
      encryptionConfig: {
        key: 'ADGcp7Kiixe1x3Sn',
      },
    };
  },
  computed: {
    realNameStatus() {
      const status = Number(this.queryParams2.isRealName)
      const config = {
        1: {
          state: 'success',
          text: '已完成实名认证',
          shortText: '已实名',
          desc: '账号已通过审核，可正常使用系统全部功能。',
          showAction: false,
          actionText: ''
        },
        2: {
          state: 'processing',
          text: '实名认证审核中',
          shortText: '审核中',
          desc: '资料已提交，审核期间可随时更新信息。',
          showAction: true,
          actionText: '更新资料'
        },
        3: {
          state: 'danger',
          text: '实名认证失败',
          shortText: '认证失败',
          desc: '资料未通过审核，请按照提示重新提交。',
          showAction: true,
          actionText: '重新提交'
        }
      }
      return config[status] || {
        state: 'pending',
        text: '尚未完成实名认证',
        shortText: '未实名',
        desc: '提交实名认证后即可查看全部菜单并发起业务。',
        showAction: true,
        actionText: '立即认证'
      }
    },
    agentCodeText() {
      return this.queryParams2.agentCode ? `代理编码：${this.queryParams2.agentCode}` : '暂无代理编码'
    }
  },
  created() {
    this.getUser();
    this.getAgentInfo();
  },
  methods: {
    getUser() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    getAgentInfo() {
      getAgentInfoVO().then((res) => {
        this.queryParams2 = Object.assign({}, res.data || {});
        const status = this.queryParams2.isRealName
        this.syncAgentAccountStatus(status);
        this.activeTab = Number(status) === 1 ? 'userinfo' : 'realname'
      });
    },
    getUserProfile() {
      getUserProfile().then(response => {
        this.user = response.data;
      });
    },
    // 点击更换手机号
    clickPhone(data) {
      this.formphone = data;
      this.openphone = true;
    },
    handleRealNameShortcut() {
      this.activeTab = 'realname';
      const status = Number(this.queryParams2.isRealName);
      if (status === 2 || status === 3) {
        this.clickName1();
      } else {
        this.clickName();
      }
    },

    // 点击实名认证
    clickName() {
      this.openName = true;
      this.xgai = true;
    },

    // 点击更改实名信息
    clickName1() {
      selectNameAudit().then((res) => {
        this.openName = true;
        this.form = res.data;
        this.xgai = false;
      });
    },

    syncAgentAccountStatus(status) {
      const normalized = Number(status != null ? status : 0)
      const current = this.$store.getters.agentAccount || {}
      this.$store.commit('SET_AGENT_ACCOUNT', {
        ...current,
        realNameStatus: normalized,
        isRealName: normalized === 1,
        yisRealName: normalized === 1
      })
    },

    // 获取验证码
    getCode1() {
      if (this.formphone.phone) {
        if (this.countdown === "获取验证码") {
          sendSms(this.encryptAndEncode({
            phoneNumber: this.formphone.phone,
            smsTemplateType: 0,
          })).then(() => {
            this.$message({
              message: "验证码已发送",
              type: "success",
            });
            this.countdown = 60;
            this.countdown--;
            const taskId = setInterval(() => {
              this.countdown--;
              if (this.countdown === 0) {
                this.countdown = "获取验证码";
                clearInterval(taskId);
              }
            }, 1000);
          });
        } else {
          this.$message.error("已发送验证码,请稍后再试");
        }
      } else {
        this.$message.error("请填写账号信息");
      }
    },

    // 加密方法
    encryptAndEncode(data) {
      try {
        const jsonString = JSON.stringify(data)
        const key = CryptoJS.enc.Utf8.parse(this.encryptionConfig.key)
        const encrypted = CryptoJS.AES.encrypt(jsonString, key, {
          mode: CryptoJS.mode.ECB,
          padding: CryptoJS.pad.Pkcs7
        })
        const base64Cipher = encrypted.toString()
        const urlSafeCipher = encodeURIComponent(base64Cipher)
        return urlSafeCipher
      } catch (error) {
        console.error('加密失败:', error)
        throw new Error('数据加密处理失败')
      }
    },

    // 提交手机号修改
    submitphone() {
      updateAgentPhone(this.formphone.smsCode, this.formphone.phone).then((res) => {
        this.$message({
          type: 'success',
          message: '修改成功!'
        });
        // 刷新代理商信息
        getAgentInfoVO().then((res) => {
          this.queryParams2 = Object.assign({}, res.data);
        });
        // 刷新用户基本信息
        this.getUserProfile();
        this.openphone = false;
      })
    },

    // 提交实名认证
    submitAudit() {
      this.form.agentCode = this.queryParams2.agentCode;
      if (this.xgai) {
        addNameAudit(this.form).then(() => {
          this.$message({
            type: 'success',
            message: '已提交审核'
          })
          // 刷新代理商信息
          this.getAgentInfo();
        })
      } else {
        updateNameAudit(this.form).then(() => {
          this.$message({
            type: 'success',
            message: '已提交'
          })
          // 刷新代理商信息
          this.getAgentInfo();
        })
      }
      this.openName = false;
    },

    // 身份证正面上传成功
    handleAvatarSuccess(res) {
      this.$set(this.form, 'cardIdUrlFront', res.message)
    },

    // 身份证反面上传成功
    handleAvatarSuccess1(res) {
      this.$set(this.form, 'cardIdUrlBack', res.message)
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.personal-info-page {
  background-color: #f5f6f8;
  padding-bottom: 32px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 24px;
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  margin-bottom: 24px;
}

.page-heading {
  h2 {
    margin: 0;
    font-size: 22px;
    font-weight: 600;
    color: #1f2f3d;
  }

  p {
    margin: 6px 0 0;
    color: #909399;
    font-size: 14px;
  }
}

.status-pill {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  border-radius: 999px;
  font-size: 13px;
  border: 1px solid transparent;
  color: #606266;

  .status-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    margin-right: 6px;
    background: currentColor;
  }
}

.status-pill--pending {
  background: #fff7e6;
  color: #d48806;
  border-color: #ffe7ba;
}

.status-pill--processing {
  background: #e6f7ff;
  color: #1890ff;
  border-color: #bae7ff;
}

.status-pill--success {
  background: #f6ffed;
  color: #52c41a;
  border-color: #d9f7be;
}

.status-pill--danger {
  background: #fff1f0;
  color: #f5222d;
  border-color: #ffccc7;
}

.info-panel,
.status-card,
.content-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 20px;
}

.info-panel__section {
  & + & {
    border-top: 1px solid #f2f4f5;
    margin-top: 16px;
    padding-top: 16px;
  }

  &--center {
    text-align: center;
  }

  &--actions {
    display: flex;
    gap: 8px;
    justify-content: flex-start;
    flex-wrap: wrap;
  }
}

.user-name {
  margin-top: 12px;
  font-size: 18px;
  font-weight: 600;
  color: #1f2f3d;
}

.user-meta {
  margin-top: 4px;
  color: #909399;
  font-size: 13px;
}

.meta-item {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;

  &:last-child {
    margin-bottom: 0;
  }
}

.meta-label {
  color: #909399;
  font-size: 13px;
}

.meta-value {
  color: #303133;
  font-weight: 500;
}

.status-card__title {
  font-size: 15px;
  font-weight: 600;
  margin: 0 0 8px;
  color: #1f2f3d;
}

.status-card__desc {
  margin: 0 0 14px;
  color: #606266;
  line-height: 1.6;
  font-size: 13px;
}

.status-card__meta {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
  color: #909399;
}

.profile-tabs {
  ::v-deep .el-tabs__header {
    margin-bottom: 16px;
  }

  ::v-deep .el-tabs__nav-wrap::after {
    display: none;
  }

  ::v-deep .el-tabs__item {
    font-size: 15px;
    color: #606266;
    padding: 0 20px;

    &.is-active {
      color: #1890ff;
      font-weight: 600;
    }
  }

  ::v-deep .el-tabs__active-bar {
    background: #1890ff;
    height: 2px;
  }
}

.section-card {
  border: 1px dashed #e4e7ed;
  border-radius: 10px;
  padding: 16px;
}

.section-card__item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-card__label {
  color: #909399;
  font-size: 14px;
}

.section-card__value {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 500;
  color: #303133;
}

.status-callout {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 20px;
  border-radius: 12px;
  border: 1px solid transparent;
  margin-bottom: 12px;
}

.status-callout__title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #1f2f3d;
}

.status-callout__desc {
  margin: 6px 0 0;
  font-size: 13px;
  color: #606266;
}

.status-callout--pending {
  background: #fffdf6;
  border-color: #ffe7ba;
}

.status-callout--processing {
  background: #f0f9ff;
  border-color: #bae7ff;
}

.status-callout--success {
  background: #f6ffed;
  border-color: #d9f7be;
}

.status-callout--danger {
  background: #fff2f0;
  border-color: #ffccc7;
}

.status-hint {
  margin: 4px 0 0;
  font-size: 13px;
  color: #909399;
}

.status-hint--danger {
  color: #f5222d;
}

::v-deep .avatar-uploader .el-upload {
  border: 2px dashed #d9d9d9;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    border-color: #1890ff;
  }
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
  border-radius: 12px;
}

.login-code {
  width: 25%;
  height: 38px;
  float: right;
  text-align: center;
  border-radius: 8px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
  background: #f5f7fa;
  color: #606266;
  font-size: 14px;
  line-height: 38px;
  transition: all 0.3s ease;

  &:hover {
    background: #1890ff;
    color: #fff;
    border-color: #1890ff;
  }
}

@media (max-width: 768px) {
  .status-card,
  .info-panel,
  .content-card {
    padding: 20px;
  }

  .status-card__meta,
  .meta-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 4px;
  }
}
</style>
