package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2017/4/25.
 */

public class VideoMoreEvent {
    private int type;//1反转摄像头2静音3关闭摄像头

    public VideoMoreEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}