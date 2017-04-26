/**
 * Project Name:welink-web
 * File Name:UpYunAction.java
 * Package Name:com.welink.web.resource
 * Date:2016年1月25日上午9:40:14
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.hiden.web.controller;

import java.io.File;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import sun.misc.BASE64Decoder;

import com.alibaba.fastjson.JSON;
import com.hiden.biz.common.BizErrorEnum;
import com.hiden.biz.security.NeedProfile;
import com.hiden.persistence.ProfileDOMapper;
import com.hiden.utils.UpYun.PARAMS;
import com.hiden.utils.UpYunUtil;
import com.hiden.web.model.HidenVO;


/**
 * ClassName:UpYunAction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年1月25日 上午9:40:14 <br/>
 * @author   LuoGuangChun
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
@RestController
public class UpYunAction {
	@Resource
	private ProfileDOMapper profileDOMapper;
	
	/**
	 * 
	 * uploadCommentPics:(给评论上传多张评论). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author LuoGuangChun
	 * @param request
	 * @param response
	 * @param files
	 * @return
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/uploadCommentPics.json", "/api/h/1.0/uploadCommentPics.json"}, produces = "application/json;charset=utf-8")
	public String uploadCommentPics(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "isSetWidth", required=false, defaultValue="false") Boolean isSetWidth,
			@RequestParam(value = "width", required=false, defaultValue="700") Integer width,
			@RequestParam(value = "file", required=true) MultipartFile[] files // 关键就是这句话起了作用
			) throws Exception {
		/*ImageInputStream iis = ImageIO.createImageInputStream(resFile);//resFile为需被

		Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
		if (!iter.hasNext()) {//文件不是图片
		    System.out.println("此文件不为图片文件");
		}*/
		
		
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		
		if(null == files || files.length < 1 || "".equals(files)){
			HidenVO.setStatus(0);
			HidenVO.setMsg("啊哦~您未上传图片");
			return JSON.toJSONString(HidenVO);
		}
		
		for(MultipartFile file : files){
			// 判断文件是否为空
			if (null != file && !file.isEmpty()) {
				if(!UpYunUtil.isImage(file)){
					//判断是否是图片
					HidenVO.setStatus(0);
					HidenVO.setMsg("啊哦~请选择正确的图片~");
					return JSON.toJSONString(HidenVO);
				}
			}
		}
		
		String picUrls = "";
		int countPic = 0;
		for(MultipartFile file : files){
			// 判断文件是否为空
			if (null != file && !file.isEmpty()) {
				try {
					String picUrl = "comment"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
					String dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
					Map<String, String> params = null;
					if(isSetWidth){
						params = new HashMap<String, String>();
						params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
						params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), String.valueOf(width));	//限定的宽度的值
						params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
					}else{
						params = new HashMap<String, String>();
						params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
					}
					byte[] bytes = file.getBytes();
					if(UpYunUtil.writePicByMultipartFile(file, dir, picUrl, params)){	//上传
						picUrls += UpYunUtil.UPYUN_URL+dir+picUrl+";";
						countPic++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					HidenVO.setStatus(0);
					HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
					HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
					return JSON.toJSONString(HidenVO);
				}
			}
		}
		if("".equals(picUrls.trim())){
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		picUrls = picUrls.substring(0, picUrls.length()-1);
		HidenVO.setStatus(1);
		resultMap.put("picUrls", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/uploadCommentPic.json", "/api/h/1.0/uploadCommentPic.json"}, produces = "application/json;charset=utf-8")
	public String uploadCommentPics(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "isSetWidth", required=false, defaultValue="false") Boolean isSetWidth,
			@RequestParam(value = "width", required=false, defaultValue="700") Integer width,
			@RequestParam(value = "file", required=true) MultipartFile file // 关键就是这句话起了作用
			) throws Exception {
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		
		String picUrls = "";
		int countPic = 0;
		// 判断文件是否为空
		if (null != file && !file.isEmpty()) {
			try {
				if(!UpYunUtil.isImage(file)){
					//判断是否是图片
					HidenVO.setStatus(0);
					HidenVO.setMsg("啊哦~请选择正确的图片~");
					return JSON.toJSONString(HidenVO);
				}
				
				String picUrl = "comment"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
				String dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
				
				Map<String, String> params = null;
				if(isSetWidth){
					params = new HashMap<String, String>();
					params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
					params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), String.valueOf(width));	//限定的宽度的值
					params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
				}else{
					params = new HashMap<String, String>();
					params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
				}
				
				byte[] bytes = file.getBytes();
				if(UpYunUtil.writePicByMultipartFile(file, dir, picUrl, params)){	//上传
					picUrls += UpYunUtil.UPYUN_URL+dir+picUrl;
					countPic++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				HidenVO.setStatus(0);
				HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
				HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
				return JSON.toJSONString(HidenVO);
			}
		}
		if("".equals(picUrls.trim())){
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		HidenVO.setStatus(1);
		resultMap.put("picUrl", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	/**
	 * 上传多张图片
	 * @param request
	 * @param response
	 * @param files
	 * @param type	1=不知道模块的根目录;
					2=微信二维码根目录;
					3=颜值兑换根目录;
					4=评论根目录;
					5=退货根目录;
					6=私人定制报告根目录;
	 * @return
	 * @throws Exception
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/upYunUploadPics.json"}, produces = "application/json;charset=utf-8")
	public String upYunUploadPics(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "isSetWidth", required=false, defaultValue="false") Boolean isSetWidth,
			@RequestParam(value = "width", required=false, defaultValue="700") Integer width,
			@RequestParam(value = "type", required=false, defaultValue="1") Integer type,
			@RequestParam(value = "file", required=true) MultipartFile[] files // 关键就是这句话起了作用
			) throws Exception {
		
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		
		if(null == files || files.length < 1 || "".equals(files)){
			HidenVO.setStatus(0);
			HidenVO.setMsg("啊哦~您未上传图片");
			return JSON.toJSONString(HidenVO);
		}
		
		for(MultipartFile file : files){
			// 判断文件是否为空
			if (null != file && !file.isEmpty()) {
				if(!UpYunUtil.isImage(file)){
					//判断是否是图片
					HidenVO.setStatus(0);
					HidenVO.setMsg("啊哦~请选择正确的图片~");
					return JSON.toJSONString(HidenVO);
				}
			}
		}
		
		String picUrls = "";
		int countPic = 0;
		for(MultipartFile file : files){
			// 判断文件是否为空
			if (null != file && !file.isEmpty()) {
				try {
					System.out.println("------------------------------------------getOriginalFilename:"+file.getOriginalFilename()+"---Name:"+file.getName());
					/*String originalFilename = file.getOriginalFilename();
					String suffix = originalFilename.substring(originalFilename.indexOf("."));	//后缀*/	
				
					String picUrl = "miku"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
					String dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
					if(null != type && type.equals(1)){
						//不知道模块的根目录
						dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(2)){
						//微信二维码根目录
						dir = UpYunUtil.WX_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(3)){
						//颜值兑换根目录
						dir = UpYunUtil.FACESCORE_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(4)){
						//评论根目录
						dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(5)){
						//退货根目录
						dir = UpYunUtil.RETURNGOODS_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(6)){
						//私人定制报告根目录
						dir = UpYunUtil.DZDETECTREPORT_DIR_ROOT;	//上传目录
					}else if(null != type && type.equals(7)){
						//肌肤测试根目录
						dir = UpYunUtil.SKIN_CHECK_DIR_ROOT;	//上传目录
					}else{
						//不知道模块的根目录
						dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
					}
					Map<String, String> params = null;
					if(isSetWidth){
						params = new HashMap<String, String>();
						params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
						params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), String.valueOf(width));	//限定的宽度的值
						params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
					}else{
						params = new HashMap<String, String>();
						params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
					}
					byte[] bytes = file.getBytes();
					if(UpYunUtil.writePicByMultipartFile(file, dir, picUrl, params)){	//上传
						picUrls += UpYunUtil.UPYUN_URL+dir+picUrl+";";
						countPic++;
					}
				} catch (Exception e) {
					e.printStackTrace();
					HidenVO.setStatus(0);
					HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
					HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
					return JSON.toJSONString(HidenVO);
				}
			}
		}
		if("".equals(picUrls.trim())){
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		picUrls = picUrls.substring(0, picUrls.length()-1);
		HidenVO.setStatus(1);
		resultMap.put("picUrls", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	/**
	 * 上传单张图片
	 * @param request
	 * @param response
	 * @param file
	 * @param type	1=不知道模块的根目录;
					2=微信二维码根目录;
					3=颜值兑换根目录;
					4=评论根目录;
					5=退货根目录;
					6=私人定制报告根目录;
	 * @return
	 * @throws Exception
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/upYunUploadPic.json", "/api/h/1.0/upYunUploadPic.json"}, produces = "application/json;charset=utf-8")
	public String upYunUploadPic(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "isSetWidth", required=false, defaultValue="false") Boolean isSetWidth,
			@RequestParam(value = "width", required=false, defaultValue="700") Integer width,
			@RequestParam(value = "type", required=false, defaultValue="1") Integer type,
			@RequestParam(value = "file", required=true) MultipartFile file // 关键就是这句话起了作用
			) throws Exception {
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		
		String picUrls = "";
		int countPic = 0;
		// 判断文件是否为空
		if (null != file && !file.isEmpty()) {
			try {
				if(!UpYunUtil.isImage(file)){
					//判断是否是图片
					HidenVO.setStatus(0);
					HidenVO.setMsg("啊哦~请选择正确的图片~");
					return JSON.toJSONString(HidenVO);
				}
				System.out.println("------------------------------------------getOriginalFilename:"+file.getOriginalFilename()+"---Name:"+file.getName());
				
				String picUrl = "miku"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
				String dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
				if(null != type && type.equals(1)){
					//不知道模块的根目录
					dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(2)){
					//微信二维码根目录
					dir = UpYunUtil.WX_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(3)){
					//颜值兑换根目录
					dir = UpYunUtil.FACESCORE_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(4)){
					//评论根目录
					dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(5)){
					//退货根目录
					dir = UpYunUtil.RETURNGOODS_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(6)){
					//私人定制报告根目录
					dir = UpYunUtil.DZDETECTREPORT_DIR_ROOT;	//上传目录
				}else if(null != type && type.equals(7)){
					//肌肤测试根目录
					dir = UpYunUtil.SKIN_CHECK_DIR_ROOT;	//上传目录
				}else{
					//不知道模块的根目录
					dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
				}
				Map<String, String> params = null;
				if(isSetWidth){
					params = new HashMap<String, String>();
					params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
					params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), String.valueOf(width));	//限定的宽度的值
					params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
				}else{
					params = new HashMap<String, String>();
					params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
				}
				byte[] bytes = file.getBytes();
				if(UpYunUtil.writePicByMultipartFile(file, dir, picUrl, params)){	//上传
					picUrls += UpYunUtil.UPYUN_URL+dir+picUrl;
					countPic++;
				}
			} catch (Exception e) {
				e.printStackTrace();
				HidenVO.setStatus(0);
				HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
				HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
				return JSON.toJSONString(HidenVO);
			}
		}
		if("".equals(picUrls.trim())){
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		HidenVO.setStatus(1);
		resultMap.put("picUrl", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	/**
	 * 根据url上传图片
	 * @param request
	 * @param response
	 * @param type
	 * @param url
	 * @return
	 * @throws Exception
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/upYunUploadPicByUrl.json", "/api/h/1.0/upYunUploadPicByUrl.json"}, produces = "application/json;charset=utf-8")
	public String upYunUploadPicByUrl(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "isSetWidth", required=false, defaultValue="false") Boolean isSetWidth,
			@RequestParam(value = "width", required=false, defaultValue="700") Integer width,
			@RequestParam(value = "type", required=false, defaultValue="1") Integer type,
			@RequestParam(value = "url", required=true) String url 
			) throws Exception {
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		HidenVO.setStatus(1);
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		String picUrls = "";
		url = URLDecoder.decode(url, "utf-8");
		byte[] bytes = UpYunUtil.getImageFromNetByUrl(url);
		if(null == bytes || (null != bytes && bytes.length <= 0)){
			/*resultMap.put("status", 0);
			HidenVO.setResult(resultMap);
			return JSON.toJSONString(HidenVO);*/
			HidenVO.setStatus(0);
			HidenVO.setMsg("阿哦~图片不存在");
			return JSON.toJSONString(HidenVO);
		}
		
		String picUrl = "miku"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
		String dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		if(null != type && type.equals(1)){
			//不知道模块的根目录
			dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(2)){
			//微信二维码根目录
			dir = UpYunUtil.WX_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(3)){
			//颜值兑换根目录
			dir = UpYunUtil.FACESCORE_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(4)){
			//评论根目录
			dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(5)){
			//退货根目录
			dir = UpYunUtil.RETURNGOODS_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(6)){
			//私人定制报告根目录
			dir = UpYunUtil.DZDETECTREPORT_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(7)){
			//肌肤测试根目录
			dir = UpYunUtil.SKIN_CHECK_DIR_ROOT;	//上传目录
		}else{
			//不知道模块的根目录
			dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		}
		Map<String, String> params = null;
		if(isSetWidth){
			params = new HashMap<String, String>();
			params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
			params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), String.valueOf(width));	//限定的宽度的值
			params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
		}else{
			params = new HashMap<String, String>();
			params.put(PARAMS.KEY_X_GMKERL_ROTATE.getValue(), PARAMS.VALUE_ROTATE_AUTO.getValue());
		}
		//params.put(PARAMS.KEY_X_GMKERL_UNSHARP.getValue(), "true");	//图片锐化
		
		
		if(UpYunUtil.writePicByBytes(bytes, dir, picUrl, params)){	//上传
			picUrls += UpYunUtil.UPYUN_URL+dir+picUrl;
		}
		if("".equals(picUrls.trim())){
			/*resultMap.put("status", 0);
			HidenVO.setResult(resultMap);
			return JSON.toJSONString(HidenVO);*/
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		resultMap.put("picUrl", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	/**
	 * 删除upyun图片
	 * @param request
	 * @param response
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/upyunDeleteFile.json", "/api/h/1.0/upyunDeleteFile.json"}, produces = "application/json;charset=utf-8")
	public String upyunDeleteFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "filePath", required=true) String filePath
			) throws Exception {
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		HidenVO.setStatus(1);
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		filePath = URLDecoder.decode(filePath, "utf-8");
		
		//可删除的路径图片
		if(null != filePath && (filePath.indexOf(UpYunUtil.DZDETECTREPORT_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.UNKNOWN_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.COMMENTS_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.WX_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.FACESCORE_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.SKIN_CHECK_DIR_ROOT) > -1 ||
				filePath.indexOf(UpYunUtil.RETURNGOODS_DIR_ROOT) > -1)){
			
		}else{
			HidenVO.setStatus(0);
			HidenVO.setMsg("阿哦~没有此图片或没有删除的权限~");
			return JSON.toJSONString(HidenVO);
		}
		
		
		resultMap.put("status", 1);
		filePath = filePath.substring(UpYunUtil.UPYUN_URL.length()).trim();
		try {
			Boolean deleteFile = UpYunUtil.deleteFile(filePath);	//删除wx二维码
			if(deleteFile){
				resultMap.put("status", 1);	//删除成功
			}else{
				resultMap.put("status", 0);	//删除失败
			}
		} catch (Exception e) {
			resultMap.put("status", 0);	//删除失败
		}
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	/**
	 * 批量上传upyun图片
	 * @param request
	 * @param response
	 * @param filePaths
	 * @return
	 * @throws Exception
	 */
	@NeedProfile
	@RequestMapping(value = {"/pc/h/1.0/upyunMultDeleteFileByPaths.jsons", "/api/h/1.0/upyunMultDeleteFileByPaths.json"}, produces = "application/json;charset=utf-8")
	public String upyunMultDeleteFileByPaths(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "filePaths", required=true) String filePaths
			) throws Exception {
		Long profileId = -1l;
		org.apache.shiro.subject.Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		HidenVO HidenVO = new HidenVO();
		HidenVO.setStatus(1);
		Map resultMap = new HashMap();
		profileId = (Long) session.getAttribute("profileId");
		if (null == profileId ||profileId < 0) {
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SESSION_TIME_OUT.getCode());
			HidenVO.setMsg(BizErrorEnum.SESSION_TIME_OUT.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		if(StringUtils.isBlank(filePaths)){
			HidenVO.setStatus(0);
			HidenVO.setMsg("阿哦~没有此图片或没有删除的权限~");
			return JSON.toJSONString(HidenVO);
		}
		filePaths = URLDecoder.decode(filePaths, "utf-8");
		
		String[] filePathsSplit = filePaths.split(";");
		if(filePathsSplit.length > 0){
			for(int i=0; i<filePathsSplit.length; i++){
				String filePath = filePathsSplit[i];
				//可删除的路径图片
				if(null != filePath && (filePath.indexOf(UpYunUtil.DZDETECTREPORT_DIR_ROOT) > -1 ||
						filePath.indexOf(UpYunUtil.UNKNOWN_DIR_ROOT) > -1 ||
						filePath.indexOf(UpYunUtil.COMMENTS_DIR_ROOT) > -1)){
					
				}else{
					HidenVO.setStatus(0);
					HidenVO.setMsg("阿哦~没有图片或没有删除的权限~");
					return JSON.toJSONString(HidenVO);
				}
			}
			
			for(int i=0; i<filePathsSplit.length; i++){
				String filePath = filePathsSplit[i];
				//resultMap.put("status", 1);
				filePath = filePath.substring(UpYunUtil.UPYUN_URL.length()).trim();
				try {
					Boolean deleteFile = UpYunUtil.deleteFile(filePath);	//删除wx二维码
					if(deleteFile){
						//resultMap.put("status", 1);	//删除成功
					}else{
						//resultMap.put("status", 0);	//删除失败
					}
				} catch (Exception e) {
					//resultMap.put("status", 0);	//删除失败
				}
			}
		}
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	
	
	
	//上传一个js传过来的blob类型的字符来存到upyun作为图片链接来进行返回
	@RequestMapping(value = {"/pc/h/1.0/upyunBlobDogetpatImgpath.json", "/api/h/1.0/upyunBlobDogetpatImgpath.json"}, produces = "application/json;charset=utf-8")
	public String upyunBlobDogetpatImgpath(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "type", required=false, defaultValue="1") Integer type,
			@RequestParam(value = "data", required=true) String data
			) throws Exception {
//		byte b[] = data.getBytes();//String转换为byte[] 
		BASE64Decoder decoder = new BASE64Decoder();
//		byte b[] = data.getBytes();//String转换为byte[] 
		byte b[] = decoder.decodeBuffer(data);//String转换为byte[] 
		String path=request.getSession().getServletContext().getRealPath("//");
		
		HidenVO HidenVO = new HidenVO();
		HidenVO.setStatus(1);
		Map resultMap = new HashMap();
		
		for (int i = 0; i < b.length; ++i) {
			if (b[i] < 0) {// 调整异常数据
				b[i] += 256;
			}
		}
		//不知道模块的根目录
		//String dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		String picUrl = "9lab"+System.currentTimeMillis()+ UUID.randomUUID() +".jpg";
		String dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		if(null != type && type.equals(1)){
			//不知道模块的根目录
			dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(2)){
			//微信二维码根目录
			dir = UpYunUtil.WX_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(3)){
			//颜值兑换根目录
			dir = UpYunUtil.FACESCORE_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(4)){
			//评论根目录
			dir = UpYunUtil.COMMENTS_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(5)){
			//退货根目录
			dir = UpYunUtil.RETURNGOODS_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(6)){
			//私人定制报告根目录
			dir = UpYunUtil.DZDETECTREPORT_DIR_ROOT;	//上传目录
		}else if(null != type && type.equals(7)){
			//肌肤测试根目录
			dir = UpYunUtil.SKIN_CHECK_DIR_ROOT;	//上传目录
		}else{
			//不知道模块的根目录
			dir = UpYunUtil.UNKNOWN_DIR_ROOT;	//上传目录
		}
		Boolean uploadFlag = UpYunUtil.writePicByBytes(b,dir,picUrl,null);
		String picUrls = UpYunUtil.UPYUN_URL+dir+picUrl;
		if(!uploadFlag){
			HidenVO.setStatus(0);
			HidenVO.setCode(BizErrorEnum.SYSTEM_BUSY.getCode());
			HidenVO.setMsg(BizErrorEnum.SYSTEM_BUSY.getMsg());
			return JSON.toJSONString(HidenVO);
		}
		resultMap.put("picUrl", picUrls);
		HidenVO.setResult(resultMap);
		return JSON.toJSONString(HidenVO);
	}
	
	public static void main(String[] args) {
		/*String preWxQrcodeUrl = UpYunUtil.UPYUN_URL+"/123456";
		preWxQrcodeUrl = preWxQrcodeUrl.substring(UpYunUtil.UPYUN_URL.length());
		System.out.println("-----------------------------------");
		System.out.println(preWxQrcodeUrl);*/
		//writeFile(String filePath, File file, boolean auto)
		System.out.println("11111111111111111111111");
		Map<String, String> params = new HashMap<String, String>();
		params.put(PARAMS.KEY_X_GMKERL_TYPE.getValue(), PARAMS.VALUE_FIX_WIDTH.getValue());		//限定宽度，高度自适应
		params.put(PARAMS.KEY_X_GMKERL_VALUE.getValue(), "500");	//限定的宽度的值
		UpYunUtil.writePicByFile(new File("E:\\1.jpg"), "/test/", "miku"+System.currentTimeMillis()+ UUID.randomUUID()+".jpg", params);
		//Boolean deleteFile = UpYunUtil.deleteFile("/test");	//删除wx二维码
		System.out.println("2222222222222222222222222");
		System.out.println(UUID.randomUUID());
		
	}
	
}

