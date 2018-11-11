package cn.bjhdltcdn.p2plive.httpresponse;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class UpdateProductInfoResponse extends BaseResponse {
    private long  productId;//商品id,

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
