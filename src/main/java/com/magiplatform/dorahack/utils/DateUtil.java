/**
 * $Id$
 * Copyright(C) 2015-2020 kowlone - internet center, All Rights Reserved.
 */
package com.magiplatform.dorahack.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @version V1.0
 * @Description: 日期工具类
 * @class: DateUtil
 * @Package com.magiplatform.dorahack.utils
 * @date 2020/8/26 15:42
 */
public abstract class DateUtil {
    /**定义常量**/
    public static final String DATE_JFP_STR="yyyyMM";
    public static final String DATE_FULL_STR = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_SMALL_STR = "yyyy-MM-dd";
    public static final String DATE_KEY_STR = "yyyyMMddHHmmss";
	/**
	 * 日期格式化器
	 */
	public enum Formatter{

		MM_dd("MM-dd"),
		yyyyMMdd("yyyyMMdd"),
		yyyy_MM_dd("yyyy-MM-dd"),
		yyyyMMddHH("yyyy-MM-dd HH"),
		yyyyMMddHHmm("yyyy-MM-dd HH:mm"),
		yyyyMMddHHmmss("yyyy-MM-dd HH:mm:ss"),
		yyyyMMddHHmmssTrim("yyyyMMddHHmmss"),
		ddHHmmssTrim("ddHHmmss"),
		yyyy_MM_dd_zh("yyyy年MM月dd日"),
		;

		/** 线程安全,可共享 */
		private DateTimeFormatter formatter;

		Formatter(String pattern) {
			this.formatter = DateTimeFormatter.ofPattern(pattern);
		}

		public String format(LocalDateTime localDateTime) {
			return formatter.format(localDateTime);
		}

		public String format(LocalDate localDate) {
			return formatter.format(localDate);
		}

	}

	/**
	 * 获取当前时间
	 * @return
	 */
	public static Date now(){
		return new Date();
	}

	/**
	 * 计算日期，增加月数
	 * @param date
	 * @param months
	 * @return
	 */
	public static Date addMonth(Date date,int months){
		Calendar c = Calendar.getInstance();
			c.setTime(date);
		c.set(Calendar.MONTH, c.get(Calendar.MONTH)+months);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 计算日期，增加年数
	 * @param date
	 * @param years
	 * @return
	 */
	public static Date addYear(Date date,int years){
		Calendar c = Calendar.getInstance();
			c.setTime(date);
		c.set(Calendar.YEAR, c.get(Calendar.YEAR)+years);

		LocalDate ldate = LocalDate.now();
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 计算日期，增加天数
	 * @param date
	 * @param days
	 * @return
	 */
	public static Date addDay(Date date,int days){
		Calendar c = Calendar.getInstance();
			c.setTime(date);
		c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+days);
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 计算日期，增加小时
	 * @param date
	 * @param hours
	 * @return
	 */
	public static Date addHour(Date date,int hours){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY)+hours);
		return new Date(c.getTimeInMillis());
	}


