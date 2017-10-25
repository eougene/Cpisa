package net.dot.com.cpisa.bean;

import java.io.Serializable;

/**
 * Created by e-dot on 2017/5/5.
 */

public class LoginBean implements Serializable {


    /**
     * status : 0
     * desc : 登录成功
     * result : 1429
     * obj : {"userName":"安卓测试服务人员","loginTime":"2017-05-05","roleIds":"0"}
     */

    private int status;
    private String desc;
    private String result;
    private ObjBean obj;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean implements Serializable {
        /**
         * userName : 安卓测试服务人员
         * loginTime : 2017-05-05
         * roleIds : 0
         */

        private String userName;
        private String loginTime;
        private String roleIds;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getRoleIds() {
            return roleIds;
        }

        public void setRoleIds(String roleIds) {
            this.roleIds = roleIds;
        }
    }
}
