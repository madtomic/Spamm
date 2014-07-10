package me.dmhacker.spamm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.ChatColor;

public class SpammMessaging {

	public static String getPrefix(){
		return ChatColor.RED+"["+ChatColor.YELLOW+"Spamm"+ChatColor.RED+"] "+ChatColor.RESET;
	}
	
	public static String getDate(){
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return "["+format.format(date)+"] ";
	}
}
