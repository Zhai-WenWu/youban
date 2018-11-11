package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/1/18.
 * 多人视频管理权限
 */

public class VideoGroupManageEvent {
    private int type;//60004 被移出房间 60009 禁言 0x19被移除麦序；1反转摄像头2关闭话筒3关闭摄像头4主持移交5.打开或关闭PK
    private int flag;//type为60009时, 1取消禁言  2禁言

    public VideoGroupManageEvent(int type) {
        this.type = type;
    }

    public VideoGroupManageEvent(int type, int flag) {
        this.type = type;
        this.flag = flag;
    }

    public int getType() {
        return type;
    }

    public int getFlag() {
        return flag;
    }
}