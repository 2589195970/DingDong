package com.ruoyi.common.core.page;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.util.List;

public class PageResultFactory {
    public PageResultFactory() {
    }

    public static <T> PageResult<T> createPageResult(Page<T> page) {
        PageResult<T> pageResult = new PageResult();
        pageResult.setRows(page.getRecords());
        pageResult.setTotalRows(Convert.toInt(page.getTotal()));
        pageResult.setPageNo(Convert.toInt(page.getCurrent()));
        pageResult.setPageSize(Convert.toInt(page.getSize()));
        pageResult.setTotalPage(PageUtil.totalPage(pageResult.getTotalRows(), pageResult.getPageSize()));
        return pageResult;
    }

    public static <T> PageResult<T> createPageResult(List<T> rows, Long count, Integer pageSize, Integer pageNo) {
        PageResult<T> pageResult = new PageResult();
        pageResult.setRows(rows);
        pageResult.setTotalRows(Convert.toInt(count));
        pageResult.setPageNo(pageNo);
        pageResult.setPageSize(pageSize);
        pageResult.setTotalPage(PageUtil.totalPage(pageResult.getTotalRows(), pageSize));
        return pageResult;
    }
}
