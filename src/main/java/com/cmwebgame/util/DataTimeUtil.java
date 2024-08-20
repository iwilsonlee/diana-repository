package com.cmwebgame.util;

import org.joda.time.DateTime;

import com.google.common.base.Strings;

public class DataTimeUtil {
	
	/**
	 * 获取当前月份
	 * @return
	 */
	public static String getThisMonth(){
		DateTime dateTime = DateTime.now();
		int month = dateTime.getMonthOfYear();
		String m = month<10?("0"+month):(""+month);
		String thisMonth = dateTime.getYear() + "-" + m;
		return thisMonth;
	}
	
	/**
	 * 根据指定月份获取上一月份
	 * @param month
	 * @return
	 */
	public static String getPreviousMonth(String month){
		DateTime dateTime = DateTime.parse(month);
		dateTime = dateTime.minusMonths(1);
		int monthInt = dateTime.getMonthOfYear();
		String m = monthInt<10?("0"+monthInt):(""+monthInt);
		String previousMonth = dateTime.getYear() + "-"
				+ m;
		return previousMonth;
	}
	/**
	 * 某日期是否在指定月份之前
	 * @param entryDate 
	 * @param month
	 * @return 
	 */
	public static boolean containDate(String entryDate, String month){
		if (Strings.isNullOrEmpty(entryDate) || Strings.isNullOrEmpty(month)) {
			return false;
		}
		DateTime entryDateTime = DateTime.parse(entryDate);
		DateTime dateTime = DateTime.parse(month).dayOfMonth().withMaximumValue();
		long dateTimelong = dateTime.getMillis();
		return entryDateTime.isBefore(dateTimelong) || entryDateTime.isEqual(dateTimelong);
	}

}
