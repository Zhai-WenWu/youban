package cn.bjhdltcdn.p2plive.api;

/**
 * Created by xiawenquan on 17/11/7.
 */

public class InterfaceUrl {
    /**
     * 注册
     */
    public static final String URL_REGISTER = "/user/register";

    /**
     * 获取验证码接口
     */
    public static final String URL_GETVERIFICATIONCODE = "/user/getVerificationCode";
    /**
     * 忘记密码
     */
    public static final String URL_FORGETPASSWORD = "/user/forgetPassword";

    /**
     * 获取实名认证状态接口
     */
    public static final String URL_FINDUSERAUTHSTATUS = "/auth/findUserAuthStatus";

    /**
     * 检查手机号是否注册接口
     */
    public static final String URL_CHECKPHONENUMBER = "/user/checkPhoneNumber";
    /**
     * 登录
     */
    public static final String URL_LOGINBYPHONE = "/user/loginByPhone";

    /**
     * 第三方登录接口
     */
    public static final String URL_LOGINBYTHIRDPARTY = "/user/loginByThirdParty";

    /**
     * 个人信息修改接口
     */
    public static final String URL_CHANGEDUSERINFO = "/user/changedUserInfo";

    /**
     * 获取是否强制升级信息接口
     */
    public static final String URL_GETUPGRADEVERSION = "/common/getUpgradeVersion";

    /**
     * 重置密码接口
     */
    public static final String URL_RESETPASSWORD = "/user/reSetPassWord";

    /**
     * 查询兴趣爱好列表接口
     */
    public static final String URL_GETHOBBYLIST = "/user/getHobbyList";

    /**
     * 更新用户所在位置信息
     */
    public static final String URL_UPDATEUSERLOCATION = "/user/updateUserLocation";

    /**
     * 查询帖子列表
     */
    public static final String URL_GETPOSTLIST = "/organ/getPostList";

    /**
     * 查询我的关注接口
     */
    public static final String URL_GETATTENTIONLIST = "/home/getAttentionList";
    /**
     * 查询线下活动列表接口
     */
    public static final String URL_GETOFFLINEACTIVELIST = "/active/getOfflineActiveList";
    /**
     * 查询表白列表接口
     */
    public static final String URL_GETSAYLOVELIST = "/sayLove/getSayLoveList";
    /**
     * 我发布的表白接口
     */
    public static final String URL_GETMYSAYLOVELIST = "/sayLove/getMySayLoveList";
    /**
     * 附近热帖列表接口
     */
    public static final String URL_GETNEARHOTPOSTLIST = "/organ/getNearHotPostList";
    /**
     * 获取活动详情接口
     */
    public static final String URL_FINDACTIVEDETAIL = "/active/findActiveDetail";
    /**
     * 发起活动接口
     */
    public static final String URL_SAVEACTIVE = "/active/saveActive";
    /**
     * 修改活动接口
     */
    public static final String URL_UPDATEACTIVE = "/active/updateActive";
    /**
     * 查询热门搜索关键字列表
     */
    public static final String URL_GETHOTWORDSLIST = "/home/getHotWordsList";
    /**
     * 获取首页banner列表
     */
    public static final String URL_GETHOMEBANNERLIST = "/home/getHomeBannerList";

    /**
     * 查询附近最新更新条数
     */
    public static final String URL_GETNEWCOUNTLIST = "/home/getNewCountList";

    /**
     * 参与活动
     */
    public static final String URL_JOINACTIVE = "/active/joinActive";

    /**
     * 退出活动
     */
    public static final String URL_SIGNOUTACTIVE = "/active/signOutActive";

    /**
     * 删除活动
     */
    public static final String URL_DELETEACTIVE = "/active/deleteActive";

    /**
     * 查询附近圈子
     */
    public static final String URL_GETNEARORGANLIST = "/organ/getNearOrganList";

    /**
     * 发起表白
     */
    public static final String URL_SAVESAYLOVE = "/sayLove/saveSayLove";

    /**
     * 查询发现页数据接口
     */
    public static final String URL_GETDISCOVERLIST = "/room/getDiscoverList";

    /**
     * 查询多人视频列表接口
     */
    public static final String URL_GETROOMLIST = "/room/getRoomList";

    /**
     * 查询标签接口
     */
    public static final String URL_GETLABELLIST = "/room/getLabelList";

    /**
     * 开启/关闭视频房间接口
     */
    public static final String URL_UPDATEROOMSTATUS = "/room/updateRoomStatus";

