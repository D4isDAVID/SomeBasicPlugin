package me.d4isdavid.somebasicplugin;

import me.d4isdavid.somebasicplugin.commands.CoordinatesCommand;
import me.d4isdavid.somebasicplugin.commands.MentionCommand;
import me.d4isdavid.somebasicplugin.listeners.AsyncPlayerChatListener;
import me.d4isdavid.somebasicplugin.listeners.CommandListener;
import me.d4isdavid.somebasicplugin.listeners.PlayerJoinListener;
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

        PluginCommand coordinatesCommand = getServer().getPluginCommand("coordinates");
        assert coordinatesCommand != null;
        coordinatesCommand.setExecutor(new CoordinatesCommand());

        PluginCommand mentionCommand = getServer().getPluginCommand("mention");
        assert mentionCommand != null;
        mentionCommand.setExecutor(new MentionCommand());
    }

}
