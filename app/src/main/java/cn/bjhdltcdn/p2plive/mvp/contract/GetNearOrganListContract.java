//package cn.bjhdltcdn.p2plive.mvp.contract;
//
//import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;
//
//public interface GetNearOrganListContract {
//
//    /**
//     * 业务控制器
//     */
//    interface Presenter extends BasePresenter {
//
//        /**
//         * 查询附近圈子
//         * @param userId 用户Id,
//         *  @param pageSize 页面大小,
//         *  @param pageNumber 第几页,
//         */
//        void getNearOrganList(long userId,int pageSize,int pageNumber);
//
//        /**
//         * 查询附近的人列表接口
//         * @param userId 用户Id,
//         * @param type 类型(0全部,1同城,2校友),
//         * @param sexLimit 性别限制(0全部,1男生,2女生),
//         * @param pageSize
//         * @param pageNumber
//         */
//        void getNearPersonList(long userId,int type,int sexLimit,int pageSize,int pageNumber);
//
//        /**
//         * 查询发送邀请函的活动列表接口
//         * @param userId 用户Id(当前操作的用户),
//         * @param toUserId 接收用户Id
//         * @param pageSize  页面大小,
//         * @param pageNumber 第几页,
//         */
//        void getInvitationActionList(long userId,long toUserId,int pageSize,int pageNumber);
//
//        /**
//         * 发送附近人邀请函接口
//         * @param userId 用户Id(当前操作的用户),
//         * @param toUserId 发送给邀请函的用户Id
//         * @param activityId 活动Id,
//         */
//        void sendNearInvitation(long userId,long toUserId,long activityId);
//    }
//}