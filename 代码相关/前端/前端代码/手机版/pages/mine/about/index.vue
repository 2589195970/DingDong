<template>
	<view class="page-container">
		<app-navbar title="推广"></app-navbar>
		<view class="container">
			<!-- 我的店铺卡片 -->
			<view class="promotion-card">
				<view class="card-header">
					<view class="section-title">
						<u-icon name="star-fill" color='#f09b7f'></u-icon>
						<text class="title-text">我的店铺</text>
					</view>
					<view class="share-btn" @click="commissionopen=true">
						<text class="share-text">分享店铺</text>
					</view>
				</view>
				<view class="url-container">
					<view class="url-display" @click="copyUrl(productList.shopUrl)">
						<text class="url-text">{{productList.shopUrl || '暂无店铺地址'}}</text>
					</view>
				</view>
			</view>

			<!-- 推广邀请卡片 -->
			<view class="promotion-card">
				<view class="card-header">
					<view class="section-title">
						<u-icon name="star-fill" color='#f09b7f'></u-icon>
						<text class="title-text">推广邀请</text>
					</view>
				</view>
				<view class="url-container">
					<view class="url-display" @click="copyUrl(productList.extendUrl)">
						<text class="url-text">{{productList.extendUrl || '暂无推广地址'}}</text>
					</view>
				</view>
			</view>
		</view>

		<!-- 分享二维码弹窗 -->
		<u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
			confirmText="保存图片" :asyncClose="true">
			<view class="qrcode-container">
				<image :src="productList.shopQrcodeMap" class="qrcode-image" />
			</view>
		</u-modal>
	</view>
</template>

<script>
	import {
		getAgentExtendUrlVO
	} from "@/api/order/order.js";
	export default {
		data() {
			return {
				commissionopen: false,
				productList: {
					extendUrl: "",
				},
			}
		},
		mounted() {
			this.getdata();
		},
		methods: {
			cancel() {
				this.commissionopen = false
			},
			confirm() {
				// 保存二维码图片
				const url = this.productList.shopQrcodeMap;

				// #ifdef H5
				// H5环境下载图片
				const link = document.createElement('a');
				link.href = url;
				link.download = 'shop-qrcode.jpg';
				document.body.appendChild(link);
				link.click();
				document.body.removeChild(link);
				// #endif

				// #ifndef H5
				// 小程序/App环境保存图片
				uni.downloadFile({
					url: url,
					success: (res) => {
						uni.saveImageToPhotosAlbum({
							filePath: res.tempFilePath,
							success: () => {
								uni.showToast({
									title: '保存成功',
									icon: 'success'
								});
							},
							fail: () => {
								uni.showToast({
									title: '保存失败',
									icon: 'none'
								});
							}
						});
					}
				});
				// #endif

				this.commissionopen = false;
			},

			// 复制URL链接
			copyUrl(url) {
				if (!url) {
					uni.showToast({
						title: '暂无链接',
						icon: 'none'
					});
					return;
				}

				// #ifdef H5
				// H5环境复制
				if (navigator.clipboard) {
					navigator.clipboard.writeText(url).then(() => {
						uni.showToast({
							title: '复制成功',
							icon: 'success'
						});
					});
				} else {
					// 兼容旧浏览器
					const textArea = document.createElement('textarea');
					textArea.value = url;
					document.body.appendChild(textArea);
					textArea.select();
					document.execCommand('copy');
					document.body.removeChild(textArea);
					uni.showToast({
						title: '复制成功',
						icon: 'success'
					});
				}
				// #endif

				// #ifndef H5
				// 小程序/App环境复制
				uni.setClipboardData({
					data: url,
					success: () => {
						uni.showToast({
							title: '复制成功',
							icon: 'success'
						});
					}
				});
				// #endif
			},

			getdata() {
				getAgentExtendUrlVO({}).then(res => {
					if (res.data) {
						this.productList = res.data;
					}
				});
			},
		}
	}
</script>

