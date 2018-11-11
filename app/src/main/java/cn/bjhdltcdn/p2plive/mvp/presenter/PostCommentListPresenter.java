package cn.bjhdltcdn.p2plive.mvp.presenter;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.bjhdltcdn.p2plive.api.ApiData;
import cn.bjhdltcdn.p2plive.api.InterfaceUrl;
import cn.bjhdltcdn.p2plive.callback.JsonCallback;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.httpresponse.BaseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.CommentPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.FindPostDetailResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetConfessionCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetHelpCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPkCommentReplyListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostAndActivityListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostCommentListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.GetPostListResponse;
import cn.bjhdltcdn.p2plive.httpresponse.HelpCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PlayCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.PostPraiseResponse;
import cn.bjhdltcdn.p2plive.httpresponse.SayLoveCommentResponse;
import cn.bjhdltcdn.p2plive.httpresponse.savePostResponse;
import cn.bjhdltcdn.p2plive.mvp.contract.PostCommentListContract;
import cn.bjhdltcdn.p2plive.mvp.view.BaseView;

public class PostCommentListPresenter implements PostCommentListContract.Presenter {

    private BaseView mView;

    public PostCommentListPresenter(BaseView mView) {
        this.mView = mView;
    }


    @Override
    public void cancelTag(String tag) {
        OkGo.getInstance().cancelTag(tag);
    }

    @Override
    public void onDestroy() {
        if (mView != null) {
            mView = null;
        }
    }

