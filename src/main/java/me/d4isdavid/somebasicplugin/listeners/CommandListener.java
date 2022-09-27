package me.d4isdavid.somebasicplugin.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

@SuppressWarnings("unused")
public class CommandListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(t("&7" + event.getPlayer().getName() + " executed " + event.getMessage()));
        }
    }

}
