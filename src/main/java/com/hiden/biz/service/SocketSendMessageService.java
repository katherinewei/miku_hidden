package com.hiden.biz.service;

import java.nio.CharBuffer;

import org.springframework.stereotype.Service;

import com.hiden.web.model.SocketVo;
import com.hiden.web.socket.android.MessageCenter;

@Service
public class SocketSendMessageService {
	
	private MessageCenter messageCenter;
	
	//进行专门发送信息给对应信息内容
	public void  sendMessage(Long profileId,String message,String sessionId,String info) throws Exception{
		CharBuffer buffer = CharBuffer.wrap(message);
    	SocketVo  socketVo=new SocketVo(profileId, null, sessionId,null);
    	messageCenter.getInstance().pcSendMessage(buffer, socketVo);
	}

}
