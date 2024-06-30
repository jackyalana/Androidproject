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
import com.example.snack.bean.Car;
import com.example.snack.bean.Snack;
import com.example.snack.bean.Orders;
import com.example.snack.util.SPUtils;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;
import android.widget.RadioButton;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 结算商品
 */
public class SnackYActivity extends AppCompatActivity {
    private Activity mActivity;
    private ImageView ivImg;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvIssuer;
    private Button btnCollect;
    private Button btnCancel;
    private ActionBar mActionBar;//标题栏
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_snack_y);
        ivImg = findViewById(R.id.img);
        tvTitle= findViewById(R.id.title);
        tvDate = findViewById(R.id.date);
        tvIssuer = findViewById(R.id.issuer);
        btnCollect = findViewById(R.id.btn_collect);
        btnCancel = findViewById(R.id.btn_cancel);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(mActivity,"结算商品", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        final RadioButton rab_weixin = (RadioButton)findViewById(R.id.rab_wenxin);
        final RadioButton rab_zhifubao = (RadioButton)findViewById(R.id.rab_zhifubao);
        final RadioButton rab_yinhangka = (RadioButton)findViewById(R.id.rab_yinhangka);
        final TextView tv_zf = (TextView)findViewById(R.id.tv_zf);

        View.OnClickListener a = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("您选择的支付方式是："+"\n"+"       微信支付");
            }
        };
        View.OnClickListener b = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("您选择的支付方式是："+"\n"+"       支付宝支付");
            }
        };

        View.OnClickListener c = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_zf.setText("您选择的支付方式是："+"\n "+"      银行卡支付");

            }
        };
        //3、将两个监听器分别配置给两个按钮
        rab_weixin.setOnClickListener(a);
        rab_zhifubao.setOnClickListener(b);
        rab_yinhangka.setOnClickListener(c);


        Snack snack = (Snack) getIntent().getSerializableExtra("snack");
        tvTitle.setText(snack.getTitle());
        tvDate.setText(String.format("上架时间:%s",snack.getDate()));
        tvIssuer.setText(String.format("￥ %s",snack.getIssuer()));
        Glide.with(mActivity)
                .asBitmap()
                .skipMemoryCache(true)
                .load(snack.getImg())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivImg);
        String account = (String) SPUtils.get(mActivity,SPUtils.ACCOUNT,"");
        Boolean isAdmin = (Boolean) SPUtils.get(mActivity,SPUtils.IS_ADMIN,false);
        if (!isAdmin){
            Car car = DataSupport.where("account = ? and title = ?",account,snack.getTitle()).findFirst(Car.class);
            btnCollect.setVisibility(car!=null?View.VISIBLE:View.GONE);
            btnCancel.setVisibility(car!=null?View.GONE:View.VISIBLE);
        }

        //支付
        btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (!rab_yinhangka.isChecked()&&!rab_zhifubao.isChecked()&&!rab_weixin.isChecked()) {
                   Toast.makeText(mActivity, "请选择支付方式!", Toast.LENGTH_LONG).show();
                   return;
               }
                Orders orders = new Orders(snack.getIssuer(),account,snack.getTitle(),"S"+System.currentTimeMillis(),account,sf.format(new Date()));
                orders.save();
                Car car = DataSupport.where("account = ? and title = ?",account,snack.getTitle()).findFirst(Car.class);
                car.delete();
               // Intent intent = new Intent(mActivity, JsActivity.class);
               // startActivity(intent);
                Toast.makeText(mActivity,"支付成功,请勿重复支付！", Toast.LENGTH_SHORT).show();

                btnCollect.setVisibility(View.GONE);
                btnCancel.setVisibility(View.VISIBLE);
            }
        });
        //已支付
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mActivity,"该商品已支付，请在我的订单中查看!", Toast.LENGTH_SHORT).show();

                btnCollect.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
            }
        });
    }
}
