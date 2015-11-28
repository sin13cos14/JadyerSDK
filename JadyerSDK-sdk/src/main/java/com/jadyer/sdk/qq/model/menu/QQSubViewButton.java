package com.jadyer.sdk.qq.model.menu;

/**
 * 封装VIEW类型的子菜单项
 * @see 1.这一类菜单有三个固定值name/type/url
 * @see 2.这里的子菜单指的是没有子菜单的菜单项
 * @see   其可能是二级菜单项(QQ公众号菜单最多两级),也有能是不含二级菜单的一级菜单(即进入QQ公众号第一眼看到的)
 * @create Nov 28, 2015 8:59:02 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class QQSubViewButton extends QQButton {
	private String type;
	private String url;
	
	public QQSubViewButton(String name, String url) {
		super(name);
		this.url = url;
		this.type = "view";
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}