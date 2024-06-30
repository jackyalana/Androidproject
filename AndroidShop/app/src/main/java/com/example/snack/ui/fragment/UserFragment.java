package com.example.snack.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.snack.ui.activity.BrowseActivity;
import com.example.snack.ui.activity.ManageActivity;
import com.example.snack.MyApplication;
import com.example.snack.ui.activity.OrderActivity;
import com.example.snack.R;
import com.example.snack.ui.activity.LoginActivity;
import com.example.snack.ui.activity.PasswordActivity;
import com.example.snack.ui.activity.PersonActivity;
import com.example.snack.util.SPUtils;

/**
 * 个人中心
 */
public class UserFragment extends Fragment {
    private Activity mActivity;
    private LinearLayout llPerson;
    private LinearLayout llSecurity;

    private LinearLayout llFavorite;
    private LinearLayout llBrowse;
    private LinearLayout order;
    private LinearLayout manage;
    private Button btnLogout;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user,container,false);
        llPerson = view.findViewById(R.id.person);
        llSecurity = view.findViewById(R.id.security);
        llBrowse = view.findViewById(R.id.browse);
        order = view.findViewById(R.id.order);
        manage = view.findViewById(R.id.manage);
        btnLogout = view.findViewById(R.id.logout);
        llFavorite = view.findViewById(R.id.favorite);
        initView();
        return view;
    }

    private void initView() {
        Boolean isAdmin = (Boolean) SPUtils.get(mActivity,SPUtils.IS_ADMIN,false);

        llFavorite.setVisibility(isAdmin?View.GONE:View.VISIBLE);
        order.setVisibility(isAdmin?View.GONE:View.VISIBLE);
        llBrowse.setVisibility(isAdmin?View.GONE:View.VISIBLE);
        manage.setVisibility(isAdmin?View.VISIBLE:View.GONE);
        //个人信息
        llPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PersonActivity.class);
                startActivity(intent);
            }
        });
        //用户管理
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, ManageActivity.class);
                startActivity(intent);
            }
        });
        //账号安全
        llSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, PasswordActivity.class);
                startActivity(intent);
            }
        });
        //浏览记录
        llBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, BrowseActivity.class);
                startActivity(intent);
            }
        });

        //我的订单
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                Intent intent = new Intent(mActivity, OrderActivity.class);
                startActivity(intent);
            }
        });
        //联系客服
        llFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转页面
                //跳转到我的资料卡
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=2737974761")));//跳转到QQ资料
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqapi://card/show_pslcard?src_type=internal&version=1&card_type=group&source=qrcode&uin=485761716")));//跳转到QQ群
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&version=1&uin=1632957243")));//跳转到临时会话
            }
        });
        //退出登录
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.Instance.getMainActivity().finish();
                SPUtils.remove(mActivity,SPUtils.IS_ADMIN);
                SPUtils.remove(mActivity,SPUtils.ACCOUNT);
                startActivity(new Intent(mActivity, LoginActivity.class));
            }
        });
    }
}
