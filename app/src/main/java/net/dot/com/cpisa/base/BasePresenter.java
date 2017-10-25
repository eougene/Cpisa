package net.dot.com.cpisa.base;

/**
 * Created by e-dot on 2017/10/20.
 * <p>
 * * 备注:activity可以把所有的逻辑都给Presenter处理,这样就达到java逻辑从activity中分离出来,以减轻activity的负担;
 * 作为一种新的模式，在MVP中View并不直接使用Model，它们之间的通信是通过Presenter来进行的，所有的交互都发生在Presenter内部，
 * 而在MVC中View会从直接Model中读取数据而不是通过 Controller。
 * <p>
 * <p>
 * Presenter负责逻辑的处理，
 * Model提供数据，
 * View负责显示。
 */

public abstract class BasePresenter<M extends BaseModel, V extends BaseView> {


    protected M model;
    protected V view;

    public void setView(V v) {
        view = v;
    }

    public void setModel(M m) {
        model = m;
    }

}

