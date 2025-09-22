<template>
    <el-form :model="queryParams" label-width="150px" class="demo-ruleForm" :inline="true">
        <div class="topss">
            <el-form-item label="佣金模式" prop="resource">
                <el-radio-group v-model="queryParams.commissionConfigType">
                    <el-radio :label="0">固定佣金</el-radio>
                    <el-radio :label="1">百分比佣金</el-radio>
                </el-radio-group>
            </el-form-item>
            <br>
            <div v-if="queryParams.commissionConfigType==0" class="wxskm">
                <el-form-item label="固定佣金">
                    <el-input v-model="queryParams.fixedCommission" placeholder="固定佣金"></el-input>
                </el-form-item>
            </div>
            <div v-if="queryParams.commissionConfigType==1">
                <el-form-item label="百分比佣金">
                    <el-input v-model="queryParams.scaleCommission" placeholder="百分比佣金"></el-input>
                </el-form-item>
                <br>


            </div>
            <el-form-item
                style="position: relative; bottom:-22px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
                <el-button type="primary" @click="submitForm(queryParams)">提交设置</el-button>
                <!-- <el-button @click="resetForm()">重置</el-button> -->
            </el-form-item>
        </div>
    </el-form>
</template>
<script>
    import {

        selectAgentCommissionConfig,

        agentUpdateCommissionConfig,
    } from "@/api/monitor/finance";
    export default {
        name: "Payouts",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {

                // 表格数据
                list: [],
                ruleForm: [],
                queryParams: {
                    commissionConfigType:'1',
                },
            };
        },
        async created() {
            try {
                // 获取配置数据
                const configRes = await selectAgentCommissionConfig({});
                if (configRes.data) {
                    this.queryParams = configRes.data;
                }

            } catch (error) {
                console.error('Error occurred while fetching data:', error);
                // 在这里可以显示用户友好的错误提示
            }
        },
        computed: {
        },
        methods: {

            submitForm(formName) {
                agentUpdateCommissionConfig(formName).then((res) => {
                    this.$modal.msgSuccess("设置成功");
                })
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