package cn.bjhdltcdn.p2plive.event;

/**
 * Description:所在匿名聊天室状态event
 * Data: 2018/6/9
 *
 * @author: zhudi
 */
public class AnonymousChatRoomStatusEvent {
    /**
     * 是否在聊天室中
     */
    private boolean isInChatRoom;
    /**
     * 服务器消息类型
     * 9001 被踢出房间
     * 9002 解散房间
     */
    private int meaaageType;
    /**
     * 推送提示消息
     */
    private String tipMsg;

    public AnonymousChatRoomStatusEvent(boolean isInChatRoom) {
        this.isInChatRoom = isInChatRoom;
    }

    public AnonymousChatRoomStatusEvent(boolean isInChatRoom, int meaaageType, String tipMsg) {
        this.isInChatRoom = isInChatRoom;
        this.meaaageType = meaaageType;
        this.tipMsg = tipMsg;
    }

    public boolean isInChatRoom() {
        return isInChatRoom;
    }

    public String getTipMsg() {
        return tipMsg;
    }

    public int getMeaaageType() {
        return meaaageType;
    }
}
