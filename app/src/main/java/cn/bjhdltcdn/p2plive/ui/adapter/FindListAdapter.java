//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.MultiTransformation;
//import com.bumptech.glide.load.resource.bitmap.CenterCrop;
//import com.bumptech.glide.request.RequestOptions;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import cn.bjhdltcdn.p2plive.R;
//import cn.bjhdltcdn.p2plive.app.App;
//import cn.bjhdltcdn.p2plive.model.HomeInfo;
//import cn.bjhdltcdn.p2plive.model.RecommendInfo;
//import cn.bjhdltcdn.p2plive.model.RoomInfo;
//import cn.bjhdltcdn.p2plive.ui.activity.FindPkItemShowActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.ChatRoomActivity;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import de.hdodenhof.circleimageview.CircleImageView;
//import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
//
///**
// * Created by ZHUDI on 2017/11/20.
// */
//
//public class FindListAdapter extends BaseRecyclerAdapter {
//    private List<HomeInfo> list_find;
//    private Activity mActivity;
//    private final RequestOptions options, userOptions;
//    private ImageHolder holder;
//
//    private final int TYPE_HEADER = 1, TYPE_ITEM = 2;
//    private int TYPE_NEW = 1, TYPE_HOT = 2, TYPE_ITEM_SHOW = 3;
//    private int tabType = TYPE_NEW;
//    private int type;
//    private ImageHolder imageHolder;
//    private List<RoomInfo> getRoomListResponse;
//    private List<RecommendInfo> recommendInfoList = new ArrayList<>();
//    private FindActivityAdapter findActivityAdapter;
//    private long parentId;
//    private int homeInfoType;
//    private boolean clickItemShow = false;
//    private boolean clickNewShow = false;
//    private String userIcon;
//    private String nickName;
//
//    public FindListAdapter(Activity activity) {
//        this.mActivity = activity;
//        MultiTransformation multi = new MultiTransformation(
//                new CenterCrop(),
//                new RoundedCornersTransformation(20, 0, RoundedCornersTransformation.CornerType.TOP));
//        options = new RequestOptions()
//                .transform(multi)
//                .placeholder(R.mipmap.error_bg)
//                .error(R.mipmap.error_bg);
//        userOptions = new RequestOptions()
//                .placeholder(R.mipmap.error_user_icon)
//                .error(R.mipmap.error_user_icon);
//    }
//
//    public void setData(List<HomeInfo> list) {
//        this.list_find = list;
//        if (list_find.size() == 0 || list_find.get(0).getType() > 0) {
//            HomeInfo homeInfoOne = new HomeInfo();//头布局
//            homeInfoOne.setType(-1);
//            list_find.add(0, homeInfoOne);
//        }
//        notifyDataSetChanged();
//    }
//
//
//    public void addData(List<HomeInfo> list) {
//        list_find.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    public void setRoomList(List<RoomInfo> roomListResponse) {
//        this.getRoomListResponse = roomListResponse;
//        if (roomListResponse == null) {
//            this.getRoomListResponse = new ArrayList<>(1);
//        }
//        notifyItemChanged(0);
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (list_find.get(position) != null) {
//            if (list_find.get(position).getType() >= 0) {
//                type = TYPE_ITEM;
//            } else if (list_find.get(position).getType() == -1) {
//                type = TYPE_HEADER;
//            }
//        }
//        return type;
//    }
//
//    @Override
//    public int getItemCount() {
//        return list_find == null ? 0 : list_find.size();
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//        if (itemType == TYPE_ITEM) {
//            holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_find_fragment, viewGroup, false));
//        } else if (itemType == TYPE_HEADER) {
//            holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.header_findfragment_top, viewGroup, false));
//        }
//        return holder;
//    }
//
//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//        super.onAttachedToRecyclerView(recyclerView);
//        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
//        if (manager instanceof GridLayoutManager) {
//            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
//            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//                @Override
//                public int getSpanSize(int position) {
//                    return getItemViewType(position) == TYPE_HEADER ? gridManager.getSpanCount() : 1;
//                }
//            });
//        }
//    }
//
//    public List<HomeInfo> getList_find() {
//        return list_find;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        super.onBindViewHolder(holder, position);
//        if (holder instanceof ImageHolder) {
//            imageHolder = (ImageHolder) holder;
//            if (getItemCount() > 0 && getItemViewType(position) == TYPE_ITEM) {
//                final HomeInfo mHomeInfo = list_find.get(position);
//                homeInfoType = mHomeInfo.getType();
//                int sex = mHomeInfo.getBaseUser().getSex();
//                if (sex == 1) {
//                    //男性
//                    imageHolder.iv_sex.setImageResource(R.mipmap.boy_icon);
//                } else if (sex == 2) {
//                    //女性
//                    imageHolder.iv_sex.setImageResource(R.mipmap.girl_icon);
//                }
//                switch (homeInfoType) {
//                    case 1:
//                        Glide.with(App.getInstance()).asBitmap().load(mHomeInfo.getPostInfo().getVideoImageUrl()).apply(options).into(imageHolder.iv_all_people_activity);
//                        userIcon = mHomeInfo.getPostInfo().getBaseUser().getUserIcon();
//                        nickName = mHomeInfo.getPostInfo().getBaseUser().getNickName();
//                        parentId = mHomeInfo.getPostInfo().getParentId();
//                        Glide.with(App.getInstance()).asBitmap().load(userIcon).apply(userOptions).into(imageHolder.iv_icon);
//                        imageHolder.tv_name.setText(nickName);
//                        break;
//                    case 8:
//                        Glide.with(App.getInstance()).asBitmap().load(mHomeInfo.getSayLoveInfo().getVideoImageUrl()).apply(options).into(imageHolder.iv_all_people_activity);
//                        userIcon = mHomeInfo.getSayLoveInfo().getBaseUser().getUserIcon();
//                        nickName = mHomeInfo.getSayLoveInfo().getBaseUser().getNickName();
//                        parentId = mHomeInfo.getSayLoveInfo().getParentId();
//                        Glide.with(App.getInstance()).asBitmap().load(userIcon).apply(userOptions).into(imageHolder.iv_icon);
//                        imageHolder.tv_name.setText(nickName);
//                        break;
//                    case 9:
//                        Glide.with(App.getInstance()).asBitmap().load(mHomeInfo.getHelpInfo().getVideoImageUrl()).apply(options).into(imageHolder.iv_all_people_activity);
//                        userIcon = mHomeInfo.getHelpInfo().getBaseUser().getUserIcon();
//                        nickName = mHomeInfo.getHelpInfo().getBaseUser().getNickName();
//                        parentId = mHomeInfo.getHelpInfo().getParentId();
//                        Glide.with(App.getInstance()).asBitmap().load(userIcon).apply(userOptions).into(imageHolder.iv_icon);
//                        imageHolder.tv_name.setText(nickName);
//                        break;
//                }
//            } else if (getItemCount() > 0 && getItemViewType(position) == TYPE_HEADER) {
//
//
//                if (list_find.size() > 1) {
//                    imageHolder.empty_view.setVisibility(View.GONE);
//                } else {//只有头布局
//                    imageHolder.empty_view.setVisibility(View.VISIBLE);
//                    imageHolder.empty_textView.setText("快来发起pk吧");
//                }
//
//                imageHolder.rela_room.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mActivity.startActivity(new Intent(mActivity, ChatRoomActivity.class));
//                    }
//                });
//
//                if (hasNewViewVisible) {
//                    imageHolder.iv_have_new_video.setVisibility(View.VISIBLE);
//                    imageHolder.iv_have_new_item_video.setVisibility(View.VISIBLE);
//                } else {
//                    imageHolder.iv_have_new_video.setVisibility(View.GONE);
//                    imageHolder.iv_have_new_item_video.setVisibility(View.GONE);
//                }
//
//                if (clickItemShow) {
//                    imageHolder.iv_have_new_item_video.setVisibility(View.GONE);
//                }
//                if (clickNewShow) {
//                    imageHolder.iv_have_new_video.setVisibility(View.GONE);
//                }
//
//                if (recommendInfoList.size() > 0) {
//                    imageHolder.tv_activity.setVisibility(View.VISIBLE);
//                    if (findActivityAdapter == null) {
//                        findActivityAdapter = new FindActivityAdapter(mActivity);
//                    }
//                    imageHolder.rv_activity.setAdapter(findActivityAdapter);
//                    findActivityAdapter.setData(recommendInfoList);
//                } else {
//                    imageHolder.tv_activity.setVisibility(View.GONE);
//                }
//
//                if (tabType == TYPE_HOT) {
//                    imageHolder.bt_hot.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
//                    imageHolder.bt_new.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                    imageHolder.tv_item_show.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                    imageHolder.bt_hot_bottom.setVisibility(View.VISIBLE);
//                    imageHolder.bt_new_bottom.setVisibility(View.GONE);
//                    imageHolder.tv_item_show_bottom.setVisibility(View.GONE);
//                } else if (tabType == TYPE_NEW) {
//                    imageHolder.bt_hot.setTextColor(mActivity.getResources().getColor(R.color.color_999999));
//                    imageHolder.bt_new.setTextColor(App.getInstance().getResources().getColor(R.color.color_333333));
//                    imageHolder.tv_item_show.setTextColor(App.getInstance().getResources().getColor(R.color.color_999999));
//                    imageHolder.bt_hot_bottom.setVisibility(View.GONE);
//                    imageHolder.bt_new_bottom.setVisibility(View.VISIBLE);
//                    imageHolder.tv_item_show_bottom.setVisibility(View.GONE);
//                }
//
//                imageHolder.bt_hot.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mFindTabClick.Hot();
//                        tabType = TYPE_HOT;
//                    }
//                });
//                imageHolder.bt_new.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clickNewShow = true;
//                        notifyItemChanged(0);
//                        mFindTabClick.Newest();
//                        tabType = TYPE_NEW;
//
//                    }
//                });
//                imageHolder.tv_item_show.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        clickItemShow = true;
//                        notifyItemChanged(0);
//                        mActivity.startActivity(new Intent(mActivity, FindPkItemShowActivity.class));
//                    }
//                });
//            }
//        }
//
//    }
//
//    public void setRecommendInfoList(List<RecommendInfo> recommendInfoList) {
//        this.recommendInfoList = recommendInfoList;
//        notifyItemChanged(0);
//    }
//
//    public boolean hasNewViewVisible = false;
//
//    public void setHasNewViewVisible(boolean hasNewViewVisible) {
//        this.hasNewViewVisible = hasNewViewVisible;
//        notifyItemChanged(0);
//    }
//
//    class ImageHolder extends BaseViewHolder {
//        private ImageView iv_all_people_activity, iv_have_new_video, iv_have_new_item_video, iv_sex;
//        private TextView tv_pk, tv_name;
//        private Button tv_item_show;
//        private TextView tv_activity;
//        private RelativeLayout rela_room;
//        private CircleImageView civ_room_icon_one, civ_room_icon_two, civ_room_icon_three, civ_room_icon_four, civ_room_icon_five, iv_icon;
//        private View empty_view;
//        private TextView empty_textView;
//        private Button bt_new, bt_hot;
//        private RecyclerView rv_activity;
//        private View tv_item_show_bottom, bt_new_bottom, bt_hot_bottom;
//
//        public ImageHolder(View itemView) {
//            super(itemView);
//            iv_all_people_activity = itemView.findViewById(R.id.iv_all_people_activity);
//            rela_room = itemView.findViewById(R.id.rela_room);
//            civ_room_icon_one = itemView.findViewById(R.id.civ_room_icon_one);
//            tv_activity = itemView.findViewById(R.id.tv_activity);
//            civ_room_icon_two = itemView.findViewById(R.id.civ_room_icon_two);
//            civ_room_icon_three = itemView.findViewById(R.id.civ_room_icon_three);
//            civ_room_icon_four = itemView.findViewById(R.id.civ_room_icon_four);
//            civ_room_icon_five = itemView.findViewById(R.id.civ_room_icon_five);
//            tv_pk = itemView.findViewById(R.id.tv_pk);
//            tv_item_show = itemView.findViewById(R.id.tv_item_show);
//            empty_view = itemView.findViewById(R.id.empty_view);
//            empty_textView = itemView.findViewById(R.id.empty_textView);
//            bt_hot = itemView.findViewById(R.id.bt_hot);
//            bt_new = itemView.findViewById(R.id.bt_new);
//            rv_activity = itemView.findViewById(R.id.rv_activity);
//            tv_item_show_bottom = itemView.findViewById(R.id.tv_item_show_bottom);
//            bt_new_bottom = itemView.findViewById(R.id.bt_new_bottom);
//            bt_hot_bottom = itemView.findViewById(R.id.bt_hot_bottom);
//            iv_have_new_video = itemView.findViewById(R.id.iv_have_new_video);
//            iv_have_new_item_video = itemView.findViewById(R.id.iv_have_new_item_video);
//            iv_icon = itemView.findViewById(R.id.iv_icon);
//            tv_name = itemView.findViewById(R.id.tv_name);
//            iv_sex = itemView.findViewById(R.id.iv_sex);
//        }
//    }
//
//    FindTabClick mFindTabClick;
//
//    public void setFindTabClick(FindTabClick findTabClick) {
//        this.mFindTabClick = findTabClick;
//    }
//
//    public interface FindTabClick {
//        void Newest();
//
//        void Hot();
//    }
//}
//
