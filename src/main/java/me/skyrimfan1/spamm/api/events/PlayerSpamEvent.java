package me.skyrimfan1.spamm.api.events;

import me.skyrimfan1.spamm.util.SpammLevel;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class PlayerSpamEvent extends PlayerEvent {

	private static HandlerList handlers = new HandlerList();
	private int count;
	private SpammLevel level;
	
	/**
	 * Called when a player spams (either at level WARNING or PUNISHING)
	 * 
	 * @param who The spammer
	 * @param spamCount Their current spam count (messages in a row)
	 * @param level Their current level (either at level WARNING or PUNISHING)
	 */
	public PlayerSpamEvent(Player who, int spamCount, SpammLevel level) {
		super(who);
		this.count = spamCount;
		this.level = level;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
	public int getSpamCount(){
		return count;
	}
	
	public SpammLevel getLevel(){
		return level;
	}

}
