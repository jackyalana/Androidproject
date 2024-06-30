package com.example.snack.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snack.MyApplication;
import com.example.snack.R;
import com.example.snack.adapter.SnackAdapter;
import com.example.snack.bean.Snack;
import com.example.snack.ui.activity.AddSnackActivity;
import com.example.snack.ui.activity.LoginActivity;
import com.example.snack.ui.activity.SnackDetailActivity;
import com.example.snack.util.KeyBoardUtil;
import com.example.snack.util.SPUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * 水果
 */

public class SnackFragment extends Fragment {
    private Activity myActivity;//上下文
    private TabLayout tabTitle;
    private RecyclerView rvsnackList;
    private SnackAdapter msnackAdapter;
    private LinearLayout llEmpty;
    private Boolean mIsAdmin;
    private EditText etQuery;//搜索内容
    private ImageView ivSearch;//搜索图标
    private FloatingActionButton btnAdd;
    private String[] state = {"0","1","2","3","4","5"};
    private String[] title = {"促销", "辣条","薯片" ,"饮料","糖类","轻食"};
    private String typeId = "0";
    private List<Snack> msnack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        myActivity= (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_snack,container,false);
        tabTitle = (TabLayout)view.findViewById(R.id.tab_title);
        rvsnackList = (RecyclerView)view.findViewById(R.id.rv_snack_list);
        llEmpty = view.findViewById(R.id.ll_empty);
        etQuery=view.findViewById(R.id.et_query);
        ivSearch=view.findViewById(R.id.iv_search);
        btnAdd = (FloatingActionButton)view.findViewById(R.id.btn_add);
        //获取控件
        initView();
        //软键盘搜索
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();//加载数据
            }
        });
        //点击软键盘中的搜索
        etQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtil.hideKeyboard(v);//隐藏软键盘
                    loadData();//加载数据
                    return true;
                }
                return false;
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myActivity, AddSnackActivity.class);
                startActivityForResult(intent,100);
            }
        });
        return view;
    }

    /**
     * 初始化页面
     */
    private void initView() {
        mIsAdmin = (Boolean) SPUtils.get(myActivity, SPUtils.IS_ADMIN, false);
        btnAdd.setVisibility(mIsAdmin? View.VISIBLE: View.GONE);
        tabTitle.setTabMode(TabLayout.MODE_SCROLLABLE);
        //设置tablayout距离上下左右的距离
       // tabTitle.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        for (int i=0;i<title.length;i++){
            tabTitle.addTab(tabTitle.newTab().setText(title[i]));
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(myActivity);
        //=1.2、设置为垂直排列，用setOrientation方法设置(默认为垂直布局)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //=1.3、设置recyclerView的布局管理器
        rvsnackList.setLayoutManager(layoutManager);
        //==2、实例化适配器
        //=2.1、初始化适配器
        msnackAdapter=new SnackAdapter(llEmpty,rvsnackList);
        //=2.3、设置recyclerView的适配器
        rvsnackList.setAdapter(msnackAdapter);
        loadData();
        msnackAdapter.setItemListener(new SnackAdapter.ItemListener() {
            @Override
            public void ItemClick(Snack snack) {
                boolean isAdmin = (boolean) SPUtils.get(myActivity,SPUtils.IS_ADMIN,false);
                String account = (String) SPUtils.get(myActivity,SPUtils.ACCOUNT,"");
                if ("".equals(account)) {//未登录,跳转到登录页面
                    MyApplication.Instance.getMainActivity().finish();
                    startActivity(new Intent(myActivity, LoginActivity.class));
                }else {//已经登录
                    Intent intent;
                    if (isAdmin){
                        intent = new Intent(myActivity, AddSnackActivity.class);
                    }else {
                        intent = new Intent(myActivity, SnackDetailActivity.class);
                    }
                    intent.putExtra("snack",snack);
                    startActivityForResult(intent,100);
                }
            }
        });
        tabTitle.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                typeId = state[tab.getPosition()];
                loadData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void loadData(){
        String content=etQuery.getText().toString();//获取搜索内容
        if ("".equals(content) ){
            msnack = DataSupport.where("typeId = ?",typeId).find(Snack.class);
        }else {
            msnack =DataSupport.where("typeId = ? and title like ?" ,typeId,"%"+content+"%").find(Snack.class);//通过标题模糊查询留言
        }
        if (msnack !=null && msnack.size()>0){
            rvsnackList.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.GONE);
            msnackAdapter.addItem(msnack);
        }else {
            rvsnackList.setVisibility(View.GONE);
            llEmpty.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK){
            loadData();//加载数据
        }
    }
}
