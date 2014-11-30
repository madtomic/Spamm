package me.dmhacker.spamm.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.api.events.PlayerSpamEvent;
import me.dmhacker.spamm.util.SpammLevel;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SpammUniversalListener implements Listener {
	private Spamm spamm = Spamm.getInstance();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(final AsyncPlayerChatEvent event) {
		if (event.getPlayer().hasPermission("spamm.exempt")) {
			return;
		}
		String msg = event.getMessage();
		if (spamm.shouldDecapitalize()) {
			int capitalLetters = 0;
			for (char c : msg.toCharArray()) {
				if (Character.isUpperCase(c))
					capitalLetters += 1;
			}
			double half = msg.length() / 2;
			if (capitalLetters >= half) {
				String precedent = msg.substring(0, 1);
				ChatColor color = ChatColor.RESET;
				if (precedent.equals("§")) {
					color = ChatColor.getByChar(msg.substring(1, 2));
				}
				String noColor = ChatColor.stripColor(msg);
				String newMsg = color + noColor.substring(0, 1).toUpperCase() + noColor.substring(1).toLowerCase();
				event.setMessage(newMsg);
			}
		}
		
		Callable<Boolean> cancel = new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				SpammLevel level = spamm.getSpamHandler().log(event.getPlayer(), event.getMessage());
				if (level == SpammLevel.WARNING) {
					event.getPlayer().sendMessage(ChatColor.RED+"Message ["+ChatColor.DARK_RED+spamm.getSpamHandler().getTracker(event.getPlayer()).getLastMessage()+ChatColor.RED+"] spammed "+spamm.getSpamHandler().getTracker(event.getPlayer()).getCount()+" times.");
					event.setMessage(ChatColor.RESET+""+ChatColor.STRIKETHROUGH+ChatColor.stripColor(event.getMessage()));
					PlayerSpamEvent spamEvent = new PlayerSpamEvent(event.getPlayer(), spamm.getSpamHandler().getTracker(event.getPlayer()).getCount(), level, event.getMessage());
					Bukkit.getPluginManager().callEvent(spamEvent);
					return false;
				}
				else if (level == SpammLevel.PUNISHING) {
					spamm.getSpamProcessor().assess(event.getPlayer(), level);
					event.getPlayer().sendMessage(ChatColor.DARK_RED+"You have been muted for persistent spamming.");
					PlayerSpamEvent spamEvent = new PlayerSpamEvent(event.getPlayer(), spamm.getSpamHandler().getTracker(event.getPlayer()).getCount(), level, event.getMessage());
					Bukkit.getPluginManager().callEvent(spamEvent);
					return true;
				}
				return false;
			}
			
		};
		
		Future<Boolean> cancelled = Bukkit.getScheduler().callSyncMethod(spamm, cancel);
		try {
			if (cancelled.get()) {
				event.setCancelled(true);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		spamm.getSpamHandler().track(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		spamm.getSpamHandler().pause(event.getPlayer());
	}
}
