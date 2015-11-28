package com.jadyer.sdk.demo.qq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jadyer.sdk.demo.common.util.LogUtil;
import com.jadyer.sdk.qq.controller.QQMsgController;
import com.jadyer.sdk.qq.helper.QQHelper;
import com.jadyer.sdk.qq.helper.QQTokenHolder;
import com.jadyer.sdk.qq.model.QQErrorInfo;
import com.jadyer.sdk.qq.model.QQFansInfo;
import com.jadyer.sdk.qq.model.custom.QQCustomTextMsg;
import com.jadyer.sdk.qq.model.custom.QQCustomTextMsg.Text;
import com.jadyer.sdk.qq.model.menu.QQButton;
import com.jadyer.sdk.qq.model.menu.QQMenu;
import com.jadyer.sdk.qq.model.menu.QQSubClickButton;
import com.jadyer.sdk.qq.model.menu.QQSubViewButton;
import com.jadyer.sdk.qq.model.menu.QQSuperButton;
import com.jadyer.sdk.qq.msg.in.QQInImageMsg;
import com.jadyer.sdk.qq.msg.in.QQInLocationMsg;
import com.jadyer.sdk.qq.msg.in.QQInTextMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInFollowEventMsg;
import com.jadyer.sdk.qq.msg.in.event.QQInMenuEventMsg;
import com.jadyer.sdk.qq.msg.out.QQOutMsg;
import com.jadyer.sdk.qq.msg.out.QQOutNewsMsg;
import com.jadyer.sdk.qq.msg.out.QQOutTextMsg;

@Controller
@RequestMapping(value="/qq")
public class QQController extends QQMsgController {
	@Override
	protected QQOutMsg processInTextMsg(QQInTextMsg inTextMsg) {
		//回复纯文本消息
		if("有一事请教".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("但说无妨");
		}
		if("兄台的趟泥步如此精纯，未知师从何处".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("家师不喜人闻，幸勿见怪");
		}
		if("小弟另有一套凌波微步".equals(inTextMsg.getContent())){
			return new QQOutTextMsg(inTextMsg).setContent("好，我等便在这步法上，证个高下");
		}
		//20151128183801测试发现QQ公众号暂时还不支持QQ表情的显示,但是支持在文本消息里写链接
		//return new QQOutTextMsg(inTextMsg).setContent("言毕，二人竟瞬息不见，步法之神令人叹绝。欲知后事如何，请访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>[阴险]");
		return new QQOutTextMsg(inTextMsg).setContent("言毕，二人竟瞬息不见，步法之神令人叹绝。欲知后事如何，请访问<a href=\"http://blog.csdn.net/jadyer\">我的博客</a>");
	}


	@Override
	protected QQOutMsg processInImageMsg(QQInImageMsg inImageMsg) {
		//return new QQOutTextMsg(inImageMsg).setContent("<a href=\""+inImageMsg.getPicUrl()+"\">点此查看</a>刚才发送的图片");
		QQOutNewsMsg outMsg = new QQOutNewsMsg(inImageMsg);
		outMsg.addNews("查看刚才发送的图片", "第一个大图文描述", inImageMsg.getPicUrl(), inImageMsg.getPicUrl());
		outMsg.addNews("点击访问我的博客", "第二个图文的描述", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "http://blog.csdn.net/jadyer");
		outMsg.addNews("点击访问我的Github", "第三个图文的描述", "http://img.my.csdn.net/uploads/201009/14/7892753_1284475095fyR0.jpg", "https://github.com/jadyer");
		return outMsg;
	}


	@Override
	protected QQOutMsg processInLocationMsg(QQInLocationMsg inLocationMsg) {
		return new QQOutTextMsg(inLocationMsg).setContent(inLocationMsg.getLabel());
	}


	@Override
	protected QQOutMsg processInFollowEventMsg(QQInFollowEventMsg inFollowEventMsg) {
		if(QQInFollowEventMsg.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			return new QQOutTextMsg(inFollowEventMsg).setContent("欢迎关注武林百晓生，民国武术，尽在此间。");
		}
		if(QQInFollowEventMsg.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEventMsg.getEvent())){
			LogUtil.getAppLogger().info("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
		}
		return new QQOutTextMsg(inFollowEventMsg).setContent("您的粉丝" + inFollowEventMsg.getFromUserName() + "取消关注了您");
	}


