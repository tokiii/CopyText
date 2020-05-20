package com.example.copytext;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.lzf.easyfloat.EasyFloat;
import com.lzf.easyfloat.anim.AppFloatDefaultAnimator;
import com.lzf.easyfloat.anim.DefaultAnimator;
import com.lzf.easyfloat.enums.ShowPattern;
import com.lzf.easyfloat.enums.SidePattern;
import com.lzf.easyfloat.interfaces.OnDisplayHeight;
import com.lzf.easyfloat.interfaces.OnFloatCallbacks;
import com.lzf.easyfloat.interfaces.OnInvokeView;
import com.lzf.easyfloat.utils.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnCopy;
    Button btnClear;
    EditText etCopy;
    Button btnOpenDialog;
    private int times;

    TextView tvPopCopy;
    TextView tvPopTimes ;
    TextView tvPopAdd ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnCopy = findViewById(R.id.btn_copy);
        btnClear = findViewById(R.id.btn_clear);
        etCopy = findViewById(R.id.et_copy);
        btnOpenDialog = findViewById(R.id.btn_open_dialog);

        btnCopy.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        btnOpenDialog.setOnClickListener(this);
    }


    private void openDialog() {
        EasyFloat.with(this)
                // 设置浮窗xml布局文件
                .setLayout(R.layout.float_app, view -> {
                    // view就是我们传入的浮窗xml布局
                     tvPopCopy = view.findViewById(R.id.tv_pop_copy);
                     tvPopTimes = view.findViewById(R.id.tv_pop_times);
                     tvPopAdd = view.findViewById(R.id.tv_pop_add);

                    tvPopCopy.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            copyStr();
                        }
                    });

                    tvPopAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (AntiShakeUtil.check(view,MainActivity.this)){
                                return;
                            }
                            times++;
                            tvPopTimes.setText(times + "次");
                        }
                    });
                })
                // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示
                .setShowPattern(ShowPattern.ALL_TIME)
                // 设置吸附方式，共15种模式，详情参考SidePattern
                .setSidePattern(SidePattern.RESULT_HORIZONTAL)
//                // 设置浮窗的标签，用于区分多个浮窗
                .setTag("copyDialog")
//                // 设置浮窗是否可拖拽
                .setDragEnable(true)
//                // 系统浮窗是否包含EditText，仅针对系统浮窗，默认不包含
                .hasEditText(false)
//                // 设置浮窗固定坐标，ps：设置固定坐标，Gravity属性和offset属性将无效
//                .setLocation(100, 200)
//                // 设置浮窗的对齐方式和坐标偏移量
//                .setGravity(Gravity.END | Gravity.CENTER_VERTICAL, 0, 200)
//                // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
//                .setMatchParent(false, false)
//                // 设置Activity浮窗的出入动画，可自定义，实现相应接口即可（策略模式），无需动画直接设置为null
                .setAnimator(new DefaultAnimator())
//                // 设置系统浮窗的出入动画，使用同上
                .setAppFloatAnimator(new AppFloatDefaultAnimator())
//                // 设置系统浮窗的不需要显示的页面
//                .setFilter(MainActivity.class)
//                // 设置系统浮窗的有效显示高度（不包含虚拟导航栏的高度），基本用不到，除非有虚拟导航栏适配问题
//                .setDisplayHeight(DisplayUtils.INSTANCE::rejectedNavHeight)
//                // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
                .registerCallbacks(new OnFloatCallbacks() {
                    @Override
                    public void createdResult(boolean isCreated, @Nullable String msg, @Nullable View view) {
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
                        Log.d("copyText", "是否被创建---->" + isCreated + " 信息--->" + msg);
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
                    public void touchEvent(@NotNull View view, @NotNull MotionEvent event) {

                    }

                    @Override
                    public void drag(@NotNull View view, @NotNull MotionEvent event) {

                    }

                    @Override
                    public void dragEnd(@NotNull View view) {

                    }
                })
//                // 创建浮窗（这是关键哦😂）
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_clear:
                etCopy.setText("");
                times = 0;
                if (tvPopTimes != null){
                    tvPopTimes.setText("0次");
                }
                break;
            case R.id.btn_copy:
                copyStr();
                break;
            case R.id.btn_open_dialog:
                openDialog();
                break;
        }
    }


    private void copyStr(){
        String str = etCopy.getText().toString();
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", str);
// 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(this, "复制成功", Toast.LENGTH_SHORT).show();
    }
}
