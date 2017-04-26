package com.hiden.persistence.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeviceInfoDOExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected List<Criteria> oredCriteria;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected Integer offset;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected Integer limit;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public DeviceInfoDOExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void setOffset(Integer offset) {
        this.offset=offset;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public Integer getOffset() {
        return offset;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public void setLimit(Integer limit) {
        this.limit=limit;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public Integer getLimit() {
        return limit;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andDateCreatedIsNull() {
            addCriterion("date_created is null");
            return (Criteria) this;
        }

        public Criteria andDateCreatedIsNotNull() {
            addCriterion("date_created is not null");
            return (Criteria) this;
        }

        public Criteria andDateCreatedEqualTo(Date value) {
            addCriterion("date_created =", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedNotEqualTo(Date value) {
            addCriterion("date_created <>", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedGreaterThan(Date value) {
            addCriterion("date_created >", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("date_created >=", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedLessThan(Date value) {
            addCriterion("date_created <", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedLessThanOrEqualTo(Date value) {
            addCriterion("date_created <=", value, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedIn(List<Date> values) {
            addCriterion("date_created in", values, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedNotIn(List<Date> values) {
            addCriterion("date_created not in", values, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedBetween(Date value1, Date value2) {
            addCriterion("date_created between", value1, value2, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andDateCreatedNotBetween(Date value1, Date value2) {
            addCriterion("date_created not between", value1, value2, "dateCreated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIsNull() {
            addCriterion("last_updated is null");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIsNotNull() {
            addCriterion("last_updated is not null");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedEqualTo(Date value) {
            addCriterion("last_updated =", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotEqualTo(Date value) {
            addCriterion("last_updated <>", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedGreaterThan(Date value) {
            addCriterion("last_updated >", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("last_updated >=", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedLessThan(Date value) {
            addCriterion("last_updated <", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("last_updated <=", value, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedIn(List<Date> values) {
            addCriterion("last_updated in", values, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotIn(List<Date> values) {
            addCriterion("last_updated not in", values, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedBetween(Date value1, Date value2) {
            addCriterion("last_updated between", value1, value2, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andLastUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("last_updated not between", value1, value2, "lastUpdated");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoIsNull() {
            addCriterion("device_info is null");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoIsNotNull() {
            addCriterion("device_info is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoEqualTo(String value) {
            addCriterion("device_info =", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoNotEqualTo(String value) {
            addCriterion("device_info <>", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoGreaterThan(String value) {
            addCriterion("device_info >", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoGreaterThanOrEqualTo(String value) {
            addCriterion("device_info >=", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoLessThan(String value) {
            addCriterion("device_info <", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoLessThanOrEqualTo(String value) {
            addCriterion("device_info <=", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoLike(String value) {
            addCriterion("device_info like", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoNotLike(String value) {
            addCriterion("device_info not like", value, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoIn(List<String> values) {
            addCriterion("device_info in", values, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoNotIn(List<String> values) {
            addCriterion("device_info not in", values, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoBetween(String value1, String value2) {
            addCriterion("device_info between", value1, value2, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andDeviceInfoNotBetween(String value1, String value2) {
            addCriterion("device_info not between", value1, value2, "deviceInfo");
            return (Criteria) this;
        }

        public Criteria andWxidIsNull() {
            addCriterion("wxid is null");
            return (Criteria) this;
        }

        public Criteria andWxidIsNotNull() {
            addCriterion("wxid is not null");
            return (Criteria) this;
        }

        public Criteria andWxidEqualTo(Long value) {
            addCriterion("wxid =", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidNotEqualTo(Long value) {
            addCriterion("wxid <>", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidGreaterThan(Long value) {
            addCriterion("wxid >", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidGreaterThanOrEqualTo(Long value) {
            addCriterion("wxid >=", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidLessThan(Long value) {
            addCriterion("wxid <", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidLessThanOrEqualTo(Long value) {
            addCriterion("wxid <=", value, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidIn(List<Long> values) {
            addCriterion("wxid in", values, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidNotIn(List<Long> values) {
            addCriterion("wxid not in", values, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidBetween(Long value1, Long value2) {
            addCriterion("wxid between", value1, value2, "wxid");
            return (Criteria) this;
        }

        public Criteria andWxidNotBetween(Long value1, Long value2) {
            addCriterion("wxid not between", value1, value2, "wxid");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeIsNull() {
            addCriterion("online_time is null");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeIsNotNull() {
            addCriterion("online_time is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeEqualTo(Date value) {
            addCriterion("online_time =", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeNotEqualTo(Date value) {
            addCriterion("online_time <>", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeGreaterThan(Date value) {
            addCriterion("online_time >", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("online_time >=", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeLessThan(Date value) {
            addCriterion("online_time <", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeLessThanOrEqualTo(Date value) {
            addCriterion("online_time <=", value, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeIn(List<Date> values) {
            addCriterion("online_time in", values, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeNotIn(List<Date> values) {
            addCriterion("online_time not in", values, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeBetween(Date value1, Date value2) {
            addCriterion("online_time between", value1, value2, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andOnlineTimeNotBetween(Date value1, Date value2) {
            addCriterion("online_time not between", value1, value2, "onlineTime");
            return (Criteria) this;
        }

        public Criteria andDeviceNameIsNull() {
            addCriterion("device_name is null");
            return (Criteria) this;
        }

        public Criteria andDeviceNameIsNotNull() {
            addCriterion("device_name is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceNameEqualTo(String value) {
            addCriterion("device_name =", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotEqualTo(String value) {
            addCriterion("device_name <>", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameGreaterThan(String value) {
            addCriterion("device_name >", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameGreaterThanOrEqualTo(String value) {
            addCriterion("device_name >=", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLessThan(String value) {
            addCriterion("device_name <", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLessThanOrEqualTo(String value) {
            addCriterion("device_name <=", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLike(String value) {
            addCriterion("device_name like", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotLike(String value) {
            addCriterion("device_name not like", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameIn(List<String> values) {
            addCriterion("device_name in", values, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotIn(List<String> values) {
            addCriterion("device_name not in", values, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameBetween(String value1, String value2) {
            addCriterion("device_name between", value1, value2, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotBetween(String value1, String value2) {
            addCriterion("device_name not between", value1, value2, "deviceName");
            return (Criteria) this;
        }

        public Criteria andVersionIsNull() {
            addCriterion("version is null");
            return (Criteria) this;
        }

        public Criteria andVersionIsNotNull() {
            addCriterion("version is not null");
            return (Criteria) this;
        }

        public Criteria andVersionEqualTo(Long value) {
            addCriterion("version =", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotEqualTo(Long value) {
            addCriterion("version <>", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThan(Long value) {
            addCriterion("version >", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionGreaterThanOrEqualTo(Long value) {
            addCriterion("version >=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThan(Long value) {
            addCriterion("version <", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionLessThanOrEqualTo(Long value) {
            addCriterion("version <=", value, "version");
            return (Criteria) this;
        }

        public Criteria andVersionIn(List<Long> values) {
            addCriterion("version in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotIn(List<Long> values) {
            addCriterion("version not in", values, "version");
            return (Criteria) this;
        }

        public Criteria andVersionBetween(Long value1, Long value2) {
            addCriterion("version between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andVersionNotBetween(Long value1, Long value2) {
            addCriterion("version not between", value1, value2, "version");
            return (Criteria) this;
        }

        public Criteria andContactFlagIsNull() {
            addCriterion("contact_flag is null");
            return (Criteria) this;
        }

        public Criteria andContactFlagIsNotNull() {
            addCriterion("contact_flag is not null");
            return (Criteria) this;
        }

        public Criteria andContactFlagEqualTo(Byte value) {
            addCriterion("contact_flag =", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagNotEqualTo(Byte value) {
            addCriterion("contact_flag <>", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagGreaterThan(Byte value) {
            addCriterion("contact_flag >", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagGreaterThanOrEqualTo(Byte value) {
            addCriterion("contact_flag >=", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagLessThan(Byte value) {
            addCriterion("contact_flag <", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagLessThanOrEqualTo(Byte value) {
            addCriterion("contact_flag <=", value, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagIn(List<Byte> values) {
            addCriterion("contact_flag in", values, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagNotIn(List<Byte> values) {
            addCriterion("contact_flag not in", values, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagBetween(Byte value1, Byte value2) {
            addCriterion("contact_flag between", value1, value2, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andContactFlagNotBetween(Byte value1, Byte value2) {
            addCriterion("contact_flag not between", value1, value2, "contactFlag");
            return (Criteria) this;
        }

        public Criteria andAutoTalkIsNull() {
            addCriterion("auto_talk is null");
            return (Criteria) this;
        }

        public Criteria andAutoTalkIsNotNull() {
            addCriterion("auto_talk is not null");
            return (Criteria) this;
        }

        public Criteria andAutoTalkEqualTo(Byte value) {
            addCriterion("auto_talk =", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkNotEqualTo(Byte value) {
            addCriterion("auto_talk <>", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkGreaterThan(Byte value) {
            addCriterion("auto_talk >", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkGreaterThanOrEqualTo(Byte value) {
            addCriterion("auto_talk >=", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkLessThan(Byte value) {
            addCriterion("auto_talk <", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkLessThanOrEqualTo(Byte value) {
            addCriterion("auto_talk <=", value, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkIn(List<Byte> values) {
            addCriterion("auto_talk in", values, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkNotIn(List<Byte> values) {
            addCriterion("auto_talk not in", values, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkBetween(Byte value1, Byte value2) {
            addCriterion("auto_talk between", value1, value2, "autoTalk");
            return (Criteria) this;
        }

        public Criteria andAutoTalkNotBetween(Byte value1, Byte value2) {
            addCriterion("auto_talk not between", value1, value2, "autoTalk");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table device_info
     *
     * @mbggenerated do_not_delete_during_merge Wed Feb 08 10:39:01 CST 2017
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table device_info
     *
     * @mbggenerated Wed Feb 08 10:39:01 CST 2017
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}