<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>JC管理系统</title>

		<meta name="description" content="overview &amp; stats" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		<jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
		<link rel="stylesheet" href="${res_url}css/conTabs.css?v=1.11" />
	</head> 
 
	<body class="no-skin full-height-layout" style="overflow:hidden">
	<div id="wrapper">
	<div id="navbar" class="navbar navbar-default navbar-fixed-top">
			<script type="text/javascript">
				try{ace.settings.check('navbar' , 'fixed')}catch(e){}
			</script>

			<div class="navbar-container" id="navbar-container">
				<!-- #section:basics/sidebar.mobile.toggle -->
				<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler" data-target="#sidebar">
					<span class="sr-only">Toggle sidebar</span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>

					<span class="icon-bar"></span>
				</button>

				<jsp:include page="/WEB-INF/view/common/top.jsp" flush="true" />

				<!-- /section:basics/navbar.dropdown -->
			</div><!-- /.navbar-container -->
		</div>
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
		<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			<div id="sidebar" class="sidebar sidebar-fixed responsive">
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
				</script>

				<div class="sidebar-shortcuts" id="sidebar-shortcuts">
					<div class="sidebar-shortcuts-large" id="sidebar-shortcuts-large">
						<button class="btn btn-success">
							<i class="ace-icon fa fa-signal"></i>
						</button>

						<button class="btn btn-info">
							<i class="ace-icon fa fa-pencil"></i>
						</button>

						<!-- #section:basics/sidebar.layout.shortcuts -->
						<button class="btn btn-warning">
							<i class="ace-icon fa fa-users"></i>
						</button>

						<button class="btn btn-danger">
							<i class="ace-icon fa fa-cogs"></i>
						</button>

						<!-- /section:basics/sidebar.layout.shortcuts -->
					</div>

					<div class="sidebar-shortcuts-mini" id="sidebar-shortcuts-mini">
						<span class="btn btn-success"></span>

						<span class="btn btn-info"></span>

						<span class="btn btn-warning"></span>

						<span class="btn btn-danger"></span>
					</div>
				</div><!-- /.sidebar-shortcuts -->

				<!-- 菜单 -->
				<jc:menu />
				<!-- /.nav-list -->

				<!-- #section:basics/sidebar.layout.minimize -->
				<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
					<i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
				</div>

				<!-- /section:basics/sidebar.layout.minimize -->
				<script type="text/javascript">
					try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
				</script>
			</div>
			<div class="main-content" id="page-wrapper">
				<div class="breadcrumbs breadcrumbs-fixed" id="breadcrumbs">
						<script type="text/javascript">
							try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
						</script>

						<!-- 面包屑 -->
					<%--jc:breadcrumb / --%>
					<div class="row content-tabs">
                <button class="roll-nav roll-left J_tabLeft"><i class="fa fa-backward"></i>
                </button>
                <nav class="page-tabs J_menuTabs">
                    <div class="page-tabs-content" style="margin-left: 0px;">
                        <a href="javascript:;" class="J_menuTab active" data-id="/home">控制台</a>
                    </div>
                </nav>
                <button class="roll-nav roll-right J_tabRight"><i class="fa fa-forward"></i>
                </button>
            </div>
					</div>
				<div class="page-content" id="page-content">
					<div class="row J_mainContent" id="content-main">
	                <iframe class="J_iframe" name="iframe0" width="100%" height="100%" src="${context_path}/home" frameborder="0" data-id="/home" seamless></iframe>
	            	</div>				
				</div>
			</div>
		</div><!-- /.main-container -->
</div>
		<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
