package me.d4isdavid.somebasicplugin.commands;

import me.d4isdavid.somebasicplugin.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.*;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

public class CoordinatesCommand implements CommandExecutor, TabCompleter {

    private final Config coordinatesConfig = Config.get("coordinates.yml");

    @Override
    public boolean onCommand(final @NonNull CommandSender sender, final @NonNull Command command, final @NonNull String label, final @NonNull String[] args) {
        if (!(sender instanceof Player player))
            return true;
        if (args.length == 0)
            return false;
        String action = args[0];
        if (!action.equalsIgnoreCase("list")
                && !action.equalsIgnoreCase("set")
                && !action.equalsIgnoreCase("remove")
                && !action.equalsIgnoreCase("tp")
                && !action.equalsIgnoreCase("reload"))
            return false;
        if (action.equalsIgnoreCase("reload")) {
            coordinatesConfig.reload();
            player.sendMessage("&6Reloaded the config.");
            return true;
        }
        FileConfiguration config = coordinatesConfig.get();
        if (action.equalsIgnoreCase("list")) {
            ConfigurationSection section = config.getConfigurationSection(player.getName());
            StringBuilder str = new StringBuilder("&eCoordinate List &7-------------\n&r");
            if (section != null) {
                Set<String> locations = section.getKeys(false);
                for (String name : locations)
                    str.append(name).append(": ").append(config.getString(player.getName() + "." + name)).append("\n&r");
            }
            str.append("&7---------------------------");
            player.sendMessage(t(str.toString()));
            return true;
        }
        if (args.length < 2)
            return false;
        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        String existing = config.getString(player.getName() + "." + name);
        if (action.equalsIgnoreCase("set")) {
            Location loc = player.getLocation();
            String xyz = String.format("%s, %s, %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            player.sendMessage(t("&7Setting '&r" + name + "&7' to " + xyz));
            if (existing != null) player.sendMessage(t("&cReplacing with existing coordinate &4" + existing + "&c."));
            config.set(player.getName() + "." + name, xyz);
        } else if (action.equalsIgnoreCase("remove")) {
            if (existing == null) {
                player.sendMessage(t("&4The coordinate '&r" + name + "&4' does not exist!"));
                return true;
            }
            player.sendMessage(t("&7Removing the coordinate " + existing + " (&r" + name + "&7)."));
            config.set(player.getName() + "." + name, null);
        } else if (action.equalsIgnoreCase("tp")) {
            if (existing == null) {
                player.sendMessage(t("&4The coordinate '&r" + name + "&4' does not exist!"));
                return true;
            }
            String[] xyz = existing.split(", ");
            Location loc = new Location(
                    Bukkit.getWorld("world"),
                    Double.parseDouble(xyz[0]) + 0.5,
                    Double.parseDouble(xyz[1]) + 0.5,
                    Double.parseDouble(xyz[2]) + 0.5);
            player.teleport(loc);
            player.sendMessage(t("&7Teleported you to " + existing + " (&r" + name + "&7)."));
            return true;
        }
        try {
            coordinatesConfig.save();
            player.sendMessage(t("&eSaved!"));
        } catch (IOException e) {
            player.sendMessage(t("&4Failed to save."));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(final @NonNull CommandSender sender, final @NonNull Command command, final @NonNull String label, final @NonNull String[] args) {
        if (!(sender instanceof Player player)) return null;
        List<String> list = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                list.add("set");
                list.add("list");
                list.add("remove");
            }
            case 2 -> {
                if (!args[0].equalsIgnoreCase("remove")
                        && !args[0].equalsIgnoreCase("tp"))
                    return list;
                ConfigurationSection section = coordinatesConfig.get().getConfigurationSection(player.getName());
                if (section == null)
                    return list;
                Set<String> locs = section.getKeys(false);
                list.addAll(locs);
            }
        }
        return list;
    }

}
