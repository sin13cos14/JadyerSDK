package com.jadyer.sdk.qq.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.qq.msg.QQInMsgParser;
import com.jadyer.sdk.qq.msg.QQOutMsgXmlBuilder;
import com.jadyer.sdk.qq.msg.in.QQInImageMsg;
import com.jadyer.sdk.qq.msg.in.QQInLocationMsg;
import com.jadyer.sdk.qq.msg.in.QQInMsg;
import com.jadyer.sdk.qq.msg.in.QQInTextMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInFollowEventMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInMenuEventMsg;
import com.jadyer.sdk.qq.msg.out.QQOutMsg;
import com.jadyer.sdk.util.SDKUtil;

/**
 * 接收QQ服务器消息,自动解析成com.jadyer.sdk.qq.msg.in.QQInMsg
 * 并分发到相应的处理方法,得到处理后的com.jadyer.sdk.qq.msg.out.QQOutMsg并回复给QQ服务器
 * @create Nov 26, 2015 7:22:14 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class QQMsgController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value="/{token}")
	public void index(@PathVariable String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String reqBodyMsg = SDKUtil.extractHttpServletRequestBodyMessage(request);
		logger.info("收到QQ服务器消息如下\n{}", SDKUtil.extractHttpServletRequestHeaderMessage(request)+"\n"+reqBodyMsg);
		//GET过来的请求表示更新开发者服务器URL
		if("GET".equalsIgnoreCase(request.getMethod())){
			//验签
			if(!this.verifySignature(DigestUtils.md5Hex(token+"http://blog.csdn.net/jadyer"+token), request)){
				PrintWriter out = response.getWriter();
				out.write("verify signature failed");
				out.flush();
				out.close();
				return;
			}
			PrintWriter out = response.getWriter();
			out.write(request.getParameter("echostr"));
			out.flush();
			out.close();
			return;
		}
		//POST过来的请求表示QQ服务器请求通信
		QQInMsg inMsg = QQInMsgParser.parse(reqBodyMsg);
		QQOutMsg outMsg = new QQOutMsg();
		if(inMsg instanceof QQInTextMsg){
			outMsg = this.processInTextMsg((QQInTextMsg)inMsg);
		}
		if(inMsg instanceof QQInImageMsg){
			outMsg = this.processInImageMsg((QQInImageMsg)inMsg);
		}
		if(inMsg instanceof QQInLocationMsg){
			outMsg = this.processInLocationMsg((QQInLocationMsg)inMsg);
		}
		if(inMsg instanceof QQInFollowEventMsg){
			outMsg = this.processInFollowEventMsg((QQInFollowEventMsg)inMsg);
		}
		if(inMsg instanceof QQInMenuEventMsg){
			outMsg = this.processInMenuEventMsg((QQInMenuEventMsg)inMsg);
		}
		String outMsgXml = QQOutMsgXmlBuilder.build(outMsg);
		PrintWriter out = response.getWriter();
		out.write(outMsgXml);
		out.flush();
		out.close();
		logger.info("应答QQ服务器消息-->{}", outMsgXml);
		return;
	}


	/**
	 * 验签
	 * @create Nov 26, 2015 7:24:26 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	private boolean verifySignature(String token, HttpServletRequest request){
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		if(StringUtils.isBlank(signature) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)){
			return false;
		}
		String[] signPlains = new String[]{token, nonce, timestamp};
		Arrays.sort(signPlains);
		if(!signature.equals(DigestUtils.sha1Hex(signPlains[0]+signPlains[1]+signPlains[2]))){
			return false;
		}
		return true;
	}


	/**
	 * 处理收到的文本消息
	 */
	protected abstract QQOutMsg processInTextMsg(QQInTextMsg inTextMsg);


	/**
	 * 处理收到的图片消息
	 */
	protected abstract QQOutMsg processInImageMsg(QQInImageMsg inImageMsg);


	/**
	 * 处理收到的地址位置消息
	 */
	protected abstract QQOutMsg processInLocationMsg(QQInLocationMsg inLocationMsg);


	/**
	 * 处理收到的关注/取消关注事件
	 */
	protected abstract QQOutMsg processInFollowEventMsg(QQInFollowEventMsg inFollowEventMsg);


	/**
	 * 处理自定义菜单拉取消息/跳转链接的事件
	 * @see 经测试,对于VIEW类型的URL跳转类,不会推到开发者服务器而是直接跳过去
	 */
	protected abstract QQOutMsg processInMenuEventMsg(QQInMenuEventMsg inMenuEventMsg);
}