package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface LoginContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 手机号登录
         *
         * @param userName 账号
         * @param password 密码
         * @param userType 用户类型(1--->使用手机号注册，2---->QQ，3--->微信，4--->微博),
         */
        void loginByPhone(String userName, String password, int userType);

        /**
         * 第三方登录
         *
         * @param userName 账号
         * @param location 所在地
         * @param sex      性别 (1-->女性，2-->男性)
         * @param userIcon 头像
         * @param birthday 生日
         * @param nickName 昵称
         * @param userType 用户类型(1--->使用手机号注册，2---->QQ，3--->微信，4--->微博)
         * @param uniqueId 第三方的唯一标识
         */
        void loginByThirdParty(String userName, String location, int sex, String userIcon, String birthday,
                               String nickName, int userType, String uniqueId);

        /**
         * 忘记密码接口
         *
         * @param userName         用户名
         * @param newPassword      新密码
         * @param verificationCode 验证码
         */
        void forgetPassword(String userName, String newPassword, String verificationCode);

        /**
         * 检查手机号是否注册接口
         *
         * @param phoneNumber 手机号
         */
        void checkPhoneNumber(String phoneNumber);
    }
}