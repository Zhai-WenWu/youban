package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;
import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by Hu_PC on 2017/11/14.
 */

public class FindActiveDetailResponse extends BaseResponse{
    private ActivityInfo activeInfo;//{ActivityInfo}活动对象,
    private Group groupInfo;//{Group群组对象}活动临时群对象,
    private List<BaseUser> joinPersonList;//[{BaseUser},{BaseUser}...]报名人数列表,
    private String defaultImg;//默认图片
    private int total;//报名总人数,

    public ActivityInfo getActiveInfo() {
        return activeInfo;
    }

    public void setActiveInfo(ActivityInfo activeInfo) {
        this.activeInfo = activeInfo;
    }

    public Group getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(Group groupInfo) {
        this.groupInfo = groupInfo;
    }

    public List<BaseUser> getJoinPersonList() {
        return joinPersonList;
    }

    public void setJoinPersonList(List<BaseUser> joinPersonList) {
        this.joinPersonList = joinPersonList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }
}
