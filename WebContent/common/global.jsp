<%@ page contentType="text/html; charset=UTF-8" import="com.bw.fit.common.util.*" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,org.apache.commons.lang3.StringUtils,org.apache.commons.lang3.ObjectUtils" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<script type="text/javascript">
	var ctx = "<%=PropertiesUtil.getValueByKey("system.default.url") %>";
</script>