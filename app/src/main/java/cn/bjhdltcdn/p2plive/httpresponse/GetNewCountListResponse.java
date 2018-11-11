package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.CountInfo;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class GetNewCountListResponse extends BaseResponse{
    private List<CountInfo> list;//[{CountInfo},{CountInfo},...]附近更新数量列表,

    public List<CountInfo> getList() {
        return list;
    }

    public void setList(List<CountInfo> list) {
        this.list = list;
    }
}
