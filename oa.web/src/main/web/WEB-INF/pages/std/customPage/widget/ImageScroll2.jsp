<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<style>

    .bannerwarp {
        width: ${param.width}px;
        height: ${param.height}px;
        overflow: hidden;
        margin: 0px auto;
    }

    .banner img {
        border: 0px;
        width: ${param.width}px;
        height: ${param.height}px;
    }

    .banner {
        width: ${param.width}px;
        height: ${param.height}px;
        clear: both;
        overflow: hidden;
        position: relative;
        float: left;
    }

    .banner ul {
        width: ${param.width}px;
        height: ${param.height}px;
        float: left;
        position: absolute;
        clear: both;
        padding: 0px;
        margin: 0px;
    }

    .banner ul li {
        float: left;
        width: ${param.width}px;
        height: ${param.height}px;
        overflow: hidden;
        position: relative;
        padding: 0px;
        margin: 0px;
    }

    .banner .preNext {
        width: ${param.width/2}px;
        height: ${param.height}px;
        position: absolute;
        top: 0px;
        cursor: pointer;
    }

    .banner .pre {
        left: 0;
        background: url('${path}/scripts/simplefoucs/images/sprite.png' ${path} /scripts/simplefoucs/images/sprite.png ') no-repeat left center; } .banner .next { right: 0; background: url('${path}/scripts/simplefoucs/images/sprite1.png' ${path} /scripts/simplefoucs/images/sprite1.png ') no-repeat right center; }
</style>
<script type="text/javascript">
    $(function () {
        var sWidth = $(".banner").width();
        var len = $(".banner ul li").length;
        var index = 0;
        var picTimer;
        var btn = "<div class='btnBg'></div><div class='btn'>";
        for (var i = 0; i < len; i++) {
            btn += "<span></span>";
        }
        btn += "</div><div class='preNext pre'></div><div class='preNext next'></div>";
        $(".banner").append(btn);
        $(".banner .btnBg").css("opacity", 0);
        $(".banner .btn span").css("opacity", 0.4).mouseenter(function () {
            index = $(".banner .btn span").index(this);
            showPics(index);
        }).eq(0).trigger("mouseenter");
        $(".banner .preNext").css("opacity", 0.0).hover(function () {
            $(this).stop(true, false).animate({ "opacity": "0.5" }, 300);
        }, function () {
            $(this).stop(true, false).animate({ "opacity": "0" }, 300);
        });
        $(".banner .pre").click(function () {
            index -= 1;
            if (index == -1) {
                index = len - 1;
            }
            showPics(index);
        });
        $(".banner .next").click(function () {
            index += 1;
            if (index == len) {
                index = 0;
            }
            showPics(index);
        });
        $(".banner ul").css("width", sWidth * (len));
        $(".banner").hover(function () {
            clearInterval(picTimer);
        }, function () {
            picTimer = setInterval(function () {
                showPics(index);
                index++;
                if (index == len) {
                    index = 0;
                }
            }, 2800);
        }).trigger("mouseleave");
        function showPics(index) {
            var nowLeft = -index * sWidth;
            $(".banner ul").stop(true, false).animate({ "left": nowLeft }, 300);
            $(".banner .btn span").stop(true, false).animate({ "opacity": "0.4" }, 300).eq(index).stop(true, false).animate({ "opacity": "1" }, 300);
        }
    });
</script>
<!-- 代码 开始 -->
<div class="bannerwarp">
    <div class="banner">
        <ul>
            <li><a href="#" target="_blank">
                <img src="${path}/scripts/simplefoucs/images/bn1.jpg" alt=""/></a></li>
            <li><a href="#" target="_blank">
                <img src="${path}/scripts/simplefoucs/images/bn2.jpg" alt=""/></a></li>
            <li><a href="#" target="_blank">
                <img src="${path}/scripts/simplefoucs/images/bn3.jpg" alt=""/></a></li>
            <li><a href="#" target="_blank">
                <img src="${path}/scripts/simplefoucs/images/bn4.jpg" alt=""/></a></li>
        </ul>
    </div>
</div>
<!-- 代码 结束 -->