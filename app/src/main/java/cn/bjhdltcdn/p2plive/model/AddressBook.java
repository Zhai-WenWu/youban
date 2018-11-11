package cn.bjhdltcdn.p2plive.model;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

/**
 * 通讯录对象
 */

public class AddressBook extends BaseIndexPinyinBean {

    private boolean isTop;//是否是最上面的 不需要被转化成拼音的

    private long userId;//用户标识id,
    private String userName;//用户名,
    private String nickName;//用户昵称,
    private String userIcon;//头像,
    private String userBigIcon;//头像大图,
    private int sex;//性别 1-->男性，2-->女性，
    private int age;//:年龄,
    private String distance;//离我距离,
    private String location;//所在地,

    private int isSelect;

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }

    public AddressBook() {
    }

    public AddressBook(String nickName) {
        this.nickName = nickName;
    }

    public boolean isTop() {
        return isTop;
    }

    public void setTop(boolean top) {
        isTop = top;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserBigIcon() {
        return userBigIcon;
    }

    public void setUserBigIcon(String userBigIcon) {
        this.userBigIcon = userBigIcon;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getTarget() {
        return nickName;
    }

    @Override
    public boolean isNeedToPinyin() {
        return !isTop;
    }


    @Override
    public boolean isShowSuspension() {
        return !isTop;
    }
}
