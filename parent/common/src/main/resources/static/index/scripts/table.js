Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"h+": this.getHours(), //小时
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) 
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
$.fn.serializeObject = function () {
    var obj = {};
    var count = 0;
    $.each(this.serializeArray(), function (i, o) {
        var n = o.name, v = o.value;
        count++;
        obj[n] = obj[n] === undefined ? v
        : $.isArray(obj[n]) ? obj[n].concat(v)
        : [obj[n], v];
    });
    //obj.nameCounts = count + "";//表单name个数
    return obj;
};

function loadErrorDataAlert(data) {
	if(data.code && data.code == "ERROR") {
		var rows = "";
		var push = {
		"total" : 0,
		"rows" : rows
		};
		top.msgAlert(data.message , "error");
		return push;
	}
	return data;
}
function add(title,url,width,height) {
	top.showUpdateModel(title , url ,width , height ,{});
}
function update(title,url,width,height) {
	var row;
	if(tableType == "tree")
		row = $("#center-table").treegrid('getSelected');
	else 
		row = $("#center-table").datagrid('getSelected');
	if (!row){
		top.msgAlert("请选择需要修改的数据" , "error");
	}
	else {
		top.showUpdateModel(title , url ,width , height ,{id:row.id,_tms:new Date().getTime()});
	}
}
function deleteFunction(title,url,width,height) {
	var row;
	if(tableType == "tree")
		row = $("#center-table").treegrid('getSelected');
	else 
		row = $("#center-table").datagrid('getSelected');
	if (!row){
		top.msgAlert("请选择需要删除的数据" , "error");
	}
	else {
		top.msgConfirm("确认删除？",function(){
			top.ajax(url ,{id:row.id} , top.public_success);
		});
	}
}

function reloadPareme(title,url,width,height) {
	top.msgConfirm("确认重置？",function(){
		top.ajax(url ,{} , top.public_success);
	});
}

/**            ******************* 列表加载时 处理查询里面可能有 下拉框的情况   *************/


function loadSelectData() {
	$("select[loadkey='combobox']").each(function(){
		if($(this).attr("data") != undefined) {
			var id = $(this).attr("id");
			var data = $(this).attr("data");
			/*var dl = data.split(",");
			var o = "";
			for(var i = 0 ; i < dl.length ; i ++) {
				if(i != 0) o +=",";
				o += "{\"id\":\""+dl[i].split("_")[0]+"\",\"text\":\""+dl[i].split("_")[1]+"\""+(dl.length > 2 ?",\"selected\":true":"")+"}";
			}*/
			data = data.replace("#BEGIN#" ,"[");
			data = data.replace("#END#" ,"]");
			$("#"+id).combobox({
			    data:$.parseJSON(data),
			    valueField:'id',
			    textField:'text',
			    editable:false
			});
		}
	});
}
loadSelectData();

//过滤参数
function getQueryParame() {
	return $("#search-form").serializeObject();
}

//重新加载页面
function reloadTable() {
	if(tableType == "tree") {
		$("#center-table").treegrid("load" , getQueryParame());
	}
	else {
		$("#center-table").datagrid("load" , getQueryParame());
	}
}
/**            ****************       formatter  ******************************** **/
function public_state(v) {
	if(v == "1") return "<span style='color:green;'>正常</span>";
	if(v == "2") return "<span style='color:red;'>禁用</span>";
}
function public_date(v) {
	if (v == null || v == ''){
		return "";
	}
  
    return new Date(v.time).Format("yyyy-MM-dd hh:mm:ss");
}
function public_wbipType(v) {
	if (v == "1"){
		return "白名单";
	}
	if (v == "2"){
		return "黑名单";
	}
	return "";
}
function publit_ms(v) {
	if(v != undefined && v != null) return v+" (ms)";
	return v;
}