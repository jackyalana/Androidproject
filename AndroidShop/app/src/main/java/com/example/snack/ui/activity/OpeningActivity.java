package com.example.snack.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.snack.bean.Snack;
import com.example.snack.bean.User;
import com.example.snack.util.SPUtils;
import com.example.snack.util.StatusBarUtil;
import com.example.snack.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 开屏页面
 */
public class OpeningActivity extends AppCompatActivity {
    private Activity myActivity;
    private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        //设置页面布局
        setContentView(R.layout.activity_opening);
        try {
            initView();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }
    private void initView() throws IOException, JSONException {
        StatusBarUtil.setStatusBar(myActivity,true);//设置当前界面是否是全屏模式（状态栏）
        StatusBarUtil.setStatusBarLightMode(myActivity,true);//状态栏文字颜色
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
                    finish();
                    return;
                }
                Boolean isFirst= (Boolean) SPUtils.get(myActivity,SPUtils.IF_FIRST,true);
                String account= (String) SPUtils.get(myActivity,SPUtils.ACCOUNT,"");
                if (isFirst){//第一次进来  初始化本地数据
                    SPUtils.put(myActivity,SPUtils.IF_FIRST,false);//第一次
                    //初始化数据
                    //获取json数据
                    String rewardJson = "";
                    String rewardJsonLine;
                    //assets文件夹下db.json文件的路径->打开db.json文件
                    BufferedReader bufferedReader = null;
                    try {
                        bufferedReader = new BufferedReader(new InputStreamReader(myActivity.getAssets().open("db.json")));
                        while (true) {
                            if (!((rewardJsonLine = bufferedReader.readLine()) != null)) break;
                            rewardJson += rewardJsonLine;
                        }
                        JSONObject jsonObject = new JSONObject(rewardJson);
                        JSONArray snackList = jsonObject.getJSONArray("snack");//获得列表
                        //把物品列表保存到本地
                        for (int i = 0, length = snackList.length(); i < length; i++) {
                            JSONObject o = snackList.getJSONObject(i);
                                            Snack snack = new Snack(o.getInt("typeId"),
                                                    o.getString("title"),
                                                    o.getString("img"),
                                                    o.getString("content"),
                                                    o.getString("issuer"),
                                                    sf.format(new Date())
                                            );
                            snack.save();//保存到本地
                        }
                        User user = new User("admin","root","管理员","19266668888","广东省江门市蓬江区");
                        user.save();
                        User user1 = new User("312","312","Michael","13866662222","广西省南宁市西乡塘区");
                        user1.save();
                        User user2 = new User("313","313","Amy","13866668888","江西省南昌市南昌酒店");
                        user2.save();
                        User user3 = new User("314","314","Anni","17866662222","上海市浦东大酒店");
                        user3.save();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
                //两秒后跳转到主页面
                Intent intent2 = new Intent();
                if ("".equals(account)) {
                    intent2.setClass(OpeningActivity.this, MainActivity.class);
                }else {
                    intent2.setClass(OpeningActivity.this, MainActivity.class);
                }
                startActivity(intent2);
                finish();
            }
        }, 2000);
    }


    @Override
    public void onBackPressed() {

    }
}
