$.ajaxPrefilter(function(e){
	if(e.data != undefined && e.data != "" && e.data != null)
		e.data = "_jr="+encrypt(JSON.stringify(getParams(e.data)));
	else 
		e.data = "_jr="+encrypt(JSON.stringify({"_tms":new Date().getTime()}))
});

function getParams(url) {
    try {
    	var list = {};
        var obj = {}, arr = url.split('&');
        for (var i = 0; i < arr.length; i++) {
            var subArr = arr[i].split('=');
            var name = decodeURIComponent(subArr[0]);
            if(name.indexOf("[]") != -1) {
            	name = name.substring(0 , name.indexOf("[]"));
            	if(list[name]) {
            		list[name].push(decodeURIComponent(subArr[1]));
            	}
            	else {
            		list[name] = new Array();
            		list[name].push(decodeURIComponent(subArr[1]));
            	}
            	continue;
            }
            obj[decodeURIComponent(subArr[0])] = decodeURIComponent(subArr[1]);
        }
        if(Object.keys(list).length > 0) {
        	for(var prop in list){
        		obj[prop] = list[prop];
        	}
        }
        obj["_tms"] = new Date().getTime();
        return obj;

    } catch (err) {
    	console.log(err);
        return null;
    }
}

function encrypt(word){
    var key = $.cookie('secretKey');
    if(key == null) key = CryptoJS.enc.Utf8.parse("abcdefghijklmnopqrstuvwxyz");
    else key = CryptoJS.enc.Utf8.parse(key);;
    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return strToHexCharCode(encrypted.toString());
}

function strToHexCharCode(str) {
	　　if(str === "")
	　　　　return "";
	　　var hexCharCode = [];
	　　for(var i = 0; i < str.length; i++) {
	　　　　hexCharCode.push((str.charCodeAt(i)).toString(16));
	　　}
	　　return hexCharCode.join("");
	}
