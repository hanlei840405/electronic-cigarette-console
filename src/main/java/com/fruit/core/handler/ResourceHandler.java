package com.fruit.core.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.PropKit;

/**
 * 资源地址初始化
 */
public class ResourceHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		// 统一设置basePath
		ServletContext context = request.getServletContext();
		if (context.getAttribute("res_url") == null) {
			context.setAttribute("static_url", PropKit.get("static_url"));
			String context_path=context.getContextPath().equals("/")?"":context.getContextPath();
			context.setAttribute("res_url", context_path+"/res/");
			context.setAttribute("context_path", context_path);
		}
		next.handle(target, request, response, isHandled);
	}

}
