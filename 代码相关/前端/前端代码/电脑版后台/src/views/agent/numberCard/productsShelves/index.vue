<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.productName" placeholder="请输入号卡名称"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.operatorType" placeholder="请选择运营商" clearable filterable>
                    <el-option label="中国移动" value="0"></el-option>
                    <el-option label="中国电信" value="1"></el-option>
                    <el-option label="中国联通" value="2"></el-option>
                    <el-option label="中国广电" value="3"></el-option>
                </el-select>
            </el-form-item>
            <!-- <el-form-item prop="businessType">
                <el-select v-model="queryParams.productStatus" placeholder="请选择状态" clearable filterable
                    style="width: 240px">
                    <el-option label="已下架" value="0"></el-option>
                    <el-option label="上架中" value="1"></el-option>
                </el-select>
            </el-form-item> -->
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.productType" placeholder="请选择结算模式" clearable filterable>
                    <el-option label="日结秒返" value="0"></el-option>
                    <el-option label="月结产品" value="1"></el-option>
                    <el-option label="长期产品" value="2"></el-option>
                    <el-option label="其它" value="3"></el-option>
                    <el-option label="组合返佣" value="4"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy height="650">
            <el-table-column label="ID" align="center" prop="productId"width="50" />
            <el-table-column label="产品名称" align="center" prop="productName" width="110"/>
            <el-table-column label="产品主图" align="center" prop="productMasterMap">
                <template slot-scope="scope">
                    <img :src="scope.row.productMasterMap" alt="" style="width: 100px;">
                </template>
            </el-table-column>

            <el-table-column label="运营商" align="center" prop="operatorType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.operatorType==0">中国移动</p>
                    <p v-if="scope.row.operatorType==1">中国电信</p>
                    <p v-if="scope.row.operatorType==2">中国联通</p>
                    <p v-if="scope.row.operatorType==3">中国广电</p>
                </template>
            </el-table-column>
            <el-table-column label="结算模式" align="center" prop="productType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.productType==0" style="color: red;">日结秒返</p>
                    <p v-if="scope.row.productType==1" style="color:green;">月结产品</p>
                    <p v-if="scope.row.productType==2" style="color: red;">长期产品</p>
                    <p v-if="scope.row.productType==3" style="color:green;">其它</p>
                    <p v-if="scope.row.productType==4" style="color: red;">组合返佣</p>
                </template>
            </el-table-column>
            <el-table-column label="归属地区" align="center" prop="productGsdq" :show-overflow-tooltip="true" />
            <el-table-column label="推广要求" align="center" prop="productDemand" :show-overflow-tooltip="true" />
            <el-table-column label="排序" align="center" prop="productSort" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <el-input v-model="scope.row.productSort" @blur="sort(scope.row)"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="productStatus" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.productStatus==0" style="color: red;">已下架</p>
                    <p v-if="scope.row.productStatus==1" style="color:green;">上架中</p>
                </template>
            </el-table-column>
            <el-table-column label="产品佣金" align="center" prop="productCommission" :show-overflow-tooltip="true" />
            <el-table-column label="下游分销佣金" align="center" prop="distributionProductCommission"
                :show-overflow-tooltip="true" />
            <el-table-column label="收入佣金" align="center" prop="revenueProductCommission"
                :show-overflow-tooltip="true" />


            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                    <el-button @click="share(scope.row)" type="text" size="small">产品海报</el-button>
                    <br>
                    <el-button @click="handleCommission(scope.row)" type="text" size="small">下游佣金</el-button>
                    <br>
                    <el-button @click="handlefuzhi(scope.row)" type="text" size="small">复制链接</el-button>
                    <el-button @click="handleOpen(scope.row)" type="text" size="small">打开链接</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <!-- 佣金 -->
        <el-dialog :visible.sync="openCommission" width="350px" append-to-body>
            <el-form ref="form" v-model="form" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="下游佣金">
                            <el-input v-model="form.distributionProductCommission"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="openCommission = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="openbianji" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="form1" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="指定代理" prop="region">
                            <el-select v-model="form1.agentCodeList" multiple placeholder="请选择代理">
                                <el-option v-for="dict in searchType" :key="dict.agentCode" :label="dict.agentName"
                                    :value="dict.agentCode" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="openbianji = false">取 消</el-button>
                <el-button type="primary" @click="submitForm1">确 定</el-button>
            </div>
        </el-dialog>

            <!-- 分享 -->
            <el-dialog :visible.sync="shareOpen" width="350px" append-to-body>
                <img :src="sharedata.productQrcodeMap" alt="" style="width: 100%;">
                <div slot="footer" >
                    <el-button type="primary" @click="downloadImage">保存图片</el-button>
                </div>
            </el-dialog>

    </div>

