package cn.bjhdltcdn.p2plive.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.constants.Constants;
import cn.bjhdltcdn.p2plive.model.RecommendInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;
import cn.bjhdltcdn.p2plive.utils.GlideRoundTransform;
import cn.bjhdltcdn.p2plive.utils.preference.SafeSharePreferenceUtils;
import cn.bjhdltcdn.p2plive.wxapi.WXPayEntryActivity;

/**
 * Created by ZHAI on 2018/2/27.
 */

class FindActivityAdapter extends BaseRecyclerAdapter {
    private List<RecommendInfo> mList;
    private Holder mHolder;
    private Activity mActivity;

    public FindActivityAdapter(Activity activity) {
        super();
        this.mActivity = activity;
    }

    public void setData(List<RecommendInfo> data) {
        this.mList = data;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        return new FindActivityAdapter.Holder(LayoutInflater.from(App.getInstance()).inflate(R.layout.item_list_find, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof Holder) {
            mHolder = (Holder) holder;
            final RecommendInfo recommendInfo = mList.get(position);
            Glide.with(mActivity).load(recommendInfo.getImgUrl()).apply(new RequestOptions().transform(new GlideRoundTransform( 9)).placeholder(R.mipmap.error_bg)).into(mHolder.iv_all_people_activity);
            mHolder.iv_all_people_activity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mActivity, WXPayEntryActivity.class);
                    intent.putExtra(Constants.KEY.KEY_URL, recommendInfo.getGotoUrl() + "?userId=" + SafeSharePreferenceUtils.getLong(Constants.Fields.USER_ID, 0));
                    mActivity.startActivity(intent);
                }
            });
        }
    }

    class Holder extends BaseViewHolder {

        private ImageView iv_all_people_activity;

        public Holder(View itemView) {
            super(itemView);
            iv_all_people_activity = itemView.findViewById(R.id.iv_all_people_activity);
        }
    }
}
