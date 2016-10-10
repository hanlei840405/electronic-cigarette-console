<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>JC后台管理系统</title>

		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

		<!-- bootstrap & fontawesome -->
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/bootstrap.css" />
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/font-awesome.css" />

		<!-- page specific plugin styles -->
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/jquery-ui.css" />
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/datepicker.css" />
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ui.jqgrid.css" />

		<!-- text fonts -->
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-fonts.css" />

		<!-- ace styles -->
		<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace.css" class="ace-main-stylesheet" id="main-ace-style" />
		
		<!-- ztree styles -->
		<link rel="stylesheet" href="${res_url}js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
		
		<!--[if lte IE 9]>
			<link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-part2.css" class="ace-main-stylesheet" />
		<![endif]-->

		<!--[if lte IE 9]>
		  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-ie.css" />
		<![endif]-->

		<!-- inline styles related to this page -->

		<!-- ace settings handler -->
		<script src="${res_url}ace-1.3.3/assets/js/ace-extra.js"></script>

		<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

		<!--[if lte IE 8]>
		<script src="${res_url}ace-1.3.3/assets/js/html5shiv.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/respond.js"></script>
		<![endif]-->
<style>
body{
	background-color: #ffffff;
}
.left{
	position: absolute;
	top: 10px;
	left: 20px;
	width: 358px;
	border-right: 1px solid #CCC;
	overflow-y: auto;
}

.tree-menu{
	float: right;
}
.tree-menu span{
	margin-left: 6px;
}
.tree-menu span i{
	cursor: pointer;
}
.icon-plus {
	background-position: -408px -96px;
}
.icon-remove {
	background-position: -312px 0;
}
.icon-edit {
	background-position: -96px -72px;
}
[class^="icon-"], [class*=" icon-"] {
	display: inline-block;
	width: 14px;
	height: 14px;
	line-height: 14px;
	vertical-align: text-top;
	background-image: url("${basePath}res/bootstrap/img/glyphicons-halflings.png");
	background-repeat: no-repeat;
	margin-top: 1px;
}
#menu_tree{
	margin-right: 20px;
}

li{
	line-height: 16px;
}
.om-tree-node a{
	display: inline-block;
	*display: inline;
	*zoom: 1;
	width: 115px;
	overflow: hidden;
	text-overflow: ellipsis;
}
#vip_tip{
	text-align: center;
}
.actions{
	position: absolute;
	bottom: 20px;
	left: 10px;
	width: 368px;
	border-right: 1px solid #CCC;
	height: 60px;
}

.ztree li span.button.add {margin-left:15px; margin-right: -1px; background-position:-144px 0; vertical-align:top; *vertical-align:middle}
</style>
</head>

<body>
	<div class="left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
	<div class="actions" style="margin-left: 300px;margin-top: 30px;">
		<jc:button className="btn btn-big btn-primary" id="btn_saveOrder"  textName="保存"/> 
	</div>
	<!-- basic scripts -->

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");
		</script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap.js"></script>
	
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/i18n/grid.locale-en.js"></script>
		
		<!-- ace scripts -->
		
		<!-- ace scripts -->
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.scroller.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.fileinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.typeahead.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.spinner.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.treeview.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wizard.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.aside.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-box.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		<script src="${res_url}js/layer/layer.js"></script>
		<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
		<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.excheck-3.5.min.js"></script>
		<script type="text/javascript" src="${res_url}js/ztree/js/jquery.ztree.exedit-3.5.min.js"></script>
<script type="text/javascript">
	$(document).ready(function () {
		
	});
</script>
<script>

$(function(){
	
	$("#btn_saveOrder").click(function(){
		//alert("sadf");
		$("#btn_saveOrder").attr("disabled","disabled");
		var nodes = zTree_Menu.getCheckedNodes(true);
		var selectIds="";
		for(var index in nodes){
			var item=nodes[index];
			if(item.id=='10000')continue;
			selectIds+=item.id+",";
		} 
		
		var submitData={
				"roleIds":selectIds,
				"id":"${item.id}",
		}
		$.post("${context_path}/sys/user/saveUserRoles" , submitData , 
			function(data){
			$("#btn_saveOrder").removeAttr("disabled");
			if(data.code==0){
				layer.msg('操作成功', {
				    icon: 1,
				    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				},function(){
					parent.reloadGrid();
					var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
					parent.layer.close(index); //再执行关闭 
				});
			}else{
				layer.msg(data.msg, {
				    icon: 2,
				    time: 2000 //2秒关闭（如果不配置，默认是3秒）
				},function(){});
			}
		},"json");
	});
	resize();
	$.fn.zTree.init($("#treeDemo"), setting, zNodes);
	zTree_Menu = $.fn.zTree.getZTreeObj("treeDemo");
	
	$(window).resize(function(){
		resize();
	});
	function resize(){
		h = $(window).height(),
		th = $("#top").outerHeight(true),
		mh = $(".main-title h3").outerHeight(true); 
		$(".left").height(h - th - mh- 55);
	}
	
	
});
var setting = {
		check: {
			enable: true
		},
		data: {
			simpleData: {
				enable: true
			}
		},
		callback: {
			beforeCheck: beforeCheck,
			onCheck: onCheck
		}
	};
function beforeCheck(treeId, treeNode) {
	return (treeNode.doCheck !== false);
}
function onCheck(e, treeId, treeNode) {

}	

	var log, className = "dark";
	var zNodes =${jsonTree.data};
	setting.check.chkboxType = { "Y" : "ps", "N" : "ps" };
</script>
</body>
</html>