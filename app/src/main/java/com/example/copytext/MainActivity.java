package com.example.copytext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.anim.AppFloatDefaultAnimator;
import com.lzf.easyfloat.anim.DefaultAnimator;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCopy;
    Button btnClear;
    EditText etCopy;
    Button btnOpenDialog;
    private int times;
    TextView tvPopCopy;
    TextView tvPopTimes;
    TextView tvPopAdd;
    RecordBean recordBean;// 实体
    private boolean isCopy;
    Button btnRecord;
    boolean isDialogCreate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCopy = findViewById(R.id.btn_copy);
        btnClear = findViewById(R.id.btn_clear);
        etCopy = findViewById(R.id.et_copy);
        btnOpenDialog = findViewById(R.id.btn_open_dialog);
        btnRecord = findViewById(R.id.btn_record);
        btnRecord.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnOpenDialog.setOnClickListener(this);
        etCopy.setText(SPUtils.getInstance().getString("copyStr", ""));
    }

    private void createDialog() {
        EasyFloat.with(this)
                // 设置浮窗xml布局文件
                .setLayout(R.layout.float_app, view -> {
                    // view就是我们传入的浮窗xml布局
                    tvPopCopy = view.findViewById(R.id.tv_pop_copy);
                    tvPopTimes = view.findViewById(R.id.tv_pop_times);
                    tvPopAdd = view.findViewById(R.id.tv_pop_add);
                    tvPopCopy.setOnClickListener(v -> copyStr());
                    TextView tvHidePop = view.findViewById(R.id.tv_hide_pop);
                    tvHidePop.setOnClickListener(v -> hidePop());
                    tvPopAdd.setOnClickListener(v -> {
                        if (!isCopy || TextUtils.isEmpty(etCopy.getText())) {
                            ToastUtils.showShort("请先复制");
                            return;
                        }
                        if (recordBean == null)
                            recordBean = new RecordBean();
                        if (AntiShakeUtil.check(view, MainActivity.this)) {
                            return;
                        }
                        times++;
                        tvPopTimes.setText(times + "次");
                        recordBean.setCopyStr(etCopy.getText().toString());
                        recordBean.setAddTimes(times);
                        recordBean.setTime(TimeUtils.getNowString());
                        recordBean.save();

                    });
                })
                .setShowPattern(ShowPattern.ALL_TIME)
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
                .setTag("copyDialog")
                .setDragEnable(true)
                .hasEditText(false)
                .setAnimator(new DefaultAnimator())
                .setAppFloatAnimator(new AppFloatDefaultAnimator())
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean b, @org.jetbrains.annotations.Nullable String s, @org.jetbrains.annotations.Nullable View view) {
                        LogUtils.d("弹窗创建---->" + s);
                    }

                    @Override
                    public void show(@NotNull View view) {

                    }

                    @Override
                    public void hide(@NotNull View view) {

                    }

                    @Override
                    public void dismiss() {

                    }

                    @Override
                    public void touchEvent(@NotNull View view, @NotNull MotionEvent motionEvent) {

                    }

                    @Override
                    public void drag(@NotNull View view, @NotNull MotionEvent motionEvent) {

                    }

                    @Override
                    public void dragEnd(@NotNull View view) {

                    }
                }).show();
        isDialogCreate = true;
    }

    /**
     * 打开悬浮窗
     */
    private void openDialog() {
        if (!isDialogCreate) {
            createDialog();
        } else {
            EasyFloat.showAppFloat("copyDialog");
        }

    }

    private void hidePop() {
        EasyFloat.hideAppFloat("copyDialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clear:
                isCopy = false;
                recordBean = null;// 重置实体
                etCopy.setText("");
                SPUtils.getInstance().clear();
                times = 0;
                if (tvPopTimes != null) {
                    tvPopTimes.setText("0次");
                }
                break;
            case R.id.btn_copy:
                copyStr();
                break;
            case R.id.btn_open_dialog:
                openDialog();
                break;
            case R.id.btn_record:
                ActivityUtils.startActivity(new Intent(MainActivity.this, RecordActivity.class));
                break;
        }
    }

    private void copyStr() {
        if (TextUtils.isEmpty(etCopy.getText().toString())) {
            ToastUtils.showShort("请填写需要复制的内容");
            return;
        }
        String str = etCopy.getText().toString();
        SPUtils.getInstance().put("copyStr", str);

        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
        isCopy = true;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
