package com.withub.model.entity.query;

import com.withub.model.entity.AbstractEntity;

import java.io.Serializable;
import java.util.List;

public final class RecordsetInfo<T extends AbstractEntity> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 记录总数
     */
    private Integer totalRecordCount;

    /**
     * 记录集的大小
     */
    private Integer recordsetSize;

    /**
     * 记录集的总数
     */
    private Integer totalRecordsetCount;

    /**
     * 记录集的索引
     */
    private Integer recordsetIndex;

    /**
     * 记录集的数据实体列表
     */
    private List<T> entityList;

    //=================== 属性方法 ===============================================

    public Integer getTotalRecordCount() {

        return totalRecordCount;
    }

    public void setTotalRecordCount(Integer totalRecordCount) {

        this.totalRecordCount = totalRecordCount;
    }

    public Integer getRecordsetSize() {

        return recordsetSize;
    }

    public void setRecordsetSize(Integer recordsetSize) {

        this.recordsetSize = recordsetSize;
    }

    public Integer getTotalRecordsetCount() {

        return totalRecordsetCount;
    }

    public void setTotalRecordsetCount(Integer totalRecordsetCount) {

        this.totalRecordsetCount = totalRecordsetCount;
    }

    public Integer getRecordsetIndex() {

        return recordsetIndex;
    }

    public void setRecordsetIndex(Integer recordsetIndex) {

        this.recordsetIndex = recordsetIndex;
    }

    public List<T> getEntityList() {

        return entityList;
    }

    public void setEntityList(List<T> entityList) {

        this.entityList = entityList;
    }
}
