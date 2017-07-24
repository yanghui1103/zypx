<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.bw.fit.common.util.*"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type"
	content="application/x-www-form-urlencoded; charset=UTF-8">
<script type="text/javascript">
	$(function() {
		$("#UUID", navTab.getCurrentPanel()).val(getUUID());

	});
	$("button", navTab.getCurrentPanel()).click(function() {
		dwzConfirmFormToBack("是否确认新建组织?", function() {
			$('#parent_company_id', navTab.getCurrentPanel()).val($('#topIds', navTab.getCurrentPanel()).val());
			$("#createCompanyFm", navTab.getCurrentPanel()).submit();
		}, function() {
		});
	});
	function openSysAddress() {
		openSysAddressBook($('#UUID', navTab.getCurrentPanel()).val(), 22221,
				'0', $('#selectIds', navTab.getCurrentPanel()).val());
	}
</script>
</head>
<body>
	<div class="pageContent">
		<form id="createCompanyFm" name="company" method=post
			action="<%=basePath%>system/createCompany?navTabId=page100&callbackType=closeCurrent"
			class="pageForm required-validate"
			onsubmit="return validateCallback(this,navTabAjaxDone);">
			<div class="pageFormContent" layoutH="56">
				<p>
					<label>机构名称：</label> <input name="company_name" class="required"
						type="text" size="30" maxlength=30 />
				</p>
				<p>
					<label>机构地址：</label> <input name="company_address" type="text"
						size="30" maxlength=30 />
				</p>
				<p>
					<label>机构类型：</label> <select id="company_type_id"  name="company_type_id"
						class="required combox">
						<option selected value="">请选择</option>
						<c:forEach var="item" items="${OrgTypeList}" varStatus="s">
							<option value="${item.dict_value}">${item.dict_name}</option>
						</c:forEach>
					</select>
				</p>
				<p>

					<label>上级机构：</label><input type="text" class="required" style="float: left" readonly lookupGroup="orgLookup1" name="orgLookup1.names"   />
					<input type="hidden" id="topIds" lookupGroup="orgLookup1"  name="orgLookup1.ids"   />
					<input name="parent_company_id" id="parent_company_id" type="hidden">
					<a href='<%=basePath %>system/openSysAddressBook/0/22221/0/${digitId}/1' target="dialog"
						mask=true maxable=false mixable=false minable=false resizable=false drawable=true  
						 width="543" height="750" max="false"  lookupGroup="orgLookup1" title="地址本" class=btnLook ></a>
				</p>
				<p>
					<label>序号：</label> <input name="company_order"
						class="required digits" type="text" size="30" maxlength=3 />
				</p>				
			</div>
			
			<input id="UUID" name="UUID" type="hidden" /> <input id="selectIds"
				type="hidden" />			
			<input  name="fdid" value="${digitId}" type="hidden" /> 	
			<div class="formBar" id="panelBar">
				<ul>
					<li><div class="buttonActive">
							<div class="buttonContent">
								<button type=button>保存</button> 
							</div>
						</div></li>
				</ul>
			</div>
		</form>
	</div>
</body>
</html>