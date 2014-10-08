<%@ page import="com.withub.web.common.Constants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<title></title>
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
                    $('#error-box').html('用户名或密码不能为空，请正确填写！').show();
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
                        $('#error-box').html('账号被锁！').show();
                        if (passwordInvalidCount >= maxPasswordInvalidCount) {
                        }

                    } else if (response['resultCode'] == '<%= Constants.PASSWORD_INVALID%>') {
                        $('#error-box').html('用户名密码不匹配，请重新输入！').show();
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
<body>

<div id="top">
</div>

<div id="main">
    <div class="login-box">
        <div style="top: 20px; position: absolute; display: block; right: 0px;">
            <div class="ui-login-aside">

                <div class="ui-form-title">
                    <h1>登录</h1>
                </div>

                <div class="error-box fn-hide" id="error-box"></div>

                <div class="login-form-cnt">
                    <form id="loginForm" action="${path}/securityLogin" method="post" onsubmit="return false;">
                        <fieldset>
                            <div class="fm-item ui-form-item sl-account sl-chacha-parent">
                                <label class="fm-label ui-form-label" for="username">用户名：</label>
                                <input id="username" name="username" value="" placeholder="请输入用户名" class="i-text ui-input" tabindex="1" type="text"
                                       autocomplete="off"
                                       maxlength="20" onKeyPress="if(event.keyCode==13){submitForm();}"/>
                                <%--<span class="sl-chacha"></span>--%>

                                <div class="ui-form-explain"></div>
                            </div>

                            <div class="fm-item ui-form-item sl-aliedit">
                                <label class="fm-label pwd-label ui-form-label" desc="登录密码">登录密码：</label>
                        <span class="fm-link">
                            <a id="forgetPsw" style="cursor: pointer;"><%--忘记登录密码？--%></a>
                        </span>
                                <input id="password" name="password" value="" placeholder="请输入登录密码" class="i-text ui-input" tabindex="1" type="password"
                                       autocomplete="off"
                                       maxlength="20" onKeyPress="if(event.keyCode==13){submitForm();}"/>
                                <%--<span class="sl-chacha"></span>--%>

                                <div class="ui-form-explain"></div>
                            </div>

                            <div class="fm-item ui-form-item ui-btn-cnt">
                                <input id="btn-submit" type="submit" class="btn-login" tabindex="4" value="登  录" onclick="submitForm();"/>
                            </div>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<div id="bottom">
</div>
</body>
</html>
