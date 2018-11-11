package cn.bjhdltcdn.p2plive.httpresponse;


import cn.bjhdltcdn.p2plive.model.Group;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class GetGroupInfoResponse extends BaseResponse{
    private Group group;
    private int flag;//群组是否解散(1正常,2解散),

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
