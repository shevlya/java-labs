<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" errorPage="error.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Альбомы</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
    <script src="${pageContext.request.contextPath}/autocomplete.js"></script>
</head>
<body>
<div class="container">
    <h2>Альбомы</h2>
    <%@ include file="menu.jspf" %>

    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <form action="albums" method="post">
        <c:choose>
            <c:when test="${empty editAlbum}">
                <h3>Добавить альбом</h3>
                <input type="hidden" name="action" value="add">
            </c:when>
            <c:otherwise>
                <h3>Изменить альбом (ID: ${editAlbum.id})</h3>
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${editAlbum.id}">
            </c:otherwise>
        </c:choose>

        <div class="form-group">
            <label for="albumName">Название альбома:</label>
            <input type="text" id="albumName" name="name"
                   value="<c:out value='${editAlbum.name}'/>" autocomplete="off" required>
            <div id="autocomplete-list-album" class="autocomplete-items"></div>
        </div>
        <div class="form-group">
            <label for="genre">Жанр:</label>
            <input type="text" id="genre" name="genre" value="<c:out value='${editAlbum.genre}'/>" required>
        </div>
        <div class="form-group">
            <label for="artistName">Исполнитель:</label>
            <input type="text" id="artistName" name="artist"
                   value="<c:out value='${editAlbum.artist.name}'/>" autocomplete="off" required>
            <div id="autocomplete-list-artist" class="autocomplete-items"></div>
        </div>

        <button type="submit">Сохранить</button>
        <c:if test="${not empty editAlbum}"><a href="albums" class="btn-cancel">Отмена</a></c:if>
    </form>

    <h3>Список альбомов:</h3>
    <table>
        <tr>
            <th>ID</th>
            <th>Название</th>
            <th>Жанр</th>
            <th>Исполнитель</th>
            <th>Действия</th>
        </tr>
        <c:forEach var="album" items="${albums}">
            <tr>
                <td>${album.id}</td>
                <td><c:out value="${album.name}"/></td>
                <td><c:out value="${album.genre}"/></td>
                <td><c:out value="${album.artist.name}"/></td>
                <td>
                    <a href="albums?edit=${album.id}">Изменить</a> |
                    <form action="albums" method="post" class="inline-form"
                          onsubmit="return confirm('Удалить альбом и все его композиции?');">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${album.id}">
                        <button type="submit" class="btn-danger">Удалить</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<script>
    setupAutocomplete("albumName", "autocomplete-list-album", "album", "${pageContext.request.contextPath}");
    setupAutocomplete("artistName", "autocomplete-list-artist", "artist", "${pageContext.request.contextPath}");
</script>
</body>
</html>
