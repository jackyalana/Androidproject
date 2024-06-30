package com.example.snack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.snack.R;
import com.example.snack.bean.Browse;
import com.example.snack.bean.Car;
import com.example.snack.bean.Snack;
import com.example.snack.util.SPUtils;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 水果明细信息
 */
public class SnackDetailActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_snack_detail);
        ivImg = findViewById(R.id.img);
        tvTitle= findViewById(R.id.title);
        tvDate = findViewById(R.id.date);
        tvContent = findViewById(R.id.content);
        tvIssuer = findViewById(R.id.issuer);
        btnCollect = findViewById(R.id.btn_collect);
        btnCancel = findViewById(R.id.btn_cancel);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"商品详情", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
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
        //浏览记录
        Browse browse = DataSupport.where("account = ? and title = ?",account,snack.getTitle()).findFirst(Browse.class);//浏览记录
        if (browse == null) {//不存在该条浏览记录  新增记录
            Browse browse1 = new Browse(account,snack.getTitle());
            browse1.save();
        }

        Boolean isAdmin = (Boolean) SPUtils.get(mActivity,SPUtils.IS_ADMIN,false);
        if (!isAdmin){
            Car order = DataSupport.where("account = ? and title = ?",account,snack.getTitle()).findFirst(Car.class);
            btnCollect.setVisibility(order!=null?View.GONE:View.VISIBLE);
            btnCancel.setVisibility(order!=null?View.VISIBLE:View.GONE);
        }
        //收藏
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car = new Car(snack.getIssuer(),account,snack.getTitle(),"S"+System.currentTimeMillis(),account,sf.format(new Date()));
                car.save();
                Toast.makeText(mActivity,"加入购物车成功", Toast.LENGTH_SHORT).show();
                btnCollect.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });
        //取消收藏
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car order = DataSupport.where("account = ? and title = ?",account,snack.getTitle()).findFirst(Car.class);
                order.delete();
                Toast.makeText(mActivity,"已从购物车移除", Toast.LENGTH_SHORT).show();
                btnCollect.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
            }
        });
    }
}
