<template>
    <div class="app-container">
        <!-- 余额提现区域 -->
        <div class="withdrawal-section">
            <h3 class="section-title">余额提现</h3>
            <el-form :model="queryParams" label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="topss">
                    <span class="pz">账户余额: {{pz.balance * 0.01}} 元，</span>
                    <span class="pz">增值税发票税费:{{pz.withdrawRate}}%</span>
                    <!-- 删除最低提现金额显示 -->
                </div>
                <div class="topss">
                    <el-form-item label="提现模式" prop="resource">
                        <el-radio-group v-model="queryParams.withdrawalType">
                            <el-radio label="1">支付宝</el-radio>
                            <el-radio label="2">银行卡</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <br>
                      <div v-if="queryParams.withdrawalType=='1'">
                        <el-form-item label="支付宝账号">
                            <el-input v-model="queryParams.zfbAccount" placeholder="支付宝账号"></el-input>
                        </el-form-item>
                        <br>
                        <el-form-item label="真实姓名">
                            <el-input v-model="queryParams.zfbAccountName" placeholder="真实姓名"></el-input>
                        </el-form-item>
                    </div>
                    <div v-if="queryParams.withdrawalType=='2'">
                        <el-form-item label="银行开户行">
                            <el-input v-model="queryParams.bankName" placeholder="银行开户行" style="width: 100%;"></el-input>
                        </el-form-item>
                        <br>
                        <el-form-item label="真实姓名">
                            <el-input v-model="queryParams.bankNumberName" placeholder="真实姓名"></el-input>
                        </el-form-item>
                        <br>
                        <el-form-item label="银行卡号">
                            <el-input v-model="queryParams.bankNumber" placeholder="银行卡号"></el-input>
                        </el-form-item>
                        <br>
                        <el-form-item label="手机号">
                            <el-input v-model="queryParams.bankNumberPhone" placeholder="手机号"></el-input>
                        </el-form-item>
                    </div>
                    <el-form-item label="提现金额">
                        <el-input v-model="queryParams.withdrawalAmount" placeholder="提现金额" @input="hlv"></el-input>
                        <p v-if="querwithd.withdrawalRate">提现费率:{{querwithd.withdrawalRate}}%</p>
                        <p v-if="querwithd.withdrawalRateAmount">提现手续费:{{querwithd.withdrawalRateAmount * 0.01}}元</p>
                        <p v-if="querwithd.receivedAmount">实际到账:{{querwithd.receivedAmount * 0.01}}元</p>
                    </el-form-item>
                    <br>
                    <el-form-item
                        style="position: relative; bottom:-22px;padding: 10px 0;height: 50px;text-align: center;width: 100%;">
                        <el-button type="primary" @click="submitForm(queryParams)">提交申请</el-button>
                    </el-form-item>
                </div>
            </el-form>
        </div>

        <!-- 提现记录区域 -->
        <div class="withdrawal-history-section">
            <h3 class="section-title">提现记录</h3>
            <el-form :model="historyQueryParams" ref="historyQueryForm" size="small" :inline="true" v-show="showSearch"
                label-width="100px">
                <el-form-item prop="responsiblePeople">
                    <el-input v-model="historyQueryParams.applicationNumber" placeholder="订单号"></el-input>
                </el-form-item>
                <el-form-item prop="businessType">
                    <el-select v-model="historyQueryParams.withdrawalType" placeholder="请选择提现方式" clearable filterable
                        style="width: 240px">
                                <el-option label="支付宝" value="1"></el-option>
                        <el-option label="银行卡" value="2"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="businessType">
                    <el-select v-model="historyQueryParams.withdrawalStatus" placeholder="请选择状态" clearable filterable
                        style="width: 240px">
                        <el-option label="申请中" value="0"></el-option>
                        <el-option label="打款中" value="1"></el-option>
                        <el-option label="提现成功" value="2"></el-option>
                        <el-option label="提现失败" value="3"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" size="mini" @click="handleHistoryQuery">搜索</el-button>
                </el-form-item>
            </el-form>
            
            <el-table ref="historyTables" v-loading="historyLoading" :data="historyList" row-key="operatorReportId" border lazy
                :tree-props="{ children: 'children' }" height="400" :row-class-name="tableRowClassName"
                @selection-change="handleHistorySelectionChange">
                <el-table-column label="ID" align="center" prop="withdrawalApplicationId" />
                <el-table-column label="代理商" align="center" prop="applyAgentName" />
                <el-table-column label="提现方式" align="center" prop="withdrawalType" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                                    <p v-if="scope.row.withdrawalType==1">支付宝</p>
                        <p v-if="scope.row.withdrawalType==2">银行卡</p>
                    </template>
                </el-table-column>
                <el-table-column label="提现金额" align="center" prop="withdrawalAmount">
                    <template slot-scope="scope">
                        <p>{{formatPercent(scope.row.withdrawalAmount)}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="打款金额" align="center" prop="withdrawalAmount">
                    <template slot-scope="scope">
                        <p>{{formatPercent(scope.row.receivedAmount)}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="手续费" align="center" prop="withdrawalRateAmount" width="110" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <p>{{formatPercent(scope.row.withdrawalRateAmount)}}</p>
                    </template>
                </el-table-column>
                <el-table-column label="收款信息" align="left" prop="operatorType" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
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
            </el-table>
            
            <pagination v-show="historyTotal > 0" :total="historyTotal" :page.sync="historyQueryParams.pageNo" :limit.sync="historyQueryParams.pageSize"
                @pagination="getHistoryList" />
        </div>
    </div>
</template>

<script>
import {
    // 申请提现
    addAgentWithdrawalApplication,
    // 计算汇率
    computeRate,
    // 查询提现配置
    selectWithdrawalConfig,
    // 查询余额
    selectWithdrawalRecord,
    // 查询提现记录
    selectWithdrawalApplicationListPage
} from "@/api/monitor/finance";
import { getToken } from "@/utils/auth";

export default {
    name: "BalanceWithdrawal",
    dicts: ['sys_oper_type', 'sys_common_status'],
    data() {
        return {
            uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址,
            headers: { Authorization: "Bearer " + getToken() },
            
            // 提现相关数据
            queryParams: {
                withdrawalType: '1',
            },
            querwithd: {},
            pz: {},
            
            // 提现记录相关数据
            historyLoading: false,
            showSearch: true,
            historyTotal: 0,
            historyList: [],
            historyQueryParams: {
                pageNo: 1,
                pageSize: 10,
            },
        };
    },
    async created() {
        try {
            // 获取配置数据
            const configRes = await selectWithdrawalConfig({});
            this.pz = configRes.data;

            // 获取提现记录并更新余额
            const recordRes = await selectWithdrawalRecord();
            this.$set(this.pz, 'balance', recordRes.data.balance);
            
            // 获取提现记录列表
            this.getHistoryList();
        } catch (error) {
            console.error('Error occurred while fetching data:', error);
        }
    },
    methods: {
        // 提现相关方法
        hlv() {
            if (this.queryParams.withdrawalAmount) {
                computeRate(this.queryParams.withdrawalAmount).then((res) => {
                    this.querwithd = res.data;
                })
            }
        },
        submitForm(formName) {
            addAgentWithdrawalApplication(formName).then((res) => {
                this.$modal.msgSuccess("已提交申请");
                // 提交成功后刷新提现记录
                this.getHistoryList();
                // 刷新余额
                this.refreshBalance();
            })
        },
        handleAvatarSuccess(res, file) {
            this.$set(this.queryParams, 'wxUrl', res.message)
        },
        handlesuccess(file) {
            // 图片上传前处理
        },
        
        // 提现记录相关方法
        getHistoryList() {
            this.historyLoading = true;
            selectWithdrawalApplicationListPage(this.historyQueryParams).then((res) => {
                if (res.data.rows) {
                    this.historyList = res.data.rows
                } else {
                    this.historyList = []
                }
                this.historyTotal = res.data.totalRows
                this.historyLoading = false;
            })
        },
        handleHistoryQuery() {
            this.historyQueryParams.pageNo = 1;
            this.getHistoryList();
        },
        handleHistorySelectionChange(val) {
            // 处理选择变化
        },
        
        // 工具方法
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
        formatPercent(value) {
            if (isNaN(parseFloat(value))) {
                console.warn("输入必须是有效数字");
                return "NaN";
            }
            return (value * 0.01).toFixed(2) + "元";
        },
        tableRowClassName({ row, rowIndex }) {
            // 可以根据需要添加行样式
            return '';
        },
        refreshBalance() {
            selectWithdrawalRecord().then((res) => {
                this.$set(this.pz, 'balance', res.data.balance);
            })
        }
    }
}
</script>

<style scoped>
.app-container {
    padding: 20px;
}

.withdrawal-section {
    margin-bottom: 40px;
    padding: 20px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background-color: #fafafa;
}

.withdrawal-history-section {
    padding: 20px;
    border: 1px solid #e4e7ed;
    border-radius: 8px;
    background-color: #fff;
}

.section-title {
    margin: 0 0 20px 0;
    padding-bottom: 10px;
    border-bottom: 2px solid #409EFF;
    color: #303133;
    font-size: 18px;
    font-weight: 600;
}

.pz {
    color: red;
    font-weight: 700;
    margin: 5px 0;
}

.topss {
    margin: 20px;
    padding: 10px;
    border: 1px solid #F2F2F2;
    background-color: #fff;
    border-radius: 4px;
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
