package cn.bjhdltcdn.p2plive.event;

/**
 * 关注状态通知
 */
public class InitiateVoteEvent {
    private int type;//1发起 2关闭,
    private long voteId;//投票Id(关闭时必填),

    public InitiateVoteEvent(int type, long voteId) {
        this.type = type;
        this.voteId = voteId;
    }

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
