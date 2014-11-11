/**
 * 
 */
package cn.salesuite.saf.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

/**
 * 使用Joda封装的日期帮助类，替换原先的DateHelper
 * @author Tony Shen
 *
 */
public class JodaUtils {
	
	/**
	 * 得到当前的年份
	 * 返回格式:yyyy
	 * @return String
	 */
	public static String getCurrentYear() {
		return new DateTime().toString("yyyy");
	}
	
	/**
	 * 得到当前的月份
	 * 返回格式:MM
	 * @return String
	 */
	public static String getCurrentMonth() {
		return new DateTime().toString("MM");
	}
	
	/**
	 * 得到当前的日期
	 * 返回格式:dd
	 * @return String
	 */
	public static String getCurrentDay() {
		return new DateTime().toString("dd");
	}
	
	/**
	 * 得到当前的小时
	 * 返回格式:hh
	 * @return String
	 */
	public static String getCurrentHour() {
		return new DateTime().toString("HH");
	}
	
	/**
	 * 得到当前的分钟
	 * 返回格式:mm
	 * @return String
	 */
	public static String getCurrentMinute() {
		return new DateTime().toString("mm");
	}
	
	/**
	 * 得到当前的秒
	 * 返回格式:ss
	 * @return String
	 */
	public static String getCurrentSecond() {
		return new DateTime().toString("ss");
	}

	/**
	 * 得到当前的时间，精确到毫秒,共14位
	 * 返回格式:yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getCurrentTime() {
		return new DateTime().toString("yyyy-MM-dd HH:mm:ss");
	}
	
	/**
	 * 得到当前的日期,共10位
	 * 返回格式：yyyy-MM-dd
	 * @return String
	 */
	public static String getCurrentDate() {
		return new DateTime().toString("yyyy-MM-dd");
	}
	
	/**
	 * parse date using default pattern yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static Date parseDate(String strDate){
		Date date = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			date = dateFormat.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}
	
	/**
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static final Date parseDate(String strDate, String pattern){
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(pattern);
		try {
			date = df.parse(strDate);
			return date;
		} catch (Exception pe) {
			return null;
		}
	}
	
	/**
	 * @param date
	 * @return formated date by yyyy-MM-dd
	 */
	public static final String formatDate(Date date) {
		return format(date,"yyyy-MM-dd");
	}
	
		
	/**
	 * @param date
	 * @return formated date by yyyy-MM-dd HH:mm:ss
	 */
	public static final String formatDateTime(Date date) {
		return format(date,"yyyy-MM-dd HH:mm:ss");
	}
    
	/**
	 * @param date
	 * @param pattern: Date format pattern
	 * @return
	 */
	public static final String format(Date date, String pattern) {
		if(date==null) return null;
		
		return new DateTime(date).toString(pattern);
	}
	
	/**
	 * @param original
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @return original+day+hour+minutes+seconds
	 */
	public static final Date addTime(Date original,int days, int hours, int minutes, int seconds){
		if(original==null) return null;
		
		return new DateTime(original).plusDays(days).plusHours(hours).plusMinutes(minutes).plusSeconds(seconds).toDate();
	}
	
	public static Date addYear(Date original, int years){
		if(original==null) return null;
		
		return new DateTime(original).plusYears(years).toDate();
	}
	
	public static Date addMonth(Date original, int months){
		if(original==null) return null;
		
		return new DateTime(original).plusMonths(months).toDate();
	}
	
	public static Date addWeek(Date original, int weeks){
		if(original==null) return null;
		
		return new DateTime(original).plusWeeks(weeks).toDate();
	}
	
	public static final Date addDay(Date original,int days){
		if(original==null) return null;
		
		return new DateTime(original).plusDays(days).toDate();
	}
	
	public static final Date addHour(Date original, int hours){
		if(original==null) return null;

		return new DateTime(original).plusHours(hours).toDate();
	}
	
	public static final Date addMinute(Date original, int minutes){
		if(original==null) return null;
		
		return new DateTime(original).plusMinutes(minutes).toDate();
	}
	
	public static final Date addSecond(Date original, int second){
		if(original==null) return null;
		
		return new DateTime(original).plusSeconds(second).toDate();
	}
	
