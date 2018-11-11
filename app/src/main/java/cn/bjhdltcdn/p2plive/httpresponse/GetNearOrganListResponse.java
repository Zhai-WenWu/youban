package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.NearOrganInfo;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class GetNearOrganListResponse extends BaseResponse {
    private List<NearOrganInfo> nearList;//[{NearOrganInfo},{NearOrganInfo}...]附近圈子列表,
    private int total;//总数,
    public List<NearOrganInfo> getNearList() {
        return nearList;
    }
    private String blankHint;//空白提示语(你附近的人还没加入圈子),
    public void setNearList(List<NearOrganInfo> nearList) {
        this.nearList = nearList;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
