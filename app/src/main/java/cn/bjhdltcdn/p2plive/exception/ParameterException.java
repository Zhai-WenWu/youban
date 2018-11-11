package cn.bjhdltcdn.p2plive.exception;

/**
 * 网络是否可用异常类
 */
public class ParameterException extends Exception {
    public ParameterException(String detailMessage) {
        super(detailMessage);
    }

    public ParameterException() {
        super();
    }

    public ParameterException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
