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
    <style type="text/css">
        .text-null {
            border-color: #ff0000 !important;
        }

        .text-not-null {
            border-color: #008000 !important;
        }
    </style>
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
                        <!-- PAGE CONTENT BEGINS -->
                        <div class="widget-box">
                            <div class="widget-body">
                                <div class="widget-main">
                                    <div class="row">
                                        <form class="form-inline" role="form" id="saveOutbound">
                                            <input type="hidden" id="outboundID" name="outboundID"
                                                   value="${outboundID}"/>

                                            <div class="form-group col-xs-2">
                                                <input type="text" class="form-control" name="sku" placeholder="商品编号"
                                                       onblur="selectSku(this)">
                                            </div>
                                            <div class="form-group col-xs-6">
                                                <input type="text" class="form-control" id="info" placeholder="商品信息"
                                                       readonly>
                                            </div>
                                            <div class="form-group col-xs-2">
                                                <input type="text" class="form-control" name="quantity"
                                                       placeholder="出库数量">
                                            </div>
                                            <div class="form-group col-xs-2">
                                                <input type="text" class="form-control" name="allcost"
                                                       placeholder="出库成本">
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
        $('#saveOutbound').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                sku: {
                    required: true
                },
                quantity: {
                    required: true
                },
                cost: {
                    required: true
                }
            },
            messages: {
                sku: {
                    required: ""
                },
                quantity: {
                    required: ""
                },
                cost: {
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
                var postData = $("#saveOutbound").serializeJson();
                $.post("${context_path}/stock/outbound/save", postData, function (data) {
                    if (data.code == '200') {
                        $('#outboundID').val(data.outboundID);
                        layer.msg('操作成功', {
                            icon: 1,
                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                        }, function () {
                            reloadGrid();
                        });
                    } else {
                        layer.msg("添加失败");
                    }
                    $("#submit-btn").removeClass("disabled");
                }, "json");
                return false;
            },
            invalidHandler: function (form) {
            }
        });

        var outboundID = $('#outboundID').val();
        var grid_selector = "#detail-table";
        var pager_selector = "#grid-pager";
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
            url: '${context_path}/stock/outbound/getDetailData?outboundID=' + outboundID,
            mtype: "GET",
            datatype: "json",
            colModel: [
                {label: '商品', name: 'skuName', width: 150, sortable: false},
                {label: '编号', name: 'sku', width: 80, sortable: false},
                {label: '规格', name: 'specName', width: 150, sortable: false},
                {label: '出库数量', name: 'quantity', width: 80, sortable: false},
                {label: '累加成本', name: 'cost', width: 80, sortable: false}
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
            $.post("${context_path}/stock/outbound/deleteDetail", submitData, function (data) {

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

    function selectSku(obj) {
        $.ajax({
            url: '${context_path}/mall/sku/get?sku=' + obj.value,
            type: 'GET',
            success: function (response) {
                var code = response.code;
                if (code == "200") {
                    var entity = response.sku;
                    var skuName = entity.skuName;
                    var specName = entity.specName;
                    $('#info').val(skuName + ' ' + specName);
                } else {
                    layer.msg(response.msg);
                }
            }
        });
    }
    function reloadGrid() {
        parent.reloadGrid();
        $("#detail-table").jqGrid('setGridParam', {url: '${context_path}/stock/outbound/getDetailData?outboundID=' + $('#outboundID').val()}).trigger("reloadGrid");
        $("#detail-table").trigger("reloadGrid"); //重新载入
    }
</script>
</body>

</html>

