<template>
	<view class="page-container">
		<app-navbar title="佣金设置"></app-navbar>
		<view class="container">
			<!-- 佣金模式配置卡片 -->
			<view class="info-card">
				<view class="info-item has-border">
					<view class="item-left">佣金模式</view>
					<view class="item-right radio-group">
						<u-radio-group v-model="queryParams.commissionConfigType" placement="row">
              <u-radio
                  style="margin-left: 15px"
                  v-for="(item, index) in radiolist1"
                  :key="index"
                  :label="item.name"
                  :name="item.id"
                  @change="radioChange(item)">
              </u-radio>
						</u-radio-group>
					</view>
				</view>

				<!-- 固定佣金输入 -->
				<view class="info-item" v-if="queryParams.commissionConfigType == 0">
					<view class="item-left">固定佣金</view>
					<view class="item-right">
						<input
							type="number"
							placeholder="请输入固定佣金金额"
							v-model="queryParams.fixedCommission"
							class="form-input" />
						<text class="unit-text">元</text>
					</view>
				</view>

				<!-- 百分比佣金输入 -->
				<view class="info-item" v-if="queryParams.commissionConfigType == 1">
					<view class="item-left">百分比佣金</view>
					<view class="item-right">
						<input
							type="number"
							placeholder="请输入佣金百分比"
							v-model="queryParams.scaleCommission"
							class="form-input" />
						<text class="unit-text">%</text>
					</view>
				</view>
			</view>

			<!-- 提交按钮 -->
			<view class="submit-section">
				<view class="submit-button" @click="submitForm(queryParams)">
					<text class="submit-text">保存设置</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {

		selectAgentCommissionConfig,

		agentUpdateCommissionConfig,
	} from "@/api/product/product.js";
  import URadioGroup from "../../../uview-ui/components/u-radio-group/u-radio-group.vue";
  import URadio from "../../../uview-ui/components/u-radio/u-radio.vue";
	export default {
    components: {URadio, URadioGroup},
		data() {
			return {
				// 基本案列数据
				value: '',
				radiolist1: [{
						name: '固定佣金',
						disabled: false,
						id: 0,
					},
					{
						name: '百分比佣金',
						disabled: false,
						id: 1,
					},

				],
				// u-radio-group的v-model绑定的值如果设置为某个radio的name，就会被默认选中
				queryParams: {
					commissionConfigType: 1,
				},
			};
		},
		async created() {
			try {
				// 获取配置数据
				const configRes = await selectAgentCommissionConfig({});
				if (configRes.data) {
					this.queryParams = configRes.data;
				}

			} catch (error) {
				console.error('Error occurred while fetching data:', error);
				// 在这里可以显示用户友好的错误提示
			}
		},
		methods: {
			submitForm(formName) {
				agentUpdateCommissionConfig(formName).then((res) => {
					this.$modal.msgSuccess("设置成功");
				})
			},
			radioChange(n) {
				console.log(n);
			}
		}
	};
</script>

<style lang="scss" scoped>
page {
  background-color: #f5f6f7;
}

.container {
  padding: 15px;
}

// 单卡片容器
.info-card {
  background-color: #fff;
  border-radius: 8rpx;
  overflow: hidden;
  margin-bottom: 20rpx;
}

// 内部项目布局
.info-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 26rpx 30rpx;
  min-height: 88rpx;

  &.has-border {
    border-bottom: 1rpx solid #eaeef1;
  }

  .item-left {
    font-size: 32rpx;
    color: #333;
    flex-shrink: 0;
    width: 180rpx;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;

    &.radio-group {
      justify-content: flex-end;

      >>> .u-radio-group {
        display: flex;
        justify-content: flex-end;
        align-items: center;
      }

      >>> .u-radio {
        margin-right: 40rpx !important;

        &:last-child {
          margin-right: 0 !important;
        }
      }

      >>> .u-radio__icon-wrap {
        margin-right: 8rpx;
      }

      >>> .u-radio__label {
        font-size: 28rpx;
        color: #606266;
      }
    }
  }
}

// 表单输入框
.form-input {
  flex: 1;
  max-width: 200rpx;
  height: 80rpx;
  padding: 0 20rpx;
  font-size: 28rpx;
  color: #333;
  background-color: #f8f9fa;
  border: 1rpx solid #eaeef1;
  border-radius: 8rpx;
  text-align: right;

  &:focus {
    border-color: #f09b7f;
    background-color: #fff;
  }

  &::placeholder {
    color: #c0c0c0;
    font-size: 28rpx;
  }
}

// 单位文本
.unit-text {
  font-size: 28rpx;
  color: #606266;
  margin-left: 16rpx;
  flex-shrink: 0;
}

// 提交按钮区域
.submit-section {
  margin-top: 40rpx;
  padding: 0 20rpx;
}

// 提交按钮
.submit-button {
  height: 88rpx;
  background-color: #f09b7f;
  border-radius: 44rpx;
  display: flex;
  align-items: center;
  justify-content: center;

  &:active {
    background-color: #d87d63;
  }
}

.submit-text {
  font-size: 32rpx;
  font-weight: 500;
  color: #fff;
}
</style>