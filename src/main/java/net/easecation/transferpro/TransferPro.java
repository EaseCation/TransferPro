package net.easecation.transferpro;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.QueryRegenerateEvent;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.api.TransferProAPI;
import net.easecation.transferpro.command.TargetTransferCommand;
import net.easecation.transferpro.command.TransferCommand;
import net.easecation.transferpro.command.TsProCommand;
import net.easecation.transferpro.provider.DataProvider;

public class TransferPro extends PluginBase implements Listener {

    private static TransferPro instance;

    public static TransferPro getInstance() {
        return instance;
    }

    private final DataProvider provider = new DataProvider(this);

    private ServersSync serversSync;

    private Dictionary lang;

    @Override
    public void onLoad() {
        instance = this;
        this.saveDefaultConfig();
        this.saveConfig();
        String lang0 = this.getConfig().getString("language", "chs");
        this.lang = new Dictionary(lang0, getResource("tspro/lang/" + lang0 + ".ini"));
    }

    @Override
    public void onEnable() {
        this.getLogger().info(lang.translateString("tspro.enabling"));
        if (provider.connect()) {
            serversSync = new ServersSync(this);
        } else {
            this.getLogger().info(lang.translateString("tspro.enabling.fail.database"));
            this.setEnabled(false);
            return;
        }

        getServer().getCommandMap().register("TransferPro", new TsProCommand(this));
        getServer().getCommandMap().register("TransferPro", new TransferCommand(this));
        getServer().getCommandMap().register("TransferPro", new TargetTransferCommand(this));

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getLogger().info(TextFormat.GREEN + "TransferPro enabled!");
    }

    @Override
    public void onDisable() {
        provider.close();
    }

    public Dictionary getLang() {
        return lang;
    }

    public DataProvider getProvider() {
        return provider;
    }

    public ServersSync getServersSync() {
        return serversSync;
    }

    @EventHandler
    public void onQueryRegenerate(QueryRegenerateEvent event) {
        String sync = this.getConfig().getString("sync-player-count", "all");
        if (sync.equalsIgnoreCase("all")) {
            event.setPlayerCount(TransferProAPI.getTotalPlayerCount());
            event.setMaxPlayerCount(TransferProAPI.getTotalMaxPlayerCount());
        } else {
            int count = TransferProAPI.getGroupPlayerCount(sync);
            int max = TransferProAPI.getGroupMaxPlayerCount(sync);
            if (count != 0 || max != 0) {
                event.setPlayerCount(count);
                event.setMaxPlayerCount(count);
            }
        }
    }
}
