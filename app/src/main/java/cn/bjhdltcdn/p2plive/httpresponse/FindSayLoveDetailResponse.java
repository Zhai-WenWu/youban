package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.model.User;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public class FindSayLoveDetailResponse extends BaseResponse{
    private SayLoveInfo sayLoveInfo;//{SayLoveInfo}表白对象,

    public SayLoveInfo getSayLoveInfo() {
        return sayLoveInfo;
    }

    public void setSayLoveInfo(SayLoveInfo sayLoveInfo) {
        this.sayLoveInfo = sayLoveInfo;
    }
}
