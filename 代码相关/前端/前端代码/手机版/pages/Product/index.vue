<template>
	<view>
		<u-search :showAction="false" v-model="queryParams.productName"@search="soplist" @clickIcon="soplist"></u-search>
		<view style="background-color: #fff; ">
			<view class="tab-bar">
				<view v-for="(item, index) in list" :key="index" class="tab-item"
					:class="{ 'active': current === index }" @click="click(index)">
					<image :src="item.icon" class="tab-icon" />
					<text class="tab-text">{{ item.name }}</text>
				</view>
				<!-- 底部滑动条 -->
				<view class="slider" :style="sliderStyle"></view>
			</view>

		</view>
		<view style="background-color: #fff; ">
			<u-collapse :value="['Docs guide']">
				<u-collapse-item title="筛选" name="Docs guide">
					<u-tabs :list="list1" lineWidth="30" :scrollable="false" lineColor="#f56c6c" @click="click1"
						></u-tabs>
					<u-tabs :list="list2" lineWidth="30" :scrollable="false" lineColor="#f56c6c" @click="click2"
						></u-tabs>
					</uni-collapse>
				</u-collapse-item>
			</u-collapse>
		</view>


		<u-loading-page :loading="loading"></u-loading-page>
		<view style="display: flex;justify-content: center;flex-direction: column;">
			<ul style="height: 180px; border: 1px solid #666; border-radius: 10px; margin: 5px 10px; padding: 5px;"
				v-for="dict in productList" :key="dict.productId">
				<view style="display: flex;">
					<span class="spid">商品ID:{{dict.productId}}</span>
					<span style="margin: 5px;">
						<p v-if="dict.productType==0" style="color: red;">日结秒返</p>
						<p v-if="dict.productType==1" style="color:green;">月结产品</p>
						<p v-if="dict.productType==2" style="color: red;">长期产品</p>
						<p v-if="dict.productType==3" style="color:green;">其它</p>
						<p v-if="dict.productType==4" style="color: red;">组合返佣</p>
					</span>
					<span style="margin: 5px 20px;">
						<p v-if="dict.productStatus==0" style="color: red;">已下架</p>
						<p v-if="dict.productStatus==1" style="color:green;">上架中</p>
					</span>
				</view>

				<view class="textwz">{{dict.productName}}</view>
				<li class="commodity" style="display: flex; flex-wrap: nowrap;" bindtap="gotoPage">

					<view class="imgtup2">
						<image :src="dict.productMasterMap" class="imgtup1"></image>
					</view>
					<view class="imgtup">

						<view style="display: flex; text-align: center;">
							<view style="flex: 1 1 0%; line-height: 0.6rem;">
								<view style="color: rgb(51, 51, 51);height: 20px; font-size: 14px; font-weight: bold;">
									{{dict.productTyll}}GB
								</view>
								<view style="color: rgb(153, 153, 153); font-size: 14px;">通用流量</view>
							</view>
							<view style="flex: 1 1 0%; line-height: 0.6rem;">
								<view style="color: rgb(51, 51, 51); font-size: 14px; height: 20px; font-weight: bold;">
									{{dict.productDxll}}GB
								</view>
								<view style="color: rgb(153, 153, 153); font-size: 14px;">定向流量</view>
							</view>
							<view style="flex: 1 1 0%; line-height: 0.6rem;">
								<view style="color: rgb(51, 51, 51); font-size: 14px; height: 20px; font-weight: bold;">
									{{dict.productThfz}}分钟
								</view>
								<view style="color: rgb(153, 153, 153); font-size: 14px;">通话分钟</view>
							</view>
						</view>
						<view style="display: flex; text-align: center;">
							<view style="flex: 1 1 0%; line-height: 1rem;">
								<view
									style="color: rgb(51, 51, 51);height: 30px; font-size: 14px; font-weight: bold; line-height: 30px;">
									{{dict.productGsdq}}
								</view>
								<view style="color: rgb(153, 153, 153); font-size: 14px;">归属地</view>
							</view>
							<view style="flex: 1 1 0%; line-height:1rem;">
								<view
									style="color: rgb(51, 51, 51); font-size: 14px; height: 30px; font-weight: bold; line-height: 30px;">
									{{dict.productAgeMin}}--{{dict.productAgeMax}}
								</view>
								<view style="color: rgb(153, 153, 153); font-size: 14px;">年龄限制</view>
							</view>

						</view>

						<!-- <span style="font-size: 12px;" class="textwz">{{dict.productFafs}}</span> -->

					</view>

				</li>
				<view class="xtx">
					<view class="xtxview" @click="xianqing(dict)">
						详情
					</view>|

					<view class="xtxview" style="color: red;font-size: 20px;">
						我的佣金：{{dict.productCommission}}
					</view>|
					<view class="xtxview" @click="showTrue(dict)">
						推广
					</view>
					<!-- <view class="xtxview">
						下架
					</view> -->
				</view>
			</ul>
		</view>
		<u-modal :show="commissionopen" @confirm="confirm" ref="uModal" @cancel="cancel" :showCancelButton='true'
			confirmText="保存" :asyncClose="true" style="line-height: 40px;">
			<image :src="fzhi.productQrcodeMap" alt="" style="height: 400px;" />
		</u-modal>
		<u-popup :show="show" @close="close" @open="open" :round="10">
			<view style="text-align: center;">
				<view class="dakalianjie" @click="dakalinjie(fzhi)">打开链接</view>
				<view class="dakalianjie" @click="handlefuzhi(fzhi)">复制链接</view>
				<view class="dakalianjie" @click="commissionOpoe">二维码</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import {
		agentSelectProductListPage,
		updateProductCommission
	} from "@/api/product/product";
	export default {
		data() {
			return {
				value: '',
				current: 0,
				list: [{
					name: '全部',
					id: null,
					icon: require('@/static/images/home/qb.png'),
				}, {
					name: '移动',
					icon: require('@/static/images/home/yd.png'),
					id: 0
				}, {
					name: '电信',
					icon: require('@/static/images/home/dx.png'),
					id: 1
				}, {
					name: '联通',
					icon: require('@/static/images/home/lt.png'),
					id: 2
				}, {
					name: '广电',
					icon: require('@/static/images/home/gd.png'),
					id: 3
				}, ],
				list1: [{
					name: '全部',
					id: null
				}, {
					name: '月结',
					id: 1
				}, {
					name: '秒返',
					id: 0
				}, ],
				list2: [{
					name: '全部',
					id: null
				}, {
					name: '上架中',
					id: 1
				}, {
					name: '已下架',
					id: 0
				}, ],
				productList: [],
				keyword: '遥看瀑布挂前川',
				show: false,
				loading: false,
				fzhi: '',
				queryParams: {
					pageNo: 1,
					pageSize: 10000,
					productType: 1,
				},
				commissionopen: false,
				frme: {},
			}
		},
		computed: {
			// 动态计算滑动条位置
			sliderStyle() {
				return {
					width: `${100 / this.list.length}%`,
					transform: `translateX(${100 * this.current}%)`
				}
			}
		},
		created() {
			this.soplist();
		},
		methods: {
			handleewma() {

			},
			cancel() {
				this.commissionopen = false
			},
			confirm() {
				// 图片地址（需允许跨域访问）
				const url = this.fzhi.productQrcodeMap;

				// 创建隐藏的 <a> 标签
				const link = document.createElement('a');
				link.href = url;
				link.download = 'img.jpg'; // 设置下载文件名
				document.body.appendChild(link);

				// 触发点击下载
				link.click();

				// 清理 DOM
				document.body.removeChild(link);
				this.commissionopen = false

			},
			commissionOpoe() {
				this.show = false;
				this.commissionopen = true;
			},
			showTrue(data) {
				this.fzhi = data;
				this.show = true;
			},
			dakalinjie(data) {
				window.open(data.h5Url);
			},
			handlefuzhi(data) {
				const textToCopy = data.h5Url;

				// 现代剪贴板API
				if (navigator.clipboard) {
					navigator.clipboard.writeText(textToCopy)
						.then(() => {
							this.$modal.msgSuccess("复制成功");
						})
						.catch(() => {
							// 现代API失败时降级处理
							this.copyWithExecCommand(textToCopy);
						});
				} else {
					// 旧浏览器直接使用execCommand
					this.copyWithExecCommand(textToCopy);
				}
			},

			// 封装传统复制方法
			copyWithExecCommand(text) {
				// 创建临时文本域
				const textArea = document.createElement('textarea');
				textArea.value = text;
				textArea.style.position = 'fixed'; // 避免滚动
				textArea.style.opacity = 0; // 透明

				// 添加到DOM并选中
				document.body.appendChild(textArea);
				textArea.select();

				try {
					// 执行复制命令
					const success = document.execCommand('copy');
					if (success) {
						this.$modal.msgSuccess("复制成功");
					} else {
						this.$modal.msgError("复制失败，请手动复制");
					}
				} catch (err) {
					this.$modal.msgError("复制失败，请手动复制");
				} finally {
					// 清理DOM
					document.body.removeChild(textArea);
				}
			},
			xianqing(data) {
				uni.navigateTo({
					url: `/pages/Product/cpxiangqing?key=${encodeURIComponent(JSON.stringify(data))}`
				})
			},
			open() {
				// console.log('open');
			},
			close() {
				this.show = false
				// console.log('close');
			},
			click(item) {
				console.log(item);
				this.current = item

				if (item == 0) {
					this.queryParams.operatorType = null;
				} else {
					this.queryParams.operatorType = item - 1;
				}

				this.soplist()
			},
			click2(item) {
				this.queryParams.productStatus = item.id;
				this.soplist()
			},
			click1(item) {
				console.log(item);
				this.queryParams.productType = item.id;
				this.soplist()
			},
			handleSelect(key, keyPath) {
				console.log(key, keyPath);
			},
			soplist() {
				this.loading = true;
				agentSelectProductListPage(this.queryParams).then(res => {

					this.productList = res.data.rows;
					this.loading = false;
				})
			},
		}
	}
