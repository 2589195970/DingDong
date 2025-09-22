package com.ruoyi.console.service.impl.agent;


import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.dto.SmsDTO;
import com.ruoyi.common.order.entity.AgentAccount;
import com.ruoyi.common.order.entity.ToolConfig;
import com.ruoyi.common.order.vo.AgentApiVO;
import com.ruoyi.common.order.vo.AgentExtendUrlVO;
import com.ruoyi.common.order.vo.AgentInfoVO;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * 代理商推广Url
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentExtendUrlServiceImpl implements AgentExtendUrlService {

    @Resource(name = "configStringRedisTemplate")
    StringRedisTemplate configStringRedisTemplate;

    @Resource
    ToolConfigService toolConfigService;

    @Resource
    AgentAccountService agentAccountService;

    @Resource
    SmsService smsService;

    @Resource
    QrCodeService qrCodeService;

    @Value(value = "${submit.shop}")
    private String shopUrl;

    @Resource
    AgentAccountMapper agentAccountMapper;

    /**
     *
     * 获取推广链接
     * @return
     * @throws BizException
     */
    public AgentExtendUrlVO getAgentExtendUrlVO(LoginUser loginUser) throws BizException {
        AgentExtendUrlVO agentExtendUrlVO = new AgentExtendUrlVO();
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        String code = "?agentCode="+agentAccount.getAgentCode();
        ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.THREE_INT);
        agentExtendUrlVO.setShopUrl(toolConfig.getAccessKey()+code);
        agentExtendUrlVO.setMobileUrl(toolConfig.getSecretKey());
        agentExtendUrlVO.setExtendUrl(toolConfig.getDomainNameUrl()+code);
        //如果不存在则生成一个海报图
        if(StringUtils.isEmpty(agentAccount.getShopQrcodeMap())){
            String content= shopUrl+code;
            String shopQrcodeMap = qrCodeService.getQrCodePosterUrl(content);
            agentExtendUrlVO.setShopQrcodeMap(shopQrcodeMap);
            agentAccount.setShopQrcodeMap(shopQrcodeMap);
            agentAccountMapper.updateById(agentAccount);
        }else {
            agentExtendUrlVO.setShopQrcodeMap(agentAccount.getShopQrcodeMap());
        }
        agentExtendUrlVO.setRegisterQrcodeMap1(agentAccount.getRegisterQrcodeMap1());
        agentExtendUrlVO.setRegisterQrcodeMap2(agentAccount.getRegisterQrcodeMap2());
        agentExtendUrlVO.setRegisterQrcodeMap3(agentAccount.getRegisterQrcodeMap3());
        return agentExtendUrlVO;
    }

    /**
     *
     * 获取API 信息
     * @return
     * @throws BizException
     */
    public AgentApiVO getAgentApiVO(LoginUser loginUser) throws BizException {
        AgentApiVO agentApiVO = new AgentApiVO();
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        agentApiVO.setAgentCode(agentAccount.getAgentCode());
        agentApiVO.setApiUrl("暂无文档地址");
        agentApiVO.setCallbackUrl(agentAccount.getCallbackUrl());
        agentApiVO.setSecurityKey(agentAccount.getSecurityKey());
        return agentApiVO;
    }

    /**
     *
     * 更新url信息
     * @return
     * @throws BizException
     */
    public void updateCallbackUrl(String agentCode,String callbackUrl) throws BizException {
        AgentAccount agentAccount = agentAccountService.getAgentAccountByCode(agentCode,true);
        agentAccount.setCallbackUrl(callbackUrl);
        agentAccount.setUpdateTime(System.currentTimeMillis());
        agentAccountService.updateById(agentAccount);
        //修改之后删除缓存
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        configStringRedisTemplate.delete(cacheKey);
    }


    /**
     *
     * 获取账号信息
     * @return
     * @throws BizException
     */
    public AgentInfoVO getAgentInfoVO(LoginUser loginUser) throws BizException {
        AgentInfoVO agentInfoVO = new AgentInfoVO();
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        agentInfoVO.setAgentCode(agentAccount.getAgentCode());
        agentInfoVO.setIsRealName(agentAccount.getIsRealName());
        agentInfoVO.setPhone(agentAccount.getPhone());
        return agentInfoVO;
    }


    /**
     *
     * 修改账号手机号
     * @return
     * @throws BizException
     */
    public void updateAgentPhone(LoginUser loginUser,String phone,String smsCode) throws BizException {
        //校验验证码
        SmsDTO smsDTO = new SmsDTO();
        smsDTO.setSmsCode(smsCode);
        smsDTO.setSmsTemplateType(BaseConstant.ONE_INT);
        smsDTO.setPhoneNumber(phone);
        smsService.checkSms(smsDTO);
        AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(),true);
        agentAccountService.updateAgentPhone(agentAccount.getAgentCode(),phone);
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_USER_ID, agentAccount.getSysUserId());
        configStringRedisTemplate.delete(cacheKey);
    }

}
