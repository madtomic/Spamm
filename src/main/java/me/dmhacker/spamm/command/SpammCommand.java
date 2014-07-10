package me.dmhacker.spamm.command;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.util.SpammMessaging;
import me.dmhacker.spamm.util.Updater;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SpammCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("spamm")) {
			int argslength = args.length;
			if (argslength == 0) {
				sender.sendMessage(ChatColor.WHITE+""+ChatColor.STRIKETHROUGH+"          "+ChatColor.RESET+" "+SpammMessaging.getPrefix()+ChatColor.WHITE+""+ChatColor.STRIKETHROUGH+"          ");
				sender.sendMessage(ChatColor.GOLD+"Goal: "+ChatColor.GRAY+"Lightweight spam-catching plugin");
				sender.sendMessage(ChatColor.GOLD+"Version: "+ChatColor.GRAY+Spamm.getInstance().getDescription().getVersion());
				sender.sendMessage(ChatColor.GOLD+"Authors: "+ChatColor.GRAY+Spamm.getInstance().getDescription().getAuthors());
				if (sender.hasPermission("spamm.update")) {
					if (Spamm.getInstance().isUpdatable()) {
						sender.sendMessage(ChatColor.YELLOW+"A new version of Spamm is out!");
					}
				}
			}
			else if (argslength == 1) {
				if (args[0].equalsIgnoreCase("update")) {
					if (sender.hasPermission("spamm.update")) {
						if (Spamm.getInstance().isUpdatable()) {
							Updater updater = new Updater(Spamm.getInstance(), 75425, Spamm.getInstance().getJavaFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
							Spamm.getInstance().log.info("[Spamm] Manually downloaded latest version: "+updater.getLatestName());
							sender.sendMessage(ChatColor.GREEN+"Downloading Spamm v"+updater.getLatestGameVersion()+".");
						}
						else {
							sender.sendMessage(ChatColor.GREEN+"No updates found.");
						}
					}
					else {
						sender.sendMessage(ChatColor.RED+"Try: "+ChatColor.YELLOW+"/spamm "+ChatColor.RED+" instead");
					}
				}
				else {
					if (sender.hasPermission("spamm.update")) {
						sender.sendMessage(ChatColor.RED+"Try: "+ChatColor.YELLOW+"/spamm (update)"+ChatColor.RED+" instead");
					}
					else {
						sender.sendMessage(ChatColor.RED+"Try: "+ChatColor.YELLOW+"/spamm "+ChatColor.RED+" instead");
					}
				}
			}
			else {
				if (sender.hasPermission("spamm.update")) {
					sender.sendMessage(ChatColor.RED+"Try: "+ChatColor.YELLOW+"/spamm (update)"+ChatColor.RED+" instead");
				}
				else {
					sender.sendMessage(ChatColor.RED+"Try: "+ChatColor.YELLOW+"/spamm "+ChatColor.RED+" instead");
				}
			}
		}
		return false;
	}

}
