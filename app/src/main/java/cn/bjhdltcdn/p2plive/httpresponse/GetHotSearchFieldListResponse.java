package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.KeywordInfo;

/**
 * Created by Hu_PC on 2017/11/15.
 */

public class GetHotSearchFieldListResponse extends BaseResponse{
    private List<KeywordInfo> list;//[{KeywordInfo},{KeywordInfo},...]关键字列表,

    public List<KeywordInfo> getList() {
        return list;
    }

    public void setList(List<KeywordInfo> list) {
        this.list = list;
    }

}
