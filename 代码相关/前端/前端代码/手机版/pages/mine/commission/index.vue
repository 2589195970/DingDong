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
						<u--input
							placeholder="请输入固定佣金金额"
							border="surround"
							shape="circle"
							v-model="queryParams.fixedCommission"
							class="form-input">
						</u--input>
					</view>
				</view>

				<!-- 百分比佣金输入 -->
				<view class="info-item" v-if="queryParams.commissionConfigType == 1">
					<view class="item-left">百分比佣金</view>
					<view class="item-right">
						<u--input
							placeholder="请输入佣金百分比"
							border="surround"
							shape="circle"
							v-model="queryParams.scaleCommission"
							class="form-input">
						</u--input>
					</view>
				</view>
			</view>

			<!-- 提交按钮 -->
			<view class="submit-section">
				<u-button
					type="primary"
					shape="circle"
					text="保存设置"
					@click="submitForm(queryParams)"
					:customStyle="submitButtonStyle">
				</u-button>
			</view>
		</view>
	</view>
</template>

<script>
	import {

		selectAgentCommissionConfig,

		agentUpdateCommissionConfig,
	} from "@/api/product/product.js";
	export default {
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
				// 提交按钮样式
				submitButtonStyle: {
					backgroundColor: '#f09b7f',
					borderColor: '#f09b7f',
				}
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
    width: 140rpx;
  }

  .item-right {
    display: flex;
    align-items: center;
    flex: 1;
    justify-content: flex-end;

    &.radio-group {
      justify-content: center;

      >>> .u-radio-group {
        justify-content: center;
        gap: 0;
      }

      >>> .u-radio {
        margin-right: 32rpx !important;

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
  max-width: 400rpx;

  >>> .u-input {
    background-color: #f8f9fa;
    border: 1rpx solid #eaeef1;

    &:focus {
      border-color: #f09b7f;
    }
  }
}

// 提交按钮区域
.submit-section {
  margin-top: 40rpx;
  padding: 0 20rpx;

  >>> .u-button {
    height: 88rpx;
    font-size: 32rpx;
    font-weight: 500;
  }
}
</style>