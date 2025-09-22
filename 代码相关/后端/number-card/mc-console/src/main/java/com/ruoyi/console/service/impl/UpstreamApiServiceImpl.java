package com.ruoyi.console.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.id.NanoId;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.core.page.PageResult;
import com.ruoyi.common.core.page.PageResultFactory;
import com.ruoyi.common.enums.UpstreamApiEnum;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.UpstreamApiAddAndUpdateBO;
import com.ruoyi.common.order.bo.UpstreamApiSelectBO;
import com.ruoyi.common.order.entity.UpstreamApi;
import com.ruoyi.common.order.entity.UpstreamExplain;
import com.ruoyi.common.order.vo.UpstreamApiTypeVO;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.UpstreamApiMapper;
import com.ruoyi.console.mapper.UpstreamExplainMapper;
import com.ruoyi.console.service.UpstreamApiService;
import com.ruoyi.framework.web.domain.server.Sys;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
public class UpstreamApiServiceImpl extends ServiceImpl<UpstreamApiMapper, UpstreamApi> implements UpstreamApiService {

    @Resource
    UpstreamExplainMapper upstreamExplainMapper;

    /**
     * 分页查询上游接口列表
     * @return
     * @throws BizException
     */
    public PageResult<UpstreamApi> selectUpstreamApiListPage(UpstreamApiSelectBO upstreamApiSelectBO) throws BizException {
        //读取分页
        Page page = new Page(upstreamApiSelectBO.getPageNo(),upstreamApiSelectBO.getPageSize());
        Page<UpstreamApi> upstreamApiPage  = baseMapper.selectPage(page,new LambdaQueryWrapper<UpstreamApi>()
                .eq(upstreamApiSelectBO.getUpstreamApiId()!=null,UpstreamApi::getUpstreamApiId,upstreamApiSelectBO.getUpstreamApiId())
                .eq(StringUtils.isNotEmpty(upstreamApiSelectBO.getUpstreamApiCode()),UpstreamApi::getUpstreamApiCode,upstreamApiSelectBO.getUpstreamApiCode())
                .like(StringUtils.isNotEmpty(upstreamApiSelectBO.getUpstreamApiName()),UpstreamApi::getUpstreamApiName,upstreamApiSelectBO.getUpstreamApiName())
        );
        return PageResultFactory.createPageResult(upstreamApiPage);
    }


    /**
     * 新增上游接口
     * @return
     * @throws BizException
     */
    @Override
    public void addUpstreamApi(UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) throws BizException {
        if(StringUtils.isEmpty(upstreamApiAddAndUpdateBO.getUpstreamApiType())){
            throw new BizException("上游API类型不能为空");
        }
        UpstreamApiEnum upstreamApiEnum = UpstreamApiEnum.getUpstreamApiEnum(upstreamApiAddAndUpdateBO.getUpstreamApiType());
        UpstreamApi upstreamApi = new UpstreamApi();
        BeanUtil.copyProperties(upstreamApiAddAndUpdateBO,upstreamApi);
        upstreamApi.setUpstreamApiId(null);
        //生成接口编码 10位数
        upstreamApi.setUpstreamApiCode(RandomUtil.randomString(BaseConstant.TEN_INT));
        upstreamApi.setUpstreamApiType(upstreamApiEnum.getUpstreamApiType());
        upstreamApi.setInterfaceClassName(upstreamApiEnum.getInterfaceClassName());
        upstreamApi.setParameterClassName(upstreamApiEnum.getParameterClassName());
        upstreamApi.setIsAsync(upstreamApiEnum.getIsAsync());
        upstreamApi.setCreateTime(System.currentTimeMillis());
        baseMapper.insert(upstreamApi);
    }


