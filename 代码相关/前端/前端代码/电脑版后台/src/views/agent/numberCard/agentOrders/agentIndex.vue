<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
             label-width="100px">
      <el-form-item prop="businessType">
        <el-select v-model="queryParams.orderSource" placeholder="订单来源" clearable filterable
                   style="width: 240px">
          <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="businessType">
        <el-select v-model="queryParams.orderStatus" placeholder="订单状态" clearable filterable
                   style="width: 240px">
          <el-option v-for="dict in orderStatus" :key="dict.id" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="businessType">
        <el-select v-model="queryParams.isRecharged" placeholder="首充状态" clearable filterable
                   style="width: 240px">
          <el-option v-for="dict in isRecharged" :key="dict.id" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item prop="responsiblePeople">
        <el-input v-model="queryParams.orderUpstreamId" placeholder="系统订单号"></el-input>
      </el-form-item>
      <el-form-item prop="responsiblePeople">
        <el-input v-model="queryParams.orderDownstreamId" placeholder="下游订单号"></el-input>
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
      <el-form-item prop="businessType">
        <el-select v-model="queryParams.downstreamCode" placeholder="下游代理" clearable filterable>
          <el-option v-for="dict in downstreamCode" :key="dict.agentCode" :label="dict.agentName"
                     :value="dict.agentCode" />
        </el-select>
      </el-form-item>
      <el-form-item prop="businessType">
        <el-select v-model="queryParams.orderCommissionStatus" placeholder="佣金状态" clearable filterable>
          <el-option v-for="dict in orderCommissionStatus" :key="dict.id" :label="dict.name"
                     :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select v-model="queryParams.photoStatus" placeholder="照片审核状态" clearable filterable>
          <el-option v-for="dict in photoStatusOptions" :key="dict.id" :label="dict.name" :value="dict.id" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="timestamp" type="daterange"
                        range-separator="至" start-placeholder="下单时间" end-placeholder="结束日期"
                        :default-time="['00:00:00', '23:59:59']"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button> -->
      </el-form-item>
    </el-form>
    <el-row :gutter="10" class="mb8">
    </el-row>
    <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
              :tree-props="{ children: 'children' }" height="550" :row-class-name="tableRowClassName"
              @selection-change="handleSelectionChange">
      <el-table-column label="订单信息" align="left" prop="companyName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div class="order-info">
            <div class="order-info__row order-info__row--two-cols">
              <span>
                来源：
                <span v-if="scope.row.orderSource==0">信息流</span>
                <span v-if="scope.row.orderSource==1">合作方API进单</span>
                <span v-if="scope.row.orderSource==2">导单</span>
                <span v-if="scope.row.orderSource==2">重推</span>
              </span>
              <span>代理商名称：{{ scope.row.showDownstreamName }}</span>
            </div>
            <div class="order-info__row">
              <span>订单ID：{{ scope.row.orderId }}</span>
            </div>
