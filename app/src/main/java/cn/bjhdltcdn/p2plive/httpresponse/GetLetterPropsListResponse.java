package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.Props;

/**
 * Created by xiawenquan on 17/12/29.
 */

public class GetLetterPropsListResponse extends BaseResponse {
    private List<Props> list;//[{Props对象},{Props对象}],

    public List<Props> getList() {
        return list;
    }

    public void setList(List<Props> list) {
        this.list = list;
    }
}
