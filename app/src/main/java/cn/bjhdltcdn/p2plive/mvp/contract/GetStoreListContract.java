package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.model.AddressInfo;
import cn.bjhdltcdn.p2plive.model.FirstLabelInfo;
import cn.bjhdltcdn.p2plive.model.ProductInfo;
import cn.bjhdltcdn.p2plive.model.StoreInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by huwenhua on 17/11/17.
 */

public interface GetStoreListContract {


    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 搜索店铺接口
         * @param userId
         * @param content
         * @param distanceSort
         * @param labelIds
         * @param pageSize
         * @param pageNumber
         */
        void findStoreKeywordList(long userId,String content,long distanceSort,List<Long> labelIds,int pageSize, int pageNumber);

        /**
         * 搜索商品接口
         * @param userId
         * @param content
         * @param distanceSort
         * @param labelIds
         * @param pageSize
         * @param pageNumber
         */
        void findProductInfoKeywordList(long userId,String content,long distanceSort,List<Long> labelIds,int pageSize, int pageNumber);

        /**
         * 查询店铺列表接口
         *
         * @param userId       用户Id,
         * @param content      搜索内容
         * @param distanceSort 距离排序(默认查询本校
         * @param merchantSort 商家排序(0默认全部
         * @param labelId      店铺类别排序(0默认全部
         * @param pageSize     每页大小
         * @param pageNumber   第几页
         */
        void getStoreList(long userId, String content, long distanceSort, int merchantSort, long labelId, int pageSize, int pageNumber);

        /**
         * 查询官方推荐的商家接口
         *
         * @param type   0：店铺banner 1:店铺推荐
         * @param userId 用户Id
         */
        void getRecommendMerchantList(int type, long userId);

        /**
         * 查询店铺标签接口
         *
         * @param userId 用户Id
         */
        void getStoreLabelList(long userId);

        /**
         * 判断用户开店权限接口
         *
         * @param userId 用户Id
         */
        void judgeCreateStoreAuth(long userId);

        /**
         * 申请开店
         *
         * @param userId       用户Id
         * @param cardFrontImg 学生证封面
         * @param cardBackImg  学生证有你身份信息的页面
         */
        void applyCreateStore(long userId, String cardFrontImg, String cardBackImg);

        /**
         * 创建/编辑店铺接口
         *
         * @param userId    用户Id
         * @param storeInfo 店铺对象
         */
        void updateCreateStore(long userId, StoreInfo storeInfo);

        /**
         * 上传店铺视频接口
         *
         * @param userId        用户Id
         * @param storeId       店铺id
         * @param videoUrl      视频路径
         * @param videoImageUrl 视频缩略图路径
         */
        void updateStoreVideo(long userId, long storeId, String videoUrl, String videoImageUrl);

        /**
         * 添加/编辑商品接口
         *
         * @param userId             用户Id,
         * @param storeId            店铺id
         * @param productId          商品id
         * @param productName        商品名称
         * @param productDesc        商品描述信息
         * @param productPrice       商品原价
         * @param productDiscount    商品折扣活动
         * @param salePrice          折后价(商品售价)
         * @param productRemainTotal 库存
         * @param status             商品状态
         */
        void updateProductInfo(long userId, long storeId, long productId, String productName, String productDesc, String productPrice, String productDiscount, String salePrice, int productRemainTotal, int status);

        /**
         * 获取店铺详情接口
         *
         * @param userId  用户Id
         * @param storeId 店铺Id
         */
        void findStoreDetail(long userId, long storeId);

        /**
         * 查询店铺商品列表接口
         *
         * @param userId     用户Id
         * @param storeId    店铺Id
         * @param pageSize   每页大小
         * @param pageNumber 第几页
         */
        void getStoreProductList(long userId, long storeId, int pageSize, int pageNumber);

        /**
         * 获取店铺商品详情接口
         *
         * @param userId    用户Id
         * @param productId 商品Id
         */
        void findProductDetail(long userId, long productId);

        /**
         * 查询店铺订单列表接口
         *
         * @param userId     用户Id
         * @param storeId    店铺Id
         * @param labelId    标签类型(25全部,26待发货-0,27已付款-2,28已完成-1/4
         * @param pageSize   页面大小
         * @param pageNumber 第几页
         */
        void getOrderList(long userId, long storeId, long labelId, int pageSize, int pageNumber);

        /**
         * 查询订单评价列表接口
         *
         * @param userId     用户Id
         * @param storeId    店铺Id
         * @param type       类型(0全部,1满意,2不满意,3有图
         * @param pageSize   每页大小
         * @param pageNumber 第几页
         */
        void getOrderEvaluateList(long userId, long storeId, int type, int pageSize, int pageNumber);

        /**
         * 查询销售记录列表接口
         *
         * @param userId     用户Id
         * @param storeId    店铺Id
         * @param type       类型(0全部,1本月
         * @param pageSize   每页大小
         * @param pageNumber 第几页
         */
        void getSalesRecordtList(long userId, long storeId, int type, int pageSize, int pageNumber);

        /**
         * @param userId       用户Id,
         * @param storeId      店铺id,
         * @param phoneNumber  手机号,
         * @param addr         地址,
         * @param selfDesc     selfDesc
         * @param cardFrontImg 学生证封面,
         * @param cardBackImg  学生证有你身份信息的页面,
         */

        void applyClert(long userId, long storeId, String phoneNumber, String addr, String selfDesc, String cardFrontImg, String cardBackImg);

