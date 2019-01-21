function loadSelectData() {
	$("select[loadkey='combobox']").each(function(){
		if($(this).attr("data") != undefined) {
			var id = $(this).attr("id");
			$("#"+id).combobox({
			    data:$.parseJSON($(this).attr("data")),
			    valueField:'id',
			    textField:'text'
			});
		}
	});
	$("select[loadkey='combotree']").each(function(){
		if($(this).attr("data") != undefined) {
			var id = $(this).attr("id");
			$("#"+id).combotree({
			    data:$.parseJSON($(this).attr("data")),
			    valueField:$(this).attr("valueField"),
			    textField:$(this).attr("textField")
			});
		}
	});

	//加载数据
	if(data != undefined && data != null) {
		$("#update-center-table").find("[key]").each(function(){
			var name = $(this).attr("key");
			var id = $(this).attr("id");
			var val = data[name];
			if(name == undefined || name == null || name == "" || val == undefined || val == null || val == "" || id ==undefined || id == null || id == "") return true;
			try{
				if($(this).hasClass("easyui-passwordbox")) {
					$("#"+id).passwordbox("setValue", val);
				}
				if($(this).hasClass("easyui-textbox")) { //文本
					$("#"+id).textbox("setValue", val);
				}
				else if($(this).hasClass("easyui-combotree")) { //下拉树
					$("#"+id).combotree('setValue', val); 
				}
				else if($(this).hasClass("easyui-combobox")) {
					$("#"+id).combobox('setValue',val);
				}
				else if($(this).hasClass("easyui-datebox")){
					$("#"+id).datebox('setValue', val);
				}
				else if($(this).hasClass("easyui-datetimebox")){
					$("#"+id).datetimebox('setValue', val);
				}
				else {//非easyui控件
					$("#"+id).val(val);
				}
			}catch (e) {
				console.log(e);
				//top.msgAlert("加载数据失败" , "error");
				//top.closeIndex();
				return false;
			}
		});
	}
}
$.parser.parse('#update-center-table');

if(loadNum == 0) {
	loadNum = 1;
	updateLoadNum();
}
function updateLoadNum() {
	loadNum--;
	if(loadNum == 0) loadSelectData();
}

function replaceData(str ,f , e) {
	 var reg=new RegExp(f,"g"); //创建正则RegExp对象   
	 return str.replace(reg,e); 
}


$.fn.check = function() {
	var t = $(this);
	var msg = {"code":1,"msg":"效验通过"};
	$(t).find("[check]").each(function(){
		var checkJson = $(this).attr("check");
		if(checkJson == "") return true;
		checkJson = replaceData(checkJson,"'","\"");
		try{
			checkJson = JSON.parse(checkJson);
		}catch (e) {
			msg = {"code":-1,"msg":"效验参数错误"};
			return false;
		}
		var value = "";
		if($(this).hasClass("easyui-textbox")) { 
			value = $(this).textbox("getValue");
		}
		else if($(this).hasClass("easyui-passwordbox")) {
			value = $(this).passwordbox("getValue");
		}
		else if($(this).hasClass("easyui-combotree")) { 
			value = $(this).combotree("getValue");
		}
		else if($(this).hasClass("easyui-combobox")) {
			value = $(this).combobox("getValue");
		}
		else if($(this).hasClass("easyui-datetimebox")) {
			value = $(this).datetimebox("getValue");
		}
		else if($(this).hasClass("easyui-datebox")) {
			value = $(this).datebox("getValue");
		}
		var title = $(this).parent().siblings("th").html();
		if(checkJson.required != undefined && checkJson.required) {
			if(value == null || value == "") {
				msg = {"code":-1,"msg":title + "不能为空"};
				return false;
			}
		}
		if(checkJson.maxLength != undefined) {
			if(value.length > checkJson.maxLength) {
				msg = {"code":-1,"msg":title + "超过最大长度【"+checkJson.maxLength+"】"};
				return false;
			}
		}
		if(checkJson.minLength != undefined) {
			if(value.length < checkJson.minLength) {
				msg = {"code":-1,"msg":title + "不足最小长度【"+checkJson.minLength+"】"};
				return false;
			}
		}
		if(checkJson.number != undefined && checkJson.number) {
			if(isNaN(value)) {
				msg = {"code":-1,"msg":title + "必须是数字"};
				return false;
			}
			if(checkJson.maxNumber != undefined) {
				if(value > checkJson.maxNumber) {
					msg = {"code":-1,"msg":title + "超过最大值【"+checkJson.maxNumber+"】"};
					return false;
				}
			}
			if(checkJson.minNumber != undefined) {
				if(value < checkJson.minNumber) {
					msg = {"code":-1,"msg":title + "不足最小值【"+checkJson.minNumber+"】"};
					return false;
				}
			}
		}
	});
	return msg;
}