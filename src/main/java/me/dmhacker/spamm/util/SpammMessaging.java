package me.dmhacker.spamm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SpammMessaging {
	
	public static String getDate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return "["+format.format(date)+"] ";
	}
}
