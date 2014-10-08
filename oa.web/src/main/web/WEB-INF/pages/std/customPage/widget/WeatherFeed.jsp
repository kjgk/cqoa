<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/tld/taglibs.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<style>
    .weatherFeed {
        width: 260px;
    }

    .weatherFeed a {
        color: #888;
    }

    .weatherFeed a:hover {
        color: #000;
        text-decoration: none;
    }

    .weatherItem {
        height: 160px;
        padding: 0.8em;
        text-align: right;
    }

    .weatherCity {
        text-transform: uppercase;
    }

    .weatherTemp {
        font-size: 2.8em;
        font-weight: bold;
    }

    .weatherDesc, .weatherCity, .weatherForecastDay {
        font-weight: bold;
        margin-right: 1.5em;
    }

    .weatherDesc {
        margin-bottom: 0.4em;
    }

    .weatherRange, .weatherWind, .weatherLink, .weatherForecastItem {
        font-size: 0.8em;
    }

    .weatherLink, .weatherForecastItem {
        margin-top: 0.5em;
        text-align: left;
    }

    .weatherForecastItem {
        padding: 0.5em 0.5em 0.5em 80px;
        background-color: #fff;
        background-position: left center;
    }

    .weatherForecastDay {
        font-size: 1.1em;
    }

    .odd {
        background-color: #e8e8fc;
    }

    .even {
        background-color: #d4d4e8;
    }
</style>
<script type="text/javascript">
    $(function () {
        $('#${param.id}').weatherfeed(['CHXX0116'], {
            wind: false,
            link: false,
            city: '上海'
        });
    })
</script>
<div id="${param.id}" style="width: ${param.width}px; height: ${param.height}px"></div>