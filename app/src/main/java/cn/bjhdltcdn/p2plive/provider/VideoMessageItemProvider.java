package cn.bjhdltcdn.p2plive.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.ui.activity.VideoPlayFullScreenActivity;
import io.rong.imkit.YouBanShortVideoMessage;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

/**
 * Created by Hu_PC on 2017/8/24.
 */
@ProviderTag(messageContent = YouBanShortVideoMessage.class)
public class VideoMessageItemProvider extends IContainerItemProvider.MessageProvider<YouBanShortVideoMessage> {
     private Context context;
    class ViewHolder {
        AsyncImageView img;
        TextView durationView;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        this.context=context;
        View view = LayoutInflater.from(context).inflate(R.layout.rc_video_item_message, null);
        ViewHolder holder = new ViewHolder();
        holder.img = (AsyncImageView) view.findViewById(R.id.video_bg_img);
        holder.durationView= (TextView) view.findViewById(R.id.duration_view);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, int i, YouBanShortVideoMessage videoMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_right);
        } else {
            view.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_no_left);
        }
        holder.img.setResource(videoMessage.getmThumbUri());

        long duration=videoMessage.getDuration()/1000;
        String durationStr=String.format("%02d:%02d", (duration % 3600) / 60, (duration% 60));
        holder.durationView.setText(durationStr);
    }

    @Override
    public Spannable getContentSummary(YouBanShortVideoMessage data) {
        return new SpannableString("[视频]");
    }

    @Override
    public void onItemClick(View view, int i, final YouBanShortVideoMessage videoMessage, UIMessage uiMessage) {

        Intent intent=new Intent(context, VideoPlayFullScreenActivity.class);
        intent.putExtra(Constants.Fields.VIDEO_PATH,videoMessage.getMediaUrl().toString());
        intent.putExtra(Constants.Fields.VIDEO_IMAGE_URL,videoMessage.getmThumbUri().toString());
        context.startActivity(intent);

    }

    @Override
    public void onItemLongClick(View view, int i, YouBanShortVideoMessage videoMessage, UIMessage uiMessage) {

    }


}
