<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Альбомы</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2><a href="index.jsp">На главную</a> | Управление Альбомами</h2>
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form action="albums" method="post">
        <input type="hidden" name="action" value="${not empty editAlbum ? 'update' : 'add'}">
        <c:if test="${not empty editAlbum}"><input type="hidden" name="id" value="${editAlbum.id}"></c:if>

        <div class="form-group">
            <label>Название альбома:</label>
            <input type="text" id="albumName" name="name" value="${editAlbum.name}" autocomplete="off" required>
            <div id="autocomplete-list-album" class="autocomplete-items"></div>
        </div>

        <div class="form-group">
            <label>Жанр:</label>
            <input type="text" name="genre" value="${editAlbum.genre}" required>
        </div>

        <div class="form-group">
            <label>Исполнитель:</label>
            <input type="text" id="artistName" name="artist"
                   value="${editAlbum.artist != null ? editAlbum.artist.name : ''}" autocomplete="off" required>
            <div id="autocomplete-list-artist" class="autocomplete-items"></div>
        </div>

        <button type="submit">${not empty editAlbum ? 'Обновить' : 'Добавить'}</button>
        <c:if test="${not empty editAlbum}"><a href="albums" style="margin-left: 15px;">Отмена</a></c:if>
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
                <td>${album.name}</td>
                <td>${album.genre}</td>
                <td>${album.artist.name}</td>
                <td>
                    <a href="albums?edit=${album.id}">Изменить</a> |
                    <form action="albums" method="post" style="display:inline;"
                          onsubmit="return confirm('Удалить альбом и ВСЕ его композиции (CASCADE)?');">
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
    function setupAutocomplete(inputId, listId, targetType) {
        const input = document.getElementById(inputId);
        const list = document.getElementById(listId);
        if (!input || !list) return;

        let timeoutId = null;

        input.addEventListener("input", function () {
            const val = this.value.trim();
            list.innerHTML = '';

            if (val.length < 2) return;

            if (timeoutId) {
                clearTimeout(timeoutId);
            }

            timeoutId = setTimeout(() => {
                fetch("autocomplete?type=" + targetType + "&query=" + encodeURIComponent(val))
                    .then(response => {
                        if (!response.ok) throw new Error("Network response was not ok");
                        return response.text();
                    })
                    .then(text => {
                        if (!text) return;
                        text.split('\n').forEach(name => {
                            const div = document.createElement("div");
                            div.innerHTML = name;
                            div.addEventListener("click", function () {
                                input.value = this.innerHTML;
                                list.innerHTML = '';
                            });
                            list.appendChild(div);
                        });
                    })
                    .catch(error => console.error("Ошибка AJAX:", error));
            }, 400);
        });

        document.addEventListener("click", function (e) {
            if (e.target !== input) {
                list.innerHTML = '';
            }
        });
    }

    setupAutocomplete("albumName", "autocomplete-list-album", "album");
    setupAutocomplete("artistName", "autocomplete-list-artist", "artist");
</script>
</body>
</html>
