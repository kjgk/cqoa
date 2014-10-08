<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>${document.documentType.name}</title>
    <link rel="stylesheet" type="text/css" href="${path}/css/style.css"/>
</head>

<body>

<div class="wrap-all" style="width: 800px;">
    <div class="title">
        ${document.lastHistory.name}
    </div>

    <div class="content">
        <ul>
            <li>所属单位：<span>${document.organization.name}</span></li>
            <li>编写时间：<span><fmt:formatDate value="${document.lastHistory.writeDate}" pattern="yyyy年MM月dd日"/></span></li>
            <li>备注：<span>${document.lastHistory.description}</span></li>
        </ul>
    </div>

    <c:if test="${!empty document.lastHistory.attachmentList}">
        <div class="attach">
            <h5>附件</h5>
            <ul>
                <c:forEach var="attachment" items="${document.lastHistory.attachmentList}">
                    <li>
                        <a class="file-download" href="${path}/std/file/download?fileInfoId=${attachment.objectId}">${attachment.name}</a>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <jsp:include page="/WEB-INF/pages/additional.jsp"/>
</div>
</body>
</html>