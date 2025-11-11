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

        <el-dialog title="产品配置管理" :visible.sync="openDisposition" append-to-body :fullscreen="true">
            <!-- 搜索表单区域 -->
            <el-form :model="searchForm" ref="searchForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
                <el-form-item label="产品名称" prop="upstreamProductName">
                    <el-input
                        v-model="searchForm.upstreamProductName"
                        placeholder="请输入产品名称"
                        clearable
                        style="width: 200px"
                        @keyup.enter.native="handleDispositionQuery"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" icon="el-icon-search" size="mini" :loading="searchLoading" @click="handleDispositionQuery">搜索</el-button>
                    <el-button icon="el-icon-refresh" size="mini" @click="resetDispositionQuery">重置</el-button>
                </el-form-item>
            </el-form>

            <!-- 操作按钮区域 -->
            <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                    <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAddopen('添加')"
                        v-hasPermi="['channel:channelManagement:add']">添加</el-button>
                </el-col>
                <!--<el-col :span="1.5">-->
                <!--    <el-button type="info" icon="el-icon-search" size="mini" @click="showSearch = !showSearch">{{showSearch ? '隐藏搜索' : '显示搜索'}}</el-button>-->
                <!--</el-col>-->
            </el-row>
            <el-table ref="tables" v-loading="dispositionLoading" :data="listDisposition" row-key="operatorReportId" border lazy height="650"
                style="font-weight: 700;">
                <el-table-column label="产品名称" align="center" prop="upstreamProductName" :show-overflow-tooltip="true" />
                <el-table-column label="关联产品" align="left" prop="productList" width="520">
                    <template slot-scope="scope">
                        <div v-if="scope.row.productList && scope.row.productList.length" class="bind-product-grid">
                            <el-card v-for="product in scope.row.productList"
                                     :key="product.productId || product.productCode"
                                     shadow="never"
                                     class="bind-product-card">
                                <div class="bind-card-header">
                                    <div class="title">
                                        <span class="dot" :class="product.productStatus === 1 ? 'dot-online' : 'dot-offline'"></span>
                                        <span class="product-name" :title="product.productName">{{ product.productName || '-' }}</span>
                                    </div>
                                    <el-tag size="mini" :type="getProductStatusTag(product)">
                                        {{ getProductStatusText(product) }}
                                    </el-tag>
                                </div>
                                <div class="bind-card-body">
                                    <div class="meta-row">
                                        <i class="el-icon-link"></i>
                                        <span>编码：{{ product.productCode || '--' }}</span>
                                    </div>
