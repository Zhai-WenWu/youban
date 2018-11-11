package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.BaseUser;
import cn.bjhdltcdn.p2plive.model.HelpInfo;
import cn.bjhdltcdn.p2plive.model.HomeInfo;
import cn.bjhdltcdn.p2plive.model.OrganBaseInfo;
import cn.bjhdltcdn.p2plive.model.PostInfo;
import cn.bjhdltcdn.p2plive.model.SayLoveInfo;
import cn.bjhdltcdn.p2plive.ui.activity.PKVideoPlayActivity;
import cn.bjhdltcdn.p2plive.ui.activity.UserDetailsActivity;
import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.Utils;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.widget.tagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ZHAI on 2017/12/21.
 */

public class PkDetailAdapter extends BaseRecyclerAdapter {
    private Activity mActivity;
    private final int TYPE_HEADER = 1, TYPE_ITEM = 2;
    private int type;
    private ViewHolder holder;
    private HomeInfo mHomeInfo;
    private BaseUser baseUser;
    private String videoImageUrl;
    private int userRole;
    private String content;
    private List<PostInfo> postInfoList = new ArrayList<>();
    private List<SayLoveInfo> sayLoveInfoList = new ArrayList<>();
    private List<HelpInfo> helpInfoList = new ArrayList<>();
    private int videoType;
    private int isAnonymous;
    private int isOriginal;
    private long userId;
    private int status;
    private List<OrganBaseInfo> organList;
    private int ranking;
    private String organName;
    private long lableUserId;

