package com.hiden.web.socket.android;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.spy.memcached.MemcachedClient;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

import com.hiden.web.model.SocketVo;

public class MeWebSocketServlet extends WebSocketServlet{
	
		

	 	private static final long serialVersionUID = -7178893327801338294L;  
	  
	    @Override  
	    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request)  
	    {  
	        System.out.println("##########client login#########");
	        String userId=request.getParameter("userId");
	        String info=request.getParameter("info");
	        String deviceInfo=request.getParameter("deviceInfo");
	        SocketVo socketVo=null;
	        userId="79269";
	        if(null!=userId){
	        	socketVo=new SocketVo(Long.parseLong(userId), info, request.getSession().getId(),deviceInfo);
	        }
	        return new MyMessageInbound(socketVo);  
	    }  
	    
}