	/**
	 * 计算日期，增加天数
	 * @param date
	 * @param days
	 * @param holidays 不包含的节假日
	 * @return
	 */
	public static Date addDay(Date date,int days,Set<String> holidays){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i=0;
		while(i<days){
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)+1);
			if(holidays.contains(format(c.getTime(), Formatter.MM_dd))){
				continue;
			}
			i++;
		}
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 计算日期，减少天数
	 * @param date
	 * @param days
	 * @param holidays 不包含的节假日
	 * @return
	 */
	public static Date reduceDay(Date date,int days,Set<String> holidays){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int i=0;
		while(i<days){
			c.set(Calendar.DAY_OF_YEAR, c.get(Calendar.DAY_OF_YEAR)-1);
			if(holidays.contains(format(c.getTime(), Formatter.MM_dd))){
				continue;
			}
			i++;
		}
		return new Date(c.getTimeInMillis());
	}

	/**
	 * 根据指定的格式,格式化日期
	 * @param date java8中的日期
	 * @param formatter 格式化
	 * @return 格式化字符串
	 */
	public static String format(LocalDateTime date,Formatter formatter){
		return formatter.format(date);
	}

	public static String format(LocalDate date,Formatter formatter){
		return formatter.format(date);
	}

	/**
	 * 根据指定的格式,格式化日期
	 * @param date java8 之前的传统日期
	 * @param formatter 格式化
	 * @return 格式化字符串
	 */
	public static String format(Date date, Formatter formatter) {
		return format(asLocalDateTime(date), formatter);
	}

	/**
	 * 格式化当前时间
	 * @param formatter 格式化
	 *
	 * @return 当前时间的格式化字符串
	 */
	public static String format(Formatter formatter) {
		return format(LocalDateTime.now(), formatter);
	}

	/**
	 * 使用参数Format将字符串转为Date
	 */
	public static Date parse(String strDate, String pattern)
			throws ParseException{
		return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
				pattern).parse(strDate);
	}

	public static Date asDate(LocalDate localDate) {
		return localDate == null?null:Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public static Date asDate(LocalDateTime localDateTime) {
		return localDateTime==null?null: Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDate asLocalDate(Date date) {
		return date == null ? null:Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public static LocalDateTime asLocalDateTime(Date date) {
		return date == null ? null:Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
	}

	/**
	 * 获取指定年份的所有周末(周六,周日)
	 * @param year
	 *
	 * @return
	 */
	public static List<LocalDate> getWeekend(int year) {
		LocalDate localDate = LocalDate.of(year, 1, 1);
		List<LocalDate> result = new ArrayList<>();
		LocalDate today = localDate;
		int i;
		do {
			switch (today.getDayOfWeek()) {
				case SATURDAY:
					result.add(today);
					today =  today.plusDays(1);
					if(today.getYear() == year){
						result.add(today);
					}
					i=6;
					break;
				case SUNDAY:
					result.add(today);
					i=6;
					break;
				default:
					i=1;
					break;
			}
			today = today.plusDays(i);
		} while (today.getYear() == year);

		return result;
	}

	/**
	 * 比较日期
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareDay(Date date1, Date date2) {
		try {
			date1 = parse(format(date1, Formatter.yyyyMMdd), Formatter.yyyyMMdd.toString());
			date2 = parse(format(date2, Formatter.yyyyMMdd), Formatter.yyyyMMdd.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date1.compareTo(date2);
	}

	/**
	 * 间隔天数
	 * @param date1 开始日期
	 * @param date2 结束日期
	 * @return
	 */
	public static long intervalDays(Date date1, Date date2) {
		return  asLocalDate(date2).toEpochDay() - asLocalDate(date1).toEpochDay();
	}

	/**
	 * 获取某天，之前或之后几天的从零点开始时间
	 * @param calendar
	 * @param days
     * @return
     */
	public static Date getBeforeDayBegin(Calendar calendar,int days){
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DATE, days);
		calendar.set(Calendar.MILLISECOND, 0);
		Date date=calendar.getTime();
		return date;
	}

	/**
	 * 获取某天，之前或之后几天的23时，59分，59秒的时间
	 * @param calendar
	 * @param days
     * @return
     */
	public static Date getBeforeDayEnd(Calendar calendar,int days){
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		calendar.add(Calendar.DATE, days);
		Date date=calendar.getTime();
		return date;
	}
	/**
	 * 间隔小时数
	 * @param date1 开始日期
	 * @param date2 结束日期
	 * @return
	 */
	public static long intervalHours(Date date1, Date date2) {
		long day = 0;
		try {

			day = (date1.getTime() - date2.getTime()) / (60 * 60 * 1000);

//         这里精确到了秒，我们可以在做差的时候将时间精确到天
		} catch (Exception e) {
			return 0;
		}

		return day;
	}
	/**
	 * 两个日期间隔秒数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long intervalSeconds(Date date1, Date date2) {
		long seconds = 0;
		try {
			seconds = (date1.getTime() - date2.getTime()) / (1000);
		} catch (Exception e) {
			return 0;
		}
		return seconds;
	}


	/**
	 * 返回当前时间 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getNowTime(){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=sdf.format(new Date());
		return time;
	}

	/**
	 * 返回当前时间
	 * @return
	 */
	public static String getNowTime(String format){
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		String time=sdf.format(new Date());
		return time;
	}

    /**
     *  获取两个日期相差的月数
     * @param d1    较大的日期
     * @param d2    较小的日期
     * @return  如果d1>d2返回 月数差 否则返回0
     */
    public static int getMonthDiff(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if(c1.getTimeInMillis() < c2.getTimeInMillis()) return 0;
        int year1 = c1.get(Calendar.YEAR);
        int year2 = c2.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH);
        int month2 = c2.get(Calendar.MONTH);
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
        // 获取月数差值
        int monthInterval =  (month1 + 12) - month2  ;
        if(day1 < day2) monthInterval --;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
    }

	public static void main(String[] args) {
//
//		Calendar calendarBefore = Calendar.getInstance();
//		System.out.println(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
//				DateUtil.getBeforeDayBegin(calendarBefore,-1)));
//		Calendar calendarEnd = Calendar.getInstance();
//		System.out.println(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
//				DateUtil.getBeforeDayEnd(calendarEnd,-1)));

		try {
			long dayNum = DateUtil.intervalHours(DateUtil.parse("2016-08-31 18:02:00","yyyy-MM-dd HH:mm:ss"),new Date());
			System.out.print("dayNum======="+dayNum);
		} catch (ParseException e) {
			e.printStackTrace();
		}

//		Set<String> holidays = new HashSet<>();
//		holidays.add("20160416");
//		holidays.add("20160417");
//		Date d = DateUtil.reduceDay(DateUtil.now(),3,holidays);
//		System.out.println(d);
//		DateUtil.getWeekend(2017).forEach((date)->{
//			System.out.println(DateUtil.format(date,Formatter.yyyy_MM_dd));
//		});
//		try{
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			long now = System.currentTimeMillis();
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTimeInMillis(now);
//			System.out.println(now + " = " + formatter.format(calendar.getTime()));
//		// 日期转换为毫秒 两个日期想减得到天数
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String start="2011-09-20 12:30:45";
//			String end ="2011-10-20 6:30:00";
//		//得到毫秒数
//			long timeStart=sdf.parse(start).getTime();
//			long timeEnd =sdf.parse(end).getTime();
//		//两个日期想减得到天数
//			long dayCount= (timeEnd-timeStart)/(24*3600*1000);
//			System.out.println(dayCount);
//		}catch(Exception e){
//
//		}

	}

}