    /**
     * 获取多人视频详情接口
     */
    public static final String URL_FINDROOMDETAIL = "/room/findRoomDetail";

    /**
     * 进入/退出视频房间接口
     */
    public static final String URL_UPDATEUSERSTATUS = "/room/updateUserStatus";

    /**
     * 获取上麦用户列表接口
     */
    public static final String URL_GETONWHEATLIST = "/room/getOnWheatList";

    /**
     * 获取房间中所有在线用户接口
     */
    public static final String URL_GETONLINEUSERLIST = "/room/getOnlineUserList";

    /**
     * 获取上麦申请用户列表接口
     */
    public static final String URL_GETONWHEATPAALYLIST = "/room/getOnWheatApplyList";

    /**
     * 用户上/下麦接口
     */
    public static final String URL_UPORDOWNWHEAT = "/room/upOrDownWheat";

    /**
     * 用户视频摄像头控制接口
     */
    public static final String URL_CONTROLUSERCAMERA = "/room/controlUserCamera";

    /**
     * 移交主持权限接口
     */
    public static final String URL_TRANSFERHOSTING = "/room/transferHosting";

    /**
     * 禁言接口
     */
    public static final String URL_BANNINGCOMMENTS = "/room/banningComments";

    /**
     * 分享房间接口
     */
    public static final String URL_SHAREROOM = "/room/shareRoom";

    /**
     * 用户投票接口
     */
    public static final String URL_USERVOTING = "/room/userVoting";

    /**
     * 发起/关闭投票接口
     */
    public static final String URL_INITIATEVOTE = "/room/initiateVote";

    /**
     * 获取本轮投票数据接口
     */
    public static final String URL_GETCURRENTVOTEDATA = "/room/getCurrentVoteData";

    /**
     * 获取房主信息接口
     */
    public static final String URL_GETOWNERINFO = "/room/getOwnerInfo";

    /**
     * 查询帖子评论/回复列表
     */
    public static final String URL_GETPOSTCOMMENTLIST = "/organ/getPostCommentList";
    /**
     * 查询表白评论/回复列表
     */
    public static final String URL_GETCOMMENTLIST = "/sayLove/getCommentList";
    /**
     * 表白评论/回复
     */
    public static final String URL_SAYLOVECOMMENT = "/sayLove/sayLoveComment";

    /**
     * 表白点赞
     */
    public static final String URL_SAYLOVEPRAISE = "/sayLove/sayLovePraise";

    /**
     * 删除表白评论/回复
     */
    public static final String URL_DELETESAYLOVECOMMENT = "/sayLove/deleteSayLoveComment";

    /**
     * 查询圈子列表接口
     */
    public static final String URL_GETORGANIZATIONLIST = "/organ/getOrganizationList";

    /**
     * 获取个人用户信息接口
     */
    public static final String URL_GETUSERINFO = "/user/getUserInfo";

    /**
     * 我参加的活动接口
     */
    public static final String URL_GETJOINACTIONLIST = "/active/getJoinActionList";

    /**
     * 我发布的帖子接口
     */
    public static final String URL_GETPUBLISHPOSTLIST = "/organ/getPublishPostList";

    /**
     * 我的账户接口
     */
    public static final String URL_MYACCOUNT = "/user/myAccount";

    /**
     * 我的礼物接口
     */
    public static final String URL_MYPROPSLIST = "/props/myPropsList";

    /**
     * 获取道具列表接口
     */
    public static final String URL_GETPROPSLIST = "/props/getPropsList";

    /**
     * 获取黑名单列表接口
     */
    public static final String URL_GETBLACKLIST = "/user/getBlackList";
    /**
     * 获取交易记录接口
     */
    public static final String URL_GETTRANSACTIONRECORDLIST = "/user/transactionRecordList";

    /**
     * 拉黑好友接口
     */
    public static final String URL_PULLBLACKUSER = "/user/pullBlackUser";

    /**
     * 解除好友拉黑接口
     */
    public static final String URL_REMOVEBLACKLIST = "/user/removeBlackList";

    /**
     * 关注状态接口
     */
    public static final String URL_ATTENTIONSTATUS = "/user/attentionStatus";

    /**
     * 关注/取消关注接口
     */
    public static final String URL_ATTENTIONOPERATION = "/user/attentionOperation";

    /**
     * 圈子名称判断接口
     */
    public static final String URL_JUDGEORGANNAME = "/organ/judgeOrganName";

    /**
     * 创建圈子接口
     */
    public static final String URL_SAVEORGANIZATIONT = "/organ/saveOrganizationt";


