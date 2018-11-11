package cn.bjhdltcdn.p2plive.httpresponse;


import cn.bjhdltcdn.p2plive.model.RoomBaseUser;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetOwnerInfoResponse extends BaseResponse {
    private RoomBaseUser baseUser;//{BaseUser对象},
    private int voteTimer;//投票计时器

    public RoomBaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(RoomBaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public int getVoteTimer() {
        return voteTimer;
    }

    public void setVoteTimer(int voteTimer) {
        this.voteTimer = voteTimer;
    }
}
