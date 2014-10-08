<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>法律法规</title>
    <link rel="stylesheet" type="text/css" href="${path}/css/style.css"/>
</head>

<body>
<div class="wrap-all" style="width: 880px; font-family: '方正仿宋简体';">
    <div class="title" style="color: navy;">
        ${withub:convertToHtml(law.name)}
        <span class="title-tag"><fmt:formatDate value="${law.issueDate}" pattern="yyyy-MM-dd"/></span>
    </div>

    <div class="content" style="font-size: 22px;">
        <withub:commonText entity="${law}"/>
    </div>

    <c:if test="${!empty law.attachmentList}">
        <div class="attach">
            <h5>附件</h5>
            <ul>
                <c:forEach var="attachment" items="${law.attachmentList}">
                    <li>
                        <a class="file-download"
                           href="${path}/std/file/download?fileInfoId=${attachment.objectId}">${attachment.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <jsp:include page="/WEB-INF/pages/additional.jsp"/>
</div>
</body>
</html>