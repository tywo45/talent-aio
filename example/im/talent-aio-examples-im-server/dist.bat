call mvn -f pom-dist.xml clean install

rd ..\..\..\dist\talent-aio-examples-im-server-0.6.8-alpha /s /q

copy target\talent-aio-examples-im-server-0.6.8-alpha.zip ..\..\..\dist\talent-aio-examples-im-server-0.6.8-alpha.zip

pause