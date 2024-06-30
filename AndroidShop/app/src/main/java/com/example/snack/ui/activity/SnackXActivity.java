package com.example.snack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.snack.R;
import com.example.snack.bean.Snack;
import com.example.snack.util.SPUtils;
import com.example.snack.widget.ActionBar;

import java.text.SimpleDateFormat;


/**
 * 订单明细信息
 */
public class SnackXActivity extends AppCompatActivity {
    private Activity mActivity;
    private ImageView ivImg;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvContent;
    private TextView tvIssuer;
    private Button btnCollect;
    private Button btnCancel;
    private ActionBar mActionBar;//标题栏
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_snack_x);
        ivImg = findViewById(R.id.img);
        tvTitle= findViewById(R.id.title);
        tvDate = findViewById(R.id.date);
        tvContent = findViewById(R.id.content);
        tvIssuer = findViewById(R.id.issuer);
        btnCollect = findViewById(R.id.btn_collect);
        btnCancel = findViewById(R.id.btn_cancel);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"查看详情", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        Snack snack = (Snack) getIntent().getSerializableExtra("snack");
        tvTitle.setText(snack.getTitle());
        tvDate.setText(String.format("上架时间:%s",snack.getDate()));
        tvContent.setText(snack.getContent());
        tvIssuer.setText(String.format("￥ %s",snack.getIssuer()));
        Glide.with(mActivity)
                .asBitmap()
                .skipMemoryCache(true)
                .load(snack.getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg);
        String account = (String) SPUtils.get(mActivity,SPUtils.ACCOUNT,"");

    }
}
