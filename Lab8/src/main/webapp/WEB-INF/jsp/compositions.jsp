<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Композиции</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    <script src="${pageContext.request.contextPath}/autocomplete.js"></script>
</head>
<body>
<div class="container">
    <h2>Композиции</h2>
    <%@ include file="menu.jspf" %>

    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <form action="compositions" method="post">
        <h3>Добавить композицию</h3>
        <input type="hidden" name="action" value="${not empty editComposition ? 'update' : 'add'}">
        <c:if test="${not empty editComposition}">
            <input type="hidden" name="id" value="${editComposition.id}">
        </c:if>

        <div class="form-group">
            <label for="compositionName">Название композиции:</label>
            <input type="text" id="compositionName" name="name"
                   value="<c:out value='${editComposition.name}'/>" required>
        </div>
        <div class="form-group">
            <label for="duration">Длительность (мин):</label>
            <input type="number" id="duration" name="duration"
                   value="${editComposition.duration}" min="1" required>
        </div>
        <div class="form-group">
            <label for="albumName">Альбом:</label>
            <input type="text" id="albumName" name="album"
                   value="<c:out value='${editComposition.album.name}'/>" autocomplete="off" required>
            <div id="autocomplete-list" class="autocomplete-items"></div>
        </div>

        <button type="submit">${not empty editComposition ? 'Обновить' : 'Добавить'}</button>
        <c:if test="${not empty editComposition}"><a href="compositions" class="btn-cancel">Отмена</a></c:if>
    </form>

    <h3>Список композиций:</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Длительность</th>
            <th>Альбом</th>
            <th>Действия</th>
        </tr>
        <c:forEach var="comp" items="${compositions}">
            <tr>
                <td>${comp.id}</td>
                <td><c:out value="${comp.name}"/></td>
                <td>${comp.duration} мин.</td>
                <td><c:out value="${comp.album.name}"/></td>
                <td>
                    <a href="compositions?edit=${comp.id}">Изменить</a> |
                    <form action="compositions" method="post" class="inline-form"
                          onsubmit="return confirm('Удалить эту композицию?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${comp.id}">
                        <button type="submit" class="btn-danger">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    setupAutocomplete("albumName", "autocomplete-list", "album", "${pageContext.request.contextPath}");
</script>
</body>
</html>
