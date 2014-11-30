package me.dmhacker.spamm;

import java.io.File;
import java.io.IOException;

import me.dmhacker.spamm.command.SpammCommand;
import me.dmhacker.spamm.handler.SpammHandler;
import me.dmhacker.spamm.handler.SpammProcessor;
import me.dmhacker.spamm.handler.SpammUniversalListener;
import me.dmhacker.spamm.util.Metrics;
import me.dmhacker.spamm.util.Updater;
import me.dmhacker.spamm.util.Updater.UpdateResult;
import me.dmhacker.spamm.util.exceptions.SpamCountRegistrationException;
import me.dmhacker.spamm.util.files.SpammLogFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("deprecation")
public class Spamm extends JavaPlugin {
	private static Spamm instance;
	
	private SpammLogFile logFile;
	private SpammHandler handler;
	private SpammProcessor processor;
	private SpammUniversalListener listener;
	private Metrics metrics;
	private boolean isUpdatable;
	
	@Override
	public void onEnable(){
		instance = this;
		handler = new SpammHandler();
		processor = new SpammProcessor();
		logFile = new SpammLogFile(new File(getDataFolder(), "spamlog.txt"));
		checkCount();
		doConfig();
		doListener();
		doCommands();
		metrics();
		if (shouldUpdate()) {
			getServer().getScheduler().runTaskLaterAsynchronously(getInstance(), new Runnable(){

				@Override
				public void run() {
					update();
				}
				
			}, 0);
		}
		else {
			getLogger().info("Did not search for an update.");
		}
		trackCurrent();
	}
	
	@Override
	public void onDisable(){
		handler.dump();
		instance = null;
	}
	
	public static Spamm getInstance(){
		return instance;
	}
	
	public SpammHandler getSpamHandler(){
		return handler;
	}
	
	public SpammProcessor getSpamProcessor(){
		return processor;
	}
	
	public SpammLogFile getSpamLog() {
		return logFile;
	}
	
	public File getJavaFile() {
		return this.getFile();
	}
	
	private void trackCurrent() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			Spamm.getInstance().getSpamHandler().track(p);
		}
	}
	
	private void update(){
		Updater updater = new Updater(this, 75425, this.getJavaFile(), Updater.UpdateType.NO_DOWNLOAD, true);
		if (updater.getResult() == UpdateResult.SUCCESS) {
			if (shouldDownload()) {
				new Updater(this, 75425, this.getJavaFile(), Updater.UpdateType.NO_VERSION_CHECK, true);
				getLogger().info("Automatically downloaded latest version: "+updater.getLatestName());
				this.isUpdatable = false;
			}
			else {
				getLogger().warning("An updated version was found: "+updater.getLatestName());
				getLogger().warning("To download it, use the command: /spamm update");
				this.isUpdatable = true;
			}
		}
		else {
			getLogger().severe("Ignored updater due to complications.");
			this.isUpdatable = false;
		}
	}
	
	public boolean isUpdatable(){
		return this.isUpdatable;
	}
	
	private void metrics(){
		if (shouldMetricize()) {
			try {
				this.metrics = new Metrics(this);
				this.metrics.start();
				getLogger().info("Initialized metrics.");
			} catch (IOException e) {
				getLogger().severe("Ignored metrics due to complications.");
			}
		}
		else {
			getLogger().info("Will not use metrics.");
		}
	}
	
	private void checkCount() {
		if (getWarningCount() >= getPunishingCount()) {
			throw new SpamCountRegistrationException(getWarningCount(), getPunishingCount());
		}
	}
	
	private void doConfig(){
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
	
	private void doListener(){
		listener = new SpammUniversalListener();
		this.getServer().getPluginManager().registerEvents(listener, this);
	}
	
	private void doCommands(){
		getCommand("spamm").setExecutor(new SpammCommand());
	}
	
	public int getDelay(){
		return getConfig().getInt("delay");
	}
	
	public int getCooldown(){
		return getConfig().getInt("cooldown");
	}
	
	public int getWarningCount(){
		return getConfig().getInt("count.warning");
	}
	
	public int getPunishingCount(){
		return getConfig().getInt("count.punishment");
	}
	
	public boolean shouldLog(){
		return getConfig().getBoolean("log");
	}
	
	public boolean shouldUpdate(){
		return getConfig().getBoolean("update");
	}
	
	public boolean shouldDownload(){
		return getConfig().getBoolean("download");
	}
	
	public boolean shouldDecapitalize() {
		return getConfig().getBoolean("misc.decapitalize");
	}
	
	private boolean shouldMetricize(){
		return getConfig().getBoolean("metrics");
	}
}
