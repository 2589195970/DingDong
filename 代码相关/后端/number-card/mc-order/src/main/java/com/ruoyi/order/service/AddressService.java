package com.ruoyi.order.service;


import com.ruoyi.common.exception.BizException;
import com.ruoyi.common.order.bo.AddressSelectBO;
import com.ruoyi.common.order.vo.AddressSelectVO;

/**
 * @Description
 * @Author 陈思伟
 * @Date 2024/12/22 17:11
 */
public interface AddressService {


    /**
     * 获取地址列表
     * @param addressSelectBO
     * @return
     */
    AddressSelectVO selectAddressList(AddressSelectBO addressSelectBO) throws BizException;
}
