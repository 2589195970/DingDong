<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.orderId" placeholder="订单ID"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardName" placeholder="姓名"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardPhone" placeholder="手机号"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardId" placeholder="身份证"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
                    v-hasPermi="['system:user:import']">导入</el-button>
                <el-button icon="el-icon-download" size="mini" @click="ExportClick">导出</el-button>
            </el-form-item>
        </el-form>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy height="550"
            :row-class-name="tableRowClassName" id="dingp">
            <el-table-column label="ID" align="center" prop="orderCommissionId" />
            <el-table-column label="代理商" align="center" prop="showDownstreamName" :show-overflow-tooltip="true" />
            <el-table-column label="关联信息" align="label" prop="phone" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>用户姓名：{{scope.row.cardName}}</p>
                    <p>手机号：{{scope.row.cardPhone}}</p>
                    <p>身份证：{{scope.row.cardId}}</p>
                    <p>订单ID：{{scope.row.orderId}}</p>
                    <p>订单状态：
                        <span v-if="scope.row.orderStatus==-1">失败</span>
                        <span v-if="scope.row.orderStatus==0">申请成功</span>
                        <span v-if="scope.row.orderStatus==1">申请中</span>
                        <span v-if="scope.row.orderStatus==2">发货</span>
                        <span v-if="scope.row.orderStatus==4">激活</span>
                    </p>
                    <p>是否充值:
                        <span v-if="scope.row.isRecharged==0">未充值</span>
                        <span v-if="scope.row.isRecharged==1">已充值</span>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="佣金" align="center" prop="orderCommissionStatus" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{scope.row.productCommission}}</p>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="isEnabled" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.orderCommissionStatus==0" style="color: red;">未到结算状态</p>
                    <p v-if="scope.row.orderCommissionStatus==1" style="color: red;">待结算</p>
                    <p v-if="scope.row.orderCommissionStatus==2" style="color: green;">部分结算</p>
                    <p v-if="scope.row.orderCommissionStatus==3" style="color: green;">已结算</p>
                    <p v-if="scope.row.orderCommissionStatus==4" style="color: red;">无法结算</p>
                </template>
            </el-table-column>
            <el-table-column label="生成时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.createTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="结算时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.commissionTime">{{formatTimestamp(scope.row.commissionTime)}}</p>
                </template>
            </el-table-column>

            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="updataClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">修改结算状态</el-button>
                    <el-button @click="settlementClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">佣金详情</el-button>
                    <el-button @click="deletClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">删除</el-button>
                </template>
            </el-table-column>

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="cambiare" width="550px" append-to-body :show-close="false">
            <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
                <el-form-item prop="businessType">
                    <el-select v-model="cambiareform.orderCommissionStatus" placeholder="订单状态" clearable filterable
                        style="width: 240px">
                        <el-option v-for="dict in orderCommissionStatus1" :key="dict.id" :label="dict.name"
                            :value="dict.id" />
                    </el-select>
                </el-form-item>
                <el-form-item prop="responsiblePeople">
                    <el-input v-model="cambiareform.orderCommissionMessage" placeholder="佣金说明" style="width: 240px"></el-input>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormUpdata">提交</el-button>
                <el-button @click="cambiarefalse">取消</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="settlementOpen" width="550px" append-to-body :fullscreen="true">

            <el-table ref="settlementform" :data="settlementform" row-key="oper" border lazy height="550">

                <el-table-column label="代理商" align="center" prop="showDownstreamName" :show-overflow-tooltip="true" />
                <el-table-column label="产品佣金" align="center" prop="productCommission" :show-overflow-tooltip="true" />
                <el-table-column label="下游分销佣金" align="center" prop="distributionProductCommission"
                    :show-overflow-tooltip="true" />
                <el-table-column label="收入佣金" align="center" prop="revenueProductCommission"
                    :show-overflow-tooltip="true" />

            </el-table>

        </el-dialog>
        <el-dialog :visible.sync="upload.open" width="400px" append-to-body>
            <el-form>
                <span>仅允许导入xls、xlsx格式文件。</span>
                <!-- <a :href="fileUrl" download="导入数据模板.xlsx" style="color: blue;">下载模板</a> -->
                <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
                    :disabled="upload.isUploading" :on-progress="handleFileUploadProgress"
                    :on-success="handleFileSuccess" :auto-upload="false" drag>
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
    import { exportSettlement, } from "@/api/monitor/business";
    import {
        selectOrderCommissionListPage,
        selectOrderCommissionDetailsList,
        updateOrderCommissionStatus,
        deleteOrderCommission,
    } from "@/api/monitor/daili";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
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
                    url: process.env.VUE_APP_BASE_API + "/orderCommission/uploadOrderCommissionExcel"
                },
                // 遮罩层
                loading: false,
                operatorsType: [],
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
                cambiare: false,
                // 表格数据
                list: [],
                // 导入文件

                // 是否显示弹出层
                open: false,
                settlementform: [],
                settlementOpen: false,
                cambiareform: [],
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: {

                },


                api: [],
                groupCode: [],
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,
                    // operatorType: 0,
                    // productCode: undefined,
                    // productGsdq: undefined,
                    // productName: undefined,
                    // productStatus: 0,
                    // productType: 0


                },
                orderCommissionStatus: [
                    {
                        name: "未到结算状态",
                        id: 0
                    },
                    {
                        name: "待结算",
                        id: 1
                    },
                    {
                        name: "已结算",
                        id: 3
                    },
                    {
                        name: "无法结算",
                        id: 4
                    },
                ],
                orderCommissionStatus1: [
                    {
                        name: "待结算",
                        id: 1
                    },
                    {
                        name: "已结算",
                        id: 3
                    },
                    {
                        name: "不予结算",
                        id: 4
                    },
                ],
                orderStatus: [
                    {
                        name: "订单失败",
                        id: -1
                    },
                    {
                        name: "订单预创建",
                        id: 0
                    },
                    {
                        name: "订单申请成功",
                        id: 1
                    },
                    {
                        name: '订单已发货',
                        id: 2
                    },
                    {
                        name: "订单已签收",
                        id: 3
                    },
                    {
                        name: "订单已激活",
                        id: 4
                    },
                ],
            };
        },
        created() {
            this.getList();
        },
        methods: {
            // 导入
            handleImport() {
                this.upload.open = true;
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
            AgentStatusqiClick(data) {
                data.isEnabled = 0;
                updateAgentStatus(data).then((res) => {

                    this.$message({
                        type: 'success',
                        message: '启用成功!'
                    });
                    this.getList();
                })
            },
            settlementClick(data) {
                this.settlementOpen = true
                selectOrderCommissionDetailsList(data.orderCommissionId).then((res) => {
                    if (res.data) {
                        this.settlementform = res.data
                    } else {
                        this.settlementform = []
                    }
                })

            },

            cambiarefalse() {
                this.cambiare = false
                this.getList();
            },
            ExportClick() {
                exportSettlement(this.queryParams, `佣金结算.csv`, '/orderCommission/exportOrderCommissionList').then(res => {
                    console.log(res);
                })
            },
            AgentStatusClick(data) {
                this.$confirm('确认要禁用吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    data.isEnabled = 1;
                    updateAgentStatus(data).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '禁用成功!'
                        });
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });

            },
            updataClick(data) {
                this.cambiare = true;
                this.cambiareform = data;
            },
            submitFormUpdata() {
                updateOrderCommissionStatus(this.cambiareform).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '提交成功!'
                    });
                    this.cambiare = false;
                    this.getList();
                })
            },
            deletClick(data) {
                this.$confirm('确认要删除吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteOrderCommission(data).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });

            },
            resetQuery() {

            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            changeTime(time) {
                return time.replace(/(\d{4})(\d{2})(\d{2})/, '$1/$2/$3');
            },
            tableRowClassName({ row, rowIndex }) {
                if (row.contactExpireTime) {
                    var begindate = new Date(Date.parse(this.changeTime(row.contactExpireTime))); //将开始时间由字符串格式转换为日期格式       
                    begindate = new Date(Date.parse(begindate)); //将开始时间由字符串格式转换为日期格式       
                    var myDate = new Date(); //此处将服务器当前日期作为结束日期，也可为其他任意时间 
                    var startDate = begindate.getTime(); //将开始日期转换成毫秒 
                    var endDate = myDate.getTime(); //将结束日期转换成毫秒  
                    var day = parseInt((startDate - endDate) / 1000 / 3600 / 24); //结束日期减去开始日期后转换成天数    
                    console.log('day', day); //day 457
                    if (day < 0) {
                        return 'warning-row';
                    } else if (day <= 7) {
                        console.log(day);
                        return 'success-row';
                    } else if (day <= 15) {
                        return 'success-row1';
                    } else if (day <= 30) {
                        return 'success-row2';
                    }
                    return '';
                }
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.getList();
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
                    response.message +
                    "</div>",
                    "导入结果",
                    { dangerouslyUseHTMLString: true }
                );
                this.getList();
            },
            getList() {
                selectOrderCommissionListPage(this.queryParams).then((res) => {
                    if (res.data) {
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
    #dingp p {
        margin: 0px;
        font-size: 14px;
        font-weight: 700;
    }

    #dingp div {
        margin: 0px;
        font-size: 14px;
        font-weight: 700;
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