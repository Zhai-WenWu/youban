package cn.bjhdltcdn.p2plive.httpresponse;

import java.util.List;

import cn.bjhdltcdn.p2plive.model.ChatInfo;

/**
 * Description:聊天室列表解析对象
 * Data: 2018/6/9
 *
 * @author: zhudi
 */
public class GetChatRoomListResponse extends BaseResponse {
    /**
     * :[{ChatInfo对象},{ChatInfo对象},...]
     */
    private List<ChatInfo> chatList;

    /**
     * 用户创建的普通聊天室
     */
    private ChatInfo chatInfo;
    /**
     * 总数,
     */
    private int total;

    public List<ChatInfo> getChatList() {
        return chatList;
    }

    public void setChatList(List<ChatInfo> chatList) {
        this.chatList = chatList;
    }

    public ChatInfo getChatInfo() {
        return chatInfo;
    }

    public void setChatInfo(ChatInfo chatInfo) {
        this.chatInfo = chatInfo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
