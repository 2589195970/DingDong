<template>
    <div class="app-container">
        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd('添加')"
                    v-hasPermi="['channel:channelManagement:add']">添加</el-button>
            </el-col>
        </el-row>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="550">
            <el-table-column label="ID" align="center" prop="upstreamApiId" />
            <el-table-column label="接口名称" align="center" prop="upstreamApiName" :show-overflow-tooltip="true" />
            <el-table-column label="参数" prop="argument">
                <template slot-scope="scope">
                    <span>参数1：{{scope.row.argument1}}</span><br>
                    <span>参数2：{{scope.row.argument2}}</span><br>
                    <span>参数3：{{scope.row.argument3}}</span><br>
                    <span>参数4：{{scope.row.argument4}}</span><br>
                    <span>参数5：{{scope.row.argument5}}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="回调地址" align="center" prop="callbackUrl" width="110" :show-overflow-tooltip="true" />
            <el-table-column label="时间" align="center" prop="createTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>{{formatTimestamp(scope.row.createTime)}}</span>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleAdd(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">编辑</el-button>
                    <el-button @click="handleClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">产品配置</el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" v-model="form" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="接口渠道" prop="companySimpleName">
                            <el-select v-model="form.upstreamApiType" style="width: 300px" placeholder="请选择" clearable
                                filterable id="upstreamApiName" @change="canq(form.upstreamApiType)">
                                <el-option v-for="item in upstreamApiName" :key="item.upstreamApiType"
                                    :label="item.upstreamApiName" :value="item.upstreamApiType">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="接口名称">
                            <el-input v-model="form.upstreamApiName" placeholder="一般是用渠道的名字，便于自己区分"
                                style="width: 300px" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数1" >
                            <el-input v-model="form.argument1" style="width: 300px;" :placeholder="chansm.argument1" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数2">
                            <el-input v-model="form.argument2" style="width: 300px":placeholder="chansm.argument2" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数3">
                            <el-input v-model="form.argument3" style="width: 300px":placeholder="chansm.argument3" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数4">
                            <el-input v-model="form.argument4" style="width: 300px" :placeholder="chansm.argument4"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数5">
                            <el-input v-model="form.argument5" style="width: 300px":placeholder="chansm.argument5" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="openDisposition" append-to-body :fullscreen="true">
            <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                    <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddopen('添加')"
                        v-hasPermi="['channel:channelManagement:add']">添加</el-button>
                </el-col>
            </el-row>
            <el-table ref="tables" :data="listDisposition" row-key="operatorReportId" border lazy height="650"
                style="font-weight: 700;">
                <el-table-column label="产品名称" align="center" prop="upstreamProductName" :show-overflow-tooltip="true" />
                <el-table-column label="关联产品" align="left" prop="territoryCheckType" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <span v-for="item in scope.row.productList">{{item}}<br></span>
                    
                    </template>
                </el-table-column>
                <el-table-column label="参数" align="left" prop="territoryCheckType" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <span>参数1：{{scope.row.argument1}}</span><br>
                        <span>参数2：{{scope.row.argument2}}</span><br>
                        <span>参数3：{{scope.row.argument3}}</span><br>
                        <span>参数4：{{scope.row.argument4}}</span><br>
                        <span>参数5：{{scope.row.argument5}}</span><br>
                    </template>
                </el-table-column>
           
                
                <el-table-column label="时间" align="center" prop="productMasterMap">
                    <template slot-scope="scope">
                        <p>{{formatTimestamp(scope.row.createTime)}}</p>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                        <el-button @click="handleAddopen(scope.row)" type="text" size="small"
                            v-hasPermi="['channel:channelManagement:edit']">编辑</el-button>
                        <el-button @click="handleDeleteopen(scope.row)" type="text" size="small"
                            v-hasPermi="['channel:channelManagement:remove']">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <pagination v-show="totalopen > 0" :total="totalopen" :page.sync="opendata.pageNo"
                :limit.sync="opendata.pageSize" @pagination="handleClick(opendata)" />
        </el-dialog>

        <el-dialog :title="title" :visible.sync="opentianjia" width="500px" append-to-body>
            <el-form ref="form" v-model="formopen" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="接口名称">
                            <el-input v-model="formopen.upstreamProductName" placeholder="一般是用渠道的名字，便于自己区分"
                                style="width: 300px" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数1">
                            <el-input v-model="formopen.argument1" style="width: 300px":placeholder="chansm1.argument1" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数2">
                            <el-input v-model="formopen.argument2" style="width: 300px":placeholder="chansm1.argument2" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数3">
                            <el-input v-model="formopen.argument3" style="width: 300px" :placeholder="chansm1.argument3"/>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数4">
                            <el-input v-model="formopen.argument4" style="width: 300px":placeholder="chansm1.argument4" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="groupCode" label="参数5">
                            <el-input v-model="formopen.argument5" style="width: 300px":placeholder="chansm1.argument5" />
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormopen">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
    import {
        selectUpstreamApiListPage,
        selectUpstreamApiTypeList,
        addUpstreamApi,
        updateUpstreamApi,
        deleteUpstreamApi,
        deleteUpstreamProduct,
        addUpstreamProduct,
        selectUpstreamProductListPage,
        updateUpstreamProduct,
        selectUpstreamExplain,
    } from "@/api/monitor/business";
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                // 遮罩层
                loading: false,
                // 选中数组
                ids: [],
                // 非多个禁用
                multiple: true,
                chansm:{
                    argument1:null,
                    argument2:null,
                    argument3:null,
                    argument4:null,
                    argument5:null,
                },
                chansm1:{
                    argument1:null,
                    argument2:null,
                    argument3:null,
                    argument4:null,
                    argument5:null,
                },
                // 显示搜索条件
                showSearch: true,
                // 总条数
                total: 100,
                totalopen: 100,
                // 弹出层标题
                title: "",
                // 表格数据
                list: [],
                listDisposition: [],
                // 导入文件

                // 是否显示弹出层
                open: false,
                openDisposition: false,
                opentianjia: false,
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: {},
                formopen: {},
                api: [],
                groupCode: [],
                opendata: {
                    pageNo: 1,
                    pageSize: 10,
                },
                upstreamApiName: [],
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
            canq(data) {
                this.chansm={};
                selectUpstreamExplain(data,'0').then((res) => {
                    if (res.data) {
                            this.chansm = res.data;
                        }
                    console.log(res.data);
                });
            },
            // 添加
            handleAddopen(data) {
                this.chansm1={};
                    selectUpstreamExplain(this.opendata.upstreamApiType,'1').then((res) => {
                        if (res.data) {
                            this.chansm1 = res.data;
                        }
                   
                    console.log(res.data);
                });
                this.opentianjia = true;
                this.tianopen = data;
                if (data == "添加") {
                } else {
                    this.formopen = data;
  
                }
            },

            submitFormopen() {
                if (this.tianopen == '添加') {
                    this.formopen.upstreamApiId = this.opendata.upstreamApiId;
                    addUpstreamProduct(this.formopen).then((res) => {
                        this.$modal.msgSuccess("新增成功");
                        this.handleClick(this.opendata);
                        this.opentianjia = false;
                    })
                } else {
                    updateUpstreamProduct(this.formopen).then((res) => {
                        this.$modal.msgSuccess("修改成功");
                        this.handleClick(this.opendata);
                        this.opentianjia = false;
                    })
                }
            },
            // 删除
            handleDeleteopen(data) {
                this.$confirm('确认要删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteUpstreamProduct(data.upstreamProductId).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.handleClick(this.opendata);
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            handleClick(data) {
                this.opendata = data;
                this.opendata.pageNo=1;
                this.opendata.pageSize=9999;
                this.openDisposition = true;
                selectUpstreamProductListPage(this.opendata).then((res) => {
                    this.listDisposition = res.data.rows;
                    this.totalopen = res.data.totalRows
                })
            },
            handleDelete(data) {
                this.$confirm('确认要删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteUpstreamApi(data.upstreamApiId).then((res) => {
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
            submitForm() {
                console.log(this.form);
                if (this.tian == '添加') {
                    addUpstreamApi(this.form).then((res) => {
                        this.$modal.msgSuccess("新增成功");
                        this.getList();
                        this.open = false;
                    })
                } else {
                    updateUpstreamApi(this.form).then((res) => {
                        this.$modal.msgSuccess("修改成功");
                        this.getList();
                        this.open = false;
                    })
                }


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
            changeTime(time) {
                return time.replace(/(\d{4})(\d{2})(\d{2})/, '$1/$2/$3');
            },

            /** 搜索按钮操作 */
            handleQuery() {
                this.getList();
            },
            handleAdd(data) {
                this.open = true;
                this.tian = data;
                if (data == "添加") {
                    selectUpstreamApiTypeList().then((res) => {
                        console.log(res.data);
                        this.upstreamApiName = res.data
                    })
                } else {
                    this.form = data;
                }

            },

            getList() {
                selectUpstreamApiTypeList().then((res) => {
                    this.upstreamApiName = res.data
                })
                selectUpstreamApiListPage(this.queryParams).then((res) => {
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
</style>