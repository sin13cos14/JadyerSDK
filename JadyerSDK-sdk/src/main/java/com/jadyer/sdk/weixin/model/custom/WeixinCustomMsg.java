package com.jadyer.sdk.weixin.model.custom;

/**
 * 客服接口请求参数的基类
 * @create Oct 18, 2015 10:37:58 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public abstract class WeixinCustomMsg {
	/**
	 * 普通用户openid
	 */
	private String touser;

	public WeixinCustomMsg(String touser) {
		this.touser = touser;
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}
}