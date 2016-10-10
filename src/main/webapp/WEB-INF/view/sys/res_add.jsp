<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
<link rel="stylesheet" href="${res_url}js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
</head>

<body class="no-skin">
		<div class="main-container" id="main-container">
			<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>

			<div class="main-container-inner">
				<div class="main-content" style="margin-left: 0px;">
					<div class="page-content">
						<div class="row">
						<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<form class="form-horizontal" id="validation-form" method="post">
													<div class="form-group">
														<input name="id" type="hidden" value="${sysRes.id}"/>
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="pname">上级资源</label>
														<div class="col-xs-12 col-sm-9">
															<div class="input-icon input-icon-right" id="kka">
																<input type="hidden" id="pid" name="pid" value="${sysRes.pid }">
													            <input type="text" readonly="readonly" id="pname" name="pname" value="${pRes.name}" class="col-xs-12 col-sm-6">
													            <button id="menuBtn" type="button" class="btn btn-sm btn-default" onclick="showMenu(); return false;" style="float: left;">选择</button>
															</div>
															<div id="menuContent" class="menuContent" style="overflow-y:auto; overflow-x:auto;display:none;position:absolute; z-index: 99999;background-color: #FFFFFF;border: 1px solid #858585;height: 250px;width: 270px;">
																 <ul id="treeDemo" class="ztree" ></ul>
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="name">资源名称</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="text" name="name" id="name" value="${sysRes.name}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="url">资源路径</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="text" name="url" id="url" value="${sysRes.url}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="type">资源类型</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <select name="type" id="type" class="col-xs-12 col-sm-6">
														       	  	<option ${sysRes.type eq 1?'selected':''} value="1">菜单</option>
																	<option ${sysRes.type eq 2?'selected':'' } value="2">功能</option>
														    	 </select>
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="seq">排序号</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="text" name="seq" id="seq" value="${sysRes.seq}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="iconCls">图标</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="text" name="iconCls" id="iconCls" value="${sysRes.iconCls}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													
													<div class="clearfix form-actions" align="center">
														<div class="col-md-offset-3 col-md-9">
															<button id="submit-btn" class="btn btn-info" type="submit" data-last="Finish">
																<i class="ace-icon fa fa-check bigger-110"></i>
																提交
															</button>
															&nbsp; &nbsp; &nbsp;
															<button class="btn" type="reset">
												<i class="ace-icon fa fa-undo bigger-110"></i>
												重置
											</button>
														</div>
													</div>
												</form>
						</div><!-- /.col -->
					</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div><!-- /.main-content -->
			</div><!-- /.main-container-inner -->
		</div><!-- /.main-container -->
			<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
<script src="${res_url}js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
	<script type="text/javascript">
			$(document).ready(function(){
				initformSubmitEvent();
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			});
			
			var setting = {
			view: {
				dblClickExpand: false
			},
			data: {
				simpleData: {
				enable: true
			}
			},
			callback: {
				beforeClick: beforeClick,
				onClick: onClick
			}
			};
			
			//json数据源，也可以从后台读取json字符串，并转换成json对象，如下所示
			var strNodes = '${jsonTree}';
			var zNodes = eval("("+strNodes+")"); //将json字符串转换成json对象数组，strNode一定要加"（）"，不然转不成功
			function beforeClick(treeId, treeNode) {
				var check = (treeNode.id!=10000);
			if (!check) alert("这是标题，不能选！");
				return check;
			}
			function onClick(e, treeId, treeNode) {
				$("#pid").val(treeNode.id);
				var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
				nodes = zTree.getSelectedNodes(),
				v = "";
				nodes.sort(function compare(a,b){return a.id-b.id;});
			for (var i=0, l=nodes.length; i<l; i++) {
				v += nodes[i].name + ",";
			}
				if (v.length > 0 ) v = v.substring(0, v.length-1);
				var cityObj = $("#pname");
				cityObj.attr("value", v); 
			}
			
			function showMenu() {
				var cityObj = $("#pname");
				var cityOffset = $("#pname").offset();
				var sel=document.getElementById("kka");
				$("#menuContent").css({left:sel.offsetLeft + "px", top:sel.offsetTop + cityObj.outerHeight() + "px"}).slideDown("fast");
				$("body").bind("mousedown", onBodyDown);
			}
			function hideMenu() {
				$("#menuContent").fadeOut("fast");
				$("body").unbind("mousedown", onBodyDown);
			}
			function onBodyDown(event) {
				if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
					hideMenu();
				}
				if(event.target.id.length>=15&&event.target.id.length!=17){
					hideMenu();
				}
			}
			var $validation = true;
			function initformSubmitEvent(){
				$('#validation-form').validate({
					errorElement: 'div',
					errorClass: 'help-block',
					focusInvalid: false,
					rules: {
						name:{
							required: true
						},
						seq:{
							required: true,
							digits:true
						}
					},
					messages: {
						name:{
							required: "请输入资源"
						},
						seq:{
							required: "请输入排序号",
							digits:"排序号只能输入整数"
						}
					},
					highlight: function (e) {
						$(e).closest('.form-group').removeClass('has-info').addClass('has-error');
					},
			
					success: function (e) {
						$(e).closest('.form-group').removeClass('has-error');//.addClass('has-info');
						$(e).remove();
					},
			
					errorPlacement: function (error, element) {
						if(element.is(':checkbox') || element.is(':radio')) {
							var controls = element.closest('div[class*="col-"]');
							if(controls.find(':checkbox,:radio').length > 1) controls.append(error);
							else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
						}
						else if(element.is('.select2')) {
							error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
						}
						else if(element.is('.chosen-select')) {
							error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
						}
						else error.insertAfter(element.parent());
					},
			
					submitHandler: function (form) {
						var $form = $("#validation-form");
						var $btn = $("#submit-btn");
						if($btn.hasClass("disabled")) return;
						$btn.addClass("disabled");
						 var postData=$("#validation-form").serializeJson();
			        	 $.post("${context_path}/sys/res/saveRes" ,postData,function(data){
			        		if(data.code=='success'){
			        			layer.msg('操作成功', {
			        				icon: 1,
			        			    time: 2000 //2秒关闭（如果不配置，默认是3秒）
			        			},function(){
			        				parent.reloadGrid();
			        				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			        				parent.layer.close(index); //再执行关闭 
			        			});
			        		}
			        		$("#submit-btn").removeClass("disabled");
			        	},"json");
						return false;
					},
					invalidHandler: function (form) {
					}
				});
			}
		</script>
</body>

</html>

