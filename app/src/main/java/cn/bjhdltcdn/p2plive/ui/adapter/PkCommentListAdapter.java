package cn.bjhdltcdn.p2plive.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.PlayComment;
import cn.bjhdltcdn.p2plive.ui.dialog.PkCommentDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ZHAI on 2017/12/23.
 */

public class PkCommentListAdapter extends BaseAdapter {
    public List<PlayComment> mList;
    private ViewHolder viewHolder;
    PkCommentDialog mPkCommentDialog;

    public PkCommentListAdapter(PkCommentDialog pkCommentDialog) {
        this.mPkCommentDialog = pkCommentDialog;
    }

    @Override
    public int getCount() {
        return mList != null ? mList.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.adapter_video_play_commen, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_comment = convertView.findViewById(R.id.tv_comment);
            viewHolder.tv_huifu = convertView.findViewById(R.id.tv_huifu);
            viewHolder.tv_return = convertView.findViewById(R.id.tv_return);
            viewHolder.tv_detail = convertView.findViewById(R.id.tv_detail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (PkCommentListAdapter.ViewHolder) convertView.getTag();
        }

        PlayComment playComment = mList.get(position);
        if (playComment != null) {
            if (playComment.getType() == 1) {
                viewHolder.tv_huifu.setVisibility(View.INVISIBLE);
                viewHolder.tv_return.setVisibility(View.INVISIBLE);
                Glide.with(mPkCommentDialog).load(playComment.getFromBaseUser().getUserIcon()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(viewHolder.iv_icon);
                viewHolder.tv_comment.setText(playComment.getFromBaseUser().getNickName());
                viewHolder.tv_detail.setText(playComment.getContent());
            } else if (playComment.getType() == 2) {
                viewHolder.tv_huifu.setVisibility(View.VISIBLE);
                viewHolder.tv_return.setVisibility(View.VISIBLE);
                Glide.with(mPkCommentDialog).load(playComment.getToBaseUser().getUserIcon()).apply(new RequestOptions().centerCrop().placeholder(R.mipmap.error_bg)).into(viewHolder.iv_icon);
                viewHolder.tv_comment.setText(playComment.getToBaseUser().getNickName());
                viewHolder.tv_return.setText(playComment.getFromBaseUser().getNickName());
                viewHolder.tv_detail.setText(playComment.getContent());
            }

        }

        return convertView;
    }

    class ViewHolder {
        CircleImageView iv_icon;
        TextView tv_comment;
        TextView tv_huifu;
        TextView tv_return;
        TextView tv_detail;
    }


    public void setList(List<PlayComment> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public List<PlayComment> getmList() {
        return mList;
    }

    public void addList(PlayComment playComment) {
        this.mList.add(0,playComment);
        notifyDataSetChanged();
    }
}
