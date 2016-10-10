<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
  <!-- Standard Meta -->
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">

  <!-- Site Properities -->
  <title>Login Example - Semantic</title>
  <link rel="stylesheet" type="text/css" href="${res_url}js/semantic/semantic.min.css">
  <script src="${res_url}js/semantic/assets/library/jquery.min.js"></script>
  <script src="${res_url}js/semantic/semantic.min.js"></script>
<script src="${res_url}js/layer/layer.js"></script>
  <style type="text/css">
    body {
      background-color: #DADADA;
    }
    body > .grid {
      height: 100%;
    }
    .image {
      margin-top: -100px;
    }
    .column {
      max-width: 450px;
    }
  </style>
  <script>
  $(document).ready(function(e) {
	  $('.ui.dropdown').dropdown();
	  $('.ui.form').form({
        fields: {
          name: {
            identifier  : 'name',
            rules: [
              {
                type   : 'empty',
                prompt : '请输入角色名'
              }
            ]
          },
          password: {
              identifier  : 'password',
              rules: [
                {
                  type   : 'empty',
                  prompt : '请输入密码'
                }
              ]
            },
            /* roleIds: {
                identifier  : 'roleIds',
                rules: [
                  {
                    type   : 'empty',
                    prompt : '请添加用户角色'
                  }
                ]
              }, */
              email: {
                identifier  : 'email',
                rules: [
                  {
                    type   : 'email',
                    prompt : '邮箱格式不正确'
                  }
                ]
              }
        },
         inline : true,
         on     : 'blur',
         onSuccess:function(){
        	 $("#btn_submit").addClass("disabled");
        	 $("#roleIds").val($("#role_ids").val());
        	 var postData=$(this).serializeJson();
        	 
        	 $.post("${context_path}/sys/user/save" ,postData, 
        				function(data){
        				if(data.code==0){
        					window.parent.reloadGrid(); //重新载入
        					layer.msg('保存成功', {
        					    icon: 1,
        					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
        					},function(){
        						var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        						parent.layer.close(index); //再执行关闭 
        						
        					});
        				}else {
        					layer.msg(data.msg, {
        					    icon: 2,
        					    time: 2000 //2秒关闭（如果不配置，默认是3秒）
        					});
        				}
        		 		$("#btn_submit").removeClass("disabled");
        		},"json");
        	 e.preventDefault();
        	 return true;
         }
      });
  })
  </script>
</head>
<body>

<form class="ui form segment">
		<input name="id" type="hidden" value="${id}"/>
        <div class="field">
          <label>用户名</label>
          <input type="text" name="name" ${id ne null?'readonly':'' } value="${item.name}">
        </div>
        <c:if test="${id eq null }">
	      <div class="field">
		    <label>密码</label>
		   	<input type="password" name="password" value="${item.password }">
		  </div>
		  <div class="field">
		    <label>用户角色</label>
		    	<input name="roleIds" id="roleIds" type="hidden">
		        <select class="ui fluid search dropdown" id="role_ids" multiple="">
		  			<option value="">请选择用户角色</option>
		  			<c:forEach items="${roleList }" var="item">
		  				<option value="${item.id }" >${item.name }</option>
		  			</c:forEach> 
		  			
		  		</select>
		  </div>
		  
		  </c:if>
        <div class="field">
          <label>描述</label>
          <input type="text" name="des" value="${item.des}">
        </div>
        <div class="field">
          <label>联系电话</label>
          <input type="text" name="phone" value="${item.phone}">
        </div>
        <div class="field">
          <label>联系邮箱</label>
          <input type="text" name="email" value="${item.email}">
        </div>
        
        <div class="ui blue submit button" id="btn_submit">提交</div>
      </form>

</body>

</html>

