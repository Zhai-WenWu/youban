//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.constants.Constants;
//import cn.bjhdltcdn.p2plive.model.HelpInfo;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.PostInfo;
//import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.PKParkActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
//import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
//
///**
// * Created by ZHUDI on 2017/11/20.
// */
//
//public class FindItemShowListAdapter extends BaseRecyclerAdapter {
//    private List<HomeInfo> list_find = new ArrayList<>();
//    private Activity mActivity;
//    private final RequestOptions options;
//    private ImageHolder holder;
//
//    private ImageHolder imageHolder;
//    private String context;
//    private int followCount;
//    private int listSize;
//    private int homeInfoType;
//    private HomeInfo homeInfo;
//    private int status;
//    private int isOriginal;
//
//    public FindItemShowListAdapter(Activity activity) {
//        this.mActivity = activity;
//        options = new RequestOptions().transform(new GlideRoundTransform(9)).error(R.mipmap.error_bg);
//    }
//
//    public void setData(List<HomeInfo> list) {
//        list_find = list;
//        notifyDataSetChanged();
//    }
//
//    public void addData(List<HomeInfo> list) {
//        list_find.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return list_find == null ? 0 : list_find.size();
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_find_two, viewGroup, false));
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//        if (holder instanceof ImageHolder) {
//            imageHolder = (ImageHolder) holder;
//            homeInfo = list_find.get(position);
//            homeInfoType = homeInfo.getType();
//            switch (homeInfoType) {
//                case 1:
//                    PostInfo postInfo = homeInfo.getPostInfo();
//                    context = postInfo.getContent();
//                    followCount = postInfo.getFollowCount();
//                    listSize = postInfo.getFollowList().size();
//                    isOriginal = postInfo.getIsOriginal();
//                    status = postInfo.getStatus();
//                    break;
//                case 8:
//                    SayLoveInfo sayLoveInfo = homeInfo.getSayLoveInfo();
//                    context = sayLoveInfo.getContent();
//                    followCount = sayLoveInfo.getFollowCount();
//                    listSize = sayLoveInfo.getFollowList().size();
//                    isOriginal = sayLoveInfo.getIsOriginal();
//                    status = sayLoveInfo.getStatus();
//                    break;
//                case 9:
//                    HelpInfo helpInfo = homeInfo.getHelpInfo();
//                    context = helpInfo.getContent();
//                    followCount = helpInfo.getFollowCount();
//                    listSize = helpInfo.getFollowList().size();
//                    isOriginal = helpInfo.getIsOriginal();
//                    status = helpInfo.getStatus();
//                    break;
//            }
//
//            imageHolder.fl_second.setVisibility(View.INVISIBLE);
//            imageHolder.fl_thread.setVisibility(View.INVISIBLE);
//            imageHolder.pk_name.setText(context);
//            imageHolder.tv_num.setText(followCount + "人跟拍");
//
//
//            if (status == 1) {
//                imageHolder.iv_first.setImageResource(R.drawable.video_already_delete);
//                imageHolder.iv_first.setEnabled(false);
//            } else {
//                imageHolder.iv_first.setEnabled(true);
//                switch (list_find.get(position).getType()) {
//                    case 1:
//                        Glide.with(mActivity).load(list_find.get(position).getPostInfo().getFollowList().get(0).getVideoImageUrl()).apply(options).into(imageHolder.iv_first);
//                        break;
//                    case 8:
//                        Glide.with(mActivity).load(list_find.get(position).getSayLoveInfo().getFollowList().get(0).getVideoImageUrl()).apply(options).into(imageHolder.iv_first);
//                        break;
//                    case 9:
//                        Glide.with(mActivity).load(list_find.get(position).getHelpInfo().getFollowList().get(0).getVideoImageUrl()).apply(options).into(imageHolder.iv_first);
//                        break;
//                }
//            }
//
//            if (listSize == 2) {
//                imageHolder.fl_second.setVisibility(View.VISIBLE);
//                imageHolder.fl_thread.setVisibility(View.INVISIBLE);
//                switch (list_find.get(position).getType()) {
//                    case 1:
//                        Glide.with(mActivity).load(list_find.get(position).getPostInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                         if (list_find.get(position).getPostInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 8:
//                        Glide.with(mActivity).load(list_find.get(position).getSayLoveInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                        if (list_find.get(position).getSayLoveInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 9:
//                        Glide.with(mActivity).load(list_find.get(position).getHelpInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                         if (list_find.get(position).getHelpInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                        break;
//                }
//            } else if (listSize > 2) {
//                imageHolder.fl_second.setVisibility(View.VISIBLE);
//                imageHolder.fl_thread.setVisibility(View.VISIBLE);
//                switch (list_find.get(position).getType()) {
//                    case 1:
//                        Glide.with(mActivity).load(list_find.get(position).getPostInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                        Glide.with(mActivity).load(list_find.get(position).getPostInfo().getFollowList().get(2).getVideoImageUrl()).apply(options).into(imageHolder.iv_thread);
//                        if (list_find.get(position).getPostInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                         if (list_find.get(position).getPostInfo().getFollowList().get(2).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_thread.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_thread.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 8:
//                        Glide.with(mActivity).load(list_find.get(position).getSayLoveInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                        Glide.with(mActivity).load(list_find.get(position).getSayLoveInfo().getFollowList().get(2).getVideoImageUrl()).apply(options).into(imageHolder.iv_thread);
//                         if (list_find.get(position).getSayLoveInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                        if (list_find.get(position).getSayLoveInfo().getFollowList().get(2).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_thread.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_thread.setVisibility(View.GONE);
//                        }
//                        break;
//                    case 9:
//                        Glide.with(mActivity).load(list_find.get(position).getHelpInfo().getFollowList().get(1).getVideoImageUrl()).apply(options).into(imageHolder.iv_second);
//                        Glide.with(mActivity).load(list_find.get(position).getHelpInfo().getFollowList().get(2).getVideoImageUrl()).apply(options).into(imageHolder.iv_thread);
//                       if (list_find.get(position).getHelpInfo().getFollowList().get(1).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_second.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_second.setVisibility(View.GONE);
//                        }
//                         if (list_find.get(position).getHelpInfo().getFollowList().get(2).getBaseUser().getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
//                            imageHolder.tv_lable_thread.setText("我的");
//                        } else {
//                            imageHolder.tv_lable_thread.setVisibility(View.GONE);
//                        }
//                        break;
//                }
//            }
//
//
//            imageHolder.iv_first.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onVideoClick.onClick(position, 0);
//                }
//            });
//            imageHolder.iv_second.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onVideoClick.onClick(position, 1);
//                }
//            });
//            imageHolder.iv_thread.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onVideoClick.onClick(position, 2);
//                }
//            });
//        }
//
//    }
//
//    OnVideoClick onVideoClick;
//
//    public void setOnVideoClick(OnVideoClick onVideoClick) {
//        this.onVideoClick = onVideoClick;
//    }
//
//
//    public List<HomeInfo> getListfind() {
//        return list_find;
//    }
//
//    class ImageHolder extends BaseViewHolder {
//        private ImageView iv_first;
//        private ImageView iv_second;
//        private ImageView iv_thread;
//        private TextView pk_name;
//        private TextView tv_num;
//        private TextView tv_lable_first;
//        private TextView tv_lable_second;
//        private TextView tv_lable_thread;
//        private FrameLayout fl_second;
//        private FrameLayout fl_thread;
//
//        public ImageHolder(View itemView) {
//            super(itemView);
//            iv_first = itemView.findViewById(R.id.iv_first);
//            iv_second = itemView.findViewById(R.id.iv_second);
//            iv_thread = itemView.findViewById(R.id.iv_thread);
//            pk_name = itemView.findViewById(R.id.pk_name);
//            tv_num = itemView.findViewById(R.id.tv_num);
//            tv_lable_first = itemView.findViewById(R.id.tv_lable_first);
//            tv_lable_second = itemView.findViewById(R.id.tv_lable_second);
//            tv_lable_thread = itemView.findViewById(R.id.tv_lable_thread);
//            fl_second = itemView.findViewById(R.id.fl_second);
//            fl_thread = itemView.findViewById(R.id.fl_thread);
//        }
//    }
//
//    public interface OnVideoClick {
//        void onClick(int position, int item);
//    }
//
//}
