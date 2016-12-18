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
                        <p>返修单号:${asAftersaleod.asodID}</p>
                        <p class="col-xs-4">快递:${asAftersaleod.courierNum}</p>
                        <p class="col-xs-4">客户:${asAftersaleod.cusName}</p>
                        <p class="col-xs-4">接收人:${asAftersaleod.executer}</p>
                        <p class="col-xs-12">${asAftersaleod.addr}</p>
                        <div class="row-fluid" style="margin-bottom: 5px;">
                            <div class="span12 control-group">
                                <input type="text" id="bkcourierNum" name="bkcourierNum" placeholder="回寄快递信息"/>
                                <jc:button className="btn btn-success" id="btn-send" textName="发货"/>
                            </div>
                        </div>
                        <!-- PAGE CONTENT BEGINS -->
                        <table id="detail-table"></table>

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
        var grid_selector = "#detail-table";
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

        $(grid_selector).jqGrid({
            url: '${context_path}/mall/aftersale/detail?asodID=${asAftersaleod.asodID}',
            mtype: "GET",
            datatype: "json",
            colModel: [
                {label: '商品', name: 'skuName', width: 150, sortable: false},
                {label: '编号', name: 'sku', width: 80, sortable: false},
                {label: '规格', name: 'specName', width: 150, sortable: false},
                {label: '退回数量', name: 'quantity', width: 80, sortable: false},
                {label: '换新数量', name: 'newQty', width: 80, sortable: false,formatter: function (cellvalue, options, rowObject) {
                    return '<input class="newQtyInput" type="number" value="0" min="0" id="' + rowObject.id + '" max="' + rowObject.quantity + '" />';
                }}
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
        $("#btn-send").click(function () {//发货
            sendAfterSale();
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

    function sendAfterSale() {
        if ($("#btn-send").hasClass("disabled")) return;
        $("#btn-send").addClass("disabled");
        var array=[];
        var flag = true;
        $('.newQtyInput').each(function () {
            if ($(this).val() < 0) {
                flag = false;
            }
            array.push({id : $(this).attr('id'), newQty: $(this).val()});
        });
        if (!flag) {
            layer.msg('换新数量需大于等于0');
            $("#btn-send").removeClass("disabled");
            return false;
        }
        var values = {bkcourierNum: $('#bkcourierNum').val(), asodID: '${asAftersaleod.asodID}', skus: JSON.stringify(array)};
        $.post("${context_path}/mall/aftersale/saveSend", values, function (data) {
            debugger;
            if (data.code == 0) {
                layer.msg('操作成功', {
                    icon: 1,
                    time: 2000 //2秒关闭（如果不配置，默认是3秒）
                }, function () {
                    parent.reloadGrid();
                    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                    parent.layer.close(index); //再执行关闭
                });
            }else {
                layer.msg(data.msg);
            }
            $("#btn-send").removeClass("disabled");
        }, "json");
    }
</script>
</body>

</html>

