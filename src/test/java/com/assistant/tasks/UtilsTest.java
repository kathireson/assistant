package com.assistant.tasks;

import java.text.ParseException;
import java.util.Date;

import org.testng.Assert;
import org.testng.annotations.Test;

public class UtilsTest {

	@Test
	public void DateFormatTest(){
		Date date = new Date(1604903784383L);		
		String strDate = Utils.formatDate(date);
		Assert.assertEquals(strDate, "2020-11-08");
	}
	
	@Test
	public void DateParserTest() throws ParseException{
		String strDate = "2020-11-08";
		Date date = Utils.parseDate(strDate);
		Assert.assertEquals(date.getTime(), 1604822400000L);
	}
}
