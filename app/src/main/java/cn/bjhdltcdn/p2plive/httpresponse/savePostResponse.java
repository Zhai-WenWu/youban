package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

/**
 * Created by xiawenquan on 17/11/24.
 */

public class savePostResponse extends BaseResponse {

    private List<Long> postIdlist;

    private long postId;

    public List<Long> getPostIdlist() {
        return postIdlist;
    }

    public void setPostIdlist(List<Long> postIdlist) {
        this.postIdlist = postIdlist;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }
}
