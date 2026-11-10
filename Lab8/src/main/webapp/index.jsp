<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Музыкальный магазин - Главная</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 40px;
        }

        .menu {
            list-style-type: none;
            padding: 0;
        }

        .menu li {
            margin: 15px 0;
        }

        .menu a {
            text-decoration: none;
            color: #0066cc;
            font-size: 18px;
        }

        .menu a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
<h1>Добро пожаловать в Музыкальный магазин</h1>
<ul class="menu">
    <li><a href="artists">Управление Исполнителями (Artist)</a></li>
    <li><a href="albums">Управление Альбомами (Album)</a></li>
    <li><a href="compositions">Управление Композициями (Composition)</a></li>
    <li><a href="queries">Сложные запросы (Отчеты)</a></li>
</ul>
</body>
</html>
