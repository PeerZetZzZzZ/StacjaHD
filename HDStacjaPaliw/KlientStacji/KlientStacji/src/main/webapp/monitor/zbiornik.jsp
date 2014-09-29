<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Stacja benzynowa - Monitor. Zbiornik info.</title>
        <script type="text/javascript" src="TankStateUpdater.js"></script>
        <!-- Bootstrap -->
        <link href="monitor/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="jumbotron">
            <h1>Monitor stacji benzynowej</h1>
            <form method="POST" action="/KlientStacji/MonitorController">
                <input name="button" value="Process monitor" type="submit" id="processMonitorButton" class="btn btn-primary btn-lg" role="button">
            </form>
        </div>
        <div class="panel panel-primary">ID Zbiornika: ${idZbiornika} </div>
        <div class="panel panel-primary">Najnowszy pomiar: ${stempelCzasowy}</div>
        <div class="panel panel-primary"> Objetosc paliwa brutto: ${objetoscBrutto}</div>
        <div class="panel panel-primary">Objetosc paliwa netto: ${objetoscNetto}</div>
        <div class="panel panel-primary">Temperatura: ${temperatura}</div>
    </body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>
</html>
