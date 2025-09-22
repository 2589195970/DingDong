<template>
    <el-form :model="ruleForm" ref="ruleForm" label-width="150px" class="demo-ruleForm" :inline="true">
        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">提现设置</div>
            <el-form-item label="最低提现金额" prop="withdrawMinAmount">
                <el-input v-model="ruleForm.withdrawMinAmount"></el-input>
            </el-form-item>
            <el-form-item label="提现手续费率" prop="withdrawRate">
                <el-input v-model="ruleForm.withdrawRate"></el-input>
            </el-form-item>
        </div>
        <el-form-item
            style=" padding: 10px 0;height: 50px;text-align: center;width: 100%;">
            <el-button type="primary" @click="submitForm(ruleForm)">立即创建</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
        </el-form-item>
    </el-form>
</template>
<script>
    import { updateWithdrawalConfig, selectWithdrawalConfig } from "@/api/monitor/finance";

    export default {
        data() {
            return {

                ruleForm: {

                },

            };
        },
        created() {
            this.getList();
        },
        methods: {
            getList() {
                selectWithdrawalConfig().then((res) => {
                    this.ruleForm = res.data
                    console.log(this.ruleForm);
                    
                })
            },
            submitForm(formName) {
                updateWithdrawalConfig(formName).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '创建成功!'
                    });
                    this.getList();
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '创建失败'
                    });
                });
            },
            resetForm(formName) {
                this.$refs[formName].resetFields();
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