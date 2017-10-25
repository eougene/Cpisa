package net.dot.com.cpisa.base;

/**
 * Created by e-dot on 2017/10/20.
 */

public interface BaseView {

    //显示加载框
    void showProgress();

    //隐藏加载框
    void hideProgress();

    //页面错误信息提示
    void onErrorTip(String errorTip);

    //错误信息页面提示
    void goErrorActivity(String message);

    //.........

}
