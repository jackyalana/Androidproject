package com.example.snack.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.snack.R;
import com.example.snack.bean.Snack;
import com.example.snack.widget.ActionBar;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 添加页面
 */
public class AddSnackActivity extends AppCompatActivity {
    private ActionBar mActionBar;//标题栏
    private Activity myActivity;
    private EditText etTitle;//标题
    private EditText etIssuer;//发布单位
    private EditText etImg;//图片
    private Spinner spType;//类型
    private EditText etContent;//内容
    private ImageView ivImg;//图片
    SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Snack msnack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myActivity = this;
        setContentView(R.layout.activity_snack_add);
        etTitle = findViewById(R.id.title);
        etIssuer = findViewById(R.id.issuer);
        spType = findViewById(R.id.type);
        etImg = findViewById(R.id.img);
        etContent = findViewById(R.id.content);
        ivImg = findViewById(R.id.iv_img);
        mActionBar = findViewById(R.id.myActionBar);
        //侧滑菜单
        mActionBar.setData(myActivity,"编辑零食信息", R.drawable.ic_back, 0, 0, getResources().getColor(R.color.colorPrimary), new ActionBar.ActionBarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
            }
        });
        initView();
    }

    private void initView() {
        msnack = (Snack) getIntent().getSerializableExtra("snack");
        if (msnack !=null){
            etTitle.setText(msnack.getTitle());
            spType.setSelection(msnack.getTypeId());
            etImg.setText(msnack.getImg());
            etIssuer.setText(msnack.getIssuer());
            etContent.setText(msnack.getContent());
            spType.setSelection(msnack.getTypeId(),true);
            //图片加载框架Glide   必要时释放资源
            Glide.with(myActivity)
                    .asBitmap()
                    .load(msnack.getImg())
                    .error(R.drawable.ic_error)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivImg);
        }
        ivImg.setVisibility(msnack ==null? View.GONE: View.VISIBLE);
    }

    public void save(View view){
        String title = etTitle.getText().toString();
        String issuer = etIssuer.getText().toString();
        String img = etImg.getText().toString();
        String content = etContent.getText().toString();
        Integer typeId =  spType.getSelectedItemPosition();
        if ("".equals(title)) {
            Toast.makeText(myActivity,"标题不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(issuer)) {
            Toast.makeText(myActivity,"价格不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(img)) {
            Toast.makeText(myActivity,"图片地址不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        if ("".equals(content)) {
            Toast.makeText(myActivity,"描述不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        Snack snack = null;
        if (!title.equals(msnack != null? msnack.getTitle():"")){
            snack = DataSupport.where("title = ?",title).findFirst(Snack.class);
        }
        if (snack == null ){
            if (msnack != null){
                snack = DataSupport.where("title = ?", msnack.getTitle()).findFirst(Snack.class);
                snack.setTypeId(typeId);
                snack.setTitle(title);
                snack.setIssuer(issuer);
                snack.setImg(img);
                snack.setContent(content);
            }else {
                snack = new Snack(typeId,title,img,content,issuer,sf.format(new Date()));
            }
            snack.save();
            setResult(RESULT_OK);
            finish();
            Toast.makeText(myActivity,"保存成功", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(myActivity,"该标题已存在", Toast.LENGTH_LONG).show();
        }
    }
}
