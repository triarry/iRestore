package com.github.triarry.iRestore;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.triarry.iRestore.metrics.Metrics;
import com.github.triarry.iRestore.utilities.Updater;
import com.github.triarry.iRestore.utilities.Utilities;
import com.github.triarry.iRestore.utilities.Updater.UpdateResult;

import java.io.*;

public class iRestore extends JavaPlugin {
	
	public final iRestorePlayerListener playerListener = new iRestorePlayerListener(this);
	
    File configFile;
    FileConfiguration config;
    
    public static Economy econ = null;
    public static boolean myPetEnabled = false;
    public static boolean playerHeadsEnabled = false;
    
    public static boolean update = false;
    public static String ver = "";
    
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
		Utilities.getUtilities().startUp(this);
	    configFile = new File(getDataFolder(), "config.yml");
	    try {
	        firstRun();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
            this.getLogger().info("Now tracking stats!");
	    } catch (IOException e) {
	        // Failed to submit the stats :-(
	    }
	    config = new YamlConfiguration();
	    loadYamls();
	    if (getConfig().getDouble("version") != 2.0) {
	    	this.getLogger().info("Your config is out of date. Regenerating...");
            configFile.setWritable(true);
            configFile.renameTo(new File(getDataFolder() + "/old-config.yml"));
	    	reConfig();
	    }
	    getCommand("pvprestore").setExecutor(new iRestoreCommandExecutor(this));
        if (!setupEconomy())
            this.getLogger().info("No Vault dependency found! (iConomy, BOSEconomy, etc.)");
        if (getConfig().getBoolean("my-pet-enabled")) {
            if (!setupMyPet())
                this.getLogger().info("MyPet not found! Disabling MyPet stuff.");
            else
            	this.getLogger().info("PvPRestore has hooked into MyPet!");
        }
		if(getConfig().getBoolean("check-for-updates") == true) {
			Updater updater = new Updater(this, "irestore", this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false);
			updater.getResult();
			if(updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
				update = true;
			}
			ver = updater.getLatestVersionString();
		}
		if (update == true) {
			this.getLogger().info("You have an update waiting for you! (dev.bukkit.org/server-mods/irestore/)");
		}
	}
	
	private void firstRun() throws Exception {
	    if(!configFile.exists()){
	        configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }
	}
	
	private void reConfig() {
        configFile.getParentFile().mkdirs();
        copy(getResource("config.yml"), configFile);
	}
	
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupMyPet() {
        if (getServer().getPluginManager().isPluginEnabled("MyPet")) {
            myPetEnabled = true;
            return true;
        }
        return false;
    }
    
	private void copy(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public void saveYamls() {
	    try {
	        config.save(configFile);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void loadYamls() {
	    try {
	        config.load(configFile);
            this.getLogger().info("Succesfully loaded config.yml");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}