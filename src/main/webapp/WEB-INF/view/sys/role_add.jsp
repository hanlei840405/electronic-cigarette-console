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
							<div class="widget-box">
								
								<div class="widget-body">
									<div class="widget-main">
										<!-- #section:plugins/fuelux.wizard.container -->
										<div class="step-content pos-rel" id="step-container">
											<div class="step-pane active" id="step1">
												<form class="form-horizontal" id="validation-form" method="post">
													<input name="id" type="hidden" value="${id}"/>
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="name">名称</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													           <input type="text" id="name" name="name" value="${item.name}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="form-group"> 
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="des">描述</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													           <input type="text" id="des" name="des" value="${item.des}" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="clearfix form-actions" align="center">
														<div class="col-md-offset-3 col-md-9">
															<button id="submit-btn" class="btn btn-info btn-sm" type="submit" data-last="Finish">
																<i class="ace-icon fa fa-check bigger-110"></i>
																提交
															</button>
															&nbsp; &nbsp; &nbsp;
															<button class="btn btn-sm" type="button" onclick="closeView()">
																取消
															</button>
														</div>
													</div>
												</form>
											</div>
										</div>
									</div><!-- /.widget-main -->
								</div><!-- /.widget-body -->
							</div>
						</div><!-- /.col -->
					</div><!-- /.row -->
					</div><!-- /.page-content -->
				</div><!-- /.main-content -->
			</div><!-- /.main-container-inner -->
		</div><!-- /.main-container -->
		<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
	<script type="text/javascript">
		
			jQuery(function($) {
				
				var $validation = true;
				$('#validation-form').validate({
					errorElement: 'div',
					errorClass: 'help-block',
					focusInvalid: false,
					rules: {
						name:{
							required: true
						}
					},
					messages: {
						name:{
							required: "请输入角色名称"
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
						var postData = {
								id:"${id}",
								name:$("#name").val(),
								des: $("#des").val()
						};
						$btn.addClass("disabled");
						$.post('/sys/role/save', postData,function(data) {
							$btn.removeClass("disabled");
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
						return false;
					},
					invalidHandler: function (form) {
					}
				});
			
			});
			
			function closeView(){
				var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
				parent.layer.close(index); //再执行关闭 
			}
			
		</script>
</body>

</html>
