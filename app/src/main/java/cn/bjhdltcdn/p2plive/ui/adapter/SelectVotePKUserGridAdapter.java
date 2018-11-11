package cn.bjhdltcdn.p2plive.ui.adapter;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.VoteRecord;
import cn.bjhdltcdn.p2plive.utils.Utils;


public class SelectVotePKUserGridAdapter extends BaseAdapter {
    private List<VoteRecord> list;

    public SelectVotePKUserGridAdapter() {
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }
    }


    public void setList(List<VoteRecord> list) {
        this.list = list;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.size() > 0) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * @return the mList
     */
    public List<VoteRecord> getList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.select_vote_user_grid_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.itemHead = (ImageView) convertView.findViewById(R.id.vote_user_img_view);
            viewHolder.itemBorder = (ImageView) convertView.findViewById(R.id.vote_user_img_border_view);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.vote_user_name_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list.get(position) != null) {
            final VoteRecord voteRecord = list.get(position);
            Utils.ImageViewDisplayByUrl(voteRecord.getUserIcon(), viewHolder.itemHead);
            viewHolder.itemBorder.setVisibility(View.VISIBLE);
            if (voteRecord.getVoteRanking() == 1 || voteRecord.getVoteRanking() == 2) {
                viewHolder.itemBorder.setImageResource(R.drawable.vote_user_red_border);
            } else if (voteRecord.getVoteRanking() == 3 || voteRecord.getVoteRanking() == 4) {
                viewHolder.itemBorder.setImageResource(R.drawable.vote_user_orange_border);
            } else {
                viewHolder.itemBorder.setImageResource(R.drawable.vote_user_yellow_border);
            }
            SpannableStringBuilder textStyle = new SpannableStringBuilder();
            textStyle.append((position + 1) + "号" + voteRecord.getNickName());
            textStyle.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 218, 68)), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.itemName.setText(textStyle);
        }


        return convertView;
    }

    class ViewHolder {
        ImageView itemHead, itemBorder;
        TextView itemName;
    }

}
