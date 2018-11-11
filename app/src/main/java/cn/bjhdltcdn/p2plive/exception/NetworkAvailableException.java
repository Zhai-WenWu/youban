package cn.bjhdltcdn.p2plive.exception;

/**
 * 网络是否可用异常类
 */
public class NetworkAvailableException extends Exception {
    public NetworkAvailableException(String detailMessage) {
        super(detailMessage);
    }

    public NetworkAvailableException() {
        super();
    }

    public NetworkAvailableException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

}
