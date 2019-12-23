package net.easecation.transferpro.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.TSProServerEntry;
import net.easecation.transferpro.TransferPro;
import net.easecation.transferpro.api.ServerMatchResult;
import net.easecation.transferpro.api.TransferProAPI;

import java.util.List;
import java.util.Map;

public class TargetTransferCommand extends PluginCommand<TransferPro> {

    public TargetTransferCommand(TransferPro owner) {
        super("playertransfer", owner);
        this.setAliases(new String[]{"pts"});
        this.setDescription("Transfer Player Command");
        this.commandParameters.clear();
        this.commandParameters.put("target", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("group", CommandParamType.STRING, false),
                new CommandParameter("serverid", CommandParamType.STRING, true),
        });
        this.commandParameters.put("onlyServerWithTarget", new CommandParameter[]{
                new CommandParameter("player", CommandParamType.TARGET, false),
                new CommandParameter("serverid", CommandParamType.STRING, false),
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("tarnsferpro.transferplayer")) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
            return false;
        }
        if (args.length > 0) {
            String arg0 = args[0];
            String arg1 = args.length > 1 ? args[1] : null;
            String arg2 = args.length > 2 ? args[2] : null;
            Player target = Server.getInstance().getPlayer(arg0);
            if (target != null) { //第一个参数是 目标玩家
                ServerMatchResult result = TransferProAPI.match(arg1, arg2);
                if (result.getServer() != null) result.getServer().transfer(target);
                sender.sendMessage(result.getResultType().getMessage());
            } else {
                sender.sendMessage(getPlugin().getLang().translateString("tspro.command.player-not-found"));
            }
        } else {
            sender.sendMessage("Usage: /transfer <player> <group> [serverid]");
        }
        return true;
    }

}
