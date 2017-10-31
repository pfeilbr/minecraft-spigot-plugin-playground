package com.brianpfeil.bukkit.myplugin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Logger;

public class Main extends JavaPlugin implements Listener {

    // example command
    public class MyCommand implements CommandExecutor {
        private Main plugin;
        public MyCommand(Main plugin) {
            this.plugin = plugin;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if (sender instanceof Player) {
                Player player = (Player) sender;
                PlayerInventory playerInventory = player.getInventory();

                // index of selected slot
                int heldItemSlot = playerInventory.getHeldItemSlot();

                // set the item held in hand
                // playerInventory.setHeldItemSlot(/* 0-9 */);

                if (args.length == 3) {
                    if (args[0].equalsIgnoreCase("add")) {
                        // e.g. /mycmd add IRON_ORE 7
                        ItemStack itemStack = new ItemStack(Material.getMaterial(args[1]));
                        itemStack.setAmount(Integer.valueOf(args[2]));
                        player.getInventory().addItem(itemStack);
                    }
                }

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("toggle_log_all_events")) {
                        RegisteredListener registeredListener = new RegisteredListener(this.plugin, (listener, event) -> {
                            if (!(event instanceof EntityAirChangeEvent)) { // ignore EntityAirChangeEvent event. fires A LOT
                                log.info(event.toString());
                            }
                        }, EventPriority.NORMAL, this.plugin, false);
                        for (HandlerList handler : HandlerList.getHandlerLists()) {
                            handler.register(registeredListener);
                        }
                    }
                }

            } else { // command invoked from console
                log.info("command " + command.getLabel() + " invoked from console and not a player");
            }

            return false;
        }
    }

    private Logger log = this.getLogger();
    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        super.onEnable();

        log.info("onEnable : " + this.getDescription().getFullName());

        this.getCommand("mycmd").setExecutor(new MyCommand(this));

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this, this);

        // example of setting config value
        config.addDefault("isOnPlayerJoinMessageEnabled", true);
        config.addDefault("onPlayerJoinMessage", "welcome to the server!");
        config.options().copyDefaults(true);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerInventory playerInventory = player.getInventory();
        ItemStack itemInMainHand = playerInventory.getItemInMainHand();
        Material material = itemInMainHand.getType();

        if (config.getBoolean("isOnPlayerJoinMessageEnabled")) {
            player.sendMessage(config.getString("onPlayerJoinMessage"));
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location fromLocation = event.getFrom();
        Location toLocation = event.getTo();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Bukkit.broadcastMessage("Block break v11");
        //event.getPlayer().getInventory().getItemInMainHand().setType(Material.DIAMOND_AXE);
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}