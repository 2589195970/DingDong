<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.orderSource" placeholder="订单来源" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.orderStatus" placeholder="订单状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in orderStatus" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.isRecharged" placeholder="首充状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in isRecharged" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.orderId" placeholder="系统订单号"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.orderUpstreamId" placeholder="上游订单号"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.productName" placeholder="产品名称"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardName" placeholder="开卡人姓名"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardPhone" placeholder="开卡人手机号"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardId" placeholder="开卡人身份证"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.downstreamCode" placeholder="下游代理" clearable filterable>
                    <el-option v-for="dict in downstreamCode" :key="dict.agentCode" :label="dict.agentName"
                        :value="dict.agentCode" />
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.orderCommissionStatus" placeholder="佣金状态" clearable filterable>
                    <el-option v-for="dict in orderCommissionStatus" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="timestamp" type="daterange"
                    range-separator="至" start-placeholder="下单时间" end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <el-row :gutter="10" class="mb8">
        </el-row>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
            <el-table-column label="订单信息" align="left" prop="companyName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>来源：
                        <span v-if="scope.row.orderSource==0">信息流</span>
                        <span v-if="scope.row.orderSource==1">合作方API进单</span>
                        <span v-if="scope.row.orderSource==2">导单</span>
                        <span v-if="scope.row.orderSource==2">重推</span>
                    </span><br>
                    <span>订单ID：{{ scope.row.orderId}}</span><br>
                    <span>系统订单号：{{scope.row.orderUpstreamId}}</span><br>
                    <span>产品名称：{{scope.row.productName}}</span><br>
                    <span>运营商：
                        <span v-if="scope.row.operatorType==0">中国移动</span>
                        <span v-if="scope.row.operatorType==1">中国电信</span>
                        <span v-if="scope.row.operatorType==2">中国联通</span>
                        <span v-if="scope.row.operatorType==3">中国广电</span>
                    </span><br>
                    <span>代理商名称：{{ scope.row.showDownstreamName}}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="开卡人信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>证件姓名: {{scope.row.cardName}}</span><br>
                    <span>证件号码：{{scope.row.cardId}}</span><br>
                    <span>联系电话：{{scope.row.cardPhone}}</span><br>
                    <span>收货地址：{{scope.row.provinceName}}-{{scope.row.cityName}}-{{scope.row.countyName}}-{{scope.row.cardAddress}}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="订单状态" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>结算模式:
                        <span v-if="scope.row.productType==0">日结秒返</span>
                        <span v-if="scope.row.productType==1">月结产品</span>
                        <span v-if="scope.row.productType==2">长期产品</span>
                        <span v-if="scope.row.productType==3">其它</span>
                        <span v-if="scope.row.productType==4">组合返佣</span>
                    </span><br>
                    <span>订单状态：
                        <span v-if="scope.row.orderStatus==-1">失败</span>
                        <span v-if="scope.row.orderStatus==0">申请成功</span>
                        <span v-if="scope.row.orderStatus==1">申请中</span>
                        <span v-if="scope.row.orderStatus==2">发货</span>
                        <span v-if="scope.row.orderStatus==4">激活</span>
                    </span><br>
                    <span>是否首充：
                        <span v-if="scope.row.isRecharged==0">未充值</span>
                        <span v-if="scope.row.isRecharged==1">已充值</span>
                    </span><br>
                    <span>首充金额：{{scope.row.rechargeAmount}}</span><br>
                    <span>佣金状态：
                        <span v-if="scope.row.orderCommissionStatus==0">未到结算状态</span>
                        <span v-if="scope.row.orderCommissionStatus==1">待结算</span>
                        <span v-if="scope.row.orderCommissionStatus==3">已结算</span>
                        <span v-if="scope.row.orderCommissionStatus==4">无法结算</span>
                    </span><br>
                    <span>佣金说明：{{scope.row.orderCommissionMessage}}</span><br>
                    <span>下单时间：{{formatTimestamp(scope.row.createTime)}}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="生产信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>生产号码: {{scope.row.accNumber }}</span><br>
                    <span>物流名称：{{scope.row.express }}</span><br>
                    <span>物流单号：{{scope.row.trackingNumber}}</span><br>
                    <span>失败原因：{{scope.row.orderMessage }}</span><br>
                </template>
            </el-table-column>
            <!-- <el-table-column label="接口" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>接口: {{scope.row.upstreamApiName}}</span><br>
                    <span>接口产品：{{scope.row.upstreamProductName }}</span><br>
                    <span>接口订单号：{{ scope.row.orderUpstreamId}}</span><br>
                    <span>状态：{{scope.row.upstreamOrderStatusMessage}}</span><br>
                </template>
            </el-table-column> -->
            <!-- <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleCommission(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:edit']">重新下单</el-button>
                    <el-button @click="registroOP(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:remove']">日志</el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:remove']">更改状态</el-button>
                </template>
            </el-table-column> -->
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="openCommission" width="550px" append-to-body>
            <el-form ref="form" v-model="form" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="businessType" label="原产品">
                            <el-input v-model="form.productName" style="width: 240px" :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="businessType" label="新产品">
                            <el-select v-model="form.productCode" placeholder="新产品" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in productCodeList " :key="dict.productCode"
                                    :label="dict.productName" :value="dict.productCode" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="真实姓名">
                            <el-input v-model="form.cardName" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="身份证号">
                            <el-input v-model="form.cardId" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="手机号">
                            <el-input v-model="form.cardPhone" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="openCommission = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="registro" width="550px" append-to-body>

            <el-table ref="tables" :data="listRegistro" row-key="oper" border lazy height="550">

                <el-table-column label="ID" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />
                <el-table-column label="时间" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />
                <el-table-column label="内容" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />

            </el-table>

        </el-dialog>
        <el-dialog :visible.sync="cambiare" width="550px" append-to-body>
            <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="businessType" label="订单来源">
                            <el-select v-model="cambiareform.orderSource" placeholder="订单来源" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="businessType" label="订单状态">
                            <el-select v-model="cambiareform.orderStatus" placeholder="订单状态" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderStatus" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="首充状态">
                            <el-select v-model="cambiareform.isRecharged" placeholder="首充状态" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in isRecharged" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="首充金额">
                            <el-input v-model="cambiareform.rechargeAmount" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="佣金状态">
                            <el-select v-model="cambiareform.orderCommissionStatus" placeholder="佣金状态" clearable
                                filterable style="width: 240px">
                                <el-option v-for="dict in orderCommissionStatus" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormUpdata">提交</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import { againOrderSubmit, selectOrderLogList, updateOrderStatus, selectProductListPage, selectUpstreamApiListPage,selectChildAgentList } from "@/api/monitor/business";
    import {agentSelectOrderListPage } from "@/api/monitor/daili";
    export default {
        name: "Operlog",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                // 遮罩层
                loading: false,
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
                downstreamCode:[],
                listRegistro: [],
                cambiareform: [],
                upstreamApiCode: [],
                productCodeList: [],
                // 导入文件
                openCommission: false,
                registro: false,
                // 是否显示弹出层
                open: false,
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: {

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

                orderSource: [
                    {
                        name: "信息流",
                        id: 0
                    },
                    {
                        name: "合作方API进单",
                        id: 1
                    },
                    {
                        name: "导单",
                        id: 2
                    },
                    {
                        name: "重推",
                        id: 3
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
                isRecharged: [
                    {
                        name: "未充值",
                        id: 0
                    },
                    {
                        name: "已充值",
                        id: 1
                    },
                ],
                cambiare: false,
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
            selectUpstreamApiListPage({}).then((res) => {
                console.log(res.data);
                this.upstreamApiCode = res.data.rows
            })
            selectChildAgentList({}).then((res) => {
                console.log(res.data);
                this.downstreamCode = res.data
            })
        },
        methods: {
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
            submitFormUpdata() {
                updateOrderStatus(this.cambiareform).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '更新成功!'
                    });
                    this.cambiare = false;
                })
            },
            handleDelete(data) {
                this.cambiare = true;
                this.cambiareform = data;
            },
            submitForm() {

                this.$confirm('确认要重推订单吗？', '推送订单', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    againOrderSubmit(this.form).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '重推成功!'
                        });
                        this.openCommission = false;
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消重推'
                    });
                });
            },
            // handleCommission(data) {
            //     this.openCommission = true;
            //     this.form = data;
            //     selectProductListPage({}).then((res) => {
            //         this.productCodeList = res.data.rows;
            //     })
            // },
            handleAdd() {

            },
            registroOP(data) {
                this.registro = true;

                selectOrderLogList(data.orderId).then((res) => {
                    if (res.data.rows) {
                        this.listRegistro = res.data.rows
                    } else {
                        this.listRegistro = []
                    }

                })
            },
            resetQuery() {

            },
            handleImport() {

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
            getList() {
                this.queryParams.starTime = undefined;
                this.queryParams.endTime = undefined;
                if (this.dateRange) {
                    if (this.dateRange.length > 0) {
                        this.queryParams.starTime = this.dateRange[0];
                        this.queryParams.endTime = this.dateRange[1];
                    };
                };
                agentSelectOrderListPage(this.queryParams).then((res) => {
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