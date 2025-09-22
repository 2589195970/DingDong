<template>
	<view class="content">

		<view class="header-section">
			<view class="flex padding justify-between">
				<view class="flex align-center">

					<image :src="avatar" class="cu-avatar">
					</image>
					<view class="user-info">
						<view class="u_title" style="color: #fff; margin-left: 10px;">
							{{ name }}
						</view>
					</view>
				</view>

			</view>
			<view style="display: flex; text-align: center; background-color: #4F97F6; padding-bottom: 20px;">
				<view style="flex: 1 1 0%; line-height: 1rem;">
					<view style="color: #fff;height: 30px; font-size: 14px; font-weight: bold; line-height: 30px;">
						{{productList.totalWithdrawalAmount*0.01}}元
					</view>
					<view style="color: #fff; font-size: 14px;">本年预估</view>
				</view>
				<view style="flex: 1 1 0%; line-height:1rem;">
					<view style="color: #fff; font-size: 14px; height: 30px; font-weight: bold; line-height: 30px;">
						{{productList.todayWithdrawalAmount*0.01}}元
					</view>
					<view style="color: #fff; font-size: 14px;">本月收入</view>
				</view>
				<view style="flex: 1 1 0%; line-height:1rem;">
					<view style="color: #fff; font-size: 14px; height: 30px; font-weight: bold; line-height: 30px;">
						{{productList.todayWithdrawalAmount*0.01}}元
					</view>
					<view style="color: #fff; font-size: 14px;">今日收入</view>
				</view>
				<view style="flex: 1 1 0%; line-height:1rem;">
					<view style="color: #fff; font-size: 14px; height: 30px; font-weight: bold;line-height: 30px;">
						{{productList.yesterdayWithdrawalAmount*0.01}}元
					</view>
					<view style="color: #fff; font-size: 14px;">昨天收入</view>
				</view>
			</view>
		</view>
		<view class="item">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="order" color="#5C98F9"></u-icon>
					订单统计
				</view>
				<u-subsection :list="list" mode="subsection" :current="current" @change="sectionChange"
					style="width: 150px;"></u-subsection>
			</view>
			<view class="" style="display: flex; flex-wrap: nowrap; justify-content: space-around;margin-top: 32px; padding-bottom: 8px;">
				<view class="orderclass-oui">
					<view class="ii-aa">{{agentOrder.yesterdayOrderNumber}}</view>
					<view class="ordercust-rwa">
						<view><image src="@/static/images/icon/sj.svg" alt="" class="imgsi-oi" /></view>
						<view class="aa-qq">昨日订单</view>
					</view>
				</view>
				<view class="orderclass-oui">
					<view class="ii-aa">{{agentOrder.todayOrderNumber}}</view>
					<view class="ordercust-rwa">
						<view><image src="@/static/images/icon/dsp.svg" alt="" class="imgsi-oi" /></view>
						<view class="aa-qq">今日订单</view>
					</view>
				</view>
				<view class="orderclass-oui">
					<view class="ii-aa">{{agentOrder.totalOrderNumber}}</view>
					<view class="ordercust-rwa">
						<view><image src="@/static/images/icon/wfsj.svg" alt="" class="imgsi-oi" /></view>
						<view class="aa-qq">总订单</view>
					</view>
				</view>
			</view>
		</view>

		<view class="item1">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="list-dot" color="#5C98F9"></u-icon>
					激活统计
				</view>
				<u-subsection :list="list" mode="subsection" :current="current1" @change="sectionChange1"
					style="width: 150px;"></u-subsection>
			</view>
			<view class="orderclass" style="margin-top: 20px;">
				<div ref="chart" style="width: 100px; height: 100px;"></div>
				<view class="ordercust" style="margin-top: 20px; width: 60%;">
					<span>年度订单数量 {{activateOrder.totalOrderNumber}}</span>
					<br />
					<span>激活数量 {{activateOrder.activatedOrderNumber}}</span>
					<br />
					<span>结算数量 {{activateOrder.settledOrderNumber}}</span>
				</view>
			</view>

		</view>
		<view class="item3" @click="xianqing">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="integral" color="#5C98F9"></u-icon>
					代理商管理
				</view>
				<view class="shu1">
					总数：{{dailidata.agentAccountTotal}}
				</view>
				<view class="shu1">
					查看
				</view>
			</view>
			<view class="daili-kan" style="display: flex; flex-wrap: nowrap; justify-content: space-around;margin-top: 20px;">
				<view class="orderclass-dd">
					<view class="dd-vla">10</view>
					<view class="dd-oimg">
						<image src="@/static/images/icon/zytj.svg" alt="" class="imgsi-dd" />
						<view class="dd-lab">昨日浏览</view>
					</view>
				</view>
				<view class="orderclass-dd">
					<view class="dd-vla">16</view>
					<view class="dd-oimg">
						<image src="@/static/images/icon/khtj.svg" alt="" class="imgsi-dd" />
						<view class="dd-lab">今日浏览</view>
					</view>
				</view>
				<view class="orderclass-dd">
					<view class="dd-vla">160</view>
					<view class="dd-oimg">
						<image src="@/static/images/icon/hytj.svg" alt="" class="imgsi-dd" />
						<view class="dd-lab">总浏览</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="item3" @click="xianqing">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="man-add" color="#5C98F9"></u-icon>
					注册数据
				</view>
			</view>
			<view class="daili-kan" style="display: flex; flex-wrap: nowrap; justify-content: space-around;margin-top: 20px;">
				<view class="orderclass-ki">
					<view class="od-im-uu">
						<image src="@/static/images/icon/qhdm.svg" alt="" class="imgsi-po" />
					</view>
					<view class="ordercust-lla">
						<view class="ok-ui-vl">5</view>
						<view class="ok-ui-la">昨日注册</view>
					</view>
				</view>
				<view class="orderclass-ki">
					<view class="od-im-uu">
						<image src="@/static/images/icon/qlzu.svg" alt="" class="imgsi-po" />
					</view>
					<view class="ordercust-lla">
						<view class="ok-ui-vl">2</view>
						<view class="ok-ui-la">今日注册</view>
					</view>
				</view>
				<view class="orderclass-ki">
					<view class="od-im-uu">
						<image src="@/static/images/icon/qysh.svg" alt="" class="imgsi-po" />
					</view>
					<view class="ordercust-lla">
						<view class="ok-ui-vl">29</view>
						<view class="ok-ui-la">总注册</view>
					</view>
				</view>
			</view>
		</view>
		
		<view class="item2-oo" @click="xianqing">
			<view style="display: flex;justify-content: space-between;">
				<view class="shu1">
					<u-icon name="bell" color="#5C98F9"></u-icon>
					系统通知
				</view>
			</view>
			<view>
				<view class="dt-item">
					<view class="dt-item-dot"></view>
					<view>新用户，首充优惠活动，月租仅需150元/月；优惠数量有限，快来抢购吧！</view>
				</view>
				<view class="dt-item">
					<view class="dt-item-dot"></view>
					<view>国庆节送流量活动，充值50元话费送1G流量，充100元话费宋1.5G流量，充越多送的越多！</view>
				</view>
				<view class="dt-item">
					<view class="dt-item-dot"></view>
					<view>中秋节满减优惠活动，消费满500元可领取1元优惠券，优惠券可抵扣话费。</view>
				</view>
			</view>
		</view>
		
	</view>

