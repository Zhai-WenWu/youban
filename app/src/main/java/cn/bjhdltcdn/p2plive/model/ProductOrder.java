package cn.bjhdltcdn.p2plive.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Zhai_PC on 2018/4/16.
 */

public class ProductOrder implements Parcelable {
    /*  "orderId":订单Id,
        "orderNumber":订单编号,
        "baseUser":{BaseUser}用户基本信息,
        "applyStatus":申请状态(1申请试用成功,2申请试用失败),
        "orderStatus":订单状态(0待收货,1已收货),
        "isSendReport":是否发试用报告(1未发,2已发),
        "addr":地址,
        "addTime":添加时间,
        "updateTime":更新时间,
        "addressInfo":{AddressInfo}用户地址信息对象,
        "productInfo":{ProductInfo对象}商品对象,
        "evalScore":评价分数(0未评价),*/

    public long orderId;
    public String orderNumber;
    public BaseUser baseUser;
    public BaseUser receiptUser;
    public int applyStatus;
    public int orderStatus;
    public int isSendReport;
    public String addr;
    public String addTime;
    public String updateTime;
    public AddressInfo addressInfo;
    public ProductInfo productInfo;
    public int evalScore;
    public String claimCode;
    public String productNum;
    public String price;
    public String totalPrice;
    public String remark;//备注
    public int distributeMode;//配送方式(1自取,2卖家配送)
    public StoreInfo storeInfo;//店铺信息
    public int isEval;//是否评价(0未评价,1已评价),
    public int isReceipt;//是否接单(0未接单,1已接单),
    public List<ProductInfo> productList;
    public int isClert;
    public RefundReason refundReason;//退款原因
    public int totalProductCount;//订单商品总数
    public int sellerSign;//1(卖家已签收) or 0(卖家未签收)，
    public int buyerSign;//3（买家已签收）or 0(买家未签收),
    public long storeId;
    public String isShow ;
    public String postAge;//邮费

    public String getIsShow() {
        return isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public int getSellerSign() {
        return sellerSign;
    }

    public void setSellerSign(int sellerSign) {
        this.sellerSign = sellerSign;
    }

    public int getBuyerSign() {
        return buyerSign;
    }

    public void setBuyerSign(int buyerSign) {
        this.buyerSign = buyerSign;
    }

    public int getTotalProductCount() {
        return totalProductCount;
    }

    public void setTotalProductCount(int totalProductCount) {
        this.totalProductCount = totalProductCount;
    }

    public BaseUser getReceiptUser() {
        return receiptUser;
    }

    public void setReceiptUser(BaseUser receiptUser) {
        this.receiptUser = receiptUser;
    }

    public int getIsClert() {
        return isClert;
    }

    public void setIsClert(int isClert) {
        this.isClert = isClert;
    }

    public int getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(int isReceipt) {
        this.isReceipt = isReceipt;
    }

    public int getIsEval() {
        return isEval;
    }

    public void setIsEval(int isEval) {
        this.isEval = isEval;
    }

    public RefundReason getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(RefundReason refundReason) {
        this.refundReason = refundReason;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public int getDistributeMode() {
        return distributeMode;
    }

    public void setDistributeMode(int distributeMode) {
        this.distributeMode = distributeMode;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ProductInfo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductInfo> productList) {
        this.productList = productList;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getClaimCode() {
        return claimCode;
    }

    public void setClaimCode(String claimCode) {
        this.claimCode = claimCode;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BaseUser getBaseUser() {
        return baseUser;
    }

    public void setBaseUser(BaseUser baseUser) {
        this.baseUser = baseUser;
    }

    public int getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(int applyStatus) {
        this.applyStatus = applyStatus;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getIsSendReport() {
        return isSendReport;
    }

    public void setIsSendReport(int isSendReport) {
        this.isSendReport = isSendReport;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public int getEvalScore() {
        return evalScore;
    }

    public void setEvalScore(int evalScore) {
        this.evalScore = evalScore;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public String getPostAge() {
        return postAge;
    }

    public void setPostAge(String postAge) {
        this.postAge = postAge;
    }

    public ProductOrder() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.orderId);
        dest.writeString(this.orderNumber);
        dest.writeParcelable(this.baseUser, flags);
        dest.writeParcelable(this.receiptUser, flags);
        dest.writeInt(this.applyStatus);
        dest.writeInt(this.orderStatus);
        dest.writeInt(this.isSendReport);
        dest.writeString(this.addr);
        dest.writeString(this.addTime);
        dest.writeString(this.updateTime);
        dest.writeParcelable(this.addressInfo, flags);
        dest.writeParcelable(this.productInfo, flags);
        dest.writeInt(this.evalScore);
        dest.writeString(this.claimCode);
        dest.writeString(this.productNum);
        dest.writeString(this.price);
        dest.writeString(this.totalPrice);
        dest.writeString(this.remark);
        dest.writeInt(this.distributeMode);
        dest.writeParcelable(this.storeInfo, flags);
        dest.writeInt(this.isEval);
        dest.writeInt(this.isReceipt);
        dest.writeTypedList(this.productList);
        dest.writeInt(this.isClert);
        dest.writeParcelable(this.refundReason, flags);
        dest.writeInt(this.totalProductCount);
        dest.writeInt(this.sellerSign);
        dest.writeInt(this.buyerSign);
        dest.writeLong(this.storeId);
        dest.writeString(this.isShow);
        dest.writeString(this.postAge);
    }

    protected ProductOrder(Parcel in) {
        this.orderId = in.readLong();
        this.orderNumber = in.readString();
        this.baseUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.receiptUser = in.readParcelable(BaseUser.class.getClassLoader());
        this.applyStatus = in.readInt();
        this.orderStatus = in.readInt();
        this.isSendReport = in.readInt();
        this.addr = in.readString();
        this.addTime = in.readString();
        this.updateTime = in.readString();
        this.addressInfo = in.readParcelable(AddressInfo.class.getClassLoader());
        this.productInfo = in.readParcelable(ProductInfo.class.getClassLoader());
        this.evalScore = in.readInt();
        this.claimCode = in.readString();
        this.productNum = in.readString();
        this.price = in.readString();
        this.totalPrice = in.readString();
        this.remark = in.readString();
        this.distributeMode = in.readInt();
        this.storeInfo = in.readParcelable(StoreInfo.class.getClassLoader());
        this.isEval = in.readInt();
        this.isReceipt = in.readInt();
        this.productList = in.createTypedArrayList(ProductInfo.CREATOR);
        this.isClert = in.readInt();
        this.refundReason = in.readParcelable(RefundReason.class.getClassLoader());
        this.totalProductCount = in.readInt();
        this.sellerSign = in.readInt();
        this.buyerSign = in.readInt();
        this.storeId = in.readLong();
        this.isShow = in.readString();
        this.postAge = in.readString();
    }

    public static final Creator<ProductOrder> CREATOR = new Creator<ProductOrder>() {
        @Override
        public ProductOrder createFromParcel(Parcel source) {
            return new ProductOrder(source);
        }

        @Override
        public ProductOrder[] newArray(int size) {
            return new ProductOrder[size];
        }
    };
}
