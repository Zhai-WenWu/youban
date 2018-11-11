package cn.bjhdltcdn.p2plive.httpresponse;


import cn.bjhdltcdn.p2plive.model.VoteInfo;

/**
 * Created by Lenovo on 2017/3/2.
 */

public class InitiateVoteResponse extends BaseResponse {
    private VoteInfo voteInfo;//{VoteInfo投票信息对象},

    public VoteInfo getVoteInfo() {
        return voteInfo;
    }

    public void setVoteInfo(VoteInfo voteInfo) {
        this.voteInfo = voteInfo;
    }
}
