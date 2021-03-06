package com.hiden.persistence;

import com.hiden.persistence.domain.ProfileWechatDO;
import com.hiden.persistence.domain.ProfileWechatDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileWechatDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int countByExample(ProfileWechatDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByExample(ProfileWechatDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insert(ProfileWechatDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insertSelective(ProfileWechatDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    List<ProfileWechatDO> selectByExample(ProfileWechatDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    ProfileWechatDO selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExampleSelective(@Param("record") ProfileWechatDO record, @Param("example") ProfileWechatDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExample(@Param("record") ProfileWechatDO record, @Param("example") ProfileWechatDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKeySelective(ProfileWechatDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_wechat
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKey(ProfileWechatDO record);
}