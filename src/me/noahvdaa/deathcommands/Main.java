package me.noahvdaa.deathcommands;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.noahvdaa.bstats.Metrics;

public class Main extends JavaPlugin implements Listener {

	public Logger logger;
	
	public void onEnable() {
		logger = getLogger();
		
		logger.info("Activating...");
		
		// Copy default config.
		getConfig().options().copyDefaults(true);
		saveDefaultConfig();
		
		// Enable Metrics
		new Metrics(this);
		
		// Register events
		getServer().getPluginManager().registerEvents(this, this);
		
		logger.info("Activated successfully!");
	}
	
	public void onDisable() {
		logger.info("Deactivating...");
		logger.info("Deactivated successfully!");
	}
	
	@EventHandler
	private void deathEvent(PlayerDeathEvent e) {
		Player p = e.getEntity();
		Location l = p.getLocation();
		ConsoleCommandSender console = getServer().getConsoleSender();
		// Check for bypass permission.
		if(!p.hasPermission("deathcommands.bypass")) {
			for (String command : getConfig().getStringList("deathcommands")) {
				command = command.replaceAll("%player%", p.getName())
			    .replaceAll("%death_x%", Math.round(l.getX())+"")
				.replaceAll("%death_y%", Math.round(l.getY())+"")
				.replaceAll("%death_z%", Math.round(l.getZ())+"");
				
				Bukkit.dispatchCommand(console, command);
			}
		}
	}
	
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("dcreload")) {
			if(sender.hasPermission("deathcommands.reload") || (sender instanceof ConsoleCommandSender)) {
				reloadConfig();
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("reloadedMessage")));
				return true;
			}else {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("noReloadPermsMessage")));
				return true;
			}
		}
		return false;
	}
}
