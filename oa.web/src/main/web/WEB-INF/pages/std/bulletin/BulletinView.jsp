<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>公告通知阅读</title>
    <link rel="stylesheet" type="text/css" href="${path}/css/style.css"/>
</head>

<body>
<div class="wrap-all" style="width: 880px; font-family: '方正仿宋简体';">
    <div class="title" style="color: navy;">
        <h5>主题</h5>  ${bulletin.title}
        <span class="title-tag"><fmt:formatDate value="${bulletin.issueTime}" pattern="yyyy-MM-dd HH:mm"/></span>
    </div>

    </br>
    <h5>内容</h5>
    <div class="content" style="font-size: 22px;">
          ${bulletin.content}
    </div>

    <c:if test="${!empty bulletin.attachmentList}">
        <div class="attach">
            <h5>附件</h5>
            <ul>
                <c:forEach var="attachment" items="${bulletin.attachmentList}">
                    <li>
                        <a class="file-download"
                           href="${path}/std/file/download?fileInfoId=${attachment.objectId}">${attachment.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

</div>
</body>
</html>