package com.jadyer.sdk.weixin.msg.in;

/**
 * 接收文本消息
 * @create Oct 18, 2015 11:30:40 AM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinInTextMsg extends WeixinInMsg {
	/**
	 * 文本消息内容
	 */
	private String content;
	
	/**
	 * 64位整型的消息id
	 */
	private String msgId;

	public WeixinInTextMsg(String toUserName, String fromUserName, long createTime, String msgType) {
		super(toUserName, fromUserName, createTime, msgType);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
}