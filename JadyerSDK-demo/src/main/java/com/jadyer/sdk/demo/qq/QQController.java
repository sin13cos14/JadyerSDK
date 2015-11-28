package com.jadyer.sdk.demo.qq;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jadyer.sdk.demo.common.util.LogUtil;
import com.jadyer.sdk.qq.controller.QQMsgController;
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
		outMsg.addNews("第一个大图文标题", "点此查看刚才发送的图片", inImageMsg.getPicUrl(), inImageMsg.getPicUrl());
		outMsg.addNews("第二个图文的标题", "第二个图文的描述", "http://img.my.csdn.net/uploads/201507/26/1437881866_3678.png", "https://github.com/jadyer");
		outMsg.addNews("第三个图文的标题", "第三个图文的描述", "http://img.my.csdn.net/uploads/201009/14/7892753_1284475095fyR0.jpg", "http://blog.csdn.net/jadyer/article/details/5859908");
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


//	@ResponseBody
//	@RequestMapping(value="/createMenu")
//	public ErrorInfo createMenu(){
//		String accesstoken = "nHVQXjVPWlyvdglrU6EgGnH_MzvdltddS4HOzUJocjX-wb_NVOi-6rJjumZJayRqwHT7xx80ziBaDCXc6dqddVHheP7g6aJAxv71Lwj3Cxg";
//		WeixinSubViewButton btn11 = new WeixinSubViewButton("我的博客", "http://blog.csdn.net/jadyer");
//		WeixinSubViewButton btn22 = new WeixinSubViewButton("我的GitHub", "http://jadyer.tunnel.mobi/weixin/getOpenid?oauth=base&openid=openid");
//		WeixinSubClickButton btn33 = new WeixinSubClickButton("历史上的今天", "123abc");
//		WeixinSubClickButton btn44 = new WeixinSubClickButton("天气预报", "456");
//		WeixinSubClickButton btn55 = new WeixinSubClickButton("幽默笑话", "joke");
//		WeixinSuperButton sbtn11 = new WeixinSuperButton("个人中心", new WeixinButton[]{btn11, btn22});
//		WeixinSuperButton sbtn22 = new WeixinSuperButton("休闲驿站", new WeixinButton[]{btn33, btn44});
//		WeixinMenu menu = new WeixinMenu(new Button[]{sbtn11, btn55, sbtn22});
//		return WeixinHelper.createWeixinMenu(accesstoken, menu);
//	}
}