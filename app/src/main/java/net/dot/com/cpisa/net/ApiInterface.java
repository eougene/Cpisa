package net.dot.com.cpisa.net;

import net.dot.com.cpisa.bean.LoginBean;
import net.dot.com.cpisa.bean.User;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by e-dot on 2017/10/20.
 */

public interface ApiInterface {


    /**
     * 管理人员登录
     * <p>
     * userName  设备代码
     * password  标签代码
     * checkCode 手机端时间
     * description  版本号
     * longide 经度
     * latitude 纬度
     */
    @POST(CommonConst.PATH_LOGIN)
    Observable<LoginBean> useLogin(@QueryMap Map<String, String> params);


    /**
     * 更新服务人员信息
     * <p>
     * deviceCode  设备代码
     * tagCode  标签代码
     * visitType 操作类别,0:获取当前老人的经纬度,1:更新当前老人的经纬度
     * longitude 经度
     * latitude 纬度
     * appKey  固定值:KSQDS5FLJ9WEI68L7KJ7LASDF0TDLAS
     */
    @POST(CommonConst.PATH_CHANGE_LOCATE)
    Observable<User> useUpdate(@QueryMap Map<String, String> params);


}
