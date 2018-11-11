package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.AnonymousUser;
import cn.bjhdltcdn.p2plive.model.ChatBaseUser;

public class GetAnonymityFriendListResponse extends BaseResponse {

    /**
     * "list":[{AnonymousUser},{AnonymousUser},...]
     */
    private List<AnonymousUser> list;
    /**
     * 总数
     */
    private int total;
    /**
     * 空白数据提示信息(你还没有匿名好友),
     */
    private String blankHint;

    public List<AnonymousUser> getList() {
        return list;
    }

    public void setList(List<AnonymousUser> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String getBlankHint() {
        return blankHint;
    }

    @Override
    public void setBlankHint(String blankHint) {
        this.blankHint = blankHint;
    }
}
