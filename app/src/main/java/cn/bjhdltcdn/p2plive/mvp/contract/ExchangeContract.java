package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 18/3/8.
 */

public interface ExchangeContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 保存提现账户信息接口
         *
         * @param userId           申请提现的当前用户id
         * @param account          提现账户
         * @param phoneNumber      手机号
         * @param verificationCode
         */
        void saveCashAccount(long userId, String account, String phoneNumber, String verificationCode);

        /**
         * 保存提现账户信息接口
         *
         * @param userId 申请提现的当前用户id
         */
        void findCashAccount(long userId);

    }

}
