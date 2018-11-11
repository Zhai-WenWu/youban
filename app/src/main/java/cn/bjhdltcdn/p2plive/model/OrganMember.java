package cn.bjhdltcdn.p2plive.model;

/**
 * Created by xiawenquan on 17/11/20.
 */

public class OrganMember {
    //基本信息对象
    private BaseUser baseUser;
    //成员Id
    private long memberId;
    //圈子Id
    private long organId;
    //用户角色(1-->圈主,2-->管理员,3-->普通成员)
    private int userRole;
    //入圈时间
    private String joinTime;
    //用户在圈子的活跃等级(字符串)
    private String activeLevel;
    //离我距离
    private double distance;
    //是否禁言(1解除禁言,2禁言)
    private int isGag;


    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getOrganId() {
        return organId;
    }

    public void setOrganId(long organId) {
        this.organId = organId;
    }

    public int getUserRole() {
        return userRole;
    }

    public void setUserRole(int userRole) {
        this.userRole = userRole;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getActiveLevel() {
        return activeLevel;
    }

    public void setActiveLevel(String activeLevel) {
        this.activeLevel = activeLevel;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getIsGag() {
        return isGag;
    }

    public void setIsGag(int isGag) {
        this.isGag = isGag;
    }
}