</template>


<script>
	import * as echarts from 'echarts';
	import {
		selectWithdrawalAPPStatistics,
		selectAgentOrderAPPStatistics,
		selectActivateAgentOrderAPPStatistics,
		selectChildAgentStatistics,
	} from "@/api/home/home.js";

	import {
		login
	} from '../api/login';

	export default {
		data() {
			return {

				productList: {
					totalWithdrawalAmount: 0,
					todayWithdrawalAmount: 0,
					yesterdayWithdrawalAmount: 0,
				},
				agentOrder: {},
				activateOrder: {},
				chartData: {
					value: 20,
					total: 100,
				},
				dailidata: {},
				list: ['我的订单', '团队订单'],
				current: 0,
				current1: 0,
				name: this.$store.state.user.name,
				version: getApp().globalData.config.appInfo.version
			}
		},
		mounted() {


			this.getdata();
			this.dail();
			this.AgentOrderdata();
			this.ActivateOrder();
		},
		computed: {

			avatar() {
				return this.$store.state.user.avatar
			},
			chartOption() {
				const {
					value,
					total
				} = this.chartData;
				return {
					backgroundColor: "#FFF",
					title: {
						text: (value || '0') + '%',
						x: 'center',
						y: 'center',
						textStyle: {
							color: '#333333',
							fontSize: '15',
							fontWeight: '600',
						},
					},
					angleAxis: {
						axisLine: {
							show: false,
						},
						axisLabel: {
							show: false,
						},
						splitLine: {
							show: false,
						},
						axisTick: {
							show: false,
						},
						min: 0,
						max: 100,
						startAngle: 90,
					},
					radiusAxis: {
						type: 'category',
						axisLine: {
							show: false,
						},
						axisTick: {
							show: false,
						},
						axisLabel: {
							show: true,
						},
						data: [],
					},
					polar: {
						radius: '150%',
						center: ['50%', '50%'],
					},
					series: [{
							type: 'bar',
							data: [value],
							z: 1,
							coordinateSystem: 'polar',
							barMaxWidth: 10,
							name: '采纳率',
							roundCap: 1,
							color: new echarts.graphic.LinearGradient(0, 1, 0, 0, [{
									offset: 0,
									color: '#0062d8'
								},
								{
									offset: 0.5,
									color: '#008cec'
								},
								{
									offset: 1,
									color: '#5bd9ff'
								}
							])
						},
						{
							type: 'bar',
							data: [total],
							z: 0,
							silent: true,
							coordinateSystem: 'polar',
							barMaxWidth: 10,
							roundCap: true,
							color: '#cee7f9',
							barGap: '-100%',
						},
						{
							type: 'pie',
							radius: '150%',
							center: ['50%', '50%'],
							hoverAnimation: false,
							startAngle: 180,
							endAngle: 0,
							silent: 1,
							z: 10,
							data: [{
									name: '',
									value: value > 75 ? (25 - (100 - value)) / total : (25 + value) / 100,
									label: {
										show: false,
									},
									labelLine: {
										show: false,
									},
									itemStyle: {
										color: 'transparent',
									},
								},

								{
									value: value > 75 ? 1 - (25 - (100 - value)) / total : 1 - (25 + value) / 100,
									label: {
										show: false,
									},
									labelLine: {
										show: false,
									},
									itemStyle: {
										color: 'transparent',
									},
								},
							],
						}
					]
				};
			}
		},
		methods: {
			xianqing() {
				uni.navigateTo({
					url: `/pages/home/use`
				})
			},
			sectionChange(index) {
				this.current = index;
				this.AgentOrderdata();
			},
			sectionChange1(index) {
				this.current1 = index;
				this.ActivateOrder();
			},
			getdata() {
				selectWithdrawalAPPStatistics({}).then(res => {
					if (res.data) {
						this.productList = res.data;
					}

				})

			},
			AgentOrderdata() {
				selectAgentOrderAPPStatistics(this.current).then(res => {
					this.agentOrder = res.data;
				})

			},
			dail() {
				selectChildAgentStatistics({}).then(res => {
					this.dailidata = res.data;
				})

			},
			ActivateOrder() {
				selectActivateAgentOrderAPPStatistics(this.current1).then(res => {
					this.activateOrder = res.data;
					// this.chartData.value=this.activateOrder.activatedOrderNumber/this.activateOrder.totalOrderNumber;
					this.chartData.value = (this.activateOrder.activatedOrderNumber / this.activateOrder
						.totalOrderNumber).toFixed(2) * 100;
					console.log(this.chartData);
					this.initChart();
				})

			},

			initChart() {
				const chartElement = this.$refs.chart;
				const myChart = echarts.init(chartElement);
				myChart.setOption(this.chartOption);
			}
		},
	}
