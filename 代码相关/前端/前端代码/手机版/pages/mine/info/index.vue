<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'person-filled'}" title="昵称" :rightText="user.nickName" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'phone-filled'}" title="手机号码" :rightText="user.phonenumber" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'email-filled'}" title="邮箱" :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="postGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="roleGroup" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'calendar-filled'}" title="创建日期" :rightText="user.createTime" />
    </uni-list>
	<view class="cu-list menu">
	  <view class="cu-item item-box">
	    <view class="content text-center" @click="handleLogout">
	      <text class="text-black">退出登录</text>
	    </view>
	  </view>
	</view>
  </view>
</template>

<script>
  import { getUserProfile } from "@/api/system/user"

  export default {
    data() {
      return {
        user: {},
        roleGroup: "",
        postGroup: ""
      }
    },
    onLoad() {
      this.getUser()
    },
    methods: {
      getUser() {
        getUserProfile().then(response => {
          this.user = response.data
          this.roleGroup = response.roleGroup
          this.postGroup = response.postGroup
        })
      },
	  handleLogout() {
	    this.$modal.confirm('确定退出系统吗？').then(() => {
	      this.$store.dispatch('LogOut').then(() => {}).finally(()=>{
	        this.$tab.reLaunch('/pages/index')
	      })
	    })
	  }
    }
  }
</script>

<style lang="scss">
  page {
    background-color: #ffffff;
  }
</style>
