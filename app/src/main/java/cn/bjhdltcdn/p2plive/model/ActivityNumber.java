package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2017/11/24.
 */

public class ActivityNumber {
    private long numberId;//主键Id,
    private int activityNumber;//活动人数(返回服务端字段),
    private String numberDesc;//活动人数描述(客户端显示字段)

    public long getNumberId() {
        return numberId;
    }

    public void setNumberId(long numberId) {
        this.numberId = numberId;
    }

    public int getActivityNumber() {
        return activityNumber;
    }

    public void setActivityNumber(int activityNumber) {
        this.activityNumber = activityNumber;
    }

    public String getNumberDesc() {
        return numberDesc;
    }

    public void setNumberDesc(String numberDesc) {
        this.numberDesc = numberDesc;
    }
}
