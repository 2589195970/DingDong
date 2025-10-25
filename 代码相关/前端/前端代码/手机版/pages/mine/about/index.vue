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

			<!-- 推广海报 -->
			<view class="promotion-card">
				<view class="card-header">
					<view class="section-title">
						<u-icon name="star-fill" color='#f09b7f'></u-icon>
						<text class="title-text">推广海报</text>
					</view>
				</view>
				<view class="poster-grid">
					<view class="poster-item" v-for="poster in posterList" :key="poster.id" :class="{'poster-item--empty': !poster.url}">
						<view class="poster-figure" @click="openPosterPreview(poster)">
							<image v-if="poster.url" :src="poster.url" class="poster-image" mode="aspectFill" />
							<view v-else class="poster-placeholder">
								<u-icon name="photo" color="#c0c4cc" size="48"></u-icon>
								<text>暂无{{poster.label}}</text>
							</view>
						</view>
						<text class="poster-label">{{poster.label}}</text>
						<view class="poster-actions">
							<view class="poster-btn poster-btn--primary" :class="{'poster-btn--loading': posterUploading[poster.id]}"
								@click.stop="handlePosterUpload(poster)">
								<text v-if="!posterUploading[poster.id]">{{ poster.url ? '更新' : '上传' }}</text>
								<text v-else>上传中...</text>
							</view>
							<view v-if="poster.url" class="poster-btn poster-btn--ghost"
								@click.stop="openPosterPreview(poster)">
								<text>预览</text>
							</view>
						</view>
					</view>
				</view>
				<view class="poster-tip">点击海报可预览/保存，支持直接重新上传</view>
			</view>

			<!-- API对接配置 -->
			<view class="promotion-card">
				<view class="card-header">
					<view class="section-title">
						<u-icon name="star-fill" color='#f09b7f'></u-icon>
						<text class="title-text">API对接配置</text>
					</view>
				</view>
				<view class="api-config">
					<view class="api-row" v-for="item in apiFields" :key="item.key">
						<text class="api-label">{{item.label}}</text>
						<view class="api-value" :class="{'api-value--clickable': !!apiConfig[item.key]}"
							@click="copyUrl(apiConfig[item.key], item.label)">
							<text class="api-text">{{apiConfig[item.key] || '暂未配置'}}</text>
							<text v-if="apiConfig[item.key]" class="api-copy-hint">点击复制</text>
						</view>
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
	import {
		getAgentApiVO,
		updatePosterImages
	} from "@/api/finance/finance.js"
	export default {
		data() {
			return {
				commissionopen: false,
				productList: {
					extendUrl: "",
					shopUrl: '',
					shopQrcodeMap: ''
				},
				posterList: [{
					id: 1,
					label: '海报图1',
					url: ''
				}, {
					id: 2,
					label: '海报图2',
					url: ''
				}, {
					id: 3,
					label: '海报图3',
					url: ''
				}],
				posterUploading: {
					1: false,
					2: false,
					3: false
				},
				apiConfig: {
					apiUrl: '',
					callbackUrl: '',
					agentCode: '',
					securityKey: ''
				},
				apiFields: [{
					key: 'apiUrl',
					label: '下单地址'
				}, {
					key: 'callbackUrl',
					label: '回调地址'
				}, {
					key: 'agentCode',
					label: '商户ID'
				}, {
					key: 'securityKey',
					label: 'apikey'
				}]
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
			copyUrl(url, emptyLabel = '链接') {
				if (!url) {
					uni.showToast({
						title: `暂无${emptyLabel}`,
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

			async getdata() {
				try {
					const [extendRes, apiRes] = await Promise.all([getAgentExtendUrlVO(), getAgentApiVO()]);
					if (extendRes.data) {
						this.productList = extendRes.data;
						this.posterList = this.buildPosterList(extendRes.data);
					} else {
						this.posterList = this.buildPosterList({});
					}
					this.apiConfig = apiRes.data || {};
				} catch (error) {
					console.error('获取推广信息失败', error);
					uni.showToast({
						title: '数据加载失败',
						icon: 'none'
					});
				}
			},
			buildPosterList(data = {}) {
				return [1, 2, 3].map(index => ({
					id: index,
					label: `海报图${index}`,
					url: data[`registerQrcodeMap${index}`] || ''
				}));
			},
	async handlePosterUpload(poster) {
		if (this.posterUploading[poster.id]) {
			return;
		}
		if (!(uni && typeof uni.chooseImage === 'function')) {
			uni?.showToast?.({
				title: '当前环境不支持上传',
				icon: 'none'
			});
			return;
		}
		uni.chooseImage({
			count: 1,
			sizeType: ['compressed'],
			sourceType: ['album', 'camera'],
			success: async (res) => {
				const tempFilePath = res.tempFilePaths?.[0];
				if (!tempFilePath) {
					uni.showToast({
						title: '未获取到图片',
						icon: 'none'
					});
					return;
				}
				this.$set(this.posterUploading, poster.id, true);
				try {
					const uploadRes = await updatePosterImages({
						filePath: tempFilePath,
						posterIndex: poster.id
					});
					if (uploadRes?.code === 200) {
						uni.showToast({
							title: '上传成功',
							icon: 'success'
						});
						await this.getdata();
					} else {
						uni.showToast({
							title: uploadRes?.msg || uploadRes?.message || '上传失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('海报上传失败', error);
					uni.showToast({
						title: '上传失败',
						icon: 'none'
					});
				} finally {
					this.$set(this.posterUploading, poster.id, false);
				}
			},
			fail: (err) => {
				console.warn('用户取消或选择图片失败', err);
			}
		});
			},
			openPosterPreview(poster) {
				if (!poster.url) {
					uni.showToast({
						title: '暂无海报图',
						icon: 'none'
					});
					return;
				}
				const urls = this.posterList.filter(item => item.url).map(item => item.url);
				uni.previewImage({
					urls,
					current: poster.url
				});
			}
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

	.poster-grid {
		display: flex;
		flex-wrap: wrap;
		gap: 24rpx;
		padding: 32rpx 30rpx 20rpx;
		justify-content: flex-start;
	}

	.poster-item {
		flex: 0 0 calc(50% - 24rpx);
		max-width: calc(50% - 24rpx);
		min-width: 240rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		gap: 20rpx;
		padding: 24rpx 18rpx 28rpx;
		border: 2rpx solid #f3d6c5;
		border-radius: 16rpx;
		background-color: #fffefb;
		box-sizing: border-box;
		transition: border-color 0.2s ease, box-shadow 0.2s ease;
	}

	.poster-item:not(.poster-item--empty):hover {
		border-color: #f09b7f;
		box-shadow: 0 10rpx 24rpx rgba(240, 155, 127, 0.16);
	}

	.poster-item--empty {
		opacity: 0.9;
	}

	.poster-figure {
		width: 100%;
		display: flex;
		justify-content: center;
		cursor: pointer;
		transition: transform 0.2s ease;
	}

	.poster-item:not(.poster-item--empty) .poster-figure:active {
		transform: scale(0.97);
	}

	.poster-image {
		width: 100%;
		max-width: 220rpx;
		height: 320rpx;
		border-radius: 12rpx;
		object-fit: cover;
		box-shadow: 0 12rpx 24rpx rgba(0, 0, 0, 0.12);
	}

	.poster-placeholder {
		width: 100%;
		max-width: 220rpx;
		height: 320rpx;
		background: linear-gradient(135deg, #ffe8de, #ffd4c6);
		border-radius: 12rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		color: #f08d6d;
		gap: 12rpx;
	}

	.poster-label {
		font-size: 26rpx;
		color: #4a4a4a;
		font-weight: 500;
	}

	.poster-actions {
		display: flex;
		gap: 12rpx;
		justify-content: center;
		align-items: center;
		flex-wrap: nowrap;
		margin-top: auto;
		width: 100%;
	}

	.poster-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		min-width: 140rpx;
		padding: 10rpx 26rpx;
		border-radius: 999rpx;
		font-size: 24rpx;
		text-align: center;
		color: #fff;
		background: linear-gradient(135deg, #f29878, #f27c63);
		border: none;
		box-shadow: 0 6rpx 14rpx rgba(240, 120, 90, 0.2);
		transition: transform 0.2s ease, box-shadow 0.2s ease, opacity 0.2s ease;
		white-space: nowrap;
	}

	.poster-btn--primary:active {
		transform: translateY(2rpx);
		box-shadow: 0 4rpx 10rpx rgba(240, 120, 90, 0.18);
	}

	.poster-btn--loading {
		opacity: 0.7;
		pointer-events: none;
	}

	.poster-btn--ghost {
		background: transparent;
		color: #f08d6d;
		border: 1rpx solid #f2b79e;
		box-shadow: none;
	}

	.poster-btn--ghost:active {
		background: rgba(240, 141, 109, 0.08);
	}

	.poster-tip {
		text-align: center;
		font-size: 24rpx;
		color: #999;
		padding: 0 30rpx 30rpx;
	}

	.api-config {
		padding: 20rpx 30rpx 40rpx;
		display: flex;
		flex-direction: column;
		gap: 24rpx;
	}

	.api-row {
		display: flex;
		flex-direction: column;
		gap: 12rpx;
	}

	.api-label {
		font-size: 28rpx;
		color: #333;
	}

	.api-value {
		background-color: #f8f9fa;
		border-radius: 8rpx;
		padding: 24rpx;
		position: relative;
		min-height: 60rpx;
		border: 1rpx solid transparent;
	}

	.api-value--clickable {
		border-color: #f09b7f;
		cursor: pointer;
	}

	.api-text {
		font-size: 28rpx;
		color: #666;
		word-break: break-all;
	}

	.api-copy-hint {
		position: absolute;
		top: 50%;
		right: 24rpx;
		transform: translateY(-50%);
		font-size: 24rpx;
		color: #f09b7f;
		opacity: 0.8;
	}

	@media (max-width: 600rpx) {
		.poster-grid {
			padding: 20rpx;
		}

		.poster-item {
			max-width: 100%;
			flex: 1 1 calc(50% - 20rpx);
		}

		.poster-image,
		.poster-placeholder {
			width: 240rpx;
			height: 320rpx;
		}

		.poster-actions {
			width: 100%;
			justify-content: center;
			flex-wrap: wrap;
		}
	}

	@media (max-width: 960rpx) {
		.poster-item {
			flex: 0 0 calc(50% - 24rpx);
			max-width: calc(50% - 24rpx);
		}
	}

	@media (max-width: 420rpx) {
		.poster-item {
			flex: 0 0 100%;
			min-width: auto;
		}
	}
</style>
