package net.plancktong.voting.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigFile {
	
	private File file;
	private FileConfiguration bukkitFile;
	
	public ConfigFile(Plugin plugin, String filename) {
		this.file = new File(plugin.getDataFolder(), filename);
		
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			plugin.saveResource(filename, true);
		}
		
		this.bukkitFile = YamlConfiguration.loadConfiguration(file);
		System.out.println("Loaded config file: " + filename);
	}
	
	public FileConfiguration getBukkitFile() {
		return bukkitFile;
	}
	
}