</script>

<style scoped>
	/* 容器样式 */
	.custom-tabs-container {
		width: 100%;
	}

	/* 导航栏 */
	.tab-bar {
		display: flex;
		position: relative;
		padding: 20rpx 0;
		background: #ffffff;
	}

	.tab-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		transition: all 0.3s;
	}

	/* 图标样式 */
	.tab-icon {
		width: 50rpx;
		height: 50rpx;
		margin-bottom: 10rpx;
	}

	/* 文字样式 */
	.tab-text {
		font-size: 24rpx;
		color: #666;
	}

	/* 激活状态 */
	.active .tab-text {
		color: #007AFF;
		/* 激活文字颜色 */
		font-weight: bold;
	}

	/* 底部滑动条 */
	.slider {
		position: absolute;
		bottom: 0;
		height: 4rpx;
		background: #007AFF;
		transition: transform 0.3s ease;
	}

	/* 内容区域 */
	.content {
		padding: 30rpx;
		min-height: 400rpx;
	}

	.custom-tab {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 10rpx;
	}

	.tab-icon {
		width: 80rpx;
		height: 80rpx;
	}

	.tab-text {
		font-size: 24rpx;
		/* margin-top: 8rpx; */
	}

	.tp {
		display: flex;
		justify-content: space-around;
	}

	.tp image {
		width: 60px;
		height: 50px;
	}

	.textwz {
		text-align: center;
		font-weight: 700;
		overflow: hidden;
		/* 隐藏溢出内容 */
		white-space: nowrap;
		/* 禁止换行 */
		text-overflow: ellipsis;
		/* 显示省略号 */
	}

	.dakalianjie {
		/* padding:20rpx; */
		margin: 20rpx;
		height: 100rpx;
		line-height: 100rpx;
		border-bottom: 1px solid #666;
		font-size: 16px;

	}

	.spid {
		margin: 5px;
		background-color: orangered;
		color: #fff;
		font-size: 12px;
		width: 120rpx;
		text-align: center;
		border-radius: 10%;
	}

	.xtx {
		display: flex;
		flex-wrap: nowrap;
		justify-content: space-around;
		line-height: 30px;
	}

	.xtxview {
		text-align: center;
		font-weight: 700;
		line-height: 30px;
	}

	.imgtup {
		width: 70%;
		padding: 5px;
	}

	.imgtup2 {
		width: 30%;
		height: 100px;
		padding: 10rpx;
		position: relative;
	}

	.imgtup1 {
		width: 100%;
		height: 100%;
		object-fit: contain;
		border-radius: 10px;
	}

	.paimin {
		position: absolute;
		top: 0;
		left: 0;
		z-index: 2;
		width: 20px;
		height: 20px;
		background-color: #000;
		color: #fff;
		text-align: center;
		border-radius: 5px;
		opacity: 0.8;
	}

	.kflexc {
		display: flex;
		align-items: center;
		justify-content: space-around;
		flex-wrap: wrap;
		padding: 0.5rem 0.15rem;
		position: relative;
		border-radius: 0.35rem 0.35rem 0px 0px;
		top: -0.4rem;
		overflow: hidden;
		/* background: rgb(255, 255, 255); */
	}

	.li {
		color: rgb(102, 102, 102);
		font-size: 16px;
		font-weight: bold;
	}

	.liact {
		font-size: 20px;
		color: rgb(30, 30, 30);
		font-weight: bold;
		background: url(http://test91.91haoka.cn/1715304099tw650481.jpg) no-repeat center;
	}

	button {
		/* 基础样式重置 */
		background: none;
		border: none;
		padding: 0;
		margin: 0;

		/* 字体继承 */
		font: inherit;
		color: inherit;
		line-height: normal;

		/* 移除默认点击效果 */
		cursor: pointer;
		outline: none;
		-webkit-appearance: none;
		-moz-appearance: none;
		appearance: none;

		/* 可选：禁用文本选中 */
		user-select: none;
	}

	.carousel {
		position: relative;
		max-width: 800px;
		margin: 0 10px;
		overflow: hidden;
		border-radius: 10px;
	}

	.carousel-inner {
		display: flex;
		transition: transform 0.5s ease;
	}

	.carousel-item {
		min-width: 100%;
		height: 120px;
	}

	.carousel-item img {
		width: 100%;
		height: 100%;
		object-fit: cover;
	}

	/* 导航箭头 */
	.carousel-control {
		position: absolute;
		top: 50%;
		transform: translateY(-50%);
		background: rgba(0, 0, 0, 0.5);
		color: white;
		padding: 10px;
		cursor: pointer;
		border: none;
		font-size: 24px;
	}

	.prev {
		left: 0;
		border-radius: 0 5px 5px 0;
	}

	.next {
		right: 0;
		border-radius: 5px 0 0 5px;
	}

	/* 指示器圆点 */
	.carousel-indicators {
		position: absolute;
		bottom: 20px;
		left: 50%;
		transform: translateX(-50%);
		display: flex;
		gap: 8px;
	}

	.indicator {
		width: 12px;
		height: 12px;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.5);
		cursor: pointer;
		border: none;
	}

	.indicator.active {
		background: white;
	}

	.jies {
		width: 95%;
		margin: 0 auto;
		display: flex;
		justify-content: space-around;
		align-items: center;
		flex-wrap: wrap;
		padding: 0.5rem 0.15rem;
		position: relative;
		border-radius: 0.35rem;
		top: -0.4rem;
		overflow: hidden;
		background: rgb(255, 255, 255);
	}

	body,
	ul,
	li,
	input,
	form,
	a,
	p {
		margin: 0;
		padding: 0;
		list-style-type: none;
	}

	/* .container {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  max-width: 440px;
 
  
} */

	.container {
		max-width: 440px;
		margin: 50px auto;
		padding: 20px;
		/* border: 1px solid #ccc; */
		border-radius: 5px;
		background-size: 100%;
		text-align: center;
		background-color: #CCE5FF;
	}

	h2 {
		text-align: center;
	}

	.search-form {
		text-align: center;
		margin-bottom: 20px;
	}

	.search-input {
		padding: 10px;
		width: calc(80% - 10px);
		font-size: 16px;
		border: 1px solid #ccc;
		border-radius: 20px;
		margin: 5px;
	}

	.search-button {
		width: calc(85% - 10px);
		padding: 10px;
		margin: 5px;
		font-size: 16px;
		background-color: #007bff;
		color: #fff;
		border: none;
		border-radius: 20px;
		cursor: pointer;
	}

	table {
		width: 100%;
		border-collapse: collapse;
	}

	th,
	td {
		padding: 10px;
		text-align: left;
		border-bottom: 1px solid #ccc;
	}

	th {
		background-color: #f2f2f2;
	}

	.orderinfonav {
		background-color: #fff;
		width: calc(80% - 0rem);
		border-radius: 1rem;
		margin: 1rem auto;
		padding: 1rem;
	}

	.ordertitle01 {
		/* font-size: 0.32rem; */
		color: #333;
	}

	.ordertitleright {
		float: right;
	}

	.ordertitleright a.orderfail {
		display: block;
		width: 4.2rem;
		/* height: 0.48rem; */
		/* line-height: 0.48rem; */
		color: #666;
		font-size: 0.6rem;
		border-radius: 1.45rem;
		text-align: center;
		background-color: #e2e3e4;
	}

	.ordertitleleft img {
		width: 1rem;
		height: 1rem;
		padding-right: 0.1rem;
	}

	.clear {
		clear: both;
	}

	.ordertitle01 span {
		/* display: block; */
		font-size: 0.5rem;
		/* line-height: 0.3rem; */
		color: #989898;
		padding: 0.1rem 0 0;
	}

	.orderjh {
		background-color: #007bff;
		color: #fff;
		padding: 1.3rem;
		font-size: 0.5rem;
		line-height: 1rem;
		height: 8rem;
	}

	.topnum {
		margin-top: -2.5rem;
	}

	.orderinfonav ul li {
		line-height: 1rem;
		font-size: 0.5rem;
		border-bottom: 1px solid #e5e5e6;
		color: #888;
		list-style-type: none;
	}

	.orderinfonav ul {
		padding: 1rem;
	}

	.float {
		line-height: 1rem;
		font-size: 0.6rem;
		color: #888;
		padding: 0 0.3rem 0.3rem;
	}

	.fx {
		font-size: 30px;
		color: red;
	}

	.fx1 {
		font-size: 14px;
		font-weight: 700;
		color: #000;
	}

	.baoc {
		width: 50%;
		height: 50px;
		line-height: 50px;
		font-size: 20px;
		color: #fff;
		background-color: #2463CF;
		border-radius: 25px;
		text-align: center;
	}
</style>