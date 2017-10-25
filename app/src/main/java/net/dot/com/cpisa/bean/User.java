package net.dot.com.cpisa.bean;

import java.io.Serializable;

/**
 * Created by e-dot on 2017/3/7.
 */

public class User implements Serializable {


    /**
     * status : 0
     * desc : 操作成功。
     * result :
     * obj : {"memberName":"安卓测试服务对象-3","longitude":"121.448078","latitude":"31.16025"}
     * jsCode :
     */

    private int status;
    private String desc;
    private String result;
    private ObjBean obj;
    private String jsCode;

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

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }

    public static class ObjBean {
        /**
         * memberName : 安卓测试服务对象-3
         * longitude : 121.448078
         * latitude : 31.16025
         */

        private String memberName;
        private String longitude;
        private String latitude;

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }
    }
}
