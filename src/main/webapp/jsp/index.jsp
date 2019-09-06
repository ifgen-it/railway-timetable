<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Railway Timetable</title>
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/style.css">
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/img/favicon.ico">
</head>
<body>
<div class="wrapper">
    <header>Trains and Rails :: Timetable</header>

    <div id="content-index">
        <div class="dark-text">Upload stations from ${server}</div>

        <form method="post">
            <input type="submit" value="Upload">
        </form>

        <c:if test="${stations == null}">
            <div class="text-bad-news-small">Stations not loaded</div>
        </c:if>

        <c:if test="${stations != null}">
            <div class="dark-text-small">Select station to display timetable:</div>

            <div class="form-element">
                <select name="stationId" class="form-element">
                    <c:forEach var="station" items="${stations}">
                        <option value="${station.stationId}">${station.stationName}</option>
                    </c:forEach>
                </select>
            </div>

        </c:if>

    </div>

</div>


</body>
</html>
