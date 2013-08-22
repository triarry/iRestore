package com.github.triarry.iRestore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.github.triarry.iRestore.iRestore;

public class InfoCommand implements CommandExecutor {

	@SuppressWarnings("unused")
	private iRestore plugin;
	  
	public InfoCommand(iRestore plugin){ 
		this.plugin = plugin; 
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("irestore")) {
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "#####");
			sender.sendMessage(ChatColor.GREEN + "Currently running iRestore version 1.5.1");
			sender.sendMessage(ChatColor.GREEN + "Plugin made by triarry");
			sender.sendMessage(ChatColor.LIGHT_PURPLE + "#####");
			return true;
		}
		
		else {
			return true;
		}
	}
}