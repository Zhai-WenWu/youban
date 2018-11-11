package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.MyProps;


/**
 * Created by xiawenquan on 17/3/8.
 */

public class GetCurrentGoldStatisticResponse extends BaseResponse {

    private List<MyProps> currentSendList;//本场贡献
    private List<MyProps> currentReceiveList;//本场收获
    private List<MyProps> currentVotelist;//[{MyProps对象},{MyProps对象},...]得票数据,
    private String activeTime;//活动时长
    private long receiveTotalGold;//收获总金币

    public List<MyProps> getCurrentSendList() {
        return currentSendList;
    }

    public void setCurrentSendList(List<MyProps> currentSendList) {
        this.currentSendList = currentSendList;
    }

    public List<MyProps> getCurrentReceiveList() {
        return currentReceiveList;
    }

    public void setCurrentReceiveList(List<MyProps> currentReceiveList) {
        this.currentReceiveList = currentReceiveList;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public long getReceiveTotalGold() {
        return receiveTotalGold;
    }

    public void setReceiveTotalGold(long receiveTotalGold) {
        this.receiveTotalGold = receiveTotalGold;
    }

    public List<MyProps> getCurrentVotelist() {
        return currentVotelist;
    }

    public void setCurrentVotelist(List<MyProps> currentVotelist) {
        this.currentVotelist = currentVotelist;
    }

}
