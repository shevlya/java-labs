<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Сложные запросы</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2><a href="index.jsp">← На главную</a> | Сложные запросы (Задание 3)</h2>

    <h3>Запрос 1: Все композиции с указанием альбома, жанра и исполнителя (3 таблицы)</h3>
    <table>
        <tr>
            <th>Композиция</th>
            <th>Длительность</th>
            <th>Альбом</th>
            <th>Жанр</th>
            <th>Исполнитель</th>
        </tr>
        <c:forEach var="row" items="${fullList}">
            <tr>
                <td>${row[0]}</td>
                <td>${row[1]} мин.</td>
                <td>${row[2]}</td>
                <td>${row[3]}</td>
                <td>${row[4]}</td>
            </tr>
        </c:forEach>
    </table>

    <h3>Запрос 2: Статистика по исполнителям (кол-во альбомов, композиций и общая длительность)</h3>
    <table>
        <tr>
            <th>Исполнитель</th>
            <th>Кол-во альбомов</th>
            <th>Кол-во композиций</th>
            <th>Общая длительность (мин)</th>
        </tr>
        <c:forEach var="row" items="${stats}">
            <tr>
                <td>${row[0]}</td>
                <td>${row[1]}</td>
                <td>${row[2]}</td>
                <td>${row[3] != null ? row[3] : 0}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
