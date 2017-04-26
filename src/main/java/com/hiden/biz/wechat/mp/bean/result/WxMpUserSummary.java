package com.hiden.biz.wechat.mp.bean.result;

import java.io.Serializable;
import java.util.Date;

public class WxMpUserSummary implements Serializable {
    private Date refDate;
    private Integer userSource;
    private Integer newUser;
    private Integer cancelUser;

    public WxMpUserSummary() {
    }

    public Date getRefDate() {
        return this.refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public Integer getUserSource() {
        return this.userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public Integer getNewUser() {
        return this.newUser;
    }

    public void setNewUser(Integer newUser) {
        this.newUser = newUser;
    }

    public Integer getCancelUser() {
        return this.cancelUser;
    }

    public void setCancelUser(Integer cancelUser) {
        this.cancelUser = cancelUser;
    }

    public String toString() {
        return "WxMpUserSummary{refDate=" + this.refDate + ", userSource=" + this.userSource + ", newUser=" + this.newUser + ", cancelUser=" + this.cancelUser + '}';
    }
}
