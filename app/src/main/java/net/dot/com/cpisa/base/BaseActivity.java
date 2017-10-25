package net.dot.com.cpisa.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.dot.com.cpisa.R;
import net.dot.com.cpisa.view.DialogOptionActivity;
import net.dot.com.cpisa.zxing.view.ProgersssDialog;

/**
 * Created by e-dot on 2017/10/20.
 * 一个基类,实现一些公用的方法
 */

public class BaseActivity<P extends BasePresenter> extends MyAppCompatActivity  {
    //错误信息提示
    private Toast toast;
    //Presenter(主导器)
    protected P presenter;
    //布局容器
    private ViewGroup content;
    //标题
    private TextView tvTitle;
    //返回按钮
    private ImageView tvBack;
    //自定义加载框
    private ProgersssDialog progersssDialog;

    //头部
    private LinearLayout baseHead;

    //返回按钮事件的处理
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backImageView:
                    onBack(v);
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.base_head);

        progersssDialog = new ProgersssDialog(this);

        content = obtainViewById(R.id.ll_content);
        tvBack = obtainViewById(R.id.backImageView);
        tvTitle = obtainViewById(R.id.title);
        baseHead = obtainViewById(R.id.baseHead);
        tvBack.setOnClickListener(onClickListener);
        initPresenter();
    }


    //隐藏整个标题
    protected void hideBaseHead() {
        baseHead.setVisibility(View.GONE);
    }


    //返回按钮
    protected void onBack(View v) {
        finish();
    }


    //显示返回按钮
    public void showBack() {
        tvBack.setVisibility(View.VISIBLE);
    }

    //隐藏返回按钮
    public void hideBack() {
        tvBack.setVisibility(View.INVISIBLE);
    }


    //添加布局
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        content.addView(getLayoutInflater().inflate(layoutResID, null), params);
    }

    //添加布局
    @Override
    public void setContentView(View view) {
        content.addView(view);
    }

    //添加布局
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        content.addView(view, params);
    }


    //标题设置
    @Override
    public void setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    //标题设置
    @Override
    public void setTitle(int titleId) {
        tvTitle.setText(titleId);
    }


    /**
     * 获取Presenter,要使用这些函数,必须先通过反射获取对应的元素:Class ,Field ,Method，
     * <p>
     * <p>
     * <p>
     * 获取指定类型的注解
     * public <A extends Annotation> A getAnnotation(Class<A> annotationType);
     * <p>
     * <p>
     * 获取所有注解，如果有的话
     * public Annotation[] getAnnotations();
     * <p>
     * <p>
     * 获取所有注解，忽略继承的注解
     * public Annotation[] getDeclaredAnnotations();
     * <p>
     * <p>
     * 指定注解是否存在该元素上，如果有则返回true，否则false
     * public boolean isAnnotationPresent(Class<? extends Annotation> annotationType);
     * <p>
     * <p>
     * 获取Method中参数的所有注解
     * public Annotation[][] getParameterAnnotations();
     */


    private void initPresenter() {

        PresenterInject pi = this.getClass().getAnnotation(PresenterInject.class);
        if (pi != null) {
            try {
                presenter = (P) pi.value().newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    //定义一个与BaseView中相同的方法,这样就在不在每个子类中去实现了
    public void onErrorTip(String tip) {
        if (TextUtils.isEmpty(tip)) {
            return;
        }
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
        toast.show();
    }


    //定义一个与BaseView中相同的方法,这样就在不在每个子类中去实现了
    public void showProgress() {
        if (progersssDialog.isShowing()) {
            progersssDialog.dismiss();
        }
        progersssDialog.show();
    }


    //义一个与BaseView中相同的方法,这样就在不在每个子类中去实现了


    public void hideProgress() {
        if (progersssDialog.isShowing()) {
            progersssDialog.dismiss();
        }
    }


    //定义一个与BaseView中相同的方法,这样就在不在每个子类中去实现了
    //调转对应的错误信息页面,防止二维码重复不能扫描
    public void goErrorActivity(String message) {
        Intent intent = new Intent(this, DialogOptionActivity.class);
        intent.putExtra("message", message);
        startActivity(intent);
    }


}
