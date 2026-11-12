function setupAutocomplete(inputId, listId, targetType, contextPath) {
    const input = document.getElementById(inputId);
    const list = document.getElementById(listId);
    if (!input || !list) return;

    const url = (contextPath || "") + "/autocomplete";
    let timeoutId = null;
    let xhr = null;

    input.addEventListener("input", function () {
        const val = this.value.trim();
        list.innerHTML = '';

        clearTimeout(timeoutId);
        if (xhr) {
            xhr.onreadystatechange = null;
            xhr.abort();
            xhr = null;
        }
        if (val.length < 2) return;

        timeoutId = setTimeout(() => {
            const request = new XMLHttpRequest();
            xhr = request;
            request.open("GET", url + "?type=" + targetType + "&query=" + encodeURIComponent(val), true);

            request.onreadystatechange = function () {
                if (request.readyState !== 4) return;
                if (request.status !== 200) {
                    console.error("Ошибка AJAX: статус ответа " + request.status);
                    return;
                }
                showHints(request.responseText);
            };
            request.send();
        }, 400);
    });

    function showHints(text) {
        list.innerHTML = '';
        if (!text) return;

        text.split('\n').forEach(name => {
            const div = document.createElement("div");
            div.textContent = name;
            div.addEventListener("click", function () {
                input.value = name;
                list.innerHTML = '';
            });
            list.appendChild(div);
        });
    }

    document.addEventListener("click", function (e) {
        if (e.target !== input) {
            list.innerHTML = '';
        }
    });
}
