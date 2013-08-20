package com.github.triarry.iRestore.listeners;

import de.Keyle.MyPet.entity.types.CraftMyPet;
import de.Keyle.MyPet.entity.types.MyPet;
import de.Keyle.MyPet.skill.skills.implementation.ranged.MyPetProjectile;

import com.github.triarry.iRestore.iRestore;
import com.github.triarry.iRestore.utilities.Utilities;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class PlayerListener implements Listener {
	
	private iRestore plugin;
	 
	public HashMap<Player , ItemStack[]> items = new HashMap<Player , ItemStack[]>();
	public HashMap<Player , ItemStack[]> armor = new HashMap<Player , ItemStack[]>();
	
	public List<ItemStack> itemsToBeRemoved = new ArrayList<ItemStack>();

    public PlayerListener(iRestore plugin) {
        this.plugin = plugin;
    }
	
	@EventHandler
	public void informAdmins(PlayerJoinEvent event) {
		Player p = event.getPlayer();
		
		if (iRestore.update && p.hasPermission("irestore.update")) {
			p.sendMessage(ChatColor.GREEN + "An update is available: " + ChatColor.GOLD + iRestore.ver);
			p.sendMessage(ChatColor.GREEN + "Download it here: " + ChatColor.GOLD + "http://dev.bukkit.org/server-mods/irestore/");
		}
	}
	
	@EventHandler
	public void iRestorePlayerDeath(PlayerDeathEvent event) {
		FileConfiguration config = plugin.getConfig();
        Player p = event.getEntity();
        String killer;
        EntityDamageEvent pDamage = event.getEntity().getLastDamageCause();

        if(p.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent lastDamageEvent = (EntityDamageByEntityEvent) p.getLastDamageCause();
            
            if(!p.hasPermission("irestore.participate")) {
            	return;
            }
            
            /*
             * Projectiles
             */
            
            else if(lastDamageEvent.getCause() == DamageCause.PROJECTILE) {
            	Projectile a = (Projectile) lastDamageEvent.getDamager();
            	
            	if(a.getShooter() instanceof Player && (config.getBoolean("events.pvp.bows") || config.getBoolean("events.pvp.all")) && p.hasPermission("irestore.events.pvp.bows")) { 
            		Player s = (Player) a.getShooter();
            		killer = s.getName();
            	}
            	
            	else if(a.getShooter() instanceof Skeleton) {
            		Skeleton s = (Skeleton) a.getShooter();
            	
            		if(s.getSkeletonType() == SkeletonType.NORMAL && (config.getBoolean("events.mobs.skeleton") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.skeleton")) {
            			killer = "Skeleton";
            		}
            		
            		else if(s.getSkeletonType() == SkeletonType.WITHER && (config.getBoolean("events.mobs.wither_skeleton") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.wither_skeleton")) {
            			killer = "Wither Skeleton";
            		}
            		
            		else {
            			return;
            		}
            	}
            	
            	else if(a.getShooter() instanceof Ghast && (config.getBoolean("events.mobs.ghast") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.ghast")) {
            		killer = "Ghast";
            	}
            	
            	else if(a.getShooter() instanceof Blaze && (config.getBoolean("events.mobs.blaze") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.blaze")) {
            		killer = "Blaze";
            	}
            	
            	else if(a.getShooter() instanceof Wither && (config.getBoolean("events.mobs.wither") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.wither")) {
            		killer = "Wither";
            	}
            	
            	else if(a.getShooter() instanceof EnderDragon && (config.getBoolean("events.mobs.enderdragon") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.enderdragon")) {
            		killer = "Enderdragon";
            	}
            	
            	else if(iRestore.myPetEnabled && config.getBoolean("my-pet-enabled")) {
            		if(a.getShooter() instanceof CraftMyPet) {
	            		MyPet myPet = ((CraftMyPet) a.getShooter()).getMyPet();
	            		killer = myPet.getOwner().getName() + "'s pet " + myPet.getPetName();
            		}
            		
            		else {
            			return;
            		}
            		
            	}
            	else { 
            		return;
            	}
            }
            
            /*
             * Magic
             */
            
            else if(lastDamageEvent.getCause() == DamageCause.MAGIC) {
            	ThrownPotion a = (ThrownPotion) lastDamageEvent.getDamager();
            	
            	if(a.getShooter() instanceof Witch && (config.getBoolean("events.mobs.witch") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.witch")) {
            		killer = "Witch";
            	}
            	
            	else if(a.getShooter() instanceof Player && (config.getBoolean("events.pvp.potions") || config.getBoolean("events.pvp.all")) && p.hasPermission("irestore.events.pvp.potion")) {
            		Player s = (Player) a.getShooter();
            		killer = s.getName();
            	}
            	
            	else {
            		return;
            	}
            }
            
            /*
             * MyPet
             */
            
            else if(iRestore.myPetEnabled && (lastDamageEvent.getDamager() instanceof CraftMyPet || lastDamageEvent.getDamager() instanceof MyPetProjectile) && config.getBoolean("my-pet-enabled")) {
                MyPet myPet = ((CraftMyPet) lastDamageEvent.getDamager()).getMyPet();
                killer = myPet.getOwner().getName() + "'s pet " + myPet.getPetName();
            }
            
            /*
             * Melee mobs
             */
            
            else if(lastDamageEvent.getDamager() instanceof Zombie && (config.getBoolean("events.mobs.zombie") || config.getBoolean("events.mobs.all")) && p.hasPermission("irestore.events.mobs.zombie")) {
            	killer = "Zombie";
            }
            
            /*
             * TNT
             */
            
        	else if (lastDamageEvent.getDamager() instanceof TNTPrimed) {
        		if((config.getBoolean("events.pvp.tnt") || config.getBoolean("events.pvp.all")) && p.hasPermission("irestore.events.pvp.tnt") && (p.getKiller() != null || lastDamageEvent.getDamager() instanceof Player)) {
        			killer = p.getKiller().getName();
        		}
        		
        		else if((config.getBoolean("events.other.tnt") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.other.tnt")) {
        			killer = "TNT";
        		}
        		
        		else {
        			return;
        		}
        	}
            
            /*
             * "Other" events
             */
            
        	else if ((pDamage.getCause() == DamageCause.FIRE || pDamage.getCause() == DamageCause.FIRE_TICK) && (config.getBoolean("events.other.fire") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.fire")) {
        		killer = "fire";
        	}
            
            else if (pDamage.getCause() == DamageCause.VOID && (config.getBoolean("events.other.void") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.void")) {
            	killer = "the Void";
            }
            
            else if (pDamage.getCause() == DamageCause.LAVA && (config.getBoolean("events.other.lava") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.lava")) {
            	killer = "lava";
            }
            
            else if (pDamage.getCause() == DamageCause.CONTACT && (config.getBoolean("events.other.cactus") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.cactus")) {
            	killer = "a cactus";
            }
            
            else if (pDamage.getCause() == DamageCause.DROWNING && (config.getBoolean("events.other.drowning") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.drowning")) {
            	killer = "the water";
            }
            
            else if (pDamage.getCause() == DamageCause.STARVATION && (config.getBoolean("events.other.starvation") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.starvation")) {
            	killer = "their lack of food";
            }
            
            else if (pDamage.getCause() == DamageCause.SUFFOCATION && (config.getBoolean("events.other.suffocation") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.suffocation")) {
            	killer = "their lack of air";
            }
            
            else if (pDamage.getCause() == DamageCause.FALL && (config.getBoolean("events.other.falling") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.falling")) {
            	killer = "breaking their own legs";
            }
            
            else if (pDamage.getCause() == DamageCause.FALL && (config.getBoolean("events.other.magic") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.magic")) {
            	killer = "magic";
            }
            
            /*
             * PvP
             */
            
            else if ((p.getKiller() != null || lastDamageEvent.getDamager() instanceof Player) && (config.getBoolean("events.pvp.melee") || config.getBoolean("events.pvp.all")) && p.hasPermission("irestore.events.pvp.melee")) { 
            	killer = p.getKiller().getName();
            }
            
            /*
             * Player was not killed by an enabled event.
             */
            
            else {
                return;
            }
        }
        
        /*
         * This is so that if the p is killed by the environment as an effect of another p, it STILL counts.
         */
        
	    else if(!p.hasPermission("irestore.participate")) {
	    	return;
	    }
        
    	else if ((pDamage.getCause() == DamageCause.FIRE || pDamage.getCause() == DamageCause.FIRE_TICK) && (config.getBoolean("events.other.fire") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.fire")) {
    		killer = "fire";
    	}
        
        else if (pDamage.getCause() == DamageCause.VOID && (config.getBoolean("events.other.void") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.void")) {
        	killer = "the Void";
        }
        
        else if (pDamage.getCause() == DamageCause.LAVA && (config.getBoolean("events.other.lava") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.lava")) {
        	killer = "lava";
        }
        
        else if (pDamage.getCause() == DamageCause.CONTACT && (config.getBoolean("events.other.cactus") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.cactus")) {
        	killer = "a cactus";
        }
        
        else if (pDamage.getCause() == DamageCause.FALL && (config.getBoolean("events.other.falling") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.falling")) {
        	killer = "breaking their own legs";
        }
        
        else if (pDamage.getCause() == DamageCause.FALL && (config.getBoolean("events.other.magic") || config.getBoolean("events.other.all")) && p.hasPermission("irestore.events.magic")) {
        	killer = "magic";
        }
        
        /*
         * Player was not killed by an enabled event.
         */
        
        else {
            return;
        }
        
        /* 
         * Start figuring what the p will get to keep.
         */

        if (p.hasPermission("irestore.keep.all") && config.getBoolean("keep-inventory") && config.getBoolean("keep-xp")) {
        	keepXP(event);
        	
            if (p.hasPermission("irestore.money.steal") && config.getBoolean("vault.enabled") && killer != null) {
                moneySteal(event);
            }
            
            if (config.getBoolean("death-message")) {
                sendDeathMessage(event, killer);
            }
            
            keepInventory(event);
        }
        
        else if ((p.hasPermission("irestore.keep.xp") || p.hasPermission("irestore.keep.all")) && config.getBoolean("keep-xp") && !config.getBoolean("keep-inventory")) {
        	keepXP(event);
            
            if (p.hasPermission("irestore.money.steal") && config.getBoolean("vault.enabled") && killer != null) {
                moneySteal(event);
            }
            
            if (config.getBoolean("death-message")) {
                sendDeathMessage(event, killer);
            }
        }
        
        else if ((p.hasPermission("irestore.keep.inventory") || p.hasPermission("irestore.keep.all")) && config.getBoolean("keep-inventory") && !config.getBoolean("keep-xp")) {            
            if (p.hasPermission("irestore.money.steal") && config.getBoolean("vault.enabled") && killer != null) {
                moneySteal(event);
            }
            
            if (config.getBoolean("death-message")) {
                event.setDeathMessage(ChatColor.YELLOW + "[iRestore] " + ChatColor.RED + p.getName() + ChatColor.GREEN + " was killed by " + ChatColor.RED + killer + ChatColor.GREEN + "!");
            }
            
            keepInventory(event);
        }
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
    	saveItems(event);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
    	if(event.getPlayer().isDead()) {
    		saveItems(event);
    	}
	}

    @EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
    	if(event.getPlayer().isDead()) {
    		saveItems(event);
    	}
	}
    
    public void keepXP(PlayerDeathEvent event) {
        event.setKeepLevel(true);
        
        if (plugin.getConfig().getInt("xp-to-remove") < 100 && plugin.getConfig().getInt("xp-to-remove") > 0) {
            event.getEntity().setLevel((int) (event.getEntity().getLevel() * ((100.0 - plugin.getConfig().getInt("xp-to-remove")) / 100.0)));
        }
        
        event.setDroppedExp(0);
    }
    
    public void keepInventory(PlayerDeathEvent event) {
    	Player p = event.getEntity();
    	
        ItemStack[] content = p.getInventory().getContents();
        ItemStack[] content_armor = p.getInventory().getArmorContents();
        
        armor.put(p, content_armor);
        items.put(p, content);
        
        p.getInventory().clear();
        
        checkDropLists(event);
    }
    
    public void sendDeathMessage(PlayerDeathEvent event, String killer) {
    	event.setDeathMessage(ChatColor.YELLOW + "[iRestore] " + ChatColor.RED + event.getEntity().getName() + ChatColor.GREEN + " was killed by " + ChatColor.RED + killer + ChatColor.GREEN + "!");
    }
    
    public void checkDropLists(PlayerDeathEvent event) {
    	FileConfiguration config = plugin.getConfig();
    	
        if (config.getBoolean("percentage-drop.enabled") != true && config.getBoolean("whitelist.enabled") != true && config.getBoolean("blacklist.enabled") != true) {
        	event.getDrops().clear();
        	return;
        }
    	
        if (config.getBoolean("blacklist.enabled")) {
            dropBlacklist(event);
        }
        
        if (config.getBoolean("whitelist.enabled")) {
            dropWhitelist(event);
        }
        
        if (config.getBoolean("percentage-drop.enabled")) {
            dropPercentage(event);
        }
        
    }
    
    public void saveItems(PlayerEvent event) {
		FileConfiguration config = plugin.getConfig();
		Player p = event.getPlayer();
			
        if (items.containsKey(p)) {
            p.getInventory().clear();
            p.getInventory().setContents(items.get(p));
            items.remove(p);
            
            if (config.getBoolean("percentage-drop.enabled")) {
            	Utilities.getUtilities().dropPercentageRemove(p, itemsToBeRemoved);
            }
            
            if (config.getBoolean("blacklist.enabled")) {
            	Utilities.getUtilities().blacklistItems(p);
            }
            
            if (config.getBoolean("whitelist.enabled")) {
            	Utilities.getUtilities().whitelistItems(p);
            }
        }
        if (armor.containsKey(event.getPlayer()) && armor.size() != 0) {
            p.getInventory().setArmorContents(armor.get(p));
            armor.remove(p);
            
            if (config.getBoolean("percentage-drop.enabled")) {
            	Utilities.getUtilities().dropPercentageRemove(p, itemsToBeRemoved);
            }
            
            if (config.getBoolean("blacklist.enabled")) {
            	Utilities.getUtilities().blacklistItems(p);
            }
            
            if (config.getBoolean("whitelist.enabled")) {
            	Utilities.getUtilities().whitelistArmor(p);
            }
        }
    }

	public void moneySteal(PlayerDeathEvent event) {
		FileConfiguration config = plugin.getConfig();
		
		Player p = event.getEntity();
		Player k = p.getKiller();
		
		if (!p.hasPermission("irestore.money.exempt") && iRestore.econ != null && k != null) {
			double r = iRestore.econ.getBalance(p.getName()) * (config.getInt("vault.money-to-steal") / 100.0);
			
            iRestore.econ.depositPlayer(k.getName(), r);
            iRestore.econ.withdrawPlayer(p.getName(), r);
            
			DecimalFormat dFormat = new DecimalFormat();
			String d = dFormat.format(r);
			
			k.sendMessage(ChatColor.YELLOW + "[iRestore] " + ChatColor.GREEN  + "You stole " + ChatColor.RED + d + " " + iRestore.econ.currencyNamePlural() + ChatColor.GREEN + " from " + ChatColor.RED + p.getName());
		}
	}
	
	public void dropBlacklist(PlayerDeathEvent event) {
		FileConfiguration config = plugin.getConfig();
		Player p = event.getEntity();
		
		for (Integer itemList : config.getIntegerList("blacklist.items")) {
			for (ItemStack itemStackList : event.getDrops()) {
				if (itemStackList.getTypeId() == itemList) {
					p.getLocation().getWorld().dropItem(p.getLocation(), itemStackList);
				}
			}
		}
		
		event.getDrops().clear();
	}
	
	public void dropWhitelist(PlayerDeathEvent event) {
		FileConfiguration config = plugin.getConfig();
		
		for (Integer itemList : config.getIntegerList("whitelist.items")) {
			Iterator<ItemStack> iterator = event.getDrops().iterator();
			
			while (iterator.hasNext()) {
				ItemStack itemStack = iterator.next();
				
				if (itemStack.getTypeId() == itemList) {
					iterator.remove();
				}
			}
		}
	}
	
	public void dropPercentage(PlayerDeathEvent event) {
		FileConfiguration config = plugin.getConfig();
		Iterator<ItemStack> iterator = event.getDrops().iterator();
		Double percentage = (config.getInt("percentage-drop.percentage") / 100.0);
		Integer i = 0;
		Integer dropModifier = (int) (percentage * event.getDrops().size());
		
		while (i < dropModifier && iterator.hasNext()) {
			ItemStack stackToRemove = iterator.next();
			
			itemsToBeRemoved.add(stackToRemove);
			
			iterator.remove();
			
			i++;
		}
	}
}