    /**
     * 获取圈子详情接口
     */
    public static final String URL_FINDORGANIZATIONDETAIL = "/organ/findOrganizationDetail";


    /**
     * 获取圈子成员接口
     */
    public static final String URL_FINDORGANIZATIONMEMBER = "/organ/findOrganizationMember";

    /**
     * 退出圈子接口
     */
    public static final String URL_SIGNOUTORGANIZATION = "/organ/signOutOrganization";
    /**
     * 关键字搜索接口
     */
    public static final String URL_SEARCHKEYWORDLIST = "/home/searchKeywordList";
    /**
     * 编辑圈子接口
     */
    public static final String URL_UPDATEORGANIZATION = "/organ/updateOrganization";
    /**
     * 查询关键字列表接口
     */
    public static final String URL_GETSEARCHDATALIST = "/home/getSearchDataList";
    /**
     * 图片上传接口
     */
    public static final String URL_UPLOADIMAGE = "/image/uploadImage";
    /**
     * 更新刷新时间接口
     */
    public static final String URL_UPDATEREFRESHTIME = "/home/updateRefreshTime";
    /**
     * 查询职业列表接口
     */
    public static final String URL_GETOCCUPATIONLIST = "/user/getOccupationList";

    /**
     * 搜索学校列表接口
     */
    public static final String URL_SEARCHSCHOOLLIST = "/user/searchSchoolList";


    /**
     * 点赞帖子接口
     */
    public static final String URL_POSTPRAISE = "/organ/postPraise";
    /**
     * 删除表白接口
     */
    public static final String URL_DELETESAYLOVE = "/sayLove/deleteSayLove";
    /**
     * 查询二级兴趣爱好列表接口
     */
    public static final String URL_GETSECONDHOBBYLIST = "/user/getSecondHobbyList";

    /**
     * 获取用户基本信息接口
     */
    public static final String URL_GETBASEUSERINFO = "/user/getBaseUserInfo";


    /**
     * 更新用户所在位置信息
     */
    public static final String URL_UPDATE_USER_LOCATION = "/user/updateUserLocation";

    /**
     * 查询活动人数/支付方式分类接口
     */
    public static final String URL_GETACTIVETYPELIST = "/active/getActiveTypeList";


    /**
     * 发布帖子接口
     */
    public static final String URL_SAVEPOST = "/organ/savePost";

    /**
     * 帖子评论/回复接口
     */
    public static final String URL_POSTCOMMENT = "/organ/postComment";

    /**
     * 加入圈子接口
     */
    public static final String URL_JOINORGANIZATION = "/organ/joinOrganization";

    /**
     * 加入群组接口
     */
    public static final String URL_JOIN_GROUP = "/group/joinGroup";

    /**
     * 删除帖子评论/回复接口
     */
    public static final String URL_DELETECOMMENT = "/organ/deleteComment";

    /**
     * 删除帖子接口
     */
    public static final String URL_DELETEPOST = "/organ/deletePost";

    /**
     * 帖子置顶接口
     */
    public static final String URL_POSTTOP = "/organ/postTop";


    /**
     * 举报接口
     */
    public static final String URL_REPORTOPERATION = "/common/reportOperation";

    /**
     * 获取举报类型接口
     */
    public static final String URL_GETREPORTTYPE = "/common/getReportType";

    /**
     * 我参与的PK挑战接口
     */
    public static final String URL_GETMYPLAYLIST = "/play/getMyPlayList";

    /**
     * 查询PK挑战评论/回复列表接口
     */
    public static final String URL_GETPLAYCOMMENTLIST = "/play/getPlayCommentList";

    /**
     * 查询PK挑战评论/回复列表接口
     */
    public static final String URL_PLAYCOMMENT = "/play/playComment";

    /**
     * 图片删除接口
     */
    public static final String URL_DELETEIMAGE = "/image/deleteImage";

    /**
     * 设置圈子管理员接口
     */
    public static final String URL_SETORGANMANAGER = "/organ/setOrganManager";

    /**
     * 查询群组列表接口
     */
    public static final String URL_GETGROUPLIST = "/group/getGroupList";

    /**
     * 我加入的群组接口
     */
    public static final String URL_GETJOINGROUPLIST = "/group/getJoinGroupList";

    /**
     * 创建群组接口
     */
    public static final String URL_CREATEGROUP = "/group/createGroup";

    /**
     * 获取关注/粉丝列表
     */
    public static final String URL_GETATTENTIONORFOLLOWERLIST = "/user/getAttentionOrFollowerList";

