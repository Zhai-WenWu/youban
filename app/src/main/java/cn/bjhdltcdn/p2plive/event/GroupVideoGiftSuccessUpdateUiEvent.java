package cn.bjhdltcdn.p2plive.event;


import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Props;

/**
 * 多人视频发送礼物成功后更新UI事件
 */

public class GroupVideoGiftSuccessUpdateUiEvent {
    private Props mProps;
    private BaseUser receiveBaseUser;
    private BaseUser sendBaseUser;


    public GroupVideoGiftSuccessUpdateUiEvent(Props mProps, BaseUser receiveBaseUser, BaseUser sendBaseUser) {
        this.mProps = mProps;
        this.receiveBaseUser = receiveBaseUser;
        this.sendBaseUser = sendBaseUser;
    }

    public Props getmProps() {
        return mProps;
    }

    public void setmProps(Props mProps) {
        this.mProps = mProps;
    }

    public BaseUser getReceiveBaseUser() {
        return receiveBaseUser;
    }

    public void setReceiveBaseUser(BaseUser receiveBaseUser) {
        this.receiveBaseUser = receiveBaseUser;
    }

    public BaseUser getSendBaseUser() {
        return sendBaseUser;
    }

    public void setSendBaseUser(BaseUser sendBaseUser) {
        this.sendBaseUser = sendBaseUser;
    }

}
