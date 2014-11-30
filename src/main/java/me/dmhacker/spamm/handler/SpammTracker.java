package me.dmhacker.spamm.handler;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.util.SpammLevel;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class SpammTracker {
	private Spamm spamm = Spamm.getInstance();
	private Player player;
	private String lastMessage;
	private int count;
	private boolean isAccepting;
	private boolean paused;
	private SpammLevel previousLevel;
	private SpammLevel level;
	private BukkitTask task; 
	
	public SpammTracker(Player player) {
		this.player = player;
		this.isAccepting = true;
		this.count = 0;
		this.previousLevel = SpammLevel.PERMITTED;
		this.level = SpammLevel.PERMITTED;
	}
	
	/**
	 * Returns the player whom the tracker is wrapped around
	 * 
	 * @return The player associated with the tracker
	 */
	public Player getPlayer(){
		return player;
	}
	
	/**
	 * Logs a message
	 * Usually only Spamm handles this but can be externally used
	 * 
	 * @param last The message a player chatted
	 * @return The tracker instance
	 */
	public SpammTracker logMessage(String last) {
		
		if (task != null)
			task.cancel();
		if (!isAccepting)
			count += 1;
		
		previousLevel = level;
		level = count >= spamm.getWarningCount() && count < spamm.getPunishingCount() ? SpammLevel.WARNING : SpammLevel.PERMITTED;
		level = count >= spamm.getPunishingCount() ? SpammLevel.PUNISHING : level;
		if (previousLevel == SpammLevel.PUNISHING && level != SpammLevel.PUNISHING) {
			level = SpammLevel.PERMITTED;
			count = 0;
		}
		lastMessage = last;
		isAccepting = false;
		task = new BukkitRunnable() {

			@Override
			public void run() {
				if (!paused) {
					isAccepting = true;
					if (count > 0) {
						count -= 1;
					}
				}
			}
			
		}.runTaskTimerAsynchronously(spamm, spamm.getDelay(), spamm.getCooldown());
		return this;
	}
	
	public void setPaused(boolean pause) {
		this.paused = pause;
	}
	
	/**
	 * Returns what the player last said in chat.
	 * The message is overrided with the {@link #logMessage(String)} method.
	 * 
	 * @return The player's last said message
	 */
	public String getLastMessage() {
		return lastMessage;
	}
	
	/**
	 * Returns the player's current spam level
	 * The spam level is determined synchronously which makes it thread-safe.
	 * 
	 * @return The player's SpammLevel
	 */
	public synchronized SpammLevel getLevel() {
		return level;
	}
	
	/**
	 * Returns an integer representing the amount of times a player has spammed
	 * The integer is accessed asynchronously consistently
	 * Thus, it is synchronized to prevent thread conflicts
	 * 
	 * @return How many times the player has spammed consecutively
	 */
	public synchronized int getCount(){
		return count;
	}
}
