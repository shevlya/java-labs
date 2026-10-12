function restrictInput(inputElement) {
    let val = inputElement.value;
    val = val.replace(/[^0-9.\-]/g, '');
    const match = val.match(/^-?(?:\d+\.?\d*)?/);
    inputElement.value = match ? match[0] : '';
}

function calculate(event) {
    event.preventDefault();

    var val1Str = document.getElementById('value1').value.trim();
    var val2Str = document.getElementById('value2').value.trim();

    if (val1Str === "" || val2Str === "") {
        alert('Поля ввода значений пусты, заполните оба поля');
        return false;
    }

    var radios = document.getElementsByName('operation');
    var operation = null;
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            operation = radios[i].value;
            break;
        }
    }

    if (operation === null) {
        alert('Операция не выбрана, выберите одну из арифметических операций');
        return false;
    }

    // parseFloat отлично понимает строки вида "-5.5"
    var value1 = parseFloat(val1Str);
    var value2 = parseFloat(val2Str);
    var result = 0;

    if (isNaN(value1) || isNaN(value2)) {
        alert('Введите корректные числа');
        return false;
    }

    var operations = {
        '+': function (x, y) {
            return x + y;
        },
        '-': function (x, y) {
            return x - y;
        },
        '*': function (x, y) {
            return x * y;
        },
        '/': function (x, y) {
            return y === 0 ? NaN : x / y;
        }
    };

    if (operation === '/' && value2 === 0) {
        alert('Деление на ноль невозможно');
        return false;
    }

    result = operations[operation](value1, value2);
    result = Math.round(result * 10000) / 10000;

    document.getElementById("result").value = result;

    var params = new URLSearchParams({
        value1: value1,
        value2: value2,
        operation: operation,
        result: result
    });
    window.location.href = "result.html?" + params.toString();

    return false;
}

function getCalcInfo() {
    var params = new URLSearchParams(window.location.search);
    document.getElementById("res-value1").textContent = params.get("value1") || "—";
    document.getElementById("res-operation").textContent = params.get("operation") || "—";
    document.getElementById("res-value2").textContent = params.get("value2") || "—";
    document.getElementById("res-result").textContent = params.get("result") || "—";
}
