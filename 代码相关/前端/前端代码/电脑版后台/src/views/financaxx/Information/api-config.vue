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
                &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap1"
                  @click="share(queryParams1.registerQrcodeMap1)" style="color: red; cursor: pointer;">海报图1</span>
                &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap2"
                  @click="share(queryParams1.registerQrcodeMap2)" style="color: red; cursor: pointer;">海报图2</span>
                &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap3"
                  @click="share(queryParams1.registerQrcodeMap3)" style="color: red; cursor: pointer;">海报图3</span>
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
import { getAgentApiVO, getAgentExtendUrlVO, updateCallbackUrl } from "@/api/monitor/finance";

export default {
  name: "ApiConfig",
  data() {
    return {
      queryParams: {},
      queryParams1: {},
      form: {},
      open: false,
      shareOpen: false,
      sharedata: ''
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
</style>