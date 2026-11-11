<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Музыкальный магазин</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container" style="text-align: center;">
    <img src="${pageContext.request.contextPath}/images/taskD.jpg" alt="Логотип Музыкального магазина"
         class="main-logo">
    <h1>Музыкальный магазин</h1>
    <ul class="menu">
        <li><a href="artists">Исполнители</a></li>
        <li><a href="albums">Альбомы</a></li>
        <li><a href="compositions">Композиции</a></li>
        <li><a href="queries">Запросы</a></li>
    </ul>
</div>
</body>
</html>
