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
    <link rel="stylesheet" href="${res_url}css/select2/select2.min.css" type="text/css">
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
                        <table width="100%">
                            <tr>
                                <td>订单编号</td>
                                <td>${orderID}</td>
                                <td>客户</td>
                                <td>${cusName}</td>
                                <td>总金额</td>
                                <td>${amount}</td>
                            </tr>
                        </table>
                        <c:if test="${status == 0 }">
                            <table width="100%">
                                <tr>
                                    <td>赠品</td>
                                    <td><select id="giftSku" style="width: 200px;">
                                        <option value="">请选择</option>
                                        <c:forEach items="${categories }" var="item">
                                            <optgroup label="${item.cateName }"></optgroup>
                                            <c:forEach items="${item.skus }" var="sku">
                                                <option value="${sku.sku }">${sku.skuName }</option>
                                            </c:forEach>
                                        </c:forEach>
                                    </select></td>
                                    <td>数量</td>
                                    <td><input id="giftQuantity" type="number" min="1" value="1"/></td>
                                    <td><button onclick="sendGift()">提交</button></td>
                                </tr>
                            </table>
                        </c:if>
                        <c:if test="${status == 1 }">
                            <table width="100%">
                                <tr>
                                    <td>赠品</td>
                                    <td>${gift}</td>
                                    <td>数量</td>
                                    <td>${quantity}</td>
                                </tr>
                            </table>
                        </c:if>
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
<script src="${res_url}js/select2/select2.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $("#giftSku").select2();
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
            url: '${context_path}/mall/order/orderDetail?orderID=${orderID}',
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

    function sendGift(){
        var gift=$("#giftSku").val();
        var quantity = $("#giftQuantity").val();
        if (gift == "" || gift == null) {
            alert("选择赠品");
            return;
        }
        if (isNaN(quantity) || quantity < 1) {
            alert("数量必须大于0");
            return;
        }
        $.post("${context_path}/mall/customerGift/send", {"id": "${id}","gift": gift,"quantity": quantity}, function (data) {

            if (data.code == 0) {
                layer.msg("操作成功", {
                    icon: 1,
                    time: 1000 //1秒关闭（如果不配置，默认是3秒）
                }, function () {
                    parent.reloadGrid();
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                });

            } else {
                layer.alert(data.msg);
            }
        }, "json");
    }
</script>
</body>

</html>

