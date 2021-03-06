package com.hiden.persistence;

import com.hiden.persistence.domain.ProfileCoopDO;
import com.hiden.persistence.domain.ProfileCoopDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileCoopDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int countByExample(ProfileCoopDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByExample(ProfileCoopDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insert(ProfileCoopDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insertSelective(ProfileCoopDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    List<ProfileCoopDO> selectByExample(ProfileCoopDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    ProfileCoopDO selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExampleSelective(@Param("record") ProfileCoopDO record, @Param("example") ProfileCoopDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExample(@Param("record") ProfileCoopDO record, @Param("example") ProfileCoopDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKeySelective(ProfileCoopDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_coop
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKey(ProfileCoopDO record);
}