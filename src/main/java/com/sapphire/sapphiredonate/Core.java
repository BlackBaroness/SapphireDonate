package com.sapphire.sapphiredonate;

import org.bukkit.plugin.java.JavaPlugin;

public final class Core extends JavaPlugin {

    @Override
    public void onEnable() {
        Events events = new Events(this);
        getServer().getPluginManager().registerEvents(events, this);
    }
}