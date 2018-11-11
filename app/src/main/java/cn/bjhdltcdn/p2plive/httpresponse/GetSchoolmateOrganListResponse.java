package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.OrganizationInfo;

/**
 * Created by xiawenquan on 18/3/5.
 */

public class GetSchoolmateOrganListResponse extends BaseResponse {

    /*

    {
    "userRole":当前用户在圈子的角色,
    "list":[{HomeInfo},{HomeInfo}...]圈子下帖子活动列表,
    "total":总数,
    "defaultImg"：活动默认图片url,
    "blankHint":空白提示语(暂无数据),
    "code": 200 拉取列表成功，0 拉取列表失败,
    "msg":提示信息(200 拉取列表成功，0 拉取列表失败)
    }

     */
    private int userRole;
    private List<HomeInfo> list;
    private int total;
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
