package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/17.
 */

public interface PayContract {


    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 获取产品服务列表
         */
        void getServiceInfo();

        /**
         * 支付宝订单保存
         * @param serviceId :服务id,
         * @param userId 用户id
         * @param reqType 请求类型 1 安卓 2 IOS
         */
        void saveAlipayOrder(String serviceId,long userId,int reqType);

        /**
         * 微信支付订单保存
         * @param serviceId 服务id,
         * @param userId 用户id
         * @param reqType 请求类型 1 安卓 2 IOS
         * @param ip ip地址
         */
        void saveWeixinOrder(String serviceId,long userId,int reqType,String ip);


    }
}
