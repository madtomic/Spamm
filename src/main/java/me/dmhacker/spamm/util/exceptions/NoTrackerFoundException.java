package me.dmhacker.spamm.util.exceptions;

import org.bukkit.entity.Player;

public class NoTrackerFoundException extends Exception {
	private static final long serialVersionUID = 1L;
	public String mishap;
	public Player player;
	
	public NoTrackerFoundException(Player player) {
		super("Unable to locate "+player.getName()+"'s tracker.");
		this.player = player;
		this.mishap = "Unable to locate "+player.getName()+"'s tracker.";
	}
}
