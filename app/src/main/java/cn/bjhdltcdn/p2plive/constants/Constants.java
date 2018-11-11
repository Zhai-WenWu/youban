package cn.bjhdltcdn.p2plive.constants;

import cn.bjhdltcdn.p2plive.model.ChatInfo;

public interface Constants {

    class KEY {

        /***********************平台信息***********************************************/
        public static final String KEY_APP_PLATFORM_INFO = "platformInfo";

        // token验证串
        public static final String KEY_SYS_TOKEN = "sysToken";

        // SHA1 的key
        public static final String KEY_SHA1 = "SHA1";

        // tab
        public static final String KEY_TAB_INIT = "key_tab_init";


        public static final String ACTION_SINGLE_POINT = "key_tab_init";


        /**
         * 融云测试环境的key
         */
        public static final String KEY_RONG_CLOUD_TEST = "mgb7ka1nmwrcg";
//        public static final String KEY_RONG_CLOUD_TEST = "8w7jv4qb8crpy";
        /**
         * 融云正式环境的key
         */
        public static final String KEY_RONG_CLOUD_OFFICIAL = "8w7jv4qb8crpy";

        /**********************阿里热修复配置****************************************/
        public static final String KEY_ALI_APP_ID = "24759303-1";
        public static final String KEY_ALI_APP_SECRET = "0804270bc469644873a918fecc9afe6f";
        public static final String KEY_ALI_APP_RSA = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoXD1sO4IKcynaYFO6RouPMeF0dvfoaG9T8H3ShY1G89b85X5hDG95LtHDxPPhg8lL9lAVrKhmsK00o92iZ48OXDj8k+2RlKG9ceHhnmOO2VYPCv3WK3v1EEHcsRoGe/aD1IjVG/MfMiPkdhAALRFclzEF622PTuXEr566f4PCpTKpI1AJBza/1vAvPGBRfsWaSAU6GrcUFtam7pU7viB7mfkO+bFmZ2NSo1mShLbcX0C4EOqBOMnH/gDOFShDLr1COjXUmAEAqRHzq4AGVlJozZpSX8kF2PWN5CNUFmkVqLDFeilT7/sTUd3iL2fcArFtmcj8EHvIG69leeo1584pAgMBAAECggEBAKSYyVQvOYuN27Urx6+tmHN+iLScOvJ0Z6Lg06Il/2EGs4C7vTqUNcd8Bogm3Wf0t7AUN2d5TYpX4H4+VkiYhju73drRpMlXTmkwFW3Gs/7MbF54mIHyMVX9duaUHqWIPBHWj5AJqz8sffq1kh8Z8IddKQhoisw4jlokeEJMuDxqOs100rcC/jYP6UedJCJJbcDflvWH84e3WcEvI3ZkYEMH/JAvWhPoBktI/n+qllUz27CkxXPrziDKG6MAdKaOYgcrs/Y/a4yujz3TTkdxaTXMG4draMaFUos8afrCdtAGgL4AY2f1YMzopu5/1ZQmpV20V+l6oIfyxr+DOOXfH6ECgYEA8gIZEM1m9YTOZCf788TqjsMtrJt4lKJ8obvMO6S7kQgyZvbM60mfQTmu8sll2TVJekPXffvl+vs3FF/Utd0wgtJIWmU6goeAaCVbs6LwIoFHT64a7kLNsSPKWi4Ybdban9aslOIlVxYvYyHngipptPx4qbS7oD9lON8d62jKu1cCgYEAshgZLGw4clrotqNm/Vk49OvTg7f8NHkJ2eor89uzjPXAwIFqtg2vtDEfUnQwEgrmZqPI8zbpIGLAWm40FAM2SnYG6LE6xh7JvxORTusepiR9n79xUNRP6/e80D9q8Mh8G3aEkYCep6//6Tji17Wj4TkfHjNYA5Meq8SNsqBlUn8CgYATygYgsJsdsnlqCTb6DP0dJWqjtabbiJnmY8PkkKjhyCjO8Jl7F+8U11Gt+rgAVfInNEr3u1Rn/IjZeqibInYoDGfsNGDfcZMXcQ6ZltZKDAY3xsXe+8l0FbgjPaezu6Du9w/vsLpa7656TbhvlWZFbWu0fNnm1ahCSURaDpydSwKBgFZ45vgvz7SOuYG8k0weH06klr3U/qtHveXGEvND0Ml3pdCSxgqBWLfIRioV6iVIj5h0nCSjm8wyxqzh5310Fc9PwYkITL5XwqN5T8ue0/Hds/V10gJqJCgx0MbYlAc3gTMgd7viVKadCUfnbBVKx7iasKZMTcbmjzTdqeN6h9fnAoGBAIlox3neUAGou/tq9w3X9PKndufkdAXXUUL9dAlmGJsD9F5RvTzTHBxlViMEgd1Uc4zVlEXQAWwzi7kuA7vBQBHqxknLlyeg/NQAV736uLEvta8C5dorT0CJnTWVx1UwM1K7n+h35cEc55IM8ThEwnt2nPaaw1P2FpZpyRWMcgOv";

