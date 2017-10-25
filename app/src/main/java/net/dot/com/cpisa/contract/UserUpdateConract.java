package net.dot.com.cpisa.contract;

import net.dot.com.cpisa.base.BaseModel;
import net.dot.com.cpisa.base.BasePresenter;
import net.dot.com.cpisa.base.BaseView;
import net.dot.com.cpisa.bean.User;

import rx.Observable;

/**
 * Created by e-dot on 2017/10/24.
 */

public interface UserUpdateConract {


    /**
     * 获取数据
     */
    abstract class Model extends BaseModel {
        //添加要传的参数
        public abstract Observable<User> useUpdate(String deviceCode, String tagCode, String visitType, String longitude, String latitude);


    }

    /**
     * 刷新UI
     */
    interface View extends BaseView {
        //数据返回到Activity中
        void useUpdate(User user);
    }


    /**
     * 关联Model和Veiw
     */
    abstract class Presenter extends BasePresenter<Model, View> {
        public abstract void useUpdate(String deviceCode, String tagCode, String visitType, String longitude, String latitude);
    }
}
