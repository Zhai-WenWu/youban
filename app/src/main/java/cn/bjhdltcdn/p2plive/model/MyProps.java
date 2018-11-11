package cn.bjhdltcdn.p2plive.model;

/**
 * Created by huwenhua on 2016/6/28.
 */
public class MyProps {

    private long myPropsId;
    private String time;//收到或者送出道具的时间,
    private String propsName;//道具名称,
    private int goldNum;//金币数,
    private long sendUserId;//送出道具人id,
    private String sendNickname;//送出道具人昵称,
    private int isExchange;//是否兑换 0 未兑换 1 已兑换
    private String propUrl;//道具图片路径
    private int salePrice;//打折价格,
    private int isSale;//是否打折 0 不打折 1 打折,
    private int isNew;//是否新道具 0 非新道具 1 新道具
    private int giftMultiple;//礼物倍数
    private long receiveUserId;//收到道具人id
    private String receiveNickname;//收到道具人昵称,
    private long totalGold;// 总金币
    private BaseUser baseUser;
    private int applyType;//道具应用类型(1--->使用，2--->赠送)

    public long getMyPropsId() {
        return myPropsId;
    }

    public void setMyPropsId(long myPropsId) {
        this.myPropsId = myPropsId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName;
    }

    public int getGoldNum() {
        return goldNum;
    }

    public void setGoldNum(int goldNum) {
        this.goldNum = goldNum;
    }

    public long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getSendNickname() {
        return sendNickname;
    }

    public void setSendNickname(String sendNickname) {
        this.sendNickname = sendNickname;
    }

    public int getIsExchange() {
        return isExchange;
    }

    public void setIsExchange(int isExchange) {
        this.isExchange = isExchange;
    }

    public String getPropUrl() {
        return propUrl;
    }

    public void setPropUrl(String propUrl) {
        this.propUrl = propUrl;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public int getIsSale() {
        return isSale;
    }

    public void setIsSale(int isSale) {
        this.isSale = isSale;
    }

    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getGiftMultiple() {
        return giftMultiple;
    }

    public void setGiftMultiple(int giftMultiple) {
        this.giftMultiple = giftMultiple;
    }

    public long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public String getReceiveNickname() {
        return receiveNickname;
    }

    public void setReceiveNickname(String receiveNickname) {
        this.receiveNickname = receiveNickname;
    }

    public long getTotalGold() {
        return totalGold;
    }

    public void setTotalGold(long totalGold) {
        this.totalGold = totalGold;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public int getApplyType() {
        return applyType;
    }

    public void setApplyType(int applyType) {
        this.applyType = applyType;
    }
}
