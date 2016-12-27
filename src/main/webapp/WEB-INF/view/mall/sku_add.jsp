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
    <link rel="stylesheet" type="text/css" href="${res_url}uploadify/uploadify.css">
    <style type="text/css">
        .uploadify-button{
            background-color: white;
        }
        .uploadify:hover .uploadify-button{
            background-color: white;
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
            <div class="page-content">
                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->
                        <form class="form-horizontal" id="validation-form" method="post"  enctype="multipart/form-data">
                            <div class="form-group">
                                <input name="id" type="hidden" value="${sku.id}"/>
                                <input id="image" name="image" type="hidden" value="${sku.image}"/>
                                <label class="control-label col-xs-2 col-sm-2 no-padding-right"
                                       for="displayName">类目</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="input-icon input-icon-right" id="kka">
                                        <input type="hidden" id="category" name="category" value="${sku.category }"/>
                                        <input type="text" readonly="readonly" id="displayName" name="displayName"
                                               value="${sku.cateName }"
                                               class="col-xs-12 col-sm-12"
                                               onclick="showMenu(); return false;"/>
                                        </button>
                                    </div>
                                    <div id="menuContent" class="menuContent col-xs-11 col-sm-11"
                                         style="overflow-y:auto; overflow-x:auto;display:none;position:absolute; z-index: 99999;background-color: #FFFFFF;border: 1px solid #858585;height: 250px;">
                                        <ul id="treeDemo" class="ztree"></ul>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right" for="skuName">名称</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="skuName" id="skuName" value="${sku.skuName}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="specName">规格</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="specName" id="specName" value="${sku.specName}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="sku">编号</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="sku" id="sku" value="${sku.sku}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="file_upload">图片</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="text" name="file_upload" id="file_upload" value="${sku.sku}"
                                               class="col-xs-12 col-sm-12">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="file_upload">专属</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="checkbox" name="exclusive" id="exclusive" value="1" <c:if test="${sku.exclusive == '1'}">checked </c:if>/>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="control-label col-xs-2 col-sm-12 no-padding-right"
                                       for="attribute">赠品</label>

                                <div class="col-xs-10 col-sm-12">
                                    <div class="clearfix">
                                        <input type="checkbox" name="attribute" id="attribute" value="1" <c:if test="${sku.attribute == '1'}">checked </c:if>/>
                                    </div>
                                </div>
                            </div>

                            <div class="clearfix form-actions" align="center">
                                <div class="col-md-offset-3 col-md-9">
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
<script src="${res_url}uploadify/jquery.uploadify.min.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        initformSubmitEvent();
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
        $('#file_upload').uploadify({
            //校验数据
            'swf' : '${res_url}uploadify/uploadify.swf', //指定上传控件的主体文件，默认‘uploader.swf’
            'uploader' : '${context_path}/mall/sku/uploadImage', //指定服务器端上传处理文件，默认‘upload.php’
            'auto' : true, //手动上传
            'buttonImage' : '${res_url}uploadify/uploadify-upload.png', //浏览按钮背景图片
            'width' :110,
            'height' :30,
            'cancelImg': '${res_url}uploadify/uploadify-cancel.png',
            //'buttonText': '选 择应用',
            'multi' : false, //单文件上传
            'fileTypeExts' : '*.jpg', //允许上传的文件后缀
            'fileSizeLimit' : '50MB', //上传文件的大小限制，单位为B, KB, MB, 或 GB
            'successTimeout' : 30, //成功等待时间
            'onUploadSuccess' : function(file, data,response) {//每成功完成一次文件上传时触发一次
                data=eval("["+data+"]")[0];
                $("#image").val(data.name);
                alert('上传成功');
            },
            'onUploadError' : function(file, data, response) {//当上传返回错误时触发
                alert('上传失败');
            }
        });
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
        $("#displayName").val(treeNode.name);
        $("#category").val(treeNode.attribute.code);
    }

    function showMenu() {
        var cityObj = $("#displayName");
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
        if (!(event.target.id == "menuContent" || $(event.target).parents("#menuContent").length > 0)) {
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
                displayName: {
                    required: true
                },
                skuName: {
                    required: true
                },
                sku: {
                    required: true
                },
                specName: {
                    required: true
                }
            },
            messages: {
                displayName: {
                    required: "请选择类目"
                },
                skuName: {
                    required: "请输入名称",
                },
                specName: {
                    required: "请输入规格",
                },
                sku: {
                    required: "请输入编号",
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
                $.post("${context_path}/mall/sku/save", postData, function (data) {
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
                        layer.msg(data.msg, {
                            icon: 1,
                            time: 2000 //2秒关闭（如果不配置，默认是3秒）
                        }, function () {
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

