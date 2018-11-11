package cn.bjhdltcdn.p2plive.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.ReportType;


/**
 * Created by huwenhua on 2016/1/22.
 */
public class ReportContentDialogAdapter extends BaseAdapter {
    private List<ReportType> reprotTypeList;

    public ReportContentDialogAdapter(List<ReportType> reprotTypeList) {
        this.reprotTypeList = reprotTypeList;
    }

    @Override
    public int getCount() {
        return reprotTypeList != null ? reprotTypeList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return (reprotTypeList != null && reprotTypeList.size() > 0) ? reprotTypeList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.report_list_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.itemTitle = (TextView) convertView.findViewById(R.id.text_report_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (getCount() > 0) {
            Object object = getItem(position);
            if (object instanceof ReportType) {
                ReportType reportType = (ReportType) object;
                viewHolder.itemTitle.setText(reportType.getReportName());
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView itemTitle;
    }
}
