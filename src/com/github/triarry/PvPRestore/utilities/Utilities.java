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
	//if (!p.hasPermission("pvprestore.blacklist.override"))
	public void blacklistItems(Player p) {
		for (Integer itemList : plugin.getConfig().getIntegerList("blacklist.items")) {
			p.getInventory().remove(itemList);
		} 
	}
}
