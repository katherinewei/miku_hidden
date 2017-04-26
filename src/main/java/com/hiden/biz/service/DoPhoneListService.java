package com.hiden.biz.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Service;

import com.hiden.persistence.PhoneBookListDoMapper;
import com.hiden.persistence.WxPhoneResultDoMapper;
import com.hiden.persistence.domain.PhoneBookListDo;
import com.hiden.persistence.domain.PhoneBookListDoExample;
import com.hiden.persistence.domain.WxContactInfoDO;
import com.hiden.persistence.domain.WxPhoneResultDo;
import com.hiden.web.model.PhoneInfoVo;


@Service
public class DoPhoneListService {
	
	@Resource
	PhoneBookListDoMapper phoneBookListDoMapper;
	@Resource
	WxPhoneResultDoMapper wxPhoneResultDoMapper;
	static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat("yyyy-MM-dd");
	
	public List<PhoneBookListDo> getPhoneList(Long userId,int pageNo,int pageSize){
		PhoneBookListDoExample  example=new PhoneBookListDoExample();
    	example.setOrderByClause("last_updated  desc");
    	example.setLimit(pageSize);
    	example.setOffset(pageNo*pageSize);
    	example.createCriteria().andUserIdEqualTo(userId);
    	List<PhoneBookListDo> plist=phoneBookListDoMapper.selectByExample(example);
    	return plist;
	}
	
	
	
	//进行修改对应的状态内容值
	//id#type#isenable
	public PhoneBookListDo updateTwoPointPhoneList(String Id,String type,String isEnable){
		int size=0;
		Long pId=Long.parseLong(Id);
		Byte ptype=Byte.parseByte(type);
		Byte PisEnable=Byte.parseByte(isEnable);
		PhoneBookListDo phoneBookListDo=phoneBookListDoMapper.selectByPrimaryKey(pId);
		if(null!=phoneBookListDo){
			phoneBookListDo.setLastUpdated(new Date());
			phoneBookListDo.setType(ptype);
			phoneBookListDo.setIsEnabled(PisEnable);
			phoneBookListDoMapper.updateByPrimaryKey(phoneBookListDo);
		}
		return phoneBookListDo;
	}
	
	
	
	//添加一条新的内容值：对进行操作过的数据进行记录起来
	public int addOneWxPhoneResult(Long phonId,String info,WxContactInfoDO wxContactInfoDO,Long userId){
		WxPhoneResultDo wxPhoneResultDo=new WxPhoneResultDo();
		wxPhoneResultDo.setDateCreated(new Date());
		wxPhoneResultDo.setLastUpdated(new Date());
		wxPhoneResultDo.setDeviceInfo(info);
		wxPhoneResultDo.setVersion(1L);
		wxPhoneResultDo.setPhoneId(phonId);
		wxPhoneResultDo.setFromWxId(wxContactInfoDO.getId());
		wxPhoneResultDo.setUserId(userId);
		wxPhoneResultDo.setFromWxName(wxContactInfoDO.getWxLemonName());
		return wxPhoneResultDoMapper.insert(wxPhoneResultDo);
	}
	
	
	
	//进行添加对应的数据前的校验,通过则进行添加相应的数据
	public int addOnePhoneData(Long userId,String phone){
		int size=0;
		PhoneBookListDoExample  example=new PhoneBookListDoExample();
		example.createCriteria().andUserIdEqualTo(userId).andPhoneNumEqualTo(phone);
		List<PhoneBookListDo> list=phoneBookListDoMapper.selectByExample(example);
		if(list.size()==0){
			PhoneBookListDo model=new PhoneBookListDo();
			model.setDateCreated(new Date());
			model.setLastUpdated(new Date());
			model.setPhoneNum(phone);
			model.setIsEnabled((byte)0);
			model.setIsRead((byte)0);
			model.setIsSuccess((byte)0);
			model.setVersion(1L);
			model.setUserId(userId);
			model.setType((byte)0);
			size=phoneBookListDoMapper.insert(model);
		}
		return size;
	}
	
	public int updatePhoneList(String idstr){
		int size=0;
		Long id=Long.parseLong(idstr);
		PhoneBookListDo model=phoneBookListDoMapper.selectByPrimaryKey(id);
		if(null!=model){
			model.setLastUpdated(new Date());
			model.setIsRead((byte)1);
			size=phoneBookListDoMapper.updateByPrimaryKey(model);
		}
		return size;
	}
	
	
	
	//查找对应的电话簿容纳的数量值
	public int getTodayCountByDeviceInfo(String deviceInfo,Long userId,Long wxId){
		 Map map=new HashedMap();
		 map.put("deviceInfo", deviceInfo);
		 map.put("userId", userId);
		 map.put("wxId", wxId);
		 Date now = new Date();
		 map.put("dateCreated",DATAFORMAT.format( now )+" 00:00:00");
		 return wxPhoneResultDoMapper.getTodayCountByDeviceInfo(map);
	}
	
	
	//根据对应的userId来进行查找对应的未读取未处理的电话号码数
	public List<PhoneInfoVo> selectPhoneListDataByUserId(Long userId,int limit){
		Map map=new HashedMap();
		map.put("userId", userId);
		map.put("limit", limit);
		List<PhoneInfoVo> list= phoneBookListDoMapper.selectPhoneListDataByUserId(map);
		return list;
	}

}
