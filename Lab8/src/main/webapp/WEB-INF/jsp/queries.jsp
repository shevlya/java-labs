<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Запросы</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    <script src="${pageContext.request.contextPath}/autocomplete.js"></script>
</head>
<body>
<div class="container">
    <h2>Запросы</h2>
    <%@ include file="menu.jspf" %>

    <h3>Запрос 1. Композиции конкретного исполнителя</h3>
    <p class="hint-text">Таблицы: composition + album + artist</p>

    <form action="queries" method="get" class="search-form">
        <div class="form-group">
            <label for="artistSearch">Введите имя исполнителя:</label>
            <input type="text" id="artistSearch" name="artist"
                   value="<c:out value='${param.artist}'/>" autocomplete="off" placeholder="Например: Бах">
            <div id="autocomplete-list-query" class="autocomplete-items"></div>
        </div>
        <button type="submit">Показать</button>
    </form>

    <c:if test="${not empty param.artist}">
        <c:choose>
            <c:when test="${empty fullList}">
                <p class="error">Композиций исполнителя «<c:out value="${param.artist}"/>» не найдено.</p>
            </c:when>
            <c:otherwise>
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
                            <td><c:out value="${row[0]}"/></td>
                            <td>${row[1]} мин.</td>
                            <td><c:out value="${row[2]}"/></td>
                            <td><c:out value="${row[3]}"/></td>
                            <td><c:out value="${row[4]}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <hr class="divider">

    <h3>Запрос 2. Статистика по исполнителям</h3>
    <p class="hint-text">Таблицы: artist + album + composition</p>
    <table>
        <tr>
            <th>Исполнитель</th>
            <th>Кол-во альбомов</th>
            <th>Кол-во композиций</th>
            <th>Общая длительность (мин)</th>
        </tr>
        <c:forEach var="row" items="${stats}">
            <tr>
                <td><c:out value="${row[0]}"/></td>
                <td>${row[1]}</td>
                <td>${row[2]}</td>
                <td>${row[3] != null ? row[3] : 0}</td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    setupAutocomplete("artistSearch", "autocomplete-list-query", "artist", "${pageContext.request.contextPath}");
</script>
</body>
</html>
