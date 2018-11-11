package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/1/14.
 */

public class VideoGroupRoomCloseEvent {
    private String msg;//系统提示信息
    private long roomId;
    private int flag;//0提前3分钟提醒,1自动关闭房间,2主持人刷新数据

    public VideoGroupRoomCloseEvent() {
    }

    public VideoGroupRoomCloseEvent(String msg, long roomId, int flag) {
        this.msg = msg;
        this.roomId = roomId;
        this.flag = flag;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
