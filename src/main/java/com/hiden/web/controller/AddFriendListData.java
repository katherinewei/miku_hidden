package com.hiden.web.controller;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hiden.persistence.WxFriendListMapper;
import com.hiden.persistence.domain.WxFriendList;
import com.hiden.persistence.domain.WxFriendListExample;
import com.hiden.utils.DoFileUtils;
import com.hiden.utils.EmojiFilter;
import com.hiden.web.common.ResponseStatusEnum;
import com.hiden.web.model.HidenVO;
import com.hiden.web.model.personalVo;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by myron on 16-9-28.
 */
@RestController
public class AddFriendListData {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(AddFriendListData.class);
    
    @Resource
    private WxFriendListMapper wxFriendListMapper;

    @RequestMapping(value = {"/api/m/1.0/addListData.json", "/api/h/1.0/addListData.json"}, produces = "application/json;charset=utf-8")
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
        //添加对应的测试内容
        WxFriendList data=new WxFriendList();
        data.setDateCreated(new Date());
        data.setLastUpdated(new Date());
        data.setVersion(1L);
        data.setWechatMainuser("xsdsadasdsa");
        data.setWechatPhone("dsadasda");
        data.setWechatRemark("xxxxxxxx");
        wxFriendListMapper.insert(data);
        System.out.println("ok");
        return JSON.toJSONString("ok");
    }
    
    
    
    
    @RequestMapping(value = {"/api/m/1.0/addFriendConnData.json", "/api/h/1.0/addFriendConnData.json"}, produces = "application/json;charset=utf-8")
    public String addFriendConnData(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="userId", required=false, defaultValue="-1") Long userId,
    		@RequestParam(value="wxId", required=false, defaultValue="-1") Long wxId,
    		@RequestParam(value="data", required=false) String data,
    		@RequestParam(value="username", required=false) String username) throws Exception {
    	System.out.println(data);
    	if(null==data){
    		return "传值为空耶~~~";
    	}
    	List<WxFriendList> list =JSON.parseArray(data, WxFriendList.class);
    	System.out.println(list.size());
    	for(int i=0;i<list.size();i++){
       	 WxFriendList model=list.get(i);
       	 WxFriendList onedata=new WxFriendList();
       	 if(model.getWechatNum()!=null){
       		 //判断是否重复的插入内容值
       		 boolean  flag=isHavethisData(username,model.getWechatNum(),userId);
       		 if(flag)
       			 continue;
       		 onedata.setWechatNum(EmojiFilter.filterEmoji(model.getWechatNum().trim()));
       	 }
       	 onedata.setDateCreated(new Date());
       	 onedata.setLastUpdated(new Date());
       	 onedata.setVersion(1L);
       	 onedata.setUserId(userId);
       	 onedata.setWxId(wxId);
       	 onedata.setWechatMainuser(username);
       	 if(username!=null){
       		 onedata.setWechatMainuser(EmojiFilter.filterEmoji(username.trim()));
       	 }
       	 if(model.getWechatPhone()!=null){
       		 onedata.setWechatPhone(EmojiFilter.filterEmoji(model.getWechatPhone().trim()));
       	 }
       	 if(model.getWechatRemark()!=null){
       		 onedata.setWechatRemark(EmojiFilter.filterEmoji(model.getWechatRemark().trim()));
       	 }
       	 if(model.getWechatNum()!=null){
       		 onedata.setWechatNum(EmojiFilter.filterEmoji(model.getWechatNum().trim()));
       	 }
       	 if(model.getWechatNick()!=null){
       		 onedata.setWechatNick(EmojiFilter.filterEmoji(model.getWechatNick().trim()));
       	 }
            wxFriendListMapper.insert(onedata);
       }
        return JSON.toJSONString("ok");
    }
    
    
    
    
    
    
    
    
    @RequestMapping(value = {"/api/m/1.0/addListDataFlist.json", "/api/h/1.0/addListDataFlist.json"}, produces = "application/json;charset=utf-8")
    public String addListDataFlist(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="data", required=false) String data) throws Exception {
        org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        HidenVO hidenVO = new HidenVO();
        hidenVO.setStatus(ResponseStatusEnum.SUCCESS.getCode());
//        data="[{'wechatPhone':'','wechatNum':'微信号: m271304463','wechatRemark':'','wechatNick':'AMona '},{'wechatPhone':'','wechatNum':'微信号: m271304463','wechatRemark':'','wechatNick':'AMona '}]";
//        String jsonStr = "{'personInfo':'[{'wechatPhone':'','wechatNum':'微信号: m271304463','wechatRemark':'','wechatNick':'AMona '},{'wechatPhone':'','wechatNum':'微信号: m271304463','wechatRemark':'','wechatNick':'AMona '}]'}";
//        jsonStr.replaceAll("personInfo", "");
//        data=jsonStr;
//        String jsonStr = "{'personInfo':'llllll'}";  
//        JSONObject json = JSON.parseObject(jsonStr);  
//        personalVo plist= JSON.parseObject(jsonStr, personalVo.class);  
        //Usa[] usa2 = JSON.parseObject(jsonstring2, new TypeReference<Usa[]>(){});
//        WxFriendList json = JSON.parseObject(data,WxFriendList.class);  
        String str=DoFileUtils.readFileByLines("E:\\utfwechanum.txt");
        //str = new String(str.getBytes("gbk"),"utf-8");
        str=new String(str.getBytes(),"UTF-8");
        System.out.println(str);
    	List<WxFriendList> list =JSON.parseArray(str, WxFriendList.class);
		System.out.println(list.size());
//        WxFriendList[] list=JSON.parseObject(data, new TypeReference<WxFriendList[]>(){});
        for(int i=0;i<list.size();i++){
        	 WxFriendList model=list.get(i);
        	 WxFriendList onedata=new WxFriendList();
        	 onedata.setDateCreated(new Date());
        	 onedata.setLastUpdated(new Date());
        	 onedata.setVersion(1L);
        	 if(model.getWechatMainuser()!=null){
        		 onedata.setWechatMainuser(EmojiFilter.filterEmoji(model.getWechatMainuser().trim()));
        	 }
        	 if(model.getWechatPhone()!=null){
        		 onedata.setWechatPhone(EmojiFilter.filterEmoji(model.getWechatPhone().trim()));
        	 }
        	 if(model.getWechatRemark()!=null){
        		 onedata.setWechatRemark(EmojiFilter.filterEmoji(model.getWechatRemark().trim()));
        	 }
        	 if(model.getWechatNum()!=null){
        		 onedata.setWechatNum(EmojiFilter.filterEmoji(model.getWechatNum().trim()));
        	 }
        	 if(model.getWechatNick()!=null){
        		 onedata.setWechatNick(EmojiFilter.filterEmoji(model.getWechatNick().trim()));
        	 }
             wxFriendListMapper.insert(onedata);
        }
        return JSON.toJSONString("ok");
    }
    
    
    
    
    public boolean isHavethisData(String username,String num,Long userId){
    	WxFriendListExample example=new WxFriendListExample();
    	example.createCriteria().andWechatNumEqualTo(num).andWechatMainuserEqualTo(username).andUserIdEqualTo(userId);
    	List<WxFriendList> list=wxFriendListMapper.selectByExample(example);
    	return (list.size()>0)?true:false;
    }
    
    
    
    
    
    
    //打印对应的excel内容
    @RequestMapping(value = {"/api/m/1.0/printFriendListData.json", "/api/h/1.0/printFriendListData.json"}, produces = "application/json;charset=utf-8")
    public String printFriendListData(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("微信用户资料");  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        String[] strArr=new String[]{"序号","微信号","微信名称","备注","电话号码"};
        HSSFCell cell = row.createCell((short) 0);
        for(int i=0,len=strArr.length;i<len;i++){
        	if(i!=0){
        		cell = row.createCell((short) i); 
        	}
        	cell.setCellStyle(style);  
        	cell.setCellValue(strArr[i]); 	
        }
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，
        WxFriendListExample example=new WxFriendListExample();
        example.createCriteria().andWechatNumIsNotNull();
        List<WxFriendList> wflist=wxFriendListMapper.selectByExample(example);
        for (int i = 0,len=wflist.size(); i < len; i++)  
        {  
            row = sheet.createRow((int) i + 1);  
            // 第四步，创建单元格，并设置值  
            WxFriendList model=wflist.get(i);
            row.createCell((short) 0).setCellValue(i); 
            row.createCell((short) 1).setCellValue(model.getWechatNum());  
            row.createCell((short) 2).setCellValue(model.getWechatNick());  
            row.createCell((short) 3).setCellValue(model.getWechatRemark());  
            row.createCell((short) 4).setCellValue(model.getWechatPhone());  
        }  
        // 第六步，将文件存到指定位置  
        try  
        {  
            FileOutputStream fout = new FileOutputStream("E:/微信信息表内容.xls");  
            wb.write(fout);  
            fout.close();  
        }  
        catch (Exception e)  
        {  
            e.printStackTrace();  
        }  
        
        return "ok";
    }
    


}
