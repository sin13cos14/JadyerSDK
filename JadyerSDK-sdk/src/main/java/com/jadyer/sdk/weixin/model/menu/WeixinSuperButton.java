package com.jadyer.sdk.weixin.model.menu;

/**
 * 封装父菜单项
 * @see 1.这类菜单项有两个固定属性name和sub_button
 * @see   而sub_button是一个可能为SubClickButton或SubViewButton子菜单项数组
 * @see 2.这里的父菜单指的是包含有二级菜单项的一级菜单
 * @create Oct 18, 2015 8:55:19 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class WeixinSuperButton extends WeixinButton {
	private WeixinButton[] sub_button;
	
	public WeixinSuperButton(String name, WeixinButton[] sub_button) {
		super(name);
		this.sub_button = sub_button;
	}

	public WeixinButton[] getSub_button() {
		return sub_button;
	}

	public void setSub_button(WeixinButton[] sub_button) {
		this.sub_button = sub_button;
	}
}