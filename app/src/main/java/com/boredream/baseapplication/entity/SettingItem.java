package com.boredream.baseapplication.entity;

public class SettingItem {

    private Integer icon;
    private String name;
    private String right;
    private boolean showRightArrow;

    public SettingItem(Integer icon, String name, String right, boolean showRightArrow) {
        this.icon = icon;
        this.name = name;
        this.right = right;
        this.showRightArrow = showRightArrow;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public boolean isShowRightArrow() {
        return showRightArrow;
    }

    public void setShowRightArrow(boolean showRightArrow) {
        this.showRightArrow = showRightArrow;
    }
}
