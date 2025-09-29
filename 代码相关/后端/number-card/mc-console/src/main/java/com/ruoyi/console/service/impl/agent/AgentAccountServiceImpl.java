package com.ruoyi.console.service.impl.agent;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.constant.BaseConstant;
import com.ruoyi.common.constant.CacheKeyConstants;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.*;
import com.ruoyi.common.order.dto.SmsDTO;
import com.ruoyi.common.order.entity.*;
import com.ruoyi.common.order.vo.AgentStatisticsVO;
import com.ruoyi.common.utils.CacheUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.console.mapper.AgentAccountMapper;
import com.ruoyi.console.mapper.ProductMapper;
import com.ruoyi.console.service.*;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * 代理商注册相关接口
 *
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/19 17:13
 */
@Service
@Slf4j
public class AgentAccountServiceImpl extends ServiceImpl<AgentAccountMapper, AgentAccount> implements AgentAccountService {

    @Resource
    ISysUserService iSysUserService;

    @Resource
    ISysDeptService iSysDeptService;

    @Resource
    ISysRoleService iSysRoleService;

    @Resource
    CommissionConfigService commissionConfigService;

    @Resource
    ProductMapper productMapper;

    @Resource
    WithdrawalRecordService withdrawalRecordService;

    @Resource(name = "configStringRedisTemplate")
    StringRedisTemplate configStringRedisTemplate;

    @Resource
    AgentProductService agentProductService;

    @Resource
    SmsService smsService;

    @Resource
    SysUserMapper userMapper;

    @Resource
    AgentProductInitService agentProductInitService;

    @Resource
    ToolConfigService toolConfigService;



    /**
     * 新增代理商账户
     *
     * @return
     * @throws BizException
     */
    public void addAgentAccount(AgentAccountAddBO agentAccountAddBO) throws BizException {
        log.info("用户注册开始:{}",JSONObject.toJSONString(agentAccountAddBO));
        //判断推荐人是否存在
        List<AgentAccount> agentAccountList = baseMapper.selectList(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getAgentCode, agentAccountAddBO.getParentAgentCode()));
        if (CollectionUtils.isEmpty(agentAccountList)) {
            throw new BizException("推荐人编码不存在");
        }
        //父编码
        AgentAccount parentAgent = agentAccountList.get(BaseConstant.ZERO_INT);
        if (parentAgent.getLevel() >= BaseConstant.AGENT_LEVEL_INT) {
            throw new BizException("代理商等级超限,请联系管理员");
        }
        SmsDTO smsDTO  = new SmsDTO();
        smsDTO.setPhoneNumber(agentAccountAddBO.getPhone());
        smsDTO.setSmsTemplateType(BaseConstant.ZERO_INT);
        smsDTO.setSmsCode(agentAccountAddBO.getSmsCode());
        // if(!smsService.checkSms(smsDTO)){
        //     throw new BizException("请输入正确的验证码");
        // }
        if(!isValidByRegex(agentAccountAddBO.getUserName())){
            throw new BizException("用户名中不能包含特殊字符");
        }

