package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.HobbyInfo;

/**
 * Created by xiawenquan on 18/2/27.
 */

public class SchoolfellowCreateResponse extends BaseResponse {
    private List<HobbyInfo> hobbyList;

    public List<HobbyInfo> getHobbyList() {
        return hobbyList;
    }

    public void setHobbyList(List<HobbyInfo> hobbyList) {
        this.hobbyList = hobbyList;
    }
}
