package cn.bjhdltcdn.p2plive.event;

/**
 * Created by xiawenquan on 17/11/18.
 */

public class CreateAssociationEvent {
    /**
     * 1,2 启动fragment
     * 3 创建完成后发送更新UI消息
     */
    private int stup;

    public CreateAssociationEvent(int stup) {
        this.stup = stup;
    }

    public int getStup() {
        return stup;
    }

    public void setStup(int stup) {
        this.stup = stup;
    }
}
