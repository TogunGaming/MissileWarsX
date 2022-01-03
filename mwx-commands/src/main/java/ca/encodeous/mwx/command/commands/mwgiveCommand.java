package ca.encodeous.mwx.command.commands;

import ca.encodeous.mwx.command.MissileWarsCommand;
import ca.encodeous.mwx.command.RootCommand;
import ca.encodeous.mwx.configuration.MissileWarsItem;
import ca.encodeous.mwx.core.game.CoreGame;
import ca.encodeous.mwx.core.utils.Chat;
import ca.encodeous.mwx.core.utils.Utils;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.lucko.commodore.MinecraftArgumentTypes;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class mwgiveCommand extends MissileWarsCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try{
            Player p = null;
            boolean successMsg;
            boolean hasCount = false;
            int aidx = 0;
            if(args.length == 1){
                if(sender instanceof Player) p = (Player) sender;
                else{
                    sender.sendMessage("You are not a player...");
                    return true;
                }
                successMsg = false;
            }else{
                p = Bukkit.getPlayer(args[0]);
                if(p == null){
                    sender.sendMessage("Player not found!");
                    return true;
                }
                successMsg = true;
                aidx = 1;
            }
            if(!Utils.CheckPrivPermission(p)) return true;
            if(aidx + 1 != args.length) hasCount = true;
            int count = 1;
            if(hasCount){
                try{
                    count = Integer.parseInt(args[aidx + 1]);
                }catch (Exception e){
                    sender.sendMessage("Please specify a valid stack size");
                    return true;
                }
            }

            if(args[aidx].equals("*")){
                for(MissileWarsItem item : CoreGame.GetImpl().CreateDefaultItems()){
                    if(item.IsExempt) continue;
                    ItemStack ritem = CoreGame.GetImpl().CreateItem(item);
                    ritem.setAmount(count);
                    p.getInventory().addItem(ritem);
                }
                p.sendMessage(Chat.FCL("&6You have been given all the items"));
                if(successMsg){
                    sender.sendMessage("Success!");
                }
            }else{
                MissileWarsItem item = CoreGame.Instance.GetItemById(args[aidx]);
                if(item == null){
                    sender.sendMessage("Item ID not found! See items by running /mwitems");
                    return true;
                }
                ItemStack ritem = CoreGame.GetImpl().CreateItem(item);
                ritem.setAmount(count);
                p.getInventory().addItem(ritem);
                p.sendMessage(Chat.FCL("&6You have been given &c" + item.MissileWarsItemId + "&6!"));
                if(successMsg){
                    sender.sendMessage("Success!");
                }
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public RootCommand BuildCommand() {
        throw new NotImplementedException("Building this command is not implemented");
    }

    @Override
    public String GetCommandName() {
        return "mwgive";
    }
}