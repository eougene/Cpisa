package net.dot.com.cpisa.model;

import android.util.Log;

import net.dot.com.cpisa.bean.User;
import net.dot.com.cpisa.contract.UserUpdateConract;
import net.dot.com.cpisa.net.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by e-dot on 2017/10/4.
 */

public class UserUpdateModel extends UserUpdateConract.Model {

    @Override
    public Observable<User> useUpdate(final String deviceCode, final String tagCode, final String visitType, final String longitude, final String latitude) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(final Subscriber<? super User> subscriber) {


                Map<String, String> params = new HashMap<>();
                params.put("deviceCode", deviceCode);//设备代码,必填
                params.put("tagCode", tagCode);//标签代码,必填
                params.put("visitType", visitType);//操作类别
                params.put("longitude", longitude);//经度
                params.put("latitude", latitude);//纬度
                params.put("appKey", "KSQDS5FLJ9WEI68L7KJ7LASDF0TDLAS");//appkey,固定值


                initRetrofit().create(ApiInterface.class).useUpdate(params)
                        .subscribe(new Subscriber<User>() {
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
                            public void onNext(User user) {
                                subscriber.onNext(user);

                                Log.e("onNext", "loginBeean:" + user.getResult());

                            }
                        });


            }
        });
    }
}
