package me.d4isdavid.somebasicplugin;

import me.d4isdavid.somebasicplugin.commands.CoordinatesCommand;
import me.d4isdavid.somebasicplugin.commands.MentionCommand;
import me.d4isdavid.somebasicplugin.listeners.AsyncPlayerChatListener;
import me.d4isdavid.somebasicplugin.listeners.CommandListener;
import me.d4isdavid.somebasicplugin.listeners.PlayerJoinListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class SomeBasicPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new Config("coordinates.yml");
        new Config("mention.yml");

        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents(new AsyncPlayerChatListener(), this);

        prepareCommand("coordinates", new CoordinatesCommand());
        prepareCommand("mention", new MentionCommand());
    }

    private void prepareCommand(String name, CommandExecutor executor) {
        PluginCommand command = getServer().getPluginCommand(name);
        assert command != null;
        command.setExecutor(executor);
    }

}
