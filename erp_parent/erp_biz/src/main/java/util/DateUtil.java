package util;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * @author Administrator
 *
 */
public class DateUtil {
	
	/**
	 * 获取当前年份
	 * @return
	 */
	public static int getYear(){
		
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		return  calendar.get(Calendar.YEAR);
	}

}
