call mvn -f pom-dist.xml clean install

rd ..\..\..\dist\talent-aio-examples-im-server-1.0.2.v20170217-SNAPSHOT /s /q

copy target\talent-aio-examples-im-server-1.0.2.v20170217-SNAPSHOT.zip ..\..\..\dist\talent-aio-examples-im-server-1.0.2.v20170217-SNAPSHOT.zip
