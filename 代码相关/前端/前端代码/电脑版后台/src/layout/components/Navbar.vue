<template>
  <div class="navbar">
    <div class="navbar-header">
      <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
      <div class="system-title">叮咚号卡订单管理系统</div>
      <div class="right-menu">
      <template v-if="device!=='mobile'">
        <!--<search id="header-search" class="right-menu-item" />-->

        <!-- <el-tooltip content="源码地址" effect="dark" placement="bottom">
          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <!-- <el-tooltip content="文档地址" effect="dark" placement="bottom">
          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />
        </el-tooltip> -->

        <!--<screenfull id="screenfull" class="right-menu-item hover-effect" />-->

        <!--<el-tooltip content="布局大小" effect="dark" placement="bottom">-->
        <!--  <size-select id="size-select" class="right-menu-item hover-effect" />-->
        <!--</el-tooltip>-->

        <vip-commission-cards class="vip-pill" />
        <router-link to="/user/profile" class="nav-link menu-pill">
          <i class="el-icon-user nav-link__icon" />
          <span class="nav-link__label">用户中心</span>
        </router-link>
        <router-link to="/system/notice" class="nav-link menu-pill">
          <i class="el-icon-bell nav-link__icon" />
          <span class="nav-link__label">公告中心</span>
        </router-link>

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <img :src="avatar" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      </div>
    </div>
    <div class="navbar-nav">
      <div class="nav-left">
        <breadcrumb id="breadcrumb-container" class="breadcrumb-container" v-if="!topNav"/>
        <top-nav id="topmenu-container" class="topmenu-container" v-if="topNav"/>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import VipCommissionCards from '@/components/VipCommissionCards'

export default {
  components: {
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc,
    VipCommissionCards
  },
  data() {
    return {
      noticeTimer: null // placeholder if future intervals are reintroduced
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    }
  },
  created() {
  },
  beforeDestroy() {
    if (this.noticeTimer) {
      clearInterval(this.noticeTimer);
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    }
  }
}
</script>

<style lang="scss" scoped>
@mixin menu-pill-base {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  height: 38px;
  border-radius: 22px;
  border: 1px solid #edf2f9;
  background: #fff;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
  color: #1f2d3d;
  line-height: 1;
  text-decoration: none;
}

.navbar {
  height: auto;
  min-height: 80px;
  overflow: visible;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  flex-direction: column;
  padding: 0 20px;

  .navbar-header {
    display: flex;
    align-items: center;
    height: 45px;
    border-bottom: 1px solid #f0f0f0;

    .hamburger-container {
      line-height: 45px;
      height: 45px;
      cursor: pointer;
      transition: background .3s;
      -webkit-tap-highlight-color:transparent;
      padding: 0 8px;

      &:hover {
        background: rgba(0, 0, 0, .025)
      }
    }

    .system-title {
      line-height: 45px;
      font-size: 22px;
      font-weight: 700;
      color: #1890ff;
      margin-left: 15px;
      background: linear-gradient(135deg, #1890ff 0%, #40a9ff 50%, #1890ff 100%);
      background-size: 200% auto;
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
      background-clip: text;
      text-shadow: 0 2px 4px rgba(24, 144, 255, 0.2);
      animation: shimmer 4s ease-in-out infinite;
      letter-spacing: 0.5px;

      @keyframes shimmer {
        0%, 100% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
      }
    }
  }

  .navbar-nav {
    display: flex;
    align-items: center;
    justify-content: space-between;
    height: 35px;

    .nav-left {
      flex: 1;
      display: flex;
      align-items: center;
    }

    .breadcrumb-container {
      line-height: 35px;
      height: 35px;
    }

    .topmenu-container {
      position: static;
      flex: 1;
    }
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    display: flex;
    align-items: center;
    height: 45px;
    margin-left: auto;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-flex;
      align-items: center;
      padding: 0 12px;
      height: 45px;
      font-size: 16px;
      color: #5a5e66;
      transition: all .3s;
      border-radius: 4px;

      &.hover-effect {
        cursor: pointer;

        &:hover {
          background: rgba(24, 144, 255, 0.1);
          color: #1890ff;
        }
      }
    }

    .menu-pill {
      @include menu-pill-base;
      gap: 8px;
      margin-left: 12px;

      &:first-of-type {
        margin-left: 0;
      }

      &:hover {
        border-color: #ffd666;
        box-shadow: 0 4px 12px rgba(255, 214, 102, 0.35);
        color: #d46b08;
      }

      .nav-link__icon {
        font-size: 18px;
        color: #d46b08;
      }

      .nav-link__label {
        font-weight: 600;
        color: inherit;
      }
    }

    .nav-link {
      display: inline-flex;
      align-items: center;
    }

    .vip-pill {
      margin-right: 12px;
      display: inline-flex;
      align-items: center;
    }

    .avatar-container {
      margin-right: 0;

      .avatar-wrapper {
        display: flex;
        align-items: center;
        cursor: pointer;
        padding: 0 8px;
        border-radius: 4px;
        transition: all .3s;
        height: 45px;

        &:hover {
          background: rgba(24, 144, 255, 0.1);
        }

        .user-avatar {
          width: 32px;
          height: 32px;
          border-radius: 50%;
          border: 2px solid #fff;
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
        }

        .el-icon-caret-bottom {
          margin-left: 8px;
          color: #5a5e66;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
