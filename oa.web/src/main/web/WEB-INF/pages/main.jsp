<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>后台管理-重庆市第二中级人民法院办公系统</title>
    <link rel="stylesheet" type="text/css" href="${path}/scripts/extjs/resources/css/ext-all-neptune.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/icon.css"/>
    <link rel="stylesheet" type="text/css" href="${path}/css/default.css"/>

    <script type="text/javascript" src="${path}/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/ext-all.js"></script>
    <script type="text/javascript" src="${path}/scripts/extjs/locale/ext-lang-zh_CN.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/common.js"></script>
    <script type="text/javascript" src="${path}/scripts/poshytip/jquery.poshytip.js"></script>
    <script type="text/javascript">
        PageContext.contextPath = '${path}';
    </script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/ex.js"></script>
    <script type="text/javascript" src="${path}/scripts/withub/ext/setup.js"></script>
    <script type="text/javascript">
        Ext.onReady(function () {
            // 加载当前登录用户信息
            ExtUtil.request({
                async: false,
                url: PageContext.contextPath + '/security/getCurrentUserInfo',
                success: function (result) {
                    var currentUser = result['userInfo'];
                    PageContext.currentUser = currentUser;
                    Ext.get('title-username').update(currentUser.name);
                }
            });
            Ext.create('withub.ext.Main', {});
            Ext.get('header').show();

            $('#username').poshytip({
                className: 'tip-twitter',
                bgImageFrameSize: 9,
                showTimeout: 1,
                alignTo: 'target',
                alignX: 'center',
                offsetY: 0,
                showOn: 'click',
                slide: false,
                content: function (updateCallback) {
                    return '<a href="#" onclick="updatePassword();" class="tip-link tip-link-icon-1">修改密码</a>' +
                            '<a href="#" onclick="logoutSystem();" class="tip-link tip-link-icon-2">注销/切换</a>';
                }
            });

        });

        function logoutSystem() {
            ExtUtil.Msg.confirm('确定要注销吗?', function (select) {
                if (select == 'yes') {
                    window.location.href = "${path}/j_spring_security_logout";
                }
            });
        }

        function exitSystem() {
            ExtUtil.Msg.confirm('确定要退出系统吗?', function (select) {
                if (select == 'yes') {
                    window.open('', '_self', '');
                    window.close();
                }
            });
        }

        function updatePassword() {
            Ext.create('withub.ext.system.user.PasswordChange', {
            }).show()
        }

        function getdatetime() {
            var today = new Date();
            var day = today.getDay();
            var year = today.getFullYear();
            if (day == 0) {
                day = ' 星期日';
            } else if (day == 1) {
                day = ' 星期一';
            } else if (day == 2) {
                day = ' 星期二';
            } else if (day == 3) {
                day = ' 星期三';
            } else if (day == 4) {
                day = ' 星期四';
            } else if (day == 5) {
                day = ' 星期五';
            } else if (day == 6) {
                day = ' 星期六';
            }
            return document.write("今天是" + year + '年' + (today.getMonth() + 1) + '月'
                    + today.getDate() + '日' + day);
        }
    </script>
</head>
<body oncontextmenu='return false;'>
<div id="main"></div>
<div id="header">
    <div style="height: 80px; background:#3892D3; background-repeat: no-repeat;">
        <h2 style="color: #ffffff; margin: 0; padding: 18px 32px; font-size: 32px; display: inline-block;">重庆市第二中级人民法院办公系统</h2>
        <div id="top">
            <div style="float: left;">
                <script>
                    getdatetime();
                </script>
            </div>
            <div style="float: left; margin-left: 12px;">当前用户：</div>
            <div id="username">
                <a href="#">
                    <span id="title-username"></span>&nbsp;<img src="${path}/images/23.png"/>
                </a>
            </div>
            <a href="javascript:exitSystem();" title="退出">
                <div style="width: 28px; height: 32px; margin-left: 12px; background: url('${path}/images/exit.png'); float: left;"></div>
            </a>
        </div>
    </div>
</div>
</body>
</html>