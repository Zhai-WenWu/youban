package cn.bjhdltcdn.p2plive.model;

import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;

/**
 * Created by ZHUDI on 2017/12/18.
 */

public class SaveLaunchPlayResponse extends BaseResponse {
    private LaunchPlay launchPlay;//{LaunchPlay对象}发起PK挑战对象,

    public LaunchPlay getLaunchPlay() {
        return launchPlay;
    }

    public void setLaunchPlay(LaunchPlay launchPlay) {
        this.launchPlay = launchPlay;
    }
}
