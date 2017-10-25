package net.dot.com.cpisa.presenter;

import android.text.TextUtils;

import net.dot.com.cpisa.bean.LoginBean;
import net.dot.com.cpisa.contract.LoginConract;
import net.dot.com.cpisa.model.LoginModel;
import net.dot.com.cpisa.utils.PreferencesUtil;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by e-dot on 2017/10/20.
 * <p>
 * 管理员登录
 */

public class LoginPresenter extends LoginConract.Presenter {

    public LoginPresenter() {
        setModel(new LoginModel());
    }

    @Override
    public void login(String userName, String password, String checkCode, String description, String longitude, String latitude) {


        //判断参数是否合理
        if (TextUtils.isEmpty(userName)) {

        }

        model.login(userName, password, checkCode, description, longitude, latitude).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {


                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorTip(e.getMessage() + "");


                    }

                    @Override
                    public void onNext(LoginBean loginBeean) {
                        //提示信息
                        view.onErrorTip(loginBeean.getDesc() + "");
                        //登录成功
                        if (loginBeean.getStatus() == 0) {
                            //保存登录信息
                            PreferencesUtil.userName(loginBeean.getObj().getUserName());
                            //保存登录信息
                            view.onUser(loginBeean);
                        } else {
                            view.goErrorActivity(loginBeean.getDesc() + "");
                        }


                    }
                });


    }


}
