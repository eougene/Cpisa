package net.dot.com.cpisa.model;

import android.util.Log;

import net.dot.com.cpisa.bean.LoginBean;
import net.dot.com.cpisa.contract.LoginConract;
import net.dot.com.cpisa.net.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by e-dot on 2017/10/20.
 * <p>
 * 获取数据
 */

public class LoginModel extends LoginConract.Model {


    //异步,观察者默认
    @Override
    public Observable<LoginBean> login(final String userName, final String password, final String checkCode, final String description, final String longitude, final String latitude) {
        return Observable.create(new Observable.OnSubscribe<LoginBean>() {
            @Override
            public void call(final Subscriber<? super LoginBean> subscriber) {


                //设置参数
                Map<String, String> params = new HashMap<>();
                params.put("userName", userName);//设备代码,必填
                params.put("password", password);//标签代码,必填
                params.put("checkCode", checkCode);//手机端时间,必填
                params.put("description", description);//版本号
                params.put("longitude", longitude);//经度
                params.put("latitude", latitude);//纬度



                //网络请求
                initRetrofit().create(ApiInterface.class).useLogin(params)
                        .subscribe(new Subscriber<LoginBean>() {
                            @Override
                            public void onCompleted() {
                                subscriber.onCompleted();
                                Log.e("onCompleted", "onCompleted:");
                            }

                            @Override
                            public void onError(Throwable e) {
                                subscriber.onError(e);

                                Log.e("onError", "onError:" + e.getMessage());
                            }

                            @Override
                            public void onNext(LoginBean loginBeean) {
                                subscriber.onNext(loginBeean);

                                Log.e("onNext", "loginBeean:" + loginBeean.getResult());

                            }
                        });


            }
        });
    }
}
