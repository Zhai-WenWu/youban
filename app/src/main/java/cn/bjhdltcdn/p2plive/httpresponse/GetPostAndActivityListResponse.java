package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HomeInfo;

/**
 * Created by xiawenquan on 17/12/16.
 */

public class GetPostAndActivityListResponse extends BaseResponse {
    // 当前用户在圈子的角色
    private int userRole;
    // 圈子下帖子活动列表
    private List<HomeInfo> list;

    private int total;

    // 活动默认图片url
    private String defaultImg;


    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public List<HomeInfo> getList() {
        return list;
    }

    public void setList(List<HomeInfo> list) {
        this.list = list;
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
