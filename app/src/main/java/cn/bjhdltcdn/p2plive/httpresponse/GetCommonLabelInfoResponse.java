package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PostLabelInfo;

/**
 * Created by zhaiww on 2018/7/18.
 */

public class GetCommonLabelInfoResponse extends BaseResponse {
    private List<PostLabelInfo> list;//{PostLabelInfo},{PostLabelInfo}对象


    public List<PostLabelInfo> getList() {
        return list;
    }

    public void setList(List<PostLabelInfo> list) {
        this.list = list;
    }
}
