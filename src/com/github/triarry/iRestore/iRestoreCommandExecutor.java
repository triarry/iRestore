package com.github.triarry.iRestore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class iRestoreCommandExecutor implements CommandExecutor {

	@SuppressWarnings("unused")
	private iRestore plugin;
	  
	public iRestoreCommandExecutor(iRestore plugin){ 
		this.plugin = plugin; 
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("irestore")) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "#####");
<<<<<<< HEAD:src/com/github/triarry/iRestore/iRestoreCommandExecutor.java
			sender.sendMessage(ChatColor.GREEN + "Currently running iRestore version 1.4.6");
=======
			sender.sendMessage(ChatColor.GREEN + "Currently running PvP Restore version 1.4.7");
>>>>>>> b1f49ede88137430d3c961c5de3c11175d84dbb9:src/com/github/triarry/PvPRestore/PvPRestoreCommandExecutor.java
			sender.sendMessage(ChatColor.GREEN + "Plugin made by triarry");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "#####");
			return true;
		}
		return true;
	}
}