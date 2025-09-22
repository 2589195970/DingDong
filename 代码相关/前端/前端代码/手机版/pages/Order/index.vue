<template>
	<view>
		<view class="" v-if="loading" style="text-align: center;">正在加载</view>
		<template v-else>
			<u-collapse :value="['Docs guide']">
				<u-collapse-item title="筛选" name="Docs guide">
					<u--input placeholder="订单号" border="surround" shape="circle" v-model="params.orderId"
						@change="change" style="margin: 5px;"></u--input>
					<u--input placeholder="姓名" border="surround" shape="circle" v-model="params.cardName"
						@change="change" style="margin: 5px;"></u--input>
					<u--input placeholder="手机号" border="surround" shape="circle" v-model="params.cardPhone"
						@change="change" style="margin: 5px;"></u--input>
					<u--input placeholder="身份证" border="surround" shape="circle" v-model="params.cardId"
						@change="change" style="margin: 5px;"></u--input>
					<u--input placeholder="产品名称" border="surround" shape="circle" v-model="params.productName"
						@change="change" style="margin: 5px;"></u--input>
					<u-calendar :show="show" v-model="show" allowSameDay @confirm="confirm" @close="show = false"
						mode="range" monthNum="50" :minDate="minDate"></u-calendar>
					<view class="list-cell list-cell-arrow" @click="show = true">
						<view class="menu-item-box">
							<view style="margin-left: 5px;" v-if="first"> 选择日期 {{first}}~~{{last}}</view>
							<view style="margin-left: 5px;" v-else> 选择日期</view>
						</view>
					</view>
					<u-action-sheet :actions="listOrderStatus" title="选择订单状态" :show="showOrderStatus" @close="showOrderStatus = false"></u-action-sheet>
					<u-button @click="showOrderStatus = true">订单状态</u-button>
					<view>
						<view>我的订单</view>
						<view>代理商订单</view>
					</view>
					<u-button type="primary" shape="circle" text="搜索" @click="getList"></u-button>
					</uni-collapse>
				</u-collapse-item>
			</u-collapse>
			<view v-if="list.length > 0">
				<view class="item" v-for="(item, index) in list" :key="item.orderId" @click="orderxiangq(item)">

					<span class="shu1">订单号:{{ item.orderId }}</span>
					<view class="" style="display: flex; flex-wrap: nowrap;">
						<view class="imgtup2">
							<image :src="item.productMasterMap" class="imgtup1"></image>
							<!-- <image src="https://yun.shengda.live/numberCard/3c051e4d-ed75-430f-bf2e-74b843055899.jpg" class="imgtup1"></image> -->

						</view>
						<view style="    margin-top: 10px;">
							<view class="shu">
								<view class="shu2">
									{{ item.productName }}
								</view>
							</view>
							<view class="shu">
								<view class="shu1">
									物流信息：{{item.express}} {{item.trackingNumber}}
								</view>
							</view>
							<view class="shu">
								<view class="shu1">
									收件信息：{{ item.cardName }} {{ item.cardPhone }}
								</view>
							</view>
							<view class="shu">
								<view class="shu2">
									<span v-if="item.orderStatus==-1">失败</span>
									<span v-if="item.orderStatus==0">申请成功</span>
									<span v-if="item.orderStatus==1">申请中</span>
									<span v-if="item.orderStatus==2">已发货</span>
									<span v-if="item.orderStatus==4">已激活</span>
								</view>
							</view>

						</view>
					</view>
					<u-divider :hairline="true"></u-divider>
					<view class=" tguan">
						<view class="">
							推广人：{{ item.showDownstreamName }}
						</view>
						<view class="">
							{{ formatTimestamp(item.createTime) }}
						</view>
					</view>

				</view>
				<u-loadmore :status="status" />
			</view>
		</template>
	</view>
</template>

