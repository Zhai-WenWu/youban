package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PostInfo;

/**
 * Created by Hu_PC on 2017/11/9.
 */

public class MyPostListResponse extends BaseResponse {
    private List<PostInfo> list;//[{PostInfo帖子对象},{PostInfo帖子对象}...]推荐热帖,
    private int total;//总数,
    private String blankHint;//空白数据提示信息

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }

    public List<PostInfo> getList() {
        return list;
    }

    public void setList(List<PostInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
