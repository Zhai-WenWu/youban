package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class AttentionStatusResponse extends BaseResponse {
    private int attentionStatus;//1 双向关注，2 当前用户单向关注对方，3 双向不关注,4 对方关注我，我不关注对方

    public int getAttentionStatus() {
        return attentionStatus;
    }

    public void setAttentionStatus(int attentionStatus) {
        this.attentionStatus = attentionStatus;
    }
}
