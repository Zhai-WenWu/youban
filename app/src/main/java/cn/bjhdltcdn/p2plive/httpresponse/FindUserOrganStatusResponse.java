package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by xiawenquan on 18/1/27.
 */

public class FindUserOrganStatusResponse extends BaseResponse {
    /**
     * 是否可以踢出圈子(0不显示(否),1可以)
     */
    private int isKickedOut;
    /**
     * 是否禁言(0不显示,1解除禁言,2禁言)
     */
    private int isGag;


    public int getIsKickedOut() {
        return isKickedOut;
    }

    public void setIsKickedOut(int isKickedOut) {
        this.isKickedOut = isKickedOut;
    }

    public int getIsGag() {
        return isGag;
    }

    public void setIsGag(int isGag) {
        this.isGag = isGag;
    }
}
