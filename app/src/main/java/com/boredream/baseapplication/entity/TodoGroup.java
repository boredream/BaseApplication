package com.boredream.baseapplication.entity;

import java.util.List;

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

    // TODO: chunyang 12/2/21 一对多关系，放服务器处理数据结构
    private List<Todo> todoList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Todo> getTodoList() {
        return todoList;
    }

    public void setTodoList(List<Todo> todoList) {
        this.todoList = todoList;
    }
}
