<h3>
  talent-aio: 让天下没有难开发的即时通讯
</h3>

<ol>
	<li><h4>简 介</h4>
		 <strong>talent-aio</strong>是基于java aio实现的即时通讯框架，源于作者另一个久经考验的talent-nio框架，但在易用性、性能及代码可读性方面又远远超越了talent-nio。官网地址：<a href="http://www.talent-tan.com:9292" target="_blank">http://www.talent-tan.com:9292</a>，上面有大家期待已久的<a href="http://www.talent-tan.com:9292/quickstart.html" target="_blank">入门文档</a>。
	</li>
	
	<li><h4>小目标</h4>
		<strong>web开发领域，springmvc之外有jfinal；TCP长连接领域，netty之外还有更易用的talent-aio</strong>
	</li>
		 
	<li><h4>应用场景</h4>
		IM、实时监控、RPC、推送服务等实时通讯类型的场景
	</li>
		
	<li><h4>特 点</h4>
		<ul class="masthead-links" style="">
		  <li>
			<strong>极简洁清晰易懂的API</strong>: 没有生涩难懂的新概念，只需<strong>花上30分钟</strong><a href="http://www.talent-tan.com:9292/quickstart.html" target="_blank">学习helloworld</a>就能很好地掌握并实现一个性能极好的即时通讯应用
		  </li>
		  <li>
			<strong>极震撼的性能</strong>
			<ul>
				<li>
					可同时支持<strong>10万级</strong>tcp长连接，彻底甩开业界当年的<strong>c10K</strong>烦恼
				</li>
				<li>
					每秒可收发<strong>283万</strong>条消息(约<strong>80M</strong>)(windows7、i7、8g、群聊场景)
				</li>
			</ul>
		  </li>
		  
		  
		  
		  <li>
			<strong>极亲民的内置功能</strong>
			<ul>
				<li>
					框架层面帮你<strong>检测心跳</strong>(tcp server)、<strong>发送心跳</strong>(tcp client)
				</li>
				<li>
					框架层面支持<strong>自动重连</strong>(可设置重连间隔时间和重连次数)
				</li>
				<li>
					框架层面支持<strong>同步消息</strong>(消息发送后，等到响应消息再往下执行)
				</li>
				<li>
					框架层面支持<strong>绑定userid</strong>(用于用户关联)、<strong>绑定groupid</strong>(用于群聊)
				</li>
				<li>
					内置各项统计功能----接受过多少连接、关闭过多少连接、已发送的消息数、已接收的消息数、当前是多少正常连接、当前多少断开的连接等。
				</li>
				
				
				
			</ul>
		  </li>
		  
		  
		</ul>
	</li>
		
	<li><h4>案 例</h4>
		<ul class="masthead-links" style="font-size:14pt;">
		  <li>
			某网管系统(管理数百台刀片服务器的系统)
		  </li>
		  <li>
			某直播平台(视频直播+聊天)
		  </li>
		  <li>
			某智能设备检测系统(数据采集)<!--小白-->
		  </li>
		  <li>
			某物联网系统(服务端)<!--好像是jackkang-->
		  </li>
		  <li>
			深圳市某在线技术发展有限公司(中银联投资)：某网络安全运营支撑平台<!--小宇-->
		  </li>
		  <li>
			<a href="https://git.oschina.net/websterlu/redisx" target="_blank">redisx</a><!--小宇-->
		  </li>
		  <li>
			<a href="https://git.oschina.net/kangjie1209/talent_dubbo" target="_blank">talent_dubbo</a><!--jackkang-->
		  </li>
		  <li>
			某移动省公司CRM业务受理消息采集平台(数据采集)<!--福州-精灵-java-->
		  </li>
		  
		  <li>
			... ...
		  </li>
		  
		</ul>
	</li>
		
		
		
		
		
		
		

	<li><h4>性能测试步骤及数据</h4>
		<table>
			<tr>
				<td>
				<img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/step/1.png'>
				</td>
			</tr>
			<tr>
				<td>
				<img  src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/performance/client-11.png'>
				</img>
				</td>
			</tr>
			
			<tr>
				<td>
				<img  src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/step/2.png'>
				</img>
				</td>
			</tr>
			
		</table>
	</li>




	<li><h4>talent-aio产生的背景</h4>
		<ol>
			<li>2011年作者参与了中兴某刀片的网管系统开发，被大领导亲点来改造原来的实时通讯模块，而且不允许使用mina。在这样的背景下，开始学习nio，改造后的系统，可管理上千个节点，消息收发速度极快，核心代码至今仍然在运行，这就是后来talent-nio的雏形</li>
			<li>后来担任热波间平台端架构师，持续优化和封装了talent-nio，使之可以支持4万TCP长连接，每秒可以收发10万条消息，当年甚至扛住了自杀式的2000人在同一房间无限点赞场景</li>
			<li>因为热波间架构师的角色，认识了不少业界朋友，部分朋友表达希望开源talent-nio， 以便参考借鉴</li>
			<li>几番考虑之后，写了talent-aio，线程池部分和部分思想来源于并优化于talent-nio，在性能大步提升的基础上，易用性得到根本性解决。</li>
		</ol>
	</li>





	<li><h4>参与talent-aio</h4>
		<ol>
			<li>talent-aio是将多线程技巧运用到极致的框架，所以一旦您参与到本项目，你将会从本项目中学到很多关于多线程的技巧。</li>
			<li>本项目会陆续提供一些业界案例作为例子供大家参考，譬如融云的IM</li>
			<li>
			通过以下方式之一，加入talent-aio技术群 (这是个付费群，但是进群不是必须的，大家也可以在码云上和作者进行交流互动)
				<ul>
					<li>通过群号加入: 428058412</li>
					<li>点击加入: <a  target="_blank" href="http://shang.qq.com/wpa/qunwpa?idkey=95588b929b2832f606f4deb74a423d61257f3c08b9790ac57c29aebd09364459"><img border="0" src="http://pub.idqqimg.com/wpa/images/group.png" alt="talent-aio技术" title="talent-aio技术"></a></li>
					<li>扫码加入<div><img src='https://git.oschina.net/tywo45/talent-aio/raw/master/docs/qq-group-2.jpg' style='width:200px;height:250px;' width='200px' height='250px'/></div></li>
				</ul>
			</li>
			<li>
			<a 
			  href="/tywo45/talent-aio/issues/new?issue%5Bassignee_id%5D=&amp;issue%5Bmilestone_id%5D="
			  class="ui mini green button"
			  title="提交issue">
			<i class="icon plus"></i>提交Issue
			</a>
			给项目提出有意义的新需求，或是帮项目发现BUG，或是上传你本地测试的一些数据让作者参考以便进一步优化。
			</li>

			<li>
			点击右上方的
			<span class="basic buttons mini star-container ui">
			<a href="javascritp:void(0);" class="ui button star" data-method="post" data-remote="true" rel="nofollow">Star</a>
			</span>
			以便随时掌握本项目的动态
			</li>
		</ol>
	</li>
	
	
	
	
	<li><h4>注意事项</h4>
		<ol>
			<li>请不要在issue中提问题，以免影响watch人员，可以在评论中提问</li>
			
		</ol>
	</li>



</ol>