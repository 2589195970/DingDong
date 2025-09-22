<template>
    <div>
        <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" style="margin: 10px"
                v-hasPermi="['channel:channelManagement:add']">新增</el-button>
        </el-col>
        <el-table ref="tables" v-loading="loading" :data="list" row-key="operatorReportId" border lazy
            :tree-props="{ children: 'children' }" height="650">
            <el-table-column label="策略" align="center" prop="tactics" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.tactics==0">允许</p>
                    <p v-else>禁止</p>
                </template>
            </el-table-column>
            <el-table-column label="级别" align="center" prop="territoryCheckType" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p v-if="scope.row.territoryCheckType==0">省级</p>
                    <p v-else>市级</p>
                </template>
            </el-table-column>
            <el-table-column label="省" align="center" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{biProvinceName(scope.row.territoryProvinceJson)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="市" align="center" prop="companySimpleName" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                    <p>{{biCityName(scope.row.territoryCityJson)}}</p>
                </template>
            </el-table-column>
            <el-table-column label="时间" align="center" prop="productMasterMap">
                <template slot-scope="scope">
                    <p>{{formatTimestamp(scope.row.createTime)}}</p>
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作" width="150" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button @click="handleupdate(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:edit']">编辑</el-button>
                    <el-button @click="handleDelete(scope.row)" type="text" size="small"
                        v-hasPermi="['channel:channelManagement:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-dialog :visible.sync="open" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="form" label-width="50px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="策略">
                            <el-select v-model="form.tactics" placeholder="策略" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="地域">
                            <el-select v-model="form.territoryCheckType" placeholder="地域" clearable filterable
                                style="width: 240px">
                                <el-option v-for="dict in orderSource1" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="省" v-if="form.territoryCheckType==0">
                            <el-select v-model="form.territoryProvince" placeholder="省" clearable filterable multiple
                                style="width: 240px">
                                <el-option v-for="dict in territoryCityJson" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="省" v-if="form.territoryCheckType==1">
                            <el-select v-model="form.sfterritoryCityList" placeholder="省" clearable filterable
                                style="width: 240px" @change="canq(form.sfterritoryCityList)">
                                <el-option v-for="dict in territoryCityJson" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="市" v-if="form.territoryCheckType==1">
                            <el-select v-model="form.territoryCity" placeholder="市" clearable filterable multiple
                                style="width: 240px">
                                <el-option v-for="dict in territoryCityJson1" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog :visible.sync="openupdate" width="350px" append-to-body :close-on-click-modal="false">
            <el-form ref="form" v-model="formupdate" label-width="50px">
                <el-row>
                    <el-col :span="24">
                        <el-form-item prop="productCommission" label="策略">
                            <el-select v-model="formupdate.tactics" placeholder="策略" clearable filterable
                                style="width: 240px" disabled>
                                <el-option v-for="dict in orderSource" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="地域">
                            <el-select v-model="formupdate.territoryCheckType" placeholder="地域" clearable filterable
                                style="width: 240px" disabled>
                                <el-option v-for="dict in orderSource1" :key="dict.id" :label="dict.name"
                                    :value="dict.id" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="省" v-if="formupdate.territoryCheckType==0">
                            <el-select v-model="formupdate.territoryProvince" placeholder="省" clearable filterable
                                multiple style="width: 240px"  @change="$forceUpdate()">
                                <el-option v-for="dict in territoryCityJson" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="省" v-if="formupdate.territoryCheckType==1">
                            <el-select v-model="formupdate.sfterritoryCityList" placeholder="省" clearable filterable
                                style="width: 240px" disabled @change="canq(formupdate.sfterritoryCityList)">
                                <el-option v-for="dict in territoryCityJson" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                        <el-form-item prop="productCommission" label="市" v-if="formupdate.territoryCheckType==1">
                            <el-select v-model="formupdate.territoryCity" placeholder="市" clearable filterable multiple
                                style="width: 240px" @change="$forceUpdate()">
                                <el-option v-for="dict in territoryCityJson1" :key="dict.code" :label="dict.name"
                                    :value="dict.code" />
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitupdate">确 定</el-button>
            </div>
        </el-dialog>

    </div>

</template>

<script>
    import { provinceAndCityData } from 'element-china-area-data'
    import { selectProductCheckConfigListPage, addProductCheckConfig, updateProductCheckConfig, deleteProductCheckConfig, selectAddressList } from "@/api/monitor/business";
    export default {
        data() {
            return {
                provinceAndCityData,
                selectedOptions: [],
                uploadUrl: process.env.VUE_APP_BASE_API + "/picture/addPicture", // 上传的图片服务器地址
                imageUrl: false,
                upstreamApiCode: [],
                // 遮罩层
                loading: false,
                open: false,
                openupdate: false,
                upstreamProductCode: [],
                list: [],
                formupdate: {},
                form: {
                    checkConfigType: 1,
                },


                ruleForm: {
                    name: '',
                    region: '',
                    date1: '',
                    date2: '',
                    delivery: false,
                    type: [],
                    upstreamProductCode: '',
                    upstreamApiCode: '',
                    resource: '1',
                    desc: ''
                },
                orderSource: [
                    {
                        name: "允许",
                        id: 0
                    },
                    {
                        name: "禁止",
                        id: 1
                    },
                ],
                orderSource1: [
                    {
                        name: "省级",
                        id: 0
                    },
                    {
                        name: "市级",
                        id: 1
                    },
                ],
                territoryCityJson: [],
                territoryCityJson1: [],
            };
        },
        created() {
            console.log(this.$route.params);
            if (this.$route.params) {
                this.canshu();
                this.form.productCode = this.$route.params.productCode;
            }



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
            canq(data) {
                this.form.territoryCity = undefined;
                selectAddressList({ provinceCode: data }).then((res) => {
                    this.territoryCityJson1 = res.data.addressCacheProvinceBO[0].cityList

                })
            },
            handleAdd() {
                this.open = true;
                selectAddressList({}).then((res) => {
                    this.territoryCityJson = res.data.addressCacheProvinceBO

                })
            },
            handleupdate(data) {
              
                this.formupdate = data;
                selectAddressList({}).then((res) => {
                    this.territoryCityJson = res.data.addressCacheProvinceBO
                })
                if (this.formupdate.territoryCheckType==0) {
                    this.formupdate.territoryProvince=this.biTerritoryCityJson1(data.territoryProvinceJson)
                    console.log(this.formupdate.territoryProvince);
                    
                }else{
                    this.formupdate.sfterritoryCityList=this.biTerritoryCityJson(data.territoryProvinceJson)
                    this.canq(this.formupdate.sfterritoryCityList)
                }
                this.openupdate = true;

            },
            submitupdate(){
                if (this.formupdate.territoryCheckType == 0) {
                    this.formupdate.territoryProvinceList = this.formupdate.territoryProvince.map(code => {
                        const province = this.territoryCityJson.find(item => item.code === code);
                        return {
                            provinceCode: province.code,
                            provinceName: province.name
                        };
                    });
                    console.log(this.formupdate.territoryProvinceList);
                } else {
                    const province1 = this.territoryCityJson.find(item => item.code === this.formupdate.sfterritoryCityList);
                    this.formupdate.territoryProvinceList = province1 ? [{
                        provinceCode: province1.code,
                        provinceName: province1.name
                    }] : [];
                    this.formupdate.territoryCityList = this.formupdate.territoryCity.map(code => {
                        const province = this.territoryCityJson1.find(item => item.code === code);
                        return {
                            cityCode: province.code,
                            cityName: province.name,
                        };
                    });
                    console.log(this.formupdate.territoryProvinceList);
                }
                updateProductCheckConfig(this.formupdate).then((res) => {
                    this.$modal.msgSuccess("修改成功");
                    this.open = false;
                    this.canshu();
                })
            },
            // 遍历
            biProvinceName(data) {
                if (data) {
                    const shu = JSON.parse(data)
                    var date1 = '';
                    // 遍历并输出每个对象的 provinceName
                    shu.forEach(item => {
                        date1 += item.provinceName + ','
                    });
                    console.log(date1);
                    return `${date1}`
                }
            },
            biTerritoryCityJson(data) {
                if (data) {
                    const shu = JSON.parse(data)
                    var date2 = '';
                    // 遍历并输出每个对象的 provinceName
                    shu.forEach(item => {
                        date2 += item.provinceCode
                    });
                    console.log(date2);
                    return `${date2}`

                }

            },
            biTerritoryCityJson1(data) {
                if (data) {
                    const shu = JSON.parse(data)
                    var date3 = [];
                    // 遍历并输出每个对象的 provinceName
                    shu.forEach(item => {
                        date3.push(item.provinceCode)
                    });
                    console.log(date3);
                    return date3

                }

            },
            biCityName(data) {
                if (data) {
                    const shu = JSON.parse(data)
                    var date3 = '';
                    // 遍历并输出每个对象的 provinceName
                    shu.forEach(item => {
                        date3 += item.cityName + ','
                    });
                    console.log(date3);
                    return `${date3}`

                }

            },
            canshu() {
                selectProductCheckConfigListPage(this.$route.params).then((res) => {
                    if (res.data.rows) {
                        this.list = res.data.rows
                        this.openupdate = false;
                    } else {
                        this.list = []
                    }
                    this.total = res.data.totalRows
                })
            },
            handleDelete(data) {
                this.$confirm('确认要删除吗？', '删除', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    deleteProductCheckConfig(data.productCheckConfigId).then((res) => {
                        this.$message({
                            type: 'success',
                            message: '删除成功!'
                        });
                        this.canshu();
                    })
                }).catch(() => {
                    this.$message({
                        type: 'info',
                        message: '已取消删除'
                    });
                });
            },
            submitForm() {
                if (this.form.territoryCheckType == 0) {
                    this.form.territoryProvinceList = this.form.territoryProvince.map(code => {
                        const province = this.territoryCityJson.find(item => item.code === code);
                        return {
                            provinceCode: province.code,
                            provinceName: province.name
                        };
                    });
                    console.log(this.form.territoryProvinceList);
                } else {
                    const province1 = this.territoryCityJson.find(item => item.code === this.form.sfterritoryCityList);
                    this.form.territoryProvinceList = province1 ? [{
                        provinceCode: province1.code,
                        provinceName: province1.name
                    }] : [];
                    this.form.territoryCityList = this.form.territoryCity.map(code => {
                        const province = this.territoryCityJson1.find(item => item.code === code);
                        return {
                            cityCode: province.code,
                            cityName: province.name,
                        };
                    });
                    console.log(this.form.territoryProvinceList);
                }
                addProductCheckConfig(this.form).then((res) => {
                    this.$modal.msgSuccess("新增成功");
                    this.open = false;
                    this.canshu();
                })

            },

        }
    }
</script>