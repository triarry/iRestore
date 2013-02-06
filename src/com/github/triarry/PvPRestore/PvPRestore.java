package com.github.triarry.PvPRestore;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;

public class PvPRestore extends JavaPlugin {
	
	public final PvPRestorePlayerListener playerListener = new PvPRestorePlayerListener(this);
    File configFile;
    FileConfiguration config;
    public static Economy econ = null;
    public static boolean myPetEnabled = false;

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
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
	    getCommand("pvprestore").setExecutor(new PvPRestoreCommandExecutor(this));
        if (!setupEconomy() ) {
            this.getLogger().info("No Vault dependency found! (iConomy, BOSEconomy, etc.)");
        }
        if (!setupMyPet() ) {
            this.getLogger().info("MyPet not found! Disabling MyPet stuff.");
        }
	}
	@Override
	public void onDisable() {
	}
	private void firstRun() throws Exception {
	    if(!configFile.exists()){
	        configFile.getParentFile().mkdirs();
	        copy(getResource("config.yml"), configFile);
	    }
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