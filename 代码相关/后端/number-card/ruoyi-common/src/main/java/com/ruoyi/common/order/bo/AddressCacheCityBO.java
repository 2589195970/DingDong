package com.ruoyi.common.order.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class AddressCacheCityBO {
    public AddressCacheCityBO(String name, String code) {
        this.name = name;
        this.code = code;
        countList = new ArrayList<>();
    }

    private String name;
    private String code;
    private List<AddressCacheCountyBO> countList;
}