    /**
     * 查询通讯录列表接口
     */
    public static final String URL_GETFRIENDLIST = "/friend/getFriendList";

    /**
     * 查询群组成员列表接口
     */
    public static final String URL_GETGROUPUSERLIST = "/group/getGroupUserList";


    /**
     * 退出群组接口
     */
    public static final String URL_SIGNOUTGROUP = "/group/signOutGroup";

    /**
     * 解散群组接口
     */
    public static final String URL_DISBANDGROUP = "/group/disbandGroup";


    /**
     * 获取群组信息接口
     */
    public static final String URL_GETGROUPINFO = "/group/getGroupInfo";

    /**
     * 分享群组信息接口
     */
    public static final String URL_SHAREGROUPINFO = "/group/shareGroupInfo";

    /**
     * 更新群组信息接口
     */
    public static final String URL_UPDATEGROUP = "/group/updateGroup";

    /**
     * 又拍云视频截图（M3U8 截图）
     */
    public static final String URL_UPYUN_SNAPSHOT = "http://p1.api.upyun.com/video-hdlt-com/snapshot";


    /**
     * 查询表白详情接口
     */
    public static final String URL_FINDSAYLOVEDETAIL = "/sayLove/findSayLoveDetail";

    /**
     * 我加入的圈子列表接口
     */
    public static final String URL_JUDGEORGANIZATIONCOUNT = "/organ/judgeOrganizationCount\n";

    /**
     * 我加入的圈子列表接口
     */
    public static final String URL_GETJOINORGANLIST = "/organ/getJoinOrganList";
    /**
     * 支付宝订单保存
     */
    public static final String URL_SAVEALIPAYORDER = "/pay/saveAlipayOrder";

    /**
     * 微信支付订单保存
     */
    public static final String URL_SAVEWEIXINORDER = "/pay/saveWeixinOrder";

    /**
     * 获取产品服务列表
     */
    public static final String URL_GETSERVICEINFO = "/common/getServiceInfo";
    /**
     * 审核入群申请接口
     */
    public static final String URL_AUDITEGROUPAPPLY = "/group/auditeGroupApply";

    /**
     * 审核入圈申请接口
     */
    public static final String URL_AUDITEAPPLY = "/organ/auditeApply";
    /**
     * 获取帖子详情接口
     */
    public static final String URL_FINDPOSTDETAIL = "/organ/findPostDetail";
    /**
     * 获取实名认证提示信息接口
     */
    public static final String URL_GETAUTHCONTENTINFO = "/auth/getAuthContentInfo";

    /**
     * 实名认证接口
     */
    public static final String URL_SAVEREALNAMEAUTH = "/auth/saveRealNameAuth";

    /**
     * 实名认证整合接口
     */
    public static final String URL_SAVEREALNAMEAUTHINTEGRATIO = "/auth/saveRealNameAuthIntegration";

    /**
     * 获取首页推荐圈子接口
     */
    public static final String URL_GETRECOMMENDORGANLIST = "/home/getRecommendOrganList";

    /**
     * 通过搜索查询二级类别接口
     */
    public static final String URL_GETSECONDTYPEBYSEARCHLIST = "/organ/getSecondTypeBySearchList";

    /**
     * 查询圈子下的帖子活动接口
     */
    public static final String URL_GETPOSTANDACTIVITYLIST = "/organ/getPostAndActivityList";

    /**
     * 发起PK挑战接口
     */
    public static final String URL_SAVELAUNCHPLAY = "/play/saveLaunchPlay";

    /**
     * 查询所有视频列表接口
     */
    public static final String URL_GETALLPLAYLIST = "/play/getAllPlayList";

    /**
     * 参与PK挑战接口
     */
    public static final String URL_SAVEPLAYINFO = "/play/savePlayInfo";

    /**
     * 查询挑战列表接口
     */
    public static final String URL_SGETPLAYLIST = "/play/getPlayList";

    /**
     * PK挑战点赞接口
     */
    public static final String URL_PLAYPRAISE = "/play/playPraise";

    /**
     * PK挑战删除接口
     */
    public static final String URL_DELETEPLAY = "/play/deletePlay";

    /**
     * 查询待处理申请列表接口
     */
    public static final String URL_GETAPPLYLIST = "/organ/getApplyList";

    /**
     * 获取用户token接口
     */
    public static final String URL_FINDTOKENBYUSERID = "/user/findTokenByUserId";

    /**
     * 赠送道具接口
     */
    public static final String URL_PRESENTEDPROPS = "/props/presentedProps";

