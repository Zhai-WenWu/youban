package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ProductDetail;

/**
 * Created by huwenhua on 2016/6/29.
 */
public class FindProductDetailResponse extends BaseResponse {
    private ProductDetail productDetail;//商品详情,

    public ProductDetail getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(ProductDetail productDetail) {
        this.productDetail = productDetail;
    }
}
