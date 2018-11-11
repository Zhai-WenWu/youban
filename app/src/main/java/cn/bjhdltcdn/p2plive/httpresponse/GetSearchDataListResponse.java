package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Group;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by Hu_PC on 2017/11/21.
 */

public class GetSearchDataListResponse {
    private List<OrganizationInfo> organList;//[{OrganizationInfo},{OrganizationInfo}...]圈子列表,
    private List<Group> groupList;//[{Group},{Group}...]群组列表,
    private List<BaseUser> userList;//[{BaseUser},{BaseUser}...]用户列表,
    private List<ActivityInfo> activeList;//[{ActivityInfo},{ActivityInfo}...]线下活动列表,
    private String defaultImg;//活动默认图片url,
    private int total;//总数,
    private int code;// 200 搜索数据成功，0 搜索数据失败,
    private String msg;//提示信息(200 搜索数据成功，0 搜索数据失败)

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

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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
