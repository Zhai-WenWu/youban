package cn.bjhdltcdn.p2plive.model;

/**
 * Created by ZHUDI on 2017/12/16.
 */

public class VoteInfo {
    private long voteId;//投票Id,
    private long roomId;//房间ID,
    private long userId;//发起投票用户ID,
    private int status;//投票状态(默认为0--->进行中,1--->停止),
    private String addTime;//开始时间,

    public long getVoteId() {
        return voteId;
    }

    public void setVoteId(long voteId) {
        this.voteId = voteId;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }
}
