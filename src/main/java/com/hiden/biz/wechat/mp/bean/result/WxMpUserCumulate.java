package com.hiden.biz.wechat.mp.bean.result;

import java.io.Serializable;
import java.util.Date;

public class WxMpUserCumulate implements Serializable {
    private Date refDate;
    private Integer cumulateUser;

    public WxMpUserCumulate() {
    }

    public Date getRefDate() {
        return this.refDate;
    }

    public void setRefDate(Date refDate) {
        this.refDate = refDate;
    }

    public Integer getCumulateUser() {
        return this.cumulateUser;
    }

    public void setCumulateUser(Integer cumulateUser) {
        this.cumulateUser = cumulateUser;
    }

    public String toString() {
        return "WxMpUserCumulate{refDate=" + this.refDate + ", cumulateUser=" + this.cumulateUser + '}';
    }
}
