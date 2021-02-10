<%--
  Created by IntelliJ IDEA.
  User: nikitaklimkin
  Date: 10.02.2021
  Time: 19:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit meal</title>
    <meta charset="UTF-8">
</head>
<body>
    <form method="post" action="MealServlet", name="frameAddOrEditUser">
        Date and Time<input type="text", name="dateAndTime", value=""><br/>
        Description<input type="text", name="description", value=""><br/>
        Calories<input type="text", name="calories", value=""><br/>
        Enter<input type="submit", value="Submit">
    </form>
</body>
</html>
