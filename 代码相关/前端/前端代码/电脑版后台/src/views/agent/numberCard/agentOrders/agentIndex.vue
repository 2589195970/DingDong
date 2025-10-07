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
                <el-input v-model="queryParams.orderId" placeholder="订单ID"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.orderUpstreamId" placeholder="系统订单号"></el-input>
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
                <el-button icon="el-icon-download" size="mini" @click="ExportClick">导出</el-button>
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
                </template>
            </el-table-column>
            <el-table-column label="开卡人信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>证件姓名: {{scope.row.cardName}}</span><br>
                    <span>证件号码：{{scope.row.cardId}}</span><br>
                    <span>联系电话：{{scope.row.cardPhone}}</span><br>
                    <span>省市区： {{scope.row.provinceName}}{{scope.row.cityName}}{{scope.row.countyName}}</span><br>
                    <span>收货地址：{{scope.row.cardAddress}}</span><br>
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
            <el-table-column label="接口" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>接口: {{scope.row.upstreamApiName}}</span><br>
                    <span>接口产品：{{scope.row.upstreamProductName }}</span><br>
                    <span>接口订单号：{{ scope.row.orderUpstreamId}}</span><br>
                    <span>状态：{{scope.row.upstreamOrderStatusMessage}}</span><br>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="selectOrderBalanceClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">查询余额</el-button><br>
                    <el-button @click="registroOP(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">日志</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="registro" width="550px" append-to-body :fullscreen="true">
            <el-table ref="tables" :data="listRegistro" row-key="oper" border lazy height="550">

                <el-table-column label="URL" align="center" prop="requestUrl" :show-overflow-tooltip="true" />
                <el-table-column label="时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <span>
                            {{formatTimestamp(scope.row.createTime)}}
                        </span>
                    </template>

                </el-table-column>
                <el-table-column label="请求报文" align="center" prop="requestBody" :show-overflow-tooltip="true" />
                <el-table-column label="返回报文" align="center" prop="requestMsg" :show-overflow-tooltip="true" />
            </el-table>
        </el-dialog>
    </div>
</template>

<script>
    import {
        selectOrderLogList,
        selectOrderBalance,
        exportSettlement,
        agentSelectOrderListPage,
    } from "@/api/monitor/business";
    import { getUserProfile } from "@/api/system/user";

    export default {
        name: "AgentOrders",
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
                listRegistro: [],
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
                registro: false,
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,
                },
                // 当前代理商代码
                currentAgentCode: null,

            };
        },
        created() {
            this.getCurrentAgent();
            this.getList();
        },
        methods: {
            // 获取当前代理商代码
            getCurrentAgent() {
                getUserProfile().then(response => {
                    const user = response.data;
                    // 假设用户信息中包含代理商代码字段
                    this.currentAgentCode = user.agentCode || user.downstreamCode;
                    // 自动设置代理商筛选条件
                    if (this.currentAgentCode) {
                        this.queryParams.downstreamCode = this.currentAgentCode;
                    }
                }).catch(error => {
                    console.error('获取用户信息失败:', error);
                    this.$message.error('获取代理商信息失败');
                });
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
            selectOrderBalanceClick(data) {
                selectOrderBalance({"orderId":data.orderId}).then((res) => {
                    this.$alert(res.message, '余额', {
                    confirmButtonText: '确定',

                });
                })

            },
            registroOP(data) {
                this.registro = true;

                selectOrderLogList(data.orderId).then((res) => {
                    if (res.data) {
                        this.listRegistro = res.data
                    } else {
                        this.listRegistro = []
                    }

                })
            },
            ExportClick() {
                // 处理时间参数
                if (this.dateRange && this.dateRange.length > 0) {
                    this.queryParams.starTime = this.dateRange[0];
                    this.queryParams.endTime = this.dateRange[1];
                } else {
                    this.queryParams.starTime = undefined;
                    this.queryParams.endTime = undefined;
                }

                // 添加排除代理商自己订单的参数
                this.queryParams.excludeOwnOrders = true;

                // 导出下级代理商的订单数据（排除自己的订单）
                exportSettlement(this.queryParams, `下级代理商订单数据.csv`, '/agentManagement/agentSelectOrderListPage').then(res => {
                    this.$message.success('导出成功');
                }).catch(error => {
                    console.error('导出失败:', error);
                    this.$message.error('导出失败，请稍后重试');
                })
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
                // 确保始终使用当前代理商的筛选条件
                if (this.currentAgentCode) {
                    this.queryParams.downstreamCode = this.currentAgentCode;
                }
                this.getList();
            },
            getList() {
                // 处理时间参数
                this.queryParams.starTime = undefined;
                this.queryParams.endTime = undefined;
                if (this.dateRange && this.dateRange.length > 0) {
                    this.queryParams.starTime = this.dateRange[0];
                    this.queryParams.endTime = this.dateRange[1];
                };

                // 添加排除代理商自己订单的参数
                this.queryParams.excludeOwnOrders = true;

                // 使用代理商专用接口查询下级代理商的订单
                agentSelectOrderListPage(this.queryParams).then((res) => {
                    if (res.data && res.data.rows) {
                        this.list = res.data.rows
                    } else {
                        this.list = []
                    }
                    this.total = res.data ? res.data.totalRows : 0
                }).catch(error => {
                    console.error('查询代理商订单失败:', error);
                    this.$message.error('查询订单数据失败');
                    this.list = [];
                    this.total = 0;
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