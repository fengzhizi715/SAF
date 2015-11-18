/**
 * 
 */
package cn.salesuite.saf.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Tony Shen
 *
 */
@Deprecated
public class DateHelper {

	private static String[] CONSTELLATIONS = new String[] { "白羊座", "金牛座", "双子座",
		"巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "魔羯座", "水瓶座", "双鱼座"};
	
	private static String currentTime;
	private static String currentDate;
	
	public static final long m_second=1000;
	public static final long m_minute=m_second*60;
	public static final long m_hour=m_minute*60;
	public static final long m_day=m_hour*24;
	
	/**
	 * 根据日期返回星座名
	 * 0白羊座：3月21日－4月20日
	 * 1金牛座：4月21日－5月21日
	 * 2双子座：5月22日－6月21日
	 * 3巨蟹座：6月22日－7月22日
	 * 4狮子座：7月23日－8月23日　　
	 * 5处女座：8月24日－9月23日　
	 * 6天秤座：9月24日－10月23日
	 * 7天蝎座：10月24日－11月22日
	 * 8射手座：11月23日－12月21日　
	 * 9魔羯座：12月22日－1月20日
	 * 10水瓶座：1月21日－2月19日　
	 * 11双鱼座：2月20日－3月20日
	 * @param month
	 * @param day
	 * @return
	 */
	public static String MonthDay2Constellation(int month, int day) {
		int ID = 0;
		switch (month) {
		case 1:
			if (day <= 20) {
				ID = 9;
			} else {
				ID = 10;
			}
			break;
		case 2:
			if (day <= 19) {
				ID = 10;
			} else {
				ID = 11;
			}
			break;
		case 3:
			if (day <= 20) {
				ID = 11;
			} else {
				ID = 0;
			}
			break;
		case 4:
			if (day <= 20) {
				ID = 0;
			} else {
				ID = 1;
			}
			break;
		case 5:
			if (day <= 21) {
				ID = 1;
			} else {
				ID = 2;
			}
			break;
		case 6:
			if (day <= 21) {
				ID = 2;
			} else {
				ID = 3;
			}
			break;
		case 7:
			if (day <= 22) {
				ID = 3;
			} else {
				ID = 4;
			}
			break;
		case 8:
			if (day <= 23) {
				ID = 4;
			} else {
				ID = 5;
			}
			break;
		case 9:
			if (day <= 23) {
				ID = 5;
			} else {
				ID = 6;
			}
			break;
		case 10:
			if (day <= 23) {
				ID = 6;
			} else {
				ID = 7;
			}
			break;
		case 11:
			if (day <= 22) {
				ID = 7;
			} else {
				ID = 8;
			}
			break;
		case 12:
			if (day <= 21) {
				ID = 8;
			} else {
				ID = 9;
			}
			break;
		default:
			break;
		}

		return CONSTELLATIONS[ID];
	}
	
	/**
	 * 得到当前的年份
	 * 返回格式:yyyy
	 * @return String
	 */
	public static String getCurrentYear() {
		java.util.Date NowDate = new java.util.Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		return formatter.format(NowDate);
	}
	
	/**
	 * 得到当前的月份
	 * 返回格式:MM
	 * @return String
	 */
	public static String getCurrentMonth() {
		java.util.Date NowDate = new java.util.Date();

		SimpleDateFormat formatter = new SimpleDateFormat("MM");
		return formatter.format(NowDate);
	}
	
	/**
	 * 得到当前的日期
	 * 返回格式:dd
	 * @return String
	 */
	public static String getCurrentDay() {
		java.util.Date NowDate = new java.util.Date();

		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		return formatter.format(NowDate);
	}
	
	/**
	 * 得到当前的时间，精确到毫秒,共14位
	 * 返回格式:yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getCurrentTime() {
		Date NowDate = new Date();
		SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentTime = formatter.format(NowDate);
		return currentTime;
	}
	
	/**
	 * 得到当前的日期,共10位
	 * 返回格式：yyyy-MM-dd
	 * @return String
	 */
	public static String getCurrentDate() {
		Date NowDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		currentDate = formatter.format(NowDate);
		return currentDate;
	}
	
	/**
	 * parse date using default pattern yyyy-MM-dd
	 * @param strDate
	 * @return
	 */
	public static final Date parseDate(String strDate){
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
	 * @param aDate
	 * @return formated date by yyyy-MM-dd
	 */
	public static final <T extends Date> String formatDate(T date) {
		if(date==null) return null;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(date);
	}
	
	/**
	 * @param aDate
	 * @param pattern: Date format pattern
	 * @return
	 */
	public static final <T extends Date> String format(T date, String pattern) {
		if(date==null) return null;
		try{
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			String result = df.format(date);
			return result;
		}catch(Exception e){
			return null;
		}
	}
	
	/**
	 * @param original
	 * @param days
	 * @param hours
	 * @param minutes
	 * @param seconds
	 * @param mill
	 * @return original+day+hour+minutes+seconds+millseconds
	 */
	public static final <T extends Date> T addTime(T original,int days, int hours, int minutes, int seconds){
		if(original==null) return null;
		long newTime=original.getTime()+m_day*days+m_hour*hours+m_minute*minutes+m_second*seconds;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	public static final <T extends Date> T addDay(T original,int days){
		if(original==null) return null;
		long newTime=original.getTime() + m_day * days;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	public static final <T extends Date> T addHour(T original, int hours){
		if(original==null) return null;
		long newTime=original.getTime()+m_hour*hours;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	public static final <T extends Date> T addMinute(T original, int minutes){
		if(original==null) return null;
		long newTime=original.getTime() + m_minute*minutes;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	public static final <T extends Date> T addSecond(T original, int second){
		if(original==null) return null;
		long newTime=original.getTime() + m_second*second;
		T another = (T) original.clone();
		another.setTime(newTime);
		return another;
	}
	
	public static Date addMonth(Date original, int month){
		if(original==null) return null;
		Calendar calender = Calendar.getInstance();
        calender.setTime(original);
        calender.add(Calendar.MONTH, month);
		return calender.getTime();
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
		String dateStr = format(date, "yyyy-MM") + "-01";
		return parseDate(dateStr);
	}
	/**
	 * 获取日期所在月份的最后一天
	 * @param days
	 * @return
	 */
	public static Date getMonthLastDay(Date date){
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
    	Calendar cal=Calendar.getInstance();
    	cal.setTime(parseDate(strDate));
    	return cal;
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
	 * 如果比较分:包含以上值，是包含关系
	 */
	public static boolean compareIsBefore(Date src, Date dest,int unit){
		if(src == null || dest == null){
			return false;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHss");
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
			default:
				result = false;
		}
		
		return result;
	}
	
    public static String getWeek(Date date){
        String[] weeks = {"周日","周一","周二","周三","周四","周五","周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if(week_index<0){
            week_index = 0;
        }
        return weeks[week_index];
    }
}
