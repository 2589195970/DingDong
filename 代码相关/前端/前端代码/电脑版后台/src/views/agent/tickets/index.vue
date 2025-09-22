<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.workOrderType" placeholder="工单类型" clearable filterable
                    style="width: 240px">
                    <el-option label="流量卡" value="0"></el-option>
                    <el-option label="技术对接" value="1"></el-option>
                    <el-option label="其他" value="10"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.workOrderStatus" placeholder="工单状态" clearable filterable
                    style="width: 240px">
                    <el-option label="待管理回复" value="0"></el-option>
                    <el-option label="待代理商回复" value="1"></el-option>
                    <el-option label="已完结" value="2"></el-option>

                </el-select>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.sysUserId" placeholder="工单ID"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.agentName" placeholder="请输入代理商名称"></el-input>
            </el-form-item>

            <el-form-item>
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="timestamp" type="daterange"
                    range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['channel:channelManagement:add']">新增</el-button>
            </el-col>
        </el-row>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550">
            <el-table-column label="ID" align="center" prop="sysUserId" />
            <el-table-column label="代理商名称" align="center" prop="agentName" />

            <el-table-column label="工单类型" align="center" prop="workOrderType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.workOrderType==0">流量卡</p>
                    <p v-if="scope.row.workOrderType==1">技术对接</p>
                    <p v-if="scope.row.workOrderType==10">其他</p>
                </template>
            </el-table-column>

            <el-table-column label="工单标题" align="center" prop="workOrderTitle" width="110"
                :show-overflow-tooltip="true" />
            <el-table-column label="工单内容" align="center" prop="workOrderContent" :show-overflow-tooltip="true" />
            <el-table-column label="工单状态" align="center" prop="workOrderStatus" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.workOrderStatus==0">待管理回复</p>
                    <p v-if="scope.row.workOrderStatus==1">待代理商回复</p>
                    <p v-if="scope.row.workOrderStatus==2">已完结</p>
                </template>
            </el-table-column>
            <el-table-column label="工单附件图片" align="center" prop="workOrderUrl">
                <template slot-scope="scope">
                    <img :src="scope.row.workOrderUrl" alt="" style="width: 100px;">
                </template>
            </el-table-column>
            <el-table-column label="时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.createTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">回复</el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="cambiare" width="550px" append-to-body :close-on-click-modal="false">
            <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="businessType" label="工单类型">
                            <el-select v-model="cambiareform.workOrderType" placeholder="工单类型" clearable filterable
                                style="width: 240px">
                                <el-option label="流量卡" value="0"></el-option>
                                <el-option label="技术对接" value="1"></el-option>
                                <el-option label="其他" value="10"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="工单标题">
                            <el-input v-model="cambiareform.workOrderTitle" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="工单内容">
                            <el-input type="textarea" v-model="cambiareform.workOrderContent"
                                style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="工单附件图片" prop="resource" style="width: 200px;height: 200px;">
                            <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                                :on-success="handleAvatarSuccess" :before-upload="handlesuccess" :headers=headers>
                                <img v-if="cambiareform.workOrderUrl" :src="cambiareform.workOrderUrl" class="avatar">
                                <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                            </el-upload>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormUpdata">提交</el-button>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="juopen" width="350px" append-to-body fullscreen="true">
            <el-form ref="form" v-model="formJu" label-width="100px">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="addWorkClick"
                    v-hasPermi="['channel:channelManagement:add']">添加回复</el-button>
                <el-table ref="formJu" v-loading="loading" :data="formJu.WorkOrderRecover" row-key="operatorReportId"
                    border lazy height="550">
                    <el-table-column label="工单内容" align="center" prop="recoverContent" :show-overflow-tooltip="true" />
                    <el-table-column label="工单附件图片" align="center" prop="recoverUrl">
                        <template slot-scope="scope">
                            <img :src="scope.row.recoverUrl" alt="" style="width: 100px;">
                        </template>
                    </el-table-column>
                    <el-table-column label="时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                        <template slot-scope="scope">
                            <p>{{formatTimestamp(scope.row.createTime)}}</p>
                        </template>
                    </el-table-column>
                </el-table>

            </el-form>
        </el-dialog>
    </div>
</template>

<script>
    import {
        // 新增工单记录
        addWorkOrder,
        //新增工单回复
        addWorkOrderRecover,
        //工单列表查询
        selectWorkOrderListPage,
        // 删除工单
        deleteWorkOrderRecover
    } from "@/api/monitor/gdan";
    import { getToken } from "@/utils/auth";

    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {

                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址
                // 设置上传的请求头部
                headers: { Authorization: "Bearer " + getToken() },
                // 遮罩层
                loading: false,
                imageUrl: '',
                // 选中数组
                ids: [],
                // 非多个禁用
                multiple: true,
                // 显示搜索条件
                showSearch: true,
                // 总条数
                total: 100,
                // 弹出层标题
                title: "",
                // 表格数据
                cambiareform: {},
                list: [],
                // 导入文件
                cambiare: false,
                // 是否显示弹出层
                open: false,
                juopen: false,
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: {

                },

                formJu: [],
                api: [],
                groupCode: [],
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,

                },
            };
        },
        created() {
            this.getList();
        },
        methods: {
            addWorkClick() {

                addWorkOrderRecover().then((res) => {

                })
            },
            // 时间戳转换
            formatTimestamp(timestamp) {
                const date = new Date(timestamp);
                const year = date.getFullYear();
                const month = ("0" + (date.getMonth() + 1)).slice(-2);
                const day = ("0" + date.getDate()).slice(-2);
                const hours = ("0" + date.getHours()).slice(-2);
                const minutes = ("0" + date.getMinutes()).slice(-2);
                const seconds = ("0" + date.getSeconds()).slice(-2);
                return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
            },
            handleAvatarSuccess(res, file) {
                this.$set(this.cambiareform, 'workOrderUrl', res.message)
                // this.cambiareform.workOrderUrl = res.message;
                // this.imageUrl = URL.createObjectURL(file.raw);
            },
            handleClick(data) {
                this.juopen = true;
                console.log(data);
                this.formJu = data
            },
            // 图片返回
            handlesuccess(file) {
            },
            changeTime(time) {
                return time.replace(/(\d{4})(\d{2})(\d{2})/, '$1/$2/$3');
            },
            submitFormUpdata() {
                addWorkOrder(this.cambiareform).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '添加成功!'
                    });
                    this.cambiare = false;
                    this.getList();
                })
            },
            // 添加
            handleAdd() {
                this.cambiare = true;
            },
            // 删除
            handleDelete(data) {
                this.$confirm('确认要删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteWorkOrderRecover(data.workOrderId).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.getList();
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            /** 搜索按钮操作 */
            handleQuery() {
                this.getList();
            },

            getList() {
                selectWorkOrderListPage(this.queryParams).then((res) => {
                    if (res.data.rows) {
                        this.list = res.data.rows
                    } else {
                        this.list = []
                    }
                    this.total = res.data.totalRows
                })
            },

        },
    }
</script>

<style>
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

    .avatar {
        width: 100%;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .el-table .warning-row {
        color: red;
    }

    .el-table .success-row {
        color: #E6A23C;
    }

    .el-table .success-row1 {
        color: #67C23A;
    }

    .el-table .success-row2 {
        color: #409EFF;
    }
</style>