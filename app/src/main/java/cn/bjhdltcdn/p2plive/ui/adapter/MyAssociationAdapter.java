//package cn.bjhdltcdn.p2plive.ui.adapter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.ViewGroup;
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
//import cn.bjhdltcdn.p2plive.model.OrganizationInfo;
//import cn.bjhdltcdn.p2plive.model.User;
//import cn.bjhdltcdn.p2plive.mvp.presenter.AssociationPresenter;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationDetailActivity;
//import cn.bjhdltcdn.p2plive.ui.activity.AssociationTypeListActivity;
//import cn.bjhdltcdn.p2plive.ui.listener.ItemListener;
//import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
//import cn.bjhdltcdn.p2plive.utils.Utils;
//import de.hdodenhof.circleimageview.CircleImageView;
//
///**
// *
// */
//
//public class MyAssociationAdapter extends BaseRecyclerAdapter {
//
//    private static final int ITEM_0 = -1;
//    private static final int ITEM_1 = 1;
//
//    /**
//     * 0 我的圈子列表
//     * 1 个人用户圈子列表
//     */
//    private int type = 0;
//
//    public void setType(int type) {
//        this.type = type;
//    }
//
//    public User user;
//    private AssociationPresenter presenter;
//    public Activity mActivity;
//
//    private List<OrganizationInfo> list;
//
//    public MyAssociationAdapter(Activity activity) {
//        this.mActivity = activity;
//    }
//
//    public List<OrganizationInfo> getList() {
//        return list;
//    }
//
//    public void setList(List<OrganizationInfo> list) {
//
//        if (this.list != null) {
//            this.list.addAll(list);
//        } else {
//            this.list = list;
//        }
//
//        notifyDataSetChanged();
//
//    }
//
//    public void addDataItems(List<OrganizationInfo> list) {
//
//        if (list == null) {
//            return;
//        }
//
//        if (this.list == null) {
//            return;
//        }
//        this.list.addAll(list);
//        notifyDataSetChanged();
//
//
//    }
//
//    public void setUser(User user) {
//
//        this.user = user;
//
//    }
//
//    public void addItem(OrganizationInfo organizationInfo) {
//        if (list == null) {
//            list = new ArrayList<>(1);
//        }
//        list.add(organizationInfo);
//        notifyDataSetChanged();
//    }
//
//    public void addItemAll(List<OrganizationInfo> list) {
//        if (this.list == null) {
//            this.list = new ArrayList<>(1);
//        }
//        this.list.addAll(list);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemCount() {
//        return list == null ? 0 : list.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//
//        if (list == null || list.size() == 0) {
//            return ITEM_1;
//        }
//
//        OrganizationInfo organizationInfo = list.get(position);
//        if (organizationInfo.getType() == -1) {
//            return ITEM_0;
//        } else {
//            return ITEM_1;
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        super.onBindViewHolder(holder, position);
//
//        int itemType = getItemViewType(position);
//        switch (itemType) {
//            case ITEM_0:
//
//                if (holder instanceof Item0ViewHolder) {
//                    final Item0ViewHolder viewHolder = (Item0ViewHolder) holder;
//                    viewHolder.cardview.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if (user != null) {
//                                Intent intent = new Intent(mActivity, AssociationTypeListActivity.class);
//                                intent.putExtra(Constants.Fields.TYPE, 3);
//                                intent.putExtra(Constants.Fields.Ta_USER_ID, user.getUserId());
//                                mActivity.startActivity(intent);
//                            }
//                        }
//                    });
//
//                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (viewHolder.getItemListener() != null) {
//                                viewHolder.getItemListener().onItemClick(v, position);
//                            }
//                        }
//                    });
//
//                }
//
//                break;
//
//            case ITEM_1:
//
//                if (holder instanceof ItemViewHolder) {
//                    final ItemViewHolder viewHolder = (ItemViewHolder) holder;
//
//                    final OrganizationInfo organizationInfo = list.get(position);
//
//                    String organImg = organizationInfo.getOrganImg();
//
//                    Glide.with(mActivity).load(organizationInfo.getOrganImg()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(viewHolder.imageView);
//
//                    viewHolder.textView1.setText("成员    " + organizationInfo.getMemberCount());
//                    viewHolder.textView2.setText("帖子    " + organizationInfo.getPostCount());
//                    // 圈子名称
//                    viewHolder.textView3.setText(organizationInfo.getOrganName());
//
//                    viewHolder.setItemListener(new ItemListener() {
//                        @Override
//                        public void onItemClick(View view, int position) {
//                            mActivity.startActivity(new Intent(mActivity, AssociationDetailActivity.class).putExtra(Constants.KEY.KEY_OBJECT, organizationInfo));
//                        }
//                    });
//
//                    if (type == 1) {
//                        ViewGroup.LayoutParams layoutParams = viewHolder.layoutView1.getLayoutParams();
//                        if (layoutParams == null) {
//                            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                        }
//                        layoutParams.width = Utils.dp2px(140);
//                        layoutParams.height = Utils.dp2px(160);
//                    }
//                }
//
//                break;
//
//        }
//
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
//
//        View itemView = null;
//        switch (itemType) {
//            case ITEM_0:
//
//                itemView = View.inflate(App.getInstance(), R.layout.adapter_my_association_list_item_add_layout, null);
//                final Item0ViewHolder view0Holder = new Item0ViewHolder(itemView);
//
//                return view0Holder;
//
//            case ITEM_1:
//                itemView = View.inflate(App.getInstance(), R.layout.adapter_my_association_list_item_layout, null);
//                ItemViewHolder viewHolder = new ItemViewHolder(itemView);
//
//                return viewHolder;
//
//        }
//
//        return null;
//
//    }
//
//    static class Item0ViewHolder extends BaseViewHolder {
//
//        CircleImageView circleImageView;
//        CardView cardview;
//        View itemView;
//
//        public Item0ViewHolder(View itemView) {
//            super(itemView);
//
//            this.itemView = itemView;
//
//            circleImageView = itemView.findViewById(R.id.add_img_view);
//            cardview = itemView.findViewById(R.id.cardview);
//
//        }
//    }
//
//    static class ItemViewHolder extends BaseViewHolder {
//
//        ImageView imageView;
//        TextView textView1;
//        TextView textView2;
//        TextView textView3;
//        View itemView;
//        View layoutView1;
//
//        public ItemViewHolder(View itemView) {
//            super(itemView);
//
//            this.itemView = itemView;
//
//            imageView = itemView.findViewById(R.id.image_view);
//            textView1 = itemView.findViewById(R.id.text_view_1);
//            textView2 = itemView.findViewById(R.id.text_view_2);
//            textView3 = itemView.findViewById(R.id.text_view_3);
//            layoutView1 = itemView.findViewById(R.id.layout_view_1);
//
//
//        }
//    }
//
//}
