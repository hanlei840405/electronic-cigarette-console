/**
 * Copyright (c) 2011-2016, Eason Pan(pylxyhome@vip.qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fruit.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import com.fruit.core.model.Condition;

@SuppressWarnings("unchecked")
public class CommonUtils {

	public static String createUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	public static String getMACAddress(String ip) {
		String str = "";
		String macAddress = "";
		try {
			Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
			InputStreamReader ir = new InputStreamReader(p.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (int i = 1; i < 100; i++) {
				str = input.readLine();
				if (str != null) {
					if (str.indexOf("MAC Address") > 1) {
						macAddress = str.substring(
								str.indexOf("MAC Address") + 14, str.length());
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace(System.out);
		}
		return macAddress;
	}

	/**
	 * 得到文件的后缀名<br/>
	 * System.out.println(getFileSubString("yulong.jpg"));>>>.jpg
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileSubString(String fileName) {
		if (fileName != null && fileName.indexOf(".") != -1) {
			return fileName.substring(fileName.indexOf("."));
		}
		return "";
	}

	/**
	 * 判断字符串是否包含内容
	 * 
	 */
	public static boolean isNotEmpty(String str) {
		return str != null && !str.trim().equals("");
	}

	/**
	 * 判断数字是否大于零
	 * 
	 */
	public static boolean isGtZero(Long ls) {
		return ls != null && ls > 0;
	}

	/**
	 * 判断数字是否大于零
	 * 
	 */
	public static boolean isGtZero(Integer ls) {
		return ls != null && ls > 0;
	}

	/**
	 * 随机count位数字字符串<br/>
	 * 可以用来生成代金券号码和密码
	 * 
	 * @param count
	 *            随机多少位
	 */
	public static String getRandomString(int count) {
		if (count > 0) {
			StringBuilder buf = new StringBuilder();
			Random rd = new Random();
			for (int i = 0; i < count; i++) {
				buf.append(rd.nextInt(10));
			}
			return buf.toString();
		} else {
			return "";
		}

	}

	/**
	 * 隐藏字符串中部分敏感信息
	 * 
	 * @param tg
	 *            目标字符串
	 * @param start
	 *            开始索引
	 * @param end
	 *            结束索引
	 * @return
	 */
	public static String hidepartChar(String tg, int start, int end) {
		return new StringBuffer(tg).replace(start, end, "**").toString();
	}

	/**
	 * 判断字符在字符串中出现的次数
	 * 
	 * @param tg
	 * @param fg
	 * @return
	 */
	public static Integer getCountInStr(String tg, char fg) {
		if (isNotEmpty(tg)) {
			int i = 0;
			if (isNotEmpty(tg)) {
				char ch[] = tg.toCharArray();
				for (char c : ch) {
					if (c == fg) {
						i++;
					}
				}
			}
			return i;
		}
		return 0;
	}

	/***
	 * 对象数组转换成对象集合
	 * 
	 * @param <T>
	 * @param ts
	 * @return
	 */
	public static <T> List<T> getList(T... ts) {
		return Arrays.asList(ts);
	}

	/**
	 * 得到long类型集合 如果遇到不能转换为long类型的字符串跳过 返回能转换为long类型的long类型集合
	 * @param strings
	 * @return
	 */
	public static List<Long> getListByStrs(String... strings) {
		List<Long> list = new ArrayList<Long>();
		if (strings != null) {
			for (String s : strings) {
				try {
					if (isNotEmpty(s)) {
						list.add(Long.valueOf(s));
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return list;
	}
	/**
	 * 得到Integer类型集合 如果遇到不能转换为Integer类型的字符串跳过 
	 * 返回能转换为Integer类型的Integer类型集合
	 * @param str 用,号隔开
	 * @return
	 */
	public static List<Integer> getIntegerListByStrs(String strs) {
		String[] strings=strs.split(",");
		List<Integer> list = new ArrayList<Integer>();
		if (strings != null) {
			for (String s : strings) {
				try {
					if (isNotEmpty(s)) {
						list.add(Integer.valueOf(s));
					}
				} catch (NumberFormatException e) {
				}
			}
		}
		return list;
	}
	public static void main(String[] args) {
		System.out.println(getIntegerListByStrs("2"));
	}
	/**
	 * 把首字母变成大写
	 * 
	 * @param str要转换的字符串
	 * @return
	 */
	public static String toUpcaseFist(String str) {
		if (str != null && !str.trim().equals("")) {
			if (str.length() == 1) {
				return str.toUpperCase();
			} else {
				return str.substring(0, 1).toUpperCase() + str.substring(1);
			}
		} else {
			return "";
		}

	}

	/**
	 * 根据键值对得到map<String,Object>对象
	 * 
	 * @param ag
	 * @return
	 */
	public static LinkedHashMap<String, Object> getMap(Object... ag) {
		LinkedHashMap<String, Object> mp = new LinkedHashMap<String, Object>();
		if (ag != null && ag.length > 0 && ag.length % 2 == 0) {
			int i = 0;
			for (@SuppressWarnings("unused")
			Object o : ag) {
				mp.put(String.valueOf(ag[i]), ag[++i]);
				i++;
				if (i == ag.length) {
					break;
				}

			}
		}
		return mp;
	}

	/**
	 * 根据键值对得到map<String,String>对象
	 * 
	 * @param ag
	 * @return
	 */
	public static LinkedHashMap<String, String> getStrValueMap(String... ag) {
		LinkedHashMap<String, String> mp = new LinkedHashMap<String, String>();
		if (ag != null && ag.length > 0 && ag.length % 2 == 0) {
			int i = 0;
			for (@SuppressWarnings("unused")
			String o : ag) {
				mp.put(ag[i], ag[++i]);
				i++;
				if (i == ag.length) {
					break;
				}

			}
		}
		return mp;
	}

	/**
	 * 动态图像转换成静态图片
	 * 
	 * @param file图片文件
	 */
	public static void convertImageToStatic(File file) {
		if (file != null) {
			try {
				BufferedImage bufferedimage = ImageIO.read(file);
				if (bufferedimage != null) {
					ImageIO.write(bufferedimage, "gif", file);// 1131.gif是静态的
				}
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 判断是否为数字
	 * 
	 * @param tg
	 * @return
	 */
	public static Boolean isNumber(String tg) {
		if (isNotEmpty(tg)) {
			try {
				Double.valueOf(tg);
				return true;
			} catch (NumberFormatException e) {
			}
		}
		return false;
	}

	/**
	 * 处理浮点数相加运算
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatAdd(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.add(b2).doubleValue();
	}

	/**
	 * 处理浮点数相减运算
	 * 
	 * @param v
	 *            被减数
	 * @param v2
	 *            减数
	 * @return
	 */
	public static Double floatSubtract(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}

	/**
	 * 处理浮点数相除
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatDiv(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2).doubleValue();
	}

	/**
	 * 处理浮点数相乘
	 * 
	 * @param v
	 * @param v2
	 * @return
	 */
	public static Double floatMulply(Double v, Double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.multiply(b2).doubleValue();
	}

	/**
	 * 判断集合是否不为空
	 * 
	 * @param list
	 * @return
	 */
	public static Boolean isNotEmpty(
			@SuppressWarnings("rawtypes") Collection list) {
		return list != null && list.size() > 0;
	}

	/**
	 * 判断数组是否不为空
	 * 
	 * @param arr
	 * @return
	 */
	public static Boolean isNotEmptyArr(Object[] arr) {
		return arr != null && arr.length > 0;
	}

	/**
	 * 得到查询条件集合
	 * 
	 * @param conditions
	 * @return
	 */
	public static Set<Condition> getConditions(Condition... conditions) {
		Set<Condition> condi = new HashSet<Condition>();
		if (isNotEmptyArr(conditions)) {
			for (Condition e : conditions) {
				condi.add(e);
			}
		}
		return condi;
	}

	/****************************************** 华丽分割线 *******************************************************/
	/**
	 * jndi配置信息
	 */
	private static ResourceBundle JNDI;
	/**
	 * JNDI上下文初始化
	 */
	public static InitialContext init;

	// 初始化环境变量
	private static void initJNDI() {
		try {
			JNDI = ResourceBundle.getBundle("jndi");
			Properties prop = new Properties();
			prop.put(Context.INITIAL_CONTEXT_FACTORY,
					JNDI.getObject(Context.INITIAL_CONTEXT_FACTORY));
			prop.put(Context.PROVIDER_URL, JNDI.getObject(Context.PROVIDER_URL));
			init = new InitialContext(prop);
		} catch (NamingException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 得到远程访问EJB会话bean对象
	 * 
	 * @param <T>
	 * @param name
	 *            jndi名称：例如：InventoryDao/remote 返回IInventoryDao的实现类的对象
	 * @return 如果不存在返回null
	 */
	public static <T> T getRemoteObjectByJndiName(String name) {
		try {
			if (init == null) {
				initJNDI();
			}
			return (T) init.lookup(name);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For",
			"X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	public static String getIP(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0
					&& !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}
		return request.getRemoteAddr();
	}

	/******************* 华丽分割线 ****************************************************************/

	/**
	 * 对页面显示内容进行编码
	 * 
	 * @param str
	 * @return
	 */
	public static String htmlEncoding(String str) {
		StringBuffer bfu = new StringBuffer();
		if (str != null) {
			String s = "&#";
			char[] cs = str.toCharArray();
			for (char c : cs) {
				int it = c;
				bfu.append(s).append(it).append(";");
			}
		}
		return bfu.toString();

	}

	/**
	 * 得到json数据格式
	 * 
	 * @param flag
	 *            obj[key] key数组
	 * @param property
	 *            name,age..对象属性数组
	 * @param values
	 *            yulong,22.对象属性对应的值
	 * @return
	 */
	public static StringBuffer getJson(String[] flag, String[] property,
			List<String[]> values) {
		StringBuffer buf = new StringBuffer();
		if (flag != null && property != null && property.length > 0) {
			if (values != null && values.size() > 0
					&& property.length == values.get(0).length
					&& values.size() == flag.length) {
				Iterator<String[]> ite = values.iterator();
				buf.append("({");
				for (int j = 0; j < flag.length; j++) {
					buf.append("\"").append(flag[j]).append("\"").append(":");
					String[] ss = ite.next();
					buf.append("{");
					for (int i = 0; i < property.length; i++) {
						buf.append(property[i]).append(":").append("\"")
								.append(ss[i]).append("\"");
						if (property.length - 1 > i) {
							buf.append(",");
						}
					}
					buf.append("}");
					if (ite.hasNext()) {
						buf.append(",");
					}
				}
				buf.append("})");
			}

		}
		return buf;
	}

}
