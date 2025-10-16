<template>
    <el-form :model="ruleForm" ref="ruleForm" label-width="150px" class="demo-ruleForm" :inline="true">
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">基础信息</div>
            <el-form-item label="产品名称" prop="name">
                <el-input v-model="ruleForm.productName"></el-input>
            </el-form-item>
            <el-form-item label="产品卖点" prop="name">
                <el-input v-model="ruleForm.productCharacteristics"></el-input>
            </el-form-item>

            <el-form-item label="运营商" prop="region">
                <el-select v-model="ruleForm.operatorType" placeholder="请选择运营商">
                    <el-option label="中国移动" value="0"></el-option>
                    <el-option label="中国移动" value="0"></el-option>
                    <el-option label="中国电信" value="1"></el-option>
                    <el-option label="中国联通" value="2"></el-option>
                    <el-option label="中国广电" value="3"></el-option>
                </el-select>
            </el-form-item>
            <br>
            <el-form-item label="结算模式" prop="region">
                <el-select v-model="ruleForm.productType" placeholder="请选择结算模式">
                    <el-option label="日结秒返" value="0"></el-option>
                    <el-option label="月结产品" value="1"></el-option>
                    <!--<el-option label="长期产品" value="2"></el-option>-->
                    <!--<el-option label="其它" value="3"></el-option>-->
                    <!--<el-option label="组合返佣" value="4"></el-option>-->
                </el-select>
            </el-form-item>
            <br>
            <el-form-item label="开放全部代理商" prop="resource">
                <el-radio-group v-model="ruleForm.isAllAgent">
                    <el-radio label="0">否</el-radio>
                    <el-radio label="1">是</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="指定代理" prop="region" v-if="ruleForm.isAllAgent=='0'">
                <el-select v-model="ruleForm.agentCodeList" multiple placeholder="请选择活动区域" style="width: 200px;">
                    <el-option v-for="dict in searchType" :key="dict.agentCode" :label="dict.agentName"
                        :value="dict.agentCode" />
                </el-select>
            </el-form-item>
            <br>
            <el-form-item label="通用流量" prop="delivery">
                <!-- <el-input-number v-model="" controls-position="right" :min="1"></el-input-number> -->
                <el-input placeholder="请输入内容" v-model="ruleForm.productTyll" :min="1">
                    <template slot="append">GB</template>
                </el-input>
            </el-form-item>
            <el-form-item label="定向流量" prop="delivery">
                <!-- <el-input-number v-model="ruleForm.productDxll" controls-position="right":min="1" :min="1"></el-input-number> -->
                <el-input placeholder="请输入内容" v-model="ruleForm.productDxll" :min="1">
                    <template slot="append">GB</template>
                </el-input>
            </el-form-item>
            <el-form-item label="通话分钟" prop="name">
                <el-input placeholder="请输入内容" v-model="ruleForm.productThfz" :min="1">
                    <template slot="append">分钟</template>
                </el-input>
                <!-- <el-input v-model="ruleForm.productThfz"></el-input> -->

            </el-form-item>
            <br>
            <!--<el-form-item label="优惠月租" prop="delivery">-->
            <!--    <el-input placeholder="请输入内容" v-model="ruleForm.productYhyz" :min="1">-->
            <!--        <template slot="append">元</template>-->
            <!--    </el-input>-->
            <!--    &lt;!&ndash; <el-input-number v-model="ruleForm.productYhyz" controls-position="right" :min="1"></el-input-number> &ndash;&gt;-->
            <!--</el-form-item>-->
            <!--<el-form-item label="原始月租" prop="delivery">-->
            <!--    <el-input placeholder="请输入内容" v-model="ruleForm.productYsyz" :min="1">-->
            <!--        <template slot="append">元</template>-->
            <!--    </el-input>-->
            <!--    &lt;!&ndash; <el-input-number v-model="ruleForm.productYsyz" controls-position="right" :min="1"></el-input-number> &ndash;&gt;-->
            <!--</el-form-item>-->
            <br>
            <el-form-item label="首充说明" prop="name">
                <el-input v-model="ruleForm.productScsm"></el-input>
            </el-form-item>
            <el-form-item label="归属地区" prop="name">
                <el-input v-model="ruleForm.productGsdq"></el-input>
            </el-form-item>
            <el-form-item label="发货方式" prop="name">
                <el-input v-model="ruleForm.productFafs"></el-input>
            </el-form-item>
            <br>
            <!--<el-form-item label="套餐介绍" prop="name">-->
            <!--    <el-input v-model="ruleForm.productTcjs"></el-input>-->
            <!--</el-form-item>-->
            <el-form-item label="推广要求" prop="name">
                <el-input v-model="ruleForm.productDemand"></el-input>
            </el-form-item>
            <el-form-item label="余额配置" prop="name">
                <el-input v-model="ruleForm.balanceConfig"></el-input>
            </el-form-item>
        </div>
        <!-- 接口对接 -->
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">接口对接</div>
            <el-form-item label="选择接口" prop="region">
                <el-select v-model="ruleForm.upstreamApiId" placeholder="请选择接口" @change="canq(ruleForm.upstreamApiId)">
                    <el-option v-for="dict in upstreamApiCode" :key="dict.upstreamApiId" :label="dict.upstreamApiName"
                        :value="dict.upstreamApiId" />
                </el-select>
            </el-form-item>
            <el-form-item label="选择产品" prop="region">
                <el-select v-model="ruleForm.upstreamProductId" placeholder="请选择产品" filterable>
                    <el-option v-for="dict in upstreamProductCode" :key="dict.upstreamProductId"
                        :label="dict.upstreamProductName" :value="dict.upstreamProductId" />
                </el-select>
            </el-form-item>
        </div>

        <!-- 限制条件 -->
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">限制条件</div>

            <el-form-item label="年龄限制（周岁）">
                <el-col :span="11">
                    <el-input v-model="ruleForm.productAgeMin"></el-input>
                </el-col>
                <el-col :span="2" style="text-align: center;">—</el-col>
                <el-col :span="11">
                    <el-input v-model="ruleForm.productAgeMax"></el-input>
                </el-col>
            </el-form-item>

        </div>
        <!-- 套餐详情 -->
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">套餐详情</div>
            <el-form-item label="模板背景颜色" prop="name">
                <el-input v-model="bgThemeColor"></el-input>
            </el-form-item>
            <br>
            <el-form-item label="产品主图" prop="resource">
                <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                    :on-success="handleAvatarSuccess" :headers=headers>
                    <img v-if="ruleForm.productMasterMap" :src="ruleForm.productMasterMap" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <el-form-item label="产品详情图" prop="resource">
                <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                    :on-success="handleAvatarSuccess1" :headers=headers>
                    <img v-if="ruleForm.productDetailMap" :src="ruleForm.productDetailMap" class="avatar">
                    <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                </el-upload>
            </el-form-item>
            <br>
            <el-form-item label="产品详情">
                <editor v-model="ruleForm.productRemark" :min-height="192" style="width: 100%; " />
            </el-form-item>
        </div>

        <!-- 照片配置 -->
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">照片配置</div>

            <photo-config
                v-model="photoConfigData"
                @change="handlePhotoConfigChange">
            </photo-config>
        </div>
        <el-form-item
            style="position: fixed; bottom:-22px; background-color: #F2F2F2;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
            <el-button type="primary" @click="submitForm('ruleForm')" :loading="submitLoading">立即创建</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>

