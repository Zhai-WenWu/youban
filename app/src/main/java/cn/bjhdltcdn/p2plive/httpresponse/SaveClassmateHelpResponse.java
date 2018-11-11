package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

/**
 * Created by ZHAI on 2018/3/1.
 */

public class SaveClassmateHelpResponse extends BaseResponse {
    public List<Long> helpIdList;

    public List<Long> getHelpIdList() {
        return helpIdList;
    }

    public void setHelpIdList(List<Long> helpIdList) {
        this.helpIdList = helpIdList;
    }
}
