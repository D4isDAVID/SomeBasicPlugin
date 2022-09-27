package me.d4isdavid.somebasicplugin.listeners;

import me.d4isdavid.somebasicplugin.Config;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.d4isdavid.somebasicplugin.utils.ColorUtils.t;

@SuppressWarnings("unused")
public class AsyncPlayerChatListener implements Listener {

    private final Config mentionConfig = Config.get("mention.yml");

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        event.setFormat(t("%1$s&r: %2$s"));
        String msg = event.getMessage().toLowerCase();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (msg.contains(player.getName().toLowerCase())) {
                FileConfiguration config = mentionConfig.get();
                event.setMessage(event.getMessage().replaceAll("(?i)@" + player.getName(), t("&e@" + player.getName() + "&r")));
                if (config.getBoolean(player.getName() + ".ignore", false))
                    return;
                if (config.getBoolean(player.getName() + ".sound", true))
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 0);
                if (config.getBoolean(player.getName() + ".title", true))
                    player.sendTitle("Mentioned", t(event.getPlayer().getDisplayName() + " &ementioned you in chat!"), 10, 70, 10);
                if (config.getBoolean(player.getName() + ".actionbar", false))
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(t(event.getPlayer().getDisplayName() + " &ementioned you in chat!")));
            }
        }
    }

}