    /**
     * 查询用户余额
     */
    public static final String URL_QUERYUSERBALANCE = "/user/queryUserBalance";

    /**
     * 解散圈子接口
     */
    public static final String URL_DISBANDORGANIZATION = "/organ/disbandOrganization";

    /**
     * 校验一对一视频接口
     */
    public static final String URL_CHECKONETOONEVIDEO = "/video/checkOneToOneVideo";

    /**
     * 获取用户一对一视频信息接口
     */
    public static final String URL_FINDUSERVIDEOINFO = "/video/findUserVideoInfo";

    /**
     * 更新用户一对一视频信息接口
     */
    public static final String URL_UPDATEUSERVIDEOINFO = "/video/updateUserVideoInfo";

    /**
     * 获取私信道具列表接口
     */
    public static final String URL_GETLETTERPROPSLIST = "/props/getLetterPropsList";

    /**
     * 私信赠送礼物接口
     */
    public static final String URL_LETTERPRESENTEDGIFTS = "/props/letterPresentedGifts";

    /**
     * 获取礼物赠送选择数列表接口
     */
    public static final String URL_GIFTNUMLIST = "/room/giftNumList";

    /**
     * 多人视频赠送礼物接口
     */
    public static final String URL_PRESENTEDGIFTS = "/props/presentedGifts";


    /**
     * 修改用户所在圈子是否公开接口
     */
    public static final String URL_UPDATEUSERORGANISPUBLIC = "/organ/updateUserOrganIsPublic";


    /**
     * 麦上嘉宾查看礼物数据接口
     */
    public static final String URL_GETWHEATPROPSDATA = "/room/getWheatPropsData";

    /**
     * 获取本场金币统计数据
     */
    public static final String URL_GETCURRENTGOLDSTATISTIC = "/room/getCurrentGoldStatistic";

    /**
     * 一对一视频接口
     */
    public static final String URL_ONETOONEVIDEO = "/video/oneToOneVideo";

    /**
     * 删除群组管理员接口
     */
    public static final String URL_DELETEGROUPMANAGER = "/group/deleteGroupManager";

    /**
     * 设置管理员接口
     */
    public static final String URL_SETMANAGER = "/group/setManager";

    /**
     * 查询首页更新条数接口  小红点
     */
    public static final String URL_GETHOMENEWCOUNT = "/home/getHomeNewCount";
    /**
     * 查询首页更新条数接口  小红点
     */
    public static final String URL_GETHOMENEWCOUNTLIST = "/home/getHomeNewCountList";

    /**
     * 分享次数记录接口
     */
    public static final String URL_SAVESHARENUMBER = "/share/saveShareNumber";

    /**
     * 我分享的内容(帖子)接口
     */
    public static final String URL_GETMYSHARELIST = "/share/getMyShareList";

    /**
     * 分享数据到关注用户接口
     */
    public static final String URL_SAVESHAREATTENTION = "/share/saveShareAttention";

    /**
     * 判断回复内容是否被删除接口
     */
    public static final String URL_CHECKISDELETECOMMENT = "/user/checkIsDeleteComment";

    /**
     * 获取PK挑战详情接口
     */
    public static final String URL_FINDPLAYDETAIL = "/play/findPlayDetail";

    /**
     * 查询圈子禁言成员列表接口
     */
    public static final String URL_GETBANNINGCOMMENTSLIST = "/organ/getBanningCommentsList";

    /**
     * 查询圈子被踢成员列表接口
     */
    public static final String URL_GETKICKEDOUTLIST = "/organ/getKickedOutList";


    /**
     * 删除圈子内容接口
     */
    public static final String URL_DELETEORGANCONTENT = "/organ/deleteOrganContent";


    /**
     * 获取用户在圈子的状态接口
     */
    public static final String URL_FINDUSERORGANSTATUS = "/organ/findUserOrganStatus";

    /**
     * 圈子用户禁言接口
     */
    public static final String URL_ORGANBANNINGCOMMENTS = "/organ/organBanningComments";

    /**
     * 修改用户在圈子状态接口
     */
    public static final String URL_UPDATEORGANUSERSTATUS = "/organ/updateOrganUserStatus";

    /**
     * 删除我的分享接口
     */
    public static final String URL_DELETEMYSHARE = "/share/deleteMyShare";

    /**
     * 评论回复包含图片视频接口
     */
    public static final String URL_COMMENTUPLOADIMAGE = "/image/commentUploadImage";


    /**
     * 查询附近的人列表接口
     */
    public static final String URL_GETNEARPERSONLIST = "/near/getNearPersonList";