        /**
         * 小米推送
         */
        public static final String KEY_MIPUSH_APP_ID = "2882303761517653791";
        public static final String KEY_MIPUSH_APP_KEY = "5771765377791";

        public static final String KEY_URL = "url";
        public static final String KEY_TYPE = "type";
        public static final String KEY_PARENT_ID = "parentId";
        public static final String KEY_DEFAULT_IMG = "defaultImg";
        public static final String KEY_POSITION = "position";
        public static final String KEY_OBJECT = "object";
        public static final String ISLABELSELECTED = "isLabelSelected";
        public static final String KEY_EXTRA_PARAM = "extraParam";
        public static final String KEY_EXTRA = "extra";
        public static final String KEY_ID = "id";
        public static final String KEY_ACTIVITY_ID = "activityId";
        public static final String KEY_SAYLOVE_ID = "sayloveId";
        public static final String KEY_SIGNATURE = "signature";
        // 每天热更新次数
        public static final String KEY_SOPHIX_MANAGER_COUNT = "sophix_manager_count";
        // 每次热更新时间
        public static final String KEY_SOPHIX_MANAGER_TIME = "sophix_manager_time";


        /**
         * 启动单张上传图片
         */
        public static final String KEY_START_SINGLE_TASK_IMAGEUPLOAD = "cn.bjhdltcdn.p2plive.StartSingleTaskImageUploadIntentService";

        /**
         * 启动短视频上传图片
         */
        public static final String KEY_START_SINGLE_TASK_SHORT_VIDEO_UPLOAD = "cn.bjhdltcdn.p2plive.StartSingleTaskShortVideoUploadIntentService";
        /**
         * 声网key
         */
        public static final String AGORA_APP_KEY = "fce9bf93c371439196b660b27b6e105f";

        public static final String TUSDK_SECRET_KEY = "b3a2af25586246b2-03-7hpvp1";
        public static final String BUG_KEY = "21b58c6d650695e7b05990324ea5593b";
        public static final String UPAIYUN_HOST = "http://upyun.bjhdlt.cn";
        public static final String OPERATOR_NAME = "bjhdlt";
        public static final String OPERATOR_PWD = "hdlt2015";
        // 命名空间
        public static final String BUCKET_NAME = "video-hdlt-com";

        public static final String KEY_WXPAY = "wx35067d49b102e7a3";

        public static final String KEY_USER_CERTIFICATE = "userCertificate";

    }

    class Action {
        public static final String ACTION_BROWSE = "cn.bjhdltcdn.p2plive.action.browse";
        public static final String ACTION_EXTRA = "cn.bjhdltcdn.p2plive.service.action.Extra";
        //记录一对一通话时长
        public static final String ACTION_CALL_TIME_RECORD = "cn.bjhdltcdn.p2plive.service.action.callTimeRecord";
        public static final String ACTION_POSITION_EXTRA = "cn.bjhdltcdn.p2plive.service.action.position.Extra";
        // 显示一对一礼物特效
        public static final String ACTION_SHOW_ONE_PROPS_ANIMATION = "cn.bjhdltcdn.app.service.action.showOnePropsAnimation";
        // 显示礼物特效
        public static final String ACTION_SHOW_MORE_PROPS_ANIMATION = "cn.bjhdltcdn.app.service.action.showMorePropsAnimation";

        // 图片上传
        public static final String ACTION_UPLOAD_IMG = "cn.bjhdltcdn.app.service.action.uploadImg";
    }

