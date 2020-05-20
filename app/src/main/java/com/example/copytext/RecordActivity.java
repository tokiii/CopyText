package com.example.copytext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.copytext.adapter.RecordListAdapter;

import org.litepal.LitePal;

import java.util.List;

public class RecordActivity extends AppCompatActivity {

    RecyclerView rvRecord;

    List<RecordBean> recordBeans;

    RecordListAdapter recordListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        rvRecord = findViewById(R.id.rv_record);

        findViewById(R.id.toolbar_back).setOnClickListener(v -> finish());

        TextView tvTitle = findViewById(R.id.toolbar_title);
        tvTitle.setText("记录");

        rvRecord.setLayoutManager(new LinearLayoutManager(this));
        recordBeans = LitePal.findAll(RecordBean.class);
        recordListAdapter = new RecordListAdapter(R.layout.item_record_list, recordBeans);
        rvRecord.setAdapter(recordListAdapter);

        recordListAdapter.setOnItemLongClickListener((adapter, view, position) -> {
            LitePal.delete(RecordBean.class, recordBeans.get(position).getId());
            updateAdapter(position);
            return false;
        });
    }

    private void updateAdapter(int position) {
        recordBeans.remove(position);
        recordListAdapter.notifyItemRemoved(position);
    }
}
