Vue.use(vant.Field);
Vue.use(vant.Button);
Vue.use(vant.Checkbox);
Vue.use(vant.Dialog);
Vue.use(vant.Popup);
Vue.use(vant.Area);
Vue.use(vant.Radio);
Vue.use(vant.RadioGroup);
Vue.use(vant.Search);
Vue.use(vant.Lazyload, {
  lazyComponent: true,
  loading: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPgogICAgPGNpcmNsZSBmaWxsPSIjZjVmNmY3IiBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiLz4KICAgIDxwYXRoIGQ9Ik0yMCAxMGMtNS41MiAwLTEwIDQuNDgtMTAgMTBzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwLTQuNDgtMTAtMTAtMTB6bTAgMThjLTQuNDEgMC04LTMuNTktOC04czMuNTktOCA4LTggOCAzLjU5IDggOC0zLjU5IDgtOCA4eiIgZmlsbD0iI2NjYyIvPgogIDwvZz4KPC9zdmc+',
  error: 'data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iNDAiIGhlaWdodD0iNDAiIHZpZXdCb3g9IjAgMCA0MCA0MCIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj4KICA8ZyBmaWxsPSJub25lIiBmaWxsLXJ1bGU9ImV2ZW5vZGQiPgogICAgPGNpcmNsZSBmaWxsPSIjZjVmNmY3IiBjeD0iMjAiIGN5PSIyMCIgcj0iMjAiLz4KICAgIDxwYXRoIGQ9Ik0yMCAxMGMtNS41MiAwLTEwIDQuNDgtMTAgMTBzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwLTQuNDgtMTAtMTAtMTB6bTUgMTMuNTlMMjIuNDEgMjEgMjUgMTguNDEgMjMuNTkgMTcgMjEgMTkuNTkgMTguNDEgMTcgMTcgMTguNDFsMi41OSAyLjU5LTIuNTkgMi41OUwxOCAyNS40MWwyLjU5LTIuNTlMMjMuNTkgMjUgMjUgMjMuNTl6IiBmaWxsPSIjZmY0NDQ0Ii8+CiAgPC9nPgo8L3N2Zz4='
});
axios.interceptors.request.use(
  (config) => {
    if (config.showLoading === false) {
      return config;
    }
    vant.Toast.loading({
      duration: 0,
      message: '加载中...',
      forbidClick: true,
    });
    return config;
  }, (error) => Promise.reject(error),
);

axios.interceptors.response.use(
  (res) => {
    if (res.config.showLoading !== false) {
      vant.Toast.clear();
    }
    if (res.data.code === 500) {
      vant.Toast.clear();
      vant.Toast(res.data.message);
      return Promise.reject(res.data);
    }
    console.log(res.data);
    return Promise.resolve(res.data);
  }, (error) => {
    console.log(error);
    console.log(vant.Toast.clear());
    vant.Toast.clear();
    vant.Toast('系统异常，请稍后重试！');
    return Promise.reject(error);
  },
);

