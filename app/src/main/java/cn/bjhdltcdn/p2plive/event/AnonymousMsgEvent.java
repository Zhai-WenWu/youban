package cn.bjhdltcdn.p2plive.event;

import cn.bjhdltcdn.p2plive.model.ChatBaseUser;

/**
 * 融云匿名消息
 */
public class AnonymousMsgEvent {

    /**
     * 发送和接收匿名用户的对象
     */
    private ChatBaseUser chatBaseUser, toChatBaseUser;

    public AnonymousMsgEvent(ChatBaseUser chatBaseUser, ChatBaseUser toChatBaseUser) {
        this.chatBaseUser = chatBaseUser;
        this.toChatBaseUser = toChatBaseUser;
    }

    public ChatBaseUser getChatBaseUser() {
        return chatBaseUser;
    }

    public ChatBaseUser getToChatBaseUser() {
        return toChatBaseUser;
    }
}
