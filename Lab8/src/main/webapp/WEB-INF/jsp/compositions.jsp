<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Композиции</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2><a href="index.jsp">← На главную</a> | Управление Композициями</h2>
    <c:if test="${not empty error}"><p class="error">${error}</p></c:if>

    <form action="compositions" method="post">
        <input type="hidden" name="action" value="${not empty editComposition ? 'update' : 'add'}">
        <c:if test="${not empty editComposition}"><input type="hidden" name="id" value="${editComposition.id}"></c:if>

        <div class="form-group">
            <label>Название композиции:</label>
            <input type="text" name="name" value="${editComposition.name}" required>
        </div>

        <div class="form-group">
            <label>Длительность (мин):</label>
            <input type="number" name="duration" value="${editComposition.duration}" min="1" required>
        </div>

        <div class="form-group">
            <label>Альбом:</label>
            <input type="text" id="albumName" name="album"
                   value="${editComposition.album != null ? editComposition.album.name : ''}" autocomplete="off"
                   required>
            <div id="autocomplete-list" class="autocomplete-items"></div>
        </div>

        <button type="submit">${not empty editComposition ? 'Обновить' : 'Добавить'}</button>
        <c:if test="${not empty editComposition}"><a href="compositions" style="margin-left: 15px;">Отмена</a></c:if>
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
                <td>${comp.name}</td>
                <td>${comp.duration} мин.</td>
                <td>${comp.album.name}</td>
                <td>
                    <a href="compositions?edit=${comp.id}">Изменить</a> |
                    <form action="compositions" method="post" style="display:inline;">
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

    setupAutocomplete("albumName", "autocomplete-list-album", "album");
</script>
</body>
</html>
