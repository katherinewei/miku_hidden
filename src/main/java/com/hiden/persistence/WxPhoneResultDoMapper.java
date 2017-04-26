package com.hiden.persistence;

import com.hiden.persistence.domain.WxPhoneResultDo;
import com.hiden.persistence.domain.WxPhoneResultDoExample;
import com.hiden.web.model.PhoneInfoVo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
@Repository
public interface WxPhoneResultDoMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int countByExample(WxPhoneResultDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int deleteByExample(WxPhoneResultDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int insert(WxPhoneResultDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int insertSelective(WxPhoneResultDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    List<WxPhoneResultDo> selectByExampleWithRowbounds(WxPhoneResultDoExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    List<WxPhoneResultDo> selectByExample(WxPhoneResultDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    WxPhoneResultDo selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int updateByExampleSelective(@Param("record") WxPhoneResultDo record, @Param("example") WxPhoneResultDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int updateByExample(@Param("record") WxPhoneResultDo record, @Param("example") WxPhoneResultDoExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int updateByPrimaryKeySelective(WxPhoneResultDo record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wx_phone_result
     *
     * @mbggenerated Tue Dec 06 12:07:42 CST 2016
     */
    int updateByPrimaryKey(WxPhoneResultDo record);
    
    
    //获取对应设备当天发送的个数
    int getTodayCountByDeviceInfo(Map map);
    
    
    //根据对应的用户id来查找对应得电话簿的数据
    List<PhoneInfoVo>  selectPhoneListDataByUserId(Map map);
}