<!--            <div class="order-info__row">-->
<!--              <span>系统订单号：{{ scope.row.orderUpstreamId }}</span>-->
<!--            </div>-->
            <div class="order-info__row">
              <span>产品名称：{{ scope.row.productName }}</span>
            </div>
            <div class="order-info__row" v-if="scope.row.photoStatus !== undefined && scope.row.photoStatus !== null && scope.row.photoStatus !== 0">
              照片审核：
              <el-tag :type="getPhotoStatusTagType(scope.row.photoStatus)" size="mini">
                {{ scope.row.photoStatusName || getPhotoStatusName(scope.row.photoStatus) }}
              </el-tag>
            </div>
            <div class="order-info__row">
              <span>运营商：
                <span v-if="scope.row.operatorType==0">中国移动</span>
                <span v-if="scope.row.operatorType==1">中国电信</span>
                <span v-if="scope.row.operatorType==2">中国联通</span>
                <span v-if="scope.row.operatorType==3">中国广电</span>
              </span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="开卡人信息" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <span>证件姓名: {{scope.row.cardName}}</span><br>
          <span>证件号码：{{scope.row.cardId}}</span><br>
          <span>联系电话：{{scope.row.cardPhone}}</span><br>
          <span>收货地址：{{scope.row.provinceName}}-{{scope.row.cityName}}-{{scope.row.countyName}}-{{scope.row.cardAddress}}</span><br>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <div class="order-status-grid">
            <div class="order-status-item">
              <span class="order-status-label">结算模式：</span>
              <span class="order-status-value">{{ formatProductType(scope.row.productType) }}</span>
            </div>
            <div class="order-status-item">
              <span class="order-status-label">订单状态：</span>
              <span class="order-status-value">{{ mapDictLabel(orderStatus, scope.row.orderStatus) }}</span>
            </div>
            <div class="order-status-item">
              <span class="order-status-label">是否首充：</span>
              <span class="order-status-value">{{ mapDictLabel(isRecharged, scope.row.isRecharged) }}</span>
            </div>
            <div class="order-status-item">
              <span class="order-status-label">首充金额：</span>
              <span class="order-status-value">{{ formatDisplay(scope.row.rechargeAmount) }}</span>
            </div>
            <div class="order-status-item">
              <span class="order-status-label">佣金状态：</span>
              <span class="order-status-value">{{ mapDictLabel(orderCommissionStatus, scope.row.orderCommissionStatus) }}</span>
            </div>
            <div class="order-status-item order-status-item--full">
              <span class="order-status-label">佣金说明：</span>
              <span class="order-status-value">{{ formatDisplay(scope.row.orderCommissionMessage) }}</span>
            </div>
            <div class="order-status-item order-status-item--full">
              <span class="order-status-label">下单时间：</span>
              <span class="order-status-value">{{ formatTimestamp(scope.row.createTime) }}</span>
            </div>
          </div>
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
      <el-table-column align="center" label="照片操作" width="160" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <template v-if="scope.row.photoRequired !== undefined && scope.row.photoRequired !== null && scope.row.photoRequired == 1">
            <div>
              <el-button v-if="hasAnyPhotos(scope.row)" @click="handleViewPhotos(scope.row)" type="text" size="small">查看照片</el-button>
              <el-button @click="handlePhotoUpload(scope.row)" type="text" size="small">上传照片</el-button>
              <el-button @click="handleCopyEditLink(scope.row)" type="text" size="small">复制证件上传链接</el-button><br>
            </div>
            <div v-if="scope.row.sfxysh == 1">
              <div v-if="scope.row.photoStatus === 2">
                <el-button @click="handleSubmitPhoto(scope.row)" type="text" size="small" style="color: #E6A23C">提交审核</el-button><br>
              </div>
              <div v-if="scope.row.photoStatus === 3">
                <el-button @click="handlePhotoAudit(scope.row)" type="text" size="small" style="color: #F56C6C">审核照片</el-button><br>
              </div>
            </div>
          </template>
        </template>
      </el-table-column>
      <!-- <el-table-column label="接口" align="left" prop="companySimpleName" :show-overflow-tooltip="true">
          <template slot-scope="scope">
              <span>接口: {{scope.row.upstreamApiName}}</span><br>
              <span>接口产品：{{scope.row.upstreamProductName }}</span><br>
              <span>接口订单号：{{ scope.row.orderUpstreamId}}</span><br>
              <span>状态：{{scope.row.upstreamOrderStatusMessage}}</span><br>
          </template>
      </el-table-column> -->
      <!-- <el-table-column align="center" label="操作" width="100" class-name="small-padding fixed-width">
          <template slot-scope="scope">
              <el-button @click="handleCommission(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:edit']">重新下单</el-button>
              <el-button @click="registroOP(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:remove']">日志</el-button>
              <el-button @click="handleDelete(scope.row)" type="text" size="small" v-hasPermi="['channel:channelManagement:remove']">更改状态</el-button>
          </template>
      </el-table-column> -->
    </el-table>
    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNo" :limit.sync="queryParams.pageSize"
                @pagination="getList" />
    <el-dialog :visible.sync="openCommission" width="550px" append-to-body>
      <el-form ref="form" v-model="form" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item prop="businessType" label="原产品">
              <el-input v-model="form.productName" style="width: 240px" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item prop="businessType" label="新产品">
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
    <el-dialog :visible.sync="registro" width="550px" append-to-body>

      <el-table ref="tables" :data="listRegistro" row-key="oper" border lazy height="550">

        <el-table-column label="ID" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />
        <el-table-column label="时间" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />
        <el-table-column label="内容" align="center" prop="companySimpleName" :show-overflow-tooltip="true" />

      </el-table>

    </el-dialog>
    <el-dialog :visible.sync="cambiare" width="550px" append-to-body>
      <el-form ref="cambiareform" v-model="cambiareform" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item prop="businessType" label="订单来源">
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
            <el-form-item prop="businessType" label="订单状态">
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

    <!-- 照片上传、审核、查看弹窗与上传组件 -->
    <el-dialog :title="photoUpload.title" :visible.sync="photoUpload.open" width="600px" append-to-body>
      <el-form ref="photoUploadForm" :model="photoUpload.form" :rules="photoUpload.rules" label-width="120px">
        <el-form-item label="订单信息">
          <el-input v-model="photoUpload.orderInfo" disabled></el-input>
        </el-form-item>
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
        <el-form-item label="备注">
          <el-input v-model="photoUpload.form.remark" type="textarea" placeholder="请输入备注信息"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="photoUpload.open = false">取 消</el-button>
        <el-button type="primary" @click="submitPhotoUpload">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="photoAudit.title" :visible.sync="photoAudit.open" width="1024px" append-to-body>
      <el-form ref="photoAuditForm" :model="photoAudit.form" :rules="photoAudit.rules" label-width="120px">
        <el-form-item label="订单信息">
          <el-input v-model="photoAudit.orderInfo" disabled></el-input>
        </el-form-item>
        <div class="photo-view-container">
          <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'idCardFrontUrl') && photoAudit.form.idCardFrontUrl" class="photo-item">
            <div class="photo-image-wrapper">
              <el-image :src="photoAudit.form.idCardFrontUrl" class="photo-image-audit" :preview-src-list="[photoAudit.form.idCardFrontUrl]"></el-image>
            </div>
            <div class="photo-info-card">
              <div class="photo-title">{{getPhotoFieldTitle('idCardFrontUrl')}}</div>
            </div>
          </div>
          <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'idCardBackUrl') && photoAudit.form.idCardBackUrl" class="photo-item">
            <div class="photo-image-wrapper">
              <el-image :src="photoAudit.form.idCardBackUrl" class="photo-image-audit" :preview-src-list="[photoAudit.form.idCardBackUrl]"></el-image>
            </div>
            <div class="photo-info-card">
              <div class="photo-title">{{getPhotoFieldTitle('idCardBackUrl')}}</div>
            </div>
          </div>
          <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'personPhotoUrl') && photoAudit.form.personPhotoUrl" class="photo-item">
            <div class="photo-image-wrapper">
              <el-image :src="photoAudit.form.personPhotoUrl" class="photo-image-audit" :preview-src-list="[photoAudit.form.personPhotoUrl]"></el-image>
            </div>
            <div class="photo-info-card">
              <div class="photo-title">{{getPhotoFieldTitle('personPhotoUrl')}}</div>
            </div>
          </div>
          <div v-if="shouldShowPhotoField(photoAudit.currentRow, 'customPhotoUrl') && photoAudit.form.customPhotoUrl" class="photo-item">
            <div class="photo-image-wrapper">
              <el-image :src="photoAudit.form.customPhotoUrl" class="photo-image-audit" :preview-src-list="[photoAudit.form.customPhotoUrl]"></el-image>
            </div>
            <div class="photo-info-card">
              <div class="photo-title">{{getPhotoFieldTitle('customPhotoUrl')}}</div>
            </div>
          </div>
        </div>
        <el-form-item label="审核结果" prop="auditAction">
          <el-radio-group v-model="photoAudit.form.auditAction">
            <el-radio :label="1">审核通过</el-radio>
            <el-radio :label="2">审核拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核备注" prop="auditRemark">
          <el-input v-model="photoAudit.form.auditRemark" type="textarea" placeholder="请输入审核备注"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="photoAudit.open = false">取 消</el-button>
        <el-button type="primary" @click="submitPhotoAudit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="photoView.title" :visible.sync="photoView.open" width="1024px" append-to-body>
      <div class="photo-view-header">
        <strong>{{photoView.orderInfo}}</strong>
      </div>
      <div class="photo-view-container">
        <div v-if="photoView.data.idCardFrontUrl && shouldShowPhotoField(photoView.data, 'idCardFrontUrl')" class="photo-item">
          <div class="photo-image-wrapper">
            <el-image :src="photoView.data.idCardFrontUrl" class="photo-image" :preview-src-list="getPhotoPreviewList()"></el-image>
          </div>
          <div class="photo-info-card">
            <div class="photo-title">{{getPhotoFieldTitle('idCardFrontUrl')}}</div>
          </div>
        </div>
        <div v-if="photoView.data.idCardBackUrl && shouldShowPhotoField(photoView.data, 'idCardBackUrl')" class="photo-item">
          <div class="photo-image-wrapper">
            <el-image :src="photoView.data.idCardBackUrl" class="photo-image" :preview-src-list="getPhotoPreviewList()"></el-image>
          </div>
          <div class="photo-info-card">
            <div class="photo-title">{{getPhotoFieldTitle('idCardBackUrl')}}</div>
          </div>
        </div>
        <div v-if="photoView.data.personPhotoUrl && shouldShowPhotoField(photoView.data, 'personPhotoUrl')" class="photo-item">
          <div class="photo-image-wrapper">
            <el-image :src="photoView.data.personPhotoUrl" class="photo-image" :preview-src-list="getPhotoPreviewList()"></el-image>
          </div>
          <div class="photo-info-card">
            <div class="photo-title">{{getPhotoFieldTitle('personPhotoUrl')}}</div>
          </div>
        </div>
        <div v-if="photoView.data.customPhotoUrl && shouldShowPhotoField(photoView.data, 'customPhotoUrl')" class="photo-item">
          <div class="photo-image-wrapper">
            <el-image :src="photoView.data.customPhotoUrl" class="photo-image" :preview-src-list="getPhotoPreviewList()"></el-image>
          </div>
          <div class="photo-info-card">
            <div class="photo-title">{{getPhotoFieldTitle('customPhotoUrl')}}</div>
          </div>
        </div>
      </div>
      <div v-if="photoView.data.photoStatus == 4 || photoView.data.photoStatus == 5" class="photo-audit-remark">
        <div class="remark-content">
          <strong>审核备注：</strong>{{photoView.data.photoAuditRemark}}
        </div>
        <div v-if="photoView.data.photoAuditTime" class="photo-audit-time">
          审核时间：{{formatTimestamp(photoView.data.photoAuditTime)}}
        </div>
      </div>
    </el-dialog>

    <el-upload
      ref="imageUpload"
      :action="photoImageUploadConfig.action"
      :headers="photoImageUploadConfig.headers"
      :show-file-list="false"
      :on-success="handleImageUploadSuccess"
      :before-upload="beforeImageUpload"
      style="display: none;">
    </el-upload>
  </div>
