<template>
    <el-form label-width="150px" class="demo-ruleForm" :inline="true">
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">H5入口
            </div>
            <div class="aly">
                <el-form-item label="我的店铺链接：">
                    <span>{{queryParams1.shopUrl}}</span>
                    &emsp; &emsp;<span v-if="queryParams1.shopUrl" @click="share(queryParams1.shopQrcodeMap)"
                        style="color: red;">分享店铺</span>
                </el-form-item>
                <el-form-item label="移动端url：">
                    <span>{{queryParams1.mobileUrl}}</span>
                </el-form-item>
                <el-form-item label="推广邀请：">
                    <span>{{queryParams1.extendUrl}}</span>
                    &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap1"
                        @click="share(queryParams1.registerQrcodeMap1)" style="color: red;">海报图1</span>
                    &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap2"
                        @click="share(queryParams1.registerQrcodeMap2)" style="color: red;">海报图2</span>
                    &emsp; &emsp;<span v-if="queryParams1.registerQrcodeMap3"
                        @click="share(queryParams1.registerQrcodeMap3)" style="color: red;">海报图3</span>
                </el-form-item>
            </div>

        </div>
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">Api对接
            </div>
            <div class="aly">
                <el-form-item label="下单地址：">
                    <span>{{queryParams.apiUrl}}</span>
                </el-form-item>
                <el-form-item label="回调地址：">
                    <div v-if="queryParams.callbackUrl">
                        <span>{{queryParams.callbackUrl}}</span>
                        &emsp; &emsp;<span @click="clickcallback" style="color: blue;">更换回调地址</span>
                    </div>
                    <div v-else>
                        <span>{{queryParams.callbackUrl}}</span>
                        <span @click="clickcallback" style="color: blue;">添加回调地址</span>
                    </div>

                </el-form-item>
                <el-form-item label="商户ID：">
                    <span>{{queryParams.agentCode}}</span>
                </el-form-item>
                <el-form-item label="apikey：">
                    <span>{{queryParams.securityKey}}</span>
                </el-form-item>
            </div>

        </div>
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">信息绑定
            </div>
            <div class="aly">
                <el-form-item label="手机号：">
                    <div v-if="queryParams2.phone">
                        <span>{{queryParams2.phone}}</span>
                        &emsp; &emsp;<span @click="clickPhone(queryParams2)" style="color: blue;">点击更换</span>
                    </div>


                    <span v-if="!queryParams2.phone">未绑定</span>
                </el-form-item>
                <el-form-item label="实名认证：">
                    <div v-if="queryParams2.isRealName==0">
                        <span>未实名</span>
                        &emsp; &emsp; <span @click="clickName" style="color: blue;"> 点击实名</span>
                    </div>
                    <div v-if="queryParams2.isRealName==1">
                        <span v-if="queryParams2.isRealName==1">已实名</span>
                    </div>
                    <div v-if="queryParams2.isRealName==2||queryParams2.isRealName==3">
                        <span v-if="queryParams2.isRealName==2">实名认证中</span>
                        <span v-if="queryParams2.isRealName==3" style="color: red;">实名认证失败</span>
                        <span @click="clickName1" style="color: blue;"> 更改实名信息</span>
                    </div>
                </el-form-item>
            </div>

        </div>
        <!-- 分享 -->
        <el-dialog :visible.sync="shareOpen" width="350px" append-to-body>
            <img :src="sharedata" alt="" style="width: 100%;">
            <div slot="footer">
                <el-button type="primary" @click="downloadImage">保存图片</el-button>
            </div>
        </el-dialog>

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
    </el-form>

</template>



