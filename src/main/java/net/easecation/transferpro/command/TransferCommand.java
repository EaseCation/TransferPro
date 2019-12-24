package net.easecation.transferpro.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.TransferPro;
import net.easecation.transferpro.api.ServerMatchResult;
import net.easecation.transferpro.api.TransferProAPI;


public class TransferCommand extends PluginCommand<TransferPro> {

    public TransferCommand(TransferPro owner) {
        super("transfer", owner);
        this.setAliases(new String[]{"ts"});
        this.setDescription("Transfer Command");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("group", CommandParamType.STRING, false),
                new CommandParameter("serverid", CommandParamType.STRING, true),
        });
        this.commandParameters.put("onlyServer", new CommandParameter[]{
                new CommandParameter("serverid", CommandParamType.STRING, false),
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("tarnsferpro.transfer")) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
            return false;
        }
        //if (sender instanceof Player) {
            if (args.length > 0) {
                String arg0 = args[0];
                String arg1 = args.length > 1 ? args[1] : null;
                ServerMatchResult result = TransferProAPI.match(arg0, arg1);
                if (result.getServer() != null) {
                    if (sender instanceof Player) {
                        result.getServer().transfer((Player) sender);
                    } else {
                        getPlugin().getLogger().info(result.getServer().toString());
                    }
                }
                else sender.sendMessage(result.getResultType().getMessage());
            } else {
                sender.sendMessage(getPlugin().getLang().translateString("tspro.command.usage.transfer"));
            }
        //} else {
            //sender.sendMessage(getPlugin().getLang().translateString("tspro.command.only-player"));
        //}
        return true;
    }

}