</template>

<script>
    import {
        selectProductListPage,
        deleteProduct,
        updateProductStatus,
        updateProductSort,
        copyProduct,
        updateProduct,
        selectChildAgentList,
        selectUpstreamProductListPage,
        selectUpstreamApiListPage
    } from "@/api/monitor/business";
   import { agentSelectProductListPage,updateAgentProduct, updateProductCommission, } from "@/api/monitor/daili"
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址
                operatorsType: [],
                imageUrl: false,
                // 遮罩层
                loading: false,
                // 选中数组
                ids: [],
                // 非多个禁用
                multiple: true,
                // 显示搜索条件
                ruleFormdialog: false,
                openbianji: false,
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
                form: {},
                form1: {},
                upstreamApiCode: [],
                upstreamProductCode: [],
                openCommission: false,
                api: [],
                groupCode: [],
                ruleForm: [],
                searchType: [],
                sharedata:{},
                shareOpen:false,
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,
                    productStatus: 0,
                },
            };
        },
        created() {
            this.getList();
        },
        methods: {
            // 分享
            share(data) {
                this.sharedata = data;
                this.shareOpen = true;

            },
            downloadImage() {
                // 图片地址（需允许跨域访问）
                const url =  this.sharedata.productQrcodeMap;

                // 创建隐藏的 <a> 标签
                const link = document.createElement('a');
                link.href = url;
                link.download = 'img.jpg'; // 设置下载文件名
                document.body.appendChild(link);

                // 触发点击下载
                link.click();

                // 清理 DOM
                document.body.removeChild(link);
            },
            handleOpen(data){
                window.open(data.h5Url, '_blank')
            },
            bianjiClick(data) {
                selectChildAgentList({}).then((res) => {
                    this.searchType = res.data
                })
                this.form1 = data
                this.openbianji = true
            },
            submitForm1() {
                updateAgentProduct(this.form1).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.openbianji = false;
                    this.getList();
                })
            },
            submitup() {
                updateProductSort(this.ruleForm).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.ruleFormdialog = false;
                    this.getList();
                })
            },

            sort(data) {
                updateProductSort(data).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.getList();
                })

            },
            handleAvatarSuccess(res, file) {
                this.ruleForm.productPlacardMap = res.message;
                this.imageUrl = URL.createObjectURL(file.raw);
            },
            // 图片返回
            handlesuccess(file) {

            },
            submitForm() {
                updateProductCommission(this.form).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.openCommission = false;
                    this.getList();
                })

            },
            handleCommission(data) {
                this.openCommission = true;
                this.form = data;
            },
            handleImport() { },
            handleAdd() { },
            resetQuery() {

            },
            handleSelectionChange(val) {
                this.multipleSelection = val;
            },
            changeTime(time) {
                return time.replace(/(\d{4})(\d{2})(\d{2})/, '$1/$2/$3');
            },


            /** 搜索按钮操作 */
            handleQuery() {
                this.getList();
            },

            // 复制
            handlefuzhi(data) {

                navigator.clipboard.writeText(data.h5Url)
                    .then(() => {
                        this.$modal.msgSuccess("复制成功");
                    })
                    .catch(err => {
                        // 兼容旧版浏览器
                        document.execCommand(data.h5Url);
                        this.$modal.msgSuccess("复制成功");
                    });

            },


            getList() {
                agentSelectProductListPage(this.queryParams).then((res) => {
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