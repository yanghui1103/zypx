<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><%@ include file="/include.inc.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<script type="text/javascript">  
$(function(){ 
	renderAuthorityOperateBtnAll($("#panelBar",navTab.getCurrentPanel()),"getOperationsByMenuId","202",false,"panelBar"); 
});

</script>

<c:import url="../_frag/pager/pagerForm.jsp"></c:import>
<form method="post" rel="pagerForm"   onsubmit="return navTabSearch(this)">
<div class="pageHeader">
	<div class="searchBar">
		<ul class="searchContent">
			<li>
				<label>关键词：</label>
				<input type="text"  value="${param.keyWords}"     name="keyWords" />
			</li> 
		</ul>
		<div class="subBar">
			<ul>						
				<li><div class="buttonActive"><div class="buttonContent"><button type="submit">查询</button></div></div></li>
			</ul>
		</div> 
	</div>
</div>
</form>
<div class="pageContent">
	<div class="panelBar" id="panelBar">
	</div>
	<table class="table" width="100%" layoutH="133">
		<thead>
			<tr>
				<th width="10">序号</th>
				<th width="200">名称</th> 
				<th width="100">处理人</th> 
				<th width="100">页面</th>  
				<th width="100">查看状态</th>  
			</tr>
		</thead>
		<tbody>
		<c:forEach var="item" items="${todolist}" varStatus="s">
			<tr target="id"  rel="${item.fdid}">
				<td>${s.index+1}</td>
				<td>${item.task_title}</td>
				<td>${item.assignee}</td>  
				<td><a href="${item.url}/${item.fdid}" target=navTab>办理</a></td>
				<td><a href="flowController/openActivitiProccessImagePage/95012" target=navTab>当前状态</a></td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

	<c:import url="../_frag/pager/panelBar.jsp"></c:import>
</div>