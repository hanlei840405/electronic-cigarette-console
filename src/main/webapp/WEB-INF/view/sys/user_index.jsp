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
		<jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true" />
	</head>
	<body class="no-skin">
		<!-- /section:basics/navbar.layout -->
		<div class="main-container" id="main-container">
		<script type="text/javascript">
				try{ace.settings.check('main-container' , 'fixed')}catch(e){}
			</script>
			<div class="main-content" id="page-wrapper">
				<div class="page-content" id="page-content">
					<div class="row">
							<div class="col-xs-12">
							<!-- PAGE CONTENT BEGINS -->
							<div class="widget-box">
											<div class="widget-header widget-header-small">
												<h5 class="widget-title lighter">筛选</h5>
											</div>

											<div class="widget-body">
												<div class="widget-main">
														<div class="row">
															<div class="col-xs-12 col-sm-8">
																<div class="input-group">
																	<span class="input-group-addon">
																		<i class="ace-icon fa fa-check"></i>
																	</span>

																	<input type="text" id="name" name="name" class="form-control search-query" placeholder="请输入关键字" />
																	<span class="input-group-btn">
																		<button type="button" id="btn_search" class="btn btn-purple btn-sm">
																			<span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
																			搜索
																		</button>
																	</span>
																</div>
															</div>
														</div>
												</div>
											</div>
										</div>
							</div>
							<div class="col-xs-12">
								<div class="row-fluid" style="margin-bottom: 5px;">
									<div class="span12 control-group">
										<jc:button className="btn btn-success" id="btn-visible" textName="启用"/>
										<jc:button className="btn btn-danger" id="btn-unvisible" textName="禁用"/>
										<jc:button className="btn btn-primary" id="btn-add" textName="添加"/>
										<jc:button className="btn btn-info" id="btn-edit" textName="编辑"/>
										<jc:button className="btn" id="bnt-grant" textName="分配角色" permission="/sys/user"/>
									</div>
								</div>
								<!-- PAGE CONTENT BEGINS -->
								<table id="grid-table"></table>

								<div id="grid-pager"></div>

								<script type="text/javascript">
									var $path_base = "..";//in Ace demo this will be used for editurl parameter
								</script>

								<!-- PAGE CONTENT ENDS -->
							</div><!-- /.col -->
						</div><!-- /.row -->
				</div>
			</div>
		</div><!-- /.main-container -->
		<!-- basic scripts -->
		<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true" />
		
			<script type="text/javascript"> 
        $(document).ready(function () {
        	var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
        	//resize to fit page size
			$(window).on('resize.jqGrid', function () {
				$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
		    });
//resize on sidebar collapse/expand
				var parent_column = $(grid_selector).closest('[class*="col-"]');
				$(document).on('settings.ace.jqGrid' , function(ev, event_name, collapsed) {
					if( event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed' ) {
						//setTimeout is for webkit only to give time for DOM changes and then redraw!!!
						setTimeout(function() {
							$(grid_selector).jqGrid( 'setGridWidth', parent_column.width() );
						}, 0);
					}
			    });

            $("#grid-table").jqGrid({
                //url: 'http://trirand.com/blog/phpjqgrid/examples/jsonp/getjsonp.php?callback=?&qwery=longorders',
                url:'${context_path}/sys/user/getListData',
                mtype: "GET",
                datatype: "json",
                colModel: [
                    { label: '用户名', name: 'name', width: 75 },
                    { label: '联系电话', name: 'phone', width: 75 ,sortable:false},
                    { label: '拥有角色', name: 'roleNames', width: 150 },
                    { label: '描述', name: 'des', width: 150 ,sortable:false},
                    { label: '注册时间', name: 'createdate', width: 150 },
                    { label: '状态', name: 'status',formatter:fmatterStatus, width:50}
                ],
				viewrecords: true,
                height: 280,
                rowNum: 10,
                multiselect: true,//checkbox多选
                altRows: true,//隔行变色
                recordtext:"{0} - {1} 共 {2} 条",
                pgtext:"第 {0} 页 共 {1} 页",
                pager: pager_selector,
                loadComplete : function() {
					var table = this;
					setTimeout(function(){
						updatePagerIcons(table);
					}, 0);
				}
            });
			$(window).triggerHandler('resize.jqGrid');

			$(window).bind('resize', function() {
			//$("#jqgrid").setGridWidth($(window).width()*0.75);
			//	$("#grid-table").setGridHeight($(window).height()-200);
			});
			$("#btn_search").click(function(){  
			    //此处可以添加对查询数据的合法验证  
			    var name = $("#name").val();  
			    $("#grid-table").jqGrid('setGridParam',{  
			        datatype:'json',  
			        postData:{'name':name}, //发送数据  
			        page:1  
			    }).trigger("reloadGrid"); //重新载入  
			}); 
			$("#btn-add").click(function(){//添加页面
				parent.layer.open({
					title:'添加新用户',
				    type: 2,
				    area: ['770px', '430px'],
				    fix: false, //不固定
				    maxmin: true,
				    content: '${context_path}/sys/user/add'
				});
			});
			$("#bnt-grant").click(function(){
				var rid = getOneSelectedRows();
				if(rid == -1){
					layer.msg("请选择一个用户", {
					    icon: 2,
					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					});
				}else if(rid == -2 ){
					layer.msg("只能选择一个用户", {
					    icon: 2,
					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					});
				}else {
					var rowData = $("#grid-table").jqGrid('getRowData',rid);
					parent.layer.open({
						title:'给用户【'+rowData.name+'】分配角色',
					    type: 2,
					    area: ['380px', '530px'],
					    fix: false, //不固定
					    maxmin: true,
					    content: '${context_path}/sys/user/userRoleSetting?uid='+rid
					});
				}
			});
			$("#btn-visible").click(function(){
				setVisible(1);
			});
			$("#btn-unvisible").click(function(){
				setVisible(0);
			});
			
			$("#btn-edit").click(function(){//添加页面
				var rid = getOneSelectedRows();
				if(rid == -1){
					layer.msg("请选择一个用户", {
					    icon: 2,
					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					});
				}else if(rid == -2 ){
					layer.msg("只能选择一个用户", {
					    icon: 2,
					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
					});
				}else {
					parent.layer.open({
						title:'修改用户信息',
					    type: 2,
					    area: ['770px', '430px'],
					    fix: false, //不固定
					    maxmin: true,
					    content: '${context_path}/sys/user/add?id='+rid
					});
				}
			});
        });
      //replace icons with FontAwesome icons like above
		function updatePagerIcons(table) {
			var replacement = 
			{
				'ui-icon-seek-first' : 'ace-icon fa fa-angle-double-left bigger-140',
				'ui-icon-seek-prev' : 'ace-icon fa fa-angle-left bigger-140',
				'ui-icon-seek-next' : 'ace-icon fa fa-angle-right bigger-140',
				'ui-icon-seek-end' : 'ace-icon fa fa-angle-double-right bigger-140'
			};
			$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
				var icon = $(this);
				var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
				if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
			})
		}
      	/**获取选中的列***/
		function getSelectedRows() {
            var grid = $("#grid-table");
            var rowKey = grid.getGridParam("selrow");
            if (!rowKey)
                return "-1";
            else {
                var selectedIDs = grid.getGridParam("selarrrow");
                var result = "";
                for (var i = 0; i < selectedIDs.length; i++) {
                    result += selectedIDs[i] + ",";
                }
                return result;
            }                
        }
      	function getOneSelectedRows() {
      		var grid = $("#grid-table");
            var rowKey = grid.getGridParam("selrow");
            if (!rowKey){
                return "-1";
            }else {
                var selectedIDs = grid.getGridParam("selarrrow");
                if(selectedIDs.length==1){
                	return selectedIDs[0];
                }else{
                	return "-2";
                }
            } 
      	}
		function setVisible(status){
			var submitData = {
					"ids" : getSelectedRows(),
					"visible":status
			};
			$.post("${context_path}/sys/user/setVisible", submitData,function(data) {
				if (data.code == 0) {
					layer.msg("操作成功", {
					    icon: 1,
					    time: 1000 //1秒关闭（如果不配置，默认是3秒）
					},function(){
						reloadGrid();
					});
				}  else{
					layer.alert("操作失败");
				} 
			},"json");
		}
		//格式化状态显示
		function fmatterStatus(cellvalue, options, rowObject){
			if(cellvalue==0){ 
				return '<span class="label label-sm label-warning">禁用</span>';
			}else{
				return '<span class="label label-sm label-success">启用</span>';
			}
		}
		function reloadGrid(){
			$("#grid-table").trigger("reloadGrid"); //重新载入
		}
   </script>
   		
	</body>
</html>
