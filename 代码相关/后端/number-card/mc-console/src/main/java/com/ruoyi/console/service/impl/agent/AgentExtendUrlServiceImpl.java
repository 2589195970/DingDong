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
import com.ruoyi.console.service.PictureService;
import com.ruoyi.console.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
    AgentProductService agentProductService;

    @Resource
    SmsService smsService;

    @Resource
    QrCodeService qrCodeService;

    @Resource
    PictureService pictureService;

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
        // 注释掉缓存删除
        //修改之后删除缓存
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        // configStringRedisTemplate.delete(cacheKey);
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
        // 注释掉缓存删除
        // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_USER_ID, agentAccount.getSysUserId());
        // configStringRedisTemplate.delete(cacheKey);
    }

    /**
     * 重置生成推广海报图
     * @param loginUser
     * @param posterIndex
     * @return
     * @throws BizException
     */
    public String resetPosterImage(LoginUser loginUser, Integer posterIndex) throws BizException {
        try {
            if (posterIndex == null || posterIndex < BaseConstant.ONE_INT || posterIndex > BaseConstant.THREE_INT) {
                throw new BizException("海报图索引必须在1-3之间");
            }

            AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);
            ToolConfig toolConfig = toolConfigService.getToolConfig(BaseConstant.FOUR_INT);

            String templateUrl;
            Integer number;
            switch (posterIndex) {
                case BaseConstant.ONE_INT:
                    templateUrl = toolConfig.getAccessKey();
                    number = BaseConstant.ONE_INT;
                    break;
                case BaseConstant.TWO_INT:
                    templateUrl = toolConfig.getSecretKey();
                    number = BaseConstant.TWO_INT;
                    break;
                case BaseConstant.THREE_INT:
                    templateUrl = toolConfig.getBucket();
                    number = BaseConstant.THREE_INT;
                    break;
                default:
                    throw new BizException("海报图索引必须在1-3之间");
            }

            if (StringUtils.isEmpty(templateUrl)) {
                throw new BizException("系统未配置对应的推广海报模板");
            }

            agentProductService.addRegisterQrcodeMap(agentAccount, templateUrl, number);

            if (posterIndex == BaseConstant.ONE_INT) {
                return agentAccount.getRegisterQrcodeMap1();
            }
            if (posterIndex == BaseConstant.TWO_INT) {
                return agentAccount.getRegisterQrcodeMap2();
            }
            return agentAccount.getRegisterQrcodeMap3();
        } catch (BizException e) {
            log.error("重置推广海报图失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("重置推广海报图时发生异常: ", e);
            throw new BizException("重置推广海报图失败，请稍后重试");
        }
    }

    /**
     * 更新海报图
     * @param loginUser
     * @param file
     * @param posterIndex
     * @return
     * @throws BizException
     */
    public String updatePosterImages(LoginUser loginUser, MultipartFile file, Integer posterIndex) throws BizException {
        try {
            // 1. 校验参数
            if (file == null || file.isEmpty()) {
                throw new BizException("上传文件不能为空");
            }

            if (posterIndex == null || posterIndex < 1 || posterIndex > 3) {
                throw new BizException("海报图索引必须在1-3之间");
            }

            // 2. 校验文件类型和大小
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null ||
                (!originalFilename.toLowerCase().endsWith(".jpg") &&
                 !originalFilename.toLowerCase().endsWith(".jpeg") &&
                 !originalFilename.toLowerCase().endsWith(".png"))) {
                throw new BizException("只支持JPG、JPEG、PNG格式的图片");
            }

            // 限制文件大小为5MB
            if (file.getSize() > 5 * 1024 * 1024) {
                throw new BizException("图片大小不能超过5MB");
            }

            // 3. 上传图片
            String imageUrl = pictureService.uploadPicture(file);

            // 4. 调用URL更新方法
            return updatePosterImages(loginUser, imageUrl, posterIndex);

        } catch (BizException e) {
            log.error("更新海报图失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("更新海报图时发生异常: ", e);
            throw new BizException("更新海报图失败，请稍后重试");
        }
    }

    /**
     * 更新海报图 - 通过URL
     * @param loginUser
     * @param imageUrl
     * @param posterIndex
     * @return
     * @throws BizException
     */
    public String updatePosterImages(LoginUser loginUser, String imageUrl, Integer posterIndex) throws BizException {
        try {
            // 1. 校验参数
            if (imageUrl == null || imageUrl.trim().isEmpty()) {
                throw new BizException("图片URL不能为空");
            }

            if (posterIndex == null || posterIndex < 1 || posterIndex > 3) {
                throw new BizException("海报图索引必须在1-3之间");
            }

            // 2. 获取代理商账户信息
            AgentAccount agentAccount = agentAccountService.getAgentAccountByUserId(loginUser.getUserId(), true);

            // 3. 更新对应的海报图字段
            switch (posterIndex) {
                case 1:
                    agentAccount.setRegisterQrcodeMap1(imageUrl);
                    break;
                case 2:
                    agentAccount.setRegisterQrcodeMap2(imageUrl);
                    break;
                case 3:
                    agentAccount.setRegisterQrcodeMap3(imageUrl);
                    break;
                default:
                    throw new BizException("海报图索引必须在1-3之间");
            }

            // 4. 更新数据库
            agentAccountMapper.updateById(agentAccount);

            // 5. 清除缓存（注释掉）
            // String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_USER_ID, agentAccount.getSysUserId());
            // configStringRedisTemplate.delete(cacheKey);

            log.info("代理商 {} 成功更新海报图{}，图片URL: {}", agentAccount.getAgentCode(), posterIndex, imageUrl);

            return imageUrl;

        } catch (BizException e) {
            log.error("更新海报图失败: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("更新海报图时发生异常: ", e);
            throw new BizException("更新海报图失败，请稍后重试");
        }
    }

}