<script src="${res_url}js/contabs.min.js"></script>
		<script type="text/javascript"> 
		var currentIframe;
		$(function(){
			menuEventInit();
			tabsEventInit();
			initPwdSettingEvent();
			var url = location.href; 
			if(url.indexOf("#")>-1){
				var postUrl = url.substring(url.indexOf("#")+1,url.length); 
				if(postUrl=="/index"||postUrl=="/"){
					postUrl="/";
				};
				var menu=$('[url="'+postUrl+'"]'); 
				menu.click();
			    $(".nav-list li").removeClass("active");
				menu.parent("li").addClass("active");
				$(".nav-list li").removeClass("open");
				menu.parent("li").parents("li").addClass("open");
			}
		});
		function reloadGridTable(){
			$("#grid-table").trigger("reloadGrid");
		}
		function tabsEventInit(){
			$(".page-tabs-content a").bind("click",function(){
				if($(this).hasClass("active")==false){
					$(".page-tabs-content a").removeClass("active");
					$(this).addClass("active");
					var menu=$('[url="'+($(this).attr("data-id")=='/home'?'/':$(this).attr("data-id"))+'"]'); 
					menu.click();
				    $(".nav-list li").removeClass("active");
					menu.parent("li").addClass("active");
					$(".nav-list li").removeClass("open");
					menu.parent("li").parents("li").addClass("open");
					menu.parent("ul").css("display","block");
				}
			});
			$(".page-tabs-content .fa-times-circle").bind("click",function(){
				if($(this).parent("a").hasClass("active")){
					var nextnode=$(this).parent("a").next();
					$(".page-tabs-content a").removeClass("active");
					if(nextnode.length>0){
						nextnode.addClass("active");
					}else{
						nextnode=$(this).parent("a").prev();
					}
					nextnode.addClass("active");
					var menu=$('[url="'+(nextnode.attr("data-id")=='/home'?'/':nextnode.attr("data-id"))+'"]'); 
					menu.click();
				    $(".nav-list li").removeClass("active");
					menu.parent("li").addClass("active");
					$(".nav-list li").removeClass("open");
					menu.parent("li").parents("li").addClass("open");
					menu.parent("ul").css("display","block");
				}
				var iframe=$("#content-main").find("[data-id='"+$(this).parent("a").attr("data-id")+"']");
				iframe.remove();
				$(this).parent("a").remove();
			});
		}
		function tabsEventClear(){
			$(".page-tabs-content a").unbind("click");
			$(".page-tabs-content .fa-times-circle").unbind("click");
		}
		function menuEventInit(){
			$(".nav-list a").click(function(e){
				e.preventDefault();
				var url=$(this).attr("url");
				if(url){
					$("iframe").css("display","none");
					window.location.hash=url;
					if(url=="/index"||url=="/"){
						url="/home";
					};
					var iframe=$("#content-main").find("[data-id='"+url+"']");
					$(".nav-list li").removeClass("active");
					$(this).parent("li").addClass("active");
					if(iframe.length>0){
						iframe.css("display","inline");
						currentIframe=iframe[0];
					}else{
						var index=$(this).attr("data-index");
						var ihtml='<iframe class="J_iframe" name="iframe'+index+'" width="100%" height="100%" src="${context_path}'+url+'" frameborder="0" data-id="'+url+'" seamless></iframe>';
						$("#content-main").append(ihtml);
						currentIframe=$("#content-main").find("[data-id='"+url+"']")[0];
					}
					var tab=$(".page-tabs-content").find("[data-id='"+url+"']");
					$(".page-tabs-content a").removeClass("active");
					if(tab.length > 0){
						tab.addClass("active");
					}else{
						$(".page-tabs-content").append('<a href="javascript:;" class="J_menuTab active" data-id="'+url+'">'+$(this).text()+'<i class="fa fa-times-circle"></i></a>');
						tabsEventClear();
						tabsEventInit();
					}
				}
			});
		}
		function reloadGrid(){
			currentIframe.contentWindow.reloadGrid();
		}
		function initPwdSettingEvent(){
			$("#pwd-update").click(function(){//添加页面
				layer.open({
					title:'修改密码',
				    type: 2,
				    area: ['770px', '500px'],
				    fix: false, //不固定
				    maxmin: true,
				    content: '${context_path}/pwdSetting'
				});
			});
		}
   		</script>
   		
	</body>
</html>
