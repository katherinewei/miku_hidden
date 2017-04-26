package com.hiden.web.socket.android;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Date;

import javax.annotation.Resource;

import net.spy.memcached.MemcachedClient;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.hiden.web.model.SocketVo;

public class MyMessageInbound extends MessageInbound  {
	

	
	private SocketVo socketVo;
	
	
	public SocketVo getSocketVo() {
		return socketVo;
	}

	public void setSocketVo(SocketVo socketVo) {
		this.socketVo = socketVo;
	}

	public MyMessageInbound(SocketVo socketVo) {
		super();
		this.socketVo = socketVo;
	}

	@Override
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		
	}

	@Override
	protected void onTextMessage(CharBuffer msg) throws IOException {
		System.out.println("Received:"+msg);  
        MessageCenter.getInstance().broadcast(msg,this.socketVo);  
	}
	
	
	@Override  
    protected void onClose(int status) {  
        System.out.println("close:"+new Date());  
        MessageCenter.getInstance().removeMessageInbound(this);  
        super.onClose(status);  
    }  
  
    @Override  
    protected void onOpen(WsOutbound outbound) {  
        System.out.println("open :"+new Date());  
        super.onOpen(outbound);  
        MessageCenter.getInstance().addMessageInbound(this);  
    }  
}
