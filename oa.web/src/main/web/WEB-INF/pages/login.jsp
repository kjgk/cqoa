<%@ page import="com.withub.web.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<title>重庆市第二中级人民法院办公系统</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<link charset="utf-8" rel="stylesheet" href="${path}/css/login.css"/>
<script type="text/javascript" src="${path}/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${path}/scripts/jquery/jquery.form.js"></script>
<script type="text/javascript">
    if (window.top != window.self) {
        window.top.location.href = 'login.page';
    }
</script>
<script type="text/javascript">
    var passwordInvalidCount = <%= session.getAttribute(Constants.PASSWORD_INVALID_COUNT_KEY) == null ? 0 : session.getAttribute(Constants.PASSWORD_INVALID_COUNT_KEY)%>;
    var maxPasswordInvalidCount = 3;
    var submitFlag = false;
    function submitForm() {
        if (submitFlag) {
            return;
        }
        var username = $('#username');
        var password = $('#password');
        $('#loginForm').ajaxSubmit({
            beforeSubmit: function () {
                if ($.trim(username.val()) == "" || $.trim(password.val()) == "") {
                    alert('用户名或密码不能为空，请正确填写！')
                    return false;
                }
                username.attr('disabled', true);
                password.attr('disabled', true);
                submitFlag = true;
            },
            success: function (response) {
                submitFlag = false;
                username.attr('disabled', false);
                password.attr('disabled', false);
                if (response.success) {
                    window.location.href = '${path}/index.jsp';
                } else {
                    if (response['resultCode'] == '<%= Constants.ACCOUNT_LOCKED%>') {
                        alert('账号被锁！')
                        if (passwordInvalidCount >= maxPasswordInvalidCount) {
                        }

                    } else if (response['resultCode'] == '<%= Constants.PASSWORD_INVALID%>') {
                        alert('用户名密码不匹配，请重新输入！');
                        password.focus().get(0).select();
                        passwordInvalidCount++;
                        if (passwordInvalidCount >= maxPasswordInvalidCount) {
                        }
                    }
                }
            },
            dataType: 'json'
        });
    }

    $(function () {
        $('#username').focus();
    });
</script>
</head>
<body style=" background:url(${path}/images/login/login_1.png) repeat-x #2488d2;">

<form id="loginForm" action="${path}/securityLogin" method="post" onsubmit="return false;">


    <div class="dlk">
        <div class="user">

            用户名：<input id="username" name="username" value="" placeholder="请输入用户名" tabindex="1" type="text"
                       autocomplete="off"
                       maxlength="20" onKeyPress="if(event.keyCode==13){submitForm();}"/><br/>
            密　码：<input id="password" name="password" value=""
                       placeholder="请输入登录密码" tabindex="1" type="password"
                       autocomplete="off"
                       maxlength="20"
                       onKeyPress="if(event.keyCode==13){submitForm();}"/>
            <div class="btn">
                <img src="${path}/images/login/login_2.png" onclick="submitForm();"/><img src="${path}/images/login/login_3.png"/>
            </div>
        </div>

    </div>
</form>


</body>
</html>
