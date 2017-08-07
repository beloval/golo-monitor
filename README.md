API to monitor server status for GOLO

API : http://localhost:8080/golomonitor/launch
      start, stop monitor and get statistics:
 POST application/json, no authentications

parameters :
            interval=10000   - integer value number millisec of delay between requests of monitor to server
            hostname=https://api.test.paysafe.com/accountmanagement/monitor  - string value of server
            launch=1 - boolean value = 1  : start monitor; value = 0 stop monitor

example :
http://localhost:8080/golomonitor/launch?interval=10000&hostname=https://api.test.paysafe.com/accountmanagement/monitor&launch=1

API : http://localhost:8080/golomonitor/stop
      stop monitor
 POST application/json, no authentications

parameters : no parameters

example :
http://localhost:8080/golomonitor/stop


API : http://localhost:8080/golomonitor/statistics
      get monitors statistics
 GET application/json, no authentications

parameters : no parameters

example :
http://localhost:8080/golomonitor/statistics

Builded war file The file is located in root directory of project(for deploy) golomonitor-1.0-SNAPSHOT.war

response example :
{
    "serverLastStatus": "READY",
    "numberRequestToServer": 9,
    "numberStatusActive": 9,
    "numberStatusInActive": 0,
    "numberStatusOfErrors": 0,
    "goloMonitorStatus": "STARTED",
    "serverStatusList": {
        "2017-08-07T04:52:34.771+0000": "READY",
        "2017-08-07T04:52:44.920+0000": "READY",
        "2017-08-07T04:52:55.010+0000": "READY",
        "2017-08-07T04:53:05.102+0000": "READY",
        "2017-08-07T04:53:15.218+0000": "READY",
        "2017-08-07T04:53:25.314+0000": "READY",
        "2017-08-07T04:53:35.431+0000": "READY",
        "2017-08-07T04:53:45.530+0000": "READY",
        "2017-08-07T04:53:55.647+0000": "READY"
    }
}