    public PkDetailAdapter(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public PkDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        if (itemType == TYPE_ITEM) {
            holder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_pk_detail, null));
        } else if (itemType == TYPE_HEADER) {
            holder = new ViewHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_pk_detail_header, null));
        }
        return holder;
    }

    @Override
    public int getItemCount() {
        if (videoType == 1) {
            return postInfoList == null ? 0 : postInfoList.size();
        } else if (videoType == 8) {
            return sayLoveInfoList == null ? 0 : sayLoveInfoList.size();
        } else if (videoType == 9) {
            return helpInfoList == null ? 0 : helpInfoList.size();
        } else {
            return 0;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return gridLayoutManager.getSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            type = TYPE_HEADER;
        } else {
            type = TYPE_ITEM;
        }
        return type;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        final PkDetailAdapter.ViewHolder itemViewHolder = (PkDetailAdapter.ViewHolder) holder;

        if (getItemViewType(position) == TYPE_ITEM) {

            if (videoType == 1) {
                PostInfo postInfo = postInfoList.get(position);
                lableUserId = postInfo.getBaseUser().getUserId();
                videoImageUrl = postInfo.getVideoImageUrl();
                userRole = postInfo.getUserRole();
                isOriginal = postInfo.getIsOriginal();
                status = postInfo.getStatus();
                isAnonymous = postInfo.getIsAnonymous();
                ranking = postInfo.getRanking();
            } else if (videoType == 8) {
                SayLoveInfo sayLoveInfo = sayLoveInfoList.get(position);
                lableUserId = sayLoveInfo.getBaseUser().getUserId();
                videoImageUrl = sayLoveInfo.getVideoImageUrl();
                userRole = sayLoveInfo.getUserRole();
                isOriginal = sayLoveInfo.getIsOriginal();
                status = sayLoveInfo.getStatus();
                isAnonymous = sayLoveInfo.getIsAnonymous();
                ranking = sayLoveInfo.getRanking();
            } else if (videoType == 9) {
                HelpInfo helpInfo = helpInfoList.get(position);
                lableUserId = helpInfo.getBaseUser().getUserId();
                videoImageUrl = helpInfo.getVideoImageUrl();
                userRole = helpInfo.getUserRole();
                isOriginal = helpInfo.getIsOriginal();
                status = helpInfo.getStatus();
                ranking = helpInfo.getRanking();
                //isAnonymous = helpInfo.getIsAnonymous();
            } else {
                return;
            }

            if (status == 1) {
                itemViewHolder.iv.setImageResource(R.drawable.video_already_delete);
            } else {
                Utils.CornerImageViewDisplayByUrl(videoImageUrl, itemViewHolder.iv,9);
            }

            if (ranking <= 3) {
                //itemViewHolder.tv_no.setText("NO." + ranking);
                itemViewHolder.tv_no.setText(Html.fromHtml("NO." + "<big>" + ranking + "</big>"));
            }


            if (isOriginal == 0) {//自己
                itemViewHolder.tv_lable.setVisibility(View.VISIBLE);
                itemViewHolder.tv_lable.setText("发起者");
            } else if (lableUserId == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0)) {
                itemViewHolder.tv_lable.setVisibility(View.VISIBLE);
                itemViewHolder.tv_lable.setText("我的");
            } else {
                itemViewHolder.tv_lable.setVisibility(View.GONE);
            }

            itemViewHolder.setItemListener(new ItemListener() {
                @Override
                public void onItemClick(View view, int position) {


                    Intent intent = new Intent(mActivity, PKVideoPlayActivity.class);
                    intent.putExtra(Constants.Fields.TYPE, videoType);
                    switch (videoType) {
                        case 1:
                            if (postInfoList.get(position).getStatus() != 1) {
                                intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, postInfoList.get(position).getPostId());
                                intent.putExtra(Constants.Fields.COME_IN_TYPE, 2);
                                mActivity.startActivity(intent);
                            }
                            break;
                        case 8:
                            if (sayLoveInfoList.get(position).getStatus() != 1) {
                                intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, sayLoveInfoList.get(position).getSayLoveId());
                                intent.putExtra(Constants.Fields.COME_IN_TYPE, 2);
                                mActivity.startActivity(intent);
                            }
                            break;
                        case 9:
                            if (helpInfoList.get(position).getStatus() != 1) {
                                intent.putExtra(Constants.Fields.VIDEO_PLAY_ID, helpInfoList.get(position).getHelpId());
                                intent.putExtra(Constants.Fields.COME_IN_TYPE, 2);
                                mActivity.startActivity(intent);
                            }
                            break;
                    }
                }
            });
        } else if (getItemViewType(position) == TYPE_HEADER) {

            if (organList.size() == 0) {
                itemViewHolder.ll_tag.setVisibility(View.INVISIBLE);
            } else {
                itemViewHolder.tagContainerLayout.removeAllViews();
                for (int i = 0; i < organList.size(); i++) {
                    TextView textView = new TextView(mActivity);
                    textView.setBackgroundResource(R.drawable.shape_round_20_stroke_ffffff);

                    organName = organList.get(i).getOrganName();
                    if (organName.length() > 4) {
                        organName = organName.substring(0, 4) + "...";
                    }
                    textView.setText(organName);

                    textView.setTextSize(10);
                    textView.setPadding(15, 2, 15, 2);
                    textView.setTextColor(App.getInstance().getResources().getColor(R.color.color_ffffff));
                    itemViewHolder.tagContainerLayout.addView(textView);
                    textView.setOnClickListener(new ViewOnClickListener(i));
                }
            }

            itemViewHolder.tvTitleDetail.setText(content);
            itemViewHolder.tvName.setText(baseUser.getNickName());

            if (isAnonymous == 1) {
                itemViewHolder.tvName.setText("匿名发布");
            }

            Glide.with(mActivity).load(baseUser.getUserIcon()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(itemViewHolder.ivIcon);
            if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) || isAnonymous == 1) {
                itemViewHolder.ivIcon.setEnabled(false);
            }

            if (baseUser.getIsAttention() == 1) {
                itemViewHolder.ivAddFriend.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.hobby_select));
                itemViewHolder.ivAddFriend.setEnabled(false);
            }

            if (baseUser.getUserId() == SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0) || isAnonymous == 1) {
                itemViewHolder.ivAddFriend.setVisibility(View.INVISIBLE);
            }

            itemViewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class).putExtra(Constants.KEY.KEY_OBJECT, baseUser));

                }
            });
            itemViewHolder.ivAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.attClick(userId);
                    itemViewHolder.ivAddFriend.setImageDrawable(App.getInstance().getResources().getDrawable(R.drawable.hobby_select));
                }
            });
        }
    }

    public void setData(HomeInfo homeInfo) {
        this.mHomeInfo = homeInfo;
        videoType = mHomeInfo.getType();
        sayLoveInfoList.clear();
        postInfoList.clear();
        helpInfoList.clear();
//        baseUser = mHomeInfo.getBaseUser();
//        userId = baseUser.getUserId();
        switch (videoType) {
            case 1:
                PostInfo postInfo = new PostInfo();
                postInfo.setPostId(-1);
                postInfoList.add(0, postInfo);
                postInfoList.addAll(mHomeInfo.getPostInfo().getFollowList());
                content = mHomeInfo.getPostInfo().getContent();
                isAnonymous = mHomeInfo.getPostInfo().getIsAnonymous();
                organList = mHomeInfo.getPostInfo().getOrganList();
                baseUser = mHomeInfo.getPostInfo().getBaseUser();
                userId = baseUser.getUserId();
                break;
            case 8:
                SayLoveInfo sayLoveInfo = new SayLoveInfo();
                sayLoveInfo.setSayLoveId(-1);
                sayLoveInfoList.add(0, sayLoveInfo);
                sayLoveInfoList.addAll(mHomeInfo.getSayLoveInfo().getFollowList());
                content = mHomeInfo.getSayLoveInfo().getContent();
                isAnonymous = mHomeInfo.getSayLoveInfo().getIsAnonymous();
                organList = mHomeInfo.getSayLoveInfo().getOrganList();
                baseUser = mHomeInfo.getSayLoveInfo().getBaseUser();
                userId = baseUser.getUserId();
                break;
            case 9:
                HelpInfo helpInfo = new HelpInfo();
                helpInfo.setHelpId(-1);
                helpInfoList.add(0, helpInfo);
                helpInfoList.addAll(mHomeInfo.getHelpInfo().getFollowList());
                content = mHomeInfo.getHelpInfo().getContent();
                //isAnonymous = mHomeInfo.getHelpInfo().getIsAnonymous();
                organList = mHomeInfo.getHelpInfo().getOrganList();
                baseUser = mHomeInfo.getHelpInfo().getBaseUser();
                userId = baseUser.getUserId();
                break;
        }
        notifyDataSetChanged();
    }

    public void addData(HomeInfo homeInfo) {
        this.mHomeInfo = homeInfo;
        videoType = mHomeInfo.getType();
        baseUser = mHomeInfo.getBaseUser();
        userId = baseUser.getUserId();
        switch (videoType) {
            case 1:
                postInfoList.addAll(mHomeInfo.getPostInfo().getFollowList());
                content = mHomeInfo.getPostInfo().getContent();
                break;
            case 8:
                sayLoveInfoList.addAll(mHomeInfo.getSayLoveInfo().getFollowList());
                content = mHomeInfo.getSayLoveInfo().getContent();
                break;
            case 9:
                helpInfoList.addAll(mHomeInfo.getHelpInfo().getFollowList());
                content = mHomeInfo.getHelpInfo().getContent();
                break;
        }
        notifyDataSetChanged();
    }

    public HomeInfo getData() {
        return mHomeInfo;
    }

    class ViewHolder extends BaseViewHolder {
        ImageView iv;
        TextView tv_lable;
        TextView tv_number;
        TextView tv_no;
        TextView tvName;
        TextView tvTitleDetail;
        CircleImageView ivIcon;
        CircleImageView ivAddFriend;
        TextView tv_starter;
        LinearLayout ll_tag;
        TagContainerLayout tagContainerLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            tv_lable = itemView.findViewById(R.id.tv_lable);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_no = itemView.findViewById(R.id.tv_no);
            tvName = itemView.findViewById(R.id.tv_name);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            ivAddFriend = itemView.findViewById(R.id.iv_add_friend);
            tvTitleDetail = itemView.findViewById(R.id.tv_title_detail);
            tv_starter = itemView.findViewById(R.id.tv_starter);
            ll_tag = itemView.findViewById(R.id.ll_tag);
            tagContainerLayout = itemView.findViewById(R.id.tag_container_view);
        }
    }

    PkDetailAdapter.OnClickListener onClickListener;

    public void setItemClickListener(PkDetailAdapter.OnClickListener listener) {
        this.onClickListener = listener;
    }


    public interface OnClickListener {

        void userIconClick();

        void attClick(long userId);
    }

    class ViewOnClickListener implements View.OnClickListener {

        private int position;

        public ViewOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {

        }


    }
}
