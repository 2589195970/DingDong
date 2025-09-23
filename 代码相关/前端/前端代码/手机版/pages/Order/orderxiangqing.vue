<template>
	<view class="page-container">
		<app-navbar title="订单详情"></app-navbar>
		<view style="margin: 10px;background-color: #fff; ">
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>订单详情</H3>
		<view class="">
			订单号:{{xqdata.orderId}}<br>
			联系姓名:{{xqdata.cardName}}<br>
			联系方式:{{xqdata.cardPhone}}<br>
			联系地址:{{xqdata.cityName}} {{xqdata.provinceName}} {{xqdata.countyName}}<br>
			身份证号:{{xqdata.cardId}}<br>
			快递信息:{{xqdata.express}} {{xqdata.trackingNumber}}<br>
			订单状态:<span v-if="xqdata.orderStatus==-1">失败</span>
			<span v-if="xqdata.orderStatus==0">申请成功</span>
			<span v-if="xqdata.orderStatus==1">申请中</span>
			<span v-if="xqdata.orderStatus==2">已发货</span>
			<span v-if="xqdata.orderStatus==4">已激活</span><br>
			失败原因:{{xqdata.orderMessage}}<br>
			是否首充:<span v-if="xqdata.isRecharged==0">未充值</span>
			<span v-if="xqdata.isRecharged==1">已充值</span><br>
			首充金额:{{xqdata.rechargeAmount}}<br>
			佣金状态:<span v-if="xqdata.orderStatus==-1">失败</span>
			<span v-if="xqdata.orderCommissionStatus==0">未到结算状态</span>
			<span v-if="xqdata.orderCommissionStatus==1">待结算</span>
			<span v-if="xqdata.orderCommissionStatus==3">已结算</span>
			<span v-if="xqdata.orderCommissionStatus==4">无法结算</span><br>
			佣金说明:{{xqdata.orderCommissionMessage}}<br>
			下单时间:{{ formatTimestamp(xqdata.createTime) }}<br>

		</view>

		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>其他信息</H3>

		<view class="">
			推广人:{{xqdata.showDownstreamName}}<br>
			产品名称:{{xqdata.productName}}<br>


		</view>

		<!-- <H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>不发货地区</H3> -->
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				xqdata: {
					"orderId": "1117964472650399744",
					"orderDownstreamId": null,
					"cardName": "张三",
					"cardPhone": "18753796611",
					"cardId": "3703051983********",
					"provinceCode": "120000",
					"provinceName": "天津市",
					"cityCode": "120100",
					"cityName": "天津市",
					"countyCode": "120101",
					"countyName": "和平区",
					"cardAddress": "测*******",
					"orderStatus": -1,
					"orderMessage": "2025-05-08系统初审:超过最大下单数量",
					"productCode": "b9uy875x",
					"productName": "M电信清秋卡29元80G流量+0.1元/分钟（只发山东）",
					"productType": "1",
					"downstreamCode": "dpoo0ncr",
					"downstreamName": "叮咚号",
					"showDownstreamCode": "dpoo0ncr",
					"showDownstreamName": "叮咚号",
					"isRecharged": 0,
					"rechargeAmount": null,
					"rechargeTime": null,
					"express": null,
					"trackingNumber": null,
					"deliveryTime": null,
					"activeTime": null,
					"createTime": 1746709966846,
					"updateTime": 1746709968837,
					"accNumber": null,
					"orderSource": 0,
					"jsonParam": "{\"productCode\":\"b9uy875x\",\"agentCode\":\"dpoo0ncr\",\"link\":\"https%3A%2F%2Fh5.shengdakeji.vip%2F%3FproductCode%3Db9uy875x%26agentCode%3Ddpoo0ncr\",\"GthSubmitOrderResponse\":\"{\\\"createdAt\\\":\\\"2025-05-08 21:12:47\\\",\\\"id\\\":\\\"127340986\\\",\\\"productSku\\\":\\\"M电信5G清秋卡 29元/月80G流量+0.1元/分钟 免首月月租\\\",\\\"shareId\\\":\\\"356390\\\",\\\"sourceId\\\":\\\"1117964472650399744\\\",\\\"status\\\":\\\"120\\\",\\\"strRand\\\":\\\"zHQDHuDhzmPkhgAd42\\\"}\"}",
					"orderCommissionStatus": 0,
					"operatorType": 1,
					"productMasterMap": "http://yun.shengda.live/numberCard/a9660c6f-6680-458d-b0a3-d98bb2e5de41.jpg"
				},
			}
		},
		onLoad(options) {
			this.xqdata = JSON.parse(decodeURIComponent(options.key));
			console.log(this.xqdata);
		},
		methods: {

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
		}
	}
</script>

<style>
</style>