package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.AddressBook;

/**
 * Created by xiawenquan on 17/12/1.
 */

public class GetFriendListResponse extends BaseResponse {
    private List<AddressBook> list;

    public List<AddressBook> getList() {
        return list;
    }

    public void setList(List<AddressBook> list) {
        this.list = list;
    }
}
