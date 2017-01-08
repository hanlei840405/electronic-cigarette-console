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
    <link rel="stylesheet" href="${res_url}css/select2/select2.min.css" type="text/css">
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
    <div class="main-container-inner">
        <div class="main-content" style="margin-left: 0px;">
            <div class="page-content" id="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-box">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <div class="row">
                                        <form class="form-inline" role="form" id="saveCustomerUpRated">
                                            <input type="hidden" name="customer" id="customer" value="${customer}"/>
                                            <div class="form-group col-xs-4">
                                                <select class="category_sku" style="width: 200px;" name="sku">
                                                    <c:forEach items="${categories }" var="item">
                                                        <optgroup label="${item.cateName }"></optgroup>
                                                        <c:forEach items="${item.skus }" var="sku">
                                                            <option value="${sku.sku }">${sku.skuName }</option>
                                                        </c:forEach>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <div class="form-group col-xs-1">
                                            </div>
                                            <div class="form-group col-xs-2">
                                                <input type="text" class="form-control" readonly value="${upName}">
                                            </div>
                                            <div class="form-group col-xs-2">
                                                <input type="number" class="form-control" name="rated" min="0"
                                                       placeholder="上家固定提成金额">
                                            </div>
                                            <div class="form-group col-xs-2 pull-right">
                                                <button id="submit-btn" type="submit" class="btn btn-primary"
                                                        data-last="Finish">提交
                                                </button>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="row-fluid" style="margin-bottom: 5px;">
                            <div class="span12 control-group">
                                <jc:button className="btn btn-danger" id="btn-delete" textName="删除"/>
                            </div>
                        </div>
                        <!-- PAGE CONTENT BEGINS -->
                        <table id="detail-table"></table>

                        <div id="detail-grid-pager"></div>
                        <!-- PAGE CONTENT ENDS -->
                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div>
        </div><!-- /.main-content -->
    </div><!-- /.main-container-inner -->
</div>
</div><!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>

<script src="${res_url}js/select2/select2.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $(".category_sku").select2();
        $('#saveCustomerUpRated').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                sku: {
                    required: true
                },
                rated: {
                    required: true
                }
            },
            messages: {
                sku: {
                    required: ""
                },
                rated: {
                    required: ""
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
                if (element.is(':checkbox') || element.is(':radio')) {
                    var controls = element.closest('div[class*="col-"]');
                    if (controls.find(':checkbox,:radio').length > 1) controls.append(error);
                    else error.insertAfter(element.nextAll('.lbl:eq(0)').eq(0));
                }
                else if (element.is('.select2')) {
                    error.insertAfter(element.siblings('[class*="select2-container"]:eq(0)'));
                }
                else if (element.is('.chosen-select')) {
                    error.insertAfter(element.siblings('[class*="chosen-container"]:eq(0)'));
                }
                else error.insertAfter(element.parent());
            },

            submitHandler: function (form) {
                var $btn = $("#submit-btn");
                if ($btn.hasClass("disabled")) return;
                $btn.addClass("disabled");
                var postData = $("#saveCustomerUpRated").serializeJson();
                $.post("${context_path}/mall/customer/saveUpRated", postData, function (data) {
                    if (data.code == 0) {
                        layer.msg('操作成功', {
                            icon: 1,
                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                        }, function () {
                            reloadGrid();
                        });
                    } else {
                        layer.msg(data.msg);
                    }
                    $("#submit-btn").removeClass("disabled");
                }, "json");
                return false;
            },
            invalidHandler: function (form) {
            }
        });
        debugger;
        var customer = $('#customer').val();
        var grid_selector = "#detail-table";
        var pager_selector = "#detail-grid-pager";
        //resize to fit page size
        $(window).on('resize.jqGrid', function () {
            $(grid_selector).jqGrid('setGridWidth', $(".page-content").width());
        });
        var parent_column = $(grid_selector).closest('[class*="col-"]');
        $(document).on('settings.ace.jqGrid', function (ev, event_name, collapsed) {
            if (event_name === 'sidebar_collapsed' || event_name === 'main_container_fixed') {
                //setTimeout is for webkit only to give time for DOM changes and then redraw!!!
                setTimeout(function () {
                    $(grid_selector).jqGrid('setGridWidth', parent_column.width());
                }, 0);
            }
        });

        $("#detail-table").jqGrid({
            url: '${context_path}/mall/customer/getCustomerUpRatedListData?customer=' + customer,
            mtype: "GET",
            datatype: "json",
            colModel: [
                {index: 'id', name: 'id', key: true, hidden: true},
                {label: '上级商家', name: 'up', key: true, width: 100},
                {label: '商品编号', name: 'sku', width: 100},
                {label: '商品名称', name: 'skuName', width: 150},
                {label: '上家固定提成金额', name: 'rated', width: 100}
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

        $("#btn-delete").click(function () {
            var submitData = {
                "ids": getSelectedRows()
            };
            $.post("${context_path}/mall/customer/deleteCustomerUpRated", submitData, function (data) {

                if (data.code == 0) {
                    layer.msg("操作成功", {
                        icon: 1,
                        time: 1000 //1秒关闭（如果不配置，默认是3秒）
                    }, function () {
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
        var grid = $("#detail-table");
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

    function reloadGrid() {
        $("#detail-table").trigger("reloadGrid"); //重新载入
    }
</script>

</body>
</html>


