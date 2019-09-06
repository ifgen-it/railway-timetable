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


    <div id="content-table-timetable">

        <c:if test="${connectionError == true}">
            <div class="text-bad-news">Server is not responding. Make sure that server is running</div>
        </c:if>
        <c:if test="${connectionError == null}">


            <c:if test="${stationIdError == true}">
                <div class="text-bad-news">Something went wrong with fetching timetable</div>
            </c:if>

            <c:if test="${stationIdError == false && notFoundStationName == true}">
                <div class="text-bad-news">Selected station is not real</div>
            </c:if>

            <c:if test="${stationIdError == false && notFoundStationName == null}">
                <div class="dark-text">Timetable :: ${stationName}</div>
                <table id="table-timetable-arr-dep">

                    <tr>
                        <th>ARRIVAL</th>
                        <th>DEPARTURE</th>
                    </tr>
                    <tr>
                            <%--                    Arrivals --%>
                        <td>
                            <table id="table-timetable-arrival" border="2">
                                <tr>
                                    <th>Arrival time</th>
                                    <th>Route</th>
                                    <th>Train</th>

                                </tr>

                                <c:forEach var="arrival" items="${arrivals}">
                                    <tr>
                                        <td>${arrival.arrivalTime}</td>
                                        <td>${arrival.routeName}</td>
                                        <td>${arrival.trainName}</td>

                                    </tr>
                                </c:forEach>

                            </table>
                        </td>
                            <%--    Departures --%>
                        <td>
                            <table id="table-timetable-departure" border="2">
                                <tr>
                                    <th>Departure time</th>
                                    <th>Route</th>
                                    <th>Train</th>

                                </tr>

                                <c:forEach var="departure" items="${departures}">
                                    <tr>
                                        <td>${departure.departureTime}</td>
                                        <td>${departure.routeName}</td>
                                        <td>${departure.trainName}</td>

                                    </tr>
                                </c:forEach>

                            </table>
                        </td>
                    </tr>
                </table>

            </c:if>
        </c:if>

    </div>

</div>


</body>
</html>
