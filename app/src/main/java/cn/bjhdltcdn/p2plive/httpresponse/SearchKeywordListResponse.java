package cn.bjhdltcdn.p2plive.httpresponse;

import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by Hu_PC on 2017/11/21.
 */

public class SearchKeywordListResponse extends BaseResponse{
    private List<OrganizationInfo> organList;//[{OrganizationInfo},{OrganizationInfo}...]圈子列表,
    private List<Group> groupList;//[{Group},{Group}...]群组列表,
    private List<BaseUser> userList;//[{BaseUser},{BaseUser}...]用户列表,
    private List<ActivityInfo> activeList;//[{ActivityInfo},{ActivityInfo}...]线下活动列表,
    private String defaultImg;//活动默认图片url,
    private String blankHint;//空白提示语(没有搜索到相关内容),

    public List<OrganizationInfo> getOrganList() {
        return organList;
    }

    public void setOrganList(List<OrganizationInfo> organList) {
        this.organList = organList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }

    public List<BaseUser> getUserList() {
        return userList;
    }

    public void setUserList(List<BaseUser> userList) {
        this.userList = userList;
    }

    public List<ActivityInfo> getActiveList() {
        return activeList;
    }

    public void setActiveList(List<ActivityInfo> activeList) {
        this.activeList = activeList;
    }

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }

    public String getDefaultImg() {
        return defaultImg;
    }

    public void setDefaultImg(String defaultImg) {
        this.defaultImg = defaultImg;
    }
}
