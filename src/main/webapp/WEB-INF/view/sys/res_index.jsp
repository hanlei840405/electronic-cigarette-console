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
							</div>
							<div class="col-xs-12">
								<div class="row-fluid" style="margin-bottom: 5px;">
									<div class="span12 control-group">
										<button class="btn btn-success" onclick="setVisible(1)">启用</button>
										<button class="btn btn-danger" onclick="setVisible(0)">禁用</button>
										<jc:button className="btn btn-primary" id="bnt-add" textName="添加"/>
										<jc:button className="btn btn-info" id="bnt-edit" textName="编辑"/>
										<jc:button className="btn" id="bnt-grant" textName="授权" permission="/sys/role/list"/>
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
		var selectRowid=-1;
        $(document).ready(function () {
        	var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
        	//resize to fit page size
			$(window).on('resize.jqGrid', function () {
				$(grid_selector).jqGrid( 'setGridWidth', $(".page-content").width() );
				$(grid_selector).jqGrid('setGridHeight', $(window).height() - 350);
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
            	url:"${context_path}/sys/res/getTreeGridView",
				datatype:"json",
				colModel:[
					{
						name:"id",
						index:"id",
						sorttype:"int",
						key:true,
						hidden:true
					},{
						name:"name",
						index:"name",
						sorttype:"string",
						label:"名称",
						width:170,sortable:false
					},{
						name:"url",
						index:"url",
						sorttype:"string",
						label:"资源地址",
						width:90,sortable:false
					}, { label: '类型', name: 'type',formatter:fmatterType, width:50,sortable:false},
					{ label: '排序号', name: 'seq', width:50,sortable:false},
					{
						name:"pid",
						hidden:true
					},{
						name:"seq",
						hidden:true
					},{
						label:'是否启用',
						name:"enabled",
						formatter:fmatterEnabled, width:50,sortable:false
					}
				],
				width:"1400",
				hoverrows:true,
				viewrecords:false,
				gridview:true,
				height:"370px",
				sortname:"seq",
				scrollrows:true,
				multiselect: true,//checkbox多选
				treeGrid:true,
				ExpandColumn:"name",
				treedatatype:"json",
				treeGridModel:"adjacency",
				loadonce:true,
				rowNum:100,
				treeReader:{
					parent_id_field:"pid",
					level_field:"level",
					leaf_field:"isLeaf",
					expanded_field:"expanded",
					loaded:"loaded",
					icon_field:"icon"
				},
				pager:pager_selector,
				onSelectRow : function( rowid ) {
					if(rowid) 
					{
						selectRowid=rowid;
						/**if(rdata.isLeaf === 'true') {
							$("#price").html(rdata.price);
							$("#uiicon").empty().append("<span class='ui-icon "+rdata.uiicon+"'></span>");
						}**/
					} 
				},
            });
            $("#grid-table").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
			$(window).triggerHandler('resize.jqGrid');
			
			$("#bnt-add").click(function(){
				parent.layer.open({
				    type: 2,
				   // shade: [1],
				    fix: false,
				    title: '添加资源',
				    maxmin: true,
				   	content: '${context_path}/sys/res/add',
				    area: ['770px', '480px']
				}); 
			});
			
			$("#bnt-edit").click(function(){
				var rdata=getSelectedRows();
				var id=rdata.id;
				if (typeof(id) == "undefined") { 
					layer.msg("请选择要编辑的资源");
					return;
				}  
				parent.layer.open({
				    type: 2,
				   // shade: [1],
				    fix: false,
				    title: '编辑资源',
				    maxmin: true,
				   	content: '${context_path}/sys/res/add?id='+id,
				    area: ['770px', '430px']
				}); 
			});
			
        });
      
      	/**获取选中的列***/
		function getSelectedRows() {
			var rdata = $('#grid-table').jqGrid('getRowData', selectRowid);
			return rdata;      
        }
      	
      	/**是否启用资源操作**/
		function setVisible(status){
			var rdata=getSelectedRows();
			var id=rdata.id;
			if (typeof(id) == "undefined") { 
				layer.msg("请选择要操作的资源");
				return;
			} 
			var submitData = {
					"resId" : id,
					"status":status
			};
			
			$.post("${context_path}/sys/res/setEnabled", submitData,function(data) {
				if (data.code ==0) {
					layer.msg(data.result, {
        				icon: 1,
        			    time: 2000 //2秒关闭（如果不配置，默认是3秒）
        			},function(){
						 $("#grid-table").trigger("reloadGrid"); //重新载入
					});
				}else{
					layer.msg(data.msg);
				}
			},"json");
		}
		//格式化状态显示
		function fmatterType(cellvalue, options, rowObject){
			if(cellvalue==1){
				return '<span class="label label-sm label-info">菜单</span>';
			}else{
				return '<span class="label label-sm label-success">功能</span>';
			}
		}
		//格式化状态显示
		function fmatterEnabled(cellvalue, options, rowObject){
			if(cellvalue==1){
				return '<span class="label label-sm label-success">启用</span>';
			}else{
				return '<span class="label label-sm label-warning">禁用</span>';
			}
		}
		function reloadGrid(){
			$("#grid-table").trigger("reloadGrid"); //重新载入
		}
   </script>
   		
	</body>
</html>





		
