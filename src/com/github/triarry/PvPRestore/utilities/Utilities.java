package com.github.triarry.PvPRestore.utilities;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
		if (plugin.getConfig().getBoolean("blacklist.enabled") == true) {
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
	
	public void whitelistItems(Player p) {
		Boolean itemCheck = false;
		if (plugin.getConfig().getBoolean("whitelist.enabled") == true) {
			for (ItemStack stackList : p.getInventory().getContents()) {
				for (Integer itemList : plugin.getConfig().getIntegerList("whitelist.items")) {
					if (stackList != null) {
						if (stackList.getTypeId() == itemList){
							itemCheck = true;
						}
					}
				}
				if (itemCheck != true) {
					if (stackList != null) {
						p.getInventory().remove(stackList);
					}
				}
				else {
					itemCheck = false;
				}
			} 
		}
	}
	
	public void whitelistArmor(Player p) {
		Boolean helmetCheck = false;
		Boolean chestplateCheck = false;
		Boolean leggingsCheck = false;
		Boolean bootsCheck = false;
		if (plugin.getConfig().getBoolean("whitelist.enabled") == true) {
			for (ItemStack stackList : p.getInventory().getArmorContents()) {
				for (Integer itemList : plugin.getConfig().getIntegerList("whitelist.items")) {
					if (stackList != null) {
						if (stackList.getTypeId() == itemList){
							if(p.getInventory().getHelmet() != null && helmetCheck == false) {
							    if(p.getInventory().getHelmet().getTypeId() == itemList) {
							    	p.getInventory().setHelmet(stackList);
							    	helmetCheck = true;
							    }
							}
							if(p.getInventory().getChestplate() != null && chestplateCheck == false) {
							    if(p.getInventory().getChestplate().getTypeId() == itemList) {
							    	p.getInventory().setChestplate(stackList);
							    	chestplateCheck = true;
							    }
							}
							if(p.getInventory().getLeggings() != null && leggingsCheck == false) {
							    if(p.getInventory().getLeggings().getTypeId() == itemList) {
							    	p.getInventory().setLeggings(stackList);
							    	leggingsCheck = true;
							    }
							}
							if(p.getInventory().getBoots() != null && bootsCheck == false) {
							    if(p.getInventory().getBoots().getTypeId() == itemList) {
							    	p.getInventory().setBoots(stackList);
							    	bootsCheck = true;
							    }
							}
						}
					}
				}
			}
			if (helmetCheck == false) {
				p.getInventory().setHelmet(null);
			}
			if (chestplateCheck == false) {
				p.getInventory().setChestplate(null);
			}
			if (leggingsCheck == false) {
				p.getInventory().setLeggings(null);
			}
			if (bootsCheck == false) {
				p.getInventory().setBoots(null);
			}
		}
	}
	
	public void dropPercentageRemove(Player p, List<ItemStack> i) {
		for (ItemStack itemList : i) {
			p.getInventory().remove(itemList);
			if(p.getInventory().getHelmet() != null) {
			    if(p.getInventory().getHelmet() == itemList) {
			    	p.getInventory().setHelmet(null);
			    }
			}
			if(p.getInventory().getChestplate() != null) {
			    if(p.getInventory().getChestplate() == itemList) {
			    	p.getInventory().setChestplate(null);
			    }
			}
			if(p.getInventory().getLeggings() != null) {
			    if(p.getInventory().getLeggings() == itemList) {
			    	p.getInventory().setLeggings(null);
			    }
			}
			if(p.getInventory().getBoots() != null) {
			    if(p.getInventory().getBoots() == itemList) {
			    	p.getInventory().setBoots(null);
			    }
			}
		} 
	}
}