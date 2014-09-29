var xmlhttp;
var licznikStron = 0;
function init() {
    if (window.XMLHttpRequest)
    {
        xmlhttp = new XMLHttpRequest(); //for IE7+, Firefox, Chrome, Opera, Safari
    }
    else
    {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP"); //for IE6, IE5
    }

}
function sendTankRequest() {
    url = "/KlientStacji/MonitorController?licznik=" + licznikStron;
    xmlhttp.open("GET", url, true)
    xmlhttp.setRequestHeader('X-Request-With', 'XMLHttpRequest');
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200)
            {
                updateTankTable();

            }
            else
            {
                reqFailed();
            }
        }
        else {
            reqFailed();
        }
    };

}
function updateTankTable() {
    var ids = xmlhttp.responseXML.getElementsByTagName("id");
    var states = xmlhttp.responseXML.getElementsByTagName("state");
    for (i = 0; i < 10; i++) {
        var inkrementator = i + 1;
        var element = "idZbiornika" + inkrementator.toString();
        document.getElementById(element).innerHTML = ids[i].childNodes[0].nodeValue;
        var stan = states[i].childNodes[0].nodeValue;
        var elementStan = "stanZbiornika" + inkrementator.toString();
        var elementGuzik = "Stan" + inkrementator.toString();
        var wartoscGuzika = "Stan" + ids[i].childNodes[0].nodeValue;

        if (stan == "true") {
            document.getElementById(elementStan).innerHTML = "STABILNY";
            document.getElementById(elementStan).className = "label label-success";
            document.getElementById(elementGuzik).value = wartoscGuzika;
            document.getElementById(elementGuzik).style.display = 'block';
        }
        else if (stan == "false") {
            document.getElementById(elementStan).innerHTML = "NIESTABILNY";
            document.getElementById(elementStan).className = "label label-danger";
            document.getElementById(elementGuzik).value = wartoscGuzika;
            document.getElementById(elementGuzik).style.display = 'block';
        } else {
            document.getElementById(element).innerHTML = "";
            document.getElementById(elementStan).innerHTML = "";
            document.getElementById(elementStan).className = "label label-success";
            document.getElementById(elementGuzik).value = "";
            document.getElementById(elementGuzik).style.display = 'none';
        }
        document.getElementById(elementStan).innerHTML = stan;
    }
}
function initButtons() {
    for (i = 1; i < 11; i++) {
        var guzik = "Stan" + i;
        document.getElementById(guzik).style.display = 'none';
    }
}

function nextPage() {
    licznik = licznikStron + 1;
    licznikStron = licznik;
    sendTankRequest();
}
function previousPage() {
    if (licznik > 0) {
        licznik = licznikStron - 1;
        licznikStron = licznik;
        sendTankRequest();
    }
}

function reqFailed() {
    for (i = 0; i < 10; i++) {
        var inkrementator = i + 1;
        var elementStan = "stanZbiornika" + inkrementator.toString();
        var elementGuzik = "Stan" + inkrementator.toString();
        var element = "idZbiornika" + inkrementator.toString();
        document.getElementById(element).innerHTML = "";
        document.getElementById(elementStan).innerHTML = "";
        document.getElementById(elementStan).className = "label label-success";
        document.getElementById(elementGuzik).value = "";
        document.getElementById(elementGuzik).style.display = 'none';
    }
}