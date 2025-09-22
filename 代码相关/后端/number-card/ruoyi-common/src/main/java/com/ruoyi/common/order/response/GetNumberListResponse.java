package com.ruoyi.common.order.response;

import com.ruoyi.common.order.entity.NumberDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * 获取号池列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetNumberListResponse {
    /**
     * 号码列表
     */
    private List<NumberDTO> list;
}
