package cn.bjhdltcdn.p2plive.event;

/**
 * Description:聊天室管理event
 * Data: 2018/6/9
 *
 * @author: zhudi
 */
public class AnonymousChatRoomManageEvent {
    /**
     * 服务器消息类型
     * 9003 被禁言
     */
    private int meaaageType;

    /**
     * 1 取消 2开启禁言
     */
    private int type;
    /**
     * 推送提示消息
     */
    private String tipMsg;

    public AnonymousChatRoomManageEvent(int meaaageType, int type) {
        this.meaaageType = meaaageType;
        this.type = type;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public int getMeaaageType() {
        return meaaageType;
    }
}
