package me.dmhacker.spamm.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.dmhacker.spamm.util.SpammLevel;

import org.bukkit.entity.Player;

public class SpammHandler {
	private Map<UUID, SpammTracker> trackers = new HashMap<UUID, SpammTracker>();
	
	public void track(Player player) {
		if (isTracking(player)) {
			trackers.get(player.getUniqueId()).setPaused(false);
			return;
		}
		trackOverride(player);
	}
	
	private void trackOverride(Player player) {
		trackers.put(player.getUniqueId(), new SpammTracker(player));
	}
	
	public void pause(Player player) {
		trackers.get(player.getUniqueId()).setPaused(true);
	}
	
	public void dump(){
		trackers.clear();
	}
	
	public SpammLevel log(Player player, String message) {
		SpammTracker tracker = getTracker(player);
		SpammLevel level = tracker.logMessage(message).getLevel();
		return level;
	}
	
	public boolean isTracking(Player player) {
		return trackers.containsKey(player.getUniqueId());
	}
	
	public SpammTracker getTracker(Player player) {
		return trackers.get(player.getUniqueId());
	}
}
