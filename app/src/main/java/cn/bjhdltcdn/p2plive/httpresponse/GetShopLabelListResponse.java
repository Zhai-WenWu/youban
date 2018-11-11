package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;

/**
 * Created by zhaiww on 2018/7/18.
 */

public class GetShopLabelListResponse extends BaseResponse {
    private List<FirstLabelInfo> list;//[{FirstLabelInfo:[{SecondLabelInfo},{SecondLabelInfo}...]},...]

    public List<FirstLabelInfo> getList() {
        return list;
    }

    public void setList(List<FirstLabelInfo> list) {
        this.list = list;
    }
}