<script>
    import Stomp from 'stompjs'
    import { getAgentApiVO, getAgentExtendUrlVO, getAgentInfoVO, updateCallbackUrl, updateAgentPhone } from "@/api/monitor/finance";
    import { addNameAudit, updateNameAudit, selectNameAudit } from "@/api/monitor/business";
    import { getToken } from "@/utils/auth";
    import { sendSms } from "@/api/login";
    import CryptoJS from 'crypto-js'
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址,
                // 设置上传的请求头部
                headers: { Authorization: "Bearer " + getToken() },
                // 表格数据
                list: [],
                ruleForm: [],
                // 导入文件
                form: {},
                countdown: "获取验证码",
                formphone: {},
                open: false,
                shareOpen: false,
                sharedata: '',
                openName: false,
                openphone: false,
                xgai: false,
                api: [],
                encryptionConfig: {
                    key: 'ADGcp7Kiixe1x3Sn', // 32字节密钥
                },
                groupCode: [],
                queryParams: {},
                queryParams1: {},
                queryParams2: {},
            };
        },
        beforeCreate() {
            getAgentApiVO().then((res) => {
                this.$set(this, 'queryParams', res.data); // 使用 $set 确保响应式
                console.log(JSON.parse(JSON.stringify(this.queryParams))); // 打印原始数据
                this.queryParams = this.queryParams
            });

            getAgentExtendUrlVO().then((res) => {
                this.queryParams1 = { ...res.data }; // 展开运算符创建新对象
                console.log(this.queryParams1);
            });

            getAgentInfoVO().then((res) => {
                this.queryParams2 = Object.assign({}, res.data); // 使用 Object.assign
                console.log(this.queryParams2);
            });
        },
        created() {

        },
        methods: {
            downloadImage() {
                // 图片地址（需允许跨域访问）
                const url = this.sharedata;

                // 创建隐藏的 <a> 标签
                const link = document.createElement('a');
                link.href = url;
                link.download = 'img.jpg'; // 设置下载文件名
                document.body.appendChild(link);

                // 触发点击下载
                link.click();

                // 清理 DOM
                document.body.removeChild(link);
            },
            // 分享
            share(data) {
                this.sharedata = data;
                this.shareOpen = true;

            },
            encryptAndEncode(data) {
                try {
                    // 1. 对象转JSON字符串
                    const jsonString = JSON.stringify(data)

                    // 2. 准备密钥和IV
                    const key = CryptoJS.enc.Utf8.parse(this.encryptionConfig.key)

                    // 3. AES-CBC加密
                    const encrypted = CryptoJS.AES.encrypt(jsonString, key, {
                        mode: CryptoJS.mode.ECB,
                        padding: CryptoJS.pad.Pkcs7
                    })

                    // 4. 获取Base64格式的加密字符串
                    const base64Cipher = encrypted.toString()

                    // 5. URL编码处理（关键步骤）
                    const urlSafeCipher = encodeURIComponent(base64Cipher)

                    return urlSafeCipher
                } catch (error) {
                    console.error('加密失败:', error)
                    throw new Error('数据加密处理失败')
                }
            },
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
            submitAudit() {
                this.form.agentCode = this.queryParams.agentCode;
                if (this.xgai) {
                    addNameAudit(this.form).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '已提交审核'
                        })
                    })
                } else {
                    updateNameAudit(this.form).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '已提交'
                        })
                    })
                }
                this.openName = false



            },
            submitphone() {
                updateAgentPhone(this.formphone.smsCode, this.formphone.phone).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    getAgentInfoVO().then((res) => {
                        this.queryParams2 = Object.assign({}, res.data); // 使用 Object.assign
                        console.log(this.queryParams2);
                    });
                    this.openphone = false;
                })
            },
            handleAvatarSuccess(res, file) {
                this.$set(this.form, 'cardIdUrlFront', res.message)
            },
            handleAvatarSuccess1(res, file) {
                this.$set(this.form, 'cardIdUrlBack', res.message)
            },
            submitFormUpdata() {
                updateCallbackUrl(this.queryParams.agentCode, this.queryParams.callbackUrl).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.open = false;
                    getAgentApiVO().then((res) => {
                        this.$set(this, 'queryParams', res.data); // 使用 $set 确保响应式
                        console.log(JSON.parse(JSON.stringify(this.queryParams))); // 打印原始数据
                        this.queryParams = this.queryParams
                    });

                })
            },
            clickName() {
                this.openName = true
                this.xgai = true;
            },
            clickName1() {
                selectNameAudit().then((res) => {
                    this.openName = true
                    this.form = res.data;
                    this.xgai = false;
                })


            },
            clickcallback() {
                this.open = true
            },
            clickPhone(data) {
                this.formphone = data
                this.openphone = true

            },

        },
    }
</script>
<style rel="stylesheet/scss" lang="scss">
    .aly .el-form-item__content {
        width: 80%;
    }

    .aly.el-input-medium {
        width: 80%;
    }

    .aly .el-form-item {
        width: 100%;
    }

    .topss {
        margin: 20px;
        padding: 10px;
        border: 1px solid #F2F2F2;
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

    .wxskm .el-icon-plus:before {
        line-height: 178px;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .el-input--suffix {
        width: 202px;
    }

    .title {
        margin: 0px auto 30px auto;
        text-align: center;
        color: #707070;
    }

    .el-login-footer {
        height: 40px;
        line-height: 40px;
        position: fixed;
        bottom: 0;
        width: 100%;
        text-align: center;
        color: #fff;
        font-family: Arial;
        font-size: 12px;
        letter-spacing: 1px;
    }

    .register-form {
        border-radius: 6px;
        background: #ffffff;
        width: 400px;
        padding: 25px 25px 5px 25px;

        .el-input {
            height: 38px;

            input {
                height: 38px;
            }
        }

        .input-icon {
            height: 39px;
            width: 14px;
            margin-left: 2px;
        }
    }

    .register-tip {
        font-size: 13px;
        text-align: center;
        color: #bfbfbf;
    }

    .register-code {
        width: 33%;
        height: 38px;
        float: right;

        img {
            cursor: pointer;
            vertical-align: middle;
        }
    }

    .el-register-footer {
        height: 40px;
        line-height: 40px;
        position: fixed;
        bottom: 0;
        width: 100%;
        text-align: center;
        color: #fff;
        font-family: Arial;
        font-size: 12px;
        letter-spacing: 1px;
    }

    .login-code {
        width: 25%;
        height: 38px;
        float: right;
        text-align: center;
        border-radius: 20px;
        border: 1px solid #dcdfe6;

        img {
            cursor: pointer;
            vertical-align: middle;
        }
    }

    .register-code-img {
        height: 38px;
    }
</style>