    /**
     * 查询校友创建的二级兴趣标签
     */
    public static final String URL_SCHOOLFELLOWCREATE = "/user/schoolfellowCreate";

    /**
     * 查询发送邀请函的活动列表接口
     */
    public static final String URL_GETINVITATIONACTIONLIST = "/active/getInvitationActionList";

    /**
     * 发送附近人邀请函接口
     */
    public static final String URL_SENDNEARINVITATION = "/near/sendNearInvitation";

    /**
     * 发送活动邀请函接口
     */
    public static final String URL_SENDACTIVITYINVITATION = "/active/sendActivityInvitation";
    /**
     * 查询帮帮忙列表接口
     */
    public static final String URL_GETCLASSMATEHELPLIST = "/help/getClassmateHelpList";

    /**
     * 查询帮帮忙列表接口
     */
    public static final String URL_GETPUBLISHHELPLIST = "/help/getPublishHelpList";

    /**
     * 点赞帮帮忙接口
     */
    public static final String URL_HELPPRAISE = "/help/helpPraise";

    /**
     * 删除帮帮忙接口
     */
    public static final String URL_DELETEHELP = "/help/deleteHelp";

    /**
     * 删除帮帮忙评论
     */
    public static final String URL_DELETEHELPCOMMENT = "/help/deleteHelpComment";

    /**
     * 评论回复点赞接口
     */
    public static final String URL_COMMENTPRAISE = "/organ/commentPraise";

    /**
     * 查询回复列表接口
     */
    public static final String URL_GETREPLYLIST = "/organ/getReplyList";

    /**
     * 判断用户是否可以修改学校接口
     */
    public static final String URL_JUDGEUSERSCHOOL = "/user/judgeUserSchool";

    /**
     * 保存学校接口
     */
    public static final String URL_SAVESCHOOL = "/user/saveSchool";

    /**
     * 保存学校接口
     */
    public static final String URL_GETLOGINRECOMMENDLIST = "/home/getLoginRecommendList";

    /**
     * 查询校友圈子数据列表接口
     */
    public static final String URL_GETSCHOOLMATEORGANLIST = "/organ/getSchoolmateOrganList";

    /**
     * 发布同学帮帮忙接口
     */
    public static final String URL_SAVECLASSMATEHELP = "/help/saveClassmateHelp";

    /**
     * 查询校友圈子列表接口
     */
    public static final String URL_GETSCHOOLORGANIZATIONLIST = "/organ/getSchoolOrganizationList";

    /**
     * 点赞帮帮忙评论/回复接口
     */
    public static final String URL_PRAISEHELPCOMMENT = "/help/praiseHelpComment";

    /**
     * 查询帮帮忙评论/回复接口
     */
    public static final String URL_GETHELPCOMMENTLIST = "/help/getHelpCommentList";

    /**
     * 绑定手机号
     */
    public static final String URL_BINDPHONENUMBER = "/user/bindPhoneNumber";

    /**
     * 保存提现账户信息接口
     */
    public static final String URL_SAVECASHACCOUNTR = "/exchange/saveCashAccount";

    /**
     * 获取用户提现账户信息接口
     */
    public static final String URL_FINDCASHACCOUNT = "/exchange/findCashAccount";

    /**
     * 校园圈子发布内容开关接口
     */
    public static final String URL_SCHOOLORGANSWITCH = "/organ/schoolOrganSwitch";

    /**
     * 获取帮帮忙详情接口
     */
    public static final String URL_FINDHELPDETAIL = "/help/findHelpDetail";

    /**
     * 查询校友录接口
     */
    public static final String URL_GETALUMNIORGANIZATIONLIST = "/organ/getAlumniOrganizationList";

    /**
     * 获取跟拍视频详情接口
     */
    public static final String URL_FINDFOLLOWDETAIL = "/follow/findFollowDetail";

    /**
     * 查询条目展示跟拍视频列表接口
     */
    public static final String URL_GETITEMFOLLOWLIST = "/follow/getItemFollowList";

    /**
     * 查询条目展示跟拍视频列表接口
     */
    public static final String URL_GETALLFOLLOWLIST = "/follow/getAllFollowList";


    /**
     * 查询二级标签接口
     */
    public static final String URL_GETSECONDLABELLIST = "/room/getSecondLabelList";

    /**
     * 查询我的订单接口
     */
    public static final String URL_GETMYORDERLIST = "/trail/getMyOrderList";

