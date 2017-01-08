package com.fruit.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.fruit.core.util.VerifyCodeUtils;
import com.jfinal.core.Controller;
/**
 * 生成验证码接口
 * @author luxiansheng
 *
 */
public class ImageController extends Controller{

	public void getCode() throws IOException{
		HttpServletResponse response=this.getResponse();
		response.setHeader("Pragma", "No-cache");  
		response.setHeader("Cache-Control", "no-cache");  
		response.setDateHeader("Expires", 0);  
		response.setContentType("image/jpeg");  
        //生成随机字串  
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4);  
        //存入会话session  
        this.setSessionAttr("imageCode",verifyCode.toLowerCase());
        //生成图片  
        int w = 200, h = 80; 
        VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);  
        this.renderNull();
	}
}