    @Override
    public void findPostDetail(long userId, long postId) {
        Map map = new LinkedHashMap(1);
        map.put("userId", userId);
        map.put("postId", postId);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_FINDPOSTDETAIL, tag, map, new JsonCallback<FindPostDetailResponse>(FindPostDetailResponse.class) {

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
                        mView.updateView(InterfaceUrl.URL_FINDPOSTDETAIL, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_FINDPOSTDETAIL, response.getException());
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
    public void getPostCommentList(long userId, long parentId, int sort, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_GETPOSTCOMMENTLIST, tag, map, new JsonCallback<GetPostCommentListResponse>(GetPostCommentListResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPOSTCOMMENTLIST, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_GETPOSTCOMMENTLIST, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postPraise(long userId, long postId, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.POST_ID, postId);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();

        try {
            ApiData.getInstance().postData(InterfaceUrl.URL_POSTPRAISE, tag, map, new JsonCallback<PostPraiseResponse>(PostPraiseResponse.class) {

                @Override
                public void onStart(Request request) {
                    super.onStart(request);
                }

                @Override
                public void onSuccess(Response<PostPraiseResponse> response) {
                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_POSTPRAISE, response.body());
                    }
                }

                @Override
                public void onError(Response response) {
                    super.onError(response);

                    if (mView != null) {
                        mView.updateView(InterfaceUrl.URL_POSTPRAISE, response.getException());
                    }


                }

                @Override
                public void onFinish() {
                    super.onFinish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void postComment(String content, int type, long toUserId, long fromUserId, long postId, long parentId, int anonymousType) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.POST_ID, postId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.ANONYMOUS_TYPE, anonymousType);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_POSTCOMMENT, tag, map, new JsonCallback<PostCommentResponse>(PostCommentResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);

            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_POSTCOMMENT, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_POSTCOMMENT, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });
    }

    @Override
    public void getPostList(long userId, long organId, int sort, int pageSize, int pageNumber) {

        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ORGAN_ID, organId);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_GETPOSTLIST, tag, map, new JsonCallback<GetPostListResponse>(GetPostListResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPOSTLIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPOSTLIST, response.getException());
                }

            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });


    }

    @Override
    public void deleteComment(long userId, long commentId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.COMMENT_ID, commentId);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_DELETECOMMENT, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_DELETECOMMENT, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETECOMMENT, response.getException());
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
    }

    @Override
    public void postTop(long organId, long relationId, int category, int type) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.ORGAN_ID, organId);
        map.put(Constants.Fields.RELATION_ID, relationId);
        map.put(Constants.Fields.CATEGORY, category);
        map.put(Constants.Fields.TYPE, type);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_POSTTOP, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_POSTTOP, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_POSTTOP, response.getException());
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
    }

    @Override
    public void deletePost(long userId, long postId) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.POST_ID, postId);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_DELETEPOST, tag, map, new JsonCallback<BaseResponse>(BaseResponse.class) {


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
                    mView.updateView(InterfaceUrl.URL_DELETEPOST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_DELETEPOST, response.getException());
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

    }

    @Override
    public void getPostAndActivityList(long userId, long organId, int type, int pageSize, int pageNumber, final boolean needShowLoading) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.ORGAN_ID, organId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();


        ApiData.getInstance().postData(InterfaceUrl.URL_GETPOSTANDACTIVITYLIST, tag, map, new JsonCallback<GetPostAndActivityListResponse>(GetPostAndActivityListResponse.class) {


            @Override
            public void onStart(Request request) {
                super.onStart(request);
                if (needShowLoading) {
                    if (mView != null) {
                        mView.showLoading();
                    }
                }

            }

            @Override
            public void onSuccess(Response response) {

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPOSTANDACTIVITYLIST, response.body());
                }

            }

            @Override
            public void onError(Response response) {
                super.onError(response);

                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_GETPOSTANDACTIVITYLIST, response.getException());
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
    }

    @Override
    public void commentUploadImage(long userId, long moduleId, int type, String content, int contentType, int anonymousType, long toUserId,
                                   long fromUserId, long parentId, long commentParentId, int commentType, String videoUrl, String videoImageUrl,
                                   String photoGraphTime, String file) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.MODULE_ID, moduleId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.CONTENT, content);
        map.put(Constants.Fields.CONTENT_TYPE, contentType);
        map.put(Constants.Fields.ANONYMOUS_TYPE, anonymousType);
        map.put(Constants.Fields.TO_USER_ID, toUserId);
        map.put(Constants.Fields.FROM_USER_ID, fromUserId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.COMMENT_PARENT_ID, commentParentId);
        map.put(Constants.Fields.COMMENT_TYPE, commentType);
        map.put(Constants.Fields.VIDEO_URL, videoUrl);
        map.put(Constants.Fields.VIDEO_IMAGE_URL, videoImageUrl);
        map.put(Constants.Fields.PHOTOGRAPH_TIME, photoGraphTime);
        map.put(Constants.Fields.FILE, file);
        String tag = mView.getClass().getSimpleName();
        JsonCallback jsonCallback = null;
        switch (type) {
            case 1:
                jsonCallback = new JsonCallback<PostCommentResponse>(PostCommentResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.getException());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }


                };
                break;
            case 2:
                jsonCallback = new JsonCallback<SayLoveCommentResponse>(SayLoveCommentResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.getException());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }


                };
                break;
            case 3:
                jsonCallback = new JsonCallback<PlayCommentResponse>(PlayCommentResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.getException());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }


                };
                break;
            case 4:
                jsonCallback = new JsonCallback<HelpCommentResponse>(HelpCommentResponse.class) {

                    @Override
                    public void onStart(Request request) {
                        super.onStart(request);
                    }

                    @Override
                    public void onSuccess(Response response) {
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.body());
                        }
                    }

                    @Override
                    public void onError(Response response) {
                        super.onError(response);
                        if (mView != null) {
                            mView.updateView(InterfaceUrl.URL_COMMENTUPLOADIMAGE, response.getException());
                        }
                    }


                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }


                };
                break;


        }
        ApiData.getInstance().uploadImage(InterfaceUrl.URL_COMMENTUPLOADIMAGE, tag, map, jsonCallback);

    }

    @Override
    public void commentPraise(long commentId, int type, long userId, int module) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.COMMENT_ID, commentId);
        map.put(Constants.Fields.TYPE, type);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.MODULE, module);
        String tag = mView.getClass().getSimpleName();

        ApiData.getInstance().postData(InterfaceUrl.URL_COMMENTPRAISE, tag, map, new JsonCallback<CommentPraiseResponse>(CommentPraiseResponse.class) {

            @Override
            public void onStart(Request request) {
                super.onStart(request);
            }

            @Override
            public void onSuccess(Response response) {
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_COMMENTPRAISE, response.body());
                }
            }

            @Override
            public void onError(Response response) {
                super.onError(response);
                if (mView != null) {
                    mView.updateView(InterfaceUrl.URL_COMMENTPRAISE, response.getException());
                }
            }


            @Override
            public void onFinish() {
                super.onFinish();
            }


        });

    }

    @Override
    public void getReplyList(long userId, long commentId, long parentId, int module, int sort, int pageSize, int pageNumber) {
        Map map = new LinkedHashMap(1);
        map.put(Constants.Fields.USER_ID, userId);
        map.put(Constants.Fields.COMMENT_ID, commentId);
        map.put(Constants.Fields.PARENT_ID, parentId);
        map.put(Constants.Fields.MODULE, module);
        map.put(Constants.Fields.SORT, sort);
        map.put(Constants.Fields.PAGE_SIZE, pageSize);
        map.put(Constants.Fields.PAGE_NUMBER, pageNumber);
        String tag = mView.getClass().getSimpleName();
        JsonCallback jsonCallback = null;
        try {
            switch (module) {
                case 1:
                    jsonCallback = new JsonCallback<GetCommentReplyListResponse>(GetCommentReplyListResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    };
                    break;
                case 2:
                    jsonCallback = new JsonCallback<GetConfessionCommentReplyListResponse>(GetConfessionCommentReplyListResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    };
                    break;
                case 3:
                    jsonCallback = new JsonCallback<GetPkCommentReplyListResponse>(GetPkCommentReplyListResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    };
                    break;
                case 4:
                    jsonCallback = new JsonCallback<GetHelpCommentReplyListResponse>(GetHelpCommentReplyListResponse.class) {

                        @Override
                        public void onStart(Request request) {
                            super.onStart(request);
                        }

                        @Override
                        public void onSuccess(Response response) {
                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.body());
                            }
                        }

                        @Override
                        public void onError(Response response) {
                            super.onError(response);

                            if (mView != null) {
                                mView.updateView(InterfaceUrl.URL_GETREPLYLIST, response.getException());
                            }


                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                        }
                    };
                    break;
            }


            ApiData.getInstance().postData(InterfaceUrl.URL_GETREPLYLIST, tag, map, jsonCallback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