<!--                                    <div class="meta-row">-->
<!--                                        <i class="el-icon-time"></i>-->
<!--                                        <span>上架时间：{{ product.shelfTime ? formatTimestamp(product.shelfTime) : '&#45;&#45;' }}</span>-->
<!--                                    </div>-->
                                </div>
                                <div class="bind-card-actions">
                                    <el-button type="primary" plain size="mini" icon="el-icon-top"
                                               :disabled="product.productStatus === 1"
                                               :loading="productActionLoading[getProductActionKey(product)]"
                                               @click="handleProductShelf(product, 1)">
                                        上架
                                    </el-button>
                                    <el-button type="danger" plain size="mini" icon="el-icon-bottom"
                                               :disabled="product.productStatus === 0"
                                               :loading="productActionLoading[getProductActionKey(product)]"
                                               @click="handleProductShelf(product, 0)">
                                        下架
                                    </el-button>
                                </div>
                            </el-card>
                        </div>
                        <el-empty v-else description="暂未关联商品" :image-size="80"></el-empty>
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
        updateProductStatus,
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
                // 产品配置搜索表单
                searchForm: {
                    upstreamProductName: ''
                },
                // 产品配置loading状态
                dispositionLoading: false,
                searchLoading: false,
                // 总条数
                total: 100,
                totalopen: 100,
                // 弹出层标题
                title: "",
                // 表格数据
                list: [],
                listDisposition: [],
                // 记录单个产品上下架的loading状态
                productActionLoading: {},
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
                    upstreamProductName: '',
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
            getProductActionKey(product = {}) {
                if (product.productId) {
                    return `id_${product.productId}`;
                }
                if (product.productCode) {
                    return `code_${product.productCode}`;
                }
                return `tmp_${Math.random().toString(36).slice(2)}`;
            },
            getProductStatusTag(product = {}) {
                const status = Number(product.productStatus);
                if (status === 1) {
                    return 'success';
                }
                if (status === 0) {
                    return 'info';
                }
                return 'warning';
            },
            getProductStatusText(product = {}) {
                const status = Number(product.productStatus);
                if (status === 1) {
                    return '上架中';
                }
                if (status === 0) {
                    return '已下架';
                }
                return product.productStatusName || '未知状态';
            },
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
                this.dispositionLoading = true;
                if (this.tianopen == '添加') {
                    this.formopen.upstreamApiId = this.opendata.upstreamApiId;
                    addUpstreamProduct(this.formopen).then((res) => {
                        this.$modal.msgSuccess("新增成功");
                        this.handleDispositionQuery();
                        this.opentianjia = false;
                    }).catch(() => {
                        this.$modal.msgError("新增失败");
                    }).finally(() => {
                        this.dispositionLoading = false;
                    })
                } else {
                    updateUpstreamProduct(this.formopen).then((res) => {
                        this.$modal.msgSuccess("修改成功");
                        this.handleDispositionQuery();
                        this.opentianjia = false;
                    }).catch(() => {
                        this.$modal.msgError("修改失败");
                    }).finally(() => {
                        this.dispositionLoading = false;
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
                    this.dispositionLoading = true;
                    deleteUpstreamProduct(data.upstreamProductId).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.handleDispositionQuery();
                    }).catch(() => {
                        this.$message({
                            type: 'error',
                            message: '删除失败!'
                        });
                    }).finally(() => {
                        this.dispositionLoading = false;
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
                this.searchForm.upstreamProductName = '';
                this.openDisposition = true;
                this.loadDispositionData();
            },
            // 加载产品配置数据
            loadDispositionData() {
                this.dispositionLoading = true;
                const queryParams = {
                    ...this.opendata,
                    upstreamProductName: this.searchForm.upstreamProductName
                };
                return selectUpstreamProductListPage(queryParams).then((res) => {
                    this.listDisposition = res.data.rows;
                    this.totalopen = res.data.totalRows
                }).finally(() => {
                    this.dispositionLoading = false;
                })
            },
            // 产品配置搜索
            handleDispositionQuery() {
                this.searchLoading = true;
                this.opendata.pageNo = 1;
                this.loadDispositionData().finally(() => {
                    this.searchLoading = false;
                });
            },
            // 重置产品配置搜索
            resetDispositionQuery() {
                this.searchForm.upstreamProductName = '';
                this.opendata.pageNo = 1;
                this.loadDispositionData();
            },
            handleProductShelf(product, targetStatus) {
                if (!product || !product.productId) {
                    this.$message.error('当前关联商品信息不完整，无法操作');
                    return;
                }
                const actionKey = this.getProductActionKey(product);
                if (Number(product.productStatus) === Number(targetStatus)) {
                    return;
                }
                const actionText = targetStatus === 1 ? '上架' : '下架';
                this.$confirm(`确认要${actionText}【${product.productName || product.productCode}】吗？`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.$set(this.productActionLoading, actionKey, true);
                    const payload = {
                        productId: product.productId,
                        productStatus: targetStatus
                    };
                    updateProductStatus(payload).then(() => {
                        this.$message({
                            type: 'success',
                            message: `${actionText}成功!`
                        });
                        this.loadDispositionData();
                    }).catch((err) => {
                        console.error(err);
                        this.$message({
                            type: 'error',
                            message: `${actionText}失败，请稍后重试`
                        });
                    }).finally(() => {
                        this.$set(this.productActionLoading, actionKey, false);
                    });
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: `已取消${actionText}`
                    });
                });
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

<style scoped>
.bind-product-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 12px;
}

.bind-product-card {
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    padding: 8px 10px 10px;
    background-color: #fff;
    min-height: 140px;
}

.bind-card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 6px;
}

.bind-card-header .title {
    display: flex;
    align-items: center;
    gap: 6px;
    max-width: calc(100% - 80px);
}

.product-name {
    font-weight: 600;
    max-width: 160px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
}

.bind-card-body {
    font-size: 12px;
    color: #606266;
    margin-bottom: 8px;
}

.meta-row {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 4px;
}

.meta-row i {
    color: #999;
}

.bind-card-actions {
    display: flex;
    gap: 8px;
}

.dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    display: inline-block;
}

.dot-online {
    background-color: #13ce66;
    box-shadow: 0 0 4px rgba(19, 206, 102, 0.6);
}

.dot-offline {
    background-color: #c0c4cc;
}
</style>
