package com.ruoyi.common.order.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AddressCacheCountyBO {

    public AddressCacheCountyBO(String name, String code) {
        this.name = name;
        this.code = code;
    }

    private String name;
    private String code;
}
