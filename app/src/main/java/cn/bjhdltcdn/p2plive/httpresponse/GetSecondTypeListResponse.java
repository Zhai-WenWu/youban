package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HobbyInfo;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class GetSecondTypeListResponse extends BaseResponse {

    private List<HobbyInfo> hobbyList;

    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }
}
