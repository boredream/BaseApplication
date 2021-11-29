package com.boredream.baseapplication.entity;

/**
 * <p>
 * 纪念日
 * </p>
 *
 * @author boredream
 */
public class TheDay extends Belong2UserEntity {


    /**
     * 提醒方式 累计天数
     */
    public static final int NOTIFY_TYPE_TOTAL_COUNT = 0;

    /**
     * 提醒方式 按年倒计天数
     */
    public static final int NOTIFY_TYPE_YEAR_COUNT_DOWN = 1;

    // 名称
    private String name;

    // 纪念日期
    private String theDayDate;

    // 提醒方式
    private int notifyType;

    public void setNotifyTypeStr(String data) {
        notifyType = "每年倒数".equals(data) ? NOTIFY_TYPE_YEAR_COUNT_DOWN : NOTIFY_TYPE_TOTAL_COUNT;
    }

    public String getNotifyTypeStr() {
        return NOTIFY_TYPE_YEAR_COUNT_DOWN == notifyType ? "每年倒数" : "累计天数";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTheDayDate() {
        return theDayDate;
    }

    public void setTheDayDate(String theDayDate) {
        this.theDayDate = theDayDate;
    }

    public int getNotifyType() {
        return notifyType;
    }

    public void setNotifyType(int notifyType) {
        this.notifyType = notifyType;
    }

}
