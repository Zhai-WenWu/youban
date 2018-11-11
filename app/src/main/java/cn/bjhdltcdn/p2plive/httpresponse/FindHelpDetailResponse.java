package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.HelpInfo;

/**
 * Created by ZHUDI on 2018/3/15.
 */

public class FindHelpDetailResponse extends BaseResponse {
    private HelpInfo helpInfo;

    public HelpInfo getHelpInfo() {
        return helpInfo;
    }

    public void setHelpInfo(HelpInfo helpInfo) {
        this.helpInfo = helpInfo;
    }
}
