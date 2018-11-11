package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.NearOrganInfo;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class GetNearPersonListResponse extends BaseResponse {
    private List<BaseUser> list;//":[{BaseUser},{BaseUser}...]用户列表
    private int total;//总数,
    private int isPublishActivity;//是否发布过活动(1未发布过，2已发布),
    private String blankHint;//空白提示语(你附近的人还没加入圈子),

    public List<BaseUser> getList() {
        return list;
    }

    public void setList(List<BaseUser> list) {
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

    public int getIsPublishActivity() {
        return isPublishActivity;
    }

    public void setIsPublishActivity(int isPublishActivity) {
        this.isPublishActivity = isPublishActivity;
    }
}
