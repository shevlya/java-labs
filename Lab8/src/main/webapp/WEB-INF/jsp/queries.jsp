<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Сложные запросы</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h2><a href="${pageContext.request.contextPath}/">На главную</a> | Сложные запросы (Задание 3)</h2>

    <h3>Композиции конкретного исполнителя</h3>
    <form action="queries" method="get" style="margin-bottom: 20px; max-width: 400px;">
        <div class="form-group" style="display: flex; gap: 10px; align-items: flex-end;">
            <div style="flex-grow: 1; position: relative;">
                <label for="artistSearch">Введите имя исполнителя:</label>
                <input type="text" id="artistSearch" name="artist" value="${param.artist}"
                       autocomplete="off" placeholder="Например: Bach" style="margin-bottom: 0;">
                <div id="autocomplete-list-query" class="autocomplete-items"></div>
            </div>
            <button type="submit" style="margin-bottom: 0; margin-top: 24px;">Показать</button>
        </div>
    </form>

    <c:if test="${not empty param.artist}">
        <c:choose>
            <c:when test="${empty fullList}">
                <p class="error" style="background: none; border: none; padding: 0;">
                    Композиций исполнителя «${param.artist}» не найдено.
                </p>
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
                            <td>${row[0]}</td>
                            <td>${row[1]} мин.</td>
                            <td>${row[2]}</td>
                            <td>${row[3]}</td>
                            <td>${row[4]}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:otherwise>
        </c:choose>
    </c:if>

    <hr style="margin: 40px 0; border: 0; border-top: 1px solid #ddd;">

    <h3>Статистика по исполнителям</h3>

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
            if (timeoutId) clearTimeout(timeoutId);

            timeoutId = setTimeout(() => {
                fetch("autocomplete?type=" + targetType + "&query=" + encodeURIComponent(val))
                    .then(response => response.ok ? response.text() : Promise.reject("Network error"))
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
            if (e.target !== input) list.innerHTML = '';
        });
    }

    setupAutocomplete("artistSearch", "autocomplete-list-query", "artist");
</script>
</body>
</html>
