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
    <h1>Meals</h1>
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
                <c:if test="${mealWithExcess.excess == true}">
                    <c:set var="color" value=" red"/>
                </c:if>
                <c:if test="${mealWithExcess.excess == false}">
                    <c:set var="color" value=" green"/>
                </c:if>>
                <tr style="color:${color}">
                    <td style="height: 35px;width: 150px"><fmt:parseDate value="${mealWithExcess.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                        <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" /></td>
                    <td style="height: 35px;width: 200px"><c:out value="${mealWithExcess.description}" /></td>
                    <td style="height: 35px;width: 100px"><c:out value="${mealWithExcess.calories}" /></td>
                    <td><a href="meals?action=edit&id=<c:out value="${mealWithExcess.id}"/>">Update</a></td>
                    <td><a href="meals?action=delete&id=<c:out value="${mealWithExcess.id}"/>">Delete</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
