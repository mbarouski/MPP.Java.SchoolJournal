<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Roles</title>
    <link rel="stylesheet" href="resources/styles/roles.css" />
</head>
<body>
<ul>
    <c:forEach items="${roles}" var="role">
        <li>
            <div>
                <h3>${role.name}</h3>
                <p>${role.level}</p>
            </div>
        </li>
    </c:forEach>
</ul>
</body>
</html>
