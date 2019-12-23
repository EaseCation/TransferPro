package net.easecation.transferpro;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import net.easecation.transferpro.provider.DataProvider;

public class TransferPro extends PluginBase {

    private final DataProvider provider = new DataProvider(this);

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Enabling TransferPro...");
        if (provider.connect()) {
            this.getLogger().info("Connected database!");
        } else {
            this.getLogger().info("Database connect failed!");
        }
        this.getLogger().info(TextFormat.GREEN + "TransferPro enabled!");
    }

}
