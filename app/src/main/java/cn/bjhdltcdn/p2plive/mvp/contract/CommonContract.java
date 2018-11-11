package cn.bjhdltcdn.p2plive.mvp.contract;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/27.
 */

public interface CommonContract {

    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 获取举报类型接口
         * @param type 类型(1其他,2校园店),
         */
        void getReportType(int type);


        /**
         * 举报接口
         *
         * @param id           (帖子id,帖子评论id,帖子回复id,群组id,用户id,表白id,表白评论id,表白回复id,活动id,PK挑战id)
         * @param type         :类型(1-->帖子,2-->帖子评论,3-->帖子回复,4-->群组,5-->用户,6-->表白,7-->表白评论,8-->表白回复,9-->活动,10-->PK挑战,11-->一对一视频),
         *                     type=1/2/3/4/6/7/8时，toUserId是null，其他都是必填;type=5时，id是0，其他都是必填;
         * @param fromUserId   举报人id
         * @param toUserId     被举报人的id
         * @param reportTypeId 举报对象id
         */
        void reportOperation(long id, int type, long fromUserId, long toUserId, long reportTypeId);


    }
}
