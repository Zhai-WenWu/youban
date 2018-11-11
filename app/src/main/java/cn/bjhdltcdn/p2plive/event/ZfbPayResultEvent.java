package cn.bjhdltcdn.p2plive.event;

/**
 * Created by huwenhua on 2016/7/26.
 */
public class ZfbPayResultEvent {
    private String code;

    public ZfbPayResultEvent(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
