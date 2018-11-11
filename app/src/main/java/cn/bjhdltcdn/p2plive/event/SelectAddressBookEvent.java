package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 17/12/4.
 */

public class SelectAddressBookEvent {

    private Object object;

    public SelectAddressBookEvent(Object object) {
        this.object = object;
    }


    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
