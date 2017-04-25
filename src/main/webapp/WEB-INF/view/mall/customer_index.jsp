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

                                            <input type="text" id="searchCustomer" name="searchCustomer"
                                                   class="form-control search-query"
                                                   placeholder="请输入商户号"/>
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
                            <jc:button className="btn btn-success" id="btn-audit" textName="审核"/>
                            <jc:button className="btn btn-primary" id="btn-upRated" textName="设置商家固定提成"/>
                            <jc:button className="btn btn-danger" id="btn-setting" textName="价格设置"/>
                            <jc:button className="btn btn-info" id="btn-exclusive" textName="专属商品"/>
                            <jc:button className="btn btn-default" id="btn-disable" textName="禁用"/>
                            <jc:button className="btn btn-danger" id="btn-reset" textName="密码重置"/>
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
            url: '${context_path}/mall/customer/getListData',
            mtype: "GET",
            datatype: "json",
            colModel: [
                {index: 'id', name: 'id', key: true, hidden: true},
                {label: '商户号', name: 'cusCode', key: true, width: 50},
                {label: '商户名称', name: 'cusName', width: 100},
                {label: '供应商', name: 'agency', width: 50, formatter: fmatterAgency},
                {label: '性别', name: 'sex', width: 50, formatter: fmatterSex},
                {label: '生日', name: 'birthday', width: 80},
                {label: '微信', name: 'wechat', width: 100},
                {label: '电话', name: 'phone', width: 80},
                {label: '邮箱', name: 'email', width: 150},
                {label: '累计金额', name: 'amount', width: 75},
                {label: '销售提成(%)', name: 'rate', width: 50},
                {label: '上级经销商', name: 'upCode', width: 100},
                {label: '平台销售', name: 'saler', width: 100},
                {label: '状态', name: 'status', width: 75, formatter: fmatterStatus}
            ],
            viewrecords: true,
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
            var searchCustomer = $("#searchCustomer").val();
            $("#grid-table").jqGrid('setGridParam', {
                datatype: 'json',
                postData: {'customer': searchCustomer}, //发送数据
                page: 1
            }).trigger("reloadGrid"); //重新载入
        });
        $("#btn-audit").click(function () {//添加页面
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                parent.layer.open({
                    title: '商户审核',
                    type: 2,
                    area: ['800px', '600px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/mall/customer/audit?id=' + rid
                });
            }
        });
        $("#btn-upRated").click(function () {//添加页面
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                var rowData = $("#grid-table").jqGrid("getRowData", rid);
                if (rowData.upCode == null || rowData.upCode == '') {
                    layer.msg("商户没有上级商户", {
                        icon: 2,
                        time: 2000 //2秒关闭（如果不配置，默认是3秒）
                    });
                } else {
                    parent.layer.open({
                        title: '设置上级商家商品固定提成',
                        type: 2,
                        area: ['800px', '600px'],
                        fix: false, //不固定
                        maxmin: true,
                        content: '${context_path}/mall/customer/upRated?id=' + rid
                    });
                }
            }
        });
        $("#btn-setting").click(function () {//添加页面
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                parent.layer.open({
                    title: '设置商品特殊价格',
                    type: 2,
                    area: ['800px', '600px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/mall/customer/setting?id=' + rid
                });
            }
        });
        $("#btn-exclusive").click(function () {//选择专属商品
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                parent.layer.open({
                    title: '设置专属商品',
                    type: 2,
                    area: ['800px', '600px'],
                    fix: false, //不固定
                    maxmin: true,
                    content: '${context_path}/mall/customer/exclusive?id=' + rid
                });
            }
        });
        $("#btn-reset").click(function () {//重置密码
            var rid = getOneSelectedRows();
            if (rid == -1) {
                layer.msg("请选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else if (rid == -2) {
                layer.msg("只能选择一个商户", {
                    icon: 2,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                });
            } else {
                var submitData = {
                    "id": getOneSelectedRows()
                };
                layer.confirm("确定修改其密码？", function (index) {
                    layer.close(index);
                    $.post("${context_path}/mall/customer/reset", submitData, function (data) {
                        if (data.code == 0) {
                            layer.msg("重置成功", {
                                icon: 1,
                                time: 1000 //1秒关闭（如果不配置，默认是3秒）
                            }, function () {
                                //$("#grid-table").trigger("reloadGrid"); //重新载入
                                reloadGrid();
                            });

                        } else {
                            layer.alert(data.msg);
                        }
                    }, "json");
                });
            }
        });
        $("#btn-disable").click(function () {
            var submitData = {
                "id": getOneSelectedRows()
            };
            $.post("${context_path}/mall/customer/disable", submitData, function (data) {

                if (data.code == 0) {
                    layer.msg("操作成功", {
                        icon: 1,
                        time: 1000 //1秒关闭（如果不配置，默认是3秒）
                    }, function () {
                        //$("#grid-table").trigger("reloadGrid"); //重新载入
                        reloadGrid();
                    });

                } else {
                    layer.alert(data.msg);
                }
            }, "json");
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
    function upOrDown(status) {
        var submitData = {
            "ids": getSelectedRows(),
            "status": status
        };
        $.post("${context_path}/mall/sku/upOrDown", submitData, function (data) {

            if (data.code == 0) {
                layer.msg("操作成功", {
                    icon: 1,
                    time: 1000 //1秒关闭（如果不配置，默认是3秒）
                }, function () {
                    //$("#grid-table").trigger("reloadGrid"); //重新载入
                    reloadGrid();
                });

            } else {
                layer.alert(data.msg);
            }
        }, "json");
    }
    //格式化状态显示
    function fmatterAgency(cellvalue, options, rowObject) {
        if (cellvalue == '0') {
            return '否';
        } else {
            return '是';
        }
    }
    //格式化状态显示
    function fmatterSex(cellvalue, options, rowObject) {
        if (cellvalue == 'M') {
            return '男';
        } else if (cellvalue == 'F') {
            return '女';
        } else {
            return '未知';
        }
    }
    //格式化状态显示
    function fmatterStatus(cellvalue, options, rowObject) {
        if (cellvalue == 0) {
            return '<span class="label label-sm label-warning">待审核</span>';
        } else if (cellvalue == 1) {
            return '<span class="label label-sm label-success">审核通过</span>';
        } else if (cellvalue == 2) {
            return '<span class="label label-sm label-danger">禁用</span>';
        } else {
            return '<span class="label label-sm label-success">审核不通过</span>';
        }
    }
    function reloadGrid() {
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }
</script>

</body>
</html>



