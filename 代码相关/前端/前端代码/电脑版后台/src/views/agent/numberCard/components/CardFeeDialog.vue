<template>
  <el-dialog
    :visible.sync="innerVisible"
    title="提卡费设置"
    width="780px"
    append-to-body
    class="card-fee-upgrade"
    :close-on-click-modal="false"
    @close="handleCancel"
  >
    <div class="card-fee-upgrade__body" v-loading="contextLoading">
      <div class="card-fee-upgrade__header">
        <div>
          <p class="card-fee-upgrade__title">{{ productName }}</p>
          <p class="card-fee-upgrade__subtitle" v-if="baseFee">
            基础提卡费 {{ formatAmount(baseFee) }} 元
          </p>
        </div>
        <el-tag size="mini" effect="plain" disable-transitions>
          提卡费
        </el-tag>
      </div>

      <el-alert
        :title="scopeAlertText"
        type="info"
        show-icon
        :closable="false"
        class="card-fee-alert"
      />

      <el-form label-width="90px" class="card-fee-form">
        <el-form-item label="调整范围">
          <el-radio-group v-model="modeProxy">
            <el-radio-button label="ALL">全部下级</el-radio-button>
            <el-radio-button label="PARTIAL">指定代理</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="mode === 'PARTIAL'" label="指定代理">
          <el-input
            v-model="searchProxy"
            size="small"
            clearable
            placeholder="输入名称或编码搜索"
            class="card-fee-search"
          >
            <i slot="prefix" class="el-icon-search"></i>
          </el-input>
          <el-select
            v-if="availableAgents && availableAgents.length"
            class="card-fee-selector"
            v-model="selectedAgentProxy"
            placeholder="请选择代理"
            filterable
          >
            <el-option
              v-for="agent in availableAgents"
              :key="agent.agentProductId"
              :label="formatAgentOption(agent)"
              :value="agent.agentProductId"
            />
          </el-select>
          <div v-else class="card-fee-selector--empty">暂无可用下级代理</div>
        </el-form-item>

        <el-row :gutter="12" class="card-fee-metrics">
          <el-col :span="12">
            <div class="metric-card">
              <p class="metric-label">当前成本</p>
              <p class="metric-value">{{ formatAmount(incomingFee) }} 元</p>
              <p class="metric-tip">
                <span>来自上级的提卡成本</span>
                <span v-if="mode === 'PARTIAL'">
                  ，所选代理当前成本
                  {{ targetIncomingFee != null ? formatAmount(targetIncomingFee) : '--' }} 元
                </span>
              </p>
            </div>
          </el-col>
          <el-col :span="12">
            <div
              class="metric-card metric-card--accent"
              :class="{ 'is-negative': profit < 0 }"
            >
              <p class="metric-label">计划对下售价</p>
              <p class="metric-value">{{ formatAmount(localDownstream) }} 元</p>
              <p class="metric-tip">
                预估利润
                <span :class="{ 'is-negative': profit < 0 }">
                  {{ formatAmount(profit) }} 元
                </span>
              </p>
            </div>
          </el-col>
        </el-row>

        <el-divider />

        <el-form-item label="对下售价">
          <div class="card-fee-input">
            <el-input-number
              :value="localDownstream"
              :min="minValue"
              :step="1"
              :precision="0"
              controls-position="right"
              @change="handleDownstreamChange"
            />
            <span class="unit">元</span>
          </div>
          <div class="card-fee-actions">
            <el-button size="mini" @click="adjustValue(1)">+1 元</el-button>
            <el-button size="mini" @click="adjustValue(5)">+5 元</el-button>
            <el-button size="mini" @click="adjustValue(10)">+10 元</el-button>
            <el-button type="text" size="mini" @click="resetToIncoming">
              恢复为当前成本
            </el-button>
          </div>
          <p class="card-fee-tip">
            最低售价不能低于当前成本 {{ formatAmount(minValue) }} 元
          </p>
        </el-form-item>
      </el-form>

      <div v-if="overrideRows.length" class="card-fee-overrides">
        <el-divider />
        <h4 class="card-fee-overrides__title">已设置的提卡费特例</h4>
        <el-table
          :data="overrideRows"
          size="mini"
          border
          :header-cell-class-name="'card-fee-overrides__header'"
          class="card-fee-overrides__table"
        >
          <el-table-column prop="targetAgentName" label="代理" min-width="140" />
          <el-table-column
            label="特例售价"
            min-width="100"
            :formatter="formatOverrideFee"
          />
          <el-table-column
            label="当前成本"
            min-width="100"
            :formatter="formatIncomingFee"
          />
          <el-table-column
            prop="updateTime"
            label="更新时间"
            min-width="160"
            :formatter="formatTime"
          />
          <el-table-column label="操作" min-width="100">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="mini"
                :disabled="removingOverride"
                @click="handleCancelOverride(scope.row.overrideId)"
              >
                取消特例
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleCancel">取 消</el-button>
      <el-button type="primary" :loading="loading" @click="handleConfirm">
        确 定
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
const toSafeNumber = (value) => {
  const parsed = Number(value);
  return Number.isNaN(parsed) ? 0 : parsed;
};