</script>
<style lang="scss" scoped>
	.orderclass-oui{
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.ordercust-rwa{
		display: flex;
		flex-direction: row;
		justify-content: flex-start;
		align-items: flex-start;
	}
	.imgsi-oi{
		height: 19px;
		width: 19px;
		margin-right: 2px;
	}
	.ii-aa{
		margin-bottom: 2px;
		text-align: center;
		font-size: 16px;
		font-weight: bold;
	}
	.aa-qq{
		color: #909399;
		font-size: 12;
	}
	.dt-item{
		padding: 4px 10px;
		display: flex;
		flex-direction: row;
		justify-content: flex-start;
		align-items: flex-start;
	}
	.dt-item-dot{
		width: 10px;
		height: 6px;
		border-radius: 10px;
		background-color: red;
		margin-top: 8px;
		margin-right: 8px;
	}
	.item2-oo{
		background-color: #fff;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}
	.ok-ui-la{
		color: #909399;
	}
	.od-im-uu{
		height: 40px;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
	}
	.orderclass-ki{
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		height: 40px;
	}
	.imgsi-po{
		height: 40px;
		width: 40px;
	}
	.ordercust-lla{
		margin-left: 4px;
	}
	.ok-ui-vl{
		text-align: center;
		font-size: 18px;
		font-weight: bold;
	}
	.orderclass-dd{
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}
	.dd-oimg{
		display: flex;
		justify-content: center;
		align-items: center;
	}
	.imgsi-dd{
		width: 20px;
		height: 20px;
	}
	.dd-vla{
		font-size: 18px;
		text-align: center;
		font-weight: bold;
		margin: 6px 0 2px 0;
	}
	.dd-lab{
		color: #999999;
	}
	.daili-kan{
		margin-top: 25px;
		padding-bottom: 12px;
	}
	.ordercust {
		font-weight: 700;
		font-size: 12px;
		margin-top: 5px;
	}

	.orderclass {
		display: flex;
		justify-content: space-around;
	}

	.imgsi {
		width: 40px;
		height: 40px;
	}

	.item {
		// height: 250rpx;
		// border: 1px solid #666;
		background-color: #fff;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}
	
	.item3{
		background-color: #fff;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}

	.item2 {
		height: 80rpx;
		// border: 1px solid #666;
		background-color: #fff;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}

	.item1 {
		height: 350rpx;
		// border: 1px solid #666;
		background-color: #fff;
		border-radius: 10px;
		margin: 10px;
		padding: 10px;
	}

	.shu1 {
		color: rgb(51, 51, 51);
		font-size: 14px;
		height: 20px;
		display: flex;
		font-weight: bold;
	}

	page {
		background-color: #f5f6f7;
	}

	.header-section {
		background-color: #4F97F6;
	}

	.cu-avatar {
		width: 40px;
		height: 40px;
		border-radius: 50%;
	}

	.mine-container {
		width: 100%;
		height: 100%;


		.header-section {
			padding: 15px 15px 45px 15px;
			background-color: #3c96f3;
			color: white;

			.login-tip {
				font-size: 18px;
				margin-left: 10px;
			}

			.cu-avatar {

				border: 2px solid #eaeaea;

				.icon {
					font-size: 40px;
				}
			}

			.user-info {
				margin-left: 15px;

				.u_title {
					font-size: 18px;
					color: #fff;
					line-height: 30px;
				}
			}
		}

		.content-section {
			position: relative;
			top: -50px;

			.mine-actions {
				margin: 15px 15px;
				padding: 20px 0px;
				border-radius: 8px;
				background-color: white;

				.action-item {
					.icon {
						font-size: 28px;
					}

					.text {
						display: block;
						font-size: 13px;
						margin: 8px 0px;
					}
				}
			}
		}
	}
</style>