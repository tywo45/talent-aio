//添加数组IndexOf方法
if (!Array.prototype.indexOf) {
    Array.prototype.indexOf = function(elt /*, from*/){
        var len = this.length >>> 0;
        var from = Number(arguments[1]) || 0;
        from = (from < 0) ? Math.ceil(from) : Math.floor(from);
        if (from < 0){
			from += len;
		}
        for (; from < len; from++) {
            if (from in this &&this[from] === elt){
				return from;
			}
        }
        return -1;
    };
}
var _m_ad, _m_stuff, _m_ismobile;
(function(win, headVar){
    var doc = win.document, domWaiters = [], queue = [], handlers = {}, assets = {}, isAsync = "async" in doc.createElement("script") || "MozAppearance" in doc.documentElement.style || win.opera, isHeadReady, isDomReady, api = win[headVar] = (win[headVar] || function(){api.ready.apply(null, arguments)}), PRELOADING = 1, PRELOADED = 2, LOADING = 3, LOADED = 4;
    api.Info = {};
    api.InfoJS = "";
    api._js = "61.160.200.242:7701";
    api._log = "";
    api._ads = "61.160.200.242:9988";
    api._cms = "61.160.200.242:9002";
	api._redirect = "61.160.200.242:7701";
    api._log_flag = "0";
	api._third_req = "http://61.160.200.242:9002/";
    if (isAsync) {
        api.load = function(){
            var args = arguments, callback = args[args.length - 1], items = {};
            if (!isFunction(callback)) {
                callback = null
            }
            each(args, function(item, i){
                if (item !== callback) {
                    item = getAsset(item);
                    items[item.name] = item;
                    load(item, callback && i === args.length - 2 ? function(){
                        if (allLoaded(items)) {
                            one(callback)
                        }
                    }: null)
                }
            });
            return api
        }
    }
    else {
        api.load = function(){
            var args = arguments, rest = [].slice.call(args, 1), next = rest[0];
            if (!isHeadReady) {
                queue.push(function(){
                    api.load.apply(null, args)
                });
                return api
            }
            if (!!next) {
                each(rest, function(item){
                    if (!isFunction(item)) {
                        preLoad(getAsset(item))
                    }
                });
                load(getAsset(args[0]), isFunction(next) ? next : function(){
                    api.load.apply(null, rest)
                })
            }
            else {
                load(getAsset(args[0]))
            }
            return api
        }
    }
    api.js = api.load;
    api.ready = function(key, callback){
        if (key === doc) {
            if (isDomReady) {
                one(callback)
            }
            else {
                domWaiters.push(callback)
            }
            return api
        }
        if (isFunction(key)) {
            callback = key;
            key = "ALL"
        }
        if (typeof key !== "string" || !isFunction(callback)) {
            return api
        }
        var asset = assets[key];
        if (asset && asset.state === LOADED || key === "ALL" && allLoaded() && isDomReady) {
            one(callback);
            return api
        }
        var arr = handlers[key];
        if (!arr) {
            arr = handlers[key] = [callback]
        }
        else {
            arr.push(callback)
        }
        return api
    };
    api.ready(doc, function(){
        if (allLoaded()) {
            each(handlers.ALL, function(callback){
                one(callback)
            })
        }
        if (api.feature) {
            api.feature("domloaded", true)
        }
    });
    function noop(){
    }
    function each(arr, callback){
        if (!arr) {
            return
        }
        if (typeof arr === "object") {
            arr = [].slice.call(arr)
        }
        for (var i = 0, l = arr.length; i < l; i++) {
            callback.call(arr, arr[i], i)
        }
    }
    function is(type, obj){
        var clas = Object.prototype.toString.call(obj).slice(8, -1);
        return obj !== undefined && obj !== null && clas === type
    }
    function isFunction(item){
        return is("Function", item)
    }
    function isArray(item){
        return is("Array", item)
    }
    function toLabel(url){
        var items = url.split("/"), name = items[items.length - 1], i = name.indexOf("?");
        return i !== -1 ? name.substring(0, i) : name
    }
    function one(callback){
        callback = callback || noop;
        if (callback._done) {
            return
        }
        callback();
        callback._done = 1
    }
    function getAsset(item){
        var asset = {};
        if (typeof item === "object") {
            for (var label in item) {
                if (!!item[label]) {
                    asset = {
                        name: label,
                        url: item[label]
                    }
                }
            }
        }
        else {
            asset = {
                name: toLabel(item),
                url: item
            }
        }
        var existing = assets[asset.name];
        if (existing && existing.url === asset.url) {
            return existing
        }
        assets[asset.name] = asset;
        return asset
    }
    function allLoaded(items){
        items = items || assets;
        for (var name in items) {
            if (items.hasOwnProperty(name) && items[name].state !== LOADED) {
                return false
            }
        }
        return true
    }
    function onPreload(asset){
        asset.state = PRELOADED;
        each(asset.onpreload, function(afterPreload){
            afterPreload.call()
        })
    }
    function preLoad(asset, callback){
        if (asset.state === undefined) {
            asset.state = PRELOADING;
            asset.onpreload = [];
            loadAsset({
                url: asset.url,
                type: "cache"
            }, function(){
                onPreload(asset)
            })
        }
    }
    function load(asset, callback){
        callback = callback || noop;
        if (asset.state === LOADED) {
            callback();
            return
        }
        if (asset.state === LOADING) {
            api.ready(asset.name, callback);
            return
        }
        if (asset.state === PRELOADING) {
            asset.onpreload.push(function(){
                load(asset, callback)
            });
            return
        }
        asset.state = LOADING;
        loadAsset(asset, function(){
            asset.state = LOADED;
            callback();
            each(handlers[asset.name], function(fn){
                one(fn)
            });
            if (isDomReady && allLoaded()) {
                each(handlers.ALL, function(fn){
                    one(fn)
                })
            }
        })
    }
    function loadAsset(asset, callback){
        callback = callback || noop;
        var ele;
        if (/\.css[^\.]*$/.test(asset.url)) {
            ele = doc.createElement("link");
            ele.type = "text/" + (asset.type || "css");
            ele.rel = "stylesheet";
            ele.href = asset.url
        }
        else {
            ele = doc.createElement("script");
            ele.type = "text/" + (asset.type || "javascript");
            ele.src = asset.url
        }
        ele.onload = ele.onreadystatechange = process;
        ele.onerror = error;
        ele.async = false;
        ele.defer = false;
        function error(event){
            event = event || win.event;
            ele.onload = ele.onreadystatechange = ele.onerror = null;
            callback()
        }
        function process(event){
            event = event || win.event;
            if (event.type === "load" || (/loaded|complete/.test(ele.readyState) && (!doc.documentMode || doc.documentMode < 9))) {
                ele.onload = ele.onreadystatechange = ele.onerror = null;
                callback()
            }
        }
        api.writeO(ele)
    }
    function domReady(){
        if (!doc.body) {
            win.clearTimeout(api.readyTimeout);
            api.readyTimeout = win.setTimeout(domReady, 50);
            return
        }
        if (!isDomReady) {
            isDomReady = true;
            each(domWaiters, function(fn){
                one(fn)
            })
        }
    }
    function domContentLoaded(){
        if (doc.addEventListener) {
            doc.removeEventListener("DOMContentLoaded", domContentLoaded, false);
            domReady()
        }
        else {
            if (doc.readyState === "complete") {
                doc.detachEvent("onreadystatechange", domContentLoaded);
                domReady()
            }
        }
    }
    if (doc.readyState === "complete") {
        domReady()
    }
    else {
        if (doc.addEventListener) {
            doc.addEventListener("DOMContentLoaded", domContentLoaded, false);
            win.addEventListener("load", domReady, false)
        }
        else {
            doc.attachEvent("onreadystatechange", domContentLoaded);
            win.attachEvent("onload", domReady);
            var top = false;
            try {
                top = win.frameElement == null && doc.documentElement
            } 
            catch (e) {
            }
            if (top && top.doScroll) {
                (function doScrollCheck(){
                    if (!isDomReady) {
                        try {
                            top.doScroll("left")
                        } 
                        catch (error) {
                            win.clearTimeout(api.readyTimeout);
                            api.readyTimeout = win.setTimeout(doScrollCheck, 50);
                            return
                        }
                        domReady()
                    }
                })()
            }
        }
    }
    setTimeout(function(){
        isHeadReady = true;
        each(queue, function(fn){
            fn()
        })
    }, 300);
    if (!window.console) {
        window.console = {
            log: function(){
            }
        }
    }
    api.log = function(s){
        if (parseInt(api._log_flag)) {
            var p = "";
            if (typeof s === "string") {
                p = s;
                if (typeof mim_sp != "undefined") {
                    p += "&sp=" + mim_sp
                }
            }
            else {
                if (typeof s === "object") {
                    if (typeof mim_sp != "undefined") {
                        s.sp = mim_sp
                    }
                    for (var i in s) {
                        p += i + "=" + escape(s[i]) + "&"
                    }
                }
                else {
                    p = s
                }
            }
            var logUrl = "http://" + api._log + "/stat.log.test?" + p + "&rand=" + Math.random();
            var img = new Image(1, 1);
            img.src = logUrl;
            img.onload = function(){
                return
            }
        }
    };
    api._isMobile = function(){
        var a = navigator.userAgent || navigator.vendor || window.opera;
        return /(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0, 4))
    };
    api.isMobile = function(){
        var u = window.navigator.userAgent.toLowerCase();
        var h = "";
        if ((/AppleWebKit.*mobile/i.test(u)) || (api._isMobile()) || (/android/i.test(u)) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(u))) {
            return true
        }
        else {
            return false
        }
    };
    api.getAd = function(sn){
        return sn
    };
    api.show_js = function(_js, _js2, _sn, aid, push_id, sp_id, _src){
        var log = {
            type: "showjs start",
            aid: aid,
            spid: sp_id,
            src: _src,
            sn: _sn,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log);
        api.js(_js + "?" + Math.ceil(Math.random() * 100000000), function(){
            var log = {
                type: "showjs ok",
                aid: aid,
		        pushid: push_id,
                spid: sp_id,
                src: _src,
                sn: _sn,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log);
            if (typeof _js2 != "undefined" && _js2 != "undefined" && _js2 != "") {
                _js2 = unescape(_js2);
                var iframe = document.createElement("IFRAME");
                iframe.frameBorder = "0";
                iframe.style.width = 0 + "px";
                iframe.setAttribute("src", "http://" + api._ads + "/stat.show?ver=0&bid=0&aid=" + aid+"&pushid=" + push_id + "&spid=" + sp_id + "&src=" + _src);
                iframe.style.height = 0 + "px";
                iframe.style.boder = 0 + "px";
                iframe.style.position = "absolute";
                document.body.appendChild(iframe)
            }
        })
    };
    api.click_js = function(_js, _js2, _sn, aid,push_id,sp_id, _src){
        var log = {
            type: "clickjs start",
            aid: aid,
            spid: sp_id,
            src: _src,
            sn: _sn,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log);
        api.js(_js + "?" + Math.ceil(Math.random() * 100000000), function(){
            var log = {
                type: "clickjs ok",
                aid: aid,
                sn: _sn,
                spid: sp_id,
                src: _src,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log);
            if (typeof _js2 != "undefined" && _js2 != "undefined" && _js2 != "") {
                _js2 = unescape(_js2);
                var iframe = document.createElement("IFRAME");
                iframe.frameBorder = "0";
                iframe.setAttribute("src", "//" + api._js + "/stat.click?ver=0&bid=0&aid=" + aid +"&pushid=" + push_id + "&spid=" + sp_id + "&src=" + _src);
                iframe.style.width = 0 + "px";
                iframe.style.height = 0 + "px";
                iframe.style.boder = 0 + "px";
				iframe.style.position = "absolute";
                document.body.appendChild(iframe)
            }
        })
    };
    api.run = function(sn){
        var ad = api.Info[sn];
        var type = "";
        if (!api.Info) {
            type = "empty staff(0)"
        }
        else {
            if (!ad) {
                type = "empty staff(1)"
            }
            else {
                if (!ad.data || !ad.data.aid) {
                    type = "empty staff(2)"
                }
                else {
                    if (!ad.data.staff || !ad.data.staff[0] || !ad.data.staff[0].type) {
                        type = "empty staff(3)"
                    }
                }
            }
        }
        if (type != "") {
            var log = {
                type: type,
                sn: sn,
                referer: document.referrer,
                url: document.location.href
            };
            log.infojs = api.InfoJS;
            api.log(log)
        }else {
			_m_ad = ad; _m_stuff = ad.data.staff[0]; _m_ismobile = api.isMobile();
			if(ad.data.view_type ==1 && mim_isset(ad.data.view_position) && ad.data.view_position == 1){
				api.writeBefore("<span id='" + sn + "' style='float:left;margin:0px;padding:0px;height:0px;width:0px;float:left;' frameborder=0 scrolling='no' marginwidth='0' marginheight='0'></span>");
			}else{
				api.write("<span id='" + sn + "' style='float:left;margin:0px;padding:0px;height:0px;width:0px;float:left;' frameborder=0 scrolling='no' marginwidth='0' marginheight='0'></span>");
			}
	        if (!document.getElementById(sn)) {
	            var log = {
	                type: "write error",
	                sn: sn,
	                referer: document.referrer,
	                url: document.location.href
	            };
	            api.log(log)
	        }
			var ctid = sn + "_ct";
	        var span = document.getElementById(ctid);
	        if (!span) {
                var wrap = "";
                if (typeof mim_sax_click_url_unesc != "undefined") {
                    ad.data.staff[0].landing_page = mim_sax_click_url_unesc + escape(ad.data.staff[0].landing_page)
                }
                switch (parseInt(ad.data.view_type)) {
                    case 2:
                        wrap = api.genFuChuangWrapper(ad, ad.data.staff[0]);
                        break;
                    case 4:
                        if (ad.data.staff[0].type == 1 || ad.data.staff[0].type == "AD_IMAGE" || ad.data.staff[0].type == 2 || ad.data.staff[0].type == "AD_FLASH") {
                            api.renderBgAd(ad, ad.data.staff[0]);
                            return
                        }
                        break;
                    case 8:
                        api.renderDirect(ad, ad.data.staff[0]);
                        return;
						break;
					case 32:
                        wrap = api.genDitongWrapper(ad, ad.data.staff[0]);
                        break;
                    case 64:
                        api.renderMobileApp(ad, ad.data.staff[0]);
                        return;
						break;
                    case 128:
                        api.renderMobileView(ad, ad.data.staff[0]);
                        return;
						break;
                    case 256:
                        wrap = api.genChayeWrapper(ad, ad.data.staff[0]);
                        break;
                    case 512:
                        wrap = api.genDuiLianWrapper(ad, ad.data.staff[0]);
                        break;
                    default:
                        wrap = api.genNormal(ad, ad.data.staff[0])
                }
                if (ad.data.staff[0].type == 1 || ad.data.staff[0].type == "AD_IMAGE") {
                    this.renderPic(ad, ad.data.staff[0], wrap)
                }else {
                    if (ad.data.staff[0].type == 5 || ad.data.staff[0].type == "AD_REDIRECT") {
                        api.renderDirect(ad, ad.data.staff[0])
                    }
                    else {
                        if (ad.data.staff[0].type == 2 || ad.data.staff[0].type == "AD_FLASH") {
                            this.renderSwf(ad, ad.data.staff[0], wrap)
                        }
                        else {
                            if (ad.data.staff[0].type == 8 || ad.data.staff[0].type == "AD_JS") {
                                if (ad.data.staff[0].text.indexOf("http") == -1) {
                                    ad.data.staff[0].text = "http://" + ad.data.staff[0].text
                                }
                                this.renderJs(ad, ad.data.staff[0], wrap)
                            }
                            else {
                                if (ad.data.staff[0].type == 7 || ad.data.staff[0].type == "AD_IFRAME") {
                                    if (ad.data.staff[0].text.indexOf("http") == -1) {
                                        ad.data.staff[0].text = "http://" + ad.data.staff[0].text
                                    }
                                    this.renderIFRAME(ad, ad.data.staff[0], wrap)
                                }
                                else {
                                    if (ad.data.staff[0].type == 5 || ad.data.staff[0].type == "AD_TEXT" || ad.data.staff[0].type == 6 || ad.data.staff[0].type == "AD_VIDEO" || ad.data.staff[0].type == 9 || ad.data.staff[0].type == "AD_HTML") {
                                        this.renderHTML(ad, ad.data.staff[0], wrap)
                                    }
                                    else {
                                        var log = {
                                            type: "type error",
                                            typecode: ad.data.staff[0].type,
                                            referer: document.referrer,
                                            url: document.location.href
                                        };
                                        api.log(log)
                                    }
                                }
                            }
                        }
                    }
                }
			}
        }
        delete api.Info[sn]
    };
	
	api.genNormal = function(ad, staff){
		var newWidth 	= staff.width;
		var newHeight 	= staff.height;
		var winWidth	= document.body.clientWidth;
		var winHeight	= document.body.clientHeight;
		if(api.isMobile() && (mim_isset(ad.data.view_position) && ad.data.view_position%4 == 1)){
			newWidth 	= winWidth;
			newHeight 	= newWidth/staff.width * staff.height;
		}
		
		var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
		
		var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
		wrap_content.style.margin = "auto";
		wrap_content.style.width = newWidth +"px";
        wrap_content.style.height = newHeight +"px";
		if(mim_isset(ad.data.view_position) && ad.data.view_position == 1){
			api.createCloseBtn(ad, staff, wrap, wrap_content, 7);	//创建关闭按钮
		}
        wrap.appendChild(wrap_content);
        return wrap
    };
	
    api.genFuChuangWrapper = function(ad, staff){
        var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
        wrap.style.position = "fixed";
		wrap.style.zIndex = "2147483647";
		wrap.style.width = staff.width + "px";
        wrap.style.height = staff.height + "px";
		
		if(mim_isset(ad.data.view_position)){
			var centerPos = (1 - (staff.width / document.body.clientWidth))/2*100;
			var centerPosV = (1 - (staff.height / document.body.clientHeight))/2*100;
			switch(ad.data.view_position) {
				case "1":
				    wrap.style.top = "0px";
        			wrap.style.left = centerPos + "%";
					break;
				case "2":
				    wrap.style.top = "0px";
        			wrap.style.right = "0px";
					break;
				case "3":
				    wrap.style.top = centerPosV + "%";
        			wrap.style.right = "0px";
					break;
				case "5":
        			wrap.style.bottom = "0px";
					wrap.style.left = centerPos + "%";
					break;
				case "6":
        			wrap.style.bottom = "0px";
					wrap.style.left = "0px";
					break;
				case "7":
        			wrap.style.top = centerPosV + "%";
					wrap.style.left = "0px";
					break;
				case "8":
        			wrap.style.top = "0px";
					wrap.style.left = "0px";
					break;
				default:
					wrap.style.right = "0px";
        			wrap.style.bottom = "0px";
					break;
			}
		}else{
			wrap.style.right = "0px";
        	wrap.style.bottom = "0px";
		}
        var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
        wrap_content.style.position = "relative";
        wrap_content.style.width = "100%";
        wrap_content.style.height = "100%";
		api.createCloseBtn(ad, staff, wrap, wrap_content, 6);	//创建关闭按钮
        wrap.appendChild(wrap_content);
        return wrap
    };
	
	api.genDitongWrapper = function(ad, staff){
		var newWidth 	= staff.width;
		var newHeight 	= staff.height;
		var winWidth	= document.body.clientWidth;
		var winHeight	= document.body.clientHeight;
		if(api.isMobile()){
			newWidth 	= winWidth;
			newHeight 	= newWidth/staff.width * staff.height;
		}
        var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
        wrap.style.position = "fixed";
        wrap.style.zIndex = "2147483647";
        wrap.style.textAlign = "center";
		wrap.style.width = newWidth +"px";
		wrap.style.height = newHeight +"px";
		wrap.style.left = (winWidth - newWidth)/2 + "px";
		if(mim_isset(ad.data.view_position) && ad.data.view_position == 1){
			wrap.style.top = "0px";
		}else{
			wrap.style.bottom = "0px";
		}
        var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
        wrap_content.style.position = "relative";
		wrap_content.style.width = "100%";
		wrap_content.style.height = "100%";
		api.createCloseBtn(ad, staff, wrap, wrap_content, 8);	//创建关闭按钮
        wrap.appendChild(wrap_content);
        return wrap
    };
    
    api.genChayeWrapper = function(ad, staff){
		var newWidth 	= staff.width;
		var newHeight 	= staff.height;
		var winWidth	= document.body.clientWidth;
		var winHeight	= document.body.clientHeight;
		if(api.isMobile()){
			newWidth 	= winWidth;
			newHeight 	= newWidth/staff.width * staff.height;
		}else if(staff.width > winWidth*0.618){
			newWidth 	= winWidth*0.618;
			newHeight 	= newWidth/staff.width * staff.height;
		}
		
		var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
        wrap.style.position = "fixed";
		wrap.style.zIndex = "2147483647";
		wrap.style.width = newWidth + "px";
		wrap.style.height = newHeight + "px";
		wrap.style.left = "50%";
		wrap.style.top = "50%";
		wrap.style.margin = "-"+newHeight/2+"px 0 0 -"+newWidth/2+"px";
		
        var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
        wrap_content.style.position = "relative";
        wrap_content.style.width = "100%";
        wrap_content.style.height = "100%";
        wrap_content.style.zIndex = 2147483647;
		
		var chaye_bg = document.createElement("div");
        chaye_bg.id = ad.sn + "_bg";
        chaye_bg.style.position = "fixed";
        chaye_bg.style.width = "100%";
        chaye_bg.style.height = "100%";
        chaye_bg.style.background = "rgba(0, 0, 0,0.7)";
        chaye_bg.style.top = "0px";
        chaye_bg.style.left = "0px";
		chaye_bg.style.zIndex = "2147483646";
		chaye_bg.setAttribute("onclick", "javascript:document.getElementById('" + wrap.id + "').style.display='none';");
		
		//创建关闭按钮
		api.createCloseBtn(ad, staff, wrap, wrap_content, 7);
		wrap.appendChild(wrap_content);
        wrap.appendChild(chaye_bg);
        return wrap;
    };
	
	api.genDuiLianWrapper = function(ad, staff){
        var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
        wrap.style.position = "fixed";
		wrap.style.zIndex = "2147483647";
		wrap.style.width = staff.width + "px";
		wrap.style.height = staff.height + "px";
		
		var centerPosV = (1 - (staff.height / document.body.clientHeight))/2*100;
		wrap.style.top = "50%";
		wrap.style.margin = "-" + staff.height/2 +"px 0 0 0";
		wrap.style.right = "0px";
		
		var wrapLeft = wrap;
		wrapLeft.style.left = "0px";
		
        var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
        wrap_content.style.position = "relative";
        wrap_content.style.width = staff.width + "px";
        wrap_content.style.height = staff.height + "px";
        wrap.appendChild(wrap_content);
        return wrap;
    };
	
	api.createCloseBtn = function(ad, staff, wrap, wrap_content, btnSize){
		var para = "?";
		var click_js = show_js = "";
		mim_isset(ad.data.aid)?(para += "&aid=" + ad.data.aid):0;
		mim_isset(ad.data.pushid)?(para += "&pushid=" + ad.data.pushid):0;
		mim_isset(ad.data.spid)?(para += "&spid=" + ad.data.spid):0;
		mim_isset(ad.data.src)?(para += "&src=" + ad.data.src):0;
		mim_isset(staff.slotid)?(para += "&slotid=" + staff.slotid):0;
		if (ad.data.click_js) {
			click_js = ad.data.click_js + para;
		}
		
		if (staff.width > 10 || staff.height > 10) {
			var statJS = "";
			var clickJs = "";
			var responsiveType = ["AD_IMAGE","AD_FLASH","1","2",1,2];
			if(mim_isset(staff.landing_page) && responsiveType.indexOf(ad.data.staff[0].type) != -1){
				if (staff.landing_page.indexOf("http") == -1) {
					staff.landing_page = "http://" + staff.landing_page
				}
				//statJS = " window.open('http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page) + "');";
				statJS = " window.open('"+staff.landing_page+"');";
				clickJs	= " javascript:BCMain.click_js('" + click_js + "','" + escape(ad.data.third_click_js) + "','" + ad.sn + "','" + ad.data.aid + "','" + ad.data.pushid + "','" + ad.data.spid  + "','" + ad.data.src + "');"
			}
			closeJs = "javascript:document.getElementById('" + wrap.id + "').style.display='none'";
			closeImgSrc = "http://" + api._js + "/assets_admin/img/close-white.png";
			var closeImg = '<img onclick="'+closeJs+'" src="'+closeImgSrc+'" style="width:12px; position: relative; z-index: 2147483647; cursor: pointer;"/>';
			var clickDiv = '<div onclick="'+clickJs+statJS+'" style="position: absolute; z-index:0; width: 60%; height: 60%; top: 20%; left: 20%; cursor: pointer;"></div>'
		    var closeDiv = '<div style="line-height: 100%; padding:'+btnSize + 'px; background:rgba(0,0,0,0.4); border-radius: 50%; font-size:0px;">'+closeImg+clickDiv+'</div>';
		}else {
		    var closeDiv = ""
		}
		var c_content = document.createElement("div");
		c_content.id = "ad_colse_container";
        c_content.style.position = "absolute";
        c_content.style.right = "10px";
        c_content.style.top = "5px";
		c_content.style.zIndex = "2147483647";
        c_content.innerHTML = closeDiv;
		
		if (!api.isMobile() && ad.data.view_type == 1 && (mim_isset(ad.data.view_position) && ad.data.view_position == 1)) {
			c_content.style.right = (document.body.clientWidth - staff.width)/2 + 5 +"px";
		}
		wrap_content.appendChild(c_content);
	}
	
    api.renderMobileView = function(ad, staff){
        var para = "?";
		var click_js = show_js = "";
		mim_isset(ad.data.aid)?(para += "&aid=" + ad.data.aid):0;
		mim_isset(ad.data.pushid)?(para += "&pushid=" + ad.data.pushid):0;
		mim_isset(ad.data.spid)?(para += "&spid=" + ad.data.spid):0;
		mim_isset(ad.data.src)?(para += "&src=" + ad.data.src):0;
		mim_isset(staff.slotid)?(para += "&slotid=" + staff.slotid):0;
		if (ad.data.click_js) {click_js = ad.data.click_js + para}
		if (ad.data.show_js)  {show_js = ad.data.click_js + para}
        if (staff.landing_page.indexOf("http") == -1) {
            staff.landing_page = "http://" + staff.landing_page
        }
        //wrap = api.genNormal(ad, staff);
		
		var wrap = document.createElement("div");
        wrap.id = ad.sn + "_wrap";
        var wrap_content = document.createElement("div");
        wrap_content.id = ad.sn + "_content";
		wrap_content.style.margin = "auto";
		wrap_content.style.width = "0px";
    	wrap_content.style.height = "0px";
		wrap_content.style.zIndex= "2147483647";

		/*CLOSE BTN start*/
		if (api.isMobile() || staff.width>10000) {
			var statJS = "";
			var clickJs = "";
			closeJs = "javascript:document.getElementById('" + wrap.id + "').style.display='none'";
			closeImgSrc = "http://" + api._js + "/assets_admin/img/close-white.png";
			if (staff.width > 10 || staff.height > 10) {
				var closeImg = '<img onclick="' + closeJs + '" src="' + closeImgSrc + '" style="width:15px; position: relative; z-index: 2147483647; cursor: pointer;"/>';
				var closeDiv = '<div style="line-height: 100%; padding:5px; background:rgba(0,0,0,0.4); font-size:0px;">' + closeImg + '</div>';
			}else {
				var closeDiv = ""
			}
		}
		/*CLOSE BTN end*/
		
        wrap.appendChild(wrap_content);
		
        var p = document.createElement("div");
        var c = '<div id="ad_colse_container" style="position: absolute; right: 0px; top: 0px; z-index: 2147483647;">'+closeDiv+'</div>';
		var html = '<div id="'+ad.sn+'_div" style="width:100%; height:100%; overflow :hidden; position:fixed; left:0px; top:0px;z-index:998;"><div style=" right:0px; bottom:0px; position:absolute;z-index:999;font-size: 0;">' + c + '<img style="width:' + staff.width + "px;height:" + staff.height + 'px;" onclick="BCMain.renderMobileViewIframe(\'' + ad.sn + "','" + staff.landing_page + "');BCMain.click_js('" + click_js + "','" + escape(ad.data.third_click_js) + "','" + ad.sn + "','" + ad.data.aid + "','" + ad.data.pushid + "','" + ad.data.spid + "','" + ad.data.src + '\');" src="' + staff.addr + '" /></div> <iframe style="border:0; transition:width 0.5s; -moz-transition:width  0.5s; -webkit-transition:width  0.5s; -o-transition:width  0.5s; position:fixed;right:0px;" id="' + ad.sn + '_i" width="0" height="0" src="" border="0" scroll="no" ></iframe></div> ';
        p.innerHTML = html;
        wrap.childNodes[0].appendChild(p);
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "pic",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
        else {
            d.parentNode.insertBefore(wrap, d)
        }
        api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid,ad.data.spid, ad.data.src);
        var log = {
            type: "show ok",
            typecode: "bg",
            aid: ad.data.aid,
            pushid: ad.data.pushid,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log)
    };
    api.renderMobileViewIframe = function(sn, addr){
        var iframe = document.getElementById(sn + "_i");
        var src = iframe.getAttribute("src");
        if (!src) {
            iframe.src = addr;
            console.log("EM")
        }
        var width = iframe.getAttribute("width");
        if (width == 0) {
            iframe.width = "100%";
            iframe.height = "100%"
        }else {
            iframe.width = "0";
            iframe.height = "0"
        }
		iframe.border = "0";
    };
    api.renderMobileApp = function(ad, staff){
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        var click_js = ad.data.click_js + para;
        var show_js = ad.data.show_js + para;
        api.load("http://cdn.tanx.com/t/tanxmobile/mraid.js", function(){
            var img = document.createElement("img");
            img.style.width = staff.width + "px";
            img.style.height = staff.height + "px";
            img.onload = function(){
                mraid.show();
                api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid,ad.data.pushid,ad.data.spid, ad.data.src);
                var log = {
                    type: "show ok",
                    typecode: "bg",
                    aid: ad.data.aid,
                    pushid: ad.data.pushid,
                    referer: document.referrer,
                    url: document.location.href
                };
                api.log(log)
            };
            img.src = staff.addr;
            img.onclick = function(){
                mraid.open(staff.landing_page);
                api.click_js(click_js, escape(ad.data.third_click_js), ad.sn, ad.data.aid,ad.data.pushid);
                return false
            };
            wrap = api.genNormal(ad, staff);
            wrap.childNodes[0].appendChild(img);
            var d = document.getElementById(ad.sn);
            if (!d || !d.parentNode) {
                var log = {
                    type: "show error",
                    typecode: "pic",
                    error: "get adid empty",
                    sn: ad.sn,
                    pushid: ad.data.pushid,
                    referer: document.referrer,
                    url: document.location.href
                };
                api.log(log)
            }
            else {
                d.parentNode.insertBefore(wrap, d)
            }
        })
    };
    api.renderDirect = function(ad, staff){
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        var click_js = ad.data.click_js + para;
        var show_js = ad.data.show_js + para;
        api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid,ad.data.spid, ad.data.src);
        var log = {
            type: "show ok",
            typecode: "bg",
            aid: ad.data.aid,
            pushid: ad.data.pushid,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log);
        api.click_js(click_js, escape(ad.data.third_click_js), ad.sn, ad.data.aid,ad.data.pushid);
        staff.landing_page = staff.landing_page.replace("%URL%", document.location);
        if (staff.landing_page.indexOf("http") == -1) {
            staff.landing_page = "http://" + staff.landing_page
        }
        setTimeout(function(){
            try {
                top.location.replace(staff.landing_page)
            } 
            catch (e) {
                document.location.replace(staff.landing_page)
            }
        }, 1000)
    };
    api.renderBgAd = function(ad, staff){
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        var click_js = ad.data.click_js + para;
        var show_js = ad.data.show_js + para;
        if (staff.landing_page.indexOf("http") == -1) {
            staff.landing_page = "http://" + staff.landing_page
        }
        api.api_bg_loading(staff.landing_page, show_js, ad.data.third_show_js, click_js, ad.data.third_click_js, ad.sn, ad.data.aid, ad.data.pushid)
    };
    api.renderPic = function(ad, staff, wrap){
        var p = document.createElement("div");
		if (ad.data.view_type != 1 && ad.data.view_type != 32 && ad.data.view_type != 256) {
			p.style.height = ad.data.h + "px";
		}
        p.style.width = "100%";
        p.style.overflow = "hidden";
        p.style.fontSize = "0";
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        var click_js = ad.data.click_js + para;
        var show_js = ad.data.show_js + para;
        var href = "";
        if (typeof staff.landing_page != "undefined" && staff.landing_page != "undefined") {
            if (staff.landing_page.indexOf("http") == -1) {
                staff.landing_page = "http://" + staff.landing_page
            }
            //href = " href='http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page) + "' ";
            var href = " href='"+staff.landing_page+"' ";
        }
		if(ad.data.view_type == 1 || ad.data.view_type == 32 || ad.data.view_type == 256){
			p.innerHTML = "<a onclick='javascript:BCMain.click_js(\"" + click_js + '","' + escape(ad.data.third_click_js) + '","' + ad.sn + '","' + ad.data.aid + '","' + ad.data.pushid + '","' + ad.data.spid + '","' + ad.data.src + "\");' style='' " + href + " target='_blank'><img  width='100%'  src='" + staff.addr + "'/></a>";
		}else{
			p.innerHTML = "<a onclick='javascript:BCMain.click_js(\"" + click_js + '","' + escape(ad.data.third_click_js) + '","' + ad.sn + '","' + ad.data.aid + '","' + ad.data.pushid + '","' + ad.data.spid + '","' + ad.data.src + "\");' style='' " + href + " target='_blank'><img  width='100%'  height='" + staff.height + "' src='" + staff.addr + "'/></a>";
		}
        api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid,ad.data.spid, ad.data.scr);
        if (wrap != "") {
            wrap.childNodes[0].appendChild(p)
        }
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "pic",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }else {
            if (wrap != "") {
                d.parentNode.appendChild(wrap);
				
				if(ad.data.view_type == 512){
					var closeHtml = document.createElement("div");
					closeHtml.style.width = "100%";
					closeHtml.style.background = "rgba(0,0,0,0.7)";
					closeHtml.style.fontSize = "10px";
					closeHtml.style.lineHeight = "22px";
					closeHtml.style.textAlign = "center";
					closeHtml.style.color = "#FFFFFF";
					closeHtml.style.cursor = "pointer";
					closeJs = "javascript:document.getElementById('" + wrap.id + "').style.display='none'; javascript:document.getElementById('" + wrap.id + "_right').style.display='none';";
					closeHtml.setAttribute("onclick", closeJs);
					closeHtml.innerText = "关闭";
					wrap.appendChild(closeHtml);
					
					var node = document.getElementById(wrap.id).parentNode.lastChild.cloneNode(true);
					node.id = node.id+"_right";
					node.style.left = "auto";
					document.getElementById(wrap.id).parentNode.appendChild(node);
				}
            }else {
                d.parentNode.insertBefore(p, d);
            }
            var log = {
                type: "show ok",
                typecode: "pic",
                aid: ad.data.aid,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
    };
    api.renderSwf = function(ad, staff, wrap){
        var p = document.createElement("div");
        p.style.height = ad.data.h + "px";
        p.style.width = "100%";
        p.style.overflow = "hidden";
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        var click_js = ad.data.click_js + para;
        var show_js = ad.data.show_js + para;
        var h = '<div style="height: ' + ad.data.h + "px; width: " + ad.data.w + 'px; overflow: hidden;position:relative;margin:auto;">';
        if (typeof staff.landing_page != "undefined" && staff.landing_page != "undefined") {
            if (staff.landing_page.indexOf("http") == -1) {
                staff.landing_page = "http://" + staff.landing_page
            }
            //var href = "http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page);
			var href = " href='"+staff.landing_page+"' ";
            h += "<a onclick=\"javascript:BCMain.click_js('" + click_js + "','" + escape(ad.data.third_click_js) + "','" + ad.sn + "','" + ad.data.aid + "','" + ad.data.pushid + "','" + ad.data.spid + "','" + ad.data.src + '\');" ' + href + ' target="_blank" style="position: absolute;left:0px;top: 0px;width:' + ad.data.w + "px;height:" + ad.data.h + 'px">';
            h += '<object type="img/gif" style="border:0px"  border=0>';
            h += '<img width="100%" src="http://' + api._js + '/assets/images/null.gif" style="border:0px" height="100%" />';
            h += "</object>";
            h += "</a>   "
        }
        h += '<object width="' + ad.data.w + '" height="' + ad.data.h + '">';
        h += '<param name="movie" value="' + staff.addr + '">';
        h += '<param name="wmode" value="transparent">';
        h += '<embed src="' + staff.addr + '" type="application/x-shockwave-flash" menu="false" wmode="transparent" style="width:' + ad.data.w + "px;height:" + ad.data.h + 'px"/>';
        h += "</object>";
        h += "</div>";
        p.innerHTML = h;
        if (wrap != "") {
            wrap.childNodes[0].appendChild(p)
        }
        api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid,ad.data.spid, ad.data.src);
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "swf",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
        else {
            if (wrap != "") {
				d.parentNode.appendChild(wrap);
                //d.parentNode.insertBefore(wrap, d);
				if(ad.data.view_type == 512){
					var closeHtml = document.createElement("div");
					closeHtml.style.width = "100%";
					closeHtml.style.background = "rgba(0,0,0,0.7)";
					closeHtml.style.fontSize = "10px";
					closeHtml.style.lineHeight = "22px";
					closeHtml.style.textAlign = "center";
					closeHtml.style.color = "#FFFFFF";
					closeHtml.style.cursor = "pointer";
					closeJs = "javascript:document.getElementById('" + wrap.id + "').style.display='none'; javascript:document.getElementById('" + wrap.id + "_right').style.display='none';";
					closeHtml.setAttribute("onclick", closeJs);
					closeHtml.innerText = "关闭";
					wrap.appendChild(closeHtml);
					
					var node = document.getElementById(wrap.id).parentNode.lastChild.cloneNode(true);
					node.id = node.id+"_right";
					node.style.left = "auto";
					document.getElementById(wrap.id).parentNode.appendChild(node);
				}
            }
            else {
                d.parentNode.insertBefore(p, d);
            }
            var log = {
                type: "show ok",
                typecode: "swf",
                aid: ad.data.aid,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
    };
    api.renderJs = function(ad, staff, wrap){
        var click_js = show_js = "";
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        if (ad.data.click_js) {
            click_js = ad.data.click_js + para
        }
        if (ad.data.show_js) {
            show_js = ad.data.show_js + para
        }
        var href = "";
        if (typeof staff.landing_page != "undefined" && staff.landing_page != "undefined") {
            if (staff.landing_page.indexOf("http") == -1) {
                staff.landing_page = "http://" + staff.landing_page
            }
            //href = " href='http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page) + "' ";
			var href = " href='"+staff.landing_page+"' ";
        }
        if (show_js != "") {
            api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid, ad.data.spid, ad.data.src)
        }
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "js",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
        else {
            api.js(staff.text, function(){});
            var log = {
                type: "show ok",
                typecode: "js",
                aid: ad.data.aid,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
    };
    api.renderIFRAME = function(ad, staff, wrap){
        var click_js = show_js = "";
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        if (ad.data.click_js) {
            click_js = ad.data.click_js + para
        }
        if (ad.data.show_js) {
            show_js = ad.data.show_js + para
        }
        var href = "";
        if (typeof staff.landing_page != "undefined" && staff.landing_page != "undefined") {
            if (staff.landing_page.indexOf("http") == -1) {
                staff.landing_page = "http://" + staff.landing_page
            }
            //href = " href='http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page) + "' ";
			var href = " href='"+staff.landing_page+"' ";
        }
        var p = document.createElement("iframe");
        p.frameBorder = "0";
        p.style.overflow = "hidden";
        p.style.border = "0px";
        p.style.cssFloat = "left";
		p.style.width = "100%";
		p.style.height = "100%";
        p.setAttribute("sn", ad.sn);
        p.setAttribute("src", staff.text);
        p.setAttribute("aid", ad.data.aid);
        p.setAttribute("scrolling", "no");
        if (ad.data.third_click_js) {
            p.setAttribute("third_click_js", ad.data.third_click_js)
        }
        if (show_js != "") {
            api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid, ad.data.spid, ad.data.src)
        }
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "js",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
        else {
            if (wrap != "") {
                wrap.childNodes[0].appendChild(p);
                api.writeO(wrap, d);
				
				if(ad.data.view_type == 512){
					var closeHtml = document.createElement("div");
					closeHtml.style.width = "100%";
					closeHtml.style.background = "rgba(0,0,0,0.7)";
					closeHtml.style.fontSize = "10px";
					closeHtml.style.lineHeight = "22px";
					closeHtml.style.textAlign = "center";
					closeHtml.style.color = "#FFFFFF";
					closeHtml.style.cursor = "pointer";
					closeJs = "javascript:document.getElementById('" + wrap.id + "').style.display='none'; javascript:document.getElementById('" + wrap.id + "_right').style.display='none';";
					closeHtml.setAttribute("onclick", closeJs);
					closeHtml.innerText = "关闭";
					wrap.appendChild(closeHtml);
					
					var node = document.getElementById(wrap.id).parentNode.lastChild.cloneNode(true);
					node.id = node.id+"_right";
					node.style.left = "auto";
					document.getElementById(wrap.id).parentNode.appendChild(node);
				}
            }
            else {
                api.writeO(p, d)
            }
            var log = {
                type: "show ok",
                typecode: "js",
                aid: ad.data.aid,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
    };
    api.renderHTML = function(ad, staff, wrap){
        var click_js = show_js = "";
        var para = "?";
        if (typeof ad.data.aid != "undefined") {
            para += "&aid=" + ad.data.aid
        }
        if (typeof ad.data.pushid != "undefined") {
            para += "&pushid=" + ad.data.pushid
        }
        if (typeof ad.data.spid != "undefined") {
            para += "&spid=" + ad.data.spid
        }
        if (typeof ad.data.src != "undefined") {
            para += "&src=" + ad.data.src
        }
        if (typeof staff.slotid != "undefined") {
            para += "&slotid=" + staff.slotid
        }
        if (ad.data.click_js) {
            click_js = ad.data.click_js + para
        }
        if (ad.data.show_js) {
            show_js = ad.data.show_js + para
        }
        var href = "";
        if (typeof staff.landing_page != "undefined" && staff.landing_page != "undefined") {
            if (staff.landing_page.indexOf("http") == -1) {
                staff.landing_page = "http://" + staff.landing_page
            }
            //href = " href='http://" + api._redirect + "/stat.log.redirect?url=" + escape(staff.landing_page) + "' ";
			var href = " href='"+staff.landing_page+"' ";
        }
        var p = document.createElement("iframe");
        p.frameBorder = "0";
        p.style.height = "100%";
        p.style.width = "100%";
        p.style.overflow = "hidden";
        p.style.border = "0px";
        p.setAttribute("sn", ad.sn);
        p.setAttribute("aid", ad.data.aid);
        p.setAttribute("scrolling", "no");
        if (ad.data.third_click_js) {
            p.setAttribute("third_click_js", ad.data.third_click_js)
        }
        if (show_js != "") {
            api.show_js(show_js, escape(ad.data.third_show_js), ad.sn, ad.data.aid, ad.data.pushid,ad.data.spid,ad.data.src)
        }
        var d = document.getElementById(ad.sn);
        if (!d || !d.parentNode) {
            var log = {
                type: "show error",
                typecode: "js",
                error: "get adid empty",
                sn: ad.sn,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
        else {
            if (wrap != "") {
                wrap.childNodes[0].appendChild(p);
                api.writeO(wrap, d);
            }else {
                api.writeO(p, d);
            }
            p.contentWindow.contents = ('<html><head></head><body marginwidth="0" marginheight="0" style="background-color: transparent;">' + staff.text + "</body></html>");
            p.src = "javascript:window.contents";
            var log = {
                type: "show ok",
                typecode: "js",
                aid: ad.data.aid,
                pushid: ad.data.pushid,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log)
        }
    };
    api.monitor = function(iframe){
        iframe.onmouseover = function(){
            api.isOverIFrame = true
        };
        iframe.onmouseout = function(){
            api.isOverIFrame = false;
            var t = top ? top : window;
            if (t) {
                setTimeout(t.focus, 0)
            }
        };
        var t = top ? top : window;
        var click_js = iframe.getAttribute("click_js");
        var third_click_js = iframe.getAttribute("third_click_js");
        var sn = iframe.getAttribute("sn");
        var aid = iframe.getAttribute("aid");
        if (typeof window.attachEvent !== "undefined") {
            t.attachEvent("onblur", function(){
                processIFrameClick(click_js, third_click_js, sn, aid)
            })
        }
        else {
            if (typeof window.addEventListener !== "undefined") {
                t.addEventListener("blur", function(){
                    processIFrameClick(click_js, third_click_js, sn, aid)
                }, false)
            }
        }
        function processIFrameClick(click_js, third_click_js, sn, aid){
            if (api.isOverIFrame) {
                api.isOverIFrame = false;
                BCMain.click_js(click_js, escape(third_click_js), sn, aid)
            }
            var t = top ? top : window;
            if (t) {
                setTimeout(t.focus(), 0)
            }
        }
    };
    api.write = function(str, ele){
        try {
            var s = document.createElement("span");
            s.innerHTML = str;
            api.writeO(s, ele)
        } 
        catch (e) {
            document.write(str)
        }
    };
    api.writeO = function(s, ele){
        if (ele) {
            ele.parentNode.appendChild(s)
        }else {
            var e = ele || document.all || document.getElementsByTagName("*");
            var ele = e[e.length - 1]
			ele.parentNode.appendChild(s)
        }
    };
    api.writeBefore = function(str, ele){
        try {
            var s = document.createElement("span");
            s.innerHTML = str;
            api.writeOBefore(s, ele)
        } 
        catch (e) {
            document.write(str)
        }
    };
    api.writeOBefore = function(s, ele){
        if (ele) {
            ele.parentNode.appendChild(s)
        }else {
            var e = ele || document.all || document.getElementsByTagName("*");
            var ele = e[e.length - 1]
			ele.parentNode.insertBefore(s,ele.parentNode.childNodes[0]);
        }
    };
    api.third_req = function(){
        if (api._third_req) {
            api.js(api._third_req, function(){})
        }
    };
    api.show = function(){
        var sn = "ads" + Math.ceil(Math.random() * 100000000);

        var para = "?sn=" + sn;
	var time = new Date().getTime();
	para += "&time="+time;
        var pushid = "";
        if (typeof mim_aid != "undefined") {
            para += "&aid=" + mim_aid
        }
        if (typeof mim_spid != "undefined") {
            para += "&spid=" + mim_spid
        }
        if (typeof mim_sp != "undefined") {
            para += "&sp=" + mim_sp
        }
        if (typeof mim_uid != "undefined" && mim_uid != "") {
            para += "&sda_man=" + mim_uid
        }
        if (typeof mim_slotid != "undefined") {
            para += "&slotid=" + mim_slotid
        }
        if (typeof mim_src != "undefined") {
            para += "&src=" + mim_src
        }
        if (typeof mim_viewtype != "undefined") {
            para += "&viewtype=" + mim_viewtype
        }
        if (typeof mim_w != "undefined") {
            para += "&w=" + mim_w
        }
        if (typeof mim_h != "undefined") {
            para += "&h=" + mim_h
        }
        if (typeof mim_atype != "undefined") {
            para += "&atype=" + mim_atype
        }
        if (typeof mim_pushid != "undefined") {
            para += "&pushid=" + mim_pushid;
            pushid = mim_pushid
        }
        if (api.isMobile()) {
            para += "&mobile=1"
        }
        else {
            para += "&mobile=0"
        }
		
		if(typeof(mim_params)==='object'){
			for(var mim_index in mim_params){
				para += "&"+mim_index+"="+mim_params[mim_index]
			}
		}
		
        var w_width = document.body.clientWidth ? document.body.clientWidth : document.body.scrollWidt;
        var s_width = window.screen.width ? window.screen.width : window.screen.availHeight;
        var zoom = w_width / s_width;
        if (zoom == 1) {
            para += "&mobileFixed=1"
        }
        else {
            para += "&mobileFixed=0"
        }
        para += "&width_page=" + w_width;
        para += "&width_screen=" + s_width;
        para += "&url=" + escape(window.location.href);
        mim_aid = mim_spid = mim_src = mim_viewtype = mim_w = mim_h = mim_atype = mim_pushid = mim_slotid = undefined;
        var log = {
            type: "start",
            pushid: pushid,
            sn: sn,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log);
        if (typeof mim_infod != "undefined") {
            _ads = "http://" + mim_infod + "/"
        }
        else {
            _ads = "http://" + api._ads + "/"
        }
        api.InfoJS = _ads + "info.js" + para;
        api.js(api.InfoJS, function(){
            var log = {
                type: "loaded",
                pushid: pushid,
                sn: sn,
                referer: document.referrer,
                url: document.location.href
            };
            api.log(log);
            api.run(sn)
        })
    };
    api.api_bg_show_js = function(){
        api.show_js(api._bg_show_js, escape(api._bg_thrid_show_js), api._bg_sn, api._bg_aid, ad.data.pushid,ad.data.spid, ad.data.src);
        var log = {
            type: "show ok",
            typecode: "bg",
            aid: api._bg_aid,
            pushid: api._bg_pushid,
            referer: document.referrer,
            url: document.location.href
        };
        api.log(log)
    };
    api.api_bg_click_js = function(){
        api.click_js(api._bg_click_js, escape(api._bg_thrid_click_js), api._bg_sn, api._bg_aid,ad.data.pushid,ad.data.spid, ad.data.src)
    };
    api.api_bg_loading = function(url, show_js, third_show_js, click_js, third_click_js, sn, aid, pushid){
        api._bg_url = api.url = url;
        api._bg_show_js = show_js;
        api._bg_thrid_show_js = third_show_js;
        api._bg_click_js = click_js;
        api._bg_thrid_click_js = third_click_js;
        api._bg_aid = aid;
        api._bg_sn = sn;
        api._bg_pushid = pushid;
        this.api_bg_set_parameter(url);
        if (this.ver.ie || this.ver.tt) {
            api.write(this.ver._d1);
            api.write(this.ver._d2)
        }
        if (this.ver.cr || this.ver.sf) {
            api.write(this.ver._d4)
        }
        this.api_bg_open();
        window.focus()
    };
    api.api_bg_set_parameter = function(url){
        this.d = document;
        this.u = navigator.userAgent;
        this.stimer = 0;
        this.w = window.screen.width;
        this.h = window.screen.height;
        this.url = url;
        this.ver = {
            ie: /MSIE/.test(this.u),
            ie6: !/MSIE 7\.0/.test(this.u) && /MSIE 6\.0/.test(this.u) && !/MSIE 8\.0/.test(this.u) && !/MSIE 9\.0/.test(this.u),
            oldie: /MSIE 6\.0/.test(this.u) || /MSIE 7\.0/.test(this.u) || /MSIE 8\.0/.test(this.u),
            tt: /TencentTraveler/.test(this.u),
            i360: /360SE/.test(this.u),
            webkit: /WebKit/.test(this.u),
            cr: /Chrome/.test(this.u),
            cr27: /Chrome\/27/.test(this.u),
            ff: /Firefox/.test(this.u),
            sf: /Safari/.test(this.u),
            op: /Opera/.test(this.u),
            sg: /MetaSr/.test(this.u) && /WebKit/.test(this.u),
            sgall: /MetaSr/.test(this.u),
            mt: /Maxthon/.test(this.u) && /WebKit/.test(this.u),
            mtie: /Maxthon/.test(this.u) && /IE/.test(this.u),
            gg: window.google && window.chrome,
            _d1: '<object id="_pp_xcy_01" width="0" height="0" classid="CLSID:6BF52A52-394A-11D3-B153-00C04F79FAA6"></object>',
            _d2: '<object id="_pp_xcy_02" style="position:absolute;left:1px;top:1px;width:1px;height:1px;" classid="clsid:2D360201-FFF5-11d1-8D03-00A0C959BC0A"></object>',
            _d4: '<div id="_pp_xcy_04" style="display:none"><form action="' + this.url + '" method="get" name="__form_xcy" target="_blank"><input type="submit" style="display:none" id="__sumit_xcy"/></form></div>'
        }
    };
    api.api_bg_kdopen = function(){
        try {
            document.forms.__form_xcy.submit()
        } 
        catch (e) {
            document.getElementById("__sumit_xcy").click()
        }
        window.focus()
    };
    api.api_bg_reopen = function(){
        this.api_bg_open()
    };
    api.api_bg_open = function(){
        api.api_bg_show_js();
        if (this.ver.sg && this.ver.sf) {
            if (this.api_bg_interceptClick()) {
                api.api_bg_click_js();
                return
            }
        }
        if (!this.ver.gg && !this.ver.cr && !this.ver.op && !this.ver.sg && !this.ver.mt && !this.ver.sf) {
            if (this.api_bg_wopen()) {
                api.api_bg_click_js();
                return
            }
        }
        else {
            if (this.ver.webkit && !this.ver.sg && !this.ver.mt && !this.ver.cr27) {
                if (this.api_bg_wopen()) {
                    api.api_bg_click_js();
                    return
                }
            }
        }
        if (api.stimer == 0) {
            api.stimer = setTimeout("BCMain.api_bg_init()", 15)
        }
    };
    api.api_bg_hrefopen = function(){
        try {
            var c = document.createElement("a");
            c.setAttribute("href", api.url);
            c.setAttribute("target", "_blank");
            c.setAttribute("style", "display:none;");
            var b = document.createEvent("MouseEvents");
            b.initMouseEvent("click", false, false, window, 0, 0, 0, 0, 0, true, false, false, false, 0, null);
            c.dispatchEvent(b);
            return true
        } 
        catch (q) {
            return false
        }
    };
    api.api_bg_wopen = function(){
        if (this.ver.ie || this.ver.tt) {
            document.getElementById("_pp_xcy_01");
            document.getElementById("_pp_xcy_02");
            var obj = document.getElementById("_pp_xcy_02");
            if (!obj) {
                return
            }
            var wPop = null;
            if (!this.ver.ie6) {
                try {
                    var wPop = obj.DOM.Script.open(api.url, "_blank", b);
                    if (wPop) {
                        try {
                            wPop.blur();
                            window.focus();
                            wPop.focus();
                            return true
                        } 
                        catch (q) {
                        }
                    }
                } 
                catch (q) {
                }
            }
            try {
                if (this.ver.ie6 && !this.ver.sgall) {
                    setTimeout(function(){
                        var obj = document.getElementById("_pp_xcy_02");
                        try {
                            var wPop = obj.DOM.Script.open(api.url, "_blank", "");
                            if (wPop) {
                                wPop.blur();
                                window.focus()
                            }
                            else {
                                if (a.ver.sg) {
                                }
                            }
                        } 
                        catch (q) {
                        }
                    }, 10);
                    document.getElementById("_pp_xcy_01").launchURL(api.url, "_blank")
                }
                else {
                    document.getElementById("_pp_xcy_01").launchURL(api.url)
                }
                return true
            } 
            catch (q) {
            }
        }
        var b = "height=" + api.h + ",width=" + api.w + ",left=0,top=0,toolbar=yes,location=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes";
        if (this.ver.sg || this.ver.mt) {
            var j = 'window.open("/", "_blank", "' + b + '")'
        }
        else {
            if (!this.ver.mtie) {
                var j = 'window.open("' + api.url + '", "_blank", "' + b + '")'
            }
        }
        var m = null;
        try {
            m = eval(j)
        } 
        catch (q) {
        }
        if (m) {
            if (this.ver.sg || this.ver.mt) {
                m.location.href = api.url
            }
            try {
                m.blur();
                window.focus();
                return true
            } 
            catch (q) {
            }
            return m
        }
        return false
    };
    api.api_bg_interceptClick = function(){
        var tmpId = "__lgUnion_a__" + Math.ceil(Math.random() * 100);
        var tmp = document.createElement("a");
        tmp.href = bql_ed_open_url;
        tmp.id = tmpId;
        tmp.target = "_blank";
        tmp.style.position = "absolute";
        tmp.style.zIndex = "2147483647";
        tmp.style.backgroundColor = "#fff";
        tmp.style.opacity = "0.01";
        tmp.style.filter = "alpha(opacity:1)";
        tmp.style.display = "block";
        tmp.style.top = "0px";
        tmp.style.left = "0px";
        document.body.appendChild(tmp);
        var el = document.getElementById(tmp.id);
        var m = setInterval(function(){
            var d = document.body;
            e = document.documentElement;
            document.compatMode == "BackCompat" ? t = d.scrollTop : t = e.scrollTop == 0 ? d.scrollTop : e.scrollTop;
            el.style.top = t + "px";
            el.style.width = "100%";
            if (d.clientHeight < 500) {
                el.style.height = 600 + "px"
            }
            else {
                el.style.height = d.clientHeight + "px"
            }
        }, 200);
        api.api_bg_linkUp(tmpId);
        el.onclick = function(e){
            api.api_bg_removeInterceptClick(el, m);
            this.firstcgm = null
        };
        if (this.firstcgel == null) {
            this.firstcgel = el;
            this.firstcgm = m
        }
    };
    api.api_bg_removeInterceptClick = function(el, m){
        setTimeout(function(){
            el.parentNode.removeChild(el)
        }, 200);
        clearInterval(m)
    };
    api.api_bg_linkUp = function(obj){
        var tmp = document.getElementsByTagName("a");
        var tmps = tmp.length;
        for (var i = 0; i < tmps; i++) {
            if (tmp[i].id.indexOf(obj) != -1) {
                tmp[i].style.zIndex = 2147483647;
                tmp[i].style.display = "block"
            }
            else {
                if (tmp[i].style.zIndex == 2147483647) {
                    tmp[i].style.zIndex--
                }
            }
        }
    };
    api.api_bg_clickpppp = function(){
        api.api_bg_uinit();
        BCMain.api_bg_click_js();
        var m = api.api_bg_wopen();
        return m
    };
    api.api_bg_uinit = function(){
        try {
            if (api.stimer > 0) {
                clearInterval(api.stimer);
                api.stimer = 0
            }
            if (document.attachEvent) {
                document.detachEvent("onclick", BCMain.api_bg_clickpppp)
            }
            else {
                if (document.addEventListener) {
                    document.removeEventListener("click", BCMain.api_bg_clickpppp, false)
                }
            }
            if (this.ver.webkit) {
                document.onkeydown = null
            }
        } 
        catch (q) {
        }
    };
    api.attachEvent = false;
    api.api_bg_init = function(){
        try {
            if (!BCMain.attachEvent) {
                if (document.attachEvent) {
                    document.attachEvent("onclick", BCMain.api_bg_clickpppp)
                }
                else {
                    if (document.addEventListener) {
                        document.addEventListener("click", BCMain.api_bg_clickpppp, false)
                    }
                }
                BCMain.attachEvent = true
            }
            window.oldonkeydown = window.onkeydown;
            if (typeof document.onkeydown == "function") {
                if (typeof document.onkeydown == "object" && this.ver.webkit) {
                    document.onkeydown = function(){
                        api.api_bg_kdopen();
                        api.api_bg_uinit();
                        window.oldonkeydown()
                    }
                }
                else {
                    if (typeof document.onkeydown == "function" && this.ver.webkit) {
                        if (document.onkeydown.toString().indexOf("__form_xcy") < 0) {
                            document.onkeydown = function(){
                                BCMain.api_bg_kdopen();
                                api.api_bg_uinit();
                                window.oldonkeydown()
                            }
                        }
                    }
                }
            }
        } 
        catch (q) {
        }
    }
})(window, "BCMain");

function mim_isset(variableName){
    try { if (typeof(variableName) == "undefined") { return false; } else { return true; } } catch (e) {  }
    return false;
}
function mim_resize_ditong(){
	var newWidth = _m_stuff.width;
	var newHeight = _m_stuff.height;
	var winWidth	= document.body.clientWidth;
	var winHeight	= document.body.clientHeight;
	var wrap = document.getElementById(_m_ad.sn + '_wrap');
	if(_m_ismobile){
		newWidth 	= winWidth;
		newHeight 	= newWidth/_m_stuff.width * _m_stuff.height;
		wrap.style.width = newWidth +"px";
		wrap.style.height = newHeight +"px";
	}else{
		wrap.style.left = (winWidth - newWidth)/2 + "px";
	}
}
function mim_resize_chaye(){
    try {
		var wrap = document.getElementById(_m_ad.sn + '_wrap');
		var newWidth 	= _m_stuff.width;
		var newHeight 	= _m_stuff.height;
		var winWidth	= document.body.clientWidth;
		var winHeight	= document.body.clientHeight;
		var winHeight	= (document.documentElement.scrollHeight >document.documentElement.clientHeight) ? document.documentElement.scrollHeight : document.documentElement.clientHeight;
		if(_m_ismobile){
			newWidth 	= winWidth;
			newHeight 	= newWidth/_m_stuff.width * _m_stuff.height;
		}else if(_m_stuff.width > winWidth*0.618){
			newWidth 	= winWidth*0.618;
			newHeight 	= newWidth/_m_stuff.width * _m_stuff.height;
		}
		var pos_x = (winWidth - newWidth)/2;
		var pos_y = (winHeight - newHeight)/2;
		wrap.style.width = newWidth + "px";
		wrap.style.height = newHeight + "px";
		
		wrap.style.left = "50%";
		wrap.style.top = "50%";
		wrap.style.margin = "-"+newHeight/2+"px 0 0 -"+newWidth/2+"px";
    }catch (exception) {
		console.log(exception.message );
    }
}
function mim_resize_qianru(){
	var newWidth = _m_stuff.width;
	var newHeight = _m_stuff.height;
	var winWidth	= document.body.clientWidth;
	var winHeight	= document.body.clientHeight;
	if(_m_ismobile && (mim_isset(_m_ad.data.view_position) && _m_ad.data.view_position%4 == 1)){
		newWidth 	= winWidth;
		newHeight 	= newWidth/_m_stuff.width * _m_stuff.height;
		var wrap = document.getElementById(_m_ad.sn + '_content');
		wrap.style.width = newWidth + "px";
		wrap.style.height = newHeight + "px";
	}
	if(mim_isset(_m_ad.data.view_position) && _m_ad.data.view_position == 1){
		var wrapClose = document.getElementById('ad_colse_container');
		wrapClose.style.right = (winWidth - newWidth)/2 + 5 +"px";
	}
}
function mim_resize_fuchuang(){
	if(mim_isset(_m_ad.data.view_position)){
		var centerPos = (1 - (_m_stuff.width / document.body.clientWidth))/2*100;
		var centerPosV = (1 - (_m_stuff.height / document.body.clientHeight))/2*100;
		var wrap = document.getElementById(_m_ad.sn + '_wrap');
		if(_m_ad.data.view_position % 4 == 1 && document.body.clientWidth > _m_stuff.width){
			wrap.style.left = centerPos + "%";
		}else if(_m_ad.data.view_position % 4 == 3 && document.body.clientHeight > _m_stuff.height){
			wrap.style.top = centerPosV + "%";
		}
	}
}
function mim_resize_duilian(){
}
function mim_auto_close(){
    try {
        if (typeof(_m_ad.data.view_type) == "undefined") {
			console.log("no ad");
            return false;
        }
        else {
            if(_m_ad.data.view_type == 1 && (mim_isset(_m_ad.data.view_position) && _m_ad.data.view_position == 1)){
				var wrap = document.getElementById(_m_ad.sn + '_wrap');
				wrap.style.display = "none";
			}else if(_m_ad.data.view_type == 256){
				var wrap = document.getElementById(_m_ad.sn + '_wrap');
				wrap.style.display = "none";
			}
        }
    } 
    catch (e) {
    }
}
window.onresize=function(){
	try {
        if (typeof(_m_ad.data.view_type) == "undefined") {
            return false;
        }
        else {
            if (_m_ad.data.view_type == 1) {mim_resize_qianru();}
			if (_m_ad.data.view_type == 2) {mim_resize_fuchuang();}
			if (_m_ad.data.view_type == 32) {mim_resize_ditong();}
			if (_m_ad.data.view_type == 256){mim_resize_chaye();}
			if (_m_ad.data.view_type == 512){mim_resize_duilian();}
        }
    } 
    catch (e) {
    }
}
BCMain.show();
BCMain.third_req();
setTimeout("mim_auto_close()",8000);
