call mvn -f pom-dist.xml clean install


rd ..\..\..\dist\talent-aio-examples-im-client-1.6.6.v20170318-RELEASE /s /q

copy target\talent-aio-examples-im-client-1.6.6.v20170318-RELEASE.zip ..\..\..\dist\talent-aio-examples-im-client-1.6.6.v20170318-RELEASE.zip
