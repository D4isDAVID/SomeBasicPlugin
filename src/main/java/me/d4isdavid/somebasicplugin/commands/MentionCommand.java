package me.d4isdavid.somebasicplugin.commands;

import me.d4isdavid.somebasicplugin.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

public class MentionCommand implements CommandExecutor, TabCompleter {

    private final Config mentionConfig = Config.get("mention.yml");

    @Override
    public boolean onCommand(final @NonNull CommandSender sender, final @NonNull Command command, final @NonNull String label, final @NonNull String[] args) {
        if (!(sender instanceof Player player))
            return true;
        if (args.length != 2)
            return false;
        if (!args[0].equalsIgnoreCase("ignore")
                && !args[0].equalsIgnoreCase("sound")
                && !args[0].equalsIgnoreCase("title")
                && !args[0].equalsIgnoreCase("actionbar")
                && !args[0].equalsIgnoreCase("action"))
            return false;
        if (args[0].equalsIgnoreCase("reload")) {
            mentionConfig.reload();
            player.sendMessage("&6Reloaded the config.");
            return true;
        }
        if (!args[1].equalsIgnoreCase("true") && !args[1].equalsIgnoreCase("false"))
            return false;
        boolean bool = args[1].equalsIgnoreCase("true");
        FileConfiguration config = mentionConfig.get();
        config.set(player.getName() + "." + args[0].toLowerCase(), bool);
        player.sendMessage(t("&7Setting " + args[0] + " to " + args[1]));
        try {
            mentionConfig.save();
            player.sendMessage(t("&eSaved!"));
        } catch (IOException e) {
            player.sendMessage(t("&4Failed to save."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final @NonNull CommandSender sender, final @NonNull Command command, final @NonNull String label, final @NonNull String[] args) {
        if (!(sender instanceof Player)) return null;
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                list.add("ignore");
                list.add("sound");
                list.add("title");
                list.add("actionbar");
            }
            case 2 -> {
                list.add("true");
                list.add("false");
            }
        }
        return list;
    }

}
