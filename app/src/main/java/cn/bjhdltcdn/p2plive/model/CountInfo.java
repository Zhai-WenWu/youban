package cn.bjhdltcdn.p2plive.model;

/**
 * Created by Hu_PC on 2017/11/16.
 */

public class CountInfo {
    private int type;//类型(1线下活动,2在玩儿啥,3表白邂逅,4圈子热帖),
    private String content;//1你附近的线下活动,2附近人在玩什么,3附近的表白邂逅,4附近的圈子热帖,
    private String description;//类型描述,
    private String nearImg;//类型对应的图片,
    private int updateCount;//更新的数量

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNearImg() {
        return nearImg;
    }

    public void setNearImg(String nearImg) {
        this.nearImg = nearImg;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
