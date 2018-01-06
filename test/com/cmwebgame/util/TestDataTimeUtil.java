package com.cmwebgame.util;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.junit.Test;

public class TestDataTimeUtil {

	//@Test
	public void testContainDate() {
		String entryDate = "2015-3-31";
		String month = "2015-3";
		boolean result = DataTimeUtil.containDate(entryDate, month);
		System.out.println(result);
		assertTrue(result);
	}
	
	@Test
	public void testMaxMonthDay(){
		String month = "2015-3";
		DateTime dateTime = DateTime.parse(month).dayOfMonth().withMaximumValue();
		System.out.println(dateTime.toString("yyyy-MM-dd"));
	}

}
