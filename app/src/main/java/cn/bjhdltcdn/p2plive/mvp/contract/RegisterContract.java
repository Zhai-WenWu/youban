package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface RegisterContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 注册
         *
         * @param userName         账号
         * @param password         密码
         * @param verificationCode 验证码
         */
        void register(String userName, String password, String verificationCode);

        /**
         * 绑定手机号
         *
         * @param userId           用户Id,
         * @param phoneNumber      手机号,
         * @param verificationCode 验证码,
         */
        void bindPhoneNumber(long userId, String phoneNumber, String verificationCode);

        /**
         * 获取验证码
         *
         * @param phoneNumber 手机号
         */
        void getVerificationCode(String phoneNumber);

    }
}