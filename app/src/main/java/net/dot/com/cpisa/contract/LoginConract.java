package net.dot.com.cpisa.contract;

import net.dot.com.cpisa.base.BaseModel;
import net.dot.com.cpisa.base.BasePresenter;
import net.dot.com.cpisa.base.BaseView;
import net.dot.com.cpisa.bean.LoginBean;

import rx.Observable;

/**
 * Created by e-dot on 2017/10/20.
 * <p>
 * 统一管理Model和View
 * <p>
 * 备注:所谓MVP者就是通过Model-处理业务逻辑,主要用来读写数据,View处理UI控件,Presenter主导器,连接和操作Model和View
 */

public interface LoginConract {


    /**
     * 获取数据
     */
    abstract class Model extends BaseModel {
        //添加要传的参数
        public abstract Observable<LoginBean> login(String userName, String password, String checkCode, String description, String longitude, String latitude);


    }

    /**
     * 刷新UI
     */
    interface View extends BaseView {

        //数据返回到Activity中
        void onUser(LoginBean loginBeean);
    }


    /**
     * 关联Model和Veiw
     */
    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void login(String userName, String password, String checkCode, String description, String longitude, String latitude);
    }

}
