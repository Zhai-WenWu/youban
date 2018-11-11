package cn.bjhdltcdn.p2plive.httpresponse;


import java.util.List;

import cn.bjhdltcdn.p2plive.model.VoteRecord;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetCurrentVoteDataResponse extends BaseResponse {
    private List<VoteRecord> voteRecordList;//[{VoteRecord对象},{VoteRecord对象},...],
    private int maxVote;//进度条的峰值数,

    public List<VoteRecord> getVoteRecordList() {
        return voteRecordList;
    }

    public void setVoteRecordList(List<VoteRecord> voteRecordList) {
        this.voteRecordList = voteRecordList;
    }

    public int getMaxVote() {
        return maxVote;
    }

    public void setMaxVote(int maxVote) {
        this.maxVote = maxVote;
    }

}