        //判断手机号是否被注册
        List<SysUser> sysUserPhone = iSysUserService.selectUserByPhoneNumber(agentAccountAddBO.getPhone());
        if(!CollectionUtils.isEmpty(sysUserPhone)){
            throw new BizException("手机号已被注册");
        }
        //判断是否存在同名账号
        SysUser sysUser = iSysUserService.selectUserByUserName(agentAccountAddBO.getUserName());
        //不存在创建账号 默认分派代理商角色和部门
        if (sysUser == null) {
            sysUser = new SysUser();
            //添加信息 创建用户
            SysDept sysDept = iSysDeptService.selectDeptByName("代理商");
            sysUser.setDeptId(sysDept.getDeptId());
            sysUser.setDept(sysDept);
            sysUser.setUserName(agentAccountAddBO.getUserName());
            sysUser.setNickName(agentAccountAddBO.getUserName());
            sysUser.setPhonenumber(agentAccountAddBO.getPhone());
            sysUser.setPassword(SecurityUtils.encryptPassword(agentAccountAddBO.getPassword()));
            sysUser.setStatus(BaseConstant.ZERO_STRING);
            sysUser.setDelFlag(BaseConstant.ZERO_STRING);
            //分派固定角色为代理商
            SysRole sysRole = iSysRoleService.selectRoleByKey("agent");
            List<SysRole> roles = new ArrayList<>();
            roles.add(sysRole);
            Long[] roleIds = new Long[]{sysRole.getRoleId()};
            sysUser.setRoles(roles);
            sysUser.setRoleIds(roleIds);
            iSysUserService.insertUser(sysUser);
            //重新查询user信息
            sysUser = iSysUserService.selectUserByUserName(agentAccountAddBO.getUserName());
        } else {
            throw new BizException("账户名已注册,请重试");
            /*List<AgentAccount> agentAccount = baseMapper.selectList(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getSysUserId, sysUser.getUserId()));
            if (!CollectionUtils.isEmpty(agentAccount)) {
                throw new BizException("账户名已注册,请重试");
            }*/
        }
        //注册代理商账号
        AgentAccount agentAccount = new AgentAccount();
        agentAccount.setSysUserId(sysUser.getUserId());
        agentAccount.setParentAgentCode(parentAgent.getAgentCode());
        agentAccount.setAgentCode(RandomUtil.randomString(BaseConstant.EIGHT_INT));
        agentAccount.setAgentName(sysUser.getUserName());
        agentAccount.setLevel(parentAgent.getLevel() + 1);
        agentAccount.setIsRealName(BaseConstant.ZERO_INT);
        agentAccount.setPhone(agentAccountAddBO.getPhone());
        agentAccount.setIsEnabled(BaseConstant.ZERO_INT);
        agentAccount.setIsEncrypt(BaseConstant.ZERO_INT);
        agentAccount.setCreateTime(System.currentTimeMillis());
        agentAccount.setSecurityKey(RandomUtil.randomString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", 16));
        //计算代理商父编码列表 admin为最高 最底部是本身
        List<ParentAgentJson> parentAgentJsons = StringUtils.isNotEmpty(parentAgent.getParentAgentList()) ? JSONObject.parseArray(parentAgent.getParentAgentList(), ParentAgentJson.class) : new ArrayList<>();
        ParentAgentJson parentAgentJson = new ParentAgentJson();
        parentAgentJson.setAgentCode(agentAccount.getAgentCode());
        parentAgentJson.setAgentName(sysUser.getUserName());
        parentAgentJson.setLevel(agentAccount.getLevel());
        parentAgentJson.setSysUserId(sysUser.getUserId());
        parentAgentJsons.add(parentAgentJson);
        agentAccount.setParentAgentList(JSONObject.toJSONString(parentAgentJsons));
        baseMapper.insert(agentAccount);
        //创建提现相关记录
        withdrawalRecordService.addWithdrawalRecord(sysUser.getUserId(), agentAccount.getAgentCode());
        CommissionConfig commissionConfig = new CommissionConfig();
        commissionConfig.setSysUserId(sysUser.getUserId());
        commissionConfig.setAgentCode(agentAccount.getAgentCode());
        //创建佣金配置记录
        commissionConfigService.addCommissionConfig(commissionConfig);
        //添加父代理商默认开放产品
        agentProductInitService.addRegisterAccountProduct(agentAccount);
        //添加代理推广海报
        ToolConfig toolConfig =toolConfigService.getToolConfig(BaseConstant.FOUR_INT);
        if(StringUtils.isNotEmpty(toolConfig.getAccessKey())){
            agentProductInitService.addRegisterQrcodeMap(agentAccount,toolConfig.getAccessKey(),BaseConstant.ONE_INT);
        }
        if(StringUtils.isNotEmpty(toolConfig.getSecretKey())){
            agentProductInitService.addRegisterQrcodeMap(agentAccount,toolConfig.getSecretKey(),BaseConstant.TWO_INT);
        }
        if(StringUtils.isNotEmpty(toolConfig.getBucket())){
            agentProductInitService.addRegisterQrcodeMap(agentAccount,toolConfig.getBucket(),BaseConstant.THREE_INT);
        }

        log.info("用户注册成功结束:{}",JSONObject.toJSONString(agentAccountAddBO));
    }

