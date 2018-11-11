package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.SayLoveInfo;

/**
 * Created by Hu_PC on 2017/11/13.
 */

public class GetSayLoveListResponse extends BaseResponse {
    private List<SayLoveInfo> list;//[{SayLoveInfo},{SayLoveInfo}...]表白列表,
    private int  total;//总数,
    private String blankHint;//空白提示语(等你来写下第一封表白),

    public List<SayLoveInfo> getList() {
        return list;
    }

    public void setList(List<SayLoveInfo> list) {
        this.list = list;
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
