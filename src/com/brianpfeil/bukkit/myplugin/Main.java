package com.brianpfeil.bukkit.myplugin;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {
    private Logger log = this.getLogger();

    @Override
    public void onEnable() {
        super.onEnable();
        log.info("HELLO MY-SPIGOT-PLUGIN v7");
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Bukkit.broadcastMessage("Block break v10");
        //event.getPlayer().getInventory().getItemInMainHand().setType(Material.DIAMOND_AXE);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}