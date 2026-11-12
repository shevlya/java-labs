<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Ошибка</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2>Ошибка при обработке запроса</h2>
    <p class="error"><c:out value="${pageContext.exception.message}"/></p>
    <p><a href="${pageContext.request.contextPath}/">Вернуться на главную</a></p>
</div>
</body>
</html>
