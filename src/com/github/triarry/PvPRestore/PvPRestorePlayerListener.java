package com.github.triarry.PvPRestore;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class PvPRestorePlayerListener implements Listener {
	
	@SuppressWarnings("unused")
	private PvPRestore plugin;
	 
	public PvPRestorePlayerListener(PvPRestore plugin) { 
		this.plugin = plugin;
	}
	
	public HashMap<Player , ItemStack[]> items = new HashMap<Player , ItemStack[]>();
	public HashMap<Player , ItemStack[]> armor = new HashMap<Player , ItemStack[]>();

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = null;

		if (event.getEntity() instanceof Player) {
			player = event.getEntity();
		}

		Player killer = player.getKiller();
		if (player.hasPermission("pvprestore.keep")){
			if (killer != null) {
				event.setKeepLevel(true);
				player.sendMessage(ChatColor.YELLOW + "[PVP_Restore] " + ChatColor.GREEN  + "Your death was player related, so I've saved your XP and your inventory.");
				event.setDroppedExp(0);
				event.setDeathMessage(ChatColor.YELLOW + "[PVP_Restore] " + ChatColor.RED + player.getName() + ChatColor.GREEN + " was killed by " + ChatColor.RED + killer.getName() + ChatColor.GREEN + ", and their XP and inventory was saved!");
		        ItemStack[] content = player.getInventory().getContents();
		        ItemStack[] content_armor = player.getInventory().getArmorContents();
		        armor.put(player, content_armor);
		        items.put(player, content);
		        player.getInventory().clear();
		        event.getDrops().clear();
			} else {
				player.sendMessage(ChatColor.RED + "Your death was not player related, so I have dropped your inventory and XP where you died.");
			}
		} else {
			// do nothing
		}
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
        if(items.containsKey(event.getPlayer())){
            event.getPlayer().getInventory().clear();
            event.getPlayer().getInventory().setContents(items.get(event.getPlayer()));
            items.remove(event.getPlayer());
        }
        if(armor.containsKey(event.getPlayer()) && armor.size() != 0) {
            event.getPlayer().getInventory().setArmorContents(armor.get(event.getPlayer()));
            armor.remove(event.getPlayer());
        }
	}

}
