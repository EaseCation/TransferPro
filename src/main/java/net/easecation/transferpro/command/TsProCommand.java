package net.easecation.transferpro.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.TSProServerEntry;
import net.easecation.transferpro.TransferPro;
import net.easecation.transferpro.api.TransferProAPI;

import java.util.Map;

public class TsProCommand extends PluginCommand<TransferPro> {

    public TsProCommand(TransferPro owner) {
        super("tspro", owner);
        this.setDescription("Transfer Pro");
        this.commandParameters.clear();
        this.commandParameters.put("setme", new CommandParameter[]{
                new CommandParameter("setme", new String[]{"setme"}),
                new CommandParameter("group", CommandParameter.ARG_TYPE_STRING),
                new CommandParameter("serverid", CommandParameter.ARG_TYPE_STRING),
                new CommandParameter("address", CommandParameter.ARG_TYPE_RAW_TEXT),
        });
        this.commandParameters.put("dump", new CommandParameter[]{
                new CommandParameter("dump", new String[]{"dump"}),
                new CommandParameter("group", true)
        });
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission("tarnsferpro.tspro")) {
            sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.permission"));
            return false;
        }
        if (args.length > 0) {
            switch (args[0]) {
                case "setme":
                    if (sender instanceof Player) {
                        sender.sendMessage(getPlugin().getLang().translateString("tspro.command.only-console"));
                        break;
                    }
                    if (args.length > 3) {
                        getPlugin().getConfig().set("my-group", args[1]);
                        getPlugin().getConfig().set("my-server", args[2]);
                        getPlugin().getConfig().set("my-address", args[3]);
                    } else {
                        sender.sendMessage("Usage: /tspro setme <group> <server> <address>");
                    }
                    break;
                case "dump":
                    if (args.length > 1) {
                        String group = args[1];
                        Map<String, TSProServerEntry> entries = TransferProAPI.getGroupEntries(group);
                        if (entries.isEmpty()) {
                            sender.sendMessage(getPlugin().getLang().translateString("tspro.command.empty-result"));
                        }
                        entries.forEach((server, entry) -> {
                            sender.sendMessage(entry.toString());
                        });
                    } else {
                        Map<String, Map<String, TSProServerEntry>> groups = TransferProAPI.getServerEntries();
                        groups.forEach((group, entries) -> {
                            sender.sendMessage("==<" + group + ">==");
                            entries.forEach((server, entry) -> {
                                sender.sendMessage(entry.toString());
                            });
                        });
                    }
                    break;
                default:
                    sender.sendMessage("Usage: /tspro <setme|dump> ...");
            }
        }
        return true;
    }

}
