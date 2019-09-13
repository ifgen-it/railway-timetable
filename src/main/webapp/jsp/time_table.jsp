<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Railway Timetable</title>
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/css/style.css">
    <link rel="shortcut icon" type="image/x-icon" href="${pageContext.request.contextPath}/resources/img/favicon.ico">

    <script type="text/javascript">

        // INIT WEB SOCKET
        var ws = new WebSocket("ws://localhost:8081/railway-timetable-1.0-SNAPSHOT/websocket");

        // HANDLERS
        ws.onopen = function (event) {
            console.log('WS : Connection opened');
        };
        ws.onerror = function (event) {
            console.log('WS : Error occurred');
            alert('Error = ' + event.toString());
        };
        ws.onclose = function (event) {
            console.log('WS : Connection closed');
        };
        ws.onmessage = function (event) {
            onMessage(event);
        };

        // FUNCTIONS
        function sendMessage() {    // WAS USED FOR TESTING
            var text = 'Hello Server!';
            ws.send(text);
            console.log(text);
            return false;
        }

        function onMessage(event) {

            console.log('WS : Message received : ' + event.data);

            // GOT THE LIST OF ARRIVALS OR DEPARTURES -- NEED TO INJECT IT INTO TABLE

            var tableArrivals = document.getElementById('table-ws-timetable-arrival');
            var tableDepartures = document.getElementById('table-ws-timetable-departure');

            var jsonArr = JSON.parse(event.data);
            var arrLength = jsonArr.length;

            // ERASE OLD INFO
            if (arrLength > 0) {
                var firstJson = jsonArr[0];
                if (firstJson.isArrival == true) {

                    tableArrivals.innerHTML =
                        '<tr>' +
                        '<th>' + 'Arrival time' + '</th>' +
                        '<th>' + 'Route' + '</th>' +
                        '<th>' + 'Train' + '</th>' +
                        '</tr>'
                    ;

                } else {

                    tableDepartures.innerHTML =
                        '<tr>' +
                        '<th>' + 'Departure time' + '</th>' +
                        '<th>' + 'Route' + '</th>' +
                        '<th>' + 'Train' + '</th>' +
                        '</tr>'
                    ;
                }
            }

            // FILL NEW INFO
            for (var i = 0; i < arrLength; i++) {
                var json = jsonArr[i];
                console.log(json);

                if (json.isArrival == true) {

                    tableArrivals.innerHTML +=
                        '<tr>' +
                        '<td>' + json.arrivalTime + '</td>' +
                        '<td>' + json.routeName + '</td>' +
                        '<td>' + json.trainName + '</td>' +
                        '</tr>'
                    ;
                } else {

                    tableDepartures.innerHTML +=
                        '<tr>' +
                        '<td>' + json.departureTime + '</td>' +
                        '<td>' + json.routeName + '</td>' +
                        '<td>' + json.trainName + '</td>' +
                        '</tr>'
                    ;
                }
            }

        }

        // TEST
        if ('WebSocket' in window) {
            console.log('test : WebSocket in window');
        }
    </script>


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
                <div class="dark-text">Timetable::${stationName}</div>

                <table id="table-ws-timetable-arr-dep">
                    <tr>
                        <th>ARRIVAL</th>
                        <th>DEPARTURE</th>
                    </tr>
                    <tr>
                        <td>
                                <%-- ARRIVALS --%>
                            <table id="table-ws-timetable-arrival" border="2">
                                <tr>
                                    <th>Arrival time</th>
                                    <th>Route</th>
                                    <th>Train</th>
                                </tr>

                                <c:forEach var="arrival" items="${arrivals}">
                                    <tr>
                                        <fmt:parseDate value="${arrival.arrivalTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                                        <td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${parsedDateTime}" /></td>

                                        <td>${arrival.routeName}</td>
                                        <td>${arrival.trainName}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </td>
                        <td>
                                <%-- DEPARTURES --%>
                            <table id="table-ws-timetable-departure" border="2">
                                <tr>
                                    <th>Departure time</th>
                                    <th>Route</th>
                                    <th>Train</th>
                                </tr>
                                <c:forEach var="departure" items="${departures}">
                                    <tr>
                                        <fmt:parseDate value="${departure.departureTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                                        <td><fmt:formatDate pattern="dd-MM-yyyy HH:mm" value="${parsedDateTime}" /></td>


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
