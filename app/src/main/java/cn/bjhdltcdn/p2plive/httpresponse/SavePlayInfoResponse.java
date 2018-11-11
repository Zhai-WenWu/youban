package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by ZHAI on 2017/12/22.
 */

public class SavePlayInfoResponse extends BaseResponse {
    public long playId;

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }
}
