package cn.bjhdltcdn.p2plive.httpresponse;

import cn.bjhdltcdn.p2plive.model.ChatBaseUser;

public class GetAnonymityUserResponse extends BaseResponse {
    private ChatBaseUser chatBaseUser;
    private ChatBaseUser toChatBaseUser;

    public ChatBaseUser getChatBaseUser() {
        return chatBaseUser;
    }

    public void setChatBaseUser(ChatBaseUser chatBaseUser) {
        this.chatBaseUser = chatBaseUser;
    }

    public ChatBaseUser getToChatBaseUser() {
        return toChatBaseUser;
    }

    public void setToChatBaseUser(ChatBaseUser toChatBaseUser) {
        this.toChatBaseUser = toChatBaseUser;
    }
}
