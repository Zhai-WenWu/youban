package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.PlayInfo;

/**
 * Created by xiawenquan on 18/1/26.
 */

public class FindPlayDetailResponse extends BaseResponse {
    private PlayInfo playInfo;

    public PlayInfo getPlayInfo() {
        return playInfo;
    }

    public void setPlayInfo(PlayInfo playInfo) {
        this.playInfo = playInfo;
    }
}
