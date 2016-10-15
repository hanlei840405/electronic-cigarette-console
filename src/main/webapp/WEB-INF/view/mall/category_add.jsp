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
                            <c:if test="${category != null}">
                                <input type="hidden" name="id" id="id" value="${category.id}"/>
                            </c:if>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="parentCode">上级类目</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <select name="parentCode" id="parentCode" class="col-xs-12 col-sm-12">
                                            <option value="">---请选择---</option>
                                            <c:forEach items="${categories}" var="item">
                                                <option value="${item.cateCode}" <c:if
                                                        test="${category.parentCode == item.cateCode}">selected</c:if>>${item.cateName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="cateCode">类目编号</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="cateCode" id="cateCode" value="${category.cateCode}"
                                               placeholder="系统自动生成" class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="cateName">类目名称</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="cateName" id="cateName" value="${category.cateName}"
                                               class="col-xs-12 col-sm-12">
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
                name: {
                    required: true
                }
            },
            messages: {
                name: {
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
                var $form = $("#validation-form");
                var $btn = $("#submit-btn");
                if ($btn.hasClass("disabled")) return;
                $btn.addClass("disabled");
                var postData = $("#validation-form").serializeJson();
                $.post("${context_path}/mall/category/save", postData, function (data) {
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

