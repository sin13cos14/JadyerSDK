package com.jadyer.sdk.qq.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.qq.helper.QQTokenHolder;
import com.jadyer.sdk.qq.model.QQOAuthAccessToken;
import com.jadyer.sdk.util.HttpUtil;

/**
 * 接收QQ服务器回调以及其它的辅助功能
 * @create Dec 23, 2015 11:58:49 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
@Controller
@RequestMapping(value="/qq/helper")
public class QQHelperController {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 获取网页access_token
	 * @param appid QQappid,通过它来支持多用户
	 * @param code  QQ服务器发放的,有效期为5分钟的,用于换取网页access_token的code
	 * @param state 重定向到QQ服务器时,由开发者服务器携带过去的参数,这里会原样带回
	 * @return 获取失败则返回一个友好的HTML页面,获取成功后直接跳转到用户原本请求的资源
	 */
	@RequestMapping(value="/oauth/{appid}")
	public String oauth(@PathVariable String appid, String code, String state, HttpServletResponse response) throws IOException{
		logger.info("收到QQ服务器回调code=[{}], state=[{}]", code, state);
		if(StringUtils.isNotBlank(code)){
			QQOAuthAccessToken oauthAccessToken = QQTokenHolder.getQQOAuthAccessToken(appid, code);
			if(0==oauthAccessToken.getErrcode() && StringUtils.isNotBlank(oauthAccessToken.getOpenid())){
				/**
				 * 还原state携带过来的粉丝请求的原URL
				 * @see state=http://www.jadyer.com/mpp/qq/getOpenid/openid=openid/test=7645
				 */
				//1.获取到URL中的非参数部分
				String uri = state.substring(0, state.indexOf("="));
				uri = uri.substring(0, uri.lastIndexOf("/"));
				//2.获取到URL中的参数部分(得到openid的方式为截取掉占位的,再追加真正的值)
				String params = state.substring(uri.length()+1);
				params = params.replaceAll("/", "&").replace("openid=openid", "openid="+oauthAccessToken.getOpenid());
				//3.拼接粉丝请求的原URL并跳转过去
				String fullURI = uri + "?" + params;
				logger.info("还原粉丝请求的资源得到state=[{}]", fullURI);
				response.sendRedirect(fullURI);
			}
		}
		response.setCharacterEncoding(HttpUtil.DEFAULT_CHARSET);
		response.setContentType("text/plain; charset=" + HttpUtil.DEFAULT_CHARSET);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		PrintWriter out = response.getWriter();
		out.print("系统繁忙Unauthorized\r\n请联系您关注的QQ公众号");
		out.flush();
		out.close();
		return null;
	}
}