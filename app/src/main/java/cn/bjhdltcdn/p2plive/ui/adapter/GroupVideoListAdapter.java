package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.RoomBaseUser;
import cn.bjhdltcdn.p2plive.model.Wheat;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.video.VideoCanvas;

/**
 * Created by ZHUDI on 2016/12/23.
 */

public class GroupVideoListAdapter extends BaseRecyclerAdapter {
    private List<Wheat> list_video;
    private onItemLongClick onItemLongClick;
    private onItemClick onItemClick;
    private int itemCount;
    private boolean isLocalMicClose, isLocalVideoClose, isTransferHosting;

    public void setLocalMicClose(boolean localMicClose) {
        isLocalMicClose = localMicClose;
    }

    public void setLocalVideoClose(boolean localVideoClose) {
        isLocalVideoClose = localVideoClose;
    }

    public void setTransferHosting(boolean transferHosting) {
        isTransferHosting = transferHosting;
    }

    @Override
    public int getItemCount() {
        if (list_video != null) {
            for (int i = list_video.size(); i > 4; i--) {
                int isUser = list_video.get(i - 1).getIsUser();
                if (isUser == 1) {//5~7麦有用户，直接显示6人，否则显示3人
                    itemCount = 6;
                    return itemCount;
                }
            }

            for (int i = 1; i < 4; i++) {
                int isUser = list_video.get(i).getIsUser();
                if (isUser == 1) {//2~4麦有用户，直接显示3人
                    itemCount = 3;
                    return itemCount;
                }
            }
        }
        return 0;
    }

    /**
     * 更新自己的画面
     */
    public void updateCurrUserView() {
        int i = 0;
        List<Wheat> list = list_video.subList(1, list_video.size());
        for (Wheat wheat : list) {
            RoomBaseUser baseUser = wheat.getUser();
            if (wheat.getIsUser() == 1 && baseUser != null && baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                notifyItemChanged(i);
                return;
            }
            i++;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View itemView = LayoutInflater.from(App.getInstance()).inflate(R.layout.item_video_group, viewGroup, false);
        WindowManager windowManager = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        itemView.getLayoutParams().width = outMetrics.widthPixels / 3;
        itemView.getLayoutParams().height = Utils.dp2px(126);
        return new VideoUserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VideoUserHolder) {
            VideoUserHolder videoUserHolder = (VideoUserHolder) holder;

            // 麦序 从第二位开始
            TypedArray levelZeroIcon = App.getInstance().getResources().obtainTypedArray(R.array.video_group_mic_order_icon);
            Drawable micOrderDrawable = levelZeroIcon.getDrawable((position + 1));
            videoUserHolder.img_mic_order.setImageDrawable(micOrderDrawable);
            levelZeroIcon.recycle();

            int isUser = list_video.get(position + 1).getIsUser();//0不在麦上，1在麦上

            if (isUser == 1) {
                // 隐藏空麦文字
                videoUserHolder.textView.setVisibility(View.GONE);
                RoomBaseUser user = list_video.get(position + 1).getUser();
                SurfaceView surfaceView = App.getInstance().getRtcEngine().CreateRendererView(App.getInstance());
                videoUserHolder.frameLayout.removeAllViews();
                videoUserHolder.frameLayout.addView(surfaceView);
                // 检测如果是当前用户
                if (user.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                    //当前本地视频
                    App.getInstance().getRtcEngine().setClientRole(io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER, null);
                    App.getInstance().getRtcEngine().enableDualStreamMode(true);
                    App.getInstance().getRtcEngine().setVideoProfile(IRtcEngineEventHandler.VideoProfile.VIDEO_PROFILE_360P_8, true);
                    App.getInstance().getRtcEngine().setupLocalVideo(new VideoCanvas(surfaceView));
                    App.getInstance().getRtcEngine().muteLocalAudioStream(isLocalMicClose);
                    App.getInstance().getRtcEngine().muteLocalVideoStream(isLocalVideoClose);

                } else {// 远程视频
                    App.getInstance().getRtcEngine().setRemoteVideoStreamType((int) user.getUserId(), io.agora.rtc.Constants.VIDEO_STREAM_LOW);
                    App.getInstance().getRtcEngine().setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, (int) user.getUserId()));
                }

                if (user.getType() == 2) {// 关闭视频流
                    videoUserHolder.colseMicView.setVisibility(View.VISIBLE);
                    Utils.ImageViewDisplayByUrl(user.getUserIcon(), videoUserHolder.img_icon);
                    videoUserHolder.tv_nickname.setText(user.getNickName());
                } else {
                    // 默认隐藏关闭视频流标识
                    videoUserHolder.colseMicView.setVisibility(View.GONE);
                }

                surfaceView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onItemLongClick != null) {
                            onItemLongClick.onItemLongClick(v, position + 1);
                        }

