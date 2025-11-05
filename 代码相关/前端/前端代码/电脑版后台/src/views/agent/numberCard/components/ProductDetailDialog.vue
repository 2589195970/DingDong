<template>
  <el-dialog
    :visible.sync="internalVisible"
    width="900px"
    append-to-body
    :title="dialogTitle"
    @close="handleClose">
    <div class="product-detail-dialog__section">
      <el-descriptions title="基础信息" :column="2" size="small" border>
        <el-descriptions-item label="产品名称">{{ display(effectiveProduct.productName) }}</el-descriptions-item>
        <el-descriptions-item label="产品编码">{{ display(effectiveProduct.productCode) }}</el-descriptions-item>
        <el-descriptions-item label="运营商">{{ formatOperator(effectiveProduct.operatorType) }}</el-descriptions-item>
        <el-descriptions-item label="结算模式">{{ formatProductType(effectiveProduct.productType) }}</el-descriptions-item>
        <el-descriptions-item label="归属地区">{{ display(effectiveProduct.productGsdq) }}</el-descriptions-item>
        <el-descriptions-item label="推广要求">{{ display(effectiveProduct.productDemand) }}</el-descriptions-item>
        <el-descriptions-item label="通用流量">{{ display(effectiveProduct.productTyll, 'GB') }}</el-descriptions-item>
        <el-descriptions-item label="定向流量">{{ display(effectiveProduct.productDxll, 'GB') }}</el-descriptions-item>
        <el-descriptions-item label="通话分钟">{{ display(effectiveProduct.productThfz, '分钟') }}</el-descriptions-item>
        <el-descriptions-item label="合约期限">{{ display(effectiveProduct.productHyqx) }}</el-descriptions-item>
        <el-descriptions-item label="年龄限制">
          {{ formatAge(effectiveProduct.productAgeMin, effectiveProduct.productAgeMax) }}
        </el-descriptions-item>
        <el-descriptions-item label="排序">{{ effectiveProduct.productSort ?? '--' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ formatStatus(effectiveProduct.productStatus) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ formatTimestamp(effectiveProduct.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ formatTimestamp(effectiveProduct.updateTime) }}</el-descriptions-item>
        <el-descriptions-item label="上架时间">{{ formatTimestamp(effectiveProduct.shelfTime) }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="product-detail-dialog__section">
      <el-descriptions title="销售配置" :column="2" size="small" border>
        <el-descriptions-item label="产品佣金(元)">{{ Number(effectiveProduct.sfyjfx) === 0 ? 0 : formatAmount(effectiveProduct.productCommission) }}</el-descriptions-item>
        <el-descriptions-item label="VIP固定加成(元)">
          {{ effectiveProduct.sfyjfx ? formatAmount(effectiveProduct.vipFixedCommission) : 0 }}
        </el-descriptions-item>
        <el-descriptions-item label="佣金返现">{{ formatYesNo(effectiveProduct.sfyjfx) }}</el-descriptions-item>
        <el-descriptions-item label="付费提卡">{{ formatYesNo(Number(effectiveProduct.productType) === paidCardProductType ? 1 : 0) }}</el-descriptions-item>
        <el-descriptions-item label="基础提卡费(元)">{{ formatAmount(effectiveProduct.baseCardFee) }}</el-descriptions-item>
        <el-descriptions-item label="初始话费余额(元)">{{ formatAmount(effectiveProduct.productInitialBalance) }}</el-descriptions-item>
        <el-descriptions-item label="上级成本(元)">{{ formatAmount(effectiveProduct.incomingCardFee) }}</el-descriptions-item>
        <el-descriptions-item label="下级卖价(元)">{{ formatAmount(effectiveProduct.downstreamCardFee) }}</el-descriptions-item>
        <el-descriptions-item label="提卡利润(元)">{{ formatAmount(effectiveProduct.cardFeeProfit) }}</el-descriptions-item>
        <el-descriptions-item label="余额配置">{{ effectiveProduct.balanceConfig ?? '--' }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="product-detail-dialog__section">
      <el-descriptions title="上游对接" :column="2" size="small" border>
        <el-descriptions-item label="上游API名称">{{ display(effectiveProduct.upstreamApiName) }}</el-descriptions-item>
        <el-descriptions-item label="上游API编码">{{ display(effectiveProduct.upstreamApiCode) }}</el-descriptions-item>
        <el-descriptions-item label="上游产品名称">{{ display(effectiveProduct.upstreamProductName) }}</el-descriptions-item>
        <el-descriptions-item label="上游产品编码">{{ display(effectiveProduct.upstreamProductCode) }}</el-descriptions-item>
        <el-descriptions-item label="是否分单">{{ formatYesNo(effectiveProduct.isDispatchUpstreamApi) }}</el-descriptions-item>
        <el-descriptions-item label="全部代理可见">{{ formatYesNo(effectiveProduct.isAllAgent) }}</el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="product-detail-dialog__section" v-if="effectiveProduct.productTemplateJson">
      <el-descriptions title="模板配置" :column="1" size="small" border>
        <el-descriptions-item label="模板JSON">
          <el-input
            type="textarea"
            :value="prettyTemplateJson"
            :rows="6"
            readonly />
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <div class="product-detail-dialog__section product-detail-dialog__images">
      <div>
        <div class="product-detail-dialog__images-title">产品主图</div>
        <el-image
          :src="effectiveProduct.productMasterMap"
          fit="contain"
          lazy
          :preview-src-list="previewList(effectiveProduct.productMasterMap)">
          <div slot="error" class="image-slot">暂无</div>
        </el-image>
      </div>
      <div>
        <div class="product-detail-dialog__images-title">产品详情图</div>
        <el-image
          :src="effectiveProduct.productDetailMap"
          fit="contain"
          lazy
          :preview-src-list="previewList(effectiveProduct.productDetailMap)">
          <div slot="error" class="image-slot">暂无</div>
        </el-image>
      </div>
      <div>
        <div class="product-detail-dialog__images-title">产品海报图</div>
        <el-image
          :src="effectiveProduct.productPlacardMap"
          fit="contain"
          lazy
          :preview-src-list="previewList(effectiveProduct.productPlacardMap)">
          <div slot="error" class="image-slot">暂无</div>
        </el-image>
      </div>
      <div>
        <div class="product-detail-dialog__images-title">产品二维码</div>
        <el-image
          :src="effectiveProduct.productQrcodeMap"
          fit="contain"
          lazy
          :preview-src-list="previewList(effectiveProduct.productQrcodeMap)">
          <div slot="error" class="image-slot">暂无</div>
        </el-image>
      </div>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="handleClose">关闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'ProductDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    product: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      internalVisible: false,
      paidCardProductType: 5
    };
  },
  computed: {
    effectiveProduct() {
      return this.product || {};
    },
    dialogTitle() {
      const name = this.effectiveProduct.productName;
      return name ? `产品详情 - ${name}` : '产品详情';
    },
    prettyTemplateJson() {
      try {
        const templateJson = this.effectiveProduct.productTemplateJson;
        if (!templateJson) {
          return '';
        }
        const parsed = JSON.parse(templateJson);
        return JSON.stringify(parsed, null, 2);
      } catch (e) {
        return this.effectiveProduct.productTemplateJson || '';
      }
    }
  },
  watch: {
    visible: {
      immediate: true,
      handler(val) {
        this.internalVisible = val;
      }
    }
  },
  methods: {
    handleClose() {
      this.internalVisible = false;
      this.$emit('update:visible', false);
    },
    previewList(url) {
      return url ? [url] : [];
    },
    display(value, unit) {
      if (value === null || value === undefined || value === '') {
        return '--';
      }
      return unit ? `${value}${unit}` : value;
    },
    formatAmount(value) {
      const parsed = Number(value);
      if (Number.isNaN(parsed)) {
        return '--';
      }
      return parsed;
    },
    formatYesNo(value) {
      const isTrue = Number(value) === 1;
      return isTrue ? '是' : '否';
    },
    formatOperator(value) {
      const map = {
        0: '中国移动',
        1: '中国电信',
        2: '中国联通',
        3: '中国广电'
      };
      return map[value] || '--';
    },
    formatProductType(value) {
      const map = {
        0: '日结秒返',
        1: '月结产品',
        2: '长期产品',
        3: '其它',
        4: '组合返佣',
        5: '付费提卡'
      };
      return map[value] || '--';
    },
    formatStatus(value) {
      const map = {
        0: '已下架',
        1: '上架中'
      };
      return map[value] || '--';
    },
    formatAge(min, max) {
      const hasMin = min !== null && min !== undefined;
      const hasMax = max !== null && max !== undefined;
      if (!hasMin && !hasMax) {
        return '--';
      }
      if (hasMin && hasMax) {
        return `${min} ~ ${max} 岁`;
      }
      if (hasMin) {
        return `≥ ${min} 岁`;
      }
      return `≤ ${max} 岁`;
    },
    formatTimestamp(timestamp) {
      if (!timestamp) {
        return '--';
      }
      const date = new Date(Number(timestamp));
      if (Number.isNaN(date.getTime())) {
        return '--';
      }
      const pad = (num) => String(num).padStart(2, '0');
      const y = date.getFullYear();
      const m = pad(date.getMonth() + 1);
      const d = pad(date.getDate());
      const hh = pad(date.getHours());
      const mm = pad(date.getMinutes());
      const ss = pad(date.getSeconds());
      return `${y}-${m}-${d} ${hh}:${mm}:${ss}`;
    }
  }
};
</script>

<style scoped lang="scss">
.product-detail-dialog__section {
  margin-bottom: 16px;
}

.product-detail-dialog__images {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
  gap: 16px;

  .el-image {
    width: 100%;
    height: 160px;
    border: 1px solid #ebeef5;
    border-radius: 4px;
  }

  .image-slot {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #c0c4cc;
    font-size: 12px;
    background-color: #f5f7fa;
  }
}

.product-detail-dialog__images-title {
  margin-bottom: 8px;
  font-size: 12px;
  color: #909399;
}
</style>
