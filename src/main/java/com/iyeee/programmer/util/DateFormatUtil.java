package com.iyeee.programmer.util;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {

	public static String getFormatDate(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	public static Date getFormatDate(String string, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(string);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}
	@Test
	public void Test(){
		Date formatDate = DateFormatUtil.getFormatDate("2020-06-30", "yyyy-MM-dd");

	}
}
