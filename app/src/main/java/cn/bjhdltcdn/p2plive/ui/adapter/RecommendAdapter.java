package cn.bjhdltcdn.p2plive.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;


public class RecommendAdapter extends BaseAdapter {
    private Context context; //到QQ空间
    private String[] titleString_recommend = new String[]{"推荐给微信好友", "推荐到朋友圈", "推荐给QQ好友", "推荐到QQ空间"};
    private String[] titleString_aboutus = new String[]{"用户协议", "版本更新", "给友伴评分"};
    private String type;//“aboutus:关于我们页面 ； recommend:推荐Vmeet唯觅 页面”

    public RecommendAdapter(String type) {
        // TODO Auto-generated constructor stub
        this.context = App.getInstance();
        this.type = type;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (type.equals("aboutus")) {
            return titleString_aboutus.length;
        } else {
            return titleString_recommend.length;
        }

    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.set_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.itemTitle = convertView.findViewById(R.id.text_set_item_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (type.equals("aboutus")) {
            viewHolder.itemTitle.setText(titleString_aboutus[position]);
        } else {
            viewHolder.itemTitle.setText(titleString_recommend[position]);
        }
        return convertView;
    }

    class ViewHolder {
        TextView itemTitle;
    }

}
