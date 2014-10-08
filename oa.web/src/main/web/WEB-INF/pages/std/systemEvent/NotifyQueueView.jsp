<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>通知队列</title>
    <link rel="stylesheet" type="text/css" href="${path}/css/style.css"/>
</head>

<body>

<div class="wrap-all" style="width: 800px;">
    <div class="title">
        通知队列
    </div>

    <div class="content">
        <ul>
            <li>接收人：<span>${notifyQueue.notifyQueueInfo.receiver}</span></li>
            <li>内容：<span>${notifyQueue.notifyQueueInfo.content}</span></li>
        </ul>
    </div>

    <jsp:include page="/WEB-INF/pages/additional.jsp"/>
</div>
</body>
</html>