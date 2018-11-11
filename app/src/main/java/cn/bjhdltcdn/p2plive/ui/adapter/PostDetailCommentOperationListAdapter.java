package cn.bjhdltcdn.p2plive.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;

public class PostDetailCommentOperationListAdapter extends BaseAdapter {
    private String[] strings;
    public static int mPosition;

    public PostDetailCommentOperationListAdapter() {
    }

    public void setStrings(String[] strings) {
        this.strings = strings;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return strings != null ? strings.length : 0;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return strings[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.comment_operation_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.operationName = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.lineView = convertView.findViewById(R.id.divider_line);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.operationName.setText(strings[position]);
        if (position == 0) {
            viewHolder.lineView.setVisibility(View.GONE);
        } else {
            viewHolder.lineView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView operationName;
        View lineView;
    }

}
