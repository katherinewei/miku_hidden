package com.hiden.web.socket.android;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;

public class ChatWebSocketServlet extends WebSocketServlet{

	@Override
	protected StreamInbound createWebSocketInbound(String arg0,	HttpServletRequest arg1) {
		return null;
	}

}
