start /d  "D:\prometheus-2.45.6\"  prometheus.exe --storage.tsdb.retention.time=7d
start /d  "D:\grafana-8.5.27\bin\" grafana-server.exe
start /d  "C:\Program Files (x86)\Microsoft\Edge\Application\" msedge.exe http://localhost:3000
start /d  "C:\Program Files (x86)\Microsoft\Edge\Application\" msedge.exe http://localhost:9090
exit
