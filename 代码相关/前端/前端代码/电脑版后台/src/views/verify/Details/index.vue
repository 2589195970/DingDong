<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item prop="groupCode">
                <el-input v-model="queryParams.phone" placeholder="手机号" style="width: 200px" />
            </el-form-item>
            <el-form-item prop="groupCode">
                <el-select v-model="queryParams.type" placeholder="查询类型" clearable filterable collapse-tags
                    style="width: 200px;">
                    <el-option v-for="dict in operatorsType" :key="dict.label + dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="timestamp" type="daterange"
                    range-separator="至" start-placeholder="开始时间" end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery()">搜索</el-button>
                <el-button type="info" plain icon="el-icon-download" size="mini" @click="importTemplate"
                    v-hasPermi="['lnfluencerReport:dailyTalent:importTemplate']">导出数据</el-button>
                <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
                    v-hasPermi="['system:user:import']">导入</el-button>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="upload.open = true">单个查询</el-button>
            </el-form-item>
        </el-form>

        <el-table ref="tables" :data="list" row-key="jobDetailId" border height="550"
            :row-class-name="tableRowClassName" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }" lazy>
            <el-table-column label="时间" align="center" prop="createTime" width="200">
                <template slot-scope="scope">
                    <span>{{ formatTimestamp(scope.row.createTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true" />
            <el-table-column label="消息" align="center" prop="message" :show-overflow-tooltip="true" />
            <el-table-column label="查询类型" align="center" prop="requestType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span v-if="scope.row.requestType==0">炫咖移动余额查询</span>
                    <span v-if="scope.row.requestType==3">炫咖携号转网</span>
                    <span v-if="scope.row.requestType==4">炫咖号码查询</span>
                    <span v-if="scope.row.requestType==5">炫咖空号检测</span>
                    <span v-if="scope.row.requestType==6">额查查余额查询</span>
                    <span v-if="scope.row.requestType==7">额查查携号查询</span>
                </template>
            </el-table-column>
            <el-table-column label="号码余额" align="center" prop="mobileFee" :show-overflow-tooltip="true" />
            <el-table-column label="号码归属地" align="center" prop="area" :show-overflow-tooltip="true" />
            <el-table-column label="号码归属省" align="center" prop="province" :show-overflow-tooltip="true" />
            <el-table-column label="号码归属市" align="center" prop="city" :show-overflow-tooltip="true" />
            <el-table-column label="原运营商名称" align="center" prop="priIspName" :show-overflow-tooltip="true" />
            <el-table-column label="现运营商名称" align="center" prop="newIspName" :show-overflow-tooltip="true" />
            <el-table-column label="携号转网" align="center" prop="isChange" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span v-if="scope.row.isChange==0">否</span>
                    <span v-if="scope.row.isChange==1">是</span>
                </template>
            </el-table-column>
            <el-table-column label="运营商类型" align="center" prop="numberType" :show-overflow-tooltip="true" />
            <el-table-column label="状态名称" align="center" prop="statusName" :show-overflow-tooltip="true" />
            <el-table-column label="请求地址" align="center" prop="requestUrl" :show-overflow-tooltip="true" />
            <el-table-column label="请求参数" align="center" prop="requestBody" :show-overflow-tooltip="true" />
            <el-table-column label="请求返回" align="center" prop="requestMsg" :show-overflow-tooltip="true" />
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <!-- 用户导入对话框 -->
        <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
            <el-select v-model="dialogParams.type" placeholder="校验" clearable filterable collapse-tags
                style="width: 100%;margin-bottom: 10px;">
                <el-option v-for="dict in operatorsType" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
            <el-input v-model="dialogParams.phone" placeholder="手机号" minlength="11" maxlength="11"
                style="width: 100%;margin-bottom: 10px;" />
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="open" width="400px" append-to-body>
            <el-form>
                <el-select v-model="queryParams.type" placeholder="校验" clearable filterable collapse-tags
                    style="width: 100%;margin-bottom: 10px;">
                    <el-option v-for="dict in operatorsType" :key="dict.label + dict.value" :label="dict.label"
                        :value="dict.value" />
                </el-select>
                <span>仅允许导入xls、xlsx格式文件。</span>
                <!-- <a :href="fileUrl" download="导入数据模板.xlsx" style="color: blue;">下载模板</a> -->
                <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
                    :action="upload.url + '?type=' + queryParams.type" :disabled="upload.isUploading"
                    :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
                </el-upload>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm1">确 定</el-button>
                <el-button @click="open = false">取 消</el-button>
            </div>

        </el-dialog>
    </div>
</template>

<script>
    import { exportSettlement, selectNumberStatusLogListPage, getPhoneByType } from "@/api/monitor/verify";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Details",
        data() {
            return {
                checkJobIdIF: true,
                upload: {
                    // 是否显示弹出层（用户导入）
                    open: false,
                    // 弹出层标题（用户导入）
                    title: "",
                    // 是否禁用上传
                    isUploading: false,
                    // 是否更新已经存在的用户数据
                    updateSupport: 0,
                    // 设置上传的请求头部
                    headers: { Authorization: "Bearer " + getToken() },
                    // 上传的地址
                    url: process.env.VUE_APP_BASE_API + "/numberStatus/uploadNumberListExcel"
                },
                open: false,
                // 查询参数
                operatorsType: [
                    {
                        "label": "炫咖移动余额查询",
                        "value": 0,
                    },
                    {
                        "label": "炫咖携号转网",
                        "value": 3,
                    },
                    {
                        "label": "炫咖号码查询",
                        "value": 4,
                    },
                    {
                        "label": "炫咖空号检测",
                        "value": 5,
                    },
                    {
                        "label": "额查查余额查询",
                        "value": 6,
                    },
                    {
                        "label": "额查查携号查询 ",
                        "value": 7,
                    },


                ],
                // 遮罩层
                loading: false,
                // 备注
                des: '',
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
                list: [],
                // 导入文件
                // 用户导入参数
                // 是否显示弹出层
                open: false,
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: [],

                groupName: [],
                companyName: [],
                partnerId: "",
                dialogParams: {},
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,
                    title: undefined,
                    operatorsType: undefined,
                    groupName: undefined,
                    groupCode: undefined,
                    businessType: undefined,
                    status: undefined
                },
            };

        },
        created() {
            this.getList();
        },
        methods: {

            refreshdata() {
                this.queryParams.name = null;
                this.queryParams.mobile = null;
                this.queryParams.no = null;
            },
            refresh() {
                this.checkJobIdIF = false;
                this.getList();
            },
            tableRowClassName({ row, rowIndex }) {
                console.log(row);
                if (row.name) {
                    if (row.name.includes('已查询')) {
                        // row.hasChildren = true;
                        return 'warning-row';
                    }
                }
                return '';
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
            /** 搜索按钮操作 */
            handleQuery(data) {
                this.getList();
            },
            // 导入
            handleImport() {
                this.open = true;
            },
            getList() {
                this.loading = true;
                this.queryParams.starTime = undefined;
                this.queryParams.endTime = undefined;
                if (this.dateRange) {
                    if (this.dateRange.length > 0) {
                        this.queryParams.starTime = this.dateRange[0];
                        this.queryParams.endTime = this.dateRange[1];
                    };
                };
                selectNumberStatusLogListPage(this.queryParams).then((res) => {
                    this.list = [];
                    if (res.data.rows) {
                        this.list = res.data.rows

                    }
                    this.total = res.data.totalRows
                    this.loading = false;
                })
            },
            importTemplate() {
                exportSettlement(this.queryParams, `校验数据详情.csv`, '/numberStatus/exportNumberStatusLogList').then(res => {
                    console.log(res);
                })
            },
            // 文件上传中处理
            handleFileUploadProgress(event, file, fileList) {
                this.upload.isUploading = true;
            },
            // 提交上传文件
            submitFileForm1() {
                this.$refs.upload.submit();
            },
            // 文件上传成功处理
            handleFileSuccess(response, file, fileList) {
                this.upload.open = false;
                this.upload.isUploading = false;
                this.$refs.upload.clearFiles();
                this.$alert(
                    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                    response.msg +
                    "</div>",
                    "导入结果",
                    { dangerouslyUseHTMLString: true }
                );
                this.getList();
            },
            // 提交上传文件
            submitFileForm() {
                getPhoneByType(this.dialogParams).then((res) => {
                    if (res.code == 200) {
                        this.$modal.msgSuccess("查询成功");
                        this.upload.open = false;
                        this.getList();
                    }


                }).catch((error) => {

                });
            },
            //取消
            cancel() {
                this.open = false;
            },

        },
    }
</script>
<style>
    .el-table .warning-row {
        color: red;
    }
</style>