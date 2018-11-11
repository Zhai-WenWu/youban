package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Lenovo on 2017/3/2.
 */

public class VoteRecord {
    private long userId;//被投用户Id,
    private String nickName;//用户昵称,
    private String userIcon;//用户头像路径,
    private int userRole;//用户角色(视频房的角色),
    private int wheat;//麦状态,
    private long wheatId;//麦Id,
    private int isShare;//是否分享,
    private int transfer;//移交主持人状态,
    private int isGag;//是否禁言,
    private int voteNumber;//投票数量,
    private int voteRanking;//票数排名,1代表第一名,以此类推..

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public int getWheat() {
        return wheat;
    }

    public void setWheat(int wheat) {
        this.wheat = wheat;
    }

    public long getWheatId() {
        return wheatId;
    }

    public void setWheatId(long wheatId) {
        this.wheatId = wheatId;
    }

    public int getIsShare() {
        return isShare;
    }

    public void setIsShare(int isShare) {
        this.isShare = isShare;
    }

    public int getTransfer() {
        return transfer;
    }

    public void setTransfer(int transfer) {
        this.transfer = transfer;
    }

    public int getIsGag() {
        return isGag;
    }

    public void setIsGag(int isGag) {
        this.isGag = isGag;
    }

    public int getVoteNumber() {
        return voteNumber;
    }

    public void setVoteNumber(int voteNumber) {
        this.voteNumber = voteNumber;
    }

    public int getVoteRanking() {
        return voteRanking;
    }

    public void setVoteRanking(int voteRanking) {
        this.voteRanking = voteRanking;
    }
}
