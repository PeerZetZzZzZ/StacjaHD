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
//    ids = xml.getElementsByTagName("id");
    states = xml.getElementsByTagName("state");
    for (i = 0; i < states.length; i++) {
        document.getElementById("idZbiornika1").innerHTML = states[i].childNodes[0].nodeValue;
//        stan = states[i].childNodes[0].nodeValue;
//        if (stan == "true") {
//            document.getElementById("stanZbiornika" + i + 1).innerHTML = "STABILNY";
//            document.getElementById("stanZbiornika" + i + 1).className = "label label-success";
//        }
//        else {
//            document.getElementById("stanZbiornika" + i + 1).innerHTML = "NIESTABILNY";
//            document.getElementById("stanZbiornika" + i + 1).className = "label label-failure";
//        }
    }
}
function zmien() {
//    document.getElementById('failure').className = "label label-danger";
    document.getElementById('idZbiornika1').innerHTML = "label label-dangerrrr";
}