package me.waterbroodje.chatgames;

import me.waterbroodje.chatgames.listener.PlayerChatListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public final class Main extends JavaPlugin {

    public static boolean isGameActive = false;
    public static String answer = "null";
    public static String type = "null";
    public static String prefix;
    private static Main instance;
    public static String before = "null";

    public static Main getInstance() {
        return instance;
    }


    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(), this);
        prefix = ChatColor.translateAlternateColorCodes('&', getConfig().getString("prefix"));
        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        isGameActive = true;
                        String key = getRandomGame();
                        type = getConfig().getString("games." + key + ".type");
                        String question = getConfig().getString("games." + key + ".question");
                        if (type.equalsIgnoreCase("wordshuffle")) {
                            answer = getConfig().getString("games." + key + ".word");
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', question
                                    .replace("%prefix%", prefix)
                                    .replace("%word%", shuffleString(getConfig().getString("games." + key + ".word")))
                            ));
                        } else if (type.equalsIgnoreCase("typeword")) {
                            answer = getConfig().getString("games." + key + ".word");
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', question
                                    .replace("%prefix%", prefix)
                                    .replace("%word%", getConfig().getString("games." + key + ".word"))
                            ));
                        } else if (type.equalsIgnoreCase("question")) {
                            answer = getConfig().getString("games." + key + ".answer");
                            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', question
                                    .replace("%prefix%", prefix)
                            ));
                        }
                        before = key;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (isGameActive) {
                                    isGameActive = false;
                                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("no-winner")
                                            .replace("%prefix%", prefix)
                                            .replace("%answer%", answer)
                                    ));
                                    answer = "null";
                                    type = "null";
                                }
                            }
                        }.runTaskLater(Main.getInstance(),20 * 30);
                    }
                }.runTaskTimer(Main.getInstance(), 0L, 20 * getConfig().getInt("delay"));
            }
        }.runTaskLater(Main.getInstance(), 20 * getConfig().getInt("delay"));
    }

    public String getRandomGame() {
        Set<String> stringSet = getConfig().getConfigurationSection("games").getKeys(false);
        ArrayList<String> stringArray = new ArrayList<>(stringSet);
        stringArray.remove(before);
        Random random = new Random();

        return stringArray.get(random.nextInt(stringArray.size()));
    }

    public String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        String shuffled = "";
        for (String letter : letters) {
            shuffled += letter;
        }
        return shuffled;
    }
}
