<template>
	<view class="page-container">
		<app-navbar title="商品详情"></app-navbar>
		<view style="margin: 10px;">
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>产品套餐：{{xqdata.productName}}</H3>
		<view class="">
			<p style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				{{xqdata.productTcjs}}
			</p>
		</view>
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>快递方式</H3>
		<view class="">
			<p style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				{{xqdata.productFafs}}
			</p>
		</view>
		<!-- <H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>激活方式</H3> -->
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>办卡年龄</H3>
		<view class="">
			<p style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				{{xqdata.productAgeMin}}-{{xqdata.productAgeMax}}
			</p>
		</view>

		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>推广要求</H3>
		<view class="">
			<p style="border: 1px solid #3374EB; margin: 10px;padding: 10px; border-radius: 10rpx">
				{{xqdata.productDemand}}
			</p>
		</view>
		<H3 style="display: flex;"><u-icon name="star-fill" color='#3374EB'></u-icon>详情图</H3>
		<view class="" >
			
					<image  :src="xqdata.productDetailMap" alt="" style="width: 100%; height:800px;" />
		
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
					"productId": 40,
					"agentProductId": 260,
					"productCode": "nn74xqc5",
					"productName": "挖金客-测试产品",
					"operatorType": 0,
					"productType": 0,
					"productDemand": "1",
					"productMasterMap": null,
					"productTemplateJson": "{\"bgThemeColor\":\"\"}",
					"productStatus": 1,
					"productCommission": 0,
					"revenueProductCommission": 0,
					"distributionProductCommission": 0,
					"productSort": 0,
					"createTime": null,
					"updateTime": null,
					productDetailMap:'',
					"h5Url": "https://h5.haoka.asia/?productCode=nn74xqc5&agentCode=j4npwb3e",
					"agentCodeList": [
						"mjh7bn6r"
					]
				},
			}
		},
		onLoad(options) {
			this.xqdata = JSON.parse(decodeURIComponent(options.key));
			console.log(this.xqdata);
		}
	}
</script>

<style>
</style>