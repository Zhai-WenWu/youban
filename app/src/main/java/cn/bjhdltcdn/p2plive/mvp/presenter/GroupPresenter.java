package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.AuditeGroupApplyResponse;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.CreateGroupResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetApplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupInfoResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetGroupUserListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.JoinGroupResponse;
import cn.bjhdltcdn.p2plive.httpresponse.getJoinGroupListResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.GroupContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;
import cn.bjhdltcdn.p2plive.utils.StringUtils;

/**
 * Created by xiawenquan on 17/11/27.
 */

public class GroupPresenter implements GroupContract.Presenter {

    private BaseView mView;

    public GroupPresenter(BaseView mView) {
        this.mView = mView;
    }

    @Override
    public void cancelTag(String tag) {

    }

    @Override
    public void onDestroy() {

        if (mView != null) {
            mView = null;
        }
    }


    @Override
    public void joinGroup(long userId, long groupId, int groupMode) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put("groupMode", groupMode);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_JOIN_GROUP, tag, map, new JsonCallback<JoinGroupResponse>(JoinGroupResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JOIN_GROUP, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_JOIN_GROUP, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getGroupList(long userId, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETGROUPLIST, tag, map, new JsonCallback<GetGroupListResponse>(GetGroupListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null & isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getJoinGroupList(long userId, long toUserId, int pageSize, int pageNumber, final boolean isNeedShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETJOINGROUPLIST, tag, map, new JsonCallback<getJoinGroupListResponse>(getJoinGroupListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                    if (mView != null && isNeedShowLoading) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETJOINGROUPLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETJOINGROUPLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    if (mView != null) {
                        mView.hideLoading();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void createGroup(long userId, long relationId, String groupName, int groupMode, int type, String imgUrl) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.RELATION_ID, relationId);
        map.put(Constants.Fields.GROUP_NAME, groupName);
        map.put(Constants.Fields.GROUP_MODE, groupMode);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.GROUP_IMG, imgUrl);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_CREATEGROUP, tag, map, new JsonCallback<CreateGroupResponse>(CreateGroupResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CREATEGROUP, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_CREATEGROUP, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGroupUserList(long userId, long groupId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETGROUPUSERLIST, tag, map, new JsonCallback<GetGroupUserListResponse>(GetGroupUserListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPUSERLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPUSERLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void signOutGroup(long groupId, long userId, List<Long> outUserIds) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.OUT_USER_IDS, outUserIds);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SIGNOUTGROUP, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SIGNOUTGROUP, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SIGNOUTGROUP, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void disbandGroup(long groupId, long userId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.USER_ID, userId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DISBANDGROUP, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DISBANDGROUP, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DISBANDGROUP, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getGroupInfo(long groupId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETGROUPINFO, tag, map, new JsonCallback<GetGroupInfoResponse>(GetGroupInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPINFO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETGROUPINFO, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void shareGroupInfo(long userId, long groupId, long toUserId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SHAREGROUPINFO, tag, map, new JsonCallback<GetGroupInfoResponse>(GetGroupInfoResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SHAREGROUPINFO, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SHAREGROUPINFO, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGroup(long groupId, String groupName, int groupMode, int isDisturbMode, int isPublic) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);

        if (!StringUtils.isEmpty(groupName)) {
            map.put(Constants.Fields.GROUP_NAME, groupName);
        }

        if (groupMode > -1) {
            map.put(Constants.Fields.GROUP_MODE, groupMode);
        }

        if (isDisturbMode > -1) {
            map.put(Constants.Fields.IS_DISTURB_MODE, isDisturbMode);
        }

        if (isPublic > -1) {
            map.put(Constants.Fields.IS_PUBLIC, isPublic);
        }

        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_UPDATEGROUP, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEGROUP, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_UPDATEGROUP, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void auditeGroupApply(long userId, long applyId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, userId);
        map.put(Constants.Fields.APPLY_ID, applyId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_AUDITEGROUPAPPLY, tag, map, new JsonCallback<AuditeGroupApplyResponse>(AuditeGroupApplyResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_AUDITEGROUPAPPLY, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_AUDITEGROUPAPPLY, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getApplyList(long userId, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETAPPLYLIST, tag, map, new JsonCallback<GetApplyListResponse>(GetApplyListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETAPPLYLIST, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETAPPLYLIST, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGroupManager(long groupId, long managerId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.MANAGER_ID, managerId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_DELETEGROUPMANAGER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEGROUPMANAGER, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_DELETEGROUPMANAGER, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setManager(long groupId, List<Long> managerIds) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.GROUP_ID, groupId);
        map.put(Constants.Fields.MANAGER_IDS, managerIds);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_SETMANAGER, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);

                    if (mView != null) {
                        mView.showLoading();
                    }
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SETMANAGER, response.body());
                    }

                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_SETMANAGER, response.getException());
                    }

                }

                @Override
                public void onFinish() {
                    super.onFinish();

                    if (mView != null) {
                        mView.hideLoading();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
