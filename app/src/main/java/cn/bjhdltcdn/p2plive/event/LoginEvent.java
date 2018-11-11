package cn.bjhdltcdn.p2plive.event;

/**
 * Created by ZHUDI on 2016/10/24.
 */

public class LoginEvent {
    private int msgCode;
    private Object obj;

    public LoginEvent(int msgCode, Object obj) {
        this.msgCode = msgCode;
        this.obj = obj;
    }

    public int getMsgCode() {
        return msgCode;
    }

    public Object getObj() {
        return obj;
    }
}
