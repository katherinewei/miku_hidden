package com.hiden.persistence;

import com.hiden.persistence.domain.ProfileStatementDo;
import com.hiden.persistence.domain.ProfileStatementDoExample;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
@Repository
public interface ProfileStatementDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int countByExample(ProfileStatementDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int deleteByExample(ProfileStatementDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int insert(ProfileStatementDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int insertSelective(ProfileStatementDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    List<ProfileStatementDo> selectByExampleWithRowbounds(ProfileStatementDoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    List<ProfileStatementDo> selectByExample(ProfileStatementDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    ProfileStatementDo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int updateByExampleSelective(@Param("record") ProfileStatementDo record, @Param("example") ProfileStatementDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int updateByExample(@Param("record") ProfileStatementDo record, @Param("example") ProfileStatementDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int updateByPrimaryKeySelective(ProfileStatementDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table profile_statement
     *
     * @mbggenerated Wed Nov 23 15:41:36 CST 2016
     */
    int updateByPrimaryKey(ProfileStatementDo record);
    
    
    //根据deviceId来查找对应的数量值
    int selectStamentFlag(Long deviceId);
    //根据用户id与终端的id集合来查找全部自动回复的内容值
    List<ProfileStatementDo> selectInfoListByDeviceId(Map map); 
    
    //根据用户id与终端的id集合来查找去重的内容值
    List<ProfileStatementDo> selectGroupByContent(Map map);
    
    //根据对应的ids来查询对应的终端的id值集合内容
    String selectDeviceIdsStrByStatementIds(Map map);
}