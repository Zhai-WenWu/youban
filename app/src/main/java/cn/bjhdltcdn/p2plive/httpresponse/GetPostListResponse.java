package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;

/**
 * Created by Hu_PC on 2017/11/9.
 */

public class GetPostListResponse extends BaseResponse {
    private List<PostInfo> postList;//[{PostInfo帖子对象},{PostInfo帖子对象}...]推荐热帖,
    private int total;//总数,
    private String blankHint;//空白提示语(若底部没有帖子时【还没人发布内容，快去占领第一帖吧】;

    //若帖子只对圈子成员展示【该圈子开启了私密功能，帖子仅圈友可见】),
    public List<PostInfo> getPostList() {
        return postList;
    }

    public void setPostList(List<PostInfo> postList) {
        this.postList = postList;
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
