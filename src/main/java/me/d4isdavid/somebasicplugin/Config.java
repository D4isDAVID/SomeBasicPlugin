package me.d4isdavid.somebasicplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {

    private static final Map<String, Config> configMap = new HashMap<>();
    private final File file;
    private FileConfiguration configuration;

    public static Config get(String fileName) {
        if (!configMap.containsKey(fileName))
            configMap.put(fileName, new Config(fileName));
        return configMap.get(fileName);
    }

    public Config(String fileName) {
        Config.configMap.put(fileName, this);
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SomeBasicPlugin").getDataFolder(), fileName);
        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return configuration;
    }

    public void save() throws IOException {
        configuration.save(file);
    }

    public void reload() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }

}
