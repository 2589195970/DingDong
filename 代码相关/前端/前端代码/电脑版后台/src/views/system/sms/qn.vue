<template>
    <el-form :model="ruleForm" ref="ruleForm" label-width="150px" class="demo-ruleForm" :inline="true">

        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">七牛云短信服务
            </div>
            <div class="aly">
                <el-form-item label="访问key">
                    <el-input v-model="queryParams.accessKey" placeholder="访问key"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="秘钥key">
                    <el-input v-model="queryParams.secretKey" placeholder="秘钥key"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="空间">
                    <el-input v-model="queryParams.bucket" placeholder="空间"></el-input>
                </el-form-item>
                <br>
                <el-form-item label="域名url">
                    <el-input v-model="queryParams.domainNameUrl" placeholder="Access Key Secret"></el-input>
                </el-form-item>
                <br>
            </div>

        </div>
        <el-form-item
            style="position: relative; bottom:-22px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
            <el-button type="primary" @click="submitForm()">提交申请</el-button>
            <!-- <el-button @click="resetForm()">重置</el-button> -->
        </el-form-item>
    </el-form>
</template>



<script>
    import Stomp from 'stompjs'
    import { getToolConfig,updateToolConfig } from "@/api/monitor/business";
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址,
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

                },
            };
        },
        created() {
            // this.getList();
            this.hha();
        },

        methods: {
            hha() {
                getToolConfig(2).then((res)=>{
                    this.queryParams=res.data
                })

            },
            submitForm(){
                updateToolConfig(this.queryParams).then((res)=>{
                    this.hha();
                })
            }
        },
    }
</script>
<style>
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
</style>