package com.ruoyi.common.order.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AddressCacheProvinceBO {

    public AddressCacheProvinceBO(String name, String code) {
        this.name = name;
        this.code = code;
        cityList = new ArrayList<>();
    }

    private String name;
    private String code;
    private List<AddressCacheCityBO> cityList;
}