    /**
     * 获取订单详情接口
     */
    public static final String URL_FINDORDERDETAIL = "/trail/findOrderDetail";

    /**
     * 确认收货接口
     */
    public static final String URL_UPDATEORDERSTATUS = "/trail/updateOrderStatus";

    /**
     * 确认收货接口
     */
    public static final String URL_GETTRAILREPORTLIST = "/trail/getTrailReportList";

    /**
     * 查询店铺列表接口
     */
    public static final String URL_GETSTORELIST = "/store/getStoreList";

    /**
     * 查询官方推荐的商家接口
     */
    public static final String URL_GETRECOMMENDMERCHANTLIST = "/store/getRecommendMerchantList";

    /**
     * 查询店铺标签接口
     */
    public static final String URL_GETSTORELABELLIST = "/store/getStoreLabelList";

    /**
     * 判断用户开店权限接口
     */
    public static final String URL_JUDGECREATESTOREAUTH = "/store/judgeCreateStoreAuth";

    /**
     * 申请开店接口
     */
    public static final String URL_APPLYCREATESTORE = "/store/applyCreateStore";

    /**
     * 创建/编辑店铺接口
     */
    public static final String URL_UPDATECREATESTORE = "/store/updateCreateStore";

    /**
     * 上传店铺视频接口
     */
    public static final String URL_UPDATESTOREVIDEO = "/store/updateStoreVideo";

    /**
     * 添加/编辑商品接口
     */
    public static final String URL_UPDATEPRODUCTINFO = "/store/updateProductInfo";

    /**
     * 获取店铺详情接口
     */
    public static final String URL_FINDSTOREDETAIL = "/store/findStoreDetail";

    /**
     * 查询店铺商品列表接口
     */
    public static final String URL_GETSTOREPRODUCTLIST = "/store/getStoreProductList";

    /**
     * 获取店铺商品详情接口
     */
    public static final String URL_FINDPRODUCTDETAIL = "/store/findProductDetail";

    /**
     * 查询店铺订单列表接口
     */
    public static final String URL_GETORDERLIST = "/store/getOrderList";

    /**
     * 查询销售记录列表接口
     */
    public static final String URL_GETSALESRECORDTLIST = "/store/getSalesRecordtList";

    /**
     * 查询订单评价列表接口
     */
    public static final String URL_GETORDEREVALUATELIST = "/store/getOrderEvaluateList";

    /**
     * 申请退款接口
     */
    public static final String URL_APPLYREFUND = "/store/applyRefund";

    /**
     * 查询退款原因列表接口
     */
    public static final String URL_GETREFUNDREASONLIST = "/store/getRefundReasonList";

    /**
     * 立即评价接口
     */
    public static final String URL_SAVEORDEREVALUATE = "/store/saveOrderEvaluate";

    /**
     * 申请店员接口
     */
    public static final String URL_APPLYCLERT = "/store/applyClert";

    /**
     * 获取店员申请信息接口
     */
    public static final String URL_FINDCLERTAPPLY = "/store/findClertApply";

    /**
     * 审核店员申请信息接口
     */
    public static final String URL_AUTHCLERTAPPLY = "/store/authClertApply";

    /**
     * 查询店员列表接口
     */
    public static final String URL_GETCLERTLIST = "/store/getClertList";

    /**
     * 更新商品状态接口
     */
    public static final String URL_UPDATEPRODUCTSTATUS = "/store/updateProductStatus";

    /**
     * 解雇店员接口
     */
    public static final String URL_FIRECLERK = "/store/fireClerk";

    /**
     * 获取店员详情接口
     */
    public static final String URL_FINDCLERTDETAIL = "/store/findClertDetail";

    /**
     * 查询用户默认地址接口
     */
    public static final String URL_FINDUSERDEFAULTADDRESS = "/store/findUserDefaultAddress";

    /**
     * 保存用户收货地址接口
     */
    public static final String URL_SAVEH5RECEIPTADDRESS = "/h5Trail/saveH5ReceiptAddress";

    /**
     * 删除用户收货地址接口
     */
    public static final String URL_DELH5RECEIPTADDRESS = "/h5Trail/delH5ReceiptAddress";

    /**
     * 编辑用户收货地址接口
     */
    public static final String URL_UPDATEH5RECEIPTADDRESS = "/h5Trail/updateH5ReceiptAddress";

    /**
     * 查询用户收货地址列表接口
     */
    public static final String URL_GETH5RECEIPTADDRESSLIST = "/h5Trail/getH5ReceiptAddressList";

