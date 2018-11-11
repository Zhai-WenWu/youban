package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.model.User;
import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by ZHUDI on 2017/11/16.
 */

public interface CompleteInfoContract {
    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 个人信息修改接口
         *
         * @param user 用户对象
         */
        void changedUserInfo(User user);

        /**
         * 查询兴趣爱好列表接口
         *
         * @param userId 用户id
         */
        void getHobbyList(long userId, int moduleType);

        /**
         * 查询职业列表接口
         *
         * @param userId
         */
        void getOccupationList(long userId);

        /**
         * 修改密码
         *
         * @param userId
         */
        void ResetPassword(long userId, String oldPassword, String newPassword);

        /**
         * 搜索学校列表接口
         *
         * @param content 输入的内容
         */
        void searchSchoolList(String content,int pageSize,int pageNumber);

        /**
         * 更新用户所在位置信息
         *
         * @param userId    用户Id
         * @param longitude 经度
         * @param latitude  维度
         * @param province  省
         * @param city      市
         * @param district  区县
         * @param add       地址
         */
        void updateUserLocation(long userId, String longitude, String latitude, String province,
                                String city, String district, String add);
    }
}
