package me.d4isdavid.somebasicplugin.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

@SuppressWarnings("unused")
public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setDisplayName(t((player.isOp() ? "&6" : "&7") + player.getName() + "&r"));
        player.sendMessage("Welcome to the server, " + player.getDisplayName() + "!");
    }

}
