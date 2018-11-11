package cn.bjhdltcdn.p2plive.event;

/**
 * 呼叫视频
 */
public class CallVideoEvent {
    /**
     * 1 视频呼叫 2语音呼叫
     */
    private int type;

    public CallVideoEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
