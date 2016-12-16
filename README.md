 **#talent-aio**  **（官方交流群：428058412）** 
高性能、易用、灵活的java aio框架，可方便用于创建tcp长连接服务器。

 **talent-aio的一些测试数据(测试环境见图)：** 
![测试机器](https://git.oschina.net/tywo45/talent-aio/raw/master/ddd.png?dir=0&filepath=ddd.png&oid=2c5667ac59cc61884cd96830dc00ced481b9dc16&sha=c6fe97a4e175a3393ab0d4b9dec2ce04148039eb "在这里输入图片标题")
1、用talent-aio-server实现的tcp长连接服务器，目前简单测试后，可以支持 **75000个TCP长连接** (这个数值后面会继续增加，因为已有的测试还没达到服务器极限，只是用完了5个客户端的极限)
2、用talent-aio-server实现的tcp长连接服务器，客户机与服务器是同一台的话，服务器可以接收、处理、发送 **18万条完整的业务消息包/每秒** ，客户端可以同时处理与之对应的数据量

 **talent-aio的一些特点：** 
1、易用。易到到什么程度，可以参考已经用talent-aio实现的im例子
2、自带与组绑定、与用户绑定等功能，并且在连接关闭时，自动销毁这些绑定(以避免用户自己忘记销毁带来的内存溢出问题)
3、API自带发送到指定用户、指定组等功能

 **talent-aio的一些缺点** 
1、文档，目前可以用例子学习，例子也是很简单的
2、性能细节方面还有些待优化，但由于总体架构包括线程模型很优秀(这是不是有点在自夸？)，所以目前的性能已经非常非常好(以前有个大企业的架构师跟我说 **netty的c10k** ，目前talent-aio已经支持到 **c75k** 了，后续这个数字肯定还要增加，预计增到了 **c200k** 问题不大)

 **用talent-aio实现的简单的im例子(先启动服务器再启动客户端，然后控制台会打印相当性能数据，性能数据的解释见图):** 
1、先运行parent/install.bat，用来安装本项目所有代码
2、运行server examples: com.talent.aio.examples.im.server.ImServerStarter
3、运行client examples: com.talent.aio.examples.im.client.ImClientStarter
![性能数据解释](https://git.oschina.net/tywo45/talent-aio/raw/master/1111.png?dir=0&filepath=1111.png&oid=1826c193f2d9b26ff503c5976b32eaa9eddc0592&sha=19c2f6d571eb48d3a671c491d4aaaf6d150bcb9c "在这里输入图片标题")