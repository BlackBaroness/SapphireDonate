package com.sapphire.sapphiredonate;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Events implements Listener {

    private final JavaPlugin core;
    private List<String> ids = new ArrayList<>();
    private final String prefix = ChatColor.AQUA + "" + ChatColor.BOLD + "" + "Донат " + ChatColor.GOLD + "| " + ChatColor.RESET;

    // ==============================================
    // паки
    String ECONOMY_PACK = "sapphiredonate:economy";
    String MAGIC_PACK = "sapphiredonate:magic";
    String TECHNO_PACK = "sapphiredonate:techno";
    String ADVANCED_MAGIC_PACK = "sapphiredonate:advanced";

    // свитки возврата
    String SCROLL_NEARNESS_10 = "sapphiredonate:scroll_10";
    String SCROLL_NEARNESS_25 = "sapphiredonate:scroll_25;
    String SCROLL_NEARNESS_50 = "sapphiredonate:scroll_50";
    // ==============================================

    public Events(JavaPlugin core) {
        this.core = core;

        // добавляем все id в лист
        ids.add(ECONOMY_PACK);
        ids.add(MAGIC_PACK);
        ids.add(TECHNO_PACK);
        ids.add(ADVANCED_MAGIC_PACK);

        ids.add(SCROLL_NEARNESS_10);
        ids.add(SCROLL_NEARNESS_25);
        ids.add(SCROLL_NEARNESS_50);
    }

    @EventHandler
    private void click(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            Player p = e.getPlayer();
            String current = p.getInventory().getItemInMainHand().getType().toString();
            if (ids.contains(current)) {
                final ItemStack itemStack = p.getInventory().getItemInMainHand();
                e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                execute(p, current, itemStack);
            }
        }
    }

    private void execute(Player p, String way, ItemStack itemStack) {
        p.sendMessage(prefix + "Обработка...");

        // получение списка предметов
        List<ItemStack> items = new ArrayList<>();
        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(core.getDataFolder() + File.separator + way));
        int i = 0;
        while (cfg.getItemStack(String.valueOf(i)) != null) {
            items.add(cfg.getItemStack(String.valueOf(i)));
            i++;
        }

        // проверка, достаточно ли места
        int free = getFreeSpace(p);
        if (free < items.size()) {
            p.getInventory().setItemInMainHand(itemStack);
            p.sendMessage(prefix + "Не хватает " + (items.size() - free) + " свободных слотов в инвентаре.");
            return;
        }

        // выдача
        for (ItemStack item : items) {
            p.getInventory().addItem(item);
        }
        p.sendMessage("Вы успешно использовали донат. Благодарим за покупку.");
    }

    private int getFreeSpace(Player p) {
        int i = 0;
        for (ItemStack itemStack : p.getInventory().getContents()) {
            if (itemStack == null) {
                i++;
                continue;
            }
            if (itemStack.getType().equals(Material.AIR)) i++;
        }
        return i;
    }
}