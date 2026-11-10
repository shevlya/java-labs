<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Альбомы</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }

        table {
            border-collapse: collapse;
            width: 100%;
            margin-top: 20px;
        }

        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }

        th {
            background-color: #f2f2f2;
        }

        .form-group {
            margin-bottom: 15px;
            position: relative;
            width: 300px;
        }

        .autocomplete-items {
            position: absolute;
            border: 1px solid #d4d4d4;
            border-top: none;
            z-index: 99;
            top: 100%;
            left: 0;
            right: 0;
            max-height: 150px;
            overflow-y: auto;
            background: white;
        }

        .autocomplete-items div {
            padding: 10px;
            cursor: pointer;
            border-bottom: 1px solid #d4d4d4;
        }

        .autocomplete-items div:hover {
            background-color: #e9e9e9;
        }

        .error {
            color: red;
            font-weight: bold;
        }
    </style>
</head>
<body>
<h2><a href="index.jsp">← На главную</a> | Управление Альбомами</h2>
<c:if test="${not empty error}"><p class="error">${error}</p></c:if>

<form action="albums" method="post">
    <input type="hidden" name="action" value="${not empty editAlbum ? 'update' : 'add'}">
    <c:if test="${not empty editAlbum}"><input type="hidden" name="id" value="${editAlbum.id}"></c:if>

    <div class="form-group">
        <label>Название альбома:</label><br>
        <input type="text" id="albumName" name="name" value="${editAlbum.name}" autocomplete="off" required>
        <div id="autocomplete-list-album" class="autocomplete-items"></div>
    </div>
    <label>Жанр:</label><br>
    <input type="text" name="genre" value="${editAlbum.genre}" required><br><br>
    <div class="form-group">
        <label>Исполнитель:</label><br>
        <input type="text" id="artistName" name="artist"
               value="${editAlbum.artist != null ? editAlbum.artist.name : ''}" autocomplete="off" required>
        <div id="autocomplete-list-artist" class="autocomplete-items"></div>
    </div>
    <br>
    <button type="submit">${not empty editAlbum ? 'Обновить' : 'Добавить'}</button>
    <c:if test="${not empty editAlbum}"><a href="albums">Отмена</a></c:if>
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
                    <input type="hidden" name="action" value="delete"><input type="hidden" name="id"
                                                                             value="${album.id}">
                    <button type="submit"
                            style="color:red; background:none; border:none; cursor:pointer; text-decoration:underline;">
                        Удалить
                    </button>
                </form>
            </td>
        </tr>
    </c:forEach>
</table>

<script>
    // Универсальная функция для подключения автодополнения
    function setupAutocomplete(inputId, listId) {
        const input = document.getElementById(inputId);
        const list = document.getElementById(listId);
        if (!input || !list) return;

        input.addEventListener("input", function () {
            const val = this.value.trim();
            list.innerHTML = ''; // Очищаем старые подсказки
            if (val.length < 1) return; // Начинаем искать только с 1 символа

            // Отправляем запрос на сервлет с параметром ?query=...
            fetch("autocomplete?query=" + encodeURIComponent(val))
                .then(response => {
                    if (!response.ok) throw new Error("Network response was not ok");
                    return response.text();
                })
                .then(text => {
                    console.log("Получен ответ от сервера:", text); // Для отладки в F12
                    if (!text) return;

                    // Разбиваем ответ по переносу строки
                    text.split('\n').forEach(name => {
                        const div = document.createElement("div");
                        div.innerHTML = name;
                        div.addEventListener("click", function () {
                            input.value = this.innerHTML;
                            list.innerHTML = ''; // Скрываем список после выбора
                        });
                        list.appendChild(div);
                    });
                })
                .catch(error => console.error("Ошибка AJAX:", error));
        });

        // Закрыть список при клике в любом месте страницы, кроме поля ввода
        document.addEventListener("click", function (e) {
            if (e.target !== input) {
                list.innerHTML = '';
            }
        });
    }

    // Инициализация для конкретных полей на страницах
    // Для artists.jsp:
    //setupAutocomplete("artistName", "autocomplete-list");

    // Для albums.jsp (раскомментируйте, если файл albums.jsp):
    setupAutocomplete("albumName", "autocomplete-list-album");
    setupAutocomplete("artistName", "autocomplete-list-artist");

    // Для compositions.jsp (раскомментируйте, если файл compositions.jsp):
    // setupAutocomplete("albumName", "autocomplete-list");
</script>
</body>
</html>
