var nav_list_conf=[
{href:"./",innerHTML:"首 页"},
{href:"./quickstart.html",innerHTML:"30分钟helloworld"},
{href:"./",innerHTML:"2小时实战(敬请期待)"},
{href:"./",innerHTML:"API文档(敬请期待)"},
{href:"https://git.oschina.net/tywo45/talent-aio",innerHTML:"下 载",target:"_blank"}
];

function initnavlist(){
	var nav_list = document.getElementById("nav_list");
	var nav_list_btn = document.getElementById("nav_list_btn");
	
	
	
	nav_list.innerHTML="";
	for (var i = 0; i < nav_list_conf.length; i++){
		var li = document.createElement("li");
		nav_list.appendChild(li);
		
		var a = document.createElement("a");
		li.appendChild(a);
		
		a.href = nav_list_conf[i].href;
		a.innerHTML = nav_list_conf[i].innerHTML;
		if(nav_list_conf[i].target){
			a.target = nav_list_conf[i].target;
		}
		
		
		var span = document.createElement("span");
		nav_list_btn.appendChild(span);
		span.className = "icon-bar";
		
		
		
	}
}
initnavlist();

function insertAfter( newElement, targetElement)
{
   var parent = targetElement.parentNode;
   if ( parent.lastChild == targetElement )
   {
        // 如果最后的节点是目标元素，则直接添加。因为默认是最后
        parent.a( newElement );
   }
   else
   {
        //如果不是，则插入在目标元素的下一个兄弟节点的前面。也就是目标元素的后面
        parent.insertBefore( newElement, targetElement.nextSibling );
   }
}

function htmltagConvert(){
	
	
	var all = $(".is_code");
	$(".is_code").each(function(index, e){
		//alert(e.value);
		
		var pre = document.createElement("pre");
		//e.parentNode.appendChild(pre);
		insertAfter(pre, e);
		pre.innerHTML = e.innerHTML;
		pre.className = $(e).attr("codestyle");
	});
	//<pre class='brush:java'>
}
htmltagConvert();





SyntaxHighlighter.config.bloggerMode = true;

SyntaxHighlighter.defaults['toolbar'] = false;
SyntaxHighlighter.defaults.gutter = true;  //是否显示左侧的行数等信息


SyntaxHighlighter.config.strings.viewSource = "查看";
SyntaxHighlighter.config.strings.help = "?";


SyntaxHighlighter.all();

SyntaxHighlighter.highlight();



