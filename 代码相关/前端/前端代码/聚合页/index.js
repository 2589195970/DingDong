Vue.use(vant.Field);
Vue.use(vant.Button);
Vue.use(vant.Checkbox);
Vue.use(vant.Dialog);
Vue.use(vant.Popup);
Vue.use(vant.Area);
Vue.use(vant.Radio);
Vue.use(vant.RadioGroup);
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
      phone: '',
      certId: '',
      productStatus:'1',
      shareStoreopon: false,
      agentCode: '',
      ShopQrcodeMap: '',

    };
  },
  computed: {

  },

  created() {
    this.agentCode= getQueryString('agentCode')
    this.soplist();
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
        productStatus:this.productStatus,
      }).then(res => {
        this.productList = res.data;
      })
    },
    Receive(data) {
      window.location.href = data;
    },
    timestampToDateString(timestamp) {
      // 创建一个新的 Date 对象，并传入时间戳（秒）
      const date = new Date(timestamp * 1000); // 时间戳是以秒为单位，因此乘以1000转换为毫秒

      // 使用 Date 对象的方法获取年、月、日、时、分、秒
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始，需要加1，并补齐两位数
      const day = String(date.getDate()).padStart(2, '0'); // 补齐两位数
      const hours = String(date.getHours()).padStart(2, '0'); // 补齐两位数
      const minutes = String(date.getMinutes()).padStart(2, '0'); // 补齐两位数
      const seconds = String(date.getSeconds()).padStart(2, '0'); // 补齐两位数

      // 构建日期字符串
      const dateString = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;

      return dateString;
    }
  }
});









