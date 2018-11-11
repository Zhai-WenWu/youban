package cn.bjhdltcdn.p2plive.event;

import io.rong.imlib.model.UserInfo;

/**
 * Description:会话界面头像点击
 * Data: 2018/6/9
 *
 * @author: zhudi
 */
public class UserPortraitClickEvent {
    /**
     * 是否为长按
     */
    private boolean isLongClick;
    private UserInfo userInfo;

    public UserPortraitClickEvent(boolean isLongClick, UserInfo userInfo) {
        this.isLongClick = isLongClick;
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public boolean isLongClick() {
        return isLongClick;
    }
}
