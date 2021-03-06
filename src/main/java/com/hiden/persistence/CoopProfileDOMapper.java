package com.hiden.persistence;

import com.hiden.persistence.domain.CoopProfileDO;
import com.hiden.persistence.domain.CoopProfileDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoopProfileDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int countByExample(CoopProfileDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByExample(CoopProfileDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insert(CoopProfileDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int insertSelective(CoopProfileDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    List<CoopProfileDO> selectByExample(CoopProfileDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    CoopProfileDO selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExampleSelective(@Param("record") CoopProfileDO record, @Param("example") CoopProfileDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByExample(@Param("record") CoopProfileDO record, @Param("example") CoopProfileDOExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKeySelective(CoopProfileDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table coop_profile
     *
     * @mbggenerated Tue Nov 01 12:19:53 CST 2016
     */
    int updateByPrimaryKey(CoopProfileDO record);
}