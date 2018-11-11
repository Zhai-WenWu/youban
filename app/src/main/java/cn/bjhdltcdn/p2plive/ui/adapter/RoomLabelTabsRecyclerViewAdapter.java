package cn.bjhdltcdn.p2plive.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

import cn.bjhdltcdn.p2plive.R;
import cn.bjhdltcdn.p2plive.app.App;
import cn.bjhdltcdn.p2plive.model.LabelInfo;
import cn.bjhdltcdn.p2plive.ui.viewholder.BaseViewHolder;

/**
 * 房间标签列表
 */

public class RoomLabelTabsRecyclerViewAdapter extends BaseRecyclerAdapter {
    private List<LabelInfo> list;
    /**
     * 选择的标签列表
     */
    private List<LabelInfo> selectLabelList;
    private List<Long> selectLabelInfoList;
    private List<String> selectLabelStrs;
    private int isSelectCount;
    private OnClickListener onClickListener;

    /**
     * 1 房间，2 一键发布
     */
    private int type;

    public RoomLabelTabsRecyclerViewAdapter(int type) {
        list = new ArrayList<>(1);
        this.type = type;
        selectLabelInfoList = new ArrayList<>(1);
        selectLabelStrs = new ArrayList<>(1);
    }

    public void setList(List<LabelInfo> list) {
        this.list = list;
        if (list != null) {
            this.list = list;
        }
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public List<LabelInfo> getList() {
        return list;
    }


    /**
     * 获取选中的数量
     *
     * @return
     */
    public int getSelectItemCount() {
        isSelectCount = 0;
        if (getItemCount() > 0) {
            for (LabelInfo info : list) {
                if (info.getIsSelect() == 1) {
                    isSelectCount++;
                }
            }
        }
        return isSelectCount;
    }

    /**
     * 已选择的标签
     *
     * @return
     */
    public List<LabelInfo> getSelectLabelList() {
        if (selectLabelList == null) {
            selectLabelList = new ArrayList<>(1);
        } else {
            selectLabelList.clear();
        }
        if (getItemCount() > 0) {
            for (LabelInfo info : list) {
                if (info.getIsSelect() == 1) {
                    selectLabelList.add(info);
                }
            }
        }
        return selectLabelList;
    }

    public LabelInfo getItem(int position) {
        return list == null ? null : list.get(position);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        ImageHolder holder = null;
        if (type == 1) {
            holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.room_label_tab_item_layout, null));
        } else if (type == 2) {
            holder = new ImageHolder(LayoutInflater.from(App.getInstance()).inflate(R.layout.public_label_tab_item_layout, null));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        if (getItemCount() > 0) {
            if (holder instanceof ImageHolder) {
                final ImageHolder imageHolder = (ImageHolder) holder;
                final CheckBox tabTextView = imageHolder.tabTextView;

                final LabelInfo keywordInfo = list.get(position);

                String labelName = keywordInfo.getLabelName();
                tabTextView.setText(labelName);

                if (keywordInfo.getIsSelect() == 1) {
                    tabTextView.setChecked(true);
                } else {
                    tabTextView.setChecked(false);
                }

                if (type == 1) {
                    tabTextView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                tabTextView.setChecked(true);
                                keywordInfo.setIsSelect(1);

                                if (onClickListener != null) {
                                    onClickListener.onClick(position);
                                }

                            } else {
                                tabTextView.setChecked(false);
                                keywordInfo.setIsSelect(0);
                            }


                        }
                    });
                } else {

                    tabTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onClickListener != null) {
                                keywordInfo.setIsSelect(keywordInfo.getIsSelect() == 1 ? 0 : 1);
                                onClickListener.onClick(position);
                            }
                        }
                    });

                }


            }
        }

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ImageHolder extends BaseViewHolder {
        CheckBox tabTextView;

        public ImageHolder(View itemView) {
            super(itemView);
            tabTextView = (CheckBox) itemView.findViewById(R.id.tv_label);
        }

    }

    public List<Long> getSelectLabelInfoList() {
        selectLabelInfoList.clear();
        for (int i = 0; i < list.size(); i++) {
            LabelInfo keywordInfo = list.get(i);
            if (keywordInfo.getIsSelect() == 1) {
                selectLabelInfoList.add(keywordInfo.getLabelId());
            }
        }
        return selectLabelInfoList;
    }

    public List<String> getSelectLabelStrs() {
        selectLabelStrs.clear();
        for (int i = 0; i < list.size(); i++) {
            LabelInfo keywordInfo = list.get(i);
            if (keywordInfo.getIsSelect() == 1) {
                selectLabelStrs.add(keywordInfo.getLabelName());
            }
        }
        return selectLabelStrs;
    }

    public void onDestroy() {
        if (list != null) {
            list.clear();
        }
        list = null;
    }

    public interface OnClickListener {
        void onClick(int position);
    }


}