const CARD_FEE_MODE_PARTIAL = 'PARTIAL';

export default {
  name: 'CardFeeDialog',
  model: {
    prop: 'visible',
    event: 'update:visible'
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    loading: {
      type: Boolean,
      default: false
    },
    contextLoading: {
      type: Boolean,
      default: false
    },
    removingOverride: {
      type: Boolean,
      default: false
    },
    cardFee: {
      type: Object,
      default: () => ({})
    },
    mode: {
      type: String,
      default: 'ALL'
    },
    availableAgents: {
      type: Array,
      default: () => []
    },
    selectedAgentId: {
      type: [String, Number],
      default: null
    },
    overrides: {
      type: Array,
      default: () => []
    },
    searchKeyword: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      localDownstream: 0
    };
  },
  computed: {
    innerVisible: {
      get() {
        return this.visible;
      },
      set(value) {
        this.$emit('update:visible', value);
      }
    },
    productName() {
      return this.cardFee?.productName || '提卡费设置';
    },
    baseFee() {
      return toSafeNumber(this.cardFee?.baseCardFee);
    },
    incomingFee() {
      const parentFee = toSafeNumber(this.cardFee?.parentIncomingCardFee);
      const fallback = toSafeNumber(this.cardFee?.incomingCardFee);
      return Number.isFinite(Number(this.cardFee?.parentIncomingCardFee))
        ? parentFee
        : fallback;
    },
    minValue() {
      return this.incomingFee;
    },
    targetIncomingFee() {
      if (this.mode !== 'PARTIAL') {
        return null;
      }
      return toSafeNumber(this.cardFee?.targetIncomingCardFee);
    },
    profit() {
      return toSafeNumber(this.localDownstream) - this.incomingFee;
    },
    scopeAlertText() {
      return this.mode === 'ALL'
        ? '调整后的价格将同步给全部下级代理，请确认后再操作'
        : '仅对所选下级代理生效，其他下级仍沿用默认提卡费';
    },
    overrideRows() {
      return Array.isArray(this.overrides) ? this.overrides : [];
    },
    modeProxy: {
      get() {
        return this.mode;
      },
      set(value) {
        this.$emit('mode-change', value);
      }
    },
    selectedAgentProxy: {
      get() {
        return this.selectedAgentId;
      },
      set(value) {
        this.$emit('target-change', value);
      }
    },
    searchProxy: {
      get() {
        return this.searchKeyword || '';
      },
      set(value) {
        this.$emit('search', value);
      }
    }
  },
  watch: {
    visible: {
      immediate: true,
      handler(val) {
        if (val) {
          this.syncLocalDownstream(true);
        }
      }
    },
    cardFee: {
      deep: true,
      handler() {
        this.syncLocalDownstream(false);
      }
    },
    mode() {
      this.syncLocalDownstream(true);
    },
    selectedAgentId() {
      if (this.mode === CARD_FEE_MODE_PARTIAL) {
        this.syncLocalDownstream(true);
      }
    }
  },
  methods: {
    syncLocalDownstream(force = false) {
      const fallback = this.minValue;
      const downstream = this.cardFee?.downstreamCardFee;
      const nextValue = Number.isFinite(Number(downstream))
        ? toSafeNumber(downstream)
        : fallback;
      if (force || Math.abs(nextValue - toSafeNumber(this.localDownstream)) > 0.0001) {
        this.localDownstream = nextValue;
      }
    },
    handleCancelOverride(overrideId) {
      if (!overrideId) {
        return;
      }
      this.$emit('cancel-override', overrideId);
    },
    handleDownstreamChange(value) {
      const safeValue = Math.max(this.minValue, toSafeNumber(value));
      this.localDownstream = safeValue;
      this.$emit('change', safeValue);
    },
    adjustValue(step) {
      this.handleDownstreamChange(this.localDownstream + step);
    },
    resetToIncoming() {
      this.handleDownstreamChange(this.minValue);
    },
    handleCancel() {
      this.$emit('cancel');
      this.innerVisible = false;
    },
    handleConfirm() {
      this.handleDownstreamChange(this.localDownstream);
      this.$emit('confirm');
    },
    formatAmount(value) {
      return toSafeNumber(value);
    },
    formatAgentOption(agent) {
      if (!agent) {
        return '';
      }
      const fallback = this.cardFee?.parentDownstreamCardFee;
      const rawFee =
        agent.overrideFee != null ? agent.overrideFee : agent.downstreamCardFee;
      const resolvedFee = Number.isFinite(Number(rawFee))
        ? Number(rawFee)
        : Number(fallback);
      const profit = resolvedFee - toSafeNumber(agent.incomingCardFee);
      const displayFee = this.formatAmount(resolvedFee);
      const profitText = profit >= 0 ? `+${profit}` : `${profit}`;
      return `${agent.agentName || agent.agentCode}（当前 ${displayFee} 元，利润 ${profitText} 元）`;
    },
    formatOverrideFee(row) {
      if (!row) {
        return '--';
      }
      return `${this.formatAmount(row.overrideFee)} 元`;
    },
    formatIncomingFee(row) {
      if (!row) {
        return '--';
      }
      return `${this.formatAmount(row.incomingCardFee)} 元`;
    },
    formatTime(row, column, cellValue) {
      if (!cellValue) {
        return '--';
      }
      const time = Number(cellValue);
      if (!Number.isFinite(time)) {
        return '--';
      }
      const date = new Date(time);
      if (Number.isNaN(date.getTime())) {
        return '--';
      }
      const pad = (num) => (num < 10 ? `0${num}` : `${num}`);
      return (
        `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ` +
        `${pad(date.getHours())}:${pad(date.getMinutes())}`
      );
    }
  }
};
</script>