<script>
    import { addProduct, selectUpstreamApiListPage, selectUpstreamProductListPage, selectChildAgentList } from "@/api/monitor/business";
    import { getToken } from "@/utils/auth";
    import PhotoConfig from "@/components/PhotoConfig/index.vue";

    export default {
        components: {
            PhotoConfig
        },
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址
                // 设置上传的请求头部
                headers: { Authorization: "Bearer " + getToken() },
                imageUrl: false,
                bgThemeColor: '',
                imageUrl1: false,
                upstreamApiCode: [],
                upstreamProductCode: [],
                submitLoading: false, // 提交loading状态
                ruleForm: {
                    isAllAgent: '1',
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    upstreamProductCode: '',
                    upstreamApiCode: '',
                    resource: '1',
                    desc: '',
                    photoRequired: 0, // 默认不需要上传照片
                    photoConfig: null, // 照片配置
                    // 接口相关字段
                    upstreamApiId: '',
                    upstreamApiName: '',
                    upstreamProductId: '',
                    upstreamProductName: '',
                    // 基础产品字段
                    productName: '',
                    productCharacteristics: '',
                    operatorType: '',
                    productType: '',
                    agentCodeList: [],
                    productTyll: '',
                    productDxll: '',
                    productThfz: '',
                    productYhyz: '',
                    productYsyz: '',
                    productScsm: '',
                    productGsdq: '',
                    productFafs: '',
                    productTcjs: '',
                    productDemand: '',
                    balanceConfig: '',
                    productMasterMap: '',
                    productDetailMap: '',
                    productRemark: '',
                    productTemplateJson: '',
                    productAgeMin: '',
                    productAgeMax: ''
                },
                // 照片配置数据
                photoConfigData: {
                    photoRequired: 0,
                    photoConfig: null
                },
            };
        },
        created() {
            selectUpstreamApiListPage({}).then((res) => {
                console.log(res.data);
                this.upstreamApiCode = res.data.rows
            })
            selectChildAgentList({}).then((res) => {
                console.log(res.data);
                this.searchType = res.data
            })

        },
        methods: {
            canq(data) {
                selectUpstreamProductListPage({ upstreamApiId: data,
                    pageNo:1,
                    pageSize:9999
                 }).then((res) => {
                    this.upstreamProductCode = res.data.rows
                })
            },
            handleAvatarSuccess(res) {
                this.$set(this.ruleForm, 'productMasterMap', res.message)

            },
            handleAvatarSuccess1(res) {
                this.$set(this.ruleForm, 'productDetailMap', res.message)
            },
            // 处理照片配置变更
            handlePhotoConfigChange(newVal) {
                // 同步到ruleForm
                this.ruleForm.photoRequired = newVal.photoRequired;
                this.ruleForm.photoConfig = newVal.photoConfig;
            },
            submitForm(formName) {

                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // 开始提交，显示loading
                        this.submitLoading = true;

                        this.ruleForm.productTemplateJson = JSON.stringify({
                            bgThemeColor: this.bgThemeColor
                        })

                        if (this.ruleForm.upstreamApiId) {
                            const foundObject = this.upstreamApiCode.find(obj => obj.upstreamApiId === this.ruleForm.upstreamApiId);
                            const foundObject1 = this.upstreamProductCode.find(obj => obj.upstreamProductId === this.ruleForm.upstreamProductId);

                            // 检查是否找到了对应的接口和产品
                            if (!foundObject) {
                                this.submitLoading = false;
                                this.$message.error("请选择有效的接口");
                                return;
                            }
                            if (!foundObject1) {
                                this.submitLoading = false;
                                this.$message.error("请选择有效的产品");
                                return;
                            }

                            this.ruleForm.upstreamProductName = foundObject1.upstreamProductName;
                            this.ruleForm.upstreamProductId = foundObject1.upstreamProductId;
                            this.ruleForm.upstreamProductCode = foundObject1.upstreamProductCode;
                            this.ruleForm.upstreamApiName = foundObject.upstreamApiName;
                            this.ruleForm.upstreamApiId = foundObject.upstreamApiId;
                            this.ruleForm.upstreamApiCode = foundObject.upstreamApiCode;

                        }

                        if (!this.ruleForm.operatorType) {
                            this.submitLoading = false;
                            this.$message.error("请选择运营商");
                        } else if (!this.ruleForm.productType) {
                            this.submitLoading = false;
                            this.$message.error("请选择结算模式");
                        } else {
                            addProduct(this.ruleForm).then(() => {
                                this.submitLoading = false;
                                this.$modal.msgSuccess("新增成功");
                                this.$router.push('/business/products/products')
                            }).catch((error) => {
                                this.submitLoading = false;
                                console.error('添加产品失败:', error);
                            })
                        }

                    } else {
                        console.log('error submit!!');
                        return false;
                    }
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
                // 重置照片配置数据
                this.photoConfigData = {
                    photoRequired: 0,
                    photoConfig: null
                };
                this.ruleForm.photoRequired = 0;
                this.ruleForm.photoConfig = null;
            }
        }
    }
</script>
<style>
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

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .el-input--suffix {
        width: 202px;
    }
</style>
