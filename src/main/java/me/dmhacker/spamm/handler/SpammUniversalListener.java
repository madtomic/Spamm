package me.dmhacker.spamm.handler;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.api.events.PlayerSpamEvent;
import me.dmhacker.spamm.util.SpammLevel;
import me.dmhacker.spamm.util.SpammMessaging;
import me.dmhacker.spamm.util.exceptions.AsyncCallableException;
import me.dmhacker.spamm.util.exceptions.NoTrackerFoundException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SpammUniversalListener implements Listener {

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(final AsyncPlayerChatEvent event) {
		if (event.getPlayer().hasPermission("spamm.exempt")) {
			return;
		}
		if (event.isAsynchronous()) {
			String msg = event.getMessage();
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
			Future<Object> future = Spamm.getInstance().getServer().getScheduler().callSyncMethod(Spamm.getInstance(), new Callable<Object>(){

				@Override
				public SpammLevel call() {
					SpammLevel level = Spamm.getInstance().getSpamHandler().log(event.getPlayer(), event.getMessage());
					return level;
				}
				
			});
			try {
				SpammLevel level = (SpammLevel) future.get();
				if (level == SpammLevel.WARNING) {
					event.getPlayer().sendMessage(SpammMessaging.getPrefix()+ChatColor.RED+"Message ("+Spamm.getInstance().getSpamHandler().getTracker(event.getPlayer()).getLastMessage()+ChatColor.RED+") spammed: "+ChatColor.DARK_GREEN+Spamm.getInstance().getSpamHandler().getTracker(event.getPlayer()).getCount()+" times");
					event.setMessage(ChatColor.RESET+""+ChatColor.STRIKETHROUGH+ChatColor.stripColor(event.getMessage()));
					PlayerSpamEvent spamEvent = new PlayerSpamEvent(event.getPlayer(), Spamm.getInstance().getSpamHandler().getTracker(event.getPlayer()).getCount(), level, event.getMessage());
					Bukkit.getPluginManager().callEvent(spamEvent);
				}
				else if (level == SpammLevel.PUNISHING) {
					Spamm.getInstance().getSpamProcessor().assess(event.getPlayer(), level);
					event.getPlayer().sendMessage(SpammMessaging.getPrefix()+ChatColor.DARK_RED+"Muted for persistent spamming.");
					event.setCancelled(true);
					PlayerSpamEvent spamEvent = new PlayerSpamEvent(event.getPlayer(), Spamm.getInstance().getSpamHandler().getTracker(event.getPlayer()).getCount(), level, event.getMessage());
					Bukkit.getPluginManager().callEvent(spamEvent);
				}
			} catch (NoTrackerFoundException ex) {
				if (event.getPlayer().isOnline()) {
					Spamm.getInstance().log.severe("Unable to locate "+event.getPlayer().getName()+"'s tracker.");
				}
			} catch (Exception e) {
				throw new AsyncCallableException(e);
			}

		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Spamm.getInstance().getSpamHandler().track(event.getPlayer());
	}
}
