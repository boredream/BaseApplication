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

    private String name;

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
