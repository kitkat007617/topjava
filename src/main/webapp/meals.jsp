<%--
  Created by IntelliJ IDEA.
  User: nikitaklimkin
  Date: 06.02.2021
  Time: 18:23
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
    <table border="1">
        <thead>
            <tr>
                <th>Date</th>
                <th>Description</th>
                <th>Calories</th>
                <th></th>
                <th></th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="mealWithExcess" items="${requestScope.mealsWithExcess}">
                <tr>
                    <td><fmt:parseDate value="${mealWithExcess.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
                    <td><c:out value="${mealWithExcess.description}" /></td>
                    <td><c:out value="${mealWithExcess.calories}" /></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