</template>

<script>
import { againOrderSubmit, selectOrderLogList, updateOrderStatus, selectUpstreamApiListPage,selectChildAgentList } from "@/api/monitor/business";
import {agentSelectOrderListPage } from "@/api/monitor/daili";
import photoAuditMixin from "@/views/agent/numberCard/mixins/photoAudit";
export default {
  name: "Operlog",
  dicts: ['sys_oper_type', 'sys_common_status'],
  mixins: [photoAuditMixin],
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
      downstreamCode:[],
      listRegistro: [],
      cambiareform: [],
      upstreamApiCode: [],
      productCodeList: [],
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
        orderUpstreamId: undefined,
        orderDownstreamId: undefined,
        orderType: 1, // 1-代理商订单（排除自己的数据）
        photoStatus: undefined,
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
      if (timestamp === null || timestamp === undefined) {
        return "--";
      }
      const date = new Date(timestamp);
      if (Number.isNaN(date.getTime())) {
        return "--";
      }
      const year = date.getFullYear();
      const month = ("0" + (date.getMonth() + 1)).slice(-2);
      const day = ("0" + date.getDate()).slice(-2);
      const hours = ("0" + date.getHours()).slice(-2);
      const minutes = ("0" + date.getMinutes()).slice(-2);
      const seconds = ("0" + date.getSeconds()).slice(-2);
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    },
    formatDisplay(value) {
      return value === 0 ? 0 : (value || "--");
    },
    mapDictLabel(list, value) {
      if (!Array.isArray(list)) {
        return "--";
      }
      const match = list.find((item) => Number(item.id) === Number(value));
      return match ? match.name : "--";
    },
    formatProductType(type) {
      const mapping = {
        0: "日结秒返",
        1: "月结产品",
        2: "长期产品",
        3: "其它",
        4: "组合返佣",
        5: "付费提卡"
      };
      return mapping[type] || "--";
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
    // handleCommission(data) {
    //     this.openCommission = true;
    //     this.form = data;
    //     selectProductListPage({}).then((res) => {
    //         this.productCodeList = res.data.rows;
    //     })
    // },
    handleAdd() {

    },
    registroOP(data) {
      this.registro = true;

      selectOrderLogList(data.orderId).then((res) => {
        if (res.data.rows) {
          this.listRegistro = res.data.rows
        } else {
          this.listRegistro = []
        }

      })
    },
    resetQuery() {

    },
    handleImport() {

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
      agentSelectOrderListPage(this.queryParams).then((res) => {
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

.order-status-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  column-gap: 16px;
  row-gap: 6px;
}

.order-status-item {
  display: flex;
  flex-wrap: wrap;
  font-size: 12px;
  line-height: 1.5;
}

.order-status-label {
  color: #606266;
  margin-right: 4px;
  white-space: nowrap;
}

.order-status-value {
  word-break: break-word;
}

.order-status-item--full {
  grid-column: 1 / -1;
}

.order-info__row {
  margin-bottom: 4px;
}

.order-info__row:last-child {
  margin-bottom: 0;
}

.order-info__row--two-cols {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  align-items: flex-start;
}

.order-info__row--two-cols > span {
  flex: 1 1 45%;
}

.photo-view-header {
  text-align: center;
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f8f9fa;
  border-radius: 6px;
}

.photo-view-header strong {
  font-size: 16px;
  color: #303133;
}

.photo-view-container {
  display: flex;
  gap: 50px;
  flex-wrap: wrap;
  justify-content: center;
  margin-bottom: 30px;
}

.photo-item {
  text-align: center;
}

.photo-image-wrapper {
  border-radius: 8px 8px 0 0;
  overflow: hidden;
}

.photo-image,
.photo-image-audit {
  width: 240px;
  height: 150px;
  border-radius: 0;
  box-shadow: none;
  display: block;
}

.photo-info-card {
  padding: 12px;
  background-color: #fff;
  border-radius: 0 0 8px 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.08);
}

.photo-title {
  font-size: 16px;
  color: #303133;
  font-weight: 500;
  margin-bottom: 6px;
}

.photo-audit-time {
  font-size: 13px;
  color: #909399;
  margin-top: 3px;
}

.photo-audit-remark {
  margin-top: 25px;
  padding: 18px;
  background-color: #f5f7fa;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.remark-content {
  font-size: 14px;
  color: #606266;
  line-height: 1.6;
}

.remark-content strong {
  color: #303133;
  font-size: 15px;
}
</style>
