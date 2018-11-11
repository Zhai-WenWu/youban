package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 17/11/21.
 */

public class SaveOrganizationtResponse extends BaseResponse {

    private long organId;//圈子Id

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }
}
