<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
<%@ include file="/WEB-INF/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <!-- Standard Meta -->
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <jsp:include page="/WEB-INF/view/common/basecss.jsp" flush="true"/>
    <link rel="stylesheet" href="${res_url}js/ztree/css/zTreeStyle/zTreeStyle.css" type="text/css">
</head>

<body class="no-skin">
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try {
            ace.settings.check('main-container', 'fixed')
        } catch (e) {
        }
    </script>
    <div class="main-container-inner">
        <div class="main-content" style="margin-left: 0px;">
            <div class="page-content" id="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <p>订单编号:${order.orderID}</p>

                        <p class="col-xs-4">客户:${order.cusName}</p>

                        <p class="col-xs-4">快递信息:${order.express} - ${order.courierNum}</p>

                        <p class="col-xs-4">总金额:${order.amount}</p>

                        <p class="col-xs-4">
                            <c:if test="${order.status == 0 }">
                                <span class="label label-sm label-info">待付款</span>
                            </c:if>
                            <c:if test="${order.status == 1 }">
                                <span class="label label-sm label-primary">已付款</span>
                            </c:if>
                            <c:if test="${order.status == 2 }">
                                <span class="label label-sm label-success">已审核</span>
                            </c:if>
                            <c:if test="${order.status == 3 }">
                                <span class="label label-sm label-danger">已发货</span>
                            </c:if>
                            <c:if test="${order.status == 4 }">
                                <span class="label label-sm label-danger">已关闭</span>
                            </c:if>
                        </p>

                        <c:if test="${order.status != 0 && order.status != 3 }">
                            <img width="100px" src="/mall/ext/private/${order.certificate}"/>
                        </c:if>
                        <p>${order.addr}</p>
                        <!-- PAGE CONTENT BEGINS -->
                        <table id="grid-table"></table>

                        <div id="grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div>
        </div><!-- /.main-content -->
    </div><!-- /.main-container-inner -->
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
            url: '${context_path}/mall/order/orderDetail?orderID=${order.orderID}',
            mtype: "GET",
            datatype: "json",
            colModel: [
                {label: '商品', name: 'skuName', width: 150, sortable: false},
                {label: '编号', name: 'sku', width: 80, sortable: false},
                {label: '规格', name: 'specName', width: 150, sortable: false},
                {label: '购买数量', name: 'quantity', width: 80, sortable: false},
                {label: '价格', name: 'price', width: 80, sortable: false},
                {label: '成本', name: 'allcost', width: 80, sortable: false}
            ],
            height: 280,
            rowNum: 10,
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
</script>
</body>

</html>

