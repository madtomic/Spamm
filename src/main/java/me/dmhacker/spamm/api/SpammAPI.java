package me.dmhacker.spamm.api;

import me.dmhacker.spamm.Spamm;
import me.dmhacker.spamm.api.punishments.SpammPunishment;

public class SpammAPI {

	/**
	 * Loads an externally-created punishment
	 * 
	 * @param punishment The punishment to load
	 */
	public static void loadPunishment(SpammPunishment punishment) {
		Spamm.getInstance().getSpamProcessor().loadExternally(punishment);
	}
	
	/**
	 * Removes any punishment
	 * 
	 * @param punishment The punishment to unload
	 */
	public static void unloadPunishment(SpammPunishment punishment) {
		Spamm.getInstance().getSpamProcessor().unloadExternally(punishment);
	}
	
}
