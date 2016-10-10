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

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtils {
	/**
	 * 保留两位小数
	 * @param num
	 * @return
	 */
	public static double formatTwoDecimalsToStr(double num){
		BigDecimal bg = new BigDecimal(num);
	    double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return  f1;
	}

	public static float formatTwoDecimals(float num){
		BigDecimal bg = new BigDecimal(num);
		float f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
		return  f1;
	}
	
	public static String formatTwoDecimalsToStr(float num){
		DecimalFormat nf = new DecimalFormat();
		nf.setMaximumFractionDigits(2);
		return nf.format(num);
	}
	public static String formatDecimalsToStr(Number num,int digits){
		DecimalFormat nf = new DecimalFormat();
		nf.setMaximumFractionDigits(digits);
		return nf.format(num);
	}
	public static void main(String[] args) {
		System.out.println(formatDecimalsToStr(2.16d,1));
	}
}
