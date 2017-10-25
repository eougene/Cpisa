package net.dot.com.cpisa.presenter;

import android.text.TextUtils;

import net.dot.com.cpisa.bean.User;
import net.dot.com.cpisa.contract.UserUpdateConract;
import net.dot.com.cpisa.model.UserUpdateModel;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by e-dot on 2017/10/24.
 */

public class UserUpdatePresenter extends UserUpdateConract.Presenter {
    public UserUpdatePresenter() {
        setModel(new UserUpdateModel());
    }

    @Override
    public void useUpdate(String deviceCode, String tagCode, String visitType, String longitude, String latitude) {
        //判断参数是否合理
        if (TextUtils.isEmpty(deviceCode)) {

        }

        model.useUpdate(deviceCode, tagCode, visitType, longitude, latitude).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        view.showProgress();
                    }

                    @Override
                    public void onCompleted() {
                        view.hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onErrorTip(e.getMessage() + "");
                        view.hideProgress();


                    }

                    @Override
                    public void onNext(User user) {
                        //提示信息
                        view.onErrorTip(user.getDesc() + "");
                        //登录成功
                        if (user.getStatus() == 0) {
                            view.useUpdate(user);

                        } else {
                            view.onErrorTip(user.getDesc() + "");
                        }


                    }
                });


    }


}

