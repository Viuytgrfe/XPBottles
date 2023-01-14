package me.bigxpbottles.bigxpbottles;

import com.sun.tools.javac.jvm.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Egg;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToIntFunction;

public final class BigXPBottles extends JavaPlugin implements Listener {
    ItemStack bottle = new ItemStack(Material.getMaterial("EXP_BOTTLE"));
    ItemMeta itemmeta = bottle.getItemMeta();
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String String, String[] args) {

        if(command.getName().equalsIgnoreCase("fb")) {

            if (sender instanceof Player) {
                Player p = (Player) sender;
                //(p.getLocation().getWorld().spawn(p.getLocation(), ExperienceOrb.class)).setExperience(100);

                if(args.length == 0)p.sendMessage(ChatColor.RED + "You did not provide any arguments when running the command. Try again.");
                else if(args.length > 1)p.sendMessage(ChatColor.RED + "Too many arguments. Try again.");
                else if(args.length == 1) {
                    int command_amount = Integer.parseInt(args[0]);
                    //System.out.println(command_amount);
                    int player_amount = p.getLevel();
                    //System.out.println(player_amount);

                    if(player_amount < command_amount) {
                        p.sendMessage(ChatColor.RED + "You didn't have so much levels. Amount of your levels: " + (String)(Integer.toString(p.getLevel())));
                    } else if (command_amount == 0) {
                        p.sendMessage(ChatColor.RED + "You can't get xp bottle with 0 levels :)");
                    } else {
                        itemmeta.setDisplayName(ChatColor.YELLOW + "Наполненный пузырёк опыта[" + ChatColor.RED + command_amount + " LVL" + ChatColor.YELLOW + "]");
                        List<String> description = Arrays.asList(ChatColor.RESET + "Содержит уровней: ", Integer.toString(command_amount));
                        itemmeta.setLore(description);
                        bottle.setItemMeta(itemmeta);
                        p.getInventory().addItem(bottle);
                        Experience.changeExp(p, -1*Experience.getExpFromLevel(command_amount));
                    }

                }
            }
        }
        return true;
    }
    @EventHandler
    public void onPlayerUseBottle(PlayerInteractEvent e) {
        Action action = e.getAction();
        ItemStack item = e.getItem();
        Player p = e.getPlayer();
        List<String> description = item.getItemMeta().getLore();
        //System.out.println(description.get(0));
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) {
            return;
        }
        if (item.getType().equals(Material.getMaterial("EXP_BOTTLE")) && description.get(0).equals("§rСодержит уровней: ")) {
            e.setCancelled(true);

            item.setAmount(item.getAmount()-1);


            int Int_num = Integer.parseInt(description.get(1));
            Experience.changeExp(e.getPlayer(), Experience.getExpFromLevel(Int_num));

        }
    }
}
