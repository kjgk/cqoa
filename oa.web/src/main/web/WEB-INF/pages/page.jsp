<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title></title>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/resources/css/ext-all-neptune.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/ux/css/CheckHeader.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/userSearchField.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/simpleSearchFiled.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/yearView.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/extensible/resources/css/calendar.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/extensible/resources/css/calendar-colors.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/extensible/resources/css/extensible-all.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/extensible/resources/css/recurrence.css"/>

    <script type="text/javascript" src="${path}/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${path}/scripts/swfupload/swfupload.js"></script>
    <script type="text/javascript" src="${path}/scripts/swfupload/swfupload.speed.js"></script>
    <script type="text/javascript" src="${path}/scripts/swfupload/swfupload.queue.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/ux/IFrame.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/common.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/util/RendererUtil.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/util/ui.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/util/string.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/extensible/lib/extensible-all.js"></script>

    <script type="text/javascript" src="${path}/scripts/angularjs/angular.min.js"></script>

    <script type="text/javascript">
        PageContext.contextPath = '${path}';
        PageContext.currentUser = window.parent.PageContext.currentUser;
    </script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/ex.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/setup.js"></script>
    <script type="text/javascript">
        Ext.onReady(function () {
            var queryString_ = '${pageContext.request.queryString}';
            var pageParams_ = {};
            if (queryString_ != '') {
                Ext.each(queryString_.split('&'), function (str) {
                    pageParams_[str.split('=')[0]] = str.split('=')[1];
                });
            }
            var page = Ext.create('${page}', pageParams_);
            if (page instanceof  Ext.window.Window) {
                page.show();
            }
        });
    </script>
</head>
<body oncontextmenu='return false;'>
</body>
</html>