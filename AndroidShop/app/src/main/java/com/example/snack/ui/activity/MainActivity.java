package com.example.snack.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.snack.MyApplication;
import com.example.snack.R;
import com.example.snack.ui.fragment.SnackFragment;
import com.example.snack.ui.fragment.CarFragment;
import com.example.snack.ui.fragment.UserFragment;
import com.example.snack.ui.fragment.OrderFragment;
import com.example.snack.util.SPUtils;
import com.example.snack.widget.ActionBar;


/**
 * 主页面
 */
public class MainActivity extends Activity {
    private ActionBar mActionBar;//标题栏
    private Activity myActivity;
    private LinearLayout llContent;
    private RadioButton rbSnack;
    private RadioButton rbSnackData;
    private RadioButton rbUserManage;
    private RadioButton rbUser;
    private Fragment[] fragments = new Fragment[]{null, null,null,null};//存放Fragment
    private Boolean mIsAdmin;//是否管理员
    private String mAccount;//账号

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_main);
        MyApplication.Instance.setMainActivity(myActivity);
        mIsAdmin = (Boolean) SPUtils.get(myActivity, SPUtils.IS_ADMIN,false);
        llContent =  (LinearLayout) findViewById(R.id.ll_main_content);
        rbSnack = (RadioButton) findViewById(R.id.rb_main_snack);
        rbSnackData = (RadioButton) findViewById(R.id.rb_main_snack_data);
        rbUserManage = (RadioButton) findViewById(R.id.rb_main_setting);
        rbUser = (RadioButton) findViewById(R.id.rb_main_user);
        mActionBar = (ActionBar) findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(myActivity,"零食工坊", 0, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();
        setViewListener();
    }

    private void setViewListener() {
        rbSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionBar.setTitle("零食分类");
                switchFragment(0);
            }
        });
        rbSnackData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(mAccount)) {////未登录,跳转到登录页面
                    MyApplication.Instance.getMainActivity().finish();
                    startActivity(new Intent(myActivity, LoginActivity.class));
                }else {//已经登录
                    mActionBar.setTitle("我的购物车");
                    switchFragment(1);
                }
            }
        });
        rbUserManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionBar.setTitle("所有订单");
                switchFragment(2);
            }
        });
        rbUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(mAccount)) {////未登录,跳转到登录页面
                    MyApplication.Instance.getMainActivity().finish();
                    startActivity(new Intent(myActivity, LoginActivity.class));
                }else {//已经登录
                    mActionBar.setTitle("个人中心");
                    switchFragment(3);
                }
            }
        });
    }

    private void initView() {
        mIsAdmin = (Boolean) SPUtils.get(myActivity,SPUtils.IS_ADMIN,false);
        mAccount = (String) SPUtils.get(myActivity,SPUtils.ACCOUNT,"");
        //设置导航栏图标样式
        Drawable iconNews=getResources().getDrawable(R.drawable.selector_main_rb_home);//设置主页图标样式
        iconNews.setBounds(0,0,68,68);//设置图标边距 大小
        rbSnack.setCompoundDrawables(null,iconNews,null,null);//设置图标位置
        rbSnack.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconNewsData=getResources().getDrawable(R.drawable.selector_main_rb_buy);//设置主页图标样式
        iconNewsData.setBounds(0,0,68,68);//设置图标边距 大小
        rbSnackData.setCompoundDrawables(null,iconNewsData,null,null);//设置图标位置
        rbSnackData.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconSetting=getResources().getDrawable(R.drawable.selector_main_rb_manage);//设置主页图标样式
        iconSetting.setBounds(0,0,60,60);//设置图标边距 大小
        rbUserManage.setCompoundDrawables(null,iconSetting,null,null);//设置图标位置
        rbUserManage.setCompoundDrawablePadding(5);//设置文字与图片的间距
        Drawable iconUser=getResources().getDrawable(R.drawable.selector_main_rb_user);//设置主页图标样式
        iconUser.setBounds(0,0,55,55);//设置图标边距 大小
        rbUser.setCompoundDrawables(null,iconUser,null,null);//设置图标位置
        rbUser.setCompoundDrawablePadding(5);//设置文字与图片的间距
        rbUserManage.setVisibility(mIsAdmin? View.VISIBLE: View.GONE);
        switchFragment(0);
        rbSnack.setChecked(true);
        rbSnackData.setVisibility(mIsAdmin? View.GONE: View.VISIBLE);
        switchFragment(0);
        rbSnack.setChecked(true);
    }
    /**
     * 方法 - 切换Fragment
     *
     * @param fragmentIndex 要显示Fragment的索引
     */
    private void switchFragment(int fragmentIndex) {
        //在Activity中显示Fragment
        //1、获取Fragment管理器 FragmentManager
        FragmentManager fragmentManager = this.getFragmentManager();
        //2、开启fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //懒加载 - 如果需要显示的Fragment为null时，就new。并添加到Fragment务中
        if (fragments[fragmentIndex] == null) {
            if (mIsAdmin){
                switch (fragmentIndex) {
                    case 0://NewsFragment
                        fragments[fragmentIndex] = new SnackFragment();
                        break;
                    case 1://CollectFragment
                        fragments[fragmentIndex] = new CarFragment();
                        break;
                    case 2://UserManageFragment
                        fragments[fragmentIndex] = new OrderFragment();
                        break;
                    case 3://UserFragment
                        fragments[fragmentIndex] = new UserFragment();
                        break;
                }
            }else {
                switch (fragmentIndex) {
                    case 0://NewsFragment
                        fragments[fragmentIndex] = new SnackFragment();
                        break;
                    case 1://CollectFragment
                        fragments[fragmentIndex] = new CarFragment();
                        break;
                    case 2://UserManageFragment
                        fragments[fragmentIndex] = new OrderFragment();
                        break;
                    case 3://UserFragment
                        fragments[fragmentIndex] = new UserFragment();
                        break;
                }
            }

            //==添加Fragment对象到Fragment事务中
            //参数：显示Fragment的容器的ID，Fragment对象
            transaction.add(R.id.ll_main_content, fragments[fragmentIndex]);
        }

        //隐藏其他的Fragment
        for (int i = 0; i < fragments.length; i++) {
            if (fragmentIndex != i && fragments[i] != null) {
                //隐藏指定的Fragment
                transaction.hide(fragments[i]);
            }
        }
        //4、显示Fragment
        transaction.show(fragments[fragmentIndex]);

        //5、提交事务
        transaction.commit();
    }
    /**
     * 双击退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
        }

        return false;
    }

    private long time = 0;

    public void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(myActivity,"再点击一次退出应用程序", Toast.LENGTH_LONG).show();
        } else {
            finish();
        }
    }
}