	public static boolean isTomorrow(Date date) {
		if(date==null) return false;
		
		if(formatDate(addTime(new Date(), 1, 0, 0, 0)).equals(formatDate(date))) return true;
		return false;
	}
	
	/**
	 * 获取日期所在月份的第一天
	 * @param date
	 * @return
	 */
	public static Date getMonthFirstDay(Date date) {
		if(date==null) return null;
		
		String dateStr = format(date, "yyyy-MM") + "-01";
		return parseDate(dateStr);
	}
	
	/**
	 * 获取日期所在月份的最后一天
	 * @param days
	 * @return
	 */
	public static Date getMonthLastDay(Date date){
		if(date==null) return null;
		
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String dateStr = format(date, "yyyy-MM") + "-" + c.getActualMaximum(Calendar.DAY_OF_MONTH);
		return parseDate(dateStr);
	}
	
	public static String getDateDesc(Date time) {
		if(time==null) return "";
		String timeContent;
		Long ss = System.currentTimeMillis()-time.getTime();
		Long minute = ss/60000;
		if (minute<1) minute=1L;
		if(minute>=60){
			Long hour = minute/60;
			if(hour>=24){
				if(hour>720)timeContent= "1月前";
				else if(hour>168 && hour<=720) timeContent= (hour/168)+"周前";
				else timeContent = (hour/24)+"天前";
			}else{
				timeContent =  hour+"小时前";
			}
		}else{
			timeContent = minute+"分钟前";
		}
		return timeContent;
	}
	
	/**
	 * 判断是否为闰年
	 * @param year
	 * @return
	 */
    public static boolean isLeapYear(int year) {
        if(year % 100==0) {
            return year % 400==0;
        }
        return year % 4==0;
    }
    
    /**
     * 日期字符串转换成Calendar
     * @param strDate
     * @return
     */
    public static Calendar string2Calendar(String strDate) {
    	return new DateTime(strDate).toCalendar(Locale.CHINA);
    }
    
    /**
	 * 比较src 是否在 dest 之前,true 代表src 小于dest 日期
	 * @param src 源日期
	 * @param dest 目标日期
	 * @param unit 单位 
	 * 0：年
	 * 1：月
	 * 2：日
	 * 3：时
	 * 4：分
	 * 5：秒
	 * 如果比较分:包含以上值，是包含关系
	 */
	public static boolean compareIsBefore(Date src, Date dest,int unit){
		if(src == null || dest == null){
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		String srcStr = format.format(src);
		String destStr = format.format(dest);
		boolean result = false;
		switch(unit){
			case 0:
				if(Long.parseLong(srcStr.substring(0,4)) < Long.parseLong(destStr.substring(0,4))){
					result = true;
				}
				break;
			case 1:
				if(Long.parseLong(srcStr.substring(0,6)) < Long.parseLong(destStr.substring(0,6))){
					result = true;
				}
				break;
			case 2:
				if(Long.parseLong(srcStr.substring(0,8)) < Long.parseLong(destStr.substring(0,8))){
					result = true;
				}
				break;
			case 3:
				if(Long.parseLong(srcStr.substring(0,10)) < Long.parseLong(destStr.substring(0,10))){
					result = true;
				}
				break;
			case 4:
				if(Long.parseLong(srcStr.substring(0,12)) < Long.parseLong(destStr.substring(0,12))){
					result = true;
				}
				break;
			case 5:
				if(Long.parseLong(srcStr.substring(0,14)) < Long.parseLong(destStr.substring(0,14))){
					result = true;
				}
				break;
			default:
				result = false;
		}
		
		return result;
	}
	
	/**
	 * 返回某一天是星期几
	 * @param date
	 * @return
	 */
    public static String getWeek(Date date){    	
    	return new DateTime(date).dayOfWeek().getAsText();
    }
    
	/**
	 * 将时间戳转换成日期字符串
	 * @param timestamp
	 * @return
	 */
	public static String timestamp2String(Long timestamp) {
		return JodaUtils.formatDate(new Date(timestamp));
	}
    
    /**
     * 将时间戳转换成日期字符串
     * @param timestamp
     * @param pattern
     * @return
     */
    public static String timestamp2String(Long timestamp,String pattern) {
        return JodaUtils.format(new Date(timestamp),pattern);
    }
}
