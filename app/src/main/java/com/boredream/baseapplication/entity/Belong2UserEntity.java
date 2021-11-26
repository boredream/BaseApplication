package com.boredream.baseapplication.entity;

public class Belong2UserEntity extends BaseEntity {

	// 所属用户id
    private Long userId;

	// 所属用户
    private User user;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
