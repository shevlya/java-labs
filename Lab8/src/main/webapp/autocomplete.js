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
            const xhr = new XMLHttpRequest();

            const url = "autocomplete?type=" + targetType + "&query=" + encodeURIComponent(val);
            xhr.open("GET", url, true);

            xhr.onreadystatechange = function () {
                if (xhr.readyState === 4) {
                    if (xhr.status === 200) {
                        const text = xhr.responseText;
                        if (!text) return;

                        text.split('\n').forEach(name => {
                            const div = document.createElement("div");
                            div.textContent = name;

                            div.addEventListener("click", function () {
                                input.value = this.textContent;
                                list.innerHTML = '';
                            });

                            list.appendChild(div);
                        });
                    } else {
                        console.error("Ошибка AJAX: Статус ответа " + xhr.status);
                    }
                }
            };

            xhr.send();

        }, 400);
    });

    document.addEventListener("click", function (e) {
        if (e.target !== input) {
            list.innerHTML = '';
        }
    });
}
