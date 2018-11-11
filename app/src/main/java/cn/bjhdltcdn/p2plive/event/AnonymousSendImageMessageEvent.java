package cn.bjhdltcdn.p2plive.event;

import io.rong.imlib.model.MessageContent;

/**
 * Description:发送图片消息event
 * Data: 2018/6/7
 *
 * @author: zhudi
 */
public class AnonymousSendImageMessageEvent {
    private MessageContent messageContent;

    public AnonymousSendImageMessageEvent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }
}
