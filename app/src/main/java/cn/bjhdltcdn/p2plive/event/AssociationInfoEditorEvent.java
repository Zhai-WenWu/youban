package cn.bjhdltcdn.p2plive.event;

/**
 * 更新圈子资料事件
 */

public class AssociationInfoEditorEvent {

    //圈子Id
    private long organId;
    private int type;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public AssociationInfoEditorEvent(int type) {
        this.type = type;
    }

    public AssociationInfoEditorEvent(long organId, int type) {
        this.organId = organId;
        this.type = type;
    }

    /**
     * type ：   1 修改圈子资料
     * 2 解散圈子
     *
     * @return
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }
}
