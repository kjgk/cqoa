<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>中国石油广东石化公司项目HSE管理系统</title>
    <script type="text/javascript" src="${path}/js/util/jquery.js"></script>
    <script type="text/javascript">
        var contextPath = '${path}';
        $(document).ready(function () {
            $('#taskList a').each(function () {
                var page = $(this).attr('page');
                var taskId = $(this).attr('taskId');
                var objectId = $(this).attr('objectId');

                $(this).bind('click', function () {
                    var url = contextPath + '/servlet/extPage?page=' + page + '&taskId=' + taskId + '&objectId=' + objectId + '&action=sumbit';
                    window.open(url, '中国石油广东石化公司项目HSE管理系统', 'width=980,height=640');
                });
            });
        });
    </script>
    <style type="text/css">
        body {
            padding: 0px;
            margin: 0px;
            font-family: '方正仿宋简体'
        }

        #taskList {
            padding: 5px 0px 0px 0px;
        }

        #taskList h4 {
            font-weight: 100;
            font-size: 13px;
            padding: 3px;
            margin: 0px;
        }

        #taskList h4 a {
            color: #0f0f0f;
            text-decoration: none;
        }

        #taskList h4 a:hover {
            color: #003399;
            text-decoration: underline;
        }

        #taskList .empty {
            text-align: center;
            color: #6f6f6f;
            padding-top: 10px;
        }
    </style>
</head>
<body>
<div id="taskList">
    <c:if test="${empty taskList}">
        <div class="empty">当前无待办事宜</div>
    </c:if>
    <c:forEach var="task" items="${taskList}">
        <h4>
            ·<a style="cursor: pointer;" page="${task.activity}" taskId="${task.objectId}"
                objectId="${task.relatedObjectId}">${task.instanceName}</a>
            <span style="color: red;">[待处理]</span><span>（<fmt:formatDate value="${task.taskArriveTime}"
                                                                         pattern="yyyy/MM/dd"/>）</span>
        </h4>
    </c:forEach>
</div>
</body>
</html>


