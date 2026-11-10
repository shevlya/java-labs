<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Исполнители</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2><a href="index.jsp">← На главную</a> | Управление Исполнителями</h2>

    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form action="artists" method="post">
        <input type="hidden" name="action" value="${not empty editArtist ? 'update' : 'add'}">
        <c:if test="${not empty editArtist}"><input type="hidden" name="id" value="${editArtist.id}"></c:if>

        <div class="form-group">
            <label>Имя исполнителя:</label>
            <input type="text" id="artistName" name="name" value="${editArtist.name}" autocomplete="off" required>
            <div id="autocomplete-list" class="autocomplete-items"></div>
        </div>

        <button type="submit">${not empty editArtist ? 'Обновить' : 'Добавить'}</button>
        <c:if test="${not empty editArtist}"><a href="artists" style="margin-left: 15px;">Отмена</a></c:if>
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
                    <form action="artists" method="post" style="display:inline;"
                          onsubmit="return confirm('Удалить исполнителя и ВСЕ его альбомы и композиции (CASCADE)?');">
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
    function setupAutocomplete(inputId, listId, targetType) {
        const input = document.getElementById(inputId);
        const list = document.getElementById(listId);
        if (!input || !list) return;

        // Переменная для хранения идентификатора таймера
        let timeoutId = null;

        input.addEventListener("input", function () {
            const val = this.value.trim();
            list.innerHTML = '';

            // 1. Не делаем запрос, если введено меньше 2 символов
            if (val.length < 2) return;

            // 2. Если таймер уже запущен, отменяем его (пользователь продолжает печатать)
            if (timeoutId) {
                clearTimeout(timeoutId);
            }

            // 3. Запускаем новый таймер на 400 миллисекунд (0.4 секунды)
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
            }, 400); // <-- Здесь задается задержка в миллисекундах
        });

        document.addEventListener("click", function (e) {
            if (e.target !== input) {
                list.innerHTML = '';
            }
        });
    }

    setupAutocomplete("artistName", "autocomplete-list-artist", "artist");
</script>
</body>
</html>
