package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.PostLabelInfo;

/**
 * Description:
 * Data: 2018/8/4
 *
 * @author: zhudi
 */
public class SearchLabelByContentResponse extends BaseResponse {
    /**
     * {PostLabelInfo},{PostLabelInfo}对象
     */
    private List<PostLabelInfo> list;
    /**
     * 标签总数
     */
    private int total;

    public List<PostLabelInfo> getList() {
        return list;
    }

    public void setList(List<PostLabelInfo> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
