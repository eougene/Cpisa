package net.dot.com.cpisa.bean;

import java.io.Serializable;

/**
 * Created by e-dot on 2017/5/31.
 */

public class ErrorBean implements Serializable {


    /**
     * status : -2004
     * desc : 校验服务卡信息失败。
     服务卡：[DDDD0012]
     * result :
     */

    private int status;
    private String desc;
    private String result;

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
}
