<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

		<!--[if !IE]> -->
		<script type="text/javascript">
			window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.js'>"+"<"+"/script>");
		</script>

		<!-- <![endif]-->

		<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery1x.js'>"+"<"+"/script>");
</script>
<![endif]-->
		<script type="text/javascript">
			if('ontouchstart' in document.documentElement) document.write("<script src='${res_url}ace-1.3.3/assets/js/jquery.mobile.custom.js'>"+"<"+"/script>");

			(function($){  
				$.fn.serializeJson=function(){
					var serializeObj={};
					var array=this.serializeArray();
					var str=this.serialize();
					$(array).each(function(){
						if(serializeObj[this.name]){
							if($.isArray(serializeObj[this.name])){
								serializeObj[this.name].push(this.value);
							}else{
								serializeObj[this.name]=[serializeObj[this.name],this.value];  
							}
						}else{
							serializeObj[this.name]=this.value;
						}
					});
					return serializeObj;
				};
			})(jQuery);

		</script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery-ui.custom.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.ui.touch-punch.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/chosen.jquery.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/fuelux/fuelux.spinner.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-timepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/moment.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/daterangepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datetimepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap-colorpicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.knob.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.autosize.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.inputlimiter.1.3.1.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jquery.maskedinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/bootstrap-tag.js"></script>

	
		<script src="${res_url}ace-1.3.3/assets/js/date-time/bootstrap-datepicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/jquery.jqGrid.src.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/jqGrid/i18n/grid.locale-en.js"></script>
		
		<!-- ace scripts -->
		
		<!-- ace scripts -->
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.scroller.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.colorpicker.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.fileinput.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.typeahead.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wysiwyg.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.spinner.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.treeview.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.wizard.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/elements.aside.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.ajax-content.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.touch-drag.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.sidebar-scroll-1.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.submenu-hover.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-box.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-rtl.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.settings-skin.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.widget-on-reload.js"></script>
		<script src="${res_url}ace-1.3.3/assets/js/ace/ace.searchbox-autocomplete.js"></script>
		
		<script src="${res_url}ace-1.3.3/assets/js/jquery.validate.js"></script>
		<script src="${res_url}js/layer/layer.js"></script>
		<script src="${res_url}js/jquery.mytime.js"></script>
		<script type="text/javascript">
		String.prototype.trim = function() {
				return this.replace(/^\s\s*/, '').replace(/\s\s*$/, '');
			}
		</script>