<style lang="scss" scoped>
	page {
		background-color: #f5f6f7;
	}

	.page-container {
		background-color: #f5f6f7;
		min-height: 100vh;
	}

	.container {
		padding: 30rpx;
	}

	/* 推广卡片样式 - 单卡片设计模式 */
	.promotion-card {
		background-color: #fff;
		border-radius: 8rpx;
		margin-bottom: 30rpx;
		overflow: hidden;
		box-shadow: none; /* 扁平化设计，不使用阴影 */
	}

	.card-header {
		display: flex !important;
		align-items: center !important;
		justify-content: space-between !important;
		padding: 30rpx;
		border-bottom: 1rpx solid #eaeef1;
		min-height: 80rpx;
		width: 100%;
		box-sizing: border-box;
		flex-wrap: nowrap; /* 强制不换行 */
	}

	.section-title {
		display: flex !important;
		align-items: center !important;
		gap: 16rpx;
		flex: 1;
		min-width: 0; /* 允许压缩 */
		flex-wrap: nowrap;
	}

	.title-text {
		font-size: 32rpx;
		font-weight: 500;
		color: #333;
		white-space: nowrap; /* 防止标题文字换行 */
	}

	.share-btn {
		padding: 12rpx 24rpx;
		background-color: #f09b7f;
		border-radius: 6rpx;
		flex-shrink: 0 !important;
		white-space: nowrap;
		display: inline-block;
		text-align: center;
		min-width: 120rpx; /* 确保按钮最小宽度 */
	}

	.share-text {
		font-size: 28rpx;
		color: #fff;
	}

	.url-container {
		padding: 30rpx;
	}

	.url-display {
		background-color: #f8f9fa;
		border: 1rpx solid #f09b7f; /* 使用主题色边框 */
		border-radius: 8rpx;
		padding: 24rpx 120rpx 24rpx 24rpx; /* 右侧预留空间给提示文字 */
		position: relative;
		min-height: 60rpx; /* 最小高度确保布局稳定 */
	}

	.url-text {
		font-size: 28rpx;
		color: #666;
		line-height: 1.6;
		word-break: break-all;
		padding-right: 0; /* 移除额外的右边距 */
	}

	/* 添加点击提示 */
	.url-display::after {
		content: '点击复制';
		position: absolute;
		top: 50%;
		right: 24rpx;
		transform: translateY(-50%); /* 垂直居中 */
		font-size: 24rpx;
		color: #f09b7f;
		opacity: 0.7;
		white-space: nowrap; /* 防止文字换行 */
		z-index: 2; /* 确保在上层 */
	}

	/* 二维码弹窗样式 */
	.qrcode-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx;
	}

	.qrcode-image {
		width: 400rpx;
		height: 400rpx;
		border-radius: 8rpx;
	}

	/* 响应式设计 - 仅在极小屏幕时才换行 */
	@media (max-width: 600rpx) {
		.card-header {
			flex-direction: column !important;
			align-items: flex-start !important;
			gap: 20rpx;
		}

		.share-btn {
			align-self: flex-end;
		}
	}

	/* 中等屏幕保持一行显示 */
	@media (min-width: 601rpx) {
		.card-header {
			flex-direction: row !important;
			align-items: center !important;
			justify-content: space-between !important;
		}
	}

	/* 小屏幕URL显示优化 */
	@media (max-width: 600rpx) {
		.url-display {
			padding: 24rpx 100rpx 24rpx 24rpx;
		}

		.url-display::after {
			font-size: 22rpx;
			right: 20rpx;
		}

		.qrcode-image {
			width: 300rpx;
			height: 300rpx;
		}
	}

	/* 点击状态反馈 */
	.url-display:active {
		background-color: #f0f1f2;
		transform: scale(0.98);
		transition: all 0.2s ease;
	}

	.share-btn:active {
		background-color: #d87d63; /* 主题色深色状态 */
		transform: scale(0.95);
		transition: all 0.2s ease;
	}
</style>