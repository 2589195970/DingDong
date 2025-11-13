import { uploadOrderPhotos, submitPhotoForAudit, auditOrderPhotos, getH5Config } from '@/api/monitor/business';
import { getToken } from '@/utils/auth';

export default {
  data() {
    return {
      photoStatusOptions: [
        { name: '无需审核', id: 0 },
        { name: '待上传照片', id: 1 },
        { name: '代理商待提交', id: 2 },
        { name: '管理员待审核', id: 3 },
        { name: '审核通过', id: 4 },
        { name: '审核拒绝', id: 5 },
      ],
      photoUpload: {
        open: false,
        title: '',
        orderInfo: '',
        currentFieldType: '',
        currentRow: null,
        form: {
          orderId: null,
          idCardFrontUrl: '',
          idCardBackUrl: '',
          personPhotoUrl: '',
          customPhotoUrl: '',
          remark: '',
        },
        rules: {},
      },
      photoAudit: {
        open: false,
        title: '',
        orderInfo: '',
        currentRow: null,
        form: {
          orderId: null,
          idCardFrontUrl: '',
          idCardBackUrl: '',
          personPhotoUrl: '',
          customPhotoUrl: '',
          auditAction: 1,
          auditRemark: '',
        },
        rules: {
          auditAction: [
            { required: true, message: '请选择审核结果', trigger: 'change' },
          ],
          auditRemark: [
            { required: true, message: '审核备注不能为空', trigger: 'blur' },
          ],
        },
      },
      photoView: {
        open: false,
        title: '',
        orderInfo: '',
        data: {},
      },
      imageUploadConfig: {
        currentField: '',
      },
      photoImageUploadConfig: {
        action: process.env.VUE_APP_BASE_API + '/common/upload',
        headers: { Authorization: 'Bearer ' + getToken() },
      },
      h5PageConfigCache: null,
      h5ConfigPromise: null,
    };
  },
  methods: {
    reloadOrderList() {
      if (typeof this.getList === 'function') {
        this.getList();
      }
    },
    getPhotoStatusName(status) {
      const statusMap = {
        0: '无需审核',
        1: '待上传照片',
        2: '代理商待提交',
        3: '管理员待审核',
        4: '审核通过',
        5: '审核拒绝',
      };
      return statusMap[status] || '--';
    },
    getPhotoStatusTagType(status) {
      const typeMap = {
        0: 'info',
        1: 'warning',
        2: 'primary',
        3: 'danger',
        4: 'success',
        5: 'danger',
      };
      return typeMap[status] || 'info';
    },
    shouldShowPhotoAuditInfo(row) {
      return row && row.photoStatus !== undefined && row.photoStatus !== null && row.photoStatus !== 0;
    },
    shouldShowPhotoAuditButtons(row) {
      return this.shouldShowPhotoAuditInfo(row);
    },
    parsePhotoConfig(photoConfigStr) {
      try {
        if (!photoConfigStr) {
          return this.getDefaultPhotoConfig();
        }
        return JSON.parse(photoConfigStr);
      } catch (e) {
        console.error('解析照片配置失败:', e);
        return this.getDefaultPhotoConfig();
      }
    },
    getDefaultPhotoConfig() {
      return [
        { photoType: 1, photoTypeName: '身份证正面', required: 1, title: '身份证正面照片', description: '请上传清晰的身份证正面照片' },
        { photoType: 2, photoTypeName: '身份证反面', required: 1, title: '身份证反面照片', description: '请上传清晰的身份证反面照片' },
        { photoType: 3, photoTypeName: '免冠照片', required: 1, title: '免冠照片', description: '请上传近期免冠照片' },
        { photoType: 4, photoTypeName: '自定义照片', required: 0, title: '自定义照片', description: '根据需要上传自定义照片' },
      ];
    },
    shouldShowPhotoField(row, fieldType) {
      if (!row) {
        return true;
      }
      if (!row.photoConfig) {
        return true;
      }
      const photoConfig = this.parsePhotoConfig(row.photoConfig);
      const photoTypeMap = {
        idCardFrontUrl: 1,
        idCardBackUrl: 2,
        personPhotoUrl: 3,
        customPhotoUrl: 4,
      };
      const photoType = photoTypeMap[fieldType];
      if (!photoType) {
        return true;
      }
      const configItem = photoConfig.find((item) => item.photoType === photoType);
      return !configItem || configItem.required !== 0;
    },
    getPhotoFieldTitle(fieldType) {
      const titleMap = {
        idCardFrontUrl: '身份证正面照片',
        idCardBackUrl: '身份证反面照片',
        personPhotoUrl: '免冠照片',
        customPhotoUrl: '自定义照片',
      };
      return titleMap[fieldType] || '照片';
    },
    isPhotoFieldRequired(row, fieldType) {
      if (!row) {
        return fieldType !== 'customPhotoUrl';
      }
      if (!row.photoConfig) {
        return fieldType !== 'customPhotoUrl';
      }
      const photoConfig = this.parsePhotoConfig(row.photoConfig);
      const photoTypeMap = {
        idCardFrontUrl: 1,
        idCardBackUrl: 2,
        personPhotoUrl: 3,
        customPhotoUrl: 4,
      };
      const photoType = photoTypeMap[fieldType];
      if (!photoType) {
        return false;
      }
      const configItem = photoConfig.find((item) => item.photoType === photoType);
      return configItem ? configItem.required === 1 : false;
    },
    handlePhotoUpload(row) {
      this.photoUpload.title = '上传订单照片';
      this.photoUpload.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
      this.photoUpload.currentRow = row;
      this.photoUpload.form.orderId = row.orderId;
      this.photoUpload.form.idCardFrontUrl = row.idCardFrontUrl || '';
      this.photoUpload.form.idCardBackUrl = row.idCardBackUrl || '';
      this.photoUpload.form.personPhotoUrl = row.personPhotoUrl || '';
      this.photoUpload.form.customPhotoUrl = row.customPhotoUrl || '';
      this.photoUpload.form.remark = '';
      this.updatePhotoUploadRules(row);
      this.photoUpload.open = true;
    },
    updatePhotoUploadRules(row) {
      const rules = {};
      if (this.shouldShowPhotoField(row, 'idCardFrontUrl') && this.isPhotoFieldRequired(row, 'idCardFrontUrl')) {
        rules.idCardFrontUrl = [
          { required: true, message: '身份证正面照片不能为空', trigger: 'blur' },
        ];
      }
      if (this.shouldShowPhotoField(row, 'idCardBackUrl') && this.isPhotoFieldRequired(row, 'idCardBackUrl')) {
        rules.idCardBackUrl = [
          { required: true, message: '身份证反面照片不能为空', trigger: 'blur' },
        ];
      }
      if (this.shouldShowPhotoField(row, 'personPhotoUrl') && this.isPhotoFieldRequired(row, 'personPhotoUrl')) {
        rules.personPhotoUrl = [
          { required: true, message: '免冠照片不能为空', trigger: 'blur' },
        ];
      }
      if (this.shouldShowPhotoField(row, 'customPhotoUrl') && this.isPhotoFieldRequired(row, 'customPhotoUrl')) {
        rules.customPhotoUrl = [
          { required: true, message: '自定义照片不能为空', trigger: 'blur' },
        ];
      }
      this.photoUpload.rules = rules;
    },
    handleSubmitPhoto(row) {
      this.$confirm('确认要提交此订单的照片进行审核吗？', '提交审核', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        submitPhotoForAudit({
          orderId: row.orderId,
          idCardFrontUrl: row.idCardFrontUrl,
          idCardBackUrl: row.idCardBackUrl,
          personPhotoUrl: row.personPhotoUrl,
          customPhotoUrl: row.customPhotoUrl,
          remark: '代理商提交审核',
        }).then(() => {
          this.$message({ type: 'success', message: '提交审核成功!' });
          this.reloadOrderList();
        }).catch(() => {
          this.$message({ type: 'error', message: '提交审核失败!' });
        });
      }).catch(() => {});
    },
    handlePhotoAudit(row) {
      this.photoAudit.title = '审核订单照片';
      this.photoAudit.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
      this.photoAudit.currentRow = row;
      this.photoAudit.form.orderId = row.orderId;
      this.photoAudit.form.idCardFrontUrl = row.idCardFrontUrl;
      this.photoAudit.form.idCardBackUrl = row.idCardBackUrl;
      this.photoAudit.form.personPhotoUrl = row.personPhotoUrl;
      this.photoAudit.form.customPhotoUrl = row.customPhotoUrl;
      this.photoAudit.form.auditAction = 1;
      this.photoAudit.form.auditRemark = '';
      this.photoAudit.open = true;
    },
    handleViewPhotos(row) {
      this.photoView.title = '查看订单照片';
      this.photoView.orderInfo = `订单ID: ${row.orderId} | 用户: ${row.cardName} | 产品: ${row.productName}`;
      this.photoView.data = {
        ...row,
        idCardFrontUrl: row.idCardFrontUrl,
        idCardBackUrl: row.idCardBackUrl,
        personPhotoUrl: row.personPhotoUrl,
        customPhotoUrl: row.customPhotoUrl,
        photoAuditTime: row.photoAuditTime,
        photoAuditRemark: row.photoAuditRemark,
      };
      this.photoView.open = true;
    },
    getPhotoPreviewList() {
      const photos = [];
      if (this.photoView.data.idCardFrontUrl) photos.push(this.photoView.data.idCardFrontUrl);
      if (this.photoView.data.idCardBackUrl) photos.push(this.photoView.data.idCardBackUrl);
      if (this.photoView.data.personPhotoUrl) photos.push(this.photoView.data.personPhotoUrl);
      if (this.photoView.data.customPhotoUrl) photos.push(this.photoView.data.customPhotoUrl);
      return photos;
    },
    hasAnyPhotos(row) {
      return !!(row.idCardFrontUrl || row.idCardBackUrl || row.personPhotoUrl || row.customPhotoUrl);
    },
    submitPhotoUpload() {
      if (!this.$refs.photoUploadForm) {
        return;
      }
      this.$refs.photoUploadForm.validate((valid) => {
        if (valid) {
          uploadOrderPhotos(this.photoUpload.form).then(() => {
            this.$message({ type: 'success', message: '照片上传成功!' });
            this.photoUpload.open = false;
            this.reloadOrderList();
          }).catch(() => {
            this.$message({ type: 'error', message: '照片上传失败!' });
          });
        }
      });
    },
    submitPhotoAudit() {
      if (!this.$refs.photoAuditForm) {
        return;
      }
      this.$refs.photoAuditForm.validate((valid) => {
        if (valid) {
          auditOrderPhotos(this.photoAudit.form).then(() => {
            this.$message({ type: 'success', message: '照片审核完成!' });
            this.photoAudit.open = false;
            this.reloadOrderList();
          }).catch(() => {
            this.$message({ type: 'error', message: '照片审核失败!' });
          });
        }
      });
    },
    handleUploadImage(fieldType) {
      this.imageUploadConfig.currentField = fieldType;
      if (this.$refs.imageUpload && this.$refs.imageUpload.$el) {
        const input = this.$refs.imageUpload.$el.querySelector('input[type="file"]');
        if (input) {
          input.click();
        }
      }
    },
    handleImageUploadSuccess(response) {
      if (response.code === 200) {
        const url = response.url || response.fileName || response.data?.url;
        this.photoUpload.form[this.imageUploadConfig.currentField] = url;
        this.$message.success('图片上传成功');
      } else {
        this.$message.error(response.msg || '图片上传失败');
      }
    },
    beforeImageUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!');
      }
      return (isJPG || isPNG) && isLt2M;
    },
    handleCopyEditLink(row) {
      this.getH5PageConfig().then((h5Config) => {
        const productCode = row.productCode || '';
        const orderId = row.orderId;
        const baseUrl = h5Config.h5BaseUrl;
        const h5Path = '/index.html';
        const editLink = `${baseUrl}${h5Path}?productCode=${productCode}&mode=edit&orderId=${orderId}`;
        const textArea = document.createElement('textarea');
        textArea.value = editLink;
        document.body.appendChild(textArea);
        textArea.select();
        try {
          const successful = document.execCommand('copy');
          if (successful) {
            this.$message.success('编辑链接已复制到剪贴板！');
          } else {
            this.$message.error('复制失败，请手动复制：' + editLink);
          }
        } catch (err) {
          this.$message.error('复制失败，请手动复制：' + editLink);
        }
        document.body.removeChild(textArea);
      }).catch((error) => {
        console.error('获取H5配置失败:', error);
        this.$message.error('获取H5页面配置失败，无法生成编辑链接');
      });
    },
    getH5PageConfig() {
      if (this.h5PageConfigCache) {
        return Promise.resolve(this.h5PageConfigCache);
      }
      if (this.h5ConfigPromise) {
        return this.h5ConfigPromise;
      }
      this.h5ConfigPromise = getH5Config().then((res) => {
        if (res && res.data) {
          this.h5PageConfigCache = res.data;
          return res.data;
        }
        throw new Error('获取配置信息失败');
      }).catch((error) => {
        this.h5ConfigPromise = null;
        throw error;
      });
      return this.h5ConfigPromise;
    },
  },
};