    class Message {
        /**
         * 小视频播放页面
         */
        public static final int MESSAGE_SHOW_PROGRESS = 1;
        public static final int MESSAGE_SHOW_SUPERNATANT = 2;
        public static final int MESSAGE_HIDE_SUPERNATANT = 3;
    }


    // 字段
    class Fields {

        public static final String PHONE_NUMBER = "phoneNumber";
        public static final String PASS_WORD = "password";
        public static final String VERIFICATION_CODE = "verificationCode";

        public static final String CODE = "code";
        public static final String USER_ICON = "usericon";
        public static final String USER_ICON_2 = "userIcon";
        public static final String MSG = "msg";

        public static final String USER = "user";
        public static final String USER_ID = "userId";
        public static final String CHAT_ID = "chatId";
        public static final String CHAT_NAME = "chatName";
        public static final String CHAT_INFO = "chatInfo";
        public static final String SHARE_ID = "shareId";
        public static final String Ta_USER_ID = "userId";
        public static final String SAY_LOVE_ID = "sayLoveId";
        public static final String OLDPWD = "oldPwd";
        public static final String NEWPWD = "newPwd";
        public static final String TYPE = "type";
        public static final String PK_DETAIL = "type";
        public static final String PK_ID = "pkId";
        public static final String HELP_ID = "helpId";
        public static final String LAUNCH_PLAY = "launchPlay";
        public static final String PHOTOLIST = "photolist";
        public static final String ISUPGRADE = "isUpgrade";
        public static final String VERSIONLINKURL = "linkUrl";
        public static final String ORGAN_ID = "organId";
        public static final String CLUB_TYPE = "clubtype";
        public static final String ORGAN = "organ";
        public static final String HOBBY_ID = "hobbyId";
        public static final String HOME_INFO = "homeInfo";
        public static final String SECOND_HOBBY_ID = "secondHobbyId";
        public static final String SECOND_HOBBY_ID_LIST = "secondHobbyIdList";
        public static final String PAGE_SIZE = "pageSize";
        public static final String PAGE_NUMBER = "pageNumber";
        public static final String ORGAN_NAME = "organName";
        public static final String MODULE_TYPE = "moduleType";
        public static final String MODULE = "module";
        public static final String ORGAN_IMG = "organImg";
        public static final String CUSTOMNAME = "customName";
        public static final String CUSTOM_LIST = "customList";
        public static final String SEX_LIMIT = "sexLimit";
        public static final String JOIN_LIMIT = "joinLimit";
        public static final String CONTENT_LIMIT = "contentLimit";
        public static final String ANONYMOUS_LIMIT = "anonymousLimit";
        public static final String DESCRIPTION = "description";
        public static final String ISCREATECHATINFO = "isCreateChatInfo";
        public static final String ISCLOSECHATINFO = "isCloseChatInfo";
        public static final String PARENT_ID = "parentId";
        public static final String VIDEO_PLAY_ID = "videoPlayId";
        public static final String PHOTOGRAPH_TIME = "photoGraphTime";
        public static final String FILE = "file";
        public static final String ORDERNUM = "orderNum";
        public static final String TOTAL = "total";
        public static final String FLAG = "flag";
        public static final String LABELID = "labelId";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String PROVINCE = "province";
        public static final String CITY = "city";
        public static final String DISTRICT = "district";
        public static final String ADD = "add";
        public static final String ADDRESS = "addr";
        public static final String CONTENT = "content";
        public static final String CONTENT_TYPE = "contentType";
        public static final String IS_NEAR = "isNear";
        public static final String IS_MATCH = "isMatch";
        public static final String IS_ANONYMOUS = "isAnonymous";
        public static final String IS_ANONYMOUS_PRIVATE_TOAST_SHOW = "isAnonymousPrivateToastShow";
        public static final String IS_ANONYMOUS_CHATROOM_TOAST_SHOW = "isAnonymousChatRoomToastShow";
        public static final String IS_LOCK = "isLock";
        public static final String IS_GAG = "isGag";
        public static final String TOPIC_TYPE = "topicType";
        public static final String HELPTYPE = "helpType";
        public static final String VIDEO_URL = "videoUrl";
        public static final String VIDEO_IMAGE_URL = "videoImageUrl";
        public static final String LOCATIONINFO = "locationInfo";
        public static final String TO_USER_ID = "toUserId";
        public static final String FROM_USER_ID = "fromUserId";
        public static final String POST_ID = "postId";
        public static final String POST_LABEL_LIST = "postLabelList";
        public static final String POST_LABEL_ID = "postLabelId";
        public static final String SORT = "sort";
        public static final String GROUP = "group";
        public static final String GROUP_ID = "groupId";
        public static final String MANAGER_ID = "managerId";
        public static final String MANAGER_IDS = "managerIds";
        public static final String GROUP_IMG = "groupImg";
        public static final String ANONYMOUS_TYPE = "anonymousType";
        public static final String COMMENT_ID = "commentId";
        public static final String COMMENT_PARENT_ID = "commentParentId";
        public static final String COMMENT_TYPE = "commentType";
        public static final String PK_POSITION = "pkposition";
        public static final String ID = "id";
        public static final String MODULE_ID = "moduleId";
        public static final String REPORT_TYPE_ID = "reportTypeId";
        public static final String MEMBER_ID = "memberId";
        public static final String RELATION_ID = "relationId";
        public static final String GROUP_NAME = "groupName";
        public static final String GROUP_MODE = "groupMode";
        public static final String OUT_USER_IDS = "outUserIds";
        public static final String IS_DISTURB_MODE = "isDisturbMode";
        public static final String MESSAGE_TYPE = "messageType";
        public static final String APPLY_ID = "applyId";
        public static final String LOGIN_TYPE = "loginType";
        public static final String AUTH_LIMIT = "authLimit";
        public static final String TITLE = "title";
        public static final String LAUNCHID = "launchid";
        public static final String COME_IN_TYPE = "comeInType";
        public static final String BLACK_ID = "blackId";
        public static final String VERSION = "version";
        public static final String PLATFORM = "platform";
        public static final String HOBBYLISTNEEDREQ = "hobbyListNeedReq";
        public static final String THIRD_ID = "thirdId";
        public static final String CATEGORY = "category";
        public static final String PLAY_ID = "playId";
        public static final String ANSWERUSER_ID = "answerUserId";
        public static final String STATUS = "status";
        public static final String IS_PUBLIC = "isPublic";
        public static final String ROOM_ID = "roomId";
        public static final String VOTE_ID = "voteId";
        public static final String PHOTO_ALBUM = "photoalbum";
        public static final String NUMBER = "number";
        public static final String IS_EXIST_GROUP = "isExistGroup";
        public static final String PROPS_ID = "propsId";
        public static final String PROPS_NUM = "propsNum";
        public static final String PRESENTED_TYPE = "presentedType";
        public static final String GOLD_NUM = "goldNum";
        public static final String NICK_NAME = "nickName";
        public static final String IS_FIRST = "isFirst";
        public static final String SEX = "sex";
        public static final String BIRTHDAY = "birthday";
        public static final String OCCUPATION_INFO = "occupationInfo";
        public static final String SCHOOL_INFO = "schoolInfo";
        public static final String ORGAN_ID_LIST = "organIdList";
        public static final String LABEL_ID = "labelId";
        public static final String LABEL_IDS = "labelIds";
        public static final String LABEL_LIST = "labelList";
        public static final String LABEL_ID_LIST = "labelIdList";
        public static final String SECOND_LABEL_IDS = "secondLabelIds";
        public static final String ORDER_ID = "orderId";
        public static final String REASONI_D = "reasonId";
        public static final String RECEIPT_CODE = "receiptCode";
        public static final String EVALSCORE = "evalScore";
        public static final String IS_CLERT = "isClert";
        public static final String IS_OPEN = "isOpen";
        public static final String IS_RECURIT = "isRecurit";
        public static final String IDCARDPHOTOURL = "idCardPhotoUrl";
        public static final String ORDERINFO = "orderinfo";
        public static final String REMARK = "remark";
        public static final String SELFDESC = "selfDesc";
        public static final String CARD_FRONT_IMG = "cardFrontImg";
        public static final String CARD_BACK_IMG = "cardBackImg";
        //消息提示开关
        //声音
        public static final String MSG_VOICE = "msgVoice";
        //私信
        public static final String MSG_PRIVATE = "msgPrivate";
        //群
        public static final String MSG_GROUP = "msgGroup";
        //通知
        public static final String MSG_NOTIFY = "msgNotify";
        //评论
        public static final String MSG_COMMENT = "msgComment";
        //入群申请、加入圈子申请
        public static final String MSG_JOIN = "msgJoin";

