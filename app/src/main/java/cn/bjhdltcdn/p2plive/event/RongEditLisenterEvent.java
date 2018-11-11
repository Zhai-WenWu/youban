package cn.bjhdltcdn.p2plive.event;

public class RongEditLisenterEvent {
    private long groupId;

    public RongEditLisenterEvent(long groupId) {
        this.groupId = groupId;
    }

    public long getGroupId() {
        return groupId;
    }
}
