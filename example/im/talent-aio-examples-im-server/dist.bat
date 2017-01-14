call mvn -f pom-dist.xml clean install

rd ..\..\..\dist\talent-aio-examples-im-server-1.0.0-RELEASE /s /q

copy target\talent-aio-examples-im-server-1.0.0-RELEASE.zip ..\..\..\dist\talent-aio-examples-im-server-1.0.0-RELEASE.zip

pause