cd parent
call mvn clean install
cd ..

cd .\example\im\talent-aio-examples-im-server
call dist.bat
cd ..\..\..

cd .\example\im\talent-aio-examples-im-client
call dist.bat
cd ..\..\..
