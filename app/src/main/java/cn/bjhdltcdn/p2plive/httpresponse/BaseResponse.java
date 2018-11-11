package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 17/11/18.
 */

public class BaseResponse {
    private int code;
    private String msg;

    private String blankHint;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBlankHint() {
        return blankHint;
    }

    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
