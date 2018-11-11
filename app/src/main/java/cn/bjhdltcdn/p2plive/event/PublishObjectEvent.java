package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 17/12/25.
 */

public class PublishObjectEvent {
    private Object object;
    private long labelId;//标签ID

    /**
     * 类型(1帖子,8表白,9同学帮帮忙)
     */
    private int type ;

    public PublishObjectEvent(Object object, int type,long labelId) {
        this.object = object;
        this.type = type;
        this.labelId = labelId;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getLabelId() {
        return labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }
}
