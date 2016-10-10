package com.fruit.core.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class SessionIdHandler extends Handler {

	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		boolean isFromURL = request.isRequestedSessionIdFromURL();
		if (isFromURL) {
			target = target.substring(0, target.indexOf(';'));
		}
		next.handle(target, request, response, isHandled);
	}

}
