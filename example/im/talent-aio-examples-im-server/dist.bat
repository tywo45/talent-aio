call mvn -f pom-dist.xml clean install

rd ..\..\..\dist\talent-aio-examples-im-server-1.0.2.v20170303-RELEASE /s /q

copy target\talent-aio-examples-im-server-1.0.2.v20170303-RELEASE.zip ..\..\..\dist\talent-aio-examples-im-server-1.0.2.v20170303-RELEASE.zip
