<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.agentName" placeholder="请输入下游名称"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.status" placeholder="请选择实名状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in operatorsType" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <!-- <el-row :gutter="10">
            <el-col :span="1.5">
                <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
                    v-hasPermi="['channel:channelManagement:import']">导出</el-button>
            </el-col>
        </el-row> -->
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
            @selection-change="handleSelectionChange">
            <el-table-column label="ID" align="center" prop="nameAuditId" />
            <el-table-column label="下游名称" align="center" prop="agentName" :show-overflow-tooltip="true" />
            <el-table-column label="实名状态" align="center" prop="status">
                <template slot-scope="scope">
                    <span v-if="scope.row.status==0">待认证</span>
                    <span v-if="scope.row.status==1" style="color: red;">实名审核失败</span>
                    <span v-if="scope.row.status==2"  style="color: blue;">实名认证成功</span>
                </template>
            </el-table-column>
            <el-table-column label="用户姓名" align="center" prop="cardName" />
            <el-table-column label="身份证" align="center" prop="cardId" :show-overflow-tooltip="true" />
            <el-table-column label="身份证反面" align="center" prop="cardIdUrlBack">
                <template slot-scope="scope">
                    <img :src="scope.row.cardIdUrlBack" alt="" style="width: 100px;">
                </template>
            </el-table-column>
            <el-table-column label="身份证正面" align="center" prop="cardIdUrlFront">
                <template slot-scope="scope">
                    <img :src="scope.row.cardIdUrlFront" alt="" style="width: 100px;">
                </template>
            </el-table-column>

            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <!-- <el-button @click="handleClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">修改</el-button> -->
                        <p v-if="scope.row.status==2" style="color: green;">已审核</p>
                    <el-button v-else @click="handleDelete(scope.row)" type="text" size="small" 
                        v-hasPermi="['channel:channelManagement:remove']">审核</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
            <el-dialog :visible.sync="open" width="350px" append-to-body>
                <el-form ref="form" v-model="form" label-width="100px">
                    <el-select v-model="form.statuss" placeholder="请选择实名状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in operatorsType" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submitFormUpdata">提交</el-button>
                </div>
            </el-dialog>
    </div>
</template>

<script>
    import { selectNameAuditListPage, updateNameAuditStatus } from "@/api/monitor/business";
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
                operatorsType: [
                    {
                        name: "待认证",
                        id: 0
                    },
                    {
                        name: "实名审核失败",
                        id: 1
                    },
                    {
                        name: "实名认证成功",
                        id: 2
                    },
                ],
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
            submitFormUpdata(){
                this.form.status=this.form.statuss;
                updateNameAuditStatus(this.form).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.getList();
                    this.open=false
                })
            },

            // handleClick(data) {
            //     this.open = true;

            // },
            handleDelete(data) {
               
                this.form=data;
                this.form.statuss=data.status;
                this.open = true;
            },

            getList() {
                selectNameAuditListPage(this.queryParams).then((res) => {
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