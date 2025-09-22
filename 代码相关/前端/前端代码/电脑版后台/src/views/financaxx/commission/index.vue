<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">


            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.orderId" placeholder="订单号"></el-input>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.cardPhone" placeholder="手机号"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.orderCommissionStatus" placeholder="请选择结算模式" clearable filterable
                    style="width: 240px">
                    <el-option label="未到结算状态" value="0"></el-option>
                    <el-option label="待结算" value="1"></el-option>
                    <el-option label="部分结算" value="2"></el-option>
                    <el-option label="已结算" value="3"></el-option>
                    <el-option label="无法结算" value="4"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.isRecharged" placeholder="请选择状态" clearable filterable
                    style="width: 240px">
                    <el-option label="未充值" value="0"></el-option>
                    <el-option label="已充值" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-date-picker v-model="dateRange" style="width: 240px" value-format="timestamp" type="daterange"
                    range-separator="至" start-placeholder="发放时间" end-placeholder="结束日期"
                    :default-time="['00:00:00', '23:59:59']"></el-date-picker>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
            <el-table-column label="订单ID" align="center" prop="orderId" >]
                <template slot-scope="scope">
                    <p>{{scope.row.orderId}}</p>
                </template>
            </el-table-column>
            <el-table-column label="关联信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>名称:{{scope.row.agentSourceName}}</p>
                    <p>产品名称:{{scope.row.productName}}</p>
                    <p>订单号:{{scope.row.orderId}}</p>
                    <p>开卡人手机号:{{scope.row.cardPhone}}</p>
                </template>
            </el-table-column>
            <el-table-column label="佣金总额" align="left" prop="productMasterMap">
                <template slot-scope="scope">
                    <p>产品佣金:{{scope.row.productCommission}}</p>
                    <p>收入佣金:{{scope.row.revenueProductCommission}}</p>
                    <p>分销佣金:{{scope.row.distributionProductCommission}}</p>
                </template>
            </el-table-column>
            <el-table-column label="结算模式" align="center" prop="orderCommissionStatus" width="110"
                :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.orderCommissionStatus==0">未到结算状态</p>
                    <p v-if="scope.row.orderCommissionStatus==1">待结算</p>
                    <p v-if="scope.row.orderCommissionStatus==2">部分结算</p>
                    <p v-if="scope.row.orderCommissionStatus==3">已结算</p>
                    <p v-if="scope.row.orderCommissionStatus==4">无法结算</p>
                    <!-- <p v-if="scope.row.withdrawalType==0" >号卡佣金</p>
                    <p v-if="scope.row.withdrawalType==1" >代理商提现</p>
                    <p v-if="scope.row.withdrawalType==2" >管理员操作</p> -->
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="operatorType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.isRecharged==0">未充值</p>
                    <p v-if="scope.row.isRecharged==1">已充值</p>
                </template>
            </el-table-column>
            <el-table-column label="生成时间" align="center" prop="orderCreateTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.orderCreateTime)}}</p>
                </template>
            </el-table-column>
            <!-- <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleCommission(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">修改</el-button>
                </template>
            </el-table-column> -->

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
    </div>
</template>

<script>
    import { selectOrderCommissionListPage } from "@/api/monitor/finance";
    export default {
        name: "Commission",
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
                // 导入文件

                // 是否显示弹出层
                open: false,
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
                },
            };
        },
        created() {
            this.getList();
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
                if (this.dateRange) {
                    (this.queryParams.startTime = this.dateRange[0]),
                        (this.queryParams.endTime = this.dateRange[1]);
                }
                selectOrderCommissionListPage(this.queryParams).then((res) => {
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
    .app-container p {
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