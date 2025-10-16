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
      // 基本信息
      productList: {},
      editOrderId: null,
      orderInfo: null,

      // 用户信息（只读显示）
      name: '',
      phone: '',
      idCardNo: '',
      region: '',
      address: '',

      // 照片上传相关
      photoConfigList: [],
      uploadedPhotos: {},
      imagePreviewVisible: false,
      previewImageUrl: '',

      // 弹窗状态
      successModalVisible: false,
    };
  },

  created() {
    // 获取订单ID
    this.editOrderId = getQueryString('orderId');

    if (!this.editOrderId) {
      vant.Toast('订单ID不能为空');
      this.goBack();
      return;
    }

    // 加载订单信息
    this.loadOrderData();
  },

  mounted() {
    // 设置页面标题
    document.title = '订单照片修改 - 叮咚号卡';
  },

  methods: {
    // 加载订单数据
    loadOrderData() {
      vant.Toast.loading({
        duration: 0,
        message: '加载中...',
        forbidClick: true,
      });

      // 同时加载产品信息和订单信息
      axios.all([
        axios.post(baseUrl + '/product/h5Info', {
          productCode: getQueryString('productCode')
        }),
        axios.get(baseUrl + '/order/getOrderInfo', {
          params: { orderId: this.editOrderId }
        })
      ]).then(axios.spread((productRes, orderRes) => {
        vant.Toast.clear();

        if (productRes.code === 200) {
          this.productList = productRes.data;
          this.productList.productTemplateJson = JSON.parse(this.productList.productTemplateJson);
          document.body.style.setProperty('--bgThemeColor', this.productList.productTemplateJson.bgThemeColor);

          // 解析照片配置
          if (this.productList.photoRequired == 1 && this.productList.photoConfig) {
            this.photoConfigList = JSON.parse(this.productList.photoConfig);
          }
        }

        if (orderRes.code === 200) {
          this.orderInfo = orderRes.data;
          this.fillOrderData();
          this.loadExistingPhotos();
        }
      })).catch(() => {
        vant.Toast.clear();
        vant.Toast('加载失败，请重试');
      });
    },

    // 填充订单数据
    fillOrderData() {
      this.name = this.orderInfo.cardName;
      this.phone = this.orderInfo.cardPhone;
      this.idCardNo = this.orderInfo.cardId;
      this.address = this.orderInfo.cardAddress;

      // 填充地区信息
      if (this.orderInfo.provinceName && this.orderInfo.cityName && this.orderInfo.countyName) {
        this.region = `${this.orderInfo.provinceName} ${this.orderInfo.cityName} ${this.orderInfo.countyName}`;
      }
    },

    // 加载已有照片
    loadExistingPhotos() {
      if (!this.orderInfo) return;

      // 根据photoType映射加载已有照片
      const photoMappings = [
        { field: 'idCardFrontUrl', type: 1 },
        { field: 'idCardBackUrl', type: 2 },
        { field: 'personPhotoUrl', type: 3 },
        { field: 'customPhotoUrl', type: 4 }
      ];

      photoMappings.forEach(mapping => {
        if (this.orderInfo[mapping.field]) {
          this.$set(this.uploadedPhotos, mapping.type, {
            url: this.orderInfo[mapping.field],
            preview: this.orderInfo[mapping.field],
            uploading: false,
            photoType: mapping.type
          });
        }
      });
    },

    // 格式化文件大小
    formatFileSize(bytes) {
      if (bytes === 0) return '0 B';
      const k = 1024;
      const sizes = ['B', 'KB', 'MB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(1)) + ' ' + sizes[i];
    },

    // 触发文件选择
    triggerFileInput(photoType) {
      const refName = 'fileInput' + photoType;
      const inputElement = this.$refs[refName];

      console.log('触发文件选择:', refName, inputElement);

      if (inputElement) {
        if (Array.isArray(inputElement)) {
          // 如果是数组，取第一个元素
          inputElement[0].click();
        } else {
          // 如果是单个元素，直接点击
          inputElement.click();
        }
      } else {
        console.error('找不到文件输入元素:', refName);
        // 使用备用方法：动态创建input元素
        this.createFallbackFileInput(photoType);
      }
    },

    // 创建备用文件输入元素
    createFallbackFileInput(photoType) {
      console.log('使用备用文件选择器，照片类型:', photoType);

      const input = document.createElement('input');
      input.type = 'file';
      input.accept = 'image/*';
      input.style.display = 'none';

      // 找到对应的photoConfig
      const photoConfig = this.photoConfigList.find(config => config.photoType === photoType);
      if (!photoConfig) {
        console.error('找不到照片配置:', photoType);
        vant.Toast('照片配置错误，请刷新页面重试');
        return;
      }

      input.addEventListener('change', (event) => {
        this.handleFileSelect(event, photoConfig);
        // 移除临时创建的input
        document.body.removeChild(input);
      });

      // 添加到DOM并触发点击
      document.body.appendChild(input);
      input.click();
    },

    // 处理文件选择
    handleFileSelect(event, photoConfig) {
      const file = event.target.files[0];
      if (!file) {
        console.log('没有选择文件');
        return;
      }

      console.log('选择文件:', file.name, '照片类型:', photoConfig.photoType);

      // 验证文件类型
      const allowedTypes = photoConfig.supportedFormats.split(',').map(type => type.trim());
      const fileExtension = file.name.split('.').pop().toLowerCase();
      if (!allowedTypes.includes(fileExtension)) {
        vant.Toast(`只支持 ${photoConfig.supportedFormats} 格式的图片`);
        return;
      }

      // 验证文件大小
      if (file.size > photoConfig.maxSize * 1024 * 1024) { // 转换为字节
        vant.Toast(`文件大小不能超过 ${this.formatFileSize(photoConfig.maxSize * 1024 * 1024)}`);
        return;
      }

      // 显示上传中状态
      vant.Toast.loading({
        duration: 0,
        message: '上传中...',
        forbidClick: true,
      });

      // 创建FileReader读取图片进行预览
      const reader = new FileReader();
      reader.onload = (e) => {
        const img = new Image();
        img.onload = () => {
          // 验证图片尺寸
          if (photoConfig.minWidth && img.width < photoConfig.minWidth) {
            vant.Toast.clear();
            vant.Toast(`图片宽度不能小于 ${photoConfig.minWidth}px`);
            return;
          }
          if (photoConfig.minHeight && img.height < photoConfig.minHeight) {
            vant.Toast.clear();
            vant.Toast(`图片高度不能小于 ${photoConfig.minHeight}px`);
            return;
          }

          console.log('图片验证通过，准备上传:', {
            width: img.width,
            height: img.height,
            size: file.size
          });

          // 先保存预览信息，标记为上传中状态
          this.$set(this.uploadedPhotos, photoConfig.photoType, {
            file: file,
            preview: e.target.result,
            width: img.width,
            height: img.height,
            name: file.name,
            uploading: true,
            url: null
          });

          // 上传图片到服务器
          this.uploadImage(file, photoConfig.photoType);
        };
        img.onerror = () => {
          vant.Toast.clear();
          vant.Toast('图片加载失败，请选择有效的图片文件');
        };
        img.src = e.target.result;
      };
      reader.onerror = () => {
        vant.Toast.clear();
        vant.Toast('文件读取失败，请重试');
      };
      reader.readAsDataURL(file);

      // 清空input值，以便可以重复选择同一文件
      event.target.value = '';
    },

    // 上传图片到服务器
    uploadImage(file, photoType) {
      const formData = new FormData();
      formData.append('file', file);

      // 添加详细的日志
      console.log('开始上传照片:', {
        fileName: file.name,
        fileSize: file.size,
        photoType: photoType,
        timestamp: new Date().toISOString()
      });

      axios.post(baseUrl + '/common/upload', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        showLoading: false
      }).then(res => {
        vant.Toast.clear();
        console.log('上传接口响应:', res);

        if (res.code === 200 && res.url) {
          // 更新照片信息，包含服务器返回的URL和photoType
          this.$set(this.uploadedPhotos, photoType, {
            ...this.uploadedPhotos[photoType],
            uploading: false,
            url: res.url,
            photoType: photoType  // 确保保存photoType
          });

          console.log('照片上传成功并保存到本地状态:', {
            photoType: photoType,
            url: res.url,
            allPhotos: this.uploadedPhotos
          });

          vant.Toast('上传成功');
        } else {
          // 上传失败，移除该照片
          this.$delete(this.uploadedPhotos, photoType);
          vant.Toast(res.msg || '上传失败，请重试');
        }
      }).catch(error => {
        vant.Toast.clear();
        console.error('图片上传失败:', error);
        // 上传失败，移除该照片
        this.$delete(this.uploadedPhotos, photoType);
        vant.Toast('上传失败，请重试');
      });
    },

    // 移除照片
    removePhoto(photoType) {
      this.$delete(this.uploadedPhotos, photoType);
    },

    // 预览完整图片
    previewFullImage(imageUrl) {
      this.previewImageUrl = imageUrl;
      this.imagePreviewVisible = true;
    },

    // 验证照片上传
    validatePhotos() {
      if (this.productList.photoRequired != 1) return true;

      const requiredPhotos = this.photoConfigList.filter(config => config.required == 1);
      for (let config of requiredPhotos) {
        const photo = this.uploadedPhotos[config.photoType];
        if (!photo || photo.uploading || !photo.url) {
          vant.Toast(`请上传${config.title}`);
          return false;
        }
      }
      return true;
    },

    // 提交修改
    submit() {
      // 验证照片上传
      if (!this.validatePhotos()) {
        return;
      }

      // 准备提交数据
      const postData = {
        orderId: this.editOrderId,
        // 只包含照片相关的数据
      };

      // 添加照片URL数据
      if (this.productList.photoRequired == 1) {
        console.log('=== 开始处理照片数据提交 ===');
        console.log('所有已上传照片:', this.uploadedPhotos);

        // 验证必需照片是否都已上传（只检测required为1的照片）
        const requiredPhotos = this.photoConfigList.filter(config => config.required == 1);
        console.log('必需照片配置:', requiredPhotos);

        let allRequiredPhotosUploaded = true;
        requiredPhotos.forEach(config => {
          const photo = this.uploadedPhotos[config.photoType];
          if (!photo || !photo.url) {
            console.error(`缺少必需照片: ${config.title} (photoType: ${config.photoType})`);
            allRequiredPhotosUploaded = false;
          }
        });

        if (!allRequiredPhotosUploaded) {
          vant.Toast('请上传所有必需的照片');
          return;
        }

        // 根据photoType映射到对应的URL字段
        Object.keys(this.uploadedPhotos).forEach(photoType => {
          const photo = this.uploadedPhotos[photoType];
          if (photo && photo.url) {
            const photoConfig = this.photoConfigList.find(config => config.photoType == photoType);

            switch(parseInt(photoType)) {
              case 1: // 身份证正面
                postData.idCardFrontUrl = photo.url;
                break;
              case 2: // 身份证反面
                postData.idCardBackUrl = photo.url;
                break;
              case 3: // 免冠照片
                postData.personPhotoUrl = photo.url;
                break;
              case 4: // 自定义照片
                postData.customPhotoUrl = photo.url;
                break;
              default:
                console.warn('未知的照片类型:', photoType);
            }
          }
        });

        console.log('=== 照片数据处理完成 ===');
        console.log('最终提交的完整数据:', postData);

        // 显示提交中状态
        vant.Toast.loading({
          duration: 0,
          message: '提交中...',
          forbidClick: true,
        });

        // 发送请求
        axios.post(baseUrl + '/order/updateOrderInfo', postData, {
          showLoading: false
        }).then(res => {
          vant.Toast.clear();
          if (res.code !== 200) {
            return;
          }

          // 编辑模式成功提示
          this.successModalVisible = true;
        }).catch(error => {
          vant.Toast.clear();
          console.error('提交失败:', error);
          vant.Toast('提交失败，请重试');
        });
      }
    },

    // 返回上一页
    goBack() {
      if (window.history.length > 1) {
        window.history.back();
      } else {
        // 如果没有历史记录，可以跳转到指定页面
        window.close();
        // 或者跳转到订单查询页面
        // window.location.href = 'order-query.html';
      }
    }
  }
});