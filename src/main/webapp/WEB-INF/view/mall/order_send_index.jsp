<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta charset="utf-8"/>
    <title>电子烟管理平台</title>
    <meta name="description" content="overview &amp; stats"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
</head>
<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
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

                                            <input type="text" id="search" name="search"
                                                   class="form-control search-query"
                                                   placeholder="请输入操作人"/>
																	<span class="input-group-btn">
																		<button type="button" id="btn_search"
                                                                                class="btn btn-purple btn-sm">
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
                            <jc:button className="btn btn-success" id="btn-send" textName="发货"/>
                        </div>
                    </div>
                    <!-- PAGE CONTENT BEGINS -->
                    <table id="grid-table"></table>

                    <div id="grid-pager"></div>
                    <!-- PAGE CONTENT ENDS -->
                </div><!-- /.col -->
            </div><!-- /.row -->
        </div>
    </div>
</div><!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>

<script type="text/javascript">
    $(document).ready(function () {
        var grid_selector = "#grid-table";
        var pager_selector = "#grid-pager";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
//resize on sidebar collapse/expand
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                setTimeout(function () {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }, 0);
            }
        });

        $("#grid-table").jqGrid({
            url: '${context_path}/mall/order/getListData?status=2',
            mtype: "GET",
            datatype: "json",
            colModel: [
                {label: '订单编号', name: 'orderID', key: true, width: 75},
                {label: '商家编号', name: 'customer', width: 150},
                {label: '商家名称', name: 'cusName', width: 150},
                {label: '总金额', name: 'amount', width: 150},
                {label: '总成本', name: 'cost', width: 150},
                {label: '复核人', name: 'reviewer', width: 150},
                {label: '数量', name: 'amount', width: 150},
                {label: '快递公司', name: 'express', width: 150},
                {label: '快递单号', name: 'courierNum', width: 150},
                {label: '状态', name: 'status', width: 75, formatter: fmatterStatus},
                {
                    label: '下单时间',
                    name: 'odtime',
                    width: 150,
                    formatter: "date",
                    formatoptions: {srcformat: "ISO8601Long", newformat: "Y-m-d"}
                }
            ],
            height: 280,
            rowNum: 10,
            multiselect: true,//checkbox多选
            altRows: true,//隔行变色
            recordtext: "{0} - {1} 共 {2} 条",
            pgtext: "第 {0} 页 共 {1} 页",
            pager: pager_selector,
            loadComplete: function () {
                var table = this;
                setTimeout(function () {
                    updatePagerIcons(table);
                }, 0);
            }
        });
        $(window).triggerHandler('resize.jqGrid');
        $("#btn_search").click(function () {
            //此处可以添加对查询数据的合法验证
            var search = $("#search").val();
            $("#grid-table").jqGrid('setGridParam', {
                datatype: 'json',
                postData: {'search': search}, //发送数据
                page: 1
            }).trigger("reloadGrid"); //重新载入
        });

        $("#btn-send").click(function () {//审核页面
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个订单", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个订单", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                parent.layer.open({
                    title: '查看明细',
                    type: 2,
                    area: ['600px', '500px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/mall/order/sendView?orderID=' + rid
                });
            }
        });
    });
    //replace icons with FontAwesome icons like above
    function updatePagerIcons(table) {
        var replacement =
        {
            'ui-icon-seek-first': 'ace-icon fa fa-angle-double-left bigger-140',
            'ui-icon-seek-prev': 'ace-icon fa fa-angle-left bigger-140',
            'ui-icon-seek-next': 'ace-icon fa fa-angle-right bigger-140',
            'ui-icon-seek-end': 'ace-icon fa fa-angle-double-right bigger-140'
        };
        $('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function () {
            var icon = $(this);
            var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
            if ($class in replacement) icon.attr('class', 'ui-icon ' + replacement[$class]);
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
        if (!rowKey) {
            return "-1";
        } else {
            var selectedIDs = grid.getGridParam("selarrrow");
            var result = "";
            if (selectedIDs.length == 1) {
                return selectedIDs[0];
            } else {
                return "-2";
            }
        }
    }
    function reloadGrid() {
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }
    //格式化状态显示
    function fmatterStatus(cellvalue, options, rowObject) {
        if (cellvalue == 0) {
            return '<span class="label label-sm label-warning">待付款</span>';
        } else if (cellvalue == 1) {
            return '<span class="label label-sm label-success">已付款</span>';
        } else if (cellvalue == 2) {
            return '<span class="label label-sm label-success">已审核</span>';
        } else if (cellvalue == 3) {
            return '<span class="label label-sm label-success">已发货</span>';
        } else {
            return '<span class="label label-sm label-success">关闭</span>';
        }
    }
</script>

</body>
</html>



