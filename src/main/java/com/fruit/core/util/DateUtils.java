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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils {
	
	private static SimpleDateFormat   sdf=   new   SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat   longsdf=   new   SimpleDateFormat("yyyyMMddHHmmssSSS");
	public static final String STARTTIME=" 00:00:00";
	public static final String ENDTIME=" 23:59:59";
	/**
	 * 私有构造函数
	 */
	private DateUtils() {
	}
	/**
	 * unix时间戳转成日期
	 * @author eason
	 * 2015年1月28日
	 * @param timestamp
	 * @return
	 */
	public static Date unixTimestampToDate(int timestamp){
		Calendar calendar = Calendar.getInstance();  
		calendar.setTimeInMillis(timestamp*1000l);
	 return calendar.getTime();
	}
	/**  
     * 计算两个日期之间相差的天数  
     * @param smdate 较小的时间 
     * @param bdate  较大的时间 
     * @return 相差天数 
     * @throws ParseException  
     */    
    public static int daysBetween(Date smdate,Date bdate) throws ParseException    
    {    
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
        smdate=sdf.parse(sdf.format(smdate));  
        bdate=sdf.parse(sdf.format(bdate));   
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }    
    /**
     * 
     * @author eason	
     * @param smdate yyyy-MM-dd
     * @param bdate yyyy-MM-dd
     * @return
     * @throws ParseException
     */
    public static int daysBetween(String smdate,String bdate) throws ParseException    
    {    
        
       return daysBetween(DateUtils.parseDate(smdate, "yyyy-MM-dd"),DateUtils.parseDate(bdate, "yyyy-MM-dd"))  ;       
    }    
    public static int minuteBetween(Date smdate,Date bdate) throws ParseException    
    {    
        Calendar cal = Calendar.getInstance();    
        cal.setTime(smdate);    
        long time1 = cal.getTimeInMillis();                 
        cal.setTime(bdate);    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*60);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    } 
	/**
	 * 转化为秒时间戳
	 * @author eason
	 * 2015年1月16日
	 * @param date
	 * @return
	 */
	public static int formatDateToUnixTimestamp(Date date){
		return Long.valueOf(date.getTime()/1000).intValue();
	}
	/**
	 * <p>
	 * 返回一个当前的时间，并按格式转换为字符串
	 * </p>
	 * 例：17:27:03
	 * 
	 * @return String
	 */
	public static String getNowTime() {
		GregorianCalendar gcNow = new GregorianCalendar();
		Date dNow = gcNow.getTime();
		DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM,
				Locale.SIMPLIFIED_CHINESE);
		return df.format(dNow);   
	}
	public static String getLongTime(){
		return longsdf.format(new Date());
	}
	/**
	 * 
	 * @author yulong
	 * @description
	 * @date 2012-8-25 
	 * @param dateStr 格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date parseDate(String dateStr){
		try {
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static Date parseDate(String dateStr,String format){
		try {
			SimpleDateFormat sdf= new SimpleDateFormat(format);
			return sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static long getTime(String  time) {
     	 SimpleDateFormat   shijian=   new   SimpleDateFormat("yyyy-MM-dd");   
     	 long longtime=0; 
     	 try{   
     		 longtime=shijian.parse(time).getTime();   
     	           
     	  }catch(Exception e)
     	  {}
     	  return longtime;
     	}
	/**
	 * <p>
	 * 返回一个当前日期，并按格式转换为字符串
	 * </p>
	 * 例：2004-4-30
	 * 
	 * @return String
	 */
	public static String getNowDate() {
		GregorianCalendar gcNow = new GregorianCalendar();
		Date dNow = gcNow.getTime();
		DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM,
				Locale.SIMPLIFIED_CHINESE);
		return df.format(dNow);
	}
	
	/**
	 * <p>
	 * 返回一个当前日期和时间，并按格式转换为字符串
	 * </p>
	 * 例：2004-4-30 17:27:03
	 * 
	 * @return String
	 */
	public static String getNowDateTime() {
		GregorianCalendar gcNow = new GregorianCalendar();
		Date dNow = gcNow.getTime();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,
				DateFormat.MEDIUM, Locale.SIMPLIFIED_CHINESE);
		return df.format(dNow);
	}

	/**
	 * <p>
	 * 返回当前年
	 * </p>
	 * 
	 * @return int
	 */
	public static int getThisYear() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.YEAR);
	}
	/**
	 * 返回当前周
	 * @author yulong
	 * @description
	 * @date 2012-8-27 
	 * @return
	 */
	public static int getThisWeek() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.WEEK_OF_YEAR);
	}

	/**
	 * 
	 * @param date java.util.Date类型的日期
	 * @return 返回年份 如1990
	 */
	public static int getYear(Date date){
		if(date==null){
			return 0;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}
	public static int getMonth(Date date){
		if(date==null){
			return 0;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH+1);
	}
	public static int getday(Date date){
		if(date==null){
			return 0;
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 返回本月
	 * 
	 * @return int
	 */
	public static int getThisMonth() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.MONTH) + 1;
	}
	/**
	 * 返回当前日 day of month
	 * @author yulong
	 * @description
	 * @date 2012-8-27 
	 * @return
	 */
	public static int getThisDay() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.DAY_OF_MONTH);
	}
	/**
	 * 返回当前年的第几周
	 * @author yulong
	 * @description
	 * @date 2012-8-27 
	 * @return
	 */
	public static int getThisWeeKofYear() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.WEEK_OF_YEAR);
	}
	/**
	 * 返回今天是本月的第几天
	 * 
	 * @return int 从1开始
	 */
	public static int getToDayOfMonth() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.DAY_OF_MONTH);
	}

	/**
	 * 返回当前的小时
	 * 
	 * @return int
	 */
	public static int getHour() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.HOUR);
	}

	/**
	 * 返回当前的分钟
	 * 
	 * @return int 返回当前的分钟
	 */
	public static int getMinute() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.MINUTE);
	}
	/**
	 * 返回当前的秒数
	 * 
	 * @return int 第几秒
	 */
	public static int getSecond() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.SECOND);
	}

	/**
	 * 返回今天是本年的第几周
	 * @return int 从1开始
	 */

	public static int getToWeekOfYear() {
		GregorianCalendar gcNow = new GregorianCalendar();
		return gcNow.get(GregorianCalendar.WEEK_OF_YEAR);
	}

	/**
	 * 返回一格式化的日期
	 * @param time
	 *            long
	 * @return String yyyy-MM-dd HH:mm:ss 格式
	 */
	public static String formatDate(Date date) {
		if (date == null)
			return "";
		else
			return sdf.format(date);
	}

	/**
	 * 返回一格式化的日期
	 * 
	 * @param time
	 *            long
	 * @return String 2005-6-17 格式
	 */
	public static String formatSDate(Date date) {
		if (date == null)
			return "";
		else {
			SimpleDateFormat bartDateFormat = new SimpleDateFormat(
					"yyyy-M-d HH:mm:ss");
			return bartDateFormat.format(date);
		}
	}
	
	public static String format(Date date,String fmt){
    	if(date == null)
    		return "0";	//as min value
		SimpleDateFormat formater = new SimpleDateFormat(fmt);
		return formater.format(date);
	}
	/** convert time '2010-10-02 hh:mm:ss' to '20101002' */
	public static String mergeDate(String time){
    	if(time == null)
    		return "0";	//as min value
    	time = time.substring(0,time.indexOf(' '));
		return time.replaceAll("-","");
	}
	public static String humanTime(int updateDate,int updateTime){
		int curDate = Calendar.getInstance().get(Calendar.YEAR) * 10000;
		curDate += (Calendar.getInstance().get(Calendar.MONTH)+1) * 100;
		curDate += Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		if(updateDate == curDate)	{//set time only if is today
			return updateTime / 100 + ":" + 
					(updateTime % 100 < 10 ? ("0" + (updateTime % 100)) : (updateTime % 100) );
		}else{	//set date only
			return "" + (updateDate / 10000) + '-' + (updateDate % 10000 / 100) 
					+ '-' + (updateDate % 10000 % 100);
		}
	}

	/**
	 * 返回已添加指定时间间隔的日期
	 * 
	 * @param interval
	 *            表示要添加的时间间隔("y":年;"d":天;"m":月;如有必要可以自行增加)
	 * @param number
	 *            表示要添加的时间间隔的个数
	 * @param date
	 *            java.util.Date()
	 * @return String 2005-5-12格式的日期字串
	 */
	public static String dateAdd(String interval, int number,
			Date date) {
		String strTmp = "";
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		// 加若干年
		if (interval.equals("y")) {
			int currYear = gc.get(Calendar.YEAR);
			gc.set(Calendar.YEAR, currYear + number);
		}
		// 加若干月
		else if (interval.equals("m")) {
			int currMonth = gc.get(Calendar.MONTH);
			gc.set(Calendar.MONTH, currMonth + number);
		}
		// 加若干天
		else if (interval.equals("d")) {
			int currDay = gc.get(Calendar.DATE);
			gc.set(Calendar.DATE, currDay + number);
		}
		// 加若小时
		else if (interval.equals("h")) {
			int currDay = gc.get(Calendar.HOUR);
			gc.set(Calendar.HOUR, currDay + number);
		}
		SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		strTmp = bartDateFormat.format(gc.getTime());
		return strTmp;
	}

	/**
	 * <p>
	 * 返回两个日期之间的单位间隔数
	 * </p>
	 * 
	 * @param a
	 *            java.util.Date
	 * @param b
	 *            java.util.Date
	 * @return int 间隔数
	 */
	public static int dateDiff(Date a, Date b) {
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}

		while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
			tempDifference = 365 * (later.get(Calendar.YEAR) - earlier
					.get(Calendar.YEAR));
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later
				.get(Calendar.DAY_OF_YEAR)) {
			tempDifference = later.get(Calendar.DAY_OF_YEAR)
					- earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		return difference;
	}
	/**
     * 获取当前日期是星期几<br>
     * 
     * @param dt
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    /**
     * yyyy-M-d
     * @author eason	
     * @param data
     * @return
     */
    public static int getWeekOfDate(String data) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseDate(data, "yyyy-M-d"));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }
    public static int getWeekOfDate(String data,String format) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.parseDate(data, format));
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }
	/**
	 * <p>
	 * 该方法是获得到每月1号星期一的数据
	 * </p>
	 * 
	 * @return -返回一个数字
	 */
	/**
	 * <p>
	 * 该方法是获得到每月1号星期一的数据
	 * </p>
	 * 
	 * @return -返回一个数字
	 */
	public static int getDate(int curYear, int curMonth, int curDate) {
		int day1 = 0;
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(curYear, curMonth - 1, curDate);
		int dayOfWeek = cal.get(cal.DAY_OF_WEEK);
		System.out.println("curDate=" + curDate + " dayOfWeek " + dayOfWeek);
		switch (dayOfWeek) {
		case 1: // 星期天
			day1 = 0;
			break;
		case 2: // 星期一
			day1 = 1;
			break;
		case 3: // 星期二
			day1 = 2;
			break;
		case 4: // 星期三
			day1 = 3;
			break;
		case 5: // 星期四
			day1 = 4;
			break;
		case 6: // 星期五
			day1 = 5;
			break;
		case 7: // 星期六
			day1 = 6;
			break;
		}
		return day1;
	}

	public static String checkTime(int id) {
		String bol = "";
		Calendar tt = Calendar.getInstance();
		int result = tt.get(Calendar.DAY_OF_WEEK);
		System.out.println("result=" + result);
		Date sdate = new Date();
		int shour = tt.get(Calendar.HOUR_OF_DAY);

		if (id == 3) {
			switch (result) {
			case 1:
				break;
			case 7:
				if ((shour >= 8) && (shour < 12)) {
					bol = "disabled";
					break;
				}
			default:
				if ((shour >= 8) && (shour < 12)) {
					bol = "disabled";
					break;
				} else if ((shour >= 14) && (shour < 17)) {
					bol = "disabled";
					break;
				}
			}
		}
		return bol;
	}

	/**
	 * <p>
	 * 该方法是将字符型转变成日期型
	 * </p>
	 * 
	 * @param strX
	 *            -传入字符类型
	 * @return -返回日期类型
	 */
	public static Date getStrDate(String strX) {
		Date date1 = new Date();
		if (!strX.equals("")) {
			try {
				date1 = (DateFormat.getDateInstance()).parse(strX);
			} catch (Exception ex) {

				System.out.println(ex.toString());
			}
		} else {
			GregorianCalendar gcNow = new GregorianCalendar();
			date1 = gcNow.getTime();
		}

		return date1;
	}

	final static long[] lunarInfo = new long[] { 0x04bd8, 0x04ae0, 0x0a570,
			0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
			0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0,
			0x0ada2, 0x095b0, 0x14977, 0x04970, 0x0a4b0, 0x0b4b5, 0x06a50,
			0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970, 0x06566,
			0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0,
			0x1c8d7, 0x0c950, 0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4,
			0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557, 0x06ca0, 0x0b550,
			0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950,
			0x06aa0, 0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260,
			0x0f263, 0x0d950, 0x05b57, 0x056a0, 0x096d0, 0x04dd5, 0x04ad0,
			0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
			0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40,
			0x0af46, 0x0ab60, 0x09570, 0x04af5, 0x04970, 0x064b0, 0x074a3,
			0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0, 0x0c960,
			0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0,
			0x092d0, 0x0cab5, 0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9,
			0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930, 0x07954, 0x06aa0,
			0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65,
			0x0d530, 0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0,
			0x1d0b6, 0x0d250, 0x0d520, 0x0dd45, 0x0b5a0, 0x056d0, 0x055b2,
			0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255, 0x06d20, 0x0ada0 };

	final public static int lYearDays(int y)// ====== 传回农历 y年的总天数
	{
		int i, sum = 348;
		for (i = 0x8000; i > 0x8; i >>= 1) {
			if ((lunarInfo[y - 1900] & i) != 0)
				sum += 1;
		}
		return (sum + leapDays(y));
	}

	final public static int leapDays(int y)// ====== 传回农历 y年闰月的天数
	{
		if (leapMonth(y) != 0) {
			if ((lunarInfo[y - 1900] & 0x10000) != 0)
				return 30;
			else
				return 29;
		} else
			return 0;
	}

	final public static int leapMonth(int y)// ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
	{
		return (int) (lunarInfo[y - 1900] & 0xf);
	}

	final public static int monthDays(int y, int m)// ====== 传回农历 y年m月的总天数
	{
		if ((lunarInfo[y - 1900] & (0x10000 >> m)) == 0)
			return 29;
		else
			return 30;
	}

	final public static String AnimalsYear(int y)// ====== 传回农历 y年的生肖
	{
		final String[] Animals = new String[] { "鼠", "牛", "虎", "兔", "龙", "蛇",
				"马", "羊", "猴", "鸡", "狗", "猪" };
		return Animals[(y - 4) % 12];
	}

	final public static String cyclicalm(int num)// ====== 传入月日的offset 传回干支,
													// 0=甲子
	{
		final String[] Gan = new String[] { "甲", "乙", "丙", "丁", "戊", "己", "庚",
				"辛", "壬", "癸" };
		final String[] Zhi = new String[] { "子", "丑", "寅", "卯", "辰", "巳", "午",
				"未", "申", "酉", "戌", "亥" };
		return (Gan[num % 10] + Zhi[num % 12]);
	}

	final public static String cyclical(int y)// ====== 传入 offset 传回干支, 0=甲子
	{
		int num = y - 1900 + 36;
		return (cyclicalm(num));
	}

	final public long[] Lunar(int y, int m)// 传出农历.year0 .month1 .day2
											// .yearCyl3 .monCyl4
	// .dayCyl5 .isLeap6
	{
		final int[] year20 = new int[] { 1, 4, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1 };
		final int[] year19 = new int[] { 0, 3, 0, 1, 0, 1, 0, 0, 1, 0, 1, 0 };
		final int[] year2000 = new int[] { 0, 3, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1 };
		long[] nongDate = new long[7];
		int i = 0, temp = 0, leap = 0;
		Date baseDate = new Date(1900, 1, 31);
		Date objDate = new Date(y, m, 1);
		long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
		if (y < 2000)
			offset += year19[m - 1];
		if (y > 2000)
			offset += year20[m - 1];
		if (y == 2000)
			offset += year2000[m - 1];
		nongDate[5] = offset + 40;
		nongDate[4] = 14;
		for (i = 1900; i < 2050 && offset > 0; i++) {
			temp = lYearDays(i);
			offset -= temp;
			nongDate[4] += 12;
		}
		if (offset < 0) {
			offset += temp;
			i--;
			nongDate[4] -= 12;
		}
		nongDate[0] = i;
		nongDate[3] = i - 1864;
		leap = leapMonth(i); // 闰哪个月
		nongDate[6] = 0;
		for (i = 1; i < 13 && offset > 0; i++) {
			// 闰月
			if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
				--i;
				nongDate[6] = 1;
				temp = leapDays((int) nongDate[0]);
			} else {
				temp = monthDays((int) nongDate[0], i);
			}
			// 解除闰月
			if (nongDate[6] == 1 && i == (leap + 1))
				nongDate[6] = 0;
			offset -= temp;
			if (nongDate[6] == 0)
				nongDate[4]++;
		}
		if (offset == 0 && leap > 0 && i == leap + 1) {
			if (nongDate[6] == 1) {
				nongDate[6] = 0;
			} else {
				nongDate[6] = 1;
				--i;
				--nongDate[4];
			}
		}
		if (offset < 0) {
			offset += temp;
			--i;
			--nongDate[4];
		}
		nongDate[1] = i;
		nongDate[2] = offset + 1;
		return nongDate;
	}

	final public static long[] calElement(int y, int m, int d)
	// 传出y年m月d日对应的农历.year0 .month1 .day2 .yearCyl3 .monCyl4 .dayCyl5 .isLeap6
	{
		long[] nongDate = new long[7];
		int i = 0, temp = 0, leap = 0;
		Date baseDate = new Date(0, 0, 31);
		Date objDate = new Date(y - 1900, m - 1, d);
		long offset = (objDate.getTime() - baseDate.getTime()) / 86400000L;
		nongDate[5] = offset + 40;
		nongDate[4] = 14;
		for (i = 1900; i < 2050 && offset > 0; i++) {
			temp = lYearDays(i);
			offset -= temp;
			nongDate[4] += 12;
		}
		if (offset < 0) {
			offset += temp;
			i--;
			nongDate[4] -= 12;
		}
		nongDate[0] = i;
		nongDate[3] = i - 1864;
		leap = leapMonth(i); // 闰哪个月
		nongDate[6] = 0;
		for (i = 1; i < 13 && offset > 0; i++) {
			// 闰月
			if (leap > 0 && i == (leap + 1) && nongDate[6] == 0) {
				--i;
				nongDate[6] = 1;
				temp = leapDays((int) nongDate[0]);
			} else {
				temp = monthDays((int) nongDate[0], i);
			}
			// 解除闰月
			if (nongDate[6] == 1 && i == (leap + 1))
				nongDate[6] = 0;
			offset -= temp;
			if (nongDate[6] == 0)
				nongDate[4]++;
		}
		if (offset == 0 && leap > 0 && i == leap + 1) {
			if (nongDate[6] == 1) {
				nongDate[6] = 0;
			} else {
				nongDate[6] = 1;
				--i;
				--nongDate[4];
			}
		}
		if (offset < 0) {
			offset += temp;
			--i;
			--nongDate[4];
		}
		nongDate[1] = i;
		nongDate[2] = offset + 1;
		return nongDate;
	}

	public static String getchina(int day) {
		String a = "";
		if (day == 10)
			return "初十";
		int two = (int) ((day) / 10);
		if (two == 0)
			a = "初";
		if (two == 1)
			a = "十";
		if (two == 2)
			a = "廿";
		if (two == 2)
			a = "卅";
		int one = (int) (day % 10);
		switch (one) {
		case 1:
			a += "一";
			break;
		case 2:
			a += "二";
			break;
		case 3:
			a += "三";
			break;
		case 4:
			a += "四";
			break;
		case 5:
			a += "五";
			break;
		case 6:
			a += "六";
			break;
		case 7:
			a += "七";
			break;
		case 8:
			a += "八";
			break;
		case 9:
			a += "九";
			break;
		}
		return a;
	}
	
	/**
	 * 将datestr字符串pattern格式的日期转化为java.util.Date
	 * 
	 * @param datestr
	 * @param pattern datestr的日期格式
	 * @return java.util.Date
	 */
	public static Date getDate(String dateStr, String format) {
		if(dateStr==null||dateStr.trim().equals("")){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	
	public static Integer getAgeFromBirthday(Date date){
		return new Date().getYear() - date.getYear();
	}
	/**
	 * 增加或减少数天
	 * @author yulong
	 * @description
	 * @date 2012-8-25 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }
	public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }
	/**
	 * 增加或减少数小时
	 * @author yulong
	 * @description
	 * @date 2012-8-25 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addHour(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, n);
        return cal.getTime();
    }
	/**
	 * 增加或减少数分钟
	 * @author yulong
	 * @description
	 * @date 2012-8-25 
	 * @param date
	 * @param n
	 * @return
	 */
	public static Date addMinute(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, n);
        return cal.getTime();
    }
	/**
	 * 判断日期是否过期
	 * @author yulong
	 * @description
	 * @date 2012-8-25 
	 * @param date    生产日期
	 * @param minute  分钟
	 * @return
	 */
	public static boolean dateIsExpire(Date date,int minute){
		Date expireDate=addMinute(date,minute);
		if(expireDate.after(new Date())){
			return false;
		}
		return true;
	}
	public static int getNowWeek() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
		return dayOfWeek;
		}
	public static String[] getThisWeekSartAndEnd(){
			Calendar calendar = Calendar.getInstance();
		   int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
		   int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
		   calendar.add(Calendar.DAY_OF_WEEK, min-current); //当天-基准，获取周开始日期
		   Date start = calendar.getTime();
		   calendar.add(Calendar.DAY_OF_WEEK, 6); //开始+6，获取周结束日期
		   Date end = calendar.getTime();
		   String[] result=new String[2];
		   result[0]=DateUtils.format(start,"yyyy-MM-dd")+" 00:00:00";
		   result[1]=DateUtils.format(end,"yyyy-MM-dd")+" 23:59:59";
		   return result;
	}
	public static String[] getThisDaySartAndEnd(){
		Date data=new Date();
		String dateStr=DateUtils.format(data, "yyyy-MM-dd");
		String[] result=new String[2];
		result[0]=dateStr+STARTTIME;
		result[1]=dateStr+ENDTIME;
		return result;
	}
	public static String[] getThisMonthSartAndEndDay(){
		  //获取当前月第一天：
        Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        String first = DateUtils.format(c.getTime(),"yyyy-MM-dd");
        //获取当前月最后一天
        Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
        String last = DateUtils.format(ca.getTime(),"yyyy-MM-dd");
		String[] result=new String[2];
		result[0]=first+STARTTIME;
		result[1]=last+ENDTIME;
		return result;
	}
	/**
	 * 
	 * @param month yyyy-MM
	 * @return
	 */
	public static String[] getMonthSartAndEndDay(String month){
		return getMonthSartAndEndDay(month,"yyyy-MM");
	}
	public static String[] getMonthSartAndEndDay(String month,String format){
		  //获取当前月第一天：
	  Date date=DateUtils.parseDate(month,format);
    Calendar c = Calendar.getInstance(); 
    c.setTime(date);
    c.add(Calendar.MONTH, 0);
    c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
    String first = DateUtils.format(c.getTime(),"yyyy-MM-dd");
    //获取当前月最后一天
    Calendar ca = Calendar.getInstance();  
    ca.setTime(date);
    ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
    String last = DateUtils.format(ca.getTime(),"yyyy-MM-dd");
		String[] result=new String[2];
		result[0]=first;
		result[1]=last;
		return result;
	}
	private static SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	/**
	 * 时间间隔方式 显示，类似新浪微博
	 * @author panyl  yyyy-MM-dd HH:mm:ss
	 * @date 2013年11月15日 下午1:53:15
	 * @param createtime
	 * @return
	 */
	public static String getInterval(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式  
        String interval = null;  
        ParsePosition pos = new ParsePosition(0);  
        Date d1 = (Date) sd.parse(createtime, pos);  
        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔  
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒  
          
        if(time/1000 < 10 && time/1000 >= 0) {  
        //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒  
            interval ="刚刚";  
              
        } else if(time/1000 < 60 && time/1000 > 0) {  
        //如果时间间隔小于60秒则显示多少秒前  
            int se = (int) ((time%60000)/1000);  
            interval = se + "秒前";  
              
        } else if(time/60000 < 60 && time/60000 > 0) {  
        //如果时间间隔小于60分钟则显示多少分钟前  
            int m = (int) ((time%3600000)/60000);//得出的时间间隔的单位是分钟  
            interval = m + "分钟前";  
              
        }else if(time/3600000 < 24 && time/3600000 >= 0) {  
        //如果时间间隔小于24小时则显示多少小时前  
            int h = (int) (time/3600000);//得出的时间间隔的单位是小时  
            interval = h + "小时前";  
              
        } else {  
            //大于24小时，则显示正常的时间，但是不显示秒  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
  
            ParsePosition pos2 = new ParsePosition(0);  
            Date d2 = (Date) sdf.parse(createtime, pos2);  
  
            interval = sdf.format(d2);  
        }  
        return interval;  
    }  
	public static String getInterval2(String createtime) { //传入的时间格式必须类似于2012-8-21 17:53:20这样的格式  
        String interval = null;  
        ParsePosition pos = new ParsePosition(0);  
        Date d1 = (Date) sd.parse(createtime, pos);  
        //用现在距离1970年的时间间隔new Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔  
        long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒  
          
        if(time/1000 < 10 && time/1000 >= 0) {  
        //如果时间间隔小于10秒则显示“刚刚”time/10得出的时间间隔的单位是秒  
            interval ="刚刚";  
              
        } else if(time/1000 < 60 && time/1000 > 0) {  
        //如果时间间隔小于60秒则显示多少秒前  
            int se = (int) ((time%60000)/1000);  
            interval = se + "秒前";  
              
        } else if(time/60000 < 60 && time/60000 > 0) {  
        //如果时间间隔小于60分钟则显示多少分钟前  
            int m = (int) ((time%3600000)/60000);//得出的时间间隔的单位是分钟  
            interval = m + "分钟前";  
              
        }else if(time/3600000 < 24 && time/3600000 >= 0) {  
        //如果时间间隔小于24小时则显示多少小时前  
            int h = (int) (time/3600000);//得出的时间间隔的单位是小时  
            interval = h + "小时前";  
              
        }else if(time/3600000 < 24*30 && time/3600000 >= 24) {  
        //如果时间间隔小于24小时则显示多少小时前  
            int d = (int) ((time/3600000)/24);//得出的时间间隔的单位是小时  
            interval = d + "天前";  
        } else if(time/3600000 < 24*30*12 && time/3600000 >= 24*30) {  
        //如果时间间隔小于24小时则显示多少小时前  
            int m = (int) ((time/3600000)/(24*30));//得出的时间间隔的单位是小时  
            interval = m + "个月前";  
        } else if(time/3600000 >= 24*30*12) {  
        //如果时间间隔小于24小时则显示多少小时前  
            int y = (int) ((time/3600000)/(24*30*12));//得出的时间间隔的单位是小时  
            interval = y + "年前";  
        }  else {  
            //大于24小时，则显示正常的时间，但是不显示秒  
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
  
            ParsePosition pos2 = new ParsePosition(0);  
            Date d2 = (Date) sdf.parse(createtime, pos2);  
  
            interval = sdf.format(d2);  
        }  
        return interval;  
    }  
	public static long getIntervalDay(Date startdate, Date enddate) {
		long m_intervalday = 0;//初始化时间间隔的值为0
		m_intervalday = enddate.getTime() - startdate.getTime();//计算所得为微秒数
		m_intervalday = m_intervalday / 1000 / 60 / 60 / 24;//计算所得的天数
		return m_intervalday;
		}
	public static long getIntervalDay(String startdate, String enddate) {
		long m_intervalday = 0;//初始化时间间隔的值为0
		//使用的时间格式为yyyy-MM-dd
		SimpleDateFormat m_simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
		try {
		//创建两个Date对象
		Date date1 = m_simpledateformat.parse(startdate);
		Date date2 = m_simpledateformat.parse(enddate);
		m_intervalday = date2.getTime() - date1.getTime();//计算所得为微秒数
		m_intervalday = m_intervalday / 1000 / 60 / 60 / 24;//计算所得的天数
		} catch (ParseException e) {
		e.printStackTrace();
		}
		return m_intervalday;
		}
	public static void main(String[] args) {
		
//		Date date=getDate("2012年08月25日 20时20分50秒","yyyy年MM月dd日 HH时mm分ss秒");
////		System.out.println(DateUtil.getThisYear() - DateUtil
////				.getYear(date));
//		System.out.println(dateIsExpire(date,30));
//		   Calendar calendar = Calendar.getInstance();
//		   int min = calendar.getActualMinimum(Calendar.DAY_OF_WEEK); //获取周开始基准
//		   int current = calendar.get(Calendar.DAY_OF_WEEK); //获取当天周内天数
//		   calendar.add(Calendar.DAY_OF_WEEK, min-current); //当天-基准，获取周开始日期
//		   Date start = calendar.getTime();
//		   calendar.add(Calendar.DAY_OF_WEEK, 6); //开始+6，获取周结束日期
//		   Date end = calendar.getTime();
//		   System.out.printf("start=%tF, end=%tF ", start, end);  
//		   System.out.println("start:"+DateUtil.format(start,"yyyy-MM-dd"));
		//System.out.println("getThisMonthSartAndEndDay:"+getThisMonthSartAndEndDay()[0]+","+getThisMonthSartAndEndDay()[1]);
		//System.out.println(getNowWeek());
//		String content="如果，可以爱你,";
//		content=content.trim().toLowerCase().replaceAll(",", "").replaceAll("，", "");
//		System.out.println(content);
//		System.out.println(new Date().getTime());
		//System.out.println("getMonthSartAndEndDay:"+getMonthSartAndEndDay("2014-10")[0]+","+getMonthSartAndEndDay("2014-10")[1]);
			try {
				
				/**System.out.println(minuteBetween(DateUtils.parseDate("2015-03-28 00:01", "yyyy-MM-dd HH:mm"),DateUtils.parseDate("2015-03-27 23:59", "yyyy-MM-dd HH:mm")));
				System.out.println(DateUtils.daysBetween(DateUtils.parseDate("2015-03-29", "yyyy-MM-dd"),DateUtils.parseDate("2015-03-28", "yyyy-MM-dd")));
				String formatIntime=DateUtils.format(DateUtils.parseDate("2015-03-29 15:08", "yyyy-MM-dd HH:mm"), "yyyy年MM月dd日 HH时mm分");
				System.out.println("formatIntime:"+formatIntime);
				System.out.println("days:"+DateUtils.daysBetween(DateUtils.parseDate("2015-05-01", "yyyy-MM-dd"), DateUtils.parseDate("2015-07-01", "yyyy-MM-dd")));
				**/
				for(int i=0;i<120;i++){
				System.out.println(DateUtils.format(DateUtils.addMonth(new Date(), -12*(RandomUtils.nextInt(7)+18)), "yyyy-MM-dd"));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
