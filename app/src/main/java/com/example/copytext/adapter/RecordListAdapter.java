package com.example.copytext.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.copytext.R;
import com.example.copytext.RecordBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.List;

public class RecordListAdapter extends BaseQuickAdapter<RecordBean, BaseViewHolder> {
    public RecordListAdapter(int layoutResId, @Nullable List<RecordBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, RecordBean recordBean) {
        baseViewHolder.setText(R.id.tv_item_copy_str, recordBean.getCopyStr());
        baseViewHolder.setText(R.id.tv_item_times, recordBean.getAddTimes() + "次");
        baseViewHolder.setText(R.id.tv_item_update_time, recordBean.getTime());
    }
}