                        return true;
                    }
                });

                surfaceView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onItemClick != null) {
                            onItemClick.onItemClick(v, position + 1);
                        }

                    }
                });

            } else {
                videoUserHolder.colseMicView.setVisibility(View.GONE);
                videoUserHolder.textView.setVisibility(View.VISIBLE);
            }


        }

    }


    class VideoUserHolder extends RecyclerView.ViewHolder implements SurfaceHolder {
        /**
         * 视频装载控件
         */
        private FrameLayout frameLayout;
        /**
         * 麦序
         */
        private ImageView img_mic_order;
        /**
         * 空麦
         */
        private TextView textView;

        /**
         * 关闭视频流标识
         */
        private View colseMicView;
        //关闭视频后显示
        private CircleImageView img_icon;
        private TextView tv_nickname;

        VideoUserHolder(View v) {
            super(v);
            frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout);
            img_mic_order = (ImageView) v.findViewById(R.id.img_mic_order);
            textView = (TextView) v.findViewById(R.id.text_view);

            colseMicView = v.findViewById(R.id.colse_mic_view);
            img_icon = (CircleImageView) v.findViewById(R.id.img_icon);
            tv_nickname = (TextView) v.findViewById(R.id.tv_nickname);
//            SurfaceView surfaceV = App.getInstance().getRtcEngine().CreateRendererView(App.getInstance());
//            frameLayout.removeAllViews();
//            frameLayout.addView(surfaceV);

        }

        @Override
        public void addCallback(Callback callback) {

        }

        @Override
        public void removeCallback(Callback callback) {

        }

        @Override
        public boolean isCreating() {
            return false;
        }

        @Override
        public void setType(int type) {

        }

        @Override
        public void setFixedSize(int width, int height) {

        }

        @Override
        public void setSizeFromLayout() {

        }

        @Override
        public void setFormat(int format) {

        }

        @Override
        public void setKeepScreenOn(boolean screenOn) {

        }

        @Override
        public Canvas lockCanvas() {
            return null;
        }

        @Override
        public Canvas lockCanvas(Rect rect) {
            return null;
        }


        @Override
        public void unlockCanvasAndPost(Canvas canvas) {

        }

        @Override
        public Rect getSurfaceFrame() {
            return null;
        }

        @Override
        public Surface getSurface() {
            return null;
        }
    }

    public void setData(List<Wheat> list_video) {
        this.list_video = list_video;
        notifyDataSetChanged();
    }

    public List<Long> getMicUserIdList() {
        List<Long> micUserIdList = new ArrayList<>();
        if (list_video != null && list_video.size() > 0) {
            for (Wheat wheat : list_video) {
                int isUser = wheat.getIsUser();
                if (isUser == 1) {
                    micUserIdList.add(wheat.getUser().getUserId());
                }
            }
        }
        return micUserIdList;
    }

    public List<RoomBaseUser> getMicUserList() {
        if (list_video == null || list_video.size() == 0) {
            return null;
        }

        List<RoomBaseUser> micUserList = new ArrayList<>();
        for (Wheat wheat : list_video) {
            int isUser = wheat.getIsUser();
            if (isUser == 1) {
                micUserList.add(wheat.getUser());
            }
        }
        return micUserList;
    }

    public ArrayList<RoomBaseUser> getMicUserOutMyselfList() {
        if (list_video == null || list_video.size() == 0) {
            return null;
        }

        ArrayList<RoomBaseUser> micUserList = new ArrayList<>();
        for (Wheat wheat : list_video) {
            int isUser = wheat.getIsUser();
            RoomBaseUser baseUser = wheat.getUser();
            if (isUser == 1 && baseUser.getUserId() != SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                micUserList.add(baseUser);
            }
        }
        return micUserList;
    }

    public Map<Long, Integer> getMicUserMap() {
        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < list_video.size(); i++) {
            int isUser = list_video.get(i).getIsUser();
            if (isUser == 1) {
                map.put(list_video.get(i).getUser().getUserId(), i);
            }
        }
        return map;
    }

    public List<Wheat> getList_video() {
        return list_video;
    }

    public interface onItemClick {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(onItemClick listener) {
        this.onItemClick = listener;
    }

    public interface onItemLongClick {
        void onItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(onItemLongClick listener) {
        this.onItemLongClick = listener;
    }

    public void clearData() {
        if (getItemCount() > 0) {
            list_video.clear();
            list_video = null;
        }
    }
}
