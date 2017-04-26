package com.hiden.biz.model;

import com.hiden.persistence.domain.ProfileDO;

/**
 * Created by myron on 16-10-8.
 */
public class UserInfo {

    private ProfileDO profileDO;

    //private TenantDO tenantDO;

    private String eTid;//加密后的tid

    public String geteTid() {
        return eTid;
    }

    public void seteTid(String eTid) {
        this.eTid = eTid;
    }

    public ProfileDO getProfileDO() {
        return profileDO;
    }

    public void setProfileDO(ProfileDO profileDO) {
        this.profileDO = profileDO;
    }

//    public TenantDO getTenantDO() {
//        return tenantDO;
//    }

//    public void setTenantDO(TenantDO tenantDO) {
//        this.tenantDO = tenantDO;
//    }
}
