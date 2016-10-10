<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properities -->
  <title>Login Example - Semantic</title>
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/bootstrap.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/font-awesome.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/select2.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-fonts.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-skins.css" />
  <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-rtl.css" />
  <!--[if lte IE 9]>
   <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-part2.css" class="ace-main-stylesheet" />
  <![endif]-->

  <!--[if lte IE 9]>
   <link rel="stylesheet" href="${res_url}ace-1.3.3/assets/css/ace-ie.css" />
  <![endif]-->
  <link rel="stylesheet" href="${res_url}js/omui/development-bundle/themes/default/om-fileupload.css" />
  <script src="${res_url}ace-1.3.3/assets/js/jquery.js" ></script>
  <script src="${res_url}ace-1.3.3/assets/js/ace-extra.js"></script>
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
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="oldPwd">旧密码</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="password" name="oldPwd" id="oldPwd" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="space-2"></div> 
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="newPwd">新密码</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="password" name="newPwd" id="newPwd" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="space-2"></div> 
													<div class="form-group">
														<label class="control-label col-xs-12 col-sm-3 no-padding-right" for="pwdRepeat">重复密码</label>
														<div class="col-xs-12 col-sm-9">
															<div class="clearfix">
													            <input type="password" name="pwdRepeat" id="pwdRepeat" class="col-xs-12 col-sm-6">
															</div>
														</div>
													</div>
													<div class="space-2"></div> 
													
													<div class="clearfix form-actions" align="center">
														<div class="col-md-offset-3 col-md-9">
															<button id="submit-btn" class="btn btn-info btn-sm" type="submit" data-last="Finish">
																<i class="ace-icon fa fa-check bigger-110"></i>
																修改
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
		
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.validate.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/additional-methods.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/bootbox.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.maskedinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/select2.js"></script>
		<!-- ace scripts -->
		<script src="${res_url}ace-1.3.3/assets/js/ace-elements.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace.js"></script>

		<!-- inline scripts related to this page -->
		<script src="${res_url}js/layer/layer.js"></script>
		<script src="${res_url}datepicker/WdatePicker.js"></script>
    
	<script type="text/javascript">
		
			jQuery(function($) {
				
				var $validation = true;
				
				$('#validation-form').validate({
					errorElement: 'div',
					errorClass: 'help-block',
					focusInvalid: false,
					//oldPwd newPwd pwdRepeat
					rules: {
						oldPwd:{
							required: true,
							minlength:6 
						},
						newPwd:{
							required: true,
							minlength:6 
						},
						pwdRepeat:{
							required: true,
							minlength:6 
						}
					},
					messages: {
						oldPwd:{
							required: "请输入旧密码",
							minlength:"旧密码长度最少为6位" 
						},
						newPwd:{
							required: "请输入新密码",
							minlength:"新密码长度最少为6位"
						},
						pwdRepeat:{
							required: "请输入重复密码",
							minlength:"重复密码长度最少为6位"
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
						////oldPwd newPwd pwdRepeat
						var submitData = {
								oldPwd:$("#oldPwd").val(),
								newPwd: $("#newPwd").val(),
								pwdRepeat:$("#pwdRepeat").val()
						};
						$btn.addClass("disabled");
						$.post('/savePwdUpdate', submitData,function(data) {
							$btn.removeClass("disabled");
							if(data.code==0){
	        					layer.msg('修改成功', {
	        					    icon: 1,
	        					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
	        					},function(){
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

