<template>
    <el-form :model="queryParams" label-width="150px" class="demo-ruleForm" :inline="true">
        <div class="topss">
            <p class="pz">账户余额:{{pz.balance * 0.01}}元</p>
            <p class="pz">增值税发票税费:{{pz.withdrawRate}}%</p>
            <p class="pz">最低提现金额:{{pz.withdrawMinAmount}}元</p>
        </div>
        <div class="topss">
            <el-form-item label="提现模式" prop="resource">
                <el-radio-group v-model="queryParams.withdrawalType">
                    <el-radio label="0">微信</el-radio>
                    <el-radio label="1">支付宝</el-radio>
                    <el-radio label="2">银行卡</el-radio>
                </el-radio-group>
            </el-form-item>
            <br>
            <div v-if="queryParams.withdrawalType=='0'" class="wxskm">
                <el-form-item label="微信收款码" prop="resource">
                    <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :before-upload="handlesuccess" :headers=headers>
                        <img v-if="queryParams.wxUrl" :src="queryParams.wxUrl" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                </el-form-item>

            </div>
            <div v-if="queryParams.withdrawalType=='1'">
                <el-form-item label="支付宝账号">
                    <el-input v-model="queryParams.zfbAccount" placeholder="支付宝账号"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="真实姓名">
                    <el-input v-model="queryParams.zfbAccountName" placeholder="真实姓名"></el-input>
                </el-form-item>
            </div>
            <div v-if="queryParams.withdrawalType=='2'">
                <el-form-item label="银行开户行">
                    <el-input v-model="queryParams.bankName" placeholder="银行开户行" style="width: 100%;"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="真实姓名">
                    <el-input v-model="queryParams.bankNumberName" placeholder="真实姓名"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="银行卡号">
                    <el-input v-model="queryParams.bankNumber" placeholder="银行卡号"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="手机号">
                    <el-input v-model="queryParams.bankNumberPhone" placeholder="手机号"></el-input>
                </el-form-item>
            </div>
            <el-form-item label="提现金额">
                <el-input v-model="queryParams.withdrawalAmount" placeholder="提现金额" @input="hlv"></el-input>
                <p v-if="querwithd.withdrawalRate">提现费率:{{querwithd.withdrawalRate}}%</p>
                <p v-if="querwithd.withdrawalRateAmount">提现手续费:{{querwithd.withdrawalRateAmount * 0.01}}元</p>
                <p v-if="querwithd.receivedAmount">实际到账:{{querwithd.receivedAmount * 0.01}}元</p>
            </el-form-item>
            <br>
            <el-form-item
                style="position: relative; bottom:-22px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
                <el-button type="primary" @click="submitForm(queryParams)">提交申请</el-button>
                <!-- <el-button @click="resetForm()">重置</el-button> -->
            </el-form-item>

        </div>
    </el-form>
</template>
<script>
    import {
        // 申请提现
        addAgentWithdrawalApplication,
        // 计算汇率
        computeRate,
        // 查询提现配置
        selectWithdrawalConfig,
        // 查询余额
        selectWithdrawalRecord
    } from "@/api/monitor/finance";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Payouts",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址,
                headers: { Authorization: "Bearer " + getToken() },
                // 表格数据
                list: [],
                ruleForm: [],
                imageUrl: false,
                // 导入文件
                form: {

                },


                api: [],
                groupCode: [],
                queryParams: {
                    withdrawalType: '0',

                },
                querwithd: {},
                pz: {}
            };
        },
        async created() {
            try {
                // 获取配置数据
                const configRes = await selectWithdrawalConfig({});
                this.pz = configRes.data;

                // 获取提现记录并更新余额
                const recordRes = await selectWithdrawalRecord();
                this.$set(this.pz, 'balance', recordRes.data.balance);
            } catch (error) {
                console.error('Error occurred while fetching data:', error);
                // 在这里可以显示用户友好的错误提示
            }
        },

        // beforeCreate() {
        //     // this.getList();
        //     selectWithdrawalConfig({}).then((res) => {
        //         this.pz = res.data;
        //     })
        //     selectWithdrawalRecord().then((res) => {
        //         this.$set(this.pz, 'balance', res.data.balance);
        //     })
        // },
        computed: {
        },
        methods: {
            hlv() {
                if (this.queryParams.withdrawalAmount) {
                    computeRate(this.queryParams.withdrawalAmount).then((res) => {
                        this.querwithd = res.data;
                    })
                }

            },
            submitForm(formName) {
                addAgentWithdrawalApplication(formName).then((res) => {
                    this.$modal.msgSuccess("已提交申请");
                })
            },
            handleSelectionChange(val) {

            },
            handleAvatarSuccess(res, file) {
                this.$set(this.queryParams, 'wxUrl', res.message)
            },
            // 图片返回
            handlesuccess(file) {
                // this.queryParams.productPlacardMap = file.msg;
            },
        },
    }
</script>
<style>
    .pz {
        color: red;
        font-weight: 700;
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
</style>