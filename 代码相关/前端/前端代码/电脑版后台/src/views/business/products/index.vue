<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.productName" placeholder="请输入号卡名称"></el-input>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.operatorType" placeholder="请选择运营商" clearable filterable>
                    <el-option v-for="dict in ydl" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.productStatus" placeholder="请选择状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in productStatus" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item prop="businessType">
                <el-select v-model="queryParams.productType" placeholder="请选择结算模式" clearable filterable>
                    <el-option v-for="dict in productType" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <!-- <el-form-item prop="responsiblePeople">
                <el-input v-model="queryParams.productGsdq" placeholder="请输入归属地"></el-input>
            </el-form-item> -->
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
            </el-form-item>
        </el-form>
        <!-- <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['channel:channelManagement:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
                    v-hasPermi="['channel:channelManagement:import']">导入</el-button>
            </el-col>
        </el-row> -->
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy height="650">
            <el-table-column label="ID" align="center" prop="productId" width="50" />
            <el-table-column label="上游产品" align="center" prop="upstreamProductName" :show-overflow-tooltip="true" />
            <el-table-column label="产品主图" align="center" prop="productMasterMap">
                <template slot-scope="scope">
                    <img :src="scope.row.productMasterMap" alt="" style="width: 100px;">
                </template>
            </el-table-column>
            <el-table-column label="产品名称" align="center" prop="productName" />
            <el-table-column label="产品编码" align="center" prop="productCode" width="110" :show-overflow-tooltip="true" />
            <el-table-column label="运营商" align="center" prop="operatorType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.operatorType=='0'">中国移动</p>
                    <p v-if="scope.row.operatorType=='1'">中国电信</p>
                    <p v-if="scope.row.operatorType=='2'">中国联通</p>
                    <p v-if="scope.row.operatorType=='3'">中国广电</p>
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
            <el-table-column label="推广要求" align="center" prop="productDemand" />
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
            <el-table-column label="佣金" align="center" prop="productCommission" :show-overflow-tooltip="true" />
            <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="upClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">编辑</el-button>
                    <el-button @click="handleCommission(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">佣金</el-button>
                    <el-button @click="handlexiajia(scope.row)" type="text" size="small"
                        v-if="scope.row.productStatus==1" v-hasPermi="['channel:channelManagement:edit']">下架</el-button>
                    <el-button @click="handleshangjia(scope.row)" type="text" size="small"
                        v-if="scope.row.productStatus==0" v-hasPermi="['channel:channelManagement:edit']">上架</el-button>
                    <br>
                    <el-button @click="handlefuzhi(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">复制</el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">删除</el-button>
                    <div slot="reference" class="name-wrapper">
                        <router-link :to="{ name: 'diquxianzhi', params: scope.row }">
                            <el-button type="text" size="small"
                                v-hasPermi="['channel:channelManagement:edit']">地区限制</el-button>
                        </router-link>

                        &thinsp; <el-button @click="share(scope.row)" type="text" size="small">产品海报</el-button>
                    </div>
                  
                    <el-button @click="handlefuzhi1(scope.row)" type="text" size="small">复制链接</el-button>
                    <el-button @click="handleOpen(scope.row)" type="text" size="small">打开链接</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <!-- 佣金 -->
        <el-dialog :visible.sync="openCommission" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="form" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="佣金">
                            <el-input v-model="form.productCommission"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="openCommission = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>


        <!-- 编辑 -->
        <el-dialog :visible.sync="ruleFormdialog" width="1000px" append-to-body :close-on-click-modal="false"
            :fullscreen="true">
            <el-form :model="ruleForm" ref="ruleForm" label-width="150px" class="demo-ruleForm" :inline="true">
                <div class="topss">
                    <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">基础信息
                    </div>
                    <el-form-item label="产品名称" prop="name">
                        <el-input v-model="ruleForm.productName"></el-input>
                    </el-form-item>
                    <el-form-item label="产品卖点" prop="name">
                        <el-input v-model="ruleForm.productCharacteristics"></el-input>
                    </el-form-item>

                    <el-form-item label="运营商" prop="region">
                        <el-select v-model="ruleForm.operatorType" placeholder="请选择运营商">
                            <el-option v-for="item in ydl" :key="item.id" :label="item.name" :value="item.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="结算模式" prop="region">
                        <el-select v-model="ruleForm.productType" placeholder="请选择结算模式">
                            <el-option v-for="dict in productType" :key="dict.id" :label="dict.name" :value="dict.id" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="开放全部代理商" prop="resource">
                        <el-radio-group v-model="ruleForm.isAllAgent">
                            <el-radio :label="0">否</el-radio>
                            <el-radio :label="1">是</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="指定代理" prop="region" v-if="ruleForm.isAllAgent==0">
                        <el-select v-model="ruleForm.agentCodeList" multiple placeholder="请选择代理">
                            <el-option v-for="dict in searchType" :key="dict.agentCode" :label="dict.agentName"
                                :value="dict.agentCode" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="通用流量" prop="delivery">
                        <!-- <el-input-number v-model="" controls-position="right" :min="1"></el-input-number> -->
                        <el-input placeholder="请输入内容" v-model="ruleForm.productTyll" :min="1">
                            <template slot="append">GB</template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="定向流量" prop="delivery">
                        <!-- <el-input-number v-model="ruleForm.productDxll" controls-position="right":min="1" :min="1"></el-input-number> -->
                        <el-input placeholder="请输入内容" v-model="ruleForm.productDxll" :min="1">
                            <template slot="append">GB</template>
                        </el-input>
                    </el-form-item>
                    <el-form-item label="通话分钟" prop="name">
                        <el-input placeholder="请输入内容" v-model="ruleForm.productThfz" :min="1">
                            <template slot="append">分</template>
                        </el-input>
                        <!-- <el-input v-model="ruleForm.productThfz"></el-input> -->

                    </el-form-item>
                    <br>
                    <el-form-item label="优惠月租" prop="delivery">
                        <el-input placeholder="请输入内容" v-model="ruleForm.productYhyz" :min="1">
                            <template slot="append">元</template>
                        </el-input>
                        <!-- <el-input-number v-model="ruleForm.productYhyz" controls-position="right" :min="1"></el-input-number> -->
                    </el-form-item>
                    <el-form-item label="原始月租" prop="delivery">
                        <el-input placeholder="请输入内容" v-model="ruleForm.productYsyz" :min="1">
                            <template slot="append">元</template>
                        </el-input>
                        <!-- <el-input-number v-model="ruleForm.productYsyz" controls-position="right" :min="1"></el-input-number> -->
                    </el-form-item>
                    <el-form-item label="首充说明" prop="name">
                        <el-input v-model="ruleForm.productScsm"></el-input>
                    </el-form-item>
                    <el-form-item label="归属地区" prop="name">
                        <el-input v-model="ruleForm.productGsdq"></el-input>
                    </el-form-item>
                    <el-form-item label="发货方式" prop="name">
                        <el-input v-model="ruleForm.productFafs"></el-input>
                    </el-form-item>
                    <el-form-item label="套餐介绍" prop="name">
                        <el-input v-model="ruleForm.productTcjs"></el-input>
                    </el-form-item>
                    <el-form-item label="推广要求" prop="name">
                        <el-input v-model="ruleForm.productDemand"></el-input>
                    </el-form-item>
                    <el-form-item label="余额配置" prop="name">
                        <el-input v-model="ruleForm.balanceConfig"></el-input>
                    </el-form-item>
                </div>
                <!-- 接口对接 -->
                <div class="topss">
                    <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">接口对接
                    </div>
                    <el-form-item label="选择接口" prop="region">
                        <el-select v-model="ruleForm.upstreamApiId" placeholder="请选择接口"
                            @change="canq(ruleForm.upstreamApiId)">
                            <el-option v-for="dict in upstreamApiCode" :key="dict.upstreamApiId"
                                :label="dict.upstreamApiName" :value="dict.upstreamApiId" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="选择产品" prop="region">
                        <el-select v-model="ruleForm.upstreamProductId" placeholder="请选择产品" >
                            <el-option v-for="dict in upstreamProductCode" :key="dict.upstreamProductId"
                                :label="dict.upstreamProductName" :value="dict.upstreamProductId" />
                        </el-select>
                    </el-form-item>
                </div>

                <!-- 限制条件 -->
                <div class="topss">
                    <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">限制条件
                    </div>

                    <el-form-item label="年龄限制（周岁）">
                        <el-col :span="11">
                            <el-input v-model="ruleForm.productAgeMin"></el-input>
                        </el-col>
                        <el-col :span="2" style="text-align: center;">—</el-col>
                        <el-col :span="11">
                            <el-input v-model="ruleForm.productAgeMax"></el-input>
                        </el-col>
                    </el-form-item>

                </div>
                <!-- 套餐详情 -->
                <div class="topss">
                    <div style="border-bottom: 1px solid #F2F2F2; font-weight: 700; font-size: 14px; margin: 10px;">套餐详情
                    </div>
                    <br>
                    <el-form-item label="产品主图" prop="resource">
                        <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                            :on-success="handleAvatarSuccess" :headers=headers>
                            <img v-if="ruleForm.productMasterMap" :src="ruleForm.productMasterMap" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="产品详情图" prop="resource">
                        <el-upload class="avatar-uploader" :action="uploadUrl" :show-file-list="false"
                            :on-success="handleAvatarSuccess1" :headers=headers>
                            <img v-if="ruleForm.productDetailMap" :src="ruleForm.productDetailMap" class="avatar">
                            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
                        </el-upload>
                    </el-form-item>
                    <br>
                    <el-form-item label="产品详情">
                        <editor v-model="ruleForm.productRemark" :min-height="192" style="width: 100%;" />
                    </el-form-item>
                </div>
                <el-form-item style="width: 100%; text-align: center;">
                    <el-button type="primary" @click="submitup()">修改</el-button>
                    <el-button @click="ruleFormdialog=false">取消</el-button>
                </el-form-item>
            </el-form>
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
        updateProductCommission,
        copyProduct,
        updateProduct,
        selectChildAgentList,
        selectUpstreamProductListPage,
        selectUpstreamApiListPage
    } from "@/api/monitor/business";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Products",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址
                headers: { Authorization: "Bearer " + getToken() },
                operatorsType: [],
                imageUrl: false,
                imageUrl1: false,
                // 遮罩层
                loading: false,
                // 选中数组
                ids: [],
                // 非多个禁用
                multiple: true,
                // 显示搜索条件
                ruleFormdialog: false,
                showSearch: true,
                // 总条数
                total: 100,
                // 弹出层标题
                title: "",
                shareOpen:false,
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
                upstreamApiCode: [],
                upstreamProductCode: [],
                openCommission: false,
                api: [],
                groupCode: [],
                ruleForm: [],
                searchType: [],
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,
                },
                sharedata:{},
                productType: [

                    {
                        name: '日结秒返',
                        id: 0
                    },
                    {
                        name: '月结产品',
                        id: 1
                    },
                    {
                        name: '长期产品',
                        id: 2
                    },
                    {
                        name: '其它',
                        id: 3
                    },
                    {
                        name: '组合返佣',
                        id: 4
                    },
                ],
                ydl: [
                    {
                        name: '中国移动',
                        id: 0
                    },
                    {
                        name: '中国电信',
                        id: 1
                    },
                    {
                        name: '中国联通',
                        id: 2
                    },
                    {
                        name: '中国广电',
                        id: 3
                    },

                ],
                productStatus: [
                    {
                        name: '已下架',
                        id: 0
                    },
                    {
                        name: '上架中',
                        id: 1
                    },
                ]
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
            canq(data) {
                this.ruleForm.upstreamProductId = undefined;
                selectUpstreamProductListPage({ upstreamApiId: data,
                    pageNo:1,
                    pageSize:9999
                 }).then((res) => {
                    this.upstreamProductCode = res.data.rows
                })
            },
            upClick(data) {
                console.log(data);

                selectChildAgentList({}).then((res) => {
                    console.log(res.data);
                    this.searchType = res.data
                })
                selectUpstreamApiListPage({}).then((res) => {
                    this.upstreamApiCode = res.data.rows
                    console.log(this.upstreamApiCode);
                    data.pageSize=9999;
                    selectUpstreamProductListPage(data).then((res) => {
                        this.upstreamProductCode = res.data.rows
                        console.log(this.upstreamProductCode);
                    })
                })

                this.ruleForm = data;
                console.log(this.ruleForm.isAllAgent);

                this.ruleFormdialog = true;
            },
            submitup() {
                // if (!this.ruleForm.operatorType) {
                //     this.$message.error("请选择运营商");
                // } else if (!this.ruleForm.productType) {
                //     this.$message.error("请选择结算模式");
                // } else {
                if (this.ruleForm.upstreamApiId) {
                    const foundObject = this.upstreamApiCode.find(obj => obj.upstreamApiId === this.ruleForm.upstreamApiId);
                    const foundObject1 = this.upstreamProductCode.find(obj => obj.upstreamProductId === this.ruleForm.upstreamProductId);
                    console.log(foundObject1);

                    this.ruleForm.upstreamProductName = foundObject1.upstreamProductName;
                    this.ruleForm.upstreamProductId = foundObject1.upstreamProductId;
                    this.ruleForm.upstreamProductCode = foundObject1.upstreamProductCode;
                    this.ruleForm.upstreamApiName = foundObject.upstreamApiName;
                    this.ruleForm.upstreamApiId = foundObject.upstreamApiId;
                    this.ruleForm.upstreamApiCode = foundObject.upstreamApiCode;

                }
                updateProduct(this.ruleForm).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '修改成功!'
                    });
                    this.ruleFormdialog = false;
                    this.getList();
                })
                // }
            },
            handlefuzhi1(data) {
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
                this.$set(this.ruleForm, 'productMasterMap', res.message)
            },
            handleAvatarSuccess1(res, file) {
                this.$set(this.ruleForm, 'productDetailMap', res.message)

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
            handlexiajia(data) {

                this.$confirm('确认要下架该产品吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    data.productStatus = 0;
                    updateProductStatus(data).then((res) => {

                        this.$message({
                            type: 'success',
                            message: '下架成功!'
                        });
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消下架'
                    });
                });
            },
            handleshangjia(data) {

                this.$confirm('确认要上架该产品吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    data.productStatus = 1;
                    updateProductStatus(data).then((res) => {

                        this.$message({
                            type: 'success',
                            message: '上架成功!'
                        });
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消上架'
                    });
                });
            },
            // 复制
            handlefuzhi(data) {
                this.$confirm('确认要复制该产品吗？', '信息', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    copyProduct(data).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '复制成功!'
                        });
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消复制'
                    });
                });
            },
            handleDelete(data) {
                this.$confirm('此操作将永久删除, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteProduct(data.productId).then((res) => {
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

            getList() {
                selectProductListPage(this.queryParams).then((res) => {
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
    /* .el-radio input[aria-hidden="true"] {
  display: none !important;
}

.el-radio:focus:not(.is-focus):not(:active):not(.is-disabled) .el-radio__inner {
  box-shadow: none !important;
} */


    .topss {
        margin: 20px;
        padding: 10px;
        border: 1px solid #F2F2F2;
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

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }

    .el-input--suffix {
        width: 202px;
    }
</style>