package com.hiden.persistence.domain;

import java.util.Date;

public class WechatProfileDO {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.profile_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Long profileId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.wechat_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Long wechatId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.date_created
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Date dateCreated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.last_updated
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Date lastUpdated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.status
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Byte status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.version
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private Long version;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column wechat_profile.union_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    private String unionId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.id
     *
     * @return the value of wechat_profile.id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.id
     *
     * @param id the value for wechat_profile.id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.profile_id
     *
     * @return the value of wechat_profile.profile_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Long getProfileId() {
        return profileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.profile_id
     *
     * @param profileId the value for wechat_profile.profile_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.wechat_id
     *
     * @return the value of wechat_profile.wechat_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Long getWechatId() {
        return wechatId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.wechat_id
     *
     * @param wechatId the value for wechat_profile.wechat_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setWechatId(Long wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.date_created
     *
     * @return the value of wechat_profile.date_created
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.date_created
     *
     * @param dateCreated the value for wechat_profile.date_created
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.last_updated
     *
     * @return the value of wechat_profile.last_updated
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.last_updated
     *
     * @param lastUpdated the value for wechat_profile.last_updated
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.status
     *
     * @return the value of wechat_profile.status
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.status
     *
     * @param status the value for wechat_profile.status
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.version
     *
     * @return the value of wechat_profile.version
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public Long getVersion() {
        return version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.version
     *
     * @param version the value for wechat_profile.version
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column wechat_profile.union_id
     *
     * @return the value of wechat_profile.union_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public String getUnionId() {
        return unionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column wechat_profile.union_id
     *
     * @param unionId the value for wechat_profile.union_id
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    public void setUnionId(String unionId) {
        this.unionId = unionId == null ? null : unionId.trim();
    }
}