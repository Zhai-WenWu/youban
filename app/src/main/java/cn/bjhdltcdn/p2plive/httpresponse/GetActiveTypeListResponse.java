package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityNumber;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class GetActiveTypeListResponse extends BaseResponse{
    private List<ActivityNumber> personList;//[{ActivityNumber},{ActivityNumber},...]活动人数列表,

    public List<ActivityNumber> getPersonList() {
        return personList;
    }

    public void setPersonList(List<ActivityNumber> personList) {
        this.personList = personList;
    }
}
