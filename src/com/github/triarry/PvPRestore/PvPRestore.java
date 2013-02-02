package com.github.triarry.PvPRestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPRestore extends JavaPlugin {
	
	public final PvPRestorePlayerListener playerListener = new PvPRestorePlayerListener(this);
    File configFile;
    FileConfiguration config;

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
	    config = new YamlConfiguration();
	    loadYamls();
	    getCommand("pvprestore").setExecutor(new PvPRestoreCommandExecutor(this));
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
	        System.out.println("[PvP Restore] Succesfully loaded config.yml");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}