<style lang="scss" scoped>
.card-fee-upgrade__body {
  padding: 4px 0 8px;
}

.card-fee-upgrade__header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.card-fee-upgrade__title {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.card-fee-upgrade__subtitle {
  margin: 4px 0 0;
  font-size: 12px;
  color: #909399;
}

.card-fee-alert {
  margin-bottom: 16px;
}

.card-fee-metrics {
  margin: 8px 0 0;
}

.metric-card {
  border-radius: 10px;
  padding: 12px;
  background: #f7f9fc;
  border: 1px solid #f0f2f5;

  &--accent {
    background: #fff7f0;
    border-color: #ffe4cc;
  }

  .metric-label {
    font-size: 12px;
    color: #909399;
    margin: 0 0 4px;
  }

  .metric-value {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 6px;
    color: #303133;
  }

  .metric-tip {
    font-size: 12px;
    color: #909399;
    margin: 0;
  }

  .is-negative {
    color: #f56c6c;
  }
}

.card-fee-form {
  margin-top: 12px;
}

.card-fee-search {
  margin-bottom: 8px;
}

.card-fee-selector {
  width: 100%;
}

.card-fee-selector--empty {
  padding: 8px 12px;
  color: #909399;
}

.card-fee-input {
  display: flex;
  align-items: center;

  .unit {
    margin-left: 8px;
    color: #606266;
  }
}

.card-fee-actions {
  margin-top: 8px;
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.card-fee-tip {
  margin-top: 8px;
  font-size: 12px;
  color: #909399;
}

.card-fee-overrides {
  margin-top: 16px;

  &__title {
    margin: 0 0 12px;
    font-size: 14px;
    font-weight: 600;
    color: #303133;
  }

  &__table {
    max-height: 220px;
  }
}
</style>
