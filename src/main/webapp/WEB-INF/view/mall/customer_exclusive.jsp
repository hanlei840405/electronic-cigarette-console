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
    <style type="text/css">
        .button-select {
            border-color: #ff0000 !important;
        }

        .button-not-select {
            border-color: #008000 !important;
        }
    </style>
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
        <input type="hidden" id="customer" value="${customer}"/>

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
                                <table width="100%">
                                    <tr>
                                        <td>
                                            <select name="searchCategory" id="searchCategory"
                                                    class="ui fluid search dropdown form-control">
                                                <option value="">---选择类目---</option>
                                                <c:forEach items="${categories }" var="item">
                                                    <option value="${item.cateCode }">${item.cateName }</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td><input type="text" id="searchSku" name="searchSku"
                                                   class="form-control search-query"
                                                   placeholder="请输入关键字"/>
                                        </td>
                                        <td>
                                            <span class="input-group-btn">
																		<button type="button" id="btn_search"
                                                                                class="btn btn-purple btn-sm">
                                                                            <span class="ace-icon fa fa-search icon-on-right bigger-110"></span>
                                                                            搜索
                                                                        </button>
																	</span>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-xs-12">
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
        var customer = $('#customer').val();
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
            url: '${context_path}/mall/sku/getListDataForEx?customer=' + customer,
            mtype: "GET",
            datatype: "json",
            colModel: [
                {index: 'id', name: 'id', key: true, hidden: true, width: 75},
                {label: '编码', name: 'sku', width: 75},
                {label: '名称', name: 'skuName', key: true, width: 150},
                {label: '类目', name: 'categoryName', width: 75},
                {label: '专属', name: 'customer', width: 50, formatter: operation, edittype: 'button'}
            ],
            viewrecords: true,
            height: 600,
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
            var searchSku = $("#searchSku").val();
            var searchCategory = $("#searchCategory").val();
            $("#grid-table").jqGrid('setGridParam', {
                datatype: 'json',
                postData: {'category': searchCategory, 'sku': searchSku}, //发送数据
                page: 1
            }).trigger("reloadGrid"); //重新载入
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
    function operation(cellvalue, options, rowObject) {
        var cssName;
        var text;
        if (cellvalue == '' || cellvalue == null) {
            text = '设定';
            cssName = 'btn btn-primary';
        } else {
            text = '取消';
            cssName = 'btn btn-warning';
        }
        return '<input type="button" class="' + cssName + '" value="' + text + ' " onclick="saveExclusive(\'' + rowObject.sku + '\')"/>';
    }
    function saveExclusive(sku) {
        var customer = $('#customer').val();
        $.post("${context_path}/mall/customer/saveCustomerExclusive", {
            customer: customer,
            sku: sku
        }, function (data) {
            if (data.code == 0) {
                reloadGrid();
            }
        }, "json");
    }
    function reloadGrid() {
        $("#grid-table").trigger("reloadGrid"); //重新载入
    }
</script>

</body>
</html>



