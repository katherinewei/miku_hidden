package com.hiden.web.socket.android;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.hiden.web.model.SocketVo;

public class MessageCenter {
	
	@Resource
	private MemcachedClient memcachedClient;
	public final static int MCTime=86400;
	public final static String KEY="socket-key";
	
	private static MessageCenter     instance        = new MessageCenter();

	private List<MyMessageInbound>   mysocketList;

	private MessageCenter()
	{

		this.mysocketList = new ArrayList<MyMessageInbound>();
	}

	public static MessageCenter getInstance()
	{
		return instance;
	}
	
	public void addMessageInbound(MyMessageInbound inbound)
	{
		memcachedClient.set(oneKey(inbound.getSocketVo()), MCTime, inbound.getSocketVo().getSessionId());
		mysocketList.add(inbound);
	}

	public void removeMessageInbound(MyMessageInbound inbound)
	{
		memcachedClient.delete(oneKey(inbound.getSocketVo()));
		mysocketList.remove(inbound);
	}


	public void broadcast(CharBuffer msg,SocketVo socketVo) throws IOException
	{
		System.out.println("sessiond:"+socketVo.getSessionId());
		System.out.println("人数为:"+mysocketList.size());
		for(MyMessageInbound str:mysocketList){
			System.out.println("轮询对应的内容值："+str.getSocketVo().toString());
		}
		for (MessageInbound messageInbound : mysocketList)
		{
			CharBuffer buffer = CharBuffer.wrap(msg);
			WsOutbound outbound = messageInbound.getWsOutbound();
			outbound.writeTextMessage(CharBuffer.wrap(msg));
			// outbound.writeTextMessage(buffer);
			outbound.flush();
		}
	}
	
	
	//专门给pc进行发送对应的信息内容
	public void pcSendMessage(CharBuffer msg,SocketVo socketVo) throws IOException{
		//根据对应信息来校验一下对应的session是否存在，如存在则进行发送信息内容
		for(MyMessageInbound data:mysocketList){
			if(socketVo.getSessionId().equals(data.getSocketVo().getSessionId())){
				//锁定目标内容
				//进行判断是否对应的pc内容来进行获取的内容值
				//pc内容的推送
				CharBuffer buffer = CharBuffer.wrap(msg);
				WsOutbound outbound = data.getWsOutbound();
				outbound.writeTextMessage(CharBuffer.wrap(msg));
				outbound.flush();
			}
		}
	}
	
	
	public String oneKey(SocketVo socketVo){
		return KEY+socketVo.getUserId()+socketVo.getDeviceInfo();
	}

}














//this.socketList = new ArrayList<MessageInbound>();
//private List<MessageInbound>     socketList;

//public void addMessageInbound(MessageInbound inbound)
//{
//	socketList.add(inbound);
//}
//
//public void removeMessageInbound(MessageInbound inbound)
//{
//	socketList.remove(inbound);
//}
//private void getField(Object model) {
//java.lang.reflect.Field[] fields = model.getClass().getDeclaredFields();
//for(java.lang.reflect.Field f:fields){
//// System.out.println(f.getName()+":   "+getFieldValueByName(f.getName(),model));
//}
//}
//
//
///**
//* 根据属性名获取属性值
//* */
//private Object getFieldValueByName(String fieldName, Object o) {
//try {  
//  String firstLetter = fieldName.substring(0, 1).toUpperCase();  
//  String getter = "get" + firstLetter + fieldName.substring(1);  
//  Method method = o.getClass().getMethod(getter, new Class[] {});  
//  Object value = method.invoke(o, new Object[] {});  
//  return value;  
//} catch (Exception e) {  
//  return null;  
//}  
//} 