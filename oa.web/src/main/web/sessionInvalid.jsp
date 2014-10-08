<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
    if (window.parent) {
        window.parent.location.href = '${path}/security/login.page';
    } else {
        window.location.href = '${path}/security/login.page';
    }
</script>