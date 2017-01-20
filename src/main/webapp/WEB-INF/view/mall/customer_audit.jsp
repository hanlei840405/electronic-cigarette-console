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
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <form class="form-horizontal" id="validation-form" method="post">
                            <input type="hidden" name="id" id="id" value="${customer.id}"/>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="cusCode">商户编号</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <input type="text" name="cusCode" id="cusCode" value="${customer.cusCode}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="cusName">商户名称</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <input type="text" name="cusName" id="cusName" value="${customer.cusName}"
                                               readonly="readonly" class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="upCode">上级商户</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <input type="text" name="upCode" id="upCode" value="${customer.upCode}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="saler">平台联络人</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <input type="text" name="saler" id="saler" value="${customer.saler}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="rate">销售提成</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <input type="text" name="rate" id="rate" value="${customer.rate}"
                                               class="col-xs-12 col-sm-12" placeholder="大于零整数">%
                                    </div>
                                </div>
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="priceType">价格类型</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <select name="priceType" id="priceType" class="col-xs-12 col-sm-12">
                                            <option value="">---请选择---</option>
                                            <option value="A" ${customer.priceType eq 'A'?'selected':''}>A</option>
                                            <option value="B" ${customer.priceType eq 'B'?'selected':''}>B</option>
                                            <option value="C" ${customer.priceType eq 'C'?'selected':''}>C</option>
                                            <option value="D" ${customer.priceType eq 'D'?'selected':''}>D</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="agency">供应商</label>

                                <div class="col-xs-3 col-sm-3">
                                    <div class="clearfix">
                                        <select name="agency" id="agency" class="col-xs-12 col-sm-12">
                                            <option value="0" ${customer.agency eq '0'?'selected':''}>否</option>
                                            <option value="1" ${customer.agency eq '1'?'selected':''}>是</option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                            <div class="clearfix form-actions" align="center">
                                <div class="col-md-offset-3 col-md-6">
                                    <button id="submit-btn" class="btn btn-info" type="submit" data-last="Finish">
                                        <i class="ace-icon fa fa-check bigger-110"></i>
                                        提交
                                    </button>
                                    &nbsp; &nbsp; &nbsp;
                                    <button class="btn" type="reset">
                                        <i class="ace-icon fa fa-undo bigger-110"></i>
                                        重置
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div><!-- /.col -->
                </div><!-- /.row -->
                <div class="row">
                    <div class="col-xs-12">
                        <table style="width: 100%">
                            <thead>
                            <tr>
                                <th>店铺名称</th>
                                <th>是否线下店铺</th>
                                <th>店铺地址</th>
                                <th>店铺图片</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${customerShops}" varStatus="i" var="item">
                                <tr>
                                    <td>${item.shopName}</td>
                                    <td><c:if test="${item.category == '0' || item.category == null}">否</c:if><c:if
                                            test="${item.category == '1'}">是</c:if></td>
                                    <td>${item.shopAddr}</td>
                                    <td><img width="80" height="80" src="/mall/ext/private/${item.shopPic}"></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div><!-- /.page-content -->
        </div><!-- /.main-content -->
    </div><!-- /.main-container-inner -->
</div><!-- /.main-container -->
<!-- basic scripts -->
<jsp:include page="/WEB-INF/view/common/basejs.jsp" flush="true"/>
<script src="${res_url}js/ztree/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        initformSubmitEvent();
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    });

    var setting = {
        view: {
            dblClickExpand: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeClick: beforeClick,
            onClick: onClick
        }
    };

    //json数据源，也可以从后台读取json字符串，并转换成json对象，如下所示
    var strNodes = '${jsonTree}';
    var zNodes = eval("(" + strNodes + ")"); //将json字符串转换成json对象数组，strNode一定要加"（）"，不然转不成功
    function beforeClick(treeId, treeNode) {
        var check = (treeNode.id != 10000);
        if (!check) alert("这是标题，不能选！");
        return check;
    }
    function onClick(e, treeId, treeNode) {
        $("#pid").val(treeNode.id);
        var zTree = $.fn.zTree.getZTreeObj("treeDemo"),
                nodes = zTree.getSelectedNodes(),
                v = "";
        nodes.sort(function compare(a, b) {
            return a.id - b.id;
        });
        for (var i = 0, l = nodes.length; i < l; i++) {
            v += nodes[i].name + ",";
        }
        if (v.length > 0) v = v.substring(0, v.length - 1);
        var cityObj = $("#pname");
        cityObj.attr("value", v);
    }

    function showMenu() {
        var cityObj = $("#pname");
        var cityOffset = $("#pname").offset();
        var sel = document.getElementById("kka");
        $("#menuContent").css({
            left: sel.offsetLeft + "px",
            top: sel.offsetTop + cityObj.outerHeight() + "px"
        }).slideDown("fast");
        $("body").bind("mousedown", onBodyDown);
    }
    function hideMenu() {
        $("#menuContent").fadeOut("fast");
        $("body").unbind("mousedown", onBodyDown);
    }
    function onBodyDown(event) {
        if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
            hideMenu();
        }
        if (event.target.id.length >= 15 && event.target.id.length != 17) {
            hideMenu();
        }
    }
    var $validation = true;
    function initformSubmitEvent() {
        $('#validation-form').validate({
            errorElement: 'div',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                cusCode: {
                    required: true
                },
                saler: {
                    required: true
                },
                rate: {
                    required: true
                },
                priceType: {
                    required: true
                }
            },
            messages: {
                cusCode: {
                    required: "请输入"
                },
                saler: {
                    required: "请输入"
                },
                rate: {
                    required: "请输入"
                },
                priceType: {
                    required: "请输入"
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
                var postData = $("#validation-form").serializeJson();
                $.post("${context_path}/mall/customer/saveAudit", postData, function (data) {
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
                    }
                    $("#submit-btn").removeClass("disabled");
                }, "json");
                return false;
            },
            invalidHandler: function (form) {
            }
        });
    }
</script>
</body>

</html>

