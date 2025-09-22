Vue.use(vant.Field);
Vue.use(vant.Button);
Vue.use(vant.Checkbox);
Vue.use(vant.Dialog);
Vue.use(vant.Popup);
Vue.use(vant.Area);

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
      arrow: 'arrow-down',
      border: '',
      num: 24732,
      animate: false,
      duration: 3000,
      thumbs_up: 123,
      config: {
        headerImgList: [],
        bottomExpandImgList: [],
        bottomHiddenImgList: [],
      },
      countdown: 60,

      // 地区选择
      areaVisible: false,
      areaList,

      // 选号
      phonePool: [],
      searchNumber: null,
      selectedNumber: {},

      // 用户协议
      protocolChecked: false,
      protocolVisible1: false,
      protocolVisible2: false,
      protocolVisible: false,
      dialog1Visible: false,
      dialog2Visible: false,
      successModalVisible: false,
      detailVisible: false,
      backTopBtnVisible: false,
      successModalVisible1: false,
      successModalVisible6: false,
      s: '',
      positioningcity: '',
      city: '',
      a: '',
      h: true,

      boos: true,
      province: '',
      name: '',
      idCardNo: '',
      phone: '',
      verifyCode: '',
      region: '',
      region1: '',
      regionDetail: '',
      address: '',
      currentPage: parseInt(Math.random() * 49),
      chooseDiv: false,
      time: 120,

      // html不走cdn，放在js里
      protocolHtml1: '',
      protocolHtml2: '',
      achieveGdPcd: true,
      xiaos: true,
      xiaos1: false,
      xiaos2: false,
      dizhi: '请选择/请选择:',
      // 评论轮播图
      animation: false,
      period: 2000,
      patrn: /[`~!@#$%^&*()\+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\+={}|《》？：“”【】、；‘'，。、]/im,

      activeClass: -1,
      noneDiv: false,

      time: ['7596', '7460', '7748', '7656', '7512', '7431', '7756', '7671', '7557', '7410'],
      timeNew: '',
      popupTimeHour: '11',
      numberOccupyDiv: 'block',
      popupTimeMinute: '27',
      popupTimeSecond: '50',
      productList: {}

    };
  },
  computed: {
    borders: function () {
      if (this.address.length > 3 && this.address.length < 7) {
        return true;
      }
    },

    addressVisible: function () {
      if (this.phone.length == 11) {
        return true;
      }
    },
  },

  created() {
    axios.post(baseUrl + '/product/h5Info', {
      productCode: getQueryString('productCode')
    }).then(res => {
      this.productList = res.data;
      this.productList.productTemplateJson = JSON.parse(this.productList.productTemplateJson);
      document.body.style.setProperty('--bgThemeColor', this.productList.productTemplateJson.bgThemeColor);
    })
    // 从浏览器本地获取用户信息
    this.name = sessionStorage.getItem('name')
    this.phone = sessionStorage.getItem('phone')
    this.address = sessionStorage.getItem('address')
    this.idCardNo = sessionStorage.getItem('idCardNo')
    if (sessionStorage.getItem('region') != null) {
      this.region = JSON.parse(sessionStorage.getItem('region')).map(item => item.name).join(' ');
      this.regionDetail = JSON.parse(sessionStorage.getItem('region'));
    }
  },

  mounted() {
    checkCode = function (val) {
      var p = /^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9X]$/;
      var factor = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
      var parity = [1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2];
      var code = val.substring(17);
      if (p.test(val)) {
        var sum = 0;
        for (var i = 0; i < 17; i++) {
          sum += val[i] * factor[i];
        }
        if (parity[sum % 11] == code.toUpperCase()) {
          return true;
        }
      }
      return false;
    };
    checkDate = function (val) {
      var pattern = /^(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)$/;
      if (pattern.test(val)) {
        var year = val.substring(0, 4);
        var month = val.substring(4, 6);
        var date = val.substring(6, 8);
        var date2 = new Date(year + "-" + month + "-" + date);
        if (date2 && date2.getMonth() == (parseInt(month) - 1)) {
          return true;
        }
      }
      return false;
    };
    checkProv = function (val) {
      var pattern = /^[1-9][0-9]/;
      var provs = { 11: "北京", 12: "天津", 13: "河北", 14: "山西", 15: "内蒙古", 21: "辽宁", 22: "吉林", 23: "黑龙江 ", 31: "上海", 32: "江苏", 33: "浙江", 34: "安徽", 35: "福建", 36: "江西", 37: "山东", 41: "河南", 42: "湖北 ", 43: "湖南", 44: "广东", 45: "广西", 46: "海南", 50: "重庆", 51: "四川", 52: "贵州", 53: "云南", 54: "西藏 ", 61: "陕西", 62: "甘肃", 63: "青海", 64: "宁夏", 65: "新疆", 71: "台湾", 81: "香港", 82: "澳门" };
      if (pattern.test(val)) {
        if (provs[val]) {
          return true;
        }
      }
      return false;
    };
    checkID = function (val) {
      if (this.checkCode(val)) {
        var date = val.substring(6, 14);
        if (checkDate(date)) {
          if (checkProv(val.substring(0, 2))) {
            return true;
          }
        }
      }
      return false;
    };
    this.fillData();
  },
  methods: {
    thumbs(data) {
      console.log(data);
      if (data.color == '#000') {
        data.color = 'red'
        data.thumbs_up++
      } else {
        data.color = '#000'
        data.thumbs_up--
      }
    },
    focusName() {
      this.xiaos = false
    },
    loseName() {
      if (!this.name) {
        this.xiaos1 = true
      }
      if (this.name) {
        this.xiaos1 = false
      }
      var reg = /[^\u4e00-\u9fa5]/g;
      var str = this.name
      if (reg.test(str)) {
        vant.Toast('请输入中文')
      }
      if (this.name.length < 2) {
        vant.Toast('请输入正确的名字')
      }
    },
    loseAddress() {
      if (this.address.length < 4) {
        vant.Toast('请输入正确的地址')
        return;
      }
    },
    lose() {
      if (!this.phone) {
        this.xiaos2 = true
      }
      if (this.phone) {
        this.xiaos2 = false
      }
      this.protocolChecked = true
    },

    scrollToTop() {
      document.scrollingElement.scrollTop = 0;
    },

    chooseDivNone() {
      this.chooseDiv = false;
      this.time = 120;
    },
    handleSelectNumber(num) {
      this.selectedNumber = num;
    },
    handleAreaConfirm(area) {
      console.log(area);
      this.region = area.map(item => item.name).join(' ');
      this.regionDetail = area;
      this.dizhi = this.regionDetail[1].name + "/" + this.regionDetail[2].name + ':';
      this.areaVisible = false;
    },


    submit() {
      var reg = /[^\u4e00-\u9fa5]/g;
      var str = this.name;
      if (reg.test(str)) {
        vant.Toast('请输入中文姓名')
        return;
      }
      if (!this.name) {
        vant.Toast('请输入您的姓名');
        return;
      }
      if (this.name.length > 20) {
        vant.Toast('姓名应小于20字');
        return;
      }
      if (!this.phone) {
        vant.Toast('请输入收货手机号码');
        return;
      }
      if (!checkPhone(this.phone)) {
        vant.Toast('请输入正确的收货手机号码');
        return;
      }
      if (this.address.length < 7) {
        vant.Toast('详细地址不能小于7个字符');
        return;
      }
      if (!this.idCardNo) {
        vant.Toast('请输入身份证号码');
        return;
      }
      if (!checkID(this.idCardNo)) {
        vant.Toast('请输入正确的身份证号码');
        return;
      }
      if (!this.region) {
        vant.Toast('请选择配送地区');
        return;
      }
      if (!this.protocolChecked) {
        vant.Toast('请阅读并同意用户协议');
        return;
      }
      if (!this.name) {
        vant.Toast('请输入您的姓名');
        return;
      }
      this.ageLimit();
    },
    fillData() {
      this.protocolHtml1 = '《入网及用户协议》<br>\n《入网协议》<br>\n甲方与乙方经自愿协商一致，就电信服务的相关事宜达成如下协议：<br><br>\n一、 服务内容<br>\n1. 甲方自主选择乙方提供的电信服务，乙方在通信网络覆盖范围内，按照国家颃布的 《电信服务规范》标准为甲方有偿提供其所选择的电信服务。<br>\n2. 电信服务费用根据政府主管部门批准或备案的资费标准及双方的约定执行。计费周期为自然月，即每月1日0时至当月最后一曰24时。<br>\n3. 乙方提供标准资费及各类套餐资费供甲方使用。如选择套餐资费，月通信消费量未超过套餐约定包含的内容及使用量时收取套餐基本费；月通信消费量超出套餐约定包含的内容或使用量时收取套餐基本费及超出费用。<br><br>\n二、 甲方的权利与义务<br>\n1. 甲方有权自主选择乙方提供的各类电信服务，有权选择符合国家入网许可规定的终端设备，并在双方约定的电信服务范围内使用。<br>\n2. 甲方或其委托人办理电信业务时，应提供真实有效的信息资料，并配合乙方进行核实；对身份不明或拒绝身份查验的，乙方有权拒绝提供服务。协议有效期内，甲方登记资料如有变更，应及时办理变更手续。<br>\n3. 甲方应按照与乙方约定的时间和方式，及时足额向乙方支付电信服务费用，否则乙方有权停止电信服务。<br>\n4. 甲方可对暂不使用的电信服务申谓停机，若为套餐资费应将套餐中包含的所有电信服务同时停机。<br>\n5. 甲方负责其自备的终端设备或线路的安装与维修。乙方可协助甲方安装或维修， 按服务标准收取费用。<br>\n6. 甲方使用电信服务时，应遵守国家法律、法规。<br>\n7. 甲方应对其使用的电信业务终端妥善保管，因甲方使用不当而造成的损失，由甲方自行承担；甲方电信业务终端丟失，应及时办理停机手续，办理停机前产生的损失由甲方承担。<br>\n8. 甲方入网后应立即修改初始产品密码，并注意保管。凡使用甲方产品密码定制、变更电信业务的行为均视为甲方行为，责任由甲方承担。<br><br>\n三、 乙方的权利与义务<br>\n1. 乙方应积极、努力的为甲方提供优质的电信服务。<br>\n2. 乙方应向甲方提供业务受理、咨询、查询、障碍申告、投诉等服务渠道。<br>\n3. 乙方应在双方约定的期限内为甲方开通其所申请的电信服务业务，若因客观原因无法为甲方开通的，应及时通知甲方。<br>\n4. 乙方按约定的资费标准向甲方收取电信服务费用，并可代收甲方定制的第三方收费业务之费用。<br>\n5. 乙方应保留甲方的电信服务费用信息5个月，以备甲方查询。<br>\n6. 乙方对甲方办理、使用电信业务产生的各类信息资料依法保密；但为建立与甲方沟通渠道，改善服务工作，乙方可以使用本协议涉及的甲方资料。<br>\n7. 乙方保留因技术进步或国家法律法规、政策变动等原因对电信业务的服务功能、操作方法、业务号码等做出调整的权利，但调整时应提前公告并提供相应解决方案。<br>\n8. 乙方在本协议外不得作出对甲方不公平、不合理的规定，或者减轻、免除乙方损害甲方合法权益应当承担的民事责任。<br><br>\n四、 其他约定<br>\n1.甲方应在乙方自营或授权代理电信业务的经营场所办理电信业务，否则责任自负。<br>\n2.甲方办理各类电信业务的表单均为本协议的补充协议；补充协议与本协议冲突部分 以补充协议为准，补充协议中未约定部分以本协议为准。<br>\n3.乙方提供的标准资费及各类套餐资费有效期为2年，另有约定的以约定为准。期满双方无异议则自动延续，延续期间，一方提出异议即可终止相应资费。<br>\n4.由于不可抗力、政府行为、国家法律法规、规章或政策变动，导致本协议部分或全部无法履行的，双方均不需承担违约责任。<br>\n5.乙方可采用电话、广播、短信、彩信、电子邮件、电视、公开张贴、信函、报纸或 联网等方式进行业务公告、通知。<br>\n6.未经乙方同意并办理过户手续，甲方将协议的全部或部分转让给第三方，对乙方不发生法律效力，责任甶甲方承担。<br><br>\n五、 违约责任<br>\n1.甲方超过交费时限，每曰按欠费金额的3‰向乙方支付违约金。<br>\n2.乙方提供的电信服务若需终端设备具备相应功能支持，应在办理该业务时告知甲方; 对甲方终端设备不具备相应功能而造成的问题，乙方不承担责任。<br>\n3.甲、乙方中一方违约给对方造成损失，应依法按实际损失向守约方承担赔偿责任。 赔偿责任不包括预期利益、商业信誉损失、数据的丟失、第三方损失以及其他间接损失。<br><br>\n六、 协议的变更与终止<br>\n1.自甲方办理电信业务注销或过户手续之曰，协议相应解除或转移。<br>\n2.甲方有下列情形之一者，乙方有权暂停或终止向甲方提供本协议约定的部分或全部服务；若引起甲方损失，乙方不承担责任：<br>\n（1）甲方办理入网、变更手续时提供虚假信息资料；<br>\n（2）甲方办理电信业务时有担保法律关系，违反保证条款或担保人无能力屨行保证义务的；<br>\n（3）甲方发送违法信息，或大量发送骚扰信息、拨打骚扰电话等引起接收方投诉或举报并经查实的；<br>\n（4）甲方自行改变电信业务使用性质或超出双方约定使用范围的；<br>\n（5）甲方使用未取得入网许可，或可能影响网络安全或服务质量设备的；<br>\n（6）甲方欠费停机60日仍未补交通信费用和违约金，或因虚假信息资料被停机60日仍未更正信息资料的；<br>\n（7）甲方被司法、行政机关认定为从事违法活动或其他不当用途的；<br>\n（8）其他违反相关法律、法规、规章的行为。<br>\n3.因技术进步、国家政策变动等原因导致本协议无法继续履行的，本协议应终止或变更。<br>\n4.本协议终止后，乙方收回甲方电信服务号码，并保留向甲方追偿所欠费用的权利。<br>\n七、 争议解决<br>\n因本协议引起的有关争议，双方协商解决。协商不成，双方均可向消费者协会投泝或电信管理部门申坼；也可向有管辖权的人民法院提起诉讼。<br>\n八、 附则<br>\n本协议自甲方确认阅读后自动生效<br>\n《中国电信号卡服务用户协议》<br>\n根据甲方（或称“用户”）与乙方（中国电信）提供的号卡的所属公司（以下称“运营商”）签订的相关入网服务协议，甲乙双方在平等自愿、公平诚信的基础上，达成协议如下：<br>\n提示：在甲方按照注册页面提示填写信息、阅读并勾选“我已阅读同意《号卡服务用户协议》”且提交订单后，或甲方以其他乙方允许的方式实际使用乙方提供的号卡服务时，即表示甲方已充分阅读、理解并同意接受本协议的全部内容的约束。甲方承诺接受并遵守本协议的约定，届时甲方不应以未阅读本协议的内容或未获得乙方对甲方问询的解答等理由，主张本协议无效，或要求撤销本协议。<br><br>\n一、协议范围<br>\n1.本协议由甲方（或称“用户”）与乙方中国电信共同缔结，本协议具有合同效力。<br>\n2.乙方作为相关运营商的产品推广商，为甲方提供运营商产品办理接口及服务，甲方通过乙方的接口及服务，办理运营商产品。甲乙双方享有本协议约定之权利，履行本协议约定之义务。<br>\n3.除另行明确声明外，乙方提供的相关服务均受本协议约束。如果甲方不同意本协议的约定，甲方应立即停止确认程序或停止使用乙方的服务。<br>\n4.本协议内容包括协议正文、法律声明，以及所有乙方已经发布或将来可能发布的各类服务内容、服务标准、使用规则、公告或通知（以下简称“规则”）。所有规则为本协议不可分割的组成部分，与本协议具有同等法律效力。<br><br>\n二、入网要求与账户<br>\n1.主体资格<br>\n甲方确认，甲方办理入网、变更手续时，应提供甲方本人真实有效的身份证件原件，不得冒用他人证件或者使用伪造、变造的证件，否则应自行承担相关后果并赔偿因此给乙方造成的损失。如甲方在办理入网手续时未通过实名认证，乙方有权拒绝为甲方提供服务且本协议自始未生效。<br>\n2.用户信息<br>\n（1）在申请入网时，甲方应当按照法律法规、相关运营商入网服务协议及本协议的要求，按相应页面的提示准确提供甲方的资料信息，并于资料信息变更后一周内通知乙方更新资料信息，以保证甲方所提交资料的真实、完整和准确。甲方对其提交的资料信息的真实性、完整性、正确性承担法律责任。如乙方有合理理由怀疑甲方提供的资料错误、不实、过时或不完整的，乙方有权向甲方发出询问及/或要求改正的通知，甲方应及时提供回应证明材料并/或进行改正和完善，否则乙方有权暂停对甲方提供部分或全部服务直至终止对甲方提供全部服务。乙方对此不承担任何责任，甲方将承担因此产生的任何直接或间接损失及不利后果。<br>\n（2）甲方同意乙方及第三方业务合作机构仅为实现本协议合作之目的收集甲方的个人信息（包括但不限于甲方姓名、身份证号、联系方式等），乙方及第三方业务合作机构收集、存储的甲方个人信息有错误的，甲方有权要求更正，乙方及第三方业务合作机构应当采取措施予以删除或者更正，如乙方及第三方业务合作机构违反法律、行政法规的规定或者双方的约定收集、使用甲方个人信息的，甲方有权要求删除其个人信息。乙方及第三方业务合作机构对甲方的个人信息依法保密，但为了与甲方建立沟通渠道，改善服务质量，甲方授权并同意乙方及第三方业务合作机构可以依法使用甲方的信息与甲方联系。<br>\n（3）为了本协议项下业务更好地开展，甲方同意并接受乙方拥有通过短信、语音呼叫等形式，向甲方发送告知等信息的权利。<br><br>\n三、甲方的权利和义务<br>\n1. 在甲方完成注册程序或以其他乙方允许的方式实际使用乙方服务时，甲方应当具备完全民事权利能力和完全民事行为能力。若甲方不具备前述主体资格，则甲方及甲方的监护人应承担因此而导致的一切后果，且乙方有权向甲方及甲方的监护人索偿因此遭受的相应损失。<br>\n2.甲方同一身份证限购同一类号卡业务的1单套餐，并保持联系电话畅通，如甲方电话无人接听或恶意下单，甲方自行承担运营商不予发货的后果，乙方对此不承担责任。<br>\n3.甲方申请办理号卡业务，应如实填写申请页面相应信息，订单生成后，相关信息不可更改，因填写内容不实、不尽造成运营商审核不通过的，由甲方承担全部责任。<br>\n4.甲方申请办理号卡业务，应当在申请审核通过并收到相关号卡之后及时激活，逾期未激活的，甲方自行承担号卡过期的后果，乙方对此不承担责任。<br>\n5.甲方申请办理号卡业务，甲方在号卡到货后拒收的，甲方自行承担运营商予以退单的后果，乙方对此不承担责任。<br>\n6.甲方申请办理号卡业务，提交申请单后，乙方即转送至上游运营商处理，后续甲方所选号码均为乙方上游运营商确定并邮寄，乙方不承担甲方所选号码与收到号码不一致的责任。<br>\n7.根据运营商要求，部分号卡需收取一定金额的预付费，该预付费为号卡购买费用，不代表预充话费，具体号卡说明以号卡申请的宣传页面为准。甲方承诺，申请号卡时甲方已仔细阅读宣传界面上载明的号卡说明且无异议。<br>\n8.甲方应当按照约定的时间和方式及时、足额地向运营商交纳电信费用。甲方逾期不交纳电信费用的，乙方有权督促甲方补交电信费用，并可按照有关规定向征信机构提供甲方的欠费信息。甲方应依据与运营商签订的相关入网服务协议承担责任，乙方因甲方逾期不交纳电信费用的受到运营商处罚的，甲方应当承担与乙方处罚金额等额的赔偿责任。<br>\n9. 甲方不得利用乙方渠道、利用本次申请的号卡制作、复制、发布、传播含有以下内容的信息或从事下列违法活动，否则乙方有权暂停或限制向甲方提供的服务，直至终止所有服务，并有权向运营商通报，涉嫌违法犯罪的，乙方将报警处理。由此产生的后果，乙方不承担责任：<br>\n（1）反对宪法所确定的基本原则，破坏国家宗教政策，宣扬邪教和封建迷信的；<br>\n（2）危害国家安全、荣誉和利益，泄露国家秘密，颠覆国家政权，破坏国家统一的；<br>\n（3）宣扬恐怖主义、极端主义，煽动民族仇恨、民族歧视，破坏民族团结的；<br>\n（4）散布虚假信息、诈骗信息、谣言，扰乱经济秩序和社会秩序，破坏社会稳定的；<br>\n（5）散布涉及实施诈骗，制作或销售违禁物品、管制物品，淫秽、色情、赌博、暴力、凶杀、恐怖或教唆犯罪的；<br>\n（6）侮辱或诽谤他人，侵害他人名誉、隐私、知识产权和其他合法权益的；<br>\n（7）发送违法信息或未经接受方同意发送骚扰信息或商业性信息的；<br>\n（8）其他利用乙方渠道、利用本次申请的号卡制作、复制、发布、传播违法犯罪信息或进行违法活动的行为。<br>\n10.依据《征信业管理条例》及相关法律法规，为确保甲方手机号码、姓名、身份证号三要素一致，符合办理相应号卡业务条件，甲方授权乙方向第三方支付/征信/金融机构或通过上述机构向其他拥有合法资质的第三方（具有资质的征信机构、电信运营商及其代理商和关联公司、公安部身份信息查询中心）合法了解、获取、核实甲方的信用信息及个人信息。';
      this.protocolHtml2 = '尊敬的客户： <br>\n根据《中华人民共和国反恐怖主义法》、《全国人民代表大会常务委员会关于加强网络信息保护的决定》、《电信和互联网用户个人信息保护规定》（工业和信息化部令第24号）和《电话用户真实身份信息登记规定》（工业和信息化部令第25号）等国家法律法规的要求，客户在中国电信集团股份有限公司各类营业网点（含自有营业厅、手机营业厅、网上营业厅、授权合作代理商等）办理固定电话、移动电话（含无线上网卡）入网、过户以及需要出示客户证件的有关业务时，客户应配合出示有效证件原件并进行查验、登记，登记信息包括姓名、证件类型、号码及地址等。同时，为更好地提供服务，需要客户提供如联系人、联系电话、通信地址、电子邮箱等信息。客户本人持有效证件可通过自有营业厅查询和/或更正本人信息，或登录手机营业厅查询本人信息。<br> \n如客户拒绝依法提供个人有效证件及真实信息，我公司有权不提供服务或终止服务。 <br>\n为向客户提供优质、个性化的服务，包括但不限于提供通信服务、保障通信服务安全、改善服务质量、推介个性化产品等，我公司将遵循合法、正当、必要的原则，按照法律法规规定和/或协议约定使用留存客户个人信息，并妥善保管，严格保密，依法保护客户个人信息，非因下列事由，不对外泄露客户个人信息： <br>\n(a)事先获得客户的明确授权； <br>\n(b)根据有关的法律法规要求； <br>\n(c)按照相关司法机关和/或政府主管部门的要求； <br>\n(d)为维护社会公众的利益所必需且适当； <br>\n(e)为维护我公司或客户的合法权益所必需且适当。 <br>\n中国电信股集团有限公司<br>';
    },
    // 隐藏规则
    noneHref() {
      this.noneDiv = !this.noneDiv;
    },
    // 填写联系提示展示下面部分
    regionShow() {
      if (this.phone.length == 11) {
        this.regionNone = 'flex';
      }
    },
    // 年龄限制
    ageLimit() {

      // var date = new Date();
      // var year = date.getFullYear();
      // var birthday_year = parseInt(this.idCardNo.substr(6, 4));
      // var userage = year - birthday_year;
      // if (userage > 60 || userage < 17) {
      //   console.log(userage);
      //   vant.Toast('对不起，此身份信息不符合办理标准');
      //   tijiao = false
      // }

      // if (tijiao) {
      const postData = {

        // 姓名
        cardName: this.name,
        //  地址省
        provinceCode: this.regionDetail[0].code,
        provinceName: this.regionDetail[0].name,
        // 地址市
        cityCode: this.regionDetail[1].code,
        cityName: this.regionDetail[1].name,
        //  地址区
        countyCode: this.regionDetail[2].code,
        countyName: this.regionDetail[2].name,
        cardAddress: this.address.replace(/\n/g, ''),
        // 身份证
        cardId: this.idCardNo,
        // 手机号
        productCode: this.productList.productCode,

        agentCode: getQueryString('agentCode'),
        cardPhone: this.phone,
        jsonParam: JSON.stringify({
          ...getUrlParamObj(),
          link: encodeURIComponent(window.location.href),
        })

      };
      axios.post(baseUrl + '/order/submitInfo', postData).then(res => {
        if (res.code !== 200) {
          return;
        }
        this.successModalVisible = true;
        // js2();
        // setTimeout(() => {
        //   if (res.data && res.data.href) {
        //     window.location.href = res.data.href;
        //   }
        // }, 1000);
      })
      sessionStorage.setItem('name', this.name);
      sessionStorage.setItem('phone', this.phone);
      sessionStorage.setItem('address', this.address);
      sessionStorage.setItem('idCardNo', this.idCardNo);
      let objStr = JSON.stringify(this.regionDetail)
      sessionStorage.setItem('region', [objStr]);

      // }
    },
  }
});









