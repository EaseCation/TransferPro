package net.easecation.transferpro.task;

import cn.nukkit.Server;
import cn.nukkit.scheduler.AsyncTask;
import net.easecation.transferpro.ServersSync;
import net.easecation.transferpro.TransferProAPI;
import net.easecation.transferpro.provider.TSProServerEntry;

import java.util.HashMap;
import java.util.Map;

public class UpdateMeAsyncTask extends AsyncTask {

    private final ServersSync serversSync;

    public UpdateMeAsyncTask(ServersSync serversSync) {
        this.serversSync = serversSync;
    }

    @Override
    public void onRun() {
        if (serversSync.getLocal() != null) {
            TSProServerEntry remote = TransferProAPI.getServerEntry(serversSync.getLocal().getGroup(), serversSync.getLocal().getServer());
            if (remote != null && !remote.equalAddress(serversSync.getLocal()))
                serversSync.getPlugin().getLogger().warning(serversSync.getPlugin().getLang().translateString(
                        "tspro.server-sync.update-warning",
                        serversSync.getLocal().getGroup(),
                        serversSync.getLocal().getServer(),
                        serversSync.getLocal().getAddress().toString(),
                        remote.getAddress().toString()
                ));
            serversSync.getPlugin().getProvider().updateServerEntry(serversSync.getLocal());
        }
    }

    @Override
    public void onCompletion(Server server) {

    }
}