<script>
	import {
		agentSelectOrderListPage
	} from "@/api/order/order.js";
	export default {
		data() {
			return {
				title: '标题',
				listOrderStatus: [{
						name: '订单失败'
					},
					{
						name: '开卡中'
					},
					{
						name: '已发货'
					},
					{
						name: '未激活'
					},
					{
						name: '已激活'
					},
					{
						name: '激活且充值'
					},
					{
						name: '已结算'
					},
					{
						name: '待结算'
					},
					{
						name: '拒绝结算'
					},
					{
						name: '可补结算'
					},
					{
						name: '已提现'
					},
					{
						name: '已到账'
					}
				],
				showOrderStatus: false,
				// 请求参数
				show: false,
				params: {
					pageNo: 1,
					pageSize: 10
				},
				status: 'loadmore', //控制上拉加载的状态
				loading: true, //控制在响应回来前展示loading加载
				list: [], //要展示的列表
				last: undefined,
				first: undefined,
				minDate: new Date(2025, 0, 1).getTime(), // 时间戳形式，1970-01-01
				maxDate: new Date(2100, 0, 1).getTime() // 例如设置到2100年1月1日
			};
		},
		onLoad() {
			this.getList(); //获取列表
		},

		// 下拉刷新
		onPullDownRefresh() {
			this.status = 'loadmore';
			this.params.pageNo = 1;
			this.getList();
			setTimeout(() => {
				uni.stopPullDownRefresh();
			}, 1000);
		},
		// 上拉加载
		onReachBottom() {
			if (this.status !== 'nomore') {
				this.status = 'loading';
				this.params.pageNo++;
				this.getList();
			}
		},
		methods: {
			confirm(e) {
				this.show = false;
				console.log(e);
				this.getTimestamps(e)

			},
			getTimestamps(dates) {
				if (!dates || dates.length === 0) return {};

				this.first = dates[0];
				this.last = dates[dates.length - 1];

				this.params.starTime = this.first ? new Date(this.first).getTime() : null,
					this.params.endTime = this.last ? new Date(this.last).getTime() : null

			},
			// 订单详情
			orderxiangq(data) {
				uni.navigateTo({
					url: `/pages/Order/orderxiangqing?key=${encodeURIComponent(JSON.stringify(data))}`
				})
			},
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
			// 获取列表
			async getList() {
				const resp = await agentSelectOrderListPage(this.params);
				if (resp.code === 200) {
					let {
						pageNo,
						pageSize
					} = this.params;
					let {
						data
					} = resp;
					if (data.length < pageSize) this.status = 'nomore'; //如果响应值列表的长度小于我们所请求的页数 那就意味已经没有下一页了
					if (pageNo === 1) this.list = data.rows;
					//如果pagenum是第一页，直接赋值
					else this.list = [...this.list, ...data.rows]; //不是第一页 将原数组和得到的数组进行合并
				}
				if (this.loading) {
					setTimeout(() => {
						this.loading = false;
					}, 1000);
				} else {
					this.status = 'nomore';
				}
			}
		}
	};
</script>

<style lang="scss" scoped>
	.u-divider {
		margin: 5px 0;
	}

	.tguan {
		display: flex;
		justify-content: space-between;
		font-size: 12px;
		line-height: 15px;
	}

	.item {
		height: 300rpx;
		// border: 1px solid #666;
		background-color: #F1F1F1;
		border-radius: 10px;
		margin: 5px 10px;
		padding: 5px;
	}

	.shu {
		flex: 1 1 0%;
		// line-height: 3.6rem;

	}

	.shu1 {
		color: rgb(51, 51, 51);
		font-size: 12px;
		height: 20px;
		width: 220px;
		font-weight: bold;
		overflow: hidden;
		/* 隐藏溢出内容 */
		white-space: nowrap;
		/* 禁止换行 */
		text-overflow: ellipsis;
		/* 显示省略号 */
	}

	.shu2 {
		color: red;
		font-size: 14px;
		height: 20px;
		width: 240px;
		font-weight: bold;
		overflow: hidden;
		/* 隐藏溢出内容 */
		white-space: nowrap;
		/* 禁止换行 */
		text-overflow: ellipsis;
		/* 显示省略号 */

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
</style>