package com.hiden.persistence.domain;

import java.util.Date;

public class ProfileDO {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.id
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.birthday
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String birthday;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.date_created
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Date dateCreated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.identity_card
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String identityCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.installed_app
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte installedApp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.last_updated
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Date lastUpdated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.mobile
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String mobile;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.nickname
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String nickname;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.password
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.profile_pic
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String profilePic;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.real_name
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String realName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.status
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.diploma
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte diploma;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.type
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.Province
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String province;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.City
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String city;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.Corp
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String corp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.AreaCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String areacode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.PostCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private String postcode;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.sex
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.effective_time
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Date effectiveTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column profile.auto_reply
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    private Byte autoReply;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.id
     *
     * @return the value of profile.id
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.id
     *
     * @param id the value for profile.id
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.birthday
     *
     * @return the value of profile.birthday
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getBirthday() {
        return birthday;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.birthday
     *
     * @param birthday the value for profile.birthday
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.date_created
     *
     * @return the value of profile.date_created
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.date_created
     *
     * @param dateCreated the value for profile.date_created
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.identity_card
     *
     * @return the value of profile.identity_card
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getIdentityCard() {
        return identityCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.identity_card
     *
     * @param identityCard the value for profile.identity_card
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.installed_app
     *
     * @return the value of profile.installed_app
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getInstalledApp() {
        return installedApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.installed_app
     *
     * @param installedApp the value for profile.installed_app
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setInstalledApp(Byte installedApp) {
        this.installedApp = installedApp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.last_updated
     *
     * @return the value of profile.last_updated
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.last_updated
     *
     * @param lastUpdated the value for profile.last_updated
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.mobile
     *
     * @return the value of profile.mobile
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.mobile
     *
     * @param mobile the value for profile.mobile
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.nickname
     *
     * @return the value of profile.nickname
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.nickname
     *
     * @param nickname the value for profile.nickname
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.password
     *
     * @return the value of profile.password
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.password
     *
     * @param password the value for profile.password
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.profile_pic
     *
     * @return the value of profile.profile_pic
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getProfilePic() {
        return profilePic;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.profile_pic
     *
     * @param profilePic the value for profile.profile_pic
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic == null ? null : profilePic.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.real_name
     *
     * @return the value of profile.real_name
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getRealName() {
        return realName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.real_name
     *
     * @param realName the value for profile.real_name
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.status
     *
     * @return the value of profile.status
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.status
     *
     * @param status the value for profile.status
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.diploma
     *
     * @return the value of profile.diploma
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getDiploma() {
        return diploma;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.diploma
     *
     * @param diploma the value for profile.diploma
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setDiploma(Byte diploma) {
        this.diploma = diploma;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.type
     *
     * @return the value of profile.type
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.type
     *
     * @param type the value for profile.type
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.Province
     *
     * @return the value of profile.Province
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getProvince() {
        return province;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.Province
     *
     * @param province the value for profile.Province
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.City
     *
     * @return the value of profile.City
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getCity() {
        return city;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.City
     *
     * @param city the value for profile.City
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.Corp
     *
     * @return the value of profile.Corp
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getCorp() {
        return corp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.Corp
     *
     * @param corp the value for profile.Corp
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setCorp(String corp) {
        this.corp = corp == null ? null : corp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.AreaCode
     *
     * @return the value of profile.AreaCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getAreacode() {
        return areacode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.AreaCode
     *
     * @param areacode the value for profile.AreaCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setAreacode(String areacode) {
        this.areacode = areacode == null ? null : areacode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.PostCode
     *
     * @return the value of profile.PostCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.PostCode
     *
     * @param postcode the value for profile.PostCode
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode == null ? null : postcode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.sex
     *
     * @return the value of profile.sex
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.sex
     *
     * @param sex the value for profile.sex
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setSex(Byte sex) {
        this.sex = sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.effective_time
     *
     * @return the value of profile.effective_time
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Date getEffectiveTime() {
        return effectiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.effective_time
     *
     * @param effectiveTime the value for profile.effective_time
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setEffectiveTime(Date effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column profile.auto_reply
     *
     * @return the value of profile.auto_reply
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public Byte getAutoReply() {
        return autoReply;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column profile.auto_reply
     *
     * @param autoReply the value for profile.auto_reply
     *
     * @mbggenerated Mon Nov 21 14:51:30 CST 2016
     */
    public void setAutoReply(Byte autoReply) {
        this.autoReply = autoReply;
    }
}