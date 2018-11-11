package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ChatInfo;

/**
 * Created by ZHUDI on 2017/11/17.
 */

public class UpdateChatRoomResponse extends BaseResponse {
    //{chatInfo},
    private ChatInfo chatInfo;

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }
}
