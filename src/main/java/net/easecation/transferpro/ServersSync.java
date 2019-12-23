package net.easecation.transferpro;

import net.easecation.transferpro.task.FetchAllServersAsyncTask;
import net.easecation.transferpro.task.UpdateMeAsyncTask;

import java.net.InetSocketAddress;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class ServersSync {

    private final TransferPro plugin;

    private TSProServerEntry local;
    private Map<String, Map<String, TSProServerEntry>> serverEntries = new HashMap<>();

    public ServersSync(TransferPro plugin) {
        this.plugin = plugin;
        this.plugin.getServer().getScheduler().scheduleRepeatingTask(this.plugin,
                () -> this.plugin.getServer().getScheduler().scheduleAsyncTask(this.plugin, new FetchAllServersAsyncTask(this)),
                20);
        this.plugin.getLogger().info(plugin.getLang().translateString("tspro.server-sync.enable.fetch"));
        if (this.startMyUpdate()) this.plugin.getLogger().info(plugin.getLang().translateString("tspro.server-sync.enable.update"));
    }

    public TransferPro getPlugin() {
        return plugin;
    }

    public Map<String, Map<String, TSProServerEntry>> getServerEntries() {
        return serverEntries;
    }

    public TSProServerEntry getLocal() {
        return local;
    }

    private boolean startMyUpdate() {
        if (local == null) {
            String group = plugin.getConfig().getString("my-group");
            String server = plugin.getConfig().getString("my-server");
            String address = plugin.getConfig().getString("my-address");
            if (server.isEmpty()) {
                plugin.getLogger().warning(plugin.getLang().translateString("tspro.server-sync.update.init.fail.my-server"));
                return false;
            } else if (address.isEmpty()) {
                plugin.getLogger().warning(plugin.getLang().translateString("tspro.server-sync.update.init.fail.my-address"));
                return false;
            }
            local = new TSProServerEntry(
                    group,
                    server,
                    new InetSocketAddress(address, plugin.getServer().getPort()),
                    plugin.getServer().getOnlinePlayers().size(),
                    plugin.getServer().getMaxPlayers(),
                    plugin.getServer().getTicksPerSecond(),
                    new Timestamp(System.currentTimeMillis())
            );
            this.plugin.getServer().getScheduler().scheduleRepeatingTask(this.plugin,
                    () -> {
                        this.local.update(new InetSocketAddress(address, plugin.getServer().getPort()),
                                plugin.getServer().getOnlinePlayers().size(),
                                plugin.getServer().getMaxPlayers(),
                                plugin.getServer().getTicksPerSecond(),
                                new Timestamp(System.currentTimeMillis()));
                        this.plugin.getServer().getScheduler().scheduleAsyncTask(this.plugin, new UpdateMeAsyncTask(this));
                    },
                    15);
            return true;
        }
        return false;
    }

}