        //视频
        public static final String VIDEO_STATUS = "videoStatus";
        public static final String BASEUSER = "baseUser";
        public static final String ROOMNUMBER = "roomNumber";
        public static final String VIDEO_TYPE = "videoType";
        public static final String CALL_HISTORY = "callHistory";
        public static final String EXTRA = "extra";
        public static final String POSITION = "position";
        public static final String TIME_TYPE = "timeType";
        public static final String ROOMINFO = "roomInfo";
        public static final String IMAGEPATH = "imagePath";
        public static final String USERROLE = "userRole";
        public static final String VIDEO_PATH = "videoPath";
        public static final String VIDEO_IMGURL = "videoImgUrl";
        public static String ROOM_BG_URL = "roomBgUrl";
        public static final String PROPS = "props";
        public static final String RECEVICE_BASEUSER = "receviceBaseUser";
        public static final String SEND_BASEUSER = "sendBaseUser";
        public static final String IMG_WIDTH = "imgWidth";
        public static final String IMG_HEIGHT = "imgHeight";
        public static final String CONTENT_STR = "contentStr";
        public static final String ORGANIZATIONINFO = "organizationInfo";
        public static final String LAUNCH_ID = "launchId";
        public static final String TITLE_STR = "titleStr";
        public static final String PLAY_TITLE_STR = "playTitleStr";
        public static final String CALL_TYPE = "callType";
        public static final String CHANNEL_NAME = "channelName";
        public static final String SHARED_OBJECT = "sharedObject";
        public static final String IMAGE_LIST = "imageList";
        public static final String IMAGE_ID = "imageId";
        public static final String IMAGE_URL = "imageUrl";
        public static final String HOBBY_IDS = "hobbyIds";
        public static final String SEND_NUMBER = "sendNumber";
        public static final String ACTIVITY_ID = "activityId";
        public static final String ACTIVITY = "activity";
        public static final String SELECT_ALUMNUS_PERSON = "selectAlumnusPerson";
        public static final String SELECT_CITY_PERSON = "selectCityPerson";
        public static final String METHOD = "method";
        public static final String USER_IDS = "userIds";
        public static final String ENTER_TYPE_MODE = "enterTypeMode";
        public static final String SCHOOL_ID = "schoolId";
        public static final String SCHOOL_NAME = "schoolName";
        public static final String SCHOOL_LIMIT = "schoolLimit";
        public static final String ACCOUNT = "account";
        public static final String AUTHSTATUS = "authstatus";
        public static final String FOLLOWING_SHOT_ENTER_TYPE = "followingShotEnterType";
        public static final String ORGAN_LIST = "organList";
        public static final String MESSAGE_TIPS = "messageTips";
        public static final String PRODUCT_ORDER = "productOrder";
        public static final String DISTANCE_SORT = "distanceSort";
        public static final String MERCHANT_SORT = "merchantSort";
        public static final String TYPE_SORT = "labelId";
        public static final String STOREINFO = "storeInfo";
        public static final String PRODUCT_ID = "productId";
        public static final String PRODUCT_NAME = "productName";
        public static final String PRODUCT_DESC = "productDesc";
        public static final String PRODUCT_PRICE = "productPrice";
        public static final String PRODUCT_DISCOUNT = "productDiscount";
        public static final String SALE_PRICE = "salePrice";
        public static final String PRODUCT_REMAIN_TOTAL = "productRemainTotal";
        public static final String STORE_ID = "storeId";
        public static final String IS_HOT = "isHot";
        public static final String IS_NEW = "isNew";
        public static final String ADDRESSINFO = "addressInfo";
        public static final String ADDRESS_ID = "addressId";
        public static final String HINT_TYPE = "hintType";
        public static final String EXCHANGE_AMOUNT = "exchangeAmount";
        public static final String AMOUNT = "amount";
        public static final String FIRST_LABELINFO = "firstLabelInfo";
        public static final String IS_CREATE_STORE = "isCreateStore";
    }

    class Constant {
        public static final int HOME_TYPE_POST = 1;
        public static final int HOME_TYPE_SAYLOVE = 8;
        public static final int HOME_TYPE_HELPINFO = 9;
    }

    /**
     * 需要全局保存的对象
     */
    class Object {
        public static ChatInfo CHATINFO;
    }

}