new Vue({
  el: '#app',
  data() {
    return {
      images: [
        'https://img.yzcdn.cn/vant/apple-1.jpg',
        'https://img.yzcdn.cn/vant/apple-2.jpg',
        'https://img.yzcdn.cn/vant/apple-3.jpg'
      ],
      items: ['全部', '移动', '电信', '联通', '广电'],
      activeIndex: 0, // 默认选中第一个
      arrow: 'arrow-down',
      xianshi: '0',
      productList: {},
      productTypeTags: {
        0: { className: 'tag-daily', label: 'M', accentColor: '#1E88E5' },
        1: { className: 'tag-month', label: 'Y', accentColor: '#FF6B35' },
        5: { className: 'tag-paid', label: 'F', accentColor: '#34C759' },
      },
      ddxx: [],
      ddxx1: {},
      phone: '',
      certId: '',
      productStatus:'1',
      shareStoreopon: false,
      agentCode: '',
      ShopQrcodeMap: '',
      visitorId: '', // 访客标识
      homeRedirectUrl: '', // 来源回退地址
      searchKeyword: '', // 搜索关键词
    };
  },
  computed: {

  },

  created() {
    this.agentCode= getQueryString('agentCode')
    this.initializeVisitor();
    this.homeRedirectUrl = this.resolveHomeRedirect();

    // 检查URL参数，如果mode=order-query则直接进入订单查询页面
    const mode = getQueryString('mode');
    if (mode === 'order-query') {
      this.orderdata(); // 直接调用订单查询方法
    } else {
      this.soplist(); // 正常加载产品列表
    }
  },

  mounted() {

  },
  methods: {
    onChange(event) {
      this.setData({
        callprcId: event.detail,
      });
    },
    onChange1(event) {
      this.setData({
        msgprcId: event.detail,
      });
    },
    orderdata() {
      this.xianshi = "1";
    },
    goHome() {
      const redirectTarget = this.homeRedirectUrl || this.resolveHomeRedirect();
      if (redirectTarget) {
        window.location.href = redirectTarget;
        return;
      }
      if (window.history.length > 1) {
        window.history.back();
        return;
      }
      this.xianshi = "0";
      this.ddxx = [];
      this.ddxx1 = {};
    },
    backToOrderList() {
      this.xianshi = "2";
    },
    search() {
      var postData = {
        // 身份证
        cardId: this.certId,
        // 手机号
        cardPhone: this.phone,
      }
      axios.post(baseUrl + '/product/selectOrderList', postData).then(res => {
        if (res.data) {
          this.ddxx = res.data
          console.log(this.ddxx);
          this.xianshi = "2";
        }

      })
    },
    selectItem(index) {
      this.activeIndex = index;
      this.soplist()
    },
    onSearch() {
      this.soplist();
    },
    onClear() {
      this.searchKeyword = '';
      this.soplist();
    },
    onCancel() {
      this.searchKeyword = '';
      this.soplist();
    },
    xiangxidingdan(data) {
      console.log(data);
      this.ddxx1 = data;
      this.xianshi = "3";
    },
    shareStore() {
      axios.get(`${baseUrl}/product/getShopQrcodeMap?agentCode=${this.agentCode}`)
        .then(res => {
          this.ShopQrcodeMap = res.message
        }
        )
      this.shareStoreopon = true;

    },
    downloadImage() {
      // 图片地址（需允许跨域访问）
      const url = this.ShopQrcodeMap;

      // 创建隐藏的 <a> 标签
      const link = document.createElement('a');
      link.href = url;
      link.download = 'img.jpg'; // 设置下载文件名
      document.body.appendChild(link);

      // 触发点击下载
      link.click();

      // 清理 DOM
      document.body.removeChild(link);
    },
    soplist() {
      axios.post(baseUrl + '/product/getAgentProductList', {
        agentCode: this.agentCode,
        operatorType: this.activeIndex - 1 == -1 ? '' : this.activeIndex - 1,
        productStatus: this.productStatus,
        productName: this.searchKeyword, // 添加搜索关键词
      }).then(res => {
        this.productList = res.data;
      })
    },
    getProductAccent(productType) {
      // 付费提卡保持绿色，其他免费领取统一使用红色
      if (Number(productType) === 5) {
        return '#34C759'; // 付费提卡 - 绿色
      }
      return '#FF3B30'; // 免费领取 - 统一红色
    },

    /**
     * 跳转到产品H5页面
     * @param {string} h5Url - 产品的H5页面地址
     */
    Receive(h5Url) {
      if (h5Url) {
        window.location.href = h5Url;
      } else {
        vant.Toast('产品链接无效');
      }
    },

    // ==================== 页面访问统计相关方法 ====================

    /**
     * 生成UUID
     * @returns {string} UUID字符串
     */
    generateUUID() {
      return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        const r = Math.random() * 16 | 0;
        const v = c === 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
      });
    },

    /**
     * 获取或创建访客ID
     * @returns {string} 访客ID
     */
    getOrCreateVisitorId() {
      const storageKey = 'dingdong_visitor_id';
      let visitorId = localStorage.getItem(storageKey);

      if (!visitorId) {
        visitorId = this.generateUUID();
        localStorage.setItem(storageKey, visitorId);
      }

      return visitorId;
    },

    /**
     * 初始化访客并记录页面访问
     */
    initializeVisitor() {
      try {
        // 获取或创建访客ID
        this.visitorId = this.getOrCreateVisitorId();

        // 记录页面访问
        this.recordPageViewAsync();
      } catch (error) {
        console.error('初始化访客失败:', error);
      }
    },

    /**
     * 异步记录页面访问
     */
    async recordPageViewAsync() {
      try {
        const pageViewData = {
          agentCode: this.agentCode || '',
          visitorId: this.visitorId,
          userAgent: navigator.userAgent,
          referer: document.referrer || ''
        };

        const response = await axios.post(baseUrl + '/product/recordPageView', pageViewData, {
          showLoading: false, // 不显示加载提示，避免影响用户体验
          timeout: 5000 // 5秒超时
        });

        if (response.code === 200) {
          console.log('页面访问记录成功');
        } else {
          console.warn('页面访问记录失败:', response.message);
        }
      } catch (error) {
        // 访问记录失败不影响页面正常功能，只记录错误日志
        console.error('记录页面访问时发生错误:', error.message || error);
      }
    },

    /**
     * 获取页面访问统计（可选功能，供后续扩展使用）
     * @param {string} dateRange 日期范围，默认为'today'
     * @returns {Promise} 统计数据
     */
    async getPageViewStats(dateRange = 'today') {
      try {
        const response = await axios.get(`${baseUrl}/product/getPageViewStats`, {
          params: {
            agentCode: this.agentCode || '',
            dateRange: dateRange
          },
          showLoading: false
        });

        if (response.code === 200) {
          return response.data;
        } else {
          console.warn('获取访问统计失败:', response.message);
          return null;
        }
      } catch (error) {
        console.error('获取页面访问统计时发生错误:', error.message || error);
        return null;
      }
    },
    resolveHomeRedirect() {
      const mode = getQueryString('mode');
      if (mode !== 'order-query') {
        return '';
      }
      const redirectParam = getQueryString('redirect');
      if (redirectParam) {
        try {
          const decodedUrl = decodeURIComponent(redirectParam);
          if (!this.isSamePage(decodedUrl)) {
            return decodedUrl;
          }
        } catch (error) {
          console.warn('Failed to decode redirect parameter:', error);
        }
      }
      if (document.referrer && !this.isSamePage(document.referrer)) {
        return document.referrer;
      }
      return '';
    },
    isSamePage(url) {
      try {
        const target = new URL(url, window.location.href);
        const current = new URL(window.location.href);
        const stripHash = (value) => value.replace(/#.*$/, '');
        return stripHash(target.origin + target.pathname + target.search)
          === stripHash(current.origin + current.pathname + current.search);
      } catch (error) {
        return false;
      }
    }
  }
});
