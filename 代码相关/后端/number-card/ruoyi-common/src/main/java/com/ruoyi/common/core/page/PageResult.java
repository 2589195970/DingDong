package com.ruoyi.common.core.page;


import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;

@ApiModel(value = "分页结果集")
public class PageResult<T> implements Serializable {
    private static final long serialVersionUID = -1L;
    private Integer pageNo = 1;
    private Integer pageSize = 20;
    private Integer totalPage = 0;
    private Integer totalRows = 0;
    private List<T> rows;

    public PageResult() {
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public Integer getPageSize() {
        return this.pageSize;
    }

    public Integer getTotalPage() {
        return this.totalPage;
    }

    public Integer getTotalRows() {
        return this.totalRows;
    }

    public List<T> getRows() {
        return this.rows;
    }

    public void setPageNo(final Integer pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(final Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalPage(final Integer totalPage) {
        this.totalPage = totalPage;
    }

    public void setTotalRows(final Integer totalRows) {
        this.totalRows = totalRows;
    }

    public void setRows(final List<T> rows) {
        this.rows = rows;
    }

    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResult)) {
            return false;
        } else {
            PageResult<?> other = (PageResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label71: {
                    Object this$pageNo = this.getPageNo();
                    Object other$pageNo = other.getPageNo();
                    if (this$pageNo == null) {
                        if (other$pageNo == null) {
                            break label71;
                        }
                    } else if (this$pageNo.equals(other$pageNo)) {
                        break label71;
                    }

                    return false;
                }

                Object this$pageSize = this.getPageSize();
                Object other$pageSize = other.getPageSize();
                if (this$pageSize == null) {
                    if (other$pageSize != null) {
                        return false;
                    }
                } else if (!this$pageSize.equals(other$pageSize)) {
                    return false;
                }

                label57: {
                    Object this$totalPage = this.getTotalPage();
                    Object other$totalPage = other.getTotalPage();
                    if (this$totalPage == null) {
                        if (other$totalPage == null) {
                            break label57;
                        }
                    } else if (this$totalPage.equals(other$totalPage)) {
                        break label57;
                    }

                    return false;
                }

                Object this$totalRows = this.getTotalRows();
                Object other$totalRows = other.getTotalRows();
                if (this$totalRows == null) {
                    if (other$totalRows != null) {
                        return false;
                    }
                } else if (!this$totalRows.equals(other$totalRows)) {
                    return false;
                }

                Object this$rows = this.getRows();
                Object other$rows = other.getRows();
                if (this$rows == null) {
                    if (other$rows == null) {
                        return true;
                    }
                } else if (this$rows.equals(other$rows)) {
                    return true;
                }

                return false;
            }
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof PageResult;
    }

    public String toString() {
        return "PageResult(pageNo=" + this.getPageNo() + ", pageSize=" + this.getPageSize() + ", totalPage=" + this.getTotalPage() + ", totalRows=" + this.getTotalRows() + ", rows=" + this.getRows() + ")";
    }
}

