package com.ruoyi.common.order.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class AddressJsonBO {

    private String name;
    private Map<String, Object> child;
}
