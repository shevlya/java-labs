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
                .then(response => response.ok ? response.text() : Promise.reject("Ошибка сети"))
                .then(text => {
                    if (!text) return;function setupAutocomplete(inputId, listId, targetType) {
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
                                    .then(response => response.ok ? response.text() : Promise.reject("Ошибка сети"))
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
