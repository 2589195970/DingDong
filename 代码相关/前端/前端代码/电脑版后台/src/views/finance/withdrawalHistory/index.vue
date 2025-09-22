<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.applicationNumber" placeholder="订单号"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.withdrawalType" placeholder="请选择提现方式" clearable filterable
                    style="width: 240px">
                    <el-option label="微信" value="0"></el-option>
                    <el-option label="支付宝" value="1"></el-option>
                    <el-option label="银行卡" value="2"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.withdrawalStatus" placeholder="请选择状态" clearable filterable
                    style="width: 240px">
                    <el-option label="申请中" value="0"></el-option>
                    <el-option label="打款中" value="1"></el-option>
                    <el-option label="提现成功" value="2"></el-option>
                    <el-option label="提现失败" value="3"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
            <el-table-column label="ID" align="center" prop="withdrawalApplicationId" />
            <el-table-column label="代理商" align="center" prop="applyAgentName" />
            <el-table-column label="提现方式" align="center" prop="withdrawalType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.withdrawalType==0">微信</p>
                    <p v-if="scope.row.withdrawalType==1">支付宝</p>
                    <p v-if="scope.row.withdrawalType==2">银行卡</p>
                </template>
            </el-table-column>
            <el-table-column label="提现金额" align="center" prop="withdrawalAmount" >
                <template slot-scope="scope">
                    <p>{{formatPercent(scope.row.withdrawalAmount)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="打款金额" align="center" prop="withdrawalAmount" >
                <template slot-scope="scope">
                    <p>{{formatPercent(scope.row.receivedAmount)}}</p>
                </template>
            </el-table-column>
            </el-table-column>
            <el-table-column label="手续费" align="center" prop="withdrawalRateAmount" width="110" :show-overflow-tooltip="true" >
                <template slot-scope="scope">
                    <p>{{formatPercent(scope.row.withdrawalRateAmount)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="收款信息" align="left" prop="operatorType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <div v-if="scope.row.withdrawalType==0">
                        <img :src="scope.row.wxUrl" alt="" style="width: 100px;">
                    </div>
                    <div v-if="scope.row.withdrawalType==1">
                        <p>支付宝姓名:{{scope.row.zfbAccountName}}</p>
                        <p>支付宝账号:{{scope.row.zfbAccount}}</p>
                    </div>
                    <div v-if="scope.row.withdrawalType==2">
                        <p>银行开户行:{{scope.row.bankName}}</p>
                        <p>真实姓名:{{scope.row.bankNumberName}}</p>
                        <p>银行卡账号:{{scope.row.bankNumber}}</p>
                        <p>手机号:{{scope.row.bankNumberPhone}}</p>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="withdrawalStatus" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.withdrawalStatus==0">申请中</p>
                    <p v-if="scope.row.withdrawalStatus==1">打款中</p>
                    <p v-if="scope.row.withdrawalStatus==2">提现成功</p>
                    <p v-if="scope.row.withdrawalStatus==3">提现失败</p>
                </template>
            </el-table-column>
            <el-table-column label="失败原因" align="center" prop="remark" :show-overflow-tooltip="true" />
            <el-table-column label="申请时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.createTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="处理时间" align="center" prop="updateTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.updateTime">{{formatTimestamp(scope.row.updateTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">

                    <div v-if="scope.row.withdrawalStatus==0||scope.row.withdrawalStatus==1">
                        <el-button @click="dakuanClick(scope.row)" type="text" size="small"
                            v-hasPermi="['channel:channelManagement:edit']">打款</el-button>
                        <el-button @click="handleClick(scope.row)" type="text" size="small"
                            v-hasPermi="['channel:channelManagement:edit']">拒绝</el-button>
                    </div>
                    <p v-if="scope.row.withdrawalStatus==2" style="color: lawngreen;">已经付款</p>
                    <p v-if="scope.row.withdrawalStatus==3" style="color: red;">拒绝付款</p>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="juopen" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="formJu" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="拒绝理由">
                            <el-input v-model="formJu.remark" type="textarea"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="juopen = false">取 消</el-button>
                <el-button type="primary" @click="submitFormJu">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog :visible.sync="open" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="form" label-width="100px">
                <div v-if="form.withdrawalType==0">
                    <img :src="form.wxUrl" alt="" style="width: 100px;">
                </div>
                <div v-if="form.withdrawalType==1">
                    <p>支付宝姓名:{{form.zfbAccountName}}</p>
                    <p>支付宝账号:{{form.zfbAccount}}</p>
                </div>
                <div v-if="form.withdrawalType==2">
                    <p>银行开户行:{{form.bankName}}</p>
                    <p>真实姓名:{{form.bankNumberName}}</p>
                    <p>银行卡账号:{{form.bankNumber}}</p>
                    <p>手机号:{{form.bankNumberPhone}}</p>
                </div>
                <el-input v-model="form.serialNumber" placeholder="交易流水号"></el-input>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="open = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import { selectWithdrawalApplicationListPage, updateWithdrawalApplication, deletelithdrawalApplication } from "@/api/monitor/finance";
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
                // 导入文件

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
                formJu: {},
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
            // 打款弹窗
            dakuanClick(data) {
                this.open = true;
                this.form = data
            },
            // 拒绝打款
            handleClick(data) {
                this.juopen = true;
                this.formJu = data

            },
            submitForm() {
                this.form.withdrawalStatus = 2
                updateWithdrawalApplication(this.form).then((res) => {
                    this.$modal.msgSuccess("提现成功");
                    this.getList();
                    this.open = false;
                })

            },
            submitFormJu() {
                this.formJu.withdrawalStatus = 3
                updateWithdrawalApplication(this.formJu).then((res) => {
                    this.$modal.msgSuccess("修改成功");
                    this.getList();
                    this.juopen = false;
                })
            },
            formatPercent(value) {
                if (isNaN(parseFloat(value))) {
                    console.warn("输入必须是有效数字");
                    return "NaN";
                }

                // 处理负数、0、整数等情况
                return (value * 0.01).toFixed(2) + "元";
            },
            getList() {
                selectWithdrawalApplicationListPage(this.queryParams).then((res) => {
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