    /**
     * 更新上游接口
     * @return
     * @throws BizException
     */
    @Override
    public void updateUpstreamApi(UpstreamApiAddAndUpdateBO upstreamApiAddAndUpdateBO) throws BizException {
        if(upstreamApiAddAndUpdateBO==null||upstreamApiAddAndUpdateBO.getUpstreamApiId()==null){
            throw new BizException("上游接口ID不能为空");
        }
        UpstreamApi upstreamApi = baseMapper.selectById(upstreamApiAddAndUpdateBO.getUpstreamApiId());
        if(upstreamApi==null){
            throw new BizException("ID{}不存在",upstreamApiAddAndUpdateBO.getUpstreamApiId());
        }
        //仅更新参数值
        upstreamApi.setUpstreamApiName(StringUtils.isNotEmpty(upstreamApiAddAndUpdateBO.getUpstreamApiName())?upstreamApiAddAndUpdateBO.getUpstreamApiName():upstreamApi.getUpstreamApiName());
        upstreamApi.setArgument1(upstreamApiAddAndUpdateBO.getArgument1());
        upstreamApi.setArgument2(upstreamApiAddAndUpdateBO.getArgument2());
        upstreamApi.setArgument3(upstreamApiAddAndUpdateBO.getArgument3());
        upstreamApi.setArgument4(upstreamApiAddAndUpdateBO.getArgument4());
        upstreamApi.setArgument5(upstreamApiAddAndUpdateBO.getArgument5());
        upstreamApi.setArgument6(upstreamApiAddAndUpdateBO.getArgument6());
        upstreamApi.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(upstreamApi);
    }

    /**
     * 删除上游接口
     * @return
     * @throws BizException
     */
    @Override
    public void deleteUpstreamApi(Integer upstreamApiId) throws BizException {
        if(upstreamApiId==null){
            throw new BizException("ID不能为空");
        }
        baseMapper.deleteById(upstreamApiId);
    }


    /**
     * 获取上游接口API类型
     * @return
     * @throws BizException
     */
    @Override
    public List<UpstreamApiTypeVO> selectUpstreamApiTypeList(String upstreamApiType) throws BizException {
        List<UpstreamApiTypeVO> upstreamApiTypeVOList = new ArrayList<>();
        if(StringUtils.isNotEmpty((upstreamApiType))){
            UpstreamApiEnum upstreamApiEnum = UpstreamApiEnum.getUpstreamApiEnum(upstreamApiType);
            UpstreamApiTypeVO upstreamApiTypeVO = new UpstreamApiTypeVO();
            upstreamApiTypeVO.setUpstreamApiName(upstreamApiEnum.getUpstreamApiName());
            upstreamApiTypeVO.setApiIntro(upstreamApiEnum.getApiIntro());
            upstreamApiTypeVO.setProductIntro(upstreamApiEnum.getProductIntro());
            upstreamApiTypeVO.setUpstreamApiType(upstreamApiEnum.getUpstreamApiType());
            upstreamApiTypeVOList.add(upstreamApiTypeVO);
            return upstreamApiTypeVOList;
        }
        for (UpstreamApiEnum value : UpstreamApiEnum.values()) {
            UpstreamApiTypeVO upstreamApiTypeVO = new UpstreamApiTypeVO();
            upstreamApiTypeVO.setUpstreamApiName(value.getUpstreamApiName());
            upstreamApiTypeVO.setApiIntro(value.getApiIntro());
            upstreamApiTypeVO.setProductIntro(value.getProductIntro());
            upstreamApiTypeVO.setUpstreamApiType(value.getUpstreamApiType());
            upstreamApiTypeVOList.add(upstreamApiTypeVO);
        }
        return upstreamApiTypeVOList;
    }

    /**
     * 查询接口参数说明
     * @param upstreamApiType
     * @param explainType
     * @return
     */
    public UpstreamExplain selectUpstreamExplain(String upstreamApiType,Integer explainType){
        return upstreamExplainMapper.selectOne(new LambdaQueryWrapper<UpstreamExplain>().eq(UpstreamExplain::getExplainType,explainType).eq(UpstreamExplain::getUpstreamApiType,upstreamApiType));
    }
}
