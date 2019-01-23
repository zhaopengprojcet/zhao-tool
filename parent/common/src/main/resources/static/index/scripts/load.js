/**
 * 首页左边菜单加载
 */
var zTree;
	
var setting = {
		view: {
			dblClickExpand: false,
			showLine: false,
			expandSpeed: ('undefined' == typeof(document.body.style.maxHeight))?"":"fast"
		},
		data: {
			key: {
				name: "menuName"
			},
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "parentMenuId",
				rootPId: "0"
			}
		},
		callback: {
// 				beforeExpand: beforeExpand,
// 				onExpand: onExpand,
			onClick: zTreeOnClick			
		}
};
$(document).ready(function(){
	loadMenu("dleft_tab1");
	// 默认展开所有节点
	if( zTree ){
		// 默认展开所有节点
		zTree.expandAll(false);
	}
	
	//加载功能
	$("#out-login").click(function(){
		msgConfirm("确认退出当前登录？",outLogin);
	});
	
	$("#seting-pass").click(function(){
		$("input[id^='user-pass-update']").each(function(){
			try{$("#"+this.id).passwordbox("setValue", "");}catch(e){}
		});
		$("#pass-dialog").dialog("open");
	});
});
//退出登录
function outLogin() {
	window.location = "/outLogin.html";
}
//更新密码
function updatePass() {
	var check = true;
	$("input[id^='user-pass-update']").each(function(){
		if($("#"+this.id).passwordbox("getValue") == "") {
			msgAlert("密码不能为空", "error");
			check = false;
			return false;
		}
	});
	if(!check) return;
	if($("#user-pass-update-new").passwordbox("getValue") != $("#user-pass-update-dnew").passwordbox("getValue")) {
		msgAlert("两次输入的新密码不同", "error");
		return;
	}
	ajax("/user/passUpdate.html" , $("#pass-form").serializeObject() , updatePassSuccess);
}
function updatePassSuccess(data) {
	top.msgAlert(data.message,data.code=="SUCCESS"?"info":"error");
}

var curExpandNode = null;

/** 用于捕获节点被点击的事件回调函数  **/
function zTreeOnClick(event, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("dleft_tab1");
	zTree.expandNode(treeNode, null, null, null, true);
	if(treeNode.isParent){
		return false;
	}
	if(treeNode.accessPath=="" || treeNode.accessPath=="#"){
		return false;
	}
    // 跳到该节点下对应的路径, 把当前资源ID(resourceID)传到后台，写进Session
	addTab(treeNode.menuName , treeNode.menuHerf + "?_jr="+encrypt("{\"menuId\":\""+treeNode.id+"\",\"_tms\":"+new Date().getTime()+"}"));
};

function addTab(title, url){
	if ($('#main-tabs').tabs('exists', title)){
		$('#main-tabs').tabs('select', title);
	} else {
		var content = '<iframe scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:99%;"></iframe>';
		$('#main-tabs').tabs('add',{
			title:title,
			content:content,
			closable:true
		});
	}
}
function loadMenu(treeObj){
	$.ajax({
		type:"POST",
		url:"/menu/menuPowerList.html",
		dataType : "json",
		success:function(data){
			// 如果返回数据不为空，加载"业务模块"目录
			if(data.code == "SUCCESS"){
				// 将返回的数据赋给zTree
				$.fn.zTree.init($("#"+treeObj), setting, data.data);
				zTree = $.fn.zTree.getZTreeObj(treeObj);
				if( zTree ){
					// 默认展开所有节点
					zTree.expandAll(false);
				}
			}
			else {
				msgAlert(data.message,'error');
			}
		}
	});
}
//刷新当前选中的tab内的表格
function reloadTabsTable() {
	$($('#main-tabs').tabs('getSelected')[0].childNodes[0]).context.contentWindow.reloadTable();
}

function ajax(url , data , successFunction , errorFunction) {
	$.ajax({  
        type:'post',  
        url:url,  
        dataType:'json',  
        data:data,  
        beforeSend:function(){  
        	showGackground(); 
        },success:function(json){  
        	try {
        		if(successFunction != undefined)
        			successFunction(json);
			} catch (e) {
				top.msgAlert("数据请求错误","error");
			}
        },
        error:function(){
        	try {
        		if(errorFunction != undefined)
        			errorFunction();
        		else 
        			top.msgAlert("数据请求错误","error");
			} catch (e) {
				top.msgAlert("数据请求错误","error");
			}
        },
        complete:function(){
        	hideGackground();
        }
	}); 
}

function ajax_submit(url , formId) {
	top.ajax(url , $("#"+formId).serializeObject() , public_success);
}

function public_success(data) {
	top.msgAlert(data.message,data.code=="SUCCESS"?"info":"error");
	if(data.code == "SUCCESS") {
		reloadTabsTable();
		try{$('#model_index').dialog('close');}catch(e){}
	}
}

function closeIndex() {
	$('#model_index').dialog('close');
}

function msgAlert(msg , type) {
	 $.messager.alert("系统提示",msg , type);
}
function msgConfirm(msg ,functionName) {
	$.messager.confirm('操作提示', msg, function(r){
		if (r){
			functionName();
		}
	});
}
function showUpdateModel(title , url ,width , height ,data) {
	$('#model_index').dialog({
	    title: title,
	    width: width,
	    height: height,
	    closed: false,
	    cache: false,
	    href: url,
	    queryParams:data,
	    method:"POST",
	    modal: true,
	    buttons:[
	            {
	            	text:"提交",
	            	iconCls:"icon-cologne-check",
	            	handler:function() {
	            		submit();
	            	}
	            },
	            {
	            	text:'取消',
	            	iconCls:"icon-munich-collaboration",
	            	handler:function(){
	            		$('#model_index').dialog('close');
	            	}
	            }
	    ]
	});
	$('#model_index').window('center');
}

$.fn.serializeObject = function()
{
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};

function hideGackground() {
	$("#model_ajax").hide();
}
function showGackground() {
	$("#model_ajax").show();
}