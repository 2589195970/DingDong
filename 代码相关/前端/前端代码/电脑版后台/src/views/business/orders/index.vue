<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="100px">
            <el-form-item>
                <el-select v-model="queryParams.orderSource" placeholder="订单来源" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-select v-model="queryParams.orderStatus" placeholder="订单状态" clearable filterable
                    style="width: 240px">
                    <el-option v-for="dict in orderStatus" :key="dict.id" :label="dict.name" :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
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
            <el-form-item>
                <el-select v-model="queryParams.upstreamApiId" placeholder="请选择接口" clearable filterable>
                    <el-option v-for="dict in upstreamApiCode" :key="dict.upstreamApiType" :label="dict.upstreamApiName"
                        :value="dict.upstreamApiId" />
                </el-select>
            </el-form-item>

            <el-form-item>
                <el-select v-model="queryParams.downstreamCode" placeholder="下游代理" clearable filterable>
                    <el-option v-for="dict in downstreamCode" :key="dict.agentCode" :label="dict.agentName"
                        :value="dict.agentCode" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-select v-model="queryParams.orderCommissionStatus" placeholder="佣金状态" clearable filterable>
                    <el-option v-for="dict in orderCommissionStatus" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-select v-model="queryParams.isNotNullOrderUpstreamId" placeholder="上游单号是否为空" clearable filterable>
                    <el-option v-for="dict in isNotNullOrderUpstreamId" :key="dict.id" :label="dict.name"
                        :value="dict.id" />
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-select v-model="queryParams.photoStatus" placeholder="照片审核状态" clearable filterable>
                    <el-option v-for="dict in photoStatusOptions" :key="dict.id" :label="dict.name"
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
                <el-button size="mini" @click="importClick">导入<i class="el-icon-upload el-icon--right"></i></el-button>
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
                    <span>代理商名称：{{ scope.row.showDownstreamName}}</span><br>
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
                    <span v-if="scope.row.photoStatus !== undefined && scope.row.photoStatus !== null && scope.row.photoStatus !== 0">
                        照片审核：
                        <el-tag :type="getPhotoStatusTagType(scope.row.photoStatus)" size="mini">
                            {{scope.row.photoStatusName || getPhotoStatusName(scope.row.photoStatus)}}
                        </el-tag>
                    </span><br>
                    <span>下单时间：{{formatTimestamp(scope.row.createTime)}}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="生产信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>生产号码: {{scope.row.accNumber }}</span><br>
                    <span>物流名称：{{scope.row.express }}</span><br>
                    <span>物流单号：{{scope.row.trackingNumber}}</span><br>
                    <span>订单状态：{{scope.row.orderMessage }}</span><br>
                </template>
            </el-table-column>
            <el-table-column label="接口" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <span>接口: {{scope.row.upstreamApiName}}</span><br>
                    <span>接口产品：{{scope.row.upstreamProductName }}</span><br>
                    <span>接口订单号：{{ scope.row.orderUpstreamId}}</span><br>
                    <span>订单状态：{{scope.row.upstreamOrderStatusMessage}}</span><br>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleCommission(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">重新下单</el-button><br>
                    <el-button @click="selectOrderBalanceClick(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">查询余额</el-button><br>
                    <!-- 照片审核相关按钮 - 只有需要照片审核的订单才显示 -->
                    <template v-if="scope.row.photoStatus !== undefined && scope.row.photoStatus !== null && scope.row.photoStatus !== 0">
                      <div  v-if="scope.row.photoStatus === 1 || scope.row.photoStatus === 5">
                        <el-button @click="handlePhotoUpload(scope.row)" type="text" size="small"
                                   style="color: #409EFF">上传照片</el-button><br>
                      </div>
                      <div v-if="scope.row.photoStatus === 2">
                        <el-button @click="handleSubmitPhoto(scope.row)" type="text" size="small"
                                   style="color: #E6A23C">提交审核</el-button><br>
                      </div>
                      <div v-if="scope.row.photoStatus === 3">
                        <el-button @click="handlePhotoAudit(scope.row)" type="text" size="small"
                                   style="color: #F56C6C">审核照片</el-button><br>
                      </div>
                        <!--<el-button @click="handleViewPhotos(scope.row)" type="text" size="small"-->
                        <!--    v-if="scope.row.photoStatus >= 2"-->
                        <!--    style="color: #67C23A">查看照片</el-button><br>-->
                    </template>
                    <el-button @click="registroOP(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">日志</el-button><br>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">更改状态</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
            @pagination="getList" />
        <el-dialog :visible.sync="openCommission" width="550px" append-to-body>
            <el-form ref="form" v-model="form" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="原产品">
                            <el-input v-model="form.productName" style="width: 240px" :disabled="true"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="新产品">
                            <el-select v-model="form.productCode" placeholder="新产品" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in productCodeList " :key="dict.productCode"
                                    :label="dict.productName" :value="dict.productCode" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="真实姓名">
                            <el-input v-model="form.cardName" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="身份证号">
                            <el-input v-model="form.cardId" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="手机号">
                            <el-input v-model="form.cardPhone" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="openCommission = false">取 消</el-button>
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>
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
        <el-dialog :visible.sync="cambiare" width="550px" append-to-body>
            <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="订单来源">
                            <el-select v-model="cambiareform.orderSource" placeholder="订单来源" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item label="订单状态">
                            <el-select v-model="cambiareform.orderStatus" placeholder="订单状态" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderStatus" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>

                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="首充状态">
                            <el-select v-model="cambiareform.isRecharged" placeholder="首充状态" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in isRecharged" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="首充金额">
                            <el-input v-model="cambiareform.rechargeAmount" style="width: 240px"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="佣金状态">
                            <el-select v-model="cambiareform.orderCommissionStatus" placeholder="佣金状态" clearable
                                filterable style="width: 240px">
                                <el-option v-for="dict in orderCommissionStatus" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFormUpdata">提交</el-button>
            </div>
        </el-dialog>
        <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
            <el-form :model="upload" ref="upload" size="small" :inline="true" label-width="100px">
                <el-form-item label="代理商" prop="region">
                    <el-select v-model="upload.downstreamCode" placeholder="请选择代理商" style="width: 200px;">
                        <el-option v-for="dict in searchType" :key="dict.agentCode" :label="dict.agentName"
                            :value="dict.agentCode" />
                    </el-select>
                </el-form-item>
                <el-form-item label="产品">
                    <el-select v-model="upload.productCode" placeholder="产品" clearable filterable style="width: 200px">
                        <el-option v-for="dict in productCodeList " :key="dict.productCode" :label="dict.productName"
                            :value="dict.productCode" />
                    </el-select>
                </el-form-item>
            </el-form>
            <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers" :action="upload.url"
                :disabled="upload.isUploading" :data="upload" :on-progress="handleFileUploadProgress"
                :on-success="handleFileSuccess" :auto-upload="false" drag>
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            </el-upload>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitFileForm">确 定</el-button>
                <el-button @click="upload.open = false">取 消</el-button>
            </div>
        </el-dialog>

        <!-- 照片上传弹窗 -->
        <el-dialog :title="photoUpload.title" :visible.sync="photoUpload.open" width="600px" append-to-body>
            <el-form ref="photoUploadForm" :model="photoUpload.form" :rules="photoUpload.rules" label-width="120px">
                <el-form-item label="订单信息">
                    <el-input v-model="photoUpload.orderInfo" disabled></el-input>
                </el-form-item>
                <!-- 动态显示照片字段 -->
                <el-form-item
                    v-if="shouldShowPhotoField(photoUpload.currentRow, 'idCardFrontUrl')"
                    :label="getPhotoFieldTitle('idCardFrontUrl')"
                    :prop="isPhotoFieldRequired(photoUpload.currentRow, 'idCardFrontUrl') ? 'idCardFrontUrl' : ''">
                    <el-input v-model="photoUpload.form.idCardFrontUrl" :placeholder="'请输入' + getPhotoFieldTitle('idCardFrontUrl')">
                        <el-button slot="append" @click="handleUploadImage('idCardFrontUrl')">上传图片</el-button>
                    </el-input>
                    <div v-if="photoUpload.form.idCardFrontUrl" style="margin-top: 10px;">
                        <el-image :src="photoUpload.form.idCardFrontUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoUpload.form.idCardFrontUrl]"></el-image>
                    </div>
                </el-form-item>
                <el-form-item
                    v-if="shouldShowPhotoField(photoUpload.currentRow, 'idCardBackUrl')"
                    :label="getPhotoFieldTitle('idCardBackUrl')"
                    :prop="isPhotoFieldRequired(photoUpload.currentRow, 'idCardBackUrl') ? 'idCardBackUrl' : ''">
                    <el-input v-model="photoUpload.form.idCardBackUrl" :placeholder="'请输入' + getPhotoFieldTitle('idCardBackUrl')">
                        <el-button slot="append" @click="handleUploadImage('idCardBackUrl')">上传图片</el-button>
                    </el-input>
                    <div v-if="photoUpload.form.idCardBackUrl" style="margin-top: 10px;">
                        <el-image :src="photoUpload.form.idCardBackUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoUpload.form.idCardBackUrl]"></el-image>
                    </div>
                </el-form-item>
                <el-form-item
                    v-if="shouldShowPhotoField(photoUpload.currentRow, 'personPhotoUrl')"
                    :label="getPhotoFieldTitle('personPhotoUrl')"
                    :prop="isPhotoFieldRequired(photoUpload.currentRow, 'personPhotoUrl') ? 'personPhotoUrl' : ''">
                    <el-input v-model="photoUpload.form.personPhotoUrl" :placeholder="'请输入' + getPhotoFieldTitle('personPhotoUrl')">
                        <el-button slot="append" @click="handleUploadImage('personPhotoUrl')">上传图片</el-button>
                    </el-input>
                    <div v-if="photoUpload.form.personPhotoUrl" style="margin-top: 10px;">
                        <el-image :src="photoUpload.form.personPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoUpload.form.personPhotoUrl]"></el-image>
                    </div>
                </el-form-item>
                <el-form-item
                    v-if="shouldShowPhotoField(photoUpload.currentRow, 'customPhotoUrl')"
                    :label="getPhotoFieldTitle('customPhotoUrl')"
                    :prop="isPhotoFieldRequired(photoUpload.currentRow, 'customPhotoUrl') ? 'customPhotoUrl' : ''">
                    <el-input v-model="photoUpload.form.customPhotoUrl" :placeholder="'请输入' + getPhotoFieldTitle('customPhotoUrl') + (isPhotoFieldRequired(photoUpload.currentRow, 'customPhotoUrl') ? '' : '（选填）')">
                        <el-button slot="append" @click="handleUploadImage('customPhotoUrl')">上传图片</el-button>
                    </el-input>
                    <div v-if="photoUpload.form.customPhotoUrl" style="margin-top: 10px;">
                        <el-image :src="photoUpload.form.customPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoUpload.form.customPhotoUrl]"></el-image>
                    </div>
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                    <el-input v-model="photoUpload.form.remark" type="textarea" placeholder="请输入备注信息"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="photoUpload.open = false">取 消</el-button>
                <el-button type="primary" @click="submitPhotoUpload">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 照片审核弹窗 -->
        <el-dialog :title="photoAudit.title" :visible.sync="photoAudit.open" width="960px" append-to-body>
            <el-form ref="photoAuditForm" :model="photoAudit.form" :rules="photoAudit.rules" label-width="120px">
                <el-form-item label="订单信息">
                    <el-input v-model="photoAudit.orderInfo" disabled></el-input>
                </el-form-item>
                <el-form-item label="照片预览">
                    <div style="display: flex; gap: 25px; flex-wrap: wrap;">
                        <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'idCardFrontUrl') && photoAudit.form.idCardFrontUrl">
                            <div style="text-align: center; margin-bottom: 5px;">{{getPhotoFieldTitle('idCardFrontUrl')}}</div>
                            <el-image :src="photoAudit.form.idCardFrontUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoAudit.form.idCardFrontUrl]"></el-image>
                        </div>
                        <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'idCardBackUrl') && photoAudit.form.idCardBackUrl">
                            <div style="text-align: center; margin-bottom: 5px;">{{getPhotoFieldTitle('idCardBackUrl')}}</div>
                            <el-image :src="photoAudit.form.idCardBackUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoAudit.form.idCardBackUrl]"></el-image>
                        </div>
                        <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'personPhotoUrl') && photoAudit.form.personPhotoUrl">
                            <div style="text-align: center; margin-bottom: 5px;">{{getPhotoFieldTitle('personPhotoUrl')}}</div>
                            <el-image :src="photoAudit.form.personPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoAudit.form.personPhotoUrl]"></el-image>
                        </div>
                        <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'customPhotoUrl') && photoAudit.form.customPhotoUrl">
                            <div style="text-align: center; margin-bottom: 5px;">{{getPhotoFieldTitle('customPhotoUrl')}}</div>
                            <el-image :src="photoAudit.form.customPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="[photoAudit.form.customPhotoUrl]"></el-image>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="审核结果" prop="auditAction">
                    <el-radio-group v-model="photoAudit.form.auditAction">
                        <el-radio :label="1">审核通过</el-radio>
                        <el-radio :label="2">审核拒绝</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="审核备注" prop="auditRemark">
                    <el-input v-model="photoAudit.form.auditRemark" type="textarea" :rows="4" placeholder="请输入审核备注"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="photoAudit.open = false">取 消</el-button>
                <el-button type="primary" @click="submitPhotoAudit">确 定</el-button>
            </div>
        </el-dialog>

        <!-- 照片查看弹窗 -->
        <el-dialog :title="photoView.title" :visible.sync="photoView.open" width="700px" append-to-body>
            <div style="text-align: center; margin-bottom: 20px;">
                <strong>{{photoView.orderInfo}}</strong>
            </div>
            <div style="display: flex; gap: 20px; justify-content: center; flex-wrap: wrap;">
                <div v-if="photoView.data.idCardFrontUrl && shouldShowPhotoField(photoView.data, 'idCardFrontUrl')">
                    <div style="text-align: center; margin-bottom: 10px;">
                        <strong>{{getPhotoFieldTitle('idCardFrontUrl')}}</strong>
                        <div v-if="photoView.data.photoAuditTime" style="font-size: 12px; color: #666;">
                            审核时间：{{formatTimestamp(photoView.data.photoAuditTime)}}
                        </div>
                    </div>
                    <el-image :src="photoView.data.idCardFrontUrl" style="width: 160px; height: 100px;" :preview-src-list="getPhotoPreviewList()"></el-image>
                </div>
                <div v-if="photoView.data.idCardBackUrl && shouldShowPhotoField(photoView.data, 'idCardBackUrl')">
                    <div style="text-align: center; margin-bottom: 10px;">
                        <strong>{{getPhotoFieldTitle('idCardBackUrl')}}</strong>
                    </div>
                    <el-image :src="photoView.data.idCardBackUrl" style="width: 160px; height: 100px;" :preview-src-list="getPhotoPreviewList()"></el-image>
                </div>
                <div v-if="photoView.data.personPhotoUrl && shouldShowPhotoField(photoView.data, 'personPhotoUrl')">
                    <div style="text-align: center; margin-bottom: 10px;">
                        <strong>{{getPhotoFieldTitle('personPhotoUrl')}}</strong>
                    </div>
                    <el-image :src="photoView.data.personPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="getPhotoPreviewList()"></el-image>
                </div>
                <div v-if="photoView.data.customPhotoUrl && shouldShowPhotoField(photoView.data, 'customPhotoUrl')">
                    <div style="text-align: center; margin-bottom: 10px;">
                        <strong>{{getPhotoFieldTitle('customPhotoUrl')}}</strong>
                    </div>
                    <el-image :src="photoView.data.customPhotoUrl" style="width: 160px; height: 100px;" :preview-src-list="getPhotoPreviewList()"></el-image>
                </div>
            </div>
            <div v-if="photoView.data.photoAuditRemark" style="margin-top: 20px; padding: 10px; background-color: #f5f5f5; border-radius: 4px;">
                <strong>审核备注：</strong>{{photoView.data.photoAuditRemark}}
            </div>
        </el-dialog>

        <!-- 图片上传组件（隐藏） -->
        <el-upload
            ref="imageUpload"
            :action="upload.imageUploadUrl"
            :headers="upload.headers"
            :show-file-list="false"
            :on-success="handleImageUploadSuccess"
            :before-upload="beforeImageUpload"
            style="display: none;">
        </el-upload>

    </div>
</template>

<script>
    import {
        selectOrderListPage,
        againOrderSubmit,
        selectOrderLogList,
        updateOrderStatus,
        selectProductListPage,
        selectUpstreamApiListPage,
        uploadOrderListExcel,
        exportOrderList,
        selectChildAgentList,
        exportSettlement,
        selectOrderBalance,
        uploadOrderPhotos,
        submitPhotoForAudit,
        auditOrderPhotos,
        getOrderPhotoStatus,
    } from "@/api/monitor/business";
    import { getToken } from "@/utils/auth";
    export default {
        name: "Operlog",
        dicts: ['sys_oper_type', 'sys_common_status'],
        data() {
            return {
                // 用户导入参数
                upload: {
                    // 是否显示弹出层（用户导入）
                    open: false,
                    // 弹出层标题（用户导入）
                    title: "",
                    // 是否禁用上传
                    isUploading: false,
                    // 是否更新已经存在的用户数据
                    updateSupport: 0,
                    // 设置上传的请求头部
                    headers: { Authorization: "Bearer " + getToken() },
                    // 上传的地址
                    url:
                        process.env.VUE_APP_BASE_API + "/order/uploadOrderListExcel",
                    // 图片上传地址
                    imageUploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
                },
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
                downstreamCode: [],
                cambiareform: [],
                upstreamApiCode: [],
                productCodeList: [],
                searchType: [],
                // 导入文件
                openCommission: false,
                registro: false,
                // 是否显示弹出层
                open: false,
                // 日期范围
                dateRange: [],
                // 默认排序
                defaultSort: { prop: 'operTime', order: 'descending' },
                // 表单参数
                form: {

                },
                isNotNullOrderUpstreamId: [
                    {
                        name: "上游单号为空",
                        id: 0
                    },
                    {
                        name: "不为空",
                        id: 1
                    },
                ],
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
                cambiare: false,
                api: [],
                groupCode: [],
                queryParams: {
                    pageNo: 1,
                    pageSize: 10,

                },

                // 照片审核状态选项
                photoStatusOptions: [
                    { name: "无需审核", id: 0 },
                    { name: "待上传照片", id: 1 },
                    { name: "代理商待提交", id: 2 },
                    { name: "管理员待审核", id: 3 },
                    { name: "审核通过", id: 4 },
                    { name: "审核拒绝", id: 5 },
                ],

                // 照片上传弹窗数据
                photoUpload: {
                    open: false,
                    title: "",
                    orderInfo: "",
                    currentFieldType: "",
                    currentRow: null,
                    form: {
                        orderId: null,
                        idCardFrontUrl: "",
                        idCardBackUrl: "",
                        personPhotoUrl: "",
                        customPhotoUrl: "",
                        remark: ""
                    },
                    rules: {
                        idCardFrontUrl: [
                            { required: true, message: "身份证正面照片不能为空", trigger: "blur" }
                        ],
                        idCardBackUrl: [
                            { required: true, message: "身份证反面照片不能为空", trigger: "blur" }
                        ],
                        personPhotoUrl: [
                            { required: true, message: "免冠照片不能为空", trigger: "blur" }
                        ]
                    }
                },

                // 照片审核弹窗数据
                photoAudit: {
                    open: false,
                    title: "",
                    orderInfo: "",
                    currentRow: null,
                    form: {
                        orderId: null,
                        idCardFrontUrl: "",
                        idCardBackUrl: "",
                        personPhotoUrl: "",
                        customPhotoUrl: "",
                        auditAction: 1,
                        auditRemark: ""
                    },
                    rules: {
                        auditAction: [
                            { required: true, message: "请选择审核结果", trigger: "change" }
                        ],
                        auditRemark: [
                            { required: true, message: "审核备注不能为空", trigger: "blur" }
                        ]
                    }
                },

                // 照片查看弹窗数据
                photoView: {
                    open: false,
                    title: "",
                    orderInfo: "",
                    data: {}
                },

                // 图片上传配置
                imageUploadConfig: {
                    currentField: "",
                    onSuccessCallback: null
                },

            };
        },
        created() {
            this.getList();
            selectUpstreamApiListPage({}).then((res) => {
                console.log(res.data);
                this.upstreamApiCode = res.data.rows
            })
            selectChildAgentList({}).then((res) => {
                console.log(res.data);
                this.downstreamCode = res.data
            })
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
            submitFormUpdata() {
                updateOrderStatus(this.cambiareform).then((res) => {
                    this.$message({
                        type: 'success',
                        message: '更新成功!'
                    });
                    this.cambiare = false;
                })
            },
            handleDelete(data) {
                this.cambiare = true;
                this.cambiareform = data;
            },
            submitForm() {

                this.$confirm('确认要重推订单吗？', '推送订单', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    againOrderSubmit(this.form).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '重推成功!'
                        });
                        this.openCommission = false;
                        this.getList();
                    })


                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消重推'
                    });
                });
            },
            handleCommission(data) {
                this.openCommission = true;
                this.form = data;
                selectProductListPage({}).then((res) => {
                    this.productCodeList = res.data.rows;
                })
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
                if (this.dateRange) {
                    if (this.dateRange.length > 0) {
                        this.queryParams.starTime = this.dateRange[0];
                        this.queryParams.endTime = this.dateRange[1];
                    };
                };
                exportSettlement(this.queryParams, `订单数据.csv`, '/order/exportOrderList').then(res => {
                    console.log(res);
                })
            },
            // 文件上传中处理
            handleFileUploadProgress(event, file, fileList) {
                this.upload.isUploading = true;
            },
            // 文件上传成功处理
            handleFileSuccess(response, file, fileList) {
                this.upload.open = false;
                this.upload.isUploading = false;
                this.$refs.upload.clearFiles();
                this.$alert(
                    "<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" +
                    response.message +
                    "</div>",
                    "导入结果",
                    { dangerouslyUseHTMLString: true }
                );
                this.getList();
            },
            // 提交上传文件
            submitFileForm() {
                this.$refs.upload.submit();
            },
            importClick() {
                this.upload.open = true;
                selectProductListPage({pageNo: 1,
                    pageSize: 1000,}).then((res) => {
                    this.productCodeList = res.data.rows;
                })
                selectChildAgentList({}).then((res) => {
                    this.searchType = res.data
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
                this.getList();
            },
            getList() {
                this.queryParams.starTime = undefined;
                this.queryParams.endTime = undefined;
                if (this.dateRange) {
                    if (this.dateRange.length > 0) {
                        this.queryParams.starTime = this.dateRange[0];
                        this.queryParams.endTime = this.dateRange[1];
                    };
                };
                selectOrderListPage(this.queryParams).then((res) => {
                    if (res.data.rows) {
                        this.list = res.data.rows
                    } else {
                        this.list = []
                    }
                    this.total = res.data.totalRows
                })
            },

            // 获取照片审核状态名称
            getPhotoStatusName(status) {
                const statusMap = {
                    0: "无需审核",
                    1: "待上传照片",
                    2: "代理商待提交",
                    3: "管理员待审核",
                    4: "审核通过",
                    5: "审核拒绝"
                };
                return statusMap[status] || "未知状态";
            },

            // 获取照片审核状态标签类型
            getPhotoStatusTagType(status) {
                const typeMap = {
                    0: "info",
                    1: "warning",
                    2: "primary",
                    3: "danger",
                    4: "success",
                    5: "danger"
                };
                return typeMap[status] || "info";
            },

            // 检查是否需要显示照片审核相关信息
            shouldShowPhotoAuditInfo(row) {
                // 只有当照片状态不为null且不为0（无需审核）时才显示
                return row.photoStatus !== undefined && row.photoStatus !== null && row.photoStatus !== 0;
            },

            // 检查是否显示照片审核操作按钮
            shouldShowPhotoAuditButtons(row) {
                return this.shouldShowPhotoAuditInfo(row);
            },

            // 解析产品照片配置
            parsePhotoConfig(photoConfigStr) {
                try {
                    if (!photoConfigStr) {
                        return this.getDefaultPhotoConfig();
                    }
                    return JSON.parse(photoConfigStr);
                } catch (e) {
                    console.error('解析照片配置失败:', e);
                    return this.getDefaultPhotoConfig();
                }
            },

            // 获取默认照片配置
            getDefaultPhotoConfig() {
                return [
                    { photoType: 1, photoTypeName: '身份证正面', required: 1, title: '身份证正面照片', description: '请上传清晰的身份证正面照片' },
                    { photoType: 2, photoTypeName: '身份证反面', required: 1, title: '身份证反面照片', description: '请上传清晰的身份证反面照片' },
                    { photoType: 3, photoTypeName: '免冠照片', required: 1, title: '免冠照片', description: '请上传近期免冠照片' },
                    { photoType: 4, photoTypeName: '自定义照片', required: 0, title: '自定义照片', description: '根据需要上传自定义照片' }
                ];
            },

            // 检查照片字段是否需要显示
            shouldShowPhotoField(row, fieldType) {
                // 如果row为null或undefined，返回默认值
                if (!row || row === null || row === undefined) {
                    return true; // 默认显示所有字段
                }

                // 如果没有照片配置，使用默认逻辑
                if (!row.photoConfig) {
                    return true; // 默认显示所有字段
                }

                const photoConfig = this.parsePhotoConfig(row.photoConfig);

                // 根据字段类型映射到照片类型
                const photoTypeMap = {
                    'idCardFrontUrl': 1,    // 身份证正面
                    'idCardBackUrl': 2,     // 身份证反面
                    'personPhotoUrl': 3,    // 免冠照片
                    'customPhotoUrl': 4     // 自定义照片
                };

                const photoType = photoTypeMap[fieldType];
                if (!photoType) {
                    return true; // 未知字段默认显示
                }

                // 查找对应的配置项
                const configItem = photoConfig.find(item => item.photoType === photoType);

                // 如果配置不存在或者required不为0，则显示该字段
                return !configItem || configItem.required !== 0;
            },

            // 获取照片字段标题
            getPhotoFieldTitle(fieldType) {
                const titleMap = {
                    'idCardFrontUrl': '身份证正面照片',
                    'idCardBackUrl': '身份证反面照片',
                    'personPhotoUrl': '免冠照片',
                    'customPhotoUrl': '自定义照片'
                };
                return titleMap[fieldType] || '照片';
            },

            // 获取照片字段是否必填
            isPhotoFieldRequired(row, fieldType) {
                // 如果row为null或undefined，返回默认值
                if (!row || row === null || row === undefined) {
                    // 默认规则：自定义照片不是必填的
                    return fieldType !== 'customPhotoUrl';
                }

                if (!row.photoConfig) {
                    // 默认规则：自定义照片不是必填的
                    return fieldType !== 'customPhotoUrl';
                }

                const photoConfig = this.parsePhotoConfig(row.photoConfig);
                const photoTypeMap = {
                    'idCardFrontUrl': 1,
                    'idCardBackUrl': 2,
                    'personPhotoUrl': 3,
                    'customPhotoUrl': 4
                };

                const photoType = photoTypeMap[fieldType];
                if (!photoType) {
                    return false;
                }

                const configItem = photoConfig.find(item => item.photoType === photoType);
                return configItem ? configItem.required === 1 : false;
            },

            // 处理照片上传
            handlePhotoUpload(row) {
                this.photoUpload.title = "上传订单照片";
                this.photoUpload.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
                this.photoUpload.currentRow = row; // 设置当前行数据，用于动态显示字段
                this.photoUpload.form.orderId = row.orderId;
                this.photoUpload.form.idCardFrontUrl = row.idCardFrontUrl || "";
                this.photoUpload.form.idCardBackUrl = row.idCardBackUrl || "";
                this.photoUpload.form.personPhotoUrl = row.personPhotoUrl || "";
                this.photoUpload.form.customPhotoUrl = row.customPhotoUrl || "";
                this.photoUpload.form.remark = "";

                // 根据配置动态设置验证规则
                this.updatePhotoUploadRules(row);

                this.photoUpload.open = true;
            },

            // 根据照片配置更新验证规则
            updatePhotoUploadRules(row) {
                const rules = {};

                if (this.shouldShowPhotoField(row, 'idCardFrontUrl') && this.isPhotoFieldRequired(row, 'idCardFrontUrl')) {
                    rules.idCardFrontUrl = [
                        { required: true, message: "身份证正面照片不能为空", trigger: "blur" }
                    ];
                }

                if (this.shouldShowPhotoField(row, 'idCardBackUrl') && this.isPhotoFieldRequired(row, 'idCardBackUrl')) {
                    rules.idCardBackUrl = [
                        { required: true, message: "身份证反面照片不能为空", trigger: "blur" }
                    ];
                }

                if (this.shouldShowPhotoField(row, 'personPhotoUrl') && this.isPhotoFieldRequired(row, 'personPhotoUrl')) {
                    rules.personPhotoUrl = [
                        { required: true, message: "免冠照片不能为空", trigger: "blur" }
                    ];
                }

                if (this.shouldShowPhotoField(row, 'customPhotoUrl') && this.isPhotoFieldRequired(row, 'customPhotoUrl')) {
                    rules.customPhotoUrl = [
                        { required: true, message: "自定义照片不能为空", trigger: "blur" }
                    ];
                }

                this.photoUpload.rules = rules;
            },

            // 处理提交照片审核
            handleSubmitPhoto(row) {
                this.$confirm('确认要提交此订单的照片进行审核吗？', '提交审核', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    submitPhotoForAudit({
                        orderId: row.orderId,
                        idCardFrontUrl: row.idCardFrontUrl,
                        idCardBackUrl: row.idCardBackUrl,
                        personPhotoUrl: row.personPhotoUrl,
                        customPhotoUrl: row.customPhotoUrl,
                        remark: "代理商提交审核"
                    }).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '提交审核成功!'
                        });
                        this.getList();
                    }).catch(() => {
                        this.$message({
                            type: 'error',
                            message: '提交审核失败!'
                        });
                    });
                });
            },

            // 处理照片审核
            handlePhotoAudit(row) {
                this.photoAudit.title = "审核订单照片";
                this.photoAudit.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
                this.photoAudit.currentRow = row;
                this.photoAudit.form.orderId = row.orderId;
                this.photoAudit.form.idCardFrontUrl = row.idCardFrontUrl;
                this.photoAudit.form.idCardBackUrl = row.idCardBackUrl;
                this.photoAudit.form.personPhotoUrl = row.personPhotoUrl;
                this.photoAudit.form.customPhotoUrl = row.customPhotoUrl;
                this.photoAudit.form.auditAction = 1;
                this.photoAudit.form.auditRemark = "";
                this.photoAudit.open = true;
            },

            // 处理查看照片
            handleViewPhotos(row) {
                this.photoView.title = "查看订单照片";
                this.photoView.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
                this.photoView.data = {
                    ...row,
                    idCardFrontUrl: row.idCardFrontUrl,
                    idCardBackUrl: row.idCardBackUrl,
                    personPhotoUrl: row.personPhotoUrl,
                    customPhotoUrl: row.customPhotoUrl,
                    photoAuditTime: row.photoAuditTime,
                    photoAuditRemark: row.photoAuditRemark
                };
                this.photoView.open = true;
            },

            // 获取照片预览列表
            getPhotoPreviewList() {
                const photos = [];
                if (this.photoView.data.idCardFrontUrl) photos.push(this.photoView.data.idCardFrontUrl);
                if (this.photoView.data.idCardBackUrl) photos.push(this.photoView.data.idCardBackUrl);
                if (this.photoView.data.personPhotoUrl) photos.push(this.photoView.data.personPhotoUrl);
                if (this.photoView.data.customPhotoUrl) photos.push(this.photoView.data.customPhotoUrl);
                return photos;
            },

            // 提交照片上传
            submitPhotoUpload() {
                this.$refs.photoUploadForm.validate((valid) => {
                    if (valid) {
                        uploadOrderPhotos(this.photoUpload.form).then((res) => {
                            this.$message({
                                type: 'success',
                                message: '照片上传成功!'
                            });
                            this.photoUpload.open = false;
                            this.getList();
                        }).catch(() => {
                            this.$message({
                                type: 'error',
                                message: '照片上传失败!'
                            });
                        });
                    }
                });
            },

            // 提交照片审核
            submitPhotoAudit() {
                this.$refs.photoAuditForm.validate((valid) => {
                    if (valid) {
                        auditOrderPhotos(this.photoAudit.form).then((res) => {
                            this.$message({
                                type: 'success',
                                message: '照片审核完成!'
                            });
                            this.photoAudit.open = false;
                            this.getList();
                        }).catch(() => {
                            this.$message({
                                type: 'error',
                                message: '照片审核失败!'
                            });
                        });
                    }
                });
            },

            // 处理上传图片
            handleUploadImage(fieldType) {
                this.imageUploadConfig.currentField = fieldType;
                // 触发隐藏的文件上传组件
                this.$refs.imageUpload.$el.querySelector('input[type="file"]').click();
            },

            // 图片上传成功回调
            handleImageUploadSuccess(response, file, fileList) {
                if (response.code === 200) {
                    // 根据项目标准格式，上传成功返回的是 {code: 200, fileName: "xxx.jpg", url: "xxx.jpg"}
                    const url = response.url || response.fileName || response.data?.url;
                    this.photoUpload.form[this.imageUploadConfig.currentField] = url;
                    this.$message.success('图片上传成功');
                } else {
                    this.$message.error(response.msg || '图片上传失败');
                }
            },

            // 图片上传前校验
            beforeImageUpload(file) {
                const isJPG = file.type === 'image/jpeg';
                const isPNG = file.type === 'image/png';
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isJPG && !isPNG) {
                    this.$message.error('上传图片只能是 JPG/PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 2MB!');
                }
                return (isJPG || isPNG) && isLt2M;
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