    /**
     *  Hutool 正则工具类 校验字符串只能包含数字汉字字母
     *  System.out.println(isValidByRegex("abc123你好"));  // true
     *  System.out.println(isValidByRegex("abc@123"));    // false
     */
    public static boolean isValidByRegex(String input) {
        if (StrUtil.isBlank(input)) {
            return false;
        }
        // 正则表达式：数字、大小写字母、汉字
        return ReUtil.isMatch("^[0-9a-zA-Z\\u4E00-\\u9FA5]+$", input);
    }


    /**
     * 查询登录账号 子代理列表
     *
     * @return
     */
    public List<AgentAccount> selectChildAgentList(LoginUser loginUser, Integer isEnabled) throws BizException {
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CHILD_LIST, loginUser.getUserId(), isEnabled);
        String agentAccountJson = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotEmpty(agentAccountJson)) {
            return JSONObject.parseArray(agentAccountJson, AgentAccount.class);
        }
        AgentAccount agentAccount = getAgentAccountByUserId(loginUser.getUserId(), false);
        if (agentAccount == null) {
            return new ArrayList<>();
        }
        List<AgentAccount> childAgentAccountList = baseMapper.selectList(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode, agentAccount.getAgentCode()).eq(isEnabled != null, AgentAccount::getIsEnabled, isEnabled));
        if (!CollectionUtils.isEmpty(childAgentAccountList)) {
            //configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(childAgentAccountList), CacheKeyConstants.CATCH_TIME, TimeUnit.MINUTES);
        }
        return childAgentAccountList;
    }

    /**
     * 根据登录账户ID获取代理商信息
     *
     * @param userId
     * @return
     */
    public AgentAccount getAgentAccountByUserId(Long userId, boolean b) throws BizException {
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_USER_ID, userId);
        String agentAccountJson = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotEmpty(agentAccountJson)) {
            return JSONObject.parseObject(agentAccountJson, AgentAccount.class);
        }
        AgentAccount agentAccount = baseMapper.selectOne(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getSysUserId, userId).eq(AgentAccount::getIsEnabled, BaseConstant.ZERO_INT));
        if (b && agentAccount == null) {
            throw new BizException("登录账号未注册代理商");
        }
        if(agentAccount.getIsEnabled() == BaseConstant.ONE_INT){
            throw new BizException("{}账号已被禁用", agentAccount.getAgentName());
        }
        //configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentAccount), CacheKeyConstants.CATCH_TIME, TimeUnit.MINUTES);
        return agentAccount;
    }


    /**
     * 根据代理商code获取代理商信息
     *
     * @return
     */
    public AgentAccount getAgentAccountByCode(String agentCode, boolean b) throws BizException {
        if(StringUtils.isEmpty(agentCode)){
            throw new BizException("代理商编码:{}", agentCode);
        }
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        String agentAccountJson = configStringRedisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotEmpty(agentAccountJson)) {
            return JSONObject.parseObject(agentAccountJson, AgentAccount.class);
        }
        AgentAccount agentAccount = baseMapper.selectOne(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getAgentCode, agentCode).eq(AgentAccount::getIsEnabled, BaseConstant.ZERO_INT));
        if (b && agentAccount == null) {
            throw new BizException("{}账号未注册代理商", agentCode);
        }
        if(agentAccount.getIsEnabled() == BaseConstant.ONE_INT){
            throw new BizException("{}账号已被禁用", agentAccount.getAgentName());
        }
        //configStringRedisTemplate.opsForValue().set(cacheKey, JSONObject.toJSONString(agentAccount), CacheKeyConstants.CATCH_TIME, TimeUnit.MINUTES);
        return agentAccount;
    }

    /**
     * 更新代理商实名状态
     *
     * @return
     */
    public void updateAgentRealNameStatus(String agentCode, Integer isRealName,String cardIdUrlFront,String cardIdUrlBack) throws BizException {
        AgentAccount agentAccount = getAgentAccountByCode(agentCode,true);
        agentAccount.setIsRealName(isRealName);
        agentAccount.setUpdateTime(System.currentTimeMillis());
        if(StringUtils.isNotEmpty(cardIdUrlFront)){
            agentAccount.setCardIdUrlFront(cardIdUrlFront);
        }
        if(StringUtils.isNotEmpty(cardIdUrlBack)){
            agentAccount.setCardIdUrlBack(cardIdUrlBack);
        }
        baseMapper.updateById(agentAccount);
        //修改之后删除缓存
        String cacheKey = CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_CODE, agentCode);
        configStringRedisTemplate.delete(cacheKey);
        configStringRedisTemplate.delete(CacheUtils.generalKey(CacheKeyConstants.AGENT_ACCOUNT_USER_ID, agentAccount.getSysUserId()));
    }

    /**
     * 更新代理商实名手机号
     *
     * @return
     */
    public void updateAgentPhone(String agentCode,String phone) throws BizException {
        AgentAccount agentAccount = getAgentAccountByCode(agentCode,true);
        agentAccount.setPhone(phone);
        agentAccount.setUpdateTime(System.currentTimeMillis());
        baseMapper.updateById(agentAccount);
        //修改user表中注册手机号
        SysUser sysUser = iSysUserService.selectUserById(agentAccount.getSysUserId());
        sysUser.setPhonenumber(phone);
        userMapper.updateUser(sysUser);
    }


    /**
     * 登录账号子代理统计
     * @param loginUser
     * @return
     * @throws BizException
     */
    @Override
    public AgentStatisticsVO selectChildAgentStatistics(LoginUser loginUser) throws BizException {
        AgentStatisticsVO agentStatisticsVO = new AgentStatisticsVO();
        AgentAccount agentAccount = getAgentAccountByUserId(loginUser.getUserId(), false);
        if (agentAccount == null) {
            return agentStatisticsVO;
        }
        List<AgentAccount> childAgentAccountList = baseMapper.selectList(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode, agentAccount.getAgentCode()));
        if (CollectionUtils.isEmpty(childAgentAccountList)) {
            return agentStatisticsVO;
        }
        agentStatisticsVO.setAgentAccountTotal(childAgentAccountList.size());
        Integer secondaryAgentTotal = 0;
        List<AgentStatistics> agentStatisticsList = new ArrayList<>();
        //查询二级代理信息
        for(AgentAccount childAgentAccount:childAgentAccountList){
            AgentStatistics agentStatistics = new AgentStatistics();
            agentStatistics.setAgentCode(childAgentAccount.getAgentCode());
            agentStatistics.setAgentName(childAgentAccount.getAgentName());
            agentStatistics.setCreateTime(childAgentAccount.getCreateTime());
            agentStatistics.setPhone(childAgentAccount.getPhone());
            //查询二级代理
            List<AgentAccount> secondaryAgentAccountList = baseMapper.selectList(new LambdaQueryWrapper<AgentAccount>().eq(AgentAccount::getParentAgentCode, childAgentAccount.getAgentCode()));
            if (!CollectionUtils.isEmpty(secondaryAgentAccountList)) {
                List<AgentStatistics> secondaryStatisticsList = new ArrayList<>();
                for (AgentAccount secondaryAgentAccount:secondaryAgentAccountList){
                    AgentStatistics secondaryStatistics = new AgentStatistics();
                    secondaryStatistics.setAgentCode(secondaryAgentAccount.getAgentCode());
                    secondaryStatistics.setAgentName(secondaryAgentAccount.getAgentName());
                    secondaryStatistics.setCreateTime(secondaryAgentAccount.getCreateTime());
                    //secondaryStatistics.setPhone(secondaryAgentAccount.getPhone());
                    secondaryStatisticsList.add(secondaryStatistics);
                }
                agentStatistics.setSecondaryAgentNumber(secondaryStatisticsList.size());
                secondaryAgentTotal = secondaryAgentTotal + secondaryStatisticsList.size();
                agentStatistics.setSecondaryAgentStatisticsList(secondaryStatisticsList);
            }
            agentStatisticsList.add(agentStatistics);
        }
        agentStatisticsVO.setSecondaryAgentTotal(secondaryAgentTotal);
        agentStatisticsVO.setAgentStatisticsList(agentStatisticsList);
        return agentStatisticsVO;
    }
}
