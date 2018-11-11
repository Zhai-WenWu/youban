package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.PostInfo;

/**
 * Created by xiawenquan on 17/11/30.
 */

public class FindPostDetailResponse extends BaseResponse {
    private PostInfo postInfo;//{PostInfo对象}帖子对象,

    public PostInfo getPostInfo() {
        return postInfo;
    }

    public void setPostInfo(PostInfo postInfo) {
        this.postInfo = postInfo;
    }
}
