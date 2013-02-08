package com.github.triarry.PvPRestore.utilities;

import org.bukkit.entity.Player;

import com.github.triarry.PvPRestore.PvPRestore;

public class Utilities {
	
	private PvPRestore plugin;
	static Utilities instance = new Utilities();
	
    public static Utilities getUtilities() {
        return instance;
    }
  
    public void startUp(PvPRestore plug) {
        plugin = plug;
    }
    
	public void blacklistItems(Player p) {
		if (p.hasPermission("pvprestore.blacklist.drop") && plugin.getConfig().getBoolean("blacklist.enabled") == true) {
			for (Integer itemList : plugin.getConfig().getIntegerList("blacklist.items")) {
				p.getInventory().remove(itemList);
				if(p.getInventory().getHelmet() != null) {
				    if(p.getInventory().getHelmet().getTypeId() == itemList) {
				    	p.getInventory().setHelmet(null);
				    }
				}
				if(p.getInventory().getChestplate() != null) {
				    if(p.getInventory().getChestplate().getTypeId() == itemList) {
				    	p.getInventory().setChestplate(null);
				    }
				}
				if(p.getInventory().getLeggings() != null) {
				    if(p.getInventory().getLeggings().getTypeId() == itemList) {
				    	p.getInventory().setLeggings(null);
				    }
				}
				if(p.getInventory().getBoots() != null) {
				    if(p.getInventory().getBoots().getTypeId() == itemList) {
				    	p.getInventory().setBoots(null);
				    }
				}
			} 
		}
	}
}