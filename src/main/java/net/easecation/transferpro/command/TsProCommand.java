package net.easecation.transferpro.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.TSProServerEntry;
import net.easecation.transferpro.TransferPro;
import net.easecation.transferpro.api.TransferProAPI;

import java.net.InetSocketAddress;
import java.util.Map;

public class TsProCommand extends PluginCommand<TransferPro> {

    public TsProCommand(TransferPro owner) {
        super("tspro", owner);
        this.setDescription("Transfer Pro");
        this.commandParameters.clear();
        this.commandParameters.put("setmeWithGroup", new CommandParameter[]{
                new CommandParameter("setme", new String[]{"setme"}),
                new CommandParameter("group", CommandParameter.ARG_TYPE_STRING),
                new CommandParameter("serverid", CommandParameter.ARG_TYPE_STRING),
                new CommandParameter("address", CommandParameter.ARG_TYPE_RAW_TEXT),
        });
        this.commandParameters.put("setmeWithoutGroup", new CommandParameter[]{
                new CommandParameter("setme", new String[]{"setme"}),
                new CommandParameter("serverid", CommandParameter.ARG_TYPE_STRING),
                new CommandParameter("address", CommandParameter.ARG_TYPE_RAW_TEXT),
        });
        this.commandParameters.put("dump", new CommandParameter[]{
                new CommandParameter("dump", new String[]{"dump"}),
                new CommandParameter("group", true)
        });
        this.commandParameters.put("exportlang", new CommandParameter[]{
                new CommandParameter("exportlang", new String[]{"exportlang"}),
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
                    if (args.length > 2) {
                        IllegalArgumentException illegalArgumentException;
                        if (args.length > 3) {
                            if ((illegalArgumentException = checkAddress(args[3])) != null) {
                                sender.sendMessage(getPlugin().getLang().translateString("tspro.server-sync.update.init.fail.my-address.reason", illegalArgumentException.getLocalizedMessage()));
                                return true;
                            }
                            getPlugin().getConfig().set("my-group", args[1]);
                            getPlugin().getConfig().set("my-server", args[2]);

                            getPlugin().getConfig().set("my-address", args[3]);
                        } else {
                            if ((illegalArgumentException = checkAddress(args[2])) != null) {
                                sender.sendMessage(getPlugin().getLang().translateString("tspro.server-sync.update.init.fail.my-address.reason", illegalArgumentException.getLocalizedMessage()));
                                return true;
                            }
                            getPlugin().getConfig().set("my-server", args[1]);
                            getPlugin().getConfig().set("my-address", args[2]);
                        }
                        getPlugin().getConfig().save();
                        sender.sendMessage(getPlugin().getLang().translateString("tspro.command.setme.set", getPlugin().getConfig().getString("my-group"), getPlugin().getConfig().getString("my-server"), getPlugin().getConfig().getString("my-address")));
                        getPlugin().getServersSync().startMyUpdate();
                    } else {
                        sender.sendMessage(getPlugin().getLang().translateString("tspro.command.usage.tspro.setme"));
                    }
                    break;
                case "dump":
                    if (args.length > 1) {
                        String group = args[1];
                        Map<String, TSProServerEntry> entries = TransferProAPI.getGroupEntries(group);
                        if (entries.isEmpty()) {
                            sender.sendMessage(getPlugin().getLang().translateString("tspro.command.empty-result"));
                        }
                        entries.forEach((server, entry) -> sender.sendMessage(entry.toString()));
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
                case "exportlang":
                    this.getPlugin().saveResource("tspro/lang/" + this.getPlugin().getLang().getLang() + ".ini", "lang.ini", true);
                    sender.sendMessage(getPlugin().getLang().translateString("tspro.command.exportlang"));
                    break;
                default:
                    sender.sendMessage(getPlugin().getLang().translateString("tspro.command.usage.tspro"));
                    break;
            }
        } else {
            sender.sendMessage(getPlugin().getLang().translateString("tspro.command.usage.tspro"));
        }
        return true;
    }

    private IllegalArgumentException checkAddress(String address) {
        try {
            new InetSocketAddress(address, Server.getInstance().getPort());
        } catch (IllegalArgumentException e) {
            return e;
        }
        return null;
    }

}
