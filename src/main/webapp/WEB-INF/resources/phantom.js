"use strict";
var server = require('webserver').create();
var args = require('system').args;
var fs = require('fs');
var spawn = require("child_process").spawn
var execFile = require("child_process").execFile

//var url = require('url');
var port = args[2];
var storePath = "test/phantom.dat";
// start a server on passed port and register a request listener
server.listen(port, function(request, response) {
console.log("started on "+ new Date());
var jsonStr = request.post,params,msg;
console.log(JSON.stringify(request.post));
try {
params = JSON.parse(jsonStr);

if (params.status) {
 // for server health validation
	response.statusCode = 200;
	response.write('OK');
	response.close();	
} else {
var urlArray = new Array();//this is used to store networklog

//Mandatory parameters
var addurl = "";
if ( typeof params.url === 'undefined' || params.url.trim()=='' ) {  throw new Error("Please provide valid url"); }
addurl=params.url;
console.log("addurl" + addurl);


var proxy=params.proxy
if( typeof proxy !== 'undefined' ) { 
var proxy_info = proxy.split(',');
console.log("proxy_info" + proxy_info);
phantom.setProxy(proxy_info[0], proxy_info[1], 'manual', proxy_info[2], proxy_info[3]);
}
//phantom.setProxy('ca.p.geoedge.com', '443', 'manual','pubmatic', 'global12');
//phantom.setProxy('ca.p.geoedge.com', '443', 'manual','pubmatic', 'global12');
// default values for optional parameters
//params.store=false
if (params.action === undefined) params.action = "image";
if (params.userAgent === undefined) params.userAgent = 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.71 Safari/537.36';
if (params.store === undefined) params.store = false;
if (params.width === undefined) params.width = 600;
if (params.height === undefined) params.height = 100;
if (params.clipWidth === undefined) params.clipWidth = params.viewportWidth;
if (params.clipHeight === undefined) params.clipHeight = params.viewportWidth;
if (params.zoom === undefined) params.zoom = 1;
if (params.timeout === undefined) params.timeout = 30;
if (params.cookies === undefined) params.cookies = [];
if (params.userAgent === undefined) params.userAgent = null;
if (params.javascript === undefined) params.javascript = true;
if (params.maxBytes === undefined) params.maxBytes = 1024*1024*5; // 5 MiB
if (params.maxRedirects === undefined) params.maxRedirects = 40;


var actionArray = params.action.split(',');
console.log("actionArray" + actionArray);
console.log("params.store" + params.store);
var page = new WebPage();
page.resources = [];
page.onLoadStarted = function () {
        page.startTime = new Date();
    };
page.settings.userAgent = params.userAgent;
page.settings.localToRemoteUrlAccessEnabled = true;
page.onResourceRequested = function (req) {
page.resources[req.id] = {
            request: req,
            startReply: null,
            endReply: null
        };		
	if(urlArray.indexOf(req.url) == -1 ) {
			urlArray.push(req.url);
	}
};
page.onResourceReceived = function (res) {	
if (res.stage === 'start') {
            page.resources[res.id].startReply = res;
        }
        if (res.stage === 'end') {
            page.resources[res.id].endReply = res;
        }	
	if(urlArray.indexOf(res.url) == -1 ) {
                        urlArray.push(res.url);
        }

};

// Capture JS errors and ignore them
page.onError = function(msg, trace) {};

page.onAlert = function (msg) {
	console.log(msg);
};
//page.content = "<html><body><b>test</b><img src=\"http://www.google.co.uk/images/srpr/logo11w.png\" alt=\"\" border=\"0\" /></body><///html>";


page.viewportSize = {
  width: params.width
, height: params.height
};
var reload = true;
page.open(addurl);

page.onLoadFinished = function(status) {
if(reload) {
console.log("running");
reload = false;
    switch(status) {
        case 'success':
            // if success: create JSON according to documentation contract. exit with code:0
            window.setTimeout(function () { 
		page.evaluate(function() {
  	document.body.bgColor = 'white';
	});           
	       // page.render("test.png");
		var imageData = '';
		var thumbnail ='';
		if (actionArray.indexOf("image") > -1) {
		console.log("Capturing Image"); 	
    		imageData = page.renderBase64("PNG");
		page.zoomFactor = 0.25;
		page.viewportSize = {
  		width: params.width * 0.25
		, height: params.height * 0.25
		};
		console.log("Capturing Thumbnail"); 
		thumbnail=page.renderBase64("PNG");
		console.log(thumbnail);
	        }

		//var imageData = page.renderBase64("PNG",{format: 'jpeg', quality: '50'});
                var content = '';
		if (actionArray.indexOf("dom") > -1) {
    		console.log("Capturing DOM"); 
		var content = page.evaluate(function() {
		return document.getElementsByTagName('html')[0].innerHTML;
		});
		
        	//content = content.replace('/<script[^>]+>(.|\n|\r)*?<\/script\s*>/ig', '');
        	//content = content.replace('<meta name=\"fragment\" content=\"!\">', '');
	        }
		
		var netlog= '';
		if (actionArray.indexOf("netlog") > -1) {
		console.log("Capturing Netlog");
    		netlog = urlArray.join("#?#");
	        }

		var har ='';
		if (actionArray.indexOf("har") > -1) {
		console.log("Capturing HAR");
	        har = createHAR(page.address, page.title, page.startTime, page.resources);                
		}
                var resObj = {   
                    image: imageData,
		    thumbnail: thumbnail,
		    netlog: netlog,
		    dom : content,
		    har : har			 	
                };
                
		for(var key in resObj) {
   			 if(!resObj[key]) {
       			 delete resObj[key];
    			}
		}
                response.statusCode = 200;  
		if (params.store){		
			fs.write(storePath, JSON.stringify(resObj), 'a');
			response.setHeader('Content-Type', 'text/plain');
    			msg = "Data stored at:" + storePath ;
			response.write(msg);	
			
		} else {
			
			response.write(JSON.stringify(resObj));
		}
    		console.log("Completed on "+ new Date());
page.close();
response.close();





            }, 10000);
            
            window.setTimeout(function () { 
            	page.close();
            	throw new Error ("Error: HTTP request timeout");
            }, 20000);
            break;
        
        case 'fail': // TODO: DOES THIS FAIL IN CASE OF SUB-REQUEST FAILING? if so, must go around with onResourceReceived..?
        default:
            throw new Error ("Error: HTTP request failed");
    }
    
    
}
}
}
} catch (e) {
        console.log(e);
	msg = "Failed rendering: \n" + e;
	response.statusCode = 500;
	response.setHeader('Content-Type', 'text/plain');
	response.setHeader('Content-Length', msg.length);
	response.write(msg);
	response.close();
					}


function createHAR(address, title, startTime, resources)
{
    var entries = [];
    console.log("createHAR"); 	
    resources.forEach(function (resource) {
        var request = resource.request,
            startReply = resource.startReply,
            endReply = resource.endReply;

        if (!request || !startReply || !endReply) {
            return;
        }

        // Exclude Data URI from HAR file because
        // they aren't included in specification
        if (request.url.match(/(^data:image\/.*)/i)) {
            return;
	}

        entries.push({
            startedDateTime: request.time.toISOString(),
            time: endReply.time - request.time,
            request: {
                method: request.method,
                url: request.url,
                httpVersion: "HTTP/1.1",
                cookies: [],
                headers: request.headers,
                queryString: [],
                headersSize: -1,
                bodySize: -1
            },
            response: {
                status: endReply.status,
                statusText: endReply.statusText,
                httpVersion: "HTTP/1.1",
                cookies: [],
                headers: endReply.headers,
                redirectURL: "",
                headersSize: -1,
                bodySize: startReply.bodySize,
                content: {
                    size: startReply.bodySize,
                    mimeType: endReply.contentType
                }
            },
            cache: {},
            timings: {
                blocked: 0,
                dns: -1,
                connect: -1,
                send: 0,
                wait: startReply.time - request.time,
                receive: endReply.time - startReply.time,
                ssl: -1
            },
            pageref: address
        });
    });

    return {
        log: {
            version: '1.2',
            creator: {
                name: "PhantomJS",
                version: phantom.version.major + '.' + phantom.version.minor +
                    '.' + phantom.version.patch
            },
            pages: [{
                startedDateTime: startTime.toISOString(),
                id: address,
                title: title,
                pageTimings: {
                    onLoad: page.endTime - page.startTime
                }
            }],
            entries: entries
        }
    };
}

});
console.log("OK, PhantomJS is ready.");

	