	@Override
	protected QQOutMsg processInMenuEventMsg(QQInMenuEventMsg inMenuEventMsg) {
		//VIEW类的直接跳转过去了,CLICK类的暂定根据关键字回复
		if(QQInMenuEventMsg.EVENT_INMENU_CLICK.equals(inMenuEventMsg.getEvent())){
			return new QQOutTextMsg(inMenuEventMsg).setContent("您刚才点击了菜单：" + inMenuEventMsg.getEventKey());
		}
		//跳到URL时,这里也不会真的推送消息给用户
		return new QQOutTextMsg(inMenuEventMsg).setContent("您正在访问<a href=\""+inMenuEventMsg.getEventKey()+"\">"+inMenuEventMsg.getEventKey()+"</a>");
	}


	/**
	 * 设置自定义菜单
	 * @see http://127.0.0.1/qq/demo/createQQMenu?appid=123&appsecret=123
	 * @create Nov 28, 2015 9:02:22 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/demo/createQQMenu")
	public QQErrorInfo createQQMenu(String appid, String appsecret){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
		QQSubViewButton btn11 = new QQSubViewButton("我的博客", "http://blog.csdn.net/jadyer");
		QQSubViewButton btn22 = new QQSubViewButton("我的GitHub", "https://github.com/jadyer");
		QQSubClickButton btn33 = new QQSubClickButton("历史上的今天", "123abc");
		QQSubClickButton btn44 = new QQSubClickButton("天气预报", "456");
		QQSubClickButton btn55 = new QQSubClickButton("幽默笑话", "joke");
		QQSuperButton sbtn11 = new QQSuperButton("个人中心", new QQButton[]{btn11, btn22});
		QQSuperButton sbtn22 = new QQSuperButton("休闲驿站", new QQButton[]{btn33, btn44});
		QQMenu menu = new QQMenu(new QQButton[]{sbtn11, btn55, sbtn22});
		return QQHelper.createQQMenu(QQTokenHolder.getQQAccessToken(), menu);
	}


	/**
	 * 拉取粉丝信息
	 * @see http://127.0.0.1/qq/demo/getQQFansInfo?appid=123&appsecret=123&openid=123
	 * @create Nov 28, 2015 9:59:01 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/demo/getQQFansInfo")
	public QQFansInfo getQQFansInfo(String appid, String appsecret, String openid){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
		return QQHelper.getQQFansInfo(QQTokenHolder.getQQAccessToken(), openid);
	}


	/**
	 * 单发主动推消息
	 * @see http://127.0.0.1/qq/demo/pushQQMsgToFans?appid=123&appsecret=123&openid=123
	 * @create Nov 28, 2015 9:54:55 PM
	 * @author 玄玉<http://blog.csdn.net/jadyer>
	 */
	@ResponseBody
	@RequestMapping(value="/demo/pushQQMsgToFans")
	public QQErrorInfo pushQQMsgToFans(String appid, String appsecret, String openid){
		QQTokenHolder.setQQAppid(appid);
		QQTokenHolder.setQQAppsecret(appsecret);
//		//推图文消息
//		QQCustomNewsMsg.News.Article article11 = new Article("", "", "", "欢迎访问玄玉博客", "玄玉博客是一个开放态度的Java生态圈", "http://avatar.csdn.net/6/0/B/1_jadyer.jpg", "http://blog.csdn.net/jadyer");
//		QQCustomNewsMsg.News.Article article22 = new Article("", "", "", "玄玉微信SDK", "玄玉微信SDK是一个正在研发中的SDK", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
//		QQCustomNewsMsg customNewsMsg = new QQCustomNewsMsg(openid, new News(new Article[]{article11, article22}));
//		return QQHelper.pushQQMsgToFans(QQTokenHolder.getQQAccessToken(), customNewsMsg);
		//推文本消息
		QQCustomTextMsg customTextMsg = new QQCustomTextMsg(openid, new Text("这是一条主动推给单个粉丝的测试消息"));
		return QQHelper.pushQQMsgToFans(QQTokenHolder.getQQAccessToken(), customTextMsg);
	}
}