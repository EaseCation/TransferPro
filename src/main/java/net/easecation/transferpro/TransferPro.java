package net.easecation.transferpro;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.provider.DataProvider;

public class TransferPro extends PluginBase {

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
        }
        this.getLogger().info(TextFormat.GREEN + "TransferPro enabled!");
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
}
