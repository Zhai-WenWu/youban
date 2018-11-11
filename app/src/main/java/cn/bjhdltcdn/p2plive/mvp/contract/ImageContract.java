package cn.bjhdltcdn.p2plive.mvp.contract;

import java.io.File;
import java.util.List;

import cn.bjhdltcdn.p2plive.mvp.presenter.BasePresenter;

/**
 * Created by xiawenquan on 17/11/21.
 */

public interface ImageContract {


    /**
     * 业务控制器
     */
    interface Presenter extends BasePresenter {

        /**
         * 多张图片上传接口
         *
         * @param parentId       所属id(type为1、2用户Id,3传圈子Id,4传活动Id,5传帖子Id,6传表白Id,7传房间Id,8\9\10传实名认证Id,11传群组Id,12PK赛Id,13圈子赛Id,14帮帮忙Id,15帖子评论回复Id,16表白评论回复Id,17帮帮忙评论回复Id,18店铺Id,19/20-0,21店铺Id,22商品Id,23订单评论回复Id),
         * @param type           上传类型(1-->头像,2-->个人相册,3-->圈子封面,4-->活动相册,5-->帖子相册,6-->表白相册,7-->房间封面,8-->正面照片,9-->反面照片,10-->手持身份证照片,11-->群组,12-->比赛截图,13-->圈子赛截图,14-->帮帮忙,15-->帖子评论回复,16-->表白评论回复,17-->帮帮忙评论回复,18-->店铺头像,19-->学生证正面照片,20-->学生证反面照片,21-->店铺宣传图片,22-->商品图片,23-->订单评论回复,24-->商品详情商品图片),
         * @param userId         上传人id
         * @param photoGraphTime 拍照时间
         * @param fileUrl        图片路径
         * @param imageId        图片id
         * @param orderNum       图片顺序
         * @param total          图片总数
         */

        void uploadImages(long parentId, int type, long userId, String photoGraphTime, String fileUrl, long imageId, int orderNum, int total);

        /**
         * 图片上传接口
         *
         * @param parentId       所属id(type为1用户Id,2传帖子Id,2传表白Id)
         * @param type           上传类型(1-->头像,2-->个人相册,3-->圈子封面,4-->活动相册,5-->帖子相册,6-->表白相册,7-->房间封面,8-->正面照片,9-->反面照片,10-->手持身份证照片,11-->群组)
         * @param userId         上传人id
         * @param photoGraphTime 拍照时间
         * @param fileUrl        图片路径
         */
        void uploadImage(long parentId, int type, long userId, String photoGraphTime, String fileUrl);


        /**
         * 图片上传接口
         *
         * @param parentId       所属id(type为1用户Id,2传帖子Id,2传表白Id)
         * @param type           上传类型(1-->头像,2-->个人相册,3-->圈子封面,4-->活动相册,5-->帖子相册,6-->表白相册,7-->房间封面,8-->正面照片,9-->反面照片,10-->手持身份证照片,11-->群组)
         * @param userId         上传人id
         * @param photoGraphTime 拍照时间
         * @param fileUrl        图片路径
         */
        void uploadImageSynchronizing(long parentId, int type, long userId, String photoGraphTime, String fileUrl, Class calzz);


    }


}
