function restrictInput(inputElement) {
    let val = inputElement.value;
    val = val.replace(/[^0-9.\-]/g, '');
    const match = val.match(/^-?(?:\d+\.?\d*)?/);
    inputElement.value = match ? match[0] : '';
}

function calculate(event) {
    event.preventDefault();
    var val1Str = document.getElementById("value1").value.trim();
    var val2Str = document.getElementById("value2").value.trim();
    var checkedRadio = document.querySelector('input[name="operation"]:checked');
    var operation = checkedRadio !== null ? checkedRadio.value : null;
    var value1 = parseFloat(val1Str);
    var value2 = parseFloat(val2Str);

    var errorMessage = null;
    if (val1Str === '' || val2Str === '') {
        errorMessage = 'Поля значений аргументов не заполнены';
    } else if (operation === null) {
        errorMessage = 'Операция не выбрана';
    } else if (isNaN(value1) || isNaN(value2)) {
        errorMessage = 'Введите корректные числа';
    } else if (operation === '/' && value2 === 0) {
        errorMessage = 'Деление на ноль невозможно';
    }

    if (errorMessage !== null) {
        alert(errorMessage);
    } else {
        var operations = {
            '+': function (x, y) { return x + y; },
            '-': function (x, y) { return x - y; },
            '*': function (x, y) { return x * y; },
            '/': function (x, y) { return x / y; }
        };
        var result = Number(operations[operation](value1, value2).toFixed(4));
        document.getElementById("result").value = result;

        var params = new URLSearchParams({
            value1: value1,
            value2: value2,
            operation: operation,
            result: result
        });
        window.location.href = "result.html?" + params.toString();
    }
}

function getCalcInfo() {
    var params = new URLSearchParams(window.location.search);
    document.getElementById("res-value1").textContent = params.get("value1") || "-";
    document.getElementById("res-operation").textContent = params.get("operation") || "-";
    document.getElementById("res-value2").textContent = params.get("value2") || "-";
    document.getElementById("res-result").textContent = params.get("result") || "-";
}
