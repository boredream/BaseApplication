package com.boredream.baseapplication.entity;

import java.io.Serializable;

public class BaseEntity implements Serializable {

    /**
     * 主键id
     */
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