    /**
     * 获取提现数据接口
     */
    public static final String URL_FINDEXCHANGEINFO = "/orderExchange/findExchangeInfo";

    /**
     * 立即提现接口
     */
    public static final String URL_SAVEEXCHANGEINFO = "/orderExchange/saveExchangeInfo";

    /**
     * 查询提现明细接口
     */
    public static final String URL_GETEXCHANGELIST = "/orderExchange/getExchangeList";

    /**
     * 商品支付宝订单保存
     */
    public static final String URL_SAVEALIPAYPRODUCTORDER = "/pay/saveAlipayProductOrder";

    /**
     * 商品支付宝订单保存
     */
    public static final String URL_SAVEWEIXINPRODUCTORDER = "/pay/saveWeixinProductOrder";


    /**
     * 查询首页商圈列表接口
     */
    public static final String URL_GETTRADAREALIST = "/home/getTradAreaList";
    /**
     * 卖家接单接口
     */
    public static final String URL_SELLERRECEIPT = "/store/sellerReceipt";

    /**
     * 确认退款接口
     */
    public static final String URL_CONFIRMREFUND = "/store/confirmRefund";

    /**
     * 发送匿名消息接口
     */
    public static final String URL_SENDANONYMOUSMSG = "/common/sendAnonymousMsg";

    /**
     * 获取匿名用户
     */
    public static final String URL_GETANONYMITYUSER = "/chat/getAnonymityUser";

    /**
     * 添加匿名好友接口
     */
    public static final String URL_SAVEANONYMITYUSER = "/friend/saveAnonymityUser";

    /**
     * 获取匿名好友列表
     */
    public static final String URL_GETANONYMITYFRIENDLIST = "/friend/getAnonymityFriendList";

    /**
     * 删除匿名好友
     */
    public static final String URL_DELETEANONYMITYUSER = "/friend/deleteAnonymityUser";

    /**
     * 修改匿名昵称接口
     */
    public static final String URL_UPDATEANONYMITYUSER = "/friend/updateAnonymityUser";

    /**
     * 开启/关闭聊天室接口
     */
    public static final String URL_UPDATECHATROOM = "/chat/updateChatRoom";
    /**
     * 进入/退出聊天室接口
     */
    public static final String URL_UPDATECHATROOMUSER = "/chat/updateChatRoomUser";
    /**
     * 查询聊天室列表接口
     */
    public static final String URL_GETCHATROOMLIST = "/chat/getChatRoomList";
    /**
     * 修改聊天室锁接口
     */
    public static final String URL_UPDATECHATROOMLOCK = "/chat/updateChatRoomLock";

    /**
     * 发工资接口
     */
    public static final String URL_SAVESHOPUSERPAYOFF = "/orderExchange/saveShopUserPayOff";

    /**
     * 查询用户申请状态
     */
    public static final String URL_FINDUSERAPPLYSTATUS = "/store/findUserApplyStatus";

    /**
     * 判断是否收货接口
     */
    public static final String URL_JUDGEUSERRECEIPT = "/trail/judgeUserReceipt";

    /**
     * 卖家拒绝/买家撤销退款接口
     */
    public static final String URL_REFUSEREFUND = "/store/refuseRefund";

    /**
     * 获取校园店标签接口
     */
    public static final String URL_GETSHOPLABELLIST = "/store/getShopLabelList";

    /**
     * 保存用户店铺标签接口
     */
    public static final String URL_SAVEUSERSTORELABEL = "/store/saveUserStoreLabel";

    /**
     * 查询标签对应帖子接口
     */
    public static final String URL_GETPOSTLISTBYLABELID = "/organ/getPostListByLabelId";

    /**
     * 查询帖子常驻标签接口
     */
    public static final String URL_GETCOMMONLABELINFO = "/organ/getCommonLabelInfo";

    /**
     * 判断用户是否开店接口
     */
    public static final String URL_JUDGEISCREATESTORE = "/store/judgeIsCreateStore";

    /**
     * 搜索帖子标签接口
     */
    public static final String URL_SEARCHLABELBYCONTENT = "/organ/searchLabelByContent";

    /**
     * 搜索店铺接口
     */
    public static final String URL_FINDSTOREKEYWORDLIST = "/home/findStoreKeywordList";

    /**
     * 搜索商品接口
     */
    public static final String URL_FINDPRODUCTINFOKEYWORDLIST = "/home/findProductInfoKeywordList";
    /**
     * 查询全部品牌商店铺列表接口
     */
    public static final String URL_GETBRANDSHOPINFOLIST = "/brand/getBrandShopInfoList";


}
