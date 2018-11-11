package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 18/3/8.
 */

public class SchoolOrganSwitchResponse extends BaseResponse {
    private int isOpen;// 系统开关(1开启,2关闭)
    private int isAuth;// 是否有发布权限(1有,2没有)

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }
}
