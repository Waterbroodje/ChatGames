package me.waterbroodje.chatgames.listener;

import me.waterbroodje.chatgames.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(PlayerChatEvent e) {
        if (Main.isGameActive) {
            String message = e.getMessage();
            if (message.equalsIgnoreCase(Main.answer)) {
                // user won

                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("reward-message")
                        .replace("%prefix%", Main.prefix)
                        .replace("%player%", e.getPlayer().getName()))
                );

                Main.isGameActive = false;
                Main.answer = "null";
                Main.type = "null";
            }
        }
    }
}
