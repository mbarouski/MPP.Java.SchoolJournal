<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<ul>
    <c:forEach items="${subjects}" var="subject">
        <li>
            <div>
                <h3>${subject.name}</h3>
                <p>${subject.description}</p>
            </div>
        </li>
    </c:forEach>
</ul>
</body>
</html>
