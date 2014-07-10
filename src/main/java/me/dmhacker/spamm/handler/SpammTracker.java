package me.dmhacker.spamm.handler;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.util.SpammLevel;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class SpammTracker {
	private Player player;
	private String lastMessage;
	private int count;
	private boolean isAccepting;
	private SpammLevel level;
	private BukkitTask task; 
	
	public SpammTracker(Player player) {
		this.player = player;
		this.isAccepting = true;
		this.count = 0;
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
	 * @return Their current SpammLevel with the latest string inputted
	 */
	public SpammLevel logMessage(String last) {
		if (task != null) task.cancel();
		if (isAccepting == false) count += 1;
		
		this.level = count >= Spamm.getInstance().getWarningCount() && count < Spamm.getInstance().getPunishingCount() ? SpammLevel.WARNING : SpammLevel.PERMITTED;
		this.level = count >= Spamm.getInstance().getPunishingCount() ? SpammLevel.PUNISHING : level;
		this.lastMessage = last;
		isAccepting = false;
		task = Spamm.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Spamm.getInstance(), new Runnable(){

			@Override
			public void run() {
				isAccepting = true;
				if (count > 0) {
					count -= 1;
				}
			}
			
		}, Spamm.getInstance().getDelay(), Spamm.getInstance().getCooldown());
		return level;
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
	public SpammLevel getLevel() {
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
