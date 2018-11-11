package cn.bjhdltcdn.p2plive.ui.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.event.VideoGroupGetUserInfoEvent;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.model.VideoCMDTextMessageModel;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;


/**
 * 视频的聊天
 */
public class VideoGroupMsgAdapter extends BaseRecyclerAdapter {

    private List<VideoCMDTextMessageModel> list;
    private String nickName;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof VideoGroupMsgHolder) {
            try {
                final VideoGroupMsgHolder videoGroupMsgHolder = (VideoGroupMsgHolder) holder;
                final VideoCMDTextMessageModel videoCMDTextMessage = list.get(position);
                //        case 60001://聊天消息
//        case 60006://刷新麦序
//        case 60007://关闭房间
//        case 60008://移交主持人刷新
//        case 60009://禁言
//        case 60010://送礼物
//        case 60011://投票
//        case 60012://关注
//        case 60013://温馨提示
//        case 60014://进入房间
                // 08开启pk
                if (videoCMDTextMessage.getMessageType() == 60013
//                        || videoCMDTextMessage.getFlag() == 0x08
                        ) {
                    videoGroupMsgHolder.tv_msg_system.setVisibility(View.VISIBLE);
                    videoGroupMsgHolder.tv_msg.setVisibility(View.GONE);
                    String msg = videoCMDTextMessage.getMessageTips();
                    videoGroupMsgHolder.tv_msg_system.setText(msg);
                    ForegroundColorSpan foregroundColorSpan = null;
                    if (videoCMDTextMessage.getMessageType() == 60013) {
                        foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_c68dff));
                    }
//                    else if (videoCMDTextMessage.getFlag() == 0x08) {
//                        foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffda44));
//                    }
                    SpannableStringBuilder builder = new SpannableStringBuilder();
                    builder.setSpan(foregroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    videoGroupMsgHolder.tv_msg_system.setVisibility(View.GONE);
                    final BaseUser messageUser = videoCMDTextMessage.getBaseUser();
                    String propsUrl = "";
                    if (messageUser != null) {
                        final SpannableStringBuilder builder = new SpannableStringBuilder();
                        nickName = messageUser.getNickName();
                        if (TextUtils.isEmpty(nickName)) {
                            nickName = "其他用户";
                        }
                        if (!TextUtils.isEmpty(nickName)) {
                            final int flag = videoCMDTextMessage.getMessageType();
                            switch (flag) {
                                case 60001:
                                    String itemTextMessage = videoCMDTextMessage.getMessageTips();
                                    builder.append(nickName + ": " + itemTextMessage);

                                    break;
                                case 60014:
                                    builder.append(nickName + "来捧场了！");
                                    break;
                                case 60012:
                                case 60011:
                                case 60009:
                                    BaseUser toUserInfo = videoCMDTextMessage.getToUserInfo();
                                    if (toUserInfo != null && !TextUtils.isEmpty(toUserInfo.getNickName())) {
                                        if (flag == 60012) {
                                            builder.append(nickName + "：我关注了" + toUserInfo.getNickName());
                                        } else if (flag == 60011) {
//                                            builder.append(nickName + ": 我支持了" + toUserInfo.getWheatId() + "号麦");
                                            builder.append(nickName + ": " + "我支持了" + toUserInfo.getNickName());
                                        } else if (flag == 60009) {
                                            nickName = toUserInfo.getNickName();
                                            if (videoCMDTextMessage.getType() == 2) {
                                                builder.append(nickName + "被管理员禁言");
                                            } else {
                                                builder.append(nickName + "被管理员取消禁言");
                                            }
                                        }
                                    }
                                    break;
                                case 60010:
                                    BaseUser giftToUserInfo = videoCMDTextMessage.getToUserInfo();
                                    Props props = videoCMDTextMessage.getProps();
//                                    List<Props> list = DBUtils.getInstance().getPropsById(videoCMDTextMessage.getPropsId());
//                                    if (list != null && list.size() > 0) {
//                                        props = list.get(0);
//                                    }

                                    propsUrl = props != null ? props.getPropUrl() : "";
                                    if (giftToUserInfo != null && !TextUtils.isEmpty(giftToUserInfo.getNickName())) {

                                        StringBuffer buffer = new StringBuffer();
                                        buffer.append("<font color=\"#ffffff\" >" + nickName + "</font>");
                                        buffer.append("<font color=\"#ffda44\" >" + "送给了" + "</font>");

//                                        if (props.getType() == 12) {//金币红包
//                                            buffer.append("<font color=\"#ffda44\" >" + (giftToUserInfo.getNickName() + "一个价值" + videoCMDTextMessage.getGoldNum() + "金币的" + props.getName()) + "</font>");
//                                        } else {
//                                        buffer.append("<font color=\"#ffda44\" >" + (giftToUserInfo.getNickName() + props.getGiftMultiple() + "个" + props.getPropsName()) + "</font>");
//                                        }
                                        buffer.append("<font color=\"#ffda44\" >" + (giftToUserInfo.getNickName() + props.getPropsName()) + "</font>");

                                        buffer.append("<img src=\"" + propsUrl + "\"/>");
//                                        if (props.getType() != 12) {//非金币红包有数量
                                        buffer.append("<font color=\"#ffda44\" >" + "X" + props.getGiftMultiple() + "</font>");
//                                        }
                                        builder.append(buffer);

                                    }
                                    break;

                            }
                            ForegroundColorSpan foregroundColorSpan;
                            if (flag == 60001 || flag == 60012 || flag == 60011) {
                                foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_e4e4e4));
                                builder.setSpan(foregroundColorSpan, 0, nickName.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (flag == 60014) {
                                foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_fff0b2));
                                builder.setSpan(foregroundColorSpan, 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            } else if (flag == 60010 || flag == 60009) {
                                foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.white));
                                builder.setSpan(foregroundColorSpan, 0, nickName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                            switch (flag) {
                                case 60001:
                                    foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.white));
                                    builder.setSpan(foregroundColorSpan, nickName.length() + 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                case 60012:
                                case 60011:
                                    foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffda44));
                                    builder.setSpan(foregroundColorSpan, nickName.length() + 1, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                case 60010:
                                    foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ffda44));
                                    builder.setSpan(foregroundColorSpan, nickName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                                case 60009:
                                    foregroundColorSpan = new ForegroundColorSpan(App.getInstance().getResources().getColor(R.color.color_ff0000));
                                    builder.setSpan(foregroundColorSpan, nickName.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    break;
                            }
                            videoGroupMsgHolder.tv_msg.setVisibility(View.VISIBLE);

                            switch (flag) {
                                case 60010:
                                    RequestOptions options = new RequestOptions().centerCrop().error(R.mipmap.ic_launcher);
                                    Glide.with(App.getInstance()).load(propsUrl).apply(options).into(new SimpleTarget<Drawable>() {
                                        @Override
                                        public void onResourceReady(final Drawable resource, Transition<? super Drawable> transition) {
                                            videoGroupMsgHolder.tv_msg.setText(Html.fromHtml(builder.toString(), new Html.ImageGetter() {
                                                @Override
                                                public Drawable getDrawable(String source) {
                                                    Drawable drawable = resource; //显示本地图片
                                                    if (drawable != null) {
//                                                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                                                        drawable.setBounds(0, 0, Utils.dp2px(18), Utils.dp2px(18));
                                                    }
                                                    return drawable;
                                                }
                                            }, null));
                                        }
                                    });
                                    break;

                                default:
                                    videoGroupMsgHolder.tv_msg.setText(builder);
                                    break;
                            }

                            if (SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) != messageUser.getUserId()) {
                                videoGroupMsgHolder.tv_msg.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        BaseUser baseUser = null;
                                        if (flag == 60009) {
                                            baseUser = videoCMDTextMessage.getToUserInfo();
                                        } else {
                                            baseUser = messageUser;
                                        }
                                        EventBus.getDefault().post(new VideoGroupGetUserInfoEvent(baseUser));
                                    }
                                });
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = View.inflate(App.getInstance(), R.layout.item_list_video_group_msg, null);
        return new VideoGroupMsgHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }


    public void clearData() {
        if (getItemCount() > 0) {
            list.clear();
            list = null;
        }
    }

    /**
     * 新增一条数据
     *
     * @param textMessageModel
     */
    public void addItemData(VideoCMDTextMessageModel textMessageModel) {
        if (textMessageModel == null) {
            return;
        }

        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(textMessageModel);
        if (list.size() > 100) {
            list.remove(0);
        }
        notifyDataSetChanged();

    }


    class VideoGroupMsgHolder extends BaseViewHolder {

        private TextView tv_msg, tv_msg_system;

        public VideoGroupMsgHolder(View itemView) {
            super(itemView);
            tv_msg = (TextView) itemView.findViewById(R.id.tv_msg);
            tv_msg_system = (TextView) itemView.findViewById(R.id.tv_msg_system);
        }
    }
}
