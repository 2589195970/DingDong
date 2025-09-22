<template>
    <el-form :model="queryParams" ref="ruleForm" label-width="150px" class="demo-ruleForm" :inline="true">

        <div class="topss">
            <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">海报图
            </div>
            <div class="aly">
                <el-form-item  prop="resource" style="width: 200px;height: 200px; text-align: center;">
                    <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                        :on-success="handleAvatarSuccess" :before-upload="handlesuccess" :headers=headers>
                        <img v-if="queryParams.accessKey" :src="queryParams.accessKey" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <span>海报图1</span>
                </el-form-item>
                <el-form-item  prop="resource" style="width: 200px;height: 200px;text-align: center;">
                    <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                        :on-success="handleAvatarSuccess1" :before-upload="handlesuccess" :headers=headers>
                        <img v-if="queryParams.secretKey" :src="queryParams.secretKey" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <span>海报图2</span>
                </el-form-item>
                <el-form-item  prop="resource" style="width: 200px;height: 200px;text-align: center;">
                    <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                        :on-success="handleAvatarSuccess2" :before-upload="handlesuccess" :headers=headers>
                        <img v-if="queryParams.bucket" :src="queryParams.bucket" class="avatar">
                        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                    </el-upload>
                    <span>海报图3</span>
                </el-form-item>
            </div>

        </div>
        
        </div>
        <el-form-item
            style="position: relative; bottom:-22px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
            <el-button type="primary" @click="submitForm()">提交配置</el-button>
            <!-- <el-button @click="resetForm()">重置</el-button> -->
        </el-form-item>
    </el-form>
</template>



<script>
    import Stomp from 'stompjs'
    import { getToolConfig,updateRegisterQrcodeMap } from "@/api/monitor/business";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Products",
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
                    toolConfigType:'4'
                },
            };
        },
        created() {
            // this.getList();
            this.hha();
        },

        methods: {
            hha() {
                getToolConfig(4).then((res)=>{
                    this.queryParams=res.data
                })

            },
            handleAvatarSuccess(res, file) {
                this.queryParams.accessKey = res.message;
            },
            handleAvatarSuccess1(res, file) {
                this.queryParams.secretKey = res.message;
            },
            handleAvatarSuccess2(res, file) {
                this.queryParams.bucket = res.message;
            },
            // 图片返回
            handlesuccess(file) {

            },
            submitForm(){
                updateRegisterQrcodeMap(this.queryParams).then((res)=>{
                    this.$message({
                        type: 'success',
                        message: '上传成功!'
                    });
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