        /**
         * @param userId  用户Id,
         * @param applyId 申请id,
         */
        void findClertApply(long userId, long applyId);

        /**
         * @param userId  用户Id,
         * @param applyId 申请id,
         * @param status  审核状态(1录用,2未通过),
         */
        void authClertApply(long userId, long applyId, int status);

        /**
         * @param userId     用户Id,
         * @param storeId    店铺Id,
         * @param pageSize
         * @param pageNumber
         */
        void getClertList(long userId, long storeId, int pageSize, int pageNumber);

        /**
         * 查询订单评价列表接口
         *
         * @param userId    用户Id
         * @param productId 商品Id,
         * @param isHot     是否热推商品(0不修改,1否,2是),
         * @param isNew     是否新商品(0不修改,1否,2是
         * @param status    商品状态(0不修改,1正常,2下架
         */
        void updateProductStatus(long userId, long storeId, long productId, int isHot, int isNew, int status);


        /**
         * @param userId   用户Id,
         * @param toUserId 解雇用户Id
         * @param storeId  店铺id,
         */
        void fireClerk(long userId, long toUserId, long storeId);

        /**
         * 获取店员详情接口
         *
         * @param userId   用户Id,
         * @param toUserId 解雇用户Id
         * @param storeId  店铺id,
         */
        void findClertDetail(long userId, long toUserId, long storeId, int pageSize, int pageNumber, int type);

        /**
         * 获取用户默认收货地址接口
         *
         * @param userId 用户Id,
         */
        void findUserDefaultAddress(long userId);


        /**
         * 保存用户收货地址接口
         *
         * @param userId      用户Id,
         * @param addressInfo ":{AddressInfo对象}用户地址信息
         */
        void saveH5ReceiptAddress(long userId, AddressInfo addressInfo);

        /**
         * 编辑用户收货地址接口
         *
         * @param userId      用户Id,
         * @param addressInfo ":{AddressInfo对象}用户地址信息
         */
        void updateH5ReceiptAddress(long userId, AddressInfo addressInfo);

        /**
         * 删除用户收货地址接口
         *
         * @param userId    用户Id,
         * @param addressId 地址id
         */
        void updateH5ReceiptAddress(long userId, long addressId);

        /**
         * 查询用户收货地址列表接口
         *
         * @param userId
         * @param pageSize
         * @param pageNumber
         */
        void getH5ReceiptAddressList(long userId, int pageSize, int pageNumber);

        /**
         * 获取提现数据接口
         *
         * @param userId   用户ID,
         * @param storeId  ":店铺Id
         * @param hintType 类型 1 兑换比例提示类型
         */
        void findExchangeInfo(long userId, int hintType);

        /**
         * 立即提现接口
         *
         * @param userId         用户ID,
         * @param type           类型(1--->支付宝),
         * @param exchangeAmount 提现金额
         */
        void saveExchangeInfo(long userId, int type, String exchangeAmount);

        /**
         * 查询提现明细接口
         *
         * @param userId     用户ID
         * @param pageSize
         * @param pageNumber
         */
        void getExchangeList(long userId, int pageSize, int pageNumber);

        /**
         * 商品支付宝订单保存
         *
         * @param productId      商品id
         * @param productNum     购买数量
         * @param userId         用户id
         * @param addressId      收获地址id
         * @param reqType        请求类型 1 安卓 2 IOS
         * @param distributeMode 配送方式(1自取,2卖家配送),
         * @param storeId        店铺Id,
         * @param productList    [{ProductInfo对象},{ProductInfo对象}...]购买商品列表,
         * @param remark         备注
         */
        void saveAlipayProductOrder(long productId, int productNum, long userId, long addressId, int reqType, long distributeMode, long storeId, List<ProductInfo> productList,String postAge, String remark);

        /**
         * 商品微信支付订单保存
         *
         * @param productId      商品id
         * @param productNum     购买数量
         * @param userId         用户id
         * @param addressId      收获地址id
         * @param reqType        请求类型 1 安卓 2 IOS
         * @param ip             ip地址
         * @param distributeMode 配送方式(1自取,2卖家配送),
         * @param remark         备注
         */
        void saveWeixinProductOrder(long productId, int productNum, long userId, long addressId, int reqType, String ip, int distributeMode, String remark);


        /**
         * @param userId  用户Id,
         * @param orderId 订单Id(商家配送订单),
         */
        void sellerReceipt(long userId, long orderId);


        /**
         * @param userId
         * @param orderId
         */
        void confirmRefund(long userId, long orderId);

        /**
         * @param userId   用户Id,
         * @param toUserId 收工资用户Id
         * @param amount   付款金额
         */
        void saveShopUserPayOff(long userId, long toUserId, String amount);

        /**
         * @param userId  户Id,
         * @param storeId 店铺Id
         */
        void findUserApplyStatus(long userId, long storeId);

        void judgeUserReceipt(long userId, long orderId);

        /**
         * @param orderId 订单Id,
         * @param isClert 卖家-->1 ,买家-->3,
         */
        void refuseRefund(long orderId, int isClert);

        /**
         * 判断用户是否开店接口
         * @param userId
         */
        void judgeIsCreateStore(long userId);

        /**
         * 获取校园店标签接口
         * @param userId 用户Id
         */
        void getShopLabelList(long userId);

        /**
         * 保存用户店铺标签接口
         * @param userId 用户Id
         * @param firstLabelInfo FirstLabelInfo
         */
        void saveUserStoreLabel(long userId, FirstLabelInfo firstLabelInfo);
    }
}
