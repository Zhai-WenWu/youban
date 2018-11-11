package cn.bjhdltcdn.p2plive.mvp.contract;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ActivityInfo;
import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

public interface SaveActiveContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 发起活动接口
         * @param userId 用户Id,
         * @param organId 圈子Id(用户在圈子页面发布活动时传入圈子Id,其他默认0),
         * @param activeInfo {ActivityInfo}活动对象,
         */
        void saveActive(long userId,long  organId,ActivityInfo activeInfo);

        /**
         * 修改活动
         * @param activeInfo {ActivityInfo}活动对象,
         */
        void updateActive(long userId, ActivityInfo activeInfo);

        /**
         * 查询活动人数/支付方式分类
         */
        void getActiveTypeList();

        /**
         * 图片删除
         * @param imageId 图片id,
         * @param type 类型(21-->店铺宣传图片,22-->商品图片,其他不用传值),
         */
        void deleteImage(long imageId,int type);

        /**
         * 发送活动邀请函接口
         * @param userId 用户Id,
         * @param activityId 活动Id,
         * @param hobbyIds [{HobbyId},{HobbyId}...](活动类型为1时赋值),
         * @param sexLimit 性别限制(0不限,1仅限男生,2仅限女生),
         * @param sendNumber 发送数量
         * @param type 类型(0全部,1校友,2同城),
         * @param method 操作方式(1查询,2发送),
         * @param userIds [{userId},{userId}...](指定人列表),
         * @param pageSize 每页大小,
         * @param pageNumber 第几页,
         */
        void sendActivityInvitation(long userId, Long activityId,List<Long> hobbyIds,int sexLimit,int sendNumber,int type,int method,List<Long> userIds,int pageSize,int pageNumber);

    }
}