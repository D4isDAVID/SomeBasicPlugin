package me.d4isdavid.somebasicplugin.commands;

import me.d4isdavid.somebasicplugin.Config;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

public class WarpsCommand implements CommandExecutor, TabCompleter {

    private final Config warpsConfig = Config.get("warps.yml");

    @Override
    public boolean onCommand(final @NonNull CommandSender sender, final @NonNull Command command, final @NonNull String label, @NonNull String[] args) {
        if (!(sender instanceof Player player))
            return true;
        if (args.length == 0) {
            args = new String[1];
            args[0] = "list";
        }
        String action = args[0];
        if (!action.equalsIgnoreCase("list")
                && !action.equalsIgnoreCase("set")
                && !action.equalsIgnoreCase("remove")
                && !action.equalsIgnoreCase("tp")
                && !action.equalsIgnoreCase("reload"))
            return false;
        if (action.equalsIgnoreCase("reload")) {
            warpsConfig.reload();
            player.sendMessage(t("&6Reloaded the config."));
            return true;
        }
        FileConfiguration config = warpsConfig.get();
        if (action.equalsIgnoreCase("list")) {
            StringBuilder str = new StringBuilder("&6Public Warps List &7----------\n&r");
            str.append(String.join("&r, ", config.getKeys(false))).append("\n");
            str.append("&7---------------------------");
            player.sendMessage(t(str.toString()));
            return true;
        }
        if (args.length < 2)
            return false;
        String name = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
        Location existing = config.getLocation(name + ".location");
        if (action.equalsIgnoreCase("set")) {
            Location loc = player.getLocation();
            String xyz = String.format("%s, %s, %s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            player.sendMessage(t("&7Setting '&r" + name + "&7' to " + xyz));
            if (existing != null) player.sendMessage(t("&cReplacing with existing warp &4" + name + "&c."));
            config.set(name + ".location", loc);
            config.set(name + ".owner", player.getName());
        } else if (action.equalsIgnoreCase("remove")) {
            if (existing == null) {
                player.sendMessage(t("&4The warp '&r" + name + "&4' does not exist!"));
                return true;
            }
            if (!config.getString(name + ".owner").equalsIgnoreCase(player.getName())) {
                player.sendMessage(t("&4You do not own this warp!"));
                return true;
            }
            player.sendMessage(t("&7Removing the warp &r" + name + "&7."));
            config.set(name, null);
        } else if (action.equalsIgnoreCase("tp")) {
            if (existing == null) {
                player.sendMessage(t("&4The warp '&r" + name + "&4' does not exist!"));
                return true;
            }
            player.teleport(existing);
            player.sendMessage(t("&7Teleported you to &r" + name + "&7."));
            return true;
        }
        try {
            warpsConfig.save();
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
                list.add("tp");
            }
            case 2 -> {
                if (!args[0].equalsIgnoreCase("remove")
                        && !args[0].equalsIgnoreCase("tp"))
                    return list;
                FileConfiguration config = warpsConfig.get();
                Set<String> locs = config.getKeys(false);
                if (args[0].equalsIgnoreCase("remove"))
                    locs.removeIf(loc -> !config.getString(loc + ".owner").equalsIgnoreCase(player.getName()));
                list.addAll(locs);
            }
        }
        return list;
    }

}
