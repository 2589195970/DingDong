<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.isEnabled" placeholder="请选择状态" clearable filterable
                    style="width: 240px">
                    <el-option label="启用" value="0"></el-option>
                    <el-option label="禁用" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.isRealName" placeholder="请选择实名状态" clearable filterable
                    style="width: 240px">
                    <el-option label="未实名" value="0"></el-option>
                    <el-option label="已实名" value="1"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.agentName" placeholder="输入代理商"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
            <el-table-column label="ID" align="center" prop="agentAccountId" />
            <el-table-column label="上级" align="center" prop="parentAgentName" :show-overflow-tooltip="true" />
            <el-table-column label="等级" align="center" prop="level" :show-overflow-tooltip="true" />
            <el-table-column label="用户名" align="center" prop="agentName" :show-overflow-tooltip="true" />
            <el-table-column label="手机号" align="center" prop="phone" :show-overflow-tooltip="true" />
            <el-table-column label="余额" align="center" prop="balance" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{scope.row.balance * 0.01}}元</p>
                </template>
            </el-table-column>
            <el-table-column label="实名状态" align="center" prop="isRealName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.isRealName==0" style="color: red;">未实名</p>
                    <p v-if="scope.row.isRealName==1" style="color: green;">已实名</p>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="isEnabled" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.isEnabled==0" style="color: green;">启用</p>
                    <p v-if="scope.row.isEnabled==1" style="color: red;">禁用</p>
                </template>
            </el-table-column>
            <el-table-column label="加解密状态" align="center" prop="isEnabled" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.isEncrypt==0" style="color: green;">订单加密</p>
                    <p v-if="scope.row.isEncrypt==1" style="color: red;">订单解密</p>
                </template>
            </el-table-column>

            <el-table-column label="注册时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.createTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <!-- <el-button @click="handleCommission(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">编辑</el-button> -->
                    <el-button @click="isEncryptClick(scope.row,1)" type="text" size="small"
                        v-if="scope.row.isEncrypt==0" v-hasPermi="['channel:channelManagement:edit']">订单解密</el-button>
                    <el-button @click="isEncryptClick(scope.row,0)" type="text" size="small" v-else
                        v-hasPermi="['channel:channelManagement:edit']">订单加密</el-button>
                    <el-button @click="AgentStatusClick(scope.row)" type="text" size="small"
                        v-if="scope.row.isEnabled==0" v-hasPermi="['channel:channelManagement:edit']">禁用</el-button>
                    <el-button @click="AgentStatusqiClick(scope.row)" type="text" size="small" v-else
                        v-hasPermi="['channel:channelManagement:edit']">启用</el-button>
                    <el-button @click="resetPwdDefaultcz(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">重置密码</el-button>
                    <el-button @click="resetPwdDefaultmianm(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">免密登陆</el-button>
                    <br>
                    <div slot="reference" class="name-wrapper">
                        <el-button @click="updataClick(scope.row)" type="text" size="small"
                            v-hasPermi="['channel:channelManagement:edit']">调整余额</el-button> &thinsp;
                        <router-link :to="{ name: 'xiangqing', params: scope.row }">
                            <el-button type="text" size="small"
                                v-hasPermi="['channel:channelManagement:edit']">余额记录</el-button>
                        </router-link>
                    </div>

                    <el-button @click="deletClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">删除</el-button>
                </template>
            </el-table-column>

        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="cambiare" width="550px" append-to-body>
            <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
                <el-form-item prop="businessType" label="余额类型">
                    <el-select v-model="cambiareform.type" placeholder="请选择余额加减" clearable filterable
                        style="width: 240px">
                        <el-option label="加" value="0"></el-option>
                        <el-option label="减" value="1"></el-option>
                    </el-select>
                </el-form-item>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="金钱">
                            <el-input v-model="cambiareform.balanceYun" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormUpdata">提交</el-button>
                <el-button @click="cambiare=false">取消</el-button>
            </div>
        </el-dialog>
    </div>

</template>

<script>
    import {
        agentSelectOrderListPage,
        deleteAgentAccount,
        updateBalance,
        updateAgentStatus,
        selectWithdrawalRecordDetailsListPage,
        updateAgentEncryptStatus,
        resetPwdDefault,
        loginFreePassword
    } from "@/api/monitor/business";
    import { getToken, setToken, removeToken } from '@/utils/auth'
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
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
            isEncryptClick(data, dai) {
                data.isEncrypt = dai;
                if (dai == 1) {
                    updateAgentEncryptStatus(data).then((res) => {

                        this.$message({
                            type: 'success',
                            message: '订单解密成功!'
                        });
                        this.getList();
                    })
                } else {
                    this.$confirm('确认要加密吗？', '信息', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        updateAgentEncryptStatus(data).then((res) => {
                            this.$message({
                                type: 'success',
                                message: '加密成功!'
                            });
                            this.getList();
                        })


                    }).catch(() => {
                        this.$message({
                            type: 'info',
                            message: '已取消'
                        });
                        this.getList();
                    });
                }


            },
            resetPwdDefaultmianm(data) {
                loginFreePassword({
                    username:data.phone,
                }).then((res) => {
                    setToken(res.token)
                    
                    this.$router.push('/login')
                    this.$router.go(0)
                })

            },

            resetPwdDefaultcz(data) {
                this.$confirm('确认要重置密码？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    resetPwdDefault(data.agentName).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '成功!'
                        });
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消'
                    });
                });

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
                updateBalance(this.cambiareform).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '提交成功!'
                    });
                    this.getList();
                })
            },
            deletClick(data) {
                this.$confirm('确认要删除吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteAgentAccount(data.agentAccountId).then((res) => {

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

            getList() {
                agentSelectOrderListPage(this.queryParams).then((res) => {
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