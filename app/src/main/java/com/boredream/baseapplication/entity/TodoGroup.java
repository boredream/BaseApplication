package com.boredream.baseapplication.entity;

/**
 * <p>
 * 清单组
 * </p>
 *
 * @author boredream
 */
public class TodoGroup extends Belong2UserEntity {

	// 名称
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
