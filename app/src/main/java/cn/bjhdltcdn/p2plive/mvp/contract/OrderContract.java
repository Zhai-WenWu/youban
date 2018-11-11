package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHAI on 2018/2/27.
 */

public interface OrderContract {
    interface Presenter extends BasePresenter {
        /**
         * @param userId  :用户Id,
         * @param labelId :标签类型(22全部,23已收货,24待收货),
         */
        void getMyOrderList(long userId, long labelId, int pageSize, int pageNumber);

        /**
         * @param userId      :用户Id,
         * @param orderId     :订单Id,
         * @param receiptCode :取货码,
         */
        void updateOrderStatus(long userId, long orderId, String receiptCode, int evalScore, int isClert);

        /**
         * 获取订单详情接口
         *
         * @param userId  :用户Id,
         * @param orderId :订单Id,
         */
        void findOrderDetail(long userId, long orderId);

        /**
         * 获取订单详情接口
         *
         * @param userId :用户Id,
         */
        void getTrailReportList(long userId, int pageSize, int pageNumber);

        /**
         * 发起退货
         *
         * @param userId :用户Id,
         */
        void applyRefund(long userId, long orderId, long reasonId, String remark);

        /**
         * @param userId          :用户Id,
         * @param orderId         :订单Id,
         * @param productId       商品Id,
         * @param toUserId        被回复人的id,
         * @param fromUserId      评论/回复人的id,
         * @param parentId        评论commentId(评论时传0),
         * @param commentParentId 楼中楼的父评论id,
         * @param contentType     内容类型(1--->评论,2--->回复),
         * @param evalScore       :评价分数,
         * @param content         :评价内容,
         * @param commentType     :评论/回复类型(0-->普通文本,1-->图/图文,2--->视频),
         * @param videoUrl        :评论/回复视频路径(类型为视频使用),
         * @param videoImageUrl   :评论/回复图片地址(类型为视频使用),,
         */
        void saveOrderEvaluate(long userId, long orderId, long productId, long toUserId, long fromUserId, long parentId, long commentParentId, int contentType, int evalScore, String content, int commentType, String videoUrl, String videoImageUrl);

        /**
         * @param userId 用户Id,
         */
        void getRefundReasonList(long userId);

    }
}
