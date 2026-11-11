<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Исполнители</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    <script src="${pageContext.request.contextPath}/autocomplete.js"></script>
</head>
<body>
<div class="container">
    <h2><a href="${pageContext.request.contextPath}/">На главную</a> | Управление Исполнителями</h2>
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form action="artists" method="post">
        <c:choose>
            <c:when test="${empty editArtist}">
                <h3>Добавить исполнителя</h3>
                <input type="hidden" name="action" value="add">
            </c:when>
            <c:otherwise>
                <h3>Изменить исполнителя (ID: ${editArtist.id})</h3>
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${editArtist.id}">
            </c:otherwise>
        </c:choose>

        <div class="form-group">
            <label>Имя исполнителя:</label>
            <input type="text" id="artistName" name="name" value="${editArtist.name}" autocomplete="off" required>
            <div id="autocomplete-list" class="autocomplete-items"></div>
        </div>

        <button type="submit">Сохранить</button>
        <c:if test="${not empty editArtist}"><a href="artists" class="btn-cancel">Отмена</a></c:if>
    </form>

    <h3>Список исполнителей:</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Имя</th>
            <th>Действия</th>
        </tr>
        <c:forEach var="artist" items="${artists}">
            <tr>
                <td>${artist.id}</td>
                <td>${artist.name}</td>
                <td>
                    <a href="artists?edit=${artist.id}">Изменить</a> |
                    <form action="artists" method="post" class="inline-form"
                          onsubmit="return confirm('Удалить исполнителя и все его альбомы/композиции (CASCADE)?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${artist.id}">
                        <button type="submit" class="btn-danger">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    setupAutocomplete("artistName", "autocomplete-list", "artist");
</script>
</body>
</html>
