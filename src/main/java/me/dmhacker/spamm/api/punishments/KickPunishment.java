package me.dmhacker.spamm.api.punishments;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class KickPunishment implements SpammPunishment {

	@Override
	public void execute(Player player) {
		player.kickPlayer(ChatColor.DARK_RED+"KICKED: "+ChatColor.RED+"Spamming!");
	}
}
