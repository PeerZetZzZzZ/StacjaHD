var xmlhttp;
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
    xmlhttp.open("GET", "/KlientStacji/MonitorController", true)
    xmlhttp.setRequestHeader('X-Request-With', 'XMLHttpRequest');
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200)
            {
                updateTankTable(xmlhttp.responseXML);
            }
            else
            {
                //request failed
            }
        }
    };

}
function updateTankTable(xml) {
    ids = xml.getElementsByTagName("id");
    states = xml.getElementsByTagName("state");
    for (i = 0; i < ids.length; i++) {
        var inkrementator = i + 1;
        element = "idZbiornika" + inkrementator.toString();
        document.getElementById(element).innerHTML = ids[i].childNodes[0].nodeValue;
        stan = states[i].childNodes[0].nodeValue;
        elementStan = "stanZbiornika" + inkrementator.toString();
        elementGuzik = "stan" + inkrementator.toString();
        if (stan == "true") {
            document.getElementById(elementStan).innerHTML = "STABILNY";
            document.getElementById(elementStan).className = "label label-success";
            document.getElementById(elementGuzik).innerHTML = "Pokaz stan";
        }
        else {
            document.getElementById(elementStan).innerHTML = "NIESTABILNY";
            document.getElementById(elementStan).className = "label label-danger";
            document.getElementById(elementGuzik).innerHTML = "";
        }
    }
}
function zmien() {
//    document.getElementById('failure').className = "label label-danger";
    document.getElementById('idZbiornika1').innerHTML = "label label-dangerrrr";
}
function pokazZbiornik(numerGuzika) {
    element = "idZbiornika" + numerGuzika.toString();
    var numerZbiornika = document.getElementById(element).innerHTML;
    sendStanRequest(numerZbiornika);
}

function sendStanRequest(numerZbiornika) {
    var url = "/KlientStacji/MonitorController?zbiornik=" + numerZbiornika.toString();
    xmlhttp.open("GET", url, true)
    xmlhttp.send(null);
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200)
            {
            }
            else
            {
                //request failed
            }
        }
    };
}