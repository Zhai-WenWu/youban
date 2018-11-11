package cn.bjhdltcdn.p2plive.ui.adapter;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.Props;
import cn.bjhdltcdn.p2plive.utils.Utils;

public class PropsGridAdapter extends BaseAdapter {
    private List<Props> list;

    private int width;

    public PropsGridAdapter() {
        DisplayMetrics displayMetrics = App.getInstance().getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
    }

    @Override
    public int getCount() {
        return list != null && list.size() > 0 ? list.size() : 0;
    }

    public void setList(List<Props> list) {
        this.list = list;
    }

    @Override
    public Props getItem(int position) {
        return list != null && list.size() > 0 ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @return the mList
     */
    public List<Props> getList() {
        return list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(App.getInstance()).inflate(R.layout.props_grid_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.propsImageView = (ImageView) convertView.findViewById(R.id.props_imge_view);
            viewHolder.propsTypeView = (ImageView) convertView.findViewById(R.id.props_imge_type_view);
            ViewGroup.LayoutParams layoutParams = viewHolder.propsImageView.getLayoutParams();
//            if (layoutParams == null) {
//                layoutParams = new ViewGroup.LayoutParams(width / 4, width / 4);
//            } else {
//                layoutParams.width = width / 4;
//                layoutParams.height = width / 4;
//            }

            viewHolder.propsImageView.setLayoutParams(layoutParams);

            viewHolder.propsNameView = (TextView) convertView.findViewById(R.id.props_name_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null && list.size() > 0) {
            try {
                Props props = list.get(position);
                if (props != null) {
                    String imageUrl = props.getUrl();
                    Integer price = props.getPrice();
                    Integer salePrice = props.getSalePrice();
                    Utils.ImageViewDisplayByUrl(imageUrl, viewHolder.propsImageView);

//                    if (props.getIsSale() == 1) {
//                        viewHolder.propsTypeView.setImageResource(R.drawable.props_type_zhe);
//                        viewHolder.propsTypeView.setVisibility(View.VISIBLE);
//                        viewHolder.propsNameView.setText(salePrice + "金币");
//                    } else {
//                        if (props.getType() == 9) {
//                            viewHolder.propsNameView.setText("金币红包");
//                        } else {
//                            if (props.getPropsId() == 52) {
//                                viewHolder.propsNameView.setText("邀客豆");
//                            } else {
                                viewHolder.propsNameView.setText(price + "金币");
//                            }
//                        }
//                    }

//                    if (props.getIsNew() == 1) {
//                        viewHolder.propsTypeView.setImageResource(R.drawable.props_type_xin);
//                        viewHolder.propsTypeView.setVisibility(View.VISIBLE);
//                    }

//                    if (props.getIsSale() == 0 && props.getIsNew() == 0) {
//                        viewHolder.propsTypeView.setVisibility(View.GONE);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return convertView;
    }

    class ViewHolder {
        ImageView propsImageView, propsTypeView;
        TextView propsNameView;
    }


}
