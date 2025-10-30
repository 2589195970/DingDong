import {
  updateProductCardFee,
  listCardFeeChildren,
  listCardFeeOverrides,
  upsertCardFeeOverride,
  cancelCardFeeOverride
} from '@/api/monitor/daili';

const CARD_FEE_MODE_ALL = 'ALL';
const CARD_FEE_MODE_PARTIAL = 'PARTIAL';

const toSafeNumber = (value) => {
  const parsed = Number(value);
  return Number.isNaN(parsed) ? 0 : parsed;
};

export default {
  data() {
    return {
      openCardFee: false,
      cardFeeSubmitting: false,
      cardFeeRemovingOverride: false,
      cardFeeContextLoading: false,
      cardFeeSourceRow: null,
      cardFeeModes: {
        ALL: CARD_FEE_MODE_ALL,
        PARTIAL: CARD_FEE_MODE_PARTIAL
      },
      cardFeeForm: {
        agentProductId: null,
        productCode: '',
        productName: '',
        parentIncomingCardFee: 0,
        parentDownstreamCardFee: 0,
        incomingCardFee: 0,
        downstreamCardFee: 0,
        baseCardFee: 0,
        mode: CARD_FEE_MODE_ALL,
        targetAgentProductId: null,
        targetAgentName: '',
        targetIncomingCardFee: 0,
        overrideId: null
      },
      cardFeeContext: {
        rawChildren: [],
        children: [],
        overrides: []
      },
      cardFeeSearchKeyword: ''
    };
  },
  computed: {
    allTargetAgents() {
      return Array.isArray(this.cardFeeContext.children)
        ? this.cardFeeContext.children
        : [];
    },
    availableTargetAgents() {
      const list = this.allTargetAgents;
      const keyword = (this.cardFeeSearchKeyword || '').trim().toLowerCase();
      if (!keyword) {
        return list;
      }
      return list.filter((item) => {
        const name = (item.agentName || '').toLowerCase();
        const code = (item.agentCode || '').toLowerCase();
        return name.includes(keyword) || code.includes(keyword);
      });
    },
    cardFeeOverrides() {
      const list = Array.isArray(this.cardFeeContext.overrides)
        ? this.cardFeeContext.overrides
        : [];
      return list.filter((item) => Number(item && item.status) === 1);
    },
    isPartialMode() {
      return this.cardFeeForm.mode === CARD_FEE_MODE_PARTIAL;
    }
  },
  methods: {
    formatAmount(value) {
      return toSafeNumber(value);
    },
    supportsCardFee(row) {
      return Number(row && row.sffftk) === 1;
    },
    handleCardFee(row) {
      if (!row || !this.supportsCardFee(row)) {
        this.$message.warning('该产品无需提卡费调整');
        return;
      }
      this.cardFeeSourceRow = row;
      this.cardFeeSearchKeyword = '';
      this.cardFeeForm = {
        agentProductId: row.agentProductId,
        productCode: row.productCode || '',
        productName: row.productName || '',
        parentIncomingCardFee: toSafeNumber(row.incomingCardFee),
        parentDownstreamCardFee: toSafeNumber(
          row.downstreamCardFee != null ? row.downstreamCardFee : row.incomingCardFee
        ),
        incomingCardFee: toSafeNumber(row.incomingCardFee),
        downstreamCardFee: toSafeNumber(
          row.downstreamCardFee != null ? row.downstreamCardFee : row.incomingCardFee
        ),
        baseCardFee: toSafeNumber(row.baseCardFee),
        mode: CARD_FEE_MODE_ALL,
        targetAgentProductId: null,
        targetAgentName: '',
        targetIncomingCardFee: 0,
        overrideId: null
      };
      this.cardFeeContext = {
        rawChildren: [],
        children: [],
        overrides: []
      };
      this.openCardFee = true;
      this.refreshCardFeeContext();
    },
    refreshCardFeeContext() {
      if (!this.cardFeeSourceRow || !this.cardFeeSourceRow.agentProductId) {
        return Promise.resolve();
      }
      this.cardFeeContextLoading = true;
      const tasks = [
        this.fetchCardFeeChildren(this.cardFeeSourceRow.agentProductId)
      ];
      if (this.cardFeeSourceRow.productCode) {
        tasks.push(this.fetchCardFeeOverrides(this.cardFeeSourceRow.productCode));
      } else {
        tasks.push(Promise.resolve());
      }
      return Promise.all(tasks)
        .catch(() => {})
        .finally(() => {
          this.cardFeeContextLoading = false;
          if (this.isPartialMode && this.cardFeeForm.targetAgentProductId) {
            this.handleTargetAgentChange(this.cardFeeForm.targetAgentProductId, {
              silent: true
            });
          }
        });
    },
    fetchCardFeeChildren(agentProductId) {
      return listCardFeeChildren({ agentProductId })
        .then((res) => {
          const children = Array.isArray(res.data) ? res.data : [];
          const normalized = children.map((item) => ({
            ...item,
            agentProductId: Number(item.agentProductId),
            incomingCardFee: toSafeNumber(item.incomingCardFee),
            downstreamCardFee: toSafeNumber(item.downstreamCardFee),
            cardFeeProfit: toSafeNumber(item.cardFeeProfit),
            overrideId: item.overrideId || null,
            overrideFee: item.overrideFee != null ? toSafeNumber(item.overrideFee) : null,
            rawDownstreamCardFee: item.downstreamCardFee
          }));
          this.cardFeeContext.rawChildren = normalized;
          this.cardFeeContext.children = normalized;
          this.applyOverridesToChildren();
        })
        .catch(() => {
          this.cardFeeContext.rawChildren = [];
          this.cardFeeContext.children = [];
        });
    },
    fetchCardFeeOverrides(productCode) {
      return listCardFeeOverrides({ productCode })
        .then((res) => {
          this.cardFeeContext.overrides = Array.isArray(res.data) ? res.data : [];
          this.applyOverridesToChildren();
        })
        .catch(() => {
          this.cardFeeContext.overrides = [];
        });
    },
    applyOverridesToChildren() {
      const overrides = this.cardFeeOverrides;
      const overrideMap = overrides.reduce((acc, item) => {
        if (item && item.targetAgentCode) {
          acc[item.targetAgentCode] = item;
        }
        return acc;
      }, {});
      const baseChildren = Array.isArray(this.cardFeeContext.rawChildren)
        ? this.cardFeeContext.rawChildren
        : [];
      const children = baseChildren.map((child) => {
        const override = overrideMap[child.agentCode];
        const incoming = toSafeNumber(child.incomingCardFee);
        const parentDefault = toSafeNumber(this.cardFeeForm.parentDownstreamCardFee);
        const rawDownstreamSource = child.rawDownstreamCardFee;
        const rawDownstream = rawDownstreamSource !== null && rawDownstreamSource !== undefined
          ? Number(rawDownstreamSource)
          : null;
        const profitBasedDownstream = incoming + toSafeNumber(child.cardFeeProfit);
        const overrideFee = override && override.overrideFee != null
          ? toSafeNumber(override.overrideFee)
          : null;
        let downstream = overrideFee;
        if (!Number.isFinite(downstream)) {
          downstream = Number.isFinite(rawDownstream)
            ? rawDownstream
            : Number.isFinite(parentDefault)
              ? parentDefault
              : Number.isFinite(profitBasedDownstream)
                ? profitBasedDownstream
                : incoming;
        }
        const profit = downstream - incoming;
        return {
          ...child,
          agentProductId: Number(child.agentProductId),
          downstreamCardFee: downstream,
          cardFeeProfit: profit,
          overrideId: override ? override.overrideId : null,
          overrideFee: override ? downstream : null,
          hasOverride: override ? 1 : 0
        };
      });
      this.cardFeeContext.children = children;
      this.ensureTargetAgentSelection();
    },
    handleCardFeeSearch(keyword) {
      this.cardFeeSearchKeyword = keyword;
      if (!this.isPartialMode) {
        return;
      }
      this.$nextTick(() => {
        this.ensureTargetAgentSelection();
      });
    },
    ensureTargetAgentSelection() {
      if (this.cardFeeForm.mode !== CARD_FEE_MODE_PARTIAL) {
        return;
      }
      const candidates = this.availableTargetAgents;
      if (!candidates.length) {
        return;
      }
      const currentId = Number(this.cardFeeForm.targetAgentProductId);
      const matched = candidates.find(
        (item) => Number(item.agentProductId) === currentId
      );
      if (matched) {
        this.handleTargetAgentChange(matched.agentProductId, { silent: true });
        return;
      }
      this.handleTargetAgentChange(candidates[0].agentProductId, { silent: true });
    },
    handleCardFeeModeChange(mode) {
      if (!mode || mode === this.cardFeeForm.mode) {
        return;
      }
      this.cardFeeForm.mode = mode;
      if (mode === CARD_FEE_MODE_ALL) {
        this.cardFeeForm.incomingCardFee = this.cardFeeForm.parentIncomingCardFee;
        this.cardFeeForm.downstreamCardFee = this.cardFeeForm.parentDownstreamCardFee;
        this.cardFeeForm.targetAgentProductId = null;
        this.cardFeeForm.targetAgentName = '';
        this.cardFeeForm.targetIncomingCardFee = 0;
        this.cardFeeForm.overrideId = null;
      } else if (mode === CARD_FEE_MODE_PARTIAL) {
        const targetId = this.cardFeeForm.targetAgentProductId;
        if (targetId) {
          this.handleTargetAgentChange(targetId, { silent: true });
        } else if (this.availableTargetAgents.length) {
          this.handleTargetAgentChange(this.availableTargetAgents[0].agentProductId, {
            silent: true
          });
        }
      }
    },
    handleTargetAgentChange(agentProductId, options = {}) {
      const normalizedId =
        agentProductId === null || agentProductId === undefined
          ? null
          : Number(agentProductId);
      if (!Number.isFinite(normalizedId)) {
        if (!options.silent) {
          this.$message.warning('请选择有效的下级代理');
        }
        return;
      }
      const target = this.availableTargetAgents.find(
        (item) => Number(item.agentProductId) === normalizedId
      );
      if (!target) {
        if (!options.silent) {
          this.$message.warning('请选择有效的下级代理');
        }
        return;
      }
      this.cardFeeForm.targetAgentProductId = target.agentProductId;
      this.cardFeeForm.targetAgentName = target.agentName || '';
      this.cardFeeForm.overrideId = target.overrideId || null;
      const incoming = toSafeNumber(target.incomingCardFee);
      const fallback = toSafeNumber(this.cardFeeForm.parentDownstreamCardFee);
      const downstream = target.overrideFee != null
        ? toSafeNumber(target.overrideFee)
        : Number.isFinite(Number(target.downstreamCardFee))
          ? toSafeNumber(target.downstreamCardFee)
          : fallback;
      this.cardFeeForm.incomingCardFee = toSafeNumber(this.cardFeeForm.parentIncomingCardFee);
      this.cardFeeForm.targetIncomingCardFee = incoming;
      this.cardFeeForm.downstreamCardFee = Number.isFinite(downstream) ? downstream : fallback;
      if (!options.silent) {
        this.cardFeeForm.mode = CARD_FEE_MODE_PARTIAL;
      }
    },
    submitCardFee() {
      if (this.cardFeeSubmitting) {
        return;
      }
      if (this.cardFeeForm.mode === CARD_FEE_MODE_PARTIAL) {
        this.submitCardFeeForTarget();
      } else {
        this.submitCardFeeForAll();
      }
    },
    submitCardFeeForAll() {
      const form = this.cardFeeForm;
      if (!form.agentProductId) {
        this.$message.error('缺少代理产品ID');
        return;
      }
      if (form.downstreamCardFee < form.parentIncomingCardFee) {
        this.$message.error(
          `对下提卡费需 ≥ 当前成本 ${this.formatAmount(
            form.parentIncomingCardFee
          )} 元`
        );
        return;
      }
      this.cardFeeSubmitting = true;
      updateProductCardFee({
        agentProductId: form.agentProductId,
        downstreamCardFee: form.downstreamCardFee
      })
        .then(() => {
          this.$message.success('提卡费已更新');
          this.openCardFee = false;
          if (typeof this.getList === 'function') {
            this.getList();
          }
        })
        .catch(() => {})
        .finally(() => {
          this.cardFeeSubmitting = false;
          this.resetCardFeeForm(false);
          this.resetCardFeeContext();
        });
    },
    submitCardFeeForTarget() {
      const form = this.cardFeeForm;
      if (!form.targetAgentProductId) {
        this.$message.error('请选择需要调整的下级代理');
        return;
      }
      if (form.downstreamCardFee < form.incomingCardFee) {
        this.$message.error(
          `对下提卡费需 ≥ 当前成本 ${this.formatAmount(
            form.incomingCardFee
          )} 元`
        );
        return;
      }
      this.cardFeeSubmitting = true;
      upsertCardFeeOverride({
        agentProductId: form.targetAgentProductId,
        overrideFee: form.downstreamCardFee
      })
        .then(() => {
          this.$message.success('特例提卡费已更新');
          this.openCardFee = false;
          if (typeof this.getList === 'function') {
            this.getList();
          }
        })
        .catch(() => {})
        .finally(() => {
          this.cardFeeSubmitting = false;
          this.resetCardFeeForm(false);
          this.resetCardFeeContext();
        });
    },
    cancelCardFeeOverrideItem(overrideId) {
      if (!overrideId || this.cardFeeRemovingOverride) {
        return;
      }
      const parsedId = Number(overrideId);
      if (!Number.isFinite(parsedId) || parsedId <= 0) {
        this.$message.error('无法识别的特例ID，无法取消');
        return;
      }
      this.$confirm('确认要取消该提卡费特例吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.cardFeeRemovingOverride = true;
          return cancelCardFeeOverride({ overrideId: parsedId });
        })
        .then(() => {
          this.$message.success('特例已取消');
          return this.refreshCardFeeContext();
        })
        .then(() => {
          if (typeof this.getList === 'function') {
            this.getList();
          }
        })
        .catch(() => {})
        .finally(() => {
          this.cardFeeRemovingOverride = false;
        });
    },
    handleCardFeeClose() {
      this.openCardFee = false;
      this.resetCardFeeForm();
      this.resetCardFeeContext();
    },
    handleCardFeeChange(value) {
      if (!this.cardFeeForm) {
        return;
      }
      this.cardFeeForm.downstreamCardFee = toSafeNumber(value);
    },
    resetCardFeeForm(resetName = true) {
      const prevName = !resetName && this.cardFeeForm
        ? this.cardFeeForm.productName
        : '';
      this.cardFeeForm = {
        agentProductId: null,
        productCode: '',
        productName: prevName,
        parentIncomingCardFee: 0,
        parentDownstreamCardFee: 0,
        incomingCardFee: 0,
        downstreamCardFee: 0,
        baseCardFee: 0,
        mode: CARD_FEE_MODE_ALL,
        targetAgentProductId: null,
        targetAgentName: '',
        targetIncomingCardFee: 0,
        overrideId: null
      };
    },
    resetCardFeeContext() {
      this.cardFeeContext = {
        rawChildren: [],
        children: [],
        overrides: []
      };
      this.cardFeeSourceRow = null;
      this.cardFeeSearchKeyword = '';
    }
  }
};
