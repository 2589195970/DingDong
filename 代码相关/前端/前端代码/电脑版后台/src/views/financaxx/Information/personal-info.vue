<template>
  <div class="app-container">
    <!-- 用户基本信息卡片 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>个人信息</span>
      </div>
      <el-row :gutter="20">
        <!-- 左侧：用户头像和基本信息 -->
        <el-col :span="6" :xs="24">
          <div class="user-profile">
            <div class="text-center">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" />用户名称
                <div class="pull-right">{{ user.userName || '未设置' }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" />创建日期
                <div class="pull-right">{{ user.createTime || '未知' }}</div>
              </li>
            </ul>
          </div>
        </el-col>

        <!-- 右侧：表单信息 -->
        <el-col :span="18" :xs="24">
          <el-tabs v-model="activeTab">
            <!-- 基本资料 -->
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" @refresh="getUserProfile" />
            </el-tab-pane>

            <!-- 修改密码 -->
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>

            <!-- 手机号绑定 -->
            <el-tab-pane label="手机号绑定" name="phone">
              <el-form label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="form-content">
                  <el-form-item label="当前手机号：">
                    <div v-if="queryParams2.phone">
                      <span>{{queryParams2.phone}}</span>
                      &emsp; &emsp;<span @click="clickPhone(queryParams2)" style="color: blue; cursor: pointer;">点击更换</span>
                    </div>
                    <span v-if="!queryParams2.phone">未绑定</span>
                  </el-form-item>
                </div>
              </el-form>
            </el-tab-pane>

            <!-- 实名认证 -->
            <el-tab-pane label="实名认证" name="realname">
              <el-form label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="form-content">
                  <el-form-item label="认证状态：">
                    <div v-if="queryParams2.isRealName==0">
                      <span>未实名</span>
                      &emsp; &emsp; <span @click="clickName" style="color: blue; cursor: pointer;"> 点击实名</span>
                    </div>
                    <div v-if="queryParams2.isRealName==1">
                      <span>已实名</span>
                    </div>
                    <div v-if="queryParams2.isRealName==2||queryParams2.isRealName==3">
                      <span v-if="queryParams2.isRealName==2">实名认证中</span>
                      <span v-if="queryParams2.isRealName==3" style="color: red;">实名认证失败</span>
                      <span @click="clickName1" style="color: blue; cursor: pointer;"> 更改实名信息</span>
                    </div>
                  </el-form-item>
                </div>
              </el-form>
            </el-tab-pane>
          </el-tabs>
        </el-col>
      </el-row>
    </el-card>

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
      activeTab: "userinfo",
      encryptionConfig: {
        key: 'ADGcp7Kiixe1x3Sn',
      },
    };
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
        this.queryParams2 = Object.assign({}, res.data);
        console.log(this.queryParams2);
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

    // 获取验证码
    getCode1() {
      if (this.formphone.phone) {
        if (this.countdown == "获取验证码") {
          sendSms(this.encryptAndEncode({
            phoneNumber: this.formphone.phone,
            smsTemplateType: 0,
          })).then((res) => {
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
          console.log(this.queryParams2);
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
.text-center {
  text-align: center;
}

.list-group {
  padding-left: 0;
  list-style: none;
}

.list-group-striped {
  border-top: 1px solid #e4e7ed;
  border-bottom: 1px solid #e4e7ed;
}

.list-group-item {
  border-bottom: 1px solid #e4e7ed;
  border-left: 1px solid #e4e7ed;
  border-right: 1px solid #e4e7ed;
  padding: 11px 0;
  font-size: 13px;
}

.pull-right {
  float: right !important;
}

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

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
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
}

.login-code {
  width: 25%;
  height: 38px;
  float: right;
  text-align: center;
  border-radius: 20px;
  border: 1px solid #dcdfe6;
  cursor: pointer;
}

.login-code img {
  cursor: pointer;
  vertical-align: middle;
}
</style>