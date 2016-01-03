<%@ page pageEncoding="UTF-8"%>
<%@ page import="com.jadyer.sdk.demo.common.constant.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jsp:include page="/header.jsp"/>

<script>
function validateForm(){
	if(isEmpty($("#appid").val())){
		$.promptBox("请填入appid", "#ffb848");
	}else if(isEmpty($("#appsecret").val())){
		$.promptBox("请填入appsecret", "#ffb848");
	}else if(isEmpty($("#mpid").val())){
		$.promptBox("请填入公众号原始ID", "#ffb848");
	/*
	}else if($("#appAESStatus").val() != 0){
		$("#appAESStatus").attr("value", "0");
		$.promptBox("暂时只能使用明文模式", "#ffb848");
	*/
	}else{
		return true;
	}
}
function submit(){
	if(validateForm()){
		if("${userInfo.bindStatus}"=="0" || ("${userInfo.bindStatus}"!="0" && confirm("确定要重新绑定么？\r\n重绑过程中公众号将无法提供服务！！"))){
			$.post("${ctx}/user/bind",
				$("#userBindForm").serialize(),
				function(data){
					if(1000 == data.code){
						alert("操作成功！！\r\n请于公众号回复\“<%=Constants.MPP_BIND_TEXT%>\”完成绑定");
					}else{
						$.promptBox(data.message, "#ffb848");
					}
				}
			);
		}
	}
}
</script>

<div class="c_nav">
	<div class="ti">个人资料</div>
</div>
<!--Content-->
<div class="c_content">
	<!--Table order list-->
	<table class="tab_head tab_in tab_list2" width="100%">
		<tr class="ti"><th colspan="2">个人信息</th></tr>
		<tr><th width="15%">用户名：</th><td>${userInfo.username}</td></tr>
		<tr><th>平台类型：</th><td>${userInfo.mptype eq 1 ? '微信' : userInfo.mptype eq 2 ? 'QQ' : '未知'}</td></tr>
		<tr><th>绑解状态：</th><td>${userInfo.bindStatus eq 0 ? '<span class="cf30 fw">未绑定</span>' : '<span class="cgre fw">已绑定</span>'}</td></tr>
		<tr><th>绑解时间：</th><td><fmt:formatDate value="${userInfo.bindTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td></tr>
	</table>
	<!--/Table order list-->
	<form id="userBindForm">
		<input type="hidden" name="id" value="${uid}"/>
		<input type="hidden" name="bindStatus" value="0"/>
		<input type="hidden" name="token" value="${token}"/>
		<table class="tab_head tab_in tab_list2" width="100%">
			<tr class="ti"><th colspan="2">公众信息</th></tr>
			<tr><th width="15%">URL：</th><td>${mpurl}</td></tr>
			<tr><th>Token：</th><td>${token}</td></tr>
			<tr><th>appid：</th><td><input class="inpte" type="text" id="appid" name="appid" value="${userInfo.appid}" maxlength="32"/></td></tr>
			<tr><th>appsecret：</th><td><input class="inpte" type="text" id="appsecret" name="appsecret" value="${userInfo.appsecret}" maxlength="64"/></td></tr>
			<tr><th>原始ID：</th><td><input class="inpte" type="text" id="mpid" name="mpid" value="${userInfo.mpid}" maxlength="32"/></td></tr>
			<tr><th>公众号：</th><td><input class="inpte" type="text" id="mpno" name="mpno" value="${userInfo.mpno}" maxlength="32"/></td></tr>
			<tr><th>公众名：</th><td><input class="inpte" type="text" id="mpname" name="mpname" value="${userInfo.mpname}" maxlength="32"/></td></tr>
			<%--
			<tr>
				<th>微信加解密方式：</th>
				<td>
					<select id="appAESStatus" name="appAESStatus">
						<option value="0" ${userInfo.appAESStatus eq 0?'selected=selected':''}>明文模式</option>
						<option value="1" ${userInfo.appAESStatus eq 1?'selected=selected':''}>兼容模式</option>
						<option value="2" ${userInfo.appAESStatus eq 2?'selected=selected':''}>安全模式</option>
					</select>
				</td>
			</tr>
			--%>
		</table>
		<table class="tab_head tab_in tab_list2" width="100%">
			<tr class="ti"><th colspan="3">操作</th></tr>
			<tr><td class="txt_l"><a class="btn_g" href="javascript:submit();">绑定公众号</a></td></tr>
		</table>
	</form>
</div>
<!--/Content-->

<jsp:include page="/footer.jsp"/>