package cn.bjhdltcdn.p2plive.event;

/**
 * 群操作事件
 */

public class GroupOperationEvent {

    /**
     * 群操作类型
     * 1 解散群
     * 2 退出群
     * 3 更改群信息
     * 4 创建群
     */
    private int operationType;

    /**
     * 群昵称
     */
    private String nickName;

    private long groupId;

    public GroupOperationEvent(int operationType, long groupId) {
        this.operationType = operationType;
        this.groupId = groupId;
    }

    public GroupOperationEvent(int operationType, String nickName) {
        this.operationType = operationType;
        this.nickName = nickName;
    }

    /**
     * 群操作类型
     * 1 解散群
     * 2 退出群
     * 3 更改群信息
     * 4 创建群
     * 5 通知上传群昵称
     * 5 刷新用户在聊天页面的